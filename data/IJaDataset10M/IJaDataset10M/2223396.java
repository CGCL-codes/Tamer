package com.ideo.sweetdevria.selenium.scenarios;

import com.ideo.sweetdevria.selenium.common.CommonSeleniumTestCase;

/**
 * @author Yannis Julienne
 *
 */
public abstract class SuggestMultipleStackTestScenario extends CommonSeleniumTestCase {

    public static String APPLI_CONTEXT_ROOT = "sweetdev-ria-gettingStarted/";

    public static String page = "layout.jsp?url=%2Fsuggest%2Fsuggest_seloptions.jsp";

    private String url;

    public SuggestMultipleStackTestScenario(String host, String browserName, int port, String url) {
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
        assertEquals("The title should be : Example : Customize the way the values are selected.", "Example : Customize the way the values are selected.", browser.getText("//div[@class='title']"));
    }

    /**
     * test the visibility of elements
     */
    public void testVisibility() {
        assertTrue("The suggest main frame is not visible", isVisible("suggest_selopts_mainA"));
        assertTrue("The suggest input is not visible", isVisible("suggest_selopts_input"));
        assertTrue("The suggest button is not visible", isVisible("suggest_selopts_button"));
        assertFalse("The suggest popup should not be visible", isVisible("suggest_selopts_popup"));
    }

    /**
     * test multiple stack selection
     */
    public void testMultipleStackSelection() {
        browser.type("suggest_selopts_input", "fr");
        browser.typeKeys("suggest_selopts_input", "fr");
        pause(500);
        verifyTrue(isVisible("suggest_selopts_popup"));
        browser.click("suggest_selopts_item_0");
        assertTrue("The suggest popup should not be visible", isVisible("suggest_selopts_popup"));
        assertTrue("Item0 should be selected", browser.getAttribute("suggest_selopts_item_0@class").contains("ideo-sug-selectedItem"));
        assertEquals("The suggest input has not the correct text", "1 items selected", browser.getValue("suggest_selopts_input"));
        browser.click("suggest_selopts_item_2");
        assertTrue("Item0 should be selected", browser.getAttribute("suggest_selopts_item_2@class").contains("ideo-sug-selectedItem"));
        assertEquals("The suggest input has not the correct text", "2 items selected", browser.getValue("suggest_selopts_input"));
        browser.click("//div[@id='suggest_selopts_header']/a");
        pause(500);
        assertEquals("The suggest header is not correct", "Show suggestions", browser.getText("//div[@id='suggest_selopts_header']/a"));
        assertEquals("There should be 2 selections", 2, browser.getXpathCount("//ul[@id='suggest_selopts_suggest']/li"));
        assertTrue("Item0 should be visible", isVisible("suggest_selopts_item_0"));
        assertEquals("Item0 text is not correct", "France", browser.getText("suggest_selopts_item_0"));
        assertTrue("Item1 should be visible", isVisible("suggest_selopts_item_1"));
        assertEquals("Item1 text is not correct", "French Polynesia", browser.getText("suggest_selopts_item_1"));
        browser.type("suggest_selopts_input", "ca");
        browser.typeKeys("suggest_selopts_input", "ca");
        pause(500);
        assertEquals("The suggest header is not correct", "Show selections", browser.getText("//div[@id='suggest_selopts_header']/a"));
        browser.click("suggest_selopts_item_1");
        pause(500);
        assertTrue("Item1 should be selected", browser.getAttribute("suggest_selopts_item_1@class").contains("ideo-sug-selectedItem"));
        assertEquals("The suggest input has not the correct text", "3 items selected", browser.getValue("suggest_selopts_input"));
        browser.click("suggest_selopts_item_3");
        pause(500);
        assertTrue("Item3 should be selected", browser.getAttribute("suggest_selopts_item_3@class").contains("ideo-sug-selectedItem"));
        assertEquals("The suggest input has not the correct text", "4 items selected", browser.getValue("suggest_selopts_input"));
        browser.click("//div[@id='suggest_selopts_header']/a");
        pause(500);
        assertEquals("There should be 4 selections", 4, browser.getXpathCount("//ul[@id='suggest_selopts_suggest']/li"));
        assertTrue("Item0 should be visible", isVisible("suggest_selopts_item_0"));
        assertEquals("Item0 text is not correct", "France", browser.getText("suggest_selopts_item_0"));
        assertTrue("Item1 should be visible", isVisible("suggest_selopts_item_1"));
        assertEquals("Item1 text is not correct", "French Polynesia", browser.getText("suggest_selopts_item_1"));
        assertTrue("Item2 should be visible", isVisible("suggest_selopts_item_2"));
        assertEquals("Item2 text is not correct", "Cameroon", browser.getText("suggest_selopts_item_2"));
        assertTrue("Item3 should be visible", isVisible("suggest_selopts_item_3"));
        assertEquals("Item3 text is not correct", "Canary Islands", browser.getText("suggest_selopts_item_3"));
        browser.click("suggest_selopts_item_1");
        pause(500);
        assertTrue("The item1 should not be selected", browser.getAttribute("suggest_selopts_item_1@class").contains("ideo-sug-unselectedItem"));
        browser.click("//div[@id='suggest_selopts_header']/a");
        pause(500);
        browser.click("//div[@id='suggest_selopts_header']/a");
        pause(500);
        assertEquals("There should be 3 selections", 3, browser.getXpathCount("//ul[@id='suggest_selopts_suggest']/li"));
        browser.click("//div[@id='suggest_selopts_header']/a");
        pause(500);
        assertTrue("Item3 should be selected", browser.getAttribute("suggest_selopts_item_3@class").contains("ideo-sug-selectedItem"));
        browser.click("suggest_selopts_item_3");
        browser.click("//div[@id='suggest_selopts_header']/a");
        pause(500);
        assertEquals("There should be 2 selections", 2, browser.getXpathCount("//ul[@id='suggest_selopts_suggest']/li"));
        assertEquals("The suggest input has not the correct text", "2 items selected", browser.getValue("suggest_selopts_input"));
    }
}
