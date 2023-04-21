package com.bluemarsh.jswat.core.breakpoint;

import com.sun.jdi.Field;

/**
 * A FieldBreakpoint applies only to a particular field of a class. This
 * is typically combined with other types of a breakpoints, such as
 * WatchBreakpoint.
 *
 * @author  Nathan Fiedler
 */
public interface FieldBreakpoint extends Breakpoint {

    /** Name of 'field' property. */
    String PROP_FIELD = "field";

    /**
     * Returns the field this breakpoint is watching.
     *
     * @return  field.
     */
    Field getField();

    /**
     * Sets the field this breakpoint should watch.
     *
     * @param  field  the field to watch.
     */
    void setField(Field field);
}
