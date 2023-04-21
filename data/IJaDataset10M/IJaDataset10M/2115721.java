package org.apache.tools.ant;

import org.apache.tools.ant.BuildFileTest;

/**
 */
public class LoaderRefTest extends BuildFileTest {

    public LoaderRefTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/core/loaderref/loaderref.xml");
    }

    public void tearDown() {
        executeTarget("clean");
    }

    public void testBadRef() {
        expectBuildExceptionContaining("testbadref", "Should fail due to ref " + "not being a class loader", "does not reference a class loader");
    }
}
