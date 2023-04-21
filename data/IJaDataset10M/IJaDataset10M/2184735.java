package org.mitre.ovalutils.merge;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.QNameHelper;
import org.apache.xmlbeans.impl.common.XmlWhitespace;

/**
 * Class used to hold XmlBeans comparison methods.
 *
 * NOTE:  This was copied over from the webservices project as-is. -mhansbury
 *
 * TODO: add in better difference reporting. It would be nice to report on what the differences are and where they were found.
 */
public class XBeanCompare {

    protected static Logger logger = Logger.getLogger(XBeanCompare.class.getPackage().getName());

    /**
     * Compares 2 XmlObjects and returns true if they are equal. Allows for an input list
     * of attributes to skip value checks and a list of attributes to skip existence checks.
     * The comparison of XmlObjects is done without regard to child element ordering.
     *
     * NOTE: I have attempted to make the code err on side of reporting elements to be different.
     *       2 elements should never be considered equal if they are not, but may be considered
     *       not equal when they actually are. This decision was made because this code was written
     *       with the primary goal of protecting the contents of a repository.
     *
     * This comparison is done as follows:
     * 1- compare the elements names. (see: XBeanCompare.compareNamesAndAttributes)
     * 2- compare the elements attributes. (see: XBeanCompare.compareNamesAndAttributes)
     * 3- check that both elements have the same number of children. (uses xpath "./child::*" then gets the length of the array of children)
     * 4- compare child elements
     *    - loop through all children of elm1
     *    - check that elm2 has a child element that matches the current elm1 child where the elm2 child has not already been used. (recursive)
     *    - each time a match is made store the index of the elm2 child to make sure it is not used again. (this protects use against two elements with the same attributes and values.)
     *
     * @param object1 the first XmlObject
     * @param object2 the sceond XmlObject
     * @param ignoreAttrValues A map of attribute names to not compare values on.
     * @param ignoreAttrExistence A map of attribute local names to skip when checking existenc of attributes
     * @return true if the two XmlObjects are equal.
     */
    public static boolean equals(XmlObject object1, XmlObject object2, Map ignoreAttrValues, Map ignoreAttrExistence) {
        boolean match = true;
        XmlCursor cur1 = object1.newCursor();
        XmlCursor cur2 = object2.newCursor();
        if (!compareNamesAndAttributes(cur1, cur2, ignoreAttrValues, ignoreAttrExistence)) {
            match = false;
        } else {
            boolean hasChildren1 = cur1.toFirstChild();
            boolean hasChildren2 = cur2.toFirstChild();
            if (hasChildren1 != hasChildren2) {
                if (hasChildren1) {
                    cur1.toParent();
                }
                if (hasChildren2) {
                    cur2.toParent();
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Topology differs: one element has " + "children where the other does not (" + QNameHelper.pretty(cur1.getName()) + ", " + QNameHelper.pretty(cur2.getName()) + ").");
                }
                match = false;
            } else if (!hasChildren1 && !hasChildren2) {
                if (!wsCollapseEqual(cur1.getTextValue(), cur2.getTextValue())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Value '" + cur1.getTextValue() + "' differs from value '" + cur2.getTextValue() + "'.");
                    }
                    match = false;
                }
            } else {
                XmlObject[] obj1Children = object1.selectPath("./child::*");
                int obj1ChildCount = obj1Children.length;
                XmlObject[] obj2Children = object2.selectPath("./child::*");
                int obj2ChildCount = obj2Children.length;
                if (obj1ChildCount != obj2ChildCount) {
                    if (hasChildren1) {
                        cur1.toParent();
                    }
                    if (hasChildren2) {
                        cur2.toParent();
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("Topology differs: one element has " + obj1ChildCount + " child element(s) where the other has " + obj2ChildCount + " child element(s) (" + QNameHelper.pretty(cur1.getName()) + ", " + QNameHelper.pretty(cur2.getName()) + ").");
                    }
                    match = false;
                } else {
                    Map matchedElmChildIndexes = new HashMap();
                    boolean childMatch = false;
                    do {
                        int elm2ChildIndex = 0;
                        do {
                            if (!matchedElmChildIndexes.containsKey(new Integer(elm2ChildIndex))) {
                                childMatch = equals(cur1.getObject(), cur2.getObject(), ignoreAttrValues, ignoreAttrExistence);
                                if (childMatch) {
                                    matchedElmChildIndexes.put(new Integer(elm2ChildIndex), new Integer(elm2ChildIndex));
                                    break;
                                }
                            }
                            elm2ChildIndex++;
                        } while (cur2.toNextSibling());
                        cur2.toParent();
                        cur2.toFirstChild();
                        if (!childMatch) {
                            match = false;
                            break;
                        }
                    } while (cur1.toNextSibling());
                    if (childMatch) {
                        match = true;
                    } else {
                        match = false;
                    }
                    cur1.toParent();
                    cur2.toParent();
                }
            }
        }
        cur1.dispose();
        cur2.dispose();
        return match;
    }

    /**
     * Return true if the elements pointed to by the input XmlCursors are equivalent.
     * Accepts as input two list of attributes to ignore durring comparison.
     *
     * Attributes are compared as follows:
     * 1- loop through all attributes in elm1 and check elm2
     *    - if the attr exists in elm2 compare values.
     *    - if the attr does not exist use the schema type system to determine the default value for the elm2 attr if it has one.
     * 2- loop through all attributes in elm2 and check elm1
     *    - if the attr does not exist use the schema type system to determine the default value for the elm1 attr
     *
     *
     * @param cur1 The first XmlCursor.
     * @param cur2 The second XmlCursor.
     * @param ignoreAttrValues A map of attribute local names to not compare values on.
     * @param ignoreAttrExistence A map of attribute local names to skip when checking existence of attributes
     * @return
     */
    private static boolean compareNamesAndAttributes(XmlCursor cur1, XmlCursor cur2, Map ignoreAttrValues, Map ignoreAttrExistence) {
        if (!cur1.getName().equals(cur2.getName())) {
            if (logger.isDebugEnabled()) {
                logger.debug("Element names '" + QNameHelper.pretty(cur1.getName()) + "' and '" + QNameHelper.pretty(cur2.getName()) + "' do not match.");
            }
            return false;
        }
        String elemName = QNameHelper.pretty(cur1.getName());
        if (cur1.toFirstAttribute()) {
            do {
                String localName1 = cur1.getName().getLocalPart();
                if (ignoreAttrExistence.containsKey(localName1)) {
                    logger.debug("Ignoring attribute exitence check for: " + localName1);
                } else {
                    String text1 = cur1.getTextValue();
                    String text2 = cur2.getAttributeText(cur1.getName());
                    if (text2 == null) {
                        SchemaProperty attrProperty = cur2.getObject().schemaType().getAttributeProperty(cur1.getName());
                        if (attrProperty.hasDefault() == SchemaProperty.CONSISTENTLY) {
                            text2 = attrProperty.getDefaultText();
                        } else {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Attribute '" + QNameHelper.pretty(cur1.getName()) + "' " + " of element '" + elemName + "' not present.");
                            }
                            return false;
                        }
                    }
                    if (ignoreAttrValues.containsKey(localName1)) {
                        logger.debug("Ignoring attribute values for: " + localName1);
                    } else {
                        if (!wsCollapseEqual(text1, text2)) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Attribute values for '" + QNameHelper.pretty(cur1.getName()) + "' " + " of element '" + elemName + "' don't match.");
                            }
                            return false;
                        }
                    }
                }
            } while (cur1.toNextAttribute());
            cur1.toParent();
        }
        if (cur2.toFirstAttribute()) {
            do {
                String localName2 = cur2.getName().getLocalPart();
                String text2 = cur2.getTextValue();
                String text1 = cur1.getAttributeText(cur2.getName());
                if (text1 == null) {
                    SchemaProperty attrProperty = cur1.getObject().schemaType().getAttributeProperty(cur2.getName());
                    if (attrProperty.hasDefault() == SchemaProperty.CONSISTENTLY) {
                        text1 = attrProperty.getDefaultText();
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Attribute '" + QNameHelper.pretty(cur1.getName()) + "' " + " of element '" + elemName + "' not present.");
                        }
                        return false;
                    }
                }
                if (ignoreAttrValues.containsKey(localName2)) {
                    logger.debug("Ignoring attribute values for: " + localName2);
                } else {
                    if (!wsCollapseEqual(text1, text2)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Attribute values for '" + QNameHelper.pretty(cur2.getName()) + "' " + " of element '" + elemName + "' don't match.");
                        }
                        return false;
                    }
                }
            } while (cur2.toNextAttribute());
            cur2.toParent();
        }
        return true;
    }

    /**
     * Return true if the 2 strings are equal after xml collapsing whitespace.
     */
    private static boolean wsCollapseEqual(String s1, String s2) {
        if (logger.isTraceEnabled()) {
            logger.trace("Comparing: " + s1 + " to " + s2);
        }
        String s1c = XmlWhitespace.collapse(s1);
        String s2c = XmlWhitespace.collapse(s2);
        return (s1c.equals(s2c));
    }
}
