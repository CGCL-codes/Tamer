package com.timothy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParseBiz {

    InputStream inputstream = null;

    public XMLParseBiz(String xmlpath) {
        try {
            File f = new File(xmlpath);
            this.inputstream = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过DOM方法解析XML文件
     * @param inputstream
     * @return
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws SAXException 
     */
    public List<Person> domParseXML() throws ParserConfigurationException, SAXException, IOException {
        List<Person> list = new ArrayList<Person>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(this.inputstream);
        Element root = doc.getDocumentElement();
        NodeList personnodes = root.getElementsByTagName("person");
        for (int i = 0; i < personnodes.getLength(); i++) {
            Element person = (Element) personnodes.item(i);
            Person p = new Person();
            p.setId(new Integer(person.getAttribute("id")));
            NodeList nodelist = person.getChildNodes();
            for (int j = 0; j < nodelist.getLength(); j++) {
                Node childnode = nodelist.item(j);
                if (childnode.getNodeType() == Node.ELEMENT_NODE && childnode.getNodeName().equals("name")) {
                    p.setName(childnode.getFirstChild().getNodeValue());
                } else if (childnode.getNodeType() == Node.ELEMENT_NODE && childnode.getNodeName().equals("age")) {
                    p.setAge(new Integer(childnode.getFirstChild().getNodeValue()));
                }
            }
            list.add(p);
        }
        return list;
    }

    /**
     * 通过SAX方法解析XML文件
     * @param inputstream
     * @return
     * @throws IOException 
     * @throws SAXException 
     * @throws ParserConfigurationException 
     */
    public List<Person> saxParseXML() throws SAXException, IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        SaxHelper helper = new SaxHelper();
        parser.parse(this.inputstream, helper);
        return helper.getList();
    }

    public static void main(String[] args) {
        XMLParseBiz biz = new XMLParseBiz("E:\\workspace\\AndroidServer\\res\\yaku.xml");
        try {
            List<Person> persons = biz.saxParseXML();
            for (int i = 0; i < persons.size(); i++) {
                System.out.println(persons.get(i).toString());
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
