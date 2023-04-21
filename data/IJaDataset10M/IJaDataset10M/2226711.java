package org.jmage.filter.merge;

import org.jmage.JmageException;
import org.jmage.mapper.JunitMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Test BackgroundImageFilter failing with smaller background image.
 */
public class BackgroundImageBACKGROUNDSMALL extends JunitMapper {

    public void setFilterChainURI() throws URISyntaxException {
        request.setFilterChainURI(new URI("chain:org.jmage.filter.merge.BackgroundImageFilter"));
    }

    public void setProperties() {
        Properties props = new Properties();
        props.setProperty("IMAGE_URI", "file:///sample/june_vase_small.jpg");
        request.setFilterChainProperties(props);
    }

    public void testFilter() throws JmageException, IOException {
        assertTrue(this.dispatchFails());
    }
}
