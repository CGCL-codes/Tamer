package com.sun.jini.lookup.entry;

import java.awt.Image;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * An implementation of ServiceType that uses ResourceBundles. If the value of
 * the public field type contains at least one dot ('.'), then the value of the
 * field is used as the name of the ResourceBundle. Otherwise, the name of the
 * ResourceBundle is obtained by prefixing "net.jini.lookup.entry.servicetype."
 * to the value of the public field. The default locale is used.
 * 
 * @author Sun Microsystems, Inc.
 * 
 */
public class BasicServiceType extends net.jini.lookup.entry.ServiceType {

    private static final long serialVersionUID = -9077088179092831351L;

    /**
	 * The type of service.
	 * 
	 * @serial
	 */
    public String type;

    private transient ResourceBundle bundle;

    private transient boolean inited = false;

    /** Simple constructor, leaves type field set to null. */
    public BasicServiceType() {
    }

    /**
	 *Simple constructor, sets type field to parameter value.
	 * 
	 * @param type
	 *            type of service
	 */
    public BasicServiceType(String type) {
        this.type = type;
    }

    /** Returns the resource named "icon.<var>int</var>", else null. */
    public Image getIcon(int iconKind) {
        init();
        if (bundle != null) {
            try {
                return (Image) bundle.getObject("icon." + iconKind);
            } catch (MissingResourceException e) {
            }
        }
        return null;
    }

    /**
	 * Returns the resource named "name", else the type field stripped of any
	 * package prefix (i.e., any characters up to and including the last dot
	 * ('.').
	 */
    public String getDisplayName() {
        init();
        if (bundle != null) {
            try {
                return bundle.getString("name");
            } catch (MissingResourceException e) {
            }
        }
        return type.substring(type.lastIndexOf('.') + 1);
    }

    /** Returns the resource named "desc", else null. */
    public String getShortDescription() {
        init();
        if (bundle != null) {
            try {
                return bundle.getString("desc");
            } catch (MissingResourceException e) {
            }
        }
        return null;
    }

    private void init() {
        if (!inited) {
            String name = type;
            if (name.indexOf('.') < 0) name = "net.jini.lookup.entry.servicetype." + name;
            try {
                bundle = ResourceBundle.getBundle(name);
            } catch (MissingResourceException e) {
            }
            inited = true;
        }
    }
}
