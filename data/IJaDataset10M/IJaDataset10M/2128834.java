package org.mitre.caasd.aixmj.feature.general;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.mitre.caasd.aixmj.data.AixmCodeVerticalDatumType;
import org.mitre.caasd.aixmj.data.AixmConstants;
import org.mitre.caasd.aixmj.data.AixmHorizontalDistance;
import org.mitre.caasd.aixmj.data.AixmVerticalDistanceValue;
import org.mitre.caasd.aixmj.data.ObjectHash;
import org.mitre.caasd.aixmj.util.AixmUtil;

/**
 * An AIXM Point derived from GM_Point that includes properties for describing 
 * a point with elevation and vertical extent.  
 * Used in obstacles, navaids, etc.
 * 
 * @author SCHASE
 *
 */
public class AixmElevatedPoint extends AixmPoint {

    /**
     * Elevation (above Mean Sea Level) refers to the top of the feature described by the Elevated Point.
     */
    private AixmVerticalDistanceValue elevation = null;

    /**
     * A distance separating the geoid and the ellipsoid at that position. In respect of WGS-84 geodetic datum, the 
     * difference between the WGS-84 ellipsoidal height and geoidal height represents geoidal undulation.
     */
    private AixmVerticalDistanceValue geoidUndulation = null;

    /**
     * Attribute to take the \"Vertical Datum\" (viz. the tide gauge to determine MSL - for example, \"AMSTERDAM GAUGE\", \"NEWLYN\" etc.).
     */
    private AixmCodeVerticalDatumType verticalDatum = null;

    /**
     * The vertical accuracy.
     */
    private AixmVerticalDistanceValue verticalAccuracy = null;

    /**
     * Constructor. 
     */
    public AixmElevatedPoint() {
    }

    /**
     * Constructor. 
     */
    public AixmElevatedPoint(double latitudeIn, double longitudeIn) {
        super(latitudeIn, longitudeIn);
    }

    /**
     * Constructor. 
     */
    public AixmElevatedPoint(double latitudeIn, double longitudeIn, AixmVerticalDistanceValue elevIn, AixmVerticalDistanceValue geoidUndulationIn, AixmCodeVerticalDatumType verticalDatumIn, AixmHorizontalDistance horizontalAccuracyIn, AixmVerticalDistanceValue verticalAccuracyIn) {
        super(latitudeIn, longitudeIn, horizontalAccuracyIn);
        this.elevation = elevIn;
        this.geoidUndulation = geoidUndulationIn;
        this.verticalDatum = verticalDatumIn;
        this.verticalAccuracy = verticalAccuracyIn;
    }

    /**
     * Create an AixmElevatedPoint from a dom4j Element.
     * @param element
     * @return AixmElevatedPoint
     */
    public AixmElevatedPoint parseElements(Element element) {
        String gmlId = element.attributeValue("id");
        Object o = ObjectHash.getObject(gmlId);
        if (o != null) return (AixmElevatedPoint) o;
        AixmElevatedPoint point = new AixmElevatedPoint();
        point.setGmlId(gmlId);
        Element positionElement = element.element(QName.get("pos", AixmConstants.GML_NAMESPACE));
        if (positionElement != null) {
            String llString = positionElement.getStringValue();
            String[] points = llString.split(" ");
            try {
                point.setLatitude(Double.parseDouble(points[0]));
                point.setLongitude(Double.parseDouble(points[1]));
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        point.elevation = AixmUtil.parseVerticalDistance("elevation", element);
        point.geoidUndulation = AixmUtil.parseVerticalDistance("geoidUndulation", element);
        point.verticalDatum = (AixmCodeVerticalDatumType) AixmUtil.parseEnum("verticalDatum", element, AixmCodeVerticalDatumType.AHD);
        point.setHorizontalAccuracy(AixmUtil.parseHorizontalDistance("horizontalAccuracy", element));
        point.verticalAccuracy = AixmUtil.parseVerticalDistance("verticalAccuracy", element);
        ObjectHash.addObject(gmlId, point);
        return point;
    }

    /**
     * Add dom4j Element objects to represent the attributes of this object
     */
    public Element constructElements(Element element, Document d) {
        if (super.getLatitude() != null && super.getLongitude() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(super.getLatitude());
            sb.append(" ");
            sb.append(super.getLongitude());
            element.add(AixmConstants.GML_NAMESPACE);
            Element position = element.addElement("gml:pos");
            position.setText(sb.toString());
        }
        AixmUtil.constructAbstractValueElement(this.elevation, element, "elevation");
        AixmUtil.constructAbstractValueElement(this.geoidUndulation, element, "geoidUndulation");
        AixmUtil.constructSingleElement(this.verticalDatum, element, "verticalDatum");
        AixmUtil.constructAbstractValueElement(super.getHorizontalAccuracy(), element, "horizontalAccuracy");
        AixmUtil.constructAbstractValueElement(this.verticalAccuracy, element, "verticalAccuracy");
        return element;
    }

    /**
	 * @return the elevation
	 */
    public AixmVerticalDistanceValue getElevation() {
        return elevation;
    }

    /**
	 * @param elevation the elevation to set
	 */
    public void setElevation(AixmVerticalDistanceValue elevation) {
        this.elevation = elevation;
    }

    /**
	 * @return the geoidUndulation
	 */
    public AixmVerticalDistanceValue getGeoidUndulation() {
        return geoidUndulation;
    }

    /**
	 * @param geoidUndulation the geoidUndulation to set
	 */
    public void setGeoidUndulation(AixmVerticalDistanceValue geoidUndulation) {
        this.geoidUndulation = geoidUndulation;
    }

    /**
	 * @return the verticalAccuracy
	 */
    public AixmVerticalDistanceValue getVerticalAccuracy() {
        return verticalAccuracy;
    }

    /**
	 * @param verticalAccuracy the verticalAccuracy to set
	 */
    public void setVerticalAccuracy(AixmVerticalDistanceValue verticalAccuracy) {
        this.verticalAccuracy = verticalAccuracy;
    }

    /**
	 * @return the verticalDatum
	 */
    public AixmCodeVerticalDatumType getVerticalDatum() {
        return verticalDatum;
    }

    /**
	 * @param verticalDatum the verticalDatum to set
	 */
    public void setVerticalDatum(AixmCodeVerticalDatumType verticalDatum) {
        this.verticalDatum = verticalDatum;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getGmlId());
        sb.append(",");
        sb.append(super.getLatitude());
        sb.append(",");
        sb.append(super.getLongitude());
        sb.append(",");
        sb.append(this.elevation);
        sb.append(",");
        sb.append(this.geoidUndulation);
        sb.append(",");
        sb.append(super.getHorizontalAccuracy());
        sb.append(",");
        sb.append(this.verticalAccuracy);
        sb.append(",");
        sb.append(this.verticalDatum);
        return sb.toString();
    }
}
