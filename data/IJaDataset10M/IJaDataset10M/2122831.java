package net.sf.rwp.core.service;

public class PropertyNotUniqueException extends Exception {

    public PropertyNotUniqueException(Object obj, String propertyName) {
        super("Object of class " + obj.getClass().getName() + " has a non-unique property " + propertyName);
    }
}
