package org.riverock.generic.test.cases;

/**
 * User: Admin
 * Date: May 9, 2003
 * Time: 12:57:29 PM
 *
 * $Id: TestCaseInterface.java,v 1.5 2006/06/05 19:19:27 serg_main Exp $
 */
public interface TestCaseInterface {

    public void insertTestData() throws Exception;

    public void doTest() throws Exception;
}
