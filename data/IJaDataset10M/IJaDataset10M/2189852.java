package org.kwantu.zakwantu.selenium;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:petrus@codewave.co.za>Petrus Pelser</a>
 */
public class ModelBrowserTest extends AbstractSeleniumTestCase {

    private static final Log LOG = LogFactory.getLog(ModelBrowserTest.class);

    private static final String WAIT_TIME = "30000";

    private static final String TIMEOUT = "timeout";

    public void testAddModel() throws InterruptedException {
        openStartPage();
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a[@id='1:2:button']/span");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail(TIMEOUT);
            }
            try {
                if (selenium.isElementPresent("itemlist:1:item:modalwindow:content:form:fields:1:editor:edit")) {
                    break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        selenium.type("itemlist:1:item:modalwindow:content:form:fields:1:editor:edit", "RegressionTesting");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail(TIMEOUT);
            }
            try {
                if (selenium.isElementPresent("1:2:buttonArgPanel:okButton")) {
                    break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        selenium.click("1:2:buttonArgPanel:okButton");
        selenium.waitForPageToLoad(WAIT_TIME);
        assertTrue(selenium.isTextPresent("Created Kwantu Model 'RegressionTesting'"));
    }

    public void testChangeModelName() throws InterruptedException {
        openStartPage();
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail(TIMEOUT);
            }
            try {
                if (selenium.isElementPresent("//a[@id='1:3:0:5:7:11:button']/span")) {
                    break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        selenium.click("//a[@id='1:3:0:5:7:11:button']/span");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail(TIMEOUT);
            }
            try {
                if (selenium.isElementPresent("itemlist:2:item:table:rows:1:cells:2:cell:form:itemlist:0:item:modalwindow:content:form:fields:1:editor:edit")) {
                    break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        selenium.type("itemlist:2:item:table:rows:1:cells:2:cell:form:itemlist:0:item:modalwindow:content:form:fields:1:editor:edit", "RegressionTest");
        selenium.click("1:3:0:5:7:11:buttonArgPanel:okButton");
        selenium.waitForPageToLoad(WAIT_TIME);
        assertTrue(selenium.isTextPresent("Updated Kwantu Model 'RegressionTest'"));
    }

    public void testAddClass() throws InterruptedException {
        openStartPage();
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a[@id='1:3:0:5:7:9:button']/span");
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a[@id='1:2:button']/span");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail(TIMEOUT);
            }
            try {
                if (selenium.isElementPresent("itemlist:3:item:modalwindow:content:form:fields:1:editor:edit")) {
                    break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        selenium.type("itemlist:3:item:modalwindow:content:form:fields:1:editor:edit", "Dummy");
        selenium.click("1:2:buttonArgPanel:okButton");
        selenium.waitForPageToLoad(WAIT_TIME);
        assertTrue(selenium.isTextPresent("Created Kwantu Class 'Dummy'"));
        selenium.click("link=Home Page");
        selenium.waitForPageToLoad(WAIT_TIME);
    }

    public void testSuperclass() throws InterruptedException {
        openStartPage();
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a[@id='1:3:0:5:7:9:button']/span");
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a[@id='1:2:button']/span");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail(TIMEOUT);
            }
            try {
                if (selenium.isElementPresent("itemlist:3:item:modalwindow:content:form:fields:1:editor:edit")) {
                    break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        selenium.type("itemlist:3:item:modalwindow:content:form:fields:1:editor:edit", "Yummy");
        selenium.click("1:2:buttonArgPanel:okButton");
        selenium.waitForPageToLoad(WAIT_TIME);
        assertTrue(selenium.isTextPresent("Created Kwantu Class 'Yummy'"));
        selenium.click("//a[@id='1:3:1:8:10:button']/span");
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a[@id='1:3:button']/span");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail(TIMEOUT);
            }
            try {
                if (selenium.isElementPresent("itemlist:6:item:modalwindow:content:form:fields:1:editor:edit")) {
                    break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        selenium.select("itemlist:6:item:modalwindow:content:form:fields:1:editor:edit", "label=Dummy");
        selenium.click("1:3:buttonArgPanel:okButton");
        selenium.waitForPageToLoad(WAIT_TIME);
        assertTrue(selenium.isTextPresent("Set 'Dummy' as superclass of 'Yummy'"));
    }

    public void testAddAttribute() throws InterruptedException {
        openStartPage();
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a[@id='1:3:0:5:7:9:button']/span");
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a[@id='1:3:0:8:10:button']/span");
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a[@id='1:6:button']/span");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail(TIMEOUT);
            }
            try {
                if (selenium.isElementPresent("itemlist:14:item:modalwindow:content:form:fields:1:editor:edit")) {
                    break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        selenium.type("itemlist:14:item:modalwindow:content:form:fields:1:editor:edit", "practise");
        selenium.type("itemlist:14:item:modalwindow:content:form:fields:1:editor:edit", "practise");
        selenium.select("itemlist:14:item:modalwindow:content:form:fields:3:editor:edit", "label=STRING");
        selenium.click("1:6:buttonArgPanel:okButton");
        selenium.waitForPageToLoad(WAIT_TIME);
        assertTrue(selenium.isTextPresent("Created attribute 'practise' with type 'STRING'"));
        selenium.click("//a[@id='1:6:button']/span");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail(TIMEOUT);
            }
            try {
                if (selenium.isElementPresent("itemlist:14:item:modalwindow:content:form:fields:1:editor:edit")) {
                    break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        selenium.type("itemlist:14:item:modalwindow:content:form:fields:1:editor:edit", "practice");
        selenium.type("itemlist:14:item:modalwindow:content:form:fields:2:editor:edit", "Practice");
        selenium.select("itemlist:14:item:modalwindow:content:form:fields:3:editor:edit", "label=INTEGER");
        selenium.click("1:6:buttonArgPanel:okButton");
        selenium.waitForPageToLoad(WAIT_TIME);
        assertTrue(selenium.isTextPresent("Created attribute 'practice' with type 'INTEGER'"));
        selenium.click("//a[@id='1:7:1:19:21:button']/span");
        selenium.waitForPageToLoad(WAIT_TIME);
        assertTrue(selenium.isTextPresent("Deleted attribute 'practise'"));
        selenium.click("//a[@id='1:7:0:18:20:button']/span");
        for (int second = 0; ; second++) {
            if (second >= 60) {
                fail(TIMEOUT);
            }
            try {
                if (selenium.isElementPresent("itemlist:15:item:table:rows:4:cells:3:cell:form:itemlist:0:item:modalwindow:content:form:fields:3:editor:edit")) {
                    break;
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            Thread.sleep(1000);
        }
        selenium.select("itemlist:15:item:table:rows:4:cells:3:cell:form:itemlist:0:item:modalwindow:content:form:fields:3:editor:edit", "label=STRING");
        selenium.click("1:7:0:18:20:buttonArgPanel:okButton");
        selenium.waitForPageToLoad(WAIT_TIME);
        assertTrue(selenium.isTextPresent("Updated attribute 'practice' with type 'STRING'"));
    }

    public void testDeleteModel() {
        openStartPage();
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a[@id='1:3:0:5:7:15:button']/span");
        selenium.waitForPageToLoad(WAIT_TIME);
        assertTrue(selenium.isTextPresent("Deleted Model 'RegressionTest'"));
    }

    private void openStartPage() {
        selenium.open("/zakwantu/");
        selenium.click("link=Home Page");
        selenium.waitForPageToLoad(WAIT_TIME);
        selenium.click("//a/span");
    }
}
