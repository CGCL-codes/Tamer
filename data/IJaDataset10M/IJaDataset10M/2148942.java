package org.jsemantic.services.examples.energy.controller;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.jsemantic.services.examples.common.WebEmbeddedContenedorService;
import org.jsemantic.services.httpservice.client.HttpTestClient;
import junit.framework.TestCase;

public class InvoicesControllerTest extends TestCase {

    private WebEmbeddedContenedorService contenedor = null;

    protected void setUp() throws Exception {
        super.setUp();
        contenedor = new WebEmbeddedContenedorService();
        contenedor.start();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        contenedor.stop();
        contenedor.dispose();
        contenedor = null;
    }

    public void test() {
        HttpTestClient httpClient = (HttpTestClient) contenedor.getComponent("httpClient");
        assertNotNull(httpClient);
        HttpResponse response = null;
        try {
            response = httpClient.executeHttpGetRequest("http://localhost:9006/test/invoice.do");
            StatusLine status = response.getStatusLine();
            assertEquals(200, status.getStatusCode());
        } catch (Throwable e) {
            fail(e.getMessage());
        } finally {
            httpClient.consumeContent(response);
        }
    }
}
