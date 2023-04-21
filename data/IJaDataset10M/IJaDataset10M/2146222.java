package org.supercsv.io;

import static org.circulartests.CircularData.columnsToWrite;
import static org.circulartests.CircularData.expectedReadResultsFromColumnToWrite;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.supercsv.exception.NullInputException;
import org.supercsv.prefs.CsvPreference;

/**
 * @author Kasper B. Graversen
 */
public class AbstractCSVWriterTest {

    /**
	 * implement abstract class in order to test it
	 * 
	 * @author Kasper B. Graversen
	 */
    static class MockWriter extends AbstractCsvWriter {

        MockWriter(final Writer stream, final CsvPreference preference) {
            super(stream, preference);
        }
    }

    @Test
    public void should_escape() {
        final MockWriter absWriter = new MockWriter(new StringWriter(), CsvPreference.EXCEL_PREFERENCE);
        assertThat(columnsToWrite.length, is(expectedReadResultsFromColumnToWrite.length));
        for (int i = 0; i < columnsToWrite.length; i++) {
            Assert.assertEquals(expectedReadResultsFromColumnToWrite[i], absWriter.escapeString(columnsToWrite[i]));
        }
    }

    /**
	 * Tests writing a List with a null element (should throw an Exception).
	 */
    @Test(expected = NullInputException.class)
    public void testWriteListWithNullElement() throws Exception {
        MockWriter writer = new MockWriter(new StringWriter(), CsvPreference.EXCEL_PREFERENCE);
        List<String> list = new ArrayList<String>();
        list.add(null);
        writer.write(list);
    }

    /**
	 * Tests writing an Object array with a null element (should throw an Exception).
	 */
    @Test(expected = NullInputException.class)
    public void testWriteObjectArrayWithNull() throws Exception {
        MockWriter writer = new MockWriter(new StringWriter(), CsvPreference.EXCEL_PREFERENCE);
        writer.write(new Object[] { null });
    }
}
