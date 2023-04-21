package tests.samples;

import org.punit.runner.*;
import tests.api.org.punit.testclasses.*;

public class PUnitTestSuiteSample {

    public static void main(String[] args) {
        new SoloRunner().run(TestSuiteClass.class);
        new ConcurrentRunner().run(TestSuiteClass.class);
    }
}
