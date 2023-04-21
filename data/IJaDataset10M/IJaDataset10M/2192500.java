package de.ifgi.simcat2.plugin.reasoner.proxy;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLProperty;
import org.semanticweb.owl.model.OWLPropertyExpression;
import org.semanticweb.owl.model.OWLRuntimeException;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.owlapi.PelletLoader;
import org.mindswap.pellet.owlapi.PelletReasonerFactory;
import org.mindswap.pellet.owlapi.Reasoner;
import aterm.ATermAppl;

/**
 * @author johannes 
 * is only a proxy and delegates methods to pellet reasoner
 *
 */
public class SimCatProxyReasoner implements SimOWLReasonerInterface {

    private Reasoner pelletReasoner;

    public SimCatProxyReasoner(OWLOntologyManager owlOntologyManager) {
        this.pelletReasoner = new PelletReasonerFactory().createReasoner(owlOntologyManager);
    }

    public void classify() {
        pelletReasoner.classify();
    }

    public void clearOntologies() {
        pelletReasoner.clearOntologies();
    }

    public OWLAxiom convertAxiom(ATermAppl term) {
        return pelletReasoner.convertAxiom(term);
    }

    public Set<OWLAxiom> convertAxioms(Set<ATermAppl> arg0) throws OWLRuntimeException {
        return pelletReasoner.convertAxioms(arg0);
    }

    public void dispose() {
        pelletReasoner.dispose();
    }

    public boolean equals(Object obj) {
        return pelletReasoner.equals(obj);
    }

    public Set<OWLClass> getAllEquivalentClasses(OWLDescription c) {
        return pelletReasoner.getAllEquivalentClasses(c);
    }

    public Set<Set<OWLClass>> getAncestorClasses(OWLDescription c) {
        return pelletReasoner.getAncestorClasses(c);
    }

    public Set<Set<OWLDataProperty>> getAncestorProperties(OWLDataProperty p) {
        return pelletReasoner.getAncestorProperties(p);
    }

    public Set<Set<OWLObjectProperty>> getAncestorProperties(OWLObjectProperty p) {
        return pelletReasoner.getAncestorProperties(p);
    }

    public Set<OWLClass> getClasses() {
        return pelletReasoner.getClasses();
    }

    public Set<OWLClass> getComplementClasses(OWLDescription c) {
        return pelletReasoner.getComplementClasses(c);
    }

    public Set<OWLDataProperty> getDataProperties() {
        return pelletReasoner.getDataProperties();
    }

    public Map<OWLIndividual, Set<OWLConstant>> getDataPropertyAssertions(OWLDataProperty arg0) {
        return pelletReasoner.getDataPropertyAssertions(arg0);
    }

    public Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(OWLIndividual arg0) {
        return pelletReasoner.getDataPropertyRelationships(arg0);
    }

    public Set<Set<OWLClass>> getDescendantClasses(OWLDescription c) {
        return pelletReasoner.getDescendantClasses(c);
    }

    public Set<Set<OWLDataProperty>> getDescendantProperties(OWLDataProperty p) {
        return pelletReasoner.getDescendantProperties(p);
    }

    public Set<Set<OWLObjectProperty>> getDescendantProperties(OWLObjectProperty p) {
        return pelletReasoner.getDescendantProperties(p);
    }

    public Set<OWLIndividual> getDifferentFromIndividuals(OWLIndividual ind) {
        return pelletReasoner.getDifferentFromIndividuals(ind);
    }

    public Set<Set<OWLClass>> getDisjointClasses(OWLDescription c) {
        return pelletReasoner.getDisjointClasses(c);
    }

    public Set<Set<OWLDescription>> getDomains(OWLDataProperty p) {
        return pelletReasoner.getDomains(p);
    }

    public Set<Set<OWLDescription>> getDomains(OWLObjectProperty p) {
        return pelletReasoner.getDomains(p);
    }

