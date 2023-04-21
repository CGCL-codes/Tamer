package com.global360.sketchpadbpmn.documents.xpdl.elements;

import org.jdom.Element;
import org.jdom.Namespace;
import com.global360.sketchpadbpmn.documents.XPDLConstants;

public class NodeGraphicsInfosElement extends InfosElement {

    public NodeGraphicsInfosElement(Namespace namespace) {
        super(XPDLConstants.S_NODE_GRAPHICS_INFOS, namespace);
    }

    public NodeGraphicsInfosElement(Element source) {
        super(source);
    }

    public NodeGraphicsInfoElement getChildElement(String toolId) {
        InfoElement match = this.getMatchingChildElement(XPDLConstants.S_NODE_GRAPHICS_INFO, toolId);
        if (match != null) {
            return NodeGraphicsInfoElement.make(match.getElement());
        }
        return null;
    }

    public static NodeGraphicsInfosElement make(Element element) {
        NodeGraphicsInfosElement result = null;
        if (element != null) {
            if (element.getName().equalsIgnoreCase(XPDLConstants.S_NODE_GRAPHICS_INFOS)) {
                result = new NodeGraphicsInfosElement(element);
            }
        }
        return result;
    }
}
