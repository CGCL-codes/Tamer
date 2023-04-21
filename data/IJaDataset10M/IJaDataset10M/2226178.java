package org.apache.batik.css.parser;

/**
 * This class provides an implementation of the
 * {@link org.w3c.css.sac.AttributeCondition} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: DefaultBeginHyphenAttributeCondition.java,v 1.1 2005/11/21 09:51:37 dev Exp $
 */
public class DefaultBeginHyphenAttributeCondition extends DefaultAttributeCondition {

    /**
     * Creates a new DefaultAttributeCondition object.
     */
    public DefaultBeginHyphenAttributeCondition(String localName, String namespaceURI, boolean specified, String value) {
        super(localName, namespaceURI, specified, value);
    }

    /**
     * <b>SAC</b>: Implements {@link
     * org.w3c.css.sac.Condition#getConditionType()}.
     */
    public short getConditionType() {
        return SAC_BEGIN_HYPHEN_ATTRIBUTE_CONDITION;
    }

    /**
     * Returns a text representation of this object.
     */
    public String toString() {
        return "[" + getLocalName() + "|=\"" + getValue() + "\"]";
    }
}