    public Set<OWLClass> getEquivalentClasses(OWLDescription c) {
        return pelletReasoner.getEquivalentClasses(c);
    }

    public Set<OWLDataProperty> getEquivalentProperties(OWLDataProperty p) {
        return pelletReasoner.getEquivalentProperties(p);
    }

    public Set<OWLObjectProperty> getEquivalentProperties(OWLObjectProperty p) {
        return pelletReasoner.getEquivalentProperties(p);
    }

    public Set<OWLAxiom> getExplanation() throws OWLRuntimeException {
        return pelletReasoner.getExplanation();
    }

    public Set<OWLClass> getInconsistentClasses() {
        return pelletReasoner.getInconsistentClasses();
    }

    public Set<OWLIndividual> getIndividuals() {
        return pelletReasoner.getIndividuals();
    }

    public Set<OWLIndividual> getIndividuals(OWLDescription clsC, boolean direct) {
        return pelletReasoner.getIndividuals(clsC, direct);
    }

    public Set<Set<OWLObjectProperty>> getInverseProperties(OWLObjectProperty prop) {
        return pelletReasoner.getInverseProperties(prop);
    }

    public KnowledgeBase getKB() {
        return pelletReasoner.getKB();
    }

    public Set<OWLOntology> getLoadedOntologies() {
        return pelletReasoner.getLoadedOntologies();
    }

    public PelletLoader getLoader() {
        return pelletReasoner.getLoader();
    }

    public OWLOntologyManager getManager() {
        return pelletReasoner.getManager();
    }

    public Set<OWLObjectProperty> getObjectProperties() {
        return pelletReasoner.getObjectProperties();
    }

    public Map<OWLIndividual, Set<OWLIndividual>> getObjectPropertyAssertions(OWLObjectProperty arg0) {
        return pelletReasoner.getObjectPropertyAssertions(arg0);
    }

    public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(OWLIndividual arg0) {
        return pelletReasoner.getObjectPropertyRelationships(arg0);
    }

    public Set<OWLProperty<?, ?>> getProperties() {
        return pelletReasoner.getProperties();
    }

    public Set<OWLDataRange> getRanges(OWLDataProperty p) {
        return pelletReasoner.getRanges(p);
    }

    public Set<OWLDescription> getRanges(OWLObjectProperty p) {
        return pelletReasoner.getRanges(p);
    }

    public Set<? extends OWLObject> getRelated(OWLIndividual ind, OWLPropertyExpression<?, ?> prop) {
        return pelletReasoner.getRelated(ind, prop);
    }

    public OWLIndividual getRelatedIndividual(OWLIndividual subject, OWLObjectPropertyExpression property) {
        return pelletReasoner.getRelatedIndividual(subject, property);
    }

