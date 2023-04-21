package com.ttdev.wicketpagetest.sample;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.thoughtworks.selenium.DefaultSelenium;
import com.ttdev.wicketpagetest.WebPageTestContext;

@Test
public class Page3Test {

    @BeforeMethod
    public void mockLogin() {
        MyApp app = (MyApp) WebPageTestContext.getWebApplication();
        app.setMockedUser("u1");
    }

    @AfterMethod
    public void mockLogout() {
        MyApp app = (MyApp) WebPageTestContext.getWebApplication();
        app.setMockedUser(null);
    }

    public void testAlreadyLoggedIn() {
        DefaultSelenium selenium = WebPageTestContext.getSelenium();
        selenium.open("?wicket:bookmarkablePage=:com.ttdev.wicketpagetest.sample.Page3");
        assert selenium.isTextPresent("u1");
    }

    public void testNeedToLogIn() {
        mockLogout();
        DefaultSelenium selenium = WebPageTestContext.getSelenium();
        selenium.open("?wicket:bookmarkablePage=:com.ttdev.wicketpagetest.sample.Page3");
        assert selenium.isTextPresent("Please login");
    }
}
