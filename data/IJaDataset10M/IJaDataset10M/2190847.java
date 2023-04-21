package net.sf.urlchecker.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import net.sf.urlchecker.commands.RetryHandler;
import net.sf.urlchecker.communication.CommunicationFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Before;
import org.junit.Test;

/**
 * @author georgosn
 * 
 */
public class RetryHandlerTest {

    private RetryHandler retry;

    private HttpClient client;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        retry = new RetryHandler();
        client = CommunicationFactory.getInstance().configureClient(true);
    }

    @Test
    public void testRetryMethodAllDone() {
        final HttpMethod get = new GetMethod("http://www.google.com");
        try {
            client.executeMethod(get);
            assertFalse(retry.retryMethod(get, new IOException(), 4));
        } catch (final HttpException e) {
            fail("Should not have http exception");
        } catch (final IOException e) {
            fail("Should Not have IOException");
        }
    }

    /**
     * Test method for
     * {@link net.sf.urlchecker.commands.RetryHandler#retryMethod(org.apache.commons.httpclient.HttpMethod, java.io.IOException, int)}
     * .
     */
    @Test
    public void testRetryMethodMoreThanMaxRetries() {
        assertFalse(retry.retryMethod(new GetMethod(), new IOException(), 11));
    }

    @Test
    public void testRetryMethodNoHttpResponseException() {
        assertTrue(retry.retryMethod(new GetMethod(), new NoHttpResponseException(), 4));
    }

    @Test
    public void testRetryMethodNotSent() {
        final HttpMethod get = new GetMethod();
        assertTrue(retry.retryMethod(get, new IOException(), 8));
    }
}
