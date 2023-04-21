package com.liferay.portalweb.portlet.stocks;

import com.liferay.portalweb.portal.BaseTestCase;

/**
 * <a href="AddPortletTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AddPortletTest extends BaseTestCase {

    public void testAddPortlet() throws Exception {
        selenium.click("link=Add Application");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//div[@id=\"Finance-Stocks\"]")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//div[@id=\"Finance-Stocks\"]/p/a");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//img[@alt='Preferences']")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }
}
