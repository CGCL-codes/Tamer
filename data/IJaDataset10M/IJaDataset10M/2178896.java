package org.restlet.client.engine.http.adapter;

import java.io.IOException;
import java.util.logging.Level;
import org.restlet.client.Context;
import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.Uniform;
import org.restlet.client.data.Method;
import org.restlet.client.data.Parameter;
import org.restlet.client.data.Status;
import org.restlet.client.engine.Edition;
import org.restlet.client.engine.http.ClientCall;
import org.restlet.client.engine.http.HttpClientHelper;
import org.restlet.client.engine.http.header.HeaderConstants;
import org.restlet.client.engine.http.header.HeaderUtils;
import org.restlet.client.util.Series;

/**
 * Converter of high-level uniform calls into low-level HTTP client calls.
 * 
 * @author Jerome Louvel
 */
public class ClientAdapter extends Adapter {

    /**
     * Constructor.
     * 
     * @param context
     *            The context to use.
     */
    public ClientAdapter(Context context) {
        super(context);
    }

    /**
     * Commits the changes to a handled HTTP client call back into the original
     * uniform call. The default implementation first invokes the
     * "addResponseHeaders" then asks the "htppCall" to send the response back
     * to the client.
     * 
     * @param httpCall
     *            The original HTTP call.
     * @param request
     *            The high-level request.
     * @param response
     *            The high-level response.
     * @throws Exception
     */
    public void commit(final ClientCall httpCall, Request request, Response response) throws Exception {
        if (httpCall != null) {
            if (request.getOnResponse() != null) {
                final Uniform userCallback = request.getOnResponse();
                httpCall.sendRequest(request, response, new Uniform() {

                    public void handle(Request request, Response response) {
                        try {
                            updateResponse(response, new Status(httpCall.getStatusCode(), null, httpCall.getReasonPhrase(), null), httpCall);
                            userCallback.handle(request, response);
                        } catch (Exception e) {
                            if ((response.getStatus() == null) || !response.getStatus().isError()) {
                                response.setStatus(Status.CONNECTOR_ERROR_INTERNAL, e);
                                userCallback.handle(request, response);
                            }
                        }
                    }
                });
            } else {
                if (Edition.CURRENT == Edition.GWT) {
                    System.err.println("HTTP client calls must have a callback in the GWT edition");
                } else {
                }
            }
        }
    }

    /**
     * Reads the response headers of a handled HTTP client call to update the
     * original uniform call.
     * 
     * @param httpCall
     *            The handled HTTP client call.
     * @param response
     *            The high-level response to update.
     */
    protected void readResponseHeaders(ClientCall httpCall, Response response) {
        try {
            Series<Parameter> responseHeaders = httpCall.getResponseHeaders();
            response.getAttributes().put(HeaderConstants.ATTRIBUTE_HEADERS, responseHeaders);
            HeaderUtils.copyResponseTransportHeaders(responseHeaders, response);
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().log(Level.FINE, "An error occured during the processing of the HTTP response.", e);
            response.setStatus(Status.CONNECTOR_ERROR_INTERNAL, e);
        }
    }

    /**
     * Converts a low-level HTTP call into a high-level uniform call.
     * 
     * @param client
     *            The HTTP client that will handle the call.
     * @param request
     *            The high-level request.
     * @return A new high-level uniform call.
     */
    public ClientCall toSpecific(HttpClientHelper client, Request request) {
        ClientCall result = client.create(request);
        if (result != null) {
            HeaderUtils.addGeneralHeaders(request, result.getRequestHeaders());
            HeaderUtils.addRequestHeaders(request, result.getRequestHeaders());
            if (request.isEntityAvailable()) {
                HeaderUtils.addEntityHeaders(request.getEntity(), result.getRequestHeaders());
            }
        }
        return result;
    }

    /**
     * Updates the response with information from the lower-level HTTP client
     * call.
     * 
     * @param response
     *            The response to update.
     * @param status
     *            The response status to apply.
     * @param httpCall
     *            The source HTTP client call.
     * @throws IOException
     */
    public void updateResponse(Response response, Status status, ClientCall httpCall) {
        response.setStatus(status);
        response.getServerInfo().setAddress(httpCall.getServerAddress());
        response.getServerInfo().setPort(httpCall.getServerPort());
        readResponseHeaders(httpCall, response);
        response.setEntity(httpCall.getResponseEntity(response));
        if (response.getEntity() != null) {
            if (response.getEntity().getSize() == 0) {
                response.getEntity().release();
            } else if (response.getRequest().getMethod().equals(Method.HEAD)) {
                response.getEntity().release();
            } else if (response.getStatus().equals(Status.SUCCESS_NO_CONTENT)) {
                response.getEntity().release();
            } else if (response.getStatus().equals(Status.SUCCESS_RESET_CONTENT)) {
                response.getEntity().release();
                response.setEntity(null);
            } else if (response.getStatus().equals(Status.REDIRECTION_NOT_MODIFIED)) {
                response.getEntity().release();
            } else if (response.getStatus().isInformational()) {
                response.getEntity().release();
                response.setEntity(null);
            }
        }
    }
}
