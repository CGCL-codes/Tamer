package bittorrent.overlay.peerlets;

import java.util.LinkedList;
import org.apache.log4j.Logger;
import protopeer.BasePeerlet;
import protopeer.Finger;
import protopeer.Peer;
import protopeer.network.Message;
import protopeer.network.NetworkAddress;
import protopeer.time.Timer;
import protopeer.time.TimerListener;
import protopeer.util.quantities.Time;
import bittorrent.data.AbstractBlock;
import bittorrent.data.AbstractChunk;
import bittorrent.overlay.messages.TrackerRequestMessage;
import bittorrent.overlay.messages.TrackerResponseMessage;
import bittorrent.overlay.peer.AbstractPeer;
import bittorrent.overlay.peer.FileSharingPeer;
import bittorrent.overlay.peerlets.interfaces.IRequestPeersMechanism;
import bittorrent.overlay.peerlets.interfaces.IStoppablePeerlet;
import bittorrent.overlay.peerlets.interfaces.ITrackerClientListener;
import bittorrent.overlay.util.IStorageWrapperListener;

/**
 * This peerlet handles the comunication with the tracker.
 * 
 * On startup it sends a request message to the tracker in order to get the
 * addresses of other peers in the swarm. Then, it sends keep-alive messages in
 * regular intervals to keep the tracker informed that this peer is still
 * online. When the peers goes offline, this peerlet notifies the tracker. In
 * addition, it provides the public method <code>requestMorePeers()</code> which
 * other classes can use if the number of neighbors is too low.
 * 
 * This class provides the listener interface <code>TrackerClient.Listener
 * </code> with the only method
 * <code>gotNewPeerList(List&lt;Finger&gt; peers)
 * </code>. By this method other classes are notified when a list of new peers
 * arrives from the tracker.
 */
public class TrackerClient extends BasePeerlet implements IStorageWrapperListener, IRequestPeersMechanism, IStoppablePeerlet {

    private static final Logger logger = Logger.getLogger(TrackerClient.class);

    protected STATE state;

    /**
	 * The address where the tracker is located.
	 */
    protected NetworkAddress trackerAddress;

    /**
	 * Duration of the keep-alive message interval. This is an optional
	 * parameter, default value is 20 minutes.
	 */
    protected Time aliveInterval = Time.inMinutes(18);

    /**
	 * Timer used to send the keep-alive messages.
	 */
    protected Timer sendAliveTimer;

    /**
	 * The number of peers which are requested at the tracker. This is an
	 * optional paramter, default value is 50.
	 */
    private int numWant = 50;

    /**
	 * The list of listeners to be notified when a list of new peers arrives
	 * from the tracker.
	 */
    private LinkedList<ITrackerClientListener> listeners;

    /**
	 * This variable can be set to start the peerlet without informing the
	 * tracker.
	 */
    private boolean startWithoutTracker = false;

    public TrackerClient() {
        super();
        listeners = new LinkedList<ITrackerClientListener>();
    }

    /**
	 * Initializes the <code>sendAliveTimer</code>.
	 */
    @Override
    public void init(Peer peer) {
        super.init(peer);
        sendAliveTimer = getPeer().getClock().createNewTimer();
        sendAliveTimer.addTimerListener(new TimerListener() {

            @Override
            public void timerExpired(Timer timer) {
                aliveTimerExpired();
            }
        });
        state = STATE.RUNNING;
        if (!startWithoutTracker) {
            ((FileSharingPeer) getPeer()).getPeerState().getStorageWrapper().addListener(this);
        }
    }

