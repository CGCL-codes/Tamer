package proguard.optimize.evaluation.value;

/**
 * This class represents a partially evaluated integer value.
 *
 * @author Eric Lafortune
 */
public class IntegerValue extends Category1Value {

    /**
     * Returns the specific integer value, if applicable.
     */
    public int value() {
        return 0;
    }

    /**
     * Returns the generalization of this IntegerValue and the given other
     * IntegerValue.
     */
    public IntegerValue generalize(IntegerValue other) {
        return this;
    }

    /**
     * Returns the sum of this IntegerValue and the given IntegerValue.
     */
    public IntegerValue add(IntegerValue other) {
        return this;
    }

    /**
     * Returns the difference of this IntegerValue and the given IntegerValue.
     */
    public IntegerValue subtract(IntegerValue other) {
        return this;
    }

    /**
     * Returns the difference of the given IntegerValue and this IntegerValue.
     */
    public IntegerValue subtractFrom(IntegerValue other) {
        return this;
    }

    /**
     * Returns the product of this IntegerValue and the given IntegerValue.
     */
    public IntegerValue multiply(IntegerValue other) throws ArithmeticException {
        return this;
    }

    /**
     * Returns the quotient of this IntegerValue and the given IntegerValue.
     */
    public IntegerValue divide(IntegerValue other) throws ArithmeticException {
        return this;
    }

    /**
     * Returns the quotient of the given IntegerValue and this IntegerValue.
     */
    public IntegerValue divideOf(IntegerValue other) throws ArithmeticException {
        return this;
    }

    /**
     * Returns the remainder of this IntegerValue divided by the given
     * IntegerValue.
     */
    public IntegerValue remainder(IntegerValue other) throws ArithmeticException {
        return this;
    }

    /**
     * Returns the remainder of the given IntegerValue divided by this
     * IntegerValue.
     */
    public IntegerValue remainderOf(IntegerValue other) throws ArithmeticException {
        return this;
    }

    /**
     * Returns this IntegerValue, shifted left by the given IntegerValue.
     */
    public IntegerValue shiftLeft(IntegerValue other) {
        return this;
    }

    /**
     * Returns the given IntegerValue, shifted left by this IntegerValue.
     */
    public IntegerValue shiftLeftOf(IntegerValue other) {
        return this;
    }

    /**
     * Returns this IntegerValue, shifted right by the given IntegerValue.
     */
    public IntegerValue shiftRight(IntegerValue other) {
        return this;
    }

    /**
     * Returns the given IntegerValue, shifted right by this IntegerValue.
     */
    public IntegerValue shiftRightOf(IntegerValue other) {
        return this;
    }

    /**
     * Returns this unsigned IntegerValue, shifted left by the given
     * IntegerValue.
     */
    public IntegerValue unsignedShiftRight(IntegerValue other) {
        return this;
    }

    /**
     * Returns the given unsigned IntegerValue, shifted left by this
     * IntegerValue.
     */
    public IntegerValue unsignedShiftRightOf(IntegerValue other) {
        return this;
    }

    /**
     * Returns the given LongValue, shifted left by this IntegerValue.
     */
    public LongValue shiftLeftOf(LongValue other) {
        return LongValueFactory.create();
    }

    /**
     * Returns the given LongValue, shifted right by this IntegerValue.
     */
    public LongValue shiftRightOf(LongValue other) {
        return LongValueFactory.create();
    }

    /**
     * Returns the given unsigned LongValue, shifted right by this IntegerValue.
     */
    public LongValue unsignedShiftRightOf(LongValue other) {
        return LongValueFactory.create();
    }

    /**
     * Returns the logical <i>and</i> of this IntegerValue and the given
     * IntegerValue.
     */
    public IntegerValue and(IntegerValue other) {
        return this;
    }

    /**
     * Returns the logical <i>or</i> of this IntegerValue and the given
     * IntegerValue.
     */
    public IntegerValue or(IntegerValue other) {
        return this;
    }

