package inertial.math.numbers;

import inertial.math.units.Conversion;
import inertial.math.units.Dimension;
import inertial.math.units.Unit;
import inertial.math.units.UnitConvertException;
import inertial.math.units.UnitsManager;

public final class MeasurableDecimalNumber extends RealNumber<MeasurableDecimalNumber> implements Measurable<MeasurableDecimalNumber> {

    public MeasurableDecimalNumber(double number, Dimension dimension) {
        super(number, dimension);
    }

    public MeasurableDecimalNumber(String number) {
        super(number);
    }

    public MeasurableDecimalNumber(MeasurableDecimalNumber number) {
        super(number);
    }

    public MeasurableDecimalNumber(MeasurableDecimalNumber number, Dimension dimension) {
        super(number, dimension);
    }

    public MeasurableDecimalNumber add(MeasurableDecimalNumber number) throws NumberException {
        if (!number.getDimension().equals(this.getDimension())) {
            throw new NumberException("The dimensions must be equals.");
        }
        MeasurableDecimalNumber newNumber = new MeasurableDecimalNumber(this);
        newNumber.setNumber(primitiveAdd(number));
        return newNumber;
    }

    public MeasurableDecimalNumber reduce(MeasurableDecimalNumber number) throws NumberException {
        if (!number.getDimension().equals(this.getDimension())) {
            throw new NumberException("The dimensions must be equals.");
        }
        MeasurableDecimalNumber newNumber = new MeasurableDecimalNumber(this);
        newNumber.setNumber(primitiveReduce(number));
        return newNumber;
    }

    public MeasurableDecimalNumber multiply(MeasurableDecimalNumber number) throws NumberException {
        Dimension newDimension = getDimension().multiply(number.getDimension());
        MeasurableDecimalNumber newNumber = new MeasurableDecimalNumber(this, newDimension);
        newNumber.setNumber(primitiveMultiply(number));
        return newNumber;
    }

    public MeasurableDecimalNumber divide(MeasurableDecimalNumber number) throws NumberException {
        Dimension newDimension = getDimension().divide(number.getDimension());
        MeasurableDecimalNumber newNumber = new MeasurableDecimalNumber(this, newDimension);
        newNumber.setNumber(primitiveDivide(number));
        return newNumber;
    }

    public MeasurableDecimalNumber elevate(MeasurableDecimalNumber number) throws NumberException {
        FractionalNumber fractionalNumber = new FractionalNumber(number);
        Dimension newDimension = getDimension().elevate(fractionalNumber.getNumerator().toInteger());
        newDimension = newDimension.rationalize(fractionalNumber.getDenominator().toInteger());
        MeasurableDecimalNumber newNumber = new MeasurableDecimalNumber(this, newDimension);
        newNumber.setNumber(primitiveElevate(number));
        return newNumber;
    }

    public MeasurableDecimalNumber rationalize(MeasurableDecimalNumber number) throws NumberException {
        FractionalNumber fractionalNumber = new FractionalNumber(number);
        Dimension newDimension = getDimension().elevate(fractionalNumber.getDenominator().toInteger());
        newDimension = newDimension.rationalize(fractionalNumber.getNumerator().toInteger());
        MeasurableDecimalNumber newNumber = new MeasurableDecimalNumber(this, newDimension);
        newNumber.setNumber(primitiveRationalize(number));
        return newNumber;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MeasurableDecimalNumber convert(String currentUnit, String endUnit) throws UnitConvertException {
        UnitsManager manager = UnitsManager.getInstance();
        return convert(manager.findUnitBySimbol(currentUnit), manager.findUnitBySimbol(endUnit));
    }

    @Override
    @SuppressWarnings("unchecked")
    public MeasurableDecimalNumber convert(Unit currentUnit, Unit endUnit) throws UnitConvertException {
        Conversion conversion = getDimension().convert(currentUnit, endUnit);
        MeasurableDecimalNumber newNumber = (MeasurableDecimalNumber) conversion.getConverter().calculate(this);
        newNumber.setDimension(conversion.getDimension());
        return newNumber;
    }

    @Override
    public MeasurableDecimalNumber additiveInverse() {
        return multiply(-1);
    }

    @Override
    public MeasurableDecimalNumber inverse() {
        return elevate(-1);
    }

    @Override
    public MeasurableDecimalNumber copy() {
        return new MeasurableDecimalNumber(this);
    }
}
