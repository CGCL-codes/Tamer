package at.ac.ait.enviro.dscsv;

import at.ac.ait.enviro.tsapi.timeseries.TimeSeries;
import at.ac.ait.enviro.tsapi.timeseries.impl.DefaultTimeSeries;
import at.ac.ait.enviro.tsapi.timeseries.impl.DefaultTimeStamp;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Ponweiser
 */
public class CSVDataSinkTest {

    CSVDataSink csvDS;

    public CSVDataSinkTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        csvDS = new CSVDataSink();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSupportedProperties() {
        String[] expected = { CSVDataSink.FILE_NAME, CSVDataSink.FILE_MODE, CSVDataSink.OUTPUT_STREAM, CSVDataSink.FORMAT_TOKSEP, CSVDataSink.FORMAT_BLOCKSEP, CSVDataSink.FORMAT_NODATA_VAL, CSVDataSink.FORMAT_DATE, CSVDataSink.CSV_HEADER, CSVDataSink.CSV_FIELDS, CSVDataSink.CSV_LABELS, CSVDataSink.OUTPUT_TS_PROPS };
        List<String> result = csvDS.getDataStorePropertyKeys();
        for (String key : expected) {
            assertTrue(String.format("property '%s' not supported.", key), result.contains(key));
        }
    }

    @Test
    public void testDefaultProperties() {
        String[] keys = { CSVDataSink.FILE_MODE, CSVDataSink.FORMAT_TOKSEP, CSVDataSink.FORMAT_BLOCKSEP, CSVDataSink.FORMAT_NODATA_VAL, CSVDataSink.FORMAT_DATE, CSVDataSink.CSV_HEADER, CSVDataSink.OUTPUT_TS_PROPS };
        Object[] expectedVals = { "write", CSVDataSink.DEFAULT_TOKSEP, CSVDataSink.DEFAULT_BLOCKSEP, CSVDataSink.DEFAULT_NODATA_VAL, CSVDataSink.DEFAULT_DATEFORMAT, CSVDataSink.YES, CSVDataSink.YES };
        for (int i = 0; i < keys.length; i++) {
            assertEquals(String.format("wrong default value for '%s'.", keys[i]), expectedVals[i], csvDS.getDataStoreProperty(keys[i]));
        }
        final String key = CSVDataSink.FILE_MODE;
        final String val = "append";
        csvDS.setDataStoreProperty(key, val);
        assertEquals(String.format("could not overwrite default value for '%s'.", key), val, csvDS.getDataStoreProperty(key));
    }

    public TimeSeries getTestTimeSeries(int slotCount) {
        String[] tsKeys = { "tsKey1", "tsKey2" };
        String[] tsVals = { "tsVal1", "tsVal2" };
        String[] slotKeys = { "slotKey1", "slotKey2" };
        String[] slotVals = { "slotVal1", "slotVal2" };
        final TimeSeries ts = new DefaultTimeSeries();
        for (int i = 0; i < tsKeys.length; i++) {
            ts.setTSProperty(tsKeys[i], tsVals[i]);
        }
        for (int j = 0; j < slotCount; j++) {
            for (int i = 0; i < slotKeys.length; i++) {
                ts.setValueByTimeStamp(slotKeys[i], new DefaultTimeStamp((long) j), slotVals[i]);
            }
        }
        return ts;
    }

    @Test
    public void testDefaultWriters() {
        TimeSeries ts = getTestTimeSeries(1);
        csvDS.setDefaultWriters(ts);
        assertEquals("wrong FieldWriter count.", 5, csvDS.writers.size());
        Iterator<CSVDataSink.FieldWriter> it = csvDS.writers.iterator();
        assertTrue("instance of TimeStampWriter expected.", it.next() instanceof CSVDataSink.TimeStampWriter);
        for (String key : ts.getSlotKeys()) {
            final CSVDataSink.FieldWriter fw = it.next();
            assertTrue("instance of SlotPropertyWriter expected.", fw instanceof CSVDataSink.SlotPropertyWriter);
            assertEquals("wrong key for SlotPropertyWriter.", key, ((CSVDataSink.PropertyWriter) fw).key);
        }
        for (String key : ts.getTSKeys()) {
            final CSVDataSink.FieldWriter fw = it.next();
            assertTrue("instance of TSPropertyWriter expected.", fw instanceof CSVDataSink.TSPropertyWriter);
            assertEquals("wrong key for TSPropertyWriter.", key, ((CSVDataSink.PropertyWriter) fw).key);
        }
    }

