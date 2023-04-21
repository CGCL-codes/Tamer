package gov.nist.msid.ontostep;

import java.net.URI;
import org.semanticweb.owl.model.*;

public class OWLHelper {

    private OWLDataFactory factory;

    private String currentOntoURI;

    public OWLHelper(OWLDataFactory factory, String currentOntoURI) {
        this.factory = factory;
        this.currentOntoURI = currentOntoURI;
    }

    public OWLClass getClas(String name) {
        return factory.getOWLClass(URI.create(currentOntoURI + "#" + name));
    }

    public OWLClass getClas(String schema, String name) {
        return factory.getOWLClass(URI.create(schema + "#" + name));
    }

    public OWLSubClassAxiom getSubClassAxiom(OWLDescription c1, OWLDescription c2) {
        return factory.getOWLSubClassAxiom(c1, c2);
    }

    public OWLDeclarationAxiom getDeclarationAxiom(OWLEntity owle) {
        return factory.getOWLDeclarationAxiom(owle);
    }

    public OWLDisjointClassesAxiom getDisjointClassesAxiom(java.util.Set<OWLDescription> owlds) {
        return factory.getOWLDisjointClassesAxiom(owlds);
    }

    public OWLDisjointClassesAxiom getDisjointClassesAxiom(OWLDescription a, OWLDescription b) {
        return factory.getOWLDisjointClassesAxiom(a, b);
    }

    public OWLDisjointUnionAxiom getDisjointUnionAxiom(OWLClass owlClass, java.util.Set<? extends OWLDescription> descriptions) {
        return factory.getOWLDisjointUnionAxiom(owlClass, descriptions);
    }

    public OWLObjectProperty getOP(String name) {
        return factory.getOWLObjectProperty(URI.create("http://www.nist.gov" + "#" + name));
    }

    public OWLObjectPropertyRangeAxiom getOPRangeAxiom(OWLObjectPropertyExpression p, OWLDescription d) {
        return factory.getOWLObjectPropertyRangeAxiom(p, d);
    }

    public OWLObjectPropertyDomainAxiom getOPDomainAxiom(OWLObjectPropertyExpression p, OWLDescription d) {
        return factory.getOWLObjectPropertyDomainAxiom(p, d);
    }

    public OWLFunctionalObjectPropertyAxiom getFunctionalOPAxiom(OWLObjectPropertyExpression p) {
        return factory.getOWLFunctionalObjectPropertyAxiom(p);
    }

    public OWLTransitiveObjectPropertyAxiom getTransitiveOPAxiom(OWLObjectPropertyExpression p) {
        return factory.getOWLTransitiveObjectPropertyAxiom(p);
    }

    public OWLObjectSubPropertyAxiom getSubOPAxiom(OWLObjectPropertyExpression op1, OWLObjectPropertyExpression op2) {
        return factory.getOWLSubObjectPropertyAxiom(op1, op2);
    }

    public OWLObjectPropertyChainSubPropertyAxiom getOPChain(java.util.List<? extends OWLObjectPropertyExpression> chain, OWLObjectPropertyExpression superProperty) {
        return factory.getOWLObjectPropertyChainSubPropertyAxiom(chain, superProperty);
    }

    public OWLObjectAllRestriction getOAllRestriction(OWLObjectPropertyExpression op, OWLDescription oc) {
        return factory.getOWLObjectAllRestriction(op, oc);
    }

    public OWLObjectMaxCardinalityRestriction getOMaxCardRestriction(OWLObjectPropertyExpression op, int i) {
        return factory.getOWLObjectMaxCardinalityRestriction(op, i);
    }

    public OWLObjectMinCardinalityRestriction getOMinCardRestriction(OWLObjectPropertyExpression op, int i) {
        return factory.getOWLObjectMinCardinalityRestriction(op, i);
    }

    public OWLObjectExactCardinalityRestriction getOExactCardRestriction(OWLObjectPropertyExpression op, int i) {
        return factory.getOWLObjectExactCardinalityRestriction(op, i);
    }

    public OWLObjectSomeRestriction getOSomeRestriction(OWLObjectPropertyExpression op, OWLDescription oc) {
        return factory.getOWLObjectSomeRestriction(op, oc);
    }

