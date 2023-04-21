package gov.nasa.jpf.jvm;

import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * JPF driver or method invocation test
 */
public class TestMethodJPF extends TestJPF {

    static final String TEST_CLASS = "gov.nasa.jpf.jvm.TestMethod";

    public static void main(String[] args) {
        JUnitCore.main("gov.nasa.jpf.jvm.TestMethodJPF");
    }

    /**************************** tests **********************************/
    @Test
    public void testCtor() {
        String[] args = { TEST_CLASS, "testCtor" };
        runJPFnoException(args);
    }

    @Test
    public void testCall() {
        String[] args = { TEST_CLASS, "testCall" };
        runJPFnoException(args);
    }

    @Test
    public void testInheritedCall() {
        String[] args = { TEST_CLASS, "testInheritedCall" };
        runJPFnoException(args);
    }

    @Test
    public void testVirtualCall() {
        String[] args = { TEST_CLASS, "testVirtualCall" };
        runJPFnoException(args);
    }

    @Test
    public void testCallAcrossGC() {
        String[] args = { TEST_CLASS, "testCallAcrossGC" };
        runJPFnoException(args);
    }

    @Test
    public void testSpecialCall() {
        String[] args = { TEST_CLASS, "testSpecialCall" };
        runJPFnoException(args);
    }

    @Test
    public void testStaticCall() {
        String[] args = { TEST_CLASS, "testStaticCall" };
        runJPFnoException(args);
    }
}
