package proveJOFS;

import java.io.File;
import model.JOFSModel;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;

/**
 * Questa classe serve per usare direttamente il modello Jena contenuto in un JOFSModel
 * @author mashiro
 *
 */
public class proveJont {

    private static String instances = "http://www.raymant.altervista.org/owl/instances.owl#";

    public static void main(String[] args) {
        JOFSModel jofs;
        File f = new File("SupportFile/Ontologie/instances.owl");
        try {
            jofs = new JOFSModel("prove_jofs");
            OntModel om = jofs.getJOntModel();
            Individual ind = om.getIndividual(instances + "italian_text_1");
            for (StmtIterator i = ind.listProperties(); i.hasNext(); ) {
                StatementImpl p = (StatementImpl) i.next();
                System.out.println(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
