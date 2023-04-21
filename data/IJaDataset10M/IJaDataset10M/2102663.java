package jhomenet.commons.responsive.condition;

import org.apache.log4j.Logger;
import jhomenet.commons.data.ValueData;
import jhomenet.commons.hw.HardwareException;
import jhomenet.commons.hw.sensor.ValueSensor;
import jhomenet.commons.responsive.ResponsiveException;

/**
 * An immutable implementation of the sensor responsive system (SRS) condition
 * interface that is evaluated based on the difference between two values
 * retrieved from two <code>ValueSensor</code> objects and the test value and
 * test operator. In particular, the three primary inputs to the difference
 * value condition are the test difference value and test operator along
 * with the two value sensors. When evaluated, the value condition
 * compares the difference in values from the two value sensors against
 * the test value and test operator and returns the result of this comparison.
 * <p>
 * As an example, suppose the two test value sensors are temperature sensors
 * and the test difference value is 25F and the test operator is Greater Than.
 * The condition first retrieves the current temperature from the two 
 * temperature sensors and calculates the difference. The condition then
 * compares this difference against the test value using the test operator
 * and returns Boolean.TRUE if the condition is true, otherwise it returns
 * Boolean.FALSE.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class DifferenceValueCondition extends AbstractCondition {

    /**
	 * Define a logging mechanism.
	 */
    private static Logger logger = Logger.getLogger(DifferenceValueCondition.class.getName());

    /**
	 * Reference to the sensor object.
	 */
    private ValueSensor sensor1;

    private ValueSensor sensor2;

    /**
	 * 
	 */
    private Integer sensorChannel1;

    private Integer sensorChannel2;

    /**
	 * The test operator.
	 */
    private ValueConditionOperator testOperator;

    /**
	 * The test value.
	 */
    private ValueData testValue;

    /**
	 * Constructor.
	 * 
	 * @param conditionName The unique condition name
	 * @param sensor1 Sensor #1
	 * @param sensorChannel1 The I/O channel to be used for sensor #1
	 * @param sensor2 Sensor #2
	 * @param sensorChannel2 The I/O channel to be used for sensor #2
	 * @param testValue The test difference value
	 * @param testOperator The test operator
	 */
    public DifferenceValueCondition(String conditionName, ValueSensor sensor1, Integer sensorChannel1, ValueSensor sensor2, Integer sensorChannel2, ValueData testValue, ValueConditionOperator testOperator) {
        super(conditionName);
        if (sensor1 == null) throw new IllegalArgumentException("Sensor #1 cannot be null!");
        if (sensor2 == null) throw new IllegalArgumentException("Sensor #2 cannot be null!");
        if (sensorChannel1 == null) throw new IllegalArgumentException("Sensor #1 channel cannot be null!");
        if (sensorChannel2 == null) throw new IllegalArgumentException("Sensor #2 channel cannot be null!");
        if (testValue == null) throw new IllegalArgumentException("Test value cannot be null!");
        if (testOperator == null) throw new IllegalArgumentException("Test operator cannot be null!");
        this.setSensor1(sensor1);
        this.setSensorChannel1(sensorChannel1);
        this.setSensor2(sensor2);
        this.setSensorChannel2(sensorChannel2);
        this.setTestOperator(testOperator);
        this.setTestValue(testValue);
    }

    /**
	 * Constructor.
	 * 
	 * @param conditionName The unique condition name
	 * @param sensor1 Sensor #1
	 * @param sensor2 Sensor #2
	 * @param testValue The test difference value
	 * @param testOperator The test operator
	 */
    public DifferenceValueCondition(String conditionName, ValueSensor sensor1, ValueSensor sensor2, ValueData testValue, ValueConditionOperator testOperator) {
        this(conditionName, sensor1, new Integer(0), sensor2, new Integer(0), testValue, testOperator);
    }

    /**
	 * @return the sensor1
	 */
    ValueSensor getSensor1() {
        return sensor1;
    }

    /**
	 * @param sensor1 the sensor1 to set
	 */
    void setSensor1(ValueSensor sensor1) {
        this.sensor1 = sensor1;
    }

    /**
	 * @return the sensor2
	 */
    ValueSensor getSensor2() {
        return sensor2;
    }

    /**
	 * @param sensor2 the sensor2 to set
	 */
    void setSensor2(ValueSensor sensor2) {
        this.sensor2 = sensor2;
    }

    /**
	 * @return the sensorChannel1
	 */
    Integer getSensorChannel1() {
        return sensorChannel1;
    }

    /**
	 * @param sensorChannel1 the sensorChannel1 to set
	 */
    void setSensorChannel1(Integer sensorChannel1) {
        this.sensorChannel1 = sensorChannel1;
    }

    /**
	 * @return the sensorChannel2
	 */
    Integer getSensorChannel2() {
        return sensorChannel2;
    }

    /**
	 * @param sensorChannel2 the sensorChannel2 to set
	 */
    void setSensorChannel2(Integer sensorChannel2) {
        this.sensorChannel2 = sensorChannel2;
    }

    /**
	 * Set the test operator.
	 *
	 * @param testOperator
	 */
    public void setTestOperator(ValueConditionOperator testOperator) {
        this.testOperator = testOperator;
    }

    /**
	 * 
	 * @return
	 */
    public ValueConditionOperator getTestOperator() {
        return this.testOperator;
    }

    /**
	 * Set the test value.
	 *
	 * @param testValue
	 */
    public void setTestValue(ValueData testValue) {
        this.testValue = testValue;
    }

    /**
	 * 
	 * @return
	 */
    public ValueData getTestValue() {
        return this.testValue;
    }

    /**
	 * @see jhomenet.commons.responsive.condition.Condition#evaluate()
	 */
    public final ConditionResult evaluate() throws ResponsiveException {
        ValueData data1, data2;
        try {
            data1 = getSensor1().readFromSensor(getSensorChannel1()).getDataObject();
            data2 = getSensor2().readFromSensor(getSensorChannel2()).getDataObject();
            ValueData diff = data1.subtract(data2);
            diff = diff.abs();
            if (this.getTestOperator() == ValueConditionOperator.GREATER_THAN) {
                if (diff.compareTo(this.getTestValue()) > 0) return new DifferenceValueConditionResult(Boolean.TRUE, data1, data2);
            } else if (this.getTestOperator() == ValueConditionOperator.LESS_THAN) {
                if (diff.compareTo(this.getTestValue()) < 0) return new DifferenceValueConditionResult(Boolean.TRUE, data1, data2);
            } else {
            }
        } catch (HardwareException he) {
            logger.error("Error while reading value from sensor: " + he.getMessage());
            throw new ResponsiveException("Error while evaluating condition: cannot read from sensor: ", he);
        }
        return new DifferenceValueConditionResult(Boolean.FALSE, data1, data2);
    }

    private class DifferenceValueConditionResult implements ConditionResult {

        private final Boolean result;

        private final ValueData data1;

        private final ValueData data2;

        private DifferenceValueConditionResult(Boolean result, ValueData data1, ValueData data2) {
            this.result = result;
            this.data1 = data1;
            this.data2 = data2;
        }

        /**
		 * @see jhomenet.commons.responsive.condition.ConditionResult#getResult()
		 */
        @Override
        public Boolean getResult() {
            return result;
        }

        /**
		 * @see jhomenet.commons.responsive.condition.ExpressionResult#getResultAsString()
		 */
        @Override
        public String getResultAsString() {
            return String.valueOf(result);
        }

        /**
		 * @see jhomenet.commons.responsive.condition.ConditionResult#getResultDetails()
		 */
        @Override
        public String getResultDetails() {
            StringBuffer buf = new StringBuffer();
            buf.append("Value condition result");
            buf.append("  Hardware #1 desc: " + getSensor1().getHardwareSetupDescription());
            buf.append("  Hardware #1 address: " + getSensor1().getHardwareAddr());
            buf.append("  Data value #1: " + data1.getValue() + " " + data1.getUnit().toString());
            buf.append("  Hardware #2 desc: " + getSensor2().getHardwareSetupDescription());
            buf.append("  Hardware #2 address: " + getSensor2().getHardwareAddr());
            buf.append("  Data value #2: " + data2.getValue() + " " + data2.getUnit().toString());
            buf.append("  Test value: " + testValue.getValue() + " " + testValue.getUnit().toString());
            buf.append("  Test operator: " + testOperator.getOperatorStr());
            return buf.toString();
        }
    }
}
