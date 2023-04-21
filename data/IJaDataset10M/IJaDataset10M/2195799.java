package com.liferay.portalweb.portlet.announcements;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="AddLowPriorityAnnouncementTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AddLowPriorityAnnouncementTest extends BaseTestCase {

    public void testAddLowPriorityAnnouncement() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Manage Entries")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Manage Entries"));
        selenium.waitForPageToLoad("30000");
        selenium.select("_84_distributionScope", "label=General");
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("//input[@value='Add Entry']"));
        selenium.waitForPageToLoad("30000");
        selenium.type("_84_title", RuntimeVariables.replace("Low Priority Announcement"));
        selenium.type("_84_url", RuntimeVariables.replace("www.liferay.com"));
        selenium.type("_84_content", RuntimeVariables.replace("Hi everyone. This is a low priority announcement."));
        selenium.select("_84_priority", RuntimeVariables.replace("label=Low"));
        selenium.click(RuntimeVariables.replace("//input[@value='Save']"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Entries"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Low Priority Announcement")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }
}
