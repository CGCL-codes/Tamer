package com.ideo.sweetdevria.selenium.scenarios;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.ideo.sweetdevria.selenium.common.CommonSeleniumTestCase;

/**
 * @author Yannis Julienne
 *
 */
public abstract class CalendarSelectionModesTestScenario extends CommonSeleniumTestCase {

    public static String APPLI_CONTEXT_ROOT = "sweetdev-ria-gettingStarted/";

    public static String page = "layout.jsp?url=%2Fcalendar%2Fcalendar_selmodes.jsp";

    private String url;

    private SimpleDateFormat mydf = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

    private Calendar today = Calendar.getInstance();

    public CalendarSelectionModesTestScenario(String host, String browserName, int port, String url) {
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
        assertEquals("The title should be : Modes of selection of the calendar", "Modes of selection of the calendar", browser.getText("//div[@class='title']"));
    }

    /**
     * test the visibility of elements
     */
    public void testVisibility() {
        assertTrue("The standalone calendar main frame is not visible", isVisible("cal1Cal_frame_border"));
        assertTrue("The standalone calendar is not visible", isVisible("cal1Cal"));
        assertEquals("The standalone calendar header is not correct", mydf.format(today.getTime()), browser.getText("cal1CalMonth"));
        assertTrue("The multi select calendar main frame is not visible", isVisible("cal3Cal_frame_border"));
        assertTrue("The multi select calendar is not visible", isVisible("cal3Cal"));
        assertTrue("The multi select calendar today button is not visible", isVisible("cal3CalToday_Button"));
        assertTrue("The multi select calendar clear button is not visible", isVisible("cal3CalClear_Button"));
        assertEquals("The multi select calendar header is not correct", mydf.format(today.getTime()), browser.getText("cal3CalMonth"));
        assertTrue("The range select calendar main frame is not visible", isVisible("cal4Cal_frame_border"));
        assertTrue("The range select calendar is not visible", isVisible("cal4Cal"));
        assertTrue("The range select calendar today button is not visible", isVisible("cal4CalToday_Button"));
        assertTrue("The range select calendar clear button is not visible", isVisible("cal4CalClear_Button"));
        assertEquals("The range select calendar header is not correct", mydf.format(today.getTime()), browser.getText("cal4CalMonth"));
    }

    /**
     * test standalone calendar
     */
    public void testStandaloneCalendar() {
        verifyFalse(browser.getAttribute("//tbody[@id='cal1Caltbody']//td[a='10']@class").contains("selected"));
        browser.mouseOver("//tbody[@id='cal1Caltbody']//td[a='10']");
        assertFalse("The 10th of this month has an incorrect class attribute", browser.getAttribute("//tbody[@id='cal1Caltbody']//td[a='10']@class").contains("calcellhover"));
        browser.click("//tbody[@id='cal1Caltbody']//td[a='10']");
        assertFalse("The 10th of this month should not be selected", browser.getAttribute("//tbody[@id='cal1Caltbody']//td[a='10']@class").contains("selected"));
    }

