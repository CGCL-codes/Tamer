package br.ufal.tci.nexos.arcolive.protocol.rtp;

import java.net.InetAddress;
import java.util.Vector;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.media.control.BufferControl;
import javax.media.protocol.DataSource;
import javax.media.rtp.Participant;
import javax.media.rtp.RTPManager;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.SessionListener;
import javax.media.rtp.event.ByeEvent;
import javax.media.rtp.event.NewParticipantEvent;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.media.rtp.event.SessionEvent;

/**
 * CLASSNAME.java
 *
 * CLASS DESCRIPTION
 *
 * @see CLASSNAME
 *
 * @author <a href="mailto:felipe@labpesquisas.tci.ufal.br">Felipe Barros Pontes</a>.
 * @author <a href="mailto:leandro@labpesquisas.tci.ufal.br">Leandro Melo de Sales</a>.
 * @since 0.1
 * @version 0.1
 *
 * <p><b>Revisions:</b>
 *
 * <p><b>yyyymmdd USERNAME:</b>
 * <ul>
 * <li> VERSION
 * </ul>
 */
public class ArCoLIVERTPStreamHandler implements ReceiveStreamListener, SessionListener, ControllerListener {

    private String sessions[] = { "first" };

    private RTPManager rtpManager[];

    private boolean dataReceived = false;

    private Object dataSync = new Object();

    private Player player;

    private Vector rtpStreamHandlerListeners;

    public ArCoLIVERTPStreamHandler() {
    }

    public ArCoLIVERTPStreamHandler(String sessions[]) {
        this.sessions = sessions;
    }

    public void handle(String serverHost, int port) {
        SessionAddress clientAddress, serverAddress;
        this.rtpManager = new RTPManager[this.sessions.length];
        this.rtpStreamHandlerListeners = new Vector();
        try {
            for (int i = 0; i < this.sessions.length; i++) {
                this.rtpManager[i] = RTPManager.newInstance();
                this.rtpManager[i].addReceiveStreamListener(this);
                this.rtpManager[i].addSessionListener(this);
                clientAddress = new SessionAddress(InetAddress.getLocalHost(), port);
                serverAddress = new SessionAddress(InetAddress.getByName(serverHost), port);
                rtpManager[i].initialize(clientAddress);
                BufferControl bufferControl = (BufferControl) rtpManager[i].getControl("javax.media.control.BufferControl");
                if (bufferControl != null) {
                    bufferControl.setBufferLength(400);
                }
                rtpManager[i].addTarget(serverAddress);
            }
        } catch (Exception e) {
            System.err.println("Cannot create the RTP Session: " + e.getMessage());
        }
        long then = System.currentTimeMillis();
        long waitingPeriod = 30000;
        try {
            synchronized (this.dataSync) {
                while (!this.dataReceived && (System.currentTimeMillis() - then < waitingPeriod)) {
                    if (!this.dataReceived) {
                        System.err.println("  - Waiting for RTP data to arrive...");
                    }
                    this.dataSync.wait(1000);
                }
            }
        } catch (Exception e) {
        }
        if (!this.dataReceived) {
            System.err.println("No RTP data was received.");
            this.quit();
        }
    }

    public void quit() {
        for (int i = 0; i < rtpManager.length; i++) {
            this.rtpManager[i].removeTargets("Session over");
            this.rtpManager[i].dispose();
            this.rtpManager[i] = null;
        }
    }

    public void update(ReceiveStreamEvent receiveStreamEvent) {
        RTPManager mgr = (RTPManager) receiveStreamEvent.getSource();
        Participant participant = receiveStreamEvent.getParticipant();
        ReceiveStream stream = receiveStreamEvent.getReceiveStream();
        if (receiveStreamEvent instanceof NewReceiveStreamEvent) {
            try {
                stream = ((NewReceiveStreamEvent) receiveStreamEvent).getReceiveStream();
                DataSource dataSource = stream.getDataSource();
                this.player = javax.media.Manager.createPlayer(dataSource);
                this.player.realize();
                synchronized (dataSync) {
                    this.dataReceived = true;
                    this.dataSync.notifyAll();
                }
            } catch (Exception e) {
                System.out.println("NewReceiveStreamException " + e.getMessage());
            }
        }
        if (receiveStreamEvent instanceof ByeEvent) {
            this.notifyStopReceiveStreamEvent();
        }
    }

    public void update(SessionEvent sessionEvent) {
        if (sessionEvent instanceof NewParticipantEvent) {
            Participant p = ((NewParticipantEvent) sessionEvent).getParticipant();
            System.err.println("  - The participant " + p.getCNAME() + " had just joined in the session.");
        }
    }

    public void controllerUpdate(ControllerEvent controlEvent) {
        if (controlEvent instanceof RealizeCompleteEvent) {
            this.notifyPlayerReadyEvent();
        }
    }

    public synchronized void addalRtpStreamListener(ArCoLIVERTPStreamHandlerListener alRtpStreamListener) {
        if (!this.rtpStreamHandlerListeners.contains(alRtpStreamListener)) {
            this.rtpStreamHandlerListeners.addElement(alRtpStreamListener);
        }
    }

    public synchronized void removealPlayerStreamListener(ArCoLIVERTPStreamHandlerListener alRtpStreamListener) {
        this.rtpStreamHandlerListeners.remove(alRtpStreamListener);
    }

    public synchronized void notifyStopReceiveStreamEvent() {
        Vector currentListeners = null;
        synchronized (this) {
            currentListeners = (Vector) this.rtpStreamHandlerListeners.clone();
        }
        ArCoLIVERTPStreamHandlerEvent alRtpStreamHandlerEvent = new ArCoLIVERTPStreamHandlerEvent(this);
        int size = rtpStreamHandlerListeners.size();
        for (int i = 0; i < size; i++) {
            ((ArCoLIVERTPStreamHandlerListener) currentListeners.elementAt(i)).stopReceiveStreamEvent(alRtpStreamHandlerEvent);
        }
    }

    public synchronized void notifyPlayerReadyEvent() {
        Vector currentListeners = null;
        synchronized (this) {
            currentListeners = (Vector) this.rtpStreamHandlerListeners.clone();
        }
        ArCoLIVERTPStreamHandlerEvent alRtpStreamHandlerEvent = new ArCoLIVERTPStreamHandlerEvent(this);
        int size = rtpStreamHandlerListeners.size();
        for (int i = 0; i < size; i++) {
            ((ArCoLIVERTPStreamHandlerListener) currentListeners.elementAt(i)).playerReadyEvent(alRtpStreamHandlerEvent);
        }
    }
}
