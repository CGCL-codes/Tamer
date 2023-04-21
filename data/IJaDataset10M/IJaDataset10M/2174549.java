package jade.content.onto;

import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

public class OntologyUtils {

    public static void exploreOntology(Ontology ontology) throws OntologyException {
        ontology.dump();
    }
}
