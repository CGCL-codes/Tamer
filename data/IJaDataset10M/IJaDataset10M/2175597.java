package com.ericsson.otp.erlang;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Collection;
import java.lang.ref.WeakReference;

/**
 * <p> Represents a local OTP node. This class is used when you do not
 * wish to manage connections yourself - outgoing connections are
 * established as needed, and incoming connections accepted
 * automatically. This class supports the use of a mailbox API for
 * communication, while management of the underlying communication
 * mechanism is automatic and hidden from the application programmer.
 * </p>
 *
 * <p> Once an instance of this class has been created, obtain one or
 * more mailboxes in order to send or receive messages. The first
 * message sent to a given node will cause a connection to be set up
 * to that node. Any messages received will be delivered to the
 * appropriate mailboxes. </p>
 *
 * <p> To shut down the node, call {@link #close close()}. This will
 * prevent the node from accepting additional connections and it will
 * cause all existing connections to be closed. Any unread messages in
 * existing mailboxes can still be read, however no new messages will
 * be delivered to the mailboxes. </p>
 *
 * <p> Note that the use of this class requires that Epmd (Erlang Port
 * Mapper Daemon) is running on each cooperating host. This class does
 * not start Epmd automatically as Erlang does, you must start it
 * manually or through some other means. See the Erlang documentation
 * for more information about this. </p>
 **/
public class OtpNode extends OtpLocalNode {

    private boolean initDone = false;

    private Acceptor acceptor = null;

    Hashtable connections = null;

    Mailboxes mboxes = null;

    OtpNodeStatus handler;

    /**
   * <p> Create a node using the default cookie. The default
   * cookie is found by reading the first line of the .erlang.cookie
   * file in the user's home directory. The home directory is obtained
   * from the System property "user.home". </p>
   *
   * <p> If the file does not exist, an empty string is used. This
   * method makes no attempt to create the file. </p>
   *
   * @param node the name of this node.
   *
   * @exception IOException if communication could not be initialized.
   *
   **/
    public OtpNode(String node) throws IOException {
        this(node, defaultCookie, 0);
    }

    /**
   * Create a node.
   *
   * @param node the name of this node.
   *
   * @param cookie the authorization cookie that will be used by this
   * node when it communicates with other nodes.
   *
   * @exception IOException if communication could not be initialized.
   *
   **/
    public OtpNode(String node, String cookie) throws IOException {
        this(node, cookie, 0);
    }

    /**
   * Create a node.
   *
   * @param node the name of this node.
   *
   * @param cookie the authorization cookie that will be used by this
   * node when it communicates with other nodes.
   *
   * @param port the port number you wish to use for incoming
   * connections. Specifying 0 lets the system choose an available
   * port.
   *
   * @exception IOException if communication could not be initialized.
   *
   **/
    public OtpNode(String node, String cookie, int port) throws IOException {
        super(node, cookie);
        init(port);
    }

    private synchronized void init(int port) throws IOException {
        if (!initDone) {
            connections = new Hashtable(17, (float) 0.95);
            mboxes = new Mailboxes();
            acceptor = new Acceptor(port);
            initDone = true;
        }
    }

    /**
   * Close the node. Unpublish the node from Epmd (preventing new
   * connections) and close all existing connections.
   **/
    public synchronized void close() {
        acceptor.quit();
        OtpCookedConnection conn;
        Collection coll = connections.values();
        Iterator it = coll.iterator();
        mboxes.clear();
        while (it.hasNext()) {
            conn = (OtpCookedConnection) it.next();
            it.remove();
            conn.close();
        }
        initDone = false;
    }

    protected void finalize() {
        close();
    }

    /**
   * Create an unnamed {@link OtpMbox mailbox} that can be used to
   * send and receive messages with other, similar mailboxes and with
   * Erlang processes. Messages can be sent to this mailbox by using
   * its associated {@link OtpMbox#self pid}.
   *
   * @return a mailbox.
   **/
    public OtpMbox createMbox() {
        return mboxes.create();
    }

