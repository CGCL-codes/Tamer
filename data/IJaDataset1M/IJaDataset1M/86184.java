package com.mindbright.ssh2;

import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import com.mindbright.jca.security.SecureRandom;
import com.mindbright.util.SecureRandomAndPad;
import com.mindbright.util.Log;

/**
 * This class implements the connection layer of the secure shell version 2
 * (ssh2) protocol stack. This layer contains the multiplexing of the secure
 * connection to the server into several different channels which can be used
 * for port forwarding and terminal sessions as defined in the connection
 * protocol spec.
 * <p>
 * To create a <code>SSH2Connection</code> instance a connected
 * <code>SSH2Transport</code> along with an authenticated
 * <code>SSH2UserAuth</code> must be created first to be passed to the
 * constructor. Optionally a <code>SSH2ConnectionEventHandler</code> can be
 * supplied to be able to monitor and control the connection layer. A log
 * handler can also be set, though by default the connection layer uses the same
 * log handler as the transport layer. All preferences are taken from the
 * <code>SSH2Preferences</code> set in the transport layer.
 * <p>
 * The connection layer must be hooked into the transport layer explicitly by
 * calling the method <code>setConnection</code> on the
 * <code>SSH2Transport</code>. Once the connection layer is hooked up to the
 * transport layer channels can be created. There are basically four types of
 * channels: session, local forward, remote forward, and X11 forward. There are
 * methods for creating local and remote forwards aswell as session channels,
 * however, X11 channels must be created through session channels.
 *
 * @see SSH2Transport
 * @see SSH2UserAuth
 * @see SSH2ConnectionEventHandler
 * @see SSH2Preferences
 * @see SSH2Channel
 * @see SSH2Connector
 * @see SSH2Listener
 */
public final class SSH2Connection implements Runnable {

    public static final int MAX_ACTIVE_CHANNELS = 1024;

    public static final String GL_REQ_START_FORWARD = "tcpip-forward";

    public static final String GL_REQ_CANCEL_FORWARD = "cancel-tcpip-forward";

    public static final String CH_REQ_PTY = "pty-req";

    public static final String CH_REQ_X11 = "x11-req";

    public static final String CH_REQ_ENV = "env";

    public static final String CH_REQ_SHELL = "shell";

    public static final String CH_REQ_EXEC = "exec";

    public static final String CH_REQ_SUBSYSTEM = "subsystem";

    public static final String CH_REQ_WINCH = "window-change";

    public static final String CH_REQ_XONOFF = "xon-xoff";

    public static final String CH_REQ_SIGNAL = "signal";

    public static final String CH_REQ_EXIT_STAT = "exit-status";

    public static final String CH_REQ_EXIT_SIG = "exit-signal";

    public static final String CH_REQ_AUTH_AGENT = "auth-agent-req";

    public static final String CH_REQ_AUTH_AGENT1 = "auth-ssh1-agent-req";

    public static final String CH_REQ_BREAK = "break";

    public static final String CH_REQ_OPENSSH_KEEPALIVE = "keepalive@openssh.com";

    public static final String CHAN_FORWARDED_TCPIP = "forwarded-tcpip";

    public static final String CHAN_DIRECT_TCPIP = "direct-tcpip";

    public static final String CHAN_SESSION = "session";

    public static final String CHAN_X11 = "x11";

    public static final String CHAN_AUTH_AGENT = "auth-agent";

    public static final int CH_TYPE_FWD_TCPIP = 0;

    public static final int CH_TYPE_DIR_TCPIP = 1;

    public static final int CH_TYPE_SESSION = 2;

    public static final int CH_TYPE_X11 = 3;

    public static final int CH_TYPE_AUTH_AGENT = 4;

    static final String[] channelTypes = { CHAN_FORWARDED_TCPIP, CHAN_DIRECT_TCPIP, CHAN_SESSION, CHAN_X11, CHAN_AUTH_AGENT };

    private SSH2Transport transport;

    private SSH2UserAuth userAuth;

