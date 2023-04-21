package gov.nasa.ltl.graph;

import java.io.IOException;

/**
 * DOCUMENT ME!
 */
public class Degeneralize {

    public static Graph degeneralize(Graph g) {
        int nsets = g.getIntAttribute("nsets");
        String type = g.getStringAttribute("type");
        if (type.equals("gba")) {
            String ac = g.getStringAttribute("ac");
            if (ac.equals("nodes")) {
                if (nsets == 1) {
                    accept(g);
                } else {
                    Label.label(g);
                    Graph d = Generate.generate(nsets);
                    g = SynchronousProduct.product(g, d);
                }
            } else if (ac.equals("edges")) {
                Graph d = Generate.generate(nsets);
                g = SynchronousProduct.product(g, d);
            }
        } else if (!type.equals("ba")) {
            throw new RuntimeException("invalid graph type: " + type);
        }
        return g;
    }

    public static void help() {
        System.err.println("usage:");
        System.err.println("\tDegenalize [outfile]");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("usage:");
            System.out.println("\tjava gov.nasa.ltl.graph.Degeneralize [<filename>]");
            return;
        }
        Graph g = null;
        try {
            if (args.length == 0) {
                g = Graph.load();
            } else {
                g = Graph.load(args[0]);
            }
        } catch (IOException e) {
            System.out.println("Can't load the graph.");
            return;
        }
        g = degeneralize(g);
        g.save();
    }

    private static void accept(Graph g) {
        g.setBooleanAttribute("nsets", false);
        g.forAllNodes(new EmptyVisitor() {

            public void visitNode(Node n) {
                if (n.getBooleanAttribute("acc0")) {
                    n.setBooleanAttribute("accepting", true);
                    n.setBooleanAttribute("acc0", false);
                }
            }
        });
    }
}
