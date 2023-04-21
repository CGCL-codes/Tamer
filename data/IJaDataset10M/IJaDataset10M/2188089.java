package com.jogamp.graph.geom;

/**
 * A Vertex with custom memory layout using custom factory. 
 */
public interface Vertex extends Cloneable {

    public static interface Factory<T extends Vertex> {

        T create();

        T create(float x, float y, float z, boolean onCurve);

        T create(float[] coordsBuffer, int offset, int length, boolean onCurve);
    }

    void setCoord(float x, float y, float z);

    /**
     * @see System#arraycopy(Object, int, Object, int, int) for thrown IndexOutOfBoundsException
     */
    void setCoord(float[] coordsBuffer, int offset, int length);

    float[] getCoord();

    void setX(float x);

    void setY(float y);

    void setZ(float z);

    float getX();

    float getY();

    float getZ();

    boolean isOnCurve();

    void setOnCurve(boolean onCurve);

    int getId();

    void setId(int id);

    float[] getTexCoord();

    void setTexCoord(float s, float t);

    /**
     * @see System#arraycopy(Object, int, Object, int, int) for thrown IndexOutOfBoundsException
     */
    void setTexCoord(float[] texCoordsBuffer, int offset, int length);

    /**
     * @param obj the Object to compare this Vertex with
     * @return true if {@code obj} is a Vertex and not null, on-curve flag is equal and has same vertex- and tex-coords. 
     */
    boolean equals(Object obj);

    /**
     * @return deep clone of this Vertex
     */
    Vertex clone();
}
