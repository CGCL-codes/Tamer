package udt;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import udt.packets.Acknowledgement;
import udt.packets.Acknowledgment2;
import udt.packets.DataPacket;
import udt.packets.KeepAlive;
import udt.packets.NegativeAcknowledgement;
import udt.sender.FlowWindow;
import udt.sender.SenderLossList;
import udt.util.MeanThroughput;
import udt.util.MeanValue;
import udt.util.SequenceNumber;
import udt.util.UDTStatistics;
import udt.util.UDTThreadFactory;
import udt.util.Util;

/**
 * sender part of a UDT entity
 * 
 * @see UDTReceiver
 */
public class UDTSender {

    private static final Logger logger = Logger.getLogger(UDTClient.class.getName());

    private final UDPEndPoint endpoint;

    private final UDTSession session;

    private final UDTStatistics statistics;

    private final SenderLossList senderLossList;

    private final Map<Long, byte[]> sendBuffer;

    private final FlowWindow flowWindow;

    private Thread senderThread;

    private final Object sendLock = new Object();

    private final AtomicInteger unacknowledged = new AtomicInteger(0);

    private volatile long currentSequenceNumber = 0;

    private volatile long largestSentSequenceNumber = -1;

    private volatile long lastAckSequenceNumber;

    private volatile boolean started = false;

    private volatile boolean stopped = false;

    private volatile boolean paused = false;

    private volatile CountDownLatch startLatch = new CountDownLatch(1);

    private final ReentrantLock ackLock = new ReentrantLock();

    private final Condition ackCondition = ackLock.newCondition();

    private final boolean storeStatistics;

    private final int chunksize;

    public UDTSender(UDTSession session, UDPEndPoint endpoint) {
        if (!session.isReady()) throw new IllegalStateException("UDTSession is not ready.");
        this.endpoint = endpoint;
        this.session = session;
        statistics = session.getStatistics();
        senderLossList = new SenderLossList();
        sendBuffer = new ConcurrentHashMap<Long, byte[]>(session.getFlowWindowSize(), 0.75f, 2);
        chunksize = session.getDatagramSize() - 24;
        flowWindow = new FlowWindow(session.getFlowWindowSize(), chunksize);
        lastAckSequenceNumber = session.getInitialSequenceNumber();
        currentSequenceNumber = session.getInitialSequenceNumber() - 1;
        storeStatistics = Boolean.getBoolean("udt.sender.storeStatistics");
        initMetrics();
        doStart();
    }

    private MeanValue dgSendTime;

    private MeanValue dgSendInterval;

    private MeanThroughput throughput;

    private void initMetrics() {
        if (!storeStatistics) return;
        dgSendTime = new MeanValue("SENDER: Datagram send time");
        statistics.addMetric(dgSendTime);
        dgSendInterval = new MeanValue("SENDER: Datagram send interval");
        statistics.addMetric(dgSendInterval);
        throughput = new MeanThroughput("SENDER: Throughput", session.getDatagramSize());
        statistics.addMetric(throughput);
    }

    /**
	 * start the sender thread
	 */
    public void start() {
        logger.info("Starting sender for " + session);
        startLatch.countDown();
        started = true;
    }

