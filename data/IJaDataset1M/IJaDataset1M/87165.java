package gov.nist.applet.phone.ua;

import java.text.ParseException;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.SipFactory;
import javax.sip.ServerTransaction;
import javax.sip.ClientTransaction;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.sip.message.MessageFactory;
import javax.sip.address.AddressFactory;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.AcceptHeader;
import javax.sip.header.CallInfoHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.ContactHeader;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Iterator;
import gov.nist.applet.phone.media.MediaManager;
import gov.nist.applet.phone.media.messaging.VoicePlayer;
import gov.nist.applet.phone.media.messaging.VoiceRecorder;
import gov.nist.applet.phone.ua.call.*;
import gov.nist.applet.phone.ua.pidf.parser.PresenceTag;
import gov.nist.applet.phone.ua.pidf.parser.XMLpidfParser;
import gov.nist.applet.phone.ua.presence.*;

/**
 * Handler of a major part of sip messages
 * 
 * @author  DERUELLE Jean
 */
public class MessageProcessor {

    /**
	 * The SipFactory instance used to create the SipStack and the Address
	 * Message and Header Factories.
	 */
    SipFactory sipFactory;

    /**
	 * The AddressFactory used to create URLs ans Address objects.
	 */
    AddressFactory addressFactory;

    /**
	 * The HeaderFactory used to create SIP message headers.
	 */
    HeaderFactory headerFactory;

    /**
	 * The Message Factory used to create SIP messages.
	 */
    MessageFactory messageFactory;

    /**
	 * The MessageListener used to handle incoming messages
	 */
    MessageListener messageListener;

    /**
	 * the configuration
	 */
    private Configuration configuration;

    VoicePlayer voicePlayer = null;

    /**
	 * 
	 */
    MessengerManager messengerManager;

    VoiceMessagingTask voiceMessagingTask;

    CallManager callManager;

    class VoiceMessagingTask implements Runnable {

        Thread voiceMessagingThread = null;

        String contactAddress = null;

        protected static final String STARTED = "Started";

        protected static final String STOPPED = "Stopped";

        String state = STOPPED;

        public VoiceMessagingTask(String contactAddress) {
            this.contactAddress = contactAddress;
        }

        public synchronized void start() {
            if (voiceMessagingThread == null) {
                voiceMessagingThread = new Thread(this);
            }
            state = STARTED;
            voiceMessagingThread.start();
        }

        public synchronized void stop() {
            state = STOPPED;
        }

