package inertial.math.numbers;

import inertial.math.units.Dimension;
import inertial.math.units.Unit;
import inertial.math.units.UnitConvertException;

public interface Measurable<M extends Measurable> {

    public Dimension getDimension();

    public <O extends M> O convert(String currentUnit, String endUnit) throws UnitConvertException;

    public <O extends M> O convert(Unit currentUnit, Unit endUnit) throws UnitConvertException;
}
