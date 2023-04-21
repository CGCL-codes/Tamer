package org.klautor.client;

import jmunit.framework.cldc10.*;

/**
 * @author joey
 */
public class TerminLoaderTest extends TestCase {

    public TerminLoaderTest() {
        super(3, "TerminLoaderTest");
    }

    public void test(int testNumber) throws Throwable {
        switch(testNumber) {
            case 0:
                testRun();
                break;
            case 1:
                testDownload();
                break;
            case 2:
                testAbort();
                break;
            default:
                break;
        }
    }

    /**
     * Test of testRun method, of class TerminLoader.
     */
    public void testRun() throws AssertionFailedException {
        System.out.println("run");
        TerminLoader instance = null;
        instance.run();
        fail("The test case is a prototype.");
    }

    /**
     * Test of testDownload method, of class TerminLoader.
     */
    public void testDownload() throws AssertionFailedException {
        System.out.println("download");
        TerminLoader instance = null;
        instance.download();
        fail("The test case is a prototype.");
    }

    /**
     * Test of testAbort method, of class TerminLoader.
     */
    public void testAbort() throws AssertionFailedException {
        System.out.println("abort");
        TerminLoader instance = null;
        instance.abort();
        fail("The test case is a prototype.");
    }
}
