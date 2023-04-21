package jahuwaldt.js.param;

import java.util.ResourceBundle;
import javolution.context.ObjectFactory;
import javolution.lang.MathLib;
import javolution.lang.ValueType;
import javolution.lang.Realtime;
import javolution.text.Text;
import javolution.util.FastComparator;
import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.XMLSerializable;
import org.jscience.mathematics.structure.Field;
import javax.measure.converter.ConversionException;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.*;
import javax.measure.unit.Unit;
import javax.measure.unit.SI;
import javax.measure.unit.NonSI;
import javax.measure.Measurable;
import javax.realtime.MemoryArea;

/**
* <p> This class represents an amount for which 
* 	operations such as addition, subtraction, multiplication and division
* 	can be performed (it implements the {@link org.jscience.mathematics.structure.Field Field} interface).</p>
* 	
* <p> The nature of an amount can be deduced from its parameterization 
* 	(compile time) or its {@link #getUnit unit} (run time)..</p>
* 	
* <p> Operations between different amounts may or may not be authorized 
* 	based upon the current {@link org.jscience.physics.model.PhysicalModel
* 	PhysicalModel}. For example, adding <code>Parameter&lt;Length&gt;</code> and 
* 	<code>Parameter&lt;Duration&gt;</code> is not allowed by the 
* 	{@link org.jscience.physics.model.StandardModel StandardModel}, 
* 	but is authorized with the {@link
* 	org.jscience.physics.model.RelativisticModel RelativisticModel}.</p>
* 	
*  <p>  Modified by:  Joseph A. Huwaldt   </p>
*
*  @author  Joseph A. Huwaldt   Date: November 15, 2008
*  @version October 2, 2011
**/
public final class Parameter<Q extends Quantity> implements Measurable<Q>, Field<Parameter<Q>>, ValueType, Realtime, XMLSerializable {

    /**
	 * Serial ID for this class.
	 */
    private static final long serialVersionUID = -7209871188244228767L;

    /**
	*  The resource bundle for this package.
	**/
    static final ResourceBundle RESOURCES = ResourceBundle.getBundle("jahuwaldt.js.param.ParameterResources", java.util.Locale.getDefault());

    /**
 	* Holds a dimensionless measure of zero.
 	**/
    public static final Parameter<Dimensionless> ZERO = new Parameter<Dimensionless>();

    static {
        ZERO._unit = Unit.ONE;
        ZERO._value = 0;
    }

    /**
 	* Holds a dimensionless measure of one.
 	*/
    public static final Parameter<Dimensionless> ONE = new Parameter<Dimensionless>();

    static {
        ONE._unit = Unit.ONE;
        ONE._value = 1.0;
    }

    /**
	*  An angular measure of zero.
	**/
    public static final Parameter<Angle> ZERO_ANGLE = new Parameter<Angle>();

    static {
        ZERO_ANGLE._unit = Angle.UNIT;
        ZERO_ANGLE._value = 0;
    }

    /**
	*  An angular measure of <code>pi</code> or 180 degrees.
	**/
    public static final Parameter<Angle> PI_ANGLE = new Parameter<Angle>();

    static {
        PI_ANGLE._unit = NonSI.DEGREE_ANGLE;
        PI_ANGLE._value = 180.0;
    }

    /**
	*  An angular measure of <code>pi/2</code> or 90 degrees.
	**/
    public static final Parameter<Angle> HALFPI_ANGLE = new Parameter<Angle>();

    static {
        HALFPI_ANGLE._unit = NonSI.DEGREE_ANGLE;
        HALFPI_ANGLE._value = 90.0;
    }

    /**
	*  An angular measure of <code>2*pi</code> or 360 degrees.
	**/
    public static final Parameter<Angle> TWOPI_ANGLE = new Parameter<Angle>();

    static {
        TWOPI_ANGLE._unit = NonSI.DEGREE_ANGLE;
        TWOPI_ANGLE._value = 360.0;
    }

    /**
	*  An length measure of zero.
	**/
    public static final Parameter<Length> ZERO_LENGTH = new Parameter<Length>();

    static {
        ZERO_LENGTH._unit = Length.UNIT;
        ZERO_LENGTH._value = 0;
    }

