package com.liferay.portalweb.portal.controlpanel;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="AddNullServerInstanceWebIDTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AddNullServerInstanceWebIDTest extends BaseTestCase {

    public void testAddNullServerInstanceWebID() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Instances")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Instances"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("//input[@value='Add']"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("//input[@value='Save']"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("You have entered invalid data. Please try again."));
        assertTrue(selenium.isTextPresent("Please enter a valid web ID."));
    }
}
