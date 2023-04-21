package org.mobicents.slee.enabler.rest.client;

import org.apache.http.HttpResponse;

/**
 * The response related with the execution of a {@link RESTClientEnablerRequest}
 * .
 * 
 * @author martins
 * 
 */
public class RESTClientEnablerResponse {

    private final RESTClientEnablerRequest request;

    private final HttpResponse response;

    private final Exception exception;

    RESTClientEnablerResponse(RESTClientEnablerRequest request, HttpResponse response, Exception exception) {
        this.request = request;
        this.response = response;
        this.exception = exception;
    }

    /**
	 * Retrieves the executed request.
	 * 
	 * @return
	 */
    public RESTClientEnablerRequest getRequest() {
        return request;
    }

    /**
	 * Retrieves the response obtained from HTTP client, if any.
	 * 
	 * @return
	 */
    public HttpResponse getHttpResponse() {
        return response;
    }

    /**
	 * Retrieves the exception thrown when executing the request, if any.
	 * 
	 * @return
	 */
    public Exception getExecutionException() {
        return exception;
    }
}
