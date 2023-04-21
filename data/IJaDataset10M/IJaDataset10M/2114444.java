package uk.ac.warwick.dcs.cokefolk.parser.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;KEY&gt;
 * nodeToken1 -> "{"
 * attributeRefCommalist -> AttributeRefCommalist()
 * nodeToken2 -> "}"
 * </PRE>
 */
public class CandidateKeyDef implements Node {

    public NodeToken nodeToken;

    public NodeToken nodeToken1;

    public AttributeRefCommalist attributeRefCommalist;

    public NodeToken nodeToken2;

    public CandidateKeyDef(NodeToken n0, NodeToken n1, AttributeRefCommalist n2, NodeToken n3) {
        nodeToken = n0;
        nodeToken1 = n1;
        attributeRefCommalist = n2;
        nodeToken2 = n3;
    }

    public CandidateKeyDef(AttributeRefCommalist n0) {
        nodeToken = new NodeToken("KEY");
        nodeToken1 = new NodeToken("{");
        attributeRefCommalist = n0;
        nodeToken2 = new NodeToken("}");
    }

    public void accept(uk.ac.warwick.dcs.cokefolk.parser.visitor.Visitor v) {
        v.visit(this);
    }

    public <R, A> R accept(uk.ac.warwick.dcs.cokefolk.parser.visitor.GJVisitor<R, A> v, A argu) {
        return v.visit(this, argu);
    }

    public <R> R accept(uk.ac.warwick.dcs.cokefolk.parser.visitor.GJNoArguVisitor<R> v) {
        return v.visit(this);
    }

    public <A> void accept(uk.ac.warwick.dcs.cokefolk.parser.visitor.GJVoidVisitor<A> v, A argu) {
        v.visit(this, argu);
    }
}