    /**
	 * Sends the initial request message to the tracker.
	 */
    @Override
    public void start() {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting TrackerClient");
        }
        if (!startWithoutTracker) {
            sendTrackerMessage(TrackerRequestMessage.EVENT.STARTED, numWant);
            sendAliveTimer.schedule(aliveInterval);
        }
    }

    /**
	 * Cancels the <code>sendAliveTimer</code>.
	 * 
	 */
    @Override
    public void stop() {
        if (logger.isDebugEnabled()) {
            logger.debug("Stoping TrackerClient");
        }
        super.stop();
        sendAliveTimer.cancel();
    }

    /**
	 * Prepare our stop by announcing that we leave to the tracker and stopping
	 * our timer.
	 */
    @Override
    public void prepareStop() {
        state = STATE.STOPPING;
        sendAliveTimer.cancel();
        announceLeave();
    }

    private void stopPrepared() {
        ((AbstractPeer) getPeer()).peerletHasStopped(this);
    }

    /**
	 * Sends a message to the tracker that this peer is leaving the swarm.
	 */
    private void announceLeave() {
        if (logger.isDebugEnabled()) {
            logger.debug("Sending stop message");
        }
        Message m = new TrackerRequestMessage(getPeer().getIdentifier(), TrackerRequestMessage.EVENT.STOPPED, 0);
        getPeer().sendMessage(trackerAddress, m);
    }

    /**
	 * Only <code>TrackerResponseMessage</code>s are interesting for this
	 * peerlet.
	 */
    @Override
    public void handleIncomingMessage(Message message) {
        if (message instanceof TrackerResponseMessage) {
            handleTrackerResponseMessage((TrackerResponseMessage) message);
        } else if (message instanceof TrackerRequestMessage) {
            handleLeaveAck();
        }
    }

    private void handleLeaveAck() {
        stopPrepared();
    }

    /**
	 * Sends a request to the tracker in order to get the addresses of more
	 * peers in the swarm. The default number of peers <code>numWant</code> is
	 * requested from the tracker.
	 */
    @Override
    public void requestMorePeers() {
        requestMorePeers(numWant);
    }

    /**
	 * Sends a request to the tracker in order to get the addresses of more
	 * peers in the swarm.
	 */
    @Override
    public void requestMorePeers(int number) {
        if (state == STATE.RUNNING) {
            sendTrackerMessage(TrackerRequestMessage.EVENT.EMPTY, number);
        } else {
            logger.warn(getPeer().toString() + ": More peers were requested, but TrackerClient is already stopping.");
        }
    }

    /**
	 * Sends a message to the tracker that this peers acts as a seeder from now
	 * on.
	 */
    public void announceFileCompleted() {
        if (state == STATE.RUNNING) {
            if (logger.isDebugEnabled()) {
                logger.debug("Sending file completed message");
            }
            sendTrackerMessage(TrackerRequestMessage.EVENT.COMPLETED, 0);
        } else {
            logger.warn(getPeer().toString() + ": AbstractFile completion wants to be announced, but TrackerClient is already stopping.");
        }
    }

    /**
	 * Notifies all listeners that new tracker-data has arrived.
	 * 
	 * @param message
	 *            message containing the list of peers
	 */
    private void handleTrackerResponseMessage(TrackerResponseMessage message) {
        if (state == STATE.RUNNING) {
            if (logger.isDebugEnabled()) {
                logger.debug("Received tracker data: " + message.getRequestedPeers());
            }
            fireGotNewTrackerData(message);
        }
    }

    /**
	 * Sends a keep-alive message to the tracker.
	 */
    protected void aliveTimerExpired() {
        if (logger.isDebugEnabled()) {
            logger.debug("Sending keep-alive message");
        }
        sendTrackerMessage(TrackerRequestMessage.EVENT.EMPTY, 0);
        sendAliveTimer.schedule(aliveInterval);
    }

    /**
	 * Sends a <code>TrackerRequestMessage</code> to the tracker. Can be a
	 * request of peers , a keep-alive message, a completed-message (when the
	 * peer turns into a seeder), a sl-request-message or a stop message (when
	 * the peer leaves the swarm).
	 * 
	 * @param event
	 *            type of message
	 * @param number
	 *            number of peers to request at the server
	 */
    protected void sendTrackerMessage(TrackerRequestMessage.EVENT event, int number) {
        double completed = ((FileSharingPeer) getPeer()).getStorageWrapper().getCompletedFraction();
        Message m = new TrackerRequestMessage(getPeer().getIdentifier(), event, completed, number);
        getPeer().sendMessage(trackerAddress, m);
        sendAliveTimer.cancel();
        sendAliveTimer.schedule(aliveInterval);
    }

    /**
	 * @return duration of the keep-alive message interval
	 */
    public Time getAliveInterval() {
        return aliveInterval;
    }

    /**
	 * Sets the duration of the interval in which keep-alive messages are sent
	 * to the tracker
	 * 
	 * @param aliveInterval
	 *            duration of the keep-alive message interval
	 */
    public void setAliveInterval(Time aliveInterval) {
        this.aliveInterval = aliveInterval;
    }

    /**
	 * @return the default number of peers which are requested from the tracker
	 *         in one request
	 */
    public int getNumWant() {
        return numWant;
    }

    /**
	 * Sets the number of peers which are requested from the tracker in one
	 * request.
	 * 
	 * @param numWant
	 *            the number of peers ...
	 */
    public void setNumWant(int numWant) {
        this.numWant = numWant;
    }

    public void setTrackerAddress(NetworkAddress trackerAddress) {
        this.trackerAddress = trackerAddress;
    }

    public void setStartWithoutTrackerNotice(boolean start) {
        this.startWithoutTracker = start;
    }

    public void addListener(ITrackerClientListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(ITrackerClientListener listener) {
        this.listeners.remove(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
        listeners = null;
    }

    public void fireGotNewTrackerData(TrackerResponseMessage m) {
        for (ITrackerClientListener listener : listeners) {
            listener.gotNewTrackerData(m);
        }
    }

    @Override
    public void completedBlock(FileSharingPeer peer, AbstractBlock block, Finger remotePeer, Time now) {
    }

    @Override
    public void completedChunk(FileSharingPeer peer, AbstractChunk chunk, Time now) {
    }

    @Override
    public void completedFile(FileSharingPeer peer, Time now) {
        announceFileCompleted();
    }

    @Override
    public void downloadStarted(FileSharingPeer peer, Time now) {
    }
}
