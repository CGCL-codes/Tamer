package com.spotimage.eosps;

import java.util.ArrayList;
import java.util.List;
import org.vast.util.DateTime;

/**
 * <p><b>Title:</b><br/>
 * Segment
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Characteristics of an acquisition segment as defined in the EO SPS standard.
 * </p>
 *
 * <p>Copyright (c) 2008, Spot Image</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Jun, 28th 2010
 * @since 2.0
 */
public class Segment extends EOReportObject {

    protected DateTime acquisitionStartTime;

    protected DateTime acquisitionStopTime;

    protected String platformID;

    protected String platformName;

    protected String instrumentID;

    protected String instrumentName;

    protected String instrumentMode;

    protected int orbitNumber = -1;

    protected float groundResolution = Float.NaN;

    protected float incidenceAngle = Float.NaN;

    protected float pitch = Float.NaN;

    protected float roll = Float.NaN;

    protected float yaw = Float.NaN;

    protected List<Segment> relatedSegments;

    public DateTime getAcquisitionStartTime() {
        return acquisitionStartTime;
    }

    public void setAcquisitionStartTime(DateTime acquisitionStartTime) {
        this.acquisitionStartTime = acquisitionStartTime;
    }

    public DateTime getAcquisitionStopTime() {
        return acquisitionStopTime;
    }

    public void setAcquisitionStopTime(DateTime acquisitionStopTime) {
        this.acquisitionStopTime = acquisitionStopTime;
    }

    public String getPlatformID() {
        return platformID;
    }

    public void setPlatformID(String platformID) {
        this.platformID = platformID;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getInstrumentID() {
        return instrumentID;
    }

    public void setInstrumentID(String instrumentID) {
        this.instrumentID = instrumentID;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public String getInstrumentMode() {
        return instrumentMode;
    }

    public void setInstrumentMode(String instrumentMode) {
        this.instrumentMode = instrumentMode;
    }

    public int getOrbitNumber() {
        return orbitNumber;
    }

    public void setOrbitNumber(int orbitNumber) {
        this.orbitNumber = orbitNumber;
    }

    public float getGroundResolution() {
        return groundResolution;
    }

    public void setGroundResolution(float groundResolution) {
        this.groundResolution = groundResolution;
    }

    public float getIncidenceAngle() {
        return incidenceAngle;
    }

    public void setIncidenceAngle(float incidenceAngle) {
        this.incidenceAngle = incidenceAngle;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public List<Segment> getRelatedSegments() {
        if (relatedSegments == null) relatedSegments = new ArrayList<Segment>();
        return relatedSegments;
    }

    public void setRelatedSegments(List<Segment> relatedSegments) {
        this.relatedSegments = relatedSegments;
    }
}
