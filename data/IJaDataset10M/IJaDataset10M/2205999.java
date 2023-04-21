package org.echarts.jain.sip;

import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.io.OutputStream;
import java.util.*;
import org.apache.log4j.Logger;

public class RTPReceiverLite {

    private Selector selector;

    private HashSet newEndpoints;

    private boolean isStopped = false;

    private boolean isAdded = false;

    private InetAddress bindAddress;

    private Logger logger;

    private int userSpecifiedRtpPort;

    private Thread receiverThread;

    private E4JSHelper e4jsHelper;

    public RTPReceiverLite(E4JSHelper e4jsHelper) throws Exception {
        this(0, e4jsHelper);
    }

    public RTPReceiverLite(int userSpecifiedRtpPort, E4JSHelper e4jsHelper) throws Exception {
        this.e4jsHelper = e4jsHelper;
        this.userSpecifiedRtpPort = userSpecifiedRtpPort;
        logger = e4jsHelper.getLogger();
        newEndpoints = new HashSet();
        try {
            selector = Selector.open();
        } catch (Exception e) {
            logger.error("RTPReceiver failed to open selector", e);
        }
        bindAddress = InetAddress.getByName(e4jsHelper.getListenIP());
        receiverThread = new RTPReceiverThread();
        receiverThread.start();
    }

    private RTPEndpoint tryAcquireRtpPort(int port, OutputStream audioFileStream) throws Exception {
        boolean isDone = false;
        boolean isFirstEven = false;
        int p1 = 0;
        DatagramChannel channel1 = null;
        channel1 = DatagramChannel.open();
        DatagramSocket socket = channel1.socket();
        socket.bind(new InetSocketAddress(bindAddress, 0));
        p1 = socket.getLocalPort();
        logger.debug("RTPReceiver :: Got local port for RTP:" + p1);
        RTPEndpoint endpoint = new RTPEndpoint(audioFileStream, this.e4jsHelper);
        endpoint.setRtpChannel(channel1);
        channel1.configureBlocking(false);
        return endpoint;
    }

    public RTPEndpoint addPort(OutputStream audioFileStream) throws Exception {
        RTPEndpoint endpoint = null;
        endpoint = tryAcquireRtpPort(userSpecifiedRtpPort, audioFileStream);
        synchronized (newEndpoints) {
            isAdded = true;
            newEndpoints.add(endpoint);
        }
        selector.wakeup();
        return endpoint;
    }

    public void removePort(RTPEndpoint ep) {
        boolean wakeup = false;
        if (ep.getRtpChannel() != null) {
            if (ep.getRtpSelectionKey() != null) {
                if (ep.getRtpChannel().isRegistered() && ep.getRtpSelectionKey().isValid()) {
                    ep.getRtpSelectionKey().cancel();
                    wakeup = true;
                }
            }
        }
        if (ep.getRtcpChannel() != null) {
            if (ep.getRtcpSelectionKey() != null) {
                if (ep.getRtcpChannel().isRegistered() && ep.getRtcpSelectionKey().isValid()) {
                    ep.getRtcpSelectionKey().cancel();
                    wakeup = true;
                }
            }
        }
        if (wakeup) {
            selector.wakeup();
        }
    }

    public void stop() throws Exception {
        isStopped = true;
        try {
            selector.close();
        } catch (Exception e) {
            logger.error("RTPReceiver: failed to close selector", e);
        }
        selector = null;
        logger.debug("about to wait for rtp recvr thread to stop");
        receiverThread.join();
        logger.debug("wait done for rtp recvr thread to stop");
    }

    private class RTPReceiverThread extends Thread {

        public void run() {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            logger.debug("RTPReceiverThread started");
            Selector mySelector = selector;
            try {
                int ret;
                while (mySelector.isOpen() && ((ret = mySelector.select()) >= 0)) {
                    try {
                        if (isStopped) {
                            return;
                        }
                        Set readyKeys = mySelector.selectedKeys();
                        Iterator it = readyKeys.iterator();
                        while (it.hasNext()) {
                            SelectionKey key = (SelectionKey) it.next();
                            it.remove();
                            if (key.isValid() && key.isReadable()) {
                                DatagramChannel channel = (DatagramChannel) key.channel();
                                try {
                                    channel.receive(buffer);
                                    buffer.flip();
                                    if ((buffer.get(0) & 0xc0) == 0x80) {
                                        int payload = buffer.get(1) & 0x7f;
                                        logger.debug("RTP packet received, payload = " + payload);
                                        if (payload == 0 || payload == 101) {
                                            RTPEndpoint ep = (RTPEndpoint) key.attachment();
                                            ep.storePacket(buffer, 0);
                                        }
                                    }
                                } catch (Exception e) {
                                    logger.error("RTPReceiver: error receiving from channel and storing RTP packet", e);
                                }
                                buffer.clear();
                            }
                        }
                        if (isAdded) {
                            logger.debug("Adding new channels");
                            synchronized (newEndpoints) {
                                Iterator epIt = newEndpoints.iterator();
                                while (epIt.hasNext()) {
                                    RTPEndpoint endpoint = (RTPEndpoint) epIt.next();
                                    SelectionKey key = endpoint.getRtpChannel().register(mySelector, SelectionKey.OP_READ, endpoint);
                                    endpoint.setRtpSelectionKey(key);
                                    epIt.remove();
                                }
                                isAdded = false;
                            }
                        }
                    } catch (java.nio.channels.CancelledKeyException e) {
                        logger.error("RTPReceiver: select loop error", e);
                    }
                }
            } catch (Exception e) {
                logger.error("RTPReceiver thread run loop error", e);
            }
            logger.debug("RTPReceiverThread stopped");
        }
    }
}
