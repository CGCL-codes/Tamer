package net.webassembletool;

import java.io.Serializable;
import net.webassembletool.cookie.CustomCookieStore;
import net.webassembletool.http.SerializableBasicHttpContext;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.HttpContext;

/**
 * User context that can be used in the master application to define the user
 * id. This context will be transmitted to the provider applications.<br />
 * There is one instance of user context associated with each session.
 * 
 * @author Francois-Xavier Bonnet
 * @contributor Nicolas Richeton
 * 
 */
public class UserContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private String user;

    private CustomCookieStore cookieStore = null;

    private HttpContext httpContext = null;

    public void init() {
        if (cookieStore == null) {
            throw new IllegalArgumentException("cookieStore implementation not set");
        }
        httpContext = new SerializableBasicHttpContext();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CustomCookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("User=");
        result.append(user);
        result.append(" Cookies={\n");
        for (Cookie cookie : cookieStore.getCookies()) {
            result.append("\t");
            if (cookie.isSecure()) {
                result.append("https");
            } else {
                result.append("http");
            }
            result.append("://");
            result.append(cookie.getDomain());
            result.append(cookie.getPath());
            result.append("#");
            result.append(cookie.getName());
            result.append("=");
            result.append(cookie.getValue());
            result.append("\n");
        }
        result.append("}");
        return result.toString();
    }
}