    /**
	*  An area measure of zero.
	**/
    public static final Parameter<Area> ZERO_AREA = new Parameter<Area>();

    static {
        ZERO_AREA._unit = Area.UNIT;
        ZERO_AREA._value = 0;
    }

    /**
	*  A volumn measure of zero.
	**/
    public static final Parameter<Volume> ZERO_VOLUME = new Parameter<Volume>();

    static {
        ZERO_VOLUME._unit = Volume.UNIT;
        ZERO_VOLUME._value = 0;
    }

    /**
	*  An velocity measure of zero.
	**/
    public static final Parameter<Velocity> ZERO_VELOCITY = new Parameter<Velocity>();

    static {
        ZERO_VELOCITY._unit = Velocity.UNIT;
        ZERO_VELOCITY._value = 0;
    }

    /**
	*  An acceleration measure of zero.
	**/
    public static final Parameter<Acceleration> ZERO_ACCELERATION = new Parameter<Acceleration>();

    static {
        ZERO_ACCELERATION._unit = Acceleration.UNIT;
        ZERO_ACCELERATION._value = 0;
    }

    /**
	*  A duration measure of zero.
	**/
    public static final Parameter<Duration> ZERO_DURATION = new Parameter<Duration>();

    static {
        ZERO_DURATION._unit = Duration.UNIT;
        ZERO_DURATION._value = 0;
    }

    /**
	*  A force measure of zero.
	**/
    public static final Parameter<Force> ZERO_FORCE = new Parameter<Force>();

    static {
        ZERO_FORCE._unit = Force.UNIT;
        ZERO_FORCE._value = 0;
    }

    /**
 	* Holds the value stated in this measure's unit.
 	*/
    private double _value;

    /**
 	* Holds this measure's unit. 
 	*/
    private Unit<Q> _unit;

    /**
 	* Returns the measure corresponding to a value 
 	* (<code>double</code>) stated in the specified unit.
 	*
 	* @param value the value stated in the specified unit.
 	* @param unit the unit in which the value is stated.
 	* @return the corresponding measure object.
 	*/
    public static <Q extends Quantity> Parameter<Q> valueOf(double value, Unit<Q> unit) {
        if (unit == null) throw new NullPointerException(RESOURCES.getString("paramNullErr").replace("<NAME/>", "unit"));
        Parameter<Q> m = Parameter.newInstance(unit);
        m._value = value;
        return m;
    }

    /**
 	* Returns the measure represented by the specified character sequence.
 	*
 	* @param csq the character sequence.
 	* @return <code>ParameterFormat.newInstance().parse(csq)</code>
 	*/
    public static Parameter<?> valueOf(CharSequence csq) {
        return ParameterFormat.newInstance().parse(csq);
    }

    /**
 	* Returns the unit in which the {@link #getValue()
 	* value} is stated.
 	*
 	* @return the measure unit.
 	*/
    public Unit<Q> getUnit() {
        return _unit;
    }

    /**
 	* Returns the value for this measure stated in this measure's
 	* {@link #getUnit unit}. 
 	*
 	* @return the value of the measure.
 	*/
    public double getValue() {
        return _value;
    }

    /**
 	* Returns the measure equivalent to this measure but stated in the 
 	* specified unit. 
 	*
 	* @param  unit the unit of the measure to be returned.
 	* @return a measure equivalent to this measure but stated in the 
 	* 		specified unit.
 	* @throws ConversionException if the current model does not allows for
 	* 		conversion to the specified unit.
 	*/
    @SuppressWarnings("unchecked")
    public <R extends Quantity> Parameter<R> to(Unit<R> unit) {
        if ((_unit == unit) || this._unit.equals(unit)) return (Parameter<R>) this;
        UnitConverter cvtr = Parameter.converterOf(_unit, unit);
        if (cvtr == UnitConverter.IDENTITY) {
            Parameter<R> result = (Parameter<R>) Parameter.copyOf(this);
            result._unit = unit;
            return result;
        }
        Parameter<R> result = Parameter.newInstance(unit);
        double value = cvtr.convert(_value);
        result._value = value;
        return result;
    }

