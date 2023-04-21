package org.cdp1802.upb.impl;

import static org.cdp1802.upb.UPBConstants.*;
import org.cdp1802.upb.UPBDeviceEvent;
import org.cdp1802.upb.UPBDimmableI;
import org.cdp1802.upb.UPBMessage;
import org.cdp1802.upb.UPBMsgType;
import org.cdp1802.upb.UPBNetworkI;
import org.cdp1802.upb.UPBProductI;

/**
 * And extension to UPBDevice for devices that allow themsevles to 
 * be set to any level between 0 and 100%
 *
 * @author gerry
 */
public class UPBDimmerDevice extends UPBDevice implements UPBDimmableI {

    static final double fadeRates[] = new double[] { 0, 0.8, 1.6, 3.3, 5, 6.6, 10, 20, 30, 60, 120, 300, 600, 900, 1800, 3600 };

    static final String fadeRateLabels[] = new String[] { "Snap!", "0.8 seconds", "1.6 seconds", "3.3 seconds", "5 seconds", "6.6 seconds", "10 seconds", "20 seconds", "30 seconds", "1 minute", "2 minutes", "5 minutes", "10 minutes", "15 minutes", "30 minutes", "1 hour" };

    /**
   * Return the number of legal fade rates
   *
   * @return number of valid fade rate times
   */
    public int getFadeRateCount() {
        return fadeRates.length;
    }

    /**
   * For a given index into the fade rates, produce a textual label that 
   * describes the fade rate in more human readable terms
   *
   * @param rateIndex to return label for
   * @return the label fo the specified rate index
   */
    public String getFadeRateLabelAt(int rateIndex) {
        return fadeRateLabels[rateIndex];
    }

    /**
   * Return the actual time a fade-rate will take, in seconds.  
   *
   * @param rateIndex index of fade-rate to return time in seconds for
   * @return time in seconds for the passed rate index
   */
    public double getFadeRateTimeAt(int rateIndex) {
        return fadeRates[rateIndex];
    }

    /**
   * Given an arbitrary number of seconds for a fade-rate, find the closest
   * allowed fade rate and return the index of it.
   *
   * <P>This helps to smooth human interaction (if they say 4 seconds, we'd round
   * that to the nearest legal fade rate of 3.3 (index 3)
   *
   * @param forTime time, in seconds of fade rate you'd like
   * @return index of the closest legal fade rate to the passed time
   */
    public int getFadeRate(double forTime) {
        if (forTime <= fadeRates[0]) return 0;
        if (forTime >= fadeRates[fadeRates.length - 1]) return fadeRates.length - 1;
        double lowDiff = 0, highDiff = 0;
        for (int rateIndex = 1; rateIndex < fadeRates.length; rateIndex++) {
            if (forTime > fadeRates[rateIndex]) continue;
            lowDiff = forTime - fadeRates[rateIndex - 1];
            highDiff = fadeRates[rateIndex] - forTime;
            return ((lowDiff < highDiff) ? rateIndex - 1 : rateIndex);
        }
        return fadeRates.length - 1;
    }

    /**
   * Given an arbitrary number of seconds for a fade-rate, find the closest
   * allowed fade rate and return the index of it.
   *
   * <P>This helps to smooth human interaction (if they say 4 seconds, we'd round
   * that to the nearest legal fade rate of 3.3 (index 3)
   *
   * @param forWholeSeconds time, in seconds of fade rate you'd like
   * @return index of the closest legal fade rate to the passed time
   */
    public int getFadeRate(int forWholeSeconds) {
        return getFadeRate((double) forWholeSeconds);
    }

    /**
   * Given a passed fade rate as an internal fade rate value, format
   * it as a textural representation that can be later parsed by the
   * parseFadeRate call.
   *
   * @param fadeRate fade rate to format
   * @param allowDefault true to allow invalid values to be returned a "default", false for "bad" (usually, use true)
   * @return formatted/printable version of this fade rate
   */
    public String formatFadeRate(int fadeRate, boolean allowDefault) {
        if (fadeRate == 0) return "0";
        if ((fadeRate < 0) || (fadeRate >= getFadeRateCount()) && allowDefault) return "default";
        if (fadeRate < getFadeRateCount()) return String.valueOf(getFadeRateTimeAt(fadeRate));
        return "bad";
    }

