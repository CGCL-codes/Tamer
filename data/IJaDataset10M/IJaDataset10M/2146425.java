package ppa.marc.validator.rules;

import ppa.marc.domain.Field;
import ppa.marc.domain.Record;
import ppa.marc.validator.Severity;
import ppa.marc.validator.ValidationMessage;
import ppa.marc.validator.rules.RecordHasFields;
import junit.framework.TestCase;

public class RecordHasFieldsTest extends TestCase {

    Record record = new Record("id");

    RecordHasFields rule = new RecordHasFields(Severity.ERROR, new int[] { 100, 200, 801 });

    {
        record.getFields().add(new Field(100));
        record.getFields().add(new Field(200));
        record.getFields().add(new Field(801));
    }

    public void testDoesNotGenerateErrorIfFieldsExist() throws Exception {
        assertTrue(rule.validate(record).isEmpty());
    }

    public void testGeneratesOneMessageIfAllFieldsAreMissing() throws Exception {
        record.getFields().clear();
        assertEquals(1, rule.validate(record).size());
        assertEquals(new ValidationMessage(Severity.ERROR, "Record is missing fields 100, 200 and 801."), rule.validate(record).get(0));
    }

    public void testMessageForMissing100IsCorrect() throws Exception {
        removeFieldAndAssertAtIndex(0);
    }

    public void testMessageForMissing200IsCorrect() throws Exception {
        removeFieldAndAssertAtIndex(1);
    }

    public void testMessageForMissing801IsCorrect() throws Exception {
        removeFieldAndAssertAtIndex(2);
    }

    private void removeFieldAndAssertAtIndex(int index) {
        int id = record.getFields().remove(index).getId();
        assertEquals(new ValidationMessage(Severity.ERROR, "Record is missing field " + id + "."), rule.validate(record).get(0));
    }
}
