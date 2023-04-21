package org.tlcdcemu.model;

import org.tlcdcemu.util.ConvertUtils;

/**
 *
 */
public class PlayingStatus {

    private short CDBitmap = 0x80;

    private short maxTrackCD = 101;

    private short currentCD = 1;

    private short currentTrack = 1;

    private int CDTimeMs = 1000;

    private int trackTimeMs = 1000;

    private boolean isRandom = false;

    private static final int TRACK_TIME_MS = 3 * 60 * 1000;

    private static final int TRACKS_TOTAL = 20;

    public PlayingStatus(short cdBitmap) {
        this.CDBitmap = cdBitmap;
    }

    public short getCDBitmap() {
        return CDBitmap;
    }

    public void setCDBitmap(short CDBitmap) {
        this.CDBitmap = CDBitmap;
    }

    public boolean isCDPresent(short CDNumber) {
        return (CDBitmap & (1 << (8 - CDNumber))) > 0;
    }

    public short getMaxTrackCD() {
        return maxTrackCD;
    }

    public void setMaxTrackCD(short maxTrackCD) {
        this.maxTrackCD = maxTrackCD;
    }

    public int getCurrentCD() {
        return currentCD;
    }

    public void setCurrentCD(int currentCD) {
        this.currentCD = (short) currentCD;
    }

    public short getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(int currentTrack) {
        this.currentTrack = (short) currentTrack;
    }

    public int getCDTimeMs() {
        return CDTimeMs;
    }

    public void setCDTimeMs(int CDTimeMs) {
        this.CDTimeMs = CDTimeMs;
    }

    public int getTrackTimeMs() {
        return trackTimeMs;
    }

    public void setTrackTimeMs(int trackTimeMs) {
        this.trackTimeMs = trackTimeMs;
    }

    public boolean isRandom() {
        return isRandom;
    }

    public void setRandom(boolean random) {
        isRandom = random;
    }

    public String toString() {
        return "CD " + currentCD + " (" + ConvertUtils.getHMSTime(CDTimeMs) + ") TR " + currentTrack + " (" + ConvertUtils.getHMSTime(trackTimeMs) + "). Random is " + (isRandom ? "ON" : "OFF");
    }

    public void incrementTrackNumber() {
        short nextTrack = (short) (currentTrack + 1);
        if (nextTrack > maxTrackCD) {
            nextTrack = 1;
        }
        currentTrack = nextTrack;
        trackTimeMs = 0;
    }

    public void decrementTrackNumber() {
        short previousTrack = (short) (currentTrack - 1);
        if (previousTrack < 1) {
            previousTrack = maxTrackCD;
        }
        currentTrack = previousTrack;
        trackTimeMs = 0;
    }

    public int getTrackLength(int currentTrack) {
        return TRACK_TIME_MS;
    }

    public int getMaxTrack() {
        return TRACKS_TOTAL;
    }

    public int getTotalCDTime() {
        return TRACKS_TOTAL * TRACK_TIME_MS;
    }
}
