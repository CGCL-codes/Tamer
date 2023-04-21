package uk.org.ogsadai.dqp.lqp.udf.scalar;

import java.math.BigDecimal;
import junit.framework.TestCase;
import uk.org.ogsadai.dqp.lqp.udf.LogicalExecutableFunctionBase;
import uk.org.ogsadai.tuple.TupleTypes;
import uk.org.ogsadai.tuple.TypeMismatchException;

/**
 * Unit test class for Mod function.
 * 
 * @author The OGSA-DAI Project Team
 */
public class ModTest extends TestCase {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2010.";

    /**
     * @param name
     */
    public ModTest(String name) {
        super(name);
    }

    /**
     * Test mod where both inputs are of the same type.
     * 
     * @throws Exception
     */
    public void testSameTypeInput() throws Exception {
        LogicalExecutableFunctionBase mod = new Mod();
        Object[] x = { new Short("5"), new Integer(95), new Long(50), new Float(5.9), new Double(14.5), new BigDecimal(67) };
        Object[] y = { new Short("2"), new Integer(40), new Long(20), new Float(2), new Double(5), new BigDecimal(30) };
        int[] type = { TupleTypes._SHORT, TupleTypes._INT, TupleTypes._LONG, TupleTypes._FLOAT, TupleTypes._DOUBLE, TupleTypes._BIGDECIMAL };
        int[] outputType = { TupleTypes._SHORT, TupleTypes._INT, TupleTypes._LONG, TupleTypes._FLOAT, TupleTypes._DOUBLE, TupleTypes._BIGDECIMAL };
        Object[] expectedResult = { new Integer(5 % 2), new Integer(95 % 40), new Long(50L % 20L), new Float(5.9F % 2F), new Double(14.5D % 5D), new BigDecimal(67).remainder(new BigDecimal(30)) };
        for (int i = 0; i < x.length; i++) {
            mod.configure(new int[] { type[i], type[i] });
            mod.put(new Object[] { x[i], y[i] });
            assertEquals(outputType[i], mod.getOutputType());
            assertEquals(expectedResult[i], mod.getResult());
        }
    }

    /**
     * Test mod where inputs are of different type.
     * 
     * @throws Exception
     */
    public void testDifferentTypeInputs() throws Exception {
        LogicalExecutableFunctionBase mod = new Mod();
        Object[] x = { new Short("5"), new Integer(95), new Long(50), new Float(5.9), new Double(14.5), new BigDecimal(67) };
        Object[] y = { new Double(2), new BigDecimal(40), new Short("20"), new Integer(2), new BigDecimal(5), new Long(30) };
        int[] typeX = { TupleTypes._SHORT, TupleTypes._INT, TupleTypes._LONG, TupleTypes._FLOAT, TupleTypes._DOUBLE, TupleTypes._BIGDECIMAL };
        int[] typeY = { TupleTypes._DOUBLE, TupleTypes._BIGDECIMAL, TupleTypes._SHORT, TupleTypes._INT, TupleTypes._BIGDECIMAL, TupleTypes._LONG };
        int[] outputType = { TupleTypes._DOUBLE, TupleTypes._BIGDECIMAL, TupleTypes._LONG, TupleTypes._FLOAT, TupleTypes._BIGDECIMAL, TupleTypes._BIGDECIMAL };
        Object[] expectedResult = { new Double(new Short("5") % 2D), new BigDecimal(95).remainder(new BigDecimal(40)), new Long(50L % new Short("20")), new Float(5.9F % 2), new BigDecimal(14.5D).remainder(new BigDecimal(5)), new BigDecimal(67).remainder(new BigDecimal(30L)) };
        for (int i = 0; i < x.length; i++) {
            mod.configure(new int[] { typeX[i], typeY[i] });
            mod.put(new Object[] { x[i], y[i] });
            assertEquals(outputType[i], mod.getOutputType());
            assertEquals(expectedResult[i], mod.getResult());
        }
    }

    public void testInvalidConfiguration() throws Exception {
        LogicalExecutableFunctionBase mod = new Mod();
        int invalidDataType = -1;
        try {
            mod.configure(new int[] { TupleTypes._INT, invalidDataType });
            mod.put(new Object[] { 1, 1 });
            fail("TypeMismatchException expected for an invalid data type of " + invalidDataType);
        } catch (TypeMismatchException e) {
        }
    }

    /**
     * Test with invalid input type.
     * 
     * @throws Exception
     */
    public void testInvalidInput() throws Exception {
        LogicalExecutableFunctionBase mod = new Mod();
        try {
            mod.configure(new int[] { TupleTypes._SHORT, TupleTypes._SHORT });
            mod.put(new Object[] { new BigDecimal(1), 1D });
            fail("ClassCastException expected for an invalid input.");
        } catch (ClassCastException e) {
        }
    }

    /**
     * Test with null input.
     * 
     * @throws Exception
     */
    public void testNullInput() throws Exception {
        LogicalExecutableFunctionBase mod = new Mod();
        try {
            mod.configure(new int[] { TupleTypes._INT, TupleTypes._INT });
            mod.put(new Object[] { 1 });
            fail("RuntimeException expected for null input.");
        } catch (RuntimeException e) {
        }
    }

    /**
     * Test constructor.
     * 
     * @throws Exception
     */
    public void testCopyConstructor() throws Exception {
        LogicalExecutableFunctionBase mod = new Mod();
        Object[] x = { new Short("5"), new Integer(95), new Long(50), new Float(5.9), new Double(14.5), new BigDecimal(67) };
        Object[] y = { new Double(2), new BigDecimal(40), new Short("20"), new Integer(2), new BigDecimal(5), new Long(30) };
        int[] typeX = { TupleTypes._SHORT, TupleTypes._INT, TupleTypes._LONG, TupleTypes._FLOAT, TupleTypes._DOUBLE, TupleTypes._BIGDECIMAL };
        int[] typeY = { TupleTypes._DOUBLE, TupleTypes._BIGDECIMAL, TupleTypes._SHORT, TupleTypes._INT, TupleTypes._BIGDECIMAL, TupleTypes._LONG };
        int[] outputType = { TupleTypes._DOUBLE, TupleTypes._BIGDECIMAL, TupleTypes._LONG, TupleTypes._FLOAT, TupleTypes._BIGDECIMAL, TupleTypes._BIGDECIMAL };
        Object[] expectedResult = { new Double(new Short("5") % 2D), new BigDecimal(95).remainder(new BigDecimal(40)), new Long(50L % new Short("20")), new Float(5.9F % 2), new BigDecimal(14.5D).remainder(new BigDecimal(5)), new BigDecimal(67).remainder(new BigDecimal(30L)) };
        for (int i = 0; i < x.length; i++) {
            mod.configure(new int[] { typeX[i], typeY[i] });
            Mod copiedMod = new Mod((Mod) mod);
            copiedMod.put(new Object[] { x[i], y[i] });
            assertEquals(outputType[i], copiedMod.getOutputType());
            assertEquals(expectedResult[i], copiedMod.getResult());
        }
    }
}
