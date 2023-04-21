package com.liferay.portalweb.portlet.staging;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * <a href="VerifyRemotePublishStagedPrivatePagesTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class VerifyRemotePublishStagedPrivatePagesTest extends BaseTestCase {

    public void testVerifyRemotePublishStagedPrivatePages() throws Exception {
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("my-community-private-pages")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("my-community-private-pages"));
        selenium.waitForPageToLoad("30000");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Sign Out")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Sign Out"));
        selenium.waitForPageToLoad("30000");
        selenium.open("http://5.227.126.113:8080/web/guest/home");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_58_login")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("_58_login", RuntimeVariables.replace("test@liferay.com"));
        selenium.type("_58_password", RuntimeVariables.replace("test"));
        selenium.click("//input[@value='Sign In']");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("//div[@id='banner']/div/div/ul/li[8]/ul/li[5]/ul/li[2]/a[1]")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("//div[@id='banner']/div/div/ul/li[8]/ul/li[5]/ul/li[2]/a[1]"));
        selenium.waitForPageToLoad("30000");
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
        verifyTrue(selenium.isElementPresent("link=Superman or Batman?!"));
        verifyTrue(selenium.isElementPresent("link=1"));
        verifyTrue(selenium.isElementPresent("link=Test Entry 3"));
        verifyTrue(selenium.isElementPresent("link=Test Entry"));
        verifyFalse(selenium.isElementPresent("link=Test Entry 2"));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Articles")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Articles"));
        selenium.waitForPageToLoad("30000");
        verifyTrue(selenium.isElementPresent("link=Article to be Published"));
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("link=Return to Full Page")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.click(RuntimeVariables.replace("link=Return to Full Page"));
        selenium.waitForPageToLoad("30000");
        selenium.open("http://localhost:8080/web/guest/home");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail("timeout");
            }
            try {
                if (selenium.isElementPresent("_58_login")) {
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
        selenium.type("_58_login", RuntimeVariables.replace("test@liferay.com"));
        selenium.type("_58_password", RuntimeVariables.replace("test"));
        selenium.click(RuntimeVariables.replace("//input[@value='Sign In']"));
        selenium.waitForPageToLoad("30000");
        selenium.click(RuntimeVariables.replace("my-community-private-pages"));
        selenium.waitForPageToLoad("30000");
    }
}
