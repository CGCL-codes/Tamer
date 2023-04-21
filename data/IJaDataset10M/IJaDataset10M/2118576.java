package org.vikamine.gui.attribute.workspace;

import java.util.Iterator;
import org.vikamine.kernel.data.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SelectedAttributesStateSaver {

    private AttributeJTree tree;

    public SelectedAttributesStateSaver(AttributeJTree tree) {
        this.tree = tree;
    }

    public Node createCorrelationTableNode(Document document) {
        Element correlationTableRootNode = document.createElement("selectedAttributesTree");
        Iterator iti = tree.getMap().keySet().iterator();
        while (iti.hasNext()) {
            Attribute anAttribute = (Attribute) iti.next();
            correlationTableRootNode.appendChild(createAttributeNode(document, anAttribute));
        }
        return correlationTableRootNode;
    }

    private Node createAttributeNode(Document document, Attribute anAttribute) {
        Element attrNode = document.createElement("attrNode");
        attrNode.setAttribute("attrID", anAttribute.getId());
        return attrNode;
    }
}
