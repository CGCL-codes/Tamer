package cactus.tools.oauth.v1_0;

import cactus.tools.httpclient.HttpClientImpl;
import cactus.tools.httpclient.HttpHelper;

/**
 * @author akwei
 */
public class OAuthHelper {

    private boolean debug;

    private HttpHelper httpHelper;

    public void setHttpHelper(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    public HttpHelper getHttpHelper() {
        if (this.httpHelper == null) {
            this.httpHelper = new HttpClientImpl(3000);
        }
        return httpHelper;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    protected void p(String s) {
        System.out.println(s);
    }
}
