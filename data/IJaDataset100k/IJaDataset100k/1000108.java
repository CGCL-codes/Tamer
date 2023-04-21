package gov.nist.javax.sip;

import gov.nist.core.InternalErrorHandler;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.Contact;
import gov.nist.javax.sip.header.Event;
import gov.nist.javax.sip.header.ReferTo;
import gov.nist.javax.sip.header.RetryAfter;
import gov.nist.javax.sip.header.Route;
import gov.nist.javax.sip.header.RouteList;
import gov.nist.javax.sip.header.Server;
import gov.nist.javax.sip.message.MessageFactoryImpl;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import gov.nist.javax.sip.stack.MessageChannel;
import gov.nist.javax.sip.stack.SIPClientTransaction;
import gov.nist.javax.sip.stack.SIPDialog;
import gov.nist.javax.sip.stack.SIPServerTransaction;
import gov.nist.javax.sip.stack.SIPTransaction;
import gov.nist.javax.sip.stack.ServerRequestInterface;
import gov.nist.javax.sip.stack.ServerResponseInterface;
import java.io.IOException;
import javax.sip.ClientTransaction;
import javax.sip.DialogState;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.TransactionState;
import javax.sip.header.EventHeader;
import javax.sip.header.ReferToHeader;
import javax.sip.header.ServerHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * An adapter class from the JAIN implementation objects to the NIST-SIP stack. The primary
 * purpose of this class is to do early rejection of bad messages and deliver meaningful messages
 * to the application. This class is essentially a Dialog filter. It checks for and rejects
 * requests and responses which may be filtered out because of sequence number, Dialog not found,
 * etc. Note that this is not part of the JAIN-SIP spec (it does not implement a JAIN-SIP
 * interface). This is part of the glue that ties together the NIST-SIP stack and event model with
 * the JAIN-SIP stack. This is strictly an implementation class.
 * 
 * @version 1.2 $Revision: 1.29 $ $Date: 2009/06/23 11:02:17 $
 * 
 * @author M. Ranganathan
 */
class DialogFilter implements ServerRequestInterface, ServerResponseInterface {

    protected SIPTransaction transactionChannel;

    protected ListeningPointImpl listeningPoint;

    private SipStackImpl sipStack;

    public DialogFilter(SipStackImpl sipStack) {
        this.sipStack = sipStack;
    }

    /**
     * Send back an error Response.
     * 
     * @param sipRequest
     * @param transaction
     */
    private void send500Response(SIPRequest sipRequest, SIPServerTransaction transaction) {
        if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Sending 500 response for out of sequence message");
        SIPResponse sipResponse = sipRequest.createResponse(Response.SERVER_INTERNAL_ERROR);
        sipResponse.setReasonPhrase("Request out of order");
        try {
            transaction.sendMessage(sipResponse);
            sipStack.removeTransaction(transaction);
            transaction.releaseSem();
        } catch (IOException ex) {
            transaction.raiseIOExceptionEvent();
            sipStack.removeTransaction(transaction);
        }
    }

