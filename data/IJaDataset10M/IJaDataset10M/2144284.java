package org.csiro.darjeeling.infuser.structure.elements.external;

import org.csiro.darjeeling.infuser.structure.ElementVisitor;
import org.csiro.darjeeling.infuser.structure.elements.AbstractHeader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ExternalHeader extends AbstractHeader {

    private ExternalHeader(String infusionName, int majorVersion, int minorVersion) {
        super(infusionName, majorVersion, minorVersion);
    }

    public static ExternalHeader fromDocument(Document doc) {
        return fromNode(NodeUtil.getNodeByXPath(doc, "dih/infusion/header"));
    }

    public static ExternalHeader fromNode(Node node) {
        int majorVersion = Integer.parseInt(node.getAttributes().getNamedItem("majorversion").getNodeValue());
        int minorVersion = Integer.parseInt(node.getAttributes().getNamedItem("minorversion").getNodeValue());
        String name = node.getAttributes().getNamedItem("name").getNodeValue();
        return new ExternalHeader(name, majorVersion, minorVersion);
    }

    @Override
    public void accept(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
