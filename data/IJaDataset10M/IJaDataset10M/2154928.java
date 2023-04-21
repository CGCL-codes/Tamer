package ostf.test.data.array;

import junit.framework.TestCase;

public class DataArrayTests extends TestCase {

    SequencedDataArray sda = null;

    MergedDataArray mda = null;

    public void setUp() throws Exception {
        super.setUp();
        sda = new SequencedDataArray();
        sda.setDefaultValue("sda_default");
        sda.setTemplate("head$id$foot");
        sda.setStartId(23);
        sda.setEndId(138);
        SimpleDataArray simple = new SimpleDataArray(new String[] { "hero", "dream", "life" });
        mda = new MergedDataArray();
        mda.setDataArrays(new DataArray[] { sda, simple });
        mda.setDefaultValue("mda_default");
    }

    public void testDataArrayDelete() {
        sda.reset();
        assertEquals(116, sda.size());
        assertTrue(sda.containValue("head97foot"));
        sda.addDataArray(new String[] { "hero", "dream", "life" });
        assertEquals(119, sda.size());
        String[] result = sda.purgeDataArray(new String[] { "hero", "life", "hehe" });
        assertEquals(2, result.length);
        assertEquals(117, sda.size());
        assertEquals("dream", sda.getValue(116, true));
        sda.getRandomValue(true);
        result = sda.purgeDataArray(new String[] { "dream" });
        assertEquals("dream", result[0]);
        assertEquals(115, sda.size());
        String[] values = sda.getRandomValues(5, true);
        assertEquals(5, values.length);
        assertEquals(110, sda.size());
        values = sda.getRandomValues(120, true);
        assertEquals(110, values.length);
        assertEquals(0, sda.size());
        String value = sda.getRandomValue(true);
        assertEquals("sda_default", value);
        sda.reclaimValue("head123foot");
        assertEquals(1, sda.size());
        value = sda.getRandomValue(true);
        assertEquals("head123foot", value);
        assertEquals(0, sda.size());
        sda.reset();
        assertEquals(116, sda.size());
        value = sda.getValue(8, true);
        assertEquals("head31foot", value);
        assertEquals(115, sda.size());
        values = sda.getIndexValues(6, 21, true);
        assertEquals("head32foot", values[2]);
        assertEquals(21, values.length);
        assertEquals("head52foot", sda.getValue(7, false));
        sda.reclaimValue("head38foot");
        sda.reclaimValue("head38foot");
        sda.reclaimValue("head24foot");
        sda.reclaimValue("head40foot");
        assertEquals(96, sda.size());
        values = sda.getAllValues(true);
        assertEquals(96, values.length);
        assertEquals(0, sda.size());
    }

    public void testMergedDataArrayDelete() {
        mda.reset();
        assertEquals(119, mda.size());
        mda.getRandomValue(true);
        assertEquals(118, mda.size());
        String[] values = mda.getRandomValues(5, true);
        assertEquals(5, values.length);
        assertEquals(113, mda.size());
        values = mda.getRandomValues(120, true);
        assertEquals(113, values.length);
        assertEquals(0, mda.size());
        String value = mda.getRandomValue(true);
        assertEquals("mda_default", value);
        mda.reclaimValue("head123foot");
        mda.reclaimValue("head140foot");
        assertEquals(1, mda.size());
        value = mda.getRandomValue(true);
        assertEquals("head123foot", value);
        assertEquals(0, mda.size());
        mda.reset();
        assertEquals(119, mda.size());
        value = mda.getValue(117, true);
        assertEquals("dream", value);
        value = mda.getValue(200, true);
        assertEquals("life", value);
        value = mda.getValue(200, true);
        assertEquals("hero", value);
        value = mda.getValue(200, true);
        assertEquals("head138foot", value);
        value = mda.getValue(200, true);
        assertEquals("head137foot", value);
        assertEquals(114, mda.size());
        value = mda.getValue(8, true);
        assertEquals("head31foot", value);
        assertEquals(113, mda.size());
        values = mda.getIndexValues(6, 21, true);
        assertEquals("head32foot", values[2]);
        assertEquals(21, values.length);
        mda.reclaimValue("head38foot");
        mda.reclaimValue("head38foot");
        mda.reclaimValue("head24foot");
        mda.reclaimValue("head40foot");
        assertEquals(94, mda.size());
        values = mda.getAllValues(true);
        assertEquals(94, values.length);
        assertEquals(0, mda.size());
        mda.reset();
        values = mda.getIndexValues(114, 4, true);
        assertEquals("head137foot", values[0]);
        assertEquals("dream", values[3]);
        assertEquals(115, mda.size());
        mda.reclaimValue("head138foot");
        mda.reclaimValue("dream");
        mda.reclaimValue("hero");
        mda.reclaimValue("life");
        assertEquals(118, mda.size());
    }
}
