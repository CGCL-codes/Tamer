package de.fzi.harmonia.basematcher.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * A helper class for base matcher that use annotation properties.
 * 
 * @author bock
 *
 */
public class AnnotationPropertyHelper {

    private Log logger = LogFactory.getLog(AnnotationPropertyHelper.class);

    /**
	 * Retrieves the value of an annotation property of an ontological entity.
	 * The annotation property must be given as annotation property object.
	 * The value must be an <code>OWLLiteral</code>. If this is not the case,
	 * the value returned is the empty string.
	 * 
	 * @param annotProperty The annotation property.
	 * @param entity The <code>OWLEntity</code> that is annotated.
	 * @param ontology The ontology containing the entity.
	 * @return The annotation value in String representation.
	 */
    public static String getAnnotationLiteralValue(OWLAnnotationProperty annotProperty, OWLEntity entity, OWLOntology ontology) {
        String annotationValue = null;
        if (entity != null) for (OWLAnnotation annot : entity.getAnnotations(ontology, annotProperty)) {
            if (annot.getValue() instanceof OWLLiteral) {
                OWLLiteral val = (OWLLiteral) annot.getValue();
                annotationValue = val.getLiteral();
                int splitIndex = annotationValue.indexOf("^^");
                if (splitIndex >= 0) annotationValue = annotationValue.substring(0, splitIndex);
                splitIndex = annotationValue.indexOf("@");
                if (splitIndex >= 0) annotationValue = annotationValue.substring(0, splitIndex);
            }
        }
        return annotationValue;
    }

    /**
	 * Retrieves all values of an annotation property of an ontological entity.
	 * The annotation property must be given as annotation property object.
	 * The values must be of type <code>OWLLiteral</code>. If this is not the case,
	 * the value returned is a empty Set.
	 * 
	 * @param annotProperty The annotation property.
	 * @param entity The <code>OWLEntity</code> that is annotated.
	 * @param ontology The ontology containing the entity.
	 * @return The annotation values as a Set of Strings.
	 * 
	 */
    public static Set<String> getAllAnnotationLiteralValue(OWLAnnotationProperty annotProperty, OWLEntity entity, OWLOntology ontology) {
        Set<String> annotationValue = new HashSet<String>();
        if (entity != null) for (OWLAnnotation annot : entity.getAnnotations(ontology, annotProperty)) {
            if (annot.getValue() instanceof OWLLiteral) {
                String val = ((OWLLiteral) annot.getValue()).getLiteral();
                int splitIndex = val.indexOf("^^");
                if (splitIndex >= 0) val = val.substring(0, splitIndex);
                splitIndex = val.indexOf("@");
                if (splitIndex >= 0) val = val.substring(0, splitIndex);
                annotationValue.add(val);
            }
        }
        return annotationValue;
    }

    /**
     * Retrieves the IRI values of an annotation property of an ontological entity.
     * The annotation property must be given as annotation property object.
     * The value must be of type <code>IRI</code>. If this is not the case, 
     * a empty list is returned.
     * 
     * @param annotProperty The annotation property.
     * @param entity The <code>OWLEntity</code> that is annotated.
     * @param ontology The ontology containing the entity.
     * @return A list of all annotation values in <code>IRI</code> representation.
     */
    public static List<IRI> getAnnotationIRIValue(OWLAnnotationProperty annotProperty, OWLEntity entity, OWLOntology ontology) {
        List<IRI> annotationValue = new ArrayList<IRI>();
        for (OWLAnnotation annot : entity.getAnnotations(ontology, annotProperty)) {
            if (annot.getValue() instanceof IRI) {
                annotationValue.add((IRI) annot.getValue());
            }
        }
        return annotationValue;
    }
}
