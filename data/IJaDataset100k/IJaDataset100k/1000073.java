package eulergui.parser.n3.impl.parser4j.service.bugs;

import eulergui.parser.n3.impl.parser4j.entity.N3ParseResult;
import eulergui.parser.n3.impl.parser4j.service.N3Parser;
import junit.framework.TestCase;
import net.sf.parser4j.parser.entity.ParseResult;
import net.sf.parser4j.parser.service.IParserListener;
import net.sf.parser4j.parser.service.ParserException;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestMissingPrefix extends TestCase implements IParserListener {

    private static final Logger _log = Logger.getLogger(TestMissingPrefix.class);

    /** where are stored input file for test */
    private static final String INPUT_FILE = "test/parser/n3/impl/parser4j/bugs/missing_prefix.n3";

    private static final N3Parser fileParser = N3Parser.getInstance();

    public void testBadUri() throws ParserException, FileNotFoundException {
        fileParser.initialize(this);
        File f = new File(INPUT_FILE);
        FileInputStream stream = new FileInputStream(f);
        final N3ParseResult n3ParseResult = fileParser.parseN3("http://sourceforge.net/projects/eulergui/", stream);
        final ParseResult parseResult = n3ParseResult.getParseResult();
        final String message = parseResult.toString();
        if (parseResult.isInError()) {
            _log.info(message);
        } else {
            _log.error(message);
            fail(message);
        }
    }

    @Override
    public void newLine(final int line) {
    }
}
