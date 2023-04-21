package org.josef.test.science.math;

import static org.josef.annotations.Status.Stage.PRODUCTION;
import org.josef.annotations.Review;
import org.josef.annotations.Status;
import org.josef.test.science.math.geometry.GeometryTestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * JUnit Test Suite for the math package.
 * @author Kees Schotanus
 * @version 1.1 $Revision: 3145 $
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ GeometryTestSuite.class, BisectionTest.class, CBigDecimalTest.class, CBigIntegerTest.class, CBooleanTest.class, CByteTest.class, CDoubleTest.class, CFloatTest.class, CIntegerTest.class, CLongTest.class, CombinationsTest.class, ComplexTest.class, CRationalTest.class, CShortTest.class, CStatisticsTest.class, FunctionIntervalTest.class, ModuloCheckTest.class, QuadraticEquationTest.class, PrimeFactorTest.class, RegulaFalsiTest.class, RomanNumbersTest.class })
@Status(stage = PRODUCTION)
@Review(by = "Kees Schotanus", at = "2009-09-28")
public final class MathTestSuite {

    /**
     * Private constructor prevents creation of an instance outside this class.
     */
    private MathTestSuite() {
    }

    /**
     * Simple main method to run/debug tests using text mode version of JUnit.
     * @param args Not used.
     */
    public static void main(final String[] args) {
        org.junit.runner.JUnitCore.runClasses(MathTestSuite.class);
    }
}
