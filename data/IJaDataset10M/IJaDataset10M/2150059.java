package org.apache.batik.css.engine.value;

import org.apache.batik.css.engine.CSSEngine;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

/**
 * This interface represents the objects which provide support for
 * shorthand properties.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: ShorthandManager.java,v 1.1 2005/11/21 09:51:39 dev Exp $
 */
public interface ShorthandManager {

    /**
     * Returns the name of the property handled.
     */
    String getPropertyName();

    /**
     * Sets the properties which are affected by this shorthand
     * property.
     * @param eng  The current CSSEngine.
     * @param ph   The property handler to use.
     * @param lu   The SAC lexical unit used to create the value.
     * @param imp  The property priority.
     */
    void setValues(CSSEngine eng, PropertyHandler ph, LexicalUnit lu, boolean imp) throws DOMException;

    /**
     * To handle a property value created by a ShorthandManager.
     */
    public interface PropertyHandler {

        public void property(String name, LexicalUnit value, boolean important);
    }
}
