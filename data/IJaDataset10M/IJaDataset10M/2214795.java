package validation.testcase;

import validation.Gte;
import validation.Lte;
import junit.framework.TestCase;

public class TestLte extends TestCase {

    public TestLte(String name) {
        super(name);
    }

    public void testValidate_sameIntValue() {
        String value = "20";
        String conditionValue = "20";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_singleValue() {
        String value = "sdfh";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate_intAndString() {
        String value = "20";
        String conditionValue = "abc";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_stringAndInt() {
        String value = "abc";
        String conditionValue = "20";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_twoString() {
        String value = "ABC";
        String conditionValue = "ABC";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_twoTimeEqual() {
        String value = "20:10:10";
        String conditionValue = "20:10:10";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_twoTimeNotEqual() {
        String value = "20:10:10";
        String conditionValue = "24:10:10";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_timeAndString() {
        String value = "20:10:10";
        String conditionValue = "ABC";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_stringAndTime() {
        String value = "ABC";
        String conditionValue = "20:10:10";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_twoAlphanumericEquals() {
        String value = "2AB";
        String conditionValue = "2AB";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_alphanumericAndInt() {
        String value = "2AB";
        String conditionValue = "10";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_twoDatesEqual() {
        String value = "20/10/10";
        String conditionValue = "20/10/10";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_twoDatesUnequal() {
        String value = "20/10/10";
        String conditionValue = "24/10/10";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate_dateAndTime() {
        String value = "20/10/10";
        String conditionValue = "10:20:10";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate15_dateAndTimeSame() {
        String value = "20/10/10";
        String conditionValue = "20:10:10";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate16_Zero() {
        String value = "0000";
        String conditionValue = "000000000";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate16_DecimalZero() {
        String value = "00.000000";
        String conditionValue = "00";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate16_FloatInt() {
        String value = "45.333";
        String conditionValue = "46";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate16_Float() {
        String value = "45.333";
        String conditionValue = "45.22222";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate17_Float() {
        String value = "45.333";
        String conditionValue = "45.22222";
        boolean expected = false;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate18_Float() {
        String value = "AJAY";
        String conditionValue = "ajay";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate19_Decimal() {
        String value = "0.00000000099";
        String conditionValue = "0.000089";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate20_DecimalMinus() {
        String value = "-0.00000000099";
        String conditionValue = "-0.000000000000000089";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate21_DecimalMinus() {
        String value = "0.01";
        String conditionValue = "0.01";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate22_Capital() {
        String value = "ABCD";
        String conditionValue = "ABCD";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }

    public void testValidate22_Space() {
        String value = "";
        String conditionValue = "";
        boolean expected = true;
        Lte lessthanEqual = new Lte();
        boolean actual = lessthanEqual.validate(value, conditionValue);
        assertEquals(expected, actual);
    }
}
