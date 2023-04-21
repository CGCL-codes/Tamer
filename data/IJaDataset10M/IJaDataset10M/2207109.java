package com.dukesoftware.utils.test.xml;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DOMPageListTest {

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            Document doc = builder.parse(new File("site.xml"));
            Element root = doc.getDocumentElement();
            System.out.println("[gvf̃^OF" + root.getTagName());
            System.out.println("***** y[WXg *****");
            NodeList list = root.getElementsByTagName("page");
            for (int i = 0; i < list.getLength(); i++) {
                Element element = (Element) list.item(i);
                String id = element.getAttribute("id");
                NodeList titleList = element.getElementsByTagName("title");
                Element titleElement = (Element) titleList.item(0);
                String title = titleElement.getFirstChild().getNodeValue();
                NodeList fileList = element.getElementsByTagName("file");
                Element fileElement = (Element) fileList.item(0);
                String file = fileElement.getFirstChild().getNodeValue();
                System.out.println("IDF" + id + "  " + "^CgF" + title + "  " + "t@CF" + file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
