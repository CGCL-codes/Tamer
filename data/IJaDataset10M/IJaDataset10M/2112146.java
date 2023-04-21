package de.uni_trier.st.nevada.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import sun.misc.Queue;
import de.uni_trier.st.nevada.graphs.PresentGraph;
import de.uni_trier.st.nevada.graphs.Int.Edge;
import de.uni_trier.st.nevada.graphs.Int.Graph;
import de.uni_trier.st.nevada.graphs.Int.Node;

public class AnalysisTools {

    private static HashMap<Graph, HashMap<Node, Number>> corhencySize = new HashMap<Graph, HashMap<Node, Number>>();

    private static HashMap<Graph, HashMap<Boolean, HashMap<Node, Number>>> closeness = new HashMap<Graph, HashMap<Boolean, HashMap<Node, Number>>>();

    private static HashMap<Graph, HashMap<Boolean, HashMap<Node, Number>>> betweeness = new HashMap<Graph, HashMap<Boolean, HashMap<Node, Number>>>();

    private static HashMap<Graph, HashMap<Boolean, Boolean>> centralityTested = new HashMap<Graph, HashMap<Boolean, Boolean>>();

    private static HashMap<Graph, HashMap<Node, HashSet<Edge>>> directed_edges = new HashMap<Graph, HashMap<Node, HashSet<Edge>>>();

    private static HashMap<Graph, HashMap<Node, HashSet<Edge>>> undirected_edges = new HashMap<Graph, HashMap<Node, HashSet<Edge>>>();

    private static HashMap<Graph, HashMap<Node, HashSet<Node>>> directed_neighbours = new HashMap<Graph, HashMap<Node, HashSet<Node>>>();

    private static HashMap<Graph, HashMap<Node, HashSet<Node>>> undirected_neighbours = new HashMap<Graph, HashMap<Node, HashSet<Node>>>();

    private static HashMap<Graph, HashMap<Node, HashSet<Node>>> predecessor = new HashMap<Graph, HashMap<Node, HashSet<Node>>>();

    private static HashMap<Graph, HashMap<Node, HashMap<Node, HashSet<Edge>>>> directedAdjacencyMatrix = new HashMap<Graph, HashMap<Node, HashMap<Node, HashSet<Edge>>>>();

    private static HashMap<Graph, HashMap<Node, HashMap<Node, HashSet<Edge>>>> directedPredecessorsAdjacencyMatrix = new HashMap<Graph, HashMap<Node, HashMap<Node, HashSet<Edge>>>>();

    private static HashMap<Graph, HashMap<Node, HashMap<Node, LinkedList<LinkedList<Edge>>>>> shortestWays = new HashMap<Graph, HashMap<Node, HashMap<Node, LinkedList<LinkedList<Edge>>>>>();

    private static HashMap<Graph, HashMap<Node, HashMap<Node, LinkedList<LinkedList<Edge>>>>> allWays = new HashMap<Graph, HashMap<Node, HashMap<Node, LinkedList<LinkedList<Edge>>>>>();

    public AnalysisTools() {
    }

    public int getCorhencyComponentSize(Graph g, Node n, Boolean directed) {
        if (!corhencySize.containsKey(g) || !corhencySize.get(g).containsKey(n)) {
            this.builtCorhencyComponentSize(g, directed);
            if (!corhencySize.get(g).containsKey(n)) return -1;
        }
        return corhencySize.get(g).get(n).intValue();
    }

    public void builtCorhencyComponentSize(Graph g, Boolean directed) {
        HashSet<Node> node = new HashSet<Node>();
        Iterator gNodes = g.getNodes().iterator();
        while (gNodes.hasNext()) node.add((Node) gNodes.next());
        HashMap<Node, Number> init = new HashMap<Node, Number>();
        corhencySize.put(g, init);
        while (!node.isEmpty()) {
            Set<Node> nodes = new HashSet<Node>();
            Stack<Node> actNodes = new Stack<Node>();
            Node n = node.iterator().next();
            actNodes.push(n);
            nodes.add(n);
            while (!actNodes.isEmpty()) {
                Node act = actNodes.pop();
                nodes.add(act);
                actNodes.addAll(this.getNeighbours(g, act, directed));
                actNodes.removeAll(nodes);
            }
            for (Node act : nodes) {
                init.put(act, nodes.size());
            }
            node.removeAll(nodes);
        }
    }

    public double getCloseness(Graph g, Node n, Boolean directed) {
        if (!centralityTested.containsKey(g) || !centralityTested.get(g).containsKey(directed)) {
            this.builtCentralities(g, directed);
        }
        if (!closeness.get(g).get(directed).containsKey(n)) {
            return 0.0;
        }
        return closeness.get(g).get(directed).get(n).doubleValue();
    }

    public double getBetweeness(Graph g, Node n, Boolean directed) {
        if (!centralityTested.containsKey(g) || !centralityTested.get(g).containsKey(directed)) {
            this.builtCentralities(g, directed);
        }
        if (!betweeness.get(g).get(directed).containsKey(n)) {
            return 0.0;
        }
        return betweeness.get(g).get(directed).get(n).doubleValue();
    }

