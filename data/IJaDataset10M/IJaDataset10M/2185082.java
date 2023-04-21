package org.mitre.midiki.compiler.parser;

import java.util.*;
import java.io.Serializable;

/**
 * An <code>MKType</code> is an object representing
 * symbol type information.
 *
 * @author <a href="mailto:cburke@mitre.org">Carl Burke</a>
 * @version 1.0
 * @since 1.0
 */
public class MKType implements Serializable {

    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String n) {
        _name = n;
    }

    public MKType() {
    }

    public MKType(String n) {
        this();
        setName(n);
    }

    public String toString() {
        return "type[" + getName() + "]";
    }

    public boolean equals(Object obj) {
        return ((MKType) obj).getName().equals(getName());
    }

    public static MKType typeType;

    static {
        typeType = new MKType("$type");
    }
}
