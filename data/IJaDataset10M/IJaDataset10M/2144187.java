package jasperdesign;

public class Units {

    public static final String INCHES = "in";

    public static final String POINTS = "pt";

    public static final String CM = "cm";

    static final String UNITS[] = { INCHES, CM, POINTS };

    private static final java.util.Vector vUnits = new java.util.Vector(5, 5);

    static final double ratios[] = { 72.0, 1.0, 28.34645669 };

    static int defaultUnit = 0;

    static {
        for (int i = 0; i < UNITS.length; i++) {
            vUnits.add(UNITS[i]);
        }
    }

    public static int strToPts(String s) {
        double u = Double.valueOf(s).doubleValue();
        return toPoints(u);
    }

    public static void setDefaultUnit(String s) {
        defaultUnit = getUnitIndex(s);
    }

    public static int toPoints(double aVal) throws IllegalArgumentException {
        return toPoints(UNITS[defaultUnit], aVal);
    }

    public static double toUnits(int pts) {
        return toUnits(UNITS[defaultUnit], pts);
    }

    public static int toPoints(String aUnit, double aVal) throws IllegalArgumentException {
        return (int) Math.round(aVal * getRatio(aUnit));
    }

    public static double toUnits(String aUnit, int pts) throws IllegalArgumentException {
        double dPts = pts;
        return dPts / getRatio(aUnit);
    }

    private static int getUnitIndex(String aUnit) throws IllegalArgumentException {
        int unitIndex = vUnits.indexOf(aUnit);
        if (unitIndex < 0) {
            throw new IllegalArgumentException("Unknown unit " + aUnit);
        }
        return unitIndex;
    }

    private static double getRatio(String aUnit) throws IllegalArgumentException {
        return ratios[getUnitIndex(aUnit)];
    }
}
