package org.xaware.server.engine.instruction.bizcomps.java;

import org.xaware.testing.util.BaseBdpTestCase;

/**
 * Test Case for Java BizComponent for Primitives.
 * 
 * @author Vasu Thadaka
 */
public class JavaPrimitiveTestCase extends BaseBdpTestCase {

    /**
	 * Default constructor.
	 */
    public JavaPrimitiveTestCase() {
        super("JavaPrimitiveTestCase");
    }

    /**
	 * Constructor with test case name as parameter.
	 */
    public JavaPrimitiveTestCase(final String p_name) {
        super(p_name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        return;
    }

    /**
	 * @returns the path location for the test data
	 * @see org.xaware.testing.util.BaseBdpTestCase#getDataFolder()
	 */
    @Override
    protected String getDataFolder() {
        return "data/org/xaware/server/engine/instruction/bizcomps/java/";
    }

    /** Test cases to test the method with primitives as parameters */
    public void testPrimitivesWithArray() {
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        setBizDocFileName("PrimitivesTest.xbd");
        setExpectedOutputFileName("PrimitivesTest_expected.xml");
        evaluateBizDoc();
    }
}
