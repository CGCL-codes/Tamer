package calclipse.lib.math.util.diff.diffs;

import static calclipse.lib.math.mp.MPConstants.ADD;
import static calclipse.lib.math.mp.MPConstants.DIVIDE;
import static calclipse.lib.math.mp.MPConstants.MULTIPLY;
import static calclipse.lib.math.mp.MPConstants.NEGATE;
import static calclipse.lib.math.mp.MPConstants.POW;
import static calclipse.lib.math.mp.MPConstants.SUBTRACT;
import static calclipse.lib.math.util.diff.DiffConstants.MINUS_TWO;
import static calclipse.lib.math.util.diff.DiffConstants.ONE;
import static calclipse.lib.math.util.diff.DiffConstants.THREE;
import static calclipse.lib.math.util.diff.DiffConstants.TWO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import calclipse.lib.math.rpn.Expression;
import calclipse.lib.math.rpn.Fragment;
import calclipse.lib.math.rpn.RPNException;
import calclipse.lib.math.rpn.Variable;
import calclipse.lib.math.util.diff.Differentiator;

public class BasicDiffTest {

    private static final Variable X = new Variable("x", 0.0);

    private static final Differentiator DIFF = new Differentiator(new BasicDiff());

    public BasicDiffTest() {
    }

    @Test
    public void testAdd1() throws RPNException {
        final Fragment f1 = new Fragment(ONE, 0);
        final Fragment f2 = new Fragment(X, 10);
        final Fragment f3 = new Fragment(ADD, 20);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 1, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
    }

    @Test
    public void testAdd2() throws RPNException {
        final Fragment f1 = new Fragment(X, 0);
        final Fragment f2 = new Fragment(TWO, 10);
        final Fragment f3 = new Fragment(ADD, 20);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 1, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
    }

    @Test
    public void testAdd3() throws RPNException {
        final Fragment f1 = new Fragment(X, 0);
        final Fragment f2 = new Fragment(ONE, 10);
        final Fragment f3 = new Fragment(ADD, 20);
        final Fragment f4 = new Fragment(TWO, 30);
        final Fragment f5 = new Fragment(X, 40);
        final Fragment f6 = new Fragment(ADD, 50);
        final Fragment f7 = new Fragment(ADD, 60);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        e1.add(f4);
        e1.add(f5);
        e1.add(f6);
        e1.add(f7);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 3, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
        assertSame("3", ONE, e2.get(1).getToken());
        assertSame("4", ADD, e2.get(2).getToken());
    }

    @Test
    public void testSubtract1() throws RPNException {
        final Fragment f1 = new Fragment(ONE, 0);
        final Fragment f2 = new Fragment(X, 10);
        final Fragment f3 = new Fragment(SUBTRACT, 20);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 2, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
        assertSame("3", NEGATE, e2.get(1).getToken());
    }

    @Test
    public void testSubtract2() throws RPNException {
        final Fragment f1 = new Fragment(X, 0);
        final Fragment f2 = new Fragment(TWO, 10);
        final Fragment f3 = new Fragment(SUBTRACT, 20);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 1, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
    }

    @Test
    public void testSubtract3() throws RPNException {
        final Fragment f1 = new Fragment(X, 0);
        final Fragment f2 = new Fragment(ONE, 10);
        final Fragment f3 = new Fragment(SUBTRACT, 20);
        final Fragment f4 = new Fragment(TWO, 30);
        final Fragment f5 = new Fragment(X, 40);
        final Fragment f6 = new Fragment(SUBTRACT, 50);
        final Fragment f7 = new Fragment(SUBTRACT, 60);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        e1.add(f4);
        e1.add(f5);
        e1.add(f6);
        e1.add(f7);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 4, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
        assertSame("3", ONE, e2.get(1).getToken());
        assertSame("4", NEGATE, e2.get(2).getToken());
        assertSame("5", SUBTRACT, e2.get(3).getToken());
    }

    @Test
    public void testMultiply1() throws RPNException {
        final Fragment f1 = new Fragment(ONE, 0);
        final Fragment f2 = new Fragment(X, 10);
        final Fragment f3 = new Fragment(MULTIPLY, 20);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 1, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
    }

    @Test
    public void testMultiply2() throws RPNException {
        final Fragment f1 = new Fragment(ONE, 0);
        final Fragment f2 = new Fragment(TWO, 10);
        final Fragment f3 = new Fragment(X, 30);
        final Fragment f4 = new Fragment(SUBTRACT, 40);
        final Fragment f5 = new Fragment(MULTIPLY, 50);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        e1.add(f4);
        e1.add(f5);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 4, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
        assertSame("3", ONE, e2.get(1).getToken());
        assertSame("4", NEGATE, e2.get(2).getToken());
        assertSame("5", MULTIPLY, e2.get(3).getToken());
    }

