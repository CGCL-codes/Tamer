package com.spextreme.nunit.xml.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.jdom.Element;
import org.junit.Test;
import com.spextreme.nunit.model.TestCase;

/**
 * Tests the test case processor object.
 */
public class TestCaseProcessorTest {

    /**
	 * The processor under test.
	 */
    TestCaseProcessor mProcessor = new TestCaseProcessor();

    /**
	 * Test method for
	 * {@link com.spextreme.nunit.xml.internal.TestCaseProcessor#parseElement(org.jdom.Element)}.
	 */
    @Test
    public void testParseElement() {
        final Element testCaseElement = new Element(TestCaseProcessor.ELEMENT_TESTCASE);
        testCaseElement.setAttribute(TestCaseProcessor.ATTRIBUTE_ASSERT, "3");
        testCaseElement.setAttribute(TestCaseProcessor.ATTRIBUTE_EXECUTED, "true");
        testCaseElement.setAttribute(TestCaseProcessor.ATTRIBUTE_NAME, "ATest");
        testCaseElement.setAttribute(TestCaseProcessor.ATTRIBUTE_SUCCESS, "true");
        testCaseElement.setAttribute(TestCaseProcessor.ATTRIBUTE_TIME, "0.68");
        final Element failureElement = new Element(TestCaseFailureProcessor.ELEMENT_FAILURE);
        testCaseElement.addContent(failureElement);
        final TestCase testCase = (TestCase) mProcessor.parseElement(testCaseElement);
        assertEquals("3", testCase.getAsserts());
        assertTrue(testCase.isExecuted());
        assertEquals("ATest", testCase.getName());
        assertTrue(testCase.isSuccess());
        assertEquals(0.68, testCase.getTime(), 0);
        assertNotNull(testCase.getFailure());
    }

    /**
	 * Test method for
	 * {@link com.spextreme.nunit.xml.internal.TestCaseProcessor#parseElement(org.jdom.Element)}.
	 */
    @Test
    public void testParseElementBadTimeNoFailure() {
        final Element testCaseElement = new Element(TestCaseProcessor.ELEMENT_TESTCASE);
        testCaseElement.setAttribute(TestCaseProcessor.ATTRIBUTE_ASSERT, "3");
        testCaseElement.setAttribute(TestCaseProcessor.ATTRIBUTE_EXECUTED, "true");
        testCaseElement.setAttribute(TestCaseProcessor.ATTRIBUTE_NAME, "ATest");
        testCaseElement.setAttribute(TestCaseProcessor.ATTRIBUTE_SUCCESS, "true");
        testCaseElement.setAttribute(TestCaseProcessor.ATTRIBUTE_TIME, "abc");
        final TestCase testCase = (TestCase) mProcessor.parseElement(testCaseElement);
        assertEquals("3", testCase.getAsserts());
        assertTrue(testCase.isExecuted());
        assertEquals("ATest", testCase.getName());
        assertTrue(testCase.isSuccess());
        assertEquals(0.0, testCase.getTime(), 0);
        assertNull(testCase.getFailure());
    }
}