    public void builtCentralities(Graph g, boolean directed) {
        HashMap<Boolean, HashMap<Node, Number>> tmpmap1 = new HashMap<Boolean, HashMap<Node, Number>>();
        HashMap<Boolean, HashMap<Node, Number>> tmpmap2 = new HashMap<Boolean, HashMap<Node, Number>>();
        HashMap<Node, Number> mapCloseness = new HashMap<Node, Number>();
        HashMap<Node, Number> mapBetweeness = new HashMap<Node, Number>();
        tmpmap1.put(directed, mapCloseness);
        tmpmap2.put(directed, mapBetweeness);
        closeness.put(g, tmpmap1);
        betweeness.put(g, tmpmap2);
        for (Node s : g.getNodes()) {
            Stack<Node> S = new Stack<Node>();
            HashMap<Node, LinkedList<Node>> P = new HashMap<Node, LinkedList<Node>>();
            HashMap<Node, Number> o = new HashMap<Node, Number>();
            HashMap<Node, Number> d = new HashMap<Node, Number>();
            for (Node n : g.getNodes()) {
                o.put(n, 0);
                d.put(n, -1);
            }
            o.put(s, 1);
            d.put(s, 0);
            LinkedList<Node> Q = new LinkedList<Node>();
            Q.add(s);
            while (!Q.isEmpty()) {
                Node v = Q.remove();
                S.add(v);
                for (Node w : this.getNeighbours(g, v, directed)) {
                    if (!d.containsKey(w) || d.get(w).intValue() < 0) {
                        Q.add(w);
                        d.put(w, d.get(v).intValue() + 1);
                    }
                    if (d.get(w).intValue() == d.get(v).intValue() + 1) {
                        o.put(w, o.get(w).intValue() + o.get(v).intValue());
                        LinkedList<Node> foo;
                        if (P.containsKey(w)) {
                            foo = P.get(w);
                        } else {
                            foo = new LinkedList<Node>();
                        }
                        foo.add(v);
                        P.put(w, foo);
                    }
                }
            }
            HashMap<Node, Number> delta = new HashMap<Node, Number>();
            for (Node n : g.getNodes()) delta.put(n, 0);
            while (!S.empty()) {
                Node w = S.pop();
                if (P.containsKey(w)) {
                    for (Node v : P.get(w)) {
                        double foo = delta.get(v).doubleValue() + (o.get(v).doubleValue() * (1 + delta.get(w).doubleValue())) / o.get(w).doubleValue();
                        delta.put(v, foo);
                    }
                }
                if (w != s) {
                    if (mapBetweeness.containsKey(w)) {
                        mapBetweeness.put(w, mapBetweeness.get(w).doubleValue() + delta.get(w).doubleValue());
                    } else {
                        mapBetweeness.put(w, delta.get(w).doubleValue());
                    }
                    if (mapCloseness.containsKey(s)) {
                        mapCloseness.put(s, 1 / ((1 / mapCloseness.get(s).doubleValue()) + d.get(w).doubleValue()));
                    } else {
                        mapCloseness.put(s, 1 / d.get(w).doubleValue());
                    }
                }
            }
        }
        HashMap<Boolean, Boolean> tested = new HashMap<Boolean, Boolean>();
        tested.put(directed, true);
        centralityTested.put(g, tested);
    }

    public void buildAdjacencyMatrix(Graph g) {
        if (!directedAdjacencyMatrix.containsKey(g)) {
            directedAdjacencyMatrix.put(g, new HashMap<Node, HashMap<Node, HashSet<Edge>>>());
            directedPredecessorsAdjacencyMatrix.put(g, new HashMap<Node, HashMap<Node, HashSet<Edge>>>());
        }
        for (Edge e : g.getEdges()) {
            if (!directedPredecessorsAdjacencyMatrix.get(g).containsKey(e.getTarget())) {
                directedPredecessorsAdjacencyMatrix.get(g).put(e.getTarget(), new HashMap<Node, HashSet<Edge>>());
            }
            if (!directedPredecessorsAdjacencyMatrix.get(g).get(e.getTarget()).containsKey(e.getSource())) {
                directedPredecessorsAdjacencyMatrix.get(g).get(e.getTarget()).put(e.getSource(), new HashSet<Edge>());
            }
            if (!directedAdjacencyMatrix.get(g).containsKey(e.getSource())) {
                directedAdjacencyMatrix.get(g).put(e.getSource(), new HashMap<Node, HashSet<Edge>>());
            }
            if (!directedAdjacencyMatrix.get(g).get(e.getSource()).containsKey(e.getTarget())) {
                directedAdjacencyMatrix.get(g).get(e.getSource()).put(e.getTarget(), new HashSet<Edge>());
            }
            directedAdjacencyMatrix.get(g).get(e.getSource()).get(e.getTarget()).add(e);
            directedPredecessorsAdjacencyMatrix.get(g).get(e.getTarget()).get(e.getSource()).add(e);
        }
    }