    /**
   * Given a passed textual representation of a fade rate, return the 
   * appropriate fade rate value or -1 if it's invalid
   *
   * @param theFadeRate text version of the fade rate
   * @return fade rate value or -1
   */
    public int parseFadeRate(String theFadeRate) {
        if ((theFadeRate == null) || (theFadeRate.length() == 0)) return -1;
        if (theFadeRate.equalsIgnoreCase("default")) return DEFAULT_FADE_RATE;
        try {
            return getFadeRate(Double.parseDouble(theFadeRate));
        } catch (Exception numError) {
            return -1;
        }
    }

    int defaultFadeRate[] = null;

    UPBDimmerDevice() {
    }

    UPBDimmerDevice(UPBNetworkI theNetwork, UPBProductI theProduct, int deviceID) {
        super(theNetwork, theProduct, deviceID);
    }

    void debug(String theMessage) {
        deviceNetwork.getUPBManager().upbDebug("DIM_DEVICE[" + deviceID + "]:: " + theMessage);
    }

    public void setDeviceInfo(UPBNetworkI theNetwork, UPBProductI theProduct, int deviceID) {
        super.setDeviceInfo(theNetwork, theProduct, deviceID);
        defaultFadeRate = new int[(getChannelCount() < 1) ? 1 : getChannelCount()];
        for (int chanIndex = 0; chanIndex < deviceState.length; chanIndex++) {
            defaultFadeRate[chanIndex] = 0;
        }
    }

    /**
   * Get the default fade rate for this device.  All dimmable devices have
   * a default fade rate -- the rate that the device takes to transition from
   * one level to another.  On most devices, this rate is configuable.
   *
   * <P>When a request is made to change the level of a device and that request
   * is not specific about how fast, this rate is used.
   *
   * @param forChannel channel to get default fade rate for
   * @return default fade rate for this device
   */
    public int getDefaultFadeRate(int forChannel) {
        return defaultFadeRate[forChannel - 1];
    }

    public void setDefaultFadeRate(int theRate, int forChannel) {
        defaultFadeRate[forChannel - 1] = theRate;
    }

    /**
   * Get the default fade rate for this device.  All dimmable devices have
   * a default fade rate -- the rate that the device takes to transition from
   * one level to another.  On most devices, this rate is configuable.
   *
   * <P>When a request is made to change the level of a device and that request
   * is not specific about how fast, this rate is used.
   *
   * @return default fade rate for this device
   */
    public int getDefaultFadeRate() {
        return getDefaultFadeRate(upbProduct.getPrimaryChannel());
    }

    /**
   * Get the current level for given channel of this device.  You should only do this after 
   * checking that isDeviceStateValid() is true.  This will return a value
   * between 0 and 100 (percent).
   *
   * @param forChannel channel to check level for
   * @return level device is currently at, 0 to 100
   */
    public int getDeviceLevel(int forChannel) {
        return deviceState[forChannel - 1];
    }

    /**
   * Get the current level of the primary channel for this device.  You should only do this after 
   * checking that isDeviceStateValid() is true.  This will return a value
   * between 0 and 100 (percent).
   *
   * @return level device is currently at, 0 to 100
   */
    public int getDeviceLevel() {
        return getDeviceLevel(upbProduct.getPrimaryChannel());
    }

    /**
   * Tell the device to change the level of the passed channel to the passed
   * level at the passed rate.
   *
   * @param toLevel new level (0-100) for the device to goto or DEFAULT_DIM_LEVEL or LAST_DIM_LEVEL
   * @param fadeRate rate/speed device to goto new level at
   * @param channel channel that device should change to reflect this
   */
    public void setDeviceLevel(int toLevel, int fadeRate, int channel) {
        transmitNewDeviceLevel(toLevel, fadeRate, channel);
    }

    /**
   * Tell the device to change it's level to the passed level at the 
   * passed rate.
   *
   * @param toLevel new level (0-100) for the device to goto or DEFAULT_DIM_LEVEL or LAST_DIM_LEVEL
   * @param fadeRate rate/speed device to goto new level at
   */
    public void setDeviceLevel(int toLevel, int fadeRate) {
        setDeviceLevel(toLevel, fadeRate, ALL_CHANNELS);
    }

