package com.ttdev.wicketpagetest.sample.spring;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.ttdev.wicketpagetest.ByWicketIdPath;
import com.ttdev.wicketpagetest.WebPageTestContext;
import com.ttdev.wicketpagetest.WicketSelenium;

@Test
public class PageExtractedByWicketIdsTest {

    public void testWicketLocator() {
        WicketSelenium ws = WebPageTestContext.getWicketSelenium();
        ws.openBookmarkablePage(PageExtractedByWicketIds.class);
        assert ws.getValue(new ByWicketIdPath("//eachRow[0]//v")).equals("3");
        assert ws.getValue(new ByWicketIdPath("//eachRow[1]//v")).equals("2");
        assert ws.getValue(new ByWicketIdPath("//eachRow[2]//v")).equals("8");
        assert ws.getText(new ByWicketIdPath("//total")).equals("13");
        WebElement v1 = ws.findWicketElement("//eachRow[1]//v");
        v1.clear();
        v1.sendKeys("5");
        WebElement ok = ws.findWicketElement("//eachRow[1]//ok");
        ok.click();
        ws.waitUntilAjaxDone();
        assert ws.getText(new ByWicketIdPath("//total")).equals("16");
        assert ws.getText(new ByWicketIdPath("//id.containing.dots")).equals("xyz");
    }
}
