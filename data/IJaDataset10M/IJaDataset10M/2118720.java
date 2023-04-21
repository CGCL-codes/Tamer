package org.xaware.server.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xaware.shared.util.XAwareException;

/**
 * Utillity class for XML based operations.
 * 
 * @author Vasu Thadaka
 * @version 1.0
 */
public class XMLUtil {

    /**
     * Replace elem with its children, return new elem
     * 
     * @param elem
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Element ShrinkHierarchy(final Element elem) {
        if (elem.isRootElement() || elem.getParent() == null) {
            final Iterator iter = elem.getChildren().iterator();
            if (!iter.hasNext()) {
                return elem;
            }
            final Element firstChild = (Element) iter.next();
            iter.remove();
            firstChild.detach();
            while (iter.hasNext()) {
                final Element celem = (Element) iter.next();
                iter.remove();
                celem.detach();
                firstChild.addContent(celem);
            }
            if (elem.getDocument() == null) {
                firstChild.detach();
                return firstChild;
            }
            elem.getDocument().setRootElement(firstChild);
            elem.detach();
            return firstChild;
        }
        final Element parent = elem.getParentElement();
        final List<Element> content = parent.getContent();
        int index = content.indexOf(elem);
        if (index >= 0) {
            final List elemChildren = elem.getChildren();
            Element firstChild = null;
            final Iterator childItr = elemChildren.iterator();
            while (childItr.hasNext()) {
                final Element child = (Element) childItr.next();
                if (firstChild == null) {
                    firstChild = child;
                }
                childItr.remove();
                child.detach();
                content.add(index, child);
                index++;
            }
            elem.detach();
            return firstChild;
        }
        final List childElements = elem.getChildren();
        if (!childElements.isEmpty()) {
            return (Element) childElements.get(childElements.size() - 1);
        }
        return elem;
    }

    /**
     * Performs validation up on result.
     * 
     * @param resultString
     *            execution resuls to validate. Return "VALID" if results are valid.
     * @return
     * @throws ClassNotFoundException
     *             if unable to load functoid class.
     * @throws XAwareException
     *             unable to instantiate functoid class.
     * @throws SecurityException
     *             unable to instantiate functoid class.
     * @throws NoSuchMethodException
     *             unable to find method to validate string.
     * @throws IllegalArgumentException
     *             unable to find method to validate string.
     * @throws IllegalAccessException
     *             unable to find method to validate string.
     * @throws InvocationTargetException
     *             unable to invoke method to validate string.
     */
    public static String performValidation(String resultString) throws ClassNotFoundException, XAwareException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class classObject = Class.forName("XAFunctoids");
        if (classObject == null) throw new XAwareException("XAFunctoids class not found.");
        Method method = classObject.getMethod("stringValidateSchema", new Class[] { String.class, String.class });
        if (method == null) throw new XAwareException("Method: stringValidateSchema not found in XAFunctoids class.");
        return (String) method.invoke(classObject, new Object[] { resultString, "" });
    }

    /**
     * Gets the String equivalent of the given Element
     */
    public static String toXML(final Element elem) {
        if (elem == null) {
            return "";
        }
        final XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        return outputter.outputString(elem);
    }
}
