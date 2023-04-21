package backend.exchange.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import backend.core.AbstractONDEXGraph;
import backend.core.security.Session;

/**
 * This class parses XML ONDEX general meta data and stores them the ONDEX graph meta data
 * @author sierenk
 * 
 */
public class GeneralMetaDataParser extends AbstractMetaDataParser {

    private String type;

    /**
	 * Creates a parser for Concept elements.
	 * 
	 * @param og
	 *            ONDEXGraph for storing parsed general meta data
	 */
    public GeneralMetaDataParser(AbstractONDEXGraph og, String type) {
        this(Session.NONE, og, type);
    }

    /**
	 * Creates a parser for Concept elements.
	 * 
	 * @param s - session context
	 * @param og
	 *            ONDEXGraph for storing parsed general meta data
	 */
    public GeneralMetaDataParser(Session s, AbstractONDEXGraph og, String type) {
        super(s, og);
        this.type = type;
    }

    /**
	 * Parses ONDEX general meta data.
	 * 
	 * @param xmlr stream
	 */
    public void parse(XMLStreamReader xmlr) throws XMLStreamException {
        if (type.equals("unit")) {
            parseUnit(xmlr);
        } else if (type.equals("attrname")) {
            try {
                parseAttributeName(xmlr);
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            }
        } else if (type.equals("evidences")) {
            parseEvidences(xmlr);
        }
    }
}
