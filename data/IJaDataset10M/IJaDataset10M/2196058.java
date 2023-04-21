package com.ideo.sweetdevria.selenium.scenarios;

import com.ideo.sweetdevria.selenium.common.CommonSeleniumTestCase;

/**
 * @author Yannis Julienne
 *
 */
public class DataGridDetailsTestScenario extends CommonSeleniumTestCase {

    public static String APPLI_CONTEXT_ROOT = "sweetdev-ria-gettingStarted/";

    public static String page = "layout.jsp?url=%2Fdatagrid%2Fgrid_detail.jsp";

    private String url;

    public DataGridDetailsTestScenario(String host, String browserName, int port, String url) {
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
        assertEquals("The title should be : The detail tag", "The detail tag", browser.getText("//div[@class='title']"));
    }

    /**
     * visibility testing
     */
    public void testVisibility() {
        assertTrue("The detailed grid is not visible", isVisible("griddetail"));
        assertTrue("The detail 0 link is not visible", isVisible("griddetail_0_detailIcon"));
        assertTrue("The detail 7 link is not visible", isVisible("griddetail_7_detailIcon"));
        assertTrue("The detail 14 link is not visible", isVisible("griddetail_14_detailIcon"));
    }

    /**
     * test the grid details opening
     */
    public void testDetailsOpening() {
        verifyTrue(browser.getAttribute("griddetail_0_detailIcon@class").contains("ideo-ndg-detailIconClose"));
        verifyTrue(browser.getAttribute("griddetail_7_detailIcon@class").contains("ideo-ndg-detailIconClose"));
        verifyTrue(browser.getAttribute("griddetail_14_detailIcon@class").contains("ideo-ndg-detailIconClose"));
        verifyFalse(isVisible("griddetail_detail_0"));
        verifyFalse(isVisible("griddetail_detail_7"));
        verifyFalse(isVisible("griddetail_detail_14"));
        browser.click("griddetail_0_detailIcon");
        browser.click("griddetail_7_detailIcon");
        browser.click("griddetail_14_detailIcon");
        assertTrue("The detail 0 link should have ideo-ndg-detailIconOpen in its class attribute", browser.getAttribute("griddetail_0_detailIcon@class").contains("ideo-ndg-detailIconOpen"));
        assertTrue("The detail 7 link should have ideo-ndg-detailIconOpen in its class attribute", browser.getAttribute("griddetail_7_detailIcon@class").contains("ideo-ndg-detailIconOpen"));
        assertTrue("The detail 14 link should have ideo-ndg-detailIconOpen in its class attribute", browser.getAttribute("griddetail_14_detailIcon@class").contains("ideo-ndg-detailIconOpen"));
        assertTrue("The detail 0 is not visible", isVisible("griddetail_detail_0"));
        assertEquals("The detail 0 does not contain the right message", "This extraordinary singer-songwriter explores the death of her own child in heartbreaking compositions.", browser.getText("griddetail_detail_0"));
        assertTrue("The detail 7 is not visible", isVisible("griddetail_detail_7"));
        assertEquals("The detail 7 does not contain the right message", "Few bands can take the passion and energy of emo and make something new with it", browser.getText("griddetail_detail_7"));
        assertTrue("The detail 14 is not visible", isVisible("griddetail_detail_14"));
        assertEquals("The detail 14 does not contain the right message", "erle Haggards just an Okie from Muskogee--whats Eddie G.?", browser.getText("griddetail_detail_14"));
        browser.click("griddetail_0_detailIcon");
        browser.click("griddetail_7_detailIcon");
        browser.click("griddetail_14_detailIcon");
    }

    /**
     * test the grid details closing
     */
    public void testDetailsClosing() {
        browser.click("griddetail_0_detailIcon");
        browser.click("griddetail_7_detailIcon");
        browser.click("griddetail_14_detailIcon");
        verifyTrue(browser.getAttribute("griddetail_0_detailIcon@class").contains("ideo-ndg-detailIconOpen"));
        verifyTrue(browser.getAttribute("griddetail_7_detailIcon@class").contains("ideo-ndg-detailIconOpen"));
        verifyTrue(browser.getAttribute("griddetail_14_detailIcon@class").contains("ideo-ndg-detailIconOpen"));
        verifyTrue(isVisible("griddetail_detail_0"));
        verifyTrue(isVisible("griddetail_detail_7"));
        verifyTrue(isVisible("griddetail_detail_14"));
        browser.click("griddetail_0_detailIcon");
        browser.click("griddetail_7_detailIcon");
        browser.click("griddetail_14_detailIcon");
        assertTrue("The detail 0 link should have ideo-ndg-detailIconClose in its class attribute", browser.getAttribute("griddetail_0_detailIcon@class").contains("ideo-ndg-detailIconClose"));
        assertTrue("The detail 7 link should have ideo-ndg-detailIconClose in its class attribute", browser.getAttribute("griddetail_7_detailIcon@class").contains("ideo-ndg-detailIconClose"));
        assertTrue("The detail 14 link should have ideo-ndg-detailIconClose in its class attribute", browser.getAttribute("griddetail_14_detailIcon@class").contains("ideo-ndg-detailIconClose"));
        assertFalse("The detail 0 should not be visible", isVisible("griddetail_detail_0"));
        assertFalse("The detail 7 should not be visible", isVisible("griddetail_detail_7"));
        assertFalse("The detail 14 should not be visible", isVisible("griddetail_detail_14"));
    }
}
