package org.neodatis.odb.xml.tool;

import java.io.IOException;

public class Test implements NodeEventListener {

    public static void main(String[] args) throws IOException {
        System.out.println("building");
        NodeEventListener nel = new Test();
        XMLGenerator.addListener(nel);
        XMLGenerator.setIncrementalWriteOn("testi.xml");
        XMLNode root = XMLGenerator.createRoot("odb");
        root.addAttribute("name", "test.odb").addAttribute("creation-date", "09/05/2006");
        root.endHeader();
        XMLNode metaModel = root.createNode("meta-model").addAttribute("nb-classes", "3");
        metaModel.endHeader();
        for (int i = 0; i < 1000; i++) {
            metaModel.createNode("class").addAttribute("name", "User" + i).end();
            metaModel.createNode("class").addAttribute("name", "Profile" + i).end();
            metaModel.createNode("class").addAttribute("name", "Function" + i).end();
            if (i % 5000 == 0) {
                System.out.println(i);
            }
        }
        metaModel.end();
        root.end();
        XMLGenerator.end();
        System.out.println("done");
    }

    public void startOfDocument() {
    }

    public void endOfDocument() {
    }

    public void startOfNode(String nodeName, XMLNode node) {
    }

    public void endOfNode(String nodeName, XMLNode node) {
    }
}
