package net.saim.game.client.oauth;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.storage.client.Storage;

/**
 * Real implementation of {@link Auth}, used in real GWT applications.
 * 
 * @author jasonhall@google.com (Jason Hall)
 */
class AuthImpl extends Auth {

    static final AuthImpl INSTANCE = new AuthImpl();

    private Window window;

    AuthImpl() {
        super(getTokenStore(), new RealClock(), new RealUrlCodex(), Scheduler.get(), GWT.getModuleBaseURL() + "oauthWindow.html");
        register();
    }

    private static TokenStoreImpl getTokenStore() {
        return Storage.isLocalStorageSupported() ? new TokenStoreImpl() : new CookieStoreImpl();
    }

    /**
   * Register a global function to receive auth responses from the popup window.
   */
    private native void register();

    /**
   * Get the OAuth 2.0 token for which this application may not have already
   * been granted access, by displaying a popup to the user.
   */
    @Override
    void doLogin(String authUrl, Callback<String, Throwable> callback) {
        if (window != null && window.isOpen()) {
            callback.onFailure(new IllegalStateException("Authentication in progress"));
        } else {
            window = openWindow(authUrl, height, width);
            if (window == null) {
                callback.onFailure(new RuntimeException("The authentication popup window appears to have been blocked"));
            }
        }
    }

    @Override
    void finish(String hash) {
        if (window != null && window.isOpen()) {
            window.close();
        }
        super.finish(hash);
    }

    private static native Window openWindow(String url, int height, int width);

    static final class Window extends JavaScriptObject {

        @SuppressWarnings("unused")
        protected Window() {
        }

        native boolean isOpen();

        native void close();
    }

    /** Real GWT implementation of Clock. */
    private static class RealClock implements Clock {

        public double now() {
            return Duration.currentTimeMillis();
        }
    }

    /** Real GWT implementation of UrlCodex. */
    private static class RealUrlCodex implements UrlCodex {

        public native String encode(String url);

        public native String decode(String url);
    }
}
