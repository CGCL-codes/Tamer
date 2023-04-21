package org.p4pp.p3p.appel.ruleset;

import org.p4pp.util.uri.UriUtils;
import org.p4pp.p3p.util.P3pNamespaces;
import org.p4pp.p3p.appel.util.AppelNamespaces;
import org.p4pp.p3p.util.P3PLogger;
import org.jdom.Element;
import org.jdom.Attribute;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.MalformedURLException;

/**
 * Abstract class representing an enumeration of APPEL attribute expressions,
 * within an APPEL expression. 
 *
 * @author <a href="mailto:budzyn@ti.informatik.uni-kiel.de">Nikolaj Budzyn</a>
 */
class P3pAttributeExpressions {

    private static final String DATA_ELEMENT_NAME = "DATA";

    private static final String DATA_GROUP_ELEMENT_NAME = "DATA-GROUP";

    private static final String BASE_ATTRIBUTE_NAME = "base";

    private static final String REF_ATTRIBUTE_NAME = "ref";

    /**
     * The attribute expressions contained.
     */
    private List p3pAttributeExpressions;

    /**
     * Creates a new <code>P3pAttributeExpressions</code> instance for the
     * APPEL attribute expressions contained within the given (JDOM) element.
     *
     * @param P3pElementExpression an <code>P3pElementExpression</code> value containing the attribute
     * expressions
     * @exception InvalidRulesetException if <code>element</code> cannot be
     * parsed as a part of a valid APPEL ruleset.
     */
    protected P3pAttributeExpressions(P3pElementExpression p3pElementExpression) throws InvalidRulesetException {
        p3pAttributeExpressions = createListOfTheAttributeExpressions(p3pElementExpression);
    }

    /**
     * Returns a (new) <code>java.util.Iterator</code> for the
     * contained items.
     *
     * @return an <code>Iterator</code> value
     */
    Iterator iterator() {
        return p3pAttributeExpressions.iterator();
    }

    /**
     * Returns true if the attribute expressions match the given
     * <code>List</code> of (JDOM) <code>Attribute</code>s (supposed to have
     * a P3P namespace).
     * The attribute expressions are implicitely AND-ed, additional attributes
     * (in the list) are skipped.
     *
     * @param  List a <code>List<code> of (JDOM) <code>attribute<code>s supposed to
     * have correct P3P namespace.
     * Such a list is typically taken from a
     * P3P element within in P3P policy.
     * @return a <code>boolean</code> value
     * @exception InvalidRulesetException if the attribute expression are taken
     * from an invalid APPEL ruleset
     */
    boolean match(List p3pAttributes) throws InvalidRulesetException {
        Iterator iterator = iterator();
        while (iterator.hasNext()) {
            if (!((P3pAttributeExpression) iterator.next()).matches(p3pAttributes)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Takes a P3P element expression
     * and returns the list of the elements p3p attributes as
     * instances of (subclasses of) <code>P3pAttributeExpression</code>.
     *
     * @param p3pElementExpression a <code>P3pElementExpression</code> value
     * @return a <code>List</code> value
     * @exception InvalidRulesetException if an error occurs
     */
    private static List createListOfTheAttributeExpressions(P3pElementExpression p3pElementExpression) throws InvalidRulesetException {
        List attributes = p3pElementExpression.getElement().getAttributes();
        int initialCapacity = attributes.size();
        ArrayList res = new ArrayList(initialCapacity);
        for (int i = 0; i < attributes.size(); i++) {
            Attribute currentAttribute = (Attribute) attributes.get(i);
            if (P3pNamespaces.isP3pNamespaceURI(currentAttribute.getNamespaceURI()) || AppelNamespaces.hasNoNamespaceButDoesNotLookLikeAppelAttribute(currentAttribute)) {
                if (AppelNamespaces.hasNoNamespaceButDoesNotLookLikeAppelAttribute(currentAttribute)) if (P3PLogger.isInfoEnabled()) P3PLogger.info(p3pElementExpression + " contains attribute " + currentAttribute + ", which has no namespace. As it does not look like an " + "APPEL attribute, it will be treated as a P3P attribute.");
                if (p3pElementExpression.getName().equals(DATA_GROUP_ELEMENT_NAME) && currentAttribute.getName().equals(BASE_ATTRIBUTE_NAME)) {
                } else {
                    P3pAttributeExpression p3pAttributeExpression = P3pAttributeExpression.createForElementExpressionAndAttribute(p3pElementExpression, currentAttribute);
                    res.add(p3pAttributeExpression);
                }
            }
        }
        return res;
    }
}
