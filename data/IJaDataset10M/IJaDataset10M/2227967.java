package org.mantikhor.literal.base;

import java.math.BigDecimal;
import java.math.BigInteger;
import junit.framework.TestCase;

/**
 * 3.3.13 integer
 * [Definition:]   integer is 'derived' from decimal by fixing the value of 'fractionDigits' 
 * to be 0and disallowing the trailing decimal point. This results in the standard mathematical 
 * concept of the integer numbers. The 'value space' of integer is the infinite set {...,-2,-1,0,1,2,...}. 
 * The 'base type' of integer is decimal. 
 * 
 * 3.3.13.1 Lexical representation
 * integer has a lexical representation consisting of a finite-length sequence of decimal digits (#x30-#x39) 
 * with an optional leading sign. If the sign is omitted, "+" is assumed. For example: -1, 0, 12678967543233, +100000. 
 * 
 * 3.3.13.2 Canonical representation
 * The canonical representation for integer is defined by prohibiting certain options from the Lexical 
 * representation ('3.3.13.1). Specifically, the preceding optional "+" sign is prohibited and leading 
 * zeroes are prohibited. 
 * 
 * 3.3.13.3 Constraining facets
 * integer has the following 'constraining facets': 
 * 
 *     - totalDigits
 *     - fractionDigits
 *     - pattern
 *     - whiteSpace
 *     - enumeration
 *     - maxInclusive
 *     - maxExclusive
 *     - minInclusive
 *     - minExclusive
 * 
 * 3.3.13.4 Derived datatypes
 * The following 'built-in' datatypes are 'derived' from integer: 
 * 
 *     - nonPositiveInteger
 *     - long
 *     - nonNegativeInteger
 *           
 * @author Bill B. and Dave W.
 */
