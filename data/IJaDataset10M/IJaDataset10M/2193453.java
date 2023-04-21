package org.jrecruiter.service;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import org.jrecruiter.common.ApiKeysHolder;
import org.jrecruiter.common.GoogleMapsUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Gunnar Hillert
 * @version $Id: GoogleMapsStaticTest.java 605 2010-08-31 05:31:30Z ghillert $
 */
public class GoogleMapsStaticTest {

    @Autowired
    private ApiKeysHolder apiKeysHolder;

    @Test
    public void testStaticMapTest() throws Exception {
        sendGetRequest();
    }

    private void sendGetRequest() {
        try {
            final URI url = GoogleMapsUtils.buildGoogleMapsStaticUrl(BigDecimal.TEN, BigDecimal.TEN, 10);
            final URLConnection conn = url.toURL().openConnection();
            final BufferedImage img = ImageIO.read(conn.getInputStream());
            Assert.assertNotNull(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
