package com.liferay.portalweb.portal.permissions.messageboards;

import com.liferay.portalweb.portal.BaseTestCase;

/**
 * <a href="Member_DeleteMessageTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class Member_DeleteMessageTest extends BaseTestCase {

    public void testMember_DeleteMessage() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Categories")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        assertTrue(selenium.isTextPresent("Test Thread 2 Edited"));
        assertTrue(selenium.isElementPresent("link=Delete"));
        selenium.click("link=Delete");
        assertTrue(selenium.getConfirmation().matches("^Are you sure you want to delete this[\\s\\S]$"));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isTextPresent("Your request processed successfully.")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        assertFalse(selenium.isElementPresent("link=Test Thread 2"));
    }
}
