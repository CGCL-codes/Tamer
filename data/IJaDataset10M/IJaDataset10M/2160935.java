package uk.ac.warwick.dcs.cokefolk.parser.syntaxtree;

/**
 * Grammar production:
 * nodeToken -> <DELETE>
 * target -> Target()
 * nodeOptional -> [ <WHERE> BoolExp() ]
 */
public class RelationDelete implements Node {

    public NodeToken nodeToken;

    public Target target;

    public NodeOptional nodeOptional;

    public RelationDelete(NodeToken n0, Target n1, NodeOptional n2) {
        nodeToken = n0;
        target = n1;
        nodeOptional = n2;
    }

    public RelationDelete(Target n0, NodeOptional n1) {
        nodeToken = new NodeToken("DELETE");
        target = n0;
        nodeOptional = n1;
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
