package org.jacex.model;

import java.util.ArrayList;
import java.util.List;

public class AccessNode implements Node {

    public final Node base;

    public final String name;

    public final List<Node> args = new ArrayList<Node>();

    public AccessNode(Node node, String name) {
        this.base = node;
        this.name = name;
    }

    @Override
    public String dump() {
        StringBuilder b = new StringBuilder();
        b.append("Call{ ");
        b.append(base != null ? base.dump() : "null");
        b.append(", ");
        b.append(name);
        b.append(", [ ");
        boolean first = true;
        for (Node n : args) {
            if (!first) b.append(", ");
            b.append(n);
            first = false;
        }
        b.append(" ] }");
        return b.toString();
    }

    @Override
    public void apply(NodeVisitor visitor) {
        visitor.visitAccessNode(this);
    }

    @Override
    public <ResultType> ResultType apply(NodeFunction<ResultType> function) {
        return function.visitAccessNode(this);
    }

    public void applyToBase(NodeVisitor visitor) {
        base.apply(visitor);
    }
}
