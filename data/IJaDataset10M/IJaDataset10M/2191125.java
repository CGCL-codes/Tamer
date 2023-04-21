package org.jmage.filter.color;

import org.jmage.JmageException;
import java.io.IOException;
import java.util.Properties;

/**
 * Test the Colorize Filter fails with non hex color code.
 */
public class ColorizeCOLORNONHEX extends ColorizeJPG {

    /**
     * Sets the properties
     */
    public void setProperties() {
        Properties props = new Properties();
        props.setProperty("COLOR", "xx00a0");
        request.setFilterChainProperties(props);
    }

    /**
     * Run the test by giving the ImageRequest to the ApplicationContext's dispatcher.
     */
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
