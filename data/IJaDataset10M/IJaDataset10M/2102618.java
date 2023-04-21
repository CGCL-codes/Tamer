package edu.rpi.usf.types;

import edu.rpi.usf.exceptions.ConfigException;
import edu.rpi.usf.utils.*;

/**
 * This class implements the parameter type<br>
 * <i>Currently, step and default are not used</i><br>
 * <br>
 * This class is a foundation class for USF since it represents 
 * the individual input parameters to the model being tested.
 * @author  yet3
 * @version  1.0
 */
public class Parameter implements Cloneable, java.io.Serializable {

    static final long serialVersionUID = 1;

    private String name;

    private double min = 0.0;

    private double max = 0.0;

    private double def = 0.0;

    private double step = 0.0;

    private boolean isInt;

    /** 
	 * Creates new Parameter
	 */
    public Parameter() {
        isInt = false;
    }

    public Parameter(String vName, double vMin, double vMax, boolean vIsInt) {
        name = vName;
        min = vMin;
        max = vMax;
        isInt = vIsInt;
    }

    public Parameter(String vName, String values[]) throws ConfigException {
        if (values.length != 5) throw new ConfigException("Bad values for parameter: " + vName);
        name = vName;
        min = Double.parseDouble(values[0]);
        max = Double.parseDouble(values[1]);
        def = Double.parseDouble(values[2]);
        step = Double.parseDouble(values[3]);
        if (values[4].equalsIgnoreCase("Y") || values[4].equalsIgnoreCase("YES")) isInt = true; else isInt = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double aMin) {
        min = aMin;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double aMax) {
        max = aMax;
    }

    public void setRange(double aMin, double aMax) {
        min = aMin;
        max = aMax;
    }

    public void setDefault(double d) {
        def = d;
    }

    public void setStep(double s) {
        step = s;
    }

    public void setIsInt(boolean b) {
        isInt = b;
    }

    public boolean isInt() {
        return isInt;
    }

    /**
	 * This method returns a random value for this parameter which
	 * is either an int, or double
	 */
    public double getRandomValue() {
        return isInt ? UsfUtil.getRandomInt((int) min, (int) max) : UsfUtil.getRandomDouble(min, max);
    }

    public Object clone() {
        Parameter p = new Parameter();
        p.setRange(min, max);
        p.setName(name);
        p.setDefault(def);
        p.setStep(step);
        p.setIsInt(isInt);
        return p;
    }

    public String toString() {
        return "Parameter: " + name + "\n\tmin: " + min + "\n\tmax: " + max + "\n\tdef: " + def + "\n\tstep: " + step + "\n\tis_int? " + isInt + "\n";
    }
}
