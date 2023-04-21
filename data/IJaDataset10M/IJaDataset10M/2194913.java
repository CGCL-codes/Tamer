package net.sourceforge.jpalm.mobiledb.record;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.jpalm.BaseTestFromResource;
import net.sourceforge.jpalm.mobiledb.record.FieldDisplaySizesRecord;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;

public class TestFieldDisplaySizesRecord extends BaseTestFromResource {

    protected FieldDisplaySizesRecord fieldDisplaySizesRecord;

    public TestFieldDisplaySizesRecord() {
        offset = 867;
        length = 959 - (int) offset;
    }

    protected void setUp() throws Exception {
        super.setUp();
        fieldDisplaySizesRecord = new FieldDisplaySizesRecord(data);
    }

    public void testFieldLengthsRecord() {
        fieldDisplaySizesRecord = new FieldDisplaySizesRecord();
        for (Integer field : fieldDisplaySizesRecord.getFieldDisplaySizes()) {
            assertEquals(Integer.valueOf(80), field);
        }
    }

    public void testFieldLengthsRecordByteArray() {
        for (Integer field : fieldDisplaySizesRecord.getFieldDisplaySizes()) {
            assertEquals(Integer.valueOf(80), field);
        }
    }

    public void testFieldLengthsRecordRecord() {
        Record record = new RecordImpl(data);
        fieldDisplaySizesRecord = new FieldDisplaySizesRecord(record);
        for (Integer field : fieldDisplaySizesRecord.getFieldDisplaySizes()) {
            assertEquals(Integer.valueOf(80), field);
        }
    }

    public void testSetFieldDisplaySizes() {
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        lengths.add(1);
        lengths.add(null);
        FieldDisplaySizesRecord record = new FieldDisplaySizesRecord();
        record.setFieldDisplaySizes(lengths);
        List<String> result = record.getInternalFields();
        assertEquals(2, result.size());
        assertEquals("1", result.get(0));
        assertEquals("0", result.get(1));
    }

    public void testGetFieldNotANumber() {
        ArrayList<String> fields = new ArrayList<String>();
        fields.add("foo");
        FieldDisplaySizesRecord record = new FieldDisplaySizesRecord();
        record.setInternalFields(fields);
        List<Integer> result = record.getFieldDisplaySizes();
        assertEquals(1, result.size());
        assertEquals(new Integer(0), result.get(0));
    }

    public void testSerialize() {
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        lengths.add(1);
        FieldDisplaySizesRecord record = new FieldDisplaySizesRecord();
        record.setFieldDisplaySizes(lengths);
        assertEquals(1, record.getInternalFields().size());
        record.serialize();
        List<String> result = record.getInternalFields();
        assertEquals(20, result.size());
        assertEquals("1", result.get(0));
        assertEquals("80", result.get(1));
    }
}
