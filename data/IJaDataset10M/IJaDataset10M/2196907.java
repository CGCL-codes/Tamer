package gov.nist.javax.sip.stack;

import java.util.*;
import gov.nist.javax.sip.*;
import gov.nist.javax.sip.header.*;
import gov.nist.javax.sip.address.*;
import gov.nist.javax.sip.message.*;
import gov.nist.core.*;
import javax.sip.header.*;
import javax.sip.*;
import javax.sip.address.*;
import javax.sip.message.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.text.ParseException;

/**
 * Tracks dialogs. A dialog is a peer to peer association of communicating SIP entities. For
 * INVITE transactions, a Dialog is created when a success message is received (i.e. a response
 * that has a To tag). The SIP Protocol stores enough state in the message structure to extract a
 * dialog identifier that can be used to retrieve this structure from the SipStack.
 * 
 * @version 1.2 $Revision: 1.105 $ $Date: 2009/06/23 11:02:16 $
 * 
 * @author M. Ranganathan
 * 
 * 
 */
public class SIPDialog implements javax.sip.Dialog, DialogExt {

    private static final long serialVersionUID = -1429794423085204069L;

    private boolean dialogTerminatedEventDelivered;

    private String method;

    private boolean isAssigned;

    private boolean reInviteFlag;

    private Object applicationData;

    private SIPRequest originalRequest;

    private SIPResponse lastResponse;

    private transient SIPTransaction firstTransaction;

    private SIPTransaction lastTransaction;

    private String dialogId;

    private String earlyDialogId;

    private long localSequenceNumber;

    private long remoteSequenceNumber;

    private String myTag;

    private String hisTag;

    private RouteList routeList;

    private transient SIPTransactionStack sipStack;

    private int dialogState;

    private boolean ackSeen;

    protected SIPRequest lastAck;

    protected boolean ackProcessed;

    protected DialogTimerTask timerTask;

    protected Long nextSeqno;

    private int retransmissionTicksLeft;

    private int prevRetransmissionTicks;

    private long originalLocalSequenceNumber;

    private int ackLine;

    public long auditTag = 0;

    private javax.sip.address.Address localParty;

    private javax.sip.address.Address remoteParty;

    protected CallIdHeader callIdHeader;

    public static final int NULL_STATE = -1;

    public static final int EARLY_STATE = DialogState._EARLY;

    public static final int CONFIRMED_STATE = DialogState._CONFIRMED;

    public static final int TERMINATED_STATE = DialogState._TERMINATED;

    private static final int DIALOG_LINGER_TIME = 8;

    private boolean serverTransactionFlag;

    private transient SipProviderImpl sipProvider;

    private boolean terminateOnBye;

    private boolean byeSent;

    private Address remoteTarget;

    private EventHeader eventHeader;

    private boolean lastInviteOkReceived;

    class LingerTimer extends SIPStackTimerTask {

        public LingerTimer() {
        }

        protected void runTask() {
            SIPDialog dialog = SIPDialog.this;
            sipStack.removeDialog(dialog);
        }
    }

    class DialogTimerTask extends SIPStackTimerTask implements Serializable {

        int nRetransmissions;

        SIPServerTransaction transaction;

        public DialogTimerTask(SIPServerTransaction transaction) {
            this.transaction = transaction;
            this.nRetransmissions = 0;
        }

        protected void runTask() {
            SIPDialog dialog = SIPDialog.this;
            if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Running dialog timer");
            nRetransmissions++;
            SIPServerTransaction transaction = this.transaction;
            if (nRetransmissions > 64 * SIPTransaction.T1) {
                dialog.setState(SIPDialog.TERMINATED_STATE);
                if (transaction != null) transaction.raiseErrorEvent(SIPTransactionErrorEvent.TIMEOUT_ERROR);
            } else if ((!dialog.ackSeen) && (transaction != null)) {
                SIPResponse response = transaction.getLastResponse();
                if (response.getStatusCode() == 200) {
                    try {
                        if (dialog.toRetransmitFinalResponse(transaction.T2)) transaction.sendMessage(response);
                    } catch (IOException ex) {
                        raiseIOException(transaction.getPeerAddress(), transaction.getPeerPort(), transaction.getPeerProtocol());
                    } finally {
                        SIPTransactionStack stack = dialog.sipStack;
                        if (stack.logWriter.isLoggingEnabled()) {
                            stack.logWriter.logDebug("resend 200 response from " + dialog);
                        }
                        transaction.fireTimer();
                    }
                }
            }
            if (dialog.isAckSeen() || dialog.dialogState == TERMINATED_STATE) {
                this.transaction = null;
                this.cancel();
            }
        }
    }

    /**
     * This timer task is used to garbage collect the dialog after some time.
     * 
     */
    class DialogDeleteTask extends SIPStackTimerTask {

        protected void runTask() {
            if (isAckSeen()) this.cancel(); else delete();
        }
    }

    /**
     * Protected Dialog constructor.
     */
    private SIPDialog() {
        this.terminateOnBye = true;
        this.routeList = new RouteList();
        this.dialogState = NULL_STATE;
        localSequenceNumber = 0;
        remoteSequenceNumber = -1;
    }

    /**
     * Constructor given the first transaction.
     * 
     * @param transaction is the first transaction.
     */
    public SIPDialog(SIPTransaction transaction) {
        this();
        SIPRequest sipRequest = (SIPRequest) transaction.getRequest();
        this.earlyDialogId = sipRequest.getDialogId(false);
        if (transaction == null) throw new NullPointerException("Null tx");
        this.sipStack = transaction.sipStack;
        this.sipProvider = (SipProviderImpl) transaction.getSipProvider();
        if (sipProvider == null) throw new NullPointerException("Null Provider!");
        this.addTransaction(transaction);
        if (sipStack.isLoggingEnabled()) {
            sipStack.logWriter.logDebug("Creating a dialog : " + this);
            sipStack.logWriter.logDebug("provider port = " + this.sipProvider.getListeningPoint().getPort());
            sipStack.logWriter.logStackTrace();
        }
    }

    /**
     * Constructor given a transaction and a response.
     * 
     * @param transaction -- the transaction ( client/server)
     * @param sipResponse -- response with the appropriate tags.
     */
    public SIPDialog(SIPTransaction transaction, SIPResponse sipResponse) {
        this(transaction);
        if (sipResponse == null) throw new NullPointerException("Null SipResponse");
        this.setLastResponse(transaction, sipResponse);
    }

    /**
     * create a sip dialog with a response ( no tx)
     */
    public SIPDialog(SipProviderImpl sipProvider, SIPResponse sipResponse) {
        this.sipProvider = sipProvider;
        this.sipStack = (SIPTransactionStack) sipProvider.getSipStack();
        this.setLastResponse(null, sipResponse);
        this.localSequenceNumber = sipResponse.getCSeq().getSeqNumber();
        this.originalLocalSequenceNumber = localSequenceNumber;
        this.myTag = sipResponse.getFrom().getTag();
        this.hisTag = sipResponse.getTo().getTag();
        this.localParty = sipResponse.getFrom().getAddress();
        this.remoteParty = sipResponse.getTo().getAddress();
        this.method = sipResponse.getCSeq().getMethod();
        this.callIdHeader = sipResponse.getCallId();
        this.serverTransactionFlag = false;
        if (sipStack.isLoggingEnabled()) {
            sipStack.logWriter.logDebug("Creating a dialog : " + this);
            sipStack.logWriter.logStackTrace();
        }
    }

    /**
     * A debugging print routine.
     */
    private void printRouteList() {
        if (sipStack.isLoggingEnabled()) {
            sipStack.logWriter.logDebug("this : " + this);
            sipStack.logWriter.logDebug("printRouteList : " + this.routeList.encode());
        }
    }

    /**
     * Return true if this is a client dialog.
     * 
     * @return true if the transaction that created this dialog is a client transaction and false
     *         otherwise.
     */
    private boolean isClientDialog() {
        SIPTransaction transaction = (SIPTransaction) this.getFirstTransaction();
        return transaction instanceof SIPClientTransaction;
    }

    /**
     * Raise an io exception for asyncrhonous retransmission of responses
     * 
     * @param host -- host to where the io was headed
     * @param port -- remote port
     * @param protocol -- protocol (udp/tcp/tls)
     */
    private void raiseIOException(String host, int port, String protocol) {
        IOExceptionEvent ioError = new IOExceptionEvent(this, host, port, protocol);
        sipProvider.handleEvent(ioError, null);
        setState(SIPDialog.TERMINATED_STATE);
    }

