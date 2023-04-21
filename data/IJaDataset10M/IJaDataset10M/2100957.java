package net.sourceforge.jpalm.mobiledb.record;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.jpalm.BaseTestFromResource;
import net.sourceforge.jpalm.mobiledb.record.FieldLabelsRecord;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;

public class TestFieldLabelsRecord extends BaseTestFromResource {

    protected FieldLabelsRecord fieldLabelsRecord;

    public TestFieldLabelsRecord() {
        offset = 580;
        length = 682 - (int) offset;
    }

    protected void setUp() throws Exception {
        super.setUp();
        fieldLabelsRecord = new FieldLabelsRecord(data);
    }

    public void testFieldLabelsRecordRecord() {
        Record record = new RecordImpl(data);
        fieldLabelsRecord = new FieldLabelsRecord(record);
        assertEquals("Seventh Field", fieldLabelsRecord.getFieldLabels().get(6));
    }

    public void testGetFieldLabels() {
        assertEquals("Seventh Field", fieldLabelsRecord.getFieldLabels().get(6));
    }

    public void testSetFieldLabels() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Field1");
        labels.add("Field2");
        fieldLabelsRecord.setFieldLabels(labels);
        List labelsAfter = fieldLabelsRecord.getFieldLabels();
        String label = (String) labelsAfter.get(1);
        assertEquals("Field2", label);
    }

    public void testSetFieldLabelsTooLong() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("A field label that is too long");
        fieldLabelsRecord.setFieldLabels(labels);
        List labelsAfter = fieldLabelsRecord.getFieldLabels();
        String label = (String) labelsAfter.get(0);
        assertEquals("A field label that is too", label);
    }

    public void testFieldLabelsRecord() {
        FieldLabelsRecord record = new FieldLabelsRecord();
        assertEquals(FieldLabelsRecord.CATEGORY_ID, record.getHeader().getAttributes());
    }
}
