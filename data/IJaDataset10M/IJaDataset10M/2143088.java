package net.java.sip.communicator.sip;

import java.text.*;
import java.util.*;
import javax.sip.*;
import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;
import net.java.sip.communicator.common.*;
import net.java.sip.communicator.sip.security.SipSecurityException;

/**
 * <p>Title: SIP COMMUNICATOR-1.1</p>
 * <p>Description: JAIN-SIP-1.1 Audio/Video Phone Application</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Organisation: LSIIT Laboratory (http://lsiit.u-strasbg.fr)<p>
 * </p>Network Research Team (http://www-r2.u-strasbg.fr))<p>
 * </p>Louis Pasteur University - Strasbourg - France</p>
 * @author Emil Ivov
 * @version 1.1
 */
public class CallProcessing {

    protected static final Console console = Console.getConsole(CallProcessing.class);

    protected SipManager sipManCallback = null;

    protected CallDispatcher callDispatcher = new CallDispatcher();

    CallProcessing() {
        try {
            console.logEntry();
        } finally {
            console.logExit();
        }
    }

    CallProcessing(SipManager sipManCallback) {
        this.sipManCallback = sipManCallback;
    }

    void setSipManagerCallBack(SipManager sipManCallback) {
        this.sipManCallback = sipManCallback;
    }

    void processTrying(ClientTransaction clientTransaction, Response response) {
        try {
            console.logEntry();
            Call call = callDispatcher.findCall(clientTransaction.getDialog());
            if (call == null) {
                sipManCallback.fireUnknownMessageReceived(response);
                return;
            }
            if (!call.getState().equals(Call.MOVING_LOCALLY)) call.setState(Call.DIALING);
        } finally {
            console.logExit();
        }
    }

    void processRinging(ClientTransaction clientTransaction, Response response) {
        try {
            console.logEntry();
            Call call = callDispatcher.findCall(clientTransaction.getDialog());
            if (call == null) {
                sipManCallback.fireUnknownMessageReceived(response);
                return;
            }
            call.setState(Call.RINGING);
        } finally {
            console.logExit();
        }
    }

    /**
     * According to the RFC  a
     * UAC canceling a request cannot rely on receiving a 487 (Request
     * Terminated) response for the original request, as an RFC 2543-
     * compliant UAS will not generate such a response. So we are closing the
     * call when sending the cancel request and here we don't do anything.
     * @param clientTransaction
     * @param response
     */
    void processRequestTerminated(ClientTransaction clientTransaction, Response response) {
        try {
            console.logEntry();
        } finally {
            console.logExit();
        }
    }

    void processByeOK(ClientTransaction clientTransaction, Response response) {
        try {
            console.logEntry();
        } finally {
            console.logExit();
        }
    }

    void processCancelOK(ClientTransaction clientTransaction, Response response) {
        try {
            console.logEntry();
        } finally {
            console.logExit();
        }
    }

    void processInviteOK(ClientTransaction clientTransaction, Response ok) {
        try {
            console.logEntry();
            Call call = callDispatcher.findCall(clientTransaction.getDialog());
            if (call == null) {
                sipManCallback.fireUnknownMessageReceived(ok);
                return;
            }
            try {
                Request ack = (Request) clientTransaction.getDialog().createRequest(Request.ACK);
                clientTransaction.getDialog().sendAck(ack);
            } catch (SipException ex) {
                console.error("Failed to acknowledge call!", ex);
                call.setState(Call.DISCONNECTED);
                sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to acknowledge call!", ex));
                return;
            }
            call.setRemoteSdpDescription(new String(ok.getRawContent()));
            if (!call.getState().equals(Call.CONNECTED)) {
                call.setState(Call.CONNECTED);
            }
        } finally {
            console.logExit();
        }
    }

    void processBusyHere(ClientTransaction clientTransaction, Response busyHere) {
        try {
            console.logEntry();
            Call call = callDispatcher.findCall(clientTransaction.getDialog());
            if (call == null) {
                sipManCallback.fireUnknownMessageReceived(busyHere);
                return;
            }
            call.setState(Call.BUSY);
        } finally {
            console.logExit();
        }
    }