    /**
     * Set the remote party for this Dialog.
     * 
     * @param sipMessage -- SIP Message to extract the relevant information from.
     */
    private void setRemoteParty(SIPMessage sipMessage) {
        if (!isServer()) {
            this.remoteParty = sipMessage.getTo().getAddress();
        } else {
            this.remoteParty = sipMessage.getFrom().getAddress();
        }
        if (sipStack.getLogWriter().isLoggingEnabled()) {
            sipStack.getLogWriter().logDebug("settingRemoteParty " + this.remoteParty);
        }
    }

    /**
     * Add a route list extracted from a record route list. If this is a server dialog then we
     * assume that the record are added to the route list IN order. If this is a client dialog
     * then we assume that the record route headers give us the route list to add in reverse
     * order.
     * 
     * @param recordRouteList -- the record route list from the incoming message.
     */
    private void addRoute(RecordRouteList recordRouteList) {
        try {
            if (this.isClientDialog()) {
                this.routeList = new RouteList();
                ListIterator li = recordRouteList.listIterator(recordRouteList.size());
                boolean addRoute = true;
                while (li.hasPrevious()) {
                    RecordRoute rr = (RecordRoute) li.previous();
                    if (addRoute) {
                        Route route = new Route();
                        AddressImpl address = ((AddressImpl) ((AddressImpl) rr.getAddress()).clone());
                        route.setAddress(address);
                        route.setParameters((NameValueList) rr.getParameters().clone());
                        this.routeList.add(route);
                    }
                }
            } else {
                this.routeList = new RouteList();
                ListIterator li = recordRouteList.listIterator();
                boolean addRoute = true;
                while (li.hasNext()) {
                    RecordRoute rr = (RecordRoute) li.next();
                    if (addRoute) {
                        Route route = new Route();
                        AddressImpl address = ((AddressImpl) ((AddressImpl) rr.getAddress()).clone());
                        route.setAddress(address);
                        route.setParameters((NameValueList) rr.getParameters().clone());
                        routeList.add(route);
                    }
                }
            }
        } finally {
            if (sipStack.getLogWriter().isLoggingEnabled()) {
                Iterator it = routeList.iterator();
                while (it.hasNext()) {
                    SipURI sipUri = (SipURI) (((Route) it.next()).getAddress().getURI());
                    if (!sipUri.hasLrParam()) {
                        sipStack.getLogWriter().logWarning("NON LR route in Route set detected for dialog : " + this);
                        sipStack.getLogWriter().logStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Add a route list extacted from the contact list of the incoming message.
     * 
     * @param contactList -- contact list extracted from the incoming message.
     * 
     */
    private void setRemoteTarget(ContactHeader contact) {
        this.remoteTarget = contact.getAddress();
        if (sipStack.isLoggingEnabled()) {
            sipStack.getLogWriter().logDebug("Dialog.setRemoteTarget: " + this.remoteTarget);
            sipStack.getLogWriter().logStackTrace();
        }
    }

    /**
     * Extract the route information from this SIP Message and add the relevant information to the
     * route set.
     * 
     * @param sipMessage is the SIP message for which we want to add the route.
     */
    private synchronized void addRoute(SIPResponse sipResponse) {
        try {
            if (sipStack.isLoggingEnabled()) {
                sipStack.logWriter.logDebug("setContact: dialogState: " + this + "state = " + this.getState());
            }
            if (sipResponse.getStatusCode() == 100) {
                return;
            } else if (this.dialogState == TERMINATED_STATE) {
                return;
            } else if (this.dialogState == CONFIRMED_STATE) {
                if (sipResponse.getStatusCode() / 100 == 2 && !this.isServer()) {
                    ContactList contactList = sipResponse.getContactHeaders();
                    if (contactList != null && SIPRequest.isTargetRefresh(sipResponse.getCSeq().getMethod())) {
                        this.setRemoteTarget((ContactHeader) contactList.getFirst());
                    }
                }
                return;
            }
            if (!isServer()) {
                RecordRouteList rrlist = sipResponse.getRecordRouteHeaders();
                if (rrlist != null) {
                    this.addRoute(rrlist);
                } else {
                    this.routeList = new RouteList();
                }
                ContactList contactList = sipResponse.getContactHeaders();
                if (contactList != null) {
                    this.setRemoteTarget((ContactHeader) contactList.getFirst());
                }
            }
        } finally {
            if (sipStack.isLoggingEnabled()) {
                sipStack.logWriter.logStackTrace();
            }
        }
    }

    /**
     * Get a cloned copy of route list for the Dialog.
     * 
     * @return -- a cloned copy of the dialog route list.
     */
    private synchronized RouteList getRouteList() {
        if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("getRouteList " + this);
        ListIterator li;
        RouteList retval = new RouteList();
        retval = new RouteList();
        if (this.routeList != null) {
            li = routeList.listIterator();
            while (li.hasNext()) {
                Route route = (Route) li.next();
                retval.add((Route) route.clone());
            }
        }
        if (sipStack.isLoggingEnabled()) {
            sipStack.logWriter.logDebug("----- ");
            sipStack.logWriter.logDebug("getRouteList for " + this);
            if (retval != null) sipStack.logWriter.logDebug("RouteList = " + retval.encode());
            if (routeList != null) sipStack.logWriter.logDebug("myRouteList = " + routeList.encode());
            sipStack.logWriter.logDebug("----- ");
        }
        return retval;
    }

    /**
     * Sends ACK Request to the remote party of this Dialogue.
     * 
     * @param request the new ACK Request message to send.
     * @throws SipException if implementation cannot send the ACK Request for any other reason
     */
    private void sendAck(Request request, boolean throwIOExceptionAsSipException) throws SipException {
        SIPRequest ackRequest = (SIPRequest) request;
        if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("sendAck" + this);
        if (!ackRequest.getMethod().equals(Request.ACK)) throw new SipException("Bad request method -- should be ACK");
        if (this.getState() == null || this.getState().getValue() == EARLY_STATE) {
            if (sipStack.logWriter.isLoggingEnabled()) {
                sipStack.logWriter.logError("Bad Dialog State for " + this + " dialogID = " + this.getDialogId());
            }
            throw new SipException("Bad dialog state " + this.getState());
        }
        if (!this.getCallId().getCallId().equals(((SIPRequest) request).getCallId().getCallId())) {
            if (sipStack.isLoggingEnabled()) {
                sipStack.logWriter.logError("CallID " + this.getCallId());
                sipStack.logWriter.logError("RequestCallID = " + ackRequest.getCallId().getCallId());
                sipStack.logWriter.logError("dialog =  " + this);
            }
            throw new SipException("Bad call ID in request");
        }
        try {
            if (sipStack.isLoggingEnabled()) {
                sipStack.logWriter.logDebug("setting from tag For outgoing ACK= " + this.getLocalTag());
                sipStack.logWriter.logDebug("setting To tag for outgoing ACK = " + this.getRemoteTag());
                sipStack.logWriter.logDebug("ack = " + ackRequest);
            }
            if (this.getLocalTag() != null) ackRequest.getFrom().setTag(this.getLocalTag());
            if (this.getRemoteTag() != null) ackRequest.getTo().setTag(this.getRemoteTag());
        } catch (ParseException ex) {
            throw new SipException(ex.getMessage());
        }
        Hop hop = sipStack.getNextHop(ackRequest);
        if (hop == null) throw new SipException("No route!");
        try {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("hop = " + hop);
            ListeningPointImpl lp = (ListeningPointImpl) this.sipProvider.getListeningPoint(hop.getTransport());
            if (lp == null) throw new SipException("No listening point for this provider registered at " + hop);
            InetAddress inetAddress = InetAddress.getByName(hop.getHost());
            MessageChannel messageChannel = lp.getMessageProcessor().createMessageChannel(inetAddress, hop.getPort());
            this.lastAck = ackRequest;
            messageChannel.sendMessage(ackRequest);
        } catch (IOException ex) {
            if (throwIOExceptionAsSipException) throw new SipException("Could not send ack", ex);
            this.raiseIOException(hop.getHost(), hop.getPort(), hop.getTransport());
        } catch (SipException ex) {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logException(ex);
            throw ex;
        } catch (Exception ex) {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logException(ex);
            throw new SipException("Could not create message channel", ex);
        }
        this.ackSeen = true;
    }

    /**
     * Set the stack address. Prevent us from routing messages to ourselves.
     * 
     * @param sipStack the address of the SIP stack.
     * 
     */
    void setStack(SIPTransactionStack sipStack) {
        this.sipStack = sipStack;
    }

    /**
     * Return True if this dialog is terminated on BYE.
     * 
     */
    boolean isTerminatedOnBye() {
        return this.terminateOnBye;
    }

    /**
     * Mark that the dialog has seen an ACK.
     */
    void ackReceived(SIPRequest sipRequest) {
        if (this.ackSeen) return;
        SIPServerTransaction tr = this.getInviteTransaction();
        if (tr != null) {
            if (tr.getCSeq() == sipRequest.getCSeq().getSeqNumber()) {
                if (this.timerTask != null) {
                    this.timerTask.cancel();
                    this.timerTask = null;
                }
                this.ackSeen = true;
                this.lastAck = sipRequest;
                if (sipStack.isLoggingEnabled()) {
                    sipStack.logWriter.logDebug("ackReceived for " + ((SIPTransaction) tr).getMethod());
                    this.ackLine = sipStack.logWriter.getLineCount();
                    this.printDebugInfo();
                }
                this.setState(CONFIRMED_STATE);
            }
        }
    }

    /**
     * Return true if a terminated event was delivered to the application as a result of the
     * dialog termination.
     * 
     */
    synchronized boolean testAndSetIsDialogTerminatedEventDelivered() {
        boolean retval = this.dialogTerminatedEventDelivered;
        this.dialogTerminatedEventDelivered = true;
        return retval;
    }

    public void setApplicationData(Object applicationData) {
        this.applicationData = applicationData;
    }

    public Object getApplicationData() {
        return this.applicationData;
    }

    /**
     * Updates the next consumable seqno.
     * 
     */
    public synchronized void requestConsumed() {
        this.nextSeqno = new Long(this.getRemoteSeqNumber() + 1);
        if (sipStack.isLoggingEnabled()) {
            this.sipStack.logWriter.logDebug("Request Consumed -- next consumable Request Seqno = " + this.nextSeqno);
        }
    }

    /**
     * Return true if this request can be consumed by the dialog.
     * 
     * @param dialogRequest is the request to check with the dialog.
     * @return true if the dialogRequest sequence number matches the next consumable seqno.
     */
    public synchronized boolean isRequestConsumable(SIPRequest dialogRequest) {
        if (dialogRequest.getMethod().equals(Request.ACK)) throw new RuntimeException("Illegal method");
        if (sipStack.isLooseDialogValidation()) {
            return true;
        }
        return remoteSequenceNumber < dialogRequest.getCSeq().getSeqNumber();
    }

    /**
     * This method is called when a forked dialog is created from the client side. It starts a
     * timer task. If the timer task expires before an ACK is sent then the dialog is cancelled
     * (i.e. garbage collected ).
     * 
     */
    public void doDeferredDelete() {
        if (sipStack.getTimer() == null) this.setState(TERMINATED_STATE); else {
            sipStack.getTimer().schedule(new DialogDeleteTask(), SIPTransaction.TIMER_H * SIPTransactionStack.BASE_TIMER_INTERVAL);
        }
    }

    /**
     * Set the state for this dialog.
     * 
     * @param state is the state to set for the dialog.
     */
    public void setState(int state) {
        if (sipStack.isLoggingEnabled()) {
            sipStack.logWriter.logDebug("Setting dialog state for " + this + "newState = " + state);
            sipStack.logWriter.logStackTrace();
            if (state != NULL_STATE && state != this.dialogState) if (sipStack.isLoggingEnabled()) {
                sipStack.logWriter.logDebug(this + "  old dialog state is " + this.getState());
                sipStack.logWriter.logDebug(this + "  New dialog state is " + DialogState.getObject(state));
            }
        }
        this.dialogState = state;
        if (state == TERMINATED_STATE) {
            if (sipStack.getTimer() != null) {
                sipStack.getTimer().schedule(new LingerTimer(), DIALOG_LINGER_TIME * 1000);
            }
            this.stopTimer();
        }
    }

    /**
     * Debugging print for the dialog.
     */
    public void printDebugInfo() {
        if (sipStack.isLoggingEnabled()) {
            sipStack.logWriter.logDebug("isServer = " + isServer());
            sipStack.logWriter.logDebug("localTag = " + getLocalTag());
            sipStack.logWriter.logDebug("remoteTag = " + getRemoteTag());
            sipStack.logWriter.logDebug("localSequenceNumer = " + getLocalSeqNumber());
            sipStack.logWriter.logDebug("remoteSequenceNumer = " + getRemoteSeqNumber());
            sipStack.logWriter.logDebug("ackLine:" + this.getRemoteTag() + " " + ackLine);
        }
    }

    /**
     * Return true if the dialog has already seen the ack.
     * 
     * @return flag that records if the ack has been seen.
     */
    public boolean isAckSeen() {
        return this.ackSeen;
    }

    /**
     * Get the last ACK for this transaction.
     */
    public SIPRequest getLastAck() {
        return this.lastAck;
    }

    /**
     * Get the transaction that created this dialog.
     */
    public Transaction getFirstTransaction() {
        return this.firstTransaction;
    }

    /**
     * Gets the route set for the dialog. When acting as an User Agent Server the route set MUST
     * be set to the list of URIs in the Record-Route header field from the request, taken in
     * order and preserving all URI parameters. When acting as an User Agent Client the route set
     * MUST be set to the list of URIs in the Record-Route header field from the response, taken
     * in reverse order and preserving all URI parameters. If no Record-Route header field is
     * present in the request or response, the route set MUST be set to the empty set. This route
     * set, even if empty, overrides any pre-existing route set for future requests in this
     * dialog.
     * <p>
     * Requests within a dialog MAY contain Record-Route and Contact header fields. However, these
     * requests do not cause the dialog's route set to be modified.
     * <p>
     * The User Agent Client uses the remote target and route set to build the Request-URI and
     * Route header field of the request.
     * 
     * @return an Iterator containing a list of route headers to be used for forwarding. Empty
     *         iterator is returned if route has not been established.
     */
    public Iterator getRouteSet() {
        if (this.routeList == null) {
            return new LinkedList().listIterator();
        } else {
            return this.getRouteList().listIterator();
        }
    }

    /**
     * Add a Route list extracted from a SIPRequest to this Dialog.
     * 
     * @param sipRequest
     */
    public synchronized void addRoute(SIPRequest sipRequest) {
        if (sipStack.isLoggingEnabled()) {
            sipStack.logWriter.logDebug("setContact: dialogState: " + this + "state = " + this.getState());
        }
        if (this.dialogState == CONFIRMED_STATE && SIPRequest.isTargetRefresh(sipRequest.getMethod())) {
            this.doTargetRefresh(sipRequest);
        }
        if (this.dialogState == CONFIRMED_STATE || this.dialogState == TERMINATED_STATE) {
            return;
        }
        RecordRouteList rrlist = sipRequest.getRecordRouteHeaders();
        if (rrlist != null) {
            this.addRoute(rrlist);
        } else {
            this.routeList = new RouteList();
        }
        ContactList contactList = sipRequest.getContactHeaders();
        if (contactList != null) {
            this.setRemoteTarget((ContactHeader) contactList.getFirst());
        }
    }

    /**
     * Set the dialog identifier.
     */
    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    /**
     * Creates a new dialog based on a received NOTIFY. The dialog state is initialized
     * appropriately. The NOTIFY differs in the From tag
     * 
     * Made this a separate method to clearly distinguish what's happening here - this is a
     * non-trivial case
     * 
     * @param subscribeTx - the transaction started with the SUBSCRIBE that we sent
     * @param notifyST - the ServerTransaction created for an incoming NOTIFY
     * @return -- a new dialog created from the subscribe original SUBSCRIBE transaction.
     * 
     * 
     */
    public static SIPDialog createFromNOTIFY(SIPClientTransaction subscribeTx, SIPTransaction notifyST) {
        SIPDialog d = new SIPDialog(notifyST);
        d.serverTransactionFlag = false;
        d.firstTransaction = d.lastTransaction = subscribeTx;
        d.terminateOnBye = false;
        d.localSequenceNumber = d.firstTransaction.getCSeq();
        SIPRequest not = (SIPRequest) notifyST.getRequest();
        d.remoteSequenceNumber = not.getCSeq().getSeqNumber();
        d.setDialogId(not.getDialogId(true));
        d.setLocalTag(not.getToTag());
        d.setRemoteTag(not.getFromTag());
        d.setLastResponse(subscribeTx, subscribeTx.getLastResponse());
        d.localParty = not.getTo().getAddress();
        d.remoteParty = not.getFrom().getAddress();
        d.addRoute(not);
        d.setState(CONFIRMED_STATE);
        return d;
    }

    /**
     * Return true if is server.
     * 
     * @return true if is server transaction created this dialog.
     */
    public boolean isServer() {
        if (this.firstTransaction == null) return this.serverTransactionFlag; else return this.firstTransaction instanceof SIPServerTransaction;
    }

    /**
     * Return true if this is a re-establishment of the dialog.
     * 
     * @return true if the reInvite flag is set.
     */
    protected boolean isReInvite() {
        return this.reInviteFlag;
    }

    /**
     * Get the id for this dialog.
     * 
     * @return the string identifier for this dialog.
     * 
     */
    public String getDialogId() {
        if (this.dialogId == null && this.lastResponse != null) this.dialogId = this.lastResponse.getDialogId(isServer());
        return this.dialogId;
    }

    /**
     * Add a transaction record to the dialog.
     * 
     * @param transaction is the transaction to add to the dialog.
     */
    public void addTransaction(SIPTransaction transaction) {
        SIPRequest sipRequest = (SIPRequest) transaction.getOriginalRequest();
        if (firstTransaction != null && firstTransaction != transaction && transaction.getMethod().equals(firstTransaction.getMethod())) {
            this.reInviteFlag = true;
        }
        if (firstTransaction == null) {
            firstTransaction = transaction;
            if (sipRequest.getMethod().equals(Request.SUBSCRIBE)) this.eventHeader = (EventHeader) sipRequest.getHeader(EventHeader.NAME);
            this.setLocalParty(sipRequest);
            this.setRemoteParty(sipRequest);
            this.setCallId(sipRequest);
            if (this.originalRequest == null) {
                this.originalRequest = sipRequest;
            }
            if (this.method == null) {
                this.method = sipRequest.getMethod();
            }
            if (transaction instanceof SIPServerTransaction) {
                this.hisTag = sipRequest.getFrom().getTag();
            } else {
                setLocalSequenceNumber(sipRequest.getCSeq().getSeqNumber());
                this.originalLocalSequenceNumber = localSequenceNumber;
                this.myTag = sipRequest.getFrom().getTag();
                if (myTag == null) sipStack.getLogWriter().logError("The request's From header is missing the required Tag parameter.");
            }
        } else if (transaction.getMethod().equals(firstTransaction.getMethod()) && !(firstTransaction.getClass().equals(transaction.getClass()))) {
            firstTransaction = transaction;
            this.setLocalParty(sipRequest);
            this.setRemoteParty(sipRequest);
            this.setCallId(sipRequest);
            this.originalRequest = sipRequest;
            this.method = sipRequest.getMethod();
        }
        if (transaction instanceof SIPServerTransaction) setRemoteSequenceNumber(sipRequest.getCSeq().getSeqNumber());
        this.lastTransaction = transaction;
        if (sipStack.isLoggingEnabled()) {
            sipStack.logWriter.logDebug("Transaction Added " + this + myTag + "/" + hisTag);
            sipStack.logWriter.logDebug("TID = " + transaction.getTransactionId() + "/" + transaction.IsServerTransaction());
            sipStack.logWriter.logStackTrace();
        }
    }

    /**
     * Set the remote tag.
     * 
     * @param hisTag is the remote tag to set.
     */
    private void setRemoteTag(String hisTag) {
        if (sipStack.getLogWriter().isLoggingEnabled()) {
            sipStack.getLogWriter().logDebug("setRemoteTag(): " + this + " remoteTag = " + this.hisTag + " new tag = " + hisTag);
        }
        if (this.hisTag != null && hisTag != null && !hisTag.equals(this.hisTag)) {
            if (this.getState() != DialogState.EARLY) {
                sipStack.getLogWriter().logDebug("Dialog is already established -- ignoring remote tag re-assignment");
                return;
            } else if (sipStack.isRemoteTagReassignmentAllowed()) {
                sipStack.getLogWriter().logDebug("UNSAFE OPERATION !  tag re-assignment " + this.hisTag + " trying to set to " + hisTag + " can cause unexpected effects ");
                boolean removed = false;
                if (this.sipStack.getDialog(dialogId) == this) {
                    this.sipStack.removeDialog(dialogId);
                    removed = true;
                }
                this.dialogId = null;
                this.hisTag = hisTag;
                if (removed) {
                    sipStack.getLogWriter().logDebug("ReInserting Dialog");
                    this.sipStack.putDialog(this);
                }
            }
        } else {
            if (hisTag != null) {
                this.hisTag = hisTag;
            } else {
                sipStack.logWriter.logWarning("setRemoteTag : called with null argument ");
            }
        }
    }

    /**
     * Get the last transaction from the dialog.
     */
    public SIPTransaction getLastTransaction() {
        return this.lastTransaction;
    }

    /**
     * Get the INVITE transaction (null if no invite transaction).
     */
    public SIPServerTransaction getInviteTransaction() {
        DialogTimerTask t = this.timerTask;
        if (t != null) return t.transaction; else return null;
    }

    /**
     * Set the local sequece number for the dialog (defaults to 1 when the dialog is created).
     * 
     * @param lCseq is the local cseq number.
     * 
     */
    private void setLocalSequenceNumber(long lCseq) {
        if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("setLocalSequenceNumber: original 	" + this.localSequenceNumber + " new  = " + lCseq);
        if (lCseq <= this.localSequenceNumber) throw new RuntimeException("Sequence number should not decrease !");
        this.localSequenceNumber = lCseq;
    }

    /**
     * Set the remote sequence number for the dialog.
     * 
     * @param rCseq is the remote cseq number.
     * 
     */
    public void setRemoteSequenceNumber(long rCseq) {
        if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("setRemoteSeqno " + this + "/" + rCseq);
        this.remoteSequenceNumber = rCseq;
    }

    /**
     * Increment the local CSeq # for the dialog. This is useful for if you want to create a hole
     * in the sequence number i.e. route a request outside the dialog and then resume within the
     * dialog.
     */
    public void incrementLocalSequenceNumber() {
        ++this.localSequenceNumber;
    }

    /**
     * Get the remote sequence number (for cseq assignment of outgoing requests within this
     * dialog).
     * 
     * @deprecated
     * @return local sequence number.
     */
    public int getRemoteSequenceNumber() {
        return (int) this.remoteSequenceNumber;
    }

    /**
     * Get the local sequence number (for cseq assignment of outgoing requests within this
     * dialog).
     * 
     * @deprecated
     * @return local sequence number.
     */
    public int getLocalSequenceNumber() {
        return (int) this.localSequenceNumber;
    }

    /**
     * Get the sequence number for the request that origianlly created the Dialog.
     * 
     * @return -- the original starting sequence number for this dialog.
     */
    public long getOriginalLocalSequenceNumber() {
        return this.originalLocalSequenceNumber;
    }

    public long getLocalSeqNumber() {
        return this.localSequenceNumber;
    }

    public long getRemoteSeqNumber() {
        return this.remoteSequenceNumber;
    }

    public String getLocalTag() {
        return this.myTag;
    }

    public String getRemoteTag() {
        return hisTag;
    }

    /**
     * Set local tag for the transaction.
     * 
     * @param mytag is the tag to use in From headers client transactions that belong to this
     *        dialog and for generating To tags for Server transaction requests that belong to
     *        this dialog.
     */
    private void setLocalTag(String mytag) {
        if (sipStack.isLoggingEnabled()) {
            sipStack.logWriter.logDebug("set Local tag " + mytag + " " + this.dialogId);
            sipStack.logWriter.logStackTrace();
        }
        this.myTag = mytag;
    }

    public void delete() {
        this.setState(TERMINATED_STATE);
    }

    public CallIdHeader getCallId() {
        return this.callIdHeader;
    }

    /**
     * set the call id header for this dialog.
     */
    private void setCallId(SIPRequest sipRequest) {
        this.callIdHeader = sipRequest.getCallId();
    }

    public javax.sip.address.Address getLocalParty() {
        return this.localParty;
    }

    private void setLocalParty(SIPMessage sipMessage) {
        if (!isServer()) {
            this.localParty = sipMessage.getFrom().getAddress();
        } else {
            this.localParty = sipMessage.getTo().getAddress();
        }
    }

    /**
     * Returns the Address identifying the remote party. This is the value of the To header of
     * locally initiated requests in this dialogue when acting as an User Agent Client.
     * <p>
     * This is the value of the From header of recieved responses in this dialogue when acting as
     * an User Agent Server.
     * 
     * @return the address object of the remote party.
     */
    public javax.sip.address.Address getRemoteParty() {
        if (sipStack.getLogWriter().isLoggingEnabled()) {
            sipStack.getLogWriter().logDebug("gettingRemoteParty " + this.remoteParty);
        }
        return this.remoteParty;
    }

    public javax.sip.address.Address getRemoteTarget() {
        return this.remoteTarget;
    }

    public DialogState getState() {
        if (this.dialogState == NULL_STATE) return null;
        return DialogState.getObject(this.dialogState);
    }

    /**
     * Returns true if this Dialog is secure i.e. if the request arrived over TLS, and the
     * Request-URI contained a SIPS URI, the "secure" flag is set to TRUE.
     * 
     * @return <code>true</code> if this dialogue was established using a sips URI over TLS, and
     *         <code>false</code> otherwise.
     */
    public boolean isSecure() {
        return this.getFirstTransaction().getRequest().getRequestURI().getScheme().equalsIgnoreCase("sips");
    }

    public void sendAck(Request request) throws SipException {
        this.sendAck(request, true);
    }

    public Request createRequest(String method) throws SipException {
        if (method.equals(Request.ACK) || method.equals(Request.PRACK)) {
            throw new SipException("Invalid method specified for createRequest:" + method);
        }
        if (lastResponse != null) return this.createRequest(method, this.lastResponse); else throw new SipException("Dialog not yet established -- no response!");
    }

    /**
     * The method that actually does the work of creating a request.
     * 
     * @param method
     * @param response
     * @return
     * @throws SipException
     */
    private Request createRequest(String method, SIPResponse sipResponse) throws SipException {
        if (method == null || sipResponse == null) throw new NullPointerException("null argument");
        if (method.equals(Request.CANCEL)) throw new SipException("Dialog.createRequest(): Invalid request");
        if (this.getState() == null || (this.getState().getValue() == TERMINATED_STATE && !method.equalsIgnoreCase(Request.BYE)) || (this.isServer() && this.getState().getValue() == EARLY_STATE && method.equalsIgnoreCase(Request.BYE))) throw new SipException("Dialog  " + getDialogId() + " not yet established or terminated " + this.getState());
        SipUri sipUri = null;
        if (this.getRemoteTarget() != null) sipUri = (SipUri) this.getRemoteTarget().getURI().clone(); else {
            sipUri = (SipUri) this.getRemoteParty().getURI().clone();
            sipUri.clearUriParms();
        }
        CSeq cseq = new CSeq();
        try {
            cseq.setMethod(method);
            cseq.setSeqNumber(this.getLocalSeqNumber());
        } catch (Exception ex) {
            sipStack.getLogWriter().logError("Unexpected error");
            InternalErrorHandler.handleException(ex);
        }
        ListeningPointImpl lp = (ListeningPointImpl) this.sipProvider.getListeningPoint(sipResponse.getTopmostVia().getTransport());
        if (lp == null) {
            if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logError("Cannot find listening point for transport " + sipResponse.getTopmostVia().getTransport());
            throw new SipException("Cannot find listening point for transport " + sipResponse.getTopmostVia().getTransport());
        }
        Via via = lp.getViaHeader();
        From from = new From();
        from.setAddress(this.localParty);
        To to = new To();
        to.setAddress(this.remoteParty);
        SIPRequest sipRequest = sipResponse.createRequest(sipUri, via, cseq, from, to);
        if (SIPRequest.isTargetRefresh(method)) {
            ContactHeader contactHeader = ((ListeningPointImpl) this.sipProvider.getListeningPoint(lp.getTransport())).createContactHeader();
            ((SipURI) contactHeader.getAddress().getURI()).setSecure(this.isSecure());
            sipRequest.setHeader(contactHeader);
        }
        try {
            cseq = (CSeq) sipRequest.getCSeq();
            cseq.setSeqNumber(this.localSequenceNumber + 1);
        } catch (InvalidArgumentException ex) {
            InternalErrorHandler.handleException(ex);
        }
        if (method.equals(Request.SUBSCRIBE)) {
            if (eventHeader != null) sipRequest.addHeader(eventHeader);
        }
        try {
            if (this.getLocalTag() != null) {
                from.setTag(this.getLocalTag());
            } else {
                from.removeTag();
            }
            if (this.getRemoteTag() != null) {
                to.setTag(this.getRemoteTag());
            } else {
                to.removeTag();
            }
        } catch (ParseException ex) {
            InternalErrorHandler.handleException(ex);
        }
        this.updateRequest(sipRequest);
        return sipRequest;
    }

    public void sendRequest(ClientTransaction clientTransactionId) throws TransactionDoesNotExistException, SipException {
        SIPRequest dialogRequest = ((SIPClientTransaction) clientTransactionId).getOriginalRequest();
        if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("dialog.sendRequest " + " dialog = " + this + "\ndialogRequest = \n" + dialogRequest);
        if (clientTransactionId == null) throw new NullPointerException("null parameter");
        if (dialogRequest.getMethod().equals(Request.ACK) || dialogRequest.getMethod().equals(Request.CANCEL)) throw new SipException("Bad Request Method. " + dialogRequest.getMethod());
        if (byeSent && isTerminatedOnBye() && !dialogRequest.getMethod().equals(Request.BYE)) {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logError("BYE already sent for " + this);
            throw new SipException("Cannot send request; BYE already sent");
        }
        if (dialogRequest.getTopmostVia() == null) {
            Via via = ((SIPClientTransaction) clientTransactionId).getOutgoingViaHeader();
            dialogRequest.addHeader(via);
        }
        if (!this.getCallId().getCallId().equalsIgnoreCase(dialogRequest.getCallId().getCallId())) {
            if (sipStack.isLoggingEnabled()) {
                sipStack.logWriter.logError("CallID " + this.getCallId());
                sipStack.logWriter.logError("RequestCallID = " + dialogRequest.getCallId().getCallId());
                sipStack.logWriter.logError("dialog =  " + this);
            }
            throw new SipException("Bad call ID in request");
        }
        ((SIPClientTransaction) clientTransactionId).setDialog(this, this.dialogId);
        this.addTransaction((SIPTransaction) clientTransactionId);
        ((SIPClientTransaction) clientTransactionId).isMapped = true;
        From from = (From) dialogRequest.getFrom();
        To to = (To) dialogRequest.getTo();
        if (this.getLocalTag() != null && from.getTag() != null && !from.getTag().equals(this.getLocalTag())) throw new SipException("From tag mismatch expecting	 " + this.getLocalTag());
        if (this.getRemoteTag() != null && to.getTag() != null && !to.getTag().equals(this.getRemoteTag())) {
            this.sipStack.getLogWriter().logWarning("To header tag mismatch expecting " + this.getRemoteTag());
        }
        if (this.getLocalTag() == null && dialogRequest.getMethod().equals(Request.NOTIFY)) {
            if (!this.getMethod().equals(Request.SUBSCRIBE)) throw new SipException("Trying to send NOTIFY without SUBSCRIBE Dialog!");
            this.setLocalTag(from.getTag());
        }
        try {
            if (this.getLocalTag() != null) from.setTag(this.getLocalTag());
            if (this.getRemoteTag() != null) to.setTag(this.getRemoteTag());
        } catch (ParseException ex) {
            InternalErrorHandler.handleException(ex);
        }
        Hop hop = ((SIPClientTransaction) clientTransactionId).getNextHop();
        if (sipStack.isLoggingEnabled()) {
            sipStack.logWriter.logDebug("Using hop = " + hop.getHost() + " : " + hop.getPort());
        }
        try {
            TCPMessageChannel oldChannel = null;
            TLSMessageChannel oldTLSChannel = null;
            MessageChannel messageChannel = sipStack.createRawMessageChannel(this.getSipProvider().getListeningPoint(hop.getTransport()).getIPAddress(), this.firstTransaction.getPort(), hop);
            if (((SIPClientTransaction) clientTransactionId).getMessageChannel() instanceof TCPMessageChannel) {
                oldChannel = (TCPMessageChannel) ((SIPClientTransaction) clientTransactionId).getMessageChannel();
                if (oldChannel.isCached && !oldChannel.isRunning) {
                    oldChannel.uncache();
                }
                if (!sipStack.cacheClientConnections) {
                    oldChannel.useCount--;
                    if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("oldChannel: useCount " + oldChannel.useCount);
                }
            } else if (((SIPClientTransaction) clientTransactionId).getMessageChannel() instanceof TLSMessageChannel) {
                oldTLSChannel = (TLSMessageChannel) ((SIPClientTransaction) clientTransactionId).getMessageChannel();
                if (oldTLSChannel.isCached && !oldTLSChannel.isRunning) {
                    oldTLSChannel.uncache();
                }
                if (!sipStack.cacheClientConnections) {
                    oldTLSChannel.useCount--;
                    if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("oldChannel: useCount " + oldTLSChannel.useCount);
                }
            }
            if (messageChannel == null) {
                if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("Null message channel using outbound proxy !");
                Hop outboundProxy = sipStack.getRouter(dialogRequest).getOutboundProxy();
                if (outboundProxy == null) throw new SipException("No route found! hop=" + hop);
                messageChannel = sipStack.createRawMessageChannel(this.getSipProvider().getListeningPoint(outboundProxy.getTransport()).getIPAddress(), this.firstTransaction.getPort(), outboundProxy);
                if (messageChannel != null) ((SIPClientTransaction) clientTransactionId).setEncapsulatedChannel(messageChannel);
            } else {
                ((SIPClientTransaction) clientTransactionId).setEncapsulatedChannel(messageChannel);
                if (sipStack.isLoggingEnabled()) {
                    sipStack.logWriter.logDebug("using message channel " + messageChannel);
                }
            }
            if (messageChannel != null && messageChannel instanceof TCPMessageChannel) ((TCPMessageChannel) messageChannel).useCount++;
            if (messageChannel != null && messageChannel instanceof TLSMessageChannel) ((TLSMessageChannel) messageChannel).useCount++;
            if ((!sipStack.cacheClientConnections) && oldChannel != null && oldChannel.useCount <= 0) oldChannel.close();
            if ((!sipStack.cacheClientConnections) && oldTLSChannel != null && oldTLSChannel.useCount == 0) oldTLSChannel.close();
        } catch (Exception ex) {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logException(ex);
            throw new SipException("Could not create message channel", ex);
        }
        try {
            localSequenceNumber++;
            dialogRequest.getCSeq().setSeqNumber(getLocalSeqNumber());
        } catch (InvalidArgumentException ex) {
            sipStack.getLogWriter().logFatalError(ex.getMessage());
        }
        try {
            ((SIPClientTransaction) clientTransactionId).sendMessage(dialogRequest);
            if (dialogRequest.getMethod().equals(Request.BYE)) {
                this.byeSent = true;
                if (isTerminatedOnBye()) {
                    this.setState(DialogState._TERMINATED);
                }
            }
        } catch (IOException ex) {
            throw new SipException("error sending message", ex);
        }
    }

    /**
     * Return yes if the last response is to be retransmitted.
     */
    private boolean toRetransmitFinalResponse(int T2) {
        if (--retransmissionTicksLeft == 0) {
            if (2 * prevRetransmissionTicks <= T2) this.retransmissionTicksLeft = 2 * prevRetransmissionTicks; else this.retransmissionTicksLeft = prevRetransmissionTicks;
            this.prevRetransmissionTicks = retransmissionTicksLeft;
            return true;
        } else return false;
    }

    protected void setRetransmissionTicks() {
        this.retransmissionTicksLeft = 1;
        this.prevRetransmissionTicks = 1;
    }

    /**
     * Resend the last ack.
     */
    public void resendAck() throws SipException {
        if (this.lastAck != null) {
            if (lastAck.getHeader(TimeStampHeader.NAME) != null && sipStack.generateTimeStampHeader) {
                TimeStamp ts = new TimeStamp();
                try {
                    ts.setTimeStamp(System.currentTimeMillis());
                    lastAck.setHeader(ts);
                } catch (InvalidArgumentException e) {
                }
            }
            this.sendAck(lastAck, false);
        }
    }

    /**
     * Get the method of the request/response that resulted in the creation of the Dialog.
     * 
     * @return -- the method of the dialog.
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * Start the dialog timer.
     * 
     * @param transaction
     */
    protected void startTimer(SIPServerTransaction transaction) {
        if (this.timerTask != null && timerTask.transaction == transaction) {
            sipStack.getLogWriter().logDebug("Timer already running for " + getDialogId());
            return;
        }
        if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Starting dialog timer for " + getDialogId());
        this.ackSeen = false;
        if (this.timerTask != null) {
            this.timerTask.transaction = transaction;
        } else {
            this.timerTask = new DialogTimerTask(transaction);
            sipStack.getTimer().schedule(timerTask, SIPTransactionStack.BASE_TIMER_INTERVAL, SIPTransactionStack.BASE_TIMER_INTERVAL);
        }
        this.setRetransmissionTicks();
    }

    /**
     * Stop the dialog timer. This is called when the dialog is terminated.
     * 
     */
    protected void stopTimer() {
        try {
            if (this.timerTask != null) this.timerTask.cancel();
            this.timerTask = null;
        } catch (Exception ex) {
        }
    }

    public Request createPrack(Response relResponse) throws DialogDoesNotExistException, SipException {
        if (this.getState() == null || this.getState().equals(DialogState.TERMINATED)) throw new DialogDoesNotExistException("Dialog not initialized or terminated");
        if ((RSeq) relResponse.getHeader(RSeqHeader.NAME) == null) {
            throw new SipException("Missing RSeq Header");
        }
        try {
            SIPResponse sipResponse = (SIPResponse) relResponse;
            SIPRequest sipRequest = (SIPRequest) this.createRequest(Request.PRACK, (SIPResponse) relResponse);
            String toHeaderTag = sipResponse.getTo().getTag();
            sipRequest.setToTag(toHeaderTag);
            RAck rack = new RAck();
            RSeq rseq = (RSeq) relResponse.getHeader(RSeqHeader.NAME);
            rack.setMethod(sipResponse.getCSeq().getMethod());
            rack.setCSequenceNumber((int) sipResponse.getCSeq().getSeqNumber());
            rack.setRSequenceNumber(rseq.getSeqNumber());
            sipRequest.setHeader(rack);
            return (Request) sipRequest;
        } catch (Exception ex) {
            InternalErrorHandler.handleException(ex);
            return null;
        }
    }

    private void updateRequest(SIPRequest sipRequest) {
        RouteList rl = this.getRouteList();
        if (rl.size() > 0) {
            sipRequest.setHeader(rl);
        } else {
            sipRequest.removeHeader(RouteHeader.NAME);
        }
        if (MessageFactoryImpl.getDefaultUserAgentHeader() != null) {
            sipRequest.setHeader(MessageFactoryImpl.getDefaultUserAgentHeader());
        }
    }

    public Request createAck(long cseqno) throws InvalidArgumentException, SipException {
        if (!method.equals(Request.INVITE)) throw new SipException("Dialog was not created with an INVITE" + method);
        if (cseqno <= 0) throw new InvalidArgumentException("bad cseq <= 0 "); else if (cseqno > ((((long) 1) << 32) - 1)) throw new InvalidArgumentException("bad cseq > " + ((((long) 1) << 32) - 1));
        if (this.remoteTarget == null) {
            throw new SipException("Cannot create ACK - no remote Target!");
        }
        if (this.sipStack.getLogWriter().isLoggingEnabled()) {
            this.sipStack.getLogWriter().logDebug("createAck " + this);
        }
        if (!lastInviteOkReceived) {
            throw new SipException("Dialog not yet established -- no OK response!");
        } else {
            lastInviteOkReceived = false;
        }
        try {
            SipURI uri4transport = null;
            if (this.routeList != null && !this.routeList.isEmpty()) {
                Route r = (Route) this.routeList.getFirst();
                uri4transport = ((SipURI) r.getAddress().getURI());
            } else {
                uri4transport = ((SipURI) this.remoteTarget.getURI());
            }
            String transport = uri4transport.getTransportParam();
            if (transport == null) {
                transport = uri4transport.isSecure() ? ListeningPoint.TLS : ListeningPoint.UDP;
            }
            ListeningPointImpl lp = (ListeningPointImpl) sipProvider.getListeningPoint(transport);
            if (lp == null) {
                sipStack.getLogWriter().logError("remoteTargetURI " + this.remoteTarget.getURI());
                sipStack.getLogWriter().logError("uri4transport = " + uri4transport);
                sipStack.getLogWriter().logError("No LP found for transport=" + transport);
                throw new SipException("Cannot create ACK - no ListeningPoint for transport towards next hop found:" + transport);
            }
            SIPRequest sipRequest = new SIPRequest();
            sipRequest.setMethod(Request.ACK);
            sipRequest.setRequestURI((SipUri) getRemoteTarget().getURI().clone());
            sipRequest.setCallId(this.callIdHeader);
            sipRequest.setCSeq(new CSeq(cseqno, Request.ACK));
            List<Via> vias = new ArrayList<Via>();
            SIPResponse response = this.getLastResponse();
            Via via = (Via) response.getTopmostVia().clone();
            via.setBranch(Utils.getInstance().generateBranchId());
            vias.add(via);
            sipRequest.setVia(vias);
            From from = new From();
            from.setAddress(this.localParty);
            from.setTag(this.myTag);
            sipRequest.setFrom(from);
            To to = new To();
            to.setAddress(this.remoteParty);
            if (hisTag != null) to.setTag(this.hisTag);
            sipRequest.setTo(to);
            sipRequest.setMaxForwards(new MaxForwards(70));
            if (this.originalRequest != null) {
                Authorization authorization = this.originalRequest.getAuthorization();
                if (authorization != null) sipRequest.setHeader(authorization);
            }
            this.updateRequest(sipRequest);
            return sipRequest;
        } catch (Exception ex) {
            InternalErrorHandler.handleException(ex);
            throw new SipException("unexpected exception ", ex);
        }
    }

    /**
     * Get the provider for this Dialog.
     * 
     * SPEC_REVISION
     * 
     * @return -- the SIP Provider associated with this transaction.
     */
    public SipProviderImpl getSipProvider() {
        return this.sipProvider;
    }

    /**
     * @param sipProvider the sipProvider to set
     */
    public void setSipProvider(SipProviderImpl sipProvider) {
        this.sipProvider = sipProvider;
    }

    /**
     * Check the tags of the response against the tags of the Dialog. Return true if the respnse
     * matches the tags of the dialog. We do this check wehn sending out a response.
     * 
     * @param sipResponse -- the response to check.
     * 
     */
    public void setResponseTags(SIPResponse sipResponse) {
        if (this.getLocalTag() != null || this.getRemoteTag() != null) {
            return;
        }
        String responseFromTag = sipResponse.getFromTag();
        if (responseFromTag.equals(this.getLocalTag())) {
            sipResponse.setToTag(this.getRemoteTag());
        } else if (responseFromTag.equals(this.getRemoteTag())) {
            sipResponse.setToTag(this.getLocalTag());
        }
    }

    /**
     * Set the last response for this dialog. This method is called for updating the dialog state
     * when a response is either sent or received from within a Dialog.
     * 
     * @param transaction -- the transaction associated with the response
     * @param sipResponse -- the last response to set.
     */
    public void setLastResponse(SIPTransaction transaction, SIPResponse sipResponse) {
        int statusCode = sipResponse.getStatusCode();
        if (statusCode == 100) {
            sipStack.getLogWriter().logWarning("Invalid status code - 100 in setLastResponse - ignoring");
            return;
        }
        this.lastResponse = sipResponse;
        this.setAssigned();
        if (sipStack.isLoggingEnabled()) {
            sipStack.getLogWriter().logDebug("sipDialog: setLastResponse:" + this + " lastResponse = " + this.lastResponse.getFirstLine());
        }
        if (this.getState() == DialogState.TERMINATED) {
            if (sipStack.isLoggingEnabled()) {
                sipStack.getLogWriter().logDebug("sipDialog: setLastResponse -- dialog is terminated - ignoring ");
            }
            return;
        }
        String cseqMethod = sipResponse.getCSeq().getMethod();
        if (sipStack.isLoggingEnabled()) {
            sipStack.getLogWriter().logStackTrace();
            sipStack.getLogWriter().logDebug("cseqMethod = " + cseqMethod);
            sipStack.getLogWriter().logDebug("dialogState = " + this.getState());
            sipStack.getLogWriter().logDebug("method = " + this.getMethod());
            sipStack.getLogWriter().logDebug("statusCode = " + statusCode);
            sipStack.getLogWriter().logDebug("transaction = " + transaction);
        }
        if (transaction == null || transaction instanceof ClientTransaction) {
            if (sipStack.isDialogCreated(cseqMethod)) {
                if (getState() == null && (statusCode / 100 == 1)) {
                    setState(SIPDialog.EARLY_STATE);
                    if ((sipResponse.getToTag() != null || sipStack.rfc2543Supported) && this.getRemoteTag() == null) {
                        setRemoteTag(sipResponse.getToTag());
                        this.setDialogId(sipResponse.getDialogId(false));
                        sipStack.putDialog(this);
                        this.addRoute(sipResponse);
                    }
                } else if (getState() != null && getState().equals(DialogState.EARLY) && statusCode / 100 == 1) {
                    if (cseqMethod.equals(getMethod()) && transaction != null && (sipResponse.getToTag() != null || sipStack.rfc2543Supported)) {
                        setRemoteTag(sipResponse.getToTag());
                        this.setDialogId(sipResponse.getDialogId(false));
                        sipStack.putDialog(this);
                        this.addRoute(sipResponse);
                    }
                } else if (statusCode / 100 == 2) {
                    if (cseqMethod.equals(getMethod()) && (sipResponse.getToTag() != null || sipStack.rfc2543Supported) && this.getState() != DialogState.CONFIRMED) {
                        setRemoteTag(sipResponse.getToTag());
                        this.setDialogId(sipResponse.getDialogId(false));
                        sipStack.putDialog(this);
                        this.addRoute(sipResponse);
                        setState(SIPDialog.CONFIRMED_STATE);
                    }
                    if (cseqMethod.equals(Request.INVITE)) {
                        this.lastInviteOkReceived = true;
                    }
                } else if (statusCode >= 300 && statusCode <= 699 && (getState() == null || (cseqMethod.equals(getMethod()) && getState().getValue() == SIPDialog.EARLY_STATE))) {
                    setState(SIPDialog.TERMINATED_STATE);
                }
                if (originalRequest != null) {
                    RecordRouteList rrList = originalRequest.getRecordRouteHeaders();
                    if (rrList != null) {
                        ListIterator<RecordRoute> it = rrList.listIterator(rrList.size());
                        while (it.hasPrevious()) {
                            RecordRoute rr = (RecordRoute) it.previous();
                            Route route = (Route) routeList.getFirst();
                            if (route != null && rr.getAddress().equals(route.getAddress())) {
                                routeList.removeFirst();
                            } else break;
                        }
                    }
                }
            } else if (cseqMethod.equals(Request.NOTIFY) && (this.getMethod().equals(Request.SUBSCRIBE) || this.getMethod().equals(Request.REFER)) && sipResponse.getStatusCode() / 100 == 2 && this.getState() == null) {
                this.setDialogId(sipResponse.getDialogId(true));
                sipStack.putDialog(this);
                this.setState(SIPDialog.CONFIRMED_STATE);
            } else if (cseqMethod.equals(Request.BYE) && statusCode / 100 == 2 && isTerminatedOnBye()) {
                setState(SIPDialog.TERMINATED_STATE);
            }
        } else {
            if (cseqMethod.equals(Request.BYE) && statusCode / 100 == 2 && this.isTerminatedOnBye()) {
                this.setState(SIPDialog.TERMINATED_STATE);
            } else {
                boolean doPutDialog = false;
                if (getLocalTag() == null && sipResponse.getTo().getTag() != null && sipStack.isDialogCreated(cseqMethod) && cseqMethod.equals(getMethod())) {
                    setLocalTag(sipResponse.getTo().getTag());
                    doPutDialog = true;
                }
                if (statusCode / 100 != 2) {
                    if (statusCode / 100 == 1) {
                        if (doPutDialog) {
                            setState(SIPDialog.EARLY_STATE);
                            this.setDialogId(sipResponse.getDialogId(true));
                            sipStack.putDialog(this);
                        }
                    } else {
                        if (statusCode == 489 && (cseqMethod.equals(Request.NOTIFY) || cseqMethod.equals(Request.SUBSCRIBE))) {
                            sipStack.logWriter.logDebug("RFC 3265 : Not setting dialog to TERMINATED for 489");
                        } else {
                            if (!this.isReInvite() && getState() != DialogState.CONFIRMED) {
                                this.setState(SIPDialog.TERMINATED_STATE);
                            }
                        }
                    }
                } else {
                    if (this.dialogState <= SIPDialog.EARLY_STATE && (cseqMethod.equals(Request.INVITE) || cseqMethod.equals(Request.SUBSCRIBE) || cseqMethod.equals(Request.REFER))) {
                        this.setState(SIPDialog.CONFIRMED_STATE);
                    }
                    if (doPutDialog) {
                        this.setDialogId(sipResponse.getDialogId(true));
                        sipStack.putDialog(this);
                    }
                }
            }
            if ((statusCode / 100 == 2) && cseqMethod.equals(Request.INVITE)) {
                SIPServerTransaction sipServerTx = (SIPServerTransaction) transaction;
                this.startTimer(sipServerTx);
            }
        }
    }

    /**
     * @return -- the last response associated with the dialog.
     */
    public SIPResponse getLastResponse() {
        return lastResponse;
    }

    /**
     * Do taget refresh dialog state updates.
     * 
     * RFC 3261: Requests within a dialog MAY contain Record-Route and Contact header fields.
     * However, these requests do not cause the dialog's route set to be modified, although they
     * may modify the remote target URI. Specifically, requests that are not target refresh
     * requests do not modify the dialog's remote target URI, and requests that are target refresh
     * requests do. For dialogs that have been established with an
     * 
     * INVITE, the only target refresh request defined is re-INVITE (see Section 14). Other
     * extensions may define different target refresh requests for dialogs established in other
     * ways.
     */
    private void doTargetRefresh(SIPMessage sipMessage) {
        ContactList contactList = sipMessage.getContactHeaders();
        if (contactList != null) {
            Contact contact = (Contact) contactList.getFirst();
            this.setRemoteTarget(contact);
        }
    }

    private static final boolean optionPresent(ListIterator l, String option) {
        while (l.hasNext()) {
            OptionTag opt = (OptionTag) l.next();
            if (opt != null && option.equalsIgnoreCase(opt.getOptionTag())) return true;
        }
        return false;
    }

    public Response createReliableProvisionalResponse(int statusCode) throws InvalidArgumentException, SipException {
        SIPServerTransaction sipServerTransaction = (SIPServerTransaction) this.getFirstTransaction();
        if (!(sipServerTransaction instanceof SIPServerTransaction)) {
            throw new SipException("Not a Server Dialog!");
        }
        if (statusCode <= 100 || statusCode > 199) throw new InvalidArgumentException("Bad status code ");
        SIPRequest request = this.originalRequest;
        if (!request.getMethod().equals(Request.INVITE)) throw new SipException("Bad method");
        ListIterator<SIPHeader> list = request.getHeaders(SupportedHeader.NAME);
        if (list == null || !optionPresent(list, "100rel")) {
            list = request.getHeaders(RequireHeader.NAME);
            if (list == null || !optionPresent(list, "100rel")) {
                throw new SipException("No Supported/Require 100rel header in the request");
            }
        }
        SIPResponse response = request.createResponse(statusCode);
        Require require = new Require();
        try {
            require.setOptionTag("100rel");
        } catch (Exception ex) {
            InternalErrorHandler.handleException(ex);
        }
        response.addHeader(require);
        RSeq rseq = new RSeq();
        rseq.setSeqNumber(1L);
        RecordRouteList rrl = request.getRecordRouteHeaders();
        if (rrl != null) {
            RecordRouteList rrlclone = (RecordRouteList) rrl.clone();
            response.setHeader(rrlclone);
        }
        return response;
    }

    /**
     * Do the processing necessary for the PRACK
     * 
     * @param prackRequest
     * @return true if this is the first time the tx has seen the prack ( and hence needs to be
     *         passed up to the TU)
     */
    public boolean handlePrack(SIPRequest prackRequest) {
        if (!this.isServer()) {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("Dropping Prack -- not a server Dialog");
            return false;
        }
        SIPServerTransaction sipServerTransaction = (SIPServerTransaction) this.getFirstTransaction();
        SIPResponse sipResponse = sipServerTransaction.getReliableProvisionalResponse();
        if (sipResponse == null) {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("Dropping Prack -- ReliableResponse not found");
            return false;
        }
        RAck rack = (RAck) prackRequest.getHeader(RAckHeader.NAME);
        if (rack == null) {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("Dropping Prack -- rack header not found");
            return false;
        }
        CSeq cseq = (CSeq) sipResponse.getCSeq();
        if (!rack.getMethod().equals(cseq.getMethod())) {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("Dropping Prack -- CSeq Header does not match PRACK");
            return false;
        }
        if (rack.getCSeqNumberLong() != cseq.getSeqNumber()) {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("Dropping Prack -- CSeq Header does not match PRACK");
            return false;
        }
        RSeq rseq = (RSeq) sipResponse.getHeader(RSeqHeader.NAME);
        if (rack.getRSequenceNumber() != rseq.getSeqNumber()) {
            if (sipStack.isLoggingEnabled()) sipStack.logWriter.logDebug("Dropping Prack -- RSeq Header does not match PRACK");
            return false;
        }
        return sipServerTransaction.prackRecieved();
    }

    public void sendReliableProvisionalResponse(Response relResponse) throws SipException {
        if (!this.isServer()) {
            throw new SipException("Not a Server Dialog");
        }
        SIPResponse sipResponse = (SIPResponse) relResponse;
        if (relResponse.getStatusCode() == 100) throw new SipException("Cannot send 100 as a reliable provisional response");
        if (relResponse.getStatusCode() / 100 > 2) throw new SipException("Response code is not a 1xx response - should be in the range 101 to 199 ");
        if (sipResponse.getToTag() == null) {
            throw new SipException("Badly formatted response -- To tag mandatory for Reliable Provisional Response");
        }
        ListIterator requireList = (ListIterator) relResponse.getHeaders(RequireHeader.NAME);
        boolean found = false;
        if (requireList != null) {
            while (requireList.hasNext() && !found) {
                RequireHeader rh = (RequireHeader) requireList.next();
                if (rh.getOptionTag().equalsIgnoreCase("100rel")) {
                    found = true;
                }
            }
        }
        if (!found) {
            Require require = new Require("100rel");
            relResponse.addHeader(require);
            if (sipStack.getLogWriter().isLoggingEnabled()) {
                sipStack.getLogWriter().logDebug("Require header with optionTag 100rel is needed -- adding one");
            }
        }
        SIPServerTransaction serverTransaction = (SIPServerTransaction) this.getFirstTransaction();
        this.setLastResponse(serverTransaction, sipResponse);
        this.setDialogId(sipResponse.getDialogId(true));
        serverTransaction.sendReliableProvisionalResponse(relResponse);
    }

    public void terminateOnBye(boolean terminateFlag) throws SipException {
        this.terminateOnBye = terminateFlag;
    }

    /**
     * Set the "assigned" flag to true. We do this when inserting the dialog into the dialog table
     * of the stack.
     * 
     */
    public void setAssigned() {
        this.isAssigned = true;
    }

    /**
     * Return true if the dialog has already been mapped to a transaction.
     * 
     */
    public boolean isAssigned() {
        return this.isAssigned;
    }

    /**
     * Get the contact header that the owner of this dialog assigned. Subsequent Requests are
     * considered to belong to the dialog if the dialog identifier matches and the contact header
     * matches the ip address and port on which the request is received.
     * 
     * @return contact header belonging to the dialog.
     */
    public Contact getMyContactHeader() {
        if (this.isServer()) {
            SIPServerTransaction st = (SIPServerTransaction) this.getFirstTransaction();
            SIPResponse response = st.getLastResponse();
            return response != null ? response.getContactHeader() : null;
        } else {
            SIPClientTransaction ct = (SIPClientTransaction) this.getFirstTransaction();
            if (ct == null) return null;
            SIPRequest sipRequest = ct.getOriginalRequest();
            return sipRequest.getContactHeader();
        }
    }

    /**
     * Override for the equals method.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Dialog)) {
            return false;
        } else {
            String id1 = this.getDialogId();
            String id2 = ((Dialog) obj).getDialogId();
            return id1 != null && id2 != null && id1.equals(id2);
        }
    }

    /**
     * Do the necessary processing to handle an ACK directed at this Dialog.
     * 
     * @param ackTransaction -- the ACK transaction that was directed at this dialog.
     * @return -- true if the ACK was successfully consumed by the Dialog and resulted in the
     *         dialog state being changed.
     */
    public boolean handleAck(SIPServerTransaction ackTransaction) {
        SIPRequest sipRequest = ackTransaction.getOriginalRequest();
        if (isAckSeen() && getRemoteSeqNumber() == sipRequest.getCSeq().getSeqNumber()) {
            if (sipStack.isLoggingEnabled()) {
                sipStack.getLogWriter().logDebug("ACK already seen by dialog -- dropping Ack" + " retransmission");
            }
            if (this.timerTask != null) {
                this.timerTask.cancel();
                this.timerTask = null;
            }
            return false;
        } else if (this.getState() == DialogState.TERMINATED) {
            if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Dialog is terminated -- dropping ACK");
            return false;
        } else {
            SIPServerTransaction tr = getInviteTransaction();
            SIPResponse sipResponse = (tr != null ? tr.getLastResponse() : null);
            if (tr != null && sipResponse != null && sipResponse.getStatusCode() / 100 == 2 && sipResponse.getCSeq().getMethod().equals(Request.INVITE) && sipResponse.getCSeq().getSeqNumber() == sipRequest.getCSeq().getSeqNumber()) {
                ackTransaction.setDialog(this, sipResponse.getDialogId(false));
                ackReceived(sipRequest);
                sipStack.getLogWriter().logDebug("ACK for 2XX response --- sending to TU ");
                return true;
            } else {
                if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug(" INVITE transaction not found  -- Discarding ACK");
                return false;
            }
        }
    }

    void setEarlyDialogId(String earlyDialogId) {
        this.earlyDialogId = earlyDialogId;
    }

    String getEarlyDialogId() {
        return earlyDialogId;
    }
}
