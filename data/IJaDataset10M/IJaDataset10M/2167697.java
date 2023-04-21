package org.openrdf.query.algebra;

import java.util.Set;

/**
 * A generalized projection (allowing the bindings to be renamed) on a tuple
 * expression.
 */
public class Projection extends UnaryTupleOperator {

    private ProjectionElemList projElemList = new ProjectionElemList();

    public Projection() {
    }

    public Projection(TupleExpr arg) {
        super(arg);
    }

    public Projection(TupleExpr arg, ProjectionElemList elements) {
        this(arg);
        setProjectionElemList(elements);
    }

    public ProjectionElemList getProjectionElemList() {
        return projElemList;
    }

    public void setProjectionElemList(ProjectionElemList projElemList) {
        this.projElemList = projElemList;
        projElemList.setParentNode(this);
    }

    @Override
    public Set<String> getBindingNames() {
        return projElemList.getTargetNames();
    }

    public <X extends Exception> void visit(QueryModelVisitor<X> visitor) throws X {
        visitor.meet(this);
    }

    @Override
    public <X extends Exception> void visitChildren(QueryModelVisitor<X> visitor) throws X {
        projElemList.visit(visitor);
        super.visitChildren(visitor);
    }

    @Override
    public void replaceChildNode(QueryModelNode current, QueryModelNode replacement) {
        if (projElemList == current) {
            setProjectionElemList((ProjectionElemList) replacement);
        } else {
            super.replaceChildNode(current, replacement);
        }
    }

    @Override
    public Projection clone() {
        Projection clone = (Projection) super.clone();
        clone.setProjectionElemList(getProjectionElemList().clone());
        return clone;
    }
}
