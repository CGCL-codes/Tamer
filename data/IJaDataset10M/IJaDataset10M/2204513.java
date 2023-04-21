package abbot.swt.util.test;

import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import not.an.abbot.pkg.NotAnAbbotClass;
import org.eclipse.swt.widgets.Widget;
import abbot.swt.tester.WidgetTester;
import abbot.swt.utilities.JavaUtil;

public class JavaUtilTest extends TestCase {

    public JavaUtilTest(String name) {
        super(name);
    }

    private static class ShouldBeQualified {
    }

    public void testGetClassName() {
        assertEquals("String", JavaUtil.getClassName(String.class));
        assertEquals("List", JavaUtil.getClassName(List.class));
        assertEquals("Widget", JavaUtil.getClassName(Widget.class));
        assertEquals("WidgetTester", JavaUtil.getClassName(WidgetTester.class));
        assertEquals("Integer[]", JavaUtil.getClassName(new Integer[0].getClass()));
        assertEquals("String[][]", JavaUtil.getClassName(new String[0][0].getClass()));
        assertEquals("JavaUtilTest$ShouldBeQualified", JavaUtil.getClassName(ShouldBeQualified.class));
        assertEquals("JavaUtilTest$ShouldBeQualified[]", JavaUtil.getClassName(new ShouldBeQualified[0].getClass()));
        assertEquals("JavaUtilTest$ShouldBeQualified[][]", JavaUtil.getClassName(new ShouldBeQualified[0][0].getClass()));
        assertEquals("not.an.abbot.pkg.NotAnAbbotClass", JavaUtil.getClassName(NotAnAbbotClass.class));
        assertEquals("not.an.abbot.pkg.NotAnAbbotClass[]", JavaUtil.getClassName(new NotAnAbbotClass[0].getClass()));
        assertEquals("not.an.abbot.pkg.NotAnAbbotClass[][]", JavaUtil.getClassName(new NotAnAbbotClass[0][0].getClass()));
    }

    public void testAppendArrayElement() {
        Integer[] expected = new Integer[] { 1, 2, 3, 4 };
        Integer[] actual = JavaUtil.append(new Integer[] { 1, 2, 3 }, 4);
        assertTrue(Arrays.deepEquals(expected, actual));
        expected = new Integer[] { 5 };
        actual = JavaUtil.append(new Integer[0], 5);
        assertTrue(Arrays.deepEquals(expected, actual));
        expected = new Integer[] { 1, 2, 3, null };
        actual = JavaUtil.append(new Integer[] { 1, 2, 3 }, (Integer) null);
        assertTrue(Arrays.deepEquals(expected, actual));
        expected = new Integer[] { null };
        actual = JavaUtil.append(new Integer[0], (Integer) null);
        assertTrue(Arrays.deepEquals(expected, actual));
        expected = new Integer[] { 1, null, 3, 4 };
        actual = JavaUtil.append(new Integer[] { 1, null, 3 }, 4);
        assertTrue(Arrays.deepEquals(expected, actual));
    }

    public void testAppendArrayArray() {
        Integer[] expected = new Integer[] { 1, 2, 3, 4 };
        Integer[] actual = JavaUtil.append(new Integer[] { 1, 2 }, new Integer[] { 3, 4 });
        assertTrue(Arrays.deepEquals(expected, actual));
        expected = new Integer[] { 1, 2, 3, 4 };
        actual = JavaUtil.append(new Integer[0], new Integer[] { 1, 2, 3, 4 });
        assertTrue(Arrays.deepEquals(expected, actual));
        actual = JavaUtil.append(new Integer[] { 1 }, new Integer[] { 2, 3, 4 });
        assertTrue(Arrays.deepEquals(expected, actual));
        actual = JavaUtil.append(new Integer[] { 1, 2 }, new Integer[] { 3, 4 });
        assertTrue(Arrays.deepEquals(expected, actual));
        actual = JavaUtil.append(new Integer[] { 1, 2, 3 }, new Integer[] { 4 });
        assertTrue(Arrays.deepEquals(expected, actual));
        actual = JavaUtil.append(new Integer[] { 1, 2, 3, 4 }, new Integer[0]);
        assertTrue(Arrays.deepEquals(expected, actual));
        expected = new Integer[0];
        actual = JavaUtil.append(new Integer[0], new Integer[0]);
        assertTrue(Arrays.deepEquals(expected, actual));
        expected = new Integer[] { 5 };
        actual = JavaUtil.append(new Integer[0], new Integer[] { 5 });
        assertTrue(Arrays.deepEquals(expected, actual));
        actual = JavaUtil.append(new Integer[] { 5 }, new Integer[0]);
        assertTrue(Arrays.deepEquals(expected, actual));
    }
}
