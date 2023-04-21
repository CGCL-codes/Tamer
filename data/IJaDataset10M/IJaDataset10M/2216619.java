package com.liferay.portalweb.portlet.imagegallery;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="DeleteFoldersTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class DeleteFoldersTest extends BaseTestCase {

    public void testDeleteFolders() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//td[4]/ul/li/strong/span")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//td[4]/ul/li/strong/span");
        selenium.click(RuntimeVariables.replace("//div[2]/ul/li[3]/nobr/a/img"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.getConfirmation().matches("^Are you sure you want to delete this[\\s\\S]$"));
        Thread.sleep(1000);
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
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (!selenium.isElementPresent("link=Test Folder")) {
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
                if (selenium.isElementPresent("link=Delete")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//td[4]/ul/li/strong/span");
        selenium.click(RuntimeVariables.replace("//div[2]/ul/li[3]/nobr/a/img"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.getConfirmation().matches("^Are you sure you want to delete this[\\s\\S]$"));
        Thread.sleep(1000);
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
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (!selenium.isElementPresent("link=Test Folder 2")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        assertFalse(selenium.isElementPresent("link=Test Folder 2"));
        assertFalse(selenium.isElementPresent("link=Test Subfolder 2"));
        assertFalse(selenium.isElementPresent("link=Test Folder"));
    }
}
