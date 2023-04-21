package org.jboss.resteasy.test.finegrain.resource;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.test.EmbeddedContainer;
import org.jboss.resteasy.util.HttpHeaderNames;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import java.util.List;
import java.util.Locale;
import static org.jboss.resteasy.test.TestPortProvider.*;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class VariantsTest {

    private static Dispatcher dispatcher;

    @BeforeClass
    public static void before() throws Exception {
        dispatcher = EmbeddedContainer.start().getDispatcher();
        dispatcher.getRegistry().addPerRequestResource(LanguageVariantResource.class);
        dispatcher.getRegistry().addPerRequestResource(ComplexVariantResource.class);
        dispatcher.getRegistry().addPerRequestResource(EncodingVariantResource.class);
    }

    @AfterClass
    public static void after() throws Exception {
        EmbeddedContainer.stop();
    }

    @Path("/")
    public static class LanguageVariantResource {

        @GET
        public Response doGet(@Context Request r) {
            List<Variant> vs = Variant.VariantListBuilder.newInstance().languages(new Locale("zh")).languages(new Locale("fr")).languages(new Locale("en")).add().build();
            Variant v = r.selectVariant(vs);
            if (v == null) return Response.notAcceptable(vs).build(); else return Response.ok(v.getLanguage(), v).build();
        }
    }

    @Test
    public void testGetLanguageEn() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/"));
        request.header(HttpHeaderNames.ACCEPT_LANGUAGE, "en");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("en", response.getEntity());
        Assert.assertEquals("en", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_LANGUAGE));
    }

    @Test
    public void testGetLanguageZh() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/"));
        request.header(HttpHeaderNames.ACCEPT_LANGUAGE, "zh");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("zh", response.getEntity());
        Assert.assertEquals("zh", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_LANGUAGE));
    }

    @Test
    public void testGetLanguageMultiple() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/"));
        request.header(HttpHeaderNames.ACCEPT_LANGUAGE, "en;q=0.3, zh;q=0.4, fr");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("fr", response.getEntity());
        Assert.assertEquals("fr", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_LANGUAGE));
    }

    @Path("/complex")
    public static class ComplexVariantResource {

        @GET
        public Response doGet(@Context Request r) {
            List<Variant> vs = Variant.VariantListBuilder.newInstance().mediaTypes(MediaType.valueOf("image/jpeg")).add().mediaTypes(MediaType.valueOf("application/xml")).languages(new Locale("en", "us")).add().mediaTypes(MediaType.valueOf("text/xml")).languages(new Locale("en")).add().mediaTypes(MediaType.valueOf("text/xml")).languages(new Locale("en", "us")).add().build();
            Variant v = r.selectVariant(vs);
            if (v == null) return Response.notAcceptable(vs).build(); else return Response.ok("GET", v).build();
        }
    }

    @Test
    public void testGetComplex1() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/complex"));
        request.header(HttpHeaderNames.ACCEPT, "text/xml");
        request.header(HttpHeaderNames.ACCEPT, "application/xml");
        request.header(HttpHeaderNames.ACCEPT, "application/xhtml+xml");
        request.header(HttpHeaderNames.ACCEPT, "image/png");
        request.header(HttpHeaderNames.ACCEPT, "text/html;q=0.9");
        request.header(HttpHeaderNames.ACCEPT, "text/plain;q=0.8");
        request.header(HttpHeaderNames.ACCEPT, "*/*;q=0.5");
        request.header(HttpHeaderNames.ACCEPT_LANGUAGE, "en-us, en;q=0.5");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("GET", response.getEntity());
        Assert.assertEquals("application/xml", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_TYPE));
        Assert.assertEquals("en-us", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_LANGUAGE));
    }

    @Test
    public void testGetComplex2() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/complex"));
        request.header(HttpHeaderNames.ACCEPT, "text/xml");
        request.header(HttpHeaderNames.ACCEPT, "application/xml");
        request.header(HttpHeaderNames.ACCEPT, "application/xhtml+xml");
        request.header(HttpHeaderNames.ACCEPT, "image/png");
        request.header(HttpHeaderNames.ACCEPT, "text/html;q=0.9");
        request.header(HttpHeaderNames.ACCEPT, "text/plain;q=0.8");
        request.header(HttpHeaderNames.ACCEPT, "*/*;q=0.5");
        request.header(HttpHeaderNames.ACCEPT_LANGUAGE, "en, en-us");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("GET", response.getEntity());
        Assert.assertEquals("application/xml", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_TYPE));
        Assert.assertEquals("en-us", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_LANGUAGE));
    }

    @Test
    public void testGetComplex3() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/complex"));
        request.header(HttpHeaderNames.ACCEPT, "application/xml");
        request.header(HttpHeaderNames.ACCEPT, "text/xml");
        request.header(HttpHeaderNames.ACCEPT, "application/xhtml+xml");
        request.header(HttpHeaderNames.ACCEPT, "image/png");
        request.header(HttpHeaderNames.ACCEPT, "text/html;q=0.9");
        request.header(HttpHeaderNames.ACCEPT, "text/plain;q=0.8");
        request.header(HttpHeaderNames.ACCEPT, "*/*;q=0.5");
        request.header(HttpHeaderNames.ACCEPT_LANGUAGE, "en-us, en;q=0.5");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("GET", response.getEntity());
        Assert.assertEquals("application/xml", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_TYPE));
        Assert.assertEquals("en-us", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_LANGUAGE));
    }

    @Test
    public void testGetComplex4() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/complex"));
        request.header(HttpHeaderNames.ACCEPT, "application/xml");
        request.header(HttpHeaderNames.ACCEPT, "text/xml");
        request.header(HttpHeaderNames.ACCEPT, "application/xhtml+xml");
        request.header(HttpHeaderNames.ACCEPT, "image/png");
        request.header(HttpHeaderNames.ACCEPT, "text/html;q=0.9");
        request.header(HttpHeaderNames.ACCEPT, "text/plain;q=0.8");
        request.header(HttpHeaderNames.ACCEPT, "*/*;q=0.5");
        request.header(HttpHeaderNames.ACCEPT_LANGUAGE, "en, en-us;q=0.5");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("GET", response.getEntity());
        Assert.assertEquals("text/xml", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_TYPE));
        Assert.assertEquals("en", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_LANGUAGE));
    }

    @Test
    public void testGetComplexNotAcceptable() throws Exception {
        {
            ClientRequest request = new ClientRequest(generateURL("/complex"));
            request.header(HttpHeaderNames.ACCEPT, "application/atom+xml");
            request.header(HttpHeaderNames.ACCEPT_LANGUAGE, "en-us, en");
            ClientResponse<?> response = request.get();
            Assert.assertEquals(406, response.getStatus());
            ;
            String vary = response.getHeaders().getFirst(HttpHeaderNames.VARY);
            Assert.assertNotNull(vary);
            System.out.println("vary: " + vary);
            Assert.assertTrue(contains(vary, "Accept"));
            Assert.assertTrue(contains(vary, "Accept-Language"));
            response.releaseConnection();
        }
        {
            ClientRequest request = new ClientRequest(generateURL("/complex"));
            request.header(HttpHeaderNames.ACCEPT, "application/xml");
            request.header(HttpHeaderNames.ACCEPT_LANGUAGE, "fr");
            ClientResponse<?> response = request.get();
            Assert.assertEquals(406, response.getStatus());
            ;
            String vary = response.getHeaders().getFirst(HttpHeaderNames.VARY);
            Assert.assertNotNull(vary);
            System.out.println("vary: " + vary);
            Assert.assertTrue(contains(vary, "Accept"));
            Assert.assertTrue(contains(vary, "Accept-Language"));
            response.releaseConnection();
        }
    }

    @Path("/encoding")
    public static class EncodingVariantResource {

        @GET
        public Response doGet(@Context Request r) {
            List<Variant> vs = Variant.VariantListBuilder.newInstance().encodings("enc1", "enc2", "enc3").add().build();
            Variant v = r.selectVariant(vs);
            if (v == null) return Response.notAcceptable(vs).build(); else return Response.ok(v.getEncoding(), v).build();
        }
    }

    @Test
    public void testGetEncoding1() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/encoding"));
        request.header(HttpHeaderNames.ACCEPT_ENCODING, "enc1");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        ;
        Assert.assertEquals("enc1", response.getEntity());
        Assert.assertEquals("enc1", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_ENCODING));
    }

    @Test
    public void testGetEncoding2() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/encoding"));
        request.header(HttpHeaderNames.ACCEPT_ENCODING, "enc2");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        ;
        Assert.assertEquals("enc2", response.getEntity());
        Assert.assertEquals("enc2", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_ENCODING));
    }

    @Test
    public void testGetEncoding3() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/encoding"));
        request.header(HttpHeaderNames.ACCEPT_ENCODING, "enc3");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        ;
        Assert.assertEquals("enc3", response.getEntity());
        Assert.assertEquals("enc3", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_ENCODING));
    }

    @Test
    public void testGetEncodingQ() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/encoding"));
        request.header(HttpHeaderNames.ACCEPT_ENCODING, "enc1;q=0.5, enc2;q=0.9");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        ;
        Assert.assertEquals("enc2", response.getEntity());
        Assert.assertEquals("enc2", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_ENCODING));
    }

    @Test
    public void testGetEncodingQ2() throws Exception {
        ClientRequest request = new ClientRequest(generateURL("/encoding"));
        request.header(HttpHeaderNames.ACCEPT_ENCODING, "enc1;q=0, enc2;q=0.888, enc3;q=0.889");
        ClientResponse<String> response = request.get(String.class);
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        ;
        Assert.assertEquals("enc3", response.getEntity());
        Assert.assertEquals("enc3", response.getHeaders().getFirst(HttpHeaderNames.CONTENT_ENCODING));
    }

    private boolean contains(String l, String v) {
        String[] vs = l.split(",");
        for (String s : vs) {
            s = s.trim();
            if (s.equalsIgnoreCase(v)) return true;
        }
        return false;
    }
}
