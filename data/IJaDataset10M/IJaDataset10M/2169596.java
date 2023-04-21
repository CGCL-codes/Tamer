package de.lichtflut.infra.http;

import java.util.Collections;
import junit.framework.TestCase;
import de.lichtflut.infra.http.HttpClient;
import de.lichtflut.infra.http.HttpRequestException;

/**
 * 
 * Copyright 2008 by Oliver Tigges
 * 
 * @author Oliver Tigges
 * 
 * Created: 02.12.2008
 *
 * Description:
 */
public class HttpClientTest extends TestCase {

    public void testGet() {
        HttpClient client = new HttpClient();
        try {
            client.send("http://www.heise.de", "GET", Collections.<String, Object>emptyMap());
        } catch (HttpRequestException e) {
            e.printStackTrace();
            fail();
        }
    }
}
