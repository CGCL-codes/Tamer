package it.cnr.stlab.xd.plugin.editor.delegates;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;

public class OWLModelDelegateAxiomFactory {

    OWLModelDelegate delegate;

    public OWLModelDelegateAxiomFactory(OWLModelDelegate delegate) {
        this.delegate = delegate;
    }

    private OWLOntologyManager getManager() {
        return delegate.getOWLOntologyManager();
    }

    private OWLOntology getOntology() {
        return delegate.getOWLOntology();
    }

    private OWLDataFactory getDataFactory() {
        return getManager().getOWLDataFactory();
    }

    /**
	 * Builds the correct axiom depending on the URI type;
	 * 
	 * @param subject
	 * @param connection
	 * @param target
	 * @return
	 */
    public OWLAxiom createConnectionAxiom(URI subject, URI connection, URI object) {
        OWLAxiom axiom = null;
        if (connection.equals(OWLRDFVocabulary.RDFS_SUBCLASS_OF.getURI())) {
            OWLDescription subj = getDataFactory().getOWLClass(subject);
            OWLDescription obj = getDataFactory().getOWLClass(object);
            axiom = getDataFactory().getOWLSubClassAxiom(subj, obj);
        } else if (connection.equals(OWLRDFVocabulary.RDFS_SUB_PROPERTY_OF.getURI())) {
            OWLObject sp = delegate.getOWLObject(subject);
            OWLObject op = delegate.getOWLObject(object);
            if (sp instanceof OWLObjectProperty && op instanceof OWLObjectProperty) {
                OWLObjectProperty subj = (OWLObjectProperty) sp;
                OWLObjectProperty obj = (OWLObjectProperty) op;
                axiom = getDataFactory().getOWLSubObjectPropertyAxiom(subj, obj);
            } else if (sp instanceof OWLDataProperty && op instanceof OWLDataProperty) {
                OWLDataProperty subj = (OWLDataProperty) sp;
                OWLDataProperty obj = (OWLDataProperty) op;
                axiom = getDataFactory().getOWLSubDataPropertyAxiom(subj, obj);
            } else throw new IllegalArgumentException("Invalid types for subject nd object!");
        } else if (connection.equals(OWLRDFVocabulary.OWL_EQUIVALENT_CLASS.getURI())) {
            OWLDescription subj = getDataFactory().getOWLClass(subject);
            OWLDescription obj = getDataFactory().getOWLClass(object);
            axiom = getDataFactory().getOWLEquivalentClassesAxiom(subj, obj);
        } else if (connection.equals(OWLRDFVocabulary.OWL_EQUIVALENT_OBJECT_PROPERTIES.getURI())) {
            OWLObjectProperty subj = getDataFactory().getOWLObjectProperty(subject);
            OWLObjectProperty obj = getDataFactory().getOWLObjectProperty(object);
            axiom = getDataFactory().getOWLEquivalentObjectPropertiesAxiom(subj, obj);
        } else if (connection.equals(OWLRDFVocabulary.OWL_EQUIVALENT_DATA_PROPERTIES.getURI())) {
            OWLDataProperty subj = getDataFactory().getOWLDataProperty(subject);
            OWLDataProperty obj = getDataFactory().getOWLDataProperty(object);
            axiom = getDataFactory().getOWLEquivalentDataPropertiesAxiom(subj, obj);
        } else if (connection.equals(OWLRDFVocabulary.OWL_DISJOINT_WITH.getURI())) {
            OWLObject s = delegate.getOWLObject(subject);
            OWLObject o = delegate.getOWLObject(object);
            if (s instanceof OWLClass && o instanceof OWLClass) {
                OWLClass subj = (OWLClass) s;
                OWLClass obj = (OWLClass) o;
                axiom = getDataFactory().getOWLDisjointClassesAxiom(subj, obj);
            } else if (s instanceof OWLObjectProperty && o instanceof OWLObjectProperty) {
                OWLObjectProperty subj = (OWLObjectProperty) s;
                OWLObjectProperty obj = (OWLObjectProperty) o;
                axiom = getDataFactory().getOWLDisjointObjectPropertiesAxiom(subj, obj);
            } else if (s instanceof OWLDataProperty && o instanceof OWLDataProperty) {
                OWLDataProperty subj = (OWLDataProperty) s;
                OWLDataProperty obj = (OWLDataProperty) o;
                axiom = getDataFactory().getOWLDisjointDataPropertiesAxiom(subj, obj);
            } else throw new IllegalArgumentException("Invalid types for subject nd object!");
        } else if (connection.equals(OWLRDFVocabulary.OWL_INVERSE_OF.getURI())) {
            OWLObjectProperty subj = getDataFactory().getOWLObjectProperty(subject);
            OWLObjectProperty obj = getDataFactory().getOWLObjectProperty(object);
            axiom = getDataFactory().getOWLInverseObjectPropertiesAxiom(subj, obj);
        } else if (connection.equals(OWLRDFVocabulary.RDFS_DOMAIN.getURI())) {
            OWLObject s = delegate.getOWLObject(subject);
            OWLObject o = delegate.getOWLObject(object);
            if (s instanceof OWLObjectProperty && o instanceof OWLClass) {
                OWLObjectProperty subj = (OWLObjectProperty) s;
                OWLClass obj = (OWLClass) o;
                axiom = getDataFactory().getOWLObjectPropertyDomainAxiom(subj, obj);
            } else if (s instanceof OWLDataProperty && o instanceof OWLClass) {
                OWLDataProperty subj = (OWLDataProperty) s;
                OWLClass obj = (OWLClass) o;
                axiom = getDataFactory().getOWLDataPropertyDomainAxiom(subj, obj);
            } else throw new IllegalArgumentException("Invalid types for subject and object!");
        } else if (connection.equals(OWLRDFVocabulary.RDFS_RANGE.getURI())) {
            OWLObject s = delegate.getOWLObject(subject);
            OWLObject o = delegate.getOWLObject(object);
            if (s instanceof OWLObjectProperty && o instanceof OWLClass) {
                OWLObjectProperty subj = (OWLObjectProperty) s;
                OWLClass obj = (OWLClass) o;
                axiom = getDataFactory().getOWLObjectPropertyRangeAxiom(subj, obj);
            } else throw new IllegalArgumentException("Invalid types for subject and object!");
        } else if (connection.equals(OWLRDFVocabulary.RDF_TYPE.getURI())) {
            OWLObject s = delegate.getOWLObject(subject);
            OWLObject o = delegate.getOWLObject(object);
            if (s instanceof OWLIndividual && o instanceof OWLClass) {
                OWLIndividual subj = (OWLIndividual) s;
                OWLClass obj = (OWLClass) o;
                axiom = getDataFactory().getOWLClassAssertionAxiom(subj, obj);
            } else throw new IllegalArgumentException("Invalid types for subject nd object!");
        } else if (connection.equals(OWLRDFVocabulary.OWL_SAME_AS.getURI())) {
            OWLObject s = delegate.getOWLObject(subject);
            OWLObject o = delegate.getOWLObject(object);
            if (s instanceof OWLIndividual && o instanceof OWLIndividual) {
                OWLIndividual subj = (OWLIndividual) s;
                OWLIndividual obj = (OWLIndividual) o;
                Set<OWLIndividual> set = new HashSet<OWLIndividual>();
                set.add(subj);
                set.add(obj);
                axiom = getDataFactory().getOWLSameIndividualsAxiom(set);
            } else throw new IllegalArgumentException("Invalid types for subject nd object!");
        } else if (connection.equals(OWLRDFVocabulary.OWL_DIFFERENT_FROM.getURI())) {
            OWLObject s = delegate.getOWLObject(subject);
            OWLObject o = delegate.getOWLObject(object);
            if (s instanceof OWLIndividual && o instanceof OWLIndividual) {
                OWLIndividual subj = (OWLIndividual) s;
                OWLIndividual obj = (OWLIndividual) o;
                axiom = getDataFactory().getOWLDifferentIndividualsAxiom(subj, obj);
            } else throw new IllegalArgumentException("Invalid types for subject nd object!");
        } else {
            OWLIndividual subjectInd = getDataFactory().getOWLIndividual(subject);
            OWLIndividual objectInd = getDataFactory().getOWLIndividual(object);
            OWLObjectPropertyExpression expression = getDataFactory().getOWLObjectProperty(connection);
            axiom = getDataFactory().getOWLObjectPropertyAssertionAxiom(subjectInd, expression, objectInd);
        }
        return axiom;
    }
}
