package net.jforerunning;

/**
 * This class represents a distance.
 * There is support for both km- and mile-based distances (see fields &
 * constructors).
 * IMPORTANT: distances must be passed in metric units, because they are
 *            internally stored in metric units. The given distance unit just
 *            determines the string representation.
 *            
 * It is also possible to change the distance by adding another XDistance object
 * 
 * There are accessors for the distance and the distance unit.
 * 
 * There is a toString method for the GUI, respecting the set distance unit.
 * Possible return values are "3.219km" oder "2mi",
 * 
 * You can also have a pace calculated, pass a XDuration object to getPace.
 * This returns a GUI formatted string, so the pace is given in either min/km
 * or min/mi, depending on the distance unit.
 * Similar to the pace, you can also get the VDOT for a duration and a distance.
 * 
 * Can be cloned.
 * 
 * @author jens
 *
 * TODO: Rounding error when many additions are used. Fix that.
 * TODO: Language specific decimal point in distance representations
 */
public class XDistance implements Cloneable {

    public static final double MILE_PER_KM = 1.609344;

    public static final int METRIC = 0;

    public static final int ENGLISH = 1;

    public static int defaultunit = METRIC;

    private double distance;

    private int unit = defaultunit;

    /**
	 * Create a distance object from the given distance in km, set the unit to
	 * the default unit  
	 * @param distanceinkm Distance in km
	 */
    public XDistance(double distanceinkm) {
        this(distanceinkm, defaultunit);
    }

    /**
	 * Create a distance object from the given distance in km and the given unit
	 * @param distanceinkm Distance in km
	 * @param unitForGUI One of the unit constants; see above
	 */
    public XDistance(double distanceinkm, int unitForGUI) {
        distance = distanceinkm;
        unit = unitForGUI;
    }

    /**
	 * Create a distance from any Number object; if null is passed, the distance
	 * is set to 0. The distance unit is set to the default unit.
	 * @param distance The value of this object is used as distance
	 */
    public XDistance(Number distanceinkm) {
        unit = defaultunit;
        if (distanceinkm instanceof Double) distance = (Double) distanceinkm; else if (distanceinkm instanceof Float) distance = (Float) distanceinkm; else if (distanceinkm instanceof Long) distance = (Long) distanceinkm; else if (distanceinkm instanceof Integer) distance = (Integer) distanceinkm; else distance = 0;
    }

    /**
	 * Get the current unit used for GUI representations
	 * @return Current unit
	 */
    public int getGUIUnit() {
        return unit;
    }

    /**
	 * Set the distance unit to one of the constants defined above.
	 * @param unit Unit to use from now on
	 */
    public void setGUIUnit(int unit) {
        if (unit != METRIC && unit != ENGLISH) unit = METRIC;
        this.unit = unit;
    }

    /**
	 * Return saved distance as double
	 * @return Saved distance in km
	 */
    public double getDistanceInKm() {
        return distance;
    }

    /**
	 * Return distance converted to the set unit
	 * @return distance in the set distance unit
	 */
    public double getDistanceInUnit() {
        return distance / (unit == METRIC ? 1 : MILE_PER_KM);
    }

    /**
	 * Return GUI formatted String representation
	 * @return Formatted distance
	 */
    @Override
    public String toString() {
        String res = "";
        if (unit == METRIC) res += StaticFunctions.roundDouble(distance); else res += StaticFunctions.roundDouble(distance / MILE_PER_KM);
        if (res.length() > 5) res = res.substring(0, 5);
        if (res.startsWith("0.")) {
            res = ("" + distance);
            res = res.replace(".", "");
            if (res.length() > 4) res = res.substring(0, 4);
            while (res.length() < 4) res += "0";
            while (res.startsWith("0")) res = res.substring(1, res.length());
            if (res.length() == 0) res = "0";
            res += "m";
        } else {
            while (res.charAt(res.length() - 1) == '0') res = res.substring(0, res.length() - 1);
            if (res.charAt(res.length() - 1) == '.') res = res.substring(0, res.length() - 1);
            if (unit == METRIC) res += "km"; else res += "mi";
        }
        return res;
    }

    /**
	 * Add a distance to this distance
	 * @param xdistance Distance to add
	 */
    public void addDistance(XDistance xdistance) {
        distance += xdistance.getDistanceInKm();
    }

    /**
	 * Computes the pace according to the given time in seconds per unit.
	 * @param time Time to base the pace on
	 * @return Pace in seconds per distance unit
	 */
    public double getPaceInSeconds(XDuration time) {
        if (time.getTimeInSecs() == 0) return 0;
        if (distance > 0) {
            double secondsperunit = time.getTimeInSecs() / (distance / (unit == ENGLISH ? MILE_PER_KM : 1));
            return secondsperunit;
        } else {
            return 0;
        }
    }

    /**
	 * Computes the pace according to the given time and turns it into a GUI
	 * formatted string
	 * @param time Time to base the pace on
	 * @return Pace formatted for usage in GUI
	 */
    public String getPace(XDuration time) {
        double secondsperunit = getPaceInSeconds(time);
        if (secondsperunit > 0) return XDuration.secondsToTimeString(secondsperunit, false) + (unit == ENGLISH ? "min/mi" : "min/km"); else return "";
    }

    /**
	 * Gets the VDOT value according to the given time and turns it into a GUI
	 * formatted STRING
	 * @param time Time to base the VDOT calculation on
	 * @return VDOT formatted for usage in GUI
	 */
    public String getVDOT(XDuration time) {
        if (time.getTimeInSecs() == 0) return "-";
        if (distance >= 1.5) {
            double fractionOfDay = time.getTimeInSecs() / 86400d;
            double fractionOfVmax = 0.8 + 0.1894393 * Math.exp(-0.012778 * fractionOfDay * 1440) + 0.2989558 * Math.exp(-0.1932605 * fractionOfDay * 1440);
            double vdot = (-4.6 + 0.182258 * distance / fractionOfDay / 1.44 + 0.000104 * Math.pow(distance / fractionOfDay / 1.44, 2)) / fractionOfVmax;
            String ret = StaticFunctions.doubleToString(vdot);
            if (ret.length() > 4) return ret.substring(0, 4); else return ret;
        } else {
            return "-";
        }
    }

    @Override
    public Object clone() {
        XDistance clonedObject = new XDistance(distance, unit);
        return clonedObject;
    }
}
