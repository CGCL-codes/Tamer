package util;

import java.util.*;

public class Graph<NodeType> {

    LinkedHashSet<NodeType> nodes = new LinkedHashSet<NodeType>();

    LinkedHashMap<NodeType, LinkedHashSet<NodeType>> preds = new LinkedHashMap<NodeType, LinkedHashSet<NodeType>>(), succs = new LinkedHashMap<NodeType, LinkedHashSet<NodeType>>();

    public Graph() {
    }

    public boolean hasNode(NodeType n) {
        return nodes.contains(n);
    }

    public void addNode(NodeType n) {
        nodes.add(n);
    }

    public void addDirectedEdge(NodeType u, NodeType v) {
        if (!hasNode(u)) addNode(u);
        if (!hasNode(v)) addNode(v);
        if (!succs.containsKey(u)) succs.put(u, new LinkedHashSet<NodeType>());
        succs.get(u).add(v);
        if (!preds.containsKey(v)) preds.put(v, new LinkedHashSet<NodeType>());
        preds.get(v).add(u);
    }

    public void addEdge(NodeType u, NodeType v) {
        addDirectedEdge(u, v);
    }

    public void addUndirectedEdge(NodeType u, NodeType v) {
        addDirectedEdge(u, v);
        addDirectedEdge(v, u);
    }

    public void removeDirectedEdge(NodeType u, NodeType v) {
        if (succs.containsKey(u)) {
            succs.get(u).remove(v);
            preds.get(v).remove(u);
        }
    }

    public void removeUndirectedEdge(NodeType u, NodeType v) {
        removeDirectedEdge(u, v);
        removeDirectedEdge(v, u);
    }

    public void removeNode(NodeType n) {
        LinkedHashSet<NodeType> t = succs.get(n);
        if (t != null) {
            t = new LinkedHashSet<NodeType>(t);
            for (NodeType v : t) removeDirectedEdge(n, v);
            succs.remove(n);
        }
        t = preds.get(n);
        if (t != null) {
            t = new LinkedHashSet<NodeType>(t);
            for (NodeType u : t) removeDirectedEdge(u, n);
            preds.remove(n);
        }
        nodes.remove(n);
    }

    public boolean isDirectedEdge(NodeType u, NodeType v) {
        LinkedHashSet<NodeType> t = succs.get(u);
        if (t == null) return false;
        return t.contains(v);
    }

    public boolean isEdge(NodeType u, NodeType v) {
        return isDirectedEdge(u, v);
    }

    public Set<NodeType> pred(NodeType n) {
        Set<NodeType> ret = preds.get(n);
        if (ret == null) return new LinkedHashSet<NodeType>(); else return ret;
    }

    public Set<NodeType> succ(NodeType n) {
        Set<NodeType> ret = succs.get(n);
        if (ret == null) return new LinkedHashSet<NodeType>(); else return ret;
    }

    public Set<NodeType> nodes() {
        return nodes;
    }

    public int inDegree(NodeType n) {
        return pred(n).size();
    }

    public int outDegree(NodeType n) {
        return succ(n).size();
    }

    public Set<NodeType> heads() {
        Set<NodeType> ret = new LinkedHashSet<NodeType>();
        for (NodeType n : nodes) if (inDegree(n) == 0) ret.add(n);
        return ret;
    }

    public Set<NodeType> tails() {
        Set<NodeType> ret = new LinkedHashSet<NodeType>();
        for (NodeType n : nodes) if (outDegree(n) == 0) ret.add(n);
        return ret;
    }

    boolean isPath(NodeType from, NodeType to, Set<NodeType> visited) {
        if (visited.contains(from)) return false;
        visited.add(from);
        if (from.equals(to)) return true;
        Set<NodeType> succs = succ(from);
        for (NodeType n : succs) {
            if (isPath(n, to, visited)) return true;
        }
        return false;
    }

    public boolean isLoopEdge(NodeType u, NodeType v) {
        if (!isEdge(u, v)) return false;
        Set<NodeType> visited = new LinkedHashSet<NodeType>();
        return isPath(v, u, visited);
    }

    public List<NodeType> topologicalSort() {
        Queue<NodeType> q = new LinkedList<NodeType>(heads());
        List<NodeType> ret = new ArrayList<NodeType>();
        Map<NodeType, Integer> inDegrees = new LinkedHashMap<NodeType, Integer>();
        for (NodeType n : nodes()) inDegrees.put(n, new Integer(inDegree(n)));
        while (!q.isEmpty()) {
            NodeType n = q.poll();
            ret.add(n);
            for (NodeType d : succ(n)) {
                int i = inDegrees.get(d).intValue() - 1;
                inDegrees.put(d, new Integer(i));
                if (i == 0) q.offer(d);
            }
        }
        return ret;
    }
}
