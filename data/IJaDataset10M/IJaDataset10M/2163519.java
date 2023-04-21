package com.ttdev.wicketpagetest.sample.guice;

import org.testng.annotations.Test;
import com.thoughtworks.selenium.DefaultSelenium;
import com.ttdev.wicketpagetest.MockableGuiceBeanInjector;
import com.ttdev.wicketpagetest.WebPageTestContext;
import com.ttdev.wicketpagetest.WicketSelenium;

@Test
public class Page6Test {

    public void testEasyWaitingForAjax() {
        MockableGuiceBeanInjector.mockBean("service", new CalcService() {

            public int calcNext(int current) {
                return current + 1;
            }
        });
        DefaultSelenium selenium = WebPageTestContext.getSelenium();
        selenium.open("?wicket:bookmarkablePage=:com.ttdev.wicketpagetest.sample.guice.Page6");
        assert selenium.getText("output").equals("Current: 0");
        WicketSelenium ws = new WicketSelenium(selenium);
        selenium.click("link=Calculate next");
        ws.waitUntilAjaxDone();
        assert selenium.getText("output").equals("Current: 1");
        selenium.click("link=Calculate next");
        ws.waitUntilAjaxDone();
        assert selenium.getText("output").equals("Current: 2");
    }
}
