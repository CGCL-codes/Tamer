package org.openrdf.query.algebra;

/**
 * The NAMESPACE function, which selects the namespace of URIs.
 * 
 * @author Arjohn Kampman
 */
public class Namespace extends UnaryValueOperator {

    public Namespace() {
    }

    public Namespace(ValueExpr arg) {
        super(arg);
    }

    public <X extends Exception> void visit(QueryModelVisitor<X> visitor) throws X {
        visitor.meet(this);
    }

    @Override
    public Namespace clone() {
        return (Namespace) super.clone();
    }
}
