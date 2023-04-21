package com.luzan.bean.georss;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import com.luzan.bean.georss.gml.PointType;
import com.luzan.bean.georss.gml.DirectPositionType;
import com.luzan.bean.georss.gml.PolygonType;
import com.luzan.bean.georss.gml.EnvelopeType;

/**
 * Where
 *
 * @author Alexander Bondar
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "where", namespace = "http://www.georss.org/georss", propOrder = { "point", "polygon", "envelope" })
public class Where {

    @XmlElement(namespace = "http://www.opengis.net/gml", name = "Point")
    PointType point;

    @XmlElement(namespace = "http://www.opengis.net/gml", name = "Polygon")
    PolygonType polygon;

    @XmlElement(namespace = "http://www.opengis.net/gml", name = "Envelope")
    EnvelopeType envelope;

    public Where() {
    }

    public Where(double lat, double lng) {
        point = new PointType();
        DirectPositionType dp = new DirectPositionType();
        dp.getValue().add(lat);
        dp.getValue().add(lng);
        point.setPos(dp);
    }

    public Where(double south, double west, double north, double east) {
        envelope = new EnvelopeType();
        DirectPositionType sw = new DirectPositionType();
        sw.getValue().add(south);
        sw.getValue().add(west);
        envelope.setLowerCorner(sw);
        DirectPositionType ne = new DirectPositionType();
        ne.getValue().add(north);
        ne.getValue().add(east);
        envelope.setUpperCorner(ne);
    }

    public PointType getPoint() {
        return point;
    }

    public void setPoint(PointType point) {
        this.point = point;
    }

    public PolygonType getPolygon() {
        return polygon;
    }

    public void setPolygon(PolygonType polygon) {
        this.polygon = polygon;
    }

    public EnvelopeType getEnvelope() {
        return envelope;
    }

    public void setEnvelope(EnvelopeType envelope) {
        this.envelope = envelope;
    }
}
