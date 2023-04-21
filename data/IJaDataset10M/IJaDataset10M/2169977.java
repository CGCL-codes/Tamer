package com.psychosally.i18n.hibernate;

import java.io.Serializable;

/**
 * @author <a href="mailto:spaddo@gmx.net">Aleksandar Vidakovic </a>
 * 
 * @since 1.0
 * 
 * @hibernate.class table="hibernate_resource"
 */
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    private String gid;

    private String bundle;

    private String locale;

    private String key;

    private String value;

    /**
	 * @hibernate.property
	 * 
	 * @return Returns the bundle.
	 */
    public String getBundle() {
        return bundle;
    }

    /**
	 * @param bundle
	 *            The bundle to set.
	 */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    /**
	 * @hibernate.id generator-class="uuid.hex"
	 * 
	 * @return Returns the gid.
	 */
    public String getGid() {
        return gid;
    }

    /**
	 * @param gid
	 *            The gid to set.
	 */
    public void setGid(String gid) {
        this.gid = gid;
    }

    /**
	 * @hibernate.property
	 * 
	 * @return Returns the key.
	 */
    public String getKey() {
        return key;
    }

    /**
	 * @param key
	 *            The key to set.
	 */
    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * @hibernate.property
	 * 
	 * @return Returns the language.
	 */
    public String getLocale() {
        return locale;
    }

    /**
	 * @param language
	 *            The language to set.
	 */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
	 * @hibernate.property
	 * 
	 * @return Returns the value.
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value
	 *            The value to set.
	 */
    public void setValue(String value) {
        this.value = value;
    }
}