    /**
     * Process a request. Check for various conditions in the dialog that can result in the
     * message being dropped. Possibly return errors for these conditions.
     * 
     * @exception SIPServerException is thrown when there is an error processing the request.
     */
    public void processRequest(SIPRequest sipRequest, MessageChannel incomingMessageChannel) {
        if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("PROCESSING INCOMING REQUEST " + sipRequest + " transactionChannel = " + transactionChannel + " listening point = " + listeningPoint.getIPAddress() + ":" + listeningPoint.getPort());
        if (listeningPoint == null) {
            if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Dropping message: No listening point registered!");
            return;
        }
        SipStackImpl sipStack = (SipStackImpl) transactionChannel.getSIPStack();
        SipProviderImpl sipProvider = listeningPoint.getProvider();
        if (sipProvider == null) {
            if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("No provider - dropping !!");
            return;
        }
        if (sipStack == null) InternalErrorHandler.handleException("Egads! no sip stack!");
        SIPServerTransaction transaction = (SIPServerTransaction) this.transactionChannel;
        if (transaction != null) {
            if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("transaction state = " + transaction.getState());
        }
        String dialogId = sipRequest.getDialogId(true);
        SIPDialog dialog = sipStack.getDialog(dialogId);
        if (dialog != null && sipProvider != dialog.getSipProvider()) {
            Contact contact = dialog.getMyContactHeader();
            if (contact != null) {
                SipUri contactUri = (SipUri) (contact.getAddress().getURI());
                String ipAddress = contactUri.getHost();
                int contactPort = contactUri.getPort();
                String contactTransport = contactUri.getTransportParam();
                if (contactTransport == null) contactTransport = "udp";
                if (contactPort == -1) {
                    if (contactTransport.equals("udp") || contactTransport.equals("tcp")) contactPort = 5060; else contactPort = 5061;
                }
                if (ipAddress != null && (!ipAddress.equals(listeningPoint.getIPAddress()) || contactPort != listeningPoint.getPort())) {
                    if (sipStack.isLoggingEnabled()) {
                        sipStack.getLogWriter().logDebug("nulling dialog -- listening point mismatch!  " + contactPort + "  lp port = " + listeningPoint.getPort());
                    }
                    dialog = null;
                }
            }
        }
        if (sipProvider.isAutomaticDialogSupportEnabled() && sipRequest.getToTag() == null) {
            SIPServerTransaction sipServerTransaction = sipStack.findMergedTransaction(sipRequest);
            if (sipServerTransaction != null && !sipServerTransaction.isMessagePartOfTransaction(sipRequest) && sipServerTransaction.getState() != TransactionState.TERMINATED) {
                SIPResponse response = sipRequest.createResponse(Response.LOOP_DETECTED);
                if (sipStack.getLogWriter().isLoggingEnabled()) sipStack.getLogWriter().logError("Loop detected while processing request");
                try {
                    sipProvider.sendResponse(response);
                } catch (SipException e) {
                    if (sipStack.getLogWriter().isLoggingEnabled()) sipStack.getLogWriter().logError("Error sending response");
                }
                return;
            }
        }
        if (sipStack.isLoggingEnabled()) {
            sipStack.getLogWriter().logDebug("dialogId = " + dialogId);
            sipStack.getLogWriter().logDebug("dialog = " + dialog);
        }
        if (sipRequest.getHeader(Route.NAME) != null && transaction.getDialog() != null) {
            RouteList routes = sipRequest.getRouteHeaders();
            Route route = (Route) routes.getFirst();
            SipUri uri = (SipUri) route.getAddress().getURI();
            int port;
            if (uri.getHostPort().hasPort()) {
                port = uri.getHostPort().getPort();
            } else {
                if (listeningPoint.getTransport().equalsIgnoreCase("TLS")) port = 5061; else port = 5060;
            }
            String host = uri.getHost();
            if ((host.equals(listeningPoint.getIPAddress()) || host.equalsIgnoreCase(listeningPoint.getSentBy())) && port == listeningPoint.getPort()) {
                if (routes.size() == 1) sipRequest.removeHeader(Route.NAME); else routes.removeFirst();
            }
        }
        if (sipRequest.getMethod().equals(Request.REFER)) {
            ReferToHeader sipHeader = (ReferToHeader) sipRequest.getHeader(ReferTo.NAME);
            if (sipHeader == null && dialog != null) {
                SIPResponse badRequest = sipRequest.createResponse(Response.BAD_REQUEST);
                badRequest.setReasonPhrase("Refer-To header missing");
                try {
                    sipProvider.sendResponse(badRequest);
                } catch (SipException e) {
                    sipStack.getLogWriter().logError("error sending response", e);
                }
                if (transaction != null) {
                    sipStack.removeTransaction(transaction);
                    transaction.releaseSem();
                }
                return;
            }
        } else if (sipRequest.getMethod().equals(Request.UPDATE)) {
            if (sipProvider.isAutomaticDialogSupportEnabled() && dialog == null) {
                Response notExist = sipRequest.createResponse(Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST);
                try {
                    sipProvider.sendResponse(notExist);
                } catch (SipException e) {
                    sipStack.getLogWriter().logError("error sending response", e);
                }
                if (transaction != null) {
                    sipStack.removeTransaction(transaction);
                    transaction.releaseSem();
                }
                return;
            }
        } else if (sipRequest.getMethod().equals(Request.ACK)) {
            if (transaction != null && transaction.isInviteTransaction()) {
                if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Processing ACK for INVITE Tx ");
            } else {
                if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Processing ACK for dialog " + dialog);
                if (dialog == null) {
                    if (sipStack.isLoggingEnabled()) {
                        sipStack.getLogWriter().logDebug("Dialog does not exist " + sipRequest.getFirstLine() + " isServerTransaction = " + true);
                    }
                    SIPServerTransaction st = sipStack.getRetransmissionAlertTransaction(dialogId);
                    if (st != null && st.isRetransmissionAlertEnabled()) {
                        st.disableRetransmissionAlerts();
                    }
                } else {
                    if (!dialog.handleAck(transaction)) {
                        if (sipStack.isLooseDialogValidation()) {
                            if (sipStack.isLoggingEnabled()) {
                                sipStack.getLogWriter().logDebug("Dialog exists with loose dialog validation " + sipRequest.getFirstLine() + " isServerTransaction = " + true + " dialog = " + dialog.getDialogId());
                            }
                            SIPServerTransaction st = sipStack.getRetransmissionAlertTransaction(dialogId);
                            if (st != null && st.isRetransmissionAlertEnabled()) {
                                st.disableRetransmissionAlerts();
                            }
                        } else {
                            return;
                        }
                    } else {
                        transaction.passToListener();
                        dialog.addTransaction(transaction);
                        dialog.addRoute(sipRequest);
                        transaction.setDialog(dialog, dialogId);
                        if (sipStack.isDialogCreated(sipRequest.getMethod())) {
                            sipStack.putInMergeTable(transaction, sipRequest);
                        }
                        if (sipStack.deliverTerminatedEventForAck) {
                            try {
                                sipStack.addTransaction(transaction);
                                transaction.scheduleAckRemoval();
                            } catch (IOException ex) {
                            }
                        } else {
                            transaction.setMapped(true);
                        }
                    }
                }
            }
        } else if (sipRequest.getMethod().equals(Request.PRACK)) {
            if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Processing PRACK for dialog " + dialog);
            if (dialog == null && sipProvider.isAutomaticDialogSupportEnabled()) {
                if (sipStack.isLoggingEnabled()) {
                    sipStack.getLogWriter().logDebug("Dialog does not exist " + sipRequest.getFirstLine() + " isServerTransaction = " + true);
                }
                if (sipStack.isLoggingEnabled()) {
                    sipStack.getLogWriter().logDebug("Sending 481 for PRACK - automatic dialog support is enabled -- cant find dialog!");
                }
                SIPResponse notExist = sipRequest.createResponse(Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST);
                try {
                    sipProvider.sendResponse(notExist);
                } catch (SipException e) {
                    sipStack.getLogWriter().logError("error sending response", e);
                }
                if (transaction != null) {
                    sipStack.removeTransaction(transaction);
                    transaction.releaseSem();
                }
                return;
            } else if (dialog != null) {
                if (!dialog.handlePrack(sipRequest)) {
                    sipStack.getLogWriter().logDebug("Dropping out of sequence PRACK ");
                    if (transaction != null) {
                        sipStack.removeTransaction(transaction);
                        transaction.releaseSem();
                    }
                    return;
                } else {
                    try {
                        sipStack.addTransaction(transaction);
                        dialog.addTransaction(transaction);
                        dialog.addRoute(sipRequest);
                        transaction.setDialog(dialog, dialogId);
                    } catch (Exception ex) {
                        InternalErrorHandler.handleException(ex);
                    }
                }
            } else {
                sipStack.getLogWriter().logDebug("Processing PRACK without a DIALOG -- this must be a proxy element");
            }
        } else if (sipRequest.getMethod().equals(Request.BYE)) {
            if (dialog != null && !dialog.isRequestConsumable(sipRequest)) {
                if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Dropping out of sequence BYE " + dialog.getRemoteSeqNumber() + " " + sipRequest.getCSeq().getSeqNumber());
                if (dialog.getRemoteSeqNumber() >= sipRequest.getCSeq().getSeqNumber() && transaction.getState() == TransactionState.TRYING) {
                    this.send500Response(sipRequest, transaction);
                }
                if (transaction != null) sipStack.removeTransaction(transaction);
                return;
            } else if (dialog == null && sipProvider.isAutomaticDialogSupportEnabled()) {
                SIPResponse response = sipRequest.createResponse(Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST);
                response.setReasonPhrase("Dialog Not Found");
                sipStack.getLogWriter().logDebug("dropping request -- automatic dialog " + "support enabled and dialog does not exist!");
                try {
                    transaction.sendResponse(response);
                } catch (SipException ex) {
                    sipStack.getLogWriter().logError("Error in sending response", ex);
                }
                if (transaction != null) {
                    sipStack.removeTransaction(transaction);
                    transaction.releaseSem();
                    transaction = null;
                }
                return;
            }
            if (transaction != null && dialog != null) {
                try {
                    if (sipProvider == dialog.getSipProvider()) {
                        sipStack.addTransaction(transaction);
                        dialog.addTransaction(transaction);
                        transaction.setDialog(dialog, dialogId);
                    }
                } catch (IOException ex) {
                    InternalErrorHandler.handleException(ex);
                }
            }
            if (sipStack.getLogWriter().isLoggingEnabled()) {
                sipStack.getLogWriter().logDebug("BYE Tx = " + transaction + " isMapped =" + transaction.isTransactionMapped());
            }
        } else if (sipRequest.getMethod().equals(Request.CANCEL)) {
            SIPServerTransaction st = (SIPServerTransaction) sipStack.findCancelTransaction(sipRequest, true);
            if (sipStack.getLogWriter().isLoggingEnabled()) {
                sipStack.getLogWriter().logDebug("Got a CANCEL, InviteServerTx = " + st + " cancel Server Tx ID = " + transaction + " isMapped = " + transaction.isTransactionMapped());
            }
            if (sipRequest.getMethod().equals(Request.CANCEL)) {
                if (st != null && st.getState() == SIPTransaction.TERMINATED_STATE) {
                    if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Too late to cancel Transaction");
                    try {
                        transaction.sendResponse(sipRequest.createResponse(Response.OK));
                    } catch (Exception ex) {
                        if (ex.getCause() != null && ex.getCause() instanceof IOException) {
                            st.raiseIOExceptionEvent();
                        }
                    }
                    return;
                }
                if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Cancel transaction = " + st);
            }
            if (transaction != null && st != null && st.getDialog() != null) {
                transaction.setDialog((SIPDialog) st.getDialog(), dialogId);
                dialog = (SIPDialog) st.getDialog();
            } else if (st == null && sipProvider.isAutomaticDialogSupportEnabled() && transaction != null) {
                SIPResponse response = sipRequest.createResponse(Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST);
                if (sipStack.isLoggingEnabled()) {
                    sipStack.getLogWriter().logDebug("dropping request -- automatic dialog support " + "enabled and INVITE ST does not exist!");
                }
                try {
                    sipProvider.sendResponse(response);
                } catch (SipException ex) {
                    InternalErrorHandler.handleException(ex);
                }
                if (transaction != null) {
                    sipStack.removeTransaction(transaction);
                    transaction.releaseSem();
                }
                return;
            }
            if (st != null) {
                try {
                    if (transaction != null) {
                        sipStack.addTransaction(transaction);
                        transaction.setPassToListener();
                        transaction.setInviteTransaction(st);
                        st.acquireSem();
                    }
                } catch (Exception ex) {
                    InternalErrorHandler.handleException(ex);
                }
            }
        } else if (sipRequest.getMethod().equals(Request.INVITE)) {
            SIPTransaction lastTransaction = dialog == null ? null : dialog.getInviteTransaction();
            if (dialog != null && transaction != null && lastTransaction != null && sipRequest.getCSeq().getSeqNumber() > dialog.getRemoteSeqNumber() && lastTransaction instanceof SIPServerTransaction && lastTransaction.isInviteTransaction() && lastTransaction.getState() != TransactionState.COMPLETED && lastTransaction.getState() != TransactionState.TERMINATED && lastTransaction.getState() != TransactionState.CONFIRMED) {
                if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Sending 500 response for out of sequence message");
                SIPResponse sipResponse = sipRequest.createResponse(Response.SERVER_INTERNAL_ERROR);
                RetryAfter retryAfter = new RetryAfter();
                try {
                    retryAfter.setRetryAfter((int) (10 * Math.random()));
                } catch (InvalidArgumentException ex) {
                    ex.printStackTrace();
                }
                sipResponse.addHeader(retryAfter);
                try {
                    transaction.sendMessage(sipResponse);
                } catch (IOException ex) {
                    transaction.raiseIOExceptionEvent();
                }
                return;
            }
            lastTransaction = (dialog == null ? null : dialog.getLastTransaction());
            if (dialog != null && lastTransaction != null && lastTransaction.isInviteTransaction() && lastTransaction instanceof SIPClientTransaction && lastTransaction.getState() != TransactionState.COMPLETED && lastTransaction.getState() != TransactionState.TERMINATED) {
                if (dialog.getRemoteSeqNumber() + 1 == sipRequest.getCSeq().getSeqNumber()) {
                    dialog.setRemoteSequenceNumber(sipRequest.getCSeq().getSeqNumber());
                    if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Sending 491 response for out of sequence message");
                    SIPResponse sipResponse = sipRequest.createResponse(Response.REQUEST_PENDING);
                    try {
                        transaction.sendMessage(sipResponse);
                    } catch (IOException ex) {
                        transaction.raiseIOExceptionEvent();
                    }
                    dialog.requestConsumed();
                } else {
                    if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Dropping message -- sequence number is too high!");
                }
                return;
            }
        }
        sipStack.getLogWriter().logDebug("CHECK FOR OUT OF SEQ MESSAGE " + dialog + " transaction " + transaction);
        if (dialog != null && transaction != null && !sipRequest.getMethod().equals(Request.BYE) && !sipRequest.getMethod().equals(Request.CANCEL) && !sipRequest.getMethod().equals(Request.ACK) && !sipRequest.getMethod().equals(Request.PRACK)) {
            if (!dialog.isRequestConsumable(sipRequest)) {
                if (sipStack.isLoggingEnabled()) {
                    sipStack.getLogWriter().logDebug("Dropping out of sequence message " + dialog.getRemoteSeqNumber() + " " + sipRequest.getCSeq());
                }
                if (dialog.getRemoteSeqNumber() >= sipRequest.getCSeq().getSeqNumber() && (transaction.getState() == TransactionState.TRYING || transaction.getState() == TransactionState.PROCEEDING)) {
                    this.send500Response(sipRequest, transaction);
                }
                return;
            }
            try {
                if (sipProvider == dialog.getSipProvider()) {
                    sipStack.addTransaction(transaction);
                    dialog.addTransaction(transaction);
                    dialog.addRoute(sipRequest);
                    transaction.setDialog(dialog, dialogId);
                }
            } catch (IOException ex) {
                transaction.raiseIOExceptionEvent();
                sipStack.removeTransaction(transaction);
                return;
            }
        }
        RequestEvent sipEvent;
        if (sipStack.getLogWriter().isLoggingEnabled()) {
            sipStack.getLogWriter().logDebug(sipRequest.getMethod() + " transaction.isMapped = " + transaction.isTransactionMapped());
        }
        if (dialog == null && sipRequest.getMethod().equals(Request.NOTIFY)) {
            SIPClientTransaction pendingSubscribeClientTx = sipStack.findSubscribeTransaction(sipRequest, listeningPoint);
            if (sipStack.getLogWriter().isLoggingEnabled()) {
                sipStack.getLogWriter().logDebug("PROCESSING NOTIFY  DIALOG == null " + pendingSubscribeClientTx);
            }
            if (sipProvider.isAutomaticDialogSupportEnabled() && pendingSubscribeClientTx == null && !sipStack.deliverUnsolicitedNotify) {
                try {
                    if (sipStack.isLoggingEnabled()) {
                        sipStack.getLogWriter().logDebug("Could not find Subscription for Notify Tx.");
                    }
                    Response errorResponse = sipRequest.createResponse(Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST);
                    errorResponse.setReasonPhrase("Subscription does not exist");
                    sipProvider.sendResponse(errorResponse);
                    return;
                } catch (Exception ex) {
                    sipStack.getLogWriter().logError("Exception while sending error response statelessly", ex);
                    return;
                }
            }
            if (pendingSubscribeClientTx != null) {
                transaction.setPendingSubscribe(pendingSubscribeClientTx);
                SIPDialog subscriptionDialog = (SIPDialog) pendingSubscribeClientTx.getDefaultDialog();
                if (subscriptionDialog == null || subscriptionDialog.getDialogId() == null || !subscriptionDialog.getDialogId().equals(dialogId)) {
                    if (subscriptionDialog != null && subscriptionDialog.getDialogId() == null) {
                        subscriptionDialog.setDialogId(dialogId);
                    } else {
                        subscriptionDialog = pendingSubscribeClientTx.getDialog(dialogId);
                    }
                    if (sipStack.getLogWriter().isLoggingEnabled()) {
                        sipStack.getLogWriter().logDebug("PROCESSING NOTIFY Subscribe DIALOG " + subscriptionDialog);
                    }
                    if (subscriptionDialog == null && (sipProvider.isAutomaticDialogSupportEnabled() || pendingSubscribeClientTx.getDefaultDialog() != null)) {
                        Event event = (Event) sipRequest.getHeader(EventHeader.NAME);
                        if (sipStack.isEventForked(event.getEventType())) {
                            subscriptionDialog = SIPDialog.createFromNOTIFY(pendingSubscribeClientTx, transaction);
                        }
                    }
                    if (subscriptionDialog != null) {
                        transaction.setDialog(subscriptionDialog, dialogId);
                        subscriptionDialog.setState(DialogState.CONFIRMED.getValue());
                        sipStack.putDialog(subscriptionDialog);
                        pendingSubscribeClientTx.setDialog(subscriptionDialog, dialogId);
                        if (!transaction.isTransactionMapped()) {
                            this.sipStack.mapTransaction(transaction);
                            transaction.setPassToListener();
                            try {
                                this.sipStack.addTransaction(transaction);
                            } catch (Exception ex) {
                            }
                        }
                    }
                } else {
                    transaction.setDialog(subscriptionDialog, dialogId);
                    dialog = subscriptionDialog;
                    if (!transaction.isTransactionMapped()) {
                        this.sipStack.mapTransaction(transaction);
                        transaction.setPassToListener();
                        try {
                            this.sipStack.addTransaction(transaction);
                        } catch (Exception ex) {
                        }
                    }
                    sipStack.putDialog(subscriptionDialog);
                    if (pendingSubscribeClientTx != null) {
                        subscriptionDialog.addTransaction(pendingSubscribeClientTx);
                        pendingSubscribeClientTx.setDialog(subscriptionDialog, dialogId);
                    }
                }
                if (transaction != null && ((SIPServerTransaction) transaction).isTransactionMapped()) {
                    sipEvent = new RequestEvent((SipProvider) sipProvider, (ServerTransaction) transaction, subscriptionDialog, (Request) sipRequest);
                } else {
                    sipEvent = new RequestEvent((SipProvider) sipProvider, null, subscriptionDialog, (Request) sipRequest);
                }
            } else {
                if (sipStack.getLogWriter().isLoggingEnabled()) {
                    sipStack.getLogWriter().logDebug("could not find subscribe tx");
                }
                sipEvent = new RequestEvent(sipProvider, null, null, (Request) sipRequest);
            }
        } else {
            if (transaction != null && (((SIPServerTransaction) transaction).isTransactionMapped())) {
                sipEvent = new RequestEvent(sipProvider, (ServerTransaction) transaction, dialog, (Request) sipRequest);
            } else {
                sipEvent = new RequestEvent(sipProvider, null, dialog, (Request) sipRequest);
            }
        }
        sipProvider.handleEvent(sipEvent, transaction);
    }

