package net.sf.uibuilder;

import junit.framework.TestCase;

/**
 * BaseTestCase is the base test case to be extended by all ui builder test
 * cases.  Test case implementations  are used to automate the testing via
 * JUnit.
 *
 * @version   1.0 2004-3-8
 * @author    <A HREF="mailto:chyxiang@yahoo.com">Chen Xiang (Sean)</A>
 */
public class BaseTestCase extends TestCase {

    /**
     * Constructor function
     */
    public BaseTestCase(String name) throws Exception {
        super(name);
    }
}
