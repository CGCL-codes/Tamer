package com.googlecode.janrain4j.api.engage.response.poco;

import java.io.Serializable;
import com.googlecode.janrain4j.json.JSONObject;

/**
 * URL of a web page related to the <code>Contact</code>.
 * 
 * @author Marcel Overdijk
 * @since 1.0
 * @see Contact
 */
@SuppressWarnings("serial")
public class Url implements Serializable {

    public static final String TYPE_WORK = "work";

    public static final String TYPE_HOME = "home";

    public static final String TYPE_BLOG = "blog";

    public static final String TYPE_PROFILE = "profile";

    public static final String TYPE_OTHER = "other";

    protected String value = null;

    protected String type = null;

    protected boolean primary = false;

    protected Url() {
    }

    public static Url fromJSON(JSONObject json) {
        Url url = null;
        if (json != null) {
            url = new Url();
            url.setValue(json.optString("value"));
            url.setType(json.optString("type"));
            url.setPrimary(json.optBoolean("primary", false));
        }
        return url;
    }

    /**
     * Returns the URL of the web page.
     * 
     * @return The URL of the web page or <code>null</code> if not found in response.
     */
    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the type of the web page.
     * 
     * @return The type or <code>null</code> if not found in response.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns true if the type is work. 
     */
    public boolean isWork() {
        return TYPE_WORK.equalsIgnoreCase(type);
    }

    /**
     * Returns true if the type is home. 
     */
    public boolean isHome() {
        return TYPE_HOME.equalsIgnoreCase(type);
    }

    /**
     * Returns true if the type is blog. 
     */
    public boolean isBlog() {
        return TYPE_BLOG.equalsIgnoreCase(type);
    }

    /**
     * Returns true if the type is profile. 
     */
    public boolean isProfile() {
        return TYPE_PROFILE.equalsIgnoreCase(type);
    }

    /**
     * Returns true if the type is other. 
     */
    public boolean isOther() {
        return TYPE_OTHER.equalsIgnoreCase(type);
    }

    void setType(String type) {
        this.type = type;
    }

    /**
     * Returns true if the web page is the primary or preferred web page.
     */
    public boolean isPrimary() {
        return primary;
    }

    void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
