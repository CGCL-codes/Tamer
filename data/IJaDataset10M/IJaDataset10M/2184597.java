package com.sun.jersey.server.simple.impl.container;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Paul.Sandoz@Sun.Com
 */
public class HeadTest extends AbstractSimpleServerTester {

    public HeadTest(String testName) {
        super(testName);
    }

    @Path("/")
    public static class Resource {

        @Path("string")
        @GET
        public String getString() {
            return "GET";
        }

        @Path("byte")
        @GET
        public byte[] getByte() {
            return "GET".getBytes();
        }

        @Path("ByteArrayInputStream")
        @GET
        public InputStream getInputStream() {
            return new ByteArrayInputStream("GET".getBytes());
        }
    }

    public void testHead() throws Exception {
        startServer(Resource.class);
        WebResource r = Client.create().resource(getUri().path("/").build());
        ClientResponse cr = r.path("string").accept("text/plain").head();
        assertEquals(200, cr.getStatus());
        assertEquals(MediaType.TEXT_PLAIN_TYPE, cr.getType());
        assertFalse(cr.hasEntity());
        cr = r.path("byte").accept("application/octet-stream").head();
        assertEquals(200, cr.getStatus());
        String length = cr.getMetadata().getFirst("Content-Length");
        assertNotNull(length);
        assertEquals(3, Integer.parseInt(length));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM_TYPE, cr.getType());
        assertFalse(cr.hasEntity());
        cr = r.path("ByteArrayInputStream").accept("application/octet-stream").head();
        assertEquals(200, cr.getStatus());
        length = cr.getMetadata().getFirst("Content-Length");
        assertNotNull(length);
        assertEquals(3, Integer.parseInt(length));
        assertEquals(MediaType.APPLICATION_OCTET_STREAM_TYPE, cr.getType());
        assertFalse(cr.hasEntity());
    }
}
