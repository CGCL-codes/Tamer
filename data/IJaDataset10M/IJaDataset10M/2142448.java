package org.mattressframework.core.providers.entity;

import org.junit.Before;
import org.junit.Test;
import org.mattressframework.test.providers.ProviderTestUtils;

public class ByteArrayHttpEntityProviderTest {

    private static class MyByteArrayHttpEntityProvider extends AbstractByteArrayHttpEntityProvider {
    }

    private AbstractByteArrayHttpEntityProvider provider;

    @Before
    public void before() throws Exception {
        provider = new MyByteArrayHttpEntityProvider();
    }

    @Test
    public void testSupportsAllTypes() throws Exception {
        ProviderTestUtils.testSupportsType(provider, AbstractByteArrayHttpEntityProvider.SUPPORTED_TYPE);
    }
}
