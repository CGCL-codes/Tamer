package org.jmage.filter.size;

import org.jmage.mapper.JunitMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Test ExtendingRotationFilter with JPG
 */
public class ExtendingRotationJPG extends JunitMapper {

    public void setFilterChainURI() throws URISyntaxException {
        request.setFilterChainURI(new URI("chain:org.jmage.filter.size.ExtendingRotationFilter"));
    }

    public void setProperties() {
        Properties props = new Properties();
        props.setProperty("ORIENTATION", "35");
        props.setProperty("BACKGROUND_COLOR", "ff0000");
        request.setFilterChainProperties(props);
    }
}