    void processCallError(ClientTransaction clientTransaction, Response notAcceptable) {
        try {
            console.logEntry();
            if (console.isDebugEnabled()) {
                console.debug("Processing CALL ERROR response:\n" + notAcceptable);
            }
            Call call = callDispatcher.findCall(clientTransaction.getDialog());
            if (call == null) {
                sipManCallback.fireUnknownMessageReceived(notAcceptable);
                return;
            }
            call.setState(Call.FAILED);
            sipManCallback.fireCommunicationsError(new CommunicationsException("Remote party returned error response: " + notAcceptable.getStatusCode() + " - " + notAcceptable.getReasonPhrase()));
            return;
        } finally {
            console.logExit();
        }
    }

    /**
     * Attempts to re-ogenerate the corresponding request with the proper
     * credentials and terminates the call if it fails.
     *
     * @param clientTransaction the corresponding transaction
     * @param response the challenge
     */
    void processAuthenticationChallenge(ClientTransaction clientTransaction, Response response) {
        try {
            console.logEntry();
            Request challengedRequest = clientTransaction.getRequest();
            console.debug("");
            ClientTransaction retryTran = sipManCallback.sipSecurityManager.handleChallenge(response, clientTransaction);
            Call call = callDispatcher.findCall(clientTransaction.getDialog());
            call.setDialog(retryTran.getDialog());
            retryTran.sendRequest();
        } catch (SipSecurityException exc) {
            callDispatcher.findCall(clientTransaction.getDialog()).setState(Call.FAILED);
            sipManCallback.fireCommunicationsError(new CommunicationsException("Authorization failed!", exc));
        } catch (Exception exc) {
            callDispatcher.findCall(clientTransaction.getDialog()).setState(Call.FAILED);
            sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to resend a request " + "after a security challenge!", exc));
        } finally {
            console.logExit();
        }
    }

    void processInvite(ServerTransaction serverTransaction, Request invite) {
        try {
            console.logEntry();
            Dialog dialog = serverTransaction.getDialog();
            Call call = callDispatcher.createCall(dialog, invite);
            sipManCallback.fireCallReceived(call);
            call.setState(Call.ALERTING);
            ContentLengthHeader cl = invite.getContentLength();
            if (cl != null && cl.getContentLength() > 0) {
                call.setRemoteSdpDescription(new String(invite.getRawContent()));
            }
            URI calleeURI = ((ToHeader) invite.getHeader(ToHeader.NAME)).getAddress().getURI();
            if (calleeURI.isSipURI()) {
                String calleeUser = ((SipURI) calleeURI).getUser();
                String localUser = sipManCallback.getLocalUser();
                boolean assertUserMatch = Boolean.valueOf(Utils.getProperty("net.java.sip.communicator.sip.FAIL_CALLS_ON_DEST_USER_MISMATCH")).booleanValue();
                if (!calleeUser.equals(localUser) && assertUserMatch) {
                    sipManCallback.fireCallRejectedLocally("The user specified by the caller did not match the local user!", invite);
                    call.setState(Call.DISCONNECTED);
                    Response notFound = null;
                    try {
                        notFound = sipManCallback.messageFactory.createResponse(Response.NOT_FOUND, invite);
                        sipManCallback.attachToTag(notFound, dialog);
                    } catch (ParseException ex) {
                        call.setState(Call.DISCONNECTED);
                        sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to create a NOT_FOUND response to an INVITE request!", ex));
                        return;
                    }
                    try {
                        serverTransaction.sendResponse(notFound);
                        if (console.isDebugEnabled()) console.debug("sent a not found response: " + notFound);
                    } catch (SipException ex) {
                        call.setState(Call.DISCONNECTED);
                        sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to send a NOT_FOUND response to an INVITE request!", ex));
                        return;
                    }
                    return;
                }
            }
            Response ringing = null;
            try {
                ringing = sipManCallback.messageFactory.createResponse(Response.RINGING, invite);
                sipManCallback.attachToTag(ringing, dialog);
            } catch (ParseException ex) {
                call.setState(Call.DISCONNECTED);
                sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to create a RINGING response to an INVITE request!", ex));
                return;
            }
            try {
                serverTransaction.sendResponse(ringing);
                if (console.isDebugEnabled()) console.debug("sent a ringing response: " + ringing);
            } catch (SipException ex) {
                call.setState(Call.DISCONNECTED);
                sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to send a RINGING response to an INVITE request!", ex));
                return;
            }
        } finally {
            console.logExit();
        }
    }

