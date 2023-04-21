package net.sourceforge.jpalm.palmdb;

import java.util.ArrayList;
import net.sourceforge.jpalm.palmdb.ApplicationInfo;
import net.sourceforge.jpalm.palmdb.Header;
import net.sourceforge.jpalm.palmdb.PalmDBImpl;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;
import net.sourceforge.jpalm.palmdb.SortInfo;
import junit.framework.TestCase;

public class TestPalmDBImpl extends TestCase {

    private PalmDBImpl database;

    protected void setUp() throws Exception {
        super.setUp();
        database = new PalmDBImpl();
    }

    public void testGetApplicationInfo() {
        ApplicationInfo info = new ApplicationInfo();
        database.applicationInfo = info;
        assertEquals(info, database.getApplicationInfo());
    }

    public void testSetApplicationInfo() {
        ApplicationInfo info = new ApplicationInfo();
        database.setApplicationInfo(info);
        assertEquals(info, database.applicationInfo);
    }

    public void testGetHeader() {
        Header header = new Header();
        database.header = header;
        assertEquals(header, database.getHeader());
    }

    public void testSetHeader() {
        Header header = new Header();
        database.setHeader(header);
        assertEquals(header, database.header);
    }

    public void testGetRecords() {
        ArrayList<Record> records = new ArrayList<Record>();
        records.add(new RecordImpl("test".getBytes()));
        database.records = records;
        assertEquals(records, database.getRecords());
    }

    public void testSetRecords() {
        ArrayList<Record> records = new ArrayList<Record>();
        records.add(new RecordImpl("test".getBytes()));
        database.setRecords(records);
        assertEquals(records, database.records);
    }

    public void testGetSortInfo() {
        SortInfo info = new SortInfo("test".getBytes());
        database.sortInfo = info;
        assertEquals(info, database.getSortInfo());
    }

    public void testSetSortInfo() {
        SortInfo info = new SortInfo("test".getBytes());
        database.setSortInfo(info);
        assertEquals(info, database.sortInfo);
    }
}
