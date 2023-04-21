package horcher;

public class Rechner {

    public static class Datensatz {

        double xre[], xim[];

        public int length;

        public Datensatz(final double xre[], final double xim[]) {
            if (xre.length != xim.length) {
                final IllegalArgumentException e = new IllegalArgumentException();
                throw e;
            }
            this.length = xre.length;
            this.xre = xre;
            this.xim = xim;
        }

        public Datensatz(final int n) {
            this.length = n;
            this.xre = new double[n];
            this.xim = new double[n];
        }

        public void glatten() {
            final int starke = 1;
            int l = 10, i, k, f = 1;
            f = (l + 1) * l / 2;
            for (int j = 0; j < starke; j++) {
                for (i = 0; (i < l) & (i < this.xre.length); i++) {
                    this.xre[i] *= l + 1;
                    for (k = 1; k <= l; k++) if (k > 0) this.xre[i] += this.xre[i + k] * (l - k + 1);
                    this.xre[i] = this.xre[i] / (f + l + 1);
                }
                for (i = l; i < this.xre.length - l; i++) {
                    this.xre[i] *= l + 1;
                    for (k = -l; k <= l; k++) {
                        if (k < 0) this.xre[i] += this.xre[i + k] * (l + k + 1);
                        if (k > 0) this.xre[i] += this.xre[i + k] * (l - k + 1);
                    }
                    this.xre[i] = this.xre[i] / (2 * f + l + 1);
                }
                for (i = this.xre.length - l; (i >= 0) & (i < this.xre.length); i++) {
                    this.xre[i] *= l + 1;
                    for (k = -l; k <= -1; k++) if (k < 0) this.xre[i] += this.xre[i + k] * (l + k + 1);
                    this.xre[i] = this.xre[i] / (f + l + 1);
                }
            }
        }

        public Datensatz korelation(final Datensatz d) {
            final int sl = (this.length < d.length) ? this.length : d.length;
            final Datensatz e = (this.length < d.length) ? new Datensatz(d.length) : new Datensatz(this.length);
            for (int i = 0; i < sl; i++) {
                e.xim[i] = this.xre[sl - 1 - i] * d.xim[i] + this.xim[sl - 1 - i] * d.xre[i];
                e.xim[i] /= Math.sqrt(Math.abs(e.xim[i]));
                e.xre[i] = this.xre[sl - 1 - i] * d.xre[i] - this.xim[sl - 1 - i] * d.xim[i];
                e.xre[i] /= Math.sqrt(Math.abs(e.xre[i]));
            }
            return e;
        }

        public double[] logbetrag() {
            final double[] e = new double[this.xre.length / 2];
            for (int i = 0; i < this.xre.length / 2; i++) e[i] = Math.sqrt(Math.abs(this.xre[i] * this.xre[i] - this.xim[i] * this.xim[i]));
            return e;
        }

        public Datensatz mul(final Datensatz d) {
            final int sl = (this.length < d.length) ? this.length : d.length;
            final Datensatz e = new Datensatz(sl);
            for (int i = 0; i < sl; i++) {
                e.xim[i] = this.xre[i] * d.xim[i] + this.xim[i] * d.xre[i];
                e.xre[i] = this.xre[i] * d.xre[i] - this.xim[i] * d.xim[i];
            }
            return e;
        }

        public Datensatz nurErste() {
            return new Datensatz(new double[] { this.xre[0] }, new double[] { this.xim[0] });
        }

        public Datensatz nurGeraden() {
            return this.nurTeil(0);
        }

        private Datensatz nurTeil(final int a) {
            final Datensatz e = new Datensatz(this.length / 2);
            for (int i = 0; i < this.length / 2; i++) {
                e.xre[i] = this.xre[i * 2 + a];
                e.xim[i] = this.xim[i * 2 + a];
            }
            return e;
        }

        public Datensatz nurUngeraden() {
            return this.nurTeil(1);
        }

        public Datensatz sum(final Datensatz d) {
            final int sl = (this.length < d.length) ? this.length : d.length;
            final Datensatz e = new Datensatz(sl);
            for (int i = 0; i < sl; i++) {
                e.xim[i] = this.xim[i] + d.xim[i];
                e.xre[i] = this.xre[i] + d.xre[i];
            }
            return e;
        }
    }

    static final double MINUSTWOPIE = -2 * Math.PI;

    int dimension;

    double[] sinus;

    double[] cosinus;

