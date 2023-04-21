package org.jcpsim.plot;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Finds the optimal scale for one axis
 *
 * @author  Dr. Frank Fischer &lt;frank@jcpsim.org&gt;
 */
public class ComputeTics {

    static final long serialVersionUID = 0L;

    public double ticmin;

    public double ticmax;

    public double ticd;

    public int tics;

    public int frac;

    private NumberFormat nf;

    /**
   * @param omin   lowest desired value
   * @param omax   highest desired value
   * @param catics approximate desired number of tics
   */
    public ComputeTics(double omin, double omax, double catics) {
        double delta[] = { 0.5, 1, 2, 2.5, 5, 10 };
        int pnt[] = { 1, 0, 0, 1, 0, 0 };
        if (catics < 1) catics = 1;
        double abst = (omax - omin) / catics;
        int logd = (int) Math.floor(Math.log(abst) / Math.log(10));
        double expo = Math.pow(10, logd);
        double mant = abst / expo;
        int i = -1;
        while (mant > delta[++i]) ;
        ticd = delta[i - 1] * expo;
        ticmin = -Math.floor(-omin / ticd) * ticd;
        ticmax = (int) (omax / ticd) * ticd;
        tics = (int) ((ticmax - ticmin) / ticd);
        frac = Math.max(0, pnt[i - 1] - logd);
        nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumIntegerDigits(8);
        nf.setMinimumIntegerDigits(1);
        nf.setMaximumFractionDigits(frac);
        nf.setMinimumFractionDigits(frac);
    }

    public String format(double x) {
        return nf.format(x);
    }

    public boolean isZero(double i) {
        return (Math.abs(i) < ticd / 10.0);
    }
}
