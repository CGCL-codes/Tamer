package org.mobicents.ha.javax.sip;

import gov.nist.javax.sip.ListeningPointImpl;
import gov.nist.javax.sip.RequestEventExt;
import gov.nist.javax.sip.ResponseEventExt;
import gov.nist.javax.sip.header.Route;
import gov.nist.javax.sip.header.RouteList;
import gov.nist.javax.sip.header.Via;
import gov.nist.javax.sip.header.ViaList;
import gov.nist.javax.sip.message.MessageExt;
import gov.nist.javax.sip.message.SIPRequest;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.EventHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.SubscriptionStateHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import org.jboss.cache.CacheException;
import org.jboss.cache.Fqn;
import org.mobicents.ha.javax.sip.cache.MobicentsSipCache;

public class SimpleB2BUAHandler {

    private SipProvider sipProvider;

    private MessageFactory messageFactory;

    private HeaderFactory headerFactory;

    private ServerTransaction serverTransaction;

    private boolean createInviteOnAck = false;

    private SipStack sipStack;

    int myPort;

    String transport;

    private boolean sendAckOn2xx = true;

    public SimpleB2BUAHandler(SipProvider sipProvider, HeaderFactory headerFactory, MessageFactory messageFactory, int port, String transport) {
        this.sipProvider = sipProvider;
        this.sipStack = sipProvider.getSipStack();
        this.messageFactory = messageFactory;
        this.headerFactory = headerFactory;
        myPort = port;
        this.transport = transport;
    }

