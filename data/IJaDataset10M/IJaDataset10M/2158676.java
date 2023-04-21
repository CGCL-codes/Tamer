package org.antdepo.common;

import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Abstract class for adding validation around a Jar {@link Manifest} object.
 * <p/>
 * ControlTier Software Inc.
 * User: alexh
 * Date: Aug 12, 2005
 * Time: 3:41:07 PM
 */
public abstract class ArchiveManifest extends Manifest {

    public static String[] REQUIRED_ATTRIBUTES = {};

    public ArchiveManifest(final Manifest manifest) {
        super(manifest);
    }

    /**
     * Checks each required attribute and returns false if one is found missing.
     *
     * @return true if valid, false if attribute(s) is missing
     */
    public boolean validate() {
        for (int i = 0; i < REQUIRED_ATTRIBUTES.length; i++) {
            if (hasAttribute(REQUIRED_ATTRIBUTES[i])) {
            } else {
                System.err.println("attribute was not found in manifest: " + REQUIRED_ATTRIBUTES[i]);
                return false;
            }
        }
        return true;
    }

    private boolean hasAttribute(final String attr) {
        return getMainAttributes().getValue(attr) != null;
    }

    public void printMainAttributes() {
        System.out.println("enumerating manifest entries");
        for (Iterator i = getMainAttributes().keySet().iterator(); i.hasNext(); ) {
            Attributes.Name attrName = (Attributes.Name) i.next();
            System.out.println("key: " + attrName);
            System.out.println("value: " + getMainAttributes().getValue(attrName));
        }
    }

    protected boolean getAttrValAsBoolean(final String attrName) {
        final String value = getMainAttributes().getValue(attrName);
        if (null == value || value.equalsIgnoreCase("false")) {
            return false;
        } else {
            return value.equalsIgnoreCase("true");
        }
    }
}
