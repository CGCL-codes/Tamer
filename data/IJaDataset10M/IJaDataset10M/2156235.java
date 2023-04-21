package edu.uvm.cs.calendar.graph;

import java.util.Comparator;

/**
 * 
 * @author Jeremy Gustie
 * @version %I%, %G%
 * @since 1.0
 */
public class NodeLevelComparator implements Comparator {

    private Graph graph;

    public NodeLevelComparator(Graph graph) {
        this.graph = graph;
    }

    public int compare(Object o1, Object o2) {
        Node n1 = (Node) o1;
        Node n2 = (Node) o2;
        if (n1.equals(n2)) {
            return 0;
        } else if (graph.isBelow(n1, n2)) {
            return -1;
        } else {
            return 1;
        }
    }
}
