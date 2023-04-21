package mimosa.ontology.ui;

import mimosa.io.XMLDefaultRecursiveHandler;
import mimosa.io.XMLReadException;
import mimosa.io.XMLReader;
import mimosa.io.XMLRecursiveHandler;
import mimosa.ontology.Link;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Jean-Pierre Muller
 */
public class LinkMEdgeHandler extends XMLRecursiveHandler {

    private LinkMEdge edge;

    private String endTag;

    /**
	 * 
	 */
    public LinkMEdgeHandler() {
    }

    /**
	 * @param xmlReader
	 * @param previous
	 * @param atts
	 * @param context
	 * @throws XMLReadException 
	 */
    public LinkMEdgeHandler(XMLReader xmlReader, XMLRecursiveHandler previous, Attributes atts, Object context) throws XMLReadException {
        super(xmlReader, previous, atts, context);
        edge = (LinkMEdge) context;
    }

    /**
	 * @see mimosa.io.XMLRecursiveHandler#initialize(mimosa.io.XMLReader, mimosa.io.XMLRecursiveHandler, org.xml.sax.Attributes, java.lang.Object)
	 */
    @Override
    public void initialize(XMLReader xmlReader, XMLRecursiveHandler previous, Attributes atts, Object context) throws XMLReadException {
        super.initialize(xmlReader, previous, atts, context);
        edge = (LinkMEdge) context;
    }

    /**
	 * Defines the behaviour when a new tag is met.
	 * @param namespace the name space
	 * @param lName the local name
	 * @param qName the qualified name
	 * @param atts the attributes of the tag
	 */
    public void startElement(String namespace, String lName, String qName, Attributes atts) throws SAXException {
        String tag = (lName.equals("")) ? qName : lName;
        endTag = tag;
        if (tag.equals("portName")) {
            pushHandler(new XMLDefaultRecursiveHandler(getReader(), this, atts, null));
        }
    }

    /**
	 * Defines the behaviour when an end tag is reached.
	 * @param namespace the name space
	 * @param lName the local name
	 * @param qName the qualified name
	 */
    public void endElement(String namespace, String lName, String qName) throws SAXException {
        String tag = (lName.equals("")) ? qName : lName;
        if (tag.equals(endTag)) {
            edge.setFromPortName((Link) getNextBuildObject());
        } else popHandler(tag);
    }
}
