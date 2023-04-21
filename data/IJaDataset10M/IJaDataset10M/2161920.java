package com.liferay.portalweb.portal.controlpanel;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="AddInvalidCommunityNameTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AddInvalidCommunityNameTest extends BaseTestCase {

    public void testAddInvalidCommunityName() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Communities")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Communities"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("link=Add"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_134_name")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.typeKeys("_134_name", RuntimeVariables.replace("!@#$"));
        selenium.type("_134_name", RuntimeVariables.replace("!@#$"));
        selenium.typeKeys("_134_description", RuntimeVariables.replace("This is an invalid communit test!"));
        selenium.type("_134_description", RuntimeVariables.replace("This is an invalid community test!"));
        selenium.click(RuntimeVariables.replace("//input[@value='Save']"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("You have entered invalid data. Please try again."));
    }
}
