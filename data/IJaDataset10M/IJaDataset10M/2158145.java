package backend.parser.kegg.gene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import backend.event.type.DataFileError;
import backend.parser.kegg.Parser;
import backend.parser.kegg.data.Entry;
import backend.parser.kegg.sink.Concept;
import backend.parser.kegg.sink.ConceptAcc;
import backend.parser.kegg.sink.ConceptName;
import backend.parser.kegg.sink.ConceptWriter;
import backend.parser.kegg.sink.Relation;
import backend.parser.kegg.sink.Sequence;
import backend.parser.kegg.util.Util;

/**
 * @author taubertj
 *  
 */
public class GeneFileParser {

    private boolean DEBUG = false;

    private String pathToGenes;

    private Hashtable gene2Entries;

    private Hashtable mapping;

    private Hashtable<String, Hashtable<String, Boolean>> genesToParse;

    private Hashtable<String, Hashtable<String, Boolean>> clashedGenes;

    public GeneFileParser(String pathToGenes, Hashtable gene2Entries, Hashtable mapping) {
        this.pathToGenes = pathToGenes;
        this.gene2Entries = gene2Entries;
        this.mapping = mapping;
        this.genesToParse = new Hashtable<String, Hashtable<String, Boolean>>();
        this.clashedGenes = new Hashtable<String, Hashtable<String, Boolean>>();
    }

