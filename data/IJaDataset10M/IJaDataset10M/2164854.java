package org.mattressframework.ext.providers.entity;

import org.junit.Test;
import org.mattressframework.test.providers.ProviderTestUtils;

public class RuntimeHttpEntityProviderTest {

    private static class MyRuntimeProvider extends AbstractRuntimeHttpEntityProvider {

        protected MyRuntimeProvider(final Class<?>... parameterTypes) {
            super(parameterTypes);
        }
    }

    private AbstractRuntimeHttpEntityProvider provider;

    @Test
    public void testSupportsType() throws Exception {
        provider = new MyRuntimeProvider(String.class);
        ProviderTestUtils.testSupportsType(provider, String.class);
        provider = new MyRuntimeProvider(int.class);
        ProviderTestUtils.testSupportsType(provider, int.class);
        provider = new MyRuntimeProvider(String.class, Boolean.class);
        ProviderTestUtils.testSupportsType(provider, String.class, Boolean.class);
    }

    @Test
    public void testSupportsType_AllTypes() throws Exception {
        provider = new MyRuntimeProvider();
        ProviderTestUtils.testSupportsAllTypes(provider);
        provider = new MyRuntimeProvider((Class<?>) null);
        ProviderTestUtils.testSupportsAllTypes(provider);
        provider = new MyRuntimeProvider(new Class<?>[0]);
        ProviderTestUtils.testSupportsAllTypes(provider);
    }
}
