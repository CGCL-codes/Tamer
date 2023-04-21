package com.liferay.portalweb.portlet.managepages;

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
        selenium.click(RuntimeVariables.replace("link=Manage Pages"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("Link=Joe Bloggs"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_88_name_en_US")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("_88_name_en_US", RuntimeVariables.replace("Manage Pages Test Page"));
        selenium.click(RuntimeVariables.replace("//input[@value='Add Page']"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isTextPresent("Manage Pages Test Page")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Return to Full Page"));
        selenium.waitForPageToLoad("30000");
    }
}
