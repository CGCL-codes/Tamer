package net.admin4j.ui.filters;

import net.admin4j.util.notify.NotifierTestingMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPerformanceMonitoringFilter extends BaseFilterTestSupport {

    private PerformanceMonitoringFilter filter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.mockConfig.addInitParameter("notifier", NotifierTestingMock.class.getName());
        filter = new PerformanceMonitoringFilter();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInit() throws Exception {
        filter.init(this.mockConfig);
    }

    @Test
    public void test() throws Exception {
        this.mockConfig.addInitParameter("notification.threshold.in.millis", "100");
        filter.init(this.mockConfig);
        MockFilterChain mockChain = new MockFilterChain();
        mockChain.setSleepTimeInMillis(0L);
        filter.doFilter(this.mockRequest, null, mockChain);
        Assert.assertTrue("No notification", ((NotifierTestingMock) filter.notifier).getMessage() == null);
        mockChain.setSleepTimeInMillis(200L);
        filter.doFilter(this.mockRequest, null, mockChain);
        Assert.assertTrue("No notification", ((NotifierTestingMock) filter.notifier).getMessage() != null);
        System.out.println(((NotifierTestingMock) filter.notifier).getMessage());
    }
}
