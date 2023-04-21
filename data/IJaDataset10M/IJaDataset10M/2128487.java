package org.jjazz.leadsheet.rhythmleadsheet;

import jjazz.rhythm.RhythmParameter;

/**
 * A RhythmParemeter representing integer values.
 */
public class RP_Integer extends RL_RhythmParameter {

    /** Step used to increase/decrease the value. */
    private int step;

    public RP_Integer() {
        throw new IllegalStateException();
    }

    /**
    * @param defaultVal Object The default value.
    * @param values Object[] The possible values for this parameter (must be > 0)
    */
    public RP_Integer(String n, int defaultVal, int min, int max, int st) {
        if (n == null || n.length() == 0 || min > max) {
            throw new IllegalArgumentException("n=" + n + " defaultVal=" + defaultVal + " min=" + min + " max=" + max + " st=" + st);
        }
        name = n;
        minValue = (Integer) min;
        maxValue = (Integer) max;
        step = st;
        setDefaultValue((Integer) defaultVal);
        setValue((Integer) getDefaultValue());
    }

    /**
    * Overridden to check v is one of the possible values.
    * @param v Object
    */
    @Override
    public void setDefaultValue(Object v) {
        if (!(v instanceof Integer)) {
            throw new IllegalArgumentException(v + " is not an Integer");
        }
        int val = (Integer) v;
        if (val < (Integer) minValue || val > (Integer) maxValue) {
            throw new IllegalArgumentException("v=" + v + " minValue" + minValue + " maxValue=" + maxValue);
        }
        super.setDefaultValue(v);
    }

    public int getStep() {
        return step;
    }

    public int getIntValue() {
        return (Integer) value;
    }

    public int getIntMinValue() {
        return (Integer) minValue;
    }

    public int getIntMaxValue() {
        return (Integer) maxValue;
    }

    /**
    * Overridden to check v is one of the possible values.
    * @param v Object
    */
    @Override
    public void setValue(Object v) {
        if (!(v instanceof Integer)) {
            throw new IllegalArgumentException(v + " is not an Integer");
        }
        int val = (Integer) v;
        if (val < (Integer) minValue || val > (Integer) maxValue) {
            throw new IllegalArgumentException("val=" + val + " minValue" + minValue + " maxValue=" + maxValue);
        }
        super.setValue(v);
    }

    @Override
    public int getNbPossibleValues() {
        return ((Integer) maxValue - (Integer) minValue + 1) / step;
    }

    /**
    * Return which percentage of the complete range the current value corresponds to.
    * @return double A value between 0 and 1.
    */
    public double getValueRatio() {
        return ((double) getIntValue() - getIntMinValue()) / (getIntMaxValue() - getIntMinValue());
    }

    @Override
    protected Object getNextValueNoLoop() {
        int v = (Integer) getValue() + step;
        int max = (Integer) maxValue;
        return v > max ? maxValue : (Integer) v;
    }

    @Override
    protected Object getPreviousValueNoLoop() {
        int v = (Integer) getValue() - step;
        int min = (Integer) minValue;
        return v < min ? minValue : (Integer) v;
    }

    /** Compatible if other is also an RP_Integer and has same name. */
    @Override
    public boolean isCompatible(RhythmParameter rp) {
        return (rp instanceof RP_Integer) && isSameName(rp);
    }

    @Override
    protected void cloneObjectFields(RhythmParameter rp) {
        rp.setDefaultValue(new Integer((Integer) getDefaultValue()));
        rp.setValue(new Integer((Integer) getValue()));
        rp.setMaxValue(new Integer((Integer) getMaxValue()));
        rp.setMinValue(new Integer((Integer) getMinValue()));
    }

    @Override
    public void transcodeValue(RhythmParameter rp) {
        if (!isCompatible(rp)) {
            throw new IllegalArgumentException("this=" + this + " rp=" + rp);
        }
        RP_Integer rpi = (RP_Integer) rp;
        int min = (Integer) getMinValue();
        int max = (Integer) getMaxValue();
        int rpMin = (Integer) rpi.getMinValue();
        int rpMax = (Integer) rpi.getMaxValue();
        int rpVal = (Integer) rpi.getValue();
        int newValue;
        if (rpVal == rpMin) {
            newValue = min;
        } else if (rpVal == max) {
            newValue = max;
        } else {
            float ratio = (float) (rpVal - rpMin) / (rpMax - rpMin);
            newValue = (int) Math.round(ratio * (max - min)) + min;
        }
        setValue(newValue);
    }
}
