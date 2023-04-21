package gr.konstant.transonto.kb.jena;

import java.net.URI;
import java.util.List;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import gr.konstant.transonto.exception.*;
import gr.konstant.transonto.interfaces.Annotation;
import gr.konstant.transonto.interfaces.Expr;
import gr.konstant.transonto.interfaces.ExprValue;
import gr.konstant.transonto.interfaces.KnowledgeBase;
import gr.konstant.transonto.interfaces.impl.ExprImpl;

public class RDFNode extends ExprImpl implements Expr {

    private com.hp.hpl.jena.rdf.model.RDFNode jenaNode;

    protected RDFNode(JenaRDF kb) {
        super(kb);
    }

    public RDFNode(JenaRDF kb, com.hp.hpl.jena.rdf.model.RDFNode other) {
        this(kb);
        jenaNode = other.inModel(kb.jenaModel);
    }

    protected com.hp.hpl.jena.rdf.model.RDFNode getJenaNode() {
        return this.jenaNode;
    }

    protected void setJenaNode(com.hp.hpl.jena.rdf.model.RDFNode node) {
        this.jenaNode = node;
    }

    public boolean isAnon() {
        return jenaNode.isAnon();
    }

    public boolean isLiteral() {
        return jenaNode.isLiteral();
    }

    public boolean isResource() {
        return jenaNode.isResource();
    }

    public boolean isURIResource() {
        return jenaNode.isURIResource();
    }

    @Override
    public boolean equals(Object o) {
        assert o instanceof RDFNode;
        return ((RDFNode) o).jenaNode.equals(jenaNode);
    }

    @Override
    public String toString() {
        return jenaNode.toString();
    }

    @Override
    public List<ExprValue> getAnnotation(URI key) throws UnsupportedFeatureException {
        throw new UnsupportedFeatureException();
    }

    @Override
    public List<Annotation> getAnnotation() throws UnsupportedFeatureException {
        throw new UnsupportedFeatureException();
    }

    @Override
    public JenaRDF livesIn() {
        KnowledgeBase k = this.kb;
        assert k instanceof JenaRDF;
        return (JenaRDF) k;
    }

    @Override
    public Expr inKnowledgeBase(KnowledgeBase other) throws BackendException, UnsupportedFeatureException {
        assert other instanceof JenaRDF;
        return new RDFNode((JenaRDF) other, this.jenaNode);
    }
}
