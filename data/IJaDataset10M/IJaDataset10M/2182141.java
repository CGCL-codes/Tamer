package lufact;

import jgfutil.*;

public class JGFLUFactBench extends Linpack implements JGFSection2 {

    private int size;

    private int datasizes[] = { 500, 1000, 2000 };

    public void JGFsetsize(int size) {
        this.size = size;
    }

    public void JGFinitialise() {
        n = datasizes[size];
        ldaa = n;
        lda = ldaa + 1;
        a = new double[ldaa][lda];
        b = new double[ldaa];
        x = new double[ldaa];
        ipvt = new int[ldaa];
        long nl = (long) n;
        ops = (2.0 * (nl * nl * nl)) / 3.0 + 2.0 * (nl * nl);
        norma = matgen(a, lda, n, b);
    }

    public void JGFkernel() {
        JGFInstrumentor.startTimer("Section2:LUFact:Kernel");
        info = dgefa(a, lda, n, ipvt);
        dgesl(a, lda, n, ipvt, b, 0);
        JGFInstrumentor.stopTimer("Section2:LUFact:Kernel");
    }

    public void JGFvalidate() {
        int i;
        double eps, residn;
        final double ref[] = { 6.0, 12.0, 20.0 };
        for (i = 0; i < n; i++) {
            x[i] = b[i];
        }
        norma = matgen(a, lda, n, b);
        for (i = 0; i < n; i++) {
            b[i] = -b[i];
        }
        dmxpy(n, b, n, lda, x, a);
        resid = 0.0;
        normx = 0.0;
        for (i = 0; i < n; i++) {
            resid = (resid > abs(b[i])) ? resid : abs(b[i]);
            normx = (normx > abs(x[i])) ? normx : abs(x[i]);
        }
        eps = epslon((double) 1.0);
        residn = resid / (n * norma * normx * eps);
        if (residn > ref[size]) {
            System.out.println("Validation failed");
            System.out.println("Computed Norm Res = " + residn);
            System.out.println("Reference Norm Res = " + ref[size]);
        }
    }

    public void JGFtidyup() {
        a = null;
        b = null;
        x = null;
        ipvt = null;
        System.gc();
    }

    public void JGFrun(int size) {
        JGFInstrumentor.addTimer("Section2:LUFact:Kernel", "Mflops", size);
        JGFsetsize(size);
        JGFinitialise();
        JGFkernel();
        JGFvalidate();
        JGFtidyup();
        JGFInstrumentor.addOpsToTimer("Section2:LUFact:Kernel", ops / 1.0e06);
        JGFInstrumentor.printTimer("Section2:LUFact:Kernel");
    }
}
