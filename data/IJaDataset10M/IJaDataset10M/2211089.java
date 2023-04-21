package de.hpi.eworld.simulationstatistic.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for statistical representation of edges and lanes.
 * Provides all statistical values and basic functions
 * 
 * @author Philipp Maschke
 * 
 */
public abstract class AbstractStatLine implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8215258553635011642L;

    /**
	 * The mean travel time that vehicles needed to pass this lane If no vehicle
	 * has passed, length/maximum_allowed_velocity is used; unit: s
	 */
    protected double traveltime;

    /**
	 * Aggregated time vehicles have spent on this lane unit: s
	 */
    protected double sampledSeconds;

    /**
	 * The mean vehicle density on the edge in veh/km
	 */
    protected double density;

    /**
	 * The mean number of vehicles occupying this lane
	 */
    protected double occupancy;

    /**
	 * The number of recognized stops
	 */
    protected int nrStops;

    /**
	 * The mean of the collected vehicle velocities. If no vehicle has passed
	 * the edge, the maximum allowed velocity is used unit: m/s
	 */
    protected double speed;

    /**
	 * The number of vehicles that entered this lane
	 */
    protected int entered;

    /**
	 * The number of vehicles that were emitted on this lane
	 */
    protected int emitted;

    /**
	 * The number of vehicles that left this lane
	 */
    protected int left;

    /**
	 * The name of the corresponding street
	 */
    protected String streetName;

    /**
	 * The maximum allowed speed
	 */
    protected double maxSpeed;

    /**
	 * The minimum allowed speed
	 */
    protected double minSpeed;

    /**
	 * The type of road. Based on OpenStreetMap priorities
	 */
    protected double roadPriority;

    /**
	 * 
	 * @param traveltime
	 * @param sampledSeconds
	 * @param density
	 * @param occupancy
	 * @param speed
	 * @param nrStops
	 * @param entered
	 * @param emitted
	 * @param left
	 */
    public AbstractStatLine(double traveltime, double sampledSeconds, double density, double occupancy, double speed, int nrStops, int entered, int emitted, int left) {
        this.traveltime = traveltime;
        this.sampledSeconds = sampledSeconds;
        this.density = density;
        this.occupancy = occupancy;
        this.nrStops = nrStops;
        this.speed = speed;
        this.entered = entered;
        this.emitted = emitted;
        this.left = left;
        streetName = "";
        maxSpeed = 0;
        minSpeed = 0;
        roadPriority = 1;
    }

    /**
	 * 
	 * @return ratio of combined vehicle lengths to length of segment, see {@link Value}
	 */
    protected double getDensity() {
        return density;
    }

    /**
	 * 
	 * @return number of vehicles, which started here, see {@link Value}
	 */
    protected int getEmitted() {
        return emitted;
    }

    /**
	 * 
	 * @return number of vehicles, which entered this segment, see {@link Value}
	 */
    protected int getEntered() {
        return entered;
    }

    /**
	 * 
	 * @return number of vehicles, which left this segment, see {@link Value}
	 */
    protected int getLeft() {
        return left;
    }

    /**
	 * 
	 * @return speed limit in m/s, see {@link Value}
	 */
    protected double getMaxSpeed() {
        return maxSpeed;
    }

    /**
	 * 
	 * @return minimum speed in m/s, see {@link Value}
	 */
    protected double getMinSpeed() {
        return minSpeed;
    }

    /**
	 * 
	 * @return number of times vehicles had to stop, see {@link Value}
	 */
    protected int getNrStops() {
        return nrStops;
    }

    /**
	 * 
	 * @return number of vehicles per kilometer, see {@link Value}
	 */
    protected double getOccupancy() {
        return occupancy;
    }

    /**
	 * 
	 * @return combined amount of time, which vehicles spent here
	 */
    protected double getSampledSeconds() {
        return sampledSeconds;
    }

    /**
	 * 
	 * @return mean speed in m/s
	 */
    protected double getSpeed() {
        return speed;
    }

    /**
	 * 
	 * @return time needed to pass the segment in seconds
	 */
    protected double getTraveltime() {
        return traveltime;
    }

    /**
	 * 
	 * @return the street name
	 */
    public String getStreetName() {
        return streetName;
    }

    /**
	 * 
	 * @param maxSpeed speed limit in m/s!!!
	 */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
	 * 
	 * @param minSpeed minimum speed in m/s!!!
	 */
    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    /**
	 * Returns a string with all values separated by comma
	 */
    public String toString() {
        DecimalFormat value = new DecimalFormat("0.00");
        return value.format(traveltime) + ", " + value.format(sampledSeconds) + ", " + value.format(density) + ", " + value.format(occupancy) + ", " + value.format(nrStops) + ", " + value.format(speed) + ", " + value.format(entered) + ", " + value.format(emitted) + ", " + value.format(left) + ", " + streetName + ", " + value.format(maxSpeed);
    }

    /**
	 * Convenience function to get all numerical values of one edge/lane
	 * @return a map containing all numerical values of this lane as a double
	 *         and the corresponding value names
	 */
    public Map<Value, Double> getValueMap() {
        HashMap<Value, Double> map = new HashMap<Value, Double>();
        map.put(Value.TRAVELTIME, this.getTraveltime());
        map.put(Value.SAMPLEDSECONDS, this.getSampledSeconds());
        map.put(Value.DENSITY, this.getDensity());
        map.put(Value.OCCUPANCY, this.getOccupancy());
        map.put(Value.NRSTOPS, ((Integer) this.getNrStops()).doubleValue());
        map.put(Value.SPEED, this.getSpeed());
        map.put(Value.RELATIVESPEED, this.getRelativeSpeed());
        map.put(Value.VEHICLESENTERED, ((Integer) this.getEntered()).doubleValue());
        map.put(Value.VEHICLESEMITTED, ((Integer) this.getEmitted()).doubleValue());
        map.put(Value.VEHICLESLEFT, ((Integer) this.getLeft()).doubleValue());
        map.put(Value.MAXSPEED, this.getMaxSpeed());
        map.put(Value.MINSPEED, this.getMinSpeed());
        map.put(Value.ROADPRIORITY, this.getRoadPriority());
        return map;
    }

    /**
	 * Returns the value specified in the parameter. Will return null
	 * if the value is not defined.
	 * @param valueName a constant from the {@link Value} enum
	 * @return the corresponding value as a double. integer values are cast
	 */
    public Double getValue(Value valueName) {
        switch(valueName) {
            case DENSITY:
                return getDensity();
            case MAXSPEED:
                return getMaxSpeed();
            case MINSPEED:
                return getMinSpeed();
            case NRSTOPS:
                return (double) getNrStops();
            case OCCUPANCY:
                return getOccupancy();
            case SAMPLEDSECONDS:
                return getSampledSeconds();
            case SPEED:
                return getSpeed();
            case TRAVELTIME:
                return getTraveltime();
            case VEHICLESEMITTED:
                return (double) getEmitted();
            case VEHICLESENTERED:
                return (double) getEntered();
            case VEHICLESLEFT:
                return (double) getLeft();
            case ROADPRIORITY:
                return (double) getRoadPriority();
            case RELATIVESPEED:
                return getRelativeSpeed();
            default:
                System.err.println("Undefined enumeration type discovered! Returning 'null'");
                return null;
        }
    }

    /**
	 * "motorway",      7 
	   "motorway_link", 7
	   "trunk",         6
	   "trunk_link",    6
	   "primary",       5
	   "primary_link",  5
	   "secondary",     4
	   "tertiary",      3
	   "residential",   2
	   "unclassified",  1
	   "unsurfaced",    1
	 * @return the road priority of this street segment. 
	 * describes the type of road
	 */
    protected double getRoadPriority() {
        return roadPriority;
    }

    /**
	 * 
	 * @return mean speed relative to the streets speed limit
	 * in percent
	 */
    protected double getRelativeSpeed() {
        if (getMaxSpeed() != 0.0) return getSpeed() * 100 / getMaxSpeed();
        return 0.0;
    }

    /**
	 * 
	 * @param roadPriority the new road priority. should be between
	 * 1 and 7, will be set to 1 (7) if lower (higher) than that.
	 */
    public void setRoadPriority(double roadPriority) {
        if (roadPriority > 7) this.roadPriority = 7; else if (roadPriority < 1) this.roadPriority = 1; else this.roadPriority = roadPriority;
    }
}
