package jist.swans.route;

import jist.swans.Constants;
import jist.swans.mac.MacAddress;
import jist.swans.misc.Message;
import jist.swans.misc.Util;
import jist.swans.net.NetAddress;
import jist.swans.net.NetInterface;
import jist.swans.net.NetMessage;
import jist.runtime.JistAPI;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * Ad-hoc On-demand Distance Vector (AODV) Routing Protocol Implementation.
 *
 * @author Clifton Lin
 * @author Rimon Barr &lt;barr+jist@cs.cornell.edu&gt;
 * @version $Id: RouteAodv.java,v 1.1 2006/10/21 00:03:45 lmottola Exp $
 * @since SWANS1.0
 */
public class RouteAodv implements RouteInterface.Aodv {

    /** debug mode. */
    public static final boolean DEBUG_MODE = false;

    /** Hello Messages setting. Should always be true, except possibly for debugging purposes. */
    public static final boolean HELLO_MESSAGES_ON = true;

    /** Starting value for node sequence numbers. */
    public static final int SEQUENCE_NUMBER_START = 0;

    /** Starting value for RREQ ID sequence numbers. */
    public static final int RREQ_ID_SEQUENCE_NUMBER_START = 0;

    /** The maximum duration of time a RREQ buffer entry can remain in the RREQ buffer. */
    public static final long RREQ_BUFFER_EXPIRE_TIME = 5 * Constants.SECOND;

    /** The maximum number of entries allowed in the RREQ buffer. */
    public static final int MAX_RREQ_BUFFER_SIZE = 10;

    /** Period of time after which the AODV timeout event gets called. */
    public static final long AODV_TIMEOUT = 30 * Constants.SECOND;

    /** Duration of inactivity after which a HELLO message should be sent to a precursor. */
    public static final long HELLO_INTERVAL = 30 * Constants.SECOND;

    /**
   * Number of timeout periods that must pass before this node can determine an outgoing
   * link unreachable.
   */
    public static final long HELLO_ALLOWED_LOSS = 3;

    /** The initial TTL value for any Route Request instance. */
    public static final byte TTL_START = 1;

    /** The amount added to current TTL upon successive broadcasts of a RREQ message. */
    public static final byte TTL_INCREMENT = 2;

    /** The maximum TTL for any RREQ message. */
    public static final byte TTL_THRESHOLD = 19;

    /** Constant term of the RREQ Timeout duration. */
    public static final long RREQ_TIMEOUT_BASE = 2 * Constants.SECOND;

    /** Variable term of the RREQ Timeout duration, dependant on the RREQ's TTL. */
    public static final long RREQ_TIMEOUT_PER_TTL = 1 * Constants.SECOND;

    /** The maximum amount of jitter before sending a packet. */
    public static final long TRANSMISSION_JITTER = 1 * Constants.MILLI_SECOND;

    /**
   * Represents a Route Request (RREQ) message.
   */
    private static class RouteRequestMessage implements Message {

        /** RREQ message size in bytes. */
        private static final int MESSAGE_SIZE = 24;

        /** Route Request identification number. */
        private int rreqId;

        /** Destination node IP address. */
        private NetAddress destIp;

        /** Originator node IP address. */
        private NetAddress origIp;

        /** Latest known destination node sequence number. */
        private int destSeqNum;

        /** Originator node sequence number. */
        private int origSeqNum;

        /** Hop count from originator node. */
        private int hopCount;

        /** Flag which indicates an unknown destination node sequence number. */
        private boolean unknownDestSeqNum;

        /**
     * Constructs a new RREQ Message object.
     * 
     * @param rreqId RREQ message identification number
     * @param destIp Destination node net address
     * @param origIp Originator node net address
     * @param destSeqNum Destination node sequence number
     * @param origSeqNum Originator node sequence number
     * @param unknownDestSeqNum Flag indicating an unknown destination node sequence number
     * @param hopCount hop count
     */
        public RouteRequestMessage(int rreqId, NetAddress destIp, NetAddress origIp, int destSeqNum, int origSeqNum, boolean unknownDestSeqNum, int hopCount) {
            this.rreqId = rreqId;
            this.destIp = destIp;
            this.origIp = origIp;
            this.destSeqNum = destSeqNum;
            this.origSeqNum = origSeqNum;
            this.unknownDestSeqNum = unknownDestSeqNum;
            this.hopCount = hopCount;
        }

        /**
     * Constructs a copy of an existing RREQ message object.
     * 
     * @param rreq An existing RREQ message
     */
        public RouteRequestMessage(RouteRequestMessage rreq) {
            this(rreq.getRreqId(), rreq.getDestIp(), rreq.getOrigIp(), rreq.getDestSeqNum(), rreq.getOrigSeqNum(), rreq.getUnknownDestSeqNum(), rreq.getHopCount());
        }

        /**
     * Returns RREQ id.
     * 
     * @return RREQ id
     */
        public int getRreqId() {
            return rreqId;
        }

        /**
     * Returns destination net address.
     * 
     * @return Destination net address
     */
        public NetAddress getDestIp() {
            return destIp;
        }

        /**
     * Returns originator net address.
     * 
     * @return Originator node net address
     */
        public NetAddress getOrigIp() {
            return origIp;
        }

        /**
     * Returns destination sequence number.
     * 
     * @return Destination node sequence number
     */
        public int getDestSeqNum() {
            return destSeqNum;
        }

        /**
     * Returns originator sequence number.
     * 
     * @return Originator sequence number
     */
        public int getOrigSeqNum() {
            return origSeqNum;
        }

        /**
     * Returns hop count.
     * 
     * @return hop count
     */
        public int getHopCount() {
            return hopCount;
        }

        /**
     * Returns unknown destination sequence number flag.
     * 
     * @return unknown destination sequence number flag
     */
        public boolean getUnknownDestSeqNum() {
            return unknownDestSeqNum;
        }

        /**
     * Increment hop count for this message.
     */
        public void incHopCount() {
            hopCount++;
        }

        /**
     * Sets the destination sequence number.
     * 
     * @param dsn destination sequence number
     */
        public void setDestSeqNum(int dsn) {
            destSeqNum = dsn;
        }

        /**
     * Sets the unknown destination sequence number flag.
     * 
     * @param flag unknown destination sequence number flag
     */
        public void setUnknownDestSeqNum(boolean flag) {
            unknownDestSeqNum = flag;
        }

        /**
     * Return packet size.
     *
     * @return packet size
     */
        public int getSize() {
            return MESSAGE_SIZE;
        }

        /**
     * Store packet into byte array.
     *
     * @param msg destination byte array
     * @param offset byte array starting offset
     */
        public void getBytes(byte[] msg, int offset) {
            throw new RuntimeException("RouteRequestMessage.getBytes() not implememented.");
        }
    }

    /**
   * Represents a Route Reply (RREP) message.
   */
    private static class RouteReplyMessage implements Message {

        /** RREP Message size in bytes. */
        private static final int MESSAGE_SIZE = 20;

        /** RREP message destination IP address field. */
        private NetAddress destIp;

        /** RREP message destination sequence number field. */
        private int destSeqNum;

        /** RREP message originator sequence number field. */
        private NetAddress origIp;

        /** RREP message hop count field. */
        private int hopCount;

