package com.xiledsystems.AlternateJavaBridgelib.components.altbridge;

import android.os.Handler;

/**
 * A component that provides a high-level interface to an ultrasonic sensor on a LEGO
 * MINDSTORMS NXT robot.
 *
 */
public class NxtUltrasonicSensor extends LegoMindstormsNxtSensor implements Deleteable {

    private enum State {

        UNKNOWN, BELOW_RANGE, WITHIN_RANGE, ABOVE_RANGE
    }

    private static final String DEFAULT_SENSOR_PORT = "4";

    private static final int DEFAULT_BOTTOM_OF_RANGE = 30;

    private static final int DEFAULT_TOP_OF_RANGE = 90;

    private Handler handler;

    private final Runnable sensorReader;

    private State previousState;

    private int bottomOfRange;

    private int topOfRange;

    private boolean belowRangeEventEnabled;

    private boolean withinRangeEventEnabled;

    private boolean aboveRangeEventEnabled;

    /**
   * Creates a new NxtUltrasonicSensor component.
   */
    public NxtUltrasonicSensor(ComponentContainer container) {
        super(container, "NxtUltrasonicSensor");
        handler = new Handler();
        previousState = State.UNKNOWN;
        sensorReader = new Runnable() {

            public void run() {
                if (bluetooth != null && bluetooth.IsConnected()) {
                    SensorValue<Integer> sensorValue = getDistanceValue("");
                    if (sensorValue.valid) {
                        State currentState;
                        if (sensorValue.value < bottomOfRange) {
                            currentState = State.BELOW_RANGE;
                        } else if (sensorValue.value > topOfRange) {
                            currentState = State.ABOVE_RANGE;
                        } else {
                            currentState = State.WITHIN_RANGE;
                        }
                        if (currentState != previousState) {
                            if (currentState == State.BELOW_RANGE && belowRangeEventEnabled) {
                                BelowRange();
                            }
                            if (currentState == State.WITHIN_RANGE && withinRangeEventEnabled) {
                                WithinRange();
                            }
                            if (currentState == State.ABOVE_RANGE && aboveRangeEventEnabled) {
                                AboveRange();
                            }
                        }
                        previousState = currentState;
                    }
                }
                if (isHandlerNeeded()) {
                    handler.post(sensorReader);
                }
            }
        };
        SensorPort(DEFAULT_SENSOR_PORT);
        BottomOfRange(DEFAULT_BOTTOM_OF_RANGE);
        TopOfRange(DEFAULT_TOP_OF_RANGE);
        BelowRangeEventEnabled(false);
        WithinRangeEventEnabled(false);
        AboveRangeEventEnabled(false);
    }

    @Override
    protected void initializeSensor(String functionName) {
        setInputMode(functionName, port, SENSOR_TYPE_LOWSPEED_9V, SENSOR_MODE_RAWMODE);
        configureUltrasonicSensor(functionName);
    }

    private void configureUltrasonicSensor(String functionName) {
        byte[] data = new byte[3];
        data[0] = (byte) 0x02;
        data[1] = (byte) 0x41;
        data[2] = (byte) 0x02;
        lsWrite(functionName, port, data, 0);
    }

    /**
   * Specifies the sensor port that the sensor is connected to.
   */
    public void SensorPort(String sensorPortLetter) {
        setSensorPort(sensorPortLetter);
    }

    public int GetDistance() {
        String functionName = "GetDistance";
        if (!checkBluetooth(functionName)) {
            return -1;
        }
        SensorValue<Integer> sensorValue = getDistanceValue(functionName);
        if (sensorValue.valid) {
            return sensorValue.value;
        }
        return -1;
    }

    private SensorValue<Integer> getDistanceValue(String functionName) {
        byte[] data = new byte[2];
        data[0] = (byte) 0x02;
        data[1] = (byte) 0x42;
        lsWrite(functionName, port, data, 1);
        for (int i = 0; i < 3; i++) {
            int countAvailableBytes = lsGetStatus(functionName, port);
            if (countAvailableBytes > 0) {
                byte[] returnPackage = lsRead(functionName, port);
                if (returnPackage != null) {
                    int value = getUBYTEValueFromBytes(returnPackage, 4);
                    if (value >= 0 && value <= 254) {
                        return new SensorValue<Integer>(true, value);
                    }
                }
                break;
            }
        }
        return new SensorValue<Integer>(false, null);
    }

