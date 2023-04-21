package de.grogra.imp3d;

import javax.vecmath.Point3f;
import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple3f;
import de.grogra.xl.util.BooleanList;
import de.grogra.xl.util.FloatList;
import de.grogra.xl.util.IntList;

public class IntersectionLine {

    IntList intersectionLine = new IntList();

    FloatList vertices = new FloatList();

    IntList clusters1 = new IntList();

    IntList clusters2 = new IntList();

    FloatList uvData1 = new FloatList();

    FloatList uvData2 = new FloatList();

    FloatList normalData1 = new FloatList();

    FloatList normalData2 = new FloatList();

    BooleanList isExteriorVertice = new BooleanList();

    /**
	 * This method adds the given vertex to the vertices Array if the vertex is not already present in the Array.
	 * It returns the position of the vertex inside the vertices Array.
	 * This method checks every vertex inside the Array for equality by comparing it within the given epsilon frame.
	 * 
	 * If the Vertex is already present in the array this method returns its position as a negative value minus 1.
	 * 
	 * Otherwise
	 * 
	 * @param vertex vertex given as a Tuple3f
	 * @return position of the vertex inside the array
	 */
    public IntersectionLine() {
    }

    float getVertexX(int vertPos) {
        return vertices.get(vertPos * 3);
    }

    float getVertexY(int vertPos) {
        return vertices.get(vertPos * 3 + 1);
    }

    float getVertexZ(int vertPos) {
        return vertices.get(vertPos * 3 + 2);
    }

    int segmentsCount() {
        return intersectionLine.size() / 2;
    }

    int getSegmentStart(int segmentPos) {
        return intersectionLine.get(segmentPos * 2);
    }

    int getSegmentEnd(int segmentPos) {
        return intersectionLine.get(segmentPos * 2 + 1);
    }

    boolean isPartOfSegment(int segmentPos, int vertPos) {
        if (getSegmentEnd(segmentPos) == vertPos || getSegmentStart(segmentPos) == vertPos) {
            return true;
        } else return false;
    }

    int getVerticesCount() {
        return vertices.size() / 3;
    }

    boolean isInteriorVertex(int vertPos) {
        return !isExteriorVertice.get(vertPos);
    }

    public void insertSegment(Tuple3f start, Tuple3f end, int cluster1, int cluster2, Tuple2f uvStart1, Tuple2f uvEnd1, Tuple2f uvStart2, Tuple2f uvEnd2, Tuple3f normalStart1, Tuple3f normalEnd1, Tuple3f normalStart2, Tuple3f normalEnd2, boolean startVertExterior, boolean endVertExterior) {
        int vertPosStart = insertVertex(start);
        int vertPosEnd = insertVertex(end);
        if (vertPosStart < 0) {
            vertPosStart = (vertPosStart + 1) * (-1);
            if (startVertExterior) {
                isExteriorVertice.set(vertPosStart, true);
            }
        } else {
            isExteriorVertice.push(startVertExterior);
        }
        if (vertPosEnd < 0) {
            vertPosEnd = (vertPosEnd + 1) * (-1);
            if (endVertExterior) {
                isExteriorVertice.set(vertPosEnd, true);
            }
        } else {
            isExteriorVertice.push(endVertExterior);
        }
        if (vertPosStart == vertPosEnd) {
            return;
        }
        if (!isSegmentInList(vertPosStart, vertPosEnd)) {
            intersectionLine.push(vertPosStart, vertPosEnd);
            clusters1.push(cluster1);
            clusters2.push(cluster2);
            addNormal1(normalStart1);
            addNormal1(normalEnd1);
            addNormal2(normalStart2);
            addNormal2(normalEnd2);
            addUV1(uvStart1);
            addUV1(uvEnd1);
            addUV2(uvStart2);
            addUV2(uvEnd2);
        }
    }

    public void removeInteriorVertices() {
        for (int i = 0; i < getVerticesCount(); i++) {
            System.out.print("[ " + i + " | " + isExteriorVertice.get(i) + " ] ");
        }
        System.out.println();
        System.out.println("before removing the interior vertices");
        System.out.println(this);
        for (int i = 0; i < getVerticesCount(); i++) {
            if (isInteriorVertex(i)) {
                removeInteriorVertex(i);
            }
        }
        System.out.println("after removing the interior vertices");
        System.out.println(this);
    }

    public void removeInteriorVertex(int vertPos) {
        int segment1 = -1;
        int segment2 = -1;
        int count = 0;
        for (int i = 0; i < segmentsCount(); i++) {
            if (isPartOfSegment(i, vertPos)) {
                if (segment1 == -1) {
                    segment1 = i;
                } else if (segment2 == -1) {
                    segment2 = i;
                } else {
                    count++;
                }
            }
        }
        if (count > 0) {
            System.err.println("Interior Vertex " + vertPos + " appears " + count + " in intersection Line");
        }
        if (segment2 == -1) {
            System.out.println("Error removing vertex " + vertPos);
            System.out.println(this);
        }
        mergeSegment(segment1, segment2, vertPos);
    }

