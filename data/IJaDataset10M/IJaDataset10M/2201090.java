package com.unitedinternet.portal.selenium.utils.logging;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

public class TestMetricsBeanTest {

    @Test
    public void getLoggingSeleniumVersion_shouldAlwaysPass() {
        TestMetricsBean myTestBean = new TestMetricsBean();
        String version = myTestBean.getLoggingSeleniumVersion();
        assertThat(version).isNotNull().isNotEmpty().doesNotMatch("unknown");
    }

    @Ignore("expected version has to be maintained manually")
    @Test
    public void getLoggingSeleniumVersion_currentVersionHasToBeMaintainedManually() {
        TestMetricsBean myTestBean = new TestMetricsBean();
        String version = myTestBean.getLoggingSeleniumVersion();
        assertEquals("1.3-SNAPSHOT", version);
    }
}