        /**
     * Constructs a new RREP message object.
     * 
     * @param destIp RREP message destination node net address
     * @param destSeqNum RREP message destination node sequence number
     * @param origIp RREP message originator node net address
     * @param hopCount RREP message hopcount
     */
        public RouteReplyMessage(NetAddress destIp, int destSeqNum, NetAddress origIp, int hopCount) {
            this.destIp = destIp;
            this.destSeqNum = destSeqNum;
            this.origIp = origIp;
            this.hopCount = hopCount;
        }

        /**
     * Returns destination ip address.
     * 
     * @return destination ip address
     */
        public NetAddress getDestIp() {
            return destIp;
        }

        /**
     * Returns destination sequence number.
     * 
     * @return destination sequence number
     */
        public int getDestSeqNum() {
            return destSeqNum;
        }

        /**
     * Returns originator sequence number.
     * 
     * @return originator sequence number
     */
        public NetAddress getOrigIp() {
            return origIp;
        }

        /**
     * Returns hop count.
     * 
     * @return hop count
     */
        public int getHopCount() {
            return hopCount;
        }

        /**
     * Increments hop count.
     */
        public void incHopCount() {
            hopCount++;
        }

        /**
     * Returns packet size.
     * 
     * @return packet size
     */
        public int getSize() {
            return MESSAGE_SIZE;
        }

        /**
     * Store packet into byte array.
     *
     * @param msg destination byte array
     * @param offset byte array starting offset
     */
        public void getBytes(byte[] msg, int offset) {
            throw new RuntimeException("RouteReplyMessage.getBytes() not implemented.");
        }
    }

    /**
   * Represents a Route Error (RERR) message class.
   */
    private static class RouteErrorMessage implements Message {

        /** RERR Message size in bytes. */
        private static final int MESSAGE_SIZE = 20;

        /** List of net addresses for destinations that have become unreachable. */
        private LinkedList unreachableList;

        /**
     * Constructs a new Route Error (RERR) Message object with an empty unreachable list.
     */
        public RouteErrorMessage() {
            this(new LinkedList());
        }

        /**
     * Constructs a new Route Error (RERR) Message object with a given unreachable list.
     * 
     * @param list List of net addresses for destinations that have become unreachable
     */
        public RouteErrorMessage(LinkedList list) {
            this.unreachableList = list;
        }

        /**
     * Returns the unreachable list.
     * 
     * @return linked list of unreachable node net addresses
     */
        public LinkedList getUnreachableList() {
            return this.unreachableList;
        }

        /**
     * Add an unreachable node.
     * 
     * @param node netAddress of node to be added
     */
        public void addUnreachable(NetAddress node) {
            this.unreachableList.add(node);
        }

        /**
     * Return packet size.
     * 
     * @return packet size
     */
        public int getSize() {
            return MESSAGE_SIZE;
        }

        /**
     * Store packet into byte array.
     *
     * @param msg destination byte array
     * @param offset byte array starting offset
     */
        public void getBytes(byte[] msg, int offset) {
            throw new RuntimeException("RouteReplyMessage.getBytes() not implemented.");
        }
    }

    /**
   * Represents a HELLO message.
   */
    private static class HelloMessage implements Message {

        /** Size of HELLO Message in bytes. */
        private static final int MESSAGE_SIZE = 20;

        /** Net address of node issuing HELLO message. */
        private NetAddress ip;

        /** Sequence number of node issuing HELLO message. */
        private int seqNum;

        /**
     * Constructs new HELLO Message object.
     * 
     * @param ip net address of this node
     * @param seqNum sequence number of this node
     */
        public HelloMessage(NetAddress ip, int seqNum) {
            this.ip = ip;
            this.seqNum = seqNum;
        }

        /**
     * Returns HELLO message ip field.
     * 
     * @return Hello message ip field
     */
        public NetAddress getIp() {
            return ip;
        }

        /**
     * Return size of packet.
     * 
     * @return size of packet
     */
        public int getSize() {
            return MESSAGE_SIZE;
        }

        /**
     * Store packet into byte array.
     *
     * @param msg destination byte array
     * @param offset byte array starting offset
     */
        public void getBytes(byte[] msg, int offset) {
            throw new RuntimeException("RouteReplyMessage.getBytes() not implemented.");
        }
    }

    /**
   * Data structure to collect AODV statistics.
   */
    public static class AodvStats {

        /** sent packets. */
        public final AodvPacketStats send = new AodvPacketStats();

        /** received packets. */
        public final AodvPacketStats recv = new AodvPacketStats();

        /** messages sent by transport layer. */
        public long netMsgs;

        /** number of total route requests (excluding retransmissions). */
        public long rreqOrig;

        /** number of route replies generated. */
        public long rrepOrig;

        /** number of new routes formed. */
        public long rreqSucc;

        /** Reset statistics. */
        public void clear() {
            send.clear();
            recv.clear();
            netMsgs = 0;
            rreqOrig = 0;
            rrepOrig = 0;
            rreqSucc = 0;
        }
    }

    /** Packet stats. */
    public static class AodvPacketStats {

        /** Sum of RREQ, RREP, RERR, and HELLO packets. */
        public long aodvPackets;

        /** HELLO packets. */
        public long helloPackets;

        /** RREQ packets. */
        public long rreqPackets;

        /** RREP packets. */
        public long rrepPackets;

        /** RERR packets. */
        public long rerrPackets;

        /** Reset statistics. */
        public void clear() {
            aodvPackets = 0;
            helloPackets = 0;
            rreqPackets = 0;
            rrepPackets = 0;
            rerrPackets = 0;
        }
    }

    /**
   * Represents a request for a route by a node.  A single route request can repeatedly
   * broadcast RREQ messages, with increasing TTL value, until a route has been found.
   * TTL values start at TTL_START, increment by TTL_INCREMENT, but do not exceed
   * TTL_THRESHOLD.
   */
    private static class RouteRequest {

        /** Net address of node for which we seek a route. */
        private NetAddress destIp;

        /** Route request identifier. */
        private int rreqId;

        /** Time to live. */
        private byte ttl;

        /**
     * Indicates whether this request has been satisfied (route has been found).
     * Once set to true, this entry can be removed from the route request list.
     */
        private boolean routeFound = false;

        /**
     * Reference to the encapsulating RouteAodv instance.
     */
        private RouteAodv thisNode;

        /**
     * Constructs a new Route Request object.
     * 
     * @param destIp net address of node for which we seek a route
     * @param thisNode reference to this RouteAodv instance
     */
        public RouteRequest(NetAddress destIp, RouteAodv thisNode) {
            this.thisNode = thisNode;
            this.destIp = destIp;
            this.ttl = TTL_START;
            this.obtainNewRreqId();
        }

        /**
     * Return RREQ id.
     *
     * @return RREQ id
     */
        public int getRreqId() {
            return rreqId;
        }

        /**
     * Returns destination net address.
     * 
     * @return destination net address
     */
        public NetAddress getDest() {
            return destIp;
        }

        /**
     * Returns TTL.
     * 
     * @return TTL value
     */
        public byte getTtl() {
            return ttl;
        }

        /**
     * Assigns a fresh RREQ id number, and updates the node's global RREQ id counter.
     */
        public void obtainNewRreqId() {
            rreqId = thisNode.rreqIdSeqNum++;
        }

