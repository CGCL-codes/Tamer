package com.tutorials.java.xmltech.other;

import java.io.File;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class JDOMApiRunMain {

    String fileToProcess = "src/com/tutorials/xml/xmlfiles/mybookstore-xmldt.xml";

    public static void main(String[] args) {
        JDOMApiRunMain demon = new JDOMApiRunMain();
        try {
            demon.updateXML();
        } catch (Exception e) {
            System.out.println(" Exception occured...." + e);
        }
    }

    public void updateXML() {
        try {
            Document doc = new SAXBuilder().build(new File(fileToProcess));
            Element rootElement = doc.getRootElement();
            Element book = rootElement.getChild("book").getChild("author").setText("Sidd");
            XMLOutputter outputter = new XMLOutputter();
            outputter.output(doc, System.out);
        } catch (Exception e) {
            System.out.println(" >>" + e);
        }
    }
}