    /**
 	* Casts this Parameter to a parameterized unit of specified nature or 
 	* throw a <code>ClassCastException</code> if the dimension of the 
 	* specified quantity and this parameter's unit dimension do not match.
 	* 
 	* @param  type the quantity class identifying the nature of the unit.
 	* @return this Parameter parameterized with the specified type.
 	* @throws ClassCastException if the dimension of this parameter's unit is different 
 	* 		from the specified quantity dimension.
 	* @throws UnsupportedOperationException if the specified quantity class
 	* 		does not have a public static field named "UNIT" holding the 
 	* 		standard unit for the quantity.
 	*/
    @SuppressWarnings("unchecked")
    public final <T extends Quantity> Parameter<T> asType(Class<T> type) throws ClassCastException {
        @SuppressWarnings("unused") Unit<T> u = _unit.asType(type);
        return (Parameter<T>) this;
    }

    /**
 	* Returns the opposite of this measure.
 	*
 	* @return <code>-this</code>.
 	*/
    public Parameter<Q> opposite() {
        Parameter<Q> m = Parameter.newInstance(_unit);
        m._value = -_value;
        return m;
    }

    /**
 	* Returns the sum of this measure with the one specified.
 	*
 	* @param  that the measure to be added.
 	* @return <code>this + that</code>.
 	* @throws ConversionException if the current model does not allows for
 	* 		these quantities to be added.
 	*/
    public Parameter<Q> plus(Parameter<Q> that) throws ConversionException {
        final Parameter<Q> thatToUnit = that.to(_unit);
        Parameter<Q> m = Parameter.newInstance(_unit);
        double value = this._value + thatToUnit._value;
        m._value = value;
        return m;
    }

    /**
 	* Returns the difference of this measure with the one specified.
 	*
 	* @param  that the measure to be subtracted.
 	* @return <code>this - that</code>.
 	* @throws ConversionException if the current model does not allows for
 	* 		these quantities to be subtracted.
 	*/
    public Parameter<Q> minus(Parameter<? extends Q> that) throws ConversionException {
        final Parameter<Q> thatToUnit = that.to(_unit);
        Parameter<Q> m = Parameter.newInstance(_unit);
        double value = this._value - thatToUnit._value;
        m._value = value;
        return m;
    }

    /**
 	* Returns this measure scaled by the specified approximate factor
 	* (dimensionless).
 	*
 	* @param  factor the scaling factor.
 	* @return <code>this · factor</code>.
 	*/
    public Parameter<Q> times(double factor) {
        Parameter<Q> m = Parameter.newInstance(_unit);
        double value = _value * factor;
        m._value = value;
        return m;
    }

    /**
 	* Returns the product of this measure with the one specified.
 	*
 	* @param  that the measure multiplier.
 	* @return <code>this · that</code>.
 	*/
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Parameter times(Parameter that) {
        Unit unit = Parameter.productOf(this._unit, that._unit);
        Parameter m = Parameter.newInstance(unit);
        double value = _value * that._value;
        m._value = value;
        return m;
    }

    /**
 	* Returns the multiplicative inverse of this measure.
 	* If this measure is possibly zero, then the result is unbounded
 	* (]-infinity, +infinity[).
 	*
 	* @return <code>1 / this</code>.
 	*/
    public Parameter<Q> inverse() {
        Parameter<Q> m = newInstance(Parameter.inverseOf(_unit));
        if (this._value == 1.0) {
            m._value = 1;
            return m;
        }
        double value = 1.0 / _value;
        m._value = value;
        return m;
    }

    /**
 	* Returns this measure divided by the specified approximate divisor
 	* (dimensionless).
 	*
 	* @param  divisor the approximated divisor.
 	* @return <code>this / divisor</code>.
 	*/
    public Parameter<Q> divide(double divisor) {
        Parameter<Q> m = Parameter.newInstance(_unit);
        double value = _value / divisor;
        m._value = value;
        return m;
    }

    /**
 	* Returns this measure divided by the one specified.
 	*
 	* @param  that the measure divisor.
 	* @return <code>this / that</code>.
 	*/
    @SuppressWarnings("unchecked")
    public Parameter<? extends Quantity> divide(Parameter<?> that) {
        return this.times(that.inverse());
    }

    /**
 	* Returns the absolute value of this measure.
 	*
 	* @return  <code>|this|</code>.
 	*/
    public Parameter<Q> abs() {
        return (_value >= 0) ? this : this.opposite();
    }

