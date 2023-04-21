package com.google.gwt.view.client;

import com.google.gwt.junit.client.GWTTestCase;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test cases for {@link AbstractDataProvider}.
 */
public class AbstractDataProviderTest extends GWTTestCase {

    /**
   * A mock {@link AbstractDataProvider} used for testing.
   *
   * @param <T> the data type
   */
    static class MockDataProvider<T> extends AbstractDataProvider<T> {

        private HasData<T> lastChanged;

        public MockDataProvider(ProvidesKey<T> keyProvider) {
            super(keyProvider);
        }

        public void assertLastRangeChanged(HasData<T> expected) {
            assertEquals(expected, lastChanged);
        }

        public void clearLastRangeChanged() {
            lastChanged = null;
        }

        @Override
        protected void onRangeChanged(HasData<T> display) {
            lastChanged = display;
        }
    }

    @Override
    public String getModuleName() {
        return "com.google.gwt.view.View";
    }

    public void testAddRemoveDataDisplay() {
        MockDataProvider<String> provider = new MockDataProvider<String>(null);
        provider.updateRowCount(10, true);
        provider.assertLastRangeChanged(null);
        MockHasData<String> display0 = new MockHasData<String>();
        provider.addDataDisplay(display0);
        provider.assertLastRangeChanged(display0);
        MockHasData<String> display1 = new MockHasData<String>();
        provider.addDataDisplay(display1);
        provider.assertLastRangeChanged(display1);
        MockHasData<String> invalidDisplay = new MockHasData<String>();
        try {
            provider.removeDataDisplay(invalidDisplay);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
        }
        provider.removeDataDisplay(display1);
        provider.assertLastRangeChanged(display1);
        display0.setVisibleRange(100, 20);
        display1.setVisibleRange(100, 20);
        provider.assertLastRangeChanged(display0);
    }

    public void testGetRanges() {
        AbstractDataProvider<String> provider = createDataProvider();
        {
            Range[] ranges = provider.getRanges();
            assertEquals(0, ranges.length);
        }
        {
            MockHasData<String> display0 = new MockHasData<String>();
            display0.setVisibleRange(0, 10);
            provider.addDataDisplay(display0);
            Range[] ranges = provider.getRanges();
            assertEquals(1, ranges.length);
            assertEquals(new Range(0, 10), ranges[0]);
        }
        {
            MockHasData<String> display1 = new MockHasData<String>();
            display1.setVisibleRange(3, 10);
            provider.addDataDisplay(display1);
            MockHasData<String> display2 = new MockHasData<String>();
            display2.setVisibleRange(30, 35);
            provider.addDataDisplay(display2);
            Set<Range> ranges = new HashSet<Range>();
            for (Range range : provider.getRanges()) {
                ranges.add(range);
            }
            assertEquals(3, ranges.size());
            assertTrue(ranges.contains(new Range(0, 10)));
            assertTrue(ranges.contains(new Range(3, 10)));
            assertTrue(ranges.contains(new Range(30, 35)));
        }
    }

    public void testGetDataDisplays() {
        AbstractDataProvider<String> provider = createDataProvider();
        MockHasData<String> display0 = new MockHasData<String>();
        MockHasData<String> display1 = new MockHasData<String>();
        assertEquals(0, provider.getDataDisplays().size());
        provider.addDataDisplay(display0);
        provider.addDataDisplay(display1);
        Set<HasData<String>> displays = provider.getDataDisplays();
        assertEquals(2, displays.size());
        assertTrue(displays.contains(display0));
        assertTrue(displays.contains(display1));
        provider.removeDataDisplay(display0);
        displays = provider.getDataDisplays();
        assertEquals(1, displays.size());
        assertTrue(displays.contains(display1));
    }

    public void testKeyProvider() {
        AbstractDataProvider<String> provider = createDataProvider(null);
        assertNull(provider.getKeyProvider());
        assertEquals("test", provider.getKey("test"));
        assertEquals(null, provider.getKey(null));
        ProvidesKey<String> keyProvider = new ProvidesKey<String>() {

            public Object getKey(String item) {
                return item == null ? item : item.toUpperCase();
            }
        };
        provider = createDataProvider(keyProvider);
        assertEquals(keyProvider, provider.getKeyProvider());
        assertEquals("TEST", provider.getKey("test"));
        assertEquals(null, provider.getKey(null));
    }

