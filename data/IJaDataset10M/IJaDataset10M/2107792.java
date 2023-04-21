package jahuwaldt.tools.units;

/**
*  An abstract class that serves as the base class for various
*  length units such as meters, feet, miles, inches, etc.
*  The reference unit for this class is "Meters".
*
*  <p>  Modified by:	Joseph A. Huwaldt  </p>
*
*  @author   Joseph A. Huwaldt   Date:  December 16, 1997
*  @version  July 25, 2004
**/
public abstract class LengthUnit extends Unit {

    /**
	*  Converts a value with the current units to a value with
	*  the new units specified.
	*
	*  @param   value   The value in the "currentUnits" to be converted to "newUnit".
	*  @param   newUnit The new unit system to convert value to.
	*  @return  The value converted to the new units.
	**/
    public double convert(double value, Unit newUnit) throws CannotConvertException {
        if (!(newUnit instanceof LengthUnit)) {
            String oldName = this.getClass().getName();
            String newName = newUnit.getClass().getName();
            throw new CannotConvertException(oldName + " to " + newName);
        }
        value *= newUnit.factor() / this.factor();
        return value;
    }

    /**
	*  Returns the reference unit for this unit type.
	*
	*  @return  The reference unit for this type of unit object.
	**/
    public Unit getRefUnit() {
        return Meters.getInstance();
    }
}
