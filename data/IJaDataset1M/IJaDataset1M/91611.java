package org.nakedobjects.remoting.facade.proxy;

import org.apache.log4j.Logger;
import org.nakedobjects.remoting.NakedObjectsRemoteException;
import org.nakedobjects.remoting.client.ClientConnection;
import org.nakedobjects.remoting.exchange.GetPropertiesResponse;
import org.nakedobjects.remoting.exchange.OpenSessionRequest;
import org.nakedobjects.remoting.exchange.OpenSessionResponse;
import org.nakedobjects.remoting.exchange.AuthorizationResponse;
import org.nakedobjects.remoting.exchange.AuthorizationRequestUsability;
import org.nakedobjects.remoting.exchange.AuthorizationRequestVisibility;
import org.nakedobjects.remoting.exchange.ClearAssociationRequest;
import org.nakedobjects.remoting.exchange.ClearAssociationResponse;
import org.nakedobjects.remoting.exchange.ClearValueRequest;
import org.nakedobjects.remoting.exchange.ClearValueResponse;
import org.nakedobjects.remoting.exchange.CloseSessionRequest;
import org.nakedobjects.remoting.exchange.CloseSessionResponse;
import org.nakedobjects.remoting.exchange.ExecuteClientActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteClientActionResponse;
import org.nakedobjects.remoting.exchange.ExecuteServerActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteServerActionResponse;
import org.nakedobjects.remoting.exchange.FindInstancesRequest;
import org.nakedobjects.remoting.exchange.FindInstancesResponse;
import org.nakedobjects.remoting.exchange.GetObjectRequest;
import org.nakedobjects.remoting.exchange.GetObjectResponse;
import org.nakedobjects.remoting.exchange.GetPropertiesRequest;
import org.nakedobjects.remoting.exchange.HasInstancesRequest;
import org.nakedobjects.remoting.exchange.HasInstancesResponse;
import org.nakedobjects.remoting.exchange.OidForServiceRequest;
import org.nakedobjects.remoting.exchange.OidForServiceResponse;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.exchange.ResolveFieldResponse;
import org.nakedobjects.remoting.exchange.ResolveObjectRequest;
import org.nakedobjects.remoting.exchange.ResolveFieldRequest;
import org.nakedobjects.remoting.exchange.ResolveObjectResponse;
import org.nakedobjects.remoting.exchange.ResponseEnvelope;
import org.nakedobjects.remoting.exchange.SetAssociationRequest;
import org.nakedobjects.remoting.exchange.SetAssociationResponse;
import org.nakedobjects.remoting.exchange.SetValueRequest;
import org.nakedobjects.remoting.exchange.SetValueResponse;
import org.nakedobjects.remoting.facade.ServerFacade;

/**
 * previously called <tt>ClientConnection</tt>.
 */
public class ServerFacadeProxy implements ServerFacade {

    private static final Logger LOG = Logger.getLogger(ServerFacadeProxy.class);

    private final ClientConnection connection;

    public ServerFacadeProxy(final ClientConnection connection) {
        this.connection = connection;
    }

    public void init() {
        connection.init();
    }

    public void shutdown() {
        connection.shutdown();
    }

    public CloseSessionResponse closeSession(CloseSessionRequest request) {
        execute(request);
        return request.getResponse();
    }

    public OpenSessionResponse openSession(OpenSessionRequest request) {
        execute(request);
        return request.getResponse();
    }

    public AuthorizationResponse authorizeUsability(final AuthorizationRequestUsability request) {
        execute(request);
        return request.getResponse();
    }

    public AuthorizationResponse authorizeVisibility(AuthorizationRequestVisibility request) {
        execute(request);
        return request.getResponse();
    }

    public SetAssociationResponse setAssociation(final SetAssociationRequest request) {
        execute(request);
        return request.getResponse();
    }

    public SetValueResponse setValue(SetValueRequest request) {
        execute(request);
        return request.getResponse();
    }

    public ClearAssociationResponse clearAssociation(ClearAssociationRequest request) {
        execute(request);
        return request.getResponse();
    }

    public ClearValueResponse clearValue(ClearValueRequest request) {
        execute(request);
        return request.getResponse();
    }

    public ExecuteServerActionResponse executeServerAction(ExecuteServerActionRequest request) {
        execute(request);
        return request.getResponse();
    }

    public ExecuteClientActionResponse executeClientAction(ExecuteClientActionRequest request) {
        execute(request);
        return request.getResponse();
    }

    public GetObjectResponse getObject(GetObjectRequest request) {
        execute(request);
        return request.getResponse();
    }

    public ResolveObjectResponse resolveImmediately(ResolveObjectRequest request) {
        execute(request);
        return request.getResponse();
    }

    public ResolveFieldResponse resolveField(ResolveFieldRequest request) {
        execute(request);
        return request.getResponse();
    }

    public FindInstancesResponse findInstances(FindInstancesRequest request) {
        execute(request);
        return request.getResponse();
    }

    public HasInstancesResponse hasInstances(HasInstancesRequest request) {
        execute(request);
        return request.getResponse();
    }

    public OidForServiceResponse oidForService(OidForServiceRequest request) {
        execute(request);
        return request.getResponse();
    }

    public GetPropertiesResponse getProperties(GetPropertiesRequest request) {
        execute(request);
        return request.getResponse();
    }

    private void execute(final Request request) {
        synchronized (connection) {
            final ResponseEnvelope response = connection.executeRemotely(request);
            if (request.getId() != response.getId()) {
                throw new NakedObjectsRemoteException("Response out of sequence with respect to the request: " + request.getId() + " & " + response.getId() + " respectively");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("response " + response);
            }
            request.setResponse(response.getObject());
        }
    }
}
