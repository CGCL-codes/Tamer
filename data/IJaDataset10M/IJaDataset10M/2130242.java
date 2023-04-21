package net.walkingtools.server.gpsTypes.hiperGps;

import java.util.Hashtable;
import java.util.Vector;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * This class is unfinished in the first alpha versions.
 * @author Brett Stalbaum
 * @version 0.1.0
 * @since 0.0.6
 */
public class HiperNarrativeWaypoint extends HiperWaypoint {

    private Vector links = null;

    /**
     * This hashtable contains all of the other HiperNarrativeWaypoints in the application,
     * all of which use the name field of each element as keys.
     */
    protected static Hashtable allHiperNarrativeWayPoints = null;

    /** Constructs a HiperWaypoint Object representing the most basic GPX fields
     * @param lat the latitude
     * @param lon the longitude
     * @param ele the elevation
     * @param name waypoint name
     * @param imageFile the file name
     * @param audioFile the file name
     * @param radius the radius from the point at which media will be triggered
     * @param table a hashTable to hold all of the other NarrativeHiperWaypoints for link lookups
     */
    public HiperNarrativeWaypoint(double lat, double lon, float ele, String name, String imageFile, String audioFile, int radius, Hashtable table) {
        super(lat, lon, ele, name, radius);
        allHiperNarrativeWayPoints = table;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * gets all of this HiperNarrativeWaypoint's links, which can be looked up by key
     * in the allHiperNarrativeWaypoints hashTable
     * @return the vector of this HiperNarrativeWaypoint's links
     * @see net.walkingtools.server.gpsTypes.hiperGps.HiperNarrativeWaypoint#allHiperNarrativeWayPoints
     */
    public Vector getLinks() {
        return links;
    }
}
