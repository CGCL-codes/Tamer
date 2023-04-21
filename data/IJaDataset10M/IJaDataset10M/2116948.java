package net.sourceforge.fluxion.pussycat.util;

import org.semanticweb.owl.util.SimpleRenderer;
import org.semanticweb.owl.model.*;
import java.net.URI;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Author: Rob Davey<br>
 * Date: 13-Dec-2007<br><br>
 * <p/>
 * An HTML axiom renderer.
 */
public class PussycatHTMLAxiomRenderer extends SimpleRenderer {

    private StringBuilder sb;

    private String subjCol = "blue";

    private String predCol = "default";

    private String objCol = "red";

    public PussycatHTMLAxiomRenderer() {
        sb = new StringBuilder();
    }

    public void reset() {
        sb = new StringBuilder();
    }

    public String getSubjectColour() {
        return this.subjCol;
    }

    public void setSubjectColour(String col) {
        this.subjCol = col;
    }

    public String getPredicateColour() {
        return this.predCol;
    }

    public void setPredicateColour(String col) {
        this.predCol = col;
    }

    public String getObjectColour() {
        return this.objCol;
    }

    public void setObjectColour(String col) {
        this.objCol = col;
    }

    public void visit(OWLOntology ontology) {
        sb.append("Ontology :: <span class='" + getSubjectColour() + "'>" + ontology.getURI() + "</span><br/>");
    }

    public void visit(OWLOntologyAnnotationAxiom axiom) {
        sb.append("<span class='" + getSubjectColour() + "'>");
        axiom.getSubject().accept(this);
        sb.append("</span> has OntologyAnnotation ");
        sb.append("<span class='" + getObjectColour() + "'>");
        axiom.getAnnotation().accept(this);
        sb.append("</span><br/>");
    }

    private void insertSpace() {
        sb.append(" ");
    }

    private <N extends OWLObject> Set<N> toSortedSet(Set<N> set) {
        Set<N> sorted = new TreeSet<N>(new Comparator() {

            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
        sorted.addAll(set);
        return sorted;
    }

    public void visit(OWLSubClassAxiom axiom) {
        sb.append("<span class='" + getSubjectColour() + "'>");
        axiom.getSubClass().accept(this);
        sb.append("</span> is SubClassOf ");
        sb.append("<span class='" + getObjectColour() + "'>");
        axiom.getSuperClass().accept(this);
        sb.append("</span><br/>");
    }

    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        sb.append("NegativeObjectPropertyAssertion(");
        axiom.getProperty().accept(this);
        insertSpace();
        axiom.getSubject().accept(this);
        insertSpace();
        axiom.getObject().accept(this);
        sb.append(")");
    }

    public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
        sb.append("AntiSymmetricObjectProperty(");
        axiom.getProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
        sb.append("ReflexiveObjectProperty(");
        axiom.getProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLDisjointClassesAxiom axiom) {
        sb.append("Disjoint Classes :: ");
        int count = 1;
        for (OWLDescription desc : toSortedSet(axiom.getDescriptions())) {
            desc.accept(this);
            if (count < axiom.getDescriptions().size()) {
                sb.append(", ");
            }
            count++;
        }
        sb.append("<br/>");
    }

    public void visit(OWLDataPropertyDomainAxiom axiom) {
        sb.append("DataPropertyDomain(");
        axiom.getProperty().accept(this);
        insertSpace();
        axiom.getDomain().accept(this);
        sb.append(")");
    }

    public void visit(OWLImportsDeclaration axiom) {
        sb.append("Imports(");
        sb.append(axiom.getSubject().getURI());
        sb.append(" -> ");
        sb.append(axiom.getImportedOntologyURI());
        sb.append(")");
    }

    public void visit(OWLAxiomAnnotationAxiom axiom) {
    }

