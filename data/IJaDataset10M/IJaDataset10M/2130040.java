package gov.nasa.jpf.delayed;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import gov.nasa.jpf.delayed.test.conf.CPConfigurableTestJPF;

/**
 * JPF-level tests for the GETFIELD instruction.
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class TestCPGETFIELDJPF extends CPConfigurableTestJPF {

    public static final String TEST_CLASS = TestCPGETFIELD.class.getName();

    public TestCPGETFIELDJPF() {
        super(TEST_CLASS);
    }

    public static void main(String[] args) {
        JUnitCore.main(TEST_CLASS + "JPF");
    }

    /** ******************* tests ******************* */
    @Test
    public void testGetAnyInfiniteGETFIELD() {
        setSearchClass("gov.nasa.jpf.search.DFSearch");
        runJPFnoException("testGetAnyInfiniteGETFIELD");
        assertTestCounter(2);
    }

    @Test
    public void testGetNewFiniteGETFIELD() {
        setSearchClass("gov.nasa.jpf.search.DFSearch");
        runJPFnoException("testGetNewFiniteGETFIELD");
        assertTestCounter(3);
    }

    @Test
    public void testMethodGetAnyFiniteGETFIELD() {
        runJPFnoException("testMethodGetAnyFiniteGETFIELD");
    }

    @Test
    public void testMethodCallUninitializedObjectGETFIELD() {
        runJPFnoException("testMethodCallUninitializedObjectGETFIELD");
        assertTestCounter(2);
    }
}
