package test.de.sicari.webservice.wss;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.message.MessageElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents a test message.
 * 
 * @author Jan Peters
 * @version $Id$
 */
public class Message extends Object {

    /**
     * The name of the element containing the message.
     */
    public static final String NAME = "WssTestMessage";

    /**
     * The represantation of the message as <code>org.w3c.dom.Document</code>.
     */
    protected Document doc_;

    /**
     * The represantation of the message as <code>org.w3c.dom.Element</code>.
     */
    protected Element element_;

    /**
     * Creates a new test message.
     */
    public Message() {
        DocumentBuilderFactory factory;
        DocumentBuilder docBuilder;
        Element element1;
        Element element2;
        Element element3;
        factory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = factory.newDocumentBuilder();
            doc_ = docBuilder.newDocument();
            element_ = doc_.createElement(NAME);
            element1 = doc_.createElement("element1");
            element1.setAttribute("attribute1", "1st attribute");
            element1.setAttribute("attribute2", "2nd attribute");
            element1.setAttribute("attribute3", "3rd attribute");
            element_.appendChild(element1);
            element2 = doc_.createElement("element2");
            element2.setAttribute("attribute1", "1st attribute");
            element2.setAttribute("attribute2", "2nd attribute");
            element2.setAttribute("attribute3", "3rd attribute");
            element_.appendChild(element2);
            element3 = doc_.createElement("element3");
            element3.setAttribute("attribute1", "1st attribute");
            element3.setAttribute("attribute2", "2nd attribute");
            element3.setAttribute("attribute3", "3rd attribute");
            element_.appendChild(element3);
            doc_.appendChild(element_);
        } catch (ParserConfigurationException e) {
            e.printStackTrace(System.err);
        }
    }

    public Element getElement() {
        return element_;
    }

    public Document getDocument() {
        return doc_;
    }

    public String toString() {
        try {
            return (new MessageElement(element_)).getAsString();
        } catch (Exception e) {
            return "<error>";
        }
    }
}