public class BaseTypeIntegerTest extends TestCase {

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#hashCode()}.
     */
    public void testHashCode() {
        TestsForCommonMethods.hashCodeTest(BaseTypeInteger.getInstance());
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#getInstance()}.
     */
    public void testGetInstance() {
        BaseTypeInteger baseTypePosativeInteger = BaseTypeInteger.getInstance();
        assertNotNull(baseTypePosativeInteger);
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#getBaseTypeEnum()}.
     */
    public void testGetBaseTypeEnum() {
        BaseTypeInteger baseTypeInteger = BaseTypeInteger.getInstance();
        EnumOfBaseTypes baseType = baseTypeInteger.getBaseTypeEnum();
        assertTrue(baseType.equals(EnumOfBaseTypes.INTEGER));
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#isFacetTypeValid(org.mantikhor.literal.facet.Facet)}.
     */
    public void testIsFacetTypeValid() {
        TestsForCommonMethods.isFacetTypeValidTest();
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#getValueClasses()}.
     */
    public void testGetValueClasses() {
        TestsForCommonMethods.getValueClassesForIntegralTypeTest(BaseTypePositiveInteger.getInstance());
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#isValueOK(long)}.
     * We're going to test 900 or so long values and make sure that they all work for BaseTypeInteger.isValueOK(long).
     */
    public void testIsValueOKLong() {
        BaseTypeInteger baseTypeInteger = BaseTypeInteger.getInstance();
        assertTrue(baseTypeInteger.isValueOK(Long.MIN_VALUE));
        for (long testVal = Long.MIN_VALUE + 1L, count = 0; count < 300; count++, testVal++) {
            assertTrue(baseTypeInteger.isValueOK(testVal));
        }
        for (long testVal = -150L, count = 0; count < 300; count++, testVal++) {
            assertTrue(baseTypeInteger.isValueOK(testVal));
        }
        for (long testVal = Long.MAX_VALUE - 300, count = 0; count < 300; count++, testVal++) {
            assertTrue(baseTypeInteger.isValueOK(testVal));
        }
        assertTrue(baseTypeInteger.isValueOK(Long.MAX_VALUE));
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#isValueOK(double)}.
     * We're going to test 900 or so double values and make sure that they all work for BaseTypeInteger.isValueOK(double).
     */
    public void testIsValueOKDouble() {
        BaseTypeInteger baseTypeInteger = BaseTypeInteger.getInstance();
        assertTrue(baseTypeInteger.isValueOK(-1.7976931348623157E308));
        double testVal = -1.7976931348623157D;
        double incVal = 0.0000000000000010D;
        for (long count = 0; count < 300; count++) {
            double val = Math.pow(10.0D, 308.0D) * testVal;
            assertTrue(baseTypeInteger.isValueOK(val));
            testVal = testVal + incVal;
        }
        testVal = -150.0D;
        for (long count = 0; count < 300; count++) {
            assertTrue(baseTypeInteger.isValueOK(testVal));
            testVal = testVal + 1.0D;
        }
        testVal = 1.7976931348619807D;
        incVal = 0.0000000000000010D;
        for (long count = 0; count < 300; count++) {
            double val = Math.pow(10.0D, 308.0D) * testVal;
            assertTrue(baseTypeInteger.isValueOK(val));
            testVal = testVal + incVal;
        }
        assertTrue(baseTypeInteger.isValueOK(1.7976931348623157E308));
        assertFalse(baseTypeInteger.isValueOK(-700000000000000100000.0E-6D));
        assertFalse(baseTypeInteger.isValueOK(-700000000000001000000.0E-7D));
        assertFalse(baseTypeInteger.isValueOK(-700000000000001000000.0E-7D));
        assertFalse(baseTypeInteger.isValueOK(7.1D));
        assertFalse(baseTypeInteger.isValueOK(-7.1D));
        assertFalse(baseTypeInteger.isValueOK(Double.MIN_VALUE));
        double d1 = 1.0D / 3;
        double d2 = 2.0D / 3;
        double d3 = d1 + d2;
        assertTrue(baseTypeInteger.isValueOK(d3));
        double d4 = 0.4 + 0.6;
        assertTrue(baseTypeInteger.isValueOK(d4));
        assertTrue(baseTypeInteger.isValueOK(-0.0D));
        assertTrue(baseTypeInteger.isValueOK(0.0D));
        assertTrue(baseTypeInteger.isValueOK(1.23765111E14D));
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#isValueOK(java.math.BigInteger)}.
     * We're going to test 900 or so BigInteger values and make sure that they all work for BaseTypeInteger.isValueOK(BigInteger).
     */
    public void testIsValueOKBigInteger() {
        BaseTypeInteger baseTypeInteger = BaseTypeInteger.getInstance();
        BigInteger min = new BigInteger("-40000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        BigInteger max = new BigInteger("40000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertTrue(baseTypeInteger.isValueOK(min));
        for (long count = 0; count < 300; count++) {
            assertTrue(baseTypeInteger.isValueOK(min.add(new BigInteger(Long.toString(count)))));
        }
        for (long testVal = -150L, count = 0; count < 300; count++, testVal++) {
            assertTrue(baseTypeInteger.isValueOK(new BigInteger(Long.toString(testVal))));
        }
        for (long count = 300; count > 0; count--) {
            BigInteger val = max.subtract(new BigInteger(Long.toString(count)));
            assertTrue(baseTypeInteger.isValueOK(val));
        }
        assertTrue(baseTypeInteger.isValueOK(max));
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#isValueOK(java.math.BigDecimal)}.
     * We're going to test 900 or so BigDecimal values and make sure that they all work for BaseTypeInteger.isValueOK(BigDecimal).
     */
    public void testIsValueOKBigDecimal() {
        BaseTypeInteger baseTypeInteger = BaseTypeInteger.getInstance();
        BigDecimal min = new BigDecimal("-40000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.0");
        BigDecimal max = new BigDecimal("40000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000.0");
        assertTrue(baseTypeInteger.isValueOK(min));
        for (long count = 0; count < 300; count++) {
            assertTrue(baseTypeInteger.isValueOK(min.add(new BigDecimal(Long.toString(count)))));
        }
        for (long testVal = -150L, count = 0; count < 300; count++, testVal++) {
            assertTrue(baseTypeInteger.isValueOK(new BigDecimal(Long.toString(testVal))));
        }
        for (long count = 300; count > 0; count--) {
            BigDecimal val = max.subtract(new BigDecimal(Long.toString(count)));
            assertTrue(baseTypeInteger.isValueOK(val));
        }
        assertTrue(baseTypeInteger.isValueOK(max));
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#isValueOK(java.lang.Object)}.
     */
    public void testIsValueOKObject() {
        BaseTypeInteger baseTypeInteger = BaseTypeInteger.getInstance();
        assertFalse(baseTypeInteger.isValueOK("Hello"));
        assertFalse(baseTypeInteger.isValueOK("-2"));
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#isTypeOK(java.lang.Object)}.
     */
    public void testIsTypeOK() {
        BaseTypePositiveInteger baseTypePositiveInteger = BaseTypePositiveInteger.getInstance();
        assertFalse(baseTypePositiveInteger.isTypeOK("Hello"));
        byte a = 0;
        short b = 0;
        int c = 0;
        long d = 0;
        char e = '0';
        assertTrue(baseTypePositiveInteger.isTypeOK(a));
        assertTrue(baseTypePositiveInteger.isTypeOK(b));
        assertTrue(baseTypePositiveInteger.isTypeOK(c));
        assertTrue(baseTypePositiveInteger.isTypeOK(d));
        assertFalse(baseTypePositiveInteger.isTypeOK(e));
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#getCaption()}.
     */
    public void testGetCaption() {
        TestsForCommonMethods.getCaptionTest(BaseTypeInteger.getInstance(), "integer");
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#toMantiscribe(boolean)}.
     */
    public void testToMantiscribe() {
        TestsForCommonMethods.toMantiscribeTest(BaseTypeInteger.getInstance());
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#toMantleNode(boolean)}.
     */
    public void testToMantleNode() {
        TestsForCommonMethods.toMantleNodeTest(BaseTypeInteger.getInstance());
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#compareTo(org.mantikhor.literal.base.BaseType)}.
     */
    public void testCompareTo() {
        TestsForCommonMethods.compareToTest();
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#toString()}.
     */
    public void testToString() {
        TestsForCommonMethods.toStringTest(BaseTypeInteger.getInstance(), "BaseTypeInteger");
    }

    /**
     * Test method for {@link org.mantikhor.literal.base.BaseTypeInteger#equals(java.lang.Object)}.
     */
    public void testEqualsObject() {
        TestsForCommonMethods.equalsTest(BaseTypeInteger.getInstance(), BaseTypeInteger.getInstance());
    }
}
