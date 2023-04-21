package edu.ksu.cis.util.graph.layout;

import edu.ksu.cis.util.graph.core.Graph;
import edu.ksu.cis.util.graph.core.Vertex;

public class LayPolyTree {

    public static int arrange(Vertex V, Graph G, int d, int offset) {
        Vertex[] C = G.getChildren(V);
        if (C.length == 0) {
            V.setAttrib(0, offset);
            V.setAttrib(1, d * 50);
            return 50;
        }
        int w = 0;
        for (int i = 0; i < C.length; i++) {
            w += arrange(C[i], G, d + 1, offset + w);
        }
        V.setAttrib(0, offset + w / 2 - 25);
        V.setAttrib(1, d * 50);
        return w;
    }

    public static void apply(Graph G) {
        Vertex[] V = G.getVertices();
        int off = 0;
        for (int i = 0; i < V.length; i++) {
            V[i].setAttrib(0, 0);
            V[i].setAttrib(1, 0);
        }
        for (int i = 0; i < V.length; i++) {
            if (G.getParents(V[i]).length == 0) {
                off += arrange(V[i], G, 0, off);
            }
        }
    }
}
