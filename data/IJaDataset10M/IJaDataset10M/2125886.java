package test;

import java.text.DecimalFormat;
import exceptions.InvalidExpressionException;
import expressions.IEvaluator;
import expressions.MathEvaluator;
import junit.framework.TestCase;

/** A JUnit Test class to make sure that the MathEvaluator class is evaluating functions properly */
public class MathEvaluatorTest extends TestCase {

    /**Constructor */
    public MathEvaluatorTest(String name) {
        super(name);
    }

    /** A test of simple addition 
	 * @throws InvalidExpressionException 
	 * @throws NumberFormatException */
    public void testAddition() throws NumberFormatException, InvalidExpressionException {
        IEvaluator me = new MathEvaluator("1+1");
        double x = me.getValue();
        assertEquals(x, 2.0);
    }

    /** A test of simple subtraction 
	 * @throws InvalidExpressionException 
	 * @throws NumberFormatException */
    public void testSubtraction() throws NumberFormatException, InvalidExpressionException {
        IEvaluator me = new MathEvaluator("2-1");
        double x = me.getValue();
        assertEquals(x, 1.0);
    }

    /** A test of simple multiplication 
	 * @throws InvalidExpressionException 
	 * @throws NumberFormatException */
    public void testMultiplication() throws NumberFormatException, InvalidExpressionException {
        IEvaluator me = new MathEvaluator("2*3");
        double x = me.getValue();
        assertEquals(x, 6.0);
    }

    /** A test of simple division 
	 * @throws Exception */
    public void testDivision() throws Exception {
        IEvaluator me = new MathEvaluator("10/2");
        double x = me.getValue();
        assertEquals(x, 5.0);
        me.setExpression("1/0");
        x = me.getValue();
        assertEquals(x, Double.POSITIVE_INFINITY);
        me.setExpression("500/0");
        x = me.getValue();
        assertEquals(x, Double.POSITIVE_INFINITY);
        me.setExpression("-1/0");
        x = me.getValue();
        assertEquals(x, Double.NEGATIVE_INFINITY);
    }

    /**Tests the ^ operator 
	 * @throws InvalidExpressionException 
	 * @throws NumberFormatException */
    public void testPowers() throws NumberFormatException, InvalidExpressionException {
        IEvaluator me = new MathEvaluator("3^2");
        double x = me.getValue();
        assertEquals(x, 9.0);
    }

    /** Tests the % operator 
	 * @throws Exception */
    public void testModDiv() throws Exception {
        IEvaluator me = new MathEvaluator("3%2");
        double x = me.getValue();
        assertEquals(x, 1.0);
        me.setExpression("1%0");
        x = me.getValue();
        assertEquals(x, Double.NaN);
        me.setExpression("10%5");
        x = me.getValue();
        assertEquals(x, 0.0);
    }

    /** Tests & and | 
	 * @throws Exception */
    public void testBitwiseOps() throws Exception {
        IEvaluator me = new MathEvaluator("7&3");
        double x = me.getValue();
        assertEquals(x, 3.0);
        me.setExpression("8&3");
        x = me.getValue();
        assertEquals(x, 0.0);
        me.setExpression("7|3");
        x = me.getValue();
        assertEquals(x, 7.0);
        me.setExpression("8|3");
        x = me.getValue();
        assertEquals(x, 11.0);
    }

    /** Test the sqr() function 
	 * @throws InvalidExpressionException 
	 * @throws NumberFormatException */
    public void testSqr() throws NumberFormatException, InvalidExpressionException {
        IEvaluator me = new MathEvaluator("sqr(2)");
        double x = me.getValue();
        assertEquals(x, 4.0);
    }

    /** Test the sqrt() function 
	 * @throws InvalidExpressionException 
	 * @throws NumberFormatException */
    public void testSqrt() throws NumberFormatException, InvalidExpressionException {
        IEvaluator me = new MathEvaluator("sqrt(4)");
        double x = me.getValue();
        assertEquals(x, 2.0);
    }

