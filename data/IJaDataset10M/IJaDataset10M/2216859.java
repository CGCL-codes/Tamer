package com.versant.core.ejb.query;

/**
 * collection_member_expression.
 */
public class MemberCompNode extends Node {

    private Node arg;

    private boolean not;

    private PathNode path;

    public MemberCompNode(Node arg, boolean not, PathNode path) {
        this.arg = arg;
        this.not = not;
        this.path = path;
    }

    public Node getArg() {
        return arg;
    }

    public boolean isNot() {
        return not;
    }

    public PathNode getPath() {
        return path;
    }

    public Object arrive(NodeVisitor v, Object msg) {
        return v.arriveMemberCompNode(this, msg);
    }

    public String toStringImp() {
        StringBuffer s = new StringBuffer();
        s.append(arg);
        s.append(not ? " NOT MEMBER OF " : " MEMBER OF ");
        s.append(path);
        return s.toString();
    }

    public void resolve(ResolveContext rc) {
        arg.resolve(rc);
        path.resolve(rc);
    }
}
