package com.entelience.test.test13servlet;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.entelience.soap.soapBaseHelper;
import com.entelience.util.Config;
import com.entelience.directory.Company;
import com.entelience.directory.PeopleFactory;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.params.HttpParams;

public class test04ChartServlet extends com.entelience.test.OurDbTestCase {

    private static String localServer = null;

    private static int localPort = 0;

    private static String baseUrl = null;

    private static String chartURL = null;

    @Test
    public void test00_setup() throws Exception {
        localServer = Config.getProperty(db, "com.entelience.esis.web.localServerAddress", "localhost");
        localPort = Config.getProperty(db, "com.entelience.esis.web.localServerPort", 8080);
        baseUrl = "http://" + localServer + ":" + Integer.toString(localPort);
        System.out.println("Using : " + baseUrl);
        chartURL = baseUrl + "/esis/html/portal/PortalChart";
        db.begin();
        Config.setProperty(db, "com.entelience.soap.soapBase.anonymousPortal", "true", PeopleFactory.anonymousId);
        db.commit();
    }

    /**
	 * Just checks that the servlet is there
	 */
    @Test
    public void test01_ok_failed_500() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(chartURL);
            HttpResponse response = client.execute(post);
            assertEquals("failed code for ", 500, response.getStatusLine().getStatusCode());
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    /**
	 * Just checks that the servlet is there
	 */
    @Test
    public void test02_ok() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(chartURL);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("ws", "getDomainEvolution"));
            nameValuePairs.add(new BasicNameValuePair("chartTitle", "test"));
            nameValuePairs.add(new BasicNameValuePair("type", "chart"));
            nameValuePairs.add(new BasicNameValuePair("firstDate", "20111124"));
            nameValuePairs.add(new BasicNameValuePair("lastDate", "20111125"));
            nameValuePairs.add(new BasicNameValuePair("wsParams", "type,counting,protocol,unit,proxy,domain,timeScale,period"));
            nameValuePairs.add(new BasicNameValuePair("wsParamsValues", "chart,volume,all,hits,all,google.com,day,360"));
            nameValuePairs.add(new BasicNameValuePair("serieTitle", "serie"));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            assertNotNull(entity);
            InputStream instream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            System.out.println(reader.readLine());
            instream.close();
            assertEquals("error :" + response.getStatusLine(), 200, response.getStatusLine().getStatusCode());
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
}