    /**
     * Returns the logical <i>xor</i> of this IntegerValue and the given
     * IntegerValue.
     */
    public IntegerValue xor(IntegerValue other) {
        return this;
    }

    /**
     * Returns whether this IntegerValue and the given IntegerValue are equal:
     * <code>NEVER</code>, <code>MAYBE</code>, or <code>ALWAYS</code>.
     */
    public int equal(IntegerValue other) {
        return MAYBE;
    }

    /**
     * Returns whether this IntegerValue is less than the given IntegerValue:
     * <code>NEVER</code>, <code>MAYBE</code>, or <code>ALWAYS</code>.
     */
    public int lessThan(IntegerValue other) {
        return MAYBE;
    }

    /**
     * Returns whether this IntegerValue is less than or equal to the given
     * IntegerValue: <code>NEVER</code>, <code>MAYBE</code>, or
     * <code>ALWAYS</code>.
     */
    public int lessThanOrEqual(IntegerValue other) {
        return MAYBE;
    }

    /**
     * Returns whether this IntegerValue and the given IntegerValue are different:
     * <code>NEVER</code>, <code>MAYBE</code>, or <code>ALWAYS</code>.
     */
    public final int notEqual(IntegerValue other) {
        return -equal(other);
    }

    /**
     * Returns whether this IntegerValue is greater than the given IntegerValue:
     * <code>NEVER</code>, <code>MAYBE</code>, or <code>ALWAYS</code>.
     */
    public final int greaterThan(IntegerValue other) {
        return -lessThanOrEqual(other);
    }

    /**
     * Returns whether this IntegerValue is greater than or equal to the given IntegerValue:
     * <code>NEVER</code>, <code>MAYBE</code>, or <code>ALWAYS</code>.
     */
    public final int greaterThanOrEqual(IntegerValue other) {
        return -lessThan(other);
    }

    /**
     * Returns the negated value of this IntegerValue.
     */
    public IntegerValue negate() {
        return this;
    }

    /**
     * Converts this IntegerValue to a byte IntegerValue.
     */
    public IntegerValue convertToByte() {
        return this;
    }

    /**
     * Converts this IntegerValue to a character IntegerValue.
     */
    public IntegerValue convertToCharacter() {
        return this;
    }

    /**
     * Converts this IntegerValue to a short IntegerValue.
     */
    public IntegerValue convertToShort() {
        return this;
    }

    /**
     * Converts this IntegerValue to a LongValue.
     */
    public LongValue convertToLong() {
        return LongValueFactory.create();
    }

    /**
     * Converts this IntegerValue to a FloatValue.
     */
    public FloatValue convertToFloat() {
        return FloatValueFactory.create();
    }

    /**
     * Converts this IntegerValue to a DoubleValue.
     */
    public DoubleValue convertToDouble() {
        return DoubleValueFactory.create();
    }

