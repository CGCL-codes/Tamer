package com.unboundid.ldap.listener;

import java.util.Arrays;
import java.util.List;
import com.unboundid.ldap.protocol.AddRequestProtocolOp;
import com.unboundid.ldap.protocol.AddResponseProtocolOp;
import com.unboundid.ldap.protocol.BindRequestProtocolOp;
import com.unboundid.ldap.protocol.BindResponseProtocolOp;
import com.unboundid.ldap.protocol.CompareRequestProtocolOp;
import com.unboundid.ldap.protocol.CompareResponseProtocolOp;
import com.unboundid.ldap.protocol.DeleteRequestProtocolOp;
import com.unboundid.ldap.protocol.DeleteResponseProtocolOp;
import com.unboundid.ldap.protocol.ExtendedRequestProtocolOp;
import com.unboundid.ldap.protocol.ExtendedResponseProtocolOp;
import com.unboundid.ldap.protocol.IntermediateResponseProtocolOp;
import com.unboundid.ldap.protocol.LDAPMessage;
import com.unboundid.ldap.protocol.ModifyRequestProtocolOp;
import com.unboundid.ldap.protocol.ModifyResponseProtocolOp;
import com.unboundid.ldap.protocol.ModifyDNRequestProtocolOp;
import com.unboundid.ldap.protocol.ModifyDNResponseProtocolOp;
import com.unboundid.ldap.protocol.SearchRequestProtocolOp;
import com.unboundid.ldap.protocol.SearchResultDoneProtocolOp;
import com.unboundid.ldap.sdk.AddRequest;
import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.CompareRequest;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.DeleteRequest;
import com.unboundid.ldap.sdk.ExtendedRequest;
import com.unboundid.ldap.sdk.ExtendedResult;
import com.unboundid.ldap.sdk.GenericSASLBindRequest;
import com.unboundid.ldap.sdk.IntermediateResponse;
import com.unboundid.ldap.sdk.IntermediateResponseListener;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ModifyRequest;
import com.unboundid.ldap.sdk.ModifyDNRequest;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.ServerSet;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.util.Debug;
import com.unboundid.util.NotMutable;
import com.unboundid.util.StaticUtils;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;
import com.unboundid.util.Validator;
import static com.unboundid.ldap.listener.ListenerMessages.*;

/**
 * This class provides an implementation of a simple LDAP listener request
 * handler that may be used to forward the request to another LDAP directory
 * server.
 */
@NotMutable()
@ThreadSafety(level = ThreadSafetyLevel.COMPLETELY_THREADSAFE)
public final class ProxyRequestHandler extends LDAPListenerRequestHandler implements IntermediateResponseListener {

    /**
   * The serial version UID for this serializable class.
   */
    private static final long serialVersionUID = -8714030276701707669L;

    private final LDAPConnection ldapConnection;

    private final LDAPListenerClientConnection listenerConnection;

    private final ServerSet serverSet;

    /**
   * Creates a new instance of this proxy request handler that will use the
   * provided {@link ServerSet} to connect to an LDAP server.
   *
   * @param  serverSet  The server that will be used to create LDAP connections
   *                    to forward any requests received.  It must not be
   *                    {@code null}.
   */
    public ProxyRequestHandler(final ServerSet serverSet) {
        Validator.ensureNotNull(serverSet);
        this.serverSet = serverSet;
        ldapConnection = null;
        listenerConnection = null;
    }

