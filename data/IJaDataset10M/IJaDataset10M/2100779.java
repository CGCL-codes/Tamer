package org.openrdf.query.algebra;

/**
 * A boolean NOT operator operating on a boolean expressions.
 */
public class Not extends UnaryValueOperator {

    public Not() {
    }

    public Not(ValueExpr arg) {
        super(arg);
    }

    public <X extends Exception> void visit(QueryModelVisitor<X> visitor) throws X {
        visitor.meet(this);
    }

    @Override
    public Not clone() {
        return (Not) super.clone();
    }
}
