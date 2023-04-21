package de.spieleck.app.jacson.filter;

import de.spieleck.app.jacson.JacsonException;
import de.spieleck.app.jacson.JacsonRegistry;
import de.spieleck.app.jacson.source.LineChunkSource;
import de.spieleck.config.Config;
import de.spieleck.config.ConfigNode;
import org.xml.sax.InputSource;

/**
 * Test class for GroupingFilter
 * 
 * @author pcs
 * @version $Id:$
 * @since 0.90
 */
public class GroupingFilterTest extends FilterTestBase {

    private GroupingFilter filter = new GroupingFilter(";");

    private static final String DATA = "test/data/filter/groupingfilter.data";

    private static final int EXPECTED_COUNT = 3;

    private int count = 0;

    private static String[] startStrings = { "#DB-Script for WmTipp", "\nCREATE TABLE tipp", "\nCREATE TABLE player" };

    private boolean error = false;

    public void testGroupingFilter() throws Exception {
        LineChunkSource src = new LineChunkSource(DATA, false);
        filter.setDrain(this);
        String c;
        while ((c = src.nextChunk()) != null) filter.putChunk(c);
        assertTrue(count == EXPECTED_COUNT);
        assertFalse(error);
    }

    public void putChunk(String chunk) throws JacsonException {
        error = !(chunk.startsWith(startStrings[count]));
        if (error) System.err.println("Chunk mismatch. Expected " + startStrings[count] + ", " + "received " + chunk);
        count++;
    }
}
