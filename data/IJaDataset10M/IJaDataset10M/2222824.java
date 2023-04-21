package com.ttdev.wicketpagetest.sample.spring;

import org.testng.annotations.Test;
import com.ttdev.wicketpagetest.ByWicketIdPath;
import com.ttdev.wicketpagetest.WebPageTestContext;
import com.ttdev.wicketpagetest.WicketSelenium;

@Test
public class ThrottlingAjaxPageTest {

    public void testEasyWaitingForAjaxWithThrottling() {
        WicketSelenium ws = WebPageTestContext.getWicketSelenium();
        ws.openBookmarkablePage(ThrottlingAjaxPage.class);
        ws.findWicketElement("//input").sendKeys("a");
        ws.findWicketElement("//input").sendKeys("b");
        ws.waitUntilAjaxDone();
        assert ws.getText(new ByWicketIdPath("//output")).equals("2");
    }
}