    /**
 	* Returns the square root of this measure.
 	*
 	* @return <code>sqrt(this)</code>
 	* 
 	*/
    public Parameter<? extends Quantity> sqrt() {
        Parameter<Q> m = Parameter.newInstance(_unit.root(2));
        double value = MathLib.sqrt(_value);
        m._value = value;
        return m;
    }

    /**
 	* Returns the given root of this measure.
 	*
 	* @param  n the root's order (n != 0).
 	* @return the result of taking the given root of this quantity.
 	* @throws ArithmeticException if <code>n == 0</code>.
 	*/
    public Parameter<? extends Quantity> root(int n) {
        if (n == 0) throw new ArithmeticException(RESOURCES.getString("badRootOrder"));
        if (n < 0) return this.root(-n).inverse();
        if (n == 2) return this.sqrt();
        Parameter<Q> m = Parameter.newInstance(_unit.root(n));
        double value = MathLib.pow(_value, 1.0 / n);
        m._value = value;
        return m;
    }

    /**
 	* Returns this measure raised at the specified exponent.
 	*
 	* @param  exp the exponent.
 	* @return <code>this<sup>exp</sup></code>
 	*/
    public Parameter<? extends Quantity> pow(int exp) {
        if (exp < 0) return this.pow(-exp).inverse();
        if (exp == 0) return ONE;
        Parameter<?> pow2 = this;
        Parameter<?> result = null;
        while (exp >= 1) {
            if ((exp & 1) == 1) {
                result = (result == null) ? pow2 : result.times(pow2);
            }
            pow2 = pow2.times(pow2);
            exp >>>= 1;
        }
        return result;
    }

    /**
 	* Compares this measure with the specified measurable object.
 	*
 	* @param  that the measure to compare with.
 	* @return a negative integer, zero, or a positive integer as this measure
 	* 		is less than, equal to, or greater than that measurable.
 	* @throws ConversionException if the current model does not allows for
 	* 		these measure to be compared.
 	*/
    public int compareTo(Measurable<Q> that) {
        double thatValue = that.doubleValue(_unit);
        return Double.compare(_value, thatValue);
    }

    /**
 	* Compares this parameter against the specified object for strict 
 	* equality (same value and same units).
 	*
 	* @param  that the object to compare with.
 	* @return <code>true</code> if this measure is identical to that
 	* 		measure; <code>false</code> otherwise.
 	*/
    public boolean equals(Object that) {
        if (this == that) return true;
        if ((that == null) || (that.getClass() != this.getClass())) return false;
        Parameter<?> m = (Parameter<?>) that;
        if (!_unit.equals(m._unit)) return false;
        if (_value != m._value) return false;
        return true;
    }

    /**
 	* Returns the hash code for this parameter.
 	* 
 	* @return the hash code value.
 	*/
    public int hashCode() {
        int hash = 7;
        int var_code = (null == _unit ? 0 : _unit.hashCode());
        hash = hash * 31 + var_code;
        long bits = Double.doubleToLongBits(_value);
        var_code = (int) (bits ^ (bits >>> 32));
        hash = hash * 31 + var_code;
        return hash;
    }

    /**
 	* Indicates if this measure is ordered before that measure
 	* (independently of the measure unit).
 	*
 	* @return <code>this.compareTo(that) < 0</code>.
 	*/
    public boolean isLessThan(Parameter<Q> that) {
        return this.compareTo(that) < 0;
    }

    /**
 	* Indicates if this measure is ordered after that measure
 	* (independently of the measure unit).
 	*
 	* @return <code>this.compareTo(that) > 0</code>.
 	*/
    public boolean isGreaterThan(Parameter<Q> that) {
        return this.compareTo(that) > 0;
    }

    /**
 	* Compares this measure with that measure ignoring the sign.
 	*
 	* @return <code>|this| > |that|</code>
 	*/
    public boolean isLargerThan(Parameter<Q> that) {
        return this.abs().isGreaterThan(that.abs());
    }

