package simplespider.simplespider.throttle.host.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HostComparatorByTimestampUpdatedTest {

    private HostComparatorByTimestampUpdated testling;

    @Before
    public void setUp() throws Exception {
        this.testling = new HostComparatorByTimestampUpdated();
    }

    @Test
    public void testCompare() {
        Assert.assertEquals("Compare is wrong", -1, this.testling.compare(new HostCounter(null, new Date(0L)), new HostCounter(null, new Date())));
        Assert.assertEquals("Compare is wrong", 1, this.testling.compare(new HostCounter(null, new Date()), new HostCounter(null, new Date(0L))));
        Assert.assertEquals("Compare is wrong", 0, this.testling.compare(new HostCounter(null, new Date(0L)), new HostCounter(null, new Date(0L))));
    }

    @Test
    public void testCompareBySortingList() {
        final List<HostCounter> hostCounters = new ArrayList<HostCounter>();
        hostCounters.add(new HostCounter(null, new Date(0L)));
        hostCounters.add(new HostCounter(null, new Date(10L)));
        hostCounters.add(new HostCounter(null, new Date(2L)));
        hostCounters.add(new HostCounter(null, new Date(30L)));
        hostCounters.add(new HostCounter(null, new Date(50L)));
        hostCounters.add(new HostCounter(null, new Date(0L)));
        hostCounters.add(new HostCounter(null, new Date(20L)));
        hostCounters.add(new HostCounter(null, new Date(12L)));
        final HostCounter[] array = hostCounters.toArray(new HostCounter[hostCounters.size()]);
        Arrays.sort(array, this.testling);
        Assert.assertEquals("Ordering is wrong", array[0].getTimestampUpdatedLong(), 0L);
        Assert.assertEquals("Ordering is wrong", array[1].getTimestampUpdatedLong(), 0L);
        Assert.assertEquals("Ordering is wrong", array[2].getTimestampUpdatedLong(), 2L);
        Assert.assertEquals("Ordering is wrong", array[3].getTimestampUpdatedLong(), 10L);
        Assert.assertEquals("Ordering is wrong", array[4].getTimestampUpdatedLong(), 12L);
        Assert.assertEquals("Ordering is wrong", array[5].getTimestampUpdatedLong(), 20L);
        Assert.assertEquals("Ordering is wrong", array[6].getTimestampUpdatedLong(), 30L);
        Assert.assertEquals("Ordering is wrong", array[7].getTimestampUpdatedLong(), 50L);
    }
}
