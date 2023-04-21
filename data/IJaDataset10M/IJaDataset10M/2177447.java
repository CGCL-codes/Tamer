package juploader.httpclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import java.net.URL;
import static org.junit.Assert.*;

/** @author Adam Pawelec */
@Ignore
public class FormSubmitRequestTest extends AbstractHttpRequestTest {

    private FormSubmitRequest formSubmit;

    @Before
    public void setUp() throws Exception {
        formSubmit = httpClient.createFormSubmitRequest();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void successfullLoginWithRedirect() throws Exception {
        formSubmit.setUrl(new URL("http://vpx.pl/zaloguj.html"));
        formSubmit.setHandleRedirects(true);
        formSubmit.addParameter("login", "proktor");
        formSubmit.addParameter("pass", "85517");
        formSubmit.addParameter("rem", "on");
        HttpResponse response = formSubmit.makeRequest();
        assertTrue(response.is2xxSuccess());
        assertFalse(response.is3xxRedirection());
        assertEquals(1, response.getResponseCookies().size());
        assertEquals(2, httpClient.getClientCookies().size());
        response.close();
    }

    @Test
    public void successfullLoginWithoutRedirect() throws Exception {
        formSubmit.setUrl(new URL("http://vpx.pl/zaloguj.html"));
        formSubmit.setHandleRedirects(false);
        formSubmit.addParameter("login", "proktor");
        formSubmit.addParameter("pass", "85517");
        formSubmit.addParameter("rem", "on");
        HttpResponse response = formSubmit.makeRequest();
        assertTrue(response.is3xxRedirection());
        assertFalse(response.is2xxSuccess());
        assertEquals(2, response.getResponseCookies().size());
        assertEquals(2, httpClient.getClientCookies().size());
        response.close();
    }

    @Test
    public void failedLogin() throws Exception {
        formSubmit.setUrl(new URL("http://vpx.pl/zaloguj.html"));
        formSubmit.setHandleRedirects(false);
        formSubmit.addParameter("login", "foo");
        formSubmit.addParameter("pass", "bar");
        formSubmit.addParameter("rem", "on");
        HttpResponse response = formSubmit.makeRequest();
        assertTrue(response.is2xxSuccess());
        assertEquals(1, response.getResponseCookies().size());
        response.close();
    }
}
