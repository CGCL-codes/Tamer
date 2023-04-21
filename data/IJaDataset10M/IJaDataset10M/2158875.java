package org.apache.batik.css.parser;

import org.w3c.css.sac.AttributeCondition;

/**
 * This class provides an abstract implementation of the {@link
 * org.w3c.css.sac.AttributeCondition} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: AbstractAttributeCondition.java,v 1.1 2005/11/21 09:51:37 dev Exp $
 */
public abstract class AbstractAttributeCondition implements AttributeCondition {

    /**
     * The attribute value.
     */
    protected String value;

    /**
     * Creates a new AbstractAttributeCondition object.
     */
    protected AbstractAttributeCondition(String value) {
        this.value = value;
    }

    /**
     * <b>SAC</b>: Implements {@link AttributeCondition#getValue()}.
     */
    public String getValue() {
        return value;
    }
}
