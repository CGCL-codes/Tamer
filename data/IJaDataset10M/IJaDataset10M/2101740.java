package gov.nist.siplite;

import gov.nist.siplite.stack.*;
import gov.nist.siplite.message.*;
import gov.nist.siplite.header.*;
import gov.nist.siplite.address.*;
import gov.nist.core.*;
import javax.microedition.sip.SipException;
import java.io.IOException;
import com.sun.j2me.log.Logging;
import com.sun.j2me.log.LogChannels;

/**
 * A helper class that runs a registration call flow.
 */
public class RegistrationHelper implements SipListener, Runnable {

    /** Current SIP stack context. */
    private SipStack sipStack;

    /** Current SIP provider. */
    private SipProvider sipProvider;

    /** Message factory. */
    private MessageFactory messageFactory;

    /** HJeader factory. */
    private HeaderFactory headerFactory;

    /** Current user name. */
    private String userName;

    /** Current user address. */
    private String userAddress;

    /** Current event listening filter. */
    private ListeningPoint lp;

    /** Local thread for asynchoronous processing. */
    private Thread myThread;

    /** Flag indicating successful registraion. */
    protected boolean successfulRegistration;

    /** Credentials listener for authentication requests. */
    private AuthenticationListener authenticationListener;

    /**
     * Count of authorization requests (RFC 2617, 3.2.2).
     */
    private int countReoriginateRequest = 1;

    /**
     * Constructor.
     * @param myStack current SIP stack context
     * @param userName current user name
     * @param userAddress current user address
     * @param lp listening point event filter
     */
    public RegistrationHelper(SipStack myStack, String userName, String userAddress, ListeningPoint lp) {
        this.sipStack = myStack;
        this.userName = userName;
        this.userAddress = userAddress;
        this.messageFactory = new MessageFactory();
        this.headerFactory = new HeaderFactory();
        this.lp = lp;
        myThread = new Thread(this);
    }

    /**
     * Performs the session registration.
     */
    public void doRegister() {
        myThread.start();
        synchronized (this) {
            try {
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "WAIT");
                }
                this.wait();
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "WAKE UP");
                }
            } catch (InterruptedException ex) {
                return;
            }
        }
    }

    /**
     * Starts asynchronous processing int separate thread.
     */
    public void run() {
        try {
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "starting registration thread");
            }
            sipStack.stackInitialized = false;
            Hop hop = sipStack.getRouter().getOutboundProxy();
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "got listening point");
            }
            sipProvider = lp.getProvider();
            StringBuffer requestLine = new StringBuffer("REGISTER sip:").append(sipStack.getNextHop().getHost()).append(":").append(sipStack.getNextHop().getPort()).append(";transport=" + hop.getTransport()).append(" SIP/2.0\r\n");
            StringBuffer from = new StringBuffer("From: <sip:").append(userName).append("@").append(userAddress).append(">;tag=1234\r\n");
            StringBuffer to = new StringBuffer("To: <sip:").append(userName).append("@").append(userAddress).append(">\r\n");
            String via = lp.messageProcessor[0].getViaHeader().toString();
            int port = lp.getPort();
            StringBuffer contact = new StringBuffer("Contact: <sip:" + userName + "@" + sipStack.getIPAddress() + ":" + port + ";transport=" + hop.getTransport() + ">\r\n");
            CallIdHeader callId = sipProvider.getNewCallId();
            CSeqHeader cseq = new CSeqHeader();
            cseq.setMethod(Request.REGISTER);
            cseq.setSequenceNumber(1);
            MaxForwardsHeader maxForwards = new MaxForwardsHeader();
            maxForwards.setMaxForwards(1);
            String registerRequest = new StringBuffer().append(requestLine).append(via).append(callId.toString()).append(cseq.toString()).append(maxForwards.toString()).append(from).append(to).append(contact).toString();
            Request request = messageFactory.createRequest(registerRequest);
            ClientTransaction ct = sipProvider.getNewClientTransaction(request);
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "Got client Transaction " + ct);
                Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "SENDING REGISTER TO THE PROXY");
            }
            ct.sendRequest();
        } catch (Exception ex) {
            if (Logging.REPORT_LEVEL <= Logging.WARNING) {
                Logging.report(Logging.WARNING, LogChannels.LC_JSR180, "Exception: " + ex);
            }
            synchronized (this) {
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "NOTIFY");
                }
                this.notify();
            }
        }
    }

    /**
     * Processes a request.
     * (always logs a message that request is ignored)
     * @param requestEvent the current request
     */
    public void processRequest(RequestEvent requestEvent) {
    }

    /**
     * Process a response message.
     * @param responseEvent the transition event to be processed.
     */
    public void processResponse(ResponseEvent responseEvent) {
        Response response = responseEvent.getResponse();
        if (response.getStatusCode() == SIPErrorCodes.OK) {
            this.successfulRegistration = true;
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "Registration listener : sending notify!");
            }
            synchronized (this) {
                this.notify();
            }
        } else {
            if (response.getStatusCode() == SIPErrorCodes.PROXY_AUTHENTICATION_REQUIRED || response.getStatusCode() == SIPErrorCodes.UNAUTHORIZED) {
                Exception ex = null;
                try {
                    ClientTransaction clientTransac = responseEvent.getClientTransaction();
                    Request newRequest = authenticationListener.createNewRequest(sipStack, clientTransac.getRequest(), response, countReoriginateRequest);
                    if (newRequest == null) {
                        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                            Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "Authentication failed...");
                        }
                        return;
                    }
                    countReoriginateRequest++;
                    ClientTransaction ct = sipProvider.getNewClientTransaction(newRequest);
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "Got client Transaction " + ct);
                    }
                    ct.sendRequest();
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "RegistrationHelper: request sent:\n" + newRequest);
                    }
                } catch (SipException se) {
                    ex = se;
                } catch (IOException ioe) {
                    ex = ioe;
                }
                if (ex != null) {
                    if (Logging.REPORT_LEVEL <= Logging.WARNING) {
                        Logging.report(Logging.WARNING, LogChannels.LC_JSR180, "RegistrationHelper: processResponse()," + " exception raised: " + ex.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Process a tiomeout event.
     * @param timeoutEvent termination event based on timeout condition
     */
    public void processTimeout(TimeoutEvent timeoutEvent) {
        synchronized (this) {
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_JSR180, "NOTIFY");
            }
            this.notify();
        }
    }
}