        public void run() {
            while (state.equals(STARTED)) {
                try {
                    byte[] buffer = VoiceRecorder.getInstance().getRecord();
                    if (buffer != null) messengerManager.sendVoiceMessage(contactAddress, buffer);
                    voiceMessagingThread.sleep(Configuration.latency4VoiceMessaging);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
	 * 
	 */
    protected void startVoiceMessagingSchedule(String callee) {
        VoiceRecorder.getInstance().initialize();
        VoiceRecorder.getInstance().start();
        voiceMessagingTask = new VoiceMessagingTask(callee);
        voiceMessagingTask.start();
    }

    /**
	 * 
	 */
    protected void stopVoiceMessagingSchedule() {
        voiceMessagingTask.stop();
        VoiceRecorder.getInstance().stop();
        if (!VoiceRecorder.isClosed()) VoiceRecorder.getInstance().close();
    }

    /** Creates a new instance of MessageProcessor 
	 * @param callListener - the sip Listener used to handle incoming messages
	 */
    public MessageProcessor(MessageListener messageListener) {
        voicePlayer = new VoicePlayer();
        this.messageListener = messageListener;
        this.addressFactory = MessageListener.addressFactory;
        this.sipFactory = MessageListener.sipFactory;
        this.headerFactory = MessageListener.headerFactory;
        this.messageFactory = MessageListener.messageFactory;
        this.configuration = messageListener.getConfiguration();
        this.messengerManager = messageListener.sipMeetingManager;
    }

    /**
	 * Process the INVITE received request
	 * @param serverTransaction - the server transaction associated with the request
	 * @param invite - the request 
	 */
    public void processInvite(ServerTransaction serverTransaction, Request invite) {
        SipURI calleeURI = (SipURI) ((FromHeader) invite.getHeader(FromHeader.NAME)).getAddress().getURI();
        String callee = "sip:" + calleeURI.getUser() + "@" + calleeURI.getHost();
        try {
            CallManager callManager = messageListener.sipMeetingManager.getCallManager();
            if (callManager.isAlreadyInAudioCall()) {
                Response busyHere = (Response) MessageListener.messageFactory.createResponse(Response.BUSY_HERE, invite);
                if (messageListener.getConfiguration().httpBusy != null) {
                    CallInfoHeader callInfoHeader = MessageListener.headerFactory.createCallInfoHeader(MessageListener.addressFactory.createURI(messageListener.getConfiguration().httpBusy));
                    busyHere.addHeader(callInfoHeader);
                }
                System.out.println("send response : " + busyHere.toString());
                serverTransaction.sendResponse(busyHere);
            } else {
                Response ringing = (Response) MessageListener.messageFactory.createResponse(Response.RINGING, invite);
                serverTransaction.sendResponse(ringing);
                AudioCall audioCall = new AudioCall(messageListener);
                audioCall.setCallee(callee);
                audioCall.setDialog(serverTransaction.getDialog());
                audioCall.setStatus(AudioCall.INCOMING_CALL);
                System.out.println("Audio Call created : " + audioCall.getDialog().getDialogId());
                callManager.addAudioCall(audioCall);
                boolean voiceMessaging = false;
                ContentTypeHeader contentTypeHeader = (ContentTypeHeader) invite.getHeader(ContentTypeHeader.NAME);
                if (contentTypeHeader == null) {
                    ListIterator acceptHeaderList = invite.getHeaders(AcceptHeader.NAME);
                    while (acceptHeaderList.hasNext() && !voiceMessaging) {
                        AcceptHeader acceptHeader = (AcceptHeader) acceptHeaderList.next();
                        if (acceptHeader.getContentSubType().equalsIgnoreCase("gsm") || acceptHeader.getContentSubType().equalsIgnoreCase("x-gsm")) voiceMessaging = true;
                    }
                }
                audioCall.setVoiceMesaging(voiceMessaging);
                messageListener.sipMeetingManager.notifyObserversNewCallStatus(audioCall);
            }
        } catch (SipException se) {
            se.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    /**
	 * Process the BYE received request
	 * @param serverTransaction - the server transaction associated with the request
	 * @param bye - the bye request 
	 */
    public void processBye(ServerTransaction serverTransaction, Request bye) {
        CallManager callManager = messageListener.sipMeetingManager.getCallManager();
        Call call = callManager.findCall(serverTransaction.getDialog().getDialogId());
        if (call == null) return;
        try {
            Response ok = (Response) MessageListener.messageFactory.createResponse(Response.OK, bye);
            serverTransaction.sendResponse(ok);
            if (call instanceof AudioCall) {
                AudioCall audioCall = (AudioCall) call;
                audioCall.setStatus(AudioCall.NOT_IN_A_CALL);
                messageListener.sipMeetingManager.notifyObserversNewCallStatus(audioCall);
                if (audioCall.getVoiceMessaging()) {
                    stopVoiceMessagingSchedule();
                } else {
                    audioCall.getMediaManager().stopMediaSession();
                }
                System.out.println("Audio Call removed : " + call.getDialog().getDialogId());
                callManager.removeAudioCall(audioCall);
            } else {
                IMCall imCall = (IMCall) call;
                System.out.println("IM Call removed : " + call.getDialog().getDialogId());
                callManager.removeIMCall(imCall);
            }
        } catch (SipException se) {
            se.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    /**
	 * Process the ACK received request
	 * @param serverTransaction - the server transaction associated with the request
	 * @param ack - the ack request 
	 */
    public void processAck(ServerTransaction serverTransaction, Request ack) {
        CallManager callManager = messageListener.sipMeetingManager.getCallManager();
        SipURI calleeURI = (SipURI) ((FromHeader) ack.getHeader(FromHeader.NAME)).getAddress().getURI();
        String callee = "sip:" + calleeURI.getUser() + "@" + calleeURI.getHost();
        AudioCall call = callManager.findAudioCall(callee);
        if (call != null) {
            if (!call.getVoiceMessaging()) call.getMediaManager().startMediaSession(false); else startVoiceMessagingSchedule(callee);
            call.setStatus(AudioCall.IN_A_CALL);
            messageListener.sipMeetingManager.notifyObserversNewCallStatus(call);
        }
    }

    /**
	 * Process the CANCEL received request
	 * @param serverTransaction - the server transaction associated with the request
	 * @param cancel - the cancel request 
	 */
    public void processCancel(ServerTransaction serverTransaction, Request cancel) {
        CallManager callManager = messageListener.sipMeetingManager.getCallManager();
        SipURI calleeURI = (SipURI) ((FromHeader) cancel.getHeader(FromHeader.NAME)).getAddress().getURI();
        String callee = "sip:" + calleeURI.getUser() + "@" + calleeURI.getHost().trim().toLowerCase();
        AudioCall call = callManager.findAudioCall(callee);
        try {
            Response ok = (Response) MessageListener.messageFactory.createResponse(Response.OK, cancel);
            serverTransaction.sendResponse(ok);
            call.setStatus(AudioCall.CANCEL);
            messageListener.sipMeetingManager.notifyObserversNewCallStatus(call);
            System.out.println("Audio Call removed : " + call.getDialog().getDialogId());
            callManager.removeAudioCall(call);
        } catch (SipException se) {
            se.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    /**
	 * Process the MESSAGE received request. MESSAGE may be handled statefully or
	 * statelessly.
	 * @param serverTransaction - the server transaction associated with the request
	 * @param message - the request 
	 */
    public void processMessage(ServerTransaction serverTrans, Request message) {
        ServerTransaction serverTransaction = null;
        try {
            serverTransaction = (serverTrans == null ? messageListener.sipProvider.getNewServerTransaction(message) : serverTrans);
        } catch (javax.sip.TransactionAlreadyExistsException ex) {
            ex.printStackTrace();
            return;
        } catch (javax.sip.TransactionUnavailableException ex1) {
            ex1.printStackTrace();
            return;
        }
        try {
            String sender = null;
            Address address = ((FromHeader) message.getHeader(FromHeader.NAME)).getAddress();
            if (address.getURI().isSipURI()) {
                SipURI sipURI = ((SipURI) address.getURI());
                String host = sipURI.getHost();
                String user = sipURI.getUser();
                sender = user + "@" + host;
            }
            ContentTypeHeader contentTypeHeader = (ContentTypeHeader) message.getHeader(ContentTypeHeader.NAME);
            if (contentTypeHeader == null || contentTypeHeader.getContentType() == null || contentTypeHeader.getContentSubType() == null) return;
            String subType = contentTypeHeader.getContentSubType();
            if (contentTypeHeader.getContentType().equals("audio")) {
                if (contentTypeHeader.getContentSubType().equals("x-gsm") || contentTypeHeader.getContentSubType().equals("gsm")) {
                    Response ok = (Response) MessageListener.messageFactory.createResponse(Response.OK, message);
                    ToHeader toHeader = (ToHeader) message.getHeader(ToHeader.NAME);
                    if (toHeader.getTag() == null) toHeader.setTag(new Integer((int) (Math.random() * 10000)).toString());
                    serverTransaction.sendResponse(ok);
                    byte[] voiceMessage = message.getRawContent();
                    voicePlayer.initialize(voiceMessage);
                    voicePlayer.play();
                    message.removeContent();
                } else {
                    System.out.println("Cannot handle this codec " + contentTypeHeader.getContentSubType());
                }
            } else if (contentTypeHeader.getContentType().equals("text") && contentTypeHeader.getContentSubType().equals("plain")) {
                Response ok = (Response) MessageListener.messageFactory.createResponse(Response.OK, message);
                ToHeader toHeader = (ToHeader) message.getHeader(ToHeader.NAME);
                if (toHeader.getTag() == null) {
                    toHeader.setTag(new Integer((int) (Math.random() * 10000)).toString());
                    IMCall imcall = this.messengerManager.callManager.findIMCall(sender);
                    if (imcall == null) {
                        imcall = new IMCall(sender);
                        imcall.setDialog(serverTransaction.getDialog());
                        this.messengerManager.callManager.addIMCall(imcall);
                    } else imcall.setDialog(serverTransaction.getDialog());
                }
                serverTransaction.sendResponse(ok);
                String content = new String(message.getRawContent());
                FromHeader fromHeader = (FromHeader) message.getHeader(FromHeader.NAME);
                messageListener.sipMeetingManager.notifyObserversIMReceived(content, sender);
            } else {
                Response notok = (Response) MessageListener.messageFactory.createResponse(Response.NOT_ACCEPTABLE, message);
                ToHeader toHeader = (ToHeader) message.getHeader(ToHeader.NAME);
                serverTransaction.sendResponse(notok);
                System.out.println("Cannot handle this content Type " + contentTypeHeader);
            }
            message.removeContent();
        } catch (SipException se) {
            se.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    /**
	 * Process the SUBSCRIBE received request
	 * @param serverTransaction - the server transaction associated with the request
	 * @param subscribe - the request 
	 */
    public void processSubscribe(ServerTransaction serverTransaction, Request subscribe) {
        Address address = ((FromHeader) subscribe.getHeader(FromHeader.NAME)).getAddress();
        String sender = null;
        if (address.getURI().isSipURI()) {
            SipURI sipURI = ((SipURI) address.getURI());
            String host = sipURI.getHost();
            String user = sipURI.getUser();
            sender = user + "@" + host;
        }
        Subscriber subscriber = new Subscriber(sender);
        subscriber.setDialog(serverTransaction.getDialog());
        messageListener.sipMeetingManager.notifySubscribe(subscriber);
    }

    /**
	 * Process the NOTIFY received request
	 * @param serverTransaction - the server transaction associated with the request
	 * @param notify - the request 
	 */
    public void processNotify(ServerTransaction serverTransaction, Request notify) {
        try {
            Response ok = (Response) MessageListener.messageFactory.createResponse(Response.OK, notify);
            ToHeader toHeader = (ToHeader) notify.getHeader(ToHeader.NAME);
            if (toHeader.getTag() == null) toHeader.setTag(new Integer((int) (Math.random() * 10000)).toString());
            serverTransaction.sendResponse(ok);
            Address address = ((FromHeader) notify.getHeader(FromHeader.NAME)).getAddress();
            String sender = null;
            if (address.getURI().isSipURI()) {
                SipURI sipURI = ((SipURI) address.getURI());
                String host = sipURI.getHost();
                String user = sipURI.getUser();
                sender = user + "@" + host;
            }
            ContentTypeHeader contentTypeHeader = (ContentTypeHeader) notify.getHeader(ContentTypeHeader.NAME);
            if (contentTypeHeader != null) {
                String xmlType = contentTypeHeader.getContentSubType();
                if (xmlType.equals("xpidf+xml")) {
                    String pidfContent = new String(notify.getRawContent());
                    int endDocIndex = pidfContent.indexOf("<presence>");
                    pidfContent = pidfContent.substring(endDocIndex);
                    XMLpidfParser pidfParser = new XMLpidfParser();
                    pidfParser.parsePidfString(pidfContent);
                    PresenceTag presenceTag = pidfParser.getPresenceTag();
                    presenceTag.setAddress(sender);
                    messageListener.sipMeetingManager.notifyPresence(presenceTag);
                }
            }
        } catch (SipException se) {
            se.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

    /**
	 * After a
	 * @param clientTransaction
	 * @param response
	 */
    public void processRequestTerminated(ClientTransaction clientTransaction, Response response) {
    }

    /**
	 * Process the Not found received response
	 * @param clientTransaction - the client transaction associated with the response
	 * @param notFound - the not found response
	 */
    public void processNotFound(ClientTransaction clientTransaction, Response notFound) {
    }

    /**
	 * Process the Not Implemented received response
	 * @param clientTransaction - the client transaction associated with the response
	 * @param notImplemented - the not found response
	 */
    public void processNotImplemented(ClientTransaction clientTransaction, Response notImplemented) {
    }

    /**
	 * Process the Trying received response
	 * @param clientTransaction - the client transaction associated with the response
	 * @param trying - the trying response
	 */
    public void processTrying(ClientTransaction clientTransaction, Response trying) {
        CallManager callManager = messageListener.sipMeetingManager.getCallManager();
        SipURI calleeURI = (SipURI) ((ToHeader) trying.getHeader(ToHeader.NAME)).getAddress().getURI();
        String callee = "sip:" + calleeURI.getUser() + "@" + calleeURI.getHost();
        AudioCall call = callManager.findAudioCall(callee);
        if (call != null) {
            call.setStatus(AudioCall.TRYING);
            messageListener.sipMeetingManager.notifyObserversNewCallStatus(call);
        }
    }

    /**
	 * Process the Ringing received response
	 * @param clientTransaction - the client transaction associated with the response
	 * @param ringing - the ringing response
	 */
    public void processRinging(ClientTransaction clientTransaction, Response ringing) {
        CallManager callManager = messageListener.sipMeetingManager.getCallManager();
        SipURI calleeURI = (SipURI) ((ToHeader) ringing.getHeader(ToHeader.NAME)).getAddress().getURI();
        String callee = "sip:" + calleeURI.getUser() + "@" + calleeURI.getHost();
        AudioCall call = callManager.findAudioCall(callee);
        call.setStatus(AudioCall.RINGING);
        messageListener.sipMeetingManager.notifyObserversNewCallStatus(call);
    }

    /**
	 * Process the OK received response for a REGISTER 
	 * @param clientTransaction - the client transaction associated with the response
	 * @param registerOK - the OK received response for a REGISTER 
	 */
    public void processRegisterOK(ClientTransaction clientTransaction, Response registerOK) {
        FromHeader fromHeader = ((FromHeader) registerOK.getHeader(FromHeader.NAME));
        Address address = fromHeader.getAddress();
        ExpiresHeader expires = registerOK.getExpires();
        if (expires != null && expires.getExpires() == 0) {
            if (messageListener.sipMeetingManager.reRegisterFlag) messageListener.sipMeetingManager.register(); else messageListener.sipMeetingManager.setRegisterStatus(RegisterStatus.NOT_REGISTERED);
        } else {
            messageListener.sipMeetingManager.setRegisterStatus(RegisterStatus.REGISTERED);
        }
    }

    /**
	 * Process the OK received response for a BYE
	 * @param clientTransaction - the client transaction associated with the response
	 * @param byeOK - the OK received response for a BYE
	 */
    public void processByeOK(ClientTransaction clientTransaction, Response byeOK) {
        CallManager callManager = messageListener.sipMeetingManager.getCallManager();
        Call call = callManager.findCall(clientTransaction.getDialog().getDialogId());
        if (call instanceof AudioCall) {
            AudioCall audioCall = (AudioCall) call;
            audioCall.setStatus(AudioCall.NOT_IN_A_CALL);
            messageListener.sipMeetingManager.notifyObserversNewCallStatus(audioCall);
            System.out.println("Audio Call removed : " + call.getDialog().getDialogId());
            callManager.removeAudioCall(audioCall);
        } else {
            IMCall imCall = (IMCall) call;
            System.out.println("IM Call removed : " + call.getDialog().getDialogId());
            callManager.removeIMCall(imCall);
        }
    }

    /**
	 * Process the OK received response for a CANCEL
	 * @param clientTransaction - the client transaction associated with the response
	 * @param cancelOK - the OK received response for a CANCEL
	 */
    public void processCancelOK(ClientTransaction clientTransaction, Response cancelOK) {
        CallManager callManager = messageListener.sipMeetingManager.getCallManager();
        SipURI calleeURI = (SipURI) ((ToHeader) cancelOK.getHeader(ToHeader.NAME)).getAddress().getURI();
        String callee = "sip:" + calleeURI.getUser() + "@" + calleeURI.getHost().trim().toLowerCase();
        AudioCall call = callManager.findAudioCall(callee);
        call.setStatus(AudioCall.NOT_IN_A_CALL);
        messageListener.sipMeetingManager.notifyObserversNewCallStatus(call);
        callManager.removeAudioCall(call);
    }

    /**
	 * Process the OK received response for a MESSAGE
	 * @param clientTransaction - the client transaction associated with the response
	 * @param cancelOK - the OK received response for a MESSAGE
	 */
    public void processMessageOK(ClientTransaction clientTransaction, Response messageOK) {
    }

    /**
	 * Process the OK received response for a SUBSCRIBE
	 * @param clientTransaction - the client transaction associated with the response
	 * @param cancelOK - the OK received response for a MESSAGE
	 */
    public void processSubscribeOK(ClientTransaction clientTransaction, Response subscribeOK) {
        messageListener.sipMeetingManager.presenceAllowed = true;
        Address address = ((FromHeader) subscribeOK.getHeader(FromHeader.NAME)).getAddress();
        String sender = null;
        if (address.getURI().isSipURI()) {
            SipURI sipURI = ((SipURI) address.getURI());
            String host = sipURI.getHost();
            String user = sipURI.getUser();
            sender = user + "@" + host;
        }
    }

    /**
	 * Process the OK received response for a SUBSCRIBE
	 * @param clientTransaction - the client transaction associated with the response
	 * @param cancelOK - the OK received response for a MESSAGE
	 */
    public void processSubscribeAccepted(ClientTransaction clientTransaction, Response subscribeAccepted) {
        messageListener.sipMeetingManager.presenceAllowed = true;
    }

    /**
	 * Process the OK received response for a INVITE
	 * @param clientTransaction - the client transaction associated with the response
	 * @param inviteOK - the OK received response for a INVITE
	 */
    public void processInviteOK(ClientTransaction clientTransaction, Response inviteOK) {
        CallManager callManager = messageListener.sipMeetingManager.getCallManager();
        SipURI calleeURI = (SipURI) ((ToHeader) inviteOK.getHeader(ToHeader.NAME)).getAddress().getURI();
        String callee = "sip:" + calleeURI.getUser() + "@" + calleeURI.getHost();
        AudioCall call = callManager.findAudioCall(callee);
        try {
            Request ack = (Request) clientTransaction.getDialog().createRequest(Request.ACK);
            System.out.println("Sending ACK : \n" + ack.toString());
            try {
                clientTransaction.getDialog().sendAck(ack);
            } catch (SipException ex) {
                System.out.println("Could not send out the ACK request! ");
                ex.printStackTrace();
            }
        } catch (SipException ex) {
            ex.printStackTrace();
        }
        ContentTypeHeader contentTypeHeader = (ContentTypeHeader) inviteOK.getHeader(ContentTypeHeader.NAME);
        if (contentTypeHeader != null) {
            String type = contentTypeHeader.getContentType();
            String subType = contentTypeHeader.getContentSubType();
            messageListener.sipMeetingManager.notifyObserversNewCallStatus(call);
            if (type.equals("application") && subType.equals("sdp")) {
                MediaManager mediaManager = call.getMediaManager();
                call.setVoiceMesaging(false);
                mediaManager.prepareMediaSession(new String(inviteOK.getRawContent()));
                mediaManager.startMediaSession(true);
            }
        } else {
            ListIterator it = inviteOK.getHeaders(AcceptHeader.NAME);
            while (it.hasNext()) {
                AcceptHeader next = (AcceptHeader) it.next();
                if (next.getContentType().equals("audio") && (next.getContentSubType().equals("gsm") || next.getContentSubType().equals("x-gsm"))) {
                    call.setVoiceMesaging(true);
                    startVoiceMessagingSchedule(callee);
                } else if (next.getContentType().equals("text") && next.getContentSubType().equals("plain")) {
                }
            }
        }
        call.setStatus(AudioCall.IN_A_CALL);
        messageListener.sipMeetingManager.notifyObserversNewCallStatus(call);
    }

    /**
	 * Process the Busy here response
	 * @param clientTransaction - the client transaction associated with the response
	 * @param busyhere - the Busy here response
	 */
    public void processBusyHere(ClientTransaction clientTransaction, Response busyHere) {
        CallManager callManager = messageListener.sipMeetingManager.getCallManager();
        SipURI calleeURI = (SipURI) ((ToHeader) busyHere.getHeader(ToHeader.NAME)).getAddress().getURI();
        String callee = "sip:" + calleeURI.getUser() + "@" + calleeURI.getHost();
        Call call = callManager.findCall(clientTransaction.getDialog().getDialogId());
        if (call instanceof AudioCall) {
            CallInfoHeader callInfoHeader = (CallInfoHeader) busyHere.getHeader(CallInfoHeader.NAME);
            URI uri = callInfoHeader.getInfo();
            ((AudioCall) call).setURL(uri);
            call.setStatus(AudioCall.BUSY);
            messageListener.sipMeetingManager.notifyObserversNewCallStatus(call);
            System.out.println("Audio Call removed : " + call.getDialog().getDialogId());
            callManager.removeAudioCall((AudioCall) call);
        }
    }

    /**
	 * Process the temporary Unavailable response
	 * @param clientTransaction - the client transaction associated with the response
	 * @param temporaryUnavailable - the temporary Unavailable response
	 */
    public void processUnavailable(ClientTransaction clientTransaction, Response temporaryUnavailable) {
        CallManager callManager = messageListener.sipMeetingManager.getCallManager();
        Call call = callManager.findCall(clientTransaction.getDialog().getDialogId());
        if (call instanceof AudioCall) {
            AudioCall audioCall = (AudioCall) call;
            audioCall.setStatus(Call.TEMPORARY_UNAVAILABLE);
            messageListener.sipMeetingManager.notifyObserversNewCallStatus(audioCall);
            System.out.println("Audio Call removed : " + call.getDialog().getDialogId());
            callManager.removeAudioCall(audioCall);
        } else if (call instanceof IMCall) {
            IMCall imCall = (IMCall) call;
            imCall.setStatus(Call.TEMPORARY_UNAVAILABLE);
            messageListener.sipMeetingManager.notifyObserversNewCallStatus(imCall);
            System.out.println("IM Call removed : " + call.getDialog().getDialogId());
            callManager.removeIMCall(imCall);
        }
    }

    /**
	 * Process the 407 - Proxy Authentication Required
	 * @param clientTransaction - the client transaction associated with the response
	 * @param proxyAuthenticationRequired - the temporary Unavailable response
	 */
    public void processProxyAuthenticationRequired(ClientTransaction clientTransaction, Response proxyAuthenticationRequired) {
        messageListener.sipMeetingManager.setRegisterStatus(RegisterStatus.PROXY_AUTHENTICATION_REQUIRED);
    }

    /**
	 * Process the 405 - Method Not Allowed
	 * @param clientTransaction - the client transaction associated with the response
	 * @param proxyAuthenticationRequired - the temporary Unavailable response
	 */
    public void processMethodNotAllowed(ClientTransaction clientTransaction, Response methodNotAllowed) {
        messageListener.sipMeetingManager.presenceAllowed = false;
    }

    /**
	 * Process the timed out MESSAGE
	 * @param message - the timedout request 
	 */
    public void processTimedOutMessage(Request message) {
        ToHeader toHeader = (ToHeader) (message.getHeader(ToHeader.NAME));
        Address address = toHeader.getAddress();
        if (address.getURI().isSipURI()) {
            SipURI toURI = (SipURI) address.getURI();
            messageListener.sipMeetingManager.notifyObserversIMReceived(new String(message.getRawContent()) + " has not been delivered successfully", toURI.getUser() + "@" + toURI.getHost());
        }
        SipURI toUri = ((SipURI) ((ToHeader) message.getHeader(ToHeader.NAME)).getAddress().getURI());
        String id = toUri.getUser() + "@" + toUri.getHost();
        this.callManager.removeIMCall(id);
    }

    /**
	 * Process the timed out REGISTER
	 * @param message - the timedout request 
	 */
    public void processTimedOutRegister(Request register) {
        messageListener.sipMeetingManager.setRegisterStatus(RegisterStatus.NOT_REGISTERED);
    }

    /**
	 * Process the timed out REGISTER
	 * @param message - the timedout request 
	 */
    public void processTimedOutInvite(Request invite) {
        messageListener.sipMeetingManager.setRegisterStatus(AudioCall.NOT_IN_A_CALL);
    }

    /**
	 * Process the Timeout received request
	 * @param transaction - the transaction associated with the request
	 * @param timeout - the timeout request
	 */
    public void processTimeout(Transaction transaction, Request timeout) {
    }
}
