package de.nameless.graphicEngine.model;

import java.util.Vector;

public class NEsmoothingGroup {

    public Vector<MS3DTriangle> triangles;

    public NEsmoothingGroup() {
        triangles = new Vector<MS3DTriangle>();
    }

    @Override
    public String toString() {
        return triangles.toString();
    }
}
