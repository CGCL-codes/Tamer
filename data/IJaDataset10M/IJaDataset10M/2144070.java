package org.nomadpim.core.util.filter;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.easymock.EasyMock;

public class CompositeFilterTest extends TestCase {

    public void testIsIFilter() {
        assertTrue(IFilter.class.isAssignableFrom(CompositeFilter.class));
    }

    /**
     * Tests size and getFilter after a filter has been removed from the
     * original collection.
     */
    public void testSizeGetAndImmutable() {
        IFilter filter0 = EasyMock.createNiceMock(IFilter.class);
        IFilter filter1 = EasyMock.createNiceMock(IFilter.class);
        List<IFilter> filters = new ArrayList<IFilter>();
        filters.add(filter0);
        filters.add(filter1);
        CompositeFilter filter = new CompositeFilter(filters);
        filters.clear();
        assertEquals(2, filter.size());
        assertEquals(filter0, filter.getFilter(0));
        assertEquals(filter1, filter.getFilter(1));
    }
}
