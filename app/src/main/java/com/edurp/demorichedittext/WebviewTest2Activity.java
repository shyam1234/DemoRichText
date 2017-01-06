package com.edurp.demorichedittext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Created by Admin on 06-01-2017.
 */

public class WebviewTest2Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.webView);
        final TextView contentView = (TextView) findViewById(R.id.contentView);

        /* An instance of this class will be registered as a JavaScript interface */
        class MyJavaScriptInterface
        {
            private TextView contentView;

            public MyJavaScriptInterface(TextView aContentView)
            {
                contentView = aContentView;
            }

            @SuppressWarnings("unused")

            public void processContent(String aContent)
            {
                final String content = aContent;
                contentView.post(new Runnable()
                {
                    public void run()
                    {
                        contentView.setText(content);
                    }
                });
            }
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(contentView), "INTERFACE");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
               // view.addJavascriptInterface(new IJavascriptHandler(WebviewTest2Activity.this), "Android"); //if your class extends Activity
                view.getSettings().setJavaScriptEnabled(true);
                view.addJavascriptInterface(new MyJavaScriptInterface(contentView), "HtmlViewer");
            }
        });

        webView.loadUrl("https://html-online.com/editor/");
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        public void showHTML(String html) {
            new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();
        }

    }
}