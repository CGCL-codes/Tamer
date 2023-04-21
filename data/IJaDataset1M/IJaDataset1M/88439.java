package org.xhtmlrenderer.render;

public class StrutMetrics {

    private int _baseline;

    private float _ascent;

    private float _descent;

    public StrutMetrics(float ascent, int baseline, float descent) {
        _ascent = ascent;
        _baseline = baseline;
        _descent = descent;
    }

    public StrutMetrics() {
    }

    public float getAscent() {
        return _ascent;
    }

    public void setAscent(float ascent) {
        _ascent = ascent;
    }

    public int getBaseline() {
        return _baseline;
    }

    public void setBaseline(int baseline) {
        _baseline = baseline;
    }

    public float getDescent() {
        return _descent;
    }

    public void setDescent(float descent) {
        _descent = descent;
    }
}
