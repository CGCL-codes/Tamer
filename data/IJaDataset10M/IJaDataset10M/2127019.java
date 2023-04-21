package ramon.mock;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.log4j.Logger;
import static junit.framework.Assert.*;

/**
 * Mock implementation of the {@link javax.servlet.RequestDispatcher} interface.
 */
public class MockRequestDispatcher implements RequestDispatcher {

    private final Logger logger = Logger.getLogger(getClass());

    private final String url;

    /**
	 * Create a new MockRequestDispatcher for the given URL.
	 * @param url the URL to dispatch to.
	 */
    public MockRequestDispatcher(String url) {
        assertNotNull(url);
        this.url = url;
    }

    public void forward(ServletRequest request, ServletResponse response) {
        assertNotNull(request);
        assertNotNull(response);
        if (response.isCommitted()) {
            throw new IllegalStateException("Cannot perform forward - response is already committed");
        }
        getMockHttpServletResponse(response).setForwardedUrl(this.url);
        if (logger.isDebugEnabled()) {
            logger.debug("MockRequestDispatcher: forwarding to URL [" + this.url + "]");
        }
    }

    public void include(ServletRequest request, ServletResponse response) {
        assertNotNull(request);
        assertNotNull(response);
        getMockHttpServletResponse(response).setIncludedUrl(this.url);
        if (logger.isDebugEnabled()) {
            logger.debug("MockRequestDispatcher: including URL [" + this.url + "]");
        }
    }

    /**
	 * Obtain the underlying MockHttpServletResponse,
	 * unwrapping {@link HttpServletResponseWrapper} decorators if necessary.
	 */
    protected MockHttpServletResponse getMockHttpServletResponse(ServletResponse response) {
        if (response instanceof MockHttpServletResponse) {
            return (MockHttpServletResponse) response;
        }
        if (response instanceof HttpServletResponseWrapper) {
            return getMockHttpServletResponse(((HttpServletResponseWrapper) response).getResponse());
        }
        throw new IllegalArgumentException("MockRequestDispatcher requires MockHttpServletResponse");
    }
}
