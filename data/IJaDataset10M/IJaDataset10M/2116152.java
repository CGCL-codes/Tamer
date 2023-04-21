package uk.org.toot.dsp;

public class DCBlocker {

    private float a = 0.999f;

    private float p = 0;

    public float block(float sample) {
        float m = sample + a * p;
        float y = m - p;
        p = m;
        return y;
    }

    public void clear() {
        p = 0;
    }
}
