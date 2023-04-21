package com.tredart.dataimport.parsers;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.tredart.table.TableDefault;

/**
 * Test the {@link DelimitedParser}.
 * 
 * @author fdegrazia
 * @author csmith
 */
public class DelimitedParserTest {

    private static final int C3 = 3;

    /**
	 * Parses a string with fields separated by commas.
	 */
    @Test
    public void testParseCSV() {
        DelimitedParser parser = new DelimitedParser(",");
        parser.setInput("the,lazy,dog");
        parser.parse();
        TableDefault table = parser.getOutput();
        assertEquals("the", table.getCell(0, 0).getValue());
        assertEquals("lazy", table.getCell(0, 1).getValue());
        assertEquals("dog", table.getCell(0, 2).getValue());
    }

    /**
	 * Parses a string with fields separated by commas distributed on several
	 * lines of text.
	 */
    @Test
    public void testParseMultiLineCSV() {
        DelimitedParser parser = new DelimitedParser(",");
        String line0 = "the,lazy,dog\n";
        String line1 = "was,jumping\n";
        String line2 = "in,a,red,lake\n";
        parser.setInput(line0 + line1 + line2);
        parser.parse();
        TableDefault table = parser.getOutput();
        assertEquals("the", table.getCell(0, 0).getValue());
        assertEquals("lazy", table.getCell(0, 1).getValue());
        assertEquals("dog", table.getCell(0, 2).getValue());
        assertEquals("was", table.getCell(1, 0).getValue());
        assertEquals("jumping", table.getCell(1, 1).getValue());
        assertEquals("in", table.getCell(2, 0).getValue());
        assertEquals("a", table.getCell(2, 1).getValue());
        assertEquals("red", table.getCell(2, 2).getValue());
        assertEquals("lake", table.getCell(2, C3).getValue());
    }

    /**
	 * Parses a string with fields separated by tabs.
	 */
    @Test
    public void testParseTAB() {
        DelimitedParser parser = new DelimitedParser("\t");
        parser.setInput("the	lazy	dog");
        parser.parse();
        TableDefault table = parser.getOutput();
        assertEquals("the", table.getCell(0, 0).getValue());
        assertEquals("lazy", table.getCell(0, 1).getValue());
        assertEquals("dog", table.getCell(0, 2).getValue());
    }
}
