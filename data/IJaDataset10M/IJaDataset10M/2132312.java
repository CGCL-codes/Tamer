package org.jomc.sequences.test;

import java.io.ObjectInputStream;
import java.net.URL;
import org.jomc.sequences.SequenceLimitException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
public class SequenceLimitExceptionTest {

    @Test
    public void testSerializable() throws Exception {
        final URL ser = this.getClass().getResource("SequenceLimitException.ser");
        assertNotNull(ser);
        final ObjectInputStream in = new ObjectInputStream(ser.openStream());
        final SequenceLimitException e = (SequenceLimitException) in.readObject();
        in.close();
        assertEquals(Long.MAX_VALUE, e.getCurrentValue());
    }

    /** Creates a new {@code SequenceLimitExceptionTest} instance. */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    public SequenceLimitExceptionTest() {
        super();
    }
}
