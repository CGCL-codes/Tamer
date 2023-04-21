package org.terentich.ox.parsers;

import org.w3c.dom.Node;

public class MainXMLParser {

    public static void main(String[] args) {
        OXXMLParser parser = new OXXMLParser("./data/test.xml", "");
        Node node = parser.getNode("/model");
        System.out.println(node.getNodeName() + " " + node.getAttributes().item(0).getNodeName());
    }
}
