package backend.parser.biopax.entity;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.io.InputStream;
import java.util.HashMap;
import org.codehaus.stax2.XMLStreamReader2;
import backend.core.AbstractONDEXGraph;
import backend.core.AbstractRelationTypeSet;
import backend.core.RelationType;
import backend.core.security.Session;
import backend.event.type.RelationTypeMissing;
import backend.parser.biopax.Parser;
import backend.parser.biopax.registers.XrefRegister;
import backend.parser.biopax.xref.OpenControlledVocabularyParser.OpenControlledVocabulary;
import backend.parser.biopax.xref.UnificationXrefParser.UnificationReference;
import com.ctc.wstx.stax.WstxInputFactory;

/**
 * Definitions of attributes shared accross Entities
 * @author hindlem
 *
 */
public abstract class EntityParser {

    public static final String RESOURCE = "resource";

    public static final String ID = "ID";

    public static final String BP_E_SYN = "SYNONYMS";

    public static final String BP_E_COMMENT = "COMMENT";

    public static final String BP_E_DATASOURCE = "DATA-SOURCE";

    public static final String BP_E_SHORT_NAME = "SHORT-NAME";

    public static final String BP_E_AVAILABILITY = "AVAILABILITY";

    public static final String BP_E_NAME = "NAME";

    public static final String BP_E_XREF = "XREF";

    protected Int2IntOpenHashMap biosourceToTaxID;

    protected Int2IntOpenHashMap publicationXrefToONDEXID;

    protected Int2ObjectOpenHashMap<OpenControlledVocabulary> openControledVocabs;

    protected Int2ObjectOpenHashMap<UnificationReference> unificationXrefs;

    protected WstxInputFactory factory;

    protected InputStream stream;

    protected HashMap<String, IntOpenHashSet> physicalEntRefToPhysicalEntityIDs;

    /**
	 * 
	 * @param factory Stax2 factory
	 * @param stream xml input
	 * @param biosourceToTaxID mappings from biosourceToTaxId see TaxonomuTranslationParser
	 * @param publicationXrefToONDEXID mappings from publicationXref to ONDEXID see PublicationParser
	 */
    public EntityParser(WstxInputFactory factory, InputStream stream, XrefRegister xrefRegisters) {
        this.factory = factory;
        this.stream = stream;
        this.biosourceToTaxID = xrefRegisters.getBiosourceToTAXID();
        this.openControledVocabs = xrefRegisters.getControledVocabs();
        this.unificationXrefs = xrefRegisters.getUni_refs();
        this.physicalEntRefToPhysicalEntityIDs = xrefRegisters.getPhysicalEntityReferenceToPhysicalEntityIDs();
    }

    /**
	 * @param s the current Session
	 * @param og the graph to create the new concepts/relations in
	 */
    public abstract void parseToGraph(Session s, AbstractONDEXGraph og);

    /**
	 * 
	 * @param reader
	 * @return the ID for this Entity
	 */
    protected String getEntityID(XMLStreamReader2 reader) {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attName = reader.getAttributeName(i).getLocalPart();
            if (attName.equalsIgnoreCase(ID)) {
                return reader.getAttributeValue(i);
            }
        }
        return null;
    }

    /**
	 * Create a RelationTypeSet, if missing in metadata.
	 * 
	 * @param name - Name of RelationType to be contained
	 * @param s the current Session
	 * @return AbstractRelationTypeSet
	 */
    protected static AbstractRelationTypeSet getRelationTypeSet(String name, AbstractONDEXGraph aog, Session s) {
        RelationType rt = aog.getONDEXGraphData(s).getRelationType(s, name);
        if (rt != null) {
            return aog.getONDEXGraphData(s).createRelationTypeSet(s, rt.getId(s), rt);
        } else {
            Parser.propagateEventOccurred(new RelationTypeMissing("Entity Parser Missing RelationType: " + name));
        }
        return null;
    }
}