    void processTimeout(Transaction transaction, Request request) {
        try {
            console.logEntry();
            Call call = callDispatcher.findCall(transaction.getDialog());
            if (call == null) {
                return;
            }
            sipManCallback.fireCommunicationsError(new CommunicationsException("The remote party has not replied!" + "The call will be disconnected"));
            call.setState(Call.DISCONNECTED);
        } finally {
            console.logExit();
        }
    }

    void processBye(ServerTransaction serverTransaction, Request byeRequest) {
        try {
            console.logEntry();
            Call call = callDispatcher.findCall(serverTransaction.getDialog());
            if (call == null) {
                sipManCallback.fireUnknownMessageReceived(byeRequest);
                return;
            }
            call.setState(Call.DISCONNECTED);
            Response ok = null;
            try {
                ok = sipManCallback.messageFactory.createResponse(Response.OK, byeRequest);
                sipManCallback.attachToTag(ok, call.getDialog());
            } catch (ParseException ex) {
                sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to construct an OK response to a BYE request!", ex));
                return;
            }
            try {
                serverTransaction.sendResponse(ok);
                if (console.isDebugEnabled()) console.debug("sent response " + ok);
            } catch (SipException ex) {
                console.error("Failed to send an OK response to BYE request," + "exception was:\n", ex);
            }
        } finally {
            console.logExit();
        }
    }

    void processAck(ServerTransaction serverTransaction, Request ackRequest) {
        try {
            console.logEntry();
            if (!serverTransaction.getDialog().getFirstTransaction().getRequest().getMethod().equals(Request.INVITE)) {
                console.debug("ignored ack");
                return;
            }
            Call call = callDispatcher.findCall(serverTransaction.getDialog());
            if (call == null) {
                console.debug("didn't find an ack's call, returning");
                return;
            }
            ContentLengthHeader cl = ackRequest.getContentLength();
            if (cl != null && cl.getContentLength() > 0) {
                call.setRemoteSdpDescription(new String(ackRequest.getRawContent()));
            }
            call.setState(Call.CONNECTED);
        } finally {
            console.logExit();
        }
    }

    void processCancel(ServerTransaction serverTransaction, Request cancelRequest) {
        try {
            console.logEntry();
            if (!serverTransaction.getDialog().getFirstTransaction().getRequest().getMethod().equals(Request.INVITE)) {
                console.debug("ignoring request");
                return;
            }
            Call call = callDispatcher.findCall(serverTransaction.getDialog());
            if (call == null) {
                sipManCallback.fireUnknownMessageReceived(cancelRequest);
                return;
            }
            call.setState(Call.DISCONNECTED);
            try {
                Response ok = sipManCallback.messageFactory.createResponse(Response.OK, cancelRequest);
                sipManCallback.attachToTag(ok, call.getDialog());
                serverTransaction.sendResponse(ok);
                if (console.isDebugEnabled()) console.debug("sent ok response: " + ok);
            } catch (ParseException ex) {
                sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to create an OK Response to an CANCEL request.", ex));
            } catch (SipException ex) {
                sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to send an OK Response to an CANCEL request.", ex));
            }
            try {
                Transaction tran = call.getDialog().getFirstTransaction();
                if (!(tran instanceof ServerTransaction)) {
                    sipManCallback.fireCommunicationsError(new CommunicationsException("Received a misplaced CANCEL request!"));
                    return;
                }
                ServerTransaction inviteTran = (ServerTransaction) tran;
                Request invite = call.getDialog().getFirstTransaction().getRequest();
                Response requestTerminated = sipManCallback.messageFactory.createResponse(Response.REQUEST_TERMINATED, invite);
                sipManCallback.attachToTag(requestTerminated, call.getDialog());
                inviteTran.sendResponse(requestTerminated);
                if (console.isDebugEnabled()) console.debug("sent request terminated response: " + requestTerminated);
            } catch (ParseException ex) {
                sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to create a REQUEST_TERMINATED Response to an INVITE request.", ex));
            } catch (SipException ex) {
                sipManCallback.fireCommunicationsError(new CommunicationsException("Failed to send an REQUEST_TERMINATED Response to an INVITE request.", ex));
            }
        } finally {
            console.logExit();
        }
    }

