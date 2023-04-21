package de.micromata.jak.demo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Assert;
import org.junit.Test;

/**
 * The test checks the HTTP status code with a correct URL syntax (code = 200) and a bad request (code = 400)
 */
public class ChartApiConnectionTest {

    @Test
    public void chartApiTest() throws IOException {
        URL url = new URL("http://chart.apis.google.com/chart?cht=p3&chd=t:60,40&chs=250x100&chl=Hello|World");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Assert.assertEquals("Wrong status code: " + conn.getResponseCode(), 200, conn.getResponseCode());
        Assert.assertEquals("Wrong data format (should be png)", "image/png", conn.getContentType());
    }

    @Test
    public void chartApiBadRequestTest() throws IOException {
        URL url = new URL("http://chart.apis.google.com/chart?cht=p3d&chd=t:60,40&chs=250x100&chl=Hello|World");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Assert.assertEquals("Wrong status code: " + conn.getResponseCode(), 400, conn.getResponseCode());
    }
}
