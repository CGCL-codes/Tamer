package org.jomc.modlet.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import org.jomc.modlet.ModelValidationReport;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test cases for class {@code org.jomc.modlet.ModelValidationReport}.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a> 1.0
 * @version $JOMC$
 */
public class ModelValidationReportTest {

    /** Constant to prefix relative resource names with. */
    private static final String ABSOLUTE_RESOURCE_NAME_PREFIX = "/org/jomc/modlet/test/";

    /** Creates a new {@code ModelValidationReportTest} instance. */
    public ModelValidationReportTest() {
        super();
    }

    @Test
    public final void testSerializabe() throws Exception {
        final ModelValidationReport report = this.readObject(ABSOLUTE_RESOURCE_NAME_PREFIX + "ModelValidationReport.ser", ModelValidationReport.class);
        final ModelValidationReport.Detail detail = this.readObject(ABSOLUTE_RESOURCE_NAME_PREFIX + "ModelValidationReportDetail.ser", ModelValidationReport.Detail.class);
        System.out.println(report);
        System.out.println(detail);
        assertEquals(1, report.getDetails("Identifier 1").size());
        assertEquals(1, report.getDetails("Identifier 2").size());
        assertEquals(1, report.getDetails("Identifier 3").size());
        assertEquals(1, report.getDetails("Identifier 4").size());
        assertEquals(1, report.getDetails("Identifier 5").size());
        assertEquals(1, report.getDetails("Identifier 6").size());
        assertEquals(1, report.getDetails("Identifier 7").size());
        assertEquals(1, report.getDetails("Identifier 8").size());
        assertEquals(1, report.getDetails("Identifier 9").size());
        assertEquals(1, report.getDetails("Identifier 10").size());
        assertEquals("Identifier", detail.getIdentifier());
        assertEquals(Level.OFF, detail.getLevel());
        assertEquals("Message", detail.getMessage());
        assertNull(detail.getElement());
    }

    private <T> T readObject(final String location, final Class<T> type) throws IOException, ClassNotFoundException {
        ObjectInputStream in = null;
        boolean suppressExceptionOnClose = true;
        try {
            in = new ObjectInputStream(this.getClass().getResourceAsStream(location));
            assertNotNull(in);
            final T object = (T) in.readObject();
            suppressExceptionOnClose = false;
            return object;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                if (!suppressExceptionOnClose) {
                    throw e;
                }
            }
        }
    }
}