    private void doStart() {
        Runnable r = new Runnable() {

            public void run() {
                try {
                    while (!stopped) {
                        startLatch.await();
                        paused = false;
                        senderAlgorithm();
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    logger.log(Level.SEVERE, "", ex);
                }
                logger.info("STOPPING SENDER for " + session);
            }
        };
        senderThread = UDTThreadFactory.get().newThread(r);
        String s = (session instanceof ServerSession) ? "ServerSession" : "ClientSession";
        senderThread.setName("UDTSender-" + s + "-" + senderThread.getName());
        senderThread.start();
    }

    /** 
	 * sends the given data packet, storing the relevant information
	 */
    private void send(DataPacket p) throws IOException {
        synchronized (sendLock) {
            if (storeStatistics) {
                dgSendInterval.end();
                dgSendTime.begin();
            }
            endpoint.doSend(p);
            if (storeStatistics) {
                dgSendTime.end();
                dgSendInterval.begin();
                throughput.end();
                throughput.begin();
            }
            int l = p.getLength();
            byte[] data = new byte[l];
            System.arraycopy(p.getData(), 0, data, 0, l);
            sendBuffer.put(p.getPacketSequenceNumber(), data);
            unacknowledged.incrementAndGet();
        }
        statistics.incNumberOfSentDataPackets();
    }

    protected void sendUdtPacket(ByteBuffer bb, int timeout, TimeUnit units) throws IOException, InterruptedException {
        if (!started) start();
        DataPacket packet = null;
        do {
            packet = flowWindow.getForProducer();
            if (packet == null) {
                Thread.sleep(10);
            }
        } while (packet == null);
        try {
            packet.setPacketSequenceNumber(getNextSequenceNumber());
            packet.setSession(session);
            packet.setDestinationID(session.getDestination().getSocketID());
            int len = Math.min(bb.remaining(), chunksize);
            byte[] data = packet.getData();
            bb.get(data, 0, len);
            packet.setLength(len);
        } finally {
            flowWindow.produce();
        }
    }

    /**
	 * writes a data packet, waiting at most for the specified time
	 * if this is not possible due to a full send queue
	 * 
	 * @param timeout
	 * @param units
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
    protected void sendUdtPacket(byte[] data, int timeout, TimeUnit units) throws IOException, InterruptedException {
        if (!started) start();
        DataPacket packet = null;
        do {
            packet = flowWindow.getForProducer();
            if (packet == null) {
                Thread.sleep(10);
            }
        } while (packet == null);
        try {
            packet.setPacketSequenceNumber(getNextSequenceNumber());
            packet.setSession(session);
            packet.setDestinationID(session.getDestination().getSocketID());
            packet.setData(data);
        } finally {
            flowWindow.produce();
        }
    }

    protected void receive(UDTPacket p) throws IOException {
        if (p instanceof Acknowledgement) {
            Acknowledgement acknowledgement = (Acknowledgement) p;
            onAcknowledge(acknowledgement);
        } else if (p instanceof NegativeAcknowledgement) {
            NegativeAcknowledgement nak = (NegativeAcknowledgement) p;
            onNAKPacketReceived(nak);
        } else if (p instanceof KeepAlive) {
            session.getSocket().getReceiver().resetEXPCount();
        }
    }

    protected void onAcknowledge(Acknowledgement acknowledgement) throws IOException {
        ackLock.lock();
        ackCondition.signal();
        ackLock.unlock();
        CongestionControl cc = session.getCongestionControl();
        long rtt = acknowledgement.getRoundTripTime();
        if (rtt > 0) {
            long rttVar = acknowledgement.getRoundTripTimeVar();
            cc.setRTT(rtt, rttVar);
            statistics.setRTT(rtt, rttVar);
        }
        long rate = acknowledgement.getPacketReceiveRate();
        if (rate > 0) {
            long linkCapacity = acknowledgement.getEstimatedLinkCapacity();
            cc.updatePacketArrivalRate(rate, linkCapacity);
            statistics.setPacketArrivalRate(cc.getPacketArrivalRate(), cc.getEstimatedLinkCapacity());
        }
        long ackNumber = acknowledgement.getAckNumber();
        cc.onACK(ackNumber);
        statistics.setCongestionWindowSize((long) cc.getCongestionWindowSize());
        boolean removed = false;
        for (long s = lastAckSequenceNumber; s < ackNumber; s++) {
            synchronized (sendLock) {
                removed = sendBuffer.remove(s) != null;
                senderLossList.remove(s);
            }
            if (removed) {
                unacknowledged.decrementAndGet();
            }
        }
        lastAckSequenceNumber = Math.max(lastAckSequenceNumber, ackNumber);
        sendAck2(ackNumber);
        statistics.incNumberOfACKReceived();
        if (storeStatistics) statistics.storeParameters();
    }

    /**
	 * procedure when a NAK is received (spec. p 14)
	 * @param nak
	 */
    protected void onNAKPacketReceived(NegativeAcknowledgement nak) {
        for (Integer i : nak.getDecodedLossInfo()) {
            senderLossList.insert(Long.valueOf(i));
        }
        session.getCongestionControl().onLoss(nak.getDecodedLossInfo());
        session.getSocket().getReceiver().resetEXPTimer();
        statistics.incNumberOfNAKReceived();
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("NAK for " + nak.getDecodedLossInfo().size() + " packets lost, " + "set send period to " + session.getCongestionControl().getSendInterval());
        }
        return;
    }

    protected void sendKeepAlive() throws Exception {
        KeepAlive keepAlive = new KeepAlive();
        keepAlive.setSession(session);
        endpoint.doSend(keepAlive);
    }

    protected void sendAck2(long ackSequenceNumber) throws IOException {
        Acknowledgment2 ackOfAckPkt = new Acknowledgment2();
        ackOfAckPkt.setAckSequenceNumber(ackSequenceNumber);
        ackOfAckPkt.setSession(session);
        ackOfAckPkt.setDestinationID(session.getDestination().getSocketID());
        endpoint.doSend(ackOfAckPkt);
    }

