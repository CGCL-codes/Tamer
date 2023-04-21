package org.openrdf.query.algebra;

/**
 * A boolean OR operator operating on two boolean expressions.
 */
public class Or extends BinaryValueOperator {

    public Or() {
    }

    public Or(ValueExpr leftArg, ValueExpr rightArg) {
        super(leftArg, rightArg);
    }

    public <X extends Exception> void visit(QueryModelVisitor<X> visitor) throws X {
        visitor.meet(this);
    }

    @Override
    public Or clone() {
        return (Or) super.clone();
    }
}
