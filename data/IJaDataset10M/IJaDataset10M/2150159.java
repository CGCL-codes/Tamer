package jhomenet.responsive;

import java.util.*;
import org.apache.log4j.Logger;
import jhomenet.hw.sensor.*;

/**
 * A sensor responsive value condition.
 * <br>
 * Id: $Id: ValueCondition.java 1139 2006-01-05 22:41:55Z dhirwinjr $
 * 
 * @author David Irwin
 */
public class ValueCondition<T extends ValueSensor> extends SensorCondition<T> {

    /***
     * Define a logging mechanism.
     */
    private static Logger logger = Logger.getLogger(ValueCondition.class.getName());

    /**
     * Define the different operators.
     */
    public static enum ValueOperator {

        _GREATER_THAN(">"), _LESS_THAN("<"), _EQUAL("=");

        private String strRepresentation;

        private ValueOperator(String s) {
            this.strRepresentation = s;
        }

        public String toString() {
            return strRepresentation;
        }

        public boolean equals(ValueOperator testOperator) {
            String testString = testOperator.toString();
            if (testString.equalsIgnoreCase(strRepresentation)) {
                return true;
            }
            return false;
        }
    }

    /**
     * Reference to the value sensor.
     */
    private ValueSensor sensor;

    /**
     * The operator.
     */
    private ValueOperator operator;

    /**
     * The test value.
     */
    private double testValue;

    /**
     * Default constructor.
     */
    public ValueCondition() {
        super();
    }

    /**
     * @see jhomenet.responsive.Condition#setSensor(Sensor)
     */
    public void setSensor(T sensor) {
        this.sensor = sensor;
    }

    /**
     * @see jhomenet.responsive.Condition#getSensor()
     */
    public ValueSensor getSensor() {
        return sensor;
    }

    /**
     * Set the test operator.
     *
     * @param operator
     */
    public void setOperator(ValueOperator operator) {
        this.operator = operator;
    }

    /**
     * Set the test value.
     *
     * @param testValue
     */
    public void setTestValue(double testValue) {
        this.testValue = testValue;
    }

    /** 
     * @see jhomenet.responsive.Condition#getOperators()
     */
    public ArrayList<String> getOperators() {
        ArrayList<String> list = new ArrayList<String>();
        ValueOperator[] tmp = ValueOperator.values();
        for (int i = 0; i < tmp.length; i++) {
            list.add(tmp[i].toString());
        }
        return list;
    }

    /**
     * Get the string representation of the value condition.
     *  
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "[C:V;ID:" + ID + ";HW:(" + sensor.getHardwareId() + "," + sensor.getSetupDescription() + ");D:" + description + ";TV:" + testValue + ";TO:" + operator.toString() + "]";
    }

    /**
     * @see jhomenet.responsive.Condition#equals(jhomenet.responsive.Condition)
     */
    public boolean equals(Condition testCondition) {
        if (!(testCondition instanceof ValueCondition)) {
            return false;
        } else {
            ValueCondition vTestCondition = (ValueCondition) testCondition;
            if (!vTestCondition.ID.equalsIgnoreCase(ID)) {
                return false;
            }
            if (!vTestCondition.operator.equals(operator)) {
                return false;
            }
            if (!(vTestCondition.testValue == testValue)) {
                return false;
            }
            if (!(vTestCondition.sensor == sensor)) {
                return false;
            }
        }
        return true;
    }

    /** 
     * @see jhomenet.responsive.Condition#evaluateToBoolean()
     */
    public boolean evaluateToBoolean() {
        double value;
        try {
            value = sensor.readSensorValue(0).getData();
        } catch (SensorException se) {
            logger.error("Error while reading value from sensor: " + se.getMessage());
            return false;
        }
        switch(operator) {
            case _GREATER_THAN:
                if (value > testValue) {
                    return true;
                }
                break;
            case _LESS_THAN:
                if (value < testValue) {
                    return true;
                }
                break;
            case _EQUAL:
                if (value == testValue) {
                    return true;
                }
                break;
            default:
                logger.debug("Unknown operator type: " + operator);
        }
        return false;
    }

    /** 
     * @see jhomenet.responsive.ExpressionElement#isSimple()
     */
    public boolean isSimple() {
        return false;
    }
}
