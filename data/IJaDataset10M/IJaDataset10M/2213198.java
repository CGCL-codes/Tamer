package org.javaseis.tests;

import org.javaseis.fft.IFFT;
import org.javaseis.fft.SeisFft;
import junit.framework.TestCase;
import edu.mines.jtk.dsp.FftReal;

/**
 * Tests the SeisFft class.
 */
public class SeisFftTest extends TestCase {

    private static int NMAX = 1000;

    public static void main(String[] args) {
        SeisFftTest test = new SeisFftTest();
        test.testReal();
        test.testComplex();
        System.out.println("*** org.javaseis.tests.SeisFftTest SUCCESS");
    }

    /**
   * Pauses program execution until user hits ENTER (so be sure to remove when
   *   finished debugging).
   */
    private static void debugPause() {
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }

    /** See testReal(). */
    public void doRealRoundTrip(int nsamps) {
        IFFT fft = new SeisFft(nsamps, 0.f, IFFT.Type.REAL);
        int arrayLen = fft.getArrayLength();
        float[] data = new float[arrayLen];
        for (int i = 0; i < nsamps; i++) {
            data[i] = 1f;
        }
        if (false) {
            System.out.println();
            System.out.println("Time-domain (input) data:");
            for (int j = 0; j < arrayLen; j++) {
                System.out.printf("%4d%10.3f\n", j, data[j]);
            }
        }
        fft.realToComplex(data);
        if (false) {
            System.out.println("Frequency-domain:");
            for (int j = 0; j < arrayLen; j++) {
                System.out.printf("%4d%10.3f\n", j, data[j]);
            }
        }
        fft.complexToReal(data);
        if (false) {
            System.out.println("Time-domain (transformed) data:");
            for (int j = 0; j < arrayLen; j++) {
                System.out.printf("%4d%10.3f\n", j, data[j]);
            }
        }
    }