    public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual subject, OWLObjectPropertyExpression property) {
        return pelletReasoner.getRelatedIndividuals(subject, property);
    }

    public OWLConstant getRelatedValue(OWLIndividual subject, OWLDataPropertyExpression property) {
        return pelletReasoner.getRelatedValue(subject, property);
    }

    public Set<OWLConstant> getRelatedValues(OWLIndividual subject, OWLDataPropertyExpression property) {
        return pelletReasoner.getRelatedValues(subject, property);
    }

    public Set<OWLIndividual> getSameAsIndividuals(OWLIndividual ind) {
        return pelletReasoner.getSameAsIndividuals(ind);
    }

    public Set<Set<OWLClass>> getSubClasses(OWLDescription c) {
        return pelletReasoner.getSubClasses(c);
    }

    public Set<Set<OWLDataProperty>> getSubProperties(OWLDataProperty p) {
        return pelletReasoner.getSubProperties(p);
    }

    public Set<Set<OWLObjectProperty>> getSubProperties(OWLObjectProperty p) {
        return pelletReasoner.getSubProperties(p);
    }

    public Set<Set<OWLClass>> getSuperClasses(OWLDescription c) {
        return pelletReasoner.getSuperClasses(c);
    }

    public Set<Set<OWLDataProperty>> getSuperProperties(OWLDataProperty p) {
        return pelletReasoner.getSuperProperties(p);
    }

    public Set<Set<OWLObjectProperty>> getSuperProperties(OWLObjectProperty p) {
        return pelletReasoner.getSuperProperties(p);
    }

    public OWLClass getType(OWLIndividual ind) {
        return pelletReasoner.getType(ind);
    }

    public Set<Set<OWLClass>> getTypes(OWLIndividual ind, boolean direct) {
        return pelletReasoner.getTypes(ind, direct);
    }

    public Set<Set<OWLClass>> getTypes(OWLIndividual individual) {
        return pelletReasoner.getTypes(individual);
    }

    public boolean hasDataPropertyRelationship(OWLIndividual subject, OWLDataPropertyExpression property, OWLConstant object) {
        return pelletReasoner.hasDataPropertyRelationship(subject, property, object);
    }

    public boolean hasDomain(OWLDataProperty p, OWLDescription c) {
        return pelletReasoner.hasDomain(p, c);
    }

    public boolean hasDomain(OWLObjectProperty p, OWLDescription c) {
        return pelletReasoner.hasDomain(p, c);
    }

    public int hashCode() {
        return pelletReasoner.hashCode();
    }

    public boolean hasObjectPropertyRelationship(OWLIndividual subject, OWLObjectPropertyExpression property, OWLIndividual object) {
        return pelletReasoner.hasObjectPropertyRelationship(subject, property, object);
    }

    public boolean hasRange(OWLDataProperty p, OWLDataRange d) {
        return pelletReasoner.hasRange(p, d);
    }

    public boolean hasRange(OWLObjectProperty p, OWLDescription c) {
        return pelletReasoner.hasRange(p, c);
    }

    public boolean hasType(OWLIndividual individual, OWLDescription type, boolean direct) throws OWLReasonerException {
        return pelletReasoner.hasType(individual, type, direct);
    }

    public boolean hasType(OWLIndividual individual, OWLDescription type) {
        return pelletReasoner.hasType(individual, type);
    }

    public boolean isAntiSymmetric(OWLObjectProperty p) {
        return pelletReasoner.isAntiSymmetric(p);
    }

    public boolean isClassified() {
        return pelletReasoner.isClassified();
    }

    public boolean isComplementOf(OWLDescription c1, OWLDescription c2) {
        return pelletReasoner.isComplementOf(c1, c2);
    }

    public boolean isConsistent() {
        return pelletReasoner.isConsistent();
    }

    public boolean isConsistent(OWLDescription d) {
        return pelletReasoner.isConsistent(d);
    }

    public boolean isConsistent(OWLOntology ontology) {
        return pelletReasoner.isConsistent(ontology);
    }

    public boolean isDefined(OWLClass cls) {
        return pelletReasoner.isDefined(cls);
    }

    public boolean isDefined(OWLDataProperty prop) {
        return pelletReasoner.isDefined(prop);
    }

    public boolean isDefined(OWLIndividual ind) {
        return pelletReasoner.isDefined(ind);
    }

    public boolean isDefined(OWLObjectProperty prop) {
        return pelletReasoner.isDefined(prop);
    }

    public boolean isDifferentFrom(OWLIndividual ind1, OWLIndividual ind2) {
        return pelletReasoner.isDifferentFrom(ind1, ind2);
    }

    public boolean isDisjointWith(OWLDataProperty p1, OWLDataProperty p2) {
        return pelletReasoner.isDisjointWith(p1, p2);
    }

    public boolean isDisjointWith(OWLDescription c1, OWLDescription c2) {
        return pelletReasoner.isDisjointWith(c1, c2);
    }

    public boolean isDisjointWith(OWLObjectProperty p1, OWLObjectProperty p2) {
        return pelletReasoner.isDisjointWith(p1, p2);
    }

    public boolean isEntailed(OWLAxiom axiom) {
        return pelletReasoner.isEntailed(axiom);
    }

    public boolean isEntailed(OWLOntology ont) {
        return pelletReasoner.isEntailed(ont);
    }

    public boolean isEntailed(Set<? extends OWLAxiom> arg0) {
        return pelletReasoner.isEntailed(arg0);
    }

    public boolean isEquivalentClass(OWLDescription c1, OWLDescription c2) {
        return pelletReasoner.isEquivalentClass(c1, c2);
    }

    public boolean isEquivalentProperty(OWLDataProperty p1, OWLDataProperty p2) {
        return pelletReasoner.isEquivalentProperty(p1, p2);
    }

    public boolean isEquivalentProperty(OWLObjectProperty p1, OWLObjectProperty p2) {
        return pelletReasoner.isEquivalentProperty(p1, p2);
    }

    public boolean isFunctional(OWLDataProperty p) {
        return pelletReasoner.isFunctional(p);
    }

    public boolean isFunctional(OWLObjectProperty p) {
        return pelletReasoner.isFunctional(p);
    }

    public boolean isInverseFunctional(OWLObjectProperty p) {
        return pelletReasoner.isInverseFunctional(p);
    }

    public boolean isInverseOf(OWLObjectProperty p1, OWLObjectProperty p2) {
        return pelletReasoner.isInverseOf(p1, p2);
    }

    public boolean isIrreflexive(OWLObjectProperty p) {
        return pelletReasoner.isIrreflexive(p);
    }

    public boolean isRealised() throws OWLReasonerException {
        return pelletReasoner.isRealised();
    }

    public boolean isReflexive(OWLObjectProperty p) {
        return pelletReasoner.isReflexive(p);
    }

    public boolean isSameAs(OWLIndividual ind1, OWLIndividual ind2) {
        return pelletReasoner.isSameAs(ind1, ind2);
    }

    public boolean isSatisfiable(OWLDescription d) {
        return pelletReasoner.isSatisfiable(d);
    }

    public boolean isSubClassOf(OWLDescription c1, OWLDescription c2) {
        return pelletReasoner.isSubClassOf(c1, c2);
    }

    public boolean isSubPropertyOf(OWLDataProperty c1, OWLDataProperty c2) {
        return pelletReasoner.isSubPropertyOf(c1, c2);
    }

    public boolean isSubPropertyOf(OWLObjectProperty c1, OWLObjectProperty c2) {
        return pelletReasoner.isSubPropertyOf(c1, c2);
    }

    public boolean isSubTypeOf(OWLDataType d1, OWLDataType d2) {
        return pelletReasoner.isSubTypeOf(d1, d2);
    }

    public boolean isSymmetric(OWLObjectProperty p) {
        return pelletReasoner.isSymmetric(p);
    }

    public boolean isTransitive(OWLObjectProperty p) {
        return pelletReasoner.isTransitive(p);
    }

    public void loadOntologies(Set<OWLOntology> ontologies) {
        pelletReasoner.loadOntologies(ontologies);
    }

    public void loadOntology(OWLOntology ontology) {
        pelletReasoner.loadOntology(ontology);
    }

    public void ontologiesChanged(List<? extends OWLOntologyChange> changes) {
        pelletReasoner.ontologiesChanged(changes);
    }

    public void realise() throws OWLReasonerException {
        pelletReasoner.realise();
    }

    public void refresh() {
        pelletReasoner.refresh();
    }

    public void setOntology(OWLOntology ontology) {
        pelletReasoner.setOntology(ontology);
    }

    public String toString() {
        return pelletReasoner.toString();
    }

    public void unloadOntologies(Set<OWLOntology> ontologies) {
        pelletReasoner.unloadOntologies(ontologies);
    }

    public void unloadOntology(OWLOntology ontology) {
        pelletReasoner.unloadOntology(ontology);
    }
}
