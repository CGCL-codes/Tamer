package com.hp.hpl.guess.mascopt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import mascoptLib.abstractGraph.MascoptObject;
import mascoptLib.algos.abstractalgos.KShortestPath;
import mascoptLib.algos.digraph.KShortestPaths;
import mascoptLib.graphs.Arc;
import mascoptLib.graphs.ArcSet;
import mascoptLib.graphs.DiGraph;
import mascoptLib.graphs.Vertex;
import mascoptLib.graphs.VertexSet;
import com.hp.hpl.guess.DirectedEdge;
import com.hp.hpl.guess.Edge;
import com.hp.hpl.guess.Node;
import edu.uci.ics.jung.graph.impl.AbstractElement;

/**
 * this is a simple example of mapping to and from an extra
 * graph library.  In this case we are using Mascopt (a network
 * optimization package)
 */
public class MascoptDiGraphProxy extends DiGraph {

    /**
     * maps a guess object to mascopt.  A little bit of 
     * a hack because a guess object (like a bidirected/undirected
     * edge) will become two mascopt objects.  The map 
     * will therefore be:
     * guess object -> mascopt object
     * guess object -> Arc[2] (two arc objects)
     */
    public HashMap<AbstractElement, Object> g2m = new HashMap<AbstractElement, Object>();

    /**
     * maps a mascopt object to a guess object
     * mascopt -> guess, possibly multiple
     * mascopt obj to single guess obj
     */
    public HashMap<MascoptObject, AbstractElement> m2g = new HashMap<MascoptObject, AbstractElement>();

    /**
     * creates a proxy for the given graph object
     */
    public static MascoptDiGraphProxy createProxy(com.hp.hpl.guess.Graph g) {
        HashMap<AbstractElement, Object> g2m = new HashMap<AbstractElement, Object>();
        HashMap<MascoptObject, AbstractElement> m2g = new HashMap<MascoptObject, AbstractElement>();
        VertexSet vs = new VertexSet();
        Iterator<Node> nodes = g.getNodes().iterator();
        while (nodes.hasNext()) {
            Node node = (Node) nodes.next();
            Vertex v = (Vertex) g2m.get(node);
            if (v == null) {
                v = new Vertex();
                g2m.put(node, v);
                m2g.put(v, node);
            }
            vs.add(v);
        }
        ArcSet as = new ArcSet(vs);
        as.setValue(KShortestPath.WEIGHT, "1");
        KShortestPaths.NAME_OF_VALUE = "poids";
        Iterator<Edge> edges = g.getEdges().iterator();
        while (edges.hasNext()) {
            Edge e = (Edge) edges.next();
            double weight = ((Double) e.__getattr__("weight")).doubleValue();
            if (g2m.containsKey(e)) {
                Object o = g2m.get(e);
                if (o instanceof Arc) {
                    ((Arc) o).setDouValue("poids", weight);
                    as.add((Arc) o);
                } else {
                    Arc[] aset = (Arc[]) o;
                    Arc a1 = aset[0];
                    Arc a2 = aset[1];
                    a1.setDouValue("poids", weight);
                    as.add(a1);
                    a2.setDouValue("poids", weight);
                    as.add(a2);
                }
            } else {
                if (e instanceof DirectedEdge) {
                    Vertex head = (Vertex) g2m.get(((DirectedEdge) e).getDestination());
                    Vertex tail = (Vertex) g2m.get(((DirectedEdge) e).getSource());
                    Arc a = new Arc(tail, head);
                    a.setDouValue("poids", weight);
                    System.out.println(a + " " + weight);
                    g2m.put(e, a);
                    m2g.put(a, e);
                    as.add(a);
                } else {
                    Arc[] aset = new Arc[2];
                    Vertex head = (Vertex) g2m.get(((Edge) e).getNode1());
                    Vertex tail = (Vertex) g2m.get(((Edge) e).getNode2());
                    Arc a1 = new Arc(tail, head);
                    a1.setDouValue("poids", weight);
                    aset[0] = a1;
                    Arc a2 = new Arc(head, tail);
                    a2.setDouValue("poids", weight);
                    aset[0] = a2;
                    g2m.put(e, aset);
                    m2g.put(a1, e);
                    m2g.put(a2, e);
                    as.add(a1);
                    as.add(a2);
                }
            }
        }
        MascoptDiGraphProxy toRet = new MascoptDiGraphProxy(vs, as);
        toRet.m2g = m2g;
        toRet.g2m = g2m;
        return (toRet);
    }

    /**
     * dummy overloaded constructor
     */
    public MascoptDiGraphProxy(VertexSet vs, ArcSet as) {
        super(vs, as);
    }

    /**
     * returns an ordered vector of k shortest paths from s to t
     * @param k the (max) number of shortest paths
     * @param s the source
     * @param t the target
     * @return a vector of vectors of edges
     */
    public Vector<Vector<AbstractElement>> kShortestPaths(int k, Node s, Node t) {
        return (kShortestPaths(k, s, t, null));
    }

    /**
     * returns an ordered vector of k shortest paths from s to t,
     * inserts the weights as a Double into the weights hashmap
     * if not null
     * @param k the (max) number of shortest paths
     * @param s the source
     * @param t the target
     * @param weights if not null, inserts the Double weight
     * of each path
     * @return a vector of vectors of edges
     */
    public Vector<Vector<AbstractElement>> kShortestPaths(int k, Node s, Node t, HashMap<Vector<AbstractElement>, Double> weights) {
        Vector<Vector<AbstractElement>> paths = new Vector<Vector<AbstractElement>>();
        KShortestPaths ksp = new KShortestPaths(this, k);
        ksp.run((Vertex) g2m.get(s), (Vertex) g2m.get(t));
        for (int i = 0; i < ksp.numberOfComputedPaths(); i++) {
            ArcSet as = ksp.getShortestPath(i).getArcSet();
            Vector<AbstractElement> path = new Vector<AbstractElement>();
            Iterator<?> it = as.iterator();
            while (it.hasNext()) {
                Arc a = (Arc) it.next();
                path.add(m2g.get(a));
            }
            paths.add(path);
            if (weights != null) {
                weights.put(path, new Double(ksp.getWeight(i)));
            }
        }
        return (paths);
    }
}
