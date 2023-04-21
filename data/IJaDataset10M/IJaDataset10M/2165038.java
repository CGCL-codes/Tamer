package uk.ac.manchester.cs.skos;

import org.semanticweb.skos.*;
import org.semanticweb.skos.extensions.SKOSLabelRelation;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLRuntimeException;
import java.net.URI;
import java.util.Set;

/**
 * Author: Simon Jupp<br>
 * Date: Sep 8, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSDataTypeImpl implements SKOSDataType {

    OWLDataType type;

    public SKOSDataTypeImpl(SKOSDataFactoryImpl skosDataFactory, URI uri) {
        this.type = skosDataFactory.getOWLDataType(uri);
    }

    public boolean isSKOSConcept() {
        return false;
    }

    public SKOSConcept asSKOSConcept() {
        throw new OWLRuntimeException("Not a SKOS Concept");
    }

    public boolean isSKOSConceptScheme() {
        return false;
    }

    public SKOSConceptScheme asSKOSConceptScheme() {
        throw new OWLRuntimeException("Not a SKOS Concept Scheme");
    }

    public boolean isSKOSLabelRelation() {
        return false;
    }

    public SKOSLabelRelation asSKOSLabelRelation() {
        throw new OWLRuntimeException("Not a SKOS Label Relation");
    }

    public boolean isSKOSResource() {
        return false;
    }

    public SKOSResource asSKOSResource() {
        throw new OWLRuntimeException("Not a SKOS Resource");
    }

    public URI getURI() {
        return type.getURI();
    }

    public Set<SKOSAnnotation> getSKOSAnnotations(SKOSDataset dataset) {
        return null;
    }

    public OWLDataType getAsOWLDataType() {
        return type;
    }

    public void accept(SKOSEntityVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(SKOSObjectVisitor visitor) {
        visitor.visit(this);
    }
}
