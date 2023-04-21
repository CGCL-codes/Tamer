package gate.yam.parse;

import java.io.*;
import java.util.*;
import junit.framework.*;
import org.apache.log4j.Logger;
import gate.util.*;
import gate.yam.*;

/**
 * Unit test for YAM parse trees.
 */
public class YamParserTest extends TestCase {

    /** Create the test case */
    public YamParserTest(String testName) {
        super(testName);
    }

    /** Logger */
    static Logger lgr = Logger.getLogger("gate.yam");

    /** SInS (plain) logger */
    static Logger slgr = Logger.getLogger("gate.sins");

    /** Test errors and warnings. */
    public void testErrorsAndWarnings() throws Exception {
        String testName = "/gate/yam/resources/yam-errors.yam";
        lgr.info("testing errors generated when parsing " + testName);
        String testFilePath = this.getClass().getResource(testName).getPath();
        File testFileDir = new File(testFilePath).getParentFile();
        InputStream testFileStream = this.getClass().getResourceAsStream(testName);
        if (testFileStream == null) fail("couldn't get resource " + testName);
        InputStreamReader testReader = new InputStreamReader(testFileStream, "UTF-8");
        StringWriter responseWriter = new StringWriter();
        YamCommand yam = new YamCommand();
        YamParseTree parseTree = yam.translate(testReader, responseWriter, YamFile.FileType.HTML, testFileDir);
        List errors = parseTree.getErrors();
        List warnings = parseTree.getWarnings();
        int e = errors.size();
        if (e != 3) lgr.info(yam.printErrors(parseTree));
        assertEquals("wrong number of errors", 3, e);
        ParsingProblem p = (ParsingProblem) errors.get(1);
        assertEquals("begin line wrong", p.beginLine, 17);
        assertEquals("begin column wrong", p.beginColumn, 19);
        assertEquals("end line wrong", p.endLine, 24);
        assertEquals("end column wrong", p.endColumn, 2);
        assertEquals("included flag wrong", p.included, false);
        p = (ParsingProblem) errors.get(2);
        assertEquals("begin line wrong", p.beginLine, 1);
        assertEquals("begin column wrong", p.beginColumn, 1);
        assertEquals("end line wrong", p.endLine, 1);
        assertEquals("end column wrong", p.endColumn, 9);
        assertEquals("included flag wrong", p.included, true);
    }

    /**
   * Test error handling utility stuff.
   */
    public void testErrorStuff() throws Exception {
        String eStrings[] = { "", "", "", "\n", "", "\n", "\n\n", "", "\n\n", "\n\n\n", "", "\n\n\n", "\n\n\n\n", "\n\n\n", "\n", "123\n0", "", "123\n0", "123\n456\n7", "123\n", "456\n7", "123\n456\n789\n0", "123\n456\n", "789\n0", "123\n", "", "123\n", "123\n456\n", "123\n", "456\n", "123\n456\n789\n", "123\n456\n", "789\n", "123\r0", "", "123\r0", "123\r456\r7", "123\r", "456\r7", "123\r456\r789\r0", "123\r456\r", "789\r0", "123\r", "", "123\r", "123\r456\r", "123\r", "456\r", "123\r456\r789\r", "123\r456\r", "789\r", "123\r\n0", "", "123\r\n0", "123\r\n456\r\n7", "123\r\n", "456\r\n7", "123\r\n456\r\n789\r\n0", "123\r\n456\r\n", "789\r\n0", "123\r\n", "", "123\r\n", "123\r\n456\r\n", "123\r\n", "456\r\n", "123\r\n456\r\n789\r\n", "123\r\n456\r\n", "789\r\n", "123\n\r456\n\r789\n\r", "123\n\r456\n\r789\n", "\r" };
        StringReader reader = new StringReader("");
        YamParser parser = new YamParser(reader);
        for (int i = 0; i < eStrings.length; i += 3) {
            String eString = eStrings[i];
            String start = eStrings[i + 1];
            String end = eStrings[i + 2];
        }
    }

    /** Test the getSepSpacing function. */
    public void testGetSepSpacing() {
        String[] cases = { "\n", "\n ", "\n  ", "\n\t", "\n  \t  ", "\n  \t\t  \t", "\r", "\r ", "\r  ", "\r\t", "\r  \t  ", "\r  \t\t  \t", "\r\n", "\r\n ", "\r\n  ", "\r\n\t", "\r\n  \t  ", "\r\n  \t\t  \t", "\n\n\r\n", "\n\n\r\n ", "\n\n\r\n  ", "\n\n\r\n\t", "\n\n\r\n  \t  ", "\n\n\r\n  \t\t  \t" };
        int[] expectedSpacings = { 0, 1, 2, 8, 10, 24, 0, 1, 2, 8, 10, 24, 0, 1, 2, 8, 10, 24, 0, 1, 2, 8, 10, 24 };
        StringReader reader = new StringReader("");
        YamParser p = new YamParser(reader);
        Token t = new Token();
        for (int caseNumber = 0; caseNumber < cases.length; caseNumber++) {
            t.image = cases[caseNumber];
            int spacing = p.getSepSpacing(t);
            assertEquals("wrong spacing: ", expectedSpacings[caseNumber], spacing);
        }
    }
}
