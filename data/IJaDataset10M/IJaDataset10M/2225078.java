package com.c2b2.open286.container;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.portlet.PortletContext;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpSession;

/**
 * This class implements the JSR 286 Portlet Session interface
 * <p>
 * Open 286 Portlet Container
 * Copyright 2007 C2B2 Consulting Limited. All rights reserved.
 * </p>
 */
public class Open286Session implements PortletSession {

    public Open286Session(PortletContext context, HttpSession session, String windowID) {
        myContext = context;
        mySession = session;
        myAttributePrefix = "javax.portlet.p." + windowID + "?";
    }

    public Object getAttribute(String string) {
        return mySession.getAttribute(myAttributePrefix + string);
    }

    public Object getAttribute(String string, int i) {
        Object result = null;
        if (i == this.PORTLET_SCOPE) {
            result = getAttribute(string);
        } else {
            result = mySession.getAttribute(string);
        }
        return result;
    }

    public Enumeration getAttributeNames() {
        return getAttributeNames(PORTLET_SCOPE);
    }

    public Enumeration getAttributeNames(int i) {
        Enumeration<String> names = mySession.getAttributeNames();
        Set<String> scopedNames = new HashSet<String>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (i == PORTLET_SCOPE && name.startsWith(myAttributePrefix)) {
                name = name.substring(myAttributePrefix.length());
                scopedNames.add(name);
            } else if (i == APPLICATION_SCOPE && !name.startsWith(myAttributePrefix)) {
                scopedNames.add(name);
            }
        }
        return Collections.enumeration(scopedNames);
    }

    public long getCreationTime() {
        return mySession.getCreationTime();
    }

    public String getId() {
        return mySession.getId();
    }

    public long getLastAccessedTime() {
        return mySession.getLastAccessedTime();
    }

    public int getMaxInactiveInterval() {
        return mySession.getMaxInactiveInterval();
    }

    public void invalidate() {
        mySession.invalidate();
    }

    public boolean isNew() {
        return mySession.isNew();
    }

    public void removeAttribute(String string) {
        mySession.removeAttribute(myAttributePrefix + string);
    }

    public void removeAttribute(String string, int i) {
        if (i == this.PORTLET_SCOPE) {
            removeAttribute(string);
        } else {
            mySession.removeAttribute(string);
        }
    }

    public void setAttribute(String string, Object object) {
        mySession.setAttribute(myAttributePrefix + string, object);
    }

    public void setAttribute(String string, Object object, int i) {
        if (i == this.PORTLET_SCOPE) {
            setAttribute(string, object);
        } else {
            mySession.setAttribute(string, object);
        }
    }

    public void setMaxInactiveInterval(int i) {
        mySession.setMaxInactiveInterval(i);
    }

    public PortletContext getPortletContext() {
        return myContext;
    }

    public Map getAttributeMap() {
        return getAttributeMap(this.PORTLET_SCOPE);
    }

    public Map getAttributeMap(int i) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        Enumeration<String> sessionAttributes = mySession.getAttributeNames();
        while (sessionAttributes.hasMoreElements()) {
            String name = sessionAttributes.nextElement();
            if (i == APPLICATION_SCOPE && !name.startsWith(myAttributePrefix)) {
                result.put(name, mySession.getAttribute(name));
            } else if (i == PORTLET_SCOPE && name.startsWith(myAttributePrefix)) {
                result.put(name.substring(myAttributePrefix.length()), mySession.getAttribute(name));
            }
        }
        return Collections.unmodifiableMap(result);
    }

    private String myAttributePrefix;

    private PortletContext myContext;

    private HttpSession mySession;
}
