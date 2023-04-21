package com.objectwave.transactionalSupport;

/**
* We may want to implement security at a field access level.  This interface
* provides a mechanism by which security could be implemented.
*/
public interface AccessSecurityIF {

    void checkReadAccess(Object obj, java.lang.reflect.Field f) throws SecurityException;

    void checkWriteAccess(Object obj, java.lang.reflect.Field f) throws SecurityException;
}
