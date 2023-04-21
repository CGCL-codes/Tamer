package org.jmage.filter.size;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Test BorderExtender with transparent PNG
 */
public class BorderExtenderPNGTransparent extends BorderExtenderPNG {

    public void setImageURI() throws URISyntaxException {
        request.setImageURI(new URI("file:///banner/revised_256.png"));
    }
}
