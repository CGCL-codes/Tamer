package dash.obtain.provider.builtin;

import junit.framework.TestCase;
import dash.obtain.provider.ObtainLookup;

public class DefaultProviderStrategyTest extends TestCase {

    DefaultProviderStrategy provider = new DefaultProviderStrategy();

    public void testGlobals() throws Exception {
        AggregatingProvider aggProvider = (AggregatingProvider) provider.delegate;
        assertEquals(3, aggProvider.providers.size());
        assertTrue(aggProvider.providers.get(0) instanceof ThreadLocalProvider);
        assertTrue(aggProvider.providers.get(1) instanceof KeyIndirectorProviderStrategy);
    }

    public void testNullLookup() throws Exception {
        Object o = provider.lookup(new ObtainLookup(this, "foo", null, null, this.getClass()));
        assertNull(o);
    }
}
