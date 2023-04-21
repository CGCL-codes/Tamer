package org.openstreetmap.travelingsalesman.painting.odr;

import java.awt.Point;
import java.awt.Shape;
import java.util.LinkedList;
import java.util.List;
import org.openstreetmap.travelingsalesman.routing.NameHelper;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;

/**
 * This is the base class for ways. inheriting classes should set their
 * geometrical representation and use the given Graphics to paint
 * themselves on it.<br/>
 * The usual implementations are {@link PathWay} for polylines
 * and {@link PolygonWay} for area-polygons.
 *
 * @author <a href="mailto:tdad@users.sourceforge.net">Sven Koehler</a>
 */
public abstract class ODRWay {

    /**
     * @param aWay
     *            the way we are painting.
     * @param aWayType
     *            the type of this way.
     */
    public ODRWay(final Way aWay, final ODR_WAY_TYPE aWayType) {
        myWay = aWay;
        myWayType = aWayType;
        if (aWay == null) {
            throw new IllegalArgumentException("null way given");
        }
        setName(NameHelper.getNameForWay(aWay));
    }

    /**
     *  @return the shape this way is representing
     */
    public abstract Shape getWayShape();

    /**
     * adds a single waypoint.
     *
     * @param wayPoint way point to add
     */
    public void addWayPoint(final Point wayPoint) {
        myWayPoints.add(wayPoint);
    }

    /**
     * @param aName
     *            the name to set
     */
    public void setName(final String aName) {
        myName = aName;
    }

    /**
     * determines the name of a way.
     *
     * @return name of the way. if the way has no name tag, null is returned.
     */
    public String getName() {
        if (myName == null) {
            myName = NameHelper.getNameForWay(getWay());
        }
        return myName;
    }

    /**
     * @return the wayType
     */
    public ODR_WAY_TYPE getWayType() {
        return myWayType;
    }

    /**
     *  @return my way points.
     */
    public List<Point> getWayPoints() {
        return myWayPoints;
    }

    /**
     * @return the oneway
     */
    public boolean isOneway() {
        return isOneway;
    }

    /**
     * @param myOneway the oneway to set
     */
    public void setOneway(final boolean myOneway) {
        this.isOneway = myOneway;
    }

    /**
     * @return the bridge?
     */
    public boolean isBridge() {
        return isBridge;
    }

    /**
     * @param myBridge the bridge to set
     */
    public void setBridge(final boolean myBridge) {
        this.isBridge = myBridge;
    }

    /**
     * @return the way
     */
    public Way getWay() {
        return myWay;
    }

    /**
     * the name of this way (if it has one).
     */
    private String myName = null;

    /**
     * is oneway road.
     */
    private boolean isOneway = false;

    /**
     * is bridge?.
     */
    private boolean isBridge = false;

    /**
     * the way itself.
     */
    private final Way myWay;

    /**
     * The points of the road-curve.
     */
    private final List<Point> myWayPoints = new LinkedList<Point>();

    /**
     * my way type.
     */
    private final ODR_WAY_TYPE myWayType;
}
