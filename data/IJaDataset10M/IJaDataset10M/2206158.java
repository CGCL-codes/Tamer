package gr.konstant.transonto.kb.jena;

import java.net.URI;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Statement;
import gr.konstant.transonto.exception.BackendException;
import gr.konstant.transonto.exception.UnsupportedFeatureException;
import gr.konstant.transonto.interfaces.Annotation;
import gr.konstant.transonto.interfaces.Axiom;
import gr.konstant.transonto.interfaces.ExprValue;
import gr.konstant.transonto.interfaces.KnowledgeBase;

public class RDFSAnnotation extends RDFTriple implements Annotation {

    public RDFSAnnotation(JenaRDFS kb, Statement stmt) {
        super(kb, stmt);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public Axiom inKnowledgeBase(KnowledgeBase otherKB) throws UnsupportedFeatureException, BackendException {
        return otherKB.createAnnotation(this.getObject(), this.getKey(), this.getValue());
    }

    @Override
    public RDFResource getObject() {
        OntResource r = (OntResource) this.stmt.getSubject();
        return this.livesIn().getVocabulary().createAbstractRDFSExpr(r);
    }

    @Override
    public URI getKey() {
        return URI.create(this.stmt.getPredicate().getURI());
    }

    @Override
    public ExprValue getValue() {
        com.hp.hpl.jena.rdf.model.RDFNode n = this.stmt.getObject();
        RDFNode node = this.livesIn().getVocabulary().createExpr(n);
        if (node instanceof RDFLiteral) {
            return (RDFLiteral) node;
        } else {
            return (RDFResource) node;
        }
    }
}
