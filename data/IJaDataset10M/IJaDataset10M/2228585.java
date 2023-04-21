package ng.runtime.metaclass.primitives.byteimpl;

import ng.runtime.metaclass.MetaClass;
import ng.runtime.threadcontext.BinaryArithmeticCategorySupport;
import ng.runtime.threadcontext.BinaryLogicalCategorySupport;
import ng.runtime.threadcontext.BooleanComparisonCategorySupport;
import ng.runtime.threadcontext.ConversionOperationCategorySupport;
import ng.runtime.threadcontext.IntegerComparisonCategorySupport;
import ng.runtime.threadcontext.ShiftCategorySupport;

public interface ByteMetaClass extends MetaClass {

    void modifyConvert(ByteConversion modifiedConvert);

    ByteConversion getOriginalConvert();

    ByteConversion convert(ConversionOperationCategorySupport conversionOperation);

    void modifyAdd(ByteBinaryArithmeticOperation modifiedAdd);

    ByteBinaryArithmeticOperation getOriginalAdd();

    ByteBinaryArithmeticOperation add(BinaryArithmeticCategorySupport binaryArithmeticOperation);

    void modifySubtract(ByteBinaryArithmeticOperation modifiedSubtract);

    ByteBinaryArithmeticOperation getOriginalSubtract();

    ByteBinaryArithmeticOperation subtract(BinaryArithmeticCategorySupport binaryArithmeticOperation);

    void modifyMultiply(ByteBinaryArithmeticOperation modifiedMultiply);

    ByteBinaryArithmeticOperation getOriginalMultiply();

    ByteBinaryArithmeticOperation multiply(BinaryArithmeticCategorySupport binaryArithmeticOperation);

    void modifyDivide(ByteBinaryArithmeticOperation modifiedDivide);

    ByteBinaryArithmeticOperation getOriginalDivide();

    ByteBinaryArithmeticOperation divide(BinaryArithmeticCategorySupport binaryArithmeticOperation);

    void modifyModulo(ByteBinaryArithmeticOperation modifiedModulo);

    ByteBinaryArithmeticOperation getOriginalModulo();

    ByteBinaryArithmeticOperation modulo(BinaryArithmeticCategorySupport binaryArithmeticOperation);

    void modifyRemainderDivide(ByteBinaryArithmeticOperation modifiedRemainderDivide);

    ByteBinaryArithmeticOperation getOriginalRemainderDivide();

    ByteBinaryArithmeticOperation remainderDivide(BinaryArithmeticCategorySupport binaryArithmeticOperation);

    void modifyAnd(ByteBinaryLogicalOperation modifiedAnd);

    ByteBinaryLogicalOperation getOriginalAnd();

    ByteBinaryLogicalOperation and(BinaryLogicalCategorySupport binaryLogicalOperation);

    void modifyOr(ByteBinaryLogicalOperation modifiedOr);

    ByteBinaryLogicalOperation getOriginalOr();

    ByteBinaryLogicalOperation or(BinaryLogicalCategorySupport binaryLogicalOperation);

    void modifyXor(ByteBinaryLogicalOperation modifiedXor);

    ByteBinaryLogicalOperation getOriginalXor();

    ByteBinaryLogicalOperation xor(BinaryLogicalCategorySupport binaryLogicalOperation);

    void modifyLeftShift(ByteShiftOperation modifiedLeftShift);

    ByteShiftOperation getOriginalLeftShift();

    ByteShiftOperation leftShift(ShiftCategorySupport shiftOperation);

    void modifyRightShift(ByteShiftOperation modifiedRightShift);

    ByteShiftOperation getOriginalRightShift();

    ByteShiftOperation rightShift(ShiftCategorySupport shiftOperation);

    void modifyUnsignedRightShift(ByteShiftOperation modifiedUnsignedRightShift);

    ByteShiftOperation getOriginalUnsignedRightShift();

    ByteShiftOperation unsignedRightShift(ShiftCategorySupport shiftOperation);

    void modifyPower(ByteBinaryArithmeticOperation modifiedPower);

    ByteBinaryArithmeticOperation getOriginalPower();

    ByteBinaryArithmeticOperation power(BinaryArithmeticCategorySupport binaryArithmeticOperation);

    void modifyCompare(ByteIntegerComparison modifiedCompare);

    ByteIntegerComparison getOriginalCompare();

    ByteIntegerComparison compare(IntegerComparisonCategorySupport integerComparison);

    void modifyEquals(ByteBooleanComparison modifiedEquals);

    ByteBooleanComparison getOriginalEquals();

    ByteBooleanComparison equals(BooleanComparisonCategorySupport booleanComparison);

    void modifyNotEquals(ByteBooleanComparison modifiedNotEquals);

    ByteBooleanComparison getOriginalNotEquals();

    ByteBooleanComparison notEquals(BooleanComparisonCategorySupport booleanComparison);

    void modifyLessThan(ByteBooleanComparison modifiedLessThan);

    ByteBooleanComparison getOriginalLessThan();

    ByteBooleanComparison lessThan(BooleanComparisonCategorySupport booleanComparison);

    void modifyGreaterThan(ByteBooleanComparison modifiedGreaterThan);

    ByteBooleanComparison getOriginalGreaterThan();

    ByteBooleanComparison greaterThan(BooleanComparisonCategorySupport booleanComparison);

    void modifyLessThanOrEquals(ByteBooleanComparison modifiedLessThanOrEquals);

    ByteBooleanComparison getOriginalLessThanOrEquals();

    ByteBooleanComparison lessThanOrEquals(BooleanComparisonCategorySupport booleanComparison);

    void modifyGreaterThanOrEquals(ByteBooleanComparison modifiedGreaterThanOrEquals);

    ByteBooleanComparison getOriginalGreaterThanOrEquals();

    ByteBooleanComparison greaterThanOrEquals(BooleanComparisonCategorySupport booleanComparison);
}
