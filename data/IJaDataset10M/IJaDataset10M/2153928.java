package org.restlet.data;

import org.restlet.util.Engine;

/**
 * Cookie setting provided by a server.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public final class CookieSetting extends Cookie {

    /** The user's comment. */
    private String comment;

    /**
     * The maximum age in seconds. Use 0 to discard an existing cookie.
     */
    private int maxAge;

    /** Indicates if cookie should only be transmitted by secure means. */
    private boolean secure;

    /**
     * Default constructor.
     */
    public CookieSetting() {
        this(0, null, null, null, null);
    }

    /**
     * Preferred constructor.
     * 
     * @param version
     *            The cookie's version.
     * @param name
     *            The cookie's name.
     * @param value
     *            The cookie's value.
     */
    public CookieSetting(int version, String name, String value) {
        this(version, name, value, null, null);
    }

    /**
     * Preferred constructor.
     * 
     * @param version
     *            The cookie's version.
     * @param name
     *            The cookie's name.
     * @param value
     *            The cookie's value.
     * @param path
     *            The cookie's path.
     * @param domain
     *            The cookie's domain name.
     */
    public CookieSetting(int version, String name, String value, String path, String domain) {
        super(version, name, value, path, domain);
        this.comment = null;
        this.maxAge = -1;
        this.secure = false;
    }

    /**
     * Preferred constructor.
     * 
     * @param name
     *            The cookie's name.
     * @param value
     *            The cookie's value.
     */
    public CookieSetting(String name, String value) {
        this(0, name, value, null, null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        boolean result = (obj == this);
        if (!result) {
            if (super.equals(obj)) {
                if ((obj instanceof CookieSetting) && obj != null) {
                    CookieSetting that = (CookieSetting) obj;
                    result = (this.maxAge == that.maxAge) && (this.secure == that.secure);
                    if (result) {
                        if (!(this.comment == null)) {
                            result = (this.comment.equals(that.comment));
                        } else {
                            result = (that.comment == null);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the comment for the user.
     * 
     * @return The comment for the user.
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Returns the description of this REST element.
     * 
     * @return The description of this REST element.
     */
    public String getDescription() {
        return "Cookie setting";
    }

    /**
     * Returns the maximum age in seconds.<br/> Use 0 to immediately discard an
     * existing cookie.<br/> Use -1 to discard the cookie at the end of the
     * session (default).
     * 
     * @return The maximum age in seconds.
     */
    public int getMaxAge() {
        return this.maxAge;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Engine.hashCode(super.hashCode(), getComment(), getMaxAge(), isSecure());
    }

    /**
     * Indicates if cookie should only be transmitted by secure means.
     * 
     * @return True if cookie should only be transmitted by secure means.
     */
    public boolean isSecure() {
        return this.secure;
    }

    /**
     * Sets the comment for the user.
     * 
     * @param comment
     *            The comment for the user.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets the maximum age in seconds.<br/> Use 0 to immediately discard an
     * existing cookie.<br/> Use -1 to discard the cookie at the end of the
     * session (default).
     * 
     * @param maxAge
     *            The maximum age in seconds.
     */
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    /**
     * Indicates if cookie should only be transmitted by secure means.
     * 
     * @param secure
     *            True if cookie should only be transmitted by secure means.
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }
}
