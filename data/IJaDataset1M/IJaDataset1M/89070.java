package jmri.jmrix.xpa;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.util.Vector;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Converts Stream-based I/O to/from Xpa messages.  The "XpaInterface"
 * side sends/receives message objects.  The connection to an 
 * XpaPortController is via a pair of *Streams, which then carry 
 * sequences of characters for transmission.   Note that this processing 
 * is handled in an independent thread.
 *
 * @author			Paul Bender  Copyright (C) 2004
 * @version			$Revision: 1.13 $
 */
public final class XpaTrafficController implements XpaInterface, Runnable {

    LinkedList<byte[]> xmtList = new LinkedList<byte[]>();

    /**
         *  xmtHandler (a local class) object to implement the transmit 
         *  thread
	 **/
    XmtHandler xmtHandler = new XmtHandler();

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", justification = "temporary until mult-system; only set at startup")
    public XpaTrafficController() {
        if (log.isDebugEnabled()) log.debug("setting instance: " + this);
        self = this;
        Thread xmtThread = new Thread(xmtHandler, "XPA transmit handler");
        xmtThread.setPriority(Thread.MAX_PRIORITY - 1);
        xmtThread.start();
    }

    protected Vector<XpaListener> cmdListeners = new Vector<XpaListener>();

    public boolean status() {
        return (ostream != null & istream != null);
    }

    public synchronized void addXpaListener(XpaListener l) {
        if (l == null) throw new java.lang.NullPointerException();
        if (!cmdListeners.contains(l)) {
            cmdListeners.addElement(l);
        }
    }

    public synchronized void removeXpaListener(XpaListener l) {
        if (cmdListeners.contains(l)) {
            cmdListeners.removeElement(l);
        }
    }

    /**
	 * Forward a XpaMessage to all registered XpaInterface listeners.
	 */
    @SuppressWarnings("unchecked")
    protected void notifyMessage(XpaMessage m, XpaListener notMe) {
        Vector<XpaListener> v;
        synchronized (this) {
            v = (Vector<XpaListener>) cmdListeners.clone();
        }
        int cnt = v.size();
        for (int i = 0; i < cnt; i++) {
            XpaListener client = v.elementAt(i);
            if (notMe != client) {
                if (log.isDebugEnabled()) log.debug("notify client: " + client);
                try {
                    client.message(m);
                } catch (Exception e) {
                    log.warn("notify: During dispatch to " + client + "\nException " + e);
                }
            }
        }
    }

    XpaListener lastSender = null;

    @SuppressWarnings("unchecked")
    protected void notifyReply(XpaMessage r) {
        Vector<XpaListener> v;
        synchronized (this) {
            v = (Vector<XpaListener>) cmdListeners.clone();
        }
        int cnt = v.size();
        for (int i = 0; i < cnt; i++) {
            XpaListener client = v.elementAt(i);
            if (log.isDebugEnabled()) log.debug("notify client: " + client);
            try {
                if (lastSender != client) client.reply(r);
            } catch (Exception e) {
                log.warn("notify: During dispatch to " + client + "\nException " + e);
            }
        }
        if (lastSender != null) lastSender.reply(r);
    }

    /**
	 * Forward a preformatted message to the actual interface.
	 */
    public synchronized void sendXpaMessage(XpaMessage m, XpaListener reply) {
        if (log.isDebugEnabled()) log.debug("sendXpaMessage message: [" + m + "]");
        lastSender = reply;
        notifyMessage(m, reply);
        int len = m.getNumDataElements();
        int cr = 1;
        byte msg[] = new byte[len + cr];
        for (int i = 0; i < len; i++) msg[i] = (byte) m.getElement(i);
        msg[len] = 0x0d;
        synchronized (xmtHandler) {
            xmtList.addLast(msg);
            xmtHandler.notify();
        }
    }

    private XpaPortController controller = null;

    /**
	 * Make connection to existing PortController object.
	 */
    public void connectPort(XpaPortController p) {
        istream = p.getInputStream();
        ostream = p.getOutputStream();
        if (controller != null) log.warn("connectPort: connect called while connected");
        controller = p;
        this.sendXpaMessage(XpaMessage.getDefaultInitMsg(), null);
    }

    /**
	 * Break connection to existing XpaPortController object. Once broken,
	 * attempts to send via "message" member will fail.
	 */
    public void disconnectPort(XpaPortController p) {
        istream = null;
        ostream = null;
        if (controller != p) log.warn("disconnectPort: disconnect called from non-connected XpaPortController");
        controller = null;
    }

    /**
	 * static function returning the XpaTrafficController instance to use.
	 * @return The registered XpaTrafficController instance for general use,
	 *         if need be creating one.
	 */
    public static XpaTrafficController instance() {
        if (self == null) {
            if (log.isDebugEnabled()) log.debug("creating a new XpaTrafficController object");
            self = new XpaTrafficController();
        }
        return self;
    }

    protected static volatile XpaTrafficController self = null;

    DataInputStream istream = null;

    OutputStream ostream = null;

    /**
	 * Handle incoming characters.  This is a permanent loop,
	 * looking for input messages in character form on the
	 * stream connected to the PortController via <code>connectPort</code>.
	 * Terminates with the input stream breaking out of the try block.
	 */
    public void run() {
        while (true) {
            try {
                handleOneIncomingReply();
            } catch (java.io.IOException e) {
                log.warn("run: Exception: " + e.toString());
            }
        }
    }

    void handleOneIncomingReply() throws java.io.IOException {
        XpaMessage msg = new XpaMessage();
        int i;
        for (i = 0; i < XpaMessage.maxSize; i++) {
            byte char1 = istream.readByte();
            msg.setElement(i, char1);
        }
        if (log.isDebugEnabled()) log.debug("dispatch reply of length " + i);
        {
            final XpaMessage thisMsg = msg;
            final XpaTrafficController thisTC = this;
            Runnable r = new Runnable() {

                XpaMessage msgForLater = thisMsg;

                XpaTrafficController myTC = thisTC;

                public void run() {
                    log.debug("Delayed notify starts");
                    myTC.notifyReply(msgForLater);
                }
            };
            javax.swing.SwingUtilities.invokeLater(r);
        }
    }

    /**
         * Captive class to handle transmission
         */
    class XmtHandler implements Runnable {

        @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "UW_UNCOND_WAIT", justification = "while loop controls access")
        public void run() {
            while (true) {
                try {
                    if (log.isDebugEnabled()) log.debug("check for input");
                    byte msg[] = null;
                    synchronized (this) {
                        msg = xmtList.removeFirst();
                    }
                    try {
                        if (ostream != null) {
                            if (log.isDebugEnabled()) log.debug("write message: " + java.util.Arrays.toString(msg));
                            synchronized (ostream) {
                                ostream.write(msg);
                                ostream.notify();
                            }
                        } else {
                            log.warn("sendMessage: no connection established");
                        }
                    } catch (Exception e) {
                        log.warn("sendMessage: Exception: " + e.toString());
                    }
                } catch (NoSuchElementException e) {
                    if (log.isDebugEnabled()) log.debug("start wait");
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (java.lang.InterruptedException ei) {
                        Thread.currentThread().interrupt();
                    }
                    if (log.isDebugEnabled()) log.debug("end wait");
                }
            }
        }
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(XpaTrafficController.class.getName());
}
