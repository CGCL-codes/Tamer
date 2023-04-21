package gr.konstant.transonto.reasoner.prolog;

import java.net.URI;
import java.util.List;
import gr.konstant.transonto.exception.BackendException;
import gr.konstant.transonto.exception.BadArgumentException;
import gr.konstant.transonto.exception.UnsupportedFeatureException;
import gr.konstant.transonto.interfaces.Annotation;
import gr.konstant.transonto.interfaces.Anything;
import gr.konstant.transonto.interfaces.Axiom;
import gr.konstant.transonto.interfaces.AxiomEquiv;
import gr.konstant.transonto.interfaces.AxiomIncl;
import gr.konstant.transonto.interfaces.AxiomInstance;
import gr.konstant.transonto.interfaces.ExprComplement;
import gr.konstant.transonto.interfaces.ExprConcept;
import gr.konstant.transonto.interfaces.ExprConceptNamed;
import gr.konstant.transonto.interfaces.ExprConceptNominals;
import gr.konstant.transonto.interfaces.ExprExists;
import gr.konstant.transonto.interfaces.ExprForAll;
import gr.konstant.transonto.interfaces.ExprIndividual;
import gr.konstant.transonto.interfaces.ExprIntersection;
import gr.konstant.transonto.interfaces.ExprLiteral;
import gr.konstant.transonto.interfaces.ExprPredicate;
import gr.konstant.transonto.interfaces.ExprPredicateNamed;
import gr.konstant.transonto.interfaces.ExprProperty;
import gr.konstant.transonto.interfaces.ExprPropertyNamed;
import gr.konstant.transonto.interfaces.ExprUnion;
import gr.konstant.transonto.interfaces.ExprValue;
import gr.konstant.transonto.interfaces.Extension;
import gr.konstant.transonto.interfaces.ExtensionConcept;
import gr.konstant.transonto.interfaces.Extensions;
import gr.konstant.transonto.interfaces.InfKnowledgeBase;
import gr.konstant.transonto.interfaces.Instantiation;
import gr.konstant.transonto.interfaces.KnowledgeBase;
import gr.konstant.transonto.interfaces.Realization;
import gr.konstant.transonto.interfaces.Realizations;
import gr.konstant.transonto.interfaces.Valuation;

public class ProdlrKB implements InfKnowledgeBase {

    @Override
    public KnowledgeBase getExplicitKB() {
        return null;
    }

    @Override
    public List<Axiom> getInferredAxioms() throws BackendException {
        return null;
    }

    @Override
    public KnowledgeBase assertKnowledgeBase(KnowledgeBase otherKB) throws UnsupportedFeatureException, BadArgumentException, BackendException {
        return null;
    }

    @Override
    public Annotation createAnnotation(Anything object, URI key, ExprValue value) throws UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public Annotation createAnnotation(URI key, ExprValue value) throws UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public AxiomEquiv createDefinition(ExprPredicateNamed defined, ExprPredicate definition) throws UnsupportedFeatureException {
        return null;
    }

    @Override
    public AxiomEquiv createDefinition(ExprPredicateNamed defined, ExprPredicate definition, Valuation v) throws UnsupportedFeatureException {
        return null;
    }

    @Override
    public AxiomInstance createInstantiation(ExprPredicate predicate, Instantiation instance) throws BadArgumentException, UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public AxiomInstance createInstantiation(ExprPredicate predicate, Instantiation instance, Valuation v) throws BadArgumentException, UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public AxiomIncl createSubsumption(ExprPredicate premises, ExprPredicate conclusion) throws UnsupportedFeatureException, BadArgumentException {
        return null;
    }

    @Override
    public AxiomIncl createSubsumption(ExprPredicate premises, ExprPredicate conclusion, Valuation v) throws UnsupportedFeatureException, BadArgumentException {
        return null;
    }

    @Override
    public ExprConceptNamed findConcept(URI name) throws BackendException {
        return null;
    }

    @Override
    public List<AxiomEquiv> findDefinitions(ExprPredicateNamed defined, ExprPredicate definition) throws BackendException {
        return null;
    }

    @Override
    public ExprIndividual findIndividual(URI name) throws BackendException {
        return null;
    }

