package org.nodevision.portal.api.impl;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.portlet.PortalContext;
import org.nodevision.portal.utils.Constants;

public class NVPortalContext implements PortalContext {

    private Hashtable props = Constants.getBasicProperties();

    private static NVPortalContext pc;

    private NVPortalContext() {
    }

    public static NVPortalContext getPortalContext() {
        if (null == pc) {
            NVPortalContext.pc = new NVPortalContext();
        }
        return NVPortalContext.pc;
    }

    public String getPortalInfo() {
        StringBuffer portalInfo = new StringBuffer();
        portalInfo.append(props.get("name") + "/" + props.get("version"));
        return portalInfo.toString();
    }

    public synchronized String getProperty(final String prop) throws IllegalArgumentException {
        if (null == prop) {
            throw new IllegalArgumentException("Propertyname is null.");
        }
        return props.get(prop).toString();
    }

    public Enumeration getPropertyNames() {
        return Collections.enumeration(props.keySet());
    }

    public Enumeration getSupportedPortletModes() {
        return Constants.getSupportedPortletModes();
    }

    public Enumeration getSupportedWindowStates() {
        return Constants.getSupportedWindowStates();
    }

    public synchronized void addProperty(final String key, final String value) {
        if (props.containsKey(key)) {
            final String[] values = (String[]) props.get(key);
            final String[] newValues = new String[values.length + 1];
            System.arraycopy(values, 0, newValues, 0, values.length);
            newValues[values.length + 1] = value;
        } else {
            final String[] values = new String[] { value };
            props.put(key, values);
        }
    }

    public synchronized void setProperty(final String key, final String value) {
        if (props.containsKey(key)) {
            props.remove(key);
        }
        final String[] values = new String[1];
        values[0] = value;
        props.put(key, values);
    }

    public Hashtable getProps() {
        return props;
    }
}
