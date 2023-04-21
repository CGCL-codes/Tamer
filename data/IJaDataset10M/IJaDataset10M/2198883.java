package fft;

import jgfutil.*;
import java.util.Random;

public class JGFFFTBench extends FFT implements JGFSection2 {

    private int size;

    private int datasizes[] = { 2097152, 8388608, 16777216 };

    private static final long RANDOM_SEED = 10101010;

    Random R = new Random(RANDOM_SEED);

    public void JGFsetsize(int size) {
        this.size = size;
    }

    public void JGFinitialise() {
    }

    public void JGFkernel() {
        double x[] = RandomVector(2 * (datasizes[size]), R);
        FFT.transform(x);
        FFT.inverse(x);
    }

    public void JGFvalidate() {
        double refval[] = { 1.726962988395339, 6.907851953579193, 13.815703907167297 };
        double refvali[] = { 2.0974756152524314, 8.389142211032294, 16.778094422092604 };
        double dev = Math.abs(JDKtotal - refval[size]);
        double devi = Math.abs(JDKtotali - refvali[size]);
        if (dev > 1.0e-12) {
            System.out.println("Validation failed");
            System.out.println("JDKtotal = " + JDKtotal + "  " + dev + "  " + size);
        }
        if (devi > 1.0e-12) {
            System.out.println("Validation failed");
            System.out.println("JDKtotalinverse = " + JDKtotali + "  " + dev + "  " + size);
        }
    }

    public void JGFtidyup() {
        System.gc();
    }

    public void JGFrun(int size) {
        JGFInstrumentor.addTimer("Section2:FFT:Kernel", "Samples", size);
        JGFsetsize(size);
        JGFinitialise();
        JGFkernel();
        JGFvalidate();
        JGFtidyup();
        JGFInstrumentor.addOpsToTimer("Section2:FFT:Kernel", (double) (datasizes[size]));
        JGFInstrumentor.printTimer("Section2:FFT:Kernel");
    }

    private static double[] RandomVector(int N, java.util.Random R) {
        double A[] = new double[N];
        for (int i = 0; i < N; i++) A[i] = R.nextDouble() * 1e-6;
        return A;
    }
}