    private SSH2ConnectionEventHandler eventHandler;

    private SSH2Channel[] channels;

    private int totalChannels;

    private int nextEmptyChan;

    private SSH2Connector connector;

    private Hashtable remoteForwards;

    private Hashtable remoteFilters;

    private Hashtable localForwards;

    private byte[] x11RealCookie;

    private byte[] x11FakeCookie;

    private boolean x11Single;

    private int x11Mappings;

    private Object reqMonitor;

    private boolean reqStatus;

    private Log connLog;

    /**
     * Basic constructor used when there is no need for event handler.
     *
     * @param userAuth  the authentication layer
     * @param transport the transport layer
     */
    public SSH2Connection(SSH2UserAuth userAuth, SSH2Transport transport) {
        this(userAuth, transport, null);
    }

    /**
     * Constructor used when there need for event handler.
     *
     * @param userAuth     the authentication layer
     * @param transport    the transport layer
     * @param eventHandler the event handler (may be <code>null</code>)
     *
     * @see SSH2ConnectionEventHandler
     */
    public SSH2Connection(SSH2UserAuth userAuth, SSH2Transport transport, SSH2ConnectionEventHandler eventHandler) {
        this.userAuth = userAuth;
        this.transport = transport;
        this.eventHandler = (eventHandler != null ? eventHandler : new SSH2ConnectionEventAdapter());
        this.channels = new SSH2Channel[64];
        this.totalChannels = 0;
        this.nextEmptyChan = 0;
        this.remoteForwards = new Hashtable();
        this.remoteFilters = new Hashtable();
        this.localForwards = new Hashtable();
        this.x11RealCookie = null;
        this.x11FakeCookie = null;
        this.x11Single = false;
        this.x11Mappings = 0;
        this.connLog = transport.getLog();
        this.reqMonitor = new Object();
        if (transport.incompatibleBuggyChannelClose) {
            startChannelReaper();
        }
    }

    void processGlobalMessage(SSH2TransportPDU pdu) {
        switch(pdu.pktType) {
            case SSH2.MSG_GLOBAL_REQUEST:
                String type = pdu.readJavaString();
                boolean wantReply = pdu.readBoolean();
                if (type.equals(GL_REQ_START_FORWARD)) {
                } else if (type.equals(GL_REQ_CANCEL_FORWARD)) {
                } else {
                }
                break;
            case SSH2.MSG_REQUEST_SUCCESS:
                synchronized (reqMonitor) {
                    reqStatus = true;
                    reqMonitor.notify();
                }
                break;
            case SSH2.MSG_REQUEST_FAILURE:
                synchronized (reqMonitor) {
                    reqStatus = false;
                    reqMonitor.notify();
                }
                break;
            case SSH2.MSG_CHANNEL_OPEN:
                getConnector().channelOpen(pdu);
                pdu = null;
                break;
        }
        if (pdu != null) {
            pdu.release();
        }
    }

