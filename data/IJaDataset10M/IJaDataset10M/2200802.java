package backend.core.exchange.xml;

import java.util.HashMap;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import backend.core.AbstractONDEXGraph;

/**
 * Parses XML Documents.
 * 
 * @author sierenk
 * 
 * Uses XML parsing pattern.
 * more about this:
 * http://www.devx.com/Java/Article/30298/1954?pf=true
 *
 */
public class XmlParser implements XmlComponentParser {

    private HashMap<String, XmlComponentParser> delegates;

    private AbstractONDEXGraph og;

    public XmlParser(AbstractONDEXGraph og) {
        delegates = new HashMap<String, XmlComponentParser>();
        this.og = og;
    }

    /**
	 * 
	 * Parses a XmlStreamReader,which contains a XML Document. 
	 * 
	 * In the main event loop,farms out parsing work 
	 * to ComponentParsers based on the XML element name. That keeps 
	 * the main event loop code simple and faciliates understanding 
	 * of the XML format. 
	 * 
	 * @param XMLStreamReader 
	 * 
	 * 
	 */
    public void parse(XMLStreamReader staxXmlReader) throws XMLStreamException {
        for (int event = staxXmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = staxXmlReader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {
                String element = staxXmlReader.getLocalName();
                System.out.println("XML Parser:" + element);
                if (delegates.containsKey(element)) {
                    XmlComponentParser parser = (XmlComponentParser) delegates.get(element);
                    parser.parse(staxXmlReader);
                }
            }
        }
    }

    /**
	 * Method to register a parsing component.
     * 
     * @param elementName XML Element
	 * @param elementParser associated parsing component
	 */
    public void registerParser(String elementName, XmlComponentParser elementParser) {
        delegates.put(elementName, elementParser);
    }
}