        /**
     * Increments the TTL value by TTL_INCREMENT, but ensures that it does not
     * exceed TTL_THRESHOLD.
     *
     */
        public void incTtl() {
            ttl = (byte) Math.min(ttl + TTL_INCREMENT, TTL_THRESHOLD);
        }

        /**
     * Sets the Route Found flag.
     * 
     * @param b value to assign to flag
     */
        public void setRouteFound(boolean b) {
            routeFound = b;
        }

        /**
     * Creates and broadcasts a RREQ message.
     *
     * Also, updates RREQ buffer and routing table.
     */
        public void broadcast() {
            thisNode.rreqBuffer.addEntry(new RreqBufferEntry(rreqId, thisNode.netAddr));
            int destSeqNum = 0;
            boolean unknownDestSeqNum = false;
            RouteTableEntry routeEntry = (RouteTableEntry) thisNode.routeTable.lookup(this.destIp);
            if (routeEntry == null) {
                unknownDestSeqNum = true;
            } else {
                unknownDestSeqNum = false;
                destSeqNum = routeEntry.getDestSeqNum();
            }
            thisNode.seqNum++;
            RouteTableEntry selfEntry = thisNode.routeTable.lookup(thisNode.netAddr);
            selfEntry.setDestSeqNum(thisNode.seqNum);
            thisNode.routeTable.printTable();
            RouteRequestMessage rreqMsg = new RouteRequestMessage(rreqId, destIp, thisNode.netAddr, destSeqNum, thisNode.seqNum, unknownDestSeqNum, 0);
            NetMessage.Ip rreqMsgIp = new NetMessage.Ip(rreqMsg, thisNode.netAddr, NetAddress.ANY, Constants.NET_PROTOCOL_AODV, Constants.NET_PRIORITY_NORMAL, this.ttl);
            printlnDebug("Broadcasting RREQ message with rreqId=" + rreqId + ", ttl=" + ttl, thisNode.netAddr);
            thisNode.self.sendIpMsg(rreqMsgIp, MacAddress.ANY);
            if (thisNode.stats != null) {
                thisNode.stats.send.rreqPackets++;
                thisNode.stats.send.aodvPackets++;
            }
        }
    }

    /**
   * Buffer for keeping track of recently sent RREQ messages (so they are not resent).
   * Also keeps track of recent RREQ messages that were answered with a RREP.
   */
    private static class RreqBuffer {

        /** List of RreqBufferEntry objects. */
        private LinkedList list;

        /** Local net address. */
        private NetAddress localAddr;

        /**
     * Constructs a Route Request Buffer object.
     * 
     * @param netAddr local net address
     */
        public RreqBuffer(NetAddress netAddr) {
            list = new LinkedList();
            localAddr = netAddr;
        }

        /**
     * Adds an entry to the RREQ buffer.
     * If RREQ buffer is full, oldest entries are removed to make room.
     * Also, any expired entries are removed.
     * 
     * @param entry RreqBufferEntry to be added
     */
        public void addEntry(RreqBufferEntry entry) {
            clearExpiredEntries();
            if (list.size() == MAX_RREQ_BUFFER_SIZE) {
                list.removeLast();
            } else if (list.size() > MAX_RREQ_BUFFER_SIZE) {
                throw new RuntimeException("RREQ Buffer is larger than allowed size!");
            }
            list.addFirst(entry);
        }

        /**
     * Checks if a given RouteBufferEntry exists in the RREQ Buffer.
     * 
     * @param entry the RouteBufferEntry 
     * @return True, if the RREQ buffer contains the specified entry
     */
        public boolean contains(RreqBufferEntry entry) {
            return list.contains(entry);
        }

        /**
     * Remove all expired entries.
     */
        public void clearExpiredEntries() {
            while (!list.isEmpty() && JistAPI.getTime() > ((RreqBufferEntry) list.getLast()).getTimeSent() + RREQ_BUFFER_EXPIRE_TIME) {
                printlnDebug("Removing Entry from RreqBuffer", localAddr);
                list.removeLast();
            }
        }
    }

    /**
   * A single entry of the RREQ Buffer.
   */
    private static class RreqBufferEntry {

        /** RREQ id of RREQ message sent. */
        private int rreqId;

        /** Net address of node that originating the RREQ message. */
        private NetAddress originIp;

        /** Time (simulation time) that this entry was created. */
        private long timeSent;

        /**
     * Constructs a RREQ Buffer Entry object.
     * 
     * @param rreqId RREQ id of RREQ message
     * @param originIp Net address of node that originated the RREQ message
     */
        public RreqBufferEntry(int rreqId, NetAddress originIp) {
            this.rreqId = rreqId;
            this.originIp = originIp;
            this.timeSent = JistAPI.getTime();
        }

        /**
     * Returns timestamp for creation of this entry.
     * 
     * @return time that entry was created
     */
        public long getTimeSent() {
            return timeSent;
        }

        /**
     * Checks whether given RreqBufferEntry is equal to this one.
     * Two entries are equal if the RREQ id and origin IP's are the same.
     * 
     * @param o An object of type RreqBufferEntry
     * @return True, if objects are equal
     */
        public boolean equals(Object o) {
            if (o == null || (!(o instanceof RreqBufferEntry))) {
                return false;
            }
            RreqBufferEntry entry = (RreqBufferEntry) o;
            return (this.rreqId == entry.rreqId && this.originIp.equals(entry.originIp));
        }

        /**
     * Returns a hash code.
     * 
     * @return hash code
     */
        public int hashCode() {
            return this.rreqId;
        }
    }

    /**
   * A routing table contains a hash map, consisting of NetAddress->RouteTableEntry mappings.
   */
    private static class RouteTable {

        /** The routing table. */
        private HashMap table;

        /** Address of local node. */
        private NetAddress localAddr;

        /**
     * Constructs a RouteTable object.
     * 
     * @param netAddr local address of this node
     */
        public RouteTable(NetAddress netAddr) {
            table = new HashMap();
            localAddr = netAddr;
        }

        /**
     * Adds a new entry to the routing table.  Also adds next hop to outgoing table.
     * 
     * @param key destination address
     * @param value routing information for this destination
     */
        public void add(NetAddress key, RouteTableEntry value) {
            table.put(key, value);
        }

        /**
     * Removes entry with given key from routing table.
     * 
     * @param key destination net address
     * @return true, if entry existed and not null; false, otherwise
     */
        private boolean remove(NetAddress key) {
            Object obj = table.remove(key);
            if (obj == null) return false;
            printlnDebug("Removing destination " + key + " from routing table", localAddr);
            return true;
        }

        /**
     * Look up routing information for a given destination address.
     * 
     * @param key destination address
     * @return routing information for this destination
     */
        public RouteTableEntry lookup(NetAddress key) {
            return (RouteTableEntry) table.get(key);
        }

        /**
     * Remove all route table entries whose destination is specified in a given list.
     * 
     * @param list List of destinations (of type NetAddress)
     * @return true, if at least one entry was removed from the table
     */
        public boolean removeList(LinkedList list) {
            boolean atLeastOneRemoved = false;
            Iterator itr = list.iterator();
            while (itr.hasNext()) {
                NetAddress addr = (NetAddress) itr.next();
                if (this.remove(addr)) {
                    atLeastOneRemoved = true;
                }
            }
            return atLeastOneRemoved;
        }

