package org.expasy.jpl.commons.ms.peak;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.expasy.jpl.commons.ms.exceptions.JPLInvalidChargeException;

/**
 * 
 * in JPL a chargestate (or chargeMask) is stored as a sum of power 2 charge=1+ =>
 * chargestate=2 charge=3+ => 8 charge=2+ or 3+ => 4+8=12
 * 
 * we define here a set of static methods to convert back and forth
 * 
 * @author alex
 * 
 */
public final class JPLCharge {

    private static Pattern patCharge = Pattern.compile("[\\d]+");

    /**
	 * convert a chargestate into a charge
	 * 
	 * @param state
	 * @return log_2(charge)
	 * @throws JPLInvalidChargeException
	 *             if the return value is not a integer (if the mask was coding
	 *             for more than one charge
	 */
    public static int state2charge(final int state) {
        int c = -1;
        for (int i = 0; i < 32; i++) {
            if (((1 << i) & state) != 0) {
                if (c > 0) {
                    throw new JPLInvalidChargeException("multipe charges are coded in " + state);
                }
                c = i;
            }
        }
        if (c < 0) {
            throw new JPLInvalidChargeException("no charge is coded in " + state);
        }
        return c;
    }

    public static int[] state2chargelist(final int state) {
        int n = 0;
        for (int i = 0; i < 32; i++) {
            if (((1 << i) & state) != 0) n++;
        }
        int ret[] = new int[n];
        n = 0;
        for (int i = 0; i < 32; i++) {
            if (((1 << i) & state) != 0) ret[n++] = i;
        }
        return ret;
    }

    /**
	 * convert a charge into a chargestate
	 * 
	 * @param charge
	 * @return 2^charge
	 * @throws JPLInvalidChargeException
	 *             if the charge>=32
	 */
    public static int charge2state(final int charge) {
        if (charge >= 32) throw new JPLInvalidChargeException("cannot take into account mass >=32:" + charge + "+");
        return 1 << charge;
    }

    public static int string2chargestate(final String s) {
        int i = 0;
        Matcher matcher = patCharge.matcher(s);
        while (matcher.find()) {
            i = i | (1 << Integer.parseInt(matcher.group(0)));
        }
        return i;
    }

    /**
	 * 
	 * @param state
	 * @return
	 */
    public static String chargestate2String(final int state) {
        StringBuffer sb = new StringBuffer();
        int c[] = state2chargelist(state);
        for (int i = 0; i < c.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(c[i]);
        }
        return sb.toString();
    }
}
