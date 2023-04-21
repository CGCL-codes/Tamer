package geocosm.gpx;

import java.io.ByteArrayOutputStream;
import org.w3c.dom.*;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;

/**
 * Automatic generated Classrepresentation of Name
 * 
 * <p>Copyright (c) 2002  Oliver Schünemann. All Rights Reserved.
 * 
 * @since 02/04/02 19:28
 * @version 1.0
 * @author Oliver Schünemann
 */
public class XML_Name {

    protected Node myNode = null;

    String pcData = null;

    String NodeNames[] = new String[1];

    /**
   * Constructor constructing the object with an empty Dom-Node
   *
   */
    public XML_Name() {
        initNodeNames();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            myNode = document.createElement("name");
            parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Constructor constructing the object with the given Dom-Node
   * @param node Node representing the object
   * @throws SAXParseException
   *
   */
    public XML_Name(Node node) throws SAXParseException {
        initNodeNames();
        myNode = node;
        parse();
    }

    /** 
   *  Help-Method for initializing the nodeNames field
   * 
   */
    private void initNodeNames() {
        NodeNames[0] = "#PCDATA";
    }

    public String getPCDATA() {
        return pcData;
    }

    public void setPCDATA(String newPcData) {
        pcData = newPcData;
        if ((newPcData != null) && (newPcData.length() > 0)) {
            Node tNode = myNode.getOwnerDocument().createTextNode(newPcData);
            myNode.appendChild(tNode);
        } else {
            NodeList list = myNode.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.TEXT_NODE) {
                    myNode.removeChild(node);
                }
            }
        }
    }

    private void parse() throws SAXParseException {
        NodeList list = myNode.getChildNodes();
        int anz = list.getLength();
        for (int i = 0; i < anz; i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                pcData = node.getNodeValue();
            }
        }
    }

    public void setNode(Node newNode) throws SAXParseException {
        myNode = newNode;
        NodeList list = myNode.getChildNodes();
        int anz = list.getLength();
        for (int i = 0; i < anz; i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                setPCDATA(node.getNodeValue());
            }
        }
    }

    public boolean equals(Object compare) {
        if (compare == null) {
            throw new NullPointerException("Can't compare XML_Name with null");
        }
        if (!(compare instanceof XML_Name)) {
            throw new UnsupportedOperationException("Can't compare with anything else then XML_Name");
        }
        return true;
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
   * This method is similar to clone but returns the correct class-type
   * @return
   * @throws SAXParseException
   */
    public XML_Name duplicate() throws SAXParseException {
        XML_Name ret = new XML_Name();
        return ret;
    }

    public String toString() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            StreamResult t = new StreamResult(os);
            DOMSource source = new DOMSource(myNode);
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(source, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(os.toByteArray());
    }
}
