package backend.parser.kegg2.comp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import backend.event.type.DataFileError;
import backend.parser.kegg2.MetaData;
import backend.parser.kegg2.Parser;
import backend.parser.kegg2.sink.Concept;
import backend.parser.kegg2.sink.ConceptAcc;
import backend.parser.kegg2.sink.ConceptName;
import backend.tools.FastSplit;

/**
 * @author taubertj
 *  
 */
public class CompParser {

    private static final String DBLINKS = "DBLINKS";

    private String pathToCompound;

    private String pathToGlycan;

    public CompParser(String pathToCompound, String pathToGlycan) {
        this.pathToCompound = pathToCompound;
        this.pathToGlycan = pathToGlycan;
    }

    public ConcurrentHashMap<String, Concept> parse() {
        ConcurrentHashMap<String, Concept> concepts = new ConcurrentHashMap<String, Concept>(5000);
        parseCompound(concepts);
        parseGlycan(concepts);
        Parser.getConceptWriter().waitToFinish("CompParser");
        return concepts;
    }

    private void parseCompound(ConcurrentHashMap<String, Concept> concepts) {
        try {
            File file = new File(pathToCompound);
            Concept concept = null;
            String conceptNames = null;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            boolean inFormula = false;
            boolean inDblinks = false;
            boolean inName = false;
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.length() > 12) {
                    if (!(line.substring(0, 11).trim().length() == 0)) {
                        inFormula = false;
                        inDblinks = false;
                        inName = false;
                        if (conceptNames != null) {
                            String[] result = FastSplit.fastSplit(conceptNames, FastSplit.SEMICOLON, false);
                            for (String name : result) {
                                name = name.trim();
                                if (name.length() > 0) {
                                    ConceptName conceptName = new ConceptName(concept.getId(), name);
                                    conceptName.setPreferred(true);
                                    concept.getConceptNames().add(conceptName);
                                }
                            }
                            conceptNames = null;
                        }
                    }
                    if (line.indexOf("ENTRY") > -1) {
                        concept = new Concept("cpd:" + line.substring(12, 18), MetaData.CV_KEGG, MetaData.CC_COMPOUND);
                        concepts.put(concept.getId().toUpperCase(), concept);
                        ConceptAcc conceptAcc = new ConceptAcc(concept.getId(), line.substring(12, 18), MetaData.CV_KEGG);
                        concept.getConceptAccs().add(conceptAcc);
                    } else if (inFormula || line.indexOf("FORMULA") > -1) {
                        inFormula = true;
                        String formular = line.substring(12, line.length()).trim();
                        ConceptName conceptName = new ConceptName(concept.getId(), formular);
                        conceptName.setPreferred(false);
                        concept.getConceptNames().add(conceptName);
                        if (concept.getDescription() == null) {
                            concept.setDescription(formular);
                        } else {
                            concept.setDescription(concept.getDescription() + " " + formular);
                        }
                    } else if (inName || line.indexOf("NAME") > -1) {
                        inName = true;
                        line = line.substring(12, line.length());
                        if (conceptNames == null) conceptNames = line; else conceptNames = conceptNames + line;
                    } else if (inDblinks || line.indexOf("DBLINKS") > -1) {
                        inDblinks = true;
                        line = line.substring(12, line.length());
                        String[] result = FastSplit.fastSplit(line, FastSplit.COLON, false);
                        if (result.length == 2) {
                            String db = result[0];
                            String acc = result[1].trim();
                            String[] accs = FastSplit.fastSplit(acc, FastSplit.SPACE, false);
                            for (String accession : accs) {
                                ConceptAcc conceptAcc = new ConceptAcc(concept.getId(), accession, db);
                                concept.getConceptAccs().add(conceptAcc);
                            }
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Parser.propagateEventOccurred(new DataFileError(ioe.getMessage()));
        }
    }

    private void parseGlycan(ConcurrentHashMap<String, Concept> concepts) {
        try {
            File file = new File(pathToGlycan);
            Concept concept = null;
            String conceptNames = null;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            boolean inComposition = false;
            boolean inDblinks = false;
            boolean inName = false;
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.length() > 12) {
                    if (!line.substring(0, 11).trim().equals("")) {
                        inComposition = false;
                        inDblinks = false;
                        inName = false;
                        if (conceptNames != null) {
                            String[] result = FastSplit.fastSplit(conceptNames, FastSplit.SEMICOLON, false);
                            for (String name : result) {
                                name = name.trim();
                                if (name.length() > 0) {
                                    ConceptName conceptName = new ConceptName(concept.getId(), name);
                                    conceptName.setPreferred(true);
                                    concept.getConceptNames().add(conceptName);
                                }
                            }
                            conceptNames = null;
                        }
                    }
                    if (line.indexOf("ENTRY") > -1) {
                        concept = new Concept("gl:" + line.substring(12, 18), MetaData.CV_KEGG, MetaData.CC_COMPOUND);
                        concepts.put(concept.getId().toUpperCase(), concept);
                    } else if (inComposition || line.indexOf("COMPOSITION") > -1) {
                        inComposition = true;
                        if (concept.getDescription() == null) {
                            concept.setDescription(line.substring(12, line.length()));
                        } else {
                            concept.setDescription(concept.getDescription() + line.substring(12, line.length()));
                        }
                    } else if (inName || line.indexOf("NAME") > -1) {
                        inName = true;
                        line = line.substring(12, line.length());
                        if (conceptNames == null) conceptNames = line; else conceptNames = conceptNames + line;
                    } else if (inDblinks || line.indexOf("DBLINKS") > -1) {
                        inDblinks = true;
                        int indexof = line.indexOf(DBLINKS);
                        if (indexof > -1) {
                            line = line.substring(indexof + DBLINKS.length());
                        }
                        String[] result = FastSplit.fastSplit(line, FastSplit.COLON, false);
                        if (result.length == 2) {
                            String db = result[0];
                            String acc = result[1].trim();
                            String[] accs = FastSplit.fastSplit(acc, FastSplit.SPACE, false);
                            for (String access : accs) {
                                ConceptAcc conceptAcc = new ConceptAcc(concept.getId(), access, db);
                                concept.getConceptAccs().add(conceptAcc);
                            }
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException ioe) {
            Parser.propagateEventOccurred(new DataFileError(ioe.getMessage()));
        }
    }
}
