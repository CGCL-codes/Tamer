package net.infordata.android.ifw2wv;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

class MyWebChromeClient extends WebChromeClient {

    private static final String PRE = "js: ";

    @SuppressWarnings("unused")
    private final Ifw2WV ivIfw2WV;

    /**
   * @param ifw2wv
   */
    MyWebChromeClient(Ifw2WV ifw2wv) {
        ivIfw2WV = ifw2wv;
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage cm) {
        String msg = PRE + cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId();
        switch(cm.messageLevel()) {
            case DEBUG:
                Log.d(Util.LOGTAG, msg);
                break;
            case LOG:
                Log.v(Util.LOGTAG, msg);
                break;
            case TIP:
                Log.i(Util.LOGTAG, msg);
                break;
            case WARNING:
                Log.w(Util.LOGTAG, msg);
                break;
            case ERROR:
                Log.e(Util.LOGTAG, msg);
                break;
        }
        return true;
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }
}
