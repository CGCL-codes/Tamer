package net.aa3sd.SMT.test;

import java.util.ArrayList;
import net.aa3sd.SMT.math.BadCalculationException;
import net.aa3sd.SMT.math.PODCalculator;
import junit.framework.TestCase;

/**
 * @author mole
 *
 */
public class testPODCalculator extends TestCase {

    /**
	 * @param name
	 */
    public testPODCalculator(String name) {
        super(name);
    }

    /**
	 * Test method for {@link net.aa3sd.SMT.math.PODCalculator#calcululateCumulativePOD(java.util.ArrayList)}.
	 */
    public void testCalcululateCumulativePOD() {
        ArrayList searchesOfSameSegment = new ArrayList<Double>();
        searchesOfSameSegment.add(Double.valueOf(0.3d));
        searchesOfSameSegment.add(Double.valueOf(0.3d));
        searchesOfSameSegment.add(Double.valueOf(0.7d));
        try {
            assertEquals(0.853d, PODCalculator.calcululateCumulativePOD(searchesOfSameSegment));
        } catch (BadCalculationException e) {
            fail("Threw unexpected exception");
        }
        searchesOfSameSegment.clear();
        searchesOfSameSegment.add(Double.valueOf(0.05d));
        searchesOfSameSegment.add(Double.valueOf(0.05d));
        try {
            assertEquals(Math.round(1000000d * 0.0975d), Math.round(1000000d * PODCalculator.calcululateCumulativePOD(searchesOfSameSegment)));
        } catch (BadCalculationException e) {
            fail("Threw unexpected exception");
        }
        searchesOfSameSegment.clear();
        searchesOfSameSegment.add(Double.valueOf(0.5d));
        searchesOfSameSegment.add(Double.valueOf(0.25d));
        try {
            assertEquals(0.625d, PODCalculator.calcululateCumulativePOD(searchesOfSameSegment));
        } catch (BadCalculationException e) {
            fail("Threw unexpected exception");
        }
    }

    public void testCalculateCumulativePODExceptions() {
        ArrayList searchesOfSameSegment = new ArrayList<Double>();
        searchesOfSameSegment.add(Double.valueOf(0.3d));
        searchesOfSameSegment.add(Double.valueOf(-0.3d));
        searchesOfSameSegment.add(Double.valueOf(0.7d));
        try {
            assertEquals(0.85d, PODCalculator.calcululateCumulativePOD(searchesOfSameSegment));
            fail("Failed to throw exception on probability less than zero.");
        } catch (BadCalculationException e) {
        }
        searchesOfSameSegment.clear();
        searchesOfSameSegment.add(Double.valueOf(0.3d));
        searchesOfSameSegment.add(Double.valueOf(30d));
        searchesOfSameSegment.add(Double.valueOf(0.7d));
        try {
            assertEquals(0.85d, PODCalculator.calcululateCumulativePOD(searchesOfSameSegment));
            fail("Failed to throw exception on probability more than one.");
        } catch (BadCalculationException e) {
        }
    }
}
