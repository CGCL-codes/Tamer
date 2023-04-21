package nz.ac.massey.se356.scotlandyard.graph;

/**
 * The Graph class provides a data structure based
 * on which the locations (points or nodes) and routes (edges)
 * are stored in the program
 * 
 * @author tmmcgrat
 * @version 1.0
 */
public class Graph {

    /** and adjacency matrix */
    private boolean[][] nodes;

    public Graph(int numberOfNodes) {
        this.nodes = new boolean[numberOfNodes][numberOfNodes];
    }

    /** connects two nodes in a non-directional graph */
    public void edge(int nodeA, int nodeB) {
        this.nodes[nodeA][nodeB] = this.nodes[nodeB][nodeA] = true;
    }

    /** returns a an array of nodes to which a given node is connected */
    public int[] getMoves(int node) {
        int[] result;
        int count = 0;
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[node][i]) count++;
        }
        result = new int[count];
        count = 0;
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[node][i]) result[count++] = i;
        }
        return result;
    }
}
