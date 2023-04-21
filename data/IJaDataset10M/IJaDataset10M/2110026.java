package org.spbu.pldoctoolkit.graph.util;

import java.util.Map;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;
import org.spbu.pldoctoolkit.graph.DrlElement;
import org.spbu.pldoctoolkit.graph.DrlPackage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DrlXMLHandler extends SAXXMIHandler {

    private Node currentNode;

    private boolean isTopNode = true;

    private String topNodeUri;

    private String topNodeLocalName;

    private String topNodeName;

    public DrlXMLHandler(XMLResource xmiResource, XMLHelper helper, Map<?, ?> options) {
        super(xmiResource, helper, options);
    }

    @Override
    public void startElement(String uri, String localName, String name) {
        if (DrlResourceImpl.DOCBOOK_URI.equals(uri)) {
            return;
        }
        if (!isTopNode && DrlPackage.eNS_URI.equals(uri)) {
            return;
        }
        super.startElement(uri, localName, name);
        if (isTopNode) {
            topNodeUri = uri;
            topNodeLocalName = localName;
            topNodeName = name;
            isTopNode = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) {
        if (DrlResourceImpl.DOCBOOK_URI.equals(uri)) {
            return;
        }
        if (DrlPackage.eNS_URI.equals(uri)) {
            boolean isTopNode = stringEquals(localName, topNodeLocalName) && stringEquals(name, topNodeName) && stringEquals(uri, topNodeUri);
            if (!isTopNode) {
                return;
            }
        }
        super.endElement(uri, localName, name);
    }

    private boolean stringEquals(String s1, String s2) {
        if (s1 == null) {
            return s1 == s2;
        }
        return s1.equals(s2);
    }

    /**
	 * @return
	 */
    public Node getCurrentNode() {
        return currentNode;
    }

    /**
	 * @param currentNode
	 */
    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected EObject createObjectFromFactory(EFactory factory, String typeName) {
        EObject result = super.createObjectFromFactory(factory, typeName);
        if (result instanceof DrlElement) {
            DrlElement drlNode = (DrlElement) result;
            drlNode.setNode((Element) getCurrentNode());
        }
        return result;
    }
}
