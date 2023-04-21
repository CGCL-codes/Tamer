package com.bpreece.webserver.core;

import com.bpreece.webserver.*;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Ben
 */
public abstract class Page implements ExchangeHandler {

    /**
     * A <code>MessageFormat</code> for sending an HTML meta refresh directive.
     * The parameters are intended to be {0} the time, {1} the URL, and {2} the
     * body content.
     */
    private static final String redirectFormat = "<html><head>" + "<meta http-equiv=\"refresh\" content=\"{0};url={1}\"/>" + "</head><body>{2}</body></html>";

    /**
     * Load the <code>Page</code> class with the given class name, and
     * return a new instance of that class.
     *
     * @param className
     * @return
     * @throws Exception
     */
    public static Page newInstance(String className) throws Exception {
        Class<?> pageClass = Class.forName(className);
        return (Page) pageClass.getConstructor().newInstance();
    }

    /**
     * Return an HTTP_TEMPORARY_REDIRECT response to redirect to the given
     * location.  The body of the response will be empty.
     *
     * @param exchange
     * @param location
     * @throws IOException
     */
    protected void sendHttpTemporaryRedirect(HttpExchange exchange, String location) throws IOException {
        exchange.setResponseHeader("Location", location);
        exchange.sendResponse(HttpConstants.HTTP_MOVED_TEMP, "Temporary Redirect");
    }

    /**
     * Send an HTTP response with the meta tag set to refresh to the given URL
     * after the given time.
     *
     * @param exchange the <code>HttpExchange</code>
     * @param time the time to pause before redirecting
     * @param url the URL to redirect to
     * @param content the HTML content to display while the redirect is pending
     * @throws IOException
     */
    protected void sendMetaRefresh(HttpExchange exchange, int time, String url, String content) throws IOException {
        Log.debug("redirect after " + time + " to " + url);
        String response = MessageFormat.format(redirectFormat, time, url, content);
        exchange.getResponseBody().write(response.getBytes());
        exchange.sendResponse(HttpConstants.HTTP_OK, "Meta Refresh");
    }

    /**
     * Use the HTTP meta tag to perform an immediate redirect.  This is done by
     * setting the time to zero and the content to empty.
     *
     * @param exchange the <code>HttpExchange</code>
     * @param url the URL to redirect to
     * @throws IOException
     */
    protected void sendMetaRedirect(HttpExchange exchange, String url) throws IOException {
        Log.debug("redirect to " + url);
        sendMetaRefresh(exchange, 0, url, "");
    }

    /**
     * The typical procedure for any page when handling an
     * <code>HttpExchange<code> performs the following steps in this order:
     * <ol><
     * <li>Call <code>getRequestMethod()</code> to determine the HTTP
     *     command (e.g. <code>GET</code> or <code>POST)</code>.</li>
     * <li>Call <code>getRequestHeaders()</code>, if necessary, to examine
     *     any required request headers.</li>
     * <li>For <code>GET</code> requests, call
     *     <code>getRequestURI().getRawQuery(</code>) to handle any query
     *     parameters.</li>
     * <li>For <code>POST</code> requests, call
     *     <code>getRequestBody(</code>) to get an <code>InputStream</code>
     *     for reading the request body.  It's not necessary to close the stream
     *     after reading - it will be closed automatically when the
     *     <code>ExchangeHandler</code> closes the exchange's response body.
     * <li>Call <code>getResponseHeaders()</code> to set any response headers
     *     besides <code>content-length</code>, which is handled
     *     automatically.</li>
     * <li>Call <code>sendResponseHeaders(int,long)</code> to send the
     *     response headers. This must be called before writing to the
     *     response body.</li>
     * <li>Call <code>getResponseBody()</code> to get an
     *     <code>OutputStream</code> to write the response body.  It's not
     *     necessary to close the response body - it will be closed
     *     automatically by the <code>ExchangeHandler</code>.
     * </ol>
     * <p>
     * <code>Page</code>s should catch all checked exceptions and handle them.
     * In the case where the <code>Page</code> really can't handle the
     * exception, it can be rethrown as a <code>HttpStatus<code>, and the
     * server will return a default error page with an internal server error
     * message.
     *
     * @param exchange
     * @throws HttpException
     */
    public abstract void doExchange(HttpExchange exchange) throws HttpException;
}
