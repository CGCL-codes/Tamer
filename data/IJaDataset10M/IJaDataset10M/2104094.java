package com.trohko.jfsim.aircraft.cockpit.c2d.elements.indikators;

import com.trohko.jfsim.aircraft.cockpit.c2d.elements.indicators.IRollIndicator2D;
import com.trohko.jfsim.fobjects.IAircraft;
import com.trohko.jfsim.core.InvalidVersionException;

public class RollIndicator2D implements IRollIndicator2D {

    public float getMin() {
        return 0;
    }

    public float getMax() {
        return 0;
    }

    public float getValue() {
        return 0;
    }

    public void setMin(float minValue) {
    }

    public void setMax(float maxValue) {
    }

    public void setValue(float currentValue) {
    }

    public void setSource(IAircraft source) {
    }

    public String getName() {
        return null;
    }

    public int getMajorVersion() {
        return 0;
    }

    public int getMinorVersion() {
        return 0;
    }

    public boolean isCompatibleWith(int majorVersion, int minorVersion) throws InvalidVersionException {
        return false;
    }

    public void setCompatibility(int minMajorVersion, int minMinorVersion, int maxMajorVersion, int maxMinorVersion) {
    }
}
