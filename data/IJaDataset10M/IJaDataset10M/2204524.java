package org.mitre.jsip;

import java.io.*;
import java.util.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.mitre.jsip.event.*;
import org.mitre.jsip.proxy.*;
import JACE.ASX.MessageQueue;

public class SipClient extends Thread {

    public static final int DontHideVia = 0;

    public static final int HideHop = 1;

    public static final int HideRoute = 2;

    static final boolean DEBUG = true;

    static final String VERSION = "0.8";

    private Hashtable users = new Hashtable();

    private ArrayList calls = new ArrayList();

    private String proxy;

    private int proxyport;

    private boolean useproxy;

    private SipUri contacturi;

    private boolean fwmode;

    private SipUri forwarduri;

    private String fwbody;

    private boolean busymode;

    private String busybody;

    private int maxforwards;

    private int hidemode;

    private boolean defusermode;

    private SipUser defuser;

    private MessageSocket listener;

    private PrintWriter loggerfd;

    private MessageQueue mQueue;

    private String acceptLanguage;

    private CallListener callListener = null;

    private SipCallMember curCallMemb;

    private boolean runFlag = true;

    protected static SipClient _instance = null;

    public static SipClient getSipClient() {
        if (_instance == null) {
            _instance = new SipClient();
            _instance.start();
        }
        return _instance;
    }

    /**
     * Creates a new SIP client with the specified explicit proxy.  This
     * starts up the client listening on a UDP port.  By default, it tries
     * to listen on port 5060, or whatever is specified in the sip.dissipate.port
     * environment variable.
     */
    protected SipClient() {
        setupSocket();
        setupContactUri();
        try {
            loggerfd = new PrintWriter(new FileOutputStream(new File("jsip.log")));
        } catch (FileNotFoundException fnfe) {
            System.err.println("error opening log file: " + fnfe);
            System.exit(1);
        }
        useproxy = false;
        fwmode = false;
        busymode = false;
        defusermode = false;
        defuser = null;
        hidemode = DontHideVia;
        maxforwards = 0;
    }

    /**
   * This call does the select on all the client's listening sockets and
   * performs any pending actions.  It should be called in some main loop
   * somewhere. --- NOT USED!!!
   *
   * @param block
   */
    public void timerTask() {
        Date timeout;
        int read_fds;
        int highest_fd;
    }

    /**
   * Returns the full contact URI for this client.  This is placed in all
   * outgoing messages (except for when clearing registrations).
   *
   * @return SipUri 
   */
    public SipUri getContactUri() {
        return contacturi;
    }

    /**
     * Allows us to set the contact uri
     */
    public void setContactUri(SipUri cUri) {
        contacturi = cUri;
    }

    /**
     * Allows us to set our capabilities
     */
    public void setContactCapabilities(int capabilities) {
        contacturi.setMethodsParam(capabilities);
    }

    /**
     * Returns the contact URI the client will use when registering. This may
     * contain some <code>methods</code> tags to support <code>Sip.MESSAGE</code>
     * and <code>Sip.SUBSCRIBE</code> Contact uri's.
     */
    public SipUri getRegisterContactUri() {
        SipUri regUri = contacturi;
        regUri.setMethodsParam(SipUri.METHOD_MESSAGE | SipUri.METHOD_SUBSCRIBE | SipUri.METHOD_NOTIFY);
        return regUri;
    }

    /**
   * Turn on default user mode.  If incoming calls arrive at this client
   * and there is no associated user matching the To line in the request,
   * the default user will be used.
   *
   * Dissipate always uses a common contact URI for all registrations.
   * This means that all incoming requests should have a request URI
   * matching that of the announced Contact URI.  So, in order to check
   * for a matching user, we match on the To header.
   *
   * When default user mode is off and a message is received when there
   * is no user associated, the call is rejected with a 404 Not Found.
   *
   * @param onoff
   */
    public void setDefaultUserMode(boolean onoff) {
        defusermode = onoff;
    }

    /**
   * Returns true if we accept calls with a default user
   *
   * @return bool
   */
    public boolean getDefaultUserMode() {
        return defusermode;
    }

    /**
   * Returns the current default user.
   *
   * @return SipUser *
   */
    public SipUser getDefaultUser() {
        return defuser;
    }

