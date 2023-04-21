package com.liferay.portalweb.portlet.blogsaggregator;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="SetupTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SetupTest extends BaseTestCase {

    public void testSetup() throws Exception {
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
        selenium.click(RuntimeVariables.replace("//a[@id=\"my-community-private-pages\"]"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//div/a/span")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//div/a/span");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("new_page")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.typeKeys("new_page", RuntimeVariables.replace("BA Setup Test Page"));
        selenium.type("new_page", RuntimeVariables.replace("BA Setup Test Page"));
        selenium.click("link=Save");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=BA Setup Test Page")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=BA Setup Test Page"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Add Application")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=Add Application");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//div[@id=\"Collaboration-Blogs\"]")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//div[@id=\"Collaboration-Blogs\"]/p/a");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//input[@value='Add Blog Entry']")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("//input[@value='Add Blog Entry']"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_33_title")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.typeKeys("_33_title", RuntimeVariables.replace("BA Setup Test Entr"));
        selenium.type("_33_title", RuntimeVariables.replace("BA Setup Test Entry"));
        Thread.sleep(5000);
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_33_editor")) {
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
                if (selenium.isElementPresent("FCKeditor1___Frame")) {
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
                if (selenium.isElementPresent("//textarea")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.selectFrame("//iframe[@id=\"_33_editor\"]");
        selenium.selectFrame("//iframe[@id=\"FCKeditor1___Frame\"]");
        selenium.selectFrame("//iframe");
        selenium.typeKeys("//body", RuntimeVariables.replace("This is a BA setup test entr"));
        selenium.type("//body", RuntimeVariables.replace("This is a BA setup test entry!"));
        selenium.selectFrame("relative=top");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_33_saveButton")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("_33_saveButton"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isElementPresent("link=BA Setup Test Entry"));
        assertTrue(selenium.isTextPresent("Your request processed successfully."));
    }
}