    /**
     * Process the response.
     * 
     * @exception SIPServerException is thrown when there is an error processing the response
     * @param incomingMessageChannel -- message channel on which the response is received.
     */
    public void processResponse(SIPResponse response, MessageChannel incomingMessageChannel, SIPDialog dialog) {
        if (sipStack.isLoggingEnabled()) {
            sipStack.getLogWriter().logDebug("PROCESSING INCOMING RESPONSE" + response.encodeMessage());
        }
        if (listeningPoint == null) {
            if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logError("Dropping message: No listening point" + " registered!");
            return;
        }
        SipProviderImpl sipProvider = listeningPoint.getProvider();
        if (sipProvider == null) {
            if (sipStack.isLoggingEnabled()) {
                sipStack.getLogWriter().logError("Dropping message:  no provider");
            }
            return;
        }
        if (sipProvider.sipListener == null) {
            if (sipStack.isLoggingEnabled()) {
                sipStack.getLogWriter().logError("No listener -- dropping response!");
            }
            return;
        }
        SIPClientTransaction transaction = (SIPClientTransaction) this.transactionChannel;
        SipStackImpl sipStackImpl = sipProvider.sipStack;
        if (sipStack.isLoggingEnabled()) sipStackImpl.getLogWriter().logDebug("Transaction = " + transaction);
        if (transaction == null) {
            if (dialog != null) {
                if (response.getStatusCode() / 100 != 2) {
                    if (sipStack.isLoggingEnabled()) {
                        sipStack.getLogWriter().logDebug("Response is not a final response and dialog is found for response -- dropping response!");
                    }
                    return;
                } else if (dialog.getState() == DialogState.TERMINATED) {
                    if (sipStack.isLoggingEnabled()) {
                        sipStack.getLogWriter().logDebug("Dialog is terminated -- dropping response!");
                    }
                    return;
                } else {
                    boolean ackAlreadySent = false;
                    if (dialog.isAckSeen() && dialog.getLastAck() != null) {
                        if (dialog.getLastAck().getCSeq().getSeqNumber() == response.getCSeq().getSeqNumber()) {
                            ackAlreadySent = true;
                        }
                    }
                    if (ackAlreadySent && response.getCSeq().getMethod().equals(dialog.getMethod())) {
                        try {
                            if (sipStack.isLoggingEnabled()) {
                                sipStack.getLogWriter().logDebug("Retransmission of OK detected: Resending last ACK");
                            }
                            dialog.resendAck();
                            return;
                        } catch (SipException ex) {
                            sipStack.getLogWriter().logError("could not resend ack", ex);
                        }
                    }
                }
            }
            if (sipStack.isLoggingEnabled()) {
                sipStack.getLogWriter().logDebug("could not find tx, handling statelessly Dialog = 	" + dialog);
            }
            ResponseEvent sipEvent = new ResponseEvent(sipProvider, transaction, dialog, (Response) response);
            sipProvider.handleEvent(sipEvent, transaction);
            return;
        }
        ResponseEvent responseEvent = null;
        responseEvent = new javax.sip.ResponseEvent(sipProvider, (ClientTransaction) transaction, dialog, (Response) response);
        if (dialog != null && response.getStatusCode() != 100) {
            dialog.setLastResponse(transaction, response);
            transaction.setDialog(dialog, dialog.getDialogId());
        }
        sipProvider.handleEvent(responseEvent, transaction);
    }

