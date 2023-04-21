package org.nomadpim.core.service;

import static junit.framework.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.nomadpim.core.util.date.DateUtil;
import org.nomadpim.core.util.date.TimeInterval;
import org.nomadpim.core.util.date.TimeUnit;

/**
 * @tag todo.documentation: type description
 */
public class IntervalDateServiceListenerTest {

    private IntervalDateServiceListener listener;

    private TimeUnit timeUnit;

    private List<TimeInterval> resultIntervals;

    @Before
    public void setUp() {
        timeUnit = TimeUnit.WEEK;
        listener = new IntervalDateServiceListener(timeUnit) {

            @Override
            protected void intervalChanged(TimeInterval newInterval) {
                resultIntervals.add(newInterval);
            }
        };
        resultIntervals = new ArrayList<TimeInterval>();
    }

    @Test
    public void testDefaultTimeUnit() {
        assertEquals(timeUnit, listener.getTimeUnit());
    }

    @Test
    public void testMultipleDateChanges() {
        DateTime date = DateUtil.day(2007, 3, 16);
        DateTime date2 = DateUtil.day(2007, 3, 15);
        DateTime date3 = DateUtil.day(2007, 3, 21);
        listener.dateChanged(date, this);
        listener.dateChanged(date, this);
        listener.dateChanged(date2, this);
        listener.dateChanged(date3, this);
        assertEquals(2, resultIntervals.size());
        assertEquals(timeUnit.getIntervalContaining(date), resultIntervals.get(0));
        assertEquals(timeUnit.getIntervalContaining(date3), resultIntervals.get(1));
    }

    @Test
    public void testSimpleDateChange() {
        DateTime date = DateUtil.day(2007, 3, 16);
        listener.dateChanged(date, this);
        assertEquals(1, resultIntervals.size());
        assertEquals(timeUnit.getIntervalContaining(date), resultIntervals.get(0));
    }
}
