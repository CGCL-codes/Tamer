package org.viewaframework.core;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a basic implementation of the ApplicationContext
 * 
 * @author Mario Garcia
 * @since 1.0.2
 *
 */
public class DefaultApplicationContext implements ApplicationContext {

    private Map<String, Object> backingMap = new HashMap<String, Object>();

    public Object getAttribute(String arg0) {
        return this.backingMap.get(arg0);
    }

    public void removeAttribute(String arg0) {
        this.backingMap.remove(arg0);
    }

    public void setAttribute(String arg0, Object arg1) {
        this.backingMap.put(arg0, arg1);
    }
}
