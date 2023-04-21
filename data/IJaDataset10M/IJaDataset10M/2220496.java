package org.jmage.filter.merge;

import org.jmage.JmageException;
import org.jmage.mapper.JunitMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Test Textoverlay fails with Empty Text
 */
public class TextOverlayEmptyText extends JunitMapper {

    public void setFilterChainURI() throws URISyntaxException {
        request.setFilterChainURI(new URI("chain:org.jmage.filter.merge.TextOverlayFilter"));
    }

    public void setProperties() {
        Properties props = new Properties();
        props.setProperty("FONT_URI", "file:///maxhandwriting.ttf");
        props.setProperty("TEXT", "");
        props.setProperty("POSITION", "7");
        props.setProperty("COLOR", "0000dd");
        props.setProperty("FONT_SIZE", "44");
        props.setProperty("FONT_STYLE", "ITALIC");
        props.setProperty("ANTIALIASING", "TRUE");
        props.setProperty("BORDEROFFSET", "20");
        props.setProperty("ORIENTATION", "355");
        props.setProperty("OPACITY", "0");
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
