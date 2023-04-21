package com.liferay.portalweb.portlet.assetpublisher;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="SelectBookmarkTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SelectBookmarkTest extends BaseTestCase {

    public void testSelectBookmark() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Asset Publisher Test Page")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Asset Publisher Test Page"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Configuration")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Configuration"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_86_assetType")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.select("_86_assetType", "label=Bookmarks Entry");
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=AP Setup Test Bookmark Folder")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=AP Setup Test Bookmark Folder"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=AP Setup Test Bookmark")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=AP Setup Test Bookmark"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("link=AP Setup Test Bookmark"));
        assertTrue(selenium.isTextPresent("You have successfully updated the setup."));
        selenium.click(RuntimeVariables.replace("link=Return to Full Page"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("AP Setup Test Bookmark"));
        assertTrue(selenium.isElementPresent("link=AP Setup Test Bookmark"));
    }
}
