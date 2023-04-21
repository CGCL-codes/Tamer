package com.fddtool.si.context;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple implementation of <code>ISessionContext</code> interface
 * that can only store ATTRIBUTES for a single user. Use it for unit test only.
 *
 * @author Serguei Khramtchenko
 */
public class SimpleSessionContext implements ISessionContext {

    /**
     * The map where user attributes will be stored.
     */
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, Object attribute) {
        attributes.put(name, attribute);
    }
}
