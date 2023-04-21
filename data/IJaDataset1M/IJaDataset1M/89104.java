package com.volantis.mcs.themes;

import com.volantis.mcs.model.copy.Copyable;
import com.volantis.mcs.model.validation.SourceLocation;

/**
 * The base interface for all objects which can be stored in a StyleProperties
 * class.
 *
 * @mock.generate
 */
public interface StyleValue extends SourceLocation, Copyable {

    /**
     * Get the type of the value.
     *
     * @return The type of the value.
     */
    StyleValueType getStyleValueType();

    /**
     * Visit the underlying type.
     */
    void visit(StyleValueVisitor visitor, Object object);

    /**
     * Get a standard CSS representation of the value.
     * @return A standard CSS representation of the value.
     */
    String getStandardCSS();

    /**
     * Get the cost in characters of the standard representation of this value.
     *
     * @return The cost.
     */
    int getStandardCost();
}
