package org.jmule.core.protocol.donkey;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.*;
import java.nio.channels.SelectableChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.LinkedList;
import java.util.logging.*;
import org.jmule.core.*;
import org.jmule.util.Convert;

/** Listens for answers send by donkey servers to *our* UDP request.
 * @author emarant
 * @version $Revision: 1.2 $
 * <br>Last changed by $Author: jmartinc $ on $Date: 2005/09/09 15:28:34 $
 */
public class DonkeyUdpListener implements DatagramChannelWrapper, DonkeyPacketConstants {

    static Logger log = Logger.getLogger(DonkeyUdpListener.class.getName());

    private DatagramChannel channel;

    private int port;

    private SelectionKey selectionKey;

    private ByteBuffer receivingbuffer = ByteBuffer.allocateDirect(65536).order(ByteOrder.LITTLE_ENDIAN);

    private final int MAX_SENDS_PER_SELECT_ROUND = 1;

    private LinkedList oSendQueue = new LinkedList();

    private boolean writeOff = true;

    private boolean writeOn = false;

    private static final int SELECTION_MASK_WRITE_OFF = ~SelectionKey.OP_WRITE;

    public DonkeyUdpListener(int port) throws IOException {
        this.port = port;
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.socket().bind(new InetSocketAddress(this.port));
        selectionKey = ConnectionManager.getInstance().addToIOSelector(this, SelectionKey.OP_READ);
    }

    public SelectableChannel getSelectableChannel() {
        return channel;
    }

