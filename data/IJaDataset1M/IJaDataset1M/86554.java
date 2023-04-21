package backend.parser.kegg.ko;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import backend.event.type.StatisticalOutput;
import backend.parser.kegg.Parser;
import backend.parser.kegg.sink.ConceptWriter;
import backend.parser.kegg.sink.Relation;
import backend.parser.kegg.util.Util;

/**
 * @author taubertj
 *
 */
public class KoRelationMerger {

    private Hashtable ko2Genes;

    public KoRelationMerger(Hashtable ko2Genes) {
        this.ko2Genes = ko2Genes;
    }

    public void merge() {
        Object[] array = ko2Genes.keySet().toArray();
        String[] kos = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            kos[i] = (String) array[i];
        }
        Arrays.sort(kos);
        for (int i = 0; i < kos.length; i++) {
            String kogene = kos[i];
            Object[] temp = ((HashSet) ko2Genes.get(kogene)).toArray();
            String[] genes = new String[temp.length];
            for (int j = 0; j < temp.length; j++) {
                genes[j] = (String) temp[j];
            }
            Arrays.sort(genes);
            for (int j = 0; j < genes.length; j++) {
                String gene = genes[j] + "_GE";
                if (!ConceptWriter.writtenConcepts.containsKey(gene)) {
                    Parser.propagateEventOccurred(new StatisticalOutput(gene + " not found in written concepts"));
                } else {
                    Relation relation_gene = new Relation(gene, kogene, "m_isp");
                    relation_gene.setFrom_element_of("KEGG");
                    relation_gene.setTo_element_of("KEGG");
                    Util.writeRelation(relation_gene);
                    String protein = gene.substring(0, gene.length() - 3) + "_PR";
                    if (ConceptWriter.writtenConcepts.containsKey(protein)) {
                        String koprotein = kogene.substring(0, kogene.length() - 3) + "_PR";
                        Relation relation_protein = new Relation(protein, koprotein, "m_isp");
                        relation_protein.setFrom_element_of("KEGG");
                        relation_protein.setTo_element_of("KEGG");
                        Util.writeRelation(relation_protein);
                    }
                    String enzyme = gene.substring(0, gene.length() - 3) + "_EN";
                    if (ConceptWriter.writtenConcepts.containsKey(enzyme)) {
                        String koenzyme = kogene.substring(0, kogene.length() - 3) + "_EN";
                        Relation relation_enzyme = new Relation(enzyme, koenzyme, "m_isp");
                        relation_enzyme.setFrom_element_of("KEGG");
                        relation_enzyme.setTo_element_of("KEGG");
                        Util.writeRelation(relation_enzyme);
                    }
                }
            }
        }
    }
}
