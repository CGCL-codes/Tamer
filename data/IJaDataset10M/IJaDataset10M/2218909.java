package org.xaware.ide.xadev.datamodel;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.EntityRef;
import org.jdom.JDOMException;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.xaware.ide.shared.UserPrefs;
import org.xaware.ide.xadev.datamodel.JDOMText;

/**
 * This factory creates instances of the following JDOMContent subclasses:
 * JDOMElement, JDOMAttribute, JDOMComment, JDOMProcessingInstruction,
 * JDOMCData, JDOMEntityRef, JDOMText.
 * 
 * @author jweaver
 *
 */
public class JDOMContentFactory {

    private static final SAXBuilder sb = UserPrefs.getSAXBuilder();

    /**
     * If the obj argument is an appropriate type of Object then an instance of
     * the appropriate JDOMContent subclass is created.  Otherwize a null is returned.
     * 
     * @param obj An object that is one of the following set of Classes:  Element,
     * Attribute, Comment, ProcessingInstruction, CDATA, EntityRef, or String.
     * @return an instance of a JDOMContent subclass or a null
     */
    public static JDOMContent createJDOMContent(final Object obj) {
        return createJDOMContent(obj, false);
    }

    /**
     * If the obj argument is an appropriate type of Object then an instance of
     * the appropriate JDOMContent subclass is created.  Otherwize a null is returned.
     * @param obj An object that is one of the following set of Classes:  Element,
     * Attribute, Comment, ProcessingInstruction, CDATA, EntityRef, or String.
     * @param isCatalogNode
     * @return an instance of a JDOMContent subclass or a null
     */
    public static JDOMContent createJDOMContent(final Object obj, final boolean isCatalogNode) {
        JDOMContent jc = null;
        if (obj instanceof Element) {
            jc = new JDOMElement(obj, isCatalogNode);
        } else if (obj instanceof Attribute) {
            jc = new JDOMAttribute(obj, isCatalogNode);
        } else if (obj instanceof Comment) {
            jc = new JDOMComment(obj, isCatalogNode);
        } else if (obj instanceof ProcessingInstruction) {
            jc = new JDOMProcessingInstruction(obj, isCatalogNode);
        } else if (obj instanceof CDATA) {
            jc = new JDOMCData(obj, isCatalogNode);
        } else if (obj instanceof EntityRef) {
            jc = new JDOMEntityRef(obj, isCatalogNode);
        } else if (obj instanceof Text) {
            jc = new JDOMText(((Text) obj).getText(), isCatalogNode);
        } else if (obj instanceof String) {
            jc = new JDOMText(obj, isCatalogNode);
        }
        return jc;
    }

    /**
     * Creates a single JDOMContent instance from an xml string.  This
     * should be changed to return a List that may have more than one 
     * JDOMContent object in it because there is no limitation on the
     * format of the input inXML string content.  That string may be a
     * set of Elements.
     *
     * @param inXML xml string.
     *
     * @return JDOMContent instance.
     *
     * @throws JDOMException if unable to construct JDOMContent from xml
     *         string.
     */
    public static JDOMContent createInstanceFromXML(final String inXML) throws JDOMException, IOException {
        final Element rootElem = createElementFromXML(inXML);
        final Iterator itr = rootElem.getContent().iterator();
        final Object next = itr.next();
        final JDOMContent retVal = createJDOMContent(next);
        retVal.detach();
        return retVal;
    }

    /**
     * Creates a single JDOMContent instance from an xml string.  This
     * should be changed to return a List that may have more than one 
     * JDOMContent object in it because there is no limitation on the
     * format of the input inXML string content.  That string may be a
     * set of Elements.
     *
     * @param inXML xml string.
     *
     * @return JDOMContent instance.
     *
     * @throws JDOMException if unable to construct JDOMContent from xml
     *         string.
     */
    public static Element createElementFromXML(final String inXML) throws JDOMException, IOException {
        try {
            final String toParse = "<DOC_ROOT xmlns:xa=\"http://xaware.org/xas/ns1\">" + inXML + "</DOC_ROOT>";
            final Document doc = sb.build(new StringReader(toParse));
            return doc.getRootElement();
        } catch (final JDOMException jde) {
            throw jde;
        } catch (final IOException jde) {
            throw jde;
        }
    }
}
