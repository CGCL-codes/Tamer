package net.sourceforge.osm2postgis.model;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;
import net.sourceforge.osm2postgis.core.FeatureType;
import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.LinearRing;
import org.postgis.Polygon;

/**
 * Derived from an OSM way object that actually represents any renderable
 * map feature.
 * 
 * @author Sakari Maaranen
 */
public class Feature extends Entity {

    private static Logger logger = Logger.getLogger("osm2postgis");

    private OSMWay way = null;

    private int geometryType = FeatureType.UNKNOWN;

    private String[] symbolType = null;

    private Geometry geometry = null;

    public Feature(OSMWay way) {
        setWay(way);
    }

    public OSMWay getWay() {
        return way;
    }

    /**
     * Sets the way object reference.
     * 
     * @param way
     */
    private void setWay(OSMWay way) {
        this.way = way;
    }

    public int getGeometryType() {
        return geometryType;
    }

    /**
     * Returns a map symbol for this feature. The symbol[i=0] will be used
     * as
     * relation name.
     * 
     * @param i
     * @param symbol
     */
    public String getSymbol(int i) {
        return symbolType[i];
    }

    /**
     * Returns the full symbol type of this feature.
     * 
     * @return an array of <code>String</code> objects representing the
     *         map symbol type of this feature.
     */
    public String[] getSymbolType() {
        return symbolType;
    }

    /**
     * Sets the geometry type and symbol type for this feature.
     * 
     * @param type
     */
    public void setType(FeatureType type) {
        geometryType = type.getGeometryType();
        symbolType = Arrays.copyOf(type.getSymbols(), type.getSymbols().length);
        for (int i = 1; i < symbolType.length; i++) {
            if (null != symbolType[i] && symbolType[i].startsWith("$")) {
                String variable = symbolType[i].substring(1);
                if (variable.startsWith("key:")) {
                    String key = variable.substring(4);
                    if ("*".equals(key)) {
                        symbolType[i] = way.getTagMap().toString();
                    } else {
                        symbolType[i] = way.getTagMap().get(key);
                    }
                } else {
                }
            }
        }
    }

    public Geometry getGeometry() throws SQLException, OSMDataException {
        if (null == geometry) {
            switch(getGeometryType()) {
                case Geometry.POINT:
                    throw new UnsupportedOperationException("Point feature");
                case Geometry.LINESTRING:
                    geometry = way.getLineString();
                    break;
                case Geometry.LINEARRING:
                    geometry = new LineString(way.getPointsClosed());
                    break;
                case Geometry.POLYGON:
                    LinearRing[] array = new LinearRing[] { way.getLinearRing() };
                    geometry = new Polygon(array);
                    break;
                default:
                    throw new UnsupportedOperationException("Geometry type: " + getGeometryType());
            }
            geometry.setSrid(SRID);
        }
        return geometry;
    }

    public String getName() {
        return getWay().getTagMap().get("name");
    }

    @Override
    public String toString() {
        return "Feature{" + Arrays.deepToString(symbolType) + "," + String.valueOf(way) + "}";
    }
}