    /**
   * Sets the current default user.
   *
   * @param user
   */
    public void setDefaultUser(SipUser user) {
        defuser = user;
        SipUri userUri = user.getUri();
        if (!users.containsKey(userUri.getUsername())) {
            users.put(userUri.getUsername(), user);
        }
    }

    /**
   * Sets the URI to forward calls to if call forwarding is enabled.
   *
   * @param u
   */
    public void setCallForwardUri(SipUri u) {
        forwarduri = u;
    }

    /**
   * Returns the URI we are currently forwarding to.
   *
   * @return SipUri &
   */
    public SipUri getCallForwardUri() {
        return forwarduri;
    }

    /**
   * Turns of or off call forwarding.
   *
   * @param onoff
   */
    public void setCallForward(boolean onoff) {
        fwmode = onoff;
    }

    /**
   * Returns the current state of call forwarding.
   *
   * @return bool
   */
    public boolean getCallForward() {
        return fwmode;
    }

    /**
   * Allows the user to set a message to be displayed when a client is
   * forwarded.
   *
   * @param newmessage
   */
    public void setCallForwardMessage(String newmessage) {
        fwbody = newmessage;
    }

    /**
   * Returns our current forward message.
   *
   * @return String
   */
    public String getCallForwardMessage() {
        return fwbody;
    }

    /**
   * Turns of or off busy mode.
   *
   * @param onoff
   */
    public void setBusy(boolean onoff) {
        busymode = onoff;
    }

    /**
   * Returns the current state of busy mode.
   *
   * @return bool
   */
    public boolean getBusy() {
        return busymode;
    }

    /**
   * Allows the user to set a message to be displayed when the client
   * discovers we are busy.
   *
   * @param newmessage
   */
    public void setBusyMessage(String newmessage) {
        busybody = newmessage;
    }

    /**
   * Returns our current busy message.
   *
   * @return String
   */
    public String getBusyMessage() {
        return busybody;
    }

    /**
   * Turn on explicit proxying.  When SipClient is acting in explicit
   * proxy mode, all outgoing SIP messages will be sent to the proxy
   * specified, regardless of what the Via dictates.
   *
   * @param eproxy
   */
    public void setExplicitProxyMode(boolean eproxy) {
        useproxy = eproxy;
    }

    /**
   * Returns true if we are currently sending outgoing requests to the
   * explicit proxy.
   *
   * @return bool
   */
    public boolean getExplicitProxyMode() {
        return useproxy;
    }

    /**
   * Specify the explicit proxy address for this client.  Address is in
   * the format address[:port], defaulting to 5060 if the port is not
   * specified.
   *
   * @param newproxy
   */
    public void setExplicitProxyAddress(String newproxy) {
        if (newproxy.indexOf(':') >= 0) {
            proxy = newproxy.substring(0, newproxy.indexOf(':') - 1);
            String pport = newproxy.substring(newproxy.indexOf(':') + 1);
            try {
                proxyport = Integer.parseInt(pport);
            } catch (NumberFormatException nfe) {
                System.err.println("Error setting proxy port. Set to default (5060)");
                proxyport = 5060;
            }
        } else {
            proxy = newproxy;
            proxyport = 5060;
        }
    }

    /**
   * Sets our current max forwards.
   *
   * @param newmax
   */
    public void setMaxForwards(int newmax) {
        maxforwards = newmax;
    }

    /**
   * Returns our current max forwards.
   *
   * @return int
   */
    public int getMaxForwards() {
        return maxforwards;
    }

    /**
   * Returns the explicit proxy for this client, in the same format as
   * above.
   *
   * @return String &
   */
    public String getExplicitProxyAddress() {
        return proxy;
    }

    /**
   * Set Via hiding mode.
   *
   * @param newmode
   */
    public void setHideViaMode(int newmode) {
        hidemode = newmode;
    }

    /**
   * Returns the current Via hiding mode.
   *
   * @return HideViaMode
   */
    public int getHideViaMode() {
        return hidemode;
    }

    /**
   * Returns an iterator to teh list of active SIP calls.
   *
   * @return SipCallIterator
   */
    public ArrayList getCallList() {
        return calls;
    }

    /**
   * Returns a user of this client.
   *
   * @return SipUser
   */
    public SipUser getUser(SipUri userUri) {
        return (SipUser) users.get(userUri.getUsername());
    }

