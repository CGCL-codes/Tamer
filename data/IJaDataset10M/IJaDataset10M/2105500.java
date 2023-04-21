package com.liferay.portalweb.portlet.journal;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="SearchTemplateTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SearchTemplateTest extends BaseTestCase {

    public void testSearchTemplate() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Journal Test Page")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Journal Test Page"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Templates")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Templates"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Advanced »")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=Advanced »");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_15_searchTemplateId")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.typeKeys("_15_searchTemplateId", RuntimeVariables.replace("Test"));
        selenium.type("_15_searchTemplateId", RuntimeVariables.replace("Test"));
        selenium.click(RuntimeVariables.replace("//input[@value='Search Templates']"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Test Journal Template"));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_15_searchTemplateId")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("_15_searchTemplateId", RuntimeVariables.replace("Test1"));
        selenium.click(RuntimeVariables.replace("//input[@value='Search Templates']"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("No templates were found."));
        selenium.type("_15_searchTemplateId", RuntimeVariables.replace(""));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_15_name")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("_15_name", RuntimeVariables.replace("Test"));
        selenium.click(RuntimeVariables.replace("//input[@value='Search Templates']"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Test Journal Template"));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_15_name")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("_15_name", RuntimeVariables.replace("Test1"));
        selenium.click(RuntimeVariables.replace("//input[@value='Search Templates']"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("No templates were found."));
        selenium.type("_15_name", RuntimeVariables.replace(""));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_15_description")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("_15_description", RuntimeVariables.replace("This is a test journal template!"));
        selenium.click(RuntimeVariables.replace("//input[@value='Search Templates']"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Test Journal Template"));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_15_description")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("_15_description", RuntimeVariables.replace("This is a test journal template!!"));
        selenium.click(RuntimeVariables.replace("//input[@value='Search Templates']"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("No templates were found."));
        selenium.type("_15_description", RuntimeVariables.replace(""));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=« Basic")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=« Basic");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_15_keywords")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }
}
