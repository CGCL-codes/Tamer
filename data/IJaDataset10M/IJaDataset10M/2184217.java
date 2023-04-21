package com.liferay.portalweb.portal.permissions.blogs;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="Member_AddCommentTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class Member_AddCommentTest extends BaseTestCase {

    public void testMember_AddComment() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//input[@value='Search']")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        assertTrue(selenium.isElementPresent("link=Test Entry 1"));
        selenium.click(RuntimeVariables.replace("link=Test Entry 1"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("link=Post Reply"));
        selenium.click("link=Post Reply");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_33_postReplyBody1")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.typeKeys("_33_postReplyBody1", RuntimeVariables.replace("Member Comment Test"));
        selenium.type("_33_postReplyBody1", RuntimeVariables.replace("Member Comment Test"));
        selenium.click(RuntimeVariables.replace("_33_postReplyButton1"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Test Comment"));
        assertTrue(selenium.isTextPresent("Member Comment Test"));
        selenium.click(RuntimeVariables.replace("_33_tabs1TabsBack"));
        selenium.waitForPageToLoad("30000");
    }
}