    void processChannelMessage(SSH2TransportPDU pdu) {
        int channelId = pdu.readInt();
        SSH2Channel channel = channels[channelId];
        if (channel == null) {
            String msg = "Error, received message to non-existent channel";
            connLog.error("SSH2Connection", "processChannelMessage", msg);
            connLog.debug2("SSH2Connection", "processChannelMessage", "got message of type: " + SSH2.msgTypeString(pdu.pktType), pdu.getData(), pdu.getPayloadOffset(), pdu.getPayloadLength());
            fatalDisconnect(SSH2.DISCONNECT_PROTOCOL_ERROR, msg);
            return;
        }
        switch(pdu.pktType) {
            case SSH2.MSG_CHANNEL_OPEN_CONFIRMATION:
                channel.openConfirmation(pdu);
                break;
            case SSH2.MSG_CHANNEL_OPEN_FAILURE:
                int reasonCode = pdu.readInt();
                String reasonText;
                String langTag;
                if (transport.incompatibleChannelOpenFail) {
                    reasonText = "";
                    langTag = "";
                } else {
                    reasonText = pdu.readJavaString();
                    langTag = pdu.readJavaString();
                }
                channel.openFailure(reasonCode, reasonText, langTag);
                break;
            case SSH2.MSG_CHANNEL_WINDOW_ADJUST:
                channel.windowAdjust(pdu);
                break;
            case SSH2.MSG_CHANNEL_DATA:
                channel.data(pdu);
                pdu = null;
                break;
            case SSH2.MSG_CHANNEL_EXTENDED_DATA:
                channel.extData(pdu);
                pdu = null;
                break;
            case SSH2.MSG_CHANNEL_EOF:
                channel.recvEOF();
                break;
            case SSH2.MSG_CHANNEL_CLOSE:
                channel.recvClose();
                break;
            case SSH2.MSG_CHANNEL_REQUEST:
                channel.handleRequest(pdu);
                break;
            case SSH2.MSG_CHANNEL_SUCCESS:
                channel.requestSuccess(pdu);
                break;
            case SSH2.MSG_CHANNEL_FAILURE:
                channel.requestFailure(pdu);
                break;
        }
        if (pdu != null) {
            pdu.release();
        }
    }

    /**
     * Gets our transport layer.
     *
     * @return the transport layer
     */
    public SSH2Transport getTransport() {
        return transport;
    }

    /**
     * Sets the event handler to use.
     *
     * @param eventHandler the event handler to use
     */
    public void setEventHandler(SSH2ConnectionEventHandler eventHandler) {
        if (eventHandler != null) {
            this.eventHandler = eventHandler;
        }
    }

    /**
     * Gets the event handler currently in use.
     *
     * @return the event handler currently in use
     */
    public SSH2ConnectionEventHandler getEventHandler() {
        return eventHandler;
    }

    /**
     * Gets the preferences set in the transport layer.
     *
     * @return the preferences
     */
    public SSH2Preferences getPreferences() {
        return transport.getOurPreferences();
    }

    /**
     * Gets the log handler currently in use.
     *
     * @return the log handler currently in use
     */
    public Log getLog() {
        return connLog;
    }

    /**
     * Sets the log handler to use.
     *
     * @param the log handler to use
     */
    public void setLog(Log log) {
        connLog = log;
    }

    /**
     * Gets the <code>SecureRandom</code> currently in use (i.e. from the
     * transport layer).
     *
     * @return the <code>SecureRandom</code> in use
     */
    public SecureRandom getSecureRandom() {
        return transport.getSecureRandom();
    }

    /**
     * Transmits the given PDU (by sending it to the transport layer, no
     * processing is needed at this point).
     *
     * @param pdu packet to send
     */
    public void transmit(SSH2TransportPDU pdu) {
        transport.transmit(pdu);
    }

    /**
     * Disconnects from peer using the DISCONNECT packet type with the given
     * reason and description. See the class <code>SSH2</code> for reason codes.
     * This is only a convenience method which calls the same method on the
     * transport layer.
     *
     * @param reason      the reason code
     * @param description the textual description for the cause of disconnect
     *
     * @see SSH2
     */
    public void fatalDisconnect(int reason, String description) {
        transport.fatalDisconnect(reason, description);
    }

    /**
     * Gets the singleton instance of the <code>SSH2Connector</code> which is
     * used by the connection layer to connect remote forwards through to local
     * hosts when they are opened.
     *
     * @return the singleton connector
     */
    public SSH2Connector getConnector() {
        if (connector == null) {
            connector = new SSH2Connector(this);
        }
        return connector;
    }

    /**
     * Gets the local target host address and port pair of a remote forward
     * identified by the given remote address and port pair. This function is
     * used to locate the local target of a remote forward when it is opened.
     *
     * @param remoteAddr the remote address of the forward
     * @param remotePort the remote port of the forward
     *
     * @return the address and port, the address beeing at index 0 and the port
     * beeing at inde 1.
     */
    public synchronized String[] getForwardTarget(String remoteAddr, int remotePort) {
        String[] target = null;
        String tgStr = (String) remoteForwards.get(remoteAddr + ":" + remotePort);
        if (tgStr != null) {
            target = new String[2];
            int i = tgStr.indexOf(":");
            target[0] = tgStr.substring(0, i);
            target[1] = tgStr.substring(i + 1);
        }
        return target;
    }