    public OWLDataType getDataTypeW3(String name) {
        return factory.getOWLDataType(URI.create("http://www.w3.org/2001/XMLSchema#" + name));
    }

    public OWLDataPropertyDomainAxiom getDPDomainAxiom(OWLDataPropertyExpression dp, OWLDescription dom) {
        return factory.getOWLDataPropertyDomainAxiom(dp, dom);
    }

    public OWLDataPropertyRangeAxiom getDPRangeAxiom(OWLDataProperty dp, OWLDataType dt) {
        return factory.getOWLDataPropertyRangeAxiom(dp, dt);
    }

    public OWLDataProperty getDP(String name) {
        return factory.getOWLDataProperty(URI.create(currentOntoURI + "#" + name));
    }

    public OWLObjectIntersectionOf getOIntersectionOf(OWLDescription... descs) {
        return factory.getOWLObjectIntersectionOf(descs);
    }

    public OWLObjectIntersectionOf getOIntersectionOf(java.util.Set<? extends OWLDescription> descs) {
        return factory.getOWLObjectIntersectionOf(descs);
    }

    public OWLObjectUnionOf getOUnionOf(OWLDescription... descs) {
        return factory.getOWLObjectUnionOf(descs);
    }

    public OWLObjectUnionOf getOUnionOf(java.util.Set<? extends OWLDescription> descs) {
        return factory.getOWLObjectUnionOf(descs);
    }

    public OWLEquivalentClassesAxiom getEquivalentClassesAxiom(OWLDescription c1, OWLDescription c2) {
        return factory.getOWLEquivalentClassesAxiom(c1, c2);
    }

    public OWLEquivalentClassesAxiom getEquivalentClassesAxiom(java.util.Set<? extends OWLDescription> descs) {
        return factory.getOWLEquivalentClassesAxiom(descs);
    }

    public OWLObjectComplementOf getOComplementOf(OWLDescription desc) {
        return factory.getOWLObjectComplementOf(desc);
    }

    public OWLObjectOneOf getOOneOf(OWLIndividual... ind) {
        return factory.getOWLObjectOneOf(ind);
    }

    public OWLObjectOneOf getOOneOf(java.util.Set<OWLIndividual> ind) {
        return factory.getOWLObjectOneOf(ind);
    }

    public OWLCommentAnnotation getCommentAnnotation(String text) {
        return factory.getCommentAnnotation(text);
    }

    public OWLConstantAnnotation getOConstantAnnotation(String name, String value) {
        return factory.getOWLConstantAnnotation(URI.create(currentOntoURI + "#" + name), factory.getOWLTypedConstant(value));
    }

    public OWLConstantAnnotation getOConstantAnnotation(String name, int value) {
        return factory.getOWLConstantAnnotation(URI.create(currentOntoURI + "#" + name), factory.getOWLTypedConstant(value));
    }

    public OWLEntityAnnotationAxiom getEntityAnnotationAxiom(OWLEntity entity, OWLAnnotation annotation) {
        return factory.getOWLEntityAnnotationAxiom(entity, annotation);
    }

    public OWLIndividual getIndividual(String name) {
        return factory.getOWLIndividual(URI.create(currentOntoURI + "#" + name));
    }

    public OWLClassAssertionAxiom getClassAssertionAxiom(OWLIndividual ind, OWLDescription cla) {
        return factory.getOWLClassAssertionAxiom(ind, cla);
    }

    public OWLDataPropertyAssertionAxiom getDPAssertion(OWLIndividual owli, OWLDataProperty owldp, int i) {
        return factory.getOWLDataPropertyAssertionAxiom(owli, owldp, i);
    }

    public OWLDataPropertyAssertionAxiom getDPAssertion(OWLIndividual owli, OWLDataProperty owldp, String s) {
        return factory.getOWLDataPropertyAssertionAxiom(owli, owldp, s);
    }

    public OWLDataPropertyAssertionAxiom getDPAssertion(OWLIndividual owli, OWLDataProperty owldp, double d) {
        return factory.getOWLDataPropertyAssertionAxiom(owli, owldp, d);
    }

    public OWLObjectPropertyAssertionAxiom getOPAssertion(OWLIndividual owli1, OWLObjectProperty owlop, OWLIndividual owli2) {
        return factory.getOWLObjectPropertyAssertionAxiom(owli1, owlop, owli2);
    }
}