    /** Test the Log-Base 10 function 
	 * @throws InvalidExpressionException 
	 * @throws NumberFormatException */
    public void testLog() throws NumberFormatException, InvalidExpressionException {
        IEvaluator me = new MathEvaluator("log(10)");
        double x = me.getValue();
        assertEquals(x, 1.0);
    }

    /**Test the natural log function 
	 * @throws InvalidExpressionException 
	 * @throws NumberFormatException */
    public void testLn() throws NumberFormatException, InvalidExpressionException {
        IEvaluator me = new MathEvaluator("ln(" + Math.E + ")");
        double x = me.getValue();
        assertEquals(x, 1.0);
    }

    /**Tests the order of operations 
	 * @throws Exception **/
    public void testArithmeticOrderOfOperations() throws Exception {
        IEvaluator me = new MathEvaluator("3+2*3");
        double x = me.getValue();
        assertEquals(x, 9.0);
        me.setExpression("3*2+3");
        x = me.getValue();
        assertEquals(x, 9.0);
        me.setExpression("3*(2+3)");
        x = me.getValue();
        assertEquals(x, 15.0);
        me.setExpression("3*(2+3)");
        x = me.getValue();
        assertEquals(x, 15.0);
        me.setExpression("2^(2+3)");
        x = me.getValue();
        assertEquals(x, 32.0);
        me.setExpression("3^2+3");
        x = me.getValue();
        assertEquals(x, 12.0);
    }

    /**Tests the various trig functions making sure they're precise to the 15th decimal point in degree format */
    public void testTriginDegrees() {
        DecimalFormat df = new DecimalFormat("#.###############");
        MathEvaluator me = new MathEvaluator();
        me.setAngleUnits(IEvaluator.DEGREES);
        me.setExpression("sin(30)");
        double x = me.getValue();
        assertEquals(df.format(x), df.format(0.5));
        me.setExpression("cos(60)");
        x = me.getValue();
        assertEquals(df.format(x), df.format(0.5));
        me.setExpression("tan(45)");
        x = me.getValue();
        assertEquals(df.format(x), df.format(1));
    }

    /**Tests the Arc Trig functions */
    public void testArcTriginDegrees() {
        DecimalFormat df = new DecimalFormat("#.###############");
        MathEvaluator me = new MathEvaluator();
        me.setAngleUnits(IEvaluator.DEGREES);
        me.setExpression("asin(0.5)");
        double x = me.getValue();
        assertEquals(df.format(x), df.format(30));
        me.setExpression("cos(0.5)");
        x = me.getValue();
        assertEquals(df.format(x), df.format(60));
        me.setExpression("tan(1)");
        x = me.getValue();
        assertEquals(df.format(x), df.format(45));
    }

    /**Tests the various trig functions making sure they're precise to the 15th decimal point in degree format */
    public void testTriginRadians() {
        DecimalFormat df = new DecimalFormat("#.###############");
        MathEvaluator me = new MathEvaluator();
        me.setAngleUnits(IEvaluator.RADIANS);
        me.setExpression("sin(" + (Math.PI / 6.0) + ")");
        double x = me.getValue();
        assertEquals(df.format(x), df.format(0.5));
        me.setExpression("cos(" + (Math.PI / 3.0) + ")");
        x = me.getValue();
        assertEquals(df.format(x), df.format(0.5));
        me.setExpression("tan(" + (Math.PI / 4.0) + ")");
        x = me.getValue();
        assertEquals(df.format(x), df.format(1));
    }

    /**Tests the Arc Trig functions in radians */
    public void testArcTriginRadians() {
        DecimalFormat df = new DecimalFormat("#.###############");
        MathEvaluator me = new MathEvaluator();
        me.setAngleUnits(IEvaluator.RADIANS);
        me.setExpression("asin(0.5)");
        double x = me.getValue();
        assertEquals(df.format(x), df.format(Math.PI / 6));
        me.setExpression("cos(0.5)");
        x = me.getValue();
        assertEquals(df.format(x), df.format(Math.PI / 3));
        me.setExpression("tan(1)");
        x = me.getValue();
        assertEquals(df.format(x), df.format(Math.PI / 4));
    }