    /**
   * Close the specified mailbox with reason 'normal'.
   *
   * @param mbox the mailbox to close.
   *
   * <p> After this operation, the mailbox will no longer be able to
   * receive messages. Any delivered but as yet unretrieved messages
   * can still be retrieved however. </p>
   *
   * <p> If there are links from the mailbox to other {@link
   * OtpErlangPid pids}, they will be broken when this method is
   * called and exit signals with reason 'normal' will be sent. </p>
   *
   **/
    public void closeMbox(OtpMbox mbox) {
        closeMbox(mbox, new OtpErlangAtom("normal"));
    }

    /**
   * Close the specified mailbox with the given reason.
   *
   * @param mbox the mailbox to close.
   * @param reason an Erlang term describing the reason for the termination.
   *
   * <p> After this operation, the mailbox will no longer be able to
   * receive messages. Any delivered but as yet unretrieved messages
   * can still be retrieved however. </p>
   *
   * <p> If there are links from the mailbox to other {@link
   * OtpErlangPid pids}, they will be broken when this method is
   * called and exit signals with the given reason will be sent. </p>
   *
   **/
    public void closeMbox(OtpMbox mbox, OtpErlangObject reason) {
        if (mbox != null) {
            mboxes.remove(mbox);
            mbox.name = null;
            mbox.breakLinks(reason);
        }
    }

    /**
   * Create an named mailbox that can be used to send and receive
   * messages with other, similar mailboxes and with Erlang processes.
   * Messages can be sent to this mailbox by using its registered name
   * or the associated {@link OtpMbox#self pid}.
   *
   * @param name a name to register for this mailbox. The name must be
   * unique within this OtpNode.
   *
   * @return a mailbox, or null if the name was already in use.
   *
   **/
    public OtpMbox createMbox(String name) {
        return mboxes.create(name);
    }

    /**
   * <p> Register or remove a name for the given mailbox. Registering
   * a name for a mailbox enables others to send messages without
   * knowing the {@link OtpErlangPid pid} of the mailbox. A mailbox
   * can have at most one name; if the mailbox already had a name,
   * calling this method will supercede that name. </p>
   *
   * @param name the name to register for the mailbox. Specify null to
   * unregister the existing name from this mailbox.
   *
   * @param mbox the mailbox to associate with the name.
   *
   * @return true if the name was available, or false otherwise.
   **/
    public boolean registerName(String name, OtpMbox mbox) {
        return mboxes.register(name, mbox);
    }

    /**
   * Get a list of all known registered names on this node.
   *
   * @return an array of Strings, containins all known registered
   * names on this node.
   **/
    public String[] getNames() {
        return mboxes.names();
    }

    /**
   * Determine the {@link OtpErlangPid pid} corresponding to a
   * registered name on this node.
   *
   * @return the {@link OtpErlangPid pid} corresponding to the
   * registered name, or null if the name is not known on this node.
   **/
    public OtpErlangPid whereis(String name) {
        OtpMbox m = mboxes.get(name);
        if (m != null) return m.self();
        return null;
    }

    /**
   * Register interest in certain system events. The {@link
   * OtpNodeStatus OtpNodeStatus} handler object contains callback
   * methods, that will be called when certain events occur. 
   *
   * @param handler the callback object to register. To clear the
   * handler, specify null as the handler to use.
   *
   **/
    public synchronized void registerStatusHandler(OtpNodeStatus handler) {
        this.handler = handler;
    }

    public boolean ping(String node, long timeout) {
        if (node.equals(this.node)) return true; else if (node.indexOf('@', 0) < 0 && node.equals(this.node.substring(0, this.node.indexOf('@', 0)))) return true;
        OtpMbox mbox = null;
        try {
            mbox = createMbox();
            mbox.send("net_kernel", node, getPingTuple(mbox));
            OtpErlangObject reply = mbox.receive(timeout);
            OtpErlangTuple t = (OtpErlangTuple) reply;
            OtpErlangAtom a = (OtpErlangAtom) (t.elementAt(1));
            return "yes".equals(a.atomValue());
        } catch (Exception e) {
        } finally {
            closeMbox(mbox);
        }
        return false;
    }