    /**
	 * checks consitancy of each given gene name
	 *
	 */
    public void parse() {
        Enumeration enu = gene2Entries.keys();
        while (enu.hasMoreElements()) {
            String gene = (String) enu.nextElement();
            HashSet entries = (HashSet) gene2Entries.get(gene);
            Iterator it = entries.iterator();
            boolean isEnzyme = false;
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (entry.getReaction() != null) {
                    isEnzyme = true;
                    break;
                }
            }
            String[] parts = gene.split(":");
            if (mapping.get(parts[0]) == null) {
                if (DEBUG) System.out.println(parts[0] + ":" + parts[1]);
                if (clashedGenes.get(parts[0]) == null) clashedGenes.put(parts[0], new Hashtable<String, Boolean>());
                Hashtable<String, Boolean> genes = (Hashtable<String, Boolean>) clashedGenes.get(parts[0]);
                genes.put(parts[1], new Boolean(isEnzyme));
            } else {
                if (genesToParse.get(parts[0]) == null) genesToParse.put(parts[0], new Hashtable<String, Boolean>());
                Hashtable<String, Boolean> genes = (Hashtable<String, Boolean>) genesToParse.get(parts[0]);
                genes.put(parts[1], new Boolean(isEnzyme));
            }
        }
        parseGeneFiles();
    }

    /**
	 * parses the gene flat files
	 *
	 */
    public void parseGeneFiles() {
        Enumeration enu = genesToParse.keys();
        while (enu.hasMoreElements()) {
            String org = (String) enu.nextElement();
            Hashtable<String, Boolean> genes = (Hashtable<String, Boolean>) genesToParse.get(org);
            HashSet<String> foundGenes = new HashSet<String>();
            try {
                String path = pathToGenes + System.getProperty("file.separator") + mapping.get(org);
                BufferedReader reader = new BufferedReader(new FileReader(path));
                while (reader.ready()) {
                    String line = reader.readLine();
                    if (line.startsWith("ENTRY")) {
                        line = line.substring(12, line.length());
                        line = line.substring(0, line.indexOf(" "));
                        if (Parser.importAllSequences || genes.containsKey(line)) {
                            foundGenes.add(line);
                            boolean isEnzyme = false;
                            if (genes.containsKey(line)) isEnzyme = ((Boolean) genes.get(line)).booleanValue();
                            parseConceptFromFile(org, line, isEnzyme, reader);
                        }
                    }
                }
                reader.close();
            } catch (IOException ioe) {
                Parser.propagateEventOccurred(new DataFileError(ioe.getMessage()));
            }
            HashSet<String> geneKeys = new HashSet<String>(genes.keySet());
            if (!foundGenes.containsAll(geneKeys)) {
                Iterator it = geneKeys.iterator();
                while (it.hasNext()) {
                    String gene = (String) it.next();
                    if (!foundGenes.contains(gene)) {
                        if (clashedGenes.get(org) == null) {
                            clashedGenes.put(org, new Hashtable<String, Boolean>());
                        }
                        Hashtable<String, Boolean> cgenes = (Hashtable<String, Boolean>) clashedGenes.get(org);
                        cgenes.put(gene, Boolean.FALSE);
                    }
                }
            }
        }
        enu = clashedGenes.keys();
        while (enu.hasMoreElements()) {
            String org = (String) enu.nextElement();
            Hashtable genes = (Hashtable) clashedGenes.get(org);
            Iterator it = genes.keySet().iterator();
            while (it.hasNext()) {
                String gene = (String) it.next();
                String id = org + ":" + gene;
                if (DEBUG) System.out.println(id);
                Concept concept = new Concept(id + "_GE");
                concept.setElement_of("KEGG");
                concept.setOf_type_fk("Gene");
                concept.setDescription("abstract gene concept");
                Util.writeConcept(concept);
            }
        }
    }

    /**
	 * parse everything belonging to a gene concept from file
	 * 
	 * @param org 
	 * @param gene
	 * @param reader
	 */
    public void parseConceptFromFile(String org, String gene, boolean isEnzyme, BufferedReader reader) {
        String id = org + ":" + gene;
        Concept concept_gene = new Concept(id + "_GE");
        concept_gene.setElement_of("KEGG");
        concept_gene.setOf_type_fk("Gene");
        String link = "http://www.genome.jp/dbget-bin/www_bget?" + org + "+" + gene;
        concept_gene.setUrl(link);
        Sequence ntseq = new Sequence(id + "_NT");
        ntseq.setConcept_fk(concept_gene.getId());
        ntseq.setSequence_type_fk("NA");
        Concept concept_protein = new Concept(id + "_PR");
        concept_protein.setElement_of("KEGG");
        concept_protein.setOf_type_fk("Protein");
        concept_protein.setDescription("derived protein");
        Sequence aaseq = new Sequence(id + "_AA");
        aaseq.setConcept_fk(concept_protein.getId());
        aaseq.setSequence_type_fk("AA");
        Relation en_by = new Relation(concept_protein.getId(), concept_gene.getId(), "en_by");
        en_by.setFrom_element_of("KEGG");
        en_by.setTo_element_of("KEGG");
        Concept concept_enzyme = new Concept(id + "_EN");
        concept_enzyme.setElement_of("KEGG");
        concept_enzyme.setOf_type_fk("Enzyme");
        concept_enzyme.setDescription("derived enzyme");
        Relation is_a = new Relation(concept_enzyme.getId(), concept_protein.getId(), "is_a");
        is_a.setFrom_element_of("KEGG");
        is_a.setTo_element_of("KEGG");
        try {
            boolean inDefinition = false;
            boolean inDblinks = false;
            boolean inAAseq = false;
            boolean inNTseq = false;
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.indexOf("///") > -1) {
                    Util.writeConcept(concept_gene);
                    if (ntseq.getSeq() != null) {
                        Util.writeSequence(ntseq);
                    }
                    if (aaseq.getSeq() != null) {
                        Util.writeConcept(concept_protein);
                        Util.writeRelation(en_by);
                        Util.writeSequence(aaseq);
                    }
                    if (isEnzyme) {
                        if (aaseq.getSeq() == null) {
                            Util.writeConcept(concept_protein);
                            Util.writeRelation(en_by);
                        }
                        Util.writeConcept(concept_enzyme);
                        Util.writeRelation(is_a);
                        if (concept_gene.getDescription().indexOf("[EC:") > 1) {
                            String d = concept_gene.getDescription();
                            String ecs = d.substring(d.indexOf("[EC:") + 4, d.length());
                            ecs = ecs.substring(0, ecs.indexOf("]"));
                            String[] result = ecs.split(" ");
                            for (int i = 0; i < result.length; i++) {
                                Concept ec = new Concept(result[i]);
                                if (!ConceptWriter.writtenConcepts.containsKey(ec.getId())) {
                                    ec.setElement_of("KEGG");
                                    ec.setOf_type_fk("EC");
                                    ec.setDescription("parsed ec from definition");
                                    ConceptName cn = new ConceptName(ec.getId(), ec.getId(), "KEGG");
                                    if (ec.conceptNames == null) {
                                        ec.conceptNames = new HashSet<ConceptName>();
                                        cn.setPreferred(true);
                                    }
                                    ec.conceptNames.add(cn);
                                    ConceptAcc ca = new ConceptAcc(ec.getId(), ec.getId(), "EC");
                                    if (ec.conceptAccs == null) ec.conceptAccs = new HashSet<ConceptAcc>();
                                    ec.conceptAccs.add(ca);
                                    Util.writeConcept(ec);
                                }
                                Relation cat_c = new Relation(concept_enzyme.getId(), ec.getId(), "cat_c");
                                cat_c.setFrom_element_of("KEGG");
                                cat_c.setTo_element_of("KEGG");
                                Util.writeRelation(cat_c);
                            }
                        }
                    }
                    return;
                }
                if (line.length() > 12) {
                    if (!line.substring(0, 11).trim().equals("")) {
                        inDefinition = false;
                        inDblinks = false;
                        inAAseq = false;
                        inNTseq = false;
                    }
                    if (inDefinition || line.startsWith("DEFINITION")) {
                        inDefinition = true;
                        if (concept_gene.getDescription() == null) {
                            concept_gene.setDescription(line.substring(12, line.length()));
                        } else {
                            concept_gene.setDescription(concept_gene.getDescription() + " " + line.substring(12, line.length()));
                        }
                    } else if (line.startsWith("NAME")) {
                        line = line.substring(12, line.length());
                        String[] result = line.split(",");
                        for (int i = 0; i < result.length; i++) {
                            String name = result[i].trim();
                            ConceptName conceptNameGE = new ConceptName(concept_gene.getId(), name, "KEGG");
                            if (concept_gene.conceptNames == null) {
                                concept_gene.conceptNames = new HashSet<ConceptName>();
                                conceptNameGE.setPreferred(true);
                            }
                            concept_gene.conceptNames.add(conceptNameGE);
                            ConceptName conceptNamePR = new ConceptName(concept_protein.getId(), name, "KEGG");
                            if (concept_protein.conceptNames == null) {
                                concept_protein.conceptNames = new HashSet<ConceptName>();
                                conceptNamePR.setPreferred(true);
                            }
                            concept_protein.conceptNames.add(conceptNamePR);
                            ConceptName conceptNameEN = new ConceptName(concept_enzyme.getId(), name, "KEGG");
                            if (concept_enzyme.conceptNames == null) {
                                concept_enzyme.conceptNames = new HashSet<ConceptName>();
                                conceptNameEN.setPreferred(true);
                            }
                            concept_enzyme.conceptNames.add(conceptNameEN);
                        }
                    } else if (inDblinks || line.startsWith("DBLINKS")) {
                        inDblinks = true;
                        line = line.substring(12, line.length());
                        String[] result = line.split(":");
                        if (result.length == 2) {
                            String db = result[0];
                            String acc = result[1].replaceAll("\\ +", " ").trim();
                            String[] accs = acc.split(" ");
                            for (int i = 0; i < accs.length; i++) {
                                ConceptAcc conceptAccGE = new ConceptAcc(concept_gene.getId(), accs[i], db);
                                if (concept_gene.conceptAccs == null) concept_gene.conceptAccs = new HashSet<ConceptAcc>();
                                concept_gene.conceptAccs.add(conceptAccGE);
                                ConceptAcc conceptAccPR = new ConceptAcc(concept_protein.getId(), accs[i], db);
                                if (concept_protein.conceptAccs == null) concept_protein.conceptAccs = new HashSet<ConceptAcc>();
                                concept_protein.conceptAccs.add(conceptAccPR);
                                ConceptAcc conceptAccEN = new ConceptAcc(concept_enzyme.getId(), accs[i], db);
                                if (concept_enzyme.conceptAccs == null) concept_enzyme.conceptAccs = new HashSet<ConceptAcc>();
                                concept_enzyme.conceptAccs.add(conceptAccEN);
                            }
                        }
                    } else if (inAAseq || line.startsWith("AASEQ")) {
                        inAAseq = true;
                        if (!line.startsWith("AASEQ")) {
                            line = line.substring(12, line.length());
                            if (aaseq.getSeq() == null) aaseq.setSeq(line); else aaseq.setSeq(aaseq.getSeq() + line);
                        }
                    } else if (inNTseq || line.startsWith("NTSEQ")) {
                        inNTseq = true;
                        if (!line.startsWith("NTSEQ")) {
                            line = line.substring(12, line.length());
                            if (ntseq.getSeq() == null) ntseq.setSeq(line); else ntseq.setSeq(ntseq.getSeq() + line);
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            Parser.propagateEventOccurred(new DataFileError(ioe.getMessage()));
        }
    }
}
