package com.ideo.sweetdevria.selenium.scenarios;

import com.ideo.sweetdevria.selenium.common.CommonSeleniumTestCase;

/**
 * @author Yannis Julienne
 *
 */
public abstract class SuggestSubmitTestScenario extends CommonSeleniumTestCase {

    public static String APPLI_CONTEXT_ROOT = "sweetdev-ria-gettingStarted/";

    public static String page = "layout.jsp?url=%2Fsuggest%2Fsuggest_submit.jsp";

    private String url;

    public SuggestSubmitTestScenario(String host, String browserName, int port, String url) {
        super(host, browserName, port, url, page);
        this.url = url + page;
    }

    /**
     * test the location
     */
    public void testLocation() {
        assertEquals(url, browser.getLocation());
    }

    /**
     * test if title is present
     */
    public void testTitle() {
        verifyEquals("SweetDEV RIA show case, play with Ajax tags SweetDEV RIA", browser.getTitle());
        assertEquals("The title should be : Example : Submit your selections.", "Example : Submit your selections.", browser.getText("//div[@class='title']"));
    }

    /**
     * test the visibility of elements
     */
    public void testVisibility() {
        assertTrue("The caseOn suggest main frame is not visible", isVisible("suggest_subsingle_mainA"));
        assertTrue("The caseOn suggest input is not visible", isVisible("suggest_subsingle_input"));
        assertTrue("The caseOff suggest main frame is not visible", isVisible("suggest_submulti_mainA"));
        assertTrue("The caseOff suggest input is not visible", isVisible("suggest_submulti_input"));
        assertTrue("The caseOff suggest button is not visible", isVisible("suggest_submulti_button"));
        assertTrue("The caseOff suggest main frame is not visible", isVisible("suggest_submultimulti_mainA"));
        assertTrue("The caseOff suggest input is not visible", isVisible("suggest_submultimulti_input"));
        assertTrue("The caseOff suggest button is not visible", isVisible("suggest_submultimulti_button"));
        assertFalse("The Single selection suggest popup should not be visible", isVisible("suggest_subsingle_popup"));
        assertFalse("The Multiple selection suggest popup should not be visible", isVisible("suggest_submulti_popup"));
        assertFalse("The Multiple selection + Multiple field suggest popup should not be visible", isVisible("suggest_submultimulti_popup"));
    }

    /**
     * test Single selection send
     */
    public void testSendSingleSelection() {
        browser.type("suggest_subsingle_input", "ca");
        browser.typeKeys("suggest_subsingle_input", "ca");
        pause(500);
        browser.click("suggest_subsingle_item_0");
        pause(500);
        browser.click("//input[@type='submit']");
        browser.waitForPageToLoad("10000");
        assertEquals("The single selection post is not correct", "Posted :Cambodia", browser.getText("singleres"));
    }

    /**
     * test Multi selection send
     */
    public void testSendMultiSelection() {
        browser.type("suggest_submulti_input", "fr");
        browser.typeKeys("suggest_submulti_input", "fr");
        pause(500);
        browser.click("suggest_submulti_item_0");
        browser.click("suggest_submulti_item_2");
        browser.click("suggest_submulti_item_3");
        browser.click("suggest_submulti_button");
        pause(500);
        browser.click("//input[@type='submit']");
        browser.waitForPageToLoad("10000");
        assertEquals("The multi selection post is not correct", "Posted :France;French Polynesia;French Southern Ter", browser.getText("multires"));
    }

    /**
     * test Multi selection and Multi field send
     */
    public void testSendMultiMultiSelection() {
        browser.type("suggest_submultimulti_input", "ca");
        browser.typeKeys("suggest_submultimulti_input", "ca");
        pause(500);
        browser.click("suggest_submultimulti_item_0");
        browser.click("suggest_submultimulti_item_1");
        browser.click("suggest_submultimulti_item_3");
        browser.click("suggest_submultimulti_button");
        pause(500);
        browser.click("//input[@type='submit']");
        browser.waitForPageToLoad("10000");
        assertEquals("The multi-multi selection post is not correct", "Posted :{Cambodia,Cameroon,Canary Islands}", browser.getText("multimultires"));
    }
}
