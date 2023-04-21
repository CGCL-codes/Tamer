package org.mobicents.slee.sipevent.server.subscription.eventlist;

import gov.nist.javax.sip.Utils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

/**
 * 
 * @author Eduardo Martins
 * 
 */
public class Subscriber implements SipListener {

    private final String eventPackage = "presence";

    private static final Timer timer = new Timer();

    private SipProvider sipProvider;

    private AddressFactory addressFactory;

    private MessageFactory messageFactory;

    private HeaderFactory headerFactory;

    private SipStack sipStack;

    private ContactHeader contactHeader;

    private String notifierPort;

    private String transport;

    private ListeningPoint listeningPoint;

    private Dialog dialog;

    private int expires = 300;

    private final String subscriber;

    private final String notifier;

    private final int listeningPort;

    private final ResourceListServerSipTest test;

    private enum StateMachine {

        starting, started, stopping
    }

    private StateMachine stateMachine;

    public Subscriber(String subscriber, String notifier, int listeningPort, ResourceListServerSipTest test) {
        this.subscriber = subscriber;
        this.notifier = notifier;
        this.listeningPort = listeningPort;
        this.test = test;
    }

    public void subscribe() {
        this.stateMachine = StateMachine.starting;
        try {
            initSipStack();
            sendInitialRequest();
        } catch (Exception e) {
            e.printStackTrace();
            test.failTest(e.getMessage());
        }
    }