    /**
 	* Compares this measure with that measure for approximate equality.
	* Approximate equality means that the two parameters, stated in this
	* parameter's units, differ by an amount less than the roundoff error
	* in this parameter.
 	*
 	* @return <code>this ~= that</code>
 	*/
    public boolean isApproxEqual(Parameter<Q> that) {
        if (this.equals(that)) return true;
        double thisVal = this.getValue();
        double thatVal = that.doubleValue(this._unit);
        if (MathLib.abs(thisVal - thatVal) >= epsValue(thisVal)) return false;
        return true;
    }

    /**
 	* Compares this measure with zero for approximate equality.
	* Approximately zero means that this parameter differs from
	* zero by an amount less than the roundoff error
	* in this parameter.
 	*
 	* @return <code>this ~= 0</code>
 	*/
    public boolean isApproxZero() {
        double thisVal = this.getValue();
        if (MathLib.abs(thisVal) >= epsValue(0.)) return false;
        return true;
    }

    /**
	*  Returns the smallest roundoff in quantities of size x, EPS, such that x + EPS > x.
	**/
    public static double epsValue(double x) {
        double eps = Double.longBitsToDouble(Double.doubleToLongBits(x) + 0x1L) - x;
        return eps;
    }

    /**
	*  Returns the minimum of this parameter and the specified one.
	**/
    public Parameter<Q> min(Parameter<Q> that) {
        if (this.isGreaterThan(that)) return that;
        return this;
    }

    /**
	*  Returns the maximum of this parameter and the specified one.
	**/
    public Parameter<Q> max(Parameter<Q> that) {
        if (this.isLessThan(that)) return that;
        return this;
    }

    /**
 	* Returns <code>true</code> if this <code>Parameter</code> value is a
	* Not-a-Number (NaN), <code>false</code> otherwise.
 	*
 	* @return <code>true</code> if the value represented by this object is NaN;
	*         <code>false</code> otherwise.
 	*/
    public boolean isNaN() {
        return Double.isNaN(_value);
    }

    /**
 	* Returns <code>true</code> if this <code>Parameter</code> value is
	* infinitely large in magnitude, <code>false</code> otherwise.
 	*
 	* @return <code>true</code> if the value represented by this object is
	*         positive infinity or negative infinity; <code>false</code> otherwise.
 	*/
    public boolean isInfinite() {
        return Double.isInfinite(_value);
    }

    /**
 	* Returns the text representation of this parameter.
 	*
 	* @return <code>ParameterFormat.newInstance().format(this)</code>
 	*/
    public Text toText() {
        return ParameterFormat.newInstance().format(this);
    }

    /**
 	* Returns the text representation of this parameter as a 
 	* <code>java.lang.String</code>.
 	* 
 	* @return <code>toText().toString()</code>
 	*/
    public final String toString() {
        return toText().toString();
    }

    /**
	* Returns the value of this measurable stated in the specified unit as 
	* a <code>double</code>.
	* 
	* @param unit the unit in which this measurable value is stated.
	* @return the numeric value after conversion to type <code>double</code>.
	*/
    public double doubleValue(Unit<Q> unit) {
        return ((_unit == unit) || _unit.equals(unit)) ? this.getValue() : this.to(unit).getValue();
    }

    /**
	* Returns the estimated integral value of this measurable stated in 
	* the specified unit as a <code>long</code>.
	* 
	* <p> Note: This method differs from the <code>Number.longValue()</code>
	* 		in the sense that the closest integer value is returned 
	* 		and an ArithmeticException is raised instead
	* 		of a bit truncation in case of overflow (safety critical).</p> 
	* 
	* @param unit the unit in which the measurable value is stated.
	* @return the numeric value after conversion to type <code>long</code>.
	* @throws ArithmeticException if this quantity cannot be represented 
	*		as a <code>long</code> number in the specified unit.
	*/
    public final long longValue(Unit<Q> unit) {
        if (!_unit.equals(unit)) return this.to(unit).longValue(unit);
        double doubleValue = this.getValue();
        if ((doubleValue >= Long.MIN_VALUE) && (doubleValue <= Long.MAX_VALUE)) return Math.round(doubleValue);
        throw new ArithmeticException(RESOURCES.getString("canNotBeLong").replace("<VAL/>", doubleValue + " " + _unit));
    }

