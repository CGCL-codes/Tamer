package com.liferay.portalweb.portal.permissions.blogs;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="Guest_ViewCommentsTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class Guest_ViewCommentsTest extends BaseTestCase {

    public void testGuest_ViewComments() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Blogs Permissions Page")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Test Entry 1"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Test Comment 1"));
        assertTrue(selenium.isTextPresent("Member Comment Test"));
        selenium.click(RuntimeVariables.replace("_33_tabs1TabsBack"));
        selenium.waitForPageToLoad("30000");
    }
}
