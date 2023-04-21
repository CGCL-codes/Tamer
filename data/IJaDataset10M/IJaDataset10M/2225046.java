package edu.gsbme.geometrykernel.data.brep.topoGraph;

public class edge extends adjObject {

    public adjObject v1;

    public adjObject v2;

    public String up;

    public String down;

    public edge(String id) {
        super(id);
    }

    public String checkVertex(vertex vtx) {
        if (v1.equals(vtx)) return "v1"; else if (v2.equals(vtx)) return "v2"; else return null;
    }

    public vertex getOppositeVertex(vertex current) {
        if (v1.equals(current)) return (vertex) v2; else if (v2.equals(current)) return (vertex) v1; else return null;
    }
}
