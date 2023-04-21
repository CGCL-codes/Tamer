package org.yactu.pyjunit;

import org.yactu.pyjunit.PythonExecTestSuite;
import junit.framework.Test;
import java.io.File;

public class SamplePythonTestSuite extends PythonExecTestSuite {

    /**
     * This class wrapp a python test module in a JUnit TestSuite NB : JUnit
     * TestSuite <-> test module pyUnit
     */
    public static Test suite() throws Exception {
        String test_module_name = "TestCalculator";
        String test_module_path = "python/sample/calculator/test";
        String module_path = "python/sample/calculator/src";
        if (!new File(test_module_path).exists()) {
            test_module_path = "yactu/python/test/calculator";
        }
        return suite(module_path, test_module_path, test_module_name);
    }
}
