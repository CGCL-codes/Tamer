package test.debug.complex;

import junit.framework.Test;
import test.core.CoreTestSuite;
import test.debug.DebuggerTestSuite;

public class TestComplex extends DebuggerTestSuite {

    public TestComplex() {
        super(TestComplex.class);
    }

    public static Test suite() {
        return CoreTestSuite.suite(new TestComplex());
    }

    public static void main(String[] args) throws Exception {
        new TestComplex().run("testUpcallstack");
    }
}
