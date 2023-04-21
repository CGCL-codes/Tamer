package com.ideo.sweetdevria.selenium.scenarios;

import com.ideo.sweetdevria.selenium.common.CommonSeleniumTestCase;

/**
 * @author Yannis Julienne
 *
 */
public abstract class ControlDataLoadingTestScenario extends CommonSeleniumTestCase {

    public static String APPLI_CONTEXT_ROOT = "sweetdev-ria-gettingStarted/";

    public static String page = "layout.jsp?url=%2Fcontrol%2Fcontrol_dataload.jsp";

    private String url;

    public ControlDataLoadingTestScenario(String host, String browserName, int port, String url) {
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
        assertEquals("The title should be : Example : Display an control loading some select's data in your page.", "Example : Display an control loading some select's data in your page.", browser.getText("//div[@class='title']"));
    }

    /**
     * test the visibility of elements
     */
    public void testVisibility() {
        assertTrue("The continent select is not visible", isVisible("continent_select"));
        assertEquals("The continent selected label is not correct", "", browser.getSelectedLabel("continent_select"));
        assertTrue("The country select is not visible", isVisible("country_select"));
        String countryLabel = browser.getSelectedLabel("country_select");
        assertEquals("The country selected label is not correct", "Select a continent", countryLabel);
    }

    /**
     * test choose continent
     */
    public void testChooseContinent() {
        verifyEquals("void", browser.getSelectedValue("continent_select"));
        verifyEquals("continent", browser.getSelectedValue("country_select"));
        verifyEquals(7, browser.getXpathCount("//select[@name='continent_select']/option"));
        verifyEquals(1, browser.getXpathCount("//select[@name='country_select']/option"));
        browser.select("continent_select", "value=africa");
        assertEquals("africa", browser.getSelectedValue("continent_select"));
        assertEquals("The count of country select options is not correct", 50, browser.getXpathCount("//select[@name='country_select']/option"));
        assertEquals("Algeria is not selected", "Algeria", browser.getSelectedValue("country_select"));
        browser.select("continent_select", "value=oceania");
        assertEquals("oceania", browser.getSelectedValue("continent_select"));
        assertEquals("The count of country select options is not correct", 14, browser.getXpathCount("//select[@name='country_select']/option"));
        assertEquals("Australia is not selected", "Australia", browser.getSelectedValue("country_select"));
        browser.select("continent_select", "value=void");
    }

    /**
     * test choose void
     */
    public void testChooseVoid() {
        browser.select("continent_select", "value=asia");
        verifyEquals("asia", browser.getSelectedValue("continent_select"));
        verifyEquals("Afghanistan", browser.getSelectedValue("country_select"));
        verifyEquals(7, browser.getXpathCount("//select[@name='continent_select']/option"));
        verifyEquals(48, browser.getXpathCount("//select[@name='country_select']/option"));
        browser.select("continent_select", "value=void");
        assertEquals("void", browser.getSelectedValue("continent_select"));
        assertEquals("", browser.getSelectedLabel("continent_select"));
        assertEquals("The count of country select options is not correct", 1, browser.getXpathCount("//select[@name='country_select']/option"));
        assertEquals("The country selected label is not correct", "Select a continent", browser.getSelectedLabel("country_select"));
    }
}