    /**
     * Returns the generalization of this IntegerValue and the given other
     * SpecificIntegerValue.
     */
    public IntegerValue generalize(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the sum of this IntegerValue and the given SpecificIntegerValue.
     */
    public IntegerValue add(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the difference of this IntegerValue and the given SpecificIntegerValue.
     */
    public IntegerValue subtract(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the difference of the given SpecificIntegerValue and this IntegerValue.
     */
    public IntegerValue subtractFrom(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the product of this IntegerValue and the given SpecificIntegerValue.
     */
    public IntegerValue multiply(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the quotient of this IntegerValue and the given
     * SpecificIntegerValue.
     */
    public IntegerValue divide(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the quotient of the given SpecificIntegerValue and this
     * IntegerValue.
     */
    public IntegerValue divideOf(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the remainder of this IntegerValue divided by the given
     * SpecificIntegerValue.
     */
    public IntegerValue remainder(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the remainder of the given SpecificIntegerValue divided by this
     * IntegerValue.
     */
    public IntegerValue remainderOf(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns this IntegerValue, shifted left by the given SpecificIntegerValue.
     */
    public IntegerValue shiftLeft(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the given SpecificIntegerValue, shifted left by this IntegerValue.
     */
    public IntegerValue shiftLeftOf(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns this IntegerValue, shifted right by the given SpecificIntegerValue.
     */
    public IntegerValue shiftRight(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the given SpecificIntegerValue, shifted right by this IntegerValue.
     */
    public IntegerValue shiftRightOf(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns this unsigned IntegerValue, shifted right by the given
     * SpecificIntegerValue.
     */
    public IntegerValue unsignedShiftRight(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the given unsigned SpecificIntegerValue, shifted right by this
     * IntegerValue.
     */
    public IntegerValue unsignedShiftRightOf(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the given SpecificLongValue, shifted left by this IntegerValue.
     */
    public LongValue shiftLeftOf(SpecificLongValue other) {
        return LongValueFactory.create();
    }

    /**
     * Returns the given SpecificLongValue, shifted right by this IntegerValue.
     */
    public LongValue shiftRightOf(SpecificLongValue other) {
        return LongValueFactory.create();
    }

    /**
     * Returns the given unsigned SpecificLongValue, shifted right by this
     * IntegerValue.
     */
    public LongValue unsignedShiftRightOf(SpecificLongValue other) {
        return LongValueFactory.create();
    }

    /**
     * Returns the logical <i>and</i> of this IntegerValue and the given
     * SpecificIntegerValue.
     */
    public IntegerValue and(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the logical <i>or</i> of this IntegerValue and the given
     * SpecificIntegerValue.
     */
    public IntegerValue or(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns the logical <i>xor</i> of this IntegerValue and the given
     * SpecificIntegerValue.
     */
    public IntegerValue xor(SpecificIntegerValue other) {
        return this;
    }

    /**
     * Returns whether this IntegerValue and the given SpecificIntegerValue are
     * equal: <code>NEVER</code>, <code>MAYBE</code>, or <code>ALWAYS</code>.
     */
    public int equal(SpecificIntegerValue other) {
        return MAYBE;
    }

    /**
     * Returns whether this IntegerValue is less than the given
     * SpecificIntegerValue: <code>NEVER</code>, <code>MAYBE</code>, or
     * <code>ALWAYS</code>.
     */
    public int lessThan(SpecificIntegerValue other) {
        return MAYBE;
    }

    /**
     * Returns whether this IntegerValue is less than or equal to the given
     * SpecificIntegerValue: <code>NEVER</code>, <code>MAYBE</code>, or
     * <code>ALWAYS</code>.
     */
    public int lessThanOrEqual(SpecificIntegerValue other) {
        return MAYBE;
    }

    /**
     * Returns whether this IntegerValue and the given SpecificIntegerValue are
     * different: <code>NEVER</code>, <code>MAYBE</code>, or <code>ALWAYS</code>.
     */
    public final int notEqual(SpecificIntegerValue other) {
        return -equal(other);
    }

    /**
     * Returns whether this IntegerValue is greater than the given
     * SpecificIntegerValue: <code>NEVER</code>, <code>MAYBE</code>, or
     * <code>ALWAYS</code>.
     */
    public final int greaterThan(SpecificIntegerValue other) {
        return -lessThanOrEqual(other);
    }

    /**
     * Returns whether this IntegerValue is greater than or equal to the given
     * SpecificIntegerValue: <code>NEVER</code>, <code>MAYBE</code>, or
     * <code>ALWAYS</code>.
     */
    public final int greaterThanOrEqual(SpecificIntegerValue other) {
        return -lessThan(other);
    }

    public final IntegerValue integerValue() {
        return this;
    }

    public final Value generalize(Value other) {
        return this.generalize(other.integerValue());
    }

    public final int computationalType() {
        return TYPE_INTEGER;
    }

    public boolean equals(Object object) {
        return object != null && this.getClass() == object.getClass();
    }

    public int hashCode() {
        return this.getClass().hashCode();
    }

    public String toString() {
        return "i";
    }
}
