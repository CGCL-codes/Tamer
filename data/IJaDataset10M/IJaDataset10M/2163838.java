package net.grinder.plugin.http;

import java.util.Map;

/**
 * Abstract interface to HTTP implementations such as HTTPClient and
 * HttpURLConnection.
 *
 * @author Philip Aston
 * @version $Revision: 887 $
 */
interface HTTPHandler {

    public void reset() throws HTTPHandlerException;

    public String sendRequest(RequestData requestData) throws HTTPHandlerException;

    interface AuthorizationData {
    }

    interface BasicAuthorizationData extends AuthorizationData {

        public String getRealm() throws HTTPHandlerException;

        public String getUser() throws HTTPHandlerException;

        public String getPassword() throws HTTPHandlerException;
    }

    interface RequestData {

        public Map getHeaders() throws HTTPHandlerException;

        public AuthorizationData getAuthorizationData() throws HTTPHandlerException;

        public String getPostString() throws HTTPHandlerException;

        public String getURLString() throws HTTPHandlerException;
    }
}
