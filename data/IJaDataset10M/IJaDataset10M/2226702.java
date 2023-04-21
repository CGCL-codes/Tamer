package com.liferay.portalweb.portlet.suntourweather;

import com.liferay.portalweb.portal.BaseTestCase;

/**
 * <a href="AddPortletTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AddPortletTest extends BaseTestCase {

    public void testAddPortlet() throws Exception {
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
                if (selenium.isElementPresent("//div[@id=\"Sun-TourWeather\"]")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//div[@id=\"Sun-TourWeather\"]/p/a");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//img[@alt='Configuration']")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }
}
