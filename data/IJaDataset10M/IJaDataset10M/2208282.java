package backend.parser.kegg2.ko;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
public class KoParser {

    private String pathToKO;

    public KoParser(String pathToKO) {
        this.pathToKO = pathToKO;
    }

    private Object2ObjectOpenHashMap<Concept, Set<String>> koConceptToGenes = new Object2ObjectOpenHashMap<Concept, Set<String>>();

    private HashMap<String, Concept> koNamesToKoConcept = new HashMap<String, Concept>();

    private HashMap<String, Concept> koAccessionToKoConcept = new HashMap<String, Concept>();

    /**
	 * Parses to ko.txt file and constructs a KO concept for each entry
	 * 
	 * @return Mapping KO concept to containing genes
	 */
    public void parse() {
        koConceptToGenes.clear();
        try {
            File file = new File(pathToKO);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            boolean inDefinition = false;
            boolean inDblinks = false;
            boolean inGenes = false;
            Concept concept = null;
            String org = null;
            ObjectOpenHashSet<String> genes = null;
            ArrayList<String> ambiguousName = new ArrayList<String>();
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.length() > 12) {
                    if (line.substring(0, 11).trim().length() != 0) {
                        inDefinition = false;
                        inDblinks = false;
                        inGenes = false;
                    }
                    if (line.startsWith("ENTRY")) {
                        String koAccession = line.substring(12, 18).toUpperCase();
                        concept = new Concept("ko:" + koAccession, MetaData.CV_KEGG, MetaData.CC_KEGG_ONTOLOGY);
                        koAccessionToKoConcept.put(koAccession.toUpperCase(), concept);
                        genes = new ObjectOpenHashSet<String>();
                        koConceptToGenes.put(concept, genes);
                    } else if (inDefinition || line.startsWith("DEFINITION")) {
                        inDefinition = true;
                        if (concept.getDescription() == null) {
                            concept.setDescription(line.substring(12, line.length()));
                        } else {
                            concept.setDescription(concept.getDescription() + line.substring(12, line.length()));
                        }
                    } else if (line.startsWith("NAME")) {
                        line = line.substring(12, line.length()).toUpperCase();
                        String[] result = FastSplit.fastSplit(line, FastSplit.COMMA, false);
                        for (int i = 0; i < result.length; i++) {
                            String name = result[i].trim();
                            ConceptName conceptName = new ConceptName(concept.getId(), name);
                            if (!koNamesToKoConcept.containsKey(name.toUpperCase()) && !ambiguousName.contains(name.toUpperCase())) {
                                koNamesToKoConcept.put(name.toUpperCase(), concept);
                            } else {
                                ambiguousName.add(name.toUpperCase());
                                koNamesToKoConcept.remove(name.toUpperCase());
                            }
                            if (concept.getConceptAccs() == null || concept.getConceptAccs().size() == 0) {
                                conceptName.setPreferred(true);
                            }
                            concept.getConceptNames().add(conceptName);
                        }
                    } else if (inDblinks || line.startsWith("DBLINKS")) {
                        inDefinition = false;
                        inDblinks = true;
                        line = line.substring(12, line.length());
                        String[] result = FastSplit.fastSplit(line, FastSplit.COLON, false);
                        if (result.length == 2) {
                            String db = result[0].trim();
                            String acc = result[1].trim().toUpperCase();
                            String[] accs = FastSplit.fastSplit(acc, FastSplit.SPACE, false);
                            for (String access : accs) {
                                ConceptAcc conceptAcc = new ConceptAcc(concept.getId(), access, db);
                                concept.getConceptAccs().add(conceptAcc);
                            }
                        }
                    } else if (inGenes || line.startsWith("GENES")) {
                        inDblinks = false;
                        inGenes = true;
                        line = line.substring(12, line.length());
                        line = removeBrackets(line);
                        String[] results = FastSplit.fastSplit(line, FastSplit.COLON, false);
                        if (results.length == 2) {
                            org = results[0];
                            line = results[1];
                        }
                        results = FastSplit.fastSplit(line.trim(), FastSplit.SPACE, false);
                        for (String gene : results) {
                            String genename = org.toLowerCase() + ":" + gene;
                            genes.add(genename.toUpperCase());
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Parser.propagateEventOccurred(new DataFileError(ioe.getMessage()));
        }
    }

    /**
	 * This remove everthing within brackets from a string
	 * 
	 * @param s - given string
	 * @return resulting string
	 */
    private static String removeBrackets(String s) {
        StringBuilder buf = new StringBuilder(s.trim());
        while (buf.indexOf("(") > -1) {
            buf.delete(buf.indexOf("("), buf.indexOf(")") + 1);
            if ((buf.indexOf(")") < buf.indexOf("(")) || (buf.indexOf("(") == -1 && buf.indexOf(")") > -1)) buf.delete(buf.indexOf(")"), buf.indexOf(")") + 1);
        }
        return buf.toString();
    }

    public Map<Concept, Set<String>> getKoConceptToGenes() {
        return koConceptToGenes;
    }

    public void finalise() {
        koConceptToGenes.clear();
    }

    public HashMap<String, Concept> getKoNamesToKoConcept() {
        return koNamesToKoConcept;
    }

    public HashMap<String, Concept> getKoAccessionToKoConcept() {
        return koAccessionToKoConcept;
    }
}
