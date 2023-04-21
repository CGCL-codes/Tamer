package jmms.processor;

/**
 * @author wooller
 *
 *18/01/2005
 *
 * Copyright JEDI/Rene Wooller
 *
 */
public class ActuatorEvent {

    private double min, max, value;

    private int time;

    /**
	 * 
	 */
    public ActuatorEvent(double min, double max, double value, int time) {
        super();
        this.min = min;
        this.max = max;
        this.value = value;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getNormValue() {
        return (value - min) / (max - min);
    }

    public String toString() {
        return "min " + min + " max " + max + " value " + value + " time " + time;
    }
}