    public boolean processInput() throws IOException {
        callCounterIn++;
        InetSocketAddress sender = (InetSocketAddress) channel.receive(receivingbuffer);
        receivingbuffer.flip();
        if (receivingbuffer.remaining() < 2) {
            log.fine("Received unknown UDP packet." + Convert.byteBufferToHexString(receivingbuffer));
            receivingbuffer.clear();
            return true;
        }
        if (Convert.byteToInt(receivingbuffer.get()) != OP_EDONKEYHEADER) {
            log.fine("Received unknown UDP packet. Size min.: " + receivingbuffer.limit() + " Contents(first 128 byte):\n" + Convert.byteBufferToHexString(receivingbuffer, 0, 128));
            receivingbuffer.clear();
            return true;
        }
        log.finer("received from " + sender + " packet: " + Convert.byteBufferToHexString(receivingbuffer));
        int opcode = Convert.byteToInt(receivingbuffer.get());
        switch(opcode) {
            case OP_GLOBSEARCHRES:
                {
                    try {
                        log.finer("Received search result(s).");
                        while (receivingbuffer.remaining() >= 20) {
                            SearchManager.getInstance().addResult(DonkeyScannedPacket.decodeUDPSearchResult(receivingbuffer));
                            if (receivingbuffer.hasRemaining()) {
                                if (Convert.byteToInt(receivingbuffer.get()) != OP_EDONKEYHEADER) {
                                    break;
                                }
                                int opcode2 = receivingbuffer.get();
                                if (((byte) opcode2) != ((byte) OP_GLOBSEARCHRES)) {
                                    log.warning("missed udp packets (" + Integer.toHexString(opcode2 & 0xff) + " after " + Integer.toHexString(opcode) + ") !");
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        String logmes = "";
                        for (int i = 0; i < e.getStackTrace().length; i++) {
                            logmes += (e.getStackTrace()[i]).toString() + "\n";
                        }
                        log.info(logmes);
                    }
                    break;
                }
            case OP_GLOBFOUNDSOURCES:
                {
                    do {
                        log.finer("Received donkey download sources.");
                        byte[] hash = new byte[16];
                        receivingbuffer.get(hash);
                        Download dl = DownloadManager.getInstance().getDownloadList().getDownload("DonkeyHash", new DonkeyFileHash(hash));
                        if (dl == null) {
                            log.finer("Sources are for unknown download.");
                            break;
                        }
                        int numSources = Convert.byteToInt(receivingbuffer.get());
                        for (int i = 1; i <= numSources; i++) {
                            byte[] ipBytes = new byte[4];
                            receivingbuffer.get(ipBytes);
                            receivingbuffer.position(receivingbuffer.position() - 4);
                            long id = DonkeyScannedPacket.getUnsignedInt(receivingbuffer);
                            InetAddress ip;
                            int port;
                            try {
                                ip = InetAddress.getByAddress(ipBytes);
                                port = DonkeyScannedPacket.getUnsignedShort(receivingbuffer);
                                if (port == 0) continue;
                                if (id != DonkeyProtocol.getInstance().getUserID() && DonkeyProtocol.getInstance().shouldAddNewSourceHook(ip)) {
                                    InetSocketAddress senderIaddr = (InetSocketAddress) sender;
                                    DonkeyDownloadSource dds = new DonkeyDownloadSource(new InetSocketAddress(ip, port), dl, new InetSocketAddress(senderIaddr.getAddress(), senderIaddr.getPort() - 4));
                                    synchronized (DonkeyProtocol.getInstance()) {
                                        dds = DonkeyProtocol.getInstance().addSource(dds);
                                    }
                                    log.finer("+" + dds);
                                    dl.addSource(new SingleDonkeySource(dds, dl));
                                    log.fine((new InetSocketAddress(ip, port)).toString());
                                }
                            } catch (UnknownHostException e) {
                                assert false : e.getMessage();
                            } catch (Exception e) {
                                String logmes = "";
                                for (int j = 0; i < e.getStackTrace().length; j++) {
                                    logmes += (e.getStackTrace()[j]).toString() + "\n";
                                }
                                log.severe(logmes);
                            }
                        }
                        log.fine("ed2k: Received " + numSources + " sources for " + dl.getFileName());
                    } while (receivingbuffer.remaining() >= 19 && (Convert.byteToInt(receivingbuffer.get()) == OP_EDONKEYHEADER) && receivingbuffer.get() == (byte) OP_GLOBFOUNDSOURCES);
                    break;
                }
            case OP_GLOBSERVERSTATUS:
                {
                    InetSocketAddress is = ((InetSocketAddress) sender);
                    try {
                        DonkeyServer ds = DonkeyProtocol.getInstance().getServerList().getIP(is.getAddress().getHostAddress() + ":" + (is.getPort() - 4));
                        receivingbuffer.getInt();
                        ds.setUsers(receivingbuffer.getInt());
                        ds.setFiles(receivingbuffer.getInt());
                        ds.setAlive();
                        if (receivingbuffer.remaining() >= 4) {
                            ds.setMaxUsers(receivingbuffer.getInt());
                            if (receivingbuffer.remaining() >= 8) {
                                ds.setSoftLimitForFiles(receivingbuffer.getInt());
                                ds.setHardLimitForFiles(receivingbuffer.getInt());
                                if (receivingbuffer.remaining() >= 4) {
                                    int flags = receivingbuffer.getInt();
                                    ds.setUDPExtendedGetSources((flags & 1) == 1);
                                    ds.setUDPExtendedGetFiles((flags & 2) == 2);
                                }
                            }
                        }
                        log.finer("ed2k: Received serverstaus from " + ds.getSocketAddress());
                    } catch (java.util.NoSuchElementException nsee) {
                        log.fine("ed2k: Received serverstaus from unknown sender: " + sender);
                    } catch (IllegalArgumentException iae) {
                        log.fine("ed2k: Received serverstaus; got exception " + iae.getMessage());
                    } catch (java.nio.BufferUnderflowException iae) {
                        log.fine("ed2k: Received currupt serverstaus from sender: " + sender);
                    }
                    break;
                }
            case OP_GLOBSERVERINFO:
                {
                    InetSocketAddress is = ((InetSocketAddress) sender);
                    try {
                        DonkeyServer ds = DonkeyProtocol.getInstance().getServerList().getIP(is.getAddress().getHostAddress() + ":" + (is.getPort() - 4));
                        int stringlength = DonkeyScannedPacket.getUnsignedShort(receivingbuffer);
                        byte[] stringbytes = new byte[stringlength];
                        receivingbuffer.get(stringbytes);
                        ds.setName(new String(stringbytes));
                        stringlength = DonkeyScannedPacket.getUnsignedShort(receivingbuffer);
                        stringbytes = new byte[stringlength];
                        receivingbuffer.get(stringbytes);
                        ds.setDescription(new String(stringbytes));
                        ds.setAlive();
                        log.finer("ed2k: Received serverinfo from " + ds.getSocketAddress());
                    } catch (java.util.NoSuchElementException nsee) {
                        log.fine("ed2k: Received serverinfo from unknown sender: " + sender);
                    } catch (IllegalArgumentException iae) {
                        log.fine("ed2k: Received serverinfo; got exception " + iae.getMessage());
                    } catch (java.nio.BufferUnderflowException iae) {
                        log.fine("ed2k: Received currupt serverinfo from sender: " + sender);
                    }
                    break;
                }
            case OP_GLOBCALLBACKFAIL:
                {
                    break;
                }
            default:
                {
                    log.warning("Received unknown \"E3\" UDP packet. Size min.: " + receivingbuffer.limit() + " Contents(first 256 byte):\n" + Convert.byteBufferToHexString(receivingbuffer, 0, 256));
                }
        }
        if (receivingbuffer.remaining() > 0) {
            log.fine("additional bytes 0x" + Integer.toHexString(opcode) + " [" + receivingbuffer.position() + "..]:" + Convert.byteBufferToHexString(receivingbuffer, receivingbuffer.position(), receivingbuffer.remaining()));
        }
        receivingbuffer.clear();
        return true;
    }

    public static long callCounterOut = 0;

    public static long callCounterIn = 0;

    public void processOutput() {
        int count = 0;
        callCounterOut++;
        while ((oSendQueue.size() > 0) && (count++ < MAX_SENDS_PER_SELECT_ROUND)) {
            DonkeyServer ds = (DonkeyServer) oSendQueue.removeFirst();
            ByteBuffer packet = ds.getNextPacket();
            log.finer("try send to " + ds.getSocketAddress() + " packet: " + Convert.byteBufferToHexString(packet) + " remaining send queue size " + oSendQueue.size());
            try {
                channel.send(packet, new InetSocketAddress((ds.getSocketAddress().getAddress()), (ds.getSocketAddress().getPort() + 4)));
            } catch (IOException ioe) {
                log.warning("ed2k UDP send packet to " + ds.getSocketAddress().getAddress() + ":" + (ds.getSocketAddress().getPort() + 4) + " " + ioe.getMessage());
            }
            DonkeyPacket.disposePlainByteBuffer(packet);
        }
        selectionKey.interestOps(SELECTION_MASK_WRITE_OFF & selectionKey.interestOps());
        if (oSendQueue.size() > 0) {
            log.finest("off -");
            writeOff = false;
            writeOn = true;
        } else {
            log.finest("off +");
            writeOff = true;
        }
    }

    public boolean check(int count) {
        if (writeOn && oSendQueue.size() > 0) {
            writeOn = false;
            log.finest("on");
            selectionKey.interestOps(SelectionKey.OP_WRITE | selectionKey.interestOps());
        }
        return true;
    }

    public void close() {
    }

    protected void add(DonkeyServer target) {
        oSendQueue.addLast(target);
        log.finer(" send queue size " + oSendQueue.size());
        if (!writeOff) {
            selectionKey.interestOps(SelectionKey.OP_WRITE | selectionKey.interestOps());
        } else {
            writeOn = true;
        }
    }
}
