package org.semanticweb.skosapibinding;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.*;
import org.semanticweb.skos.*;
import org.semanticweb.skos.extensions.SKOSLabelRelation;
import uk.ac.manchester.cs.skos.SKOSRDFVocabulary;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Simon Jupp<br>
 * Date: Aug 28, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSReasoner {

    SKOSManager manager;

    OWLReasoner reasoner;

    OWLOntology ontology;

    SKOSDataset dataSet;

    SKOStoOWLConverter skos2owlconverter;

    OWLOntology skosOntology;

    static final String SKOSURI = "http://www.w3.org/2009/08/skos-reference/skos-owl1-dl.rdf";

    public SKOSReasoner(SKOSManager manager, OWLReasoner reasoner) {
        this.manager = manager;
        this.reasoner = reasoner;
        this.skos2owlconverter = new SKOStoOWLConverter();
        try {
            this.skosOntology = manager.getOWLManger().loadOntology(URI.create(SKOSURI));
        } catch (OWLOntologyCreationException e) {
            System.err.println("Can't load SKOS data model from SKOSURI");
            e.printStackTrace();
        }
        classify();
    }

    public SKOSReasoner(SKOSManager manager, OWLReasoner reasoner, OWLOntology skosOntology) {
        this.manager = manager;
        this.reasoner = reasoner;
        this.skos2owlconverter = new SKOStoOWLConverter();
        this.skosOntology = skosOntology;
        classify();
    }

    public void classify() {
        Set<OWLOntology> ontologies = new HashSet<OWLOntology>();
        ontologies.addAll(manager.getOWLManger().getOntologies());
        ontologies.add(skosOntology);
        try {
            reasoner.loadOntologies(ontologies);
            reasoner.classify();
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }
    }

    public void loadDataset(SKOSDataset dataSet) throws OWLReasonerException {
        this.ontology = skos2owlconverter.getAsOWLOntology(dataSet);
        reasoner.loadOntologies(Collections.singleton(ontology));
    }

    public Set<SKOSConceptScheme> getSKOSConceptSchemes() {
        OWLClass cls = manager.getOWLManger().getOWLDataFactory().getOWLClass(SKOSRDFVocabulary.CONCEPTSCHEME.getURI());
        Set<SKOSConceptScheme> conSet = new HashSet<SKOSConceptScheme>();
        try {
            for (OWLIndividual ind : reasoner.getIndividuals(cls, false)) {
                conSet.add(manager.getSKOSDataFactory().getSKOSConceptScheme(ind.getURI()));
            }
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }
        return conSet;
    }

    public Set<SKOSConcept> getSKOSConcepts() {
        OWLClass cls = manager.getOWLManger().getOWLDataFactory().getOWLClass(SKOSRDFVocabulary.CONCEPT.getURI());
        Set<SKOSConcept> conSet = new HashSet<SKOSConcept>();
        try {
            for (OWLIndividual ind : reasoner.getIndividuals(cls, false)) {
                conSet.add(manager.getSKOSDataFactory().getSKOSConcept(ind.getURI()));
            }
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }
        return conSet;
    }

    public Set<SKOSObjectRelationAssertion> getSKOSObjectRelationAssertions(SKOSEntity skosEntity) {
        return null;
    }

    public Set<SKOSObjectRelationAssertion> getReferencingSKOSObjectRelationAssertions(SKOSEntity skosEntity) {
        return null;
    }

    public Set<SKOSConcept> getTopConcepts(SKOSConceptScheme scheme) {
        return getSKOSRelatedConcept(scheme, SKOSRDFVocabulary.TOPCONCEPTOF.getURI());
    }

    public Set<SKOSConcept> getConceptsInScheme(SKOSConceptScheme scheme) {
        return getSKOSRelatedConcept(scheme, SKOSRDFVocabulary.INSCHEME.getURI());
    }

    public Set<SKOSConcept> getSKOSRelatedConcept(SKOSEntity concept, URI uri) {
        OWLIndividual ind = skos2owlconverter.getAsOWLIndiviudal(concept);
        OWLObjectProperty prop = manager.getOWLManger().getOWLDataFactory().getOWLObjectProperty(uri);
        Set<SKOSConcept> conSet = new HashSet<SKOSConcept>();
        try {
            for (OWLIndividual relInd : reasoner.getRelatedIndividuals(ind, prop)) {
                conSet.add(manager.getSKOSDataFactory().getSKOSConcept(relInd.getURI()));
            }
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }
        return conSet;
    }

    public Set<SKOSConcept> getSKOSNarrowerConcepts(SKOSConcept skosConcept) {
        return getSKOSRelatedConcept(skosConcept, SKOSRDFVocabulary.NARROWER.getURI());
    }

    public Set<SKOSConcept> getSKOSRelatedConcepts(SKOSConcept concept) {
        return getSKOSRelatedConcept(concept, SKOSRDFVocabulary.RELATED.getURI());
    }

    public Set<SKOSConcept> getSKOSBroaderConcepts(SKOSConcept skosConcept) {
        return getSKOSRelatedConcept(skosConcept, SKOSRDFVocabulary.BROADER.getURI());
    }

    public Set<SKOSConcept> getSKOSBroaderTransitiveConcepts(SKOSConcept concept) {
        return getSKOSRelatedConcept(concept, SKOSRDFVocabulary.BROADERTRANS.getURI());
    }

    public Set<SKOSConcept> getSKOSNarrowerTransitiveConcepts(SKOSConcept concept) {
        return getSKOSRelatedConcept(concept, SKOSRDFVocabulary.NARROWERTRANS.getURI());
    }

    public Set<SKOSConcept> getSKOSSemanticRelatedConcepts(SKOSConcept concept) {
        return getSKOSRelatedConcept(concept, SKOSRDFVocabulary.SEMANTICRELATION.getURI());
    }

    public Set<SKOSConcept> getSKOSRelatedMatchConcepts(SKOSConcept concept) {
        return getSKOSRelatedConcept(concept, SKOSRDFVocabulary.RELATEDMATCH.getURI());
    }

    public Set<SKOSConcept> getSKOSNarrowMatchConcepts(SKOSConcept concept) {
        return getSKOSRelatedConcept(concept, SKOSRDFVocabulary.NARROWMATCH.getURI());
    }

    public Set<SKOSConcept> getSKOSBroadMatchConcepts(SKOSConcept concept) {
        return getSKOSRelatedConcept(concept, SKOSRDFVocabulary.BROADMATCH.getURI());
    }

    public Set<SKOSDataRelationAssertion> getSKOSDataRelationAssertions(SKOSEntity skosEntity) {
        return null;
    }

    public Set<SKOSLabelRelation> getSKOSLabelRelations(SKOSConcept concept) {
        return null;
    }

    public Set<SKOSUntypedLiteral> getSKOSPrefLabel(SKOSConcept skosConcept) {
        return null;
    }

    public Set<SKOSUntypedLiteral> getSKOSAltLabel(SKOSConcept skosConcept) {
        return null;
    }

    public Set<SKOSUntypedLiteral> getSKOSHiddenLabel(SKOSConcept skosConcept) {
        return null;
    }

    public Set<SKOSAnnotation> getSKOSDefinition(SKOSConcept skosConcept) {
        return null;
    }

    public Set<SKOSAnnotation> getSKOSAnnotation(SKOSConcept skosConcept, URI annotationURI) {
        return null;
    }
}
