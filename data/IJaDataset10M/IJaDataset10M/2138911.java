package com.testtoolinterfaces.testsuite;

import java.util.ArrayList;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Before;
import org.testtoolinterfaces.testsuite.ParameterTable;
import org.testtoolinterfaces.testsuite.TestCaseImpl;
import org.testtoolinterfaces.testsuite.TestEntryArrayList;
import org.testtoolinterfaces.testsuite.TestGroupImpl;
import org.testtoolinterfaces.testsuite.TestStep;
import org.testtoolinterfaces.testsuite.TestStepArrayList;
import org.testtoolinterfaces.testsuite.TestStepImpl;

public class TestEntryArrayListTester extends TestCase {

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
        TestEntryArrayList teArray = new TestEntryArrayList();
        Assert.assertEquals("Incorrect Size", 0, teArray.size());
    }

    /**
	 * Test Cases
	 */
    public void testCase_sort() {
        TestCaseImpl testCase = new TestCaseImpl("tcId", 5, "An extensive description", new ArrayList<String>(), new TestStepArrayList(), new TestStepArrayList(), new TestStepArrayList());
        TestStepImpl testStep = new TestStepImpl(TestStep.ActionType.check, 3, "description2", "command2", new ParameterTable());
        TestGroupImpl testGroup = new TestGroupImpl("ID", 4, "A Description", new ArrayList<String>(), new TestStepArrayList(), new TestEntryArrayList(), new TestStepArrayList());
        TestEntryArrayList teArray = new TestEntryArrayList();
        teArray.add(testCase);
        teArray.add(testStep);
        teArray.add(testGroup);
        TestEntryArrayList newArray = teArray.sort();
        Assert.assertEquals("Incorrect Size", 3, teArray.size());
        Assert.assertEquals("Incorrect SeqNr 1st step", 5, teArray.get(0).getSequenceNr());
        Assert.assertEquals("Incorrect SeqNr 2nd step", 3, teArray.get(1).getSequenceNr());
        Assert.assertEquals("Incorrect SeqNr 3rd step", 4, teArray.get(2).getSequenceNr());
        Assert.assertEquals("Incorrect Size", 3, newArray.size());
        Assert.assertEquals("Incorrect SeqNr 1st step", 3, newArray.get(0).getSequenceNr());
        Assert.assertEquals("Incorrect SeqNr 2nd step", 4, newArray.get(1).getSequenceNr());
        Assert.assertEquals("Incorrect SeqNr 3rd step", 5, newArray.get(2).getSequenceNr());
    }
}
