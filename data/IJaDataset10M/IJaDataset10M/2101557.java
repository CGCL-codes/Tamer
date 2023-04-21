package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;PROGRAM_ID&gt;
 * nodeToken1 -> &lt;DOT&gt;
 * programName -> ProgramName()
 * nodeOptional -> [ [ &lt;IS&gt; ] &lt;INITIAL&gt; [ &lt;PROGRAM&gt; ] ]
 * nodeToken2 -> &lt;DOT&gt;
 * </PRE>
 */
public class ProgramIdParagraph extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public NodeToken nodeToken1;

    public ProgramName programName;

    public NodeOptional nodeOptional;

    public NodeToken nodeToken2;

    public ProgramIdParagraph(NodeToken n0, NodeToken n1, ProgramName n2, NodeOptional n3, NodeToken n4) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        nodeToken1 = n1;
        if (nodeToken1 != null) nodeToken1.setParent(this);
        programName = n2;
        if (programName != null) programName.setParent(this);
        nodeOptional = n3;
        if (nodeOptional != null) nodeOptional.setParent(this);
        nodeToken2 = n4;
        if (nodeToken2 != null) nodeToken2.setParent(this);
    }

    public ProgramIdParagraph() {
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
