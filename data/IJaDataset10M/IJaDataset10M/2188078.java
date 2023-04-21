package com.liferay.portalweb.portal.controlpanel;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="AddServerCategoryTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AddServerCategoryTest extends BaseTestCase {

    public void testAddServerCategory() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Server")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Server"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Log Levels"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Add Category"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_137_loggerName")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("_137_loggerName", RuntimeVariables.replace("CategoryTest!"));
        selenium.click(RuntimeVariables.replace("//input[@value='Save']"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Your request processed successfully."));
    }
}