    /**
     * Gets the filter factory instance for a remote forward identified by the
     * given remote address and port pair.
     *
     * @param remoteAddr the remote address of the forward
     * @param remotePort the remote port of the forward
     *
     * @return the stream filter factory instance
     */
    public synchronized SSH2StreamFilterFactory getForwardFilterFactory(String remoteAddr, int remotePort) {
        return (SSH2StreamFilterFactory) remoteFilters.get(remoteAddr + ":" + remotePort);
    }

    /**
     * Gets the <code>SSH2Listener</code> instance of a local forward if it is
     * set up.
     *
     * @param localAddr the local address of the forward
     * @param locaPort the local port of the forward
     *
     * @return the listener instance for the given forward or <code>null</code>
     * if none is set
     */
    public synchronized SSH2Listener getLocalListener(String localAddr, int localPort) {
        return (SSH2Listener) localForwards.get(localAddr + ":" + localPort);
    }

    /**
     * Creates a new remote forward from the given remote address and port on
     * the server to the local address and port.
     *
     * @param remoteAddr the remote address where the server listens
     * @param remotePort the remote port where the server listens
     * @param localAddr  the local address to connect through to
     * @param localAddr the local port to connect through to
     */
    public synchronized void newRemoteForward(String remoteAddr, int remotePort, String localAddr, int localPort) {
        newRemoteForward(remoteAddr, remotePort, localAddr, localPort, (SSH2StreamFilterFactory) null);
    }

    /**
     * Creates a new remote forward from the given remote address and port on
     * the server to the local address and port using the given filter factory
     * to insert filters in the input/output streams of the forwarded channels.
     *
     * @param remoteAddr    the remote address where the server listens
     * @param remotePort    the remote port where the server listens
     * @param localAddr     the local address to connect through to
     * @param localAddr     the local port to connect through to
     * @param filterFactory the filter factory instance to use for producing
     * filters.
     */
    public synchronized void newRemoteForward(String remoteAddr, int remotePort, String localAddr, int localPort, SSH2StreamFilterFactory filterFactory) {
        newRemoteForward(remoteAddr, remotePort, localAddr, localPort, filterFactory, false);
    }

    /**
     * Creates a new remote forward from the given remote address and port on
     * the server to the local address and port using the given filter factory
     * to insert filters in the input/output streams of the forwarded
     * channel. This is a blocking version of the method
     * <code>newRemoteForward</code> with the same parameters which waits until
     * a result is reported from the server which indicates whether the forward
     * could be set up or not.
     *
     * @param remoteAddr    the remote address where the server listens
     * @param remotePort    the remote port where the server listens
     * @param localAddr     the local address to connect through to
     * @param localAddr     the local port to connect through to
     * @param filterFactory the filter factory instance to use for producing
     * filters.
     */
    public boolean newRemoteForwardBlocking(String remoteAddr, int remotePort, String localAddr, int localPort, SSH2StreamFilterFactory filterFactory) {
        synchronized (reqMonitor) {
            newRemoteForward(remoteAddr, remotePort, localAddr, localPort, filterFactory, true);
            try {
                reqMonitor.wait();
            } catch (InterruptedException e) {
            }
            return reqStatus;
        }
    }

    private synchronized void newRemoteForward(String remoteAddr, int remotePort, String localAddr, int localPort, SSH2StreamFilterFactory filterFactory, boolean wantReply) {
        connLog.debug("SSH2Connection", "newRemoteForward", remoteAddr + ":" + remotePort + "->" + localAddr + ":" + localPort);
        remoteForwards.put(remoteAddr + ":" + remotePort, localAddr + ":" + localPort);
        if (filterFactory != null) {
            remoteFilters.put(remoteAddr + ":" + remotePort, filterFactory);
        }
        SSH2TransportPDU pdu = SSH2TransportPDU.createOutgoingPacket(SSH2.MSG_GLOBAL_REQUEST);
        pdu.writeString(GL_REQ_START_FORWARD);
        pdu.writeBoolean(wantReply);
        pdu.writeString(remoteAddr);
        pdu.writeInt(remotePort);
        transport.transmit(pdu);
    }

