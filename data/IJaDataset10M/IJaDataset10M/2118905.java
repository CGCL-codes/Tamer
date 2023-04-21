package org.omwg.mediation.align.calculator;

import com.wcohen.secondstring.Levenstein;

/**
 * @author richi
 * 
 */
public class LevensteinCalculator implements Calculator {

    public double[][] calculateDistances(final String[] clazzes1, final String[] clazzes2) {
        final Levenstein ls = new Levenstein();
        final double[][] distances = new double[clazzes1.length][clazzes2.length];
        int iCounter1 = -1;
        for (String s1 : clazzes1) {
            iCounter1++;
            int iCounter2 = 0;
            for (String s2 : clazzes2) {
                distances[iCounter1][iCounter2++] = 1 / (1 + Math.abs(ls.score(s1, s2)));
            }
        }
        return distances;
    }
}
