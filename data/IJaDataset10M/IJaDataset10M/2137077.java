package org.one.stone.soup.xapp.components.tree;

import org.one.stone.soup.xapp.controller.xml.XmlSchemaController;
import org.one.stone.soup.xml.XmlElement;

public class XappTreeAPI {

    private XTree tree;

    public XappTreeAPI(XTree tree) {
        this.tree = tree;
    }

    public XmlElement getSelectedElement() {
        return tree.getSelectedNode();
    }

    public void setSelectedElement(XmlElement element) {
        tree.selectNode(element);
    }

    public void setSelectedElements(XmlElement elements) {
        tree.selectNodes(elements);
    }
}
