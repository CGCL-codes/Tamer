package net.url404.umodular.filters;

/**
 * IIR (Infinite Impulse Response) filter, translated from
 * original C-sources by baltrax@hotmail.com.
 *
 * @author makela@url404.net
 */
public class FilterIIR {

    public class FilterStruct {

        public int length = 0;

        public double history[] = null;

        public double coef[] = null;
    }

    public FilterStruct iir = null;

    private final int FILTER_SECTIONS = 2;

    private BiQuad protoCoef[] = new BiQuad[FILTER_SECTIONS];

    private double sampleRate = 0.0;

    /**
   * Constructor
   */
    public FilterIIR(double sampleRate) {
        this.sampleRate = sampleRate;
        protoCoef[0] = new BiQuad();
        protoCoef[0].a0 = 1.0;
        protoCoef[0].a1 = 0;
        protoCoef[0].a2 = 0;
        protoCoef[0].b0 = 1.0;
        protoCoef[0].b1 = 0.765367;
        protoCoef[0].b2 = 1.0;
        protoCoef[1] = new BiQuad();
        protoCoef[1].a0 = 1.0;
        protoCoef[1].a1 = 0;
        protoCoef[1].a2 = 0;
        protoCoef[1].b0 = 1.0;
        protoCoef[1].b1 = 1.847759;
        protoCoef[1].b2 = 1.0;
    }

    /**
   * Perform IIR filtering sample by sample on doubles.
   * <p>
   * Implements cascaded direct form II second order sections.
   * Requires FILTER structure for history and coefficients.
   * The length in the filter structure specifies the number of sections.
   * The size of the history array is 2*iir.length.
   * The size of the coefficient array is 4*iir.length + 1 because
   * the first coefficient is the overall scale factor for the filter.
   * Returns one output sample for each input sample.  Allocates history
   * array if not previously allocated.
   * 
   * @param input        new float input sample
   * @return             Returns double value giving the current output.
   */
    public double iirFilter(double input) {
        if (iir == null || iir.coef == null) {
            return input;
        }
        if (iir.history == null) {
            iir.history = new double[2 * iir.length];
        }
        int coefPtr = 0;
        int histPtr1 = 0;
        int histPtr2 = 1;
        double output = input * (iir.coef[coefPtr++]);
        for (int i = 0; i < iir.length; i++) {
            double history1 = iir.history[histPtr1];
            double history2 = iir.history[histPtr2];
            output = output - history1 * (iir.coef[coefPtr++]);
            double newHist = output - history2 * (iir.coef[coefPtr++]);
            output = newHist + history1 * (iir.coef[coefPtr++]);
            output = output + history2 * (iir.coef[coefPtr++]);
            iir.history[histPtr2++] = iir.history[histPtr1];
            iir.history[histPtr1++] = newHist;
            histPtr1++;
            histPtr2++;
        }
        return output;
    }

    /**
   * Setup filter, transform from s-domain to z-domain, must
   * be done when changing filter cutoff or resonance.
   *
   * @param fc      Filter cutoff
   * @param q       Filter resonance
   */
    public void setupFilter(double fc, double q) {
        int coefPtr = 0;
        double k = 1.0;
        double fs = sampleRate;
        if (iir == null) {
            iir = new FilterStruct();
            iir.length = FILTER_SECTIONS;
            iir.coef = new double[4 * iir.length + 1];
        }
        if (fc < 1.0) {
            fc = 1.0;
        }
        if (q < 1.0) {
            q = 1.0;
        }
        coefPtr = 1;
        for (int nInd = 0; nInd < iir.length; nInd++) {
            BiQuad b = new BiQuad();
            b.a0 = protoCoef[nInd].a0;
            b.a1 = protoCoef[nInd].a1;
            b.a2 = protoCoef[nInd].a2;
            b.b0 = protoCoef[nInd].b0;
            b.b1 = protoCoef[nInd].b1 / q;
            b.b2 = protoCoef[nInd].b2;
            k = BilinearTransform.szTransform(b, fc, fs, k, iir.coef, coefPtr);
            coefPtr += 4;
        }
        iir.coef[0] = k;
    }

    /**
   * Testing.
   */
    public static void main(String args[]) {
        FilterIIR filter = new FilterIIR(44100.0);
        filter.setupFilter(50.0, 1.0);
        for (int nInd = 0; nInd < (filter.iir.length * 4 + 1); nInd++) {
            System.out.println("C[" + nInd + "] = " + filter.iir.coef[nInd]);
        }
        double rad = 0.0;
        double speed = ((Math.PI * 2) / 44100.0) * 440;
        for (int i = 0; i < 100; i++) {
            double s = Math.sin(rad);
            System.out.println("s[" + i + "]=" + s + " || " + filter.iirFilter(s));
            rad += speed;
        }
        System.exit(0);
    }
}