    /**
	* Returns a copy of this Parameter 
	* {@link javolution.context.AllocatorContext allocated} 
	* by the calling thread (possibly on the stack).
	*	
	* @return an identical and independant copy of this Parameter.
	*/
    public Parameter<Q> copy() {
        Parameter<Q> value = Parameter.newInstance(_unit);
        value._value = _value;
        return value;
    }

    /**
	*  Returns the trigonometric cosine of the specified angle.
	**/
    public static Parameter<Dimensionless> cos(Parameter<Angle> angle) {
        double ca = MathLib.cos(angle.doubleValue(SI.RADIAN));
        return Parameter.valueOf(ca, Dimensionless.UNIT);
    }

    /**
	*  Returns the trigonometric sine of the specified angle.
	**/
    public static Parameter<Dimensionless> sin(Parameter<Angle> angle) {
        double ca = MathLib.sin(angle.doubleValue(SI.RADIAN));
        return Parameter.valueOf(ca, Dimensionless.UNIT);
    }

    /**
	*  Returns the trigonometric tangent of the specified angle.
	**/
    public static Parameter<Dimensionless> tan(Parameter<Angle> angle) {
        double ca = MathLib.tan(angle.doubleValue(SI.RADIAN));
        return Parameter.valueOf(ca, Dimensionless.UNIT);
    }

    /**
	*  Returns the arc sine of the specified value, in the range of
	*  <code>-HALFPI_ANGLE</code> through <code>HALFPI_ANGLE</code>.
	**/
    public static Parameter<Angle> asin(Parameter<Dimensionless> x) {
        double a = MathLib.asin(x.getValue());
        return Parameter.valueOf(a, SI.RADIAN);
    }

    /**
	*  Returns the arc cosine of the specified value, in the range of
	*  <code>ZERO_ANGLE</code> through <code>PI_ANGLE</code>.
	**/
    public static Parameter<Angle> acos(Parameter<Dimensionless> x) {
        double a = MathLib.acos(x.getValue());
        return Parameter.valueOf(a, SI.RADIAN);
    }

    /**
	*  Returns the arc tangent of the specified value, in the range of
	*  <code>-HALFPI_ANGLE</code> through <code>HALFPI_ANGLE</code>.
	**/
    public static Parameter<Angle> atan(Parameter<Dimensionless> x) {
        double a = MathLib.atan(x.getValue());
        return Parameter.valueOf(a, SI.RADIAN);
    }

    /**
	*  Returns the angle theta such that (x == cos(theta)) && (y == sin(theta)).
	*  The two parameters must be the same type of unit, but otherwise they
	*  may be in different units of the same type (e.g.: y in "km" and x in "ft").
	**/
    public static <R extends Quantity> Parameter<Angle> atan2(Parameter<R> y, Parameter<R> x) {
        double a = MathLib.atan2(y.getValue(), x.doubleValue(y.getUnit()));
        return Parameter.valueOf(a, SI.RADIAN);
    }

    static final FastMap<Unit<?>, FastMap<Unit<?>, Unit<?>>> MULT_LOOKUP = new FastMap<Unit<?>, FastMap<Unit<?>, Unit<?>>>("UNITS_MULT_LOOKUP").setKeyComparator(FastComparator.DIRECT);

    static final FastMap<Unit<?>, Unit<?>> INV_LOOKUP = new FastMap<Unit<?>, Unit<?>>("UNITS_INV_LOOKUP").setKeyComparator(FastComparator.DIRECT);

    static final FastMap<Unit<?>, FastMap<Unit<?>, UnitConverter>> CVTR_LOOKUP = new FastMap<Unit<?>, FastMap<Unit<?>, UnitConverter>>("UNITS_CVTR_LOOKUP").setKeyComparator(FastComparator.DIRECT);

    private static Unit<?> productOf(Unit<?> left, Unit<?> right) {
        FastMap<Unit<?>, Unit<?>> leftTable = MULT_LOOKUP.get(left);
        if (leftTable == null) return calculateProductOf(left, right);
        Unit<?> result = leftTable.get(right);
        if (result == null) return calculateProductOf(left, right);
        return result;
    }

