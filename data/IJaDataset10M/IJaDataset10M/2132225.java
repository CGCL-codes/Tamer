package gov.nasa.ltl.graph;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * DOCUMENT ME!
 */
public class SCC {

    public static void help() {
        System.err.println("usage:");
        System.err.println("\tDegenalize [-join|-degeneralize] [outfile]");
        System.exit(1);
    }

    public static void main(String[] args) {
        String outname = null;
        for (int i = 0, l = args.length; i < l; i++) {
            if (outname == null) {
                outname = args[i];
            } else {
                help();
            }
        }
        try {
            Graph g = Graph.load("out.sm");
            List<List<Node>> scc = scc(g);
            for (Iterator<List<Node>> i = scc.iterator(); i.hasNext(); ) {
                List<Node> l = i.next();
                System.out.println("component:");
                for (Iterator<Node> j = l.iterator(); j.hasNext(); ) {
                    Node n = j.next();
                    System.out.println("  " + n.getStringAttribute("label"));
                }
                System.out.println();
            }
            if (outname == null) {
                g.save();
            } else {
                g.save(outname);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static void print(List<List<Node>> sccs) {
        System.out.println("Strongly connected components:");
        int cnt = 0;
        for (Iterator<List<Node>> i = sccs.iterator(); i.hasNext(); ) {
            List<Node> scc = i.next();
            System.out.println("\tSCC #" + (cnt++));
            for (Iterator<Node> j = scc.iterator(); j.hasNext(); ) {
                Node n = j.next();
                System.out.println("\t\t" + n.getId() + " - " + n.getStringAttribute("label"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static List<List<Node>> scc(Graph g) {
        Node init = g.getInit();
        if (init == null) {
            return new LinkedList<List<Node>>();
        }
        init.setBooleanAttribute("_reached", true);
        SCCState s = new SCCState();
        visit(init, s);
        final List<Node>[] scc = new List[s.SCC];
        for (int i = 0; i < s.SCC; i++) {
            scc[i] = new LinkedList<Node>();
        }
        g.forAllNodes(new EmptyVisitor() {

            public void visitNode(Node n) {
                scc[n.getIntAttribute("_scc")].add(n);
                n.setBooleanAttribute("_reached", false);
                n.setBooleanAttribute("_dfsnum", false);
                n.setBooleanAttribute("_low", false);
                n.setBooleanAttribute("_scc", false);
            }
        });
        List<List<Node>> list = new LinkedList<List<Node>>();
        for (int i = 0; i < s.SCC; i++) {
            list.add(scc[i]);
        }
        return list;
    }

    private static void visit(Node p, SCCState s) {
        s.L.add(0, p);
        p.setIntAttribute("_dfsnum", s.N);
        p.setIntAttribute("_low", s.N);
        s.N++;
        for (Iterator<Edge> i = p.getOutgoingEdges().iterator(); i.hasNext(); ) {
            Edge e = i.next();
            Node q = e.getNext();
            if (!q.getBooleanAttribute("_reached")) {
                q.setBooleanAttribute("_reached", true);
                visit(q, s);
                p.setIntAttribute("_low", Math.min(p.getIntAttribute("_low"), q.getIntAttribute("_low")));
            } else if (q.getIntAttribute("_dfsnum") < p.getIntAttribute("_dfsnum")) {
                if (s.L.contains(q)) {
                    p.setIntAttribute("_low", Math.min(p.getIntAttribute("_low"), q.getIntAttribute("_dfsnum")));
                }
            }
        }
        if (p.getIntAttribute("_low") == p.getIntAttribute("_dfsnum")) {
            Node v;
            do {
                v = s.L.remove(0);
                v.setIntAttribute("_scc", s.SCC);
            } while (v != p);
            s.SCC++;
        }
    }

    /**
   * DOCUMENT ME!
   */
    private static class SCCState {

        public int N = 0;

        public int SCC = 0;

        public List<Node> L = new LinkedList<Node>();
    }
}
