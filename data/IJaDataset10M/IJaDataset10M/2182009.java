package gov.nasa.jpf.jvm;

import org.junit.Test;
import org.junit.runner.JUnitCore;

public class TestEnumJPF extends TestJPF {

    static final String TEST_CLASS = "gov.nasa.jpf.jvm.TestEnum";

    public static void main(String[] args) {
        JUnitCore.main(TEST_CLASS + "JPF");
    }

    /**************************** tests **********************************/
    @Test
    public void testValueOf() {
        String[] args = { TEST_CLASS, "testValueOf" };
        runJPFnoException(args);
    }
}
