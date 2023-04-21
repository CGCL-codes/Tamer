package ppa.marc.reader;

import ppa.marc.reader.AbstractFieldParserTest;
import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Field;
import ppa.marc.domain.SubField;
import ppa.marc.reader.UnimarcFieldParser;

public class UnimarcFieldParserTest extends AbstractFieldParserTest {

    protected FieldParser createFieldParser(SubFieldParser subFieldParser) {
        return new UnimarcFieldParser(subFieldParser);
    }

    public void testNormalSubFieldGetsParsed() throws Exception {
        Field expectedField = new Field(10, '0', '1');
        addExpectedSubField(new SubField('a', "data"), expectedField, "adata", false);
        parseReplayAndVerify(expectedField, 10, "01" + UnimarcConstants.SUBFIELD_DELIMITER + "adata");
    }

    public void testTwoSubFieldsGetParsed() throws Exception {
        Field expectedField = new Field(10, '0', '1');
        addExpectedSubField(new SubField('a', "data"), expectedField, "adata", false);
        addExpectedSubField(new SubField('b', "dat"), expectedField, "bdat", false);
        parseReplayAndVerify(expectedField, 10, "01" + UnimarcConstants.SUBFIELD_DELIMITER + "adata" + UnimarcConstants.SUBFIELD_DELIMITER + "bdat");
    }
}
