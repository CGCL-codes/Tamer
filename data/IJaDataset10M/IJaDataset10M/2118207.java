package org.jomc.sequences.test;

import java.io.ObjectInputStream;
import java.net.URL;
import org.jomc.sequences.ConcurrentModificationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
public class ConcurrentModificationExceptionTest {

    @Test
    public void testSerializable() throws Exception {
        final URL ser = this.getClass().getResource("ConcurrentModificationException.ser");
        assertNotNull(ser);
        final ObjectInputStream in = new ObjectInputStream(ser.openStream());
        final ConcurrentModificationException e = (ConcurrentModificationException) in.readObject();
        in.close();
        assertNotNull(e.getMostRecentRevision());
        assertEquals(0L, e.getMostRecentRevision().getDate());
        assertEquals(Long.MAX_VALUE, e.getMostRecentRevision().getIncrement());
        assertEquals(Long.MAX_VALUE, e.getMostRecentRevision().getMaximum());
        assertEquals(Long.MAX_VALUE, e.getMostRecentRevision().getMinimum());
        assertEquals("Sequence 1", e.getMostRecentRevision().getName());
        assertEquals(0L, e.getMostRecentRevision().getRevision());
        assertEquals(Long.MAX_VALUE, e.getMostRecentRevision().getValue());
    }

    /** Creates a new {@code ConcurrentModificationExceptionTest} instance. */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    public ConcurrentModificationExceptionTest() {
        super();
    }
}
