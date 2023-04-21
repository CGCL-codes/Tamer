package org.stellarium.ui.fader;

class BooleanFader extends Fader {

    public BooleanFader(boolean _state) {
        super(_state);
    }

    public BooleanFader(boolean _state, float _min_value, float _max_value) {
        super(_state, _min_value, _max_value);
    }

    public void update(long delta_ticks) {
    }

    public float getInterstate() {
        return state ? maxValue : minValue;
    }

    public float getInterstatePercentage() {
        return state ? 100.f : 0.f;
    }

    public Fader set(boolean s) {
        state = s;
        return this;
    }

    public boolean hasInterstate() {
        return true;
    }

    public long getDuration() {
        return 0;
    }
}