    /**
   * Returns our User-Agent string (library version)
   *
   * @return String
   */
    public String getUserAgent() {
        return "jSIP/" + VERSION;
    }

    public void setAcceptLanguage(String languages) {
        acceptLanguage = languages;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public int getSipPort() {
        return proxyport;
    }

    public void incomingCall(SipCall call, SipCallMember member) {
    }

    /**
     * An external trigger for checking incoming messages
     */
    public void incomingMessage() {
        try {
            SipMessage newMessage = (SipMessage) mQueue.dequeueHead().obj();
            switch(newMessage.getType()) {
                case SipMessage.Request:
                    incomingRequestMessage((SipRequestMessage) newMessage);
                    break;
                case SipMessage.Response:
                    incomingResponseMessage((SipResponseMessage) newMessage);
                    break;
                default:
                    System.err.println("SipClient: bad incoming message type");
                    return;
            }
            CallEventMulticaster cemc = CallEventMulticaster.getMulticaster();
            cemc.dispatchEvents();
        } catch (InterruptedException ie) {
            ie.printStackTrace(System.err);
        }
    }

    public void callListUpdated() {
    }

    /**
     * Method for adding CallEvent listeners
     */
    public synchronized void addCallListener(CallListener cl) {
        CallEventMulticaster cemc = CallEventMulticaster.getMulticaster();
        callListener = cemc.addListener(cl);
    }

    /**
     * Method for removing CallEvent listeners
     */
    public synchronized void removeCallListener(CallListener cl) {
        CallEventMulticaster cemc = CallEventMulticaster.getMulticaster();
        callListener = cemc.remove(cl);
    }

    /**
     * Method for accessing the CallListener
     */
    public CallListener getCallListener() {
        return callListener;
    }

    /**
   * setupSocketStuff
   */
    private void setupSocket() {
        int listenport = 5060;
        listener = MessageSocketFactory.getMessageSocket(MessageSocket.UDP);
        try {
            String sport = System.getProperty("sip.dissipate.port");
            listenport = Integer.parseInt(sport);
        } catch (NumberFormatException nfe) {
            System.err.println("Property sip.dissipate.port not defined; setting to default (5060)");
            listenport = 5060;
        } finally {
            listener.forcePortNumber(listenport);
        }
        listener.connect((String) null, listenport);
        proxyport = listenport;
        mQueue = new JACE.ASX.MessageQueue();
        listener.listen(mQueue);
        listener.forcePortNumber(listenport);
        if (SipClient.DEBUG) System.out.println("SipClient: Listening on port: " + listener.getPortNumber());
        if (SipClient.DEBUG) System.out.println("SipClient: Our address: " + Sip.getLocalAddress());
    }

    public void run() {
        System.out.println("SipClient: going to run...");
        while (runFlag) {
            incomingMessage();
        }
    }

    public void kill() {
        runFlag = false;
        listener.close();
        CallEventMulticaster cemc = CallEventMulticaster.getMulticaster();
        cemc.shutdown();
        if (DEBUG) System.out.println("SipStack Exiting");
    }

    /**
   * setupContactUri
   */
    private void setupContactUri() {
        String username;
        String fullname;
        username = System.getProperty("user.name", "Unknown");
        fullname = System.getProperty("sip.dissipate.fullname", null);
        acceptLanguage = System.getProperty("sip.accept.language", null);
        if (contacturi == null) contacturi = new SipUri();
        contacturi.setUsername(username);
        contacturi.setHostname(Sip.getLocalAddress());
        contacturi.setPortNumber(listener.getPortNumber());
    }

    public SipCallMember getCurrentCallMemb() {
        return curCallMemb;
    }

    public void setCurrentCallMemb(SipCallMember callMemb) {
        curCallMemb = callMemb;
    }

    /**
     * incomingRequestMessage
     */
    private void incomingRequestMessage(SipRequestMessage currentMessage) {
        if (SipClient.DEBUG) System.out.println("SipClient: Request Received: \n" + currentMessage.message());
        String logmessage = "Parsed message: \n" + currentMessage.message() + "\n: End parsed message\n";
        loggerfd.print(logmessage);
        String callid = currentMessage.getHeaderData(SipHeader.Call_ID);
        if (callid == null) return;
        SipUri touri = new SipUri(currentMessage.getHeaderData(SipHeader.To));
        for (ListIterator li = calls.listIterator(); li.hasNext(); ) {
            SipCall curcall = (SipCall) li.next();
            if (callid.equals(curcall.getCallId())) {
                if (touri.hasTag()) {
                    String toTag = touri.getTag();
                    if ((toTag != null) && (toTag.compareTo(curcall.localAddress().getTag()) != 0)) {
                        if (currentMessage.getMethod() != Sip.ACK && currentMessage.getMethod() != Sip.CANCEL) {
                            sendQuickResponse(currentMessage, new SipStatus(481));
                            return;
                        } else {
                            if (SipClient.DEBUG) System.out.println("SipClient:" + "Dropping ACK/CANCEL which deserved a 481");
                        }
                    }
                }
                curcall.incomingRequest(currentMessage);
                return;
            }
        }
        if (currentMessage.getMethod() == Sip.OPTIONS) {
            touri.setTag(null);
        }
        if (currentMessage.handleNewRequest(this)) return;
        if (fwmode) {
            if (SipClient.DEBUG) System.out.println("SipClient: Forwarding call.");
            if (fwbody != null) {
                sendQuickResponse(currentMessage, new SipStatus(302), fwbody, new MimeContentType("text/plain"));
            } else {
                sendQuickResponse(currentMessage, new SipStatus(302));
            }
            return;
        }
        if (busymode) {
            if (SipClient.DEBUG) System.out.println("SipClient: We're busy.\n");
            if (busybody != null) {
                sendQuickResponse(currentMessage, new SipStatus(486), busybody, new MimeContentType("text/plain"));
            } else {
                sendQuickResponse(currentMessage, new SipStatus(486));
            }
            return;
        }
        if (currentMessage.getMethod() == Sip.BadMethod) {
            if (SipClient.DEBUG) System.out.println("SipClient: I don't recognize that method... Returning a 501.\n");
            sendQuickResponse(currentMessage, new SipStatus(501));
            return;
        }
        if (currentMessage.hasHeader(SipHeader.Accept)) {
            if (acceptHeaderBad(currentMessage.getHeaderData(SipHeader.Accept).toLowerCase())) {
                System.err.println("We got a questionable Accept header!!!");
                System.err.println("It's: " + currentMessage.getHeaderData(SipHeader.Accept).toLowerCase());
                sendQuickResponse(currentMessage, new SipStatus(406));
            }
        }
        if ((currentMessage.hasHeader(SipHeader.Require)) && (currentMessage.getHeaderData(SipHeader.Require) != null)) {
            if (SipClient.DEBUG) System.out.println("SipClient: This messages says it requires " + currentMessage.getHeaderData(SipHeader.Require) + ", returning 420.");
            sendQuickResponse(currentMessage, new SipStatus(420));
            return;
        }
        if (SipClient.DEBUG) System.out.println("SipClient: Searching for a user");
        SipUser curuser = (SipUser) users.get(touri.getUsername());
        if (curuser == null) {
            if (defusermode) {
                curuser = defuser;
                if (SipClient.DEBUG) System.out.println("SipClient: Using default user");
            } else {
                if (SipClient.DEBUG) System.out.println("SipClient: No user found");
                sendQuickResponse(currentMessage, new SipStatus(404));
                return;
            }
        }
        if (SipClient.DEBUG) System.out.println("SipClient: Creating new call for user " + curuser.getUri().nameAddr());
        SipCall newcall = new SipCall(curuser, currentMessage.getHeaderData(SipHeader.Call_ID));
        SipCallMember member = newcall.incomingRequest(currentMessage);
        this.setCurrentCallMemb(member);
        incomingCall(newcall, member);
    }

    /**
     * Pass response message to its corresponding call
     */
    private void incomingResponseMessage(SipResponseMessage currentMessage) {
        if (SipClient.DEBUG) System.out.println("SipClient: Received: \n" + currentMessage.message());
        String callid = currentMessage.getHeaderData(SipHeader.Call_ID);
        if (callid == null) return;
        for (ListIterator li = calls.listIterator(); li.hasNext(); ) {
            SipCall curcall = (SipCall) li.next();
            if (callid.equals(curcall.getCallId())) {
                curcall.incomingResponse(currentMessage);
                return;
            }
        }
        if (SipClient.DEBUG) System.err.println("No call found for this message");
    }

    /**
     * Check to see if what was sent in Accept: can be read here
     */
    private boolean acceptHeaderBad(String acceptHeader) {
        String[] acceptibleHeaders = { "application/sdp", "application/cpim-pidf+xml", "text/plain" };
        for (int i = 0; i < acceptibleHeaders.length; i++) {
            if (acceptibleHeaders[i].equals(acceptHeader)) {
                return false;
            }
        }
        return true;
    }

    /**
     * determine an Accept header that is appropriate for this message
     */
    private String getAcceptHeader(SipRequestMessage message) {
        switch(message.getMethod()) {
            case Sip.INVITE:
                return "application/sdp";
            case Sip.OPTIONS:
            case Sip.MESSAGE:
                return "text/plain";
            case Sip.SUBSCRIBE:
                return "application/cpim-pidf.xml";
        }
        return null;
    }

    /**
     * sendQuickResponse
     */
    public void sendQuickResponse(SipMessage origmessage, SipStatus status) {
        sendQuickResponse(origmessage, status, null, null);
    }

    /**
   * sendQuickResponse
   */
    private void sendQuickResponse(SipMessage origmessage, SipStatus status, String body, MimeContentType bodytype) {
        SipResponseMessage msg = new SipResponseMessage();
        SipVia topvia;
        String sendaddr;
        topvia = origmessage.getViaList().getTopmostVia();
        MessageSocket outsocket = MessageSocketFactory.getMessageSocket(topvia.getTransport());
        if (outsocket == null) return;
        msg.setStatus(status);
        msg.setViaList(origmessage.getViaList());
        msg.insertHeader(SipHeader.From, origmessage.getHeaderData(SipHeader.From));
        msg.insertHeader(SipHeader.To, origmessage.getHeaderData(SipHeader.To));
        msg.insertHeader(SipHeader.CSeq, origmessage.getHeaderData(SipHeader.CSeq));
        msg.insertHeader(SipHeader.Call_ID, origmessage.getHeaderData(SipHeader.Call_ID));
        if (origmessage.hasHeader(SipHeader.Require)) {
            msg.insertHeader(SipHeader.Unsupported, origmessage.getHeaderData(SipHeader.Require));
        }
        if ((status.getCode() >= 300) && (status.getCode() < 400)) {
            msg.getContactList().addToHead(forwarduri);
        }
        if (bodytype != null) {
            msg.insertHeader(SipHeader.Content_Type, bodytype.type());
        }
        msg.setBody(body);
        msg.insertHeader(SipHeader.Content_Length, String.valueOf(msg.getContentLength()));
        msg.insertHeader(SipHeader.User_Agent, getUserAgent());
        if (topvia.hasMaddrParam()) {
            if (SipClient.DEBUG) System.out.println("SipClient: Using address from maddr via parameter");
            sendaddr = topvia.getMaddrParam();
        } else if (topvia.hasReceivedParam()) {
            if (SipClient.DEBUG) System.out.println("SipClient: Using address from received via parameter");
            sendaddr = topvia.getReceivedParam();
        } else {
            sendaddr = topvia.getHostname();
        }
        ProxyLookup pl = ProxyServer.getProxyLookup();
        SipUri sendtoUri = null;
        try {
            sendtoUri = pl.getConnectionUri(sendaddr);
        } catch (SipProxyException spe) {
            System.err.println("Unable to send message because: " + spe.toString());
            return;
        }
        if (SipClient.DEBUG) System.out.println("SipClient: Really ending to " + sendtoUri.getHostname() + " port " + topvia.getPortNumber());
        outsocket.connect(sendtoUri.getHostname(), topvia.getPortNumber());
        if (SipClient.DEBUG) System.out.println("SipClient: Sending:");
        if (SipClient.DEBUG) System.out.println(msg.message());
        String logmessage;
        logmessage = ": Sending message:\n" + msg.message() + "\n: End sent message";
        loggerfd.println(logmessage);
        outsocket.send(msg.message(), msg.message().length());
    }

    /**
   * addUser
   * @param user
   */
    void addUser(SipUser user) {
        SipUri userUri = user.getUri();
        if (!users.containsKey(userUri.getUsername())) {
            users.put(userUri.getUsername(), user);
        }
    }

    /**
   * deleteUser
   * @param user
   */
    private void deleteUser(SipUser user) {
        users.remove(user.getUri().getUsername());
    }

    /**
     * addCall
     * @param call
     */
    public void addCall(SipCall call) {
        if (!calls.contains(call)) {
            calls.add(call);
        }
        callListUpdated();
    }

    /**
     * deleteCall
     * @param call
     */
    public void deleteCall(SipCall call) {
        calls.remove(call);
        callListUpdated();
    }

    /**
     * sendRequest
     * @param msg
     * @param contact
     */
    void sendRequest(SipRequestMessage msg, boolean contact) {
        SipVia regvia = new SipVia();
        regvia.setTransport(SipVia.UDP);
        regvia.setHostname(Sip.getLocalAddress());
        regvia.setPortNumber(listener.getPortNumber());
        msg.getViaList().insertTopmostVia(regvia);
        msg.insertHeader(SipHeader.Content_Length, String.valueOf(msg.getContentLength()));
        msg.insertHeader(SipHeader.User_Agent, getUserAgent());
        if (acceptLanguage != null) msg.insertHeader(SipHeader.Accept_Language, acceptLanguage);
        if (maxforwards != 0) {
            msg.insertHeader(SipHeader.Max_Forwards, String.valueOf(maxforwards));
        }
        if (contact) {
            if (msg.getMethod() == Sip.REGISTER) {
                contacturi.setShowMethods(true);
            } else {
                contacturi.setShowMethods(false);
            }
            msg.getContactList().addToHead(contacturi);
        }
        msg.setTimestamp();
        msg.setTimeTick(500);
        msg.incrSendCount();
        MessageSocket sendsocket = MessageSocketFactory.getMessageSocket(MessageSocket.UDP);
        int connResult = 0;
        if (useproxy) {
            connResult = sendsocket.connect(proxy, proxyport);
        } else {
            String sendtoaddr;
            if (msg.getRequestUri().hasMaddrParam()) {
                sendtoaddr = msg.getRequestUri().getMaddrParam();
            } else if (msg.hasSendToAddress()) {
                sendtoaddr = msg.getSendToAddress().getHostname();
            } else {
                sendtoaddr = msg.getRequestUri().getHostname();
            }
            System.out.println("The contact uri is " + contacturi.uri());
            System.out.println("Got sendtoaddr of " + sendtoaddr);
            ProxyLookup pl = null;
            if ((pl = ProxyServer.getProxyLookup()) == null) return;
            SipUri sendtoUri = null;
            try {
                sendtoUri = pl.getConnectionUri(sendtoaddr);
            } catch (SipProxyException spe) {
                System.err.println("Unable to send message because " + spe.toString());
                return;
            }
            if (msg.getMethod() == Sip.SUBSCRIBE || msg.getMethod() == Sip.MESSAGE) {
                SipUri subUri = new SipUri(msg.getHeaderData(SipHeader.To));
                msg.setRequestUri(subUri);
            } else {
                msg.setRequestUri(sendtoUri);
            }
            InetAddress sendInetAddress = null;
            try {
                sendInetAddress = InetAddress.getByName(sendtoUri.getHostname());
            } catch (UnknownHostException uhe) {
                System.err.println("Unable to connect to host: " + sendtoUri.getHostname() + " because " + uhe);
                return;
            }
            if (sendInetAddress.isMulticastAddress()) {
                sendsocket = MessageSocketFactory.getMessageSocket(MessageSocket.MCAST);
            }
            if (SipClient.DEBUG) System.out.println("SipClient: Really sending to " + sendtoUri.getHostname() + ":" + msg.getRequestUri().getPortNumber());
            if (SipClient.DEBUG) System.out.println("SipClient: RequestURI is " + msg.getRequestUri());
            connResult = sendsocket.connect(sendInetAddress, msg.getRequestUri().getPortNumber());
        }
        if (connResult < 0) {
            if (SipClient.DEBUG) System.err.println("Error connecting to remote host");
        } else {
            if (SipClient.DEBUG) System.out.println("SipClient: Sending:\n" + msg.message());
            sendsocket.send(msg.message(), msg.message().length());
        }
    }

    /**
     * sendResponse
     * @param msg
     * @param contact
     */
    public void sendResponse(SipResponseMessage msg, boolean contact) {
        SipVia topvia;
        String sendaddr;
        topvia = msg.getViaList().getTopmostVia();
        MessageSocket outsocket = MessageSocketFactory.getMessageSocket(topvia.getTransport());
        if (outsocket == null) return;
        msg.insertHeader(SipHeader.Content_Length, String.valueOf(msg.getContentLength()));
        msg.insertHeader(SipHeader.User_Agent, getUserAgent());
        if (contact) {
            System.out.println("Adding contact:" + contacturi.nameAddr());
            contacturi.setShowMethods(false);
            msg.getContactList().addToHead(contacturi);
        }
        if (msg.getHeaderData(SipHeader.CSeq).indexOf("OPTIONS") > 0) {
            msg.insertHeader(SipHeader.Allow, "INVITE, OPTIONS, ACK, BYE, CANCEL, " + "MESSAGE, SUBSCRIBE, NOTIFY");
        }
        if (topvia.hasMaddrParam()) {
            if (SipClient.DEBUG) System.out.println("SipClient: Using address from maddr via parameter\n");
            sendaddr = topvia.getMaddrParam();
        } else if (topvia.hasReceivedParam()) {
            if (SipClient.DEBUG) System.out.println("SipClient: Using address from received via parameter\n");
            sendaddr = topvia.getReceivedParam();
        } else {
            sendaddr = topvia.getHostname();
        }
        ProxyLookup pl = null;
        if ((pl = ProxyServer.getProxyLookup()) == null) return;
        SipUri sendtoUri = null;
        try {
            sendtoUri = pl.getConnectionUri(sendaddr);
        } catch (SipProxyException spe) {
            System.err.println("Unable to send message because: " + spe.toString());
            return;
        }
        if (SipClient.DEBUG) System.out.println("SipClient: Really sending to " + sendaddr + " port " + topvia.getPortNumber());
        if (!outsocket.setHostname(sendtoUri.getHostname())) {
            return;
        }
        if (outsocket.connect(sendtoUri.getHostname(), topvia.getPortNumber()) < 0) return;
        if (SipClient.DEBUG) System.out.println("SipClient: Sending:\n" + msg.message());
        String logmessage;
        logmessage = ": Sending message:\n" + msg.message() + "\n: End sent message";
        loggerfd.println(logmessage);
        outsocket.send(msg.message(), msg.message().length());
    }

    /**
   * sendRaw
   * @param msg
   */
    void sendRaw(SipRequestMessage msg) {
        if (SipClient.DEBUG) System.out.println("SipClient: Sending:");
        if (SipClient.DEBUG) System.out.println(msg.message());
        String logmessage;
        logmessage = ": Sending message:\n" + msg.message() + "\n: End sent message";
        loggerfd.println(logmessage);
        MessageSocket sendsocket = MessageSocketFactory.getMessageSocket(SipVia.UDP);
        if (useproxy) {
            if (!sendsocket.setHostname(proxy)) {
                return;
            }
            sendsocket.connect((String) null, proxyport);
        } else {
            String sendtoaddr;
            if (msg.getRequestUri().hasMaddrParam()) {
                sendtoaddr = msg.getRequestUri().getMaddrParam();
            } else {
                sendtoaddr = msg.getRequestUri().getHostname();
            }
            ProxyLookup pl = ProxyServer.getProxyLookup();
            SipUri sendtoUri = null;
            try {
                sendtoUri = pl.getConnectionUri(sendtoaddr);
            } catch (SipProxyException spe) {
                System.err.println("Unable to send message because: " + spe.toString());
                return;
            }
            sendsocket.connect(sendtoUri.getHostname(), msg.getRequestUri().getPortNumber());
        }
        sendsocket.send(msg.message(), msg.message().length());
    }

    /**
   * callTypeUpdated
   */
    void callTypeUpdated() {
        callListUpdated();
    }

    /**
     * Check if incoming messages are available
     */
    public boolean haveMessages() {
        return (!mQueue.isEmpty());
    }

    /**
     * Test method
     *
     */
    public static void main(String[] args) {
    }
}
