package com.totalchange.wtframework;

import org.junit.Test;
import static org.junit.Assert.*;
import com.totalchange.wtframework.Wtf;
import com.totalchange.wtframework.testplan.WtfTestPlan;
import com.totalchange.wtframework.testplan.WtfTestUrl;
import com.totalchange.wtframework.testplan.WtfTestUrls;

public class WtfTest {

    @Test
    public void runTestPlan() {
        WtfTestUrl url = new WtfTestUrl();
        url.setAddress(WtfTestUtils.urlToTestResource(""));
        WtfTestUrls urls = new WtfTestUrls();
        urls.getUrl().add(url);
        WtfTestPlan plan = new WtfTestPlan();
        plan.setUrls(urls);
        Wtf wtf = new Wtf();
        wtf.runTestPlan(plan);
        assertTrue(wtf.getReport().getTotalErrors() <= 0);
    }
}
