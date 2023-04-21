package org.klautor.client;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import jmunit.framework.cldc10.*;

/**
 * @author joey
 */
public class AbstractLoadScreenTest extends TestCase {

    public AbstractLoadScreenTest() {
        super(9, "AbstractLoadScreenTest");
    }

    public void test(int testNumber) throws Throwable {
        switch(testNumber) {
            case 0:
                testSetGauge();
                break;
            case 1:
                testHeading();
                break;
            case 2:
                testWasError();
                break;
            case 3:
                testNeedTermine();
                break;
            case 4:
                testCommandAction();
                break;
            case 5:
                testGetGaugeValue();
                break;
            case 6:
                testStartThreads();
                break;
            case 7:
                testLoadNow();
                break;
            case 8:
                testText();
                break;
            default:
                break;
        }
    }

    /**
     * Test of testSetGauge method, of class AbstractLoadScreen.
     */
    public void testSetGauge() throws AssertionFailedException {
        System.out.println("setGauge");
        AbstractLoadScreen instance = null;
        int cur_1 = 0;
        int max_1 = 0;
        instance.setGauge(cur_1, max_1);
        fail("The test case is a prototype.");
    }

    /**
     * Test of testHeading method, of class AbstractLoadScreen.
     */
    public void testHeading() throws AssertionFailedException {
        System.out.println("heading");
        AbstractLoadScreen instance = null;
        String expResult_1 = "";
        String result_1 = instance.heading();
        assertEquals(expResult_1, result_1);
        fail("The test case is a prototype.");
    }

    /**
     * Test of testWasError method, of class AbstractLoadScreen.
     */
    public void testWasError() throws AssertionFailedException {
        System.out.println("wasError");
        AbstractLoadScreen instance = null;
        instance.wasError();
        fail("The test case is a prototype.");
    }

    /**
     * Test of testNeedTermine method, of class AbstractLoadScreen.
     */
    public void testNeedTermine() throws AssertionFailedException {
        System.out.println("needTermine");
        AbstractLoadScreen instance = null;
        boolean expResult_1 = false;
        boolean result_1 = instance.needTermine();
        assertEquals(expResult_1, result_1);
        fail("The test case is a prototype.");
    }

    /**
     * Test of testCommandAction method, of class AbstractLoadScreen.
     */
    public void testCommandAction() throws AssertionFailedException {
        System.out.println("commandAction");
        AbstractLoadScreen instance = null;
        Command c_1 = null;
        Displayable d_1 = null;
        instance.commandAction(c_1, d_1);
        fail("The test case is a prototype.");
    }

    /**
     * Test of testGetGaugeValue method, of class AbstractLoadScreen.
     */
    public void testGetGaugeValue() throws AssertionFailedException {
        System.out.println("getGaugeValue");
        AbstractLoadScreen instance = null;
        int expResult_1 = 0;
        int result_1 = instance.getGaugeValue();
        assertEquals(expResult_1, result_1);
        fail("The test case is a prototype.");
    }

    /**
     * Test of testStartThreads method, of class AbstractLoadScreen.
     */
    public void testStartThreads() throws AssertionFailedException {
        System.out.println("startThreads");
        AbstractLoadScreen instance = null;
        instance.startThreads();
        fail("The test case is a prototype.");
    }

    /**
     * Test of testLoadNow method, of class AbstractLoadScreen.
     */
    public void testLoadNow() throws AssertionFailedException {
        System.out.println("loadNow");
        AbstractLoadScreen instance = null;
        instance.loadNow();
        fail("The test case is a prototype.");
    }

    /**
     * Test of testText method, of class AbstractLoadScreen.
     */
    public void testText() throws AssertionFailedException {
        System.out.println("text");
        AbstractLoadScreen instance = null;
        String expResult_1 = "";
        String result_1 = instance.text();
        assertEquals(expResult_1, result_1);
        fail("The test case is a prototype.");
    }
}
