package org.tcpfile.net;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoSession;
import org.jdesktop.beans.AbstractBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcpfile.crypto.CryptServerAccessor;
import org.tcpfile.crypto.Encryption;
import org.tcpfile.gui.settingsmanager.HookToSetting;
import org.tcpfile.gui.settingsmanager.SettingHookGenerator;
import org.tcpfile.gui.settingsmanager.SettingsManager;
import org.tcpfile.main.BuddyList;
import org.tcpfile.main.Contact;
import org.tcpfile.main.Misc;
import org.tcpfile.main.UnbreakableTimerTask;
import org.tcpfile.net.messages.AwayMessage;
import org.tcpfile.net.messages.AwayMessage.Status;
import org.tcpfile.net.mina.Mina;
import org.tcpfile.net.mina.SimpleHandler;
import org.tcpfile.net.packets.Packet;
import org.tcpfile.net.packets.ProtocolsPacket;
import org.tcpfile.net.packets.PublicKeyPacket;
import org.tcpfile.net.protocols.LowLevelProtocol;
import org.tcpfile.net.protocols.Protocol;
import org.tcpfile.net.queues.SendableInQueue;
import org.tcpfile.net.queues.SendableQueue;

/**
 * This class is used to handle the protocol and the IOSession.
 * It is the central for all data flow, every Sendable comes through here at least once.
 * 
 * @author Stivo
 *
 */
public class Connection extends AbstractBean {

    private static Logger log = LoggerFactory.getLogger(Connection.class);

    public static final Vector<Connection> CONNECTIONS = new Vector<Connection>();

    private static int seconds = 5;

    private static int kbs = 1024 * seconds;

    @HookToSetting
    public static int UploadPerConnection;

    @HookToSetting
    private static int UploadTotal;

    public static final int RECEIVE_NOTHINGRECEIVED = 0;

    public static final int RECEIVE_PUBLICKEYRECEIVED = 10;

    public static final int RECEIVE_CHALLENGETRUEBUTNOTYETPROTOCOLS = 15;

    public static final int RECEIVE_PROTOCOLSRECEIVED = 20;

    public static final int RECEIVE_PROTOCOLINITIALIZE = 40;

    public static final int RECEIVE_READY = 1000;

    public static final int SEND_NOTHINGSENT = 10000;

    public static final int SEND_PUBLICKEYSENT = 10010;

    public static final int SEND_PROTOCOLSSENT = 10020;

    public static final int SEND_PROTOCOLINITIALIZE = 10040;

    public static final int SEND_OBJECTS = 11000;

    private static Timer timer = new Timer();

    public int speedByTrustLevel = UploadPerConnection;

    private static final Object syncnext = new Object();

    private long nextSendTime = 0;

    private static long globalNextSendTime = 0;

    public static Mina mina = new Mina();

    public LowLevelProtocol llp = LowLevelProtocol.getCurrentLowLevel();

    public HashMap<Integer, Vector<byte[]>> messageCache = new HashMap<Integer, Vector<byte[]>>();

    public HashMap<Integer, Long> messageCacheNumberOfPackets = new HashMap<Integer, Long>();

    public HashMap<Byte, Protocol> protocols = new HashMap<Byte, Protocol>();

    public long startedAt = System.currentTimeMillis();

    public byte usingProtocol = 0;

    public Semaphore waitLock = new Semaphore(0);

    public IoSession session;

    public SimpleHandler simplehandler;

    public boolean isValid = true;

    public boolean connected = false;

    public Contact contact;

    private SendableQueue protocolqueue = new SendableQueue();

    private int receiveStatus = 0;

    public int sendStatus = 0;

    public byte[] challenge = Encryption.createkey(true);

    public Packet challengeanswer;

    public boolean isReady = false;

    public BandwidthAnalyzer ba;

    private UnbreakableTimerTask tt;

    public int sendSize = 1200;

    static {
        SettingHookGenerator.generateHooks(Connection.class);
    }

