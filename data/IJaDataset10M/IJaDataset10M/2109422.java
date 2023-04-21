package android.util.cts;

import android.test.AndroidTestCase;
import android.util.StringBuilderPrinter;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(StringBuilderPrinter.class)
public class StringBuilderPrinterTest extends AndroidTestCase {

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, notes = "test methods: StringBuilderPrinter and println", method = "StringBuilderPrinter", args = { java.lang.StringBuilder.class }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "test methods: StringBuilderPrinter and println", method = "println", args = { java.lang.String.class }) })
    public void testStringBuilderPrinter() {
        StringBuilder strBuilder = new StringBuilder("Hello");
        StringBuilderPrinter strBuilderPrinter = new StringBuilderPrinter(strBuilder);
        assertEquals("Hello", strBuilder.toString());
        strBuilderPrinter.println(" Android");
        String str = strBuilder.toString();
        assertTrue(str.startsWith("Hello"));
        assertEquals(' ', str.charAt(5));
        assertEquals('A', str.charAt(6));
        assertEquals('n', str.charAt(7));
        assertEquals('d', str.charAt(8));
        assertEquals('r', str.charAt(9));
        assertEquals('o', str.charAt(10));
        assertEquals('i', str.charAt(11));
        assertEquals('d', str.charAt(12));
    }
}
