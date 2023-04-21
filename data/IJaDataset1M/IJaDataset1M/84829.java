package edu.uiuc.ncsa.security.delegation.server.request;

import edu.uiuc.ncsa.security.delegation.server.issuers.AuthorizationGrantIssuer;
import edu.uiuc.ncsa.security.delegation.services.Response;
import edu.uiuc.ncsa.security.delegation.services.Server;
import edu.uiuc.ncsa.security.delegation.storage.Client;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Created by Jeff Gaynor<br>
 * on May 13, 2011 at  11:57:19 AM
 */
public class AGRequest extends IssuerRequest {

    public AGRequest(HttpServletRequest servletRequest, Client client) {
        super(servletRequest, client);
    }

    @Override
    public Response process(Server server) {
        if (server instanceof AuthorizationGrantIssuer) {
            return ((AuthorizationGrantIssuer) server).processAGRequest(this);
        }
        return super.process(server);
    }
}