    /**
	 * @return the incomingDialog
	 */
    public String getIncomingDialogId() {
        String incomingDialogId = null;
        try {
            incomingDialogId = (String) ((MobicentsSipCache) ((ClusteredSipStack) sipProvider.getSipStack()).getSipCache()).getMobicentsCache().getJBossCache().get(Fqn.fromString("DIALOG_IDS"), "incomingDialogId");
        } catch (CacheException e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
        return incomingDialogId;
    }

    /**
	 * @return the outgoingDialog
	 */
    public String getOutgoingDialogId() {
        String outgoingDialogId = null;
        try {
            outgoingDialogId = (String) ((MobicentsSipCache) ((ClusteredSipStack) sipProvider.getSipStack()).getSipCache()).getMobicentsCache().getJBossCache().get(Fqn.fromString("DIALOG_IDS"), "outgoingDialogId");
        } catch (CacheException e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
        return outgoingDialogId;
    }

    /**
	 * @return the incomingDialog
	 */
    public Dialog getIncomingDialog() {
        String incomingDialogId = null;
        try {
            incomingDialogId = (String) ((MobicentsSipCache) ((ClusteredSipStack) sipProvider.getSipStack()).getSipCache()).getMobicentsCache().getJBossCache().get(Fqn.fromString("DIALOG_IDS"), "incomingDialogId");
        } catch (CacheException e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
        ((SipStackImpl) sipStack).getStackLogger().logInfo("Incoming Dialog Id " + incomingDialogId);
        if (incomingDialogId == null) {
            return null;
        }
        return ((ClusteredSipStack) sipProvider.getSipStack()).getDialog(incomingDialogId);
    }

    /**
	 * @return the outgoingDialog
	 */
    public Dialog getOutgoingDialog() {
        String outgoingDialogId = null;
        try {
            outgoingDialogId = (String) ((MobicentsSipCache) ((ClusteredSipStack) sipProvider.getSipStack()).getSipCache()).getMobicentsCache().getJBossCache().get(Fqn.fromString("DIALOG_IDS"), "outgoingDialogId");
        } catch (CacheException e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
        ((SipStackImpl) sipStack).getStackLogger().logInfo("Outgoing Dialog Id " + outgoingDialogId);
        if (outgoingDialogId == null) {
            return null;
        }
        return ((ClusteredSipStack) sipProvider.getSipStack()).getDialog(outgoingDialogId);
    }

    private void storeOutgoingDialogId(String outgoingDialogId) {
        ((ClusteredSipStack) sipProvider.getSipStack()).getStackLogger().logDebug("Storing outgoing dialog Id " + outgoingDialogId);
        try {
            ((MobicentsSipCache) ((ClusteredSipStack) sipProvider.getSipStack()).getSipCache()).getMobicentsCache().getJBossCache().put(Fqn.fromString("DIALOG_IDS"), "outgoingDialogId", outgoingDialogId);
        } catch (CacheException e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
    }

    private void storeIncomingDialogId(String incomingDialogId) {
        ((ClusteredSipStack) sipProvider.getSipStack()).getStackLogger().logDebug("Storing incoming dialog Id " + incomingDialogId);
        try {
            ((MobicentsSipCache) ((ClusteredSipStack) sipProvider.getSipStack()).getSipCache()).getMobicentsCache().getJBossCache().put(Fqn.fromString("DIALOG_IDS"), "incomingDialogId", incomingDialogId);
        } catch (CacheException e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
    }

    private void storeServerTransactionId(String branchId) {
        ((ClusteredSipStack) sipProvider.getSipStack()).getStackLogger().logDebug("Storing transaction Id " + branchId);
        try {
            ((MobicentsSipCache) ((ClusteredSipStack) sipProvider.getSipStack()).getSipCache()).getMobicentsCache().getJBossCache().put(Fqn.fromString("STX_IDS"), "serverTransactionId", branchId);
        } catch (CacheException e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
    }

    /**
	 * @return the outgoingDialog
	 */
    public ServerTransaction getServerTransaction() {
        if (serverTransaction != null) {
            return serverTransaction;
        }
        String serverTransactionId = null;
        try {
            serverTransactionId = (String) ((MobicentsSipCache) ((ClusteredSipStack) sipProvider.getSipStack()).getSipCache()).getMobicentsCache().getJBossCache().get(Fqn.fromString("STX_IDS"), "serverTransactionId");
        } catch (CacheException e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
        ((ClusteredSipStack) sipStack).getStackLogger().logInfo("server transaction Id " + serverTransactionId);
        if (serverTransactionId == null) {
            return null;
        }
        return (ServerTransaction) ((ClusteredSipStack) sipProvider.getSipStack()).findTransaction(serverTransactionId, true);
    }

    public void processInvite(RequestEvent requestEvent) {
        try {
            serverTransaction = requestEvent.getServerTransaction();
            if (serverTransaction == null) {
                try {
                    serverTransaction = sipProvider.getNewServerTransaction(requestEvent.getRequest());
                } catch (Exception e) {
                    ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
                    return;
                }
            }
            storeServerTransactionId(serverTransaction.getBranchId());
            if (serverTransaction.getDialog() == null) {
                setupIncomingDialog();
                forwardInvite(5070);
            } else {
                Request request = getIncomingDialog().createRequest(Request.INVITE);
                final ClientTransaction ct = sipProvider.getNewClientTransaction(request);
                getIncomingDialog().sendRequest(ct);
            }
            if (((FromHeader) serverTransaction.getRequest().getHeader(FromHeader.NAME)).getAddress().getURI().toString().contains("ReInviteSubsNotify")) {
                createInviteOnAck = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * @param serverTransaction2
	 * @return
	 * @throws SipException 
	 */
    private void setupIncomingDialog() throws SipException {
        Dialog incomingDialog = sipProvider.getNewDialog(serverTransaction);
        incomingDialog.setApplicationData(this);
    }

    /**
	 * @param incomingDialog2
	 * @return
	 * @throws SipException 
	 */
    private void forwardInvite(int peerPort) throws SipException {
        Request request = createRequest(getServerTransaction().getRequest(), peerPort);
        ClientTransaction ct = sipProvider.getNewClientTransaction(request);
        Dialog outgoingDialog = sipProvider.getNewDialog(ct);
        outgoingDialog.setApplicationData(this);
        ct.sendRequest();
    }

    @SuppressWarnings("unchecked")
    private Request createRequest(Request origRequest, int peerPort) throws SipException {
        final SIPRequest request = (SIPRequest) origRequest.clone();
        try {
            long l = new AtomicLong().incrementAndGet();
            request.getFromHeader().setTag(Long.toString(l));
        } catch (ParseException e1) {
            throw new SipException("failed to set local tag", e1);
        }
        final String transport = request.getTopmostViaHeader().getTransport();
        final ListeningPointImpl listeningPointImpl = (ListeningPointImpl) sipProvider.getListeningPoint(transport);
        final ViaList viaList = new ViaList();
        viaList.add((Via) listeningPointImpl.createViaHeader());
        request.setVia(viaList);
        try {
            request.setHeader(headerFactory.createMaxForwardsHeader(70));
        } catch (InvalidArgumentException e) {
            throw new SipException("Failed to create max forwards header", e);
        }
        request.setHeader((Header) sipProvider.getNewCallId());
        if (origRequest.getHeader(ContactHeader.NAME) != null) {
            request.setHeader(listeningPointImpl.createContactHeader());
        }
        if (getOutgoingDialog() == null || getOutgoingDialog().getState() == null) {
            final RouteList routeList = request.getRouteHeaders();
            if (routeList != null) {
                final RouteHeader topRoute = routeList.get(0);
                final URI topRouteURI = topRoute.getAddress().getURI();
                if (topRouteURI.isSipURI()) {
                    final SipURI topRouteSipURI = (SipURI) topRouteURI;
                    if (topRouteSipURI.getHost().equals(listeningPointImpl.getIPAddress()) && topRouteSipURI.getPort() == listeningPointImpl.getPort()) {
                        if (routeList.size() > 1) {
                            routeList.remove(0);
                        } else {
                            request.removeHeader(RouteHeader.NAME);
                        }
                    }
                }
            }
        } else {
            request.removeHeader(RouteHeader.NAME);
            final RouteList routeList = new RouteList();
            for (Iterator<Route> it = getOutgoingDialog().getRouteSet(); it.hasNext(); ) {
                Route route = it.next();
                routeList.add(route);
            }
            if (!routeList.isEmpty()) {
                request.addHeader(routeList);
            }
        }
        request.removeHeader(RecordRouteHeader.NAME);
        ((SipURI) request.getRequestURI()).setPort(peerPort);
        return request;
    }

    public void processAck(RequestEvent requestEvent) {
        int remotePort = ((RequestEventExt) requestEvent).getRemotePort();
        if (ListeningPoint.TCP.equalsIgnoreCase(transport)) {
            remotePort = ((MessageExt) requestEvent.getRequest()).getTopmostViaHeader().getPort();
        }
        ((ClusteredSipStack) sipStack).getStackLogger().logDebug("remotePort = " + remotePort);
        try {
            if (!sendAckOn2xx) {
                Dialog dialog = null;
                if (remotePort == 5070) {
                    dialog = getIncomingDialog();
                }
                if (remotePort == 5050 || remotePort == 5060 || remotePort == 5065) {
                    storeIncomingDialogId(requestEvent.getDialog().getDialogId());
                    dialog = getOutgoingDialog();
                }
                final Request ack = dialog.createAck(((MessageExt) requestEvent.getRequest()).getCSeqHeader().getSeqNumber());
                dialog.sendAck(ack);
            } else {
                if (myPort == 5080 && getIncomingDialogId() == null) {
                    storeIncomingDialogId(requestEvent.getDialog().getDialogId());
                }
                if (remotePort == 5050 || remotePort == 5060 || remotePort == 5065) {
                    storeIncomingDialogId(requestEvent.getDialog().getDialogId());
                }
            }
            if (createInviteOnAck) {
                createInviteOnAck = false;
                Request request = getIncomingDialog().createRequest("INVITE");
                final ClientTransaction ct = sipProvider.getNewClientTransaction(request);
                getIncomingDialog().sendRequest(ct);
            }
        } catch (Exception e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
    }

    public void processBye(RequestEvent requestEvent) {
        try {
            int remotePort = ((RequestEventExt) requestEvent).getRemotePort();
            if (ListeningPoint.TCP.equalsIgnoreCase(transport)) {
                remotePort = ((MessageExt) requestEvent.getRequest()).getTopmostViaHeader().getPort();
            }
            ((ClusteredSipStack) sipStack).getStackLogger().logDebug("remotePort = " + remotePort);
            requestEvent.getServerTransaction().sendResponse(messageFactory.createResponse(200, requestEvent.getRequest()));
            Dialog dialog = getOutgoingDialog();
            if (remotePort == 5060 || remotePort == 5065) {
                dialog = getIncomingDialog();
            }
            Request request = dialog.createRequest(Request.BYE);
            final ClientTransaction ct = sipProvider.getNewClientTransaction(request);
            dialog.sendRequest(ct);
        } catch (Exception e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
    }

    public void processSubscribe(RequestEvent requestEvent) {
        try {
            Response response = messageFactory.createResponse(200, requestEvent.getRequest());
            response.addHeader(headerFactory.createHeader(ExpiresHeader.NAME, "3600"));
            requestEvent.getServerTransaction().sendResponse(response);
            Dialog dialog = getOutgoingDialog();
            Request request = dialog.createRequest(Request.SUBSCRIBE);
            ((SipURI) request.getRequestURI()).setPort(5070);
            final ClientTransaction ct = sipProvider.getNewClientTransaction(request);
            dialog.sendRequest(ct);
        } catch (Exception e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
    }

    public void processNotify(RequestEvent requestEvent) {
        try {
            requestEvent.getServerTransaction().sendResponse(messageFactory.createResponse(200, requestEvent.getRequest()));
            Dialog dialog = getIncomingDialog();
            Request request = dialog.createRequest(Request.NOTIFY);
            request.addHeader(headerFactory.createHeader(SubscriptionStateHeader.NAME, SubscriptionStateHeader.ACTIVE));
            request.addHeader(headerFactory.createHeader(EventHeader.NAME, "presence"));
            ((SipURI) request.getRequestURI()).setUser(null);
            ((SipURI) request.getRequestURI()).setPort(5050);
            final ClientTransaction ct = sipProvider.getNewClientTransaction(request);
            dialog.sendRequest(ct);
        } catch (Exception e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
    }

    public void process180(ResponseEvent responseEvent) {
        try {
            forwardResponse(responseEvent.getResponse());
        } catch (Exception e) {
            ((SipStackImpl) sipStack).getStackLogger().logError("unexpected exception", e);
        }
    }

    /**
	 * @param responseEvent
	 * @throws InvalidArgumentException 
	 */
    @SuppressWarnings("unchecked")
    private void forwardResponse(Response receivedResponse) throws SipException, InvalidArgumentException {
        if (receivedResponse.getStatusCode() == 202) {
            return;
        }
        System.out.println("Forwarding " + receivedResponse);
        final ServerTransaction origServerTransaction = this.getServerTransaction();
        Response forgedResponse = null;
        try {
            forgedResponse = messageFactory.createResponse(receivedResponse.getStatusCode(), origServerTransaction.getRequest());
        } catch (ParseException e) {
            throw new SipException("Failed to forge message", e);
        }
        ListIterator<String> lit = receivedResponse.getHeaderNames();
        String headerName = null;
        ListIterator<Header> headersIterator = null;
        while (lit.hasNext()) {
            headerName = lit.next();
            if (SimpleB2BUAHandler.getHeadersToOmmitOnResponseCopy().contains(headerName)) {
                continue;
            } else {
                forgedResponse.removeHeader(headerName);
                headersIterator = receivedResponse.getHeaders(headerName);
                while (headersIterator.hasNext()) {
                    forgedResponse.addLast((Header) headersIterator.next().clone());
                }
            }
        }
        final byte[] rawOriginal = receivedResponse.getRawContent();
        if (rawOriginal != null && rawOriginal.length != 0) {
            final byte[] copy = new byte[rawOriginal.length];
            System.arraycopy(rawOriginal, 0, copy, 0, copy.length);
            try {
                forgedResponse.setContent(copy, (ContentTypeHeader) forgedResponse.getHeader(ContentTypeHeader.NAME));
            } catch (ParseException e) {
                throw new SipException("Failed to copy content.", e);
            }
        }
        if (receivedResponse.getHeader(ContactHeader.NAME) != null) {
            final String transport = ((ViaHeader) forgedResponse.getHeader(ViaHeader.NAME)).getTransport();
            forgedResponse.setHeader(((ListeningPointImpl) sipProvider.getListeningPoint(transport)).createContactHeader());
        }
        if (((MessageExt) forgedResponse).getToHeader().getTag() == null) {
            try {
                ((MessageExt) forgedResponse).getToHeader().setTag("696");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        origServerTransaction.sendResponse(forgedResponse);
    }

    public void process200(ResponseEvent responseEvent) {
        boolean isRetransmission = ((ResponseEventExt) responseEvent).isRetransmission();
        ClientTransaction clientTransaction = ((ResponseEventExt) responseEvent).getClientTransaction();
        ((ClusteredSipStack) sipStack).getStackLogger().logDebug("clientTransaction = " + clientTransaction + ", isRetransmission " + isRetransmission);
        try {
            final CSeqHeader cSeqHeader = (CSeqHeader) responseEvent.getResponse().getHeader(CSeqHeader.NAME);
            if (cSeqHeader.getMethod().equals(Request.INVITE)) {
                processInvite200(responseEvent, cSeqHeader);
            } else if (cSeqHeader.getMethod().equals(Request.BYE) || cSeqHeader.getMethod().equals(Request.SUBSCRIBE) || cSeqHeader.getMethod().equals(Request.NOTIFY)) {
                processBye200(responseEvent);
            } else {
                System.err.println("Unexpected response: " + responseEvent.getResponse());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * @param responseEvent
	 * @throws SipException 
	 * @throws InvalidArgumentException 
	 */
    private void processInvite200(ResponseEvent responseEvent, CSeqHeader cseq) throws InvalidArgumentException, SipException {
        String outgoingDialogId = responseEvent.getDialog().getDialogId();
        int remotePort = ((ResponseEventExt) responseEvent).getRemotePort();
        if (ListeningPoint.TCP.equalsIgnoreCase(transport)) {
            remotePort = ((MessageExt) responseEvent.getResponse()).getTopmostViaHeader().getPort();
        }
        ((ClusteredSipStack) sipStack).getStackLogger().logDebug("remotePort = " + remotePort);
        if (remotePort == 5065 || remotePort == 5081) {
            storeOutgoingDialogId(outgoingDialogId);
        }
        if (myPort == 5080 && getOutgoingDialogId() == null) {
            storeOutgoingDialogId(outgoingDialogId);
        }
        if (sendAckOn2xx) {
            System.out.println("Generating ACK to 200");
            final Request ack = responseEvent.getDialog().createAck(cseq.getSeqNumber());
            responseEvent.getDialog().sendAck(ack);
        }
        forwardResponse(responseEvent.getResponse());
    }

    /**
	 * @param responseEvent
	 */
    private void processBye200(ResponseEvent responseEvent) {
    }

    private static Set<String> HEADERS_TO_OMMIT_ON_RESPONSE_COPY;

    private static Set<String> getHeadersToOmmitOnResponseCopy() {
        if (HEADERS_TO_OMMIT_ON_RESPONSE_COPY == null) {
            final Set<String> set = new HashSet<String>();
            set.add(RouteHeader.NAME);
            set.add(RecordRouteHeader.NAME);
            set.add(ViaHeader.NAME);
            set.add(CallIdHeader.NAME);
            set.add(CSeqHeader.NAME);
            set.add(ContactHeader.NAME);
            set.add(FromHeader.NAME);
            set.add(ToHeader.NAME);
            set.add(ContentLengthHeader.NAME);
            HEADERS_TO_OMMIT_ON_RESPONSE_COPY = Collections.unmodifiableSet(set);
        }
        return HEADERS_TO_OMMIT_ON_RESPONSE_COPY;
    }

    public void checkState() {
    }

    /**
	 * @param sendAckOn2xx the sendAckOn2xx to set
	 */
    public void setSendAckOn2xx(boolean sendAckOn2xx) {
        this.sendAckOn2xx = sendAckOn2xx;
    }

    /**
	 * @return the sendAckOn2xx
	 */
    public boolean isSendAckOn2xx() {
        return sendAckOn2xx;
    }
}
