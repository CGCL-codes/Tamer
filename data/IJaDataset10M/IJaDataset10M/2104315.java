package com.objectwave.persist.broker;

import java.util.Date;

/**
 * @author  dhoag
 * @version  $Id: AccessObjectFormatterTest.java,v 1.2 2005/02/21 04:09:15 dave_hoag Exp $
 */
public class AccessObjectFormatterTest extends com.objectwave.test.UnitTestBaseImpl {

    public AccessObjectFormatterTest() {
    }

    public AccessObjectFormatterTest(String test) {
        super(test);
    }

    /**
     *The main program for the Test class
     *
     * @param  args The command line arguments
     */
    public static void main(String[] args) {
        com.objectwave.test.TestRunner.run(new AccessObjectFormatterTest(), args);
    }

    /**
     *A unit test for JUnit
     *
     * @exception  Exception
     */
    public void testDateFormat() throws Exception {
        Date date = java.text.DateFormat.getDateInstance().parse("October 9, 1999");
        StringBuffer buffer = new StringBuffer();
        new AccessObjectFormatter().formatDate(date, buffer);
        testContext.assertEquals("# 1999-10-09 00:00:00 #", buffer.toString());
    }
}
