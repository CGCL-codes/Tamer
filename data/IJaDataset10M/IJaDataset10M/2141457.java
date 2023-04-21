package BisnessLogic.Data;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Configuration of server
 * @author Андрейка
 */
public class Configuration {

    public static int port = 666;

    public static String vfs = "";

    /**
     * Load configuration from file
     * @param path pathway to file
     * @return result of operation
     */
    public static boolean load(String path) {
        File f = new File(path);
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
            parse(doc);
            root = doc;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Save configuration to file
     * @param path pathway to file
     */
    public static void save(String path) {
        File f = new File(path);
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            changeRoot(root);
            transformer.transform(new DOMSource(root), new StreamResult(f));
        } catch (Exception e) {
            System.out.println("file not saved");
        }
    }

    /**
     * setValues to root
     * @param node root
     */
    private static void changeRoot(Node node) {
        int type = node.getNodeType();
        switch(type) {
            case Node.DOCUMENT_NODE:
                {
                    changeRoot(((Document) node).getDocumentElement());
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    NamedNodeMap attrs = node.getAttributes();
                    for (int i = 0; i < attrs.getLength(); i++) changeRoot(attrs.item(i));
                    if (node.hasChildNodes()) {
                        NodeList children = node.getChildNodes();
                        for (int i = 0; i < children.getLength(); i++) changeRoot(children.item(i));
                    }
                    break;
                }
            case Node.ATTRIBUTE_NODE:
                {
                    if (node.getNodeName().compareTo("path") == 0) {
                        ((Attr) node).setValue(vfs);
                    } else if (node.getNodeName().compareTo("number") == 0) {
                        ((Attr) node).setValue(Integer.toString(port));
                    }
                    break;
                }
        }
    }

    /**
     * Parsing xml file
     * @param node xml
     */
    private static void parse(Node node) {
        int type = node.getNodeType();
        switch(type) {
            case Node.DOCUMENT_NODE:
                {
                    parse(((Document) node).getDocumentElement());
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    NamedNodeMap attrs = node.getAttributes();
                    for (int i = 0; i < attrs.getLength(); i++) parse(attrs.item(i));
                    if (node.hasChildNodes()) {
                        NodeList children = node.getChildNodes();
                        for (int i = 0; i < children.getLength(); i++) parse(children.item(i));
                    }
                    break;
                }
            case Node.ATTRIBUTE_NODE:
                {
                    if (node.getNodeName().compareTo("path") == 0) {
                        vfs = ((Attr) node).getValue();
                    } else if (node.getNodeName().compareTo("number") == 0) {
                        port = Integer.parseInt(((Attr) node).getValue());
                    }
                    break;
                }
        }
    }

    private static Node root;
}
