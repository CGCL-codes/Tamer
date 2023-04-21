package com.versant.core.jdo.query;

import com.versant.core.metadata.ClassMetaData;

/**
 * COUNT(*).
 */
public class AggregateCountStarNode extends AggregateNode {

    public AggregateCountStarNode(Node parent) {
        this.parent = parent;
    }

    public AggregateCountStarNode() {
    }

    /**
     * Resolve field refs and so on relative to the compiler. This must
     * recursively resolve any child nodes.
     */
    public void resolve(QueryParser comp, ClassMetaData cmd, boolean ordering) {
    }

    /**
     * Simplify this node tree as much as possible.
     */
    protected void normalizeImp() {
    }

    public String toString() {
        return super.toString() + "Type = count(*)";
    }

    public Object accept(NodeVisitor visitor, Object[] results) {
        return visitor.visitAggregateCountStarNode(this, results);
    }

    public Object arrive(NodeVisitor v, Object msg) {
        return v.arriveAggregateCountStarNode(this, msg);
    }
}