    void mergeSegment(int segment1, int segment2, int vertPos) {
        if (getSegmentStart(segment1) == vertPos) {
            switchSegment(segment1);
        }
        if (getSegmentEnd(segment2) == vertPos) {
            switchSegment(segment2);
        }
        mergeSegment(segment1, segment2);
    }

    void mergeSegment(int segment1, int segment2) {
        intersectionLine.set(segment1 * 2 + 1, getSegmentEnd(segment2));
        normalData1.set(segment1 * 6 + 3, normalData1.get(segment2 * 6 + 3));
        normalData1.set(segment1 * 6 + 4, normalData1.get(segment2 * 6 + 4));
        normalData1.set(segment1 * 6 + 5, normalData1.get(segment2 * 6 + 5));
        normalData2.set(segment1 * 6 + 3, normalData2.get(segment2 * 6 + 3));
        normalData2.set(segment1 * 6 + 4, normalData2.get(segment2 * 6 + 4));
        normalData2.set(segment1 * 6 + 5, normalData2.get(segment2 * 6 + 5));
        uvData1.set(segment1 * 4 + 2, uvData1.get(segment2 * 4 + 2));
        uvData1.set(segment1 * 4 + 3, uvData1.get(segment2 * 4 + 3));
        uvData2.set(segment1 * 4 + 2, uvData2.get(segment2 * 4 + 2));
        uvData2.set(segment1 * 4 + 3, uvData2.get(segment2 * 4 + 3));
        removeSegment(segment2);
    }

    private void removeSegment(int segment) {
        intersectionLine.set(segment * 2, -1);
        intersectionLine.set(segment * 2 + 1, -1);
    }

    void switchSegment(int segmentPos) {
        float tempFloat;
        tempFloat = normalData1.get(segmentPos * 6 + 3);
        normalData1.set(segmentPos * 6 + 3, normalData1.get(segmentPos * 6));
        normalData1.set(segmentPos * 6, tempFloat);
        tempFloat = normalData1.get(segmentPos * 6 + 4);
        normalData1.set(segmentPos * 6 + 4, normalData1.get(segmentPos * 6 + 1));
        normalData1.set(segmentPos * 6 + 1, tempFloat);
        tempFloat = normalData1.get(segmentPos * 6 + 5);
        normalData1.set(segmentPos * 6 + 5, normalData1.get(segmentPos * 6 + 2));
        normalData1.set(segmentPos * 6 + 2, tempFloat);
        tempFloat = normalData2.get(segmentPos * 6 + 3);
        normalData2.set(segmentPos * 6 + 3, normalData2.get(segmentPos * 6));
        normalData2.set(segmentPos * 6, tempFloat);
        tempFloat = normalData2.get(segmentPos * 6 + 4);
        normalData2.set(segmentPos * 6 + 4, normalData2.get(segmentPos * 6 + 1));
        normalData2.set(segmentPos * 6 + 1, tempFloat);
        tempFloat = normalData2.get(segmentPos * 6 + 5);
        normalData2.set(segmentPos * 6 + 5, normalData2.get(segmentPos * 6 + 2));
        normalData2.set(segmentPos * 6 + 2, tempFloat);
        tempFloat = uvData1.get(segmentPos * 4 + 2);
        uvData1.set(segmentPos * 4 + 2, uvData1.get(segmentPos * 4));
        uvData1.set(segmentPos * 4, tempFloat);
        tempFloat = uvData1.get(segmentPos * 4 + 3);
        uvData1.set(segmentPos * 4 + 3, uvData1.get(segmentPos * 4 + 1));
        uvData1.set(segmentPos * 4 + 1, tempFloat);
        tempFloat = uvData2.get(segmentPos * 4 + 2);
        uvData2.set(segmentPos * 4 + 2, uvData2.get(segmentPos * 4));
        uvData2.set(segmentPos * 4, tempFloat);
        tempFloat = uvData2.get(segmentPos * 4 + 3);
        uvData2.set(segmentPos * 4 + 3, uvData2.get(segmentPos * 4 + 1));
        uvData2.set(segmentPos * 4 + 1, tempFloat);
        int tempInt = intersectionLine.get(segmentPos * 2);
        intersectionLine.set(segmentPos * 2, intersectionLine.get(segmentPos * 2 + 1));
        intersectionLine.set(segmentPos * 2 + 1, tempInt);
    }

    void addUV1(Tuple2f uv) {
        uvData1.push(uv.x, uv.y);
    }

    void addUV2(Tuple2f uv) {
        uvData2.push(uv.x, uv.y);
    }

