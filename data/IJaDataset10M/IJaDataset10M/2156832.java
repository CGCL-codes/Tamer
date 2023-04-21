package org.gvsig.remoteClient.gml.types;

/**
 * All the GML geometries must be here
 * 
 * @author Jorge Piera Llodr� (piera_jor@gva.es)
 * @author Carlos S�nchez Peri��n (sanchez_carper@gva.es)
 * 
 */
public class GMLGeometryType implements IXMLType {

    private String type = null;

    public static final String POINT = "gml:PointPropertyType";

    public static final String MULTIPOINT = "gml:MultiPointPropertyType";

    public static final String LINE = "gml:LineStringPropertyType";

    public static final String MULTILINE = "gml:MultiLineStringPropertyType";

    public static final String POLYGON = "gml:PolygonPropertyType";

    public static final String MULTIPOLYGON = "gml:MultiPolygonPropertyType";

    public static final String GEOMETRY = "gml:GeometryPropertyType";

    public static final String MULTIGEOMETRY = "gml:MultiGeometryPropertyType";

    public static final String SURFACE = "gml:SurfacePropertyType";

    public static final String MULTISURFACE = "gml:MultiSurfacePropertyType";

    public GMLGeometryType(String type) {
        super();
        this.type = type;
    }

    public int getType() {
        return IXMLType.GML_GEOMETRY;
    }

    public String getName() {
        return type;
    }
}
