package org.shapelogic.reporting;

import junit.framework.TestCase;
import org.shapelogic.calculation.RecursiveContext;
import org.shapelogic.calculation.SimpleRecursiveContext;
import org.shapelogic.mathematics.NaturalNumberStream;
import org.shapelogic.streams.NumberedStream;

/** ColumnDefinitionTest tests one column definition.<br />
 *
 * @author Sami Badawi
 */
public class ColumnDefinitionTest extends TestCase {

    public void testConstructor() {
        String streamName = "streamName";
        String columnName = "columnName";
        ColumnDefinition columnDefinition = new ColumnDefinition(streamName, columnName);
        assertNotNull(columnDefinition);
        assertEquals(streamName, columnDefinition.getStreamName());
        assertEquals(columnName, columnDefinition.getColumnName());
        assertNull(columnDefinition.isEmpty());
        assertFalse(columnDefinition.findStream(null));
    }

    public void testFind() {
        String streamName = "streamName";
        String columnName = "columnName";
        RecursiveContext recursiveContext = new SimpleRecursiveContext(null);
        NumberedStream<Integer> naturalNumberStream = new NaturalNumberStream(2);
        recursiveContext.getContext().put(streamName, naturalNumberStream);
        ColumnDefinition columnDefinition = new ColumnDefinition(streamName, columnName);
        assertNotNull(columnDefinition);
        assertEquals(streamName, columnDefinition.getStreamName());
        assertEquals(columnName, columnDefinition.getColumnName());
        assertNull(columnDefinition.isEmpty());
        assertFalse(columnDefinition.findStream(null));
    }

    public void testDirectStreams() {
        String columnName = "columnName";
        NumberedStream<Integer> naturalNumberStream = new NaturalNumberStream(2);
        ColumnDefinition columnDefinition = new ColumnDefinition(naturalNumberStream, columnName);
        assertNotNull(columnDefinition);
        assertEquals(null, columnDefinition.getStreamName());
        assertEquals(columnName, columnDefinition.getColumnName());
        assertNull(columnDefinition.isEmpty());
        assertTrue(columnDefinition.findStream(null));
    }
}
