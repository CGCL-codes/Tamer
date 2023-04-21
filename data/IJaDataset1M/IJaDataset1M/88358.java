package com.ttdev.wicketpagetest.sample;

import org.testng.annotations.Test;
import com.thoughtworks.selenium.DefaultSelenium;
import com.ttdev.wicketpagetest.WebPageTestContext;

@Test
public class Page4Test {

    public void testOverrideWebXml() {
        DefaultSelenium selenium = WebPageTestContext.getSelenium();
        selenium.open("?wicket:bookmarkablePage=:com.ttdev.wicketpagetest.sample.Page4");
        assert selenium.getText("p1").equals("foo");
    }
}
