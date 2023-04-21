package de.unikoblenz.isweb.xcosima.annotation;

import org.openrdf.model.URI;
import de.unikoblenz.isweb.xcosima.aprocessor.OntologyClass;
import de.unikoblenz.isweb.xcosima.dns.Concept;
import de.unikoblenz.isweb.xcosima.dns.Description;
import de.unikoblenz.isweb.xcosima.dns.Endurant;
import de.unikoblenz.isweb.xcosima.dns.Particular;
import de.unikoblenz.isweb.xcosima.dns.Situation;
import de.unikoblenz.isweb.xcosima.ontologies.ONT;

@OntologyClass(ONT.COMM_C_semantic_annotation)
public class SemanticAnnotation extends Situation {

    public SemanticAnnotation() {
        super();
    }

    /**
	 * @param type
	 */
    public SemanticAnnotation(Particular annotatedEntity, URI semanticLabel) {
        super();
        Concept label = new Concept(semanticLabel);
        constructAnnotation(annotatedEntity, label);
    }

    public SemanticAnnotation(Particular annotatedEntity, Concept label) {
        super();
        constructAnnotation(annotatedEntity, label);
    }

    protected void constructAnnotation(Particular annotatedEntity, Endurant label) {
        description = new Method();
        setSettingFor(annotatedEntity);
        setSettingFor(label);
        SemanticLabelRole labelRole = new SemanticLabelRole();
        label.addClassifier(labelRole);
        description.addConcept(labelRole);
        if (annotatedEntity instanceof Endurant) {
            AnnotatedDataRole role = new AnnotatedDataRole();
            ((Endurant) annotatedEntity).addRole(role);
            description.addConcept(role);
        } else {
            Concept<Particular> annotatedEntityC = new AnnotatedEntity();
            annotatedEntity.addClassifier(annotatedEntityC);
            description.addConcept(annotatedEntityC);
        }
    }

    @OntologyClass(ONT.COMM_C_method)
    public class Method extends Description {

        public Method() {
            super();
        }
    }
}
