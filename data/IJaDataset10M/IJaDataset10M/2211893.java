package org.jmage.filter.merge;

import java.util.Properties;

/**
 * Test ImageOverlay with GIF
 */
public class ImageOverlayGIF extends ImageOverlayJPG {

    public void setProperties() {
        Properties props = new Properties();
        props.setProperty("IMAGE_URI", "file:///jmage_small.gif");
        request.setFilterChainProperties(props);
    }
}
