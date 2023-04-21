package clp.parser.nlp;

public class ParentChildIndex {

    private NLPNode parent;

    private NLPNode node;

    private int nodeIndex;

    public ParentChildIndex(NLPNode node, NLPNode parent, int nodeIndex) {
        this.node = node;
        this.parent = parent;
        this.nodeIndex = nodeIndex;
    }

    public NLPNode getNode() {
        return node;
    }

    public void setNode(NLPNode node) {
        this.node = node;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public NLPNode getParent() {
        return parent;
    }

    public void setParent(NLPNode parent) {
        this.parent = parent;
    }
}
