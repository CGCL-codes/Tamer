package net.sourceforge.ondex.parser.kegg53.comp;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import net.sourceforge.ondex.exception.type.MetaDataMissingException;
import net.sourceforge.ondex.parser.kegg53.MetaData;
import net.sourceforge.ondex.parser.kegg53.Parser;
import net.sourceforge.ondex.parser.kegg53.data.Entry;
import net.sourceforge.ondex.parser.kegg53.data.Pathway;
import net.sourceforge.ondex.parser.kegg53.sink.Concept;
import net.sourceforge.ondex.parser.kegg53.util.DPLPersistantSet;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Merges pathway maps information with parsing results from compound and glycan
 * database.
 *
 * @author taubertj
 */
public class CompPathwayMerger {

    /**
     * Include only those compounds mentioned in pathway maps.
     *
     * @param pathways KGML files
     * @param concepts extracted from compound and glycan files
     */
    public void mergeAndWrite(DPLPersistantSet<Pathway> pathways, Map<String, Concept> concepts) throws MetaDataMissingException {
        final Pattern spaceSplit = Pattern.compile(" ");
        EntityCursor<Pathway> cursor = pathways.getCursor();
        for (Pathway pathway : cursor) {
            for (Entry entry : pathway.getEntries().values()) {
                if (entry.getType().equalsIgnoreCase("compound")) {
                    String[] results = spaceSplit.split(entry.getName().toUpperCase().trim());
                    for (String result : results) {
                        result = result.trim().toUpperCase().replaceAll("GLYCAN", "GL");
                        Concept concept = concepts.get(result);
                        if (concept == null) {
                            System.err.println("Entry \"" + result + "\"  is referenced in pathway \"" + pathway.getId() + "\" but was not found in compound, drug or glycan file");
                            concept = new Concept(result, MetaData.CV_KEGG, MetaData.CC_COMPOUND);
                            concept.setDescription("abstract compound entry");
                        }
                        if (entry.getLink() != null) {
                            concept.setUrl(entry.getLink());
                        }
                        entry.getConceptIDs().add(concept.getId().trim().toUpperCase());
                        if (!Parser.getConceptWriter().conceptParserIDIsWritten(concept.getId())) {
                            Parser.getUtil().writeConcept(concept, false);
                        }
                    }
                }
            }
            try {
                cursor.update(pathway);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
        pathways.closeCursor(cursor);
    }
}
