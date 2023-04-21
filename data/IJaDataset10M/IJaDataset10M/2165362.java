package org.gdi3d.vrmlloader.impl;

import java.util.HashMap;
import java.util.Map;
import javax.media.ding3d.BoundingBox;
import javax.media.ding3d.GeometryArray;
import javax.media.ding3d.LineStripArray;
import javax.media.ding3d.vecmath.Point3d;

/**  Description of the Class */
public class IndexedLineSet extends Geometry {

    Point3d locale;

    LineStripArray impl;

    BoundingBox bounds;

    SFNode color;

    SFNode coord;

    MFInt32 colorIndex;

    SFBool colorPerVertex;

    MFInt32 coordIndex;

    int vertexFormat = 0;

    int numIndices = 0;

    int numLines = 0;

    boolean haveColor = false;

    int[] lineSizes;

    int[] implCoordIndex = null;

    int[] implColorIndex = null;

    Coordinate coordNode = null;

    Color colorNode = null;

    /**
     *Constructor for the IndexedLineSet object
     *
     *@param  loader Description of the Parameter
     */
    public IndexedLineSet(Loader loader) {
        super(loader);
        colorPerVertex = new SFBool(true);
        color = new SFNode();
        coord = new SFNode();
        colorIndex = new MFInt32();
        coordIndex = new MFInt32();
        initFields();
    }

    public boolean equals(BaseNode other) {
        boolean result = false;
        System.out.println(this.getClass().getName() + ".equals not implemented yet");
        return result;
    }

    /**
     *Constructor for the IndexedLineSet object
     *
     *@param  loader Description of the Parameter
     *@param  coord Description of the Parameter
     *@param  coordIndex Description of the Parameter
     *@param  color Description of the Parameter
     *@param  colorIndex Description of the Parameter
     *@param  colorPerVertex Description of the Parameter
     */
    public IndexedLineSet(Loader loader, SFNode coord, MFInt32 coordIndex, SFNode color, MFInt32 colorIndex, SFBool colorPerVertex) {
        super(loader);
        this.coord = coord;
        this.coordIndex = coordIndex;
        this.color = color;
        this.colorIndex = colorIndex;
        this.colorPerVertex = colorPerVertex;
        initFields();
    }

