package verilogx.syntaxtree;

/**
 * The interface which NodeList, NodeListOptional, and NodeSequence
 * implement.
 */
public interface NodeListInterface extends Node {

    public void addNode(Node n);

    public Node elementAt(int i);

    public java.util.Enumeration elements();

    public int size();
}
