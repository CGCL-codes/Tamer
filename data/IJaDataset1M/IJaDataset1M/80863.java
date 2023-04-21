package com.acuityph.commons.dsv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.Test;

/**
 * JUnit test for {@link DsvReader}.
 *
 * @author Alistair A. Israel
 */
public final class DsvReaderTest {

    /**
     * Test for {@link DsvReader}
     *
     * @throws Exception
     *         on exception
     *
     */
    @Test
    public void testReadingAndParsing() throws Exception {
        final InputStream is = this.getClass().getResourceAsStream("caret_separated.txt");
        final InputStreamReader isr = new InputStreamReader(is);
        final DsvReader reader = new DsvReader('^', isr);
        int lines = 0;
        String[] values = reader.readLine();
        while (values != null) {
            assertEquals(3, values.length);
            assertTrue(values[2].startsWith(values[1]));
            ++lines;
            values = reader.readLine();
        }
        assertEquals(2, lines);
        isr.close();
        is.close();
    }
}
