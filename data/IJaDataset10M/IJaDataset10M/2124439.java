package com.xith3d.scenegraph;

/**
 * TriangleFanArray defines attributes that apply to .
 */
public class TriangleFanArray extends GeometryStripArray {

    /**
     * Used by direct sub-classes to define where their capabilty
     * bit positions start. The first bit in a sub-class should be defined
     * as TriangleFanArray.LAST_CAPS_BIT_POSITION+1.
     */
    public static final int LAST_CAPS_BIT_POSITION = GeometryStripArray.LAST_CAPS_BIT_POSITION;

    public TriangleFanArray(int vertexCount, int vertexFormat, int[] stripVertexCounts) {
        super(GeomContainer.GEOM_TRI_FAN, vertexCount, vertexFormat, 0, null, stripVertexCounts);
    }

    public TriangleFanArray(int type, int vertexCount, int vertexFormat, int texCoordSetCount, int[] texCoordSetMap, int[] stripVertexCounts) {
        super(GeomContainer.GEOM_TRI_FAN, vertexCount, vertexFormat, texCoordSetCount, texCoordSetMap, stripVertexCounts);
    }

    public TriangleFanArray(int vertexCount, int vertexFormat, int texCoordSetCount, int[] texCoordSetMap, int[] stripVertexCounts) {
        super(GeomContainer.GEOM_TRI_FAN, vertexCount, vertexFormat, texCoordSetCount, texCoordSetMap, stripVertexCounts);
    }
}
