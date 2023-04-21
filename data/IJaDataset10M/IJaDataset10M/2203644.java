package displayObjects;

import java.io.PrintStream;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

public class XMLprinter {

    public static Document getDoc() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docParser = null;
        try {
            docParser = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        Document doc = docParser.newDocument();
        return doc;
    }

    public static String printXMLtoString(DisplayObject dispObj) {
        StringWriter sw = new StringWriter();
        XMLprinter.printXML(dispObj, sw);
        return sw.toString();
    }

    public static void printXML(DisplayObject dispObj, PrintStream out) {
        StreamResult result = new StreamResult(out);
        printXML(dispObj, result);
    }

    public static void printXML(DisplayObject dispObj, Writer out) {
        StreamResult result = new StreamResult(out);
        printXML(dispObj, result);
    }

    public static void printXML(DisplayObject dispObj, StreamResult result) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docParser = null;
        try {
            docParser = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return;
        }
        Document doc = docParser.newDocument();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer trans = null;
        try {
            trans = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e1) {
            e1.printStackTrace();
            return;
        }
        DOMSource source = new DOMSource(dispObj.toHTMLElement(doc));
        try {
            trans.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
