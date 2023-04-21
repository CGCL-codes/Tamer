package com.h9labs;

import org.junit.Test;

/**
 * The test class for the HttpClient class.
 * 
 * @author akutz
 * 
 */
public class HttpClientTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void getContentTest() throws Exception {
        long epochs = 1241503889;
        HttpClient hc = new HttpClient("https://192.168.0.44/rrd_updates?start=" + epochs + "&host=true", "root", "password");
        String xml = hc.getContent();
        System.out.println(xml);
    }
}
