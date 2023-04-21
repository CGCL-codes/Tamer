package org.jitr;

import javax.ws.rs.core.MediaType;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jitr.Jitr;
import org.jitr.annotation.JitrConfiguration;
import org.jitr.core.OperationalMode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.GenericXmlContextLoader;

@RunWith(Jitr.class)
@JitrConfiguration(mode = OperationalMode.MIXED)
@ContextConfiguration(locations = { "/META-INF/spring/test-beans.xml" }, loader = GenericXmlContextLoader.class)
public class JitrTestWithExternalSpring extends JitrTestBase {

    private static final String EXTERNAL_TEST_STRING = "EXTERNAL_TEST_STRING";

    /**
     * In this test, we need to setup the remote Jetty service since the service injected is a local
     * and different Spring application context.
     */
    @Before
    public void before() throws Exception {
        final HttpClient httpClient = new HttpClient();
        final PostMethod postMethod = new PostMethod(baseUri + "string/echo");
        postMethod.setRequestEntity(new StringRequestEntity(EXTERNAL_TEST_STRING, MediaType.TEXT_PLAIN, "UTF-8"));
        int statusCode = httpClient.executeMethod(postMethod);
        Assert.assertEquals(200, statusCode);
        String responseString = getInputStreamAsString(postMethod.getResponseBodyAsStream());
        Assert.assertEquals(EXTERNAL_TEST_STRING, responseString);
        final PutMethod putMethod = new PutMethod(baseUri + "string");
        putMethod.setRequestEntity(new StringRequestEntity(EXTERNAL_TEST_STRING, MediaType.TEXT_PLAIN, "UTF-8"));
        statusCode = httpClient.executeMethod(putMethod);
        Assert.assertEquals(200, statusCode);
        putMethod.releaseConnection();
    }

    @Override
    protected String getRemoteTestString() {
        return EXTERNAL_TEST_STRING;
    }
}
