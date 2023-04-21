package gr.konstant.transonto.kb.jena;

import com.hp.hpl.jena.ontology.AllValuesFromRestriction;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import gr.konstant.transonto.exception.BackendException;
import gr.konstant.transonto.exception.BadArgumentException;
import gr.konstant.transonto.exception.UnsupportedFeatureException;
import gr.konstant.transonto.interfaces.ExprConcept;
import gr.konstant.transonto.interfaces.ExprForAll;
import gr.konstant.transonto.interfaces.ExprPredicate;
import gr.konstant.transonto.interfaces.KnowledgeBase;

public class OWLAllValuesFrom extends OWLClass implements ExprForAll {

    public OWLAllValuesFrom(JenaOWL kb, AllValuesFromRestriction c) {
        super(kb, c);
    }

    @Override
    protected AllValuesFromRestriction getJenaNode() {
        com.hp.hpl.jena.rdf.model.RDFNode n = super.getJenaNode();
        assert n instanceof AllValuesFromRestriction;
        return (AllValuesFromRestriction) n;
    }

    @Override
    protected void setJenaNode(com.hp.hpl.jena.rdf.model.RDFNode n) {
        assert n instanceof AllValuesFromRestriction;
        super.setJenaNode(n);
    }

    @Override
    public ExprForAll inKnowledgeBase(KnowledgeBase other) throws BackendException, UnsupportedFeatureException {
        if (other instanceof JenaOWL) {
            AllValuesFromRestriction otherRestr;
            OntModel otherM = (OntModel) ((JenaOWL) other).jenaModel;
            otherRestr = (AllValuesFromRestriction) this.getJenaNode().inModel(otherM);
            return new OWLAllValuesFrom((JenaOWL) other, otherRestr);
        }
        OWLProperty pred = this.getPredicate();
        ExprPredicate foreignPred;
        try {
            foreignPred = other.getPredicate(pred.getName(), pred.getArity(), pred.isConcrete());
        } catch (BadArgumentException ex) {
            throw new AssertionError(ex);
        }
        ExprPredicate range = this.getRange();
        ExprPredicate foreignRange;
        if (range == null) {
            foreignRange = null;
        } else if (range instanceof OWLDataRange) {
            foreignRange = null;
        } else if (range instanceof OWLClassNamed) {
            try {
                foreignRange = other.getPredicate(((OWLClassNamed) range).getName(), 1, false);
            } catch (BadArgumentException ex) {
                throw new AssertionError(ex);
            }
        } else if (range instanceof OWLClass) {
            foreignRange = (ExprConcept) range.inKnowledgeBase(other);
        } else {
            throw new AssertionError();
        }
        return other.getForAll(foreignPred, foreignRange);
    }

    @Override
    public OWLProperty getPredicate() {
        OntProperty p = this.getJenaNode().getOnProperty();
        return new OWLProperty(this.livesIn(), p);
    }

    @Override
    public ExprPredicate getRange() {
        OntResource range = (OntResource) this.getJenaNode().getAllValuesFrom();
        if (range.isDataRange()) {
            return new OWLDataRange(this.livesIn(), range.asDataRange());
        } else if (range.isClass() && range.isURIResource()) {
            return new OWLClassNamed(this.livesIn(), range.asClass());
        } else if (range.isClass()) {
            return new OWLClass(this.livesIn(), range.asClass());
        } else {
            throw new AssertionError();
        }
    }
}