    /**
     * Deletes the remote forward identified by the given remote address and
     * port pair. Note that the channels that was previously opened through this
     * forward are not deleted, only a CANCEL_FORWARD request is sent to the
     * server which deletes the forward on the server preventing further
     * channels to be opened through this forward.
     *
     * @param remoteAddr the remote address of the forward
     * @param remotePort the remote port of the forward
     */
    public synchronized void deleteRemoteForward(String remoteAddr, int remotePort) {
        connLog.debug("SSH2Connection", "deleteRemoteForward", remoteAddr + ":" + remotePort);
        String tgStr = (String) remoteForwards.get(remoteAddr + ":" + remotePort);
        if (tgStr != null) {
            SSH2TransportPDU pdu = SSH2TransportPDU.createOutgoingPacket(SSH2.MSG_GLOBAL_REQUEST);
            pdu.writeString(GL_REQ_CANCEL_FORWARD);
            pdu.writeBoolean(true);
            pdu.writeString(remoteAddr);
            pdu.writeInt(remotePort);
            transport.transmit(pdu);
            remoteForwards.remove(remoteAddr + ":" + remotePort);
        }
    }

    /**
     * Creates a new local forward from the given local address and port to
     * the remote address and port on the server side.
     *
     * @param localAddr     the local address to listen to
     * @param localAddr     the local port to listen to
     * @param remoteAddr    the remote address where the connects to
     * @param remotePort    the remote port where the connects to
     *
     * @return a listener instance accepting connections to forward
     */
    public synchronized SSH2Listener newLocalForward(String localAddr, int localPort, String remoteAddr, int remotePort) throws IOException {
        return newLocalForward(localAddr, localPort, remoteAddr, remotePort, (SSH2StreamFilterFactory) null);
    }

    /**
     * Creates a new local forward from the given local address and port to the
     * remote address and port on the server side using the given filter factory
     * to insert filters in the input/output streams of the forwarded channels.
     *
     * @param localAddr     the local address to listen to
     * @param localAddr     the local port to listen to
     * @param remoteAddr    the remote address where the connects to
     * @param remotePort    the remote port where the connects to
     * @param filterFactory the filter factory instance to use for producing
     * filters.
     *
     * @return a listener instance accepting connections to forward
     */
    public synchronized SSH2Listener newLocalForward(String localAddr, int localPort, String remoteAddr, int remotePort, SSH2StreamFilterFactory filterFactory) throws IOException {
        connLog.debug("SSH2Connection", "newLocalForward", localAddr + ":" + localPort + "->" + remoteAddr + ":" + remotePort);
        SSH2Listener listener = new SSH2Listener(localAddr, localPort, remoteAddr, remotePort, this, filterFactory);
        localForwards.put(localAddr + ":" + localPort, listener);
        return listener;
    }

    /**
     * Creates a new internal forward to
     * the remote address and port on the server side.
     *
     * @param remoteAddr    the remote address where the connects to
     * @param remotePort    the remote port where the connects to
     *
     * @return an internal channel
     */
    public synchronized SSH2InternalChannel newLocalInternalForward(String remoteAddr, int remotePort) {
        return newLocalInternalForward(remoteAddr, remotePort, null);
    }

