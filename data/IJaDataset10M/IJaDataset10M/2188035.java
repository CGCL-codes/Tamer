package org.jomc.ant.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.junit.Test;

/**
 * Test cases for class {@code org.jomc.ant.ClassProcessingException}.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JOMC$
 */
public class ClassProcessingExceptionTest {

    /** Constant to prefix relative resource names with. */
    private static final String ABSOLUTE_RESOURCE_NAME_PREFIX = "/org/jomc/ant/test/";

    /** Creates a new {@code ClassProcessingExceptionTest} instance. */
    public ClassProcessingExceptionTest() {
        super();
    }

    @Test
    public final void testClassProcessingException() throws Exception {
        ObjectInputStream in = null;
        boolean suppressExceptionOnClose = true;
        try {
            in = new ObjectInputStream(this.getClass().getResourceAsStream(ABSOLUTE_RESOURCE_NAME_PREFIX + "ClassProcessingException.ser"));
            System.out.println(in.readObject());
            suppressExceptionOnClose = false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException ex) {
                if (!suppressExceptionOnClose) {
                    throw ex;
                }
            }
        }
    }
}
