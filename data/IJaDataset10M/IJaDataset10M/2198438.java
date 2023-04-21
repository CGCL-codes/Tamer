package com.liferay.portalweb.portlet.samplelar;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="AddPageTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AddPageTest extends BaseTestCase {

    public void testAddPage() throws Exception {
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
        selenium.typeKeys("new_page", RuntimeVariables.replace("Sample LAR Test Page"));
        selenium.type("new_page", RuntimeVariables.replace("Sample LAR Test Page"));
        selenium.click("link=Save");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Sample LAR Test Page")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Sample LAR Test Page"));
        selenium.waitForPageToLoad("30000");
    }
}
