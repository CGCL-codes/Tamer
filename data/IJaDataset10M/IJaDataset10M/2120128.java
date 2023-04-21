package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;

/**
 * An interface to be implemented by any Fig that contains a FigEnumLiterals.
 * 
 * @author Tom Morris
 * 
 * TODO: I've just extended the existing structure for now, but it all really
 * needs to be refactored into something more general so that we can easily deal
 * with arbitrary types and numbers of optionally displayed containers. - 
 * tfm - 20060315
 */
public interface EnumLiteralsCompartmentContainer {

    /**
     * Determine if the EnumerationLiterals compartment is visible.
     * @return true if the EnumerationLiterals compartment is visible.
     */
    boolean isEnumLiteralsVisible();

    /**
     * Set the visibility of the EnumerationLiterals compartment.
     * @param visible the new visibility status.
     */
    void setEnumLiteralsVisible(boolean visible);

    /**
     * @return The bounds of the EnumerationLiterals compartment
     */
    Rectangle getEnumLiteralsBounds();
}
