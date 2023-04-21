package net.admin4j.ui.filters;

import net.admin4j.util.notify.NotifierTestingMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestConcurrentUsageFilter extends BaseFilterTestSupport {

    private ConcurrentUsageFilter filter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.mockConfig.addInitParameter("notifier", NotifierTestingMock.class.getName());
        filter = new ConcurrentUsageFilter();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        this.mockConfig.addInitParameter("sleep.interval.millis", "20000");
        this.mockConfig.addInitParameter("alert.levels", "50");
        filter.init(this.mockConfig);
        MockFilterChain mockChain = new MockFilterChain();
        mockChain.setSleepTimeInMillis(0L);
        filter.doFilter(this.mockRequest, null, mockChain);
        Assert.assertTrue("No notification", ((NotifierTestingMock) filter.notifier).getMessage() == null);
    }
}
