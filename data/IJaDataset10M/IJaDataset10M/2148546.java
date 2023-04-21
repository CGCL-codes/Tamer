package net.java.sipmack.sip;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;
import javax.sdp.SessionDescription;
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
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Message;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import net.java.sipmack.common.Log;
import net.java.sipmack.sip.event.CallEvent;
import net.java.sipmack.sip.event.CallRejectedEvent;
import net.java.sipmack.sip.event.CommunicationsErrorEvent;
import net.java.sipmack.sip.event.CommunicationsListener;
import net.java.sipmack.sip.event.MessageEvent;
import net.java.sipmack.sip.event.RegistrationEvent;
import net.java.sipmack.sip.event.UnknownMessageEvent;
import net.java.sipmack.sip.security.Credentials;
import net.java.sipmack.sip.security.SecurityAuthority;
import net.java.sipmack.sip.security.SipSecurityManager;
import net.java.sipmack.sip.simple.MessageProcessing;

/**
 * Title: SIPark
 * Description: JAIN-SIP Audio/Video phone application
 *
 * @author Thiago Rocha Camargo (thiago@jivesoftware.com) The
 *         <code>SipManager</code> class that Manage SIP Stack
 * @version 1.0, 20/07/2006
 */
public class SipManager implements SipListener {

    /**
     * Specifies the number of retries that should be attempted when deleting a
     * sipProvider
     */
    protected static final int RETRY_OBJECT_DELETES = 10;

    protected static final long REGISTER_TIMEOUT = 20000;

    /**
     * Specifies the time to wait before retrying delete of a sipProvider.
     */
    protected static final long RETRY_OBJECT_DELETES_AFTER = 500;

    protected static final String DEFAULT_TRANSPORT = "udp";

    /**
     * The SipFactory instance used to create the SipStack and the Address
     * Message and Header Factories.
     */
    public SipFactory sipFactory;

    /**
     * The AddressFactory used to create URLs ans Address objects.
     */
    public AddressFactory addressFactory;

    /**
     * The HeaderFactory used to create SIP message headers.
     */
    public HeaderFactory headerFactory;

    /**
     * The Message Factory used to create SIP messages.
     */
    public MessageFactory messageFactory;

    /**
     * The sipStack instance that handles SIP communications.
     */
    SipStack sipStack;

    /**
     * The busy state of this SIP Manager
     */
    public boolean isBusy = false;

    /**
     * The default (and currently the only) SIP listening point of the
     * application.
     */
    ListeningPoint listeningPoint;

    /**
     * The JAIN SIP SipProvider instance.
     */
    public SipProvider sipProvider;

    /**
     * An instance used to provide user credentials
     */
    private SecurityAuthority securityAuthority = null;

    /**
     * Used for the contact header to provide firewall support.
     */
    private InetSocketAddress publicIpAddress = null;

    protected String sipStackPath = "gov.nist";

    /**
     */
    protected String currentlyUsedURI = null;

    protected String username = null;

    protected String displayName = null;

    protected String transport = null;

    protected String registrarAddress = null;

    protected int localPort = -1;

    protected int registrarPort = -1;

    protected int registrationsExpiration = -1;

    protected String registrarTransport = null;

    private int registerRetries = 0;

    protected String stackAddress = null;

    protected String stackName = "SIPark";

    /**
     */
    protected FromHeader fromHeader = null;

    /**
     */
    protected ContactHeader contactHeader = null;

    protected ArrayList<ViaHeader> viaHeaders = null;

    protected static final int MAX_FORWARDS = 70;

    /**
     */
    protected MaxForwardsHeader maxForwardsHeader = null;

    protected long registrationTransaction = -1;

    protected List<CommunicationsListener> listeners = new ArrayList<CommunicationsListener>();

    /**
     * The instance that handles all registration associated activity such as
     * registering, unregistering and keeping track of expirations.
     */
    RegisterProcessing registerProcessing = null;

    /**
     * The instance that handles all call associated activity such as
     * establishing, managing, and terminating calls.
     */
    CallProcessing callProcessing = null;

    /**
     * The instance that handles incoming/outgoing MESSAGE requests.
     */
    public MessageProcessing messageProcessing = null;

    /**
     * The instance that handles incoming/outgoing REFER requests.
     */
    TransferProcessing transferProcessing = null;

    /**
     * Authentication manager.
     */
    public SipSecurityManager sipSecurityManager = null;

    /**
     */
    protected boolean isStarted = false;

    /**
     * Constructor. It only creates a SipManager instance without initializing
     * the stack itself.
     */
    public SipManager() {
        registerProcessing = new RegisterProcessing(this);
        callProcessing = new CallProcessing(this);
        messageProcessing = new MessageProcessing(this);
        transferProcessing = new TransferProcessing(this, callProcessing);
        sipSecurityManager = new SipSecurityManager();
        registerRetries = 0;
    }