    @Test
    public void testMultiply3() throws RPNException {
        final Fragment f1 = new Fragment(ONE, 0);
        final Fragment f2 = new Fragment(X, 10);
        final Fragment f3 = new Fragment(SUBTRACT, 30);
        final Fragment f4 = new Fragment(TWO, 40);
        final Fragment f5 = new Fragment(MULTIPLY, 50);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        e1.add(f4);
        e1.add(f5);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 4, e2.size());
        assertSame("2", TWO, e2.get(0).getToken());
        assertSame("3", ONE, e2.get(1).getToken());
        assertSame("4", NEGATE, e2.get(2).getToken());
        assertSame("5", MULTIPLY, e2.get(3).getToken());
    }

    @Test
    public void testMultiply4() throws RPNException {
        final Fragment f1 = new Fragment(TWO, 0);
        final Fragment f2 = new Fragment(X, 10);
        final Fragment f3 = new Fragment(MULTIPLY, 20);
        final Fragment f4 = new Fragment(THREE, 30);
        final Fragment f5 = new Fragment(X, 40);
        final Fragment f6 = new Fragment(MULTIPLY, 50);
        final Fragment f7 = new Fragment(MULTIPLY, 60);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        e1.add(f4);
        e1.add(f5);
        e1.add(f6);
        e1.add(f7);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 11, e2.size());
        assertSame("2", TWO, e2.get(0).getToken());
        assertSame("3", X, e2.get(1).getToken());
        assertSame("4", MULTIPLY, e2.get(2).getToken());
        assertSame("5", THREE, e2.get(3).getToken());
        assertSame("6", MULTIPLY, e2.get(4).getToken());
        assertSame("7", THREE, e2.get(5).getToken());
        assertSame("8", X, e2.get(6).getToken());
        assertSame("9", MULTIPLY, e2.get(7).getToken());
        assertSame("10", TWO, e2.get(8).getToken());
        assertSame("11", MULTIPLY, e2.get(9).getToken());
        assertSame("12", ADD, e2.get(10).getToken());
    }

    @Test
    public void testNegate() throws RPNException {
        final Fragment f1 = new Fragment(TWO, 0);
        final Fragment f2 = new Fragment(X, 10);
        final Fragment f3 = new Fragment(MULTIPLY, 20);
        final Fragment f4 = new Fragment(NEGATE, 30);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        e1.add(f4);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 2, e2.size());
        assertSame("2", TWO, e2.get(0).getToken());
        assertSame("3", NEGATE, e2.get(1).getToken());
    }

    @Test
    public void testDivide1() throws RPNException {
        final Fragment f1 = new Fragment(ONE, 0);
        final Fragment f2 = new Fragment(TWO, 10);
        final Fragment f3 = new Fragment(ADD, 20);
        final Fragment f4 = new Fragment(X, 30);
        final Fragment f5 = new Fragment(NEGATE, 40);
        final Fragment f6 = new Fragment(DIVIDE, 50);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        e1.add(f4);
        e1.add(f5);
        e1.add(f6);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 12, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
        assertSame("3", TWO, e2.get(1).getToken());
        assertSame("4", ADD, e2.get(2).getToken());
        assertSame("5", NEGATE, e2.get(3).getToken());
        assertSame("6", X, e2.get(4).getToken());
        assertSame("7", NEGATE, e2.get(5).getToken());
        assertSame("8", MINUS_TWO, e2.get(6).getToken());
        assertSame("9", POW, e2.get(7).getToken());
        assertSame("10", MULTIPLY, e2.get(8).getToken());
        assertSame("11", ONE, e2.get(9).getToken());
        assertSame("12", NEGATE, e2.get(10).getToken());
        assertSame("13", MULTIPLY, e2.get(11).getToken());
    }

    @Test
    public void testDivide2() throws RPNException {
        final Fragment f1 = new Fragment(X, 0);
        final Fragment f2 = new Fragment(NEGATE, 10);
        final Fragment f3 = new Fragment(ONE, 20);
        final Fragment f4 = new Fragment(TWO, 30);
        final Fragment f5 = new Fragment(ADD, 40);
        final Fragment f6 = new Fragment(DIVIDE, 50);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        e1.add(f4);
        e1.add(f5);
        e1.add(f6);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 6, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
        assertSame("3", NEGATE, e2.get(1).getToken());
        assertSame("4", ONE, e2.get(2).getToken());
        assertSame("5", TWO, e2.get(3).getToken());
        assertSame("6", ADD, e2.get(4).getToken());
        assertSame("7", DIVIDE, e2.get(5).getToken());
    }

    @Test
    public void testDivide3() throws RPNException {
        final Fragment f1 = new Fragment(X, 0);
        final Fragment f2 = new Fragment(NEGATE, 10);
        final Fragment f3 = new Fragment(X, 20);
        final Fragment f4 = new Fragment(NEGATE, 30);
        final Fragment f5 = new Fragment(DIVIDE, 40);
        final Expression e1 = new Expression();
        e1.add(f1);
        e1.add(f2);
        e1.add(f3);
        e1.add(f4);
        e1.add(f5);
        final Expression e2 = DIFF.differentiate(e1, "x");
        assertEquals("1", 16, e2.size());
        assertSame("2", ONE, e2.get(0).getToken());
        assertSame("3", NEGATE, e2.get(1).getToken());
        assertSame("4", X, e2.get(2).getToken());
        assertSame("5", NEGATE, e2.get(3).getToken());
        assertSame("6", MULTIPLY, e2.get(4).getToken());
        assertSame("7", X, e2.get(5).getToken());
        assertSame("8", NEGATE, e2.get(6).getToken());
        assertSame("9", ONE, e2.get(7).getToken());
        assertSame("10", NEGATE, e2.get(8).getToken());
        assertSame("11", MULTIPLY, e2.get(9).getToken());
        assertSame("12", SUBTRACT, e2.get(10).getToken());
        assertSame("13", X, e2.get(11).getToken());
        assertSame("14", NEGATE, e2.get(12).getToken());
        assertSame("15", MINUS_TWO, e2.get(13).getToken());
        assertSame("16", POW, e2.get(14).getToken());
        assertSame("17", MULTIPLY, e2.get(15).getToken());
    }
}