    /**
     * Just a placeholder. This is called from the stack for message logging. Auxiliary processing
     * information can be passed back to be written into the log file.
     * 
     * @return auxiliary information that we may have generated during the message processing
     *         which is retrieved by the message logger.
     */
    public String getProcessingInfo() {
        return null;
    }

    public void processResponse(SIPResponse sipResponse, MessageChannel incomingChannel) {
        String dialogID = sipResponse.getDialogId(false);
        SIPDialog sipDialog = this.sipStack.getDialog(dialogID);
        String method = sipResponse.getCSeq().getMethod();
        if (sipStack.isLoggingEnabled()) {
            sipStack.getLogWriter().logDebug("PROCESSING INCOMING RESPONSE: " + sipResponse.encodeMessage());
        }
        if (listeningPoint == null) {
            if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Dropping message: No listening point" + " registered!");
            return;
        }
        SipProviderImpl sipProvider = listeningPoint.getProvider();
        if (sipProvider == null) {
            if (sipStack.isLoggingEnabled()) {
                sipStack.getLogWriter().logDebug("Dropping message:  no provider");
            }
            return;
        }
        if (sipProvider.sipListener == null) {
            if (sipStack.isLoggingEnabled()) {
                sipStack.getLogWriter().logDebug("Dropping message:  no sipListener registered!");
            }
            return;
        }
        SIPClientTransaction transaction = (SIPClientTransaction) this.transactionChannel;
        if (sipDialog == null && transaction != null) {
            sipDialog = transaction.getDialog(dialogID);
            if (sipDialog != null && sipDialog.getState() == DialogState.TERMINATED) sipDialog = null;
        }
        if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("Transaction = " + transaction + " sipDialog = " + sipDialog);
        if (this.transactionChannel != null) {
            String originalFrom = ((SIPRequest) this.transactionChannel.getRequest()).getFromTag();
            if (originalFrom == null ^ sipResponse.getFrom().getTag() == null) {
                sipStack.getLogWriter().logDebug("From tag mismatch -- dropping response");
                return;
            }
            if (originalFrom != null && !originalFrom.equalsIgnoreCase(sipResponse.getFrom().getTag())) {
                sipStack.getLogWriter().logDebug("From tag mismatch -- dropping response");
                return;
            }
        }
        if (sipStack.isDialogCreated(method) && sipResponse.getStatusCode() != 100 && sipResponse.getFrom().getTag() != null && sipResponse.getTo().getTag() != null && sipDialog == null) {
            if (sipProvider.isAutomaticDialogSupportEnabled()) {
                if (this.transactionChannel != null) {
                    if (sipDialog == null) {
                        sipDialog = sipStack.createDialog((SIPClientTransaction) this.transactionChannel, sipResponse);
                        this.transactionChannel.setDialog(sipDialog, sipResponse.getDialogId(false));
                    }
                } else {
                    sipDialog = new SIPDialog(sipProvider, sipResponse);
                }
            }
        } else {
            if (sipDialog != null && transaction == null) {
                if (sipResponse.getStatusCode() / 100 != 2) {
                    if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("status code != 200 ; statusCode = " + sipResponse.getStatusCode());
                    return;
                } else if (sipDialog.getState() == DialogState.TERMINATED) {
                    if (sipStack.isLoggingEnabled()) {
                        sipStack.getLogWriter().logDebug("Dialog is terminated -- dropping response!");
                    }
                    return;
                } else {
                    boolean ackAlreadySent = false;
                    if (sipDialog.isAckSeen() && sipDialog.getLastAck() != null) {
                        if (sipDialog.getLastAck().getCSeq().getSeqNumber() == sipResponse.getCSeq().getSeqNumber() && sipResponse.getDialogId(false).equals(sipDialog.getLastAck().getDialogId(false))) {
                            ackAlreadySent = true;
                        }
                    }
                    if (ackAlreadySent && sipResponse.getCSeq().getMethod().equals(sipDialog.getMethod())) {
                        try {
                            if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("resending ACK");
                            sipDialog.resendAck();
                            return;
                        } catch (SipException ex) {
                        }
                    }
                }
            }
        }
        if (sipStack.isLoggingEnabled()) sipStack.getLogWriter().logDebug("sending response to TU for processing ");
        if (sipDialog != null && sipResponse.getStatusCode() != 100 && sipResponse.getTo().getTag() != null) {
            sipDialog.setLastResponse(transaction, sipResponse);
        }
        ResponseEvent responseEvent = new javax.sip.ResponseEvent(sipProvider, (ClientTransaction) transaction, sipDialog, (Response) sipResponse);
        sipProvider.handleEvent(responseEvent, transaction);
    }
}
