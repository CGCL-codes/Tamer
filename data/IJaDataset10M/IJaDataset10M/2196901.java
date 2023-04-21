package com.liferay.portalweb.portlet.messageboards;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="AddIncorrectEntryTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AddIncorrectEntryTest extends BaseTestCase {

    public void testAddIncorrectEntry() throws Exception {
        selenium.click(RuntimeVariables.replace("//div[@id='portlet-wrapper-19']/div[2]/div/div/form[1]/div[4]/table/tbody/tr[2]/td[1]/a/b"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("//input[@value='Post New Thread']"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_19_subject")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("//input[@value='Save']"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isTextPresent("You have entered invalid data. Please try again.")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=« Back"));
        selenium.waitForPageToLoad("30000");
    }
}