    /**
   * Tell the device to change it's level to the passed level.  The passed
   * level must be between 0 and 100 or DEFAULT_DIM_LEVEL or LAST_DIM_LEVEL
   *
   * <P>In this method, the switch will use it's default fade rate to effect
   * it's change
   *
   * @param theLevel level to set device to (0-100) or DEFAULT_DIM_LEVEL or LAST_DIM_LEVEL
   */
    public void setDeviceLevel(int theLevel) {
        setDeviceLevel(theLevel, DEFAULT_FADE_RATE, ALL_CHANNELS);
    }

    /**
   * Tell the channel on this UPB device to turn on.  For dimming devices, ON means go to
   * the default level for the device.
   *
   * <P>If this is a change for the device, a device state message will be sent to listeners
   *
   * @param forChannel channel to turn on
   */
    public void turnDeviceOn(int forChannel) {
        transmitNewDeviceLevel(DEFAULT_DIM_LEVEL, DEFAULT_FADE_RATE, forChannel);
    }

    /**
   * Tell all channels on this UPB device to turn on.  For dimming devices, ON means go to
   * the default level for the device.
   *
   * <P>If this is a change for the device, a device state message will be sent to listeners
   *
   */
    public void turnDeviceOn() {
        transmitNewDeviceLevel(DEFAULT_DIM_LEVEL, DEFAULT_FADE_RATE, ALL_CHANNELS);
    }

    /**
   * Tell the device to start changing it's level to the passed level over the
   * passed amount of time.
   *
   * <P>This is very similar to the setDeviceLevel() method (in fact, they do the
   * same basic thing), but it's usually used with a later stopFade to halt
   * the transition to the level where it is at.  This is really only of value
   * for longer fade rates.
   *
   * @param toLevel level to set device to (0-100) or DEFAULT_DIM_LEVEL or LAST_DIM_LEVEL
   * @param atFadeRate how fast should the change take
   * @param forChannel channel fading should happen on
   */
    public void startFade(int toLevel, int atFadeRate, int forChannel) {
        if (!deviceNetwork.getUPBManager().sendConfirmedMessage(new UPBMessage(this, UPBMsgType.FADE_START, toLevel, atFadeRate, forChannel))) return;
        updateInternalDeviceLevel(toLevel, atFadeRate, forChannel);
        deviceNetwork.getUPBManager().fireDeviceEvent(this, UPBDeviceEvent.EventCode.DEVICE_START_FADE, forChannel);
    }

    /**
   * Tell the device to start changing it's level to the passed level over the
   * passed amount of time.
   *
   * <P>This is very similar to the setDeviceLevel() method (in fact, they do the
   * same basic thing), but it's usually used with a later stopFade to halt
   * the transition to the level where it is at.  This is really only of value
   * for longer fade rates.
   *
   * @param toLevel level to set device to (0-100) or DEFAULT_DIM_LEVEL or LAST_DIM_LEVEL
   * @param atFadeRate how fast should the change take
   */
    public void startFade(int toLevel, int atFadeRate) {
        startFade(toLevel, atFadeRate, ALL_CHANNELS);
    }

    /**
   * Tell the device to start changing it's level to the passed level at the 
   * devices default fade rate
   *
   * <P>This is very similar to the setDeviceLevel() method (in fact, they do the
   * same basic thing), but it's usually used with a later stopFade to halt
   * the transition to the level where it is at.  This is really only of value
   * for longer fade rates.
   *
   * @param toLevel level to set device to (0-100) or DEFAULT_DIM_LEVEL or LAST_DIM_LEVEL
   */
    public void startFade(int toLevel) {
        startFade(toLevel, DEFAULT_FADE_RATE, ALL_CHANNELS);
    }

    /**
   * If the device is currently in the process of fading to a new level, this will
   * stop the fade immediately and leave the device at whatever level it was
   * at when the device received the command
   *
   * <P>NOTE: Stopping fading stops ALL channels of a device.  There is no ability
   * to stop fading on a single channel in UPB
   */
    public void stopFade() {
        if (!deviceNetwork.getUPBManager().sendConfirmedMessage(new UPBMessage(this, UPBMsgType.FADE_STOP))) return;
        deviceNetwork.getUPBManager().fireDeviceEvent(this, UPBDeviceEvent.EventCode.DEVICE_STOP_FADE, ALL_CHANNELS);
        deviceNetwork.getUPBManager().queueStateRequest(this);
    }

    public void releaseResources() {
        defaultFadeRate = null;
        super.releaseResources();
    }
}