        /**
     * Remove all routing table entries with a given next hop address.
     * 
     * @param nextHop the next hop address
     */
        public void removeNextHop(MacAddress nextHop) {
            printlnDebug("Removing all route table entries through " + nextHop, localAddr);
            Iterator itr = table.values().iterator();
            while (itr.hasNext()) {
                RouteTableEntry entry = (RouteTableEntry) itr.next();
                if (entry.getNextHop().equals(nextHop)) {
                    itr.remove();
                }
            }
        }

        /**
     * Returns all destinations through a given next hop.
     * 
     * @param hop Next hop address
     * @return list of destinations (of type NetAddress)
     */
        public LinkedList destsViaHop(MacAddress hop) {
            LinkedList list = new LinkedList();
            Iterator itr = table.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry kvpair = (Map.Entry) itr.next();
                RouteTableEntry rtentry = (RouteTableEntry) kvpair.getValue();
                NetAddress dest = (NetAddress) kvpair.getKey();
                if (rtentry.getNextHop().equals(hop)) {
                    list.add(dest);
                }
            }
            return list;
        }

        /**
     * Print contents of routing table, for debugging purposes.
     */
        public void printTable() {
            Iterator itr = table.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) itr.next();
                NetAddress dest = (NetAddress) mapEntry.getKey();
                RouteTableEntry route = (RouteTableEntry) mapEntry.getValue();
                printDebug("route_table: [" + dest + ": (", localAddr);
                if (route != null) {
                    printlnDebug_plain("nextHop=" + route.nextHop + " DSN=" + route.destSeqNum + " hopCnt=" + route.hopCount + ")]");
                } else {
                    printlnDebug_plain("null)]");
                }
            }
        }
    }

    /**
   * Information to be stored for each destination in routing table.
   */
    private static class RouteTableEntry {

        /** Next hop address. */
        private MacAddress nextHop;

        /** Latest known sequence number for destination node. */
        private int destSeqNum;

        /** Hop count for known route to destination. */
        private int hopCount;

        /**
     * Constructs a RouteTableEntry object.
     * 
     * @param nextHop next hop address
     * @param destSeqNum latest known sequence number of destination node
     * @param hopCount hop count
     */
        public RouteTableEntry(MacAddress nextHop, int destSeqNum, int hopCount) {
            this.nextHop = nextHop;
            this.destSeqNum = destSeqNum;
            this.hopCount = hopCount;
        }

        /**
     * Returns next hop address.
     * 
     * @return next hop address.
     */
        public MacAddress getNextHop() {
            return nextHop;
        }

        /**
     * Returns latest known sequence number for destination.
     * 
     * @return sequence number
     */
        public int getDestSeqNum() {
            return destSeqNum;
        }

        /**
     * Returns hop count for route.
     * 
     * @return hop count
     */
        public int getHopCount() {
            return hopCount;
        }

        /**
     * Sets a new latest known sequence number for destination node.
     * 
     * @param dsn sequence number
     */
        public void setDestSeqNum(int dsn) {
            destSeqNum = dsn;
        }
    }

    /**
   * A MessageQueue object temporarily stores transport-layer messages while routes are
   * being determined.  When route information becomes available, the messages are then
   * sent along the routes.
   */
    private static class MessageQueue {

        /** list of IP messages (with type NetMessage.Ip). */
        private LinkedList list;

        /** reference to this RouteAodv instance. */
        private RouteAodv thisNode;

        /**
     * Constructs a MessageQueue object, with an empty list.
     * 
     * @param thisNode reference to this RouteAodv instance
     */
        public MessageQueue(RouteAodv thisNode) {
            list = new LinkedList();
            this.thisNode = thisNode;
        }

        /**
     * Adds a NetMessage.Ip to the queue.
     * 
     * @param msg message to add to queue
     */
        public void add(NetMessage.Ip msg) {
            list.addLast(msg);
        }

        /**
     * Sends all messages in queue destined for a given destination via a given next hop.
     * 
     * @param dest destination address
     * @param nextHop next hop address
     */
        public void dequeueAndSend(NetAddress dest, MacAddress nextHop) {
            for (int i = 0; i < list.size(); i++) {
                if (((NetMessage.Ip) list.get(i)).getDst().equals(dest)) {
                    NetMessage.Ip msg = (NetMessage.Ip) list.remove(i);
                    printlnDebug("Routing IP message to " + nextHop, thisNode.netAddr);
                    thisNode.self.sendIpMsg(msg, nextHop);
                }
            }
        }

        /**
     * Removes all messages bound for a given destination.
     * 
     * @param dest destination net address
     */
        public void removeMsgsForDest(NetAddress dest) {
            Iterator itr = list.listIterator(0);
            while (itr.hasNext()) {
                NetMessage.Ip msg = (NetMessage.Ip) itr.next();
                if (msg.getDst().equals(dest)) {
                    itr.remove();
                }
            }
        }
    }

    /**
   * Represents the set of neighboring nodes which (likely) route through this node.
   * 
   * -A node periodically sends HELLO messages to its precursors, signaling to them
   * that this node is still in range to receive packets.
   * -A node also may send RERR messages to its precursors, informing them
   * of broken routes.
   */
    private static class PrecursorSet {

        /** Data structure for storing the precursor set. */
        private Map map = new HashMap();

        /** Reference to this RouteAodv instance. */
        private RouteAodv thisNode;

        /**
     * Constructs a new PrecursorSet object.
     * 
     * @param thisNode reference to this RouteAodv instance
     */
        public PrecursorSet(RouteAodv thisNode) {
            this.thisNode = thisNode;
        }

        /**
     * Returns an Iterator for the set.
     * 
     * Each item of the iterator is of type Map.Entry,
     * with map keys of type MacAddress, and map values of type PrecursorInfo
     * 
     * @return the iterator
     */
        public Iterator iterator() {
            return map.entrySet().iterator();
        }

        /**
     * Adds an item to the precursor set.
     * 
     * @param m Mac address of node to add to set
     */
        public void add(MacAddress m) {
            printlnDebug("Adding " + m + " to precursor set", thisNode.netAddr);
            map.put(m, new PrecursorInfo());
        }

        /**
     * Removes an item from the precursor set.
     * 
     * @param m Mac address of the node to remove from set
     */
        public void remove(MacAddress m) {
            printlnDebug("Removing " + m + " from precursor set", thisNode.netAddr);
            map.remove(m);
        }

        /**
     * Returns information for a precursor node.
     * 
     * @param m mac address of the precursor node
     * @return precursor information
     */
        public PrecursorInfo getInfo(MacAddress m) {
            return (PrecursorInfo) map.get(m);
        }

        /** 
     * Sends a RERR message to all precursors.
     * 
     * @param nodes the list of destination addresses (of type NetAddress) to include in RERR message
     * @param ttl TTL value to use
     */
        public void sendRERR(LinkedList nodes, byte ttl) {
            RouteErrorMessage rerrMsg = new RouteErrorMessage(nodes);
            NetMessage.Ip ipMsg = new NetMessage.Ip(rerrMsg, thisNode.netAddr, NetAddress.ANY, Constants.NET_PROTOCOL_AODV, Constants.NET_PRIORITY_NORMAL, ttl);
            Iterator itr = map.keySet().iterator();
            while (itr.hasNext()) {
                MacAddress macAddr = (MacAddress) itr.next();
                printlnDebug("Sending RERR to precursor " + macAddr, thisNode.netAddr);
                thisNode.self.sendIpMsg(ipMsg, macAddr);
                if (thisNode.stats != null) {
                    thisNode.stats.send.rerrPackets++;
                    thisNode.stats.send.aodvPackets++;
                }
            }
        }
    }

    /**
   * Information stored for each precursor node.
   */
    private static class PrecursorInfo {

        /** time of last message sent to precursor. */
        private long lastMsgTime;

        /**
     * Constructs a new precursor entry.
     */
        public PrecursorInfo() {
            lastMsgTime = JistAPI.getTime();
        }

        /**
     * Returns the time that the last message was sent to this precursor.
     * 
     * @return time that last message was sent to precursor
     */
        public long getLastMsgTime() {
            return lastMsgTime;
        }

        /**
     * Updates the precursor entry with the current time, indicating the most recent
     * time that a message was sent to the precursor.
     * This should be called whenever any message is sent to the precursor.
     */
        public void renew() {
            lastMsgTime = JistAPI.getTime();
        }
    }

    /**
   * Represents the set of neighboring nodes through which this node routes messages.
   * 
   * The node expects to periodically receive messages (HELLO or other) from each
   * neighbor in its outgoing set.  If it does not receive any messages from a
   * particular neighbor over a certain period of time, it can assume that neighbor
   * is no longer within range.
   * 
   * Each node in this set is mapped to a corresponding OutgoingInfo object. 
   */
    private static class OutgoingSet {

        /** Data structure for the outgoing node set. */
        private Map map = new HashMap();

        /** Local net address. */
        private NetAddress localAddr;

        /**
     * Constructs a new outgoingSet object.
     * 
     * @param netAddr local net address
     */
        public OutgoingSet(NetAddress netAddr) {
            localAddr = netAddr;
        }

        /**
     * Returns an iterator for this outgoing set.
     * 
     * @return the iterator
     */
        public Iterator iterator() {
            return map.entrySet().iterator();
        }

        /**
     * Adds an entry to the outgoing node set.
     * 
     * @param m mac address of node to add
     */
        public void add(MacAddress m) {
            printlnDebug("Adding " + m + " to outgoing set", localAddr);
            map.put(m, new OutgoingInfo());
        }

        /**
     * Returns the outgoing node info for a given MAC address.
     *  
     * @param m the given MAC address
     * @return the corresponding outgoing node info
     */
        public OutgoingInfo getInfo(MacAddress m) {
            return (OutgoingInfo) map.get(m);
        }
    }

    /**
   * Information for each node in the outgoing node set.
   */
    private static class OutgoingInfo {

        /** Indication of how long the node has been waiting for HELLO from the outgoing node. */
        private byte helloWaitCount;

        /**
     * Constructs an Outgoing Set entry.
     */
        public OutgoingInfo() {
            helloWaitCount = 0;
        }

        /**
     * Returns a count of the HELLO intervals that have passed since last receiving
     * a message from this outgoing node.
     * 
     * @return hello interval count
     */
        public byte getHelloWaitCount() {
            return helloWaitCount;
        }

        /**
     * Increment the count of HELLO intervals that have passed.
     */
        public void incHelloWaitCount() {
            helloWaitCount++;
        }

        /**
     * Set count of HELLO intervals to zero.
     */
        public void resetHelloWaitCount() {
            helloWaitCount = 0;
        }
    }

    /** Network entity. */
    private NetInterface netEntity;

    /** Self-referencing proxy entity. */
    private RouteInterface.Aodv self;

    /** local network address. */
    private NetAddress netAddr;

    /** node sequence number. */
    private int seqNum;

    /** sequence number for RREQ id's. */
    private int rreqIdSeqNum;

    /** routing table. */
    private RouteTable routeTable;

    /** list of pending route requests (originated by this node). */
    private LinkedList rreqList;

    /** buffer for storing info about previously sent RREQ messages. */
    private RreqBuffer rreqBuffer;

    /** buffer for storing messages that need routes. */
    private MessageQueue msgQueue;

    /** set of nodes that route through this node. */
    private PrecursorSet precursorSet;

    /** set of nodes that this node routes through. */
    private OutgoingSet outgoingSet;

    /** statistics accumulator. */
    private AodvStats stats;

    /**
   * Constructs new RouteAodv instance.
   * 
   * @param addr node's network address
   */
    public RouteAodv(NetAddress addr) {
        this.netAddr = addr;
        this.seqNum = SEQUENCE_NUMBER_START;
        this.rreqIdSeqNum = RREQ_ID_SEQUENCE_NUMBER_START;
        this.self = (RouteInterface.Aodv) JistAPI.proxy(this, RouteInterface.Aodv.class);
        this.rreqList = new LinkedList();
        this.rreqBuffer = new RreqBuffer(addr);
        this.msgQueue = new MessageQueue(this);
        this.precursorSet = new PrecursorSet(this);
        this.outgoingSet = new OutgoingSet(addr);
        this.routeTable = new RouteTable(addr);
        routeTable.add(this.netAddr, new RouteTableEntry(MacAddress.NULL, this.seqNum, 0));
        routeTable.printTable();
    }

    /**
   * This event is called periodically after a route request is originated, until
   * a route has been found.
   * 
   * Each time it is called, it rebroadcasts the route request message with a new
   * rreq id and incremented TTL. 
   * 
   * @param rreqObj RouteRequest object
   */
    public void RREQtimeout(Object rreqObj) {
        RouteRequest rreq = (RouteRequest) rreqObj;
        if (!rreq.routeFound) {
            printlnDebug("RREQ timeout event at " + JistAPI.getTime());
            if (rreq.getTtl() < TTL_THRESHOLD) {
                rreq.obtainNewRreqId();
                rreq.incTtl();
                rreq.broadcast();
                JistAPI.sleep(computeRREQTimeout(rreq.getTtl()));
                self.RREQtimeout(rreqObj);
            } else {
                msgQueue.removeMsgsForDest(rreq.getDest());
                rreqList.remove(rreqObj);
            }
        }
    }

    /**
   * AODV Timeout event, which gets called periodically at fixed intervals.
   * 
   * Clears expired RREQ buffer entries.
   * Sends hello messages.
   * Updates wait counters, and checks for idle outgoing-nodes
   */
    public void timeout() {
        printlnDebug("Timeout at " + JistAPI.getTime());
        rreqBuffer.clearExpiredEntries();
        if (HELLO_MESSAGES_ON) {
            helloSendEvent();
            helloWaitEvent();
        }
        JistAPI.sleep(AODV_TIMEOUT);
        self.timeout();
    }

    /**
   * Send hello messages to any precursors to which we have not sent a message in the
   * past HELLO_INTERVAL time.
   */
    private void helloSendEvent() {
        NetMessage.Ip helloMsgIp = null;
        Iterator itr = precursorSet.iterator();
        while (itr.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) itr.next();
            MacAddress macAddr = (MacAddress) mapEntry.getKey();
            PrecursorInfo precInfo = (PrecursorInfo) mapEntry.getValue();
            if (JistAPI.getTime() >= precInfo.getLastMsgTime() + HELLO_INTERVAL) {
                printlnDebug("Sending HELLO message to macAddr " + macAddr);
                if (helloMsgIp == null) {
                    HelloMessage helloMsg = new HelloMessage(this.netAddr, this.seqNum);
                    helloMsgIp = new NetMessage.Ip(helloMsg, this.netAddr, NetAddress.ANY, Constants.NET_PROTOCOL_AODV, Constants.NET_PRIORITY_NORMAL, (byte) 1);
                }
                self.sendIpMsg(helloMsgIp, macAddr);
                if (stats != null) {
                    stats.send.helloPackets++;
                    stats.send.aodvPackets++;
                }
                precInfo.renew();
            }
        }
    }

    /**
   * Increments the HELLO_wait counter for each outgoing node.  If this event gets
   * called more than HELLO_ALLOWED_LOSS times without us having heard a message from
   * some outgoing node, then we can assume that outgoing node is no longer reachable,
   * and we update our data structures accordingly, and send out RERR messages to our
   * precursors. 
   */
    private void helloWaitEvent() {
        Iterator itr = outgoingSet.iterator();
        while (itr.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) itr.next();
            MacAddress macAddr = (MacAddress) mapEntry.getKey();
            OutgoingInfo outInfo = (OutgoingInfo) mapEntry.getValue();
            if (outInfo.getHelloWaitCount() > HELLO_ALLOWED_LOSS) {
                this.routeTable.removeNextHop(macAddr);
                precursorSet.remove(macAddr);
                printlnDebug("Removing " + macAddr + " from outgoing set");
                itr.remove();
                precursorSet.sendRERR(routeTable.destsViaHop(macAddr), Constants.TTL_DEFAULT);
            } else {
                outInfo.incHelloWaitCount();
            }
        }
    }

    /**
   * Sends IP message after transmission delay, and renews precursor list entry.
   * 
   * @param ipMsg IP message to send
   * @param destMacAddr next hop mac address
   */
    public void sendIpMsg(NetMessage.Ip ipMsg, MacAddress destMacAddr) {
        randomSleep(TRANSMISSION_JITTER);
        netEntity.send(ipMsg, Constants.NET_INTERFACE_DEFAULT, destMacAddr);
        if (destMacAddr.equals(MacAddress.ANY)) {
            Iterator itr = this.precursorSet.iterator();
            while (itr.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) itr.next();
                PrecursorInfo precInfo = (PrecursorInfo) mapEntry.getValue();
                printlnDebug("Renewing precusor entry for " + (MacAddress) mapEntry.getKey());
                precInfo.renew();
            }
        } else {
            PrecursorInfo precInfo = precursorSet.getInfo(destMacAddr);
            if (precInfo != null) {
                printlnDebug("Renewing precursor entry for " + destMacAddr);
                precInfo.renew();
            }
        }
    }

    /** {@inheritDoc} */
    public void start() {
        self.timeout();
    }

    /**
   * Called by the network layer for every incoming packet. A routing
   * implementation may wish to look at these packets for informational
   * purposes, but should not change their contents.
   *
   * @param msg incoming packet
   * @param lastHop last link-level hop of incoming packet
   */
    public void peek(NetMessage msg, MacAddress lastHop) {
        OutgoingInfo lastHopInfo = outgoingSet.getInfo(lastHop);
        if (lastHopInfo != null) {
            printlnDebug("Peeking at message from " + lastHop + "; resetting hello_wait_count");
            lastHopInfo.resetHelloWaitCount();
        }
    }

    /**
   * Called by the network layer to request transmission of a packet that 
   * requires routing. It is the responsibility of the routing layer to provide
   * a best-effort transmission of this packet to an appropriate next hop by
   * calling the network slayer sending routines once this routing information
   * becomes available.
   *
   * @param msg outgoing packet
   */
    public void send(NetMessage msg) {
        NetMessage.Ip ipMsg = (NetMessage.Ip) msg;
        if (ipMsg.getDst().equals(netAddr)) {
            throw new RuntimeException("Message is already at destination.  Why is RouteAodv.send() being called?");
        }
        printlnDebug("Attempting to route from " + netAddr + " to " + ipMsg.getDst());
        printlnDebug("src=" + ipMsg.getSrc() + " dst=" + ipMsg.getDst() + " prot=" + ipMsg.getProtocol() + " ttl=" + ipMsg.getTTL() + " getMsg=" + ipMsg.getPayload());
        NetAddress destNetAddr = ipMsg.getDst();
        RouteTableEntry routeEntry = routeTable.lookup(destNetAddr);
        MacAddress nextHopMacAddr = routeEntry == null ? null : routeEntry.getNextHop();
        if (nextHopMacAddr != null) {
            printlnDebug("Attempting to route from " + netAddr + " to " + ipMsg.getDst() + " via " + nextHopMacAddr);
            printlnDebug("sent ipMsg:" + " src=" + ipMsg.getSrc() + " dst=" + ipMsg.getDst() + " TTL=" + ipMsg.getTTL());
            self.sendIpMsg(ipMsg, nextHopMacAddr);
        } else {
            msgQueue.add(ipMsg);
            RouteRequest rreq = new RouteRequest(destNetAddr, this);
            printlnDebug("Adding rreq id " + rreq.getRreqId() + " to rreq list");
            rreqList.add(rreq);
            rreq.broadcast();
            if (stats != null) {
                stats.rreqOrig++;
            }
            JistAPI.sleep(computeRREQTimeout(rreq.getTtl()));
            self.RREQtimeout(rreq);
        }
    }

    /**
   * Receive a message from network layer.
   *
   * @param msg message received
   * @param src source network address
   * @param lastHop source link address
   * @param macId mac identifier
   * @param dst destination network address
   * @param priority packet priority
   * @param ttl packet time-to-live
   */
    public void receive(Message msg, NetAddress src, MacAddress lastHop, byte macId, NetAddress dst, byte priority, byte ttl) {
        RouteRequestMessage rreqMsg = null;
        printlnDebug("receive: src=" + src + " lastHop=" + lastHop + " dst=" + dst + " ttl=" + ttl);
        if (msg instanceof HelloMessage) {
            receiveHelloMessage((HelloMessage) msg);
            if (stats != null) {
                stats.recv.helloPackets++;
                stats.recv.aodvPackets++;
            }
        } else if (msg instanceof RouteRequestMessage) {
            receiveRouteRequestMessage((RouteRequestMessage) msg, src, lastHop, dst, priority, ttl);
            if (stats != null) {
                stats.recv.aodvPackets++;
                stats.recv.rreqPackets++;
            }
        } else if (msg instanceof RouteReplyMessage) {
            receiveRouteReplyMessage((RouteReplyMessage) msg, src, lastHop, dst, priority, ttl);
            if (stats != null) {
                stats.recv.aodvPackets++;
                stats.recv.rrepPackets++;
            }
        } else if (msg instanceof RouteErrorMessage) {
            receiveRouteErrorMessage((RouteErrorMessage) msg, lastHop, ttl);
            if (stats != null) {
                stats.recv.aodvPackets++;
                stats.recv.rerrPackets++;
            }
        } else {
            throw new RuntimeException("RouteAodv.receive() does not know how to handle message of type" + msg);
        }
    }

    /**
   * Process an incoming RREQ message.
   * 
   * @param rreqMsg incoming route request message
   * @param src source of message
   * @param lastHop last hop of message
   * @param dst destination of message
   * @param priority message priority
   * @param ttl message TTL
   */
    private void receiveRouteRequestMessage(RouteRequestMessage rreqMsg, NetAddress src, MacAddress lastHop, NetAddress dst, byte priority, byte ttl) {
        if (DEBUG_MODE) {
            printlnDebug("Receiving RREQ:" + " rreqId=" + rreqMsg.getRreqId() + " destIp=" + rreqMsg.getDestIp() + " origIp=" + rreqMsg.getOrigIp() + " destSN=" + rreqMsg.getDestSeqNum() + " origSN=" + rreqMsg.getOrigSeqNum() + " unkDSN=" + rreqMsg.getUnknownDestSeqNum() + " hopCnt=" + rreqMsg.getHopCount());
        }
        RouteTableEntry origRouteEntry = routeTable.lookup(rreqMsg.getOrigIp());
        boolean updateRoute = shouldUpdateRouteToOrigin(rreqMsg, origRouteEntry);
        if (updateRoute) {
            routeTable.add(rreqMsg.getOrigIp(), new RouteTableEntry(lastHop, rreqMsg.getOrigSeqNum(), rreqMsg.getHopCount() + 1));
            routeTable.printTable();
        }
        boolean isDest = rreqMsg.destIp.equals(this.netAddr);
        RouteTableEntry destRouteEntry = routeTable.lookup(rreqMsg.destIp);
        boolean routeToDestExists = (destRouteEntry != null && destRouteEntry.nextHop != null);
        boolean hasFreshRoute = routeToDestExists && !rreqMsg.getUnknownDestSeqNum() && destRouteEntry.getDestSeqNum() > rreqMsg.getDestSeqNum();
        boolean inRreqBuffer = rreqBuffer.contains(new RreqBufferEntry(rreqMsg.getRreqId(), rreqMsg.getOrigIp()));
        if (isDest || hasFreshRoute) {
            if (!inRreqBuffer || updateRoute) {
                generateRouteReplyMessage(rreqMsg, isDest, destRouteEntry);
            }
        } else {
            if (!inRreqBuffer) {
                byte newTtl = (byte) (ttl - 1);
                if (newTtl > 0) {
                    printlnDebug("Forwarding RREQ");
                    forwardRouteRequestMessage(rreqMsg, newTtl, destRouteEntry);
                }
            }
        }
    }

    /**
   * Process an incoming RREP message.
   * 
   * @param rrepMsg incoming route reply message
   * @param src source of message
   * @param lastHop last hop of message
   * @param dst destination of message
   * @param priority message priority
   * @param ttl message TTL value
   */
    private void receiveRouteReplyMessage(RouteReplyMessage rrepMsg, NetAddress src, MacAddress lastHop, NetAddress dst, byte priority, byte ttl) {
        printlnDebug("handling RREP:" + " destIp=" + rrepMsg.getDestIp() + " destSN=" + rrepMsg.getDestSeqNum() + " origIp=" + rrepMsg.getOrigIp() + " hopCnt=" + rrepMsg.getHopCount());
        RouteTableEntry entry = routeTable.lookup(rrepMsg.getDestIp());
        if (entry == null || (rrepMsg.getDestSeqNum() > entry.getDestSeqNum()) || (rrepMsg.getDestSeqNum() == entry.getDestSeqNum() && rrepMsg.hopCount < entry.getHopCount())) {
            routeTable.add(rrepMsg.getDestIp(), new RouteTableEntry(lastHop, rrepMsg.getDestSeqNum(), rrepMsg.getHopCount() + 1));
            routeTable.printTable();
            outgoingSet.add(lastHop);
            precursorSet.add(lastHop);
        }
        if (this.netAddr.equals(rrepMsg.getOrigIp())) {
            Iterator itr = rreqList.iterator();
            while (itr.hasNext()) {
                RouteRequest rreq = (RouteRequest) itr.next();
                if (rreq.getDest().equals(rrepMsg.getDestIp())) {
                    printlnDebug("Removing rreq from rreqlist");
                    rreq.setRouteFound(true);
                    if (stats != null) {
                        stats.rreqSucc++;
                    }
                    itr.remove();
                }
            }
            msgQueue.dequeueAndSend(rrepMsg.getDestIp(), lastHop);
        } else {
            RouteTableEntry origRouteEntry = routeTable.lookup(rrepMsg.getOrigIp());
            if (origRouteEntry != null && (ttl > 0)) {
                MacAddress nextHop = origRouteEntry.getNextHop();
                rrepMsg.incHopCount();
                NetMessage.Ip rrepMsgIp = new NetMessage.Ip(rrepMsg, src, dst, Constants.NET_PROTOCOL_AODV, Constants.NET_PRIORITY_NORMAL, (byte) (ttl - 1));
                printlnDebug("Forwarding RREP message to node " + nextHop);
                self.sendIpMsg(rrepMsgIp, nextHop);
                if (stats != null) {
                    stats.send.rrepPackets++;
                    stats.send.aodvPackets++;
                }
                this.precursorSet.add(nextHop);
                this.outgoingSet.add(nextHop);
            }
        }
    }

    /**
   * Process an incoming RERR message.
   * 
   * @param rerrMsg incoming route error message
   * @param lastHop last hop of message
   * @param ttl message TTL value
   */
    private void receiveRouteErrorMessage(RouteErrorMessage rerrMsg, MacAddress lastHop, byte ttl) {
        printlnDebug("Receiving RERR message from " + lastHop);
        boolean somethingRemoved = routeTable.removeList(rerrMsg.getUnreachableList());
        if (somethingRemoved && ttl > 0) {
            precursorSet.sendRERR(rerrMsg.getUnreachableList(), (byte) (ttl - 1));
        }
    }

    /**
   * Process an incoming HELLO message.
   * 
   * @param helloMsg incoming hello message
   */
    private void receiveHelloMessage(HelloMessage helloMsg) {
        printlnDebug("Receiving HELLO message from " + helloMsg.getIp());
    }

    /**
   * Forwards a RREQ message to all neighbors.
   * 
   * @param rreqMsg incoming route request message
   * @param newTtl  TTL value for the (new) RREQ message to be forwarded
   * @param destRouteEntry route table entry for destination node
   */
    private void forwardRouteRequestMessage(RouteRequestMessage rreqMsg, byte newTtl, RouteTableEntry destRouteEntry) {
        rreqBuffer.addEntry(new RreqBufferEntry(rreqMsg.getRreqId(), rreqMsg.getOrigIp()));
        RouteRequestMessage newRreqMsg = new RouteRequestMessage(rreqMsg);
        newRreqMsg.incHopCount();
        if (destRouteEntry != null) {
            boolean condition = newRreqMsg.getUnknownDestSeqNum() || destRouteEntry.getDestSeqNum() > newRreqMsg.getDestSeqNum();
            if (condition) {
                newRreqMsg.setDestSeqNum(destRouteEntry.getDestSeqNum());
                newRreqMsg.setUnknownDestSeqNum(false);
            }
        }
        NetMessage.Ip rreqMsgIp = new NetMessage.Ip(newRreqMsg, this.netAddr, NetAddress.ANY, Constants.NET_PROTOCOL_AODV, Constants.NET_PRIORITY_NORMAL, newTtl);
        self.sendIpMsg(rreqMsgIp, MacAddress.ANY);
        if (stats != null) {
            stats.send.rreqPackets++;
            stats.send.aodvPackets++;
        }
    }

    /**
   * Generates and sends a RREP message.
   * 
   * @param rreqMsg RREQ message that this RREP message is responding to
   * @param isDest true if this node is the destination of the RREQ message
   * @param destRouteEntry route table entry for the destination node, if this is not dest. (otherwise, null)
   */
    private void generateRouteReplyMessage(RouteRequestMessage rreqMsg, boolean isDest, RouteTableEntry destRouteEntry) {
        RreqBufferEntry newEntry = new RreqBufferEntry(rreqMsg.rreqId, rreqMsg.origIp);
        if (!rreqBuffer.contains(newEntry)) {
            rreqBuffer.addEntry(newEntry);
        }
        int initialHopCount = 0;
        if (!isDest) {
            initialHopCount = destRouteEntry.getHopCount();
        }
        if (!rreqMsg.getUnknownDestSeqNum() && rreqMsg.getDestSeqNum() > this.seqNum) {
            this.seqNum = rreqMsg.getDestSeqNum();
            RouteTableEntry selfRoute = routeTable.lookup(this.netAddr);
            selfRoute.setDestSeqNum(this.seqNum);
            routeTable.printTable();
        }
        RouteReplyMessage rrepMsg = new RouteReplyMessage(rreqMsg.getDestIp(), this.seqNum, rreqMsg.getOrigIp(), initialHopCount);
        MacAddress nextHop = routeTable.lookup(rreqMsg.getOrigIp()).getNextHop();
        NetMessage.Ip rrepMsgIp = new NetMessage.Ip(rrepMsg, this.netAddr, NetAddress.ANY, Constants.NET_PROTOCOL_AODV, Constants.NET_PRIORITY_NORMAL, Constants.TTL_DEFAULT);
        printlnDebug("Sending RREP to " + nextHop);
        self.sendIpMsg(rrepMsgIp, nextHop);
        if (stats != null) {
            stats.send.rrepPackets++;
            stats.send.aodvPackets++;
            stats.rrepOrig++;
        }
        precursorSet.add(nextHop);
        outgoingSet.add(nextHop);
    }

    /**
   * Decides whether a node receiving a RREQ message should update its route to the
   * RREQ originator.
   * 
   * @param rreqMsg incoming route request message
   * @param origRouteEntry existing routing table entry for the RREQ-originating node
   * @return true, if node should update its routing table entry to RREQ-originating node
   */
    private boolean shouldUpdateRouteToOrigin(RouteRequestMessage rreqMsg, RouteTableEntry origRouteEntry) {
        if (origRouteEntry == null || origRouteEntry.getNextHop() == null) return true;
        if (rreqMsg.getOrigSeqNum() > origRouteEntry.getDestSeqNum()) return true;
        boolean equalSeqNum = rreqMsg.getOrigSeqNum() == origRouteEntry.getDestSeqNum();
        boolean hopCountBetter = rreqMsg.getHopCount() + 1 < origRouteEntry.getHopCount();
        if (equalSeqNum && hopCountBetter) {
            return true;
        }
        return false;
    }

    /**
   * Computes the RREQ Timeout period, given a TTL value.
   * 
   * @param ttl TTL value
   * @return timeout period
   */
    private long computeRREQTimeout(byte ttl) {
        return RREQ_TIMEOUT_BASE + RREQ_TIMEOUT_PER_TTL * ttl;
    }

    /**
   * Sleep for a random time. 
   * 
   * @param time max sleep time
   */
    private static void randomSleep(long time) {
        JistAPI.sleep(Util.randomTime(time));
    }

    /**
    * Sets aodv statistics object.
    *
    * @param stats aodv statistics object
    */
    public void setStats(AodvStats stats) {
        this.stats = stats;
    }

    /**
   * Gets node's local address.
   * 
   * @return local address
   */
    public NetAddress getLocalAddr() {
        return this.netAddr;
    }

    /**
   * Returns self-referencing proxy entity.
   * 
   * @return self-referencing proxy entity
   */
    public RouteInterface.Aodv getProxy() {
        return this.self;
    }

    /**
   * Sets network entity.
   * 
   * @param netEntity network entity
   */
    public void setNetEntity(NetInterface netEntity) {
        this.netEntity = netEntity;
    }

    /**
   * Println given string with JiST time and local net address, if debug mode on.
   * 
   * @param s string to print
   */
    private void printlnDebug(String s) {
        if (RouteAodv.DEBUG_MODE) {
            System.out.println(JistAPI.getTime() + "\t" + netAddr + ": " + s);
        }
    }

    /**
   * Print given string with JiST time and local net address, if debug mode on.
   * 
   * @param s string to print
   */
    private void printDebug(String s) {
        if (RouteAodv.DEBUG_MODE) {
            System.out.print(JistAPI.getTime() + "\t" + netAddr + ": " + s);
        }
    }

    /**
   * Println given string with JiST time and given net address, if debug mode on.
   * 
   * @param s string to print
   * @param addr node address
   */
    private static void printlnDebug(String s, NetAddress addr) {
        if (RouteAodv.DEBUG_MODE) {
            System.out.println(JistAPI.getTime() + "\t" + addr + ": " + s);
        }
    }

    /**
   * Print given string with JiST time and given net address, if debug mode on.
   * 
   * @param s string to print
   * @param addr node address
   */
    private static void printDebug(String s, NetAddress addr) {
        if (RouteAodv.DEBUG_MODE) {
            System.out.print(JistAPI.getTime() + "\t" + addr + ": " + s);
        }
    }

    /**
   * Println given string only if debug mode on.
   * 
   * @param s string to print
   */
    private static void printlnDebug_plain(String s) {
        if (RouteAodv.DEBUG_MODE) {
            System.out.println(s);
        }
    }

    /**
   * Print given string only if debug mode on.
   * @param s string to print
   */
    private static void printDebug_plain(String s) {
        if (RouteAodv.DEBUG_MODE) {
            System.out.print(s);
        }
    }

    /**
   * Prints the node's precusor set.
   */
    public void printPrecursors() {
        Set keySet = precursorSet.map.keySet();
        Iterator itr = keySet.iterator();
        System.out.print(netAddr + ": prec: ");
        while (itr.hasNext()) {
            MacAddress mac = (MacAddress) itr.next();
            System.out.print(mac + ", ");
        }
        System.out.println();
    }

    /**
   * Prints the node's outgoing set.
   */
    public void printOutgoing() {
        Set keySet = outgoingSet.map.keySet();
        Iterator itr = keySet.iterator();
        System.out.print(netAddr + ": outg: ");
        while (itr.hasNext()) {
            MacAddress mac = (MacAddress) itr.next();
            System.out.print(mac + ", ");
        }
        System.out.println();
    }
}