    /**
     * Creates and initializes JAIN SIP objects (factories, stack, listening
     * point and provider). Once this method is called the application is ready
     * to handle (incoming and outgoing) sip messages.
     *
     * @throws CommunicationsException if an axception should occur during the initialization
     *                                 process
     */
    public void start() throws CommunicationsException {
        try {
            initProperties();
            SIPConfig.setSystemProperties();
            this.sipFactory = SipFactory.getInstance();
            sipFactory.setPathName(sipStackPath);
            try {
                addressFactory = sipFactory.createAddressFactory();
                headerFactory = sipFactory.createHeaderFactory();
                messageFactory = sipFactory.createMessageFactory();
            } catch (PeerUnavailableException ex) {
                Log.error("start", ex);
                throw new CommunicationsException("Could not create factories!", ex);
            }
            try {
                sipStack = sipFactory.createSipStack(System.getProperties());
                ((SipCommRouter) sipStack.getRouter()).setOutboundProxy(SIPConfig.getOutboundProxy());
            } catch (PeerUnavailableException ex) {
                Log.error("start", ex);
                throw new CommunicationsException("Cannot connect!\n" + "Please verify your connection.", ex);
            }
            try {
                boolean successfullyBound = false;
                int tries = 0;
                while (!successfullyBound) {
                    try {
                        publicIpAddress = NetworkAddressManager.getPublicAddressFor(localPort);
                        listeningPoint = sipStack.createListeningPoint(publicIpAddress.getAddress().getHostAddress(), localPort, transport);
                    } catch (InvalidArgumentException ex) {
                        Log.error("start", ex);
                        if (tries > 3) {
                            if (!NetworkAddressManager.nextIndex()) throw new CommunicationsException("Cannot connect!\n" + "Please verify your connection.", ex);
                            tries = 0;
                        }
                        tries++;
                        localPort = (int) ((65000 - 1024) * Math.random()) + 1024;
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }
                        continue;
                    }
                    successfullyBound = true;
                }
            } catch (TransportNotSupportedException ex) {
                throw new CommunicationsException("Transport " + transport + " is not suppported by the stack!\n Try specifying another" + " transport in Mais property files.\n", ex);
            }
            try {
                sipProvider = sipStack.createSipProvider(listeningPoint);
            } catch (ObjectInUseException ex) {
                Log.error("start", ex);
                throw new CommunicationsException("Could not create factories!\n", ex);
            }
            try {
                sipProvider.addSipListener(this);
            } catch (TooManyListenersException exc) {
                throw new CommunicationsException("Could not register SipManager as a sip listener!", exc);
            }
            if (sipSecurityManager.getSecurityAuthority() == null) {
                throw new CommunicationsException("No SecurityAuthority was provided to SipManager!");
            }
            if (sipSecurityManager.getSecurityAuthority() == null) {
                throw new CommunicationsException("No SubscriptionAuthority was provided to SipManager!");
            }
            sipSecurityManager.setHeaderFactory(headerFactory);
            sipSecurityManager.setTransactionCreator(sipProvider);
            sipSecurityManager.setSipManCallback(this);
            contactHeader = null;
            fromHeader = null;
            viaHeaders = null;
            maxForwardsHeader = null;
            isStarted = true;
        } finally {
        }
    }

    /**
     * Unregisters listening points, deletes sip providers, and generally
     * prepares the stack for a re-start(). This method is meant to be used when
     * properties are changed and should be reread by the stack.
     *
     * @throws CommunicationsException
     */
    public synchronized void stop() throws CommunicationsException {
        try {
            if (registerProcessing != null) registerProcessing.cancelSchedules();
            if (sipStack == null) return;
            sipProvider.removeSipListener(this);
            int tries = 0;
            for (tries = 0; tries < RETRY_OBJECT_DELETES; tries++) {
                try {
                    sipStack.deleteSipProvider(sipProvider);
                } catch (ObjectInUseException ex) {
                    sleep(RETRY_OBJECT_DELETES_AFTER);
                    continue;
                }
                break;
            }
            if (sipStack == null) return;
            if (tries >= RETRY_OBJECT_DELETES) throw new CommunicationsException("Failed to delete the sipProvider!");
            if (sipStack == null) return;
            for (tries = 0; tries < RETRY_OBJECT_DELETES; tries++) {
                try {
                    sipStack.deleteListeningPoint(listeningPoint);
                } catch (ObjectInUseException ex) {
                    sleep(RETRY_OBJECT_DELETES_AFTER);
                    continue;
                }
                break;
            }
            if (sipStack != null) {
                for (Iterator it = sipStack.getSipProviders(); it.hasNext(); ) {
                    SipProvider element = (SipProvider) it.next();
                    try {
                        sipStack.deleteSipProvider(element);
                    } catch (Exception e) {
                    }
                }
            }
            if (tries >= RETRY_OBJECT_DELETES) throw new CommunicationsException("Failed to delete a listeningPoint!");
            listeningPoint = null;
            addressFactory = null;
            messageFactory = null;
            headerFactory = null;
            sipStack = null;
            registrarAddress = null;
            securityAuthority = null;
            viaHeaders = null;
            contactHeader = null;
            fromHeader = null;
            NetworkAddressManager.shutDown();
        } finally {
            isStarted = false;
        }
    }

    /**
     * Waits during _no_less_ than sleepFor milliseconds. Had to implement it on
     * top of Thread.sleep() to guarantee minimum sleep time.
     *
     * @param sleepFor the number of miliseconds to wait
     */
    protected static void sleep(long sleepFor) {
        long startTime = System.currentTimeMillis();
        long haveBeenSleeping = 0;
        while (haveBeenSleeping < sleepFor) {
            try {
                Thread.sleep(sleepFor - haveBeenSleeping);
            } catch (InterruptedException ex) {
            }
            haveBeenSleeping = (System.currentTimeMillis() - startTime);
        }
    }

    /**
     * @param uri the currentlyUsedURI to set.
     */
    public void setCurrentlyUsedURI(String uri) {
        this.currentlyUsedURI = uri;
    }

    /**
     * Get the currently used SIP username
     */
    public String getCurrentUsername() {
        return this.username;
    }

    /**
     * Causes the RegisterProcessing object to send a registration request to
     * the registrar defined in net.java.mais.sip.REGISTRAR_ADDRESS and to
     * register with the address defined in the net.java.mais.sip.PUBLIC_ADDRESS
     * property
     *
     * @throws CommunicationsException if an exception is thrown by the underlying stack. The
     *                                 exception that caused this CommunicationsException may be
     *                                 extracted with CommunicationsException.getCause()
     */
    public void register() throws CommunicationsException {
        register(currentlyUsedURI);
    }

    /**
     * Registers using the specified public address.
     *
     * @param sipServerAddress the address of the server.
     * @throw CommunicationsException
     */
    public void register(String sipServerAddress) throws CommunicationsException {
        try {
            if (sipServerAddress == null || sipServerAddress.trim().length() == 0) {
                Log.debug("PUBLIC NOT FOUND!");
                throw new CommunicationsException("Public address NOT FOUND");
            }
            String defaultDomainName = SIPConfig.getDefaultDomain();
            if (sipServerAddress.toLowerCase().indexOf("sipphone.com") != -1 || (defaultDomainName != null && defaultDomainName.indexOf("sipphone.com") != -1)) {
                StringBuffer buff = new StringBuffer(sipServerAddress);
                int nameEnd = sipServerAddress.indexOf('@');
                nameEnd = nameEnd == -1 ? Integer.MAX_VALUE : nameEnd;
                nameEnd = Math.min(nameEnd, buff.length()) - 1;
                int nameStart = sipServerAddress.indexOf("sip:");
                nameStart = nameStart == -1 ? 0 : nameStart + "sip:".length();
                for (int i = nameEnd; i >= nameStart; i--) if (!Character.isLetter(buff.charAt(i)) && !Character.isDigit(buff.charAt(i))) buff.deleteCharAt(i);
                sipServerAddress = buff.toString();
            }
            if (defaultDomainName != null && sipServerAddress.indexOf('@') == -1) {
                sipServerAddress = sipServerAddress + "@" + defaultDomainName;
            }
            if (!sipServerAddress.trim().toLowerCase().startsWith("sip:")) {
                sipServerAddress = "sip:" + sipServerAddress;
            }
            this.currentlyUsedURI = sipServerAddress;
            registerProcessing.register(registrarAddress, registrarPort, registrarTransport, registrationsExpiration);
        } catch (Exception e) {
            fireRegistrationFailed(registrarAddress, RegistrationEvent.Type.TimeOut);
            Log.error("register", e);
        }
    }

    public void startRegisterProcess(String userName, String authUserName, String password) throws CommunicationsException {
        try {
            checkIfStarted();
            Credentials defaultCredentials = new Credentials();
            this.username = userName;
            defaultCredentials.setUserName(userName);
            defaultCredentials.setPassword(password.toCharArray());
            defaultCredentials.setAuthUserName(authUserName != null ? authUserName : userName);
            String realm = SIPConfig.getAuthenticationRealm();
            realm = realm == null ? "" : realm;
            Credentials initialCredentials = securityAuthority.obtainCredentials(realm, defaultCredentials);
            SIPConfig.setUserName(initialCredentials.getUserName());
            SIPConfig.setAuthUserName(initialCredentials.getAuthUserName());
            register(initialCredentials.getUserName());
            cacheCredentials(realm, initialCredentials);
        } catch (Exception ee) {
            Log.error("startRegisterProcess", ee);
        }
    }

    /**
     * Causes the PresenceAgent object to notify all subscribers of our brand
     * new offline status and the RegisterProcessing object to send a
     * registration request with a 0 "expires" interval to the registrar defined
     * in net.java.mais.sip.REGISTRAR_ADDRESS.
     *
     * @throws CommunicationsException if an exception is thrown by the underlying stack. The
     *                                 exception that caused this CommunicationsException may be
     *                                 extracted with CommunicationsException.getCause()
     */
    public void unregister() throws CommunicationsException {
        try {
            checkIfStarted();
            if (!isRegistered()) {
                return;
            }
            registerProcessing.unregister();
            fireUnregistered(registrarAddress == null ? "" : registrarAddress);
        } catch (Exception e) {
            Log.error("unregister", e);
        }
    }

    private boolean registrationFailed(RegistrationEvent.Type type) {
        try {
            if (registerRetries++ < 2) {
                stop();
                while (isStarted()) ;
                start();
                while (!isStarted()) ;
                register();
                return false;
            } else {
                if (NetworkAddressManager.nextIndex()) {
                    stop();
                    start();
                    registerRetries = 0;
                    register();
                    return false;
                }
            }
        } catch (Exception e) {
            Log.error("unregister", e);
        }
        return true;
    }

    /**
     * Queries the RegisterProcessing object whether the application is
     * registered with a registrar.
     *
     * @return true if the application is registered with a registrar.
     */
    public boolean isRegistered() {
        return (registerProcessing != null && registerProcessing.isRegistered());
    }

    /**
     * Determines whether the SipManager was started.
     *
     * @return true if the SipManager was started.
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * Causes the CallProcessing object to send an INVITE request to the URI
     * specified by <code>callee</code> setting sdpContent as message body.
     * The method generates a Call object that will represent the resulting call
     * and will be used for later references to the same call.
     *
     * @param callee     the URI to send the INVITE to.
     * @param sdpContent the sdp session offer.
     * @return the Call object that will represent the call resulting from
     *         invoking this method.
     * @throws CommunicationsException if an exception occurs while sending and parsing.
     */
    public Call establishCall(String callee, String sdpContent) throws CommunicationsException {
        checkIfStarted();
        if (isBusy()) return null;
        return callProcessing.invite(callee, sdpContent);
    }

    /**
     * Causes the CallProcessing object to send a terminating request (CANCEL,
     * BUSY_HERE or BYE) and thus terminate that call with id
     * <code>callID</code>.
     *
     * @param callID the id of the call to terminate.
     * @throws CommunicationsException if an exception occurs while invoking this method.
     */
    public void endCall(int callID) throws CommunicationsException {
        fireCallEnded(callProcessing.callDispatcher.getCall(callID));
        checkIfStarted();
        callProcessing.endCall(callID);
    }

    public void sendDTMF(int callID, String digit) throws CommunicationsException {
        callProcessing.sendDTMF(callID, digit);
    }

    public void hold(int callID, SessionDescription sdp, boolean mic, boolean cam) throws CommunicationsException {
        callProcessing.hold(callID, sdp.toString());
        callProcessing.callDispatcher.getCall(callID).onHoldMic(mic);
        callProcessing.callDispatcher.getCall(callID).onHoldCam(cam);
    }

    public void transfer(int callID, String callee) {
        try {
            transferProcessing.transfer(callID, callee);
        } catch (CommunicationsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls endCall for all currently active calls.
     *
     * @throws CommunicationsException if an exception occurs while
     */
    public void endAllCalls() throws CommunicationsException {
        try {
            checkIfStarted();
            if (!isRegistered()) {
                return;
            }
            if (callProcessing == null) {
                return;
            }
            Object[] keys = callProcessing.getCallDispatcher().getAllCalls();
            for (int i = 0; i < keys.length; i++) {
                endCall(((Integer) keys[i]).intValue());
            }
        } finally {
            setBusy(false);
        }
    }

    /**
     * Causes CallProcessing to send a 200 OK response, with the specified sdp
     * description, to the specified call's remote party.
     *
     * @param callID     the id of the call that is to be answered.
     * @param sdpContent this party's media description (as defined by SDP).
     * @throws CommunicationsException if an axeption occurs while invoking this method.
     */
    public void answerCall(int callID, String sdpContent) throws CommunicationsException {
        checkIfStarted();
        callProcessing.sayOK(callID, sdpContent);
    }

    /**
     * Sends a NOT_IMPLEMENTED response through the specified transaction.
     *
     * @param serverTransaction the transaction to send the response through.
     * @param request           the request that is being answered.
     */
    void sendNotImplemented(ServerTransaction serverTransaction, Request request) {
        Response notImplemented = null;
        try {
            notImplemented = messageFactory.createResponse(Response.NOT_IMPLEMENTED, request);
            attachToTag(notImplemented, serverTransaction.getDialog());
        } catch (ParseException ex) {
            fireCommunicationsError(new CommunicationsException("Failed to create a NOT_IMPLEMENTED response to a " + request.getMethod() + " request!", ex));
            return;
        }
        try {
            serverTransaction.sendResponse(notImplemented);
        } catch (SipException ex) {
            fireCommunicationsError(new CommunicationsException("Failed to create a NOT_IMPLEMENTED response to a " + request.getMethod() + " request!", ex));
        } catch (InvalidArgumentException e) {
            fireCommunicationsError(new CommunicationsException("Failed to create a NOT_IMPLEMENTED response to a " + request.getMethod() + " request!", e));
        }
    }

    public void processIOException(IOExceptionEvent ioExceptionEvent) {
    }

    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
    }

    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
    }

    /**
     * Initialises SipManager's fromHeader field in accordance with
     * net.java.mais.sip.PUBLIC_ADDRESS net.java.mais.sip.DISPLAY_NAME
     * net.java.mais.sip.TRANSPORT net.java.mais.sip.PREFERRED_LOCAL_PORT and
     * returns a reference to it.
     *
     * @return a reference to SipManager's fromHeader field.
     * @throws CommunicationsException if a ParseException occurs while initially composing the
     *                                 FromHeader.
     */
    public FromHeader getFromHeader() throws CommunicationsException {
        return this.getFromHeader(false);
    }

    public FromHeader getFromHeader(boolean isNew) throws CommunicationsException {
        if (fromHeader != null && !isNew) {
            return fromHeader;
        }
        try {
            SipURI fromURI = (SipURI) addressFactory.createURI(currentlyUsedURI);
            fromURI.setTransportParam(listeningPoint.getTransport());
            fromURI.setPort(listeningPoint.getPort());
            Address fromAddress = addressFactory.createAddress(fromURI);
            if (displayName != null && displayName.trim().length() > 0) {
                fromAddress.setDisplayName(displayName);
            } else {
                fromAddress.setDisplayName(Credentials.getUserDisplay());
            }
            fromHeader = headerFactory.createFromHeader(fromAddress, Integer.toString(hashCode()));
        } catch (ParseException ex) {
            throw new CommunicationsException("A ParseException occurred while creating From Header!", ex);
        }
        return fromHeader;
    }

    /**
     * Same as calling getContactHeader(true)
     *
     * @return the result of getContactHeader(true)
     * @throws CommunicationsException if an exception is thrown while calling
     *                                 getContactHeader(false)
     * @uml.property name="contactHeader"
     */
    public ContactHeader getContactHeader() throws CommunicationsException {
        return getContactHeader(true);
    }

    /**
     * Same as calling getContactHeader(true).
     *
     * @return the result of calling getContactHeader(true).
     * @throws CommunicationsException if an exception occurs while executing
     *                                 getContactHeader(true).
     */
    ContactHeader getRegistrationContactHeader() throws CommunicationsException {
        return getContactHeader(true);
    }

    /**
     * Initialises SipManager's contactHeader field in accordance with
     * javax.sip.IP_ADDRESS net.java.mais.sip.DISPLAY_NAME
     * net.java.mais.sip.TRANSPORT net.java.mais.sip.PREFERRED_LOCAL_PORT and
     * returns a reference to it.
     *
     * @param useLocalHostAddress specifies whether the SipURI in the contact header should
     *                            contain the value of javax.sip.IP_ADDRESS (true) or that of
     *                            net.java.mais.sip.PUBLIC_ADDRESS (false).
     * @return a reference to SipManager's contactHeader field.
     * @throws CommunicationsException if a ParseException occurs while initially composing the
     *                                 FromHeader.
     */
    public ContactHeader getContactHeader(boolean useLocalHostAddress) throws CommunicationsException {
        if (contactHeader != null) {
            return contactHeader;
        }
        try {
            SipURI contactURI;
            if (useLocalHostAddress) {
                contactURI = (SipURI) addressFactory.createSipURI(null, Credentials.getUserDisplay() + "@" + publicIpAddress.getAddress().getHostAddress());
            } else {
                contactURI = (SipURI) addressFactory.createURI(currentlyUsedURI);
            }
            contactURI.setPort(publicIpAddress.getPort());
            Address contactAddress = addressFactory.createAddress(contactURI);
            if (displayName != null && displayName.trim().length() > 0) {
                contactAddress.setDisplayName(displayName);
            }
            contactHeader = headerFactory.createContactHeader(contactAddress);
        } catch (ParseException ex) {
            throw new CommunicationsException("A ParseException occurred while creating From Header!", ex);
        }
        return contactHeader;
    }

    /**
     * Initializes (if null) and returns an ArrayList with a single ViaHeader
     * containing localhost's address. This ArrayList may be used when sending
     * requests.
     *
     * @return ViaHeader-s list to be used when sending requests.
     * @throws CommunicationsException if a ParseException is to occur while initializing the array
     *                                 list.
     */
    public ArrayList<ViaHeader> getLocalViaHeaders() throws CommunicationsException {
        ListeningPoint lp = sipProvider.getListeningPoint();
        viaHeaders = new ArrayList<ViaHeader>();
        try {
            ViaHeader viaHeader = headerFactory.createViaHeader(SIPConfig.getIPAddress(), lp.getPort(), lp.getTransport(), null);
            viaHeader.setParameter("rport", null);
            viaHeaders.add(viaHeader);
            return viaHeaders;
        } catch (ParseException ex) {
            throw new CommunicationsException("A ParseException occurred while creating Via Headers!");
        } catch (InvalidArgumentException ex) {
            throw new CommunicationsException("Unable to create a via header for port " + lp.getPort(), ex);
        }
    }

    /**
     * Initializes and returns SipManager's maxForwardsHeader field using the
     * value specified by MAX_FORWARDS.
     *
     * @return an instance of a MaxForwardsHeader that can be used when sending
     *         requests
     * @throws CommunicationsException if MAX_FORWARDS has an invalid value.
     */
    public MaxForwardsHeader getMaxForwardsHeader() throws CommunicationsException {
        if (maxForwardsHeader != null) {
            return maxForwardsHeader;
        }
        try {
            maxForwardsHeader = headerFactory.createMaxForwardsHeader(MAX_FORWARDS);
            return maxForwardsHeader;
        } catch (InvalidArgumentException ex) {
            throw new CommunicationsException("A problem occurred while creating MaxForwardsHeader", ex);
        }
    }

    /**
     * Returns the user used to create the From Header URI.
     *
     * @return the user used to create the From Header URI.
     */
    public String getLocalUser() {
        try {
            return ((SipURI) getFromHeader().getAddress().getURI()).getUser();
        } catch (CommunicationsException ex) {
            return "";
        }
    }

    /**
     * Generates a ToTag (the containingDialog's hashCode())and attaches it to
     * response's ToHeader.
     *
     * @param response         the response that is to get the ToTag.
     * @param containingDialog the Dialog instance that is to extract a unique Tag value
     *                         (containingDialog.hashCode())
     */
    public void attachToTag(Response response, Dialog containingDialog) {
        ToHeader to = (ToHeader) response.getHeader(ToHeader.NAME);
        if (to == null) {
            fireCommunicationsError(new CommunicationsException("No TO header found in, attaching a to tag is therefore impossible"));
        }
        try {
            if (to.getTag() == null || to.getTag().trim().length() == 0) {
                int toTag = containingDialog != null ? containingDialog.hashCode() : (int) System.currentTimeMillis();
                to.setTag(Integer.toString(toTag));
            }
        } catch (ParseException ex) {
            fireCommunicationsError(new CommunicationsException("Failed to attach a TO tag to an outgoing response"));
        }
    }

    protected void initProperties() {
        try {
            stackAddress = getLocalHostAddress();
            SIPConfig.setIPAddress(stackAddress);
            SIPConfig.setSystemProperties();
            if (stackAddress.indexOf(':') != stackAddress.lastIndexOf(':') && stackAddress.charAt(0) != '[') {
                stackAddress = '[' + stackAddress.trim() + ']';
            }
            stackName = SIPConfig.getStackName();
            if (stackName == null) {
                stackName = "SIPark@" + Integer.toString(hashCode());
            }
            String retransmissionFilter = SIPConfig.getRetransmissionFilter();
            if (currentlyUsedURI == null) {
                currentlyUsedURI = SIPConfig.getUserName() + "@" + stackAddress;
            }
            if (!currentlyUsedURI.trim().toLowerCase().startsWith("sip:")) {
                currentlyUsedURI = "sip:" + currentlyUsedURI.trim();
            }
            String presenceUri = "pres" + currentlyUsedURI.substring(currentlyUsedURI.indexOf(':'));
            registrarAddress = SIPConfig.getRegistrarAddress();
            try {
                registrarPort = SIPConfig.getRegistrarPort();
            } catch (NumberFormatException ex) {
                registrarPort = 5060;
            }
            registrarTransport = SIPConfig.getRegistrarTransport();
            if (registrarTransport == null) {
                registrarTransport = DEFAULT_TRANSPORT;
            }
            try {
                registrationsExpiration = SIPConfig.getRegistrationExpiration();
            } catch (NumberFormatException ex) {
                registrationsExpiration = 3600;
            }
            sipStackPath = SIPConfig.getStackPath();
            if (sipStackPath == null) {
                sipStackPath = "gov.nist";
            }
            String routerPath = SIPConfig.getRouterPath();
            transport = SIPConfig.getTransport();
            if (transport.equals("")) {
                transport = DEFAULT_TRANSPORT;
            }
            try {
                localPort = SIPConfig.getLocalPort();
            } catch (NumberFormatException exc) {
                localPort = 5060;
            }
            displayName = SIPConfig.getDisplayName();
        } catch (Exception e) {
            Log.error("initProperties", e);
        }
    }

    /**
     * Sets the SecurityAuthority instance that should be consulted later on for
     * user credentials.
     *
     * @param authority the SecurityAuthority instance that should be consulted later
     *                  on for user credentials.
     */
    public void setSecurityAuthority(SecurityAuthority authority) {
        this.securityAuthority = authority;
        sipSecurityManager.setSecurityAuthority(authority);
    }

    /**
     * Adds the specified credentials to the security manager's credentials
     * cache so that they get tried next time they're needed.
     *
     * @param realm       the realm these credentials should apply for.
     * @param credentials a set of credentials (username and pass)
     */
    public void cacheCredentials(String realm, Credentials credentials) {
        sipSecurityManager.cacheCredentials(realm, credentials);
    }

    /**
     * Adds a CommunicationsListener to SipManager.
     *
     * @param listener The CommunicationsListener to be added.
     */
    public void addCommunicationsListener(CommunicationsListener listener) {
        try {
            listeners.add(listener);
        } catch (Exception e) {
            Log.error("addCommunicationsListener", e);
        }
    }

    void fireCallReceived(Call call) {
        try {
            CallEvent evt = new CallEvent(call);
            for (int i = listeners.size() - 1; i >= 0; i--) {
                ((CommunicationsListener) listeners.get(i)).callReceived(evt);
            }
        } catch (Exception e) {
            Log.error("fireCallReceived", e);
        }
    }

    void fireCallEnded(Call call) {
        try {
            CallEvent evt = new CallEvent(call);
            for (int i = listeners.size() - 1; i >= 0; i--) {
                ((CommunicationsListener) listeners.get(i)).callEnded(evt);
            }
        } catch (Exception e) {
            Log.error("fireCallEnded", e);
        }
    }

    void fireMessageReceived(Request message) {
        MessageEvent evt = new MessageEvent(message);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            ((CommunicationsListener) listeners.get(i)).messageReceived(evt);
        }
    }

    void fireRegistered(String address) {
        try {
            registerProcessing.subscribe(registrarAddress, registrarPort, registrarTransport);
        } catch (CommunicationsException e) {
            Log.error(e);
        }
        RegistrationEvent evt = new RegistrationEvent(address);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            ((CommunicationsListener) listeners.get(i)).registered(evt);
        }
    }

    void fireRegistering(String address) {
        RegistrationEvent evt = new RegistrationEvent(address);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            ((CommunicationsListener) listeners.get(i)).registering(evt);
        }
    }

    public void fireUnregistered(String address) {
        RegistrationEvent evt = new RegistrationEvent(address);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            ((CommunicationsListener) listeners.get(i)).unregistered(evt);
        }
    }

    void fireRegistrationFailed(String address, RegistrationEvent.Type type) {
        if (registrationFailed(type)) {
            RegistrationEvent evt = new RegistrationEvent(address, type);
            for (int i = listeners.size() - 1; i >= 0; i--) {
                ((CommunicationsListener) listeners.get(i)).registrationFailed(evt);
            }
        }
    }

    void fireUnregistering(String address) {
        RegistrationEvent evt = new RegistrationEvent(address);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            ((CommunicationsListener) listeners.get(i)).unregistering(evt);
        }
    }

    void fireUnknownMessageReceived(Message message) {
        UnknownMessageEvent evt = new UnknownMessageEvent(message);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            ((CommunicationsListener) listeners.get(i)).receivedUnknownMessage(evt);
        }
    }

    public void fireCallRejectedLocally(String reason, Message invite, Call call) {
        CallRejectedEvent evt = new CallRejectedEvent(reason, invite, call);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            ((CommunicationsListener) listeners.get(i)).callRejectedLocally(evt);
        }
    }

    void fireCallRejectedRemotely(String reason, Message invite, Call call) {
        CallRejectedEvent evt = new CallRejectedEvent(reason, invite, call);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            ((CommunicationsListener) listeners.get(i)).callRejectedRemotely(evt);
        }
    }

    public void fireCommunicationsError(Throwable throwable) {
        try {
            CommunicationsErrorEvent evt = new CommunicationsErrorEvent(throwable);
            for (int i = listeners.size() - 1; i >= 0; i--) {
                ((CommunicationsListener) listeners.get(i)).communicationsErrorOccurred(evt);
            }
        } catch (Throwable e) {
            Log.error("fireCommunicationsError", e);
        }
    }

    public void processRequest(RequestEvent requestReceivedEvent) {
        Log.debug(requestReceivedEvent.getRequest().toString());
        ServerTransaction serverTransaction = requestReceivedEvent.getServerTransaction();
        Request request = requestReceivedEvent.getRequest();
        if (serverTransaction == null) {
            try {
                serverTransaction = sipProvider.getNewServerTransaction(request);
            } catch (TransactionAlreadyExistsException ex) {
                return;
            } catch (TransactionUnavailableException ex) {
                return;
            }
        }
        Dialog dialog = serverTransaction.getDialog();
        if (request.getMethod().equals(Request.NOTIFY)) {
            Response ok = null;
            try {
                ok = messageFactory.createResponse(Response.OK, request);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            ContactHeader contactHeader = null;
            try {
                contactHeader = getContactHeader();
                ok.addHeader(contactHeader);
                attachToTag(ok, dialog);
                System.err.println(ok.toString());
                serverTransaction.sendResponse(ok);
            } catch (CommunicationsException e) {
                e.printStackTrace();
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            } catch (SipException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fireMessageReceived(request);
            return;
        }
        Request requestClone = (Request) request.clone();
        if (request.getMethod().equals(Request.INVITE)) {
            if (serverTransaction.getDialog().getState() == null) {
                callProcessing.processInvite(serverTransaction, request);
            } else {
                callProcessing.processReInvite(serverTransaction, request);
            }
        } else if (request.getMethod().equals(Request.ACK)) {
            if (serverTransaction != null && serverTransaction.getDialog().getFirstTransaction().getRequest().getMethod().equals(Request.INVITE)) {
                callProcessing.processAck(serverTransaction, request);
            } else {
            }
        } else if (request.getMethod().equals(Request.BYE)) {
            if (dialog.getFirstTransaction().getRequest().getMethod().equals(Request.INVITE) || dialog.getFirstTransaction().getRequest().getMethod().equals(Request.REFER)) {
                callProcessing.processBye(serverTransaction, request);
            }
        } else if (request.getMethod().equals(Request.CANCEL)) {
            if (dialog.getFirstTransaction().getRequest().getMethod().equals(Request.INVITE)) {
                callProcessing.processCancel(serverTransaction, request);
            } else {
                sendNotImplemented(serverTransaction, request);
                fireUnknownMessageReceived(requestReceivedEvent.getRequest());
            }
        } else if (request.getMethod().equals(Request.REFER)) {
            transferProcessing.processRefer(serverTransaction, request);
        } else if (request.getMethod().equals(Request.INFO)) {
            fireMessageReceived(request);
        } else if (request.getMethod().equals(Request.MESSAGE)) {
            messageProcessing.processMessageRequest(serverTransaction, request);
            fireMessageReceived(request);
        } else if (request.getMethod().equals(Request.OPTIONS)) {
            sendNotImplemented(serverTransaction, request);
            fireUnknownMessageReceived(requestReceivedEvent.getRequest());
        } else if (request.getMethod().equals(Request.PRACK)) {
            sendNotImplemented(serverTransaction, request);
            fireUnknownMessageReceived(requestReceivedEvent.getRequest());
        } else if (request.getMethod().equals(Request.REGISTER)) {
            sendNotImplemented(serverTransaction, request);
            fireUnknownMessageReceived(requestReceivedEvent.getRequest());
        } else if (request.getMethod().equals(Request.SUBSCRIBE)) {
        } else if (request.getMethod().equals(Request.UPDATE)) {
            sendNotImplemented(serverTransaction, request);
            fireUnknownMessageReceived(requestReceivedEvent.getRequest());
        } else if (request.getMethod().equals(Request.INFO)) {
            sendNotImplemented(serverTransaction, request);
            fireUnknownMessageReceived(requestReceivedEvent.getRequest());
        } else {
            sendNotImplemented(serverTransaction, request);
            fireUnknownMessageReceived(requestReceivedEvent.getRequest());
        }
    }

    public void processTimeout(TimeoutEvent transactionTimeOutEvent) {
        Transaction transaction;
        if (transactionTimeOutEvent.isServerTransaction()) {
            transaction = transactionTimeOutEvent.getServerTransaction();
        } else {
            transaction = transactionTimeOutEvent.getClientTransaction();
        }
        Request request = transaction.getRequest();
        if (request.getMethod().equals(Request.REGISTER)) {
            registerProcessing.processTimeout(transaction, request);
        } else if (request.getMethod().equals(Request.INVITE)) {
            callProcessing.processTimeout(transaction, request);
        } else {
        }
    }

    public void processResponse(ResponseEvent responseReceivedEvent) {
        Log.debug("<RESPONSE>");
        Log.debug("[" + responseReceivedEvent.getResponse().getStatusCode() + "]");
        Log.debug("</RESPONSE>");
        ClientTransaction clientTransaction = responseReceivedEvent.getClientTransaction();
        if (clientTransaction == null) {
            return;
        }
        Response response = responseReceivedEvent.getResponse();
        Dialog dialog = clientTransaction.getDialog();
        String method = ((CSeqHeader) response.getHeader(CSeqHeader.NAME)).getMethod();
        Response responseClone = (Response) response.clone();
        if (response.getStatusCode() == Response.OK) {
            if (method.equals(Request.REGISTER)) {
                registerProcessing.processOK(clientTransaction, response);
            } else if (method.equals(Request.INVITE)) {
                callProcessing.processInviteOK(clientTransaction, response);
            } else if (method.equals(Request.BYE)) {
                callProcessing.processByeOK(clientTransaction, response);
            } else if (method.equals(Request.CANCEL)) {
                callProcessing.processCancelOK(clientTransaction, response);
            } else if (method.equals(Request.SUBSCRIBE)) {
            }
        } else if (response.getStatusCode() == Response.SESSION_PROGRESS) {
            callProcessing.processRingingBack(clientTransaction, response);
        } else if (response.getStatusCode() == Response.ACCEPTED) {
            if (method.equals(Request.SUBSCRIBE)) {
            } else if (method.equals(Request.REFER)) {
                Call call = callProcessing.getCallDispatcher().findCall(clientTransaction.getDialog());
                try {
                    callProcessing.endCall(call);
                } catch (CommunicationsException e) {
                    Log.error("Trasnfer", e);
                }
            }
        } else if (response.getStatusCode() == Response.RINGING) {
            if (method.equals(Request.INVITE)) {
                callProcessing.processRinging(clientTransaction, response);
            } else {
                fireUnknownMessageReceived(response);
            }
        } else if (response.getStatusCode() == Response.TRYING || response.getStatusCode() / 100 == 1) {
            if (method.equals(Request.INVITE)) {
                callProcessing.processTrying(clientTransaction, response);
            } else if (method.equals(Request.REGISTER)) {
            } else {
            }
        } else if (response.getStatusCode() == Response.NOT_FOUND) {
            if (method.equals(Request.INVITE)) {
                callProcessing.processNotFound(clientTransaction, response);
            }
            if (method.equals(Request.SUBSCRIBE)) {
            } else if (method.equals(Request.REGISTER)) {
                fireRegistrationFailed("Invalid username.", RegistrationEvent.Type.NotFound);
                Log.debug("REGISTER NOT FOUND");
            }
        } else if (response.getStatusCode() == Response.NOT_IMPLEMENTED) {
            if (method.equals(Request.REGISTER)) {
                registerProcessing.processNotImplemented(clientTransaction, response);
            } else if (method.equals(Request.INVITE)) {
                callProcessing.processNotImplemented(clientTransaction, response);
            } else {
                fireUnknownMessageReceived(response);
            }
        } else if (response.getStatusCode() == Response.REQUEST_TERMINATED) {
            callProcessing.processRequestTerminated(clientTransaction, response);
        } else if (response.getStatusCode() == Response.BUSY_HERE) {
            if (method.equals(Request.INVITE)) {
                callProcessing.processBusyHere(clientTransaction, response);
            } else {
                fireUnknownMessageReceived(response);
            }
        } else if (response.getStatusCode() == Response.UNAUTHORIZED || response.getStatusCode() == Response.PROXY_AUTHENTICATION_REQUIRED) {
            if (method.equals(Request.INVITE)) {
                callProcessing.processAuthenticationChallenge(clientTransaction, response);
            } else if (method.equals(Request.REGISTER)) {
                CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
                if (cseq.getSeqNumber() < 2) registerProcessing.processAuthenticationChallenge(clientTransaction, response); else fireRegistrationFailed("Invalid password.", RegistrationEvent.Type.WrongPass);
            } else if (method.equals(Request.SUBSCRIBE)) {
            } else fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.FORBIDDEN) {
        } else if (response.getStatusCode() == 403) {
            fireRegistrationFailed("Invalid auth user.", RegistrationEvent.Type.WrongAuthUser);
        } else if (response.getStatusCode() / 100 == 4) {
            if (method.equals(Request.INVITE)) {
                callProcessing.processCallError(clientTransaction, response);
            } else {
                fireUnknownMessageReceived(response);
            }
        } else if (response.getStatusCode() == Response.ACCEPTED) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.ADDRESS_INCOMPLETE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.ALTERNATIVE_SERVICE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.AMBIGUOUS) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.BAD_EVENT) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.BAD_EXTENSION) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.BAD_GATEWAY) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.BAD_REQUEST) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.BUSY_EVERYWHERE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.CALL_IS_BEING_FORWARDED) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST) {
        } else if (response.getStatusCode() == Response.DECLINE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.DOES_NOT_EXIST_ANYWHERE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.EXTENSION_REQUIRED) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.GONE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.INTERVAL_TOO_BRIEF) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.LOOP_DETECTED) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.MESSAGE_TOO_LARGE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.METHOD_NOT_ALLOWED) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.MOVED_PERMANENTLY) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.MOVED_TEMPORARILY) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.MULTIPLE_CHOICES) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.NOT_ACCEPTABLE_HERE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.PAYMENT_REQUIRED) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.QUEUED) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.REQUEST_ENTITY_TOO_LARGE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.REQUEST_PENDING) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.REQUEST_TIMEOUT) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.REQUEST_URI_TOO_LONG) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.SERVER_INTERNAL_ERROR) {
        } else if (response.getStatusCode() == Response.SERVER_TIMEOUT) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.SERVICE_UNAVAILABLE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.SESSION_NOT_ACCEPTABLE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.SESSION_PROGRESS) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.TEMPORARILY_UNAVAILABLE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.TOO_MANY_HOPS) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.UNDECIPHERABLE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.UNSUPPORTED_MEDIA_TYPE) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.UNSUPPORTED_URI_SCHEME) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.USE_PROXY) {
            fireUnknownMessageReceived(response);
        } else if (response.getStatusCode() == Response.VERSION_NOT_SUPPORTED) {
            fireUnknownMessageReceived(response);
        } else {
            fireUnknownMessageReceived(response);
        }
    }

    String getLocalHostAddress() {
        InetAddress localhost = NetworkAddressManager.getLocalHost();
        String hostAddress = localhost.getHostAddress();
        return hostAddress;
    }

    protected void checkIfStarted() throws CommunicationsException {
        if (!isStarted) {
            throw new CommunicationsException("The underlying SIP Stack had not been" + "properly initialised! Impossible to continue");
        }
    }

    public void sendServerInternalError(int callID) throws CommunicationsException {
        checkIfStarted();
        callProcessing.sayInternalError(callID);
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }
}
