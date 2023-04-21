package com.groovyj.jgprog;

/**
 * The if function. Allowed to be of any type. It has three children,
 * the first of which must be boolean. The other two children must be of the
 * same type as this node. If the first child evaluates to true, the return value
 * of the first child is used as the return value of this node, otherwise the
 * second child is used.
 * <P>
 * Copyright (c) 2000 Robert Baruch. This code is released under
 * the <a href=http://www.gnu.org/copyleft/gpl.html>GNU General Public License</a> (GPL).<p>
 *
 * @author Robert Baruch (groovyjava@linuxstart.com)
 * @version 1.0
 */
public class If extends Node {

    /**
   * Creates an if node
   *
   * @since 1.0
   */
    public If(Class type) {
        super(3, type);
    }

    public String getName() {
        return "IF";
    }

    public boolean execute_boolean() {
        return children[0].execute_boolean() ? children[1].execute_boolean() : children[2].execute_boolean();
    }

    public void execute_void() {
        if (children[0].execute_boolean()) children[1].execute_void(); else children[2].execute_void();
    }

    public int execute_int() {
        return children[0].execute_boolean() ? children[1].execute_int() : children[2].execute_int();
    }

    public long execute_long() {
        return children[0].execute_boolean() ? children[1].execute_long() : children[2].execute_long();
    }

    public float execute_float() {
        return children[0].execute_boolean() ? children[1].execute_float() : children[2].execute_float();
    }

    public double execute_double() {
        return children[0].execute_boolean() ? children[1].execute_double() : children[2].execute_double();
    }

    public Class getChildType(int i) {
        if (i == 0) return Boolean.class; else return returnType;
    }
}
