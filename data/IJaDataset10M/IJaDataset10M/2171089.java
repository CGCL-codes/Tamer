package pl.sind.http;

import java.net.URI;
import pl.sind.blip.message.BlipMessagePart;

/**
 * Interface representing objects abstracting HTTP conectivity with blip.
 * 
 * @author Lukasz Wozniak
 * 
 */
public interface HttpConnector<E> {

    public static final HttpHeader[] EMPTY_HEADERS = new HttpHeader[] {};

    /**
	 * Sets default headers used in every request.
	 * 
	 * @param headers
	 */
    public void setDefaultHeaders(HttpHeader[] headers);

    /**
	 * Gets a remote resource and returns stream to it wrapped in
	 * {@link Download}.
	 * 
	 * @param headers
	 *            additional headers to use on request.
	 * @param target
	 *            uri of resource
	 * @return {@link Download}
	 */
    public Download doDownload(HttpHeader[] headers, URI target) throws HttpRequestException;

    /**
	 * Executes GET method.
	 * 
	 * @param method
	 *            method to be used
	 * @param headers
	 *            additional headers to use on request.
	 * @param auth
	 *            should authentication be performed flag
	 * @param target
	 *            uri to be called
	 * @param body
	 *            message body to be sent
	 * @return result of request
	 * @throws HttpRequestException
	 *             on connection errors
	 */
    public HttpResponse<E> doRequest(HttpMethods method, HttpHeader[] headers, boolean auth, URI target, BlipMessagePart body) throws HttpRequestException;
}
