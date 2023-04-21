package joelib2.feature.types.count;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import tests.JOELib2TestsHelper;

public class NumberOfAtomsTest extends TestCase {

    public NumberOfAtomsTest(String name) {
        super(name);
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    public static Test suite() {
        return new TestSuite(NumberOfAtomsTest.class);
    }

    public void testFeature4Aspirin() {
        assertTrue("The calculated feature is not equal to the stored version.", JOELib2TestsHelper.instance().equalFeatureIn(NumberOfAtoms.getName(), "aspirin 2D deprotonated"));
    }
}
