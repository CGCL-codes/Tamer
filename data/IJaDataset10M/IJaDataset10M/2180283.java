package com.omnicarecr.accounts.util.test;

import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.*;
import com.omnicarecr.accounts.util.Passwd;
import com.omnicarecr.accounts.util.Shadow;

/**		
 * Tests for UnixCrypt class.		
 *		
 * @author marc.dojka@omnicarecr.com		
 *		
 */
public class ShadowTest extends TestCase {

    private final String CRYPT_PASSWORD = "Nh/bT1kyT3yjY";

    private final String PASSWORD = "omnicare";

    private final String PASSWORD_FAIL = "hi";

    private final String SALT = CRYPT_PASSWORD.substring(0, 2);

    private final String USER_ID = "test1";

    private final InputStream SHADOW_STREAM = this.getClass().getResourceAsStream("test_files/test_shadow.txt");

    /**		
   * Constructor		
   *		
   * @param testName name of test suite		
   */
    public ShadowTest(java.lang.String testName) {
        super(testName);
    }

    /**		
   * Main - for running stand-alone test		
   *		
   * @param args command line arguments		
   */
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**		
   * Return a new test suite		
   *		
   * @return a new test suite		
   */
    public static Test suite() {
        TestSuite suite = new TestSuite(ShadowTest.class);
        return suite;
    }

    /**		
   * Unit test to make sure that the hash generation works correctly		
   * given a salt and a password		
   */
    public void testGetPassword() {
        Shadow shadow = null;
        try {
            shadow = new Shadow(SHADOW_STREAM);
        } catch (Exception e) {
            fail();
        }
        String userid = shadow.getPassword(USER_ID);
        assertEquals(userid, CRYPT_PASSWORD);
    }

    public void testValidPasswordPass() {
        Shadow shadow = null;
        try {
            shadow = new Shadow(SHADOW_STREAM);
        } catch (Exception e) {
            fail();
        }
        boolean valid = shadow.validPassword(USER_ID, PASSWORD);
        assertTrue(valid == true);
    }

    public void testValidPasswordFail() {
        Shadow shadow = null;
        try {
            shadow = new Shadow(SHADOW_STREAM);
        } catch (Exception e) {
            fail();
        }
        boolean valid = shadow.validPassword(USER_ID, PASSWORD_FAIL);
        assertTrue(valid == false);
    }
}
