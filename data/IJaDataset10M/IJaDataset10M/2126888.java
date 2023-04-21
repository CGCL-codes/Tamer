package framework;

import framework.*;
import java.awt.Color;
import java.util.LinkedList;

/**
 *
 * @author Rainer Keller
 */
public interface IRadarPlaneObject {

    /**
     * Updates the radarPlaneObject according to the planeupdateobject.
     * 
     * @param puo   the planeupdateobject with the new values.
     */
    public void update(IPlaneUpdateObject puo);

    /**
     * Sets the planestate of the radarplaneobject.
     * 
     * @param state the new state.
     */
    public void setPlaneState(String state);

    /**
     * Used to get the planestate of the radarplaneobject.
     * 
     * @return  returns the planestate of the plane.
     */
    public String getPlaneState();

    /**
     * Used to get the Color to be used to draw the plane in the radarscreen and
     * the flightlist. This function compares the controllers.
     * 
     * @return  returns the Color of the plane.
     */
    public Color getPlaneColor(String controllerIp);

    /**
     * Sets the active Conctroller of the plane.
     * 
     * @param the new assigned controller of the plane.
     */
    public void setActiveController(String controllerIp);

    /**
     * Used to get the controller of the radarplaneobject.
     * 
     * @return  returns the current controller of the plane.
     */
    public String getActiveController();

    /**
     * Used to set the manufacturer of the radarplaneobject.
     * 
     * @param the new manufacturer of the plane.
     */
    public void setManufacturer(String manufacturer);

    /**
     * Used to get the manufacturer of the plane.
     * 
     * @return  returns the manufacturer of the plane.
     */
    public String getManufacturer();

    /**
     * Sets the new type of the plane.
     * 
     * @param type  the new type of the plane.
     */
    public void setPlaneType(String type);

    /**
     * Used to get the type of the plane.
     * 
     * @return  returns the type of the plane.
     */
    public String getPlaneType();

    /**
     * Used to get the subtype of the plane.
     * 
     * @param type  returns the subtype of the plane.
     */
    public void setPlaneSubType(String type);

    /**
     * Used to get the subtype of the plane.
     * 
     * @return  returns the subtype of the plane.
     */
    public String getPlaneSubType();

    /**
     * Sets the new airline of the plane.
     * 
     * @param airline   the new airline of the plane.
     */
    public void setAirline(String airline);

    /** 
     * Used to get the airline of the plane.
     * 
     * @return  returns the airline of the plane.
     */
    public String getAirline();

    /**
     * Sets the FlightNumber of the plane.
     * 
     * @param s the new flightnumber.
     */
    public void setFlightNumber(String s);

    /**
     * Used to get the flightnumber.
     * 
     * @return  returns the flightnumber of the plane.
     */
    public String getFlightNumber();

    /**
     * Used to get the IP of the plane.
     * 
     * @return  returns the ip of the plane.
     */
    public String getIp();

    /**
     * Used to get the current position of the plane.
     * 
     * @return  returns the current position of the plane.
     */
    public IPlaneDataObject getCurrentPos();

    /**
     * Used to get the squawk of the plane.
     * 
     * @return  returns the squawk of the plane.
     */
    public String getSquawk();

    /**
     * Sets the new squawk of the plane.
     * 
     * @param squawk    the new squawk of the plane.
     */
    public void setSquawk(String squawk);

    /**
     * Adds a position to the historylist of the plane.
     * 
     * @param pdo   the new position to be added.
     */
    public void addPosition(IPlaneDataObject pdo);

    /**
     * Used to get the positions of the historylist of the plane.
     * 
     * @return  returns the positions of the historylist of the plane.
     */
    public LinkedList getPositions();

    /**
     * Used to clear the historylist and reset all the positions.
     */
    public void clearHistory();

    public float getSpeed();

    public float getAltitude();

    public float getHeading();

    /**
     *  Returns the nickname from the pilot
     * @return The nickname from the pilot
     */
    public String getNick();
}
