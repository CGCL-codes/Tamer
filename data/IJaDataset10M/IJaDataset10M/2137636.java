package org.openrdf.query.algebra;

/**
 * The STR function, as defined in <a
 * href="http://www.w3.org/TR/rdf-sparql-query/#func-str">SPARQL Query Language
 * for RDF</a>; returns the label of literals or the string representation of
 * URIs.
 * 
 * @author Arjohn Kampman
 */
public class Str extends UnaryValueOperator {

    public Str() {
    }

    public Str(ValueExpr arg) {
        super(arg);
    }

    public <X extends Exception> void visit(QueryModelVisitor<X> visitor) throws X {
        visitor.meet(this);
    }

    @Override
    public Str clone() {
        return (Str) super.clone();
    }
}