    /**
   * Returns the bottom of the range used for the BelowRange, WithinRange,
   * and AboveRange events.
   */
    public int BottomOfRange() {
        return bottomOfRange;
    }

    /**
   * Specifies the bottom of the range used for the BelowRange, WithinRange,
   * and AboveRange events.
   */
    public void BottomOfRange(int bottomOfRange) {
        this.bottomOfRange = bottomOfRange;
        previousState = State.UNKNOWN;
    }

    /**
   * Returns the top of the range used for the BelowRange, WithinRange, and
   * AboveRange events.
   */
    public int TopOfRange() {
        return topOfRange;
    }

    /**
   * Specifies the top of the range used for the BelowRange, WithinRange, and
   * AboveRange events.
   */
    public void TopOfRange(int topOfRange) {
        this.topOfRange = topOfRange;
        previousState = State.UNKNOWN;
    }

    /**
   * Returns whether the BelowRange event should fire when the distance
   * goes below the BottomOfRange.
   */
    public boolean BelowRangeEventEnabled() {
        return belowRangeEventEnabled;
    }

    /**
   * Specifies whether the BelowRange event should fire when the distance
   * goes below the BottomOfRange.
   */
    public void BelowRangeEventEnabled(boolean enabled) {
        boolean handlerWasNeeded = isHandlerNeeded();
        belowRangeEventEnabled = enabled;
        boolean handlerIsNeeded = isHandlerNeeded();
        if (handlerWasNeeded && !handlerIsNeeded) {
            handler.removeCallbacks(sensorReader);
        }
        if (!handlerWasNeeded && handlerIsNeeded) {
            previousState = State.UNKNOWN;
            handler.post(sensorReader);
        }
    }

    public void BelowRange() {
        EventDispatcher.dispatchEvent(this, "BelowRange");
    }

    /**
   * Returns whether the WithinRange event should fire when the distance
   * goes between the BottomOfRange and the TopOfRange.
   */
    public boolean WithinRangeEventEnabled() {
        return withinRangeEventEnabled;
    }

    /**
   * Specifies whether the WithinRange event should fire when the distance
   * goes between the BottomOfRange and the TopOfRange.
   */
    public void WithinRangeEventEnabled(boolean enabled) {
        boolean handlerWasNeeded = isHandlerNeeded();
        withinRangeEventEnabled = enabled;
        boolean handlerIsNeeded = isHandlerNeeded();
        if (handlerWasNeeded && !handlerIsNeeded) {
            handler.removeCallbacks(sensorReader);
        }
        if (!handlerWasNeeded && handlerIsNeeded) {
            previousState = State.UNKNOWN;
            handler.post(sensorReader);
        }
    }

    public void WithinRange() {
        EventDispatcher.dispatchEvent(this, "WithinRange");
    }

    /**
   * Returns whether the AboveRange event should fire when the distance
   * goes above the TopOfRange.
   */
    public boolean AboveRangeEventEnabled() {
        return aboveRangeEventEnabled;
    }

    /**
   * Specifies whether the AboveRange event should fire when the distance
   * goes above the TopOfRange.
   */
    public void AboveRangeEventEnabled(boolean enabled) {
        boolean handlerWasNeeded = isHandlerNeeded();
        aboveRangeEventEnabled = enabled;
        boolean handlerIsNeeded = isHandlerNeeded();
        if (handlerWasNeeded && !handlerIsNeeded) {
            handler.removeCallbacks(sensorReader);
        }
        if (!handlerWasNeeded && handlerIsNeeded) {
            previousState = State.UNKNOWN;
            handler.post(sensorReader);
        }
    }

    public void AboveRange() {
        EventDispatcher.dispatchEvent(this, "AboveRange");
    }

    private boolean isHandlerNeeded() {
        return belowRangeEventEnabled || withinRangeEventEnabled || aboveRangeEventEnabled;
    }

    @Override
    public void onDelete() {
        handler.removeCallbacks(sensorReader);
    }
}
