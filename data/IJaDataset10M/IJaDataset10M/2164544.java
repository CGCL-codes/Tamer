package doclava;

import com.google.doclava.FieldInfo;
import junit.framework.TestCase;

public class FloatingPointToStringTest extends TestCase {

    public void testFloatEquals() {
        assertEquals("1.0f", FieldInfo.constantLiteralValue(1.0f));
        assertEquals("0.01f", FieldInfo.constantLiteralValue(0.01f));
        assertEquals("0.001f", FieldInfo.constantLiteralValue(0.001f));
        assertEquals("1.0E-10f", FieldInfo.constantLiteralValue(0.0000000001f));
        assertEquals("(0.0f / 0.0f)", FieldInfo.constantLiteralValue(Float.NaN));
        assertEquals("(-1.0f / 0.0f)", FieldInfo.constantLiteralValue(Float.NEGATIVE_INFINITY));
        assertEquals("(1.0f / 0.0f)", FieldInfo.constantLiteralValue(Float.POSITIVE_INFINITY));
    }

    public void testDoubleEquals() {
        assertEquals("1.0", FieldInfo.constantLiteralValue(1.0));
        assertEquals("0.01", FieldInfo.constantLiteralValue(0.01));
        assertEquals("0.001", FieldInfo.constantLiteralValue(0.001));
        assertEquals("1.0E-10", FieldInfo.constantLiteralValue(0.0000000001));
        assertEquals("(0.0 / 0.0)", FieldInfo.constantLiteralValue(Double.NaN));
        assertEquals("(-1.0 / 0.0)", FieldInfo.constantLiteralValue(Double.NEGATIVE_INFINITY));
        assertEquals("(1.0 / 0.0)", FieldInfo.constantLiteralValue(Double.POSITIVE_INFINITY));
    }
}
