package com.testtoolinterfaces.testsuite;

import java.util.ArrayList;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Before;
import org.testtoolinterfaces.testsuite.TestCaseImpl;
import org.testtoolinterfaces.testsuite.TestEntry;
import org.testtoolinterfaces.testsuite.TestStepArrayList;

public class TestCaseImplTester extends TestCase {

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        System.out.println("==========================================================================");
        System.out.println(this.getName() + ":");
    }

    /**
	 * Test Cases
	 */
    public void testCase_constructor() {
        TestCaseImpl testCase = new TestCaseImpl("tcId", 3, "An extensive description", new ArrayList<String>(), new TestStepArrayList(), new TestStepArrayList(), new TestStepArrayList());
        Assert.assertEquals("Incorrect Type", TestEntry.TYPE.Case, testCase.getType());
        Assert.assertEquals("Incorrect ID", "tcId", testCase.getId());
        Assert.assertEquals("Incorrect Sequence NR", 3, testCase.getSequenceNr());
        Assert.assertEquals("Incorrect Description", "An extensive description", testCase.getDescription());
    }
}
