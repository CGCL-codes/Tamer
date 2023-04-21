package com.liferay.portalweb.portlet.sessionexpiration;

import com.liferay.portalweb.portal.BaseTestCase;

/**
 * <a href="ExtendSessionTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ExtendSessionTest extends BaseTestCase {

    public void testExtendSession() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Session Expiration Test Page")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        Thread.sleep(30000);
        Thread.sleep(15000);
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//input[@value='Extend']")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click("//input[@value='Extend']");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (!selenium.isElementPresent("//input[@value='Extend']")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }
}
