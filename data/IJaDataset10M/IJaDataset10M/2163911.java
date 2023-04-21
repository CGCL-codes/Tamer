package net.nexttext.renderer.util;

import java.util.ArrayList;
import java.util.logging.Logger;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * This class represents as its name indicates a planar subdivision.
 * 
 * Its uses are many, but to name a few, its good for triangulation of complex polygons 
 * (those with holes in them....).
 * 
 * To make a triangulation it is assumed that some subset of the edges form a closed polygon around
 * the rest of the triangulation. These points must be connected in counter-clockwise order,
 * that is the interior of the polygon they form lies to the left of every edge in it.
 * The internal representation of the planar subdivision does allow though to traverse the edges backwards,
 * since we use DCEL representation.
 * 
 * NOTE:
 *  - At the moment the planar subdivision does not accept anything but 1 or two manifold vertices. If you
 *    add more edges than that, stuff will break :-)
 * 
 * @author emanuel
 *
 */
public abstract class DoublyConnectedEdgeList<Vertex extends PlanarVertex, Edge extends PlanarEdge> {

    ArrayList<Vertex> vertices = new ArrayList<Vertex>();

    ArrayList<Edge> edges = new ArrayList<Edge>();

    public abstract Vertex createVertex(int index, PVector p);

    public abstract Edge createEdge(Vertex origin, boolean real);

    public Vertex addVertex(PVector p) {
        Vertex point = createVertex(vertices.size(), p);
        vertices.add(point);
        return point;
    }

    public Edge addEdge(int src_i, int dst_i) {
        Vertex src = vertices.get(src_i);
        Vertex dst = vertices.get(dst_i);
        Edge src_e = (Edge) src.getEdge(dst);
        boolean new_src_e = false;
        if (src_e == null) {
            src_e = createEdge(src, true);
            new_src_e = true;
        } else {
            src_e.realedge = true;
        }
        Edge dst_e = (Edge) dst.getEdge(src);
        boolean new_dst_e = false;
        if (dst_e == null) {
            dst_e = createEdge(dst, false);
            new_dst_e = true;
        } else {
        }
        src_e.setTwin(dst_e);
        if (new_src_e) src.addOutgoingEdge(src_e); else if (new_dst_e) throw new RuntimeException("Damng, created a twin to an existing edge, that can never happen");
        edges.add(src_e);
        edges.add(dst_e);
        return src_e;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }
}
