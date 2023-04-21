package hr.fer.grafovi.model;

public class AdjacencyLists implements Graph {

    private int Vcnt;

    private int Ecnt;

    private final boolean digraph;

    private final boolean weighted;

    private Node adj[];

    public AdjacencyLists(int vcnt) {
        this(vcnt, false, false);
    }

    public AdjacencyLists(int vcnt, boolean weighted, boolean digraph) {
        this.weighted = weighted;
        this.digraph = digraph;
        Vcnt = vcnt;
        Ecnt = 0;
        adj = new Node[vcnt];
        ;
    }

    @Override
    public int E() {
        return Ecnt;
    }

    @Override
    public int V() {
        return Vcnt;
    }

    @Override
    public boolean directed() {
        return digraph;
    }

    @Override
    public boolean weighted() {
        return weighted;
    }

    @Override
    public Edge edge(int v, int w) {
        for (Node i = adj[v]; i != null; i = i.next) if (i.e.other(v) == w) return i.e;
        return null;
    }

    @Override
    public void insert(Edge e) {
        int v = e.getV();
        int w = e.getW();
        adj[v] = new Node(e, adj[v]);
        if (!digraph) adj[w] = new Node(e, adj[w]);
        Ecnt++;
    }

    @Override
    public void remove(Edge e) {
        int v = e.getV();
        int w = e.getW();
        if (adj[v] == null) return;
        if (adj[v].e.other(v) == w) {
            Ecnt--;
            adj[v] = adj[v].next;
        } else for (Node i = adj[v]; i.next != null; i = i.next) if (i.next.e.other(v) == w) {
            i.next = i.next.next;
            Ecnt--;
            break;
        }
        if (digraph) return;
        if (adj[w] == null) return;
        if (adj[w].e.other(w) == v) adj[w] = adj[w].next; else for (Node i = adj[w]; i.next != null; i = i.next) if (i.next.e.other(w) == v) {
            i.next = i.next.next;
            break;
        }
    }

    @Override
    public AdjList getAdjList(int v) {
        return new adjLinkedList(v);
    }

    private class adjLinkedList implements AdjList {

        private int v;

        private Node t;

        private adjLinkedList(int v) {
            this.v = v;
            t = null;
        }

        @Override
        public Edge beg() {
            t = adj[v];
            return t == null ? null : t.e;
        }

        @Override
        public Edge nxt() {
            if (t != null) t = t.next;
            return t == null ? null : t.e;
        }

        @Override
        public boolean end() {
            return t == null;
        }
    }

    private class Node {

        Edge e;

        Node next;

        private Node(Edge e, Node t) {
            this.e = e;
            next = t;
        }
    }
}