    public void testUpdateDataSize() {
        AbstractDataProvider<String> provider = createDataProvider();
        provider.updateRowCount(10, true);
        MockHasData<String> display0 = new MockHasData<String>();
        provider.addDataDisplay(display0);
        provider.updateRowCount(20, true);
        assertEquals(20, display0.getRowCount());
        assertTrue(display0.isRowCountExact());
        MockHasData<String> display1 = new MockHasData<String>();
        provider.addDataDisplay(display1);
        provider.updateRowCount(30, false);
        assertEquals(30, display0.getRowCount());
        assertFalse(display0.isRowCountExact());
        assertEquals(30, display1.getRowCount());
        assertFalse(display1.isRowCountExact());
    }

    public void testUpdateRowData() {
        AbstractDataProvider<String> provider = createDataProvider();
        MockHasData<String> display = new MockHasData<String>();
        display.setVisibleRange(10, 5);
        provider.addDataDisplay(display);
        {
            List<String> values = createData(10, 5);
            provider.updateRowData(10, values);
            assertEquals(values, display.getLastRowData());
            assertEquals(new Range(10, 5), display.getLastRowDataRange());
            display.clearLastRowDataAndRange();
        }
        {
            List<String> values = createData(12, 2);
            provider.updateRowData(12, values);
            assertEquals(values, display.getLastRowData());
            assertEquals(new Range(12, 2), display.getLastRowDataRange());
            display.clearLastRowDataAndRange();
        }
        {
            List<String> values = createData(10, 0);
            provider.updateRowData(10, values);
            assertEquals(values, display.getLastRowData());
            assertEquals(new Range(10, 0), display.getLastRowDataRange());
            display.clearLastRowDataAndRange();
        }
        {
            List<String> values = createData(5, 5);
            provider.updateRowData(5, values);
            assertNull(display.getLastRowData());
            assertNull(display.getLastRowDataRange());
            display.clearLastRowDataAndRange();
        }
        {
            List<String> values = createData(15, 5);
            provider.updateRowData(15, values);
            assertNull(display.getLastRowData());
            assertNull(display.getLastRowDataRange());
            display.clearLastRowDataAndRange();
        }
        {
            List<String> values = createData(5, 15);
            provider.updateRowData(5, values);
            assertEquals(values.subList(5, 10), display.getLastRowData());
            assertEquals(new Range(10, 5), display.getLastRowDataRange());
            display.clearLastRowDataAndRange();
        }
        {
            List<String> values = createData(5, 7);
            provider.updateRowData(5, values);
            assertEquals(values.subList(5, 7), display.getLastRowData());
            assertEquals(new Range(10, 2), display.getLastRowDataRange());
            display.clearLastRowDataAndRange();
        }
        {
            List<String> values = createData(13, 5);
            provider.updateRowData(13, values);
            assertEquals(values.subList(0, 2), display.getLastRowData());
            assertEquals(new Range(13, 2), display.getLastRowDataRange());
            display.clearLastRowDataAndRange();
        }
    }

    public void testUpdateRowDataMultipleDisplays() {
        AbstractDataProvider<String> provider = createDataProvider();
        MockHasData<String> display0 = new MockHasData<String>();
        display0.setVisibleRange(10, 5);
        provider.addDataDisplay(display0);
        MockHasData<String> display1 = new MockHasData<String>();
        display1.setVisibleRange(0, 5);
        provider.addDataDisplay(display1);
        MockHasData<String> display2 = new MockHasData<String>();
        display2.setVisibleRange(12, 10);
        provider.addDataDisplay(display2);
        List<String> values = createData(10, 5);
        provider.updateRowData(10, values);
        assertEquals(values, display0.getLastRowData());
        assertEquals(new Range(10, 5), display0.getLastRowDataRange());
        assertNull(display1.getLastRowData());
        assertNull(display1.getLastRowDataRange());
        assertEquals(values.subList(2, 5), display2.getLastRowData());
        assertEquals(new Range(12, 3), display2.getLastRowDataRange());
    }

    /**
   * Create an {@link AbstractDataProvider} for testing.
   *
   * @return the data provider
   */
    protected AbstractDataProvider<String> createDataProvider() {
        return new MockDataProvider<String>(null);
    }

    /**
   * Create an {@link AbstractDataProvider} for testing.
   *
   * @return the data provider
   */
    protected AbstractDataProvider<String> createDataProvider(ProvidesKey<String> keyProvider) {
        return new MockDataProvider<String>(keyProvider);
    }

    /**
   * Create a list of data for testing.
   *
   * @param start the start index
   * @param length the length
   * @return a list of data
   */
    protected List<String> createData(int start, int length) {
        List<String> toRet = new ArrayList<String>();
        for (int i = 0; i < length; i++) {
            toRet.add("test " + (i + start));
        }
        return toRet;
    }
}
