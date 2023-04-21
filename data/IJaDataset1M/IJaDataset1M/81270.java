package javax.print.attribute;

import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.NumberUp;
import javax.print.attribute.standard.PagesPerMinute;
import junit.framework.TestCase;

public class IntegerSyntaxTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(IntegerSyntaxTest.class);
    }

    static {
        System.out.println("IntegerSyntax testing...");
    }

    IntegerSyntax is1, is2;

    public final void testIntegerSyntax_Int() {
        is1 = new integerSyntax(300);
        assertEquals(300, is1.getValue());
    }

    public final void testIntegerSyntax_IntIntInt() {
        try {
            is1 = new integerSyntax(-1, 1, 1);
            fail("Constructor IntegerSyntax(int value, int lowerBound, int upperBound) " + "doesn't throw IllegalArgumentException " + "if value is less than lowerBound");
        } catch (IllegalArgumentException e) {
        }
        try {
            is1 = new integerSyntax(2, 1, 1);
            fail("Constructor IntegerSyntax(int value, int lowerBound, int upperBound) " + "doesn't throw IllegalArgumentException " + "if value is greater than upperBound");
        } catch (IllegalArgumentException e) {
        }
        is1 = new integerSyntax(300, 1, 400);
        assertEquals(300, is1.getValue());
    }

    public final void testHashCode() {
        is1 = new integerSyntax(1000);
        is2 = new integerSyntax(1000 - 1);
        assertTrue(is2.hashCode() == 999);
        assertTrue(is1.hashCode() != is2.hashCode());
    }

    public final void testHashCode1() {
        is1 = new integerSyntax(1, 1, 10);
        is2 = new integerSyntax(1, 1, 15);
        assertTrue(is1.hashCode() == 1);
        assertTrue(is1.hashCode() == is2.hashCode());
    }

    public final void testHashCode2() {
        is1 = new Copies(5);
        is2 = new NumberUp(5);
        assertTrue(is1.hashCode() == 5);
        assertTrue(is2.hashCode() == 5);
        assertTrue(is1.hashCode() == is2.hashCode());
    }

    public final void testEquals() {
        is1 = new Copies(99);
        is2 = new Copies(99);
        assertTrue(is1.equals(is2));
    }

    public final void testEquals1() {
        is1 = new Copies(99);
        is2 = new NumberUp(99);
        assertFalse(is1.equals(is2));
        is2 = null;
        assertFalse(is1.equals(is2));
    }

    public final void testGetValue() {
        is1 = new PagesPerMinute(30);
        assertTrue(is1.getValue() == 30);
    }

    protected class integerSyntax extends IntegerSyntax {

        public integerSyntax(int value) {
            super(value);
        }

        public integerSyntax(int value, int lowerBound, int upperBound) {
            super(value, lowerBound, upperBound);
        }
    }
}
