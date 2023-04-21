package org.apache.shindig.gadgets.rewrite.image;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.shindig.gadgets.http.HttpResponse;
import org.junit.Ignore;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test BMPOptimizer
 */
public class BMPOptimizerTest extends BaseOptimizerTest {

    Logger log = Logger.getLogger(BMPOptimizerTest.class.getName());

    public void testSimpleImage() throws Exception {
        HttpResponse resp = createResponse("org/apache/shindig/gadgets/rewrite/image/simple.bmp", "image/bmp");
        HttpResponse rewritten = rewrite(resp);
        assertTrue(rewritten.getContentLength() < resp.getContentLength());
        assertEquals(rewritten.getHeader("Content-Type"), "image/png");
    }

    @Ignore("Kills some VMs")
    public void testEvilImages() throws Exception {
        try {
            HttpResponse resp = createResponse("org/apache/shindig/gadgets/rewrite/image/evil.bmp", "image/bmp");
            rewrite(resp);
            fail("Evil image should not be readable");
        } catch (RuntimeException re) {
            log.log(Level.INFO, "Good failure while reading evil image", re);
        }
    }

    public void testEvilImage2() throws Exception {
        try {
            HttpResponse resp = createResponse("org/apache/shindig/gadgets/rewrite/image/evil2.bmp", "image/bmp");
            rewrite(resp);
            fail("Evil image should not be readable");
        } catch (RuntimeException re) {
            log.log(Level.INFO, "Good failure while reading evil image", re);
        }
    }

    protected HttpResponse rewrite(HttpResponse original) throws IOException, ImageReadException {
        return new BMPOptimizer(new OptimizerConfig(), original).rewrite(Sanselan.getBufferedImage(original.getResponse()));
    }
}
