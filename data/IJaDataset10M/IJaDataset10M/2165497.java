package com.thoughtworks.selenium.corebased;

import com.thoughtworks.selenium.*;

/**
 * @author XlateHtmlSeleneseToJava
 * Generated from E:\buildroot\Dev\selenium-rc_svn\trunk\rc\clients\java\target\selenium-server\tests/TestCheckUncheck.html.
 */
public class TestCheckUncheck extends SeleneseTestCase {

    public void testCheckUncheck() throws Throwable {
        selenium.setContext("Test check/uncheck of toggle-buttons", "info");
        selenium.open("../tests/html/test_check_uncheck.html");
        assertTrue(selenium.isChecked("base-spud"));
        assertTrue(!selenium.isChecked("base-rice"));
        assertTrue(selenium.isChecked("option-cheese"));
        assertTrue(!selenium.isChecked("option-onions"));
        selenium.check("base-rice");
        assertTrue(!selenium.isChecked("base-spud"));
        assertTrue(selenium.isChecked("base-rice"));
        selenium.uncheck("option-cheese");
        assertTrue(!selenium.isChecked("option-cheese"));
        selenium.check("option-onions");
        assertTrue(selenium.isChecked("option-onions"));
        assertTrue(!selenium.isChecked("option-chilli"));
        selenium.check("option chilli");
        assertTrue(selenium.isChecked("option-chilli"));
        selenium.uncheck("option index=3");
        assertTrue(!selenium.isChecked("option-chilli"));
        checkForVerificationErrors();
    }
}
