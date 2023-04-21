package sketch.ounit.fuzz;

import sketch.ounit.Values;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ExistingObjectFuzzerExceptionThrowingTest extends TestCase {

    public static Test suite() {
        return new TestSuite(ExistingObjectFuzzerExceptionThrowingTest.class);
    }

    public void setUp() {
        ExistingObjectFuzzer.setTimeLimit(2);
    }

    public void testFuzzingExistingObject() {
        System.out.println();
        ExistingObjectFuzzer.print_sequence = true;
        System.out.println("test testFuzzingExistingObject....");
        for (int i = 0; i < 5; i++) {
            System.out.println("The " + i + "-th round in creating test ... ");
            ExceptionThrowingCode code = new ExceptionThrowingCode();
            Values.randomFuzz(code, ExceptionThrowingCode.class);
            code.openFlag();
            Values.randomFuzz(code, ExceptionThrowingCode.class);
            code.closeFlag();
            System.out.println();
            System.out.println();
        }
        ExistingObjectFuzzer.print_sequence = false;
    }
}
