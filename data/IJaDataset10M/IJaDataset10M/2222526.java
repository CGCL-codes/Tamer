package gov.nasa.jpf.delayed;

import gov.nasa.jpf.delayed.test.conf.NCPConfigurableTestJPF;
import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * JPF-level tests for AALOAD instruction.
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class TestNCPAALOADJPF extends NCPConfigurableTestJPF {

    private static String TEST_CLASS = TestNCPAALOAD.class.getName();

    public TestNCPAALOADJPF() {
        super(TEST_CLASS);
    }

    public static void main(String[] args) {
        JUnitCore.main(TEST_CLASS + "JPF");
    }

    /** ******************* tests ******************* */
    @Test
    public void testFiniteNullAALOAD() {
        setSearchClass("gov.nasa.jpf.search.DFSearch");
        runJPFnoException("testFiniteNullAALOAD");
        assertTestCounter(15);
    }

    @Test
    public void testFiniteAALOAD() {
        setSearchClass("gov.nasa.jpf.search.DFSearch");
        runJPFnoException("testFiniteAALOAD");
        assertTestCounter(5);
    }
}