    void processNotFound(ClientTransaction clientTransaction, Response response) {
        try {
            console.logEntry();
            if (!clientTransaction.getDialog().getFirstTransaction().getRequest().getMethod().equals(Request.INVITE)) {
                console.debug("ignoring not found response");
                return;
            }
            Call call = callDispatcher.findCall(clientTransaction.getDialog());
            call.setState(Call.DISCONNECTED);
            sipManCallback.fireCallRejectedRemotely("Server returned a NOT FOUND Response", response);
        } finally {
            console.logExit();
        }
    }

    void processNotImplemented(ClientTransaction clientTransaction, Response response) {
        try {
            console.logEntry();
            if (!clientTransaction.getDialog().getFirstTransaction().getRequest().getMethod().equals(Request.INVITE)) {
                console.debug("ignoring not implemented response");
                return;
            }
            Call call = callDispatcher.findCall(clientTransaction.getDialog());
            call.setState(Call.DISCONNECTED);
            sipManCallback.fireCallRejectedRemotely("Server returned a NOT IMPLEMENTED Response", response);
        } finally {
            console.logExit();
        }
    }

    Call invite(String callee, String sdpContent) throws CommunicationsException {
        try {
            console.logEntry();
            callee = callee.trim();
            String excessiveChars = Utils.getProperty("net.java.sip.communicator.sip.EXCESSIVE_URI_CHARACTERS");
            String isSipphone = Utils.getProperty("net.java.sip.communicator.sipphone.IS_RUNNING_SIPPHONE");
            if (excessiveChars == null && isSipphone != null && isSipphone.equalsIgnoreCase("true")) {
                excessiveChars = "( )-";
                PropertiesDepot.setProperty("net.java.sip.communicator.sip.EXCESSIVE_URI_CHARACTERS", excessiveChars);
                PropertiesDepot.storeProperties();
            }
            if (excessiveChars != null) {
                StringBuffer calleeBuff = new StringBuffer(callee);
                for (int i = 0; i < excessiveChars.length(); i++) {
                    String charToDeleteStr = excessiveChars.substring(i, i + 1);
                    int charIndex = -1;
                    while ((charIndex = calleeBuff.indexOf(charToDeleteStr)) != -1) calleeBuff.delete(charIndex, charIndex + 1);
                }
                callee = calleeBuff.toString();
            }
            String defaultDomainName = Utils.getProperty("net.java.sip.communicator.sip.DEFAULT_DOMAIN_NAME");
            if (defaultDomainName != null && !callee.trim().startsWith("tel:") && callee.indexOf('@') == -1) {
                callee = callee + "@" + defaultDomainName;
            }
            if (callee.toLowerCase().indexOf("sip:") == -1 && callee.indexOf('@') != -1) {
                callee = "sip:" + callee;
            }
            URI requestURI;
            try {
                requestURI = sipManCallback.addressFactory.createURI(callee);
            } catch (ParseException ex) {
                console.error(callee + " is not a legal SIP uri!", ex);
                throw new CommunicationsException(callee + " is not a legal SIP uri!", ex);
            }
            CallIdHeader callIdHeader = sipManCallback.sipProvider.getNewCallId();
            CSeqHeader cSeqHeader;
            try {
                cSeqHeader = sipManCallback.headerFactory.createCSeqHeader(1, Request.INVITE);
            } catch (ParseException ex) {
                console.error(ex, ex);
                throw new CommunicationsException("An unexpected erro occurred while" + "constructing the CSeqHeadder", ex);
            } catch (InvalidArgumentException ex) {
                console.error("An unexpected erro occurred while" + "constructing the CSeqHeadder", ex);
                throw new CommunicationsException("An unexpected erro occurred while" + "constructing the CSeqHeadder", ex);
            }
            FromHeader fromHeader = sipManCallback.getFromHeader();
            Address toAddress = sipManCallback.addressFactory.createAddress(requestURI);
            ToHeader toHeader;
            try {
                toHeader = sipManCallback.headerFactory.createToHeader(toAddress, null);
            } catch (ParseException ex) {
                console.error("Null is not an allowed tag for the to header!", ex);
                throw new CommunicationsException("Null is not an allowed tag for the to header!", ex);
            }
            ArrayList viaHeaders = sipManCallback.getLocalViaHeaders();
            MaxForwardsHeader maxForwards = sipManCallback.getMaxForwardsHeader();
            ContactHeader contactHeader = sipManCallback.getContactHeader();
            Request invite = null;
            try {
                invite = sipManCallback.messageFactory.createRequest(requestURI, Request.INVITE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
            } catch (ParseException ex) {
                console.error("Failed to create invite Request!", ex);
                throw new CommunicationsException("Failed to create invite Request!", ex);
            }
            invite.addHeader(contactHeader);
            ContentTypeHeader contentTypeHeader = null;
            try {
                contentTypeHeader = sipManCallback.headerFactory.createContentTypeHeader("application", "sdp");
            } catch (ParseException ex) {
                console.error("Failed to create a content type header for the INVITE request", ex);
                throw new CommunicationsException("Failed to create a content type header for the INVITE request", ex);
            }
            try {
                invite.setContent(sdpContent, contentTypeHeader);
            } catch (ParseException ex) {
                console.error("Failed to parse sdp data while creating invite request!", ex);
                throw new CommunicationsException("Failed to parse sdp data while creating invite request!", ex);
            }
            ClientTransaction inviteTransaction;
            try {
                inviteTransaction = sipManCallback.sipProvider.getNewClientTransaction(invite);
            } catch (TransactionUnavailableException ex) {
                console.error("Failed to create inviteTransaction.\n" + "This is most probably a network connection error.", ex);
                throw new CommunicationsException("Failed to create inviteTransaction.\n" + "This is most probably a network connection error.", ex);
            }
            try {
                inviteTransaction.sendRequest();
                if (console.isDebugEnabled()) console.debug("sent request: " + invite);
            } catch (SipException ex) {
                console.error("An error occurred while sending invite request", ex);
                throw new CommunicationsException("An error occurred while sending invite request", ex);
            }
            Call call = callDispatcher.createCall(inviteTransaction.getDialog(), invite);
            call.setState(Call.DIALING);
            return call;
        } finally {
            console.logExit();
        }
    }

    void endCall(int callID) throws CommunicationsException {
        try {
            console.logEntry();
            Call call = callDispatcher.getCall(callID);
            if (call == null) {
                console.error("Could not find call with id=" + callID);
                throw new CommunicationsException("Could not find call with id=" + callID);
            }
            Dialog dialog = call.getDialog();
            if (call.getState().equals(Call.CONNECTED) || call.getState().equals(Call.RECONNECTED)) {
                call.setState(Call.DISCONNECTED);
                sayBye(dialog);
            } else if (call.getState().equals(Call.DIALING) || call.getState().equals(Call.RINGING)) {
                if (dialog.getFirstTransaction() != null) {
                    try {
                        sayCancel(dialog);
                    } catch (CommunicationsException ex) {
                        console.error("Could not send the CANCEL request! " + "Remote party won't know we're leaving!", ex);
                        sipManCallback.fireCommunicationsError(new CommunicationsException("Could not send the CANCEL request! " + "Remote party won't know we're leaving!", ex));
                    }
                }
                call.setState(Call.DISCONNECTED);
            } else if (call.getState().equals(Call.ALERTING)) {
                call.setState(Call.DISCONNECTED);
                sayBusyHere(dialog);
            } else if (call.getState().equals(Call.BUSY)) {
                call.setState(Call.DISCONNECTED);
            } else if (call.getState().equals(Call.FAILED)) {
                call.setState(Call.DISCONNECTED);
            } else {
                call.setState(Call.DISCONNECTED);
                console.error("Could not determine call state!");
                throw new CommunicationsException("Could not determine call state!");
            }
        } finally {
            console.logExit();
        }
    }

    private void sayBye(Dialog dialog) throws CommunicationsException {
        try {
            console.logEntry();
            Request request = dialog.getFirstTransaction().getRequest();
            Request bye = null;
            try {
                bye = dialog.createRequest(Request.BYE);
            } catch (SipException ex) {
                console.error("Failed to create bye request!", ex);
                throw new CommunicationsException("Failed to create bye request!", ex);
            }
            ClientTransaction clientTransaction = null;
            try {
                clientTransaction = sipManCallback.sipProvider.getNewClientTransaction(bye);
            } catch (TransactionUnavailableException ex) {
                console.error("Failed to construct a client transaction from the BYE request", ex);
                throw new CommunicationsException("Failed to construct a client transaction from the BYE request", ex);
            }
            try {
                dialog.sendRequest(clientTransaction);
                if (console.isDebugEnabled()) console.debug("sent request: " + bye);
            } catch (SipException ex1) {
                throw new CommunicationsException("Failed to send the BYE request");
            }
        } finally {
            console.logExit();
        }
    }

    private void sayCancel(Dialog dialog) throws CommunicationsException {
        try {
            console.logEntry();
            Request request = dialog.getFirstTransaction().getRequest();
            if (dialog.isServer()) {
                console.error("Cannot cancel a server transaction");
                throw new CommunicationsException("Cannot cancel a server transaction");
            }
            ClientTransaction clientTransaction = (ClientTransaction) dialog.getFirstTransaction();
            try {
                Request cancel = clientTransaction.createCancel();
                ClientTransaction cancelTransaction = sipManCallback.sipProvider.getNewClientTransaction(cancel);
                cancelTransaction.sendRequest();
                if (console.isDebugEnabled()) console.debug("sent request: " + cancel);
            } catch (SipException ex) {
                console.error("Failed to send the CANCEL request", ex);
                throw new CommunicationsException("Failed to send the CANCEL request", ex);
            }
        } finally {
            console.logExit();
        }
    }

    private void sayBusyHere(Dialog dialog) throws CommunicationsException {
        try {
            console.logEntry();
            Request request = dialog.getFirstTransaction().getRequest();
            Response busyHere = null;
            try {
                busyHere = sipManCallback.messageFactory.createResponse(Response.BUSY_HERE, request);
                sipManCallback.attachToTag(busyHere, dialog);
            } catch (ParseException ex) {
                console.error("Failed to create the BUSY_HERE response!", ex);
                throw new CommunicationsException("Failed to create the BUSY_HERE response!", ex);
            }
            if (!dialog.isServer()) {
                console.error("Cannot send BUSY_HERE in a client transaction");
                throw new CommunicationsException("Cannot send BUSY_HERE in a client transaction");
            }
            ServerTransaction serverTransaction = (ServerTransaction) dialog.getFirstTransaction();
            try {
                serverTransaction.sendResponse(busyHere);
                if (console.isDebugEnabled()) console.debug("sent response: " + busyHere);
            } catch (SipException ex) {
                console.error("Failed to send the BUSY_HERE response", ex);
                throw new CommunicationsException("Failed to send the BUSY_HERE response", ex);
            }
        } finally {
            console.logExit();
        }
    }

    public void sayOK(int callID, String sdpContent) throws CommunicationsException {
        try {
            console.logEntry();
            Call call = callDispatcher.getCall(callID);
            if (call == null) {
                console.error("Failed to find call with id=" + callID);
                throw new CommunicationsException("Failed to find call with id=" + callID);
            }
            Dialog dialog = call.getDialog();
            if (dialog == null) {
                call.setState(Call.DISCONNECTED);
                console.error("Failed to extract call's associated dialog! Ending Call!");
                throw new CommunicationsException("Failed to extract call's associated dialog! Ending Call!");
            }
            Transaction transaction = dialog.getFirstTransaction();
            if (transaction == null || !dialog.isServer()) {
                call.setState(Call.DISCONNECTED);
                throw new CommunicationsException("Failed to extract a ServerTransaction " + "from the call's associated dialog!");
            }
            ServerTransaction serverTransaction = (ServerTransaction) transaction;
            Response ok = null;
            try {
                ok = sipManCallback.messageFactory.createResponse(Response.OK, dialog.getFirstTransaction().getRequest());
                sipManCallback.attachToTag(ok, dialog);
            } catch (ParseException ex) {
                call.setState(Call.DISCONNECTED);
                console.error("Failed to construct an OK response to an INVITE request", ex);
                throw new CommunicationsException("Failed to construct an OK response to an INVITE request", ex);
            }
            ContentTypeHeader contentTypeHeader = null;
            try {
                contentTypeHeader = sipManCallback.headerFactory.createContentTypeHeader("application", "sdp");
            } catch (ParseException ex) {
                call.setState(Call.DISCONNECTED);
                console.error("Failed to create a content type header for the OK request", ex);
                throw new CommunicationsException("Failed to create a content type header for the OK request", ex);
            }
            try {
                ok.setContent(sdpContent, contentTypeHeader);
            } catch (NullPointerException ex) {
                call.setState(Call.DISCONNECTED);
                console.error("No sdp data was provided for the ok response to an INVITE request!", ex);
                throw new CommunicationsException("No sdp data was provided for the ok response to an INVITE request!", ex);
            } catch (ParseException ex) {
                call.setState(Call.DISCONNECTED);
                console.error("Failed to parse sdp data while creating invite request!", ex);
                throw new CommunicationsException("Failed to parse sdp data while creating invite request!", ex);
            }
            if (((ToHeader) ok.getHeader(ToHeader.NAME)).getTag() == null) {
                try {
                    ((ToHeader) ok.getHeader(ToHeader.NAME)).setTag(Integer.toString(dialog.hashCode()));
                } catch (ParseException ex) {
                    call.setState(Call.DISCONNECTED);
                    throw new CommunicationsException("Unable to set to tag", ex);
                }
            }
            ContactHeader contactHeader = sipManCallback.getContactHeader();
            ok.addHeader(contactHeader);
            try {
                serverTransaction.sendResponse(ok);
                if (console.isDebugEnabled()) console.debug("sent response " + ok);
            } catch (SipException ex) {
                call.setState(Call.DISCONNECTED);
                console.error("Failed to send an OK response to an INVITE request", ex);
                throw new CommunicationsException("Failed to send an OK response to an INVITE request", ex);
            }
        } finally {
            console.logExit();
        }
    }

    void sayInternalError(int callID) throws CommunicationsException {
        try {
            console.logEntry();
            Call call = callDispatcher.getCall(callID);
            if (call == null) {
                console.error("Failed to find call with id=" + callID);
                throw new CommunicationsException("Failed to find call with id=" + callID);
            }
            Dialog dialog = call.getDialog();
            if (dialog == null) {
                call.setState(Call.DISCONNECTED);
                console.error("Failed to extract call's associated dialog! Ending Call!");
                throw new CommunicationsException("Failed to extract call's associated dialog! Ending Call!");
            }
            Transaction transaction = dialog.getFirstTransaction();
            if (transaction == null || !dialog.isServer()) {
                call.setState(Call.DISCONNECTED);
                console.error("Failed to extract a transaction" + " from the call's associated dialog!");
                throw new CommunicationsException("Failed to extract a transaction from the call's associated dialog!");
            }
            ServerTransaction serverTransaction = (ServerTransaction) transaction;
            Response internalError = null;
            try {
                internalError = sipManCallback.messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, dialog.getFirstTransaction().getRequest());
                sipManCallback.attachToTag(internalError, dialog);
            } catch (ParseException ex) {
                call.setState(Call.DISCONNECTED);
                console.error("Failed to construct an OK response to an INVITE request", ex);
                throw new CommunicationsException("Failed to construct an OK response to an INVITE request", ex);
            }
            ContactHeader contactHeader = sipManCallback.getContactHeader();
            internalError.addHeader(contactHeader);
            try {
                serverTransaction.sendResponse(internalError);
                if (console.isDebugEnabled()) console.debug("sent response: " + internalError);
            } catch (SipException ex) {
                call.setState(Call.DISCONNECTED);
                console.error("Failed to send an OK response to an INVITE request", ex);
                throw new CommunicationsException("Failed to send an OK response to an INVITE request", ex);
            }
        } finally {
            console.logExit();
        }
    }

    CallDispatcher getCallDispatcher() {
        return callDispatcher;
    }

    protected void processReInvite(ServerTransaction serverTransaction, Request request) {
        try {
            console.logEntry();
            console.error("processReInvite is not yet implemented");
        } finally {
            console.logExit();
        }
    }
}