    /**
   * Creates a new instance of this proxy request handler with the provided
   * information.
   *
   * @param  serverSet           The server that will be used to create LDAP
   *                             connections to forward any requests received.
   *                             It must not be {@code null}.
   * @param  ldapConnection      The connection to the LDAP server to which
   *                             requests will be forwarded.
   * @param  listenerConnection  The client connection with which this request
   *                             handler is associated.
   */
    private ProxyRequestHandler(final ServerSet serverSet, final LDAPConnection ldapConnection, final LDAPListenerClientConnection listenerConnection) {
        this.serverSet = serverSet;
        this.ldapConnection = ldapConnection;
        this.listenerConnection = listenerConnection;
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public ProxyRequestHandler newInstance(final LDAPListenerClientConnection connection) throws LDAPException {
        return new ProxyRequestHandler(serverSet, serverSet.getConnection(), connection);
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public void closeInstance() {
        ldapConnection.close();
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public LDAPMessage processAddRequest(final int messageID, final AddRequestProtocolOp request, final List<Control> controls) {
        final AddRequest addRequest = new AddRequest(request.getDN(), request.getAttributes());
        if (!controls.isEmpty()) {
            addRequest.setControls(controls);
        }
        addRequest.setIntermediateResponseListener(this);
        LDAPResult addResult;
        try {
            addResult = ldapConnection.add(addRequest);
        } catch (final LDAPException le) {
            Debug.debugException(le);
            addResult = le.toLDAPResult();
        }
        final AddResponseProtocolOp addResponseProtocolOp = new AddResponseProtocolOp(addResult.getResultCode().intValue(), addResult.getMatchedDN(), addResult.getDiagnosticMessage(), Arrays.asList(addResult.getReferralURLs()));
        return new LDAPMessage(messageID, addResponseProtocolOp, Arrays.asList(addResult.getResponseControls()));
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public LDAPMessage processBindRequest(final int messageID, final BindRequestProtocolOp request, final List<Control> controls) {
        final Control[] controlArray;
        if ((controls == null) || (controls.isEmpty())) {
            controlArray = StaticUtils.NO_CONTROLS;
        } else {
            controlArray = new Control[controls.size()];
            controls.toArray(controlArray);
        }
        final BindRequest bindRequest;
        if (request.getCredentialsType() == BindRequestProtocolOp.CRED_TYPE_SIMPLE) {
            bindRequest = new SimpleBindRequest(request.getBindDN(), request.getSimplePassword().getValue(), controlArray);
        } else {
            bindRequest = new GenericSASLBindRequest(request.getBindDN(), request.getSASLMechanism(), request.getSASLCredentials(), controlArray);
        }
        bindRequest.setIntermediateResponseListener(this);
        LDAPResult bindResult;
        try {
            bindResult = ldapConnection.bind(bindRequest);
        } catch (final LDAPException le) {
            Debug.debugException(le);
            bindResult = le.toLDAPResult();
        }
        final BindResponseProtocolOp bindResponseProtocolOp = new BindResponseProtocolOp(bindResult.getResultCode().intValue(), bindResult.getMatchedDN(), bindResult.getDiagnosticMessage(), Arrays.asList(bindResult.getReferralURLs()), null);
        return new LDAPMessage(messageID, bindResponseProtocolOp, Arrays.asList(bindResult.getResponseControls()));
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public LDAPMessage processCompareRequest(final int messageID, final CompareRequestProtocolOp request, final List<Control> controls) {
        final CompareRequest compareRequest = new CompareRequest(request.getDN(), request.getAttributeName(), request.getAssertionValue().getValue());
        if (!controls.isEmpty()) {
            compareRequest.setControls(controls);
        }
        compareRequest.setIntermediateResponseListener(this);
        LDAPResult compareResult;
        try {
            compareResult = ldapConnection.compare(compareRequest);
        } catch (final LDAPException le) {
            Debug.debugException(le);
            compareResult = le.toLDAPResult();
        }
        final CompareResponseProtocolOp compareResponseProtocolOp = new CompareResponseProtocolOp(compareResult.getResultCode().intValue(), compareResult.getMatchedDN(), compareResult.getDiagnosticMessage(), Arrays.asList(compareResult.getReferralURLs()));
        return new LDAPMessage(messageID, compareResponseProtocolOp, Arrays.asList(compareResult.getResponseControls()));
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public LDAPMessage processDeleteRequest(final int messageID, final DeleteRequestProtocolOp request, final List<Control> controls) {
        final DeleteRequest deleteRequest = new DeleteRequest(request.getDN());
        if (!controls.isEmpty()) {
            deleteRequest.setControls(controls);
        }
        deleteRequest.setIntermediateResponseListener(this);
        LDAPResult deleteResult;
        try {
            deleteResult = ldapConnection.delete(deleteRequest);
        } catch (final LDAPException le) {
            Debug.debugException(le);
            deleteResult = le.toLDAPResult();
        }
        final DeleteResponseProtocolOp deleteResponseProtocolOp = new DeleteResponseProtocolOp(deleteResult.getResultCode().intValue(), deleteResult.getMatchedDN(), deleteResult.getDiagnosticMessage(), Arrays.asList(deleteResult.getReferralURLs()));
        return new LDAPMessage(messageID, deleteResponseProtocolOp, Arrays.asList(deleteResult.getResponseControls()));
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public LDAPMessage processExtendedRequest(final int messageID, final ExtendedRequestProtocolOp request, final List<Control> controls) {
        final ExtendedRequest extendedRequest;
        if (controls.isEmpty()) {
            extendedRequest = new ExtendedRequest(request.getOID(), request.getValue());
        } else {
            final Control[] controlArray = new Control[controls.size()];
            controls.toArray(controlArray);
            extendedRequest = new ExtendedRequest(request.getOID(), request.getValue(), controlArray);
        }
        extendedRequest.setIntermediateResponseListener(this);
        try {
            final ExtendedResult extendedResult = ldapConnection.processExtendedOperation(extendedRequest);
            final ExtendedResponseProtocolOp extendedResponseProtocolOp = new ExtendedResponseProtocolOp(extendedResult.getResultCode().intValue(), extendedResult.getMatchedDN(), extendedResult.getDiagnosticMessage(), Arrays.asList(extendedResult.getReferralURLs()), extendedResult.getOID(), extendedResult.getValue());
            return new LDAPMessage(messageID, extendedResponseProtocolOp, Arrays.asList(extendedResult.getResponseControls()));
        } catch (final LDAPException le) {
            Debug.debugException(le);
            final ExtendedResponseProtocolOp extendedResponseProtocolOp = new ExtendedResponseProtocolOp(le.getResultCode().intValue(), le.getMatchedDN(), le.getMessage(), Arrays.asList(le.getReferralURLs()), null, null);
            return new LDAPMessage(messageID, extendedResponseProtocolOp, Arrays.asList(le.getResponseControls()));
        }
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public LDAPMessage processModifyRequest(final int messageID, final ModifyRequestProtocolOp request, final List<Control> controls) {
        final ModifyRequest modifyRequest = new ModifyRequest(request.getDN(), request.getModifications());
        if (!controls.isEmpty()) {
            modifyRequest.setControls(controls);
        }
        modifyRequest.setIntermediateResponseListener(this);
        LDAPResult modifyResult;
        try {
            modifyResult = ldapConnection.modify(modifyRequest);
        } catch (final LDAPException le) {
            Debug.debugException(le);
            modifyResult = le.toLDAPResult();
        }
        final ModifyResponseProtocolOp modifyResponseProtocolOp = new ModifyResponseProtocolOp(modifyResult.getResultCode().intValue(), modifyResult.getMatchedDN(), modifyResult.getDiagnosticMessage(), Arrays.asList(modifyResult.getReferralURLs()));
        return new LDAPMessage(messageID, modifyResponseProtocolOp, Arrays.asList(modifyResult.getResponseControls()));
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public LDAPMessage processModifyDNRequest(final int messageID, final ModifyDNRequestProtocolOp request, final List<Control> controls) {
        final ModifyDNRequest modifyDNRequest = new ModifyDNRequest(request.getDN(), request.getNewRDN(), request.deleteOldRDN(), request.getNewSuperiorDN());
        if (!controls.isEmpty()) {
            modifyDNRequest.setControls(controls);
        }
        modifyDNRequest.setIntermediateResponseListener(this);
        LDAPResult modifyDNResult;
        try {
            modifyDNResult = ldapConnection.modifyDN(modifyDNRequest);
        } catch (final LDAPException le) {
            Debug.debugException(le);
            modifyDNResult = le.toLDAPResult();
        }
        final ModifyDNResponseProtocolOp modifyDNResponseProtocolOp = new ModifyDNResponseProtocolOp(modifyDNResult.getResultCode().intValue(), modifyDNResult.getMatchedDN(), modifyDNResult.getDiagnosticMessage(), Arrays.asList(modifyDNResult.getReferralURLs()));
        return new LDAPMessage(messageID, modifyDNResponseProtocolOp, Arrays.asList(modifyDNResult.getResponseControls()));
    }

    /**
   * {@inheritDoc}
   */
    @Override()
    public LDAPMessage processSearchRequest(final int messageID, final SearchRequestProtocolOp request, final List<Control> controls) {
        final String[] attrs;
        final List<String> attrList = request.getAttributes();
        if (attrList.isEmpty()) {
            attrs = StaticUtils.NO_STRINGS;
        } else {
            attrs = new String[attrList.size()];
            attrList.toArray(attrs);
        }
        final ProxySearchResultListener searchListener = new ProxySearchResultListener(listenerConnection, messageID);
        final SearchRequest searchRequest = new SearchRequest(searchListener, request.getBaseDN(), request.getScope(), request.getDerefPolicy(), request.getSizeLimit(), request.getTimeLimit(), request.typesOnly(), request.getFilter(), attrs);
        if (!controls.isEmpty()) {
            searchRequest.setControls(controls);
        }
        searchRequest.setIntermediateResponseListener(this);
        LDAPResult searchResult;
        try {
            searchResult = ldapConnection.search(searchRequest);
        } catch (final LDAPException le) {
            Debug.debugException(le);
            searchResult = le.toLDAPResult();
        }
        final SearchResultDoneProtocolOp searchResultDoneProtocolOp = new SearchResultDoneProtocolOp(searchResult.getResultCode().intValue(), searchResult.getMatchedDN(), searchResult.getDiagnosticMessage(), Arrays.asList(searchResult.getReferralURLs()));
        return new LDAPMessage(messageID, searchResultDoneProtocolOp, Arrays.asList(searchResult.getResponseControls()));
    }

    /**
   * {@inheritDoc}
   */
    public void intermediateResponseReturned(final IntermediateResponse intermediateResponse) {
        try {
            listenerConnection.sendIntermediateResponse(intermediateResponse.getMessageID(), new IntermediateResponseProtocolOp(intermediateResponse.getOID(), intermediateResponse.getValue()), intermediateResponse.getControls());
        } catch (final LDAPException le) {
            Debug.debugException(le);
        }
    }
}
