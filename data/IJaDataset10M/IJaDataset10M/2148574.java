package org.apache.http;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;

/**
 * HTTP protocol interceptor is a routine that implements a specific aspect of 
 * the HTTP protocol. Usually protocol interceptors are expected to act upon 
 * one specific header or a group of related headers of the incoming message 
 * or populate the outgoing message with one specific header or a group of 
 * related headers. 
 * <p>
 *  Protocol Interceptors can also manipulate content entities enclosed with messages.
 * Usually this is accomplished by using the 'Decorator' pattern where a wrapper 
 * entity class is used to decorate the original entity. 
 * <p>
 * Protocol interceptors must be implemented as thread-safe. Similarly to 
 * servlets, protocol interceptors should not use instance variables unless 
 * access to those variables is synchronized.
 *
 *
 *
 * <!-- empty lines above to avoid 'svn diff' context problems -->
 * @version $Revision: 744522 $
 * 
 * @since 4.0
 */
public interface HttpRequestInterceptor {

    /**
     * Processes a request.
     * On the client side, this step is performed before the request is
     * sent to the server. On the server side, this step is performed
     * on incoming messages before the message body is evaluated.
     *
     * @param request   the request to preprocess
     * @param context   the context for the request
     *
     * @throws HttpException in case of an HTTP protocol violation
     * @throws IOException in case of an I/O error
     */
    void process(HttpRequest request, HttpContext context) throws HttpException, IOException;
}
