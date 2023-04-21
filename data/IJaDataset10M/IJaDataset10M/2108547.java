package com.liferay.portalweb.portlet.webcam;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="DownloadPortletTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class DownloadPortletTest extends BaseTestCase {

    public void testDownloadPortlet() throws Exception {
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
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Plugins")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Plugins"));
        selenium.waitForPageToLoad("30000");
        selenium.type("_111_keywords", RuntimeVariables.replace("webcam"));
        selenium.click(RuntimeVariables.replace("//input[@value='Search Plugins']"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Webcam 5.0.0.1")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Webcam 5.0.0.1"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("//input[@value='Install']"));
        selenium.waitForPageToLoad("30000");
        Thread.sleep(20000);
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isTextPresent("The plugin was downloaded successfully and is now being installed.")) {
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
