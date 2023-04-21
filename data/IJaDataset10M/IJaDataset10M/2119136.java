package backend.parser.kegg.genes;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import backend.event.type.StatisticalOutput;
import backend.parser.kegg.Parser;
import backend.parser.kegg.data.Entry;
import backend.parser.kegg.data.Pathway;
import backend.parser.kegg.sink.Concept;
import backend.parser.kegg.sink.ConceptWriter;
import backend.parser.kegg.sink.Relation;
import backend.parser.kegg.util.Util;

/**
 * @author taubertj
 *
 */
public class GenesPathwayParser {

    private Vector pathways;

    public GenesPathwayParser(Vector pathways) {
        this.pathways = pathways;
    }

    public void parse() {
        Iterator it = pathways.iterator();
        while (it.hasNext()) {
            Pathway pathway = (Pathway) it.next();
            Enumeration enu = pathway.entries.elements();
            while (enu.hasMoreElements()) {
                Entry entry = (Entry) enu.nextElement();
                if (entry.getType().equals("genes")) {
                    Concept protcmplx = new Concept("group:" + pathway.getName() + "_" + entry.getId());
                    protcmplx.setDescription("kegg protein complex");
                    protcmplx.setElement_of("KEGG");
                    protcmplx.setOf_type_fk("Protcmplx");
                    entry.conceptIDs.add(protcmplx.getId());
                    Util.writeConcept(protcmplx);
                    HashSet<Relation> relations = new HashSet<Relation>();
                    Enumeration enu2 = entry.components.elements();
                    while (enu2.hasMoreElements()) {
                        Entry component = (Entry) enu2.nextElement();
                        Iterator it2 = component.conceptIDs.iterator();
                        while (it2.hasNext()) {
                            String id = (String) it2.next();
                            if (id.indexOf("_GE") > -1 || id.indexOf("_PR") > -1 || id.indexOf("_EN") > -1) {
                                id = id.substring(0, id.length() - 3);
                            }
                            String cIDPR = id + "_PR";
                            String cIDEN = id + "_EN";
                            String cIDGE = id + "_GE";
                            if (ConceptWriter.writtenConcepts.containsKey(cIDPR)) {
                                Relation r = new Relation(cIDPR, protcmplx.getId(), "is_p");
                                r.setFrom_element_of("KEGG");
                                r.setTo_element_of("KEGG");
                                relations.add(r);
                            } else if (ConceptWriter.writtenConcepts.containsKey(cIDEN)) {
                                Relation r = new Relation(cIDEN, protcmplx.getId(), "is_p");
                                r.setFrom_element_of("KEGG");
                                r.setTo_element_of("KEGG");
                                relations.add(r);
                            } else if (ConceptWriter.writtenConcepts.containsKey(cIDGE)) {
                                Relation r = new Relation(cIDGE, protcmplx.getId(), "is_p");
                                r.setFrom_element_of("KEGG");
                                r.setTo_element_of("KEGG");
                                relations.add(r);
                            } else if (ConceptWriter.writtenConcepts.containsKey(id)) {
                                Relation r = new Relation(id, protcmplx.getId(), "is_p");
                                r.setFrom_element_of("KEGG");
                                r.setTo_element_of("KEGG");
                                relations.add(r);
                            } else {
                                Parser.propagateEventOccurred(new StatisticalOutput(id + " not in written concepts"));
                            }
                        }
                    }
                    Util.writeRelations(relations);
                }
            }
        }
    }
}
