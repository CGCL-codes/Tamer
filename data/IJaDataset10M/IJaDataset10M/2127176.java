package com.googlecode.dni.type;

import com.googlecode.dni.type.nativetype.NativeLong;
import com.googlecode.dni.type.utility.LongStructure;

/**
 * <p>
 *  Encapsulates a reference to a numeric <code>long</code> value that can be
 *  passed to a native function.
 * </p>
 * <p>
 *  When passed to a native function, the pointer to a slot is passed, allowing
 *  the caller to then retrieve a value set by the native function.  Native
 *  memory is only allocated when the function call is made, and lasts only for
 *  the duration of the call.
 * </p>
 * <p>
 *  The type can be annotated on a function call with the same native modifiers
 *  (such as {@link NativeLong}).
 * </p>
 * <p>
 *  Note that this type maps to a pointer to a <em>single</em> value only, not a
 *  list of values.
 * </p>
 *
 * @see ObjectReference
 * @see LongStructure
 *
 * @author Matthew Wilson
 */
public final class LongReference {

    private long value;

    /**
     * @param value
     *            the initial value
     */
    public LongReference(final long value) {
        this.value = value;
    }

    /**
     * Obtains the value.
     *
     * @return the value
     */
    public long getValue() {
        return this.value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *            the value to set
     */
    public void setValue(final long value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "LongReference[" + this.value + "]";
    }
}
