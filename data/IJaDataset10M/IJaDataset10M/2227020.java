package org.xorm.util.jdoxml;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Typesafe enumeration of JDONullValue settings.
 */
public class JDONullValue {

    private String name;

    public static final JDONullValue EXCEPTION = new JDONullValue("exception");

    public static final JDONullValue DEFAULT = new JDONullValue("default");

    public static final JDONullValue NONE = new JDONullValue("none");

    private JDONullValue(String name) {
        this.name = name;
    }

    public static JDONullValue forName(String name) {
        if (EXCEPTION.name.equals(name)) return EXCEPTION;
        if (DEFAULT.name.equals(name)) return DEFAULT;
        return NONE;
    }
}
