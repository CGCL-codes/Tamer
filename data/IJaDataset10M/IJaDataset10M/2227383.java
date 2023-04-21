package net.sourceforge.ondex.parser.psimi_lite;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.RelationType;
import psidev.psi.mi.xml.model.Confidence;

/**
 * 
 * @author lysenkoa
 *
 */
public class SafetyFirstWrapper {

    DatabaseTranslator resolver = new DatabaseTranslator();

    private HashSet<String> unmappedCVS = new HashSet<String>();

    private HashSet<String> unmappedCCs = new HashSet<String>();

    private HashSet<String> unmappedRTs = new HashSet<String>();

    private HashSet<String> unmappedDRs = new HashSet<String>();

    private HashSet<String> unmappedCONFs = new HashSet<String>();

    private HashMap<String, String> ccMap;

    private HashMap<String, String> rtMap;

    private HashMap<String, String> dbMap;

    private HashMap<String, String> drMap;

    private HashMap<String, String> confMap;

    private int ccsFailed = 0;

    private int rtsFailed = 0;

    private ONDEXGraph graph;

    static final String cvTAIR = "TAIR".intern();

    static final String cvUNIPROTKB = "UNIPROTKB".intern();

    static final String cvSGD = "SGD".intern();

    static final String cvINTACT = "INTACT".intern();

    static final Set<String> accs = new HashSet<String>(Arrays.asList(new String[] { cvTAIR, cvUNIPROTKB, cvSGD, cvINTACT }));

    private final boolean DEBUG = false;

    /**
	 * 
	 * @author lysenkoa
	 * 
	 */
    public SafetyFirstWrapper(String pathAndFile, ONDEXGraph graph) {
        this.graph = graph;
        resolver.parseFile(pathAndFile);
        ccMap = resolver.getTranslation().get("cc");
        rtMap = resolver.getTranslation().get("rt");
        dbMap = resolver.getTranslation().get("db");
        drMap = resolver.getTranslation().get("direction");
        confMap = resolver.getTranslation().get("confidences");
    }

    public ONDEXConcept createConcept(String pid, CV mainCV, String fullTypename, EvidenceType evidence) {
        boolean failed = false;
        String ccName = ccMap.get(fullTypename);
        if (ccName == null) {
            unmappedCCs.add(fullTypename);
            failed = true;
        }
        if (failed) {
            ccsFailed++;
            return null;
        }
        ConceptClass cc = graph.getMetaData().getConceptClass(ccName);
        ONDEXConcept c = graph.getFactory().createConcept(pid, mainCV, cc, evidence);
        return c;
    }

    public ONDEXRelation createRelation(ONDEXConcept source, ONDEXConcept target, String type, Collection<EvidenceType> evidence, String directionKey) {
        String ofTypeSet = rtMap.get(type);
        if (ofTypeSet == null) ofTypeSet = type;
        RelationType rts = graph.getMetaData().getRelationType(ofTypeSet);
        if (rts == null) {
            RelationType rt = graph.getMetaData().getRelationType(ofTypeSet);
            if (rt != null) {
                rts = graph.getMetaData().getFactory().createRelationType(ofTypeSet, rt);
            } else {
                unmappedRTs.add(type);
                rtsFailed++;
                return null;
            }
        }
        if (!drMap.containsKey(directionKey)) {
            unmappedDRs.add(directionKey);
            return null;
        }
        if (drMap.get(directionKey).equals("SOURCE")) {
            if (graph.getRelation(source, target, rts) == null && graph.getRelation(target, source, rts) == null) {
                return graph.getFactory().createRelation(source, target, rts, evidence);
            }
        } else if (drMap.get(directionKey).equals("TARGET")) {
            if (graph.getRelation(target, source, rts) == null && graph.getRelation(target, source, rts) == null) {
                return graph.getFactory().createRelation(target, source, rts, evidence);
            }
        } else if (drMap.get(directionKey).equals("BOTH")) {
            if (graph.getRelation(source, target, rts) == null && graph.getRelation(target, source, rts) == null) {
                return graph.getFactory().createRelation(source, target, rts, evidence);
            }
        } else System.err.println(directionKey + " -> " + drMap.get(directionKey));
        return null;
    }

    public boolean addAccession(ONDEXConcept c, String strDB, String acc, boolean isPrimary) {
        String strCV = dbMap.get(strDB);
        if (strCV != null) {
            CV refCv = graph.getMetaData().getCV(strCV.toUpperCase());
            String id = refCv.getId().intern();
            if (!accs.contains(strDB) && !isPrimary) {
                return true;
            }
            if (id.equals(cvUNIPROTKB) && !isPrimary) {
                return true;
            }
            boolean ambiguous = true;
            if (isPrimary || id.equals(cvTAIR) || id.equals(cvSGD)) {
                ambiguous = false;
            }
            if (c.getConceptAccession(acc, refCv) == null) c.createConceptAccession(acc, refCv, ambiguous);
            return true;
        }
        unmappedCVS.add(strDB);
        return false;
    }

    public boolean addConfidence(ONDEXRelation r, AttributeName confAN, Collection<Confidence> confids) {
        if (r != null && confids != null && confids.iterator().hasNext()) {
            Iterator<Confidence> coIt = confids.iterator();
            Confidence c;
            String label, valString;
            Double value;
            while (coIt.hasNext()) {
                c = coIt.next();
                label = c.getUnit().getNames().getShortLabel().trim();
                valString = c.getValue().trim();
                if (label.equals("score") || label.equals("author-confidence") || label.equals("Hybrigenics PBS(r)")) {
                    if (DEBUG) System.out.print(label + ": " + valString + " - ");
                    try {
                        value = Double.parseDouble(valString);
                        r.createRelationGDS(confAN, value, false);
                        if (DEBUG) System.out.println("processed");
                    } catch (NumberFormatException nfe) {
                        confMap.containsKey(valString);
                        if (confMap.containsKey(valString)) {
                            value = Double.parseDouble(confMap.get(valString));
                            Object success = r.createRelationGDS(confAN, value, false);
                            if (success != null) if (DEBUG) System.out.println("processed"); else if (DEBUG) System.out.println("not processed");
                        } else {
                            unmappedCONFs.add(valString);
                            if (DEBUG) System.out.println("not processed");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void printReport() {
        if (unmappedCVS.size() != 0 || ccsFailed != 0 || rtsFailed != 0 || unmappedDRs.size() != 0) {
            System.err.println("Parser Report");
            System.err.println("=============================");
            System.err.println("Concepts skipped: " + ccsFailed);
            System.err.println("Unmapped source names: " + unmappedCVS);
            System.err.println("Unmapped concept types: " + unmappedCCs);
            System.err.println("Relations skipped: " + rtsFailed);
            System.err.println("Unmapped relation types: " + unmappedRTs);
            System.err.println("Unmapped relation direction keys: " + unmappedDRs);
        }
    }
}
