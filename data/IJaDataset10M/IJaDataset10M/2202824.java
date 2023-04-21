package jpercentilos.res;

/**
 *
 * @author Joaquín Ignacio Aramendía <samsagax@gmail.com>
 */
public class Length extends Magnitude implements Dimensionizable {

    private LengthUnit unit;

    public static final Length NA = new Length(-1, LengthUnit.M);

    public Length(double value, LengthUnit unit) {
        super(value);
        this.unit = unit;
    }

    public double getValueInUnit(Unit anotherUnit) throws InvalidUnitException {
        return getValueInUnit(value, anotherUnit);
    }

    public double getValueInUnit(double value, Unit anotherUnit) throws InvalidUnitException {
        if (anotherUnit instanceof LengthUnit) {
            LengthUnit lengthUnit = (LengthUnit) anotherUnit;
            return value * unit.getMultiplier() / lengthUnit.getMultiplier();
        } else {
            throw new InvalidUnitException("Invalid unit: " + anotherUnit.toString() + " not an instance of " + unit.getClass().toString());
        }
    }

    public static class Height extends Length {

        public static final Height NA = new Height(-1, LengthUnit.M);

        public Height(double value, LengthUnit unit) {
            super(value, unit);
        }
    }

    public static class HeadPerimeter extends Length {

        public static final HeadPerimeter NA = new HeadPerimeter(-1, LengthUnit.M);

        public HeadPerimeter(double value, LengthUnit unit) {
            super(value, unit);
        }
    }
}