    private static synchronized Unit<?> calculateProductOf(final Unit<?> left, final Unit<?> right) {
        MemoryArea memoryArea = MemoryArea.getMemoryArea(MULT_LOOKUP);
        memoryArea.executeInArea(new Runnable() {

            public void run() {
                FastMap<Unit<?>, Unit<?>> leftTable = MULT_LOOKUP.get(left);
                if (leftTable == null) {
                    leftTable = new FastMap<Unit<?>, Unit<?>>().setKeyComparator(FastComparator.DIRECT);
                    MULT_LOOKUP.put(left, leftTable);
                }
                Unit<?> result = leftTable.get(right);
                if (result == null) {
                    result = left.times(right);
                    leftTable.put(right, result);
                }
            }
        });
        return MULT_LOOKUP.get(left).get(right);
    }

    private static Unit<?> inverseOf(Unit<?> unit) {
        Unit<?> inverse = INV_LOOKUP.get(unit);
        if (inverse == null) return calculateInverseOf(unit);
        return inverse;
    }

    private static synchronized Unit<?> calculateInverseOf(final Unit<?> unit) {
        MemoryArea memoryArea = MemoryArea.getMemoryArea(INV_LOOKUP);
        memoryArea.executeInArea(new Runnable() {

            public void run() {
                Unit<?> inverse = INV_LOOKUP.get(unit);
                if (inverse == null) {
                    inverse = unit.inverse();
                    INV_LOOKUP.put(unit, inverse);
                }
            }
        });
        return INV_LOOKUP.get(unit);
    }

    /**
	*  Returns a unit converter that will convert between the specified
	*  units
	**/
    public static UnitConverter converterOf(Unit<?> left, Unit<?> right) {
        FastMap<Unit<?>, UnitConverter> leftTable = CVTR_LOOKUP.get(left);
        if (leftTable == null) return calculateConverterOf(left, right);
        UnitConverter result = leftTable.get(right);
        if (result == null) return calculateConverterOf(left, right);
        return result;
    }

    private static synchronized UnitConverter calculateConverterOf(final Unit<?> left, final Unit<?> right) {
        MemoryArea memoryArea = MemoryArea.getMemoryArea(CVTR_LOOKUP);
        memoryArea.executeInArea(new Runnable() {

            public void run() {
                FastMap<Unit<?>, UnitConverter> leftTable = CVTR_LOOKUP.get(left);
                if (leftTable == null) {
                    leftTable = new FastMap<Unit<?>, UnitConverter>().setKeyComparator(FastComparator.DIRECT);
                    synchronized (CVTR_LOOKUP) {
                        CVTR_LOOKUP.put(left, leftTable);
                    }
                }
                UnitConverter result = leftTable.get(right);
                if (result == null) {
                    result = left.getConverterTo(right);
                    synchronized (leftTable) {
                        leftTable.put(right, result);
                    }
                }
            }
        });
        return CVTR_LOOKUP.get(left).get(right);
    }

    /**
 	* Holds the default XML representation for measures.
 	* This representation consists of a <code>value</code>, 
 	* and an <code>unit</code>.
 	* The unit attribute determines the measurement type. For example:<pre>
 	* &lt;Parameter value="12" unit="µA"/&gt;</pre>
 	* represents an electric current measurement.
 	*/
    @SuppressWarnings({ "rawtypes" })
    protected static final XMLFormat<Parameter> XML = new XMLFormat<Parameter>(Parameter.class) {

        @Override
        public Parameter newInstance(Class<Parameter> cls, InputElement xml) throws XMLStreamException {
            Unit unit = Unit.valueOf(xml.getAttribute("unit"));
            Parameter<?> m = Parameter.newInstance(unit);
            double value = xml.getAttribute("value", 0.0);
            m._value = value;
            return m;
        }

        @Override
        public void read(javolution.xml.XMLFormat.InputElement arg0, Parameter arg1) throws XMLStreamException {
        }

        @Override
        public void write(Parameter m, OutputElement xml) throws XMLStreamException {
            xml.setAttribute("value", m._value);
            xml.setAttribute("unit", m._unit.toString());
        }
    };

    @SuppressWarnings("unchecked")
    private static <Q extends Quantity> Parameter<Q> newInstance(Unit<?> unit) {
        Parameter<Q> measure = FACTORY.object();
        measure._unit = (Unit<Q>) unit;
        return measure;
    }

