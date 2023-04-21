package net.sourceforge.align.parser;

import static net.sourceforge.align.util.Util.assertAlignmentListEquals;
import java.util.List;
import net.sourceforge.align.Alignment;
import org.junit.Test;

public class StringParserTest {

    public static final String SOURCE_STRING = "aaabbb";

    public static final String TARGET_STRING = "1122";

    public static final String[][] SOURCE_SEGMENT_ARRAY = { new String[] { SOURCE_STRING } };

    public static final String[][] TARGET_SEGMENT_ARRAY = { new String[] { TARGET_STRING } };

    @Test
    public void parse() {
        Parser parser = new StringParser(SOURCE_STRING, TARGET_STRING);
        List<Alignment> alignmentList = parser.parse();
        assertAlignmentListEquals(SOURCE_SEGMENT_ARRAY, TARGET_SEGMENT_ARRAY, alignmentList);
    }
}