    private OtpErlangTuple getPingTuple(OtpMbox mbox) {
        OtpErlangObject[] ping = new OtpErlangObject[3];
        OtpErlangObject[] pid = new OtpErlangObject[2];
        OtpErlangObject[] node = new OtpErlangObject[2];
        pid[0] = mbox.self();
        pid[1] = createRef();
        node[0] = new OtpErlangAtom("is_auth");
        node[1] = new OtpErlangAtom(node());
        ping[0] = new OtpErlangAtom("$gen_call");
        ping[1] = new OtpErlangTuple(pid);
        ping[2] = new OtpErlangTuple(node);
        return new OtpErlangTuple(ping);
    }

    private boolean netKernel(OtpMsg m) {
        OtpMbox mbox = null;
        try {
            OtpErlangTuple t = (OtpErlangTuple) (m.getMsg());
            OtpErlangTuple req = (OtpErlangTuple) t.elementAt(1);
            OtpErlangPid pid = (OtpErlangPid) req.elementAt(0);
            OtpErlangObject[] pong = new OtpErlangObject[2];
            pong[0] = req.elementAt(1);
            pong[1] = new OtpErlangAtom("yes");
            mbox = createMbox();
            mbox.send(pid, new OtpErlangTuple(pong));
            return true;
        } catch (Exception e) {
        } finally {
            closeMbox(mbox);
        }
        return false;
    }

