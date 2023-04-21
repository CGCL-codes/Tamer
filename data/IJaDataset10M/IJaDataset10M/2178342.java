package nz.ac.massey.softwarec.group3.game.map;

import java.util.List;

/**
 *
 * @author Natalie
 */
public interface ZoneInterface {

    /**
     * Getter for starting latitude.
     * @return double startLat
     */
    double getStartLat();

    /**
     * Getter for ending latitude.
     * @return double endLat
     */
    double getEndLat();

    /**
     * Getter for start longitude.
     * @return double startLong 
     */
    double getStartLong();

    /**
     * Getter for end longitude.
     * @return double endLong
     */
    double getEndLong();

    /**
     * Adds a mapstation to the list of mapStaions
     * @param mapStation 
     */
    void addMapStation(final MapStation mapStation);

    /**
     * Getter for the number of nodes in mapstations list.
     * @return int mapStations.size
     */
    int getNumberOfNodes();

    /**
     * Getter for the list of mapStations
     * @return List mapStations
     */
    List<MapStation> getMapStations();

    /**
     * getter for isRightEdge
     * @return boolean isRightEdge
     */
    boolean getIsRightEdge();

    /**
     * Setter for isRightEdge
     * @param isRightEdge 
     */
    void setIsRightEdge(final boolean isRightEdge);

    /**
     * Getter for isBottomEdge
     * @return boolean isBottomEdge
     */
    boolean getIsBottomEdge();

    /**
     * Setter for isBottomEdge
     * @param isBottomEdge 
     */
    void setIsBottomEdge(final boolean isBottomEdge);

    /**
     * Creates a station, with a random number of stations each with a set station type.
     * @param type 
     */
    void setStation(final int type);
}