    /**
     * Creates a new internal forward to
     * remote address and port on the server side using the given filter
     * factory to insert filters in the input/output streams of the forwarded
     * channels.
     *
     * @param remoteAddr    the remote address where the connects to
     * @param remotePort    the remote port where the connects to
     * @param filterFactory the filter factory instance to use for producing
     * filters.
     *
     * @return an internal channel
     */
    public synchronized SSH2InternalChannel newLocalInternalForward(String remoteAddr, int remotePort, SSH2StreamFilterFactory filterFactory) {
        connLog.debug("SSH2Connection", "newLocalInternalForward", "->" + remoteAddr + ":" + remotePort);
        SSH2InternalChannel chan = new SSH2InternalChannel(CH_TYPE_DIR_TCPIP, this);
        if (filterFactory != null) {
            chan.applyFilter(filterFactory.createFilter(this, chan));
        }
        connectLocalChannel(chan, remoteAddr, remotePort, "127.0.0.1", remotePort);
        return chan;
    }

    /**
     * Deletes the local forward identified by the given local address and port
     * pair. Note that the channels that was previously opened through this
     * forward are not deleted, only the corresponding listener is stopped
     * preventing further channels to be opened through this forward.
     *
     * @param localAddr     the local address of the forward
     * @param localAddr the local port of the forward
     */
    public synchronized void deleteLocalForward(String localAddr, int localPort) {
        connLog.debug("SSH2Connection", "deleteLocalForward", localAddr + ":" + localPort);
        SSH2Listener listener = (SSH2Listener) localForwards.get(localAddr + ":" + localPort);
        if (listener != null) {
            listener.stop();
            localForwards.remove(localAddr + ":" + localPort);
        }
    }

    /**
     * Creates a new session channel.
     *
     * @return the new session channel
     */
    public synchronized SSH2SessionChannel newSession() {
        return newSession((SSH2StreamFilterFactory) null);
    }

    /**
     * Creates a new session channel using the given filter for filtering the
     * standard input/output streams of the session.
     *
     * @param filter the filter to use
     *
     * @return the new session channel
     */
    public synchronized SSH2SessionChannel newSession(SSH2StreamFilterFactory filterFactory) {
        SSH2SessionChannel channel = new SSH2SessionChannel(this);
        if (filterFactory != null) {
            channel.applyFilter(filterFactory.createFilter(this, channel));
        }
        SSH2TransportPDU pdu = getChannelOpenPDU(channel);
        transmit(pdu);
        return channel;
    }

    /**
     * Creates a new session channel attaching its standard input/output streams
     * to the given terminal adapter. It is up to the terminal adapter
     * implementation to attach itself to the I/O streams of the session
     * channel. For this purpose the interface method <code>attach</code> is
     * called before the channel open message is sent to the server so the
     * terminal adapter is attached before I/O is started.
     *
     * @param termAdapter the terminal adapter to attach to the session
     *
     * @return the new session channel
     */
    public synchronized SSH2SessionChannel newTerminal(SSH2TerminalAdapter termAdapter) {
        SSH2SessionChannel channel = new SSH2SessionChannel(this);
        SSH2TransportPDU pdu = getChannelOpenPDU(channel);
        termAdapter.attach(channel);
        transmit(pdu);
        return channel;
    }

    public void setSocketOptions(String desc, Socket sock) throws IOException {
        transport.setSocketOptions(desc, sock);
    }

    synchronized boolean hasX11Mapping() {
        boolean hasMapping = (x11Mappings > 0);
        if (x11Single) {
            x11Mappings--;
        }
        return hasMapping;
    }

    synchronized void setX11Mapping(boolean single) {
        x11Single = single;
        x11Mappings++;
    }

    synchronized void clearX11Mapping() {
        if (x11Mappings > 0) {
            x11Mappings--;
        }
    }

    byte[] getX11FakeCookie() {
        if (x11FakeCookie == null) {
            x11FakeCookie = new byte[16];
            SecureRandomAndPad srap = (SecureRandomAndPad) transport.getSecureRandom();
            srap.nextPadBytes(x11FakeCookie, 0, 16);
        }
        return x11FakeCookie;
    }

    void setX11RealCookie(byte[] cookie) {
        x11RealCookie = cookie;
    }

    byte[] getX11RealCookie() {
        if (x11RealCookie == null) {
            x11RealCookie = getX11FakeCookie();
        }
        return x11RealCookie;
    }