    public void visit(OWLObjectPropertyDomainAxiom axiom) {
        sb.append("ObjectPropertyDomain(");
        axiom.getProperty().accept(this);
        insertSpace();
        axiom.getDomain().accept(this);
        sb.append(")");
    }

    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        sb.append("EquivalentObjectProperties(");
        for (OWLObjectPropertyExpression prop : toSortedSet(axiom.getProperties())) {
            insertSpace();
            prop.accept(this);
        }
        sb.append(" )");
    }

    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        sb.append("NegativeDataPropertyAssertion(");
        axiom.getProperty().accept(this);
        insertSpace();
        axiom.getSubject().accept(this);
        insertSpace();
        axiom.getObject().accept(this);
        sb.append(")");
    }

    public void visit(OWLDifferentIndividualsAxiom axiom) {
        sb.append("DifferentIndividuals(");
        for (OWLIndividual ind : axiom.getIndividuals()) {
            insertSpace();
            ind.accept(this);
        }
        sb.append(" )");
    }

    public void visit(OWLDisjointDataPropertiesAxiom axiom) {
        sb.append("DisjointDataProperties(");
        for (OWLDataPropertyExpression prop : toSortedSet(axiom.getProperties())) {
            insertSpace();
            prop.accept(this);
        }
        sb.append(" )");
    }

    public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
        sb.append("DisjointObjectProperties(");
        for (OWLObjectPropertyExpression prop : toSortedSet(axiom.getProperties())) {
            insertSpace();
            prop.accept(this);
        }
        sb.append(" )");
    }

    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        sb.append("ObjectPropertyRange(");
        axiom.getProperty().accept(this);
        insertSpace();
        axiom.getRange().accept(this);
        sb.append(")");
    }

    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        sb.append("<span class='" + getSubjectColour() + "'>");
        axiom.getSubject().accept(this);
        sb.append("</span>");
        sb.append(" <span class='" + getPredicateColour() + "'>");
        axiom.getProperty().accept(this);
        sb.append("</span> ");
        sb.append("<span class='" + getObjectColour() + "'>");
        axiom.getObject().accept(this);
        sb.append("</span><br/>");
    }

    public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
        axiom.getProperty().accept(this);
        sb.append(" is a FunctionalObjectProperty");
    }

    public void visit(OWLObjectSubPropertyAxiom axiom) {
        sb.append("SubObjectProperty(");
        axiom.getSubProperty().accept(this);
        insertSpace();
        axiom.getSuperProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLDisjointUnionAxiom axiom) {
        sb.append("DisjointUnion(");
        axiom.getOWLClass().accept(this);
        insertSpace();
        for (OWLDescription desc : toSortedSet(axiom.getDescriptions())) {
            insertSpace();
            desc.accept(this);
        }
        sb.append(" )");
    }

    public void visit(OWLDeclarationAxiom axiom) {
        sb.append("Declaration(");
        axiom.getEntity().accept(this);
        sb.append(")");
    }

    public void visit(OWLEntityAnnotationAxiom axiom) {
        sb.append("EntityAnnotationAxiom(");
        axiom.getSubject().accept(this);
        insertSpace();
        axiom.getAnnotation().accept(this);
        sb.append(")");
    }

    public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
        sb.append("SymmetricObjectProperty(");
        axiom.getProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLDataPropertyRangeAxiom axiom) {
        sb.append("DataPropertyRange(");
        axiom.getProperty().accept(this);
        insertSpace();
        axiom.getRange().accept(this);
        sb.append(")");
    }

    public void visit(OWLFunctionalDataPropertyAxiom axiom) {
        axiom.getProperty().accept(this);
        sb.append(" is a FunctionalDataProperty");
    }

    public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
        sb.append("EquivalentDataProperties(");
        for (OWLDataPropertyExpression prop : toSortedSet(axiom.getProperties())) {
            insertSpace();
            prop.accept(this);
        }
        sb.append(" )");
    }

    public void visit(OWLClassAssertionAxiom axiom) {
        sb.append("<span class='" + getSubjectColour() + "'>");
        axiom.getIndividual().accept(this);
        sb.append("</span> is instance of ");
        sb.append("<span class='" + getObjectColour() + "'>");
        axiom.getDescription().accept(this);
        sb.append("</span><br/>");
    }

    public void visit(OWLEquivalentClassesAxiom axiom) {
        sb.append("EquivalentClasses :: <ul>");
        for (OWLDescription desc : axiom.getDescriptions()) {
            sb.append("<li>");
            desc.accept(this);
            sb.append("</li>");
        }
        sb.append(" </ul>");
    }

    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        sb.append("<span class='" + getSubjectColour() + "'>");
        axiom.getSubject().accept(this);
        sb.append("</span>");
        sb.append(" <span class='" + getPredicateColour() + "'>");
        axiom.getProperty().accept(this);
        sb.append("</span> ");
        axiom.getObject().accept(this);
        sb.append("<br/>");
    }

    public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
        sb.append("TransitiveObjectProperty(");
        axiom.getProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        sb.append("IrreflexiveObjectProperty(");
        axiom.getProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLDataSubPropertyAxiom axiom) {
        sb.append("SubDataProperty(");
        axiom.getSubProperty().accept(this);
        insertSpace();
        axiom.getSuperProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        sb.append("InverseFunctionalObjectProperty(");
        axiom.getProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLSameIndividualsAxiom axiom) {
        sb.append("Same Individuals :: ");
        int count = 1;
        for (OWLIndividual ind : axiom.getIndividuals()) {
            ind.accept(this);
            if (count < axiom.getIndividuals().size()) {
                sb.append(", ");
            }
            count++;
        }
        sb.append("<br/>");
    }

    public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
        sb.append("ObjectPropertyChainSubProperty(");
        sb.append("(");
        for (OWLObjectPropertyExpression prop : axiom.getPropertyChain()) {
            insertSpace();
            prop.accept(this);
        }
        sb.append(" )");
        insertSpace();
        axiom.getSuperProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLClass desc) {
        sb.append(getShortForm(desc.getURI()));
    }

    public void visit(OWLObjectIntersectionOf desc) {
        sb.append("<ul>");
        for (OWLDescription op : desc.getOperands()) {
            sb.append("<li>");
            op.accept(this);
            sb.append("</li>");
        }
        sb.append("</ul>");
    }

    public void visit(OWLObjectUnionOf desc) {
        sb.append("ObjectUnionOf(");
        for (OWLDescription op : desc.getOperands()) {
            insertSpace();
            op.accept(this);
        }
        sb.append(")");
    }

    public void visit(OWLObjectComplementOf desc) {
        sb.append("ObjectComplementOf(");
        desc.getOperand().accept(this);
        sb.append(")");
    }

    public void visit(OWLObjectSomeRestriction desc) {
        sb.append("<span class='" + getPredicateColour() + "'>");
        desc.getProperty().accept(this);
        sb.append(" <b>some</b> ");
        sb.append("</span>");
        sb.append("<span class='" + getObjectColour() + "'>");
        desc.getFiller().accept(this);
        sb.append("</span>");
    }

    public void visit(OWLObjectAllRestriction desc) {
        sb.append("<span class='" + getPredicateColour() + "'>");
        desc.getProperty().accept(this);
        sb.append(" <b>only</b> ");
        sb.append("</span>");
        sb.append("<span class='" + getObjectColour() + "'>");
        desc.getFiller().accept(this);
        sb.append("</span>");
    }

    public void visit(OWLObjectValueRestriction desc) {
        sb.append("ObjectHasValue(");
        desc.getProperty().accept(this);
        insertSpace();
        desc.getValue().accept(this);
        sb.append(")");
    }

    public void visit(OWLObjectMinCardinalityRestriction desc) {
        sb.append("ObjectMinCardinality(");
        sb.append(desc.getCardinality());
        insertSpace();
        desc.getProperty().accept(this);
        insertSpace();
        desc.getFiller().accept(this);
        sb.append(")");
    }

    public void visit(OWLObjectExactCardinalityRestriction desc) {
        sb.append("ObjectExactCardinality(");
        sb.append(desc.getCardinality());
        insertSpace();
        desc.getProperty().accept(this);
        insertSpace();
        desc.getFiller().accept(this);
        sb.append(")");
    }

    public void visit(OWLObjectMaxCardinalityRestriction desc) {
        sb.append("ObjectMaxCardinality(");
        sb.append(desc.getCardinality());
        insertSpace();
        desc.getProperty().accept(this);
        insertSpace();
        desc.getFiller().accept(this);
        sb.append(")");
    }

    public void visit(OWLObjectSelfRestriction desc) {
        sb.append("ObjectExistsSelf(");
        desc.getProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLObjectOneOf desc) {
        sb.append("ObjectOneOf(");
        for (OWLIndividual ind : desc.getIndividuals()) {
            insertSpace();
            ind.accept(this);
        }
        sb.append(" )");
    }

    public void visit(OWLDataSomeRestriction desc) {
        sb.append("DataSomeValuesFrom(");
        desc.getProperty().accept(this);
        insertSpace();
        desc.getFiller().accept(this);
        sb.append(")");
    }

    public void visit(OWLDataAllRestriction desc) {
        sb.append("DataAllValuesFrom(");
        desc.getProperty().accept(this);
        insertSpace();
        desc.getFiller().accept(this);
        sb.append(")");
    }

    public void visit(OWLDataValueRestriction desc) {
        sb.append("DataValue(");
        desc.getProperty().accept(this);
        insertSpace();
        desc.getValue().accept(this);
        sb.append(")");
    }

    public void visit(OWLDataMinCardinalityRestriction desc) {
        sb.append("DataMinCardinality(");
        sb.append(desc.getCardinality());
        insertSpace();
        desc.getProperty().accept(this);
        insertSpace();
        desc.getFiller().accept(this);
        sb.append(")");
    }

    public void visit(OWLDataExactCardinalityRestriction desc) {
        sb.append("DataExactCardinality(");
        sb.append(desc.getCardinality());
        insertSpace();
        desc.getProperty().accept(this);
        insertSpace();
        desc.getFiller().accept(this);
        sb.append(")");
    }

    public void visit(OWLDataMaxCardinalityRestriction desc) {
        sb.append("DataMaxCardinality(");
        sb.append(desc.getCardinality());
        insertSpace();
        desc.getProperty().accept(this);
        insertSpace();
        desc.getFiller().accept(this);
        sb.append(")");
    }

    public void visit(OWLDataType node) {
        sb.append(node.getURI().getFragment());
    }

    public void visit(OWLDataComplementOf node) {
        sb.append("DataComplementOf(");
        node.getDataRange().accept(this);
        sb.append(")");
    }

    public void visit(OWLDataOneOf node) {
        sb.append("DataOneOf(");
        for (OWLConstant con : node.getValues()) {
            insertSpace();
            con.accept(this);
        }
        sb.append(" )");
    }

    public void visit(OWLDataRangeRestriction node) {
        sb.append("DataRangeRestriction(");
        node.getDataRange().accept(this);
        for (OWLDataRangeFacetRestriction restriction : node.getFacetRestrictions()) {
            insertSpace();
            restriction.accept(this);
        }
        sb.append(")");
    }

    public void visit(OWLDataRangeFacetRestriction node) {
        sb.append("facetRestriction(");
        sb.append(node.getFacet());
        insertSpace();
        node.getFacetValue().accept(this);
        sb.append(")");
    }

    public void visit(OWLTypedConstant node) {
        sb.append("\"");
        sb.append(node.getLiteral());
        sb.append("\" (");
        node.getDataType().accept(this);
        sb.append(")");
    }

    public void visit(OWLUntypedConstant node) {
        sb.append(node.getLiteral());
        if (node.hasLang()) {
            sb.append("@");
            sb.append(node.getLang());
        }
    }

    public void visit(OWLObjectProperty property) {
        sb.append(getShortForm(property.getURI()));
    }

    public void visit(OWLObjectPropertyInverse property) {
        sb.append("InverseOf(");
        property.getInverse().accept(this);
        sb.append(")");
    }

    public void visit(OWLDataProperty property) {
        sb.append(getShortForm(property.getURI()));
    }

    public void visit(OWLIndividual individual) {
        sb.append(getShortForm(individual.getURI()));
    }

    public void visit(OWLInverseObjectPropertiesAxiom axiom) {
        sb.append("InverseProperties(");
        axiom.getFirstProperty().accept(this);
        sb.append(" ");
        axiom.getSecondProperty().accept(this);
        sb.append(")");
    }

    public void visit(OWLConstantAnnotation annotation) {
        if (annotation.isLabel()) {
            sb.append("Label(");
        } else if (annotation.isComment()) {
            sb.append("Comment(");
        } else {
            sb.append("Annotation(");
            sb.append(getShortForm(annotation.getAnnotationURI()));
        }
        sb.append(" ");
        annotation.getAnnotationValue().accept(this);
        sb.append(")");
    }

    public void visit(OWLObjectAnnotation annotation) {
        sb.append("Annotation(");
        sb.append(getShortForm(annotation.getAnnotationURI()));
        sb.append(" ");
        annotation.getAnnotationValue().accept(this);
        sb.append(")");
    }

    public void visit(SWRLRule rule) {
        sb.append("Rule(");
        if (!rule.isAnonymous()) {
            sb.append(getShortForm(rule.getURI()));
            sb.append(" ");
        }
        sb.append("antecedent(");
        for (SWRLAtom at : rule.getBody()) {
            at.accept(this);
            sb.append(" ");
        }
        sb.append(")");
        sb.append("consequent(");
        for (SWRLAtom at : rule.getHead()) {
            at.accept(this);
            sb.append(" ");
        }
        sb.append(")");
        sb.append(")");
    }

    public void visit(SWRLClassAtom node) {
        node.getPredicate().accept(this);
        sb.append("(");
        node.getArgument().accept(this);
        sb.append(")");
    }

    public void visit(SWRLDataRangeAtom node) {
        node.getPredicate().accept(this);
        sb.append("(");
        node.getArgument().accept(this);
        sb.append(")");
    }

    public void visit(SWRLDifferentFromAtom node) {
        sb.append("differentFromAtom(");
        node.getFirstArgument().accept(this);
        sb.append(" ");
        node.getSecondArgument().accept(this);
        sb.append(")");
    }

    public void visit(SWRLSameAsAtom node) {
        sb.append("sameAsAtom(");
        node.getFirstArgument().accept(this);
        sb.append(" ");
        node.getSecondArgument().accept(this);
        sb.append(")");
    }

    public void visit(SWRLObjectPropertyAtom node) {
        node.getPredicate().accept(this);
        sb.append("(");
        node.getFirstArgument().accept(this);
        sb.append(" ");
        node.getSecondArgument().accept(this);
        sb.append(")");
    }

    public void visit(SWRLDataValuedPropertyAtom node) {
        node.getPredicate().accept(this);
        sb.append("(");
        node.getFirstArgument().accept(this);
        sb.append(" ");
        node.getSecondArgument().accept(this);
        sb.append(")");
    }

    public void visit(SWRLBuiltInAtom node) {
        sb.append(getShortForm(node.getPredicate().getURI()));
        sb.append("(");
        for (SWRLAtomObject arg : node.getArguments()) {
            arg.accept(this);
            sb.append(" ");
        }
        sb.append(")");
    }

    public void visit(SWRLAtomDVariable node) {
        sb.append("?");
        sb.append(getShortForm(node.getURI()));
    }

    public void visit(SWRLAtomIVariable node) {
        sb.append("?");
        sb.append(getShortForm(node.getURI()));
    }

    public void visit(SWRLAtomIndividualObject node) {
        node.getIndividual().accept(this);
    }

    public void visit(SWRLAtomConstantObject node) {
        node.getConstant().accept(this);
    }

    public String toString() {
        return sb.toString();
    }

    private String getShortForm(URI uri) {
        String fragment = uri.getFragment();
        if (fragment != null) {
            return fragment;
        }
        int lastSlashIndex = uri.getPath().lastIndexOf('/');
        if (lastSlashIndex != -1) {
            return uri.getPath().substring(lastSlashIndex + 1, uri.getPath().length());
        }
        return uri.toString();
    }
}
