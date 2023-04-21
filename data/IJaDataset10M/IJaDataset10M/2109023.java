package com.sun.spot.sensorboard.io;

import java.io.IOException;

/**
 * IScalarInput provides access to the values returned by an analogue input device.
 * The value range is from zero to the value returned by {@link #getRange()}
 *
 * @author Syntropy & Ron Goldman
 */
public interface IScalarInput {

    /**
     * Return the PinDescriptor associated with this input pin.
     *
     * @return the PinDescriptor associated with this input pin.
     */
    public PinDescriptor getIndex();

    /**
     * Return the maximum possible value that could be returned from the input device.
     *
     * @return maximum possible value that could be returned from the input device
     * @throws IOException
     */
    int getRange() throws IOException;

    /**
     * Return the current value of the input.
     *
     * @return current value of the input
     * @throws IOException
     */
    int getValue() throws IOException;

    /**
     * Returns whether the underlying hardware supports threshold events.
     *
     * @return true if threshold events are supported
     */
    public boolean supportsThresholdEvents();

    /**
     * Adds the specified scalar input threshold listener to receive callbacks
     * from this input device. Callbacks occur when the scalar input reading
     * falls below or equal to the low threshold value, or when it rises above
     * or equal to the high threshold value.
     *
     * @param who the light sensor threshold listener to add.
     */
    void addIScalarInputThresholdListener(IScalarInputThresholdListener who);

    /**
     * Removes the specified scalar input threshold listener so that it no longer 
     * receives callbacks from this input device. This method performs no function,
     * nor does it throw an exception, if the listener specified by the argument
     * was not previously added to this input device.
     *
     * @param who the scalar input threshold listener to remove.
     */
    void removeIScalarInputThresholdListener(IScalarInputThresholdListener who);

    /**
     * Returns an array of all the scalar input threshold listeners registered
     * on this input device.
     *
     * @return all of this scalar input's ILightSensorThresholdListener or an empty
     *         array if no scalar input threshold listeners are currently registered.
     */
    IScalarInputThresholdListener[] getIScalarInputThresholdListeners();

    /**
     * Set the low and high threshold values for this scalar input. Callbacks
     * occur when the scalar input reading falls below or equal to the low
     * threshold value, or when it rises above or equal to the high threshold
     * value.
     *
     * Note: some devices, such as the ADT7411, only compare against the most
     * significant 8-bits.
     *
     * @param low the new low threshold value.
     * @param high the new high threshold value.
     */
    void setThresholds(int low, int high);

    /**
     * Return the current low threshold value.
     *
     * @return the current low threshold value.
     */
    int getLowThreshold();

    /**
     * Return the current high threshold value.
     *
     * @return the current high threshold value.
     */
    int getHighThreshold();

    /**
     * Enable or disable threshold events.
     *
     * @param enable if true then listeners will be notified if the scalar input
     *               reading goes above the high threshold or below the low threshold.
     */
    void enableThresholdEvents(boolean enable);

    /**
     * Return whether threshold events are enabled or not.
     *
     * @return true if listeners will be notified if the scalar input reading
     *         goes above the high threshold or below the low threshold.
     */
    boolean areThresholdEventsEnabled();
}
