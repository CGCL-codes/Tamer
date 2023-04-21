package apps.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Invoke all the JMRI project JUnit tests via a GUI interface.
 *
 * <hr>
 * This file is part of JMRI.
 * <P>
 * JMRI is free software; you can redistribute it and/or modify it under 
 * the terms of version 2 of the GNU General Public License as published 
 * by the Free Software Foundation. See the "COPYING" file for a copy
 * of this license.
 * <P>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License 
 * for more details.
 *
 * @author	Bob Jacobsen
 * @version	$Revision: 17977 $
 */
public class AllTest extends TestCase {

    public AllTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { "-noloading", AllTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("AllTest");
        suite.addTest(jmri.JmriTest.suite());
        return suite;
    }

    public static void initLogging() {
        apps.tests.Log4JFixture.initLogging();
    }

    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
    }

    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }
}