    synchronized void terminate() {
        if (connector != null) {
            connector.stop();
        }
        Enumeration listeners = localForwards.elements();
        while (listeners.hasMoreElements()) {
            ((SSH2Listener) listeners.nextElement()).stop();
        }
        for (int i = 0; i < channels.length; i++) {
            if (channels[i] != null) {
                channels[i].close();
            }
        }
        if (reaperActive) {
            stopChannelReaper();
        }
    }

    synchronized void addChannel(SSH2Channel channel) {
        int newChan = nextEmptyChan;
        if (nextEmptyChan < channels.length) {
            int i;
            for (i = nextEmptyChan + 1; i < channels.length; i++) if (channels[i] == null) break;
            nextEmptyChan = i;
        } else {
            if (channels.length + 16 > MAX_ACTIVE_CHANNELS) {
                fatalDisconnect(SSH2.DISCONNECT_TOO_MANY_CONNECTIONS, "Number of channels exceeded maximum");
                return;
            }
            SSH2Channel[] tmp = new SSH2Channel[channels.length + 16];
            System.arraycopy(channels, 0, tmp, 0, channels.length);
            channels = tmp;
            nextEmptyChan++;
        }
        channel.channelId = newChan;
        channels[newChan] = channel;
        totalChannels++;
        eventHandler.channelAdded(this, channel);
    }

    synchronized void killChannel(SSH2Channel channel) {
        if (channel == null || channel.channelId == -1 || channel.channelId >= channels.length || channels[channel.channelId] == null) {
            connLog.error("SSH2Connection", "killChannel", "ch. # " + (channel != null ? String.valueOf(channel.getId()) : "<null>") + " not present");
            return;
        }
        totalChannels--;
        channels[channel.channelId] = null;
        if (channel.channelId < nextEmptyChan) nextEmptyChan = channel.channelId;
        eventHandler.channelDeleted(this, channel);
    }

    void delChannel(SSH2Channel channel) {
        if (reaperActive) {
            life.addElement(channel);
        } else {
            killChannel(channel);
        }
    }

    SSH2TransportPDU getChannelOpenPDU(SSH2Channel channel) {
        SSH2TransportPDU pdu = SSH2TransportPDU.createOutgoingPacket(SSH2.MSG_CHANNEL_OPEN);
        pdu.writeString(channelTypes[channel.channelType]);
        pdu.writeInt(channel.channelId);
        pdu.writeInt(channel.rxInitWinSz);
        pdu.writeInt(channel.rxMaxPktSz);
        return pdu;
    }

    public void connectLocalChannel(SSH2Channel channel, String remoteAddr, int remotePort, String originAddr, int originPort) {
        SSH2TransportPDU pdu = getChannelOpenPDU(channel);
        pdu.writeString(remoteAddr);
        pdu.writeInt(remotePort);
        pdu.writeString(originAddr);
        pdu.writeInt(originPort);
        transmit(pdu);
    }

    private Vector life;

    private volatile boolean reaperActive;

    /**
     * Start a new thread which repeatedly tries to kill channels
     * which should be killed.
     */
    protected void startChannelReaper() {
        this.life = new Vector();
        this.reaperActive = true;
        Thread reaper = new Thread(this, "SSH2ChannelReaper");
        reaper.setDaemon(true);
        reaper.setPriority(Thread.MIN_PRIORITY);
        reaper.start();
    }

    /**
     * Stop the channel reaper.
     */
    protected void stopChannelReaper() {
        reaperActive = false;
    }

    /**
     * The <code>run</code> routine which implements the channel reaper.
     */
    public void run() {
        Vector death = new Vector();
        while (reaperActive) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            while (!death.isEmpty()) {
                SSH2Channel toBeKilled = (SSH2Channel) death.firstElement();
                killChannel(toBeKilled);
                death.removeElementAt(0);
            }
            Vector limbo = death;
            death = life;
            life = limbo;
        }
    }
}
