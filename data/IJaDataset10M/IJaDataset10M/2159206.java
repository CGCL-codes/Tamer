package ppa.marc.converter.marc21;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.Predicate;
import ppa.marc.converter.FieldConverter;
import ppa.marc.converter.marc21.TocSplitter;
import ppa.marc.domain.Field;
import ppa.marc.domain.SubField;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class TocSplitterTest extends TestCase {

    Predicate subFieldByIdSelector = createStrictMock(Predicate.class);

    Field field = new Field(505, '1', '0');

    FieldConverter converter = new TocSplitter(subFieldByIdSelector);

    List<SubField> expectedSubFields = new ArrayList<SubField>();

    protected void setUp() throws Exception {
        expectedSubFields.add(new SubField('a', "First item"));
        expectedSubFields.add(new SubField('a', "Second"));
        expectedSubFields.add(new SubField('a', "Third"));
    }

    public void testDoesNotAddSubfieldsIfNoContent() throws Exception {
        replay(subFieldByIdSelector);
        converter.convert(field);
        assertEquals(0, field.getSubFields().size());
        verify(subFieldByIdSelector);
    }

    public void testSplitsTocToSubFieldsA() throws Exception {
        field.getSubFields().add(new SubField('a', "First item -- Second -- Third"));
        expect(subFieldByIdSelector.evaluate(field.getSubFields().get(0))).andReturn(true);
        replay(subFieldByIdSelector);
        converter.convert(field);
        assertEquals(expectedSubFields, field.getSubFields());
        verify(subFieldByIdSelector);
    }

    public void testFailsIfSourceHasSeveralSubFields() throws Exception {
        try {
            field.getSubFields().add(new SubField('a', "a -- a"));
            field.getSubFields().add(new SubField('a', "a -- a"));
            converter.convert(field);
            fail();
        } catch (RuntimeException ignore) {
        }
    }
}
