package net.sf.reactionlab.combust;

import java.util.*;
import net.sf.reactionlab.*;

public abstract class CommonReactor extends CombustionReactor {

    protected Pressure definedPressure = new Pressure(false);

    public static final int CONSTANT_PRESSURE = 1;

    public static final int CONSTANT_VOLUME = 2;

    public static final int ADIABATIC = 1;

    public static final int ISOTHERMAL = 2;

    private int constraintMode = CONSTANT_PRESSURE;

    private int tempMode = ADIABATIC;

    public void addErrors(Network net, List errors) {
        if (constraintMode != CONSTANT_PRESSURE && super.getPressure(net).isAuto()) errors.add("Input pressure cannot be automatic.");
    }

    public int getConstraintMode() {
        return constraintMode;
    }

    public void setConstraintMode(int mode) {
        switch(mode) {
            case CONSTANT_VOLUME:
            case CONSTANT_PRESSURE:
                constraintMode = mode;
                break;
            default:
                throw new IllegalArgumentException("Bad mode: " + mode);
        }
    }

    public int getTemperatureMode() {
        return tempMode;
    }

    public void setTemperatureMode(int tempMode) {
        switch(tempMode) {
            case ADIABATIC:
            case ISOTHERMAL:
                this.tempMode = tempMode;
                break;
            default:
                throw new IllegalArgumentException("Bad temperature mode: " + tempMode);
        }
    }

    protected Pressure getPressure(Network net) {
        return constraintMode == CONSTANT_PRESSURE ? super.getPressure(net) : definedPressure;
    }

    public boolean hasTemperatureMode() {
        return true;
    }
}
