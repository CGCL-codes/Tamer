package net.sourceforge.ondex.parser.oxl2;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.export.oxl.XMLTagNames;

/**
 * This class parses XML ONDEX Concept meta data and stores them in the ONDEX
 * graph meta data.
 * 
 * @author sierenk
 */
public class ConceptMetaDataParser extends ConceptParser {

    private final String type;

    /**
	 * Creates a parser for Concept metadata elements.
	 * 
	 * @param og
	 *            ONDEXGraph for storing parsed concepts.
	 * @param type
	 *            name of meta element
	 */
    public ConceptMetaDataParser(ONDEXGraph og, String type) throws JAXBException {
        super(og, new Int2IntOpenHashMap(), null);
        this.type = type;
    }

    /**
	 * Parses ONDEX Concept Meta Data.
	 * 
	 * @param xmlr
	 *            XMLStreamReader
	 */
    @Override
    public void parse(XMLStreamReader xmlr) throws XMLStreamException {
        if (type.equals(XMLTagNames.CV)) {
            parseCVContent(xmlr);
        } else if (type.equals(XMLTagNames.CC)) {
            parseConceptClassContent(xmlr);
        }
    }
}
