package validation.testcase;

import validation.Isdouble;
import junit.framework.TestCase;

public class TestIsdouble extends TestCase {

    public TestIsdouble(String name) {
        super(name);
    }

    public void testValidate1_IntValue() {
        String value = "20";
        Isdouble id = new Isdouble();
        boolean expected = true;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate2_DoubleValue() {
        String value = "20.22";
        Isdouble id = new Isdouble();
        boolean expected = true;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate3_IntCharValue() {
        String value = "20rr";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate4_DoubleCharValue() {
        String value = "20.32rr";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate5_CharValue() {
        String value = "abcde";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate6_SpaceValue() {
        String value = "";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate7_StartSpaceValue() {
        String value = "	555";
        Isdouble id = new Isdouble();
        boolean expected = true;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate8_EndSpaceValue() {
        String value = "555	";
        Isdouble id = new Isdouble();
        boolean expected = true;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate9_SpecialCharValue() {
        String value = "@#$%";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate10_ZeroValue() {
        String value = "000";
        Isdouble id = new Isdouble();
        boolean expected = true;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate11_ZeroDecimalValue() {
        String value = "000.00";
        Isdouble id = new Isdouble();
        boolean expected = true;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate12_ExpressionValue() {
        String value = "5+10+90";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate13_DoubleExpressionValue() {
        String value = "5.5+1.1+9.9";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate14_Decimal() {
        String value = "9.9.9.9";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate15_Decimal() {
        String value = "9999999999999999.9999999";
        Isdouble id = new Isdouble();
        boolean expected = true;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate16_Decimal() {
        String value = "00000000000000000000000000.9999999";
        Isdouble id = new Isdouble();
        boolean expected = true;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate17_Middlespace() {
        String value = "22 24";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate18_Check() {
        String value = "22";
        String conditionValue = "40";
        Isdouble id = new Isdouble();
        boolean expected = true;
        boolean actual = id.validate(value, conditionValue, "");
        assertEquals(expected, actual);
    }

    public void testValidate19_Date() {
        String value = "10/10/2008";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate20_Time() {
        String value = "10:21:20";
        Isdouble id = new Isdouble();
        boolean expected = false;
        boolean actual = id.validate(value);
        assertEquals(expected, actual);
    }

    public void testValidate19_Time() {
        String value = "10:10:10";
        String conditionValue = "10:10:10";
        Isdouble id = new Isdouble();
        boolean expected = true;
        boolean actual = id.validate(value, conditionValue, "");
        assertEquals(expected, actual);
    }
}
