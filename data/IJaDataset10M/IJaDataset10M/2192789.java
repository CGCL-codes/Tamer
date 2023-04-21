package com.testtoolinterfaces.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testtoolinterfaces.utils.Trace;

/**
 * @author Arjan Kranenburg
 *
 */
public class TraceTester extends TestCase {

    private final ByteArrayOutputStream myOut = new ByteArrayOutputStream();

    private final PrintStream myOrigOut = System.out;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        System.out.println("==========================================================================");
        System.out.println(this.getName() + ":");
        System.setOut(new PrintStream(myOut));
    }

    @After
    public void tearDown() {
        System.setOut(myOrigOut);
    }

    /**
	 * Test method for {@link org.testtoolinterfaces.utils.Trace#addBaseClass(java.lang.String)}.
	 * 
	 * This test must runfirst as it adds a base class and since Trace is a global object, the
	 * added baseclass remains and cannot be removed.
	 */
    @Test
    public void testAddBaseClass() {
        Trace traceObj = Trace.getInstance();
        traceObj.setEnabled(true);
        traceObj.setDepth(3);
        traceObj.setTraceClass("org.testtoolinterfaces.utils.TraceTester");
        traceObj.setTraceLevel(Trace.ALL);
        Trace.print(Trace.EXEC, "testAddBaseClass");
        assertEquals("Incorrect printout", "", myOut.toString());
        myOut.reset();
        traceObj.addBaseClass("org.testtoolinterfaces");
        Trace.print(Trace.EXEC, "testAddBaseClass", true);
        assertEquals("Incorrect printout", " TraceTester.testAddBaseClass", myOut.toString());
        myOut.reset();
    }

    /**
	 * Test method for {@link org.testtoolinterfaces.utils.Trace#setDepth(int)}.
	 */
    @Test
    public void testSetDepth() {
        Trace traceObj = Trace.getInstance();
        traceObj.setEnabled(true);
        traceObj.setDepth(0);
        traceObj.setTraceClass("org.testtoolinterfaces.utils.TraceTester");
        traceObj.setTraceLevel(Trace.ALL);
        Trace.println(Trace.EXEC, "testSetDepth");
        depthTestHelper();
        assertEquals("Incorrect printout", " testSetDepth\n depthTestHelper\n", myOut.toString());
        myOut.reset();
        traceObj.setDepth(1);
        depthTestHelper();
        assertEquals("Incorrect printout", " depthTestHelper\n+ depthTestHelper1\n", myOut.toString());
        myOut.reset();
        traceObj.setDepth(2);
        depthTestHelper();
        assertEquals("Incorrect printout", " depthTestHelper\n+ depthTestHelper1\n++ depthTestHelper2\n", myOut.toString());
        myOut.reset();
    }

    /**
	 * Test method for {@link org.testtoolinterfaces.utils.Trace#setEnabled(boolean)}.
	 */
    @Test
    public void testSetEnabled() {
        Trace traceObj = Trace.getInstance();
        traceObj.setEnabled(false);
        traceObj.setDepth(3);
        traceObj.setTraceClass("org.testtoolinterfaces.utils.TraceTester");
        traceObj.setTraceLevel(Trace.ALL);
        Trace.print(Trace.EXEC, "testSetEnabled");
        assertEquals("Incorrect printout", "", myOut.toString());
        myOut.reset();
        traceObj.setEnabled(true);
        Trace.print(Trace.EXEC, "testSetEnabled");
        assertEquals("Incorrect printout", " testSetEnabled", myOut.toString());
        myOut.reset();
    }

    /**
	 * Test method for {@link org.testtoolinterfaces.utils.Trace#setTraceClass(java.lang.String)}.
	 */
    @Test
    public void testSetTraceClass() {
        Trace traceObj = Trace.getInstance();
        traceObj.setEnabled(true);
        traceObj.setDepth(3);
        traceObj.setTraceClass("org.testtoolinterfaces.utils");
        traceObj.setTraceLevel(Trace.ALL);
        Trace.print(Trace.EXEC, "testSetTraceClass");
        assertEquals("Incorrect printout", "", myOut.toString());
        myOut.reset();
        traceObj.setTraceClass("org.testtoolinterfaces.utils.TraceTester");
        Trace.print(Trace.EXEC, "testSetTraceClass");
        assertEquals("Incorrect printout", " testSetTraceClass", myOut.toString());
        myOut.reset();
        traceObj.setTraceClass("org.testtoolinterfaces.utils.TraceTester2");
        Trace.print(Trace.EXEC, "testSetTraceClass");
        assertEquals("Incorrect printout", "", myOut.toString());
        myOut.reset();
    }

    /**
	 * Test method for {@link org.testtoolinterfaces.utils.Trace#setTraceLevel(org.testtoolinterfaces.utils.Trace.LEVEL)}.
	 */
    @Test
    public void testSetTraceLevel() {
        Trace traceObj = Trace.getInstance();
        traceObj.setEnabled(true);
        traceObj.setDepth(3);
        traceObj.setTraceClass("org.testtoolinterfaces.utils.TraceTester");
        traceObj.setTraceLevel(Trace.HIGH);
        Trace.print(Trace.EXEC, "testSetTraceLevel");
        assertEquals("Incorrect printout", "", myOut.toString());
        myOut.reset();
        traceObj.setTraceLevel(Trace.EXEC);
        Trace.print(Trace.EXEC, "testSetTraceLevel");
        assertEquals("Incorrect printout", " testSetTraceLevel", myOut.toString());
        myOut.reset();
        traceObj.setTraceLevel(Trace.EXEC_PLUS);
        Trace.print(Trace.EXEC, "testSetTraceLevel2");
        Trace.print(Trace.EXEC_UTIL, "testSetTraceLevel3");
        assertEquals("Incorrect printout", " testSetTraceLevel2", myOut.toString());
        myOut.reset();
    }

    /**
	 * Test method for {@link org.testtoolinterfaces.utils.Trace#println(org.testtoolinterfaces.utils.Trace.LEVEL)}.
	 */
    @Test
    public void testPrintlnLEVEL() {
        Trace traceObj = Trace.getInstance();
        traceObj.setEnabled(true);
        traceObj.setDepth(3);
        traceObj.setTraceClass("org.testtoolinterfaces.utils.TraceTester");
        traceObj.setTraceLevel(Trace.ALL);
        Trace.println(Trace.EXEC);
        assertEquals("Incorrect printout", " TraceTester.testPrintlnLEVEL\n", myOut.toString());
        myOut.reset();
    }

    private void depthTestHelper() {
        Trace.println(Trace.EXEC, "depthTestHelper");
        TraceTesterHelper helper = new TraceTesterHelper();
        helper.depthTestHelper1();
    }
}
