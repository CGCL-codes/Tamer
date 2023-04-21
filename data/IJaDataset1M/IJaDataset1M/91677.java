package com.res.cobol.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;USE&gt;
 * nodeChoice -> ( [ &lt;FOR&gt; ] &lt;DEBUGGING&gt; [ &lt;ON&gt; ] ( ( Identifier() | &lt;ALL&gt; [ &lt;REFERENCES&gt; ] [ &lt;OF&gt; ] Identifier() | FileName() | ProcedureName() )+ | &lt;ALL&gt; &lt;PROCEDURES&gt; ) | [ &lt;GLOBAL&gt; ] &lt;AFTER&gt; [ &lt;STANDARD&gt; ] ( ( &lt;EXCEPTION&gt; | &lt;ERROR&gt; ) | [ ( &lt;BEGINNING&gt; | &lt;ENDING&gt; ) ] [ ( &lt;FILE&gt; | &lt;REEL&gt; | &lt;UNIT&gt; ) ] &lt;LABEL&gt; ) &lt;PROCEDURE&gt; [ &lt;ON&gt; ] ( ( FileName() [ &lt;COMMACHAR&gt; ] )+ | &lt;INPUT&gt; | &lt;OUTPUT&gt; | &lt;I_O&gt; | &lt;EXTEND&gt; ) )
 * </PRE>
 */
public class UseStatement extends com.res.cobol.RESNode implements Node {

    private Node parent;

    public NodeToken nodeToken;

    public NodeChoice nodeChoice;

    public UseStatement(NodeToken n0, NodeChoice n1) {
        nodeToken = n0;
        if (nodeToken != null) nodeToken.setParent(this);
        nodeChoice = n1;
        if (nodeChoice != null) nodeChoice.setParent(this);
    }

    public UseStatement() {
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
