package org.apache.harmony.awt.tests.image;

import java.awt.image.ImageProducer;
import java.io.IOException;
import java.net.URL;
import junit.framework.TestCase;
import tests.support.resource.Support_Resources;

public class ImageLoadingTest extends TestCase {

    public void test_getContent1718() throws IOException {
        URL url;
        url = Support_Resources.class.getResource(Support_Resources.RESOURCE_PACKAGE + "Harmony.GIF");
        assertTrue("Returned object doesn't implement ImageProducer interface", url.getContent() instanceof ImageProducer);
        url = Support_Resources.class.getResource(Support_Resources.RESOURCE_PACKAGE + "Harmony.jpg");
        assertTrue("Returned object doesn't implement ImageProducer interface", url.getContent() instanceof ImageProducer);
        url = Support_Resources.class.getResource(Support_Resources.RESOURCE_PACKAGE + "Harmony.png");
        assertTrue("Returned object doesn't implement ImageProducer interface", url.getContent() instanceof ImageProducer);
    }
}
