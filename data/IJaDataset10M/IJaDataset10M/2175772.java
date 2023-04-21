package com.volantis.shared.net.http.cookies;

import com.volantis.shared.net.http.HTTPMessageEntityIdentity;
import com.volantis.shared.net.http.SimpleHTTPMessageEntityIdentity;

/**
 * Encapsulate the identity of a Cookie.
 */
public class CookieIdentity extends SimpleHTTPMessageEntityIdentity {

    /**
     * The domain of the Cookie identified by this identity.
     */
    private String domain;

    /**
     * The path of the Cookie identified by this identity.
     */
    private String path;

    /**
     * Create a new CookieIdentity with the specified name, domain and path.
     * @param name The cookie name.
     * @param domain The cookie domain - can be null.
     * @param path The cookie path - can be null.
     */
    public CookieIdentity(String name, String domain, String path) {
        super(name, Cookie.class);
        this.domain = domain;
        this.path = path;
    }

    public boolean identityEquals(HTTPMessageEntityIdentity identity) {
        boolean equals = super.identityEquals(identity);
        if (equals) {
            CookieIdentity cookieIdentity = (CookieIdentity) identity;
            equals = getDomain() == null ? cookieIdentity.getDomain() == null : getDomain().equals(cookieIdentity.getDomain());
            if (equals) {
                equals = getPath() == null ? cookieIdentity.getPath() == null : getPath().equals(cookieIdentity.getPath());
            }
        }
        return equals;
    }

    public int hashCode() {
        return super.hashCode() + (getDomain() == null ? 0 : getDomain().hashCode()) + (getPath() == null ? 0 : getPath().hashCode());
    }

    /**
     * Get the domain of the Cookie identified by this identity.
     * @return The domain.
     */
    protected String getDomain() {
        return domain;
    }

    /**
     * Get the path of the Cookie identified by this identity.
     * @return The path.
     */
    protected String getPath() {
        return path;
    }
}
