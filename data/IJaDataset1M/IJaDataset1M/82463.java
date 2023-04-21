package net.saim.game.client.oauth;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Backup implementation of TokenStoreImpl storing tokens in cookies, for
 * browsers where localStorage is not supported.
 * 
 * @author jasonhall@google.com (Jason Hall)
 */
class CookieStoreImpl extends TokenStoreImpl {

    private static final String COOKIE_PREFIX = "gwt-oauth2-";

    @SuppressWarnings("deprecation")
    @Override
    public native void set(String key, String value);

    @Override
    public native String get(String key);

    @Override
    public native void clear();

    private static JavaScriptObject cachedCookies = null;

    private static String rawCookies;

    private static native void loadCookies();

    private static JavaScriptObject ensureCookies() {
        if (cachedCookies == null || needsRefresh()) {
            loadCookies();
        }
        return cachedCookies;
    }

    private static native boolean needsRefresh();
}
