package org.modyna.modyna.simulation.integration;

import junit.framework.TestCase;
import org.modyna.modyna.simulation.MockHareFox;
import org.jfree.data.XYSeriesCollection;

/**
 * Testcase to test the Integrator class
 * 
 * Integrate the two functions
 * y_1(t) = t^2 and y_2(t) = t^2
 * in the interval 0.0 and 3.0
 * where
 * y_1(t=0)=2.0 and y_2(t=0)=3.0
 * The result should be of course:
 * int(y_1) = 11.0 and int(y_2) = 12.0
 * @author  Rupert
 */
public class IntegratorTest extends TestCase {

    /** Creates a new instance of IntegratorTest */
    public IntegratorTest(java.lang.String testName) {
        super(testName);
    }

    public void testSimpleUncoupledODEs() {
        DoubleVector yStart = new DoubleVector();
        yStart.add(2.0);
        yStart.add(3.0);
        MockXSquare mockXSquare = new MockXSquare();
        Integrator integrator = new Integrator();
        try {
            XYSeriesCollection xyc = integrator.driveIntegrationOld(yStart, 0.0, 3.0, 0.0001, mockXSquare);
            assertEquals(0.0, xyc.getXValue(0, 0).doubleValue(), 0.0);
            assertEquals(2.0, xyc.getYValue(0, 0).doubleValue(), 0.0);
            assertEquals(0.0, xyc.getXValue(1, 0).doubleValue(), 0.0);
            assertEquals(3.0, xyc.getYValue(1, 0).doubleValue(), 0.0);
            int lastIndex = xyc.getItemCount(0);
            assertEquals(3.0, xyc.getXValue(0, lastIndex - 1).doubleValue(), 0.0);
            assertEquals(11.0, xyc.getYValue(0, lastIndex - 1).doubleValue(), 0.0);
            assertEquals(3.0, xyc.getXValue(1, lastIndex - 1).doubleValue(), 0.0);
            assertEquals(12.0, xyc.getYValue(1, lastIndex - 1).doubleValue(), 0.0);
        } catch (NumericsException ne) {
            fail("numeric exception in driving the integration: " + ne.getMessage());
        }
    }

    /**
 * This test case's objective is to test whether the integration routine works correctly, when
 * the starting point is greater than the end point of the integration.
 * 
 * We use a simple set of equation and first integrate from the smalle value to the bigger 
 * value. We then reverse the direction of integration and we expect that the value of the
 * integral changes its name.
 */
    public void testIntegrationFromGreaterToSmaller() {
        DoubleVector yStart = new DoubleVector();
        yStart.add(2.0);
        yStart.add(3.0);
        MockXSquare mockXSquare = new MockXSquare();
        Integrator integrator = new Integrator();
        try {
            XYSeriesCollection xycFirstIntegration = integrator.driveIntegrationOld(yStart, 0.0, 3.0, 0.0001, mockXSquare);
            int lastIndex = xycFirstIntegration.getItemCount(0);
            double integralFirstEqnFirstIntegration = xycFirstIntegration.getYValue(0, lastIndex - 1).doubleValue();
            double integralSecondEqnFirstIntegration = xycFirstIntegration.getYValue(1, lastIndex - 1).doubleValue();
            XYSeriesCollection xycSecondIntegration = integrator.driveIntegrationOld(yStart, 3.0, 0.0, 0.0001, mockXSquare);
            double integralFirstEqnSecondIntegration = xycSecondIntegration.getYValue(0, 0).doubleValue();
            double integralSecondEqnSecondIntegration = xycSecondIntegration.getYValue(1, 0).doubleValue();
            assertEquals(integralFirstEqnFirstIntegration, -integralFirstEqnSecondIntegration + 4.0, 0.0001);
            assertEquals(integralSecondEqnFirstIntegration, -integralSecondEqnSecondIntegration + 6.0, 0.0001);
        } catch (NumericsException ne) {
            fail("numeric exception in driving the integration: " + ne.getMessage());
        }
    }

    /**
 * This test case's objective is to check whether the example dynamic
 * system (hunter prey Volterra like) is integrated correctly.
 * 
 * Another objective is to check whether model auxiliaries 
 * other than level are correctly stored in the output of the integrator.
 */
    public void testHareFoxIntegration() {
        DoubleVector yStart = new DoubleVector();
        yStart.add(2.0);
        yStart.add(2500.0);
        MockHareFox mockHareFox = new MockHareFox();
        Integrator integrator = new Integrator();
        try {
            XYSeriesCollection xycIntegration = integrator.driveIntegrationOld(yStart, 0.0, 500.0, 0.1, mockHareFox);
            int lastIndex = xycIntegration.getItemCount(0);
            double tEnd = xycIntegration.getXValue(0, lastIndex - 1).doubleValue();
            assertEquals(tEnd, 500, 0.0);
            double foxTEnd = xycIntegration.getYValue(0, lastIndex - 1).doubleValue();
            assertEquals(25.6, foxTEnd, 0.1);
            double hareTEnd = xycIntegration.getYValue(4, lastIndex - 1).doubleValue();
            assertEquals(500.0, hareTEnd, 0.1);
            double foxFindsHareTIntermediate = xycIntegration.getYValue(1, 25 - 1).doubleValue();
            double foxShrinkTIntermediate = xycIntegration.getYValue(2, 25 - 1).doubleValue();
            double freeMawnCapacityTIntermediate = xycIntegration.getYValue(3, 25 - 1).doubleValue();
            double hareGrowthTIntermediate = xycIntegration.getYValue(5, 25 - 1).doubleValue();
            double foxTIntermediateInput = xycIntegration.getYValue(0, 25 - 1).doubleValue();
            double hareTIntermediateInput = xycIntegration.getYValue(4, 25 - 1).doubleValue();
            double foxTIntermediate = xycIntegration.getYValue(0, 26 - 1).doubleValue();
            double hareTIntermediate = xycIntegration.getYValue(4, 26 - 1).doubleValue();
            assertEquals(17745.91, foxFindsHareTIntermediate, 0.1);
            assertEquals(10.25, foxShrinkTIntermediate, 0.1);
            assertEquals(1053.94, freeMawnCapacityTIntermediate, 0.1);
            assertEquals(20.84, hareGrowthTIntermediate, 0.1);
            assertEquals(51.28, foxTIntermediateInput, 0.1);
            assertEquals(346.05, hareTIntermediateInput, 0.1);
            assertEquals(33.69, foxTIntermediate, 0.1);
            assertEquals(303.59, hareTIntermediate, 0.1);
        } catch (NumericsException ne) {
            fail("numeric exception in driving the integration: " + ne.getMessage());
        }
    }
}
