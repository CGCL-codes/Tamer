package org.xaware.ide.xadev.wizard;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.xaware.ide.xadev.datamodel.TemplateXMLInfoData;
import org.xaware.ide.xadev.gui.actions.WSDLCreateBaseAction;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This Class is used to template finisher
 * 
 * @author Govind
 * @version 1.0
 */
public class TemplateFinisher extends WizardFinisher {

    /** Holds instance of XA_Designer_Plugin translator */
    public static final Translator t = Translator.getInstance();

    /** Holds Reference to Namespace */
    public static final Namespace ns = XAwareConstants.xaNamespace;

    private static final XAwareLogger lf = XAwareLogger.getXAwareLogger(WSDLCreateBaseAction.class.getName());

    /** BizDocument document */
    private Document theDoc;

    /**
     * Creates a new TemplateFinisher object(Constructor).
     * 
     * @param inDoc
     *            BizDocument
     */
    public TemplateFinisher(final Document inDoc) {
        super();
        theDoc = inDoc;
    }

    /**
     * Method from WizardFinisher. Returns the constructed object.
     * 
     * @param input
     *            Vector instance representing input data.
     * @param root
     *            root element of bizcomponent.
     * 
     * @return Object instance.
     */
    @Override
    public Object getData(final Vector input, final Element root) {
        lf.debug("Finisher input : " + input);
        theDoc.getRootElement().removeChildren(WizardConstants.TEMPLATE_DESC, ns);
        if (input != null) {
            final Iterator itr = input.iterator();
            while (itr.hasNext()) {
                final Object next = itr.next();
                if (next instanceof TemplateXMLInfoData) {
                    modify((TemplateXMLInfoData) next);
                }
            }
        }
        return theDoc;
    }

    /**
     * Modify the TemplateXMLInfoData information
     * 
     * @param inData
     *            as TemplateXMLInfoData
     */
    private void modify(final TemplateXMLInfoData inData) {
        Document newXML = inData.getXML();
        if (newXML == null) {
            newXML = new Document(new Element(t.getString("ReplaceStructure")));
        }
        final Element infoElem = inData.getInfoElement();
        final Document docToModify = inData.getDocument();
        String pathToChange = infoElem.getAttributeValue(WizardConstants.TEMPLATE_TARGET, ns);
        if (pathToChange == null) {
            pathToChange = "";
        }
        final Element elemToModify = findElement(docToModify, pathToChange);
        lf.debug("elemToModify = " + elemToModify);
        lf.debug("newXML = " + newXML);
        final Element parent = elemToModify.getParentElement();
        if (parent != null) {
            parent.removeContent(elemToModify);
            parent.addContent(newXML.getRootElement().detach());
        }
        infoElem.detach();
    }

    /**
     * Finds the element in bizdoucment using string value
     * 
     * @param inDoc
     *            BizDocument
     * @param xpath
     *            String value
     * 
     * @return element
     */
    public static Element findElement(final Document inDoc, final String xpath) {
        final StringTokenizer st = new StringTokenizer(xpath, "/");
        Element curElem = inDoc.getRootElement();
        while (st.hasMoreTokens()) {
            final String fullQual = st.nextToken();
            lf.debug("fullQual = " + fullQual);
            if (fullQual.equals(curElem.getQualifiedName())) {
                continue;
            }
            String namespaceStr = null;
            String nameStr = null;
            Namespace namespace = null;
            final int colonIndex = fullQual.indexOf(":");
            if (colonIndex >= 0) {
                namespaceStr = fullQual.substring(0, colonIndex);
                nameStr = fullQual.substring(colonIndex + 1);
                lf.debug("namespaceStr = " + namespaceStr);
                lf.debug("nameStr = " + nameStr);
                lf.debug("curElem = " + curElem);
                lf.debug("curElem.getChildren() = " + curElem.getChildren());
                final Iterator kids = curElem.getChildren().iterator();
                while (kids.hasNext()) {
                    final Element tmp = (Element) kids.next();
                    try {
                        namespace = curElem.getNamespace(namespaceStr);
                        if (namespace != null) {
                            break;
                        }
                    } catch (final Exception e) {
                        lf.debug("No namespace with prefix: " + namespaceStr + ", on element : " + curElem.getQualifiedName());
                    }
                }
            } else {
                nameStr = fullQual;
            }
            if (namespace == null) {
                lf.debug("curElem = " + curElem + ", curElem.getChild(" + nameStr + ") = " + curElem.getChild(nameStr));
                curElem = curElem.getChild(nameStr);
            } else {
                lf.debug("curElem = " + curElem + ", curElem.getChild(" + nameStr + ", " + namespace + ") = " + curElem.getChild(nameStr, namespace));
                curElem = curElem.getChild(nameStr, namespace);
            }
        }
        return curElem;
    }
}
