package com.googlecode.dni.type.utility;

import com.googlecode.dni.DirectNativeInterface;
import com.googlecode.dni.library.Symbol;
import com.googlecode.dni.type.FloatReference;
import com.googlecode.dni.type.structure.Structure;
import com.googlecode.dni.type.structure.StructureFactory;

/**
 * <p>
 *  Encapsulates a reference to a numeric <code>float</code> value that can be
 *  used in many contexts.
 * </p>
 * <p>
 *  As with a normal structure, the memory is allocated when the object is
 *  created, and must be explicitly freed when no longer required.  The explicit
 *  memory management is what differentiates this type from
 *  {@link FloatReference}, and that allows it to be used in
 *  {@linkplain Structure structures} or by {@linkplain Symbol symbols}.
 * </p>
 * <p>
 *  Note that this type maps to a pointer to a <em>single</em> value only, not a
 *  list of values.
 * </p>
 *
 * @see FloatReference
 *
 * @author Matthew Wilson
 */
public interface FloatStructure extends Structure {

    /**
     * Factory instance for creating instances of {@link FloatStructure}.
     */
    public static final StructureFactory<FloatStructure> FACTORY = DirectNativeInterface.getStructureFactory(FloatStructure.class);

    /**
     * Obtains the value.
     *
     * @return the value
     */
    float getValue();

    /**
     * Sets the value.
     *
     * @param value
     *            the value to set
     */
    void setValue(float value);
}
