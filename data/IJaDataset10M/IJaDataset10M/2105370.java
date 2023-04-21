package org.openscience.cdk.coverage;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @cdk.module test-render
 */
public class RenderCoverageTest extends CoverageAnnotationTest {

    private static final String CLASS_LIST = "render.javafiles";

    @BeforeClass
    public static void setUp() throws Exception {
        loadClassList(CLASS_LIST, RenderCoverageTest.class.getClassLoader());
    }

    @Test
    public void testCoverage() {
        super.runCoverageTest();
    }
}
