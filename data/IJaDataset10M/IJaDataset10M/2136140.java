package edu.unika.aifb.rules.rules.feature.owlclass;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.semanticweb.kaon2.api.owl.elements.OWLClass;
import org.semanticweb.kaon2.api.owl.elements.ObjectCardinality;
import org.semanticweb.kaon2.api.owl.elements.Description;
import edu.unika.aifb.rules.input.MyOntology;
import edu.unika.aifb.rules.input.Structure;
import edu.unika.aifb.rules.rules.feature.Feature;

/**
 * @author Marc Ehrig
 *
 */
public class OWLClassObjectCardinality implements Feature {

    public Object get(Object object, Structure structure) {
        try {
            MyOntology ontology = (MyOntology) structure;
            OWLClass concept = (OWLClass) object;
            Set descriptions = concept.getSuperDescriptions(ontology.ontology);
            Set specialDescriptions = new HashSet();
            Iterator iter = descriptions.iterator();
            while (iter.hasNext()) {
                Description next = (Description) iter.next();
                if (next instanceof ObjectCardinality) {
                    specialDescriptions.add(next);
                }
            }
            return specialDescriptions;
        } catch (Exception e) {
            return null;
        }
    }
}
