package com.teracode.prototipogwt.backend.providers.base;

import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import com.teracode.prototipogwt.backend.extras.util.BackendConstants;

/**
 * @author Maxi
 */
public abstract class BaseProviderTest extends RestTest {

    private static Logger logger = Logger.getLogger(BaseProviderTest.class);

    /**
	 * @param url
	 * @return
	 */
    protected ClientRequest getUnsecuredRequestWithToken(String url, MediaType accepts) {
        return getUnsecuredRequestWithToken(url, BackendConstants.REST_TOKEN_TEST_VALUE, accepts);
    }

    /**
	 * @param url
	 * @return
	 */
    protected ClientRequest getUnsecuredRequestWithToken(String url, String token, MediaType accepts) {
        String completeUrl = getJettyHttpContext() + url;
        ClientRequest request = createRequest(completeUrl, accepts);
        request.header(BackendConstants.REST_TOKEN, token);
        return request;
    }

    /**
	 * @param url
	 * @return
	 */
    protected ClientRequest getSecuredRequestWithoutToken(String url, MediaType accepts) {
        String completeUrl = getJettyHttpsContext() + url;
        ClientRequest request = createRequest(completeUrl, accepts);
        return request;
    }

    /**
	 * @param completeUrl
	 * @return
	 */
    public ClientRequest createRequest(String completeUrl, MediaType accepts) {
        logger.info("------------------------------------------------------------------");
        logger.info("Creating Request For URL:" + completeUrl);
        logger.info("------------------------------------------------------------------");
        ClientRequest request = new ClientRequest(completeUrl);
        request.accept(accepts);
        return request;
    }
}