    /**
	 * sender algorithm
	 */
    long iterationStart;

    public void senderAlgorithm() throws InterruptedException, IOException {
        while (!paused) {
            iterationStart = Util.getCurrentTime();
            Long entry = senderLossList.getFirstEntry();
            if (entry != null) {
                handleRetransmit(entry);
            } else {
                int unAcknowledged = unacknowledged.get();
                if (unAcknowledged < session.getCongestionControl().getCongestionWindowSize() && unAcknowledged < session.getFlowWindowSize()) {
                    DataPacket dp = flowWindow.consumeData();
                    if (dp != null) {
                        send(dp);
                        largestSentSequenceNumber = dp.getPacketSequenceNumber();
                    } else {
                        statistics.incNumberOfMissingDataEvents();
                    }
                } else {
                    if (unAcknowledged >= session.getCongestionControl().getCongestionWindowSize()) {
                        statistics.incNumberOfCCWindowExceededEvents();
                    }
                    waitForAck();
                }
            }
            if (largestSentSequenceNumber % 16 != 0) {
                long snd = (long) session.getCongestionControl().getSendInterval();
                long passed = Util.getCurrentTime() - iterationStart;
                int x = 0;
                while (snd - passed > 0) {
                    if (x == 0) {
                        statistics.incNumberOfCCSlowDownEvents();
                        x++;
                    }
                    passed = Util.getCurrentTime() - iterationStart;
                    if (stopped) return;
                }
            }
        }
    }

    private final DataPacket retransmit = new DataPacket();

    /**
	 * re-transmit an entry from the sender loss list
	 * @param entry
	 */
    protected void handleRetransmit(Long seqNumber) {
        try {
            byte[] data = sendBuffer.get(seqNumber);
            if (data != null) {
                retransmit.setPacketSequenceNumber(seqNumber);
                retransmit.setSession(session);
                retransmit.setDestinationID(session.getDestination().getSocketID());
                retransmit.setData(data);
                endpoint.doSend(retransmit);
                statistics.incNumberOfRetransmittedDataPackets();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "", e);
        }
    }

    /**
	 * for processing EXP event (see spec. p 13)
	 */
    protected void putUnacknowledgedPacketsIntoLossList() {
        synchronized (sendLock) {
            for (Long l : sendBuffer.keySet()) {
                senderLossList.insert(l);
            }
        }
    }

    /**
	 * the next sequence number for data packets.
	 * The initial sequence number is "0"
	 */
    public long getNextSequenceNumber() {
        currentSequenceNumber = SequenceNumber.increment(currentSequenceNumber);
        return currentSequenceNumber;
    }

    public long getCurrentSequenceNumber() {
        return currentSequenceNumber;
    }

    /**
	 * returns the largest sequence number sent so far
	 */
    public long getLargestSentSequenceNumber() {
        return largestSentSequenceNumber;
    }

    /**
	 * returns the last Ack. sequence number 
	 */
    public long getLastAckSequenceNumber() {
        return lastAckSequenceNumber;
    }

    boolean haveAcknowledgementFor(long sequenceNumber) {
        return SequenceNumber.compare(sequenceNumber, lastAckSequenceNumber) <= 0;
    }

    boolean isSentOut(long sequenceNumber) {
        return SequenceNumber.compare(largestSentSequenceNumber, sequenceNumber) >= 0;
    }

    boolean haveLostPackets() {
        return !senderLossList.isEmpty();
    }

    /**
	 * wait until the given sequence number has been acknowledged
	 * 
	 * @throws InterruptedException
	 */
    public void waitForAck(long sequenceNumber) throws InterruptedException {
        while (!session.isShutdown() && !haveAcknowledgementFor(sequenceNumber)) {
            ackLock.lock();
            try {
                ackCondition.await(100, TimeUnit.MICROSECONDS);
            } finally {
                ackLock.unlock();
            }
        }
    }

    public void waitForAck(long sequenceNumber, int timeout) throws InterruptedException {
        while (!session.isShutdown() && !haveAcknowledgementFor(sequenceNumber)) {
            ackLock.lock();
            try {
                ackCondition.await(timeout, TimeUnit.MILLISECONDS);
            } finally {
                ackLock.unlock();
            }
        }
    }

    /**
	 * wait for the next acknowledge
	 * @throws InterruptedException
	 */
    public void waitForAck() throws InterruptedException {
        ackLock.lock();
        try {
            ackCondition.await(200, TimeUnit.MICROSECONDS);
        } finally {
            ackLock.unlock();
        }
    }

    public void stop() {
        stopped = true;
    }

    public void pause() {
        startLatch = new CountDownLatch(1);
        paused = true;
    }
}
