package org.jnet.shapespecial;

import java.util.BitSet;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.jnet.g3d.Graphics3D;
import org.jnet.util.ArrayUtil;
import org.jnet.jvxl.data.JvxlData;
import org.jnet.jvxl.calc.MarchingSquares;
import org.jnet.shape.Mesh;

public class IsosurfaceMesh extends Mesh {

    JvxlData jvxlData = new JvxlData();

    public boolean hideBackground;

    public int realVertexCount;

    public int vertexIncrement = 1;

    public int firstRealVertex = -1;

    public boolean hasGridPoints;

    public float[] vertexValues;

    public short[] vertexColixes;

    IsosurfaceMesh(String thisID, Graphics3D g3d, short colix) {
        super(thisID, g3d, colix);
        haveCheckByte = true;
    }

    void clear(String meshType, boolean iAddGridPoints, boolean showTriangles) {
        super.clear(meshType);
        vertexColixes = null;
        vertexValues = null;
        assocGridPointMap = null;
        assocGridPointNormals = null;
        vertexSets = null;
        isColorSolid = true;
        firstRealVertex = -1;
        hasGridPoints = iAddGridPoints;
        showPoints = iAddGridPoints;
        this.showTriangles = showTriangles;
        jvxlData.jvxlSurfaceData = "";
        jvxlData.jvxlEdgeData = "";
        jvxlData.jvxlColorData = "";
        surfaceSet = null;
        nSets = 0;
    }

    void allocVertexColixes() {
        if (vertexColixes == null) {
            vertexColixes = new short[vertexCount];
            for (int i = vertexCount; --i >= 0; ) vertexColixes[i] = colix;
        }
        isColorSolid = false;
    }

    public void setColorSchemeSets() {
        allocVertexColixes();
        int n = 2;
        for (int i = 0; i < surfaceSet.length; i++) if (surfaceSet[i] != null) {
            int c = Graphics3D.getColorArgb(n++);
            short colix = Graphics3D.getColix(c);
            for (int j = 0; j < vertexCount; j++) if (surfaceSet[i].get(j)) vertexColixes[j] = colix;
        }
    }

    Hashtable assocGridPointMap;

    Hashtable assocGridPointNormals;

    int addVertexCopy(Point3f vertex, float value, int assocVertex, boolean associateNormals) {
        int vPt = addVertexCopy(vertex, value);
        switch(assocVertex) {
            case MarchingSquares.CONTOUR_POINT:
                if (firstRealVertex < 0) firstRealVertex = vPt;
                break;
            case MarchingSquares.VERTEX_POINT:
                hasGridPoints = true;
                break;
            case MarchingSquares.EDGE_POINT:
                vertexIncrement = 3;
                break;
            default:
                if (firstRealVertex < 0) firstRealVertex = vPt;
                if (associateNormals) {
                    if (assocGridPointMap == null) {
                        assocGridPointMap = new Hashtable();
                        assocGridPointNormals = new Hashtable();
                    }
                    Integer key = new Integer(assocVertex);
                    assocGridPointMap.put(new Integer(vPt), key);
                    if (!assocGridPointNormals.containsKey(key)) assocGridPointNormals.put(key, new Vector3f(0, 0, 0));
                }
        }
        return vPt;
    }

    int addVertexCopy(Point3f vertex, float value) {
        if (vertexCount == 0) vertexValues = new float[SEED_COUNT]; else if (vertexCount >= vertexValues.length) vertexValues = (float[]) ArrayUtil.doubleLength(vertexValues);
        vertexValues[vertexCount] = value;
        return addVertexCopy(vertex);
    }

    public void setTranslucent(boolean isTranslucent, float iLevel) {
        super.setTranslucent(isTranslucent, iLevel);
        if (vertexColixes != null) for (int i = vertexCount; --i >= 0; ) vertexColixes[i] = Graphics3D.getColixTranslucent(vertexColixes[i], isTranslucent, iLevel);
    }

    void addTriangleCheck(int vertexA, int vertexB, int vertexC, int check) {
        if (vertexValues != null && (Float.isNaN(vertexValues[vertexA]) || Float.isNaN(vertexValues[vertexB]) || Float.isNaN(vertexValues[vertexC]))) return;
        if (Float.isNaN(vertices[vertexA].x) || Float.isNaN(vertices[vertexB].x) || Float.isNaN(vertices[vertexC].x)) return;
        if (polygonCount == 0) polygonIndexes = new int[SEED_COUNT][]; else if (polygonCount == polygonIndexes.length) polygonIndexes = (int[][]) ArrayUtil.doubleLength(polygonIndexes);
        polygonIndexes[polygonCount++] = new int[] { vertexA, vertexB, vertexC, check };
    }

    void invalidateTriangles() {
        for (int i = polygonCount; --i >= 0; ) {
            int[] vertexIndexes = polygonIndexes[i];
            if (vertexIndexes == null) continue;
            int iA = vertexIndexes[0];
            int iB = vertexIndexes[1];
            int iC = vertexIndexes[2];
            if (Float.isNaN(vertexValues[iA]) || Float.isNaN(vertexValues[iB]) || Float.isNaN(vertexValues[iC])) polygonIndexes[i] = null;
        }
    }

    public BitSet[] surfaceSet;

    public int[] vertexSets;

    public int nSets = 0;

    public void sumVertexNormals(Vector3f[] vectorSums) {
        super.sumVertexNormals(vectorSums);
        if (assocGridPointMap != null) {
            Enumeration e = assocGridPointMap.keys();
            while (e.hasMoreElements()) {
                Integer I = (Integer) e.nextElement();
                ((Vector3f) assocGridPointNormals.get(assocGridPointMap.get(I))).add(vectorSums[I.intValue()]);
            }
            e = assocGridPointMap.keys();
            while (e.hasMoreElements()) {
                Integer I = (Integer) e.nextElement();
                vectorSums[I.intValue()] = ((Vector3f) assocGridPointNormals.get(assocGridPointMap.get(I)));
            }
        }
    }
}
