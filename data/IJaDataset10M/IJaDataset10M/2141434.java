package ATOFMS;

/**
 * @author ritza
 * 
 * Peak can be constructed with height, value, rel. value and m/z or just height,
 * value, and m/z.
 */
public class Peak {

    public double massToCharge;

    public double value;

    public Peak(double v, double mz) {
        massToCharge = mz;
        value = v;
    }

    /**
	 * prints peak.
	 */
    public String toString() {
        String returnThis = "Location: " + massToCharge + " Value: " + value;
        return returnThis;
    }
}
