package uk.org.toot.audio.filter;

public class FIRFilter extends AbstractFilter {

    private float[] a;

    public FIRFilter(FilterSpecification spec, boolean relative) {
        super(spec, relative);
    }

    public float filterSample(float sample, FIRState s) {
        float y = 0.0f;
        int taps = a.length;
        for (int k = taps - 1; k > 0; k--) {
            s.x[k] = s.x[k - 1];
        }
        s.x[0] = sample;
        for (int k = 0; k < taps; k++) {
            y += a[k] * s.x[k];
        }
        return y;
    }

    public void filter(float[] signal, float[] mixBuffer, int len, int chan, boolean doMix) {
        FIRState s = (FIRState) getState(chan);
        float y;
        for (int i = 0; i < len; i++) {
            y = filterSample(signal[i], s);
            if (doMix) {
                mixBuffer[i] += (amplitudeAdj * y);
            } else {
                mixBuffer[i] = y;
            }
        }
    }

    protected void updateFilterCoefficients() {
        float[] coeffs = ((FIRDesign) getDesign()).getCoefficients();
        if (a.length != coeffs.length) {
            a = new float[coeffs.length];
        }
        System.arraycopy(coeffs, 0, a, 0, a.length);
    }

    protected FilterDesign createDesign(FilterSpecification spec) {
        return new FIRDesign(spec);
    }

    protected State createState() {
        return new FIRState();
    }

    private class FIRState implements Filter.State {

        public float[] x;

        public FIRState() {
            x = new float[a.length];
        }

        public void clear() {
            for (int i = 0; i < x.length; i++) {
                x[i] = 0f;
            }
        }
    }
}
