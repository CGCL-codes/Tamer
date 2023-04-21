package com.jpatch.boundary.tools;

import com.jpatch.entity.sds2.*;
import javax.vecmath.*;

public class ConstraintVertexTranslation {

    private final AbstractVertex vertex;

    private final Point3d startPosition = new Point3d();

    private final Vector3d vector = new Vector3d();

    public ConstraintVertexTranslation(AbstractVertex vertex, Vector3d vector) {
        this.vertex = vertex;
        vertex.getPosition(startPosition);
        this.vector.set(vector);
    }

    public ConstraintVertexTranslation(AbstractVertex vertex, Point3d target) {
        this.vertex = vertex;
        vertex.getPosition(startPosition);
        vector.sub(target, startPosition);
    }

    public void moveTo(double alpha) {
        vertex.setPosition(startPosition.x + vector.x * alpha, startPosition.y + vector.y * alpha, startPosition.z + vector.z * alpha);
    }

    public Vector3d getVector() {
        return vector;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ConstraintVertexTranslation) {
            return vertex == ((ConstraintVertexTranslation) o).vertex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(vertex);
    }
}