    boolean deliver(OtpMsg m) {
        OtpMbox mbox = null;
        try {
            int t = m.type();
            if (t == OtpMsg.regSendTag) {
                String name = m.getRecipientName();
                if (name.equals("net_kernel")) return netKernel(m); else mbox = mboxes.get(name);
            } else {
                mbox = mboxes.get(m.getRecipientPid());
            }
            if (mbox == null) return false;
            mbox.deliver(m);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    void deliverError(OtpCookedConnection conn, Exception e) {
        removeConnection(conn);
        remoteStatus(conn.name, false, e);
    }

    OtpCookedConnection getConnection(String node) {
        OtpPeer peer = null;
        OtpCookedConnection conn = null;
        synchronized (connections) {
            conn = (OtpCookedConnection) connections.get(node);
            if (conn == null) {
                peer = new OtpPeer(node);
                conn = (OtpCookedConnection) connections.get(peer.node());
                if (conn == null) {
                    try {
                        conn = new OtpCookedConnection(this, peer);
                        addConnection(conn);
                    } catch (Exception e) {
                        connAttempt(peer.node(), false, e);
                    }
                }
            }
            return conn;
        }
    }

    private void addConnection(OtpCookedConnection conn) {
        if ((conn != null) && (conn.name != null)) {
            connections.put(conn.name, conn);
            remoteStatus(conn.name, true, null);
        }
    }

    private void removeConnection(OtpCookedConnection conn) {
        if ((conn != null) && (conn.name != null)) {
            connections.remove(conn.name);
        }
    }

    private synchronized void remoteStatus(String node, boolean up, Object info) {
        if (handler == null) return;
        try {
            handler.remoteStatus(node, up, info);
        } catch (Exception e) {
        }
    }

    private synchronized void localStatus(String node, boolean up, Object info) {
        if (handler == null) return;
        try {
            handler.localStatus(node, up, info);
        } catch (Exception e) {
        }
    }

    private synchronized void connAttempt(String node, boolean incoming, Object info) {
        if (handler == null) return;
        try {
            handler.connAttempt(node, incoming, info);
        } catch (Exception e) {
        }
    }

    public class Mailboxes {

        private Hashtable byPid = null;

        private Hashtable byName = null;

        public Mailboxes() {
            byPid = new Hashtable(17, (float) 0.95);
            byName = new Hashtable(17, (float) 0.95);
        }

        public OtpMbox create(String name) {
            OtpMbox m = null;
            synchronized (byName) {
                if (get(name) != null) return null;
                OtpErlangPid pid = OtpNode.this.createPid();
                m = new OtpMbox(OtpNode.this, pid, name);
                byPid.put(pid, new WeakReference(m));
                byName.put(name, new WeakReference(m));
            }
            return m;
        }

        public OtpMbox create() {
            OtpErlangPid pid = OtpNode.this.createPid();
            OtpMbox m = new OtpMbox(OtpNode.this, pid);
            byPid.put(pid, new WeakReference(m));
            return m;
        }

        public void clear() {
            byPid.clear();
            byName.clear();
        }

        public String[] names() {
            String allnames[] = null;
            synchronized (byName) {
                int n = byName.size();
                java.util.Enumeration keys = byName.keys();
                allnames = new String[n];
                int i = 0;
                while (keys.hasMoreElements()) {
                    allnames[i++] = (String) (keys.nextElement());
                }
            }
            return allnames;
        }

        public boolean register(String name, OtpMbox mbox) {
            if (name == null) {
                if (mbox.name != null) {
                    byName.remove(mbox.name);
                    mbox.name = null;
                }
            } else {
                synchronized (byName) {
                    if (get(name) != null) return false;
                    byName.put(name, new WeakReference(mbox));
                    mbox.name = name;
                }
            }
            return true;
        }

        public OtpMbox get(String name) {
            WeakReference wr = (WeakReference) byName.get(name);
            if (wr != null) {
                OtpMbox m = (OtpMbox) wr.get();
                if (m != null) return m;
                byName.remove(name);
            }
            return null;
        }

        public OtpMbox get(OtpErlangPid pid) {
            WeakReference wr = (WeakReference) byPid.get(pid);
            if (wr != null) {
                OtpMbox m = (OtpMbox) wr.get();
                if (m != null) return m;
                byPid.remove(pid);
            }
            return null;
        }

        public void remove(OtpMbox mbox) {
            byPid.remove(mbox.self);
            if (mbox.name != null) byName.remove(mbox.name);
        }
    }

    public class Acceptor extends Thread {

        private ServerSocket sock;

        private int port;

        private volatile boolean done = false;

        Acceptor(int port) throws IOException {
            IOException e;
            this.sock = new ServerSocket(port);
            this.port = sock.getLocalPort();
            OtpNode.this.port = this.port;
            this.setDaemon(true);
            this.setName("acceptor");
            publishPort();
            this.start();
        }

        private boolean publishPort() throws IOException {
            if (getEpmd() != null) return false;
            OtpEpmd.publishPort(OtpNode.this);
            return true;
        }

        private void unPublishPort() {
            OtpEpmd.unPublishPort(OtpNode.this);
            closeSock(epmd);
            epmd = null;
        }

        public void quit() {
            unPublishPort();
            done = true;
            closeSock(sock);
            localStatus(node, false, null);
        }

        private void closeSock(ServerSocket s) {
            try {
                if (s != null) s.close();
            } catch (Exception e) {
            }
        }

        private void closeSock(Socket s) {
            try {
                if (s != null) s.close();
            } catch (Exception e) {
            }
        }

        public int port() {
            return this.port;
        }

        public void run() {
            Socket newsock = null;
            OtpCookedConnection conn = null;
            localStatus(node, true, null);
            accept_loop: while (!done) {
                conn = null;
                try {
                    newsock = sock.accept();
                } catch (Exception e) {
                    if (!done) localStatus(node, false, e);
                    break accept_loop;
                }
                try {
                    synchronized (connections) {
                        conn = new OtpCookedConnection(OtpNode.this, newsock);
                        addConnection(conn);
                    }
                } catch (OtpAuthException e) {
                    if (conn != null && conn.name != null) connAttempt(conn.name, true, e); else connAttempt("unknown", true, e);
                    closeSock(newsock);
                } catch (IOException e) {
                    if (conn != null && conn.name != null) connAttempt(conn.name, true, e); else connAttempt("unknown", true, e);
                    closeSock(newsock);
                } catch (Exception e) {
                    closeSock(newsock);
                    closeSock(sock);
                    localStatus(node, false, e);
                    break accept_loop;
                }
            }
            unPublishPort();
        }
    }
}
