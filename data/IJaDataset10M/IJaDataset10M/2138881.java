package org.jmage.filter.size;

import org.jmage.JmageException;
import java.io.IOException;
import java.util.Properties;

/**
 * Test ExtendingRotationFilter with property ORIENATION > 360
 */
public class ExtendingRotationJPGORIENTATIONGT360 extends ExtendingRotationJPG {

    public void setProperties() {
        Properties props = new Properties();
        props.setProperty("ORIENTATION", "400");
        props.setProperty("BACKGROUND_COLOR", "ff0000");
        request.setFilterChainProperties(props);
    }

    public void testFilter() throws JmageException, IOException {
        boolean failed = false;
        try {
            dispatcher.dispatch(request);
            this.writeResults(request);
        } catch (Throwable t) {
            failed = true;
        }
        assertTrue(failed);
    }
}
