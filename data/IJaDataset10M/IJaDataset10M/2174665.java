package org.chernovia.sims.wondrous;

import java.util.Vector;
import org.chernovia.lib.misc.MiscUtil;
import org.chernovia.lib.sims.ca.CA_Engine;

public class JWondrousMachine extends CA_Engine {

    public static Vector<JWondrousMachine> MACHINES;

    public static final String CR = System.getProperty("line.separator");

    public static final int DEF_SPD = 250, DEF_LIMIT = 255, DEF_MULT = 3, DEF_VOL = 88;

    public static final int[] DEF_ORCH = { 71, 0, 40, 68, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    public static int MAX_CHAN = 8, MAX_INST = 128, MAX_VOL = 128, MIN_LIMIT = 25, MAX_LIMIT = 9901, MIN_VAL = 25, MAX_VAL = 9901, MIN_MULT = 3, MAX_MULT = 16, MIN_SPD = 25, MAX_SPD = 9901, MIN_PITCH = 33, MAX_PITCH = 128;

    private int min_pitch = MIN_PITCH, max_pitch = MAX_PITCH;

    private int chan = 0, inst = 0, start_val = 3, end_val = 9999, curr_val = 3, vol = DEF_VOL, limit = DEF_LIMIT, mult = DEF_MULT, speed = DEF_SPD;

    public JWondrousMachine(int ch, int i, int v, int spd, int minPitch, int maxPitch, int startVal, int endVal, int lim, int m) {
        setChannel(ch);
        setInstrument(i);
        setVolume(v);
        setSpeed(spd);
        setMinPitch(minPitch);
        setMaxPitch(maxPitch);
        setCurrentValue(startVal);
        setStartValue(startVal);
        setEndValue(endVal);
        setLimit(lim);
        setMultiplier(m);
    }

    public JWondrousMachine(JWondrousMachine jwm) {
        setChannel(jwm.getChannel());
        setInstrument(jwm.getInstrument());
        setVolume(jwm.getVolume());
        setSpeed(jwm.getSpeed());
        setMinPitch(jwm.getMinPitch());
        setMaxPitch(jwm.getMaxPitch());
        setStartValue(jwm.getStartValue());
        setEndValue(jwm.getEndValue());
        setCurrentValue(jwm.getStartValue());
        setLimit(jwm.getLimit());
        setMultiplier(jwm.getMultiplier());
    }

    public boolean setChannel(int ch) {
        if (ch >= 0 && ch < MAX_CHAN) {
            chan = ch;
            return true;
        } else return false;
    }

    public boolean setInstrument(int i) {
        if (i >= 0 && i < MAX_INST) {
            inst = i;
            return true;
        } else return false;
    }

    public boolean setVolume(int v) {
        if (v >= 0 && v < MAX_VOL) {
            vol = v;
            return true;
        } else return false;
    }

    public boolean setMinPitch(int p) {
        if (p >= MIN_PITCH && p < max_pitch) {
            min_pitch = p;
            return true;
        } else return false;
    }

    public boolean setMaxPitch(int p) {
        if (p >= min_pitch && p < MAX_PITCH) {
            max_pitch = p;
            return true;
        } else return false;
    }

    public boolean setCurrentValue(int v) {
        if (v >= start_val && v < end_val) {
            curr_val = v;
            return true;
        } else return false;
    }

    public boolean setStartValue(int v) {
        if (v >= MIN_VAL && v < end_val) {
            start_val = v;
            return true;
        } else return false;
    }

    public boolean setEndValue(int v) {
        if (v >= start_val && v < MAX_VAL) {
            end_val = v;
            return true;
        } else return false;
    }

    public boolean setLimit(int l) {
        if (l >= MIN_LIMIT && l < MAX_LIMIT) {
            limit = l;
            return true;
        } else return false;
    }

    public boolean setMultiplier(int m) {
        if (m >= MIN_MULT && m < MAX_MULT) {
            mult = m;
            return true;
        } else return false;
    }

    public boolean setSpeed(int s) {
        if (s >= MIN_SPD && s < MAX_SPD) {
            speed = s;
            return true;
        } else return false;
    }

    public int getChannel() {
        return chan;
    }

    public int getInstrument() {
        return inst;
    }

    public int getVolume() {
        return vol;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCurrentValue() {
        return curr_val;
    }

    public int getStartValue() {
        return start_val;
    }

    public int getEndValue() {
        return end_val;
    }

    public int getMinPitch() {
        return min_pitch;
    }

    public int getMaxPitch() {
        return max_pitch;
    }

    public int getLimit() {
        return limit;
    }

    public int getMultiplier() {
        return mult;
    }

    public int getCurrentWondrousness() {
        return JWondrous.wondrousness(curr_val, limit, mult);
    }

    public int getCurrentPitch() {
        return (int) MiscUtil.mapValueToRange(getCurrentWondrousness(), 0, limit, min_pitch, max_pitch);
    }

    @Override
    public void nextTick() {
        curr_val++;
        if (curr_val > end_val || curr_val < start_val) curr_val = start_val;
        try {
            sleep(speed);
        } catch (InterruptedException augh) {
            return;
        }
    }

    @Override
    public String toString() {
        return "Chan " + chan + " settings: " + CR + "Volume: " + vol + " Speed: " + speed + CR + "Pitch Base: " + min_pitch + " Pitch Ceiling: " + max_pitch + CR + "Current Value: " + curr_val + " Limit: " + limit + " Mult: " + mult + CR + "***";
    }
}
