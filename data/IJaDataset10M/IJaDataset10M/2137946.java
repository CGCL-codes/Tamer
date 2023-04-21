package org.tripcom.security.main;

import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.jini.core.entry.Entry;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tripcom.integration.entry.ClientInfo;
import org.tripcom.integration.entry.Error;
import org.tripcom.integration.entry.ErrorResultExternal;
import org.tripcom.integration.entry.ManagementDataResultExtenal;
import org.tripcom.integration.entry.ManagementDataResultExternal;
import org.tripcom.integration.entry.ManagementOperation;
import org.tripcom.integration.entry.ManagementOperationExternal;
import org.tripcom.integration.entry.MgmtMetaMEntry;
import org.tripcom.integration.entry.Operation;
import org.tripcom.integration.entry.OutDMEntry;
import org.tripcom.integration.entry.OutMetaMEntry;
import org.tripcom.integration.entry.OutOperationExternal;
import org.tripcom.integration.entry.RdDMEntry;
import org.tripcom.integration.entry.RdOperationExternal;
import org.tripcom.integration.entry.SecurityAssertionsInfo;
import org.tripcom.integration.entry.SecurityCookieInfo;
import org.tripcom.integration.entry.SecurityInfo;
import org.tripcom.integration.entry.SetCookieEntry;
import org.tripcom.integration.entry.TransactionOperationExternal;
import org.tripcom.integration.entry.TripleEntry;
import org.tripcom.security.Action;
import org.tripcom.security.Assertion;
import org.tripcom.security.Constants;
import org.tripcom.security.Request;
import org.tripcom.security.metadata.MetadataRetriever;
import org.tripcom.security.policies.PolicyManager;
import org.tripcom.security.util.Certificates;
import org.tripcom.security.util.JavaSpaces;
import org.tripcom.security.util.Util;