    /**  Description of the Method */
    public void initImpl() {
        if ((coord != null) && (coord.node instanceof Coordinate) && (coordIndex.size != 0)) {
            coordNode = (Coordinate) coord.node;
            vertexFormat = GeometryArray.COORDINATES;
            numLines = coordIndex.primCount();
            numIndices = coordIndex.indexCount();
            lineSizes = new int[numLines];
            implCoordIndex = new int[numIndices];
            coordIndex.fillImplArrays(lineSizes, implCoordIndex);
            if ((color != null) && (color.node instanceof Color)) {
                colorNode = (Color) color.node;
                vertexFormat |= javax.media.ding3d.GeometryArray.COLOR_3;
                haveColor = true;
                if (colorPerVertex.value == false) {
                    if (colorIndex.size == 0) {
                        implColorIndex = new int[numIndices];
                        int curIndex = 0;
                        for (int j = 0; j < numLines; j++) {
                            for (int i = 0; i < lineSizes[j]; i++) {
                                implColorIndex[curIndex++] = j;
                            }
                        }
                    } else {
                        if (colorIndex.size != numLines) {
                            System.out.println("ILS: colorIndex.size = " + colorIndex.size + " != numLines = " + numLines);
                        }
                        implColorIndex = new int[numIndices];
                        int curIndex = 0;
                        for (int j = 0; j < numLines; j++) {
                            for (int i = 0; i < lineSizes[j]; i++) {
                                implColorIndex[curIndex++] = colorIndex.value[j];
                            }
                        }
                    }
                } else {
                    if (colorIndex.size == 0) {
                        implColorIndex = implCoordIndex;
                    } else {
                        implColorIndex = new int[numIndices];
                        if (!coordIndex.fillImplArraysTest(lineSizes, implColorIndex)) {
                        }
                    }
                }
            }
            int locale_indexBase = implCoordIndex[0] * 3;
            double x = coordNode.point.value[locale_indexBase];
            double y = coordNode.point.value[locale_indexBase + 1];
            double z = coordNode.point.value[locale_indexBase + 2];
            locale = new Point3d(x, y, z);
            double[] implCoords = new double[3 * numIndices];
            for (int i = 0; i < numIndices; i++) {
                int implBase = i * 3;
                int indexBase = implCoordIndex[i] * 3;
                implCoords[implBase] = coordNode.point.value[indexBase];
                implCoords[implBase + 1] = coordNode.point.value[indexBase + 1];
                implCoords[implBase + 2] = coordNode.point.value[indexBase + 2];
                implCoords[implBase] -= locale.x;
                implCoords[implBase + 1] -= locale.y;
                implCoords[implBase + 2] -= locale.z;
            }
            impl = new LineStripArray(numIndices, vertexFormat, lineSizes);
            impl.setCoordinates(0, implCoords);
            Map userData = new HashMap();
            userData.put("locale", locale);
            impl.setUserData(userData);
            if (haveColor) {
                float[] implColors = new float[3 * numIndices];
                for (int i = 0; i < numIndices; i++) {
                    int implBase = i * 3;
                    int indexBase = implColorIndex[i] * 3;
                    implColors[implBase] = colorNode.color.vals[indexBase];
                    implColors[implBase + 1] = colorNode.color.vals[indexBase + 1];
                    implColors[implBase + 2] = colorNode.color.vals[indexBase + 2];
                }
                impl.setColors(0, implColors);
            }
            bounds = coordNode.point.getBoundingBox();
        }
    }

    /**
     *  Description of the Method
     *
     *@param  eventInName Description of the Parameter
     *@param  time Description of the Parameter
     */
    public void notifyMethod(String eventInName, double time) {
        if (eventInName.equals("colorIndex") || eventInName.equals("coordIndex")) {
            initImpl();
        }
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public Object clone() {
        return new IndexedLineSet(loader, (SFNode) coord.clone(), (MFInt32) coordIndex.clone(), (SFNode) color.clone(), (MFInt32) colorIndex.clone(), (SFBool) colorPerVertex.clone());
    }

    /**
     *  Gets the type attribute of the IndexedLineSet object
     *
     *@return  The type value
     */
    public String getType() {
        return "IndexedLineSet";
    }

    /**  Description of the Method */
    void initFields() {
        coord.init(this, FieldSpec, Field.EXPOSED_FIELD, "coord");
        coordIndex.init(this, FieldSpec, Field.EVENT_IN, "coordIndex");
        color.init(this, FieldSpec, Field.EXPOSED_FIELD, "color");
        colorIndex.init(this, FieldSpec, Field.EVENT_IN, "colorIndex");
        colorPerVertex.init(this, FieldSpec, Field.FIELD, "colorPerVertex");
    }

    /**
     *  Gets the implGeom attribute of the IndexedLineSet object
     *
     *@return  The implGeom value
     */
    public javax.media.ding3d.Geometry getImplGeom() {
        return (javax.media.ding3d.Geometry) impl;
    }

    /**
     *  Gets the boundingBox attribute of the IndexedLineSet object
     *
     *@return  The boundingBox value
     */
    public javax.media.ding3d.BoundingBox getBoundingBox() {
        return bounds;
    }

    /**
     *  Description of the Method
     *
     *@return  Description of the Return Value
     */
    public boolean haveTexture() {
        return false;
    }

    /**
     *  Gets the numTris attribute of the IndexedLineSet object
     *
     *@return  The numTris value
     */
    public int getNumTris() {
        return 0;
    }

    @Override
    javax.media.ding3d.Geometry getImplGeomNormals() {
        return null;
    }
}
