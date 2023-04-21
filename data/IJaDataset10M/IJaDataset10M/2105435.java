package com.liferay.portalweb.portlet.pagecomments;

import com.liferay.portalweb.portal.BaseTestCase;

/**
 * <a href="DeleteCommentTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class DeleteCommentTest extends BaseTestCase {

    public void testDeleteComment() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Delete")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=Delete");
        assertTrue(selenium.getConfirmation().matches("^Are you sure you want to delete this[\\s\\S]$"));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (!selenium.isTextPresent("This is a test page comment!")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//a[@id=\"my-community-private-pages\"]")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }
}
