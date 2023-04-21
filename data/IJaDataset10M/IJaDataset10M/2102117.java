package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;K_SET&gt;
 * relObjectName -> RelObjectName()
 * nodeChoice -> ( &lt;K_TO&gt; | "=" )
 * arguments -> Arguments()
 * </PRE>
 */
public class SetVariableStatement extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public RelObjectName relObjectName;

    public NodeChoice nodeChoice;

    public Arguments arguments;

    public SetVariableStatement(NodeToken n0, RelObjectName n1, NodeChoice n2, Arguments n3) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        relObjectName = n1;
        if (relObjectName != null) relObjectName.setParent(this);
        nodeChoice = n2;
        if (nodeChoice != null) nodeChoice.setParent(this);
        arguments = n3;
        if (arguments != null) arguments.setParent(this);
    }

    public SetVariableStatement() {
    }

    public void accept(com.res.cobol.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(com.res.cobol.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(com.res.cobol.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(com.res.cobol.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }

    public void setParent(Node n) {
        parent = n;
    }

    public Node getParent() {
        return parent;
    }
}
