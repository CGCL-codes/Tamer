package com.bluemarsh.jswat.panel;

import com.sun.jdi.StringReference;
import com.sun.jdi.Value;
import com.bluemarsh.jswat.util.Strings;

/**
 * A <code>StringVariable</code> represents a string debugger variable.
 *
 * @author  David Lum
 */
class StringVariable extends Variable {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /** Value of 'this' variable. */
    protected StringReference value;

    /**
     * Creates a new <code>StringVariable</code> from a name, type, and
     * value.
     *
     * @param  name  the name of the variable.
     * @param  type  the type of the variable.
     * @param  val   the value of the variable.
     */
    protected StringVariable(String name, String type, StringReference val) {
        super(name, type);
        value = val;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param  obj  the reference object with which to compare.
     * @return  true if this object is the same as the obj argument;
     *          false otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof StringVariable) {
            String ours = value.value();
            String theirs = ((StringVariable) obj).value.value();
            return ours.equals(theirs);
        }
        return false;
    }

    /**
     * Retrieve the value this variable represents.
     *
     * @return  Value.
     */
    public Value getValue() {
        return value;
    }

    /**
     * Refreshes the variable. This particular implementation does nothing.
     */
    public void refresh() {
    }

    /**
     * Returns a string description of 'this' variable.
     *
     * @return  a description of 'this' variable.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(128);
        buf.append(varName);
        buf.append(": \"");
        if (value == null) {
            buf.append("null");
        } else {
            buf.append(Strings.cleanForPrinting(value.value(), 100));
        }
        buf.append("\"");
        return buf.toString();
    }
}
