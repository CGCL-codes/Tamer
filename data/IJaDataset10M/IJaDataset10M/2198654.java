package edu.unika.aifb.rules.agenda;

import java.util.*;
import org.semanticweb.kaon2.api.Ontology;
import org.semanticweb.kaon2.api.owl.elements.Individual;
import edu.unika.aifb.rules.input.MyOntology;
import edu.unika.aifb.rules.input.Structure;
import edu.unika.aifb.rules.util.UserInterface;

/**
 * All instances are compared with all 
 * other instances, resulting in n x m comparisons.
 * 
 * @author Marc Ehrig
 */
public class CompleteInstanceAgenda extends AgendaImpl {

    private int position = 0;

    private int total = 0;

    public void create(Structure structure, boolean internaltooT) {
        internaltoo = internaltooT;
        try {
            MyOntology myOntology = (MyOntology) structure;
            Ontology ontology = myOntology.ontology;
            list = new HashSet();
            SortedMap entityList = new TreeMap();
            Set set1 = ontology.createEntityRequest(Individual.class).getAll();
            Set set2 = new HashSet();
            set2.addAll(set1);
            add(set1, set2);
        } catch (Exception e) {
            UserInterface.print(e.getMessage());
        }
    }
}
