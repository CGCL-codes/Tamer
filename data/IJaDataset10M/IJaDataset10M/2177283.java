package org.jsemantic.services.cxf;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.jsemantic.services.httpservice.client.HttpTestClient;
import junit.framework.TestCase;

public class CXFServiceTest extends TestCase {

    private CFXContenedorService embeddedContenedorService = null;

    private HttpTestClient httpClient = null;

    protected void setUp() throws Exception {
        super.setUp();
        embeddedContenedorService = new CFXContenedorService();
        this.embeddedContenedorService.start();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        this.embeddedContenedorService.stop();
        this.embeddedContenedorService.dispose();
    }

    public void test() {
        httpClient = (HttpTestClient) embeddedContenedorService.getComponent("httpClient");
        assertNotNull(httpClient);
        HttpResponse response = null;
        try {
            response = httpClient.executeHttpGetRequest("http://localhost:9005/prueba");
            StatusLine status = response.getStatusLine();
            assertEquals(200, status.getStatusCode());
        } catch (Throwable e) {
            fail(e.getMessage());
        } finally {
            httpClient.consumeContent(response);
        }
        try {
            response = httpClient.executeHttpGetRequest("http://localhost:9005/prueba/test?wsdl");
            StatusLine status = response.getStatusLine();
            assertEquals(200, status.getStatusCode());
        } catch (Throwable e) {
            fail(e.getMessage());
        } finally {
            httpClient.consumeContent(response);
        }
    }
}
