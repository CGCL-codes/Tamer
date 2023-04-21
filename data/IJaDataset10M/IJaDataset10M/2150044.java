package gov.nasa.jpf.jvm;

import org.junit.Test;
import org.junit.runner.JUnitCore;

public class TestSemaphoreJPF extends TestJPF {

    public static void main(String args[]) {
        JUnitCore.main("gov.nasa.jpf.jvm.TestSemaphoreJPF");
    }

    /**************************** tests **********************************/
    @Test
    public void testCast() {
        String[] args = { "gov.nasa.jpf.jvm.TestSemaphore", "testResourceAcquisition" };
        runJPFnoException(args);
    }
}
