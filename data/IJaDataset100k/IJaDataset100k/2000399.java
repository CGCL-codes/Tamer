package edu.unika.aifb.rules.rules.feature.entity;

import org.semanticweb.kaon2.api.StringWithLanguage;
import org.semanticweb.kaon2.api.owl.elements.OWLEntity;
import edu.unika.aifb.rules.input.MyOntology;
import edu.unika.aifb.rules.input.Structure;
import edu.unika.aifb.rules.rules.feature.Feature;

/**
 * @author Marc Ehrig
 *
 */
public class EntityLabel implements Feature {

    public Object get(Object object, Structure structure) {
        MyOntology ontology = (MyOntology) structure;
        OWLEntity entity = (OWLEntity) object;
        try {
            String label = (String) entity.getAnnotationValue(ontology.ontology, ontology.owlxlabel);
            return label;
        } catch (Exception e) {
            try {
                StringWithLanguage label2 = (StringWithLanguage) entity.getAnnotationValue(ontology.ontology, ontology.owlxlabel);
                return label2.getString();
            } catch (Exception e2) {
                return null;
            }
        }
    }
}
