package org.jmage.filterchain.frame;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Test InstantPhoto Filter with GIF
 */
public class InstantPhotoGIF extends InstantPhotoJPG {

    public void setImageURI() throws URISyntaxException {
        request.setImageURI(new URI("file:///jmage_large.gif"));
    }

    public void setEncodingFormat() {
        request.setEncodingFormat("gif");
    }
}
