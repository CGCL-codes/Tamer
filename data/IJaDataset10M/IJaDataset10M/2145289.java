package plecotus.models.bwin.doc.treegross;

/**
 * NormalDistributed : class is used to calculated normal distributed errors,
 * etc. It is possible to limit sigma. The values are calculated use the
 * procedure described in Hartung, J. (1989) Statistik (7.Auflage), Oldenbourg,
 * page: 890
 * 
 * @version 1.0 16-Feb-2004
 * @author Juergen Nagel
 */
class NormalDistributed {

    public NormalDistributed() {
    }

    double value(double sigma) {
        double a1 = 0.31938513;
        double a2 = -0.356563782;
        double a3 = 1.781477937;
        double a4 = -1.821255978;
        double a5 = 1.330274429;
        double p = 0.0;
        double px = 0.0;
        double z = 0.0;
        double x = 0.0;
        double t = 0.0;
        double x2 = 0.0;
        do {
            p = Math.random();
            if (p < 0.5) px = 1 - p; else px = p;
            z = 0.0;
            x = sigma;
            x2 = sigma / 2.0;
            for (int i = 0; i < 10; i++) {
                t = 1.0 / (1.0 + 0.2316419 * x);
                z = 1.0 - (1.0 / (Math.sqrt(2.0 * Math.PI))) * Math.exp(-x * x / 2.0) * (a1 * t + a2 * t * t + a3 * t * t * t + a4 * t * t * t * t + a5 * t * t * t * t * t);
                if (z < px) x = x + x2; else x = x - x2;
                x2 = x2 / 2.0;
            }
        } while (x > sigma);
        if (p < 0.5) x = -x;
        return x;
    }
}