    void addNormal1(Tuple3f normal) {
        normalData1.push(normal.x, normal.y, normal.z);
    }

    void addNormal2(Tuple3f normal) {
        normalData2.push(normal.x, normal.y, normal.z);
    }

    boolean isSegmentInList(int startVert, int endVert) {
        for (int i = 0; i < segmentsCount(); i++) {
            if (getSegmentStart(i) == startVert && getSegmentEnd(i) == endVert) {
                return true;
            }
            if (getSegmentStart(i) == endVert && getSegmentEnd(i) == startVert) {
                return true;
            }
        }
        return false;
    }

    int insertVertex(Tuple3f vertex) {
        for (int i = 0; i < vertices.size / 3; i++) {
            if (vertex.epsilonEquals(vertexGetTuple3f(i), IntersectionTests.EPSILON)) return ((i) * (-1)) - 1;
        }
        vertices.push(vertex.x, vertex.y, vertex.z);
        return (vertices.size() - 1) / 3;
    }

    Tuple3f vertexGetTuple3f(int vertexPos) {
        return new Point3f(vertices.get(vertexPos * 3), vertices.get(vertexPos * 3 + 1), vertices.get(vertexPos * 3 + 2));
    }

    public FloatList getIntersectionLine() {
        FloatList out = new FloatList();
        for (int i = 0; i < segmentsCount(); i++) {
            if (getSegmentEnd(i) > -1 && getSegmentStart(i) > -1) {
                out.push(getVertexX(getSegmentStart(i)), getVertexY(getSegmentStart(i)), getVertexZ(getSegmentStart(i)));
                out.push(getVertexX(getSegmentEnd(i)), getVertexY(getSegmentEnd(i)), getVertexZ(getSegmentEnd(i)));
            }
        }
        return out;
    }

    public FloatList getUVData1() {
        FloatList out = new FloatList();
        for (int i = 0; i < segmentsCount(); i++) {
            if (getSegmentEnd(i) > -1 && getSegmentStart(i) > -1) {
                if (getSegmentEnd(i) > -1 && getSegmentStart(i) > -1) {
                    out.push(uvData1.get(4 * i), uvData1.get(4 * i + 1));
                    out.push(uvData1.get(4 * i + 2), uvData1.get(4 * i + 3));
                }
            }
        }
        return out;
    }

    public FloatList getUVData2() {
        FloatList out = new FloatList();
        for (int i = 0; i < segmentsCount(); i++) {
            if (getSegmentEnd(i) > -1 && getSegmentStart(i) > -1) {
                if (getSegmentEnd(i) > -1 && getSegmentStart(i) > -1) {
                    out.push(uvData2.get(4 * i), uvData2.get(4 * i + 1));
                    out.push(uvData2.get(4 * i + 2), uvData2.get(4 * i + 3));
                }
            }
        }
        return out;
    }

    public FloatList getNormalData1() {
        FloatList out = new FloatList();
        for (int i = 0; i < segmentsCount(); i++) {
            if (getSegmentEnd(i) > -1 && getSegmentStart(i) > -1) {
                out.push(normalData1.get(6 * i), normalData1.get(6 * i + 1), normalData1.get(6 * i + 2));
                out.push(normalData1.get(6 * i + 3), normalData1.get(6 * i + 4), normalData1.get(6 * i + 5));
            }
        }
        return out;
    }

    public FloatList getNormalData2() {
        FloatList out = new FloatList();
        for (int i = 0; i < segmentsCount(); i++) {
            if (getSegmentEnd(i) > -1 && getSegmentStart(i) > -1) {
                out.push(normalData2.get(6 * i), normalData2.get(6 * i + 1), normalData2.get(6 * i + 2));
                out.push(normalData2.get(6 * i + 3), normalData2.get(6 * i + 4), normalData2.get(6 * i + 5));
            }
        }
        return out;
    }

    public IntList getClusters1() {
        IntList out = new IntList();
        for (int i = 0; i < segmentsCount(); i++) {
            if (getSegmentEnd(i) > -1 && getSegmentStart(i) > -1) {
                out.push(clusters1.get(i));
            }
        }
        return out;
    }

    public IntList getClusters2() {
        IntList out = new IntList();
        for (int i = 0; i < segmentsCount(); i++) {
            if (getSegmentEnd(i) > -1 && getSegmentStart(i) > -1) {
                out.push(clusters2.get(i));
            }
        }
        return out;
    }

    public FloatList getVertices() {
        return vertices;
    }

    public String toString() {
        String out = new String();
        for (int i = 0; i < segmentsCount(); i++) {
            out += " [ ";
            out += "" + getSegmentStart(i);
            out += " | ";
            out += "" + getSegmentEnd(i);
            out += " ]";
        }
        return out;
    }
}