    @Override
    public ExprLiteral findLiteral(String data, String datatype) throws BadArgumentException, BackendException {
        return null;
    }

    @Override
    public ExprLiteral findLiteral(URI xsdData) throws BadArgumentException, BackendException {
        return null;
    }

    @Override
    public ExprPredicateNamed findPredicate(URI name) throws BackendException {
        return null;
    }

    @Override
    public ExprPropertyNamed findProperty(URI name) throws BackendException {
        return null;
    }

    @Override
    public List<AxiomIncl> findSubsumers(ExprPredicate subsumed, ExprPredicate subsumer) throws BackendException {
        return null;
    }

    @Override
    public List<Axiom> getAllAxioms() throws BackendException {
        return null;
    }

    @Override
    public Extensions getAllExtensions() throws BackendException {
        return null;
    }

    @Override
    public Realizations getAllRealizations() throws BackendException {
        return null;
    }

    @Override
    public ExprPredicateNamed getBottom() {
        return null;
    }

    @Override
    public ExprPredicateNamed getBottom(int arity) {
        return null;
    }

    @Override
    public ExprComplement getComplement(ExprConcept concept) throws UnsupportedFeatureException {
        return null;
    }

    @Override
    public ExprConceptNamed getConcept(URI name) throws BadArgumentException, BackendException, UnsupportedFeatureException {
        return null;
    }

    @Override
    public ExprConceptNamed getConcept(URI name, ExprConceptNamed superConcept) throws BadArgumentException, UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public ExprExists getExists(ExprPredicate predicate, ExprPredicate range, Integer min, Integer max) throws UnsupportedFeatureException {
        return null;
    }

    @Override
    public Extension getExtension(ExprPredicate predicate) throws BackendException {
        return null;
    }

    @Override
    public ExprForAll getForAll(ExprPredicate predicate, ExprPredicate range) throws UnsupportedFeatureException {
        return null;
    }

    @Override
    public ExprIndividual getIndividual(URI name) throws BadArgumentException, BackendException {
        return null;
    }

    @Override
    public ExprIndividual getIndividual(URI name, ExprConceptNamed concept) throws BadArgumentException, UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public ExprIntersection getIntersection(List<ExprConcept> concepts) throws UnsupportedFeatureException {
        return null;
    }

    @Override
    public ExprLiteral getLiteral(URI Datatype, String lexicalForm) throws BadArgumentException, UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public URI getNameBase() {
        return null;
    }

    @Override
    public ExprConceptNominals getNominals(ExtensionConcept ext) throws UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public ExprPredicateNamed getPredicate(URI name, int arity, boolean concrete) throws BadArgumentException, UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public ExprPropertyNamed getProperty(URI name) throws BadArgumentException, BackendException, UnsupportedFeatureException {
        return null;
    }

    @Override
    public ExprPropertyNamed getProperty(URI name, ExprProperty superProperty) throws BadArgumentException, UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public Realization getRealization(Instantiation instance) throws BackendException {
        return null;
    }

    @Override
    public ExprPredicateNamed getTop() {
        return null;
    }

    @Override
    public ExprPredicateNamed getTop(int arity) {
        return null;
    }

    @Override
    public ExprUnion getUnion(List<ExprConcept> concepts) throws UnsupportedFeatureException {
        return null;
    }

    @Override
    public KnowledgeBase inKnowledgeBase(KnowledgeBase otherKB) throws BackendException, UnsupportedFeatureException {
        return null;
    }

    @Override
    public Valuation instantiatesPredicate(Instantiation instance, ExprPredicate predicate) throws BackendException, BadArgumentException {
        return null;
    }

    @Override
    public KnowledgeBase retractKnowledgeBase(KnowledgeBase otherKB) throws UnsupportedFeatureException, BadArgumentException, BackendException {
        return null;
    }

    @Override
    public List<ExprValue> getAnnotation(URI key) throws UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public List<Annotation> getAnnotation() throws UnsupportedFeatureException, BackendException {
        return null;
    }

    @Override
    public KnowledgeBase livesIn() {
        return null;
    }

    @Override
    public void connect() throws BackendException {
    }

    @Override
    public void disconnect() throws BackendException {
    }
}
