package com.groovyj.jgprog;

public class GreaterThan extends Node {

    Class childType;

    public GreaterThan(Class type) {
        super(2, Boolean.class);
        childType = type;
    }

    public String getName() {
        return ">";
    }

    public boolean execute_boolean() {
        if (childType == Integer.class) return children[0].execute_int() > children[1].execute_int(); else if (childType == Long.class) return children[0].execute_long() > children[1].execute_long(); else if (childType == Float.class) return children[0].execute_float() > children[1].execute_float(); else if (childType == Double.class) return children[0].execute_double() > children[1].execute_double();
        throw new UnsupportedOperationException("GreaterThan may only take int, long, float, or double");
    }

    public Class getChildType(int parm1) {
        return childType;
    }
}