    /**
     * test multi select calendar
     */
    public void testMultiSelectCalendar() {
        verifyFalse(browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='2']@class").contains("selected"));
        verifyFalse(browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='16']@class").contains("selected"));
        browser.mouseOver("//tbody[@id='cal3Caltbody']//td[a='2']");
        assertTrue("The 2nd of this month has an incorrect class attribute", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='2']@class").contains("calcellhover"));
        browser.click("//tbody[@id='cal3Caltbody']//td[a='2']");
        assertTrue("The 2nd is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='2']@class").contains("selected"));
        browser.mouseOver("//tbody[@id='cal3Caltbody']//td[a='16']");
        assertTrue("The 16th of this month has an incorrect class attribute", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='16']@class").contains("calcellhover"));
        browser.click("//tbody[@id='cal3Caltbody']//td[a='16']");
        assertTrue("The 16th is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='16']@class").contains("selected"));
        assertTrue("The 2nd is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='2']@class").contains("selected"));
        browser.mouseDown("cal3CalnavMonthRight");
        assertFalse("The 16th should not be selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='16']@class").contains("selected"));
        assertFalse("The 2nd should not be selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='2']@class").contains("selected"));
        browser.mouseOver("//tbody[@id='cal3Caltbody']//td[a='5']");
        assertTrue("The 5th of this month has an incorrect class attribute", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='5']@class").contains("calcellhover"));
        browser.click("//tbody[@id='cal3Caltbody']//td[a='5']");
        assertTrue("The 5th is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='5']@class").contains("selected"));
        browser.mouseDown("cal3CalnavMonthLeft");
        assertTrue("The 16th is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='16']@class").contains("selected"));
        assertTrue("The 2nd is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='2']@class").contains("selected"));
        assertFalse("The 5th should not be selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='5']@class").contains("selected"));
        browser.click("//tbody[@id='cal3Caltbody']//td[a='2']");
        assertFalse("The 2nd should not be selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='2']@class").contains("selected"));
        assertTrue("The 16th is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='16']@class").contains("selected"));
        browser.mouseDown("cal3CalnavMonthRight");
        assertTrue("The 5th is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='5']@class").contains("selected"));
        browser.click("//tbody[@id='cal3Caltbody']//td[a='5']");
        assertFalse("The 2nd should not be selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='5']@class").contains("selected"));
        browser.mouseDown("cal3CalnavMonthLeft");
        browser.click("//tbody[@id='cal3Caltbody']//td[a='19']");
        assertTrue("The 19th is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='19']@class").contains("selected"));
        browser.mouseDown("cal3CalToday");
        assertTrue("Today is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='" + today.get(Calendar.DAY_OF_MONTH) + "']@class").contains("selected"));
        assertFalse("The 19th should not be selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='19']@class").contains("selected"));
        browser.mouseDown("cal3CalnavMonthLeft");
        browser.click("//tbody[@id='cal3Caltbody']//td[a='4']");
        assertTrue("The 4th is not selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='4']@class").contains("selected"));
        browser.mouseDown("cal3CalClear");
        Calendar prevMonth = Calendar.getInstance();
        prevMonth.roll(Calendar.MONTH, false);
        assertEquals("The multi select calendar is not on the correct month", mydf.format(prevMonth.getTime()), browser.getText("cal3CalMonth"));
        assertFalse("The 4th should not be selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='4']@class").contains("selected"));
        browser.mouseDown("cal3CalnavMonthRight");
        assertFalse("Today should not be selected", browser.getAttribute("//tbody[@id='cal3Caltbody']//td[a='" + today.get(Calendar.DAY_OF_MONTH) + "']@class").contains("selected"));
    }

    /**
     * test range select calendar submit
     */
    public void testRangeSelectSubmit() {
        verifyFalse(browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='6']@class").contains("selected"));
        verifyFalse(browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='13']@class").contains("rangeSelected"));
        verifyFalse(browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='21']@class").contains("selected"));
        browser.mouseOver("//tbody[@id='cal4Caltbody']//td[a='6']");
        assertTrue("The 6th of this month has an incorrect class attribute", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='6']@class").contains("calcellhover"));
        browser.click("//tbody[@id='cal4Caltbody']//td[a='6']");
        assertTrue("The 6th is not selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='6']@class").contains("selected"));
        browser.click("//tbody[@id='cal4Caltbody']//td[a='21']");
        assertTrue("The 21st is not selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='21']@class").contains("selected"));
        assertTrue("The 6th is not selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='6']@class").contains("selected"));
        for (int day = 7; day < 21; day++) {
            browser.click("//tbody[@id='cal4Caltbody']//td[a='" + day + "']");
            assertTrue("The " + day + " is not range selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='" + day + "']@class").contains("rangeSelected"));
        }
        browser.click("//tbody[@id='cal4Caltbody']//td[a='21']");
        assertFalse("The 21st should not be selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='21']@class").contains("selected"));
        assertTrue("The 6th is not selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='6']@class").contains("selected"));
        for (int day = 7; day < 21; day++) {
            assertFalse("The " + day + " should not be range selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='" + day + "']@class").contains("rangeSelected"));
        }
        browser.mouseDown("cal4CalnavMonthRight");
        browser.click("//tbody[@id='cal4Caltbody']//td[a='13']");
        assertTrue("The 13th is not selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='13']@class").contains("selected"));
        for (int day = 1; day < 13; day++) {
            browser.click("//tbody[@id='cal4Caltbody']//td[a='" + day + "']");
            assertTrue("The " + day + " is not range selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='" + day + "']@class").contains("rangeSelected"));
        }
        browser.mouseDown("cal4CalnavMonthLeft");
        for (int day = 7; day < today.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
            assertTrue("The " + day + " should not be range selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='" + day + "']@class").contains("rangeSelected"));
        }
        browser.click("//tbody[@id='cal4Caltbody']//td[a='6']");
        assertFalse("The 6th should not be selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='6']@class").contains("selected"));
        for (int day = 7; day < today.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
            assertFalse("The " + day + " should not be range selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='" + day + "']@class").contains("rangeSelected"));
        }
        browser.mouseDown("cal4CalnavMonthRight");
        for (int day = 1; day < 13; day++) {
            assertFalse("The " + day + " is not range selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='" + day + "']@class").contains("rangeSelected"));
        }
        browser.click("//tbody[@id='cal4Caltbody']//td[a='13']");
        assertFalse("The 13th should not be selected", browser.getAttribute("//tbody[@id='cal4Caltbody']//td[a='13']@class").contains("selected"));
        browser.mouseDown("cal4CalnavMonthLeft");
    }
}
