package core.xml;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import core.Cleanable;

/**
 * XMLPatcher transform XML file into class.
 * Utilisation du DOM.
 * 
 * Optimisation possible with SAX
 * 
 * @since 22/10/2008
 * @version 1.0
 * @author gael
 */
public class XMLPatcher implements Cleanable {

    private static XMLPatcher singleton = null;

    private DocumentBuilderFactory factory;

    private DocumentBuilder builder;

    private XMLPatcher() {
        factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static XMLPatcher getInstance() {
        if (singleton == null) singleton = new XMLPatcher();
        return singleton;
    }

    public XMLClass load(String path) {
        try {
            return load(new BufferedInputStream(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public XMLClass load(InputStream is) {
        try {
            final Document document = builder.parse(is);
            String parent = document.getElementsByTagName("parent").item(0).getTextContent();
            String path = document.getElementsByTagName("classname").item(0).getTextContent();
            NodeList node = document.getElementsByTagName("content").item(0).getChildNodes();
            HashMap<String, String> content = new HashMap<String, String>();
            int i, j;
            String name = "", value = "";
            NodeList item;
            for (i = 0; i < node.getLength(); i++) {
                if (node.item(i).getNodeName().equalsIgnoreCase("item")) {
                    item = node.item(i).getChildNodes();
                    for (j = 0; j < item.getLength(); j++) {
                        if (item.item(j).getNodeName().equalsIgnoreCase("name")) {
                            name = item.item(j).getTextContent();
                        } else if (item.item(j).getNodeName().equalsIgnoreCase("value")) {
                            value = item.item(j).getTextContent();
                        }
                    }
                    content.put(name, value);
                }
            }
            content.put("classname", path);
            XMLClass el = new XMLClass(path, content, parent);
            return el;
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, String> loadContent(String path) {
        try {
            return loadContent(new BufferedInputStream(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, String> loadContent(InputStream is) {
        HashMap<String, String> content = new HashMap<String, String>();
        try {
            final Document document = builder.parse(is);
            NodeList node = document.getElementsByTagName("content").item(0).getChildNodes();
            int i, j;
            String name = "", value = "";
            NodeList item;
            for (i = 0; i < node.getLength(); i++) {
                if (node.item(i).getNodeName().equalsIgnoreCase("item")) {
                    item = node.item(i).getChildNodes();
                    for (j = 0; j < item.getLength(); j++) {
                        if (item.item(j).getNodeName().equalsIgnoreCase("name")) {
                            name = item.item(j).getTextContent();
                        } else if (item.item(j).getNodeName().equalsIgnoreCase("value")) {
                            value = item.item(j).getTextContent();
                        }
                    }
                    content.put(name, value);
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    @Override
    public void cleanMemory() {
        singleton = null;
        factory = null;
        builder = null;
    }
}
