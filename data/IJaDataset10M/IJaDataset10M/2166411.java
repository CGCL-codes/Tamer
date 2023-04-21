package gov.nasa.jpf.delayed;

import gov.nasa.jpf.delayed.test.conf.CPConfigurableTestJPF;
import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * JPF-level tests for the IFEQ instruction.
 * 
 * @author Milos Gligoric (milos.gligoric@gmail.com)
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 * 
 */
public class TestCPIFEQJPF extends CPConfigurableTestJPF {

    private static String TEST_CLASS = TestCPIFEQ.class.getName();

    public TestCPIFEQJPF() {
        super(TEST_CLASS);
    }

    public static void main(String[] args) {
        JUnitCore.main(TEST_CLASS + "JPF");
    }

    /** ******************* tests ******************* */
    @Test
    public void testFirstIFEQ() {
        runJPFnoException("testFirstIFEQ");
        assertTestCounter(4);
    }

    @Test
    public void testSecondIFEQ() {
        runJPFnoException("testSecondIFEQ");
        assertTestCounter(4);
    }

    @Test
    public void testBooleanNoArgIFEQ() {
        setFalseFirst("true");
        runJPFnoException("testBooleanNoArgIFEQ");
    }

    @Test
    public void testBooleanTrueArgIFEQ() {
        runJPFnoException("testBooleanTrueArgIFEQ");
    }
}
