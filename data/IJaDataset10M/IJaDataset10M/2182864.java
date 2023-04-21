package stixar.graph;

class DiAdjList extends AdjList implements Digraph {

    public DiAdjList(Node[] nodes) {
        super(nodes);
    }

    public DiAdjList(Node[] nodes, int edgeCount) {
        super(nodes, edgeCount);
    }
}
