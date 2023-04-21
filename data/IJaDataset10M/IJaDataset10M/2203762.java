package edu.unika.aifb.rules.rules.feature.special;

import java.util.Set;
import org.semanticweb.kaon2.api.owl.elements.OWLClass;
import org.semanticweb.kaon2.api.owl.elements.ObjectProperty;
import edu.unika.aifb.rules.input.MyOntology;
import edu.unika.aifb.rules.input.Structure;

/**
 * @author Marc Ehrig
 *
 */
public class OWLClassObjectPropertiesFromSpecial extends FeatureSpecialImpl {

    private ObjectProperty property;

    public OWLClassObjectPropertiesFromSpecial(Object propertyT) {
        property = (ObjectProperty) propertyT;
    }

    public Object get(Object object, Structure structure) {
        try {
            MyOntology ontology = (MyOntology) structure;
            OWLClass concept = (OWLClass) object;
            Set properties = concept.getObjectPropertiesFrom(ontology.ontology);
            return new Boolean(properties.contains(property));
        } catch (Exception e) {
            return null;
        }
    }
}