/**
 * Implementation of <tt>RequestHandler</tt> based on the communication on
 * JavaSpaces.
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public class JavaSpaceRequestHandler implements RequestHandler {

    /** The log object. */
    private static Log log = LogFactory.getLog(JavaSpaceRequestHandler.class);

    /** The space corresponding to the inbound area. */
    private JavaSpace inboundArea;

    /** The space corresponding to the outbound area. */
    private JavaSpace outboundArea;

    /** The space corresponding to the internal area. */
    private JavaSpace internalArea;

    /** The space corresponding to the security area. */
    private JavaSpace securityArea;

    /** The metadata retriever module. */
    private MetadataRetriever metadateRetriever;

    /** The policy manager module. */
    private PolicyManager policyManager;

    /** The ID of this Security Manager instance. */
    private int securityManagerId;

    /** The total number of Security Manager instances. */
    private int securityManagerCount;

    /** The event registration objects obtained from notify operations. */
    private Collection<EventRegistration> registrations;

    /**
	 * Create a new instance of the object given the provided dependencies.
	 * 
	 * @param inboundArea the space representing the inbound area (not null).
	 * @param outboundArea the space representing the outbound area (not null).
	 * @param internalArea the space representing the internal area (not null).
	 * @param securityArea the space representing the security area (not null).
	 * @param metadataRetriever the metadata retriever module (not null).
	 * @param policyManager the policy manager module (not null).
	 * @param securityManagerId the ID of this Security Manager instance.
	 * @param securityManagerCount the number of Security Manager instances.
	 */
    public JavaSpaceRequestHandler(JavaSpace inboundArea, JavaSpace outboundArea, JavaSpace internalArea, JavaSpace securityArea, MetadataRetriever metadataRetriever, PolicyManager policyManager, int securityManagerId, int securityManagerCount) {
        if ((inboundArea == null) || (outboundArea == null) || (internalArea == null) || (securityArea == null) || (metadataRetriever == null) || (policyManager == null)) {
            throw new NullPointerException();
        }
        if ((securityManagerCount <= 0) || (securityManagerId < 0) || (securityManagerId >= securityManagerCount)) {
            throw new IllegalArgumentException();
        }
        this.inboundArea = inboundArea;
        this.outboundArea = outboundArea;
        this.internalArea = internalArea;
        this.securityArea = securityArea;
        this.metadateRetriever = metadataRetriever;
        this.policyManager = policyManager;
        this.securityManagerId = securityManagerId;
        this.securityManagerCount = securityManagerCount;
        this.registrations = new ArrayList<EventRegistration>();
    }

    /**
	 * {@inheritDoc}
	 */
    public void processError(Request request, Error error, String message) {
        if ((request == null) || (error == null)) {
            throw new NullPointerException();
        }
        processError(request.getOperationId(), error, message);
    }

    /**
	 * Signals an error with the parameters specified.
	 * 
	 * @param operationId an optional operation ID (possibly null).
	 * @param error the error code (not null).
	 * @param message an optional message associated to the error.
	 */
    private void processError(Long operationId, Error error, String message) {
        assert (error != null);
        ErrorResultExternal entry = new ErrorResultExternal();
        entry.operationID = operationId;
        entry.error = error;
        entry.description = message;
        JavaSpaces.write(outboundArea, entry);
    }

    /**
	 * {@inheritDoc}
	 */
    public void processCookie(Request request, String cookie) {
        if ((request == null) || (cookie == null)) {
            throw new NullPointerException();
        }
        SetCookieEntry entry = new SetCookieEntry();
        entry.operationID = request.getOperationId();
        entry.cookie = cookie;
        JavaSpaces.write(outboundArea, entry);
    }

    /**
	 * {@inheritDoc}
	 */
    public void registerIncomingRequestListener(final RequestListener listener) {
        final Entry template = new Operation();
        EventRegistration registration = JavaSpaces.notify(inboundArea, template, Lease.FOREVER, new RemoteEventListener() {

            public void notify(RemoteEvent event) {
                try {
                    Entry entry = JavaSpaces.takeIfExists(inboundArea, template);
                    if (entry != null) {
                        Request request = null;
                        try {
                            request = buildRequest(entry);
                        } catch (Exception ex) {
                            if (log.isWarnEnabled()) {
                                log.warn("Received malformed request: " + ex.getMessage());
                            }
                            processError(((Operation) entry).operationID, Error.SMMalformedRequest, ex.getMessage());
                            return;
                        }
                        listener.notifyRequestReceipt(request);
                    }
                } catch (Exception ex) {
                    log.error("Exception caught while signaling " + "incoming request", ex);
                }
            }
        });
        registrations.add(registration);
    }

    /**
	 * {@inheritDoc}. This method sends a {@link NotificationEntry} to all the
	 * others Security Manager instances. Entries written in the bus are taken
	 * and therefore deleted by the target component. If this doesn't happen, we
	 * rely on entry timeout mechanism of JavaSpaces in order to remove the
	 * entry.
	 */
    public void notifyAuthorizedRequest(Request request) {
        if (request == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < securityManagerCount; ++i) {
            if (i != securityManagerId) {
                JavaSpaces.write(securityArea, new NotificationEntry(i, request.getRequestEntry()));
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void registerAuthorizedRequestListener(final RequestListener listener) {
        final Entry template = new NotificationEntry(securityManagerId, null);
        EventRegistration registration = JavaSpaces.notify(securityArea, template, Lease.FOREVER, new RemoteEventListener() {

            public void notify(RemoteEvent event) {
                try {
                    NotificationEntry entry = (NotificationEntry) JavaSpaces.takeIfExists(securityArea, template);
                    if (entry != null) {
                        Request request = buildRequest(entry.request);
                        listener.notifyRequestReceipt(request);
                    }
                } catch (Exception ex) {
                    log.error("Exception caught while signaling " + "request authorized by another security " + "manager.", ex);
                }
            }
        });
        registrations.add(registration);
    }

    /**
	 * Release the resources allocated by the instance. Currently, this method
	 * unregister the listeners established with previous JavaSpace notify
	 * operations.
	 */
    public void close() {
        for (EventRegistration registration : registrations) {
            try {
                if (registration == null) {
                    continue;
                }
                Lease lease = registration.getLease();
                if (lease != null) {
                    lease.cancel();
                }
            } catch (Exception ex) {
                log.warn("Exception caught while unregistering notify listener", ex);
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void processAuthorizedRequest(Request request) {
        if (request == null) {
            throw new NullPointerException();
        }
        Entry requestEntry = request.getRequestEntry();
        if (requestEntry instanceof OutOperationExternal) {
            processWriteOperation((OutOperationExternal) requestEntry, buildClientInfo(request));
        } else if (requestEntry instanceof RdOperationExternal) {
            processReadOperation((RdOperationExternal) requestEntry, buildClientInfo(request), request.getSpaceRestrictions());
        } else if (requestEntry instanceof TransactionOperationExternal) {
            processTransactionOperation((TransactionOperationExternal) requestEntry, buildClientInfo(request));
        } else if (requestEntry instanceof ManagementOperationExternal) {
            ManagementOperationExternal op;
            op = (ManagementOperationExternal) requestEntry;
            if (op.operation == ManagementOperation.SET_POLICY) {
                processSetPolicyOperation(request);
            } else if (op.operation == ManagementOperation.GET_POLICY) {
                processGetPolicyOperation(request);
            } else {
                if ((op.operation == ManagementOperation.Create) && !request.isAnonymous() && !storeInitialPolicy(request)) {
                    processError(request, Error.SMInternalError, "Unable to store the initial policy");
                } else {
                    processManagementOperation(op, buildClientInfo(request));
                }
            }
        }
    }

    /**
	 * Process a WriteOperationExternal entry. This method sends a
	 * {@link OutMetaMEntry} to the Metadata Manager and a {@link OutDMEntry} to
	 * the Distribution Manager with the data of the received request.
	 * 
	 * @param operation the operation to process (not null).
	 * @param clientInfo the ClientInfo structure to forward to internal
	 *            components (not null).
	 */
    private void processWriteOperation(OutOperationExternal operation, ClientInfo clientInfo) {
        assert (operation != null) && (clientInfo != null);
        OutDMEntry dmEntry = new OutDMEntry();
        dmEntry.clientInfo = clientInfo;
        dmEntry.operationID = operation.operationID;
        dmEntry.space = operation.space;
        dmEntry.transactionID = operation.transactionID;
        dmEntry.timestamp = operation.timestamp;
        dmEntry.data = operation.data;
        JavaSpaces.write(internalArea, dmEntry);
    }

    /**
	 * Process a ReadOperationExternal entry. This method sends a
	 * {@link RdDMEntry} to the Distribution Manager, with the data received in
	 * the incoming request.
	 * 
	 * @param operation the operation to process (not null).
	 * @param clientInfo the ClientInfo structure to forward to internal
	 *            components (not null).
	 * @param subspaces a list of subspaces for which the operation should be
	 *            allowed (not null).
	 */
    private void processReadOperation(RdOperationExternal operation, ClientInfo clientInfo, Collection<String> subspaces) {
        assert (operation != null) && (clientInfo != null);
        Set<URI> subspaceURIs = new HashSet<URI>();
        for (String subspace : subspaces) {
            subspaceURIs.add(Util.toURI(subspace));
        }
        RdDMEntry dmEntry = new RdDMEntry();
        dmEntry.clientInfo = clientInfo;
        dmEntry.kind = operation.kind;
        dmEntry.operationID = operation.operationID;
        dmEntry.space = operation.space;
        dmEntry.subspaces = subspaceURIs;
        dmEntry.template = operation.query;
        dmEntry.timeout = operation.timeout;
        dmEntry.transactionID = operation.transactionID;
        dmEntry.qpData = operation.qpData;
        dmEntry.toFromReasoner = operation.toFromReasoner;
        JavaSpaces.write(internalArea, dmEntry);
    }

    /**
	 * Process a TransactionOperationExternal entry. Currently, this method does
	 * nothing since the Transaction Manager is not included in this prototype.
	 * 
	 * @param operation the operation to process (not null).
	 * @param clientInfo the ClientInfo structure to forward to internal
	 *            components (not null).
	 */
    private void processTransactionOperation(TransactionOperationExternal operation, ClientInfo clientInfo) {
        assert (operation != null) && (clientInfo != null);
    }

    /**
	 * Process a ManagementOperationExternal entry, targeted at internal
	 * components. This method forwards a {@link MgmtMetaMEntry} request to the
	 * Metadata Manager. Note that this method does not handle management
	 * operations targeted at the Security Manager.
	 * 
	 * @param operation the operation to process (not null).
	 * @param clientInfo the ClientInfo structure to forward to internal
	 *            components (not null).
	 */
    private void processManagementOperation(ManagementOperationExternal operation, ClientInfo clientInfo) {
        assert (operation != null) && (clientInfo != null);
        MgmtMetaMEntry mmEntry = new MgmtMetaMEntry();
        mmEntry.clientInfo = clientInfo;
        mmEntry.oper = operation.operation;
        mmEntry.opid = operation.operationID;
        mmEntry.parameters = operation.parameters;
        JavaSpaces.write(internalArea, mmEntry);
    }

    /**
	 * Process a M-OUT management operation targeted at the Security Manager.
	 * This method invokes the Policy Manager in order to store the new policy
	 * for the specified space. A {@link ManagementDataResultExtenal} entry is
	 * returned to the invoking API.
	 * 
	 * @param request the request to process (not null).
	 */
    private void processSetPolicyOperation(Request request) {
        assert (request != null);
        ManagementOperationExternal operation = (ManagementOperationExternal) request.getRequestEntry();
        if ((operation.parameters == null) || (operation.parameters.get(0) == null) || !(operation.parameters.get(0) instanceof URI) || (operation.parameters.get(1) == null) || !(operation.parameters.get(1) instanceof Set)) {
            processError(request, Error.SMMalformedRequest, null);
        } else {
            String spaceURL = ((URI) operation.parameters.get(0)).toString();
            Set<TripleEntry> policyGraph = (Set<TripleEntry>) operation.parameters.get(1);
            if (metadateRetriever.isLocalSpace(spaceURL)) {
                try {
                    policyManager.writePolicyGraph(spaceURL, policyGraph);
                    if (log.isInfoEnabled()) {
                        log.info("Updated policy for space " + spaceURL);
                    }
                    ManagementDataResultExternal entry = new ManagementDataResultExternal();
                    entry.operationID = operation.operationID;
                    entry.result = Collections.singletonList((Object) true);
                    JavaSpaces.write(outboundArea, entry);
                } catch (Exception ex) {
                    processError(request, Error.SMInternalError, Util.printStackTrace(ex));
                }
            } else {
                processError(request, Error.SMInvalidSpace, "Target space is not hosted by this kernel");
            }
        }
    }

    /**
	 * Process a M-RD management operation targeted at the Security Manager.
	 * This method invokes the Policy Manager in order to retrieve policy for
	 * the specified space. A {@link ManagementDataResultExtenal} entry is
	 * returned to the invoking API, carrying the requested policy.
	 * 
	 * @param request the request to process (not null).
	 */
    private void processGetPolicyOperation(Request request) {
        assert (request != null);
        ManagementOperationExternal operation = (ManagementOperationExternal) request.getRequestEntry();
        if ((operation.parameters == null) || (operation.parameters.get(0) == null) || !(operation.parameters.get(0) instanceof URI)) {
            processError(request, Error.SMMalformedRequest, null);
        } else {
            String spaceURL = ((URI) operation.parameters.get(0)).toString();
            if (metadateRetriever.isLocalSpace(spaceURL)) {
                try {
                    Set<TripleEntry> policyGraph = policyManager.retrievePolicyGraph(spaceURL);
                    if (log.isDebugEnabled()) {
                        log.debug("Retrieved policy for space " + spaceURL);
                    }
                    ManagementDataResultExternal entry = new ManagementDataResultExternal();
                    entry.operationID = operation.operationID;
                    entry.result = Collections.singletonList((Object) policyGraph);
                    JavaSpaces.write(outboundArea, entry);
                } catch (Exception ex) {
                    processError(request, Error.SMInternalError, Util.printStackTrace(ex));
                }
            } else {
                processError(request, Error.SMInvalidSpace, "Target space is not hosted by this kernel");
            }
        }
    }

    /**
	 * Stores the initial policy for a newly created space. The policy maps the
	 * creator of the space as its owner.
	 * 
	 * @param request the Request object for the create space operation (not
	 *            null).
	 * @return false if the operation succeeded, otherwise true.
	 */
    private boolean storeInitialPolicy(Request request) {
        assert (request != null);
        String clientId = null;
        for (Assertion assertion : request.getIdentityAssertions()) {
            if (Constants.ATTRIBUTE_ID.equals(assertion.getAttributeName())) {
                clientId = assertion.getAttributeValue();
                break;
            }
        }
        if (clientId == null) {
            return false;
        }
        X509Certificate authorityCertificate = null;
        for (X509Certificate certificate : policyManager.retrieveCertificates(request.getAuthnAuthorityDN())) {
            if (Certificates.stringHash(certificate).equals(request.getAuthnAuthorityCertificateDigest())) {
                authorityCertificate = certificate;
                break;
            }
        }
        if (authorityCertificate == null) {
            return false;
        }
        try {
            policyManager.writeInitialPolicy(request.getTargetSpaceURL(), clientId, authorityCertificate);
            return true;
        } catch (Exception ex) {
            if (log.isErrorEnabled()) {
                log.error("Exception caught while writing the initial policy for space " + request.getTargetSpaceURL(), ex);
            }
            return false;
        }
    }

    /**
	 * Creates a new ClientInfo structure from the data contained in the
	 * provided Request. This method employs the results of the authorization
	 * process in order to build a {@link ClientInfo} structure to be forwarded
	 * to internal clients. Note that currently only the security context ID is
	 * forwarded as the security info data.
	 * 
	 * @param request the request for which to build a ClientInfo structure (not
	 *            null).
	 * @return a ClientInfo structure for the specified request.
	 */
    private ClientInfo buildClientInfo(Request request) {
        assert (request != null);
        Assertion id = request.getIdentityAssertion(Constants.ATTRIBUTE_ID);
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setClientID((id != null) ? id.getAttributeValue() : null);
        clientInfo.setRoles(null);
        clientInfo.setSecurityData(request.getContextId());
        return clientInfo;
    }

    /**
	 * Build a request object from the entry specified. The method throws an
	 * exception if the conversion is not possible.
	 * 
	 * @param entry the entry to transform to a Request object (not null).
	 * @return the request object for the entry read.
	 */
    private Request buildRequest(Entry entry) {
        assert (entry != null);
        Action action = null;
        Long operationId = null;
        URI targetSpaceURI = null;
        SecurityInfo securityInfo = null;
        if (entry instanceof RdOperationExternal) {
            RdOperationExternal operation = (RdOperationExternal) entry;
            operationId = operation.operationID;
            targetSpaceURI = operation.space;
            securityInfo = operation.securityInfo;
            action = Action.RD;
        } else if (entry instanceof OutOperationExternal) {
            OutOperationExternal operation = (OutOperationExternal) entry;
            operationId = operation.operationID;
            targetSpaceURI = operation.space;
            securityInfo = operation.securityInfo;
            action = Action.OUT;
        } else if (entry instanceof TransactionOperationExternal) {
            TransactionOperationExternal operation;
            operation = (TransactionOperationExternal) entry;
            operationId = operation.operationID;
            securityInfo = operation.securityInfo;
            action = Action.OUT;
        } else if (entry instanceof ManagementOperationExternal) {
            ManagementOperationExternal operation;
            operation = (ManagementOperationExternal) entry;
            operationId = operation.operationID;
            securityInfo = operation.securityInfo;
            if (operation.operation == ManagementOperation.Create) {
                targetSpaceURI = (URI) operation.parameters.get(0);
                action = Action.CREATE;
            } else if (operation.operation == ManagementOperation.Delete) {
                targetSpaceURI = (URI) operation.parameters.get(0);
                action = Action.DESTROY;
            } else if (operation.operation == ManagementOperation.SET_POLICY) {
                targetSpaceURI = (URI) operation.parameters.get(0);
                action = Action.SET_POLICY;
            } else if (operation.operation == ManagementOperation.GET_POLICY) {
                targetSpaceURI = (URI) operation.parameters.get(0);
                action = Action.GET_POLICY;
            }
        }
        List<X509Certificate> certificates = new ArrayList<X509Certificate>();
        List<String> samlAssertions = new ArrayList<String>();
        String contextId = null;
        if (securityInfo != null) {
            if (securityInfo.getCertificate() != null) {
                certificates.add(Certificates.decode(securityInfo.getCertificate()));
            }
            if (securityInfo instanceof SecurityAssertionsInfo) {
                samlAssertions.addAll(((SecurityAssertionsInfo) securityInfo).getAssertions());
            }
            if (securityInfo instanceof SecurityCookieInfo) {
                contextId = ((SecurityCookieInfo) securityInfo).getCookie();
            }
        }
        Request request = new Request(entry, action, (operationId == null) ? 0L : operationId.longValue(), contextId != null, targetSpaceURI == null ? null : Util.normalizeURL(targetSpaceURI.toString()));
        request.setContextId(contextId);
        request.setCertificates(certificates.toArray(new X509Certificate[certificates.size()]));
        request.setSAMLAssertions(samlAssertions);
        return request;
    }
}