    @Test
    public void testCustomWriters() {
        csvDS.setDataStoreProperty(CSVDataStore.CSV_FIELDS, "ds:writeTime|ts:geo_ref|time|slot:value|ts:unit");
        csvDS.setDataStoreProperty(CSVDataStore.CSV_LABELS, "WriteTime|Position|Timestamp|Value|Unit");
        csvDS.setDataStoreProperty(CSVDataSink.OUTPUT_TS_PROPS, CSVDataSink.YES);
        csvDS.setLabels();
        assertNotNull("FieldWriters not inititalized.", csvDS.writers);
        assertEquals("wrong FieldWriter count.", 5, csvDS.writers.size());
        final Iterator<CSVDataSink.FieldWriter> it = csvDS.writers.iterator();
        CSVDataSink.FieldWriter fw;
        fw = it.next();
        assertTrue("instance of DSPropertyWriter expected.", fw instanceof CSVDataSink.DSPropertyWriter);
        assertEquals("wrong key for DSPropertyWriter.", "writeTime", ((CSVDataSink.PropertyWriter) fw).key);
        assertEquals("wrong label for DSPropertyWriter.", "WriteTime", fw.label);
        fw = it.next();
        assertTrue("instance of TSPropertyWriter expected.", fw instanceof CSVDataSink.TSPropertyWriter);
        assertEquals("wrong key for TSPropertyWriter.", "geo_ref", ((CSVDataSink.PropertyWriter) fw).key);
        assertEquals("wrong label for TSPropertyWriter.", "Position", fw.label);
        fw = it.next();
        assertTrue("instance of TimeStampWriter expected.", fw instanceof CSVDataSink.TimeStampWriter);
        assertEquals("wrong label for TimeStampWriter.", "Timestamp", fw.label);
        fw = it.next();
        assertTrue("instance of SlotPropertyWriter expected.", fw instanceof CSVDataSink.SlotPropertyWriter);
        assertEquals("wrong key for SlotPropertyWriter.", "value", ((CSVDataSink.PropertyWriter) fw).key);
        assertEquals("wrong label for SlotPropertyWriter.", "Value", fw.label);
        fw = it.next();
        assertTrue("instance of TSPropertyWriter expected.", fw instanceof CSVDataSink.TSPropertyWriter);
        assertEquals("wrong key for TSPropertyWriter.", "unit", ((CSVDataSink.PropertyWriter) fw).key);
        assertEquals("wrong label for TSPropertyWriter.", "Unit", fw.label);
    }

    @Test
    public void testStoreTimeSeriesDefaultFormat() {
        final int slots = 2;
        final TimeSeries ts = getTestTimeSeries(slots);
        final String[] expected = { "^time;slot:slotKey1;slot:slotKey2;ts:tsKey1;ts:tsKey2$", "^.*;slotVal1;slotVal2;tsVal1;tsVal2$", "^.*;slotVal1;slotVal2;tsVal1;tsVal2$" };
        Writer output = new StringWriter();
        csvDS.setDataStoreProperty(CSVDataSink.OUTPUT_STREAM, output);
        csvDS.connect();
        csvDS.storeTimeSeries(ts);
        csvDS.disconnect();
        final String[] result = output.toString().split("\n");
        assertEquals("wrong output line count.", slots + 1, result.length);
        for (int i = 0; i < expected.length; i++) {
            if (!result[i].matches(expected[i])) {
                fail(String.format("wrong output on line %d: '%s'", i, result[i]));
            }
        }
    }

    @Test
    public void testStoreTimeSeriesCustomFormat() {
        final int slots = 2;
        final TimeSeries ts = getTestTimeSeries(slots);
        final String[] expected = { "^Time,SlotCol,DSCol,TSCol$", "^.*,slotVal1,test,tsVal2$", "^.*,slotVal1,test,tsVal2$" };
        Writer output = new StringWriter();
        csvDS.setDataStoreProperty(CSVDataSink.OUTPUT_STREAM, output);
        csvDS.setDataStoreProperty(CSVDataSink.FORMAT_TOKSEP, ',');
        csvDS.setDataStoreProperty("MyProperty", "test");
        csvDS.setDataStoreProperty(CSVDataSink.CSV_FIELDS, "time|slot:slotKey1|ds:MyProperty|ts:tsKey2");
        csvDS.setDataStoreProperty(CSVDataSink.CSV_LABELS, "Time|SlotCol|DSCol|TSCol");
        csvDS.connect();
        csvDS.storeTimeSeries(ts);
        csvDS.disconnect();
        final String[] result = output.toString().split("\n");
        assertEquals("wrong output line count.", slots + 1, result.length);
        for (int i = 0; i < expected.length; i++) {
            if (!result[i].matches(expected[i])) {
                fail(String.format("wrong output on line %d: '%s'", i, result[i]));
            }
        }
    }
}
