package ces.platform.infoplat.utils.unit;

/**
 * Volume unit converter class.
 *
 * @author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
 */
public class VolumeUnit extends BaseUnit {

    private static final String[] UNITS = { "cu meters", "liters", "cu centimeters", "cu yards", "cu feet", "cu inches", "barrels (Brit)", "barrels (US oil)", "bushels (Brit)", "bushels (US)", "gallons (Brit)", "gallons (US, dry)", "gallons (US, liq)", "ounces (Brit, flu)", "ounces (US, flu)", "pints (Brit)", "pints (US, dry)", "pints (US, liq)", "quarts (Brit)", "quarts (US, dry)", "quarts (US, liq)", "cups", "tablespoons", "teaspoons" };

    private static final double[] LITER_TABLE = { 0.001, 1.0, 1000.0, 0.0013079506193212351, 0.03531466672148859, 61.02374409473229, 0.006110266429645065, 0.006289810770352982, 0.027496198932814762, 0.028377857186588352, 0.21996959146614708, 0.22702285749528378, 0.2641720523581484, 35.195134634273856, 33.814022701271305, 1.7597567317136928, 1.8161828599622702, 2.113376418865187, 0.8798783658258791, 0.9080914299811351, 1.0566882094325935, 4.226752837730374, 67.62804540482938, 202.88413620899988 };

    /**
     * Get all the possible units.
     */
    public String[] getAllUnits() {
        return UNITS;
    }

    /**
     * Convert one unit from another
     */
    public String convertUnit(String value, int from, int to) throws UnitException {
        double inVal = getDoubleValue(value);
        double outVal = inVal * LITER_TABLE[to] / LITER_TABLE[from];
        return String.valueOf(outVal);
    }

    /**
     * Return unit type.
     */
    public String toString() {
        return "Volume";
    }
}
