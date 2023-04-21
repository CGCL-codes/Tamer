package com.od.jtimeseries.timeseries.impl;

import com.od.jtimeseries.timeseries.Item;
import com.od.jtimeseries.timeseries.TimeSeriesItem;
import com.od.jtimeseries.util.numeric.LongNumeric;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: nick
 * Date: 02-Mar-2009
 * Time: 00:35:40
 * To change this template use File | Settings | File Templates.
 */
public class TestTimeSeriesItem extends Assert {

    @Test
    public void testEqualityAndHashcode() {
        long time = System.currentTimeMillis();
        long value = (long) (Math.random() * Long.MAX_VALUE);
        TimeSeriesItem item1 = new Item(time, value);
        TimeSeriesItem item2 = new Item(time, value);
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
        item1 = new Item(time, value - 1);
        assertFalse(item1.equals(item2));
        assertFalse(item1.hashCode() == item2.hashCode());
        item1 = new Item(time - 1, value);
        assertFalse(item1.equals(item2));
    }
}
