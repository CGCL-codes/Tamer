package com.liferay.portalweb.portlet.documentlibrary;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="EditCommentsTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class EditCommentsTest extends BaseTestCase {

    public void testEditComments() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Edit")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("link=Edit");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_20_editBody1")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.typeKeys("_20_editBody1", RuntimeVariables.replace("Edited comments test!!!"));
        selenium.type("_20_editBody1", RuntimeVariables.replace("Edited comments test!!!"));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_20_updateReplyButton1")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("_20_updateReplyButton1"));
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Edited comments test!!!"));
        assertTrue(selenium.isTextPresent("Your request processed successfully."));
    }
}
