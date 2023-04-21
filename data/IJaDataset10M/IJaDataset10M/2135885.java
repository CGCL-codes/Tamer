package bouttime.sort;

import bouttime.stat.TwoColumnStat;
import java.util.Comparator;

/**
 * A class to sort TwoColumnStat objects :
 *     (1) by columnOne
 *     (2) by columnTwo
 */
public class TwoColumnStatSort implements Comparator<TwoColumnStat> {

    public int compare(TwoColumnStat s1, TwoColumnStat s2) {
        int rv;
        String c11 = s1.getColumnOne();
        String c21 = s2.getColumnOne();
        rv = c11.compareTo(c21);
        if (rv != 0) {
            return rv;
        } else {
            String c12 = s1.getColumnTwo();
            String c22 = s2.getColumnTwo();
            rv = c12.compareTo(c22);
            return rv;
        }
    }
}
