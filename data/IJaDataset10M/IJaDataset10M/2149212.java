package org.mattressframework.ext.providers.parameter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mattressframework.test.providers.ProviderTestUtils;
import org.springframework.mock.web.MockHttpServletRequest;

public class RemoteAddrParameterProviderTest {

    private static final String TEST_REMOTE_ADDR = "TEST_REMOTE_ADDR";

    private RemoteAddrParameterProvider provider;

    @Before
    public void before() throws Exception {
        provider = new RemoteAddrParameterProvider();
    }

    @Test
    public void testObtainInstance() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(TEST_REMOTE_ADDR);
        Object obj = provider.obtainInstance(null, null, request, null);
        Assert.assertTrue(obj instanceof String);
        Assert.assertEquals(TEST_REMOTE_ADDR, obj);
    }

    @Test
    public void testSupportsType() throws Exception {
        ProviderTestUtils.testSupportsType(provider, String.class);
    }
}