    public HashSet<Edge> getAllEdgesBetween(Graph g, Node source, Node target, boolean directed) {
        HashSet<Edge> result = new HashSet<Edge>();
        if (!directedAdjacencyMatrix.containsKey(g)) this.buildAdjacencyMatrix(g);
        if (directed) {
            if (directedAdjacencyMatrix.get(g).containsKey(source) && directedAdjacencyMatrix.get(g).get(source).containsKey(target)) result.addAll(directedAdjacencyMatrix.get(g).get(source).get(target));
        } else {
            if (directedAdjacencyMatrix.get(g).containsKey(source) && directedAdjacencyMatrix.get(g).get(source).containsKey(target)) result.addAll(directedAdjacencyMatrix.get(g).get(source).get(target));
            if (directedAdjacencyMatrix.get(g).containsKey(target) && directedAdjacencyMatrix.get(g).get(target).containsKey(source)) result.addAll(directedAdjacencyMatrix.get(g).get(target).get(source));
        }
        return result;
    }

    public Set<Edge> getEdges(Graph g, Node n, boolean directed) {
        HashSet<Edge> result = new HashSet<Edge>();
        if (!directedAdjacencyMatrix.containsKey(g)) this.buildAdjacencyMatrix(g);
        if (directedAdjacencyMatrix.get(g).containsKey(n)) for (HashSet<Edge> edges : directedAdjacencyMatrix.get(g).get(n).values()) {
            result.addAll(edges);
        }
        if (!directed) {
            if (directedPredecessorsAdjacencyMatrix.get(g).containsKey(n)) for (HashSet<Edge> edges : directedPredecessorsAdjacencyMatrix.get(g).get(n).values()) {
                result.addAll(edges);
            }
        }
        return result;
    }

    public Set<Node> getNeighbours(Graph g, Node n, boolean directed) {
        HashSet<Node> result = new HashSet<Node>();
        if (!directedAdjacencyMatrix.containsKey(g)) this.buildAdjacencyMatrix(g);
        if (directedAdjacencyMatrix.get(g).containsKey(n)) for (Node nodes : directedAdjacencyMatrix.get(g).get(n).keySet()) {
            result.add(nodes);
        }
        if (!directed) {
            if (directedPredecessorsAdjacencyMatrix.get(g).containsKey(n)) for (Node nodes : directedPredecessorsAdjacencyMatrix.get(g).get(n).keySet()) {
                result.add(nodes);
            }
        }
        return result;
    }

    public Set<Node> getNeighboursForAll(Graph g, Set<Node> nodes, boolean directed) {
        HashSet<Node> result = new HashSet<Node>();
        if (!directedAdjacencyMatrix.containsKey(g)) this.buildAdjacencyMatrix(g);
        for (Node n : nodes) {
            if (directedAdjacencyMatrix.get(g).containsKey(n)) for (Node nodes2 : directedAdjacencyMatrix.get(g).get(n).keySet()) {
                result.add(nodes2);
            }
            if (!directed) {
                if (directedPredecessorsAdjacencyMatrix.get(g).containsKey(n)) for (Node nodes2 : directedPredecessorsAdjacencyMatrix.get(g).get(n).keySet()) {
                    result.add(nodes2);
                }
            }
        }
        return result;
    }

    public Set<Node> getPredecessors(Graph g, Node n) {
        HashSet<Node> result = new HashSet<Node>();
        if (!directedAdjacencyMatrix.containsKey(g)) this.buildAdjacencyMatrix(g);
        if (directedPredecessorsAdjacencyMatrix.get(g).containsKey(n)) for (Node nodes : directedPredecessorsAdjacencyMatrix.get(g).get(n).keySet()) {
            result.add(nodes);
        }
        return result;
    }

    public Graph cloneGraph(Graph g) {
        Graph newG = new PresentGraph();
        newG.addEdges(g.getEdges());
        newG.addNodes(g.getNodes());
        return newG;
    }

    public Set<Node> getNodesFromAllGraphs(Set<Graph> gs) {
        HashSet<Node> nodes = new HashSet<Node>();
        for (Graph g : gs) {
            nodes.addAll(g.getNodes());
        }
        return nodes;
    }

    public Boolean isGraphWithSameNodesInGraphs(Graph g, Set<Graph> gs) {
        for (Graph g_temp : gs) {
            if (g.getNodes().equals(g_temp.getNodes())) return true;
        }
        return false;
    }

    public void update() {
        directed_edges.clear();
        undirected_edges.clear();
        directed_neighbours.clear();
        undirected_neighbours.clear();
        shortestWays.clear();
        allWays.clear();
        directedAdjacencyMatrix.clear();
        directedPredecessorsAdjacencyMatrix.clear();
        centralityTested.clear();
        corhencySize.clear();
        predecessor.clear();
    }
}
