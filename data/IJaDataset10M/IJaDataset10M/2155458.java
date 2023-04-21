package org.objectwiz.remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import junit.framework.TestCase;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.net.www.protocol.http.AuthCache;

/**
 *
 * @author aqin
 */
public class RemotingServletImplTest extends TestCase {

    public RemotingServletImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testDoGet() throws Exception {
        HttpHost targetHost = new HttpHost("localhost", 8080, "http");
        DefaultHttpClient client = new DefaultHttpClient();
        client.getCredentialsProvider().setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()), new UsernamePasswordCredentials("vince", "test56"));
        try {
            HttpGet httpget = new HttpGet("http://localhost:8080/objectwiz/api?invokeFacetOperation=createNewEntity&objectClassName=org.objectwiz.testapp.jee5.addressbook.Person&applicationName=addressbook&methodReference=persist(E)&args=(lastname=toto)");
            System.out.println("executing request " + httpget.getURI());
            HttpResponse response = client.execute(httpget);
            HttpEntity entity = response.getEntity();
            Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                Header h = headers[i];
                System.out.println(h.getName() + "/" + h.getValue());
            }
            assertEquals(response.getStatusLine().getStatusCode(), 200);
            System.out.println("----------------------------------------");
            if (entity != null) {
                System.out.println("response content length" + entity.getContentLength());
                System.out.println(entity.getContentType().getName() + "/" + entity.getContentType().getValue());
            }
            httpget.abort();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
}
