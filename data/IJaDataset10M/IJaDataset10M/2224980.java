package ppa.marc.converter.generic;

import ppa.marc.converter.FieldConverter;
import ppa.marc.converter.generic.SubFieldAppender;
import ppa.marc.domain.Field;
import ppa.marc.domain.SubField;
import junit.framework.TestCase;

public class SubFieldAppenderTest extends TestCase {

    SubField subField = new SubField('2', "lc");

    FieldConverter converter = new SubFieldAppender(subField);

    Field field = new Field(100);

    protected void setUp() throws Exception {
        field.getSubFields().add(new SubField('a', "data"));
    }

    public void testAppendsSubFieldToField() throws Exception {
        converter.convert(field);
        assertEquals(subField, field.getSubFields().get(1));
    }
}
