package mimosa.ontology.ui;

import mimosa.io.XMLDefaultRecursiveHandler;
import mimosa.io.XMLReadException;
import mimosa.io.XMLReader;
import mimosa.io.XMLRecursiveHandler;
import mimosa.ontology.Category;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A category model node handler is a SAX handler to read in the specification of a category model node.
 * 
 * @see CategoryMNode
 * @author Jean-Pierre Muller
 */
public class CategoryMNodeHandler extends XMLRecursiveHandler {

    private CategoryMNode node;

    private String predefined;

    /**
	 * 
	 */
    public CategoryMNodeHandler() {
    }

    /**
	 * @param xmlReader
	 * @param previous
	 * @param atts
	 * @param context
	 * @throws XMLReadException 
	 */
    public CategoryMNodeHandler(XMLReader xmlReader, XMLRecursiveHandler previous, Attributes atts, Object context) throws XMLReadException {
        super(xmlReader, previous, atts, context);
    }

    /**
	 * @see mimosa.io.XMLRecursiveHandler#initialize(mimosa.io.XMLReader, mimosa.io.XMLRecursiveHandler, org.xml.sax.Attributes, java.lang.Object)
	 */
    @Override
    public void initialize(XMLReader xmlReader, XMLRecursiveHandler previous, Attributes atts, Object context) throws XMLReadException {
        super.initialize(xmlReader, previous, atts, context);
        node = (CategoryMNode) context;
        predefined = atts.getValue("predefined");
    }

    /**
	 * Defines the behaviour when a new tag is met.
	 * @param namespace the name space
	 * @param lName the local name
	 * @param qName the qualified name
	 * @param atts the attributes of the tag
	 */
    public void startElement(String namespace, String lName, String qName, Attributes atts) throws SAXException {
        super.startElement(namespace, lName, qName, atts);
        String tag = (lName.equals("")) ? qName : lName;
        if (tag.equals("category") || tag.equals("entityType")) {
            pushHandler(new XMLDefaultRecursiveHandler(getReader(), this, atts, node));
        }
    }

    /**
	 * Defines the behaviour when an end tag is reached.
	 * @param namespace the name space
	 * @param lName the local name
	 * @param qName the qualified name
	 */
    public void endElement(String namespace, String lName, String qName) throws SAXException {
        super.endElement(namespace, lName, qName);
        String tag = (lName.equals("")) ? qName : lName;
        if (tag.equals("category") || tag.equals("entityType")) {
            node.setEntityType((Category) getNextBuildObject());
        } else if (predefined != null) {
            node.setEntityType(null);
            popHandler(tag);
        } else popHandler(tag);
    }
}
