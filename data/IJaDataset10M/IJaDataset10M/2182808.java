package net.teqlo.search.generic;

import java.util.*;

/**
 * @author  jthwaite
 */
public class Test {

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.addGraphListener(Graph.GRAPH_EVENT_PRINTER);
        graph.addNode("zero");
        graph.addSimpleEdge("zero", "one", 1);
        graph.addSimpleEdge("one", "three", 4);
        graph.addSimpleEdge("zero", "two", 3);
        graph.addSimpleEdge("one", "nine", 10);
        graph.addSimpleEdge("two", "three", 1);
        graph.addSimpleEdge("three", "four", 1);
        graph.addSimpleEdge("four", "six", 1);
        graph.addSimpleEdge("one", "five", 3);
        graph.addSimpleEdge("five", "six", 4);
        graph.addSimpleEdge("six", "nine", 2);
        Search s = new AStarSearch(false);
        s.setGraph(graph);
        s.setStartSpec("zero");
        s.setGoal(new Search.SimpleGoal("nine"));
        s.setMaxCost(10);
        Collection edges = s.getEdges();
        if (edges == null) {
            System.out.println("No solution");
            return;
        }
        System.out.println("Found route cost " + s.getCost());
        for (Iterator it = edges.iterator(); it.hasNext(); ) {
            Edge edge = (Edge) it.next();
            System.out.println(edge);
        }
    }
}
