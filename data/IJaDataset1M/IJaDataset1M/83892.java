package dcartes.plot;

import java.text.DecimalFormat;

/**
  * This class provides a static method getFormatted that
  * returns a double value as a string in decimal or scientific
  * notation.<br><br>
 **/
public class PlotFormat {

    static final DecimalFormat DF1 = new DecimalFormat("0.0");

    static final DecimalFormat DF2 = new DecimalFormat("0.00");

    static final DecimalFormat DF3 = new DecimalFormat("0.000");

    static final DecimalFormat DF4 = new DecimalFormat("0.0000");

    static final DecimalFormat SF1 = new DecimalFormat("0.0E0");

    static final DecimalFormat SF2 = new DecimalFormat("0.00E0");

    static final DecimalFormat SF3 = new DecimalFormat("0.000E0");

    static final DecimalFormat SF4 = new DecimalFormat("0.0000E0");

    /**
  * The options include 1 to 3 decimal places. Values below
  * decimalLimit use decimal notation; above this use scientific
  * notation.
  * @param val value
  * @param decimal_hi_limit limit before changing to scientific notation
  * @param decimal_lo_limit limit before changing to scientific notation
  * @param decimal_places of decimal places in the output.
  */
    public static String getFormatted(double val, double decimal_hi_limit, double decimal_lo_limit, int decimal_places) {
        if (val == 0.0 || (Math.abs(val) <= decimal_hi_limit && Math.abs(val) > decimal_lo_limit)) {
            switch(decimal_places) {
                case 1:
                    return DF1.format(val);
                case 2:
                    return DF2.format(val);
                case 3:
                    return DF3.format(val);
                case 4:
                    return DF4.format(val);
                default:
                    return DF1.format(val);
            }
        } else {
            switch(decimal_places) {
                case 1:
                    return SF1.format(val);
                case 2:
                    return SF2.format(val);
                case 3:
                    return SF3.format(val);
                case 4:
                    return SF4.format(val);
                default:
                    return SF1.format(val);
            }
        }
    }
}
