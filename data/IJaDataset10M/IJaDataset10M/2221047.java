package loengud.yheksas.it140;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Andmete �ratoomise klass.
 * 
 * @author A
 *
 */
public class AndmeteTooja {

    /**
	 * Konstruktor - peab ette andma l�hteandmete asukoha.
	 * 
	 * @param uri
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
    public AndmeteTooja(String uri) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(uri);
        NodeList list = doc.getDocumentElement().getElementsByTagName("row");
        for (int i = 0; i < list.getLength(); i++) {
            Element node = (Element) list.item(i);
            System.out.println(node.getTextContent());
        }
    }
}