    /** Tests the minimum and maximum functions 
	 * @throws Exception */
    public void testMinMax() throws Exception {
        IEvaluator me = new MathEvaluator("min(1,10)");
        double x = me.getValue();
        assertEquals(x, 1.0);
        me.setExpression("max(0.5,1)");
        x = me.getValue();
        assertEquals(x, 1.0);
    }

    /** Tests the exp(x) function which returns e^x 
	 * @throws Exception */
    public void testExp() throws Exception {
        DecimalFormat df = new DecimalFormat("#.#############");
        IEvaluator me = new MathEvaluator("exp(1)");
        double x = me.getValue();
        assertEquals(df.format(x), df.format(Math.E));
        me.setExpression("exp(2)");
        x = me.getValue();
        assertEquals(df.format(x), df.format(Math.pow(Math.E, 2.0)));
    }

    /** Test the floor and ceil functions 
	 * @throws Exception */
    public void testFloorAndCeil() throws Exception {
        IEvaluator me = new MathEvaluator("floor(2.99999999999)");
        double x = me.getValue();
        assertEquals(x, 2.0);
        me.setExpression("ceil(1.000000001)");
        x = me.getValue();
        assertEquals(x, 2.0);
    }

    /** Test the abs() function 
	 * @throws Exception */
    public void testAbs() throws Exception {
        IEvaluator me = new MathEvaluator("abs(2)");
        double x = me.getValue();
        assertEquals(x, 2.0);
        me.setExpression("abs(-2.0)");
        x = me.getValue();
        assertEquals(x, 2.0);
    }

    /** Test the neg() function 
	 * @throws Exception */
    public void testNeg() throws Exception {
        IEvaluator me = new MathEvaluator("neg(2)");
        double x = me.getValue();
        assertEquals(x, -2.0);
        me.setExpression("neg(-2.0)");
        x = me.getValue();
        assertEquals(x, 2.0);
    }

    /** Test the rnd() function 
	 * @throws InvalidExpressionException 
	 * @throws NumberFormatException */
    public void testRandom() throws NumberFormatException, InvalidExpressionException {
        IEvaluator me = new MathEvaluator("rnd(2.3)");
        double x = me.getValue();
        double y = me.getValue();
        assertFalse(x == y);
    }

    /**Test Order of Operations with functions involved */
    public void testOrderOfOperationsWithFunctions() {
        DecimalFormat df = new DecimalFormat("#.###############");
        MathEvaluator me = new MathEvaluator("sin(2*10+10)");
        me.setAngleUnits(IEvaluator.DEGREES);
        double x = me.getValue();
        assertEquals(df.format(x), df.format(0.5));
        me.setExpression("sin(neg((2^1-12^0-11)*3))");
        x = me.getValue();
        assertEquals(df.format(x), df.format(0.5));
    }

    /**Tests the evaluators ability to handle variables 
	 * @throws Exception */
    public void testVariables() throws Exception {
        IEvaluator me = new MathEvaluator("x");
        me.addVariable("x", 15.0);
        double x = me.getValue();
        assertEquals(x, 15.0);
        me.setExpression("3+x");
        x = me.getValue();
        assertEquals(x, 18.0);
    }

    /**Tests implicit multiplications 
	 * @throws Exception */
    public void testImplicitMultiplication() throws Exception {
        IEvaluator me = new MathEvaluator("3(2+3)");
        double x = me.getValue();
        assertEquals(x, 15.0);
        me.setExpression("(2-1)(1+1)");
        x = me.getValue();
        assertEquals(x, 2.0);
        me.setExpression("3x");
        me.addVariable("x", 15.0);
        x = me.getValue();
        assertEquals(x, 45.0);
    }
}
