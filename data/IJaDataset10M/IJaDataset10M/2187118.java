package net.sourceforge.nattable.blink;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sourceforge.nattable.data.ListDataProvider;
import net.sourceforge.nattable.layer.event.PropertyUpdateEvent;
import net.sourceforge.nattable.test.fixture.data.RowDataFixture;
import net.sourceforge.nattable.test.fixture.data.RowDataListFixture;
import net.sourceforge.nattable.test.fixture.layer.DataLayerFixture;
import org.junit.Before;
import org.junit.Test;

public class EventsCacheTest {

    private static final String ASK_PRICE = RowDataListFixture.ASK_PRICE_PROP_NAME;

    UpdateEventsCache<RowDataFixture> cache;

    private PropertyUpdateEvent<RowDataFixture> testEvent1;

    private PropertyUpdateEvent<RowDataFixture> testEvent2;

    private RowDataFixture bean1;

    private RowDataFixture bean2;

    private DataLayerFixture layerFixture = new DataLayerFixture();

    @Before
    public void setup() {
        ListDataProvider<RowDataFixture> columnPropertyAccessor = new ListDataProvider<RowDataFixture>(RowDataListFixture.getList(), RowDataListFixture.getPropertyNames());
        cache = new UpdateEventsCache<RowDataFixture>(columnPropertyAccessor, RowDataFixture.rowIdAccessor);
        bean1 = RowDataListFixture.getList().get(0);
        testEvent1 = new PropertyUpdateEvent<RowDataFixture>(layerFixture, bean1, ASK_PRICE, 10, 15);
        bean2 = RowDataListFixture.getList().get(1);
        testEvent2 = new PropertyUpdateEvent<RowDataFixture>(layerFixture, bean2, ASK_PRICE, 20, 25);
    }

    @Test
    public void shouldAddUpdateEvents() throws Exception {
        cache.put(testEvent1);
        cache.put(testEvent2);
        assertEquals(2, cache.getCount());
    }

    @Test
    public void shouldUpdateEventDataForMultipleUpdatesToBean() throws Exception {
        cache.put(testEvent1);
        assertEquals(1, cache.getCount());
        PropertyUpdateEvent<RowDataFixture> bean1Update = new PropertyUpdateEvent<RowDataFixture>(layerFixture, bean1, ASK_PRICE, 15, 20);
        cache.put(bean1Update);
        assertEquals(1, cache.getCount());
        String key = cache.getKey(bean1Update);
        PropertyUpdateEvent<RowDataFixture> event = cache.getEvent(key);
        assertEquals(String.valueOf(15), event.getOldValue().toString());
        assertEquals(String.valueOf(20), event.getNewValue().toString());
    }

    @Test
    public void shouldConstructTheKeyUsingTheColumnIndexAndRowId() throws Exception {
        assertEquals("6-A Alphabet Co.", cache.getKey(testEvent1));
    }

    @Test
    public void keyGeneration() throws Exception {
        String key = cache.getKey(6, "100");
        assertTrue(key.startsWith("6-100"));
    }

    @Test
    public void shouldCleanUpStaleEventsAterTTLExpires() throws Exception {
        cache.put(testEvent1);
        cache.put(testEvent2);
        assertEquals(2, cache.getCount());
        Thread.sleep(UpdateEventsCache.INITIAL_DELAY + UpdateEventsCache.TIME_TO_LIVE + 100);
        assertEquals(0, cache.getCount());
    }
}