    @SuppressWarnings("unchecked")
    private static <Q extends Quantity> Parameter<Q> copyOf(Parameter<Q> original) {
        Parameter<Q> measure = FACTORY.object();
        measure._value = original._value;
        measure._unit = original._unit;
        return measure;
    }

    @SuppressWarnings({ "rawtypes" })
    private static final ObjectFactory<Parameter> FACTORY = new ObjectFactory<Parameter>() {

        @Override
        protected Parameter create() {
            return new Parameter();
        }
    };

    private Parameter() {
    }

    /**
	*  Testing code for this class.
	**/
    @SuppressWarnings("unchecked")
    public static void main(String args[]) {
        System.out.println("Testing Parameter:  test = result [correct result]");
        Parameter<Length> p1 = Parameter.valueOf(2, NonSI.FOOT);
        System.out.println("p1 = " + p1);
        System.out.println("  converted to m  = " + p1.to(SI.METER) + " [0.6096 m]");
        System.out.println("  opposite		= " + p1.opposite() + " [-2.0 ft]");
        System.out.println("  p1.inverse()	= " + p1.inverse() + " [0.5 1/ft]");
        System.out.println("  p1.abs()		= " + p1.abs() + " [2.0 ft]");
        System.out.println("  p1.opposite().abs() = " + p1.opposite().abs() + " [2.0 ft]");
        System.out.println("  p1.sqrt()   	= " + p1.sqrt() + " [1.4142135623731 ft^1:2]");
        System.out.println("  p1.root(3)  	= " + p1.root(3) + " [1.25992104989487 ft^1:3]");
        System.out.println("  p1.pow(5)   	= " + p1.pow(5) + " [32.0 ft^5]");
        Parameter<Length> p2 = Parameter.valueOf(1, SI.METER);
        System.out.println("p2 = " + p2);
        System.out.println("  p1 + p2  		= " + p1.plus(p2) + " [5.28083989501312 ft]");
        System.out.println("  p1 - p2  		= " + p1.minus(p2) + " [-1.28083989501312 ft]");
        System.out.println("  p1*2 			= " + p1.times(2) + " [4.0 ft]");
        System.out.println("  p1*ZERO  		= " + p1.times(ZERO) + " [0.0 ft]");
        System.out.println("  p1 * p2  		= " + p1.times(p2));
        System.out.println("  converted to ft² = " + p1.times(p2).to(NonSI.SQUARE_FOOT) + " [6.56167979002625 ft²]");
        System.out.println("  p1 / 2   		= " + p1.divide(2) + " [1.0 ft]");
        System.out.println("  p1 / p2  		= " + p1.divide(p2));
        System.out.println("  converted to dimensionless = " + p1.divide(p2).to(Dimensionless.UNIT) + " [0.6096]");
        System.out.println("  p1.compareTo(p2) = " + p1.compareTo(p2) + " [-1]");
        System.out.println("  p2.compareTo(p1) = " + p2.compareTo(p1) + " [1]");
        System.out.println("  p1.compareTo(p1) = " + p1.compareTo(p1) + " [0]");
        System.out.println("  p1.equals(p1)	= " + p1.equals(p1) + " [true]");
        System.out.println("  p1.equals(p2)	= " + p1.equals(p2) + " [false]");
        System.out.println("  p1.equals(p1.to(SI.METER)) = " + p1.equals(p1.to(SI.METER)) + " [false]");
        System.out.println("  p1.isLessThan(p2)	= " + p1.isLessThan(p2) + " [true]");
        System.out.println("  p1.isGreaterThan(p2) = " + p1.isGreaterThan(p2) + " [false]");
        System.out.println("  p1.isLargerThan(p2)  = " + p1.isLargerThan(p2) + " [false]");
        Parameter<Length> p3 = Parameter.valueOf(-1, SI.METER);
        System.out.println("p3 = " + p3);
        System.out.println("  p3.isGreaterThan(p1) = " + p3.isGreaterThan(p1) + " [false]");
        System.out.println("  p3.isLargerThan(p1)  = " + p3.isLargerThan(p1) + " [true]");
        Parameter<?> a1 = Parameter.valueOf("2.5 ft^2");
        System.out.println("Parameter.valueOf(\"2.5 ft^2\") = " + a1);
    }
}