    public static void init() {
        if (SettingsManager.settingsManager.findSetting("RunListener").getBoolean()) mina.startServer(SettingsManager.settingsManager.findSetting("ListenerPort").getInteger());
    }

    private Connection() {
        sendStatus = Connection.SEND_NOTHINGSENT;
        setReceiveStatus(Connection.RECEIVE_NOTHINGRECEIVED);
        CONNECTIONS.add(this);
        addNewProtocol((byte) 0);
        updateThread();
    }

    public Connection(IoSession sc) {
        this();
        this.session = sc;
        if (simplehandler == null) simplehandler = ((SimpleHandler) session.getHandler());
        connected = true;
    }

    public Connection(Contact c) {
        this();
        c.setStatus(2);
        this.contact = c;
        mina.connect(this);
        log.info("Trying to connect to " + c);
    }

    PropertyChangeListener pcl = new PropertyChangeListener() {

        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (contact.trust.uploadSpeedKBs == -1) {
                speedByTrustLevel = (Integer) evt.getNewValue();
            }
        }

        ;
    };

    public void setReceiveStatus(int receiveStatus) {
        if (receiveStatus == RECEIVE_PROTOCOLINITIALIZE && this.receiveStatus == RECEIVE_PUBLICKEYRECEIVED) receiveStatus = RECEIVE_CHALLENGETRUEBUTNOTYETPROTOCOLS;
        if (this.receiveStatus == RECEIVE_CHALLENGETRUEBUTNOTYETPROTOCOLS && receiveStatus == RECEIVE_PROTOCOLSRECEIVED) receiveStatus = RECEIVE_PROTOCOLINITIALIZE;
        log.debug("Receive status from {} to {}", this.receiveStatus, receiveStatus);
        this.receiveStatus = receiveStatus;
    }

    public void connected(IoSession ios) {
        this.session = ios;
        session.setIdleTime(IdleStatus.BOTH_IDLE, 15);
        if (simplehandler == null) simplehandler = ((SimpleHandler) session.getHandler());
        if (ba == null) ba = new BandwidthAnalyzer(ios);
        connected = true;
    }

    public void connectionReady() {
        contact.setStatus(3);
        speedByTrustLevel = contact.trust.uploadSpeedKBs != -1 ? contact.trust.uploadSpeedKBs : UploadPerConnection;
        SettingsManager.settingsManager.findSetting("UploadPerConnection").addPropertyChangeListener(pcl);
        session.setIdleTime(IdleStatus.BOTH_IDLE, Misc.settings.findSetting("ConnectionTimeOut").getInteger());
        firePropertyChange("connected", false, true);
        sendStatus = Connection.SEND_OBJECTS;
        setReceiveStatus(Connection.RECEIVE_READY);
        isReady = true;
        if (Misc.awayManager.isAway()) contact.send(new AwayMessage(Status.Away));
    }

    /**
	 * If there is nothing to send, or something has to be encrypted,
	 * This thread puts the next message on the CryptServer.
	 */
    public void putNextByteArrayOnCryptServer() {
        if (!connected) return;
        Sendable s;
        switch(sendStatus) {
            case Connection.SEND_NOTHINGSENT:
                sendStatus = Connection.SEND_PUBLICKEYSENT;
                s = new PublicKeyPacket(this);
                protocolqueue.addAndPutOnCryptserver(s, this);
                break;
            case Connection.SEND_PUBLICKEYSENT:
                if (getReceiveStatus() >= Connection.RECEIVE_PUBLICKEYRECEIVED) {
                    contact.setStatus(2);
                    s = new ProtocolsPacket(this);
                    protocolqueue.addAndPutOnCryptserver(s, this);
                    sendStatus = Connection.SEND_PROTOCOLSSENT;
                    break;
                } else break;
            case Connection.SEND_PROTOCOLSSENT:
                if (getReceiveStatus() >= Connection.RECEIVE_PROTOCOLSRECEIVED) {
                    if (challengeanswer == null) throw new RuntimeException("Error here");
                    protocolqueue.addAndPutOnCryptserver(challengeanswer, this);
                    sendStatus = Connection.SEND_PROTOCOLINITIALIZE;
                    break;
                } else break;
            case Connection.SEND_PROTOCOLINITIALIZE:
                if (getReceiveStatus() >= Connection.RECEIVE_PROTOCOLINITIALIZE) {
                    Sendable next = protocols.get(usingProtocol).initiateConnection(this);
                    if (next != null) {
                        protocolqueue.addAndPutOnCryptserver(next, this);
                        break;
                    }
                }
            case Connection.SEND_OBJECTS:
                contact.outgoing.putNextOnCryptServer(this);
        }
    }

    public boolean addNewProtocol(byte prot) {
        try {
            if (prot != 0 && protocols.containsKey(prot)) {
                log.warn("Tried to add a protocol that was already there");
                return false;
            }
            protocols.put(prot, netMisc.protocols.get(prot).getClass().newInstance());
            return true;
        } catch (InstantiationException e) {
            log.warn("", e);
        } catch (IllegalAccessException e) {
            log.warn("", e);
        }
        return false;
    }

    /**
	 * Delivers the next Sendable in either of the queues.
	 * @return can be null
	 */
    public SendableInQueue getSendableToSend() {
        SendableInQueue out = protocolqueue.getNextSendableToSend(this);
        if (!isReady) return out;
        if (out == null) out = contact.outgoing.getNextSendableToSend(this);
        if (out == null) putNextByteArrayOnCryptServer();
        return out;
    }

    /**
	 * Signals the connection that it should look for new messages to write.
	 */
    public void writeInterest() {
        log.trace("Entering");
        if (isValid) if (session != null) {
            assert (simplehandler != null);
            simplehandler.writeNextOne(session);
        }
    }

    /**
	 * Kills the connection directly and calls cancel to clean up
	 */
    public void hangup() {
        if (this.session != null) {
            session.close();
        }
        cancel();
    }

    public String toString() {
        return "Current sendstatus " + sendStatus + " current receive status " + getReceiveStatus();
    }

    /**
	 * When the NioThread receives something, it uses this function to let the whole
	 * vector be processed.
	 * @param incoming Vector containing possibly several byte[]
	 */
    public void handleForNIOThread(Vector<byte[]> incoming) {
        for (byte[] b : incoming) {
            handleForNIOThread(b);
        }
    }

    /**
	 * This function handles one byte[] received on the session.
	 * It determines which thread should be used to execute it and either
	 * leaves it for the Cryptserver or decodes it itself.
	 * @param incoming
	 */
    public void handleForNIOThread(byte[] incoming) {
        CryptServerAccessor.addWork(incoming, this, false);
    }

    /**
	 * This receives the actual message and handles it afterwards.
	 * @param incoming
	 */
    public void handleForCryptServer(byte[] incoming) {
        if (!protocols.containsKey(incoming[0])) throw new RuntimeException("No initialized protocol for this");
        protocols.get(incoming[0]).prepareToHandle(ByteArray.copyfromto(incoming, 1), this);
    }

    /**
	 * This retrieves the current active protocol for this connection.
	 * @return
	 */
    public Protocol getCurrentProtocol() {
        if (sendStatus >= Connection.SEND_OBJECTS) return protocols.get(usingProtocol); else return protocols.get((byte) 0);
    }

    /**
	 * Gets the protocol with the given number.
	 * @param prot
	 * @return
	 */
    public Protocol getProtocol(byte prot) {
        return protocols.get(prot);
    }

    public boolean writingAllowed() {
        if (contact == null) return true;
        if (!contact.inLan) {
            if (UploadTotal != 0) {
                if (BandwidthAnalyzer.totalUpload(seconds) > UploadTotal * kbs) return false;
                synchronized (syncnext) {
                    if (System.currentTimeMillis() < globalNextSendTime) return false;
                }
            } else if (speedByTrustLevel != 0) {
                if (ba.getBytesFromSeconds(seconds, false) > speedByTrustLevel * kbs) return false;
                synchronized (syncnext) {
                    if (System.currentTimeMillis() < nextSendTime) return false;
                }
            }
        }
        return true;
    }

    public void didWrite(long bytes) {
        boolean scheduled = false;
        if (speedByTrustLevel != 0) {
            long halftime = 500 * bytes / (speedByTrustLevel * 1024);
            synchronized (syncnext) {
                nextSendTime = System.currentTimeMillis() + ((13 * halftime) / 8);
            }
            scheduleTimer(2 * halftime);
            scheduled = true;
        }
        if (UploadTotal != 0) {
            long halftime = 500 * bytes / (UploadTotal * 1024);
            synchronized (syncnext) {
                globalNextSendTime = System.currentTimeMillis() + ((13 * halftime) / 8);
            }
            if (!scheduled) scheduleTimer(2 * halftime);
        }
    }

    private void scheduleTimer(long delay) {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                writeInterest();
            }
        }, delay);
    }

    /**
	 * Cleans up a Connection once it has been hung up.
	 * 
	 */
    public void cancel() {
        CONNECTIONS.remove(this);
        if (contact != null) {
            contact.outgoing.resetReadyOnes();
            contact.setStatus(1);
            contact.pollingcounter = BuddyList.pollFrequency;
            firePropertyChange("connected", true, false);
            contact.setCon(null);
        }
        isValid = false;
        connected = false;
        tt.cancel();
        if (pcl != null) SettingsManager.settingsManager.findSetting("UploadPerConnection").removePropertyChangeListener(pcl);
        pcl = null;
        if (ba != null) ba.remove();
        ba = null;
        contact = null;
    }

    /**
	 * This updates the BandwidthAnalyzer with current counters.
	 */
    public void updateInfos() {
        if (ba != null) ba.update();
    }

    /**
	 * Updates the Send Size each minute.
	 */
    public void updateThread() {
        class SendSize extends UnbreakableTimerTask {

            public void runCaught() {
                if (ba == null) return;
                sendSize = ba.getSendSizeRecommendation((speedByTrustLevel + UploadTotal) * 1024);
                log.debug("New Send Size: " + sendSize);
                getCurrentProtocol().setPacketSize(sendSize);
                log.debug(Arrays.toString(Connection.this.getInfos()));
            }
        }
        tt = new SendSize();
        Misc.timer.schedule(tt, 60000, 60000);
    }

    public String[] getInfos() {
        String[] info = new String[10];
        info[0] = contact != null ? contact.name : "(Unknown)";
        long seconds = (System.currentTimeMillis() - startedAt) / 1000;
        info[1] = Misc.fromSecondsToTime(seconds);
        if (ba != null) {
            info[2] = (ba.getSpeedFromSeconds(1, false));
            info[3] = ((ba.getSpeedFromSeconds(5, false)));
            info[4] = (ba.getSpeedFromSeconds(1, true));
            info[5] = (ba.getSpeedFromSeconds(5, true));
        }
        if (session != null) {
            info[6] = pretty(session.getWrittenBytes());
            info[7] = pretty(session.getReadBytes());
            info[8] = session.getScheduledWriteRequests() + "";
            info[9] = pretty(session.getScheduledWriteBytes());
        }
        return info;
    }

    public static String[][] getAllInfos() {
        ArrayList<String[]> out = new ArrayList<String[]>();
        for (int j = 0; j < CONNECTIONS.size(); j++) {
            out.add(CONNECTIONS.get(j).getInfos());
        }
        return out.toArray(new String[0][0]);
    }

    public static String pretty(long size) {
        return Misc.prettyPrintSize(size);
    }

    public int getReceiveStatus() {
        return receiveStatus;
    }
}
