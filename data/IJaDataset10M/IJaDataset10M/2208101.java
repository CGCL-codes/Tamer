package org.dbunit.dataset.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import junit.framework.TestCase;
import org.dbunit.dataset.common.handlers.IllegalInputCharacterException;
import org.dbunit.dataset.common.handlers.PipelineException;
import org.dbunit.testutil.TestUtils;

public class CsvParserTest extends TestCase {

    CsvParser parser;

    public void testCanParseNonQuotedStrings() throws PipelineException, IllegalInputCharacterException {
        String csv = "Hello, world";
        List parsed = parser.parse(csv);
        assertEquals(2, parsed.size());
        assertEquals(parsed.get(0), "Hello");
        assertEquals(parsed.get(1), "world");
    }

    public void testAFieldCanContainANewLine() throws PipelineException, IllegalInputCharacterException {
        assertEquals("", 3, parser.parse("Hello, World\nIt's today, the day before tomorrow").size());
    }

    public void testDontAcceptIncompleteFields() throws PipelineException, IllegalInputCharacterException {
        String incompleteFields = "AAAAA,\"BB";
        try {
            parser.parse(incompleteFields);
            fail("should have thrown an exception");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    public void testAFileCanContainFieldWithNewLine() throws IOException, CsvParserException {
        final String pathname = "csv/with-newlines.csv";
        List list = parser.parse(TestUtils.getFile(pathname));
        assertEquals("wrong number of lines parsed from " + pathname, 2, list.size());
        List row = (List) list.get(1);
        assertEquals("AA\nAAA", row.get(0));
        assertEquals("BB\nBBB", row.get(1));
    }

    public void testRaiseACSVParserExceptonWhenParsingAnEmptyFile() throws IOException {
        failParsing(TestUtils.getFile("csv/empty-file.csv"));
    }

    public void testRaiseACSVParserExceptonWhenParsingFileWithDifferentNumberOfColumns() throws IllegalInputCharacterException, IOException, PipelineException {
        failParsing(TestUtils.getFile("csv/different-column-numbers-last.csv"));
        failParsing(TestUtils.getFile("csv/different-column-numbers-first.csv"));
    }

    private void failParsing(File sample) throws IOException {
        try {
            parser.parse(sample);
            fail("should have thrown a CsvParserException");
        } catch (CsvParserException e) {
            assertTrue(true);
        }
    }

    public void testSample() throws Exception {
        File sample = TestUtils.getFile("csv/sample.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sample)));
        LineNumberReader lineNumberReader = new LineNumberReader(reader);
        String line;
        while ((line = lineNumberReader.readLine()) != null) {
            if (line.startsWith("#") || line.trim().length() == 0) continue;
            List actual = parser.parse(line);
            assertEquals("wrong tokens on line " + lineNumberReader.getLineNumber() + " " + line, 3, actual.size());
        }
    }

    public void testWhitespacePreservedOnQuotedStrings() throws PipelineException, IllegalInputCharacterException {
        String csv = "\" Hello, \",world";
        List parsed = parser.parse(csv);
        assertEquals(2, parsed.size());
        assertEquals(" Hello, ", parsed.get(0));
        assertEquals("world", parsed.get(1));
        csv = " Hello, world";
        parsed = parser.parse(csv);
        assertEquals(2, parsed.size());
        assertEquals("Hello", parsed.get(0));
        assertEquals("world", parsed.get(1));
        csv = "\" Hello, \",\" world \"";
        parsed = parser.parse(csv);
        assertEquals(2, parsed.size());
        assertEquals(" Hello, ", parsed.get(0));
        assertEquals(" world ", parsed.get(1));
    }

    protected void setUp() throws Exception {
        parser = new CsvParserImpl();
    }
}
