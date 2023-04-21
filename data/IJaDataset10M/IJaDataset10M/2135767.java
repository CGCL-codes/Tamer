package alefpp.parser.syntaxtree;

/**
 * The interface which NodeList, NodeListOptional, and NodeSequence
 * implement.
 */
@SuppressWarnings("all")
public interface NodeListInterface extends Node {

    public void addNode(Node n);

    public Node elementAt(int i);

    public java.util.Enumeration<Node> elements();

    public int size();

    public void accept(alefpp.parser.visitor.Visitor v);

    public <R, A> R accept(alefpp.parser.visitor.GJVisitor<R, A> v, A argu);

    public <R> R accept(alefpp.parser.visitor.GJNoArguVisitor<R> v);

    public <A> void accept(alefpp.parser.visitor.GJVoidVisitor<A> v, A argu);
}