    private void initSipStack() throws PeerUnavailableException, TransportNotSupportedException, InvalidArgumentException, ObjectInUseException, TooManyListenersException {
        notifierPort = "5060";
        transport = "udp";
        SipFactory sipFactory = SipFactory.getInstance();
        sipFactory.setPathName("gov.nist");
        Properties properties = new Properties();
        properties.setProperty("javax.sip.USE_ROUTER_FOR_ALL_URIS", "false");
        properties.setProperty("javax.sip.STACK_NAME", subscriber);
        properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "subscriber_" + listeningPort + "_debug.txt");
        properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "subscriber_" + listeningPort + "_log.txt");
        properties.setProperty("javax.sip.FORKABLE_EVENTS", "foo");
        properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "0");
        sipStack = sipFactory.createSipStack(properties);
        headerFactory = sipFactory.createHeaderFactory();
        addressFactory = sipFactory.createAddressFactory();
        messageFactory = sipFactory.createMessageFactory();
        this.listeningPoint = sipStack.createListeningPoint("localhost", listeningPort, transport);
        sipProvider = sipStack.createSipProvider(listeningPoint);
        sipProvider.addSipListener(this);
    }

    private void sendInitialRequest() throws ParseException, InvalidArgumentException, SipException {
        Address fromAddress = addressFactory.createAddress(subscriber);
        FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, Utils.getInstance().generateTag());
        Address toAddress = addressFactory.createAddress(notifier);
        ToHeader toHeader = headerFactory.createToHeader(toAddress, null);
        ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
        int port = sipProvider.getListeningPoint(transport).getPort();
        ViaHeader viaHeader = headerFactory.createViaHeader("localhost", port, transport, null);
        viaHeaders.add(viaHeader);
        CallIdHeader callIdHeader = sipProvider.getNewCallId();
        CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(1L, Request.SUBSCRIBE);
        MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);
        Request request = messageFactory.createRequest(toAddress.getURI(), Request.SUBSCRIBE, callIdHeader, cSeqHeader, fromHeader, toHeader, viaHeaders, maxForwards);
        SipURI contactURI = (SipURI) addressFactory.createURI(subscriber + ":" + listeningPoint.getIPAddress());
        contactURI.setTransportParam(transport);
        contactURI.setPort(port);
        Address contactAddress = addressFactory.createAddress(contactURI);
        contactHeader = headerFactory.createContactHeader(contactAddress);
        request.addHeader(contactHeader);
        request.addHeader(headerFactory.createRouteHeader(addressFactory.createAddress("<sip:localhost:" + notifierPort + ";transport=" + transport + ";lr>")));
        request.addHeader(headerFactory.createEventHeader(this.eventPackage));
        request.addHeader(headerFactory.createExpiresHeader(this.expires));
        request.addHeader(headerFactory.createAcceptHeader("application", "pidf+xml"));
        request.addHeader(headerFactory.createAcceptHeader("application", "rlmi+xml"));
        request.addHeader(headerFactory.createAcceptHeader("multipart", "related"));
        request.addHeader(headerFactory.createSupportedHeader("eventlist"));
        ClientTransaction clientTransaction = sipProvider.getNewClientTransaction(request);
        this.dialog = clientTransaction.getDialog();
        clientTransaction.sendRequest();
    }

    private void refresh() throws SipException, InvalidArgumentException, ParseException {
        Request request = dialog.createRequest(Request.SUBSCRIBE);
        request.addHeader(headerFactory.createMaxForwardsHeader(70));
        request.addHeader(headerFactory.createEventHeader(this.eventPackage));
        request.addHeader(headerFactory.createExpiresHeader(this.expires));
        request.addHeader(headerFactory.createAcceptHeader("application", "pidf+xml"));
        request.addHeader(headerFactory.createAcceptHeader("application", "rlmi+xml"));
        request.addHeader(headerFactory.createAcceptHeader("multipart", "related"));
        request.addHeader(headerFactory.createSupportedHeader("eventlist"));
        request.setRequestURI(addressFactory.createURI(notifier));
        sipProvider.getNewClientTransaction(request).sendRequest();
    }

    public void unsubscribe() throws SipException, InvalidArgumentException, ParseException {
        Request request = dialog.createRequest(Request.SUBSCRIBE);
        request.addHeader(headerFactory.createMaxForwardsHeader(70));
        request.addHeader(headerFactory.createEventHeader(this.eventPackage));
        request.addHeader(headerFactory.createExpiresHeader(0));
        request.addHeader(headerFactory.createAcceptHeader("application", "pidf+xml"));
        request.addHeader(headerFactory.createAcceptHeader("application", "rlmi+xml"));
        request.addHeader(headerFactory.createAcceptHeader("multipart", "related"));
        request.addHeader(headerFactory.createSupportedHeader("eventlist"));
        request.setRequestURI(addressFactory.createURI(notifier));
        sipProvider.getNewClientTransaction(request).sendRequest();
    }

    private void setRefreshTimer(int expires) {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                try {
                    this.cancel();
                    refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                    test.failTest(e.getMessage());
                }
            }
        };
        timer.schedule(task, (expires - 1) * 1000);
    }

    public void processDialogTerminated(DialogTerminatedEvent arg0) {
    }

    public void processIOException(IOExceptionEvent arg0) {
        test.failTest("processIOException: arg0 = " + arg0);
    }

    public void processRequest(RequestEvent arg0) {
        Request request = arg0.getRequest();
        System.out.println("processRequest: request = " + request);
        try {
            arg0.getServerTransaction().sendResponse(messageFactory.createResponse(Response.OK, request));
        } catch (Exception e) {
            e.printStackTrace();
            test.failTest(e.getMessage());
        }
    }

    public void processResponse(ResponseEvent arg0) {
        Response response = arg0.getResponse();
        System.out.println("Subscriber " + subscriber + " rcvd response:\n" + response);
        switch(this.stateMachine) {
            case starting:
                if (response.getStatusCode() > 299) {
                    test.failTest("unexpected response when " + this.stateMachine + " subscriber " + subscriber + ", can't proceeed");
                } else if (response.getStatusCode() > 199) {
                    if (this.dialog == null) {
                        this.dialog = arg0.getDialog();
                    }
                    ExpiresHeader expiresHeader = response.getExpires();
                    if (expiresHeader != null) {
                        this.expires = expiresHeader.getExpires();
                        setRefreshTimer(this.expires);
                    }
                }
                break;
            case started:
                if (response.getStatusCode() > 299) {
                    test.failTest("unexpected response when " + this.stateMachine + " subscriber " + subscriber + ", can't proceeed");
                } else if (response.getStatusCode() > 199) {
                    ExpiresHeader expiresHeader = response.getExpires();
                    if (expiresHeader != null) {
                        this.expires = expiresHeader.getExpires();
                        setRefreshTimer(this.expires);
                    }
                }
                break;
            case stopping:
                if (response.getStatusCode() > 299) {
                    test.failTest("unexpected response when " + this.stateMachine + " subscriber " + subscriber + ", can't proceeed");
                } else if (response.getStatusCode() > 199) {
                } else {
                    test.failTest("unexpected response when " + this.stateMachine + " subscriber " + subscriber + ", can't proceeed");
                }
                break;
            default:
                test.failTest("invalid state " + this.stateMachine + " on subscriber " + subscriber);
        }
    }

    public void processTimeout(TimeoutEvent arg0) {
        test.failTest("processTimeout: arg0 = " + arg0);
    }

    public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
    }
}