    /** See testComplex(). */
    public void doComplexRoundTrip(int nsamps) {
        double tolerance = 1.e-5;
        SeisFft fft = new SeisFft(nsamps, 0.f, IFFT.Type.COMPLEX);
        int arrayLen = fft.getArrayLength();
        float[] data = new float[arrayLen];
        for (int i = 0; i < nsamps; i++) {
            data[i] = 1f;
        }
        float[] copy = new float[arrayLen];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = data[i];
        }
        if (false) {
            System.out.println();
            System.out.println("Input data:");
            for (int j = 0; j < arrayLen; j++) {
                System.out.printf("%4d%10.3f\n", j, data[j]);
            }
        }
        fft.complexForward(data);
        if (false) {
            System.out.println("After forward transform:");
            for (int j = 0; j < arrayLen; j++) {
                System.out.printf("%4d%10.3f\n", j, data[j]);
            }
        }
        fft.complexInverse(data);
        if (false) {
            System.out.println("After inverse transform:");
            for (int j = 0; j < arrayLen; j++) {
                System.out.printf("%4d%10.3f\n", j, data[j]);
            }
        }
        for (int i = 0; i < nsamps; i++) {
            if (Math.abs(copy[i] - data[i]) > tolerance) System.out.println("complex round-trip is out of spec for nsamps = " + nsamps);
            assertEquals(copy[i], data[i], tolerance);
        }
    }

    /**
   * Tests SeisFft for IFFT.Type.REAL.
   * NOTE: Patterned after code moved from SeisFft main() 04/07.
   */
    public void testReal() {
        for (int i = 2; i < NMAX; i++) {
            doRealRoundTrip(i);
        }
    }

    /**
   * Tests SeisFft for IFFT.Type.COMPLEX by doing a round-trip
   *   complexForward -> complexInverse, then validating the entire array.
   *   This test exists because nsamps currently has to be a valid prime factor
   *   in Pfacc.java in order to get valid results (e.g. nsamps == 52 is passing
   *   this test, and nsamps == 55 is passing also, but as of this writing,
   *   nsamps == 50 is failing this round-trip tolerance test).
   *
   *   NOTE:  All nsamps values pass now (1 to 1000 anyway...).  Problem was
   *   a couple of places in SeisFft where _lendata was used when _lentrans
   *   should have been used.
   */
    public void testComplex() {
        for (int i = 2; i < NMAX; i++) {
            doComplexRoundTrip(i);
        }
    }

    /**
	 * Test method for 'org.javaseis.fft.SeisFft.setFftParms(int, Type, Scale, float, int)'
	 */
    public void testSetFftParms() {
        IFFT f = new SeisFft();
        f.setFftParms(1024, IFFT.Type.REAL, IFFT.Scale.SYMMETRIC, 50.0f, -1);
        assertEquals(FftReal.nfftSmall(1536), f.getLength());
    }

    /**
	 * Test method for 'org.javaseis.fft.SeisFft.RealToComplex(float[], int)'
	 */
    public void testRealToComplexFloatArrayInt() {
        int nt = 1040;
        IFFT f = new SeisFft(nt, 0.0f, IFFT.Type.REAL, -1, IFFT.Scale.INVERSE);
        int nft = f.getLength();
        assertEquals(1040, nft);
        int xoff = 100;
        float[] x = new float[nft + 2 + xoff];
        for (int i = 0; i < nft; i++) x[i + xoff] = 1.0f;
        f.realToComplex(x, xoff);
        assertEquals("SeisFft.realToComplex failure", x[xoff], (float) nft, 0.1f);
        f.complexToReal(x, xoff);
        assertEquals("SeisFft.complexToReal failure", x[xoff], 1.0, 0.1f);
    }

    /**
	 * Test method for 'org.javaseis.fft.SeisFft.ComplexForward(float[], int)'
	 */
    public void testComplexForwardFloatArrayInt() {
        int nt = 1040;
        IFFT f = new SeisFft(nt, 0.0f, IFFT.Type.COMPLEX, 1, IFFT.Scale.SYMMETRIC);
        int nft = f.getLength();
        assertEquals(1040, nft);
        int xoff = 100;
        float[] cx = new float[2 * nft + xoff];
        int ix = xoff;
        int iy = ix + 1;
        for (int i = 0; i < nft; i++, ix += 2, iy += 2) {
            cx[ix] = (float) i;
            cx[iy] = (float) i;
        }
        f.complexForward(cx, xoff);
        f.complexInverse(cx, xoff);
        ix = xoff;
        iy = ix + 1;
        for (int i = 0; i < nft; i++, ix += 2, iy += 2) {
            assertEquals("SeisFft.complexToReal failure", cx[ix], (float) i, 0.001f);
            assertEquals("SeisFft.complexToReal failure", cx[iy], (float) i, 0.001f);
        }
    }

    /**
   * Test DC behavior for even- and odd-numbered prime and non-prime complex cases.
   * <p>
   * IFFT.Type.REAL cases always return an even number for getLength().
   * IFFT.Type.COMPLEX cases return the appropriate prime factor (which depends
   * on whether nfft 'small' or 'fast' is used underneath).
   * <p>
   * The purpose of this test is to demonstrate where DC energy ends up.  It
   * turns out that for all cases (even, odd, prime or not), the DC energy ends
   * up, as expected, all in the first sample.  The only difference is that the
   * that the non-prime cases have noise (on the order of a 1% level), whereas
   * the prime cases have hard zeroes outside of the DC spike (or nearly so).
   * Hopefully this is the way it is supposed to be -- dunno offhand.
   * <p>
   * This test turned out to be useful in an unexpected manner -- it turned up a
   * bug in SeisFft.java wherein the out-of-place complexForward() method was
   * loading the result into internal buffer '_cy' instead of return buffer 'cy'.
   */
    public void testEvenAndOddDC() {
        int nsamps;
        SeisFft fft;
        float[] dataIn, dataOut;
        nsamps = 195;
        fft = new SeisFft(nsamps, 0.f, IFFT.Type.COMPLEX);
        assertEquals(nsamps, fft.getLength());
        dataIn = new float[2 * nsamps];
        dataOut = new float[fft.getArrayLength()];
        for (int i = 0; i < nsamps; i++) {
            dataIn[i * 2] = 1.f;
        }
        fft.complexForward(dataIn, dataOut);
        assertEquals(nsamps, dataOut[0], 1.e-5);
        if (false) {
            System.out.println("Odd prime result:");
            for (int i = 0; i < fft.getArrayLength(); i++) {
                System.out.printf("%3d%10.5f\n", i, dataOut[i]);
            }
        }
        nsamps = 193;
        fft = new SeisFft(nsamps, 0.f, IFFT.Type.COMPLEX);
        assertEquals(195, fft.getLength());
        dataIn = new float[2 * nsamps];
        dataOut = new float[fft.getArrayLength()];
        for (int i = 0; i < nsamps; i++) {
            dataIn[i * 2] = 1.f;
        }
        fft.complexForward(dataIn, dataOut);
        assertEquals(nsamps, dataOut[0], 1.e-5);
        if (false) {
            System.out.println("Odd non-prime result:");
            for (int i = 0; i < fft.getArrayLength(); i++) {
                System.out.printf("%3d%10.5f\n", i, dataOut[i]);
            }
        }
        nsamps = 198;
        fft = new SeisFft(nsamps, 0.f, IFFT.Type.COMPLEX);
        assertEquals(198, fft.getLength());
        dataIn = new float[2 * nsamps];
        dataOut = new float[fft.getArrayLength()];
        for (int i = 0; i < nsamps; i++) {
            dataIn[i * 2] = 1.f;
        }
        fft.complexForward(dataIn, dataOut);
        assertEquals(nsamps, dataOut[0], 1.e-5);
        if (false) {
            System.out.println("Even non-prime result:");
            for (int i = 0; i < fft.getArrayLength(); i++) {
                System.out.printf("%3d%10.5f\n", i, dataOut[i]);
            }
        }
        nsamps = 196;
        fft = new SeisFft(nsamps, 0.f, IFFT.Type.COMPLEX);
        assertEquals(198, fft.getLength());
        dataIn = new float[2 * nsamps];
        dataOut = new float[fft.getArrayLength()];
        for (int i = 0; i < nsamps; i++) {
            dataIn[i * 2] = 1.f;
        }
        fft.complexForward(dataIn, dataOut);
        assertEquals(nsamps, dataOut[0], 1.e-5);
        if (false) {
            System.out.println("Even non-prime result:");
            for (int i = 0; i < fft.getArrayLength(); i++) {
                System.out.printf("%3d%10.5f\n", i, dataOut[i]);
            }
        }
    }
}
