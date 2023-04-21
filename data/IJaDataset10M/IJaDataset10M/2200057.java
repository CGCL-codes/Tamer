package com.od.jtimeseries.context;

import com.od.jtimeseries.context.impl.SeriesContext;
import com.od.jtimeseries.source.ValueRecorder;
import com.od.jtimeseries.util.time.Time;
import junit.framework.TestCase;
import org.junit.Before;
import static com.od.jtimeseries.capture.function.CaptureFunctions.*;

/**
 * Created by IntelliJ IDEA.
 * User: nick
 * Date: 31-Jan-2010
 * Time: 11:56:19
 * To change this template use File | Settings | File Templates.
 *
 * When we create value source (e.g. Counter, ValueRecorder) via the context.new methods,
 * in addition to the value source itself we end up creating one or more timeseries
 * in the context (one per function specified) to hold the values, each of which which derive their id
 * from the id of the value sounds
 *
 * We need to check that these ids don't get changed accidentally, since the id of a series identifies
 * it when stored in a time series server, and also users may have preferences to reload series
 * by id in a client application - changing the series ID will break this link.
 */
public class TestTimeSeriesNaming extends TestCase {

    protected TimeSeriesContext rootContext;

    @Before
    public void setUp() {
        rootContext = new SeriesContext("Test Root Context", "Test Root Context");
    }

    public void testMilliseconds() {
        ValueRecorder v = rootContext.createValueRecorderSeries("Value", "Value Description", CHANGE(Time.milliseconds(10)));
        assertEquals("Value (Change 10ms)", rootContext.findTimeSeries(v).getFirstMatch().getId());
    }

    public void testSeconds() {
        ValueRecorder v = rootContext.createValueRecorderSeries("Value", "Value Description", MAX(Time.seconds(30)));
        assertEquals("Value (Max 30s)", rootContext.findTimeSeries(v).getFirstMatch().getId());
    }

    public void testMinutes() {
        ValueRecorder v = rootContext.createValueRecorderSeries("Value", "Value Description", MIN(Time.minutes(20)));
        assertEquals("Value (Min 20min)", rootContext.findTimeSeries(v).getFirstMatch().getId());
    }

    public void testHours() {
        ValueRecorder v = rootContext.createValueRecorderSeries("Value", "Value Description", MEAN(Time.hours(2)));
        assertEquals("Value (Mean 2hr)", rootContext.findTimeSeries(v).getFirstMatch().getId());
    }

    public void testDays() {
        ValueRecorder v = rootContext.createValueRecorderSeries("Value", "Value Description", SUM(Time.days(3)));
        assertEquals("Value (Sum 3day)", rootContext.findTimeSeries(v).getFirstMatch().getId());
    }

    public void testNaming() {
        rootContext.createCounterSeries("Login Attempts", "Count of Login Attempts", CHANGE(Time.days(3)), COUNT_OVER(Time.days(3)), MAX(Time.milliseconds(50)), MEAN(Time.seconds(10)), MEDIAN(Time.seconds(10)), PERCENTILE(Time.seconds(10), 15), MEAN_CHANGE(Time.minutes(1), Time.minutes(30)), MEAN_COUNT_OVER(Time.seconds(30), Time.hours(1)), LATEST(Time.minutes(240)), SUM(Time.hours(3)), MIN(Time.minutes(120)), VALUE_COUNT(Time.hours(1)), RAW_VALUES());
        assertNotNull(rootContext.get("Login Attempts (Change 3day)"));
        assertNotNull(rootContext.get("Login Attempts (Count 3day)"));
        assertNotNull(rootContext.get("Login Attempts (Max 50ms)"));
        assertNotNull(rootContext.get("Login Attempts (Mean 10s)"));
        assertNotNull(rootContext.get("Login Attempts (Median 10s)"));
        assertNotNull(rootContext.get("Login Attempts (15 Percentile 10s)"));
        assertNotNull(rootContext.get("Login Attempts (Change Per 1min Over 30min)"));
        assertNotNull(rootContext.get("Login Attempts (Count Per 30s Over 1hr)"));
        assertNotNull(rootContext.get("Login Attempts (Latest 240min)"));
        assertNotNull(rootContext.get("Login Attempts (Sum 3hr)"));
        assertNotNull(rootContext.get("Login Attempts (Min 120min)"));
        assertNotNull(rootContext.get("Login Attempts (ValueCount 1hr)"));
        assertNotNull(rootContext.get("Login Attempts"));
    }
}
