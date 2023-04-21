package ie.ucd.searchengine.fileparser;

import java.io.File;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Tests the Parser Manager
 * @author Brendan Maguire
 *
 */
public class ParserManagerTester {

    private static final String schema_loc = "schema/parsers.xsd";

    private static final String parser_file = "testFiles/parser.xml";

    /** Test parser manager with a null parser file */
    @Test
    public void testNullFile() {
        ParserManager manager = new ParserManager(null, new File(schema_loc));
        Assert.assertNull(manager.getParser("java"));
    }

    /** Test parser manager with a null parser schema */
    @Test
    public void testNullSchema() {
        ParserManager manager = new ParserManager(new File(parser_file), null);
        Assert.assertNull(manager.getParser("java"));
    }

    /** Test parser manager  */
    @Test
    public void testManager() {
        ParserManager manager = new ParserManager(new File(parser_file), new File(schema_loc));
        Assert.assertNotNull(manager.getParser("java"));
    }
}
