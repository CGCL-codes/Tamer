package gr.konstant.transonto.kb.jena;

import com.hp.hpl.jena.ontology.OntClass;
import gr.konstant.transonto.exception.BackendException;
import gr.konstant.transonto.exception.UnsupportedFeatureException;
import gr.konstant.transonto.interfaces.ExprConcept;
import gr.konstant.transonto.interfaces.ExprPredicate;
import gr.konstant.transonto.interfaces.KnowledgeBase;

public class OWLClass extends RDFSClass implements ExprConcept {

    protected OWLClass(JenaOWL kb) {
        super(kb);
    }

    public OWLClass(JenaOWL kb, OntClass c) {
        super(kb, c);
    }

    @Override
    protected OntClass getJenaNode() {
        com.hp.hpl.jena.rdf.model.RDFNode n = super.getJenaNode();
        assert n instanceof OntClass;
        return (OntClass) n;
    }

    @Override
    protected void setJenaNode(com.hp.hpl.jena.rdf.model.RDFNode n) {
        assert n instanceof OntClass;
        super.setJenaNode(n);
    }

    @Override
    public JenaOWL livesIn() {
        assert this.kb instanceof JenaOWL;
        return (JenaOWL) kb;
    }

    @Override
    public ExprPredicate inKnowledgeBase(KnowledgeBase other) throws BackendException, UnsupportedFeatureException {
        assert other instanceof JenaOWL;
        return new OWLClass((JenaOWL) other, getJenaNode());
    }

    @Override
    public boolean isTop() {
        String name = this.getJenaNode().getURI();
        if (this.getJenaNode().isURIResource() && (Vocabulary.getVocabURI(Vocabulary.OWL_THING).toString().compareTo(name) == 0)) {
            return true;
        } else {
            return false;
        }
    }
}