    Datensatz doFFT(final int n, final Datensatz d) {
        if (n == 1) return (d.nurErste()); else {
            final int nhalbe = n / 2;
            Datensatz gerade = d.nurGeraden();
            gerade = this.doFFT(nhalbe, gerade);
            Datensatz ungerade = d.nurUngeraden();
            ungerade = this.doFFT(nhalbe, ungerade);
            final Datensatz e = new Datensatz(n);
            double cos, sin;
            final double MTWOPIDBN = Rechner.MINUSTWOPIE / n;
            for (int k = 0; k < nhalbe; k++) {
                cos = Math.cos(MTWOPIDBN * k);
                sin = Math.sin(MTWOPIDBN * k);
                e.xre[k] = gerade.xre[k] + ungerade.xre[k] * cos;
                e.xim[k] = gerade.xim[k] + ungerade.xim[k] * sin;
                e.xre[k + nhalbe] = gerade.xre[k] - ungerade.xre[k] * cos;
                e.xim[k + nhalbe] = gerade.xim[k] - ungerade.xim[k] * sin;
            }
            return e;
        }
    }

    Datensatz doFFTi(final int n, final Datensatz d) {
        if (n == 1) return (d.nurErste()); else {
            final int nhalbe = n / 2;
            Datensatz gerade = d.nurGeraden();
            gerade = this.doFFT(nhalbe, gerade);
            Datensatz ungerade = d.nurUngeraden();
            ungerade = this.doFFT(nhalbe, ungerade);
            final Datensatz e = new Datensatz(n);
            double cos, sin;
            final double MTWOPIDBN = Rechner.MINUSTWOPIE / n;
            for (int k = 0; k < nhalbe; k++) {
                cos = Math.cos(MTWOPIDBN * k);
                sin = -Math.sin(MTWOPIDBN * k);
                e.xre[k] = gerade.xre[k] + ungerade.xre[k] * cos;
                e.xim[k] = gerade.xim[k] + ungerade.xim[k] * sin;
                e.xre[k + nhalbe] = gerade.xre[k] - ungerade.xre[k] * cos;
                e.xim[k + nhalbe] = gerade.xim[k] - ungerade.xim[k] * sin;
            }
            return e;
        }
    }

    public void fft(final Datensatz d, boolean invert) {
        int mit2, iter, irem, it, it2, nxp, nxp2, m, mxp, j1, j2, k, n, i, j;
        double wre, wim, tre, tim;
        n = d.xre.length;
        for (iter = 0, irem = n / 2; irem != 0; irem /= 2, iter++) ;
        if (this.dimension != n) this.ini_fft(n, iter);
        for (it = 0, nxp2 = n, it2 = 1; it < iter; it++, it2 *= 2) {
            nxp = nxp2;
            nxp2 = nxp / 2;
            for (m = 0, mit2 = 0; m < nxp2; m++, mit2 += it2) {
                wre = this.cosinus[mit2];
                wim = invert ? this.sinus[mit2] : -this.sinus[mit2];
                for (mxp = nxp, j1 = m; mxp <= n; mxp += nxp, j1 += nxp) {
                    j2 = j1 + nxp2;
                    tre = d.xre[j1] - d.xre[j2];
                    tim = d.xim[j1] - d.xim[j2];
                    d.xre[j1] += d.xre[j2];
                    d.xre[j2] = tre * wre - tim * wim;
                    d.xim[j1] += d.xim[j2];
                    d.xim[j2] = tre * wim + tim * wre;
                }
            }
        }
        for (i = 0, j = 0; i < n - 1; i++) {
            if (i < j) {
                tre = d.xre[j];
                d.xre[j] = d.xre[i];
                d.xre[i] = tre;
                tim = d.xim[j];
                d.xim[j] = d.xim[i];
                d.xim[i] = tim;
            }
            k = n / 2;
            while (k <= j) {
                j -= k;
                k = (k + 1) / 2;
            }
            j += k;
        }
        if (!invert) for (i = 0; i < n; i++) {
            d.xre[i] /= n;
            d.xim[i] /= n;
        }
    }

    void ini_fft(final int dim, final int deg) {
        double pidim2;
        int i, dim2;
        this.dimension = dim;
        this.sinus = new double[dim / 2];
        this.cosinus = new double[dim / 2];
        pidim2 = 2 * Math.PI / dim;
        for (i = 0, dim2 = dim / 2; i < dim2; i++) {
            this.cosinus[i] = Math.cos(pidim2 * i);
            this.sinus[i] = Math.sin(pidim2 * i);
        }
    }

    Datensatz startFFT(final Datensatz d) {
        int n;
        for (n = 1; n <= d.length; n *= 2) ;
        n /= 2;
        return this.doFFT(n, d);
    }

    Datensatz startFFTi(Datensatz d) {
        int n;
        for (n = 1; n <= d.length; n *= 2) ;
        n /= 2;
        d = this.doFFTi(n, d);
        for (int i = 0; i < n; i++) {
            d.xre[i] /= n;
            d.xim[i] /= n;
        }
        return d;
    }
}
