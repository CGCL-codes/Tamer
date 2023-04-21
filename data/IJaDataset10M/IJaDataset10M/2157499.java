package vdoclet.ejb;

import vdoclet.docinfo.*;
import java.util.*;

/**
 * Represents a class that can be looked-up in JNDI.
 */
public class RegisteredClassInfo extends ClassInfo {

    private final String _jndiName;

    public RegisteredClassInfo(String className, String jndiName) {
        super(className);
        if (jndiName == null) {
            throw new IllegalArgumentException("jndiName cannot be null");
        }
        _jndiName = jndiName;
    }

    /**
     * Construct a RegisteredClassInfo, where the jndiName defaults to the
     * className.
     */
    public RegisteredClassInfo(String name) {
        this(name, name);
    }

    /**
     * Get the JNDI name.
     */
    public String getJndiName() {
        return _jndiName;
    }
}
