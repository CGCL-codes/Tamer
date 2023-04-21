package hypercast.DT;

import hypercast.*;
import hypercast.events.*;
import hypercast.util.XmlUtil;
import java.util.*;
import java.io.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.xpath.*;

public abstract class DT_Node implements I_AdapterCallback, I_Node {

    /** debug flag */
    static final boolean debug = false;

    /** It may be used to generate the node's LogicalAddress. */
    protected static Random rand = new Random();

    /** The property prefix, it can be changed by constructors. */
    protected String PROPERTY_PROTO_PREFIX = "/Public/Node/DTServer/";

    /** Adapter timer index used for heartbeat. */
    private static final int HEARTBEAT_TIMER_INDEX = 0;

    /** Name of property controlling keep alive. */
    private static final String SLOW_HEARTBEAT_TIME_PROPERTY_NAME = "SlowHeartbeatTime";

    /** Name of property controlling update. */
    private static final String FAST_HEARTBEAT_TIME_PROPERTY_NAME = "FastHeartbeatTime";

    /** Name of property controlling timeout time. */
    private static final String TIMEOUT_TIME_PROPERTY_NAME = "TimeoutTime";

    /** Name of property controlling coordinates. */
    private static final String NODE_COORDS_PROPERTY_NAME = "Coords";

    /** In milliseconds, the time between the keep-alive messages. 
     * Set from the HyperCastConfig object.
     */
    private long SLOW_HEARTBEAT_TIME;

    /** In milliseconds, the maximum time to wait to send a message to
     * the neighbors informing them of a change to the neighbor table.*/
    private long FAST_HEARTBEAT_TIME;

    /** In milliseconds, the time required before a silent neighbor is removed. 
     * Set from the HyperCastConfig object.
     */
    protected long TIMEOUT_TIME;

    /** Used to coordinate resetting and clearing of the heartbeat timer.*/
    private boolean timerCleared;

    /** The list of neighbors */
    protected DT_Neighborhood neighborhood;

    /** Source of constants & error/log message display */
    protected HyperCastConfig config;

    /** Network adapter (used for timers and Physical Address). */
    protected I_UnicastAdapter adapter;

    /** True if joinOverlay has been called without matching leaveOverlay. */
    protected boolean joined;

    /** Used the calculate the burst-message-load. */
    private long timeOfLastHeartbeat;

    /** Used the calculate the burst-message-load. */
    protected int messagesReceivedInLastHeartbeat;

    /** Used the calculate the burst-message-load. */
    protected int messagesSentInLastHeartbeat;

    /** Max burst seen since last joinOverlay. */
    private double maxReceiveRateInAHeartbeat;

    /** Max burst seen since last joinOverlay. */
    private double maxSendRateInAHeartbeat;

    /** Keeps track of whether heartbeat is set to Fast or Slow heartbeat. */
    private boolean HeartbeatSetToFastHeartbeat;

    /** True if isStable() returned true at the last expiration of the heartbeat timer. */
    private boolean StableAtHeartbeat;

    /** Originally it is set to false. It is set to true when this node starts
	 * sending protocol message to other nodes. Once it is set to true, this node 
	 * will check the status to determine the heartbeat speed.
	 */
    protected boolean StartConsiderStable;

    /** Debug string. */
    String TimerCallbackDebug;

    /** Debug string. */
    String MessageCallbackDebug;

    /** Time of the call to joinGroup() */
    private long startTime;

    /** Time of last change to the neighborhood.  Used to determine how long a node has been stable. */
    private long lastChangeToNeighborhood;

    /** Time of the call to leaveOverlay() */
    private long stopTime;

    /** Object used to prevent problems while starting and stopping the node. */
    Object startStopLock;

    /** GNP_measurement component */
    GNP_DT gnpAgent;

    /** The protocol subfield value which represents the used rendezvous and will be put 
	 * in the Proto_sub field of a DT_Message. It will be changed in the constructors 
	 * of DT_Node's derived class.
	 */
    byte Proto_Sub = I_Node.PROTOSUB_NOSPEC;

    /** Notification handler. When interesting events happen, this node 
	 * reports the events to the notification handler if it is not null.
	 */
    protected NotificationHandler n_handler = null;

    /** Check mode on the previous hop of OL messages.
	 */
    protected String checkmode;

    /**
	 * A StatsProcessor instance which handles the statistics in this object.
	 */
    protected StatsProcessor statsPro;

    /**
	 * The statistics name for this object (as an I_Stats instance). Its default
	 * name is defined in the socket configuration schema file. It can be assigned
	 * by any other object through method setStattisticsName defined in the 
	 * I_Stats interface.
	 */
    protected String statisticsName;

    DT_Node(HyperCastConfig c, I_UnicastAdapter a) {
        config = c;
        adapter = a;
        a.setCallback(this);
        checkmode = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Verification"));
        FAST_HEARTBEAT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + FAST_HEARTBEAT_TIME_PROPERTY_NAME));
        SLOW_HEARTBEAT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + SLOW_HEARTBEAT_TIME_PROPERTY_NAME));
        TIMEOUT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + TIMEOUT_TIME_PROPERTY_NAME));
        if (SLOW_HEARTBEAT_TIME < FAST_HEARTBEAT_TIME) {
            config.err.println("DT_Node: SlowHeartbeat time is less than FastHeartbeat time - setting it to FastHeartbeat time.");
            SLOW_HEARTBEAT_TIME = FAST_HEARTBEAT_TIME;
        }
        if (TIMEOUT_TIME < SLOW_HEARTBEAT_TIME) {
            config.err.println("DT_Node: Timeout time is less than SlowHeartbeat time - setting it to 5*SlowHeartbeat time.");
            TIMEOUT_TIME = 5 * SLOW_HEARTBEAT_TIME;
        }
        startStopLock = new Object();
        timerCleared = true;
        joined = false;
        InitStatisticsStructure();
        neighborhood = new DT_Neighborhood(c, a, createLogicalAddress(), statsPro);
    }

    DT_Node(HyperCastConfig c, I_UnicastAdapter a, String prefix) {
        PROPERTY_PROTO_PREFIX = prefix;
        config = c;
        adapter = a;
        a.setCallback(this);
        checkmode = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Verification"));
        FAST_HEARTBEAT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + FAST_HEARTBEAT_TIME_PROPERTY_NAME));
        SLOW_HEARTBEAT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + SLOW_HEARTBEAT_TIME_PROPERTY_NAME));
        TIMEOUT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + TIMEOUT_TIME_PROPERTY_NAME));
        if (SLOW_HEARTBEAT_TIME < FAST_HEARTBEAT_TIME) {
            config.err.println("DT_Node: SlowHeartbeat time is less than FastHeartbeat time - setting it to FastHeartbeat time.");
            SLOW_HEARTBEAT_TIME = FAST_HEARTBEAT_TIME;
        }
        if (TIMEOUT_TIME < SLOW_HEARTBEAT_TIME) {
            config.err.println("DT_Node: Timeout time is less than SlowHeartbeat time - setting it to 5*SlowHeartbeat time.");
            TIMEOUT_TIME = 5 * SLOW_HEARTBEAT_TIME;
        }
        startStopLock = new Object();
        timerCleared = true;
        joined = false;
        InitStatisticsStructure();
        neighborhood = new DT_Neighborhood(c, a, createLogicalAddress(), statsPro);
    }

    DT_Node(HyperCastConfig c, I_UnicastAdapter a, DT_LogicalAddress startingCoords) {
        config = c;
        adapter = a;
        a.setCallback(this);
        checkmode = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Verification"));
        FAST_HEARTBEAT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + FAST_HEARTBEAT_TIME_PROPERTY_NAME));
        SLOW_HEARTBEAT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + SLOW_HEARTBEAT_TIME_PROPERTY_NAME));
        TIMEOUT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + TIMEOUT_TIME_PROPERTY_NAME));
        if (SLOW_HEARTBEAT_TIME < FAST_HEARTBEAT_TIME) {
            config.err.println("DT_Node: KeepAlive time is less than Update time - setting it to Update time.");
            SLOW_HEARTBEAT_TIME = FAST_HEARTBEAT_TIME;
        }
        if (TIMEOUT_TIME < SLOW_HEARTBEAT_TIME) {
            config.err.println("DT_Node: Timeout time is less than KeepAlive time - setting it to 5*KeepAlive time.");
            TIMEOUT_TIME = 5 * SLOW_HEARTBEAT_TIME;
        }
        startStopLock = new Object();
        timerCleared = true;
        joined = false;
        InitStatisticsStructure();
        neighborhood = new DT_Neighborhood(c, a, startingCoords, statsPro);
    }

    DT_Node(HyperCastConfig c, I_UnicastAdapter a, DT_LogicalAddress startingCoords, String prefix) {
        PROPERTY_PROTO_PREFIX = prefix;
        config = c;
        adapter = a;
        a.setCallback(this);
        checkmode = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Verification"));
        FAST_HEARTBEAT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + FAST_HEARTBEAT_TIME_PROPERTY_NAME));
        SLOW_HEARTBEAT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + SLOW_HEARTBEAT_TIME_PROPERTY_NAME));
        TIMEOUT_TIME = config.getPositiveLongAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + TIMEOUT_TIME_PROPERTY_NAME));
        if (SLOW_HEARTBEAT_TIME < FAST_HEARTBEAT_TIME) {
            config.err.println("DT_Node: KeepAlive time is less than Update time - setting it to Update time.");
            SLOW_HEARTBEAT_TIME = FAST_HEARTBEAT_TIME;
        }
        if (TIMEOUT_TIME < SLOW_HEARTBEAT_TIME) {
            config.err.println("DT_Node: Timeout time is less than KeepAlive time - setting it to 5*KeepAlive time.");
            TIMEOUT_TIME = 5 * SLOW_HEARTBEAT_TIME;
        }
        startStopLock = new Object();
        timerCleared = true;
        joined = false;
        InitStatisticsStructure();
        neighborhood = new DT_Neighborhood(c, a, startingCoords, statsPro);
    }

    /** Creates the node's logical address given properties. 
 * @throws IllegalArgumentException if properties are incorrectly formatted.
 */
    private DT_LogicalAddress createLogicalAddress() {
        String coordString = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + NODE_COORDS_PROPERTY_NAME));
        if (coordString.startsWith("RANDOM")) {
            int maxCoord = config.getNonNegativeIntAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Coords/RANDOM/base"));
            int x = Math.abs(rand.nextInt()) % maxCoord;
            int y = Math.abs(rand.nextInt()) % maxCoord;
            return new DT_LogicalAddress(x, y);
        } else if (coordString.equals("USE_LM")) {
            int[] coords_int = new int[2];
            gnpAgent = new GNP_DT(config, this, PROPERTY_PROTO_PREFIX);
            coords_int = gnpAgent.getLogicalAddress_nonblock();
            System.out.println("coords_int[0]=" + coords_int[0] + ", coords_int[1]= " + coords_int[1]);
            return new DT_LogicalAddress((int) coords_int[0], (int) coords_int[1]);
        } else if (coordString.equals("USE_GEO")) {
            float x_l = 0;
            float y_l = 0;
            float base_meridian = 0;
            String MString = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Coords/USE_GEO/BaseMeridian"));
            if (MString != null) {
                base_meridian = (Float.valueOf(MString)).floatValue();
                System.out.println("Base meridian line is specified as:" + base_meridian);
            }
            String GeoString = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Coords/USE_GEO/GeoLocation"));
            if (GeoString == null) {
                throw new HyperCastFatalRuntimeException("Cannot read location (longitude and latitude) information!");
            } else {
                StringTokenizer geotoken = new StringTokenizer(GeoString, ",\t\n\r\f");
                try {
                    x_l = (Float.valueOf(geotoken.nextToken())).floatValue();
                    y_l = ((Float.valueOf(geotoken.nextToken())).floatValue() + 90) * 10;
                    if ((x_l > base_meridian) || (x_l == base_meridian)) {
                        x_l = x_l - base_meridian;
                    } else {
                        x_l = (float) 360.0 + x_l - base_meridian;
                    }
                    x_l = x_l * 10;
                } catch (NumberFormatException e) {
                    throw new HyperCastFatalRuntimeException(e);
                }
            }
            return new DT_LogicalAddress((int) (x_l + 0.5), (int) (y_l + 0.5));
        } else if (coordString.equals("FIXED")) {
            String coords = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "Coords/FIXED/coordinate"));
            try {
                return new DT_LogicalAddress(coords);
            } catch (Exception e) {
                throw new IllegalArgumentException(PROPERTY_PROTO_PREFIX + NODE_COORDS_PROPERTY_NAME + " property caused an exception -- probably poorly formated.");
            }
        } else {
            throw new HyperCastFatalRuntimeException("Unknown method of creating logical address is specified.");
        }
    }

    /** Joins the overlay multicast group. */
    public void joinOverlay() {
        if (joined) throw new IllegalStateException("DT_Node.joinGroup() called twice without leaveOverlay()!");
        neighborhood.removeAllNeighbors();
        synchronized (startStopLock) {
            adapter.Start();
        }
        synchronized (this) {
            HeartbeatSetToFastHeartbeat = true;
            StableAtHeartbeat = false;
            StartConsiderStable = false;
            adapter.setTimer(new Integer(HEARTBEAT_TIMER_INDEX), (long) (rand.nextDouble() * FAST_HEARTBEAT_TIME));
            timerCleared = false;
            notifyAll();
        }
        timeOfLastHeartbeat = -1;
        maxReceiveRateInAHeartbeat = 0;
        maxSendRateInAHeartbeat = 0;
        startTime = adapter.getCurrentTime();
        lastChangeToNeighborhood = startTime;
        stopTime = -1;
        joined = true;
    }

    /** Leaves the overlay multicast group. */
    public void leaveOverlay() {
        if (!joined) throw new IllegalStateException("DT_Node.leaveOverlay() called without calling joinGroup!");
        synchronized (this) {
            while (adapter.getTimer(new Integer(HEARTBEAT_TIMER_INDEX)) < 0) {
                try {
                    wait();
                } catch (InterruptedException ie) {
                }
            }
            adapter.clearTimer(new Integer(HEARTBEAT_TIMER_INDEX));
            timerCleared = true;
            notifyAll();
            goodbyeAllNeighbors();
        }
        stopTime = adapter.getCurrentTime();
        adapter.Stop(TIMEOUT_TIME);
        joined = false;
        if (n_handler != null) {
            NODE_LEAVEOVERLAY ne = new NODE_LEAVEOVERLAY(adapter.getCurrentTime(), null);
            n_handler.eventOccurred(ne);
        }
    }

    /** Creates a protocol message from a byte[].  
 * Required in order to implement I_AdapterCallback.  
 * @see I_AdapterCallback#restoreMessage
 */
    public I_Message restoreMessage(byte[] buffer, int[] startValidBytes, int endValidBytes) {
        return DT_Message.restoreMessage(buffer, startValidBytes, endValidBytes, adapter, config.getOverlayHash());
    }

    /** Returns true if none of the node's neighbors has a greater logical address. */
    public synchronized boolean isLeader() {
        return neighborhood.isLeader();
    }

    /** Returns the physical address/logical address pair of this node. 
*/
    public synchronized I_AddressPair getAddressPair() {
        return neighborhood.getMyAddressPair();
    }

    /** Returns AddressPair of neighbor that is parent of this node in a spanning tree
 * rooted at <code>rootCoords</code>.
 * This is calculated by finding the neighbor that forms the smallest angle with <code>rootCoords</code>.
 */
    public synchronized I_AddressPair[] getParent(I_LogicalAddress root) {
        if (!joined) throw new IllegalStateException("Must joinGroup before calling getParent!");
        I_AddressPair Pa = neighborhood.getParent((DT_LogicalAddress) root);
        if (Pa == null) {
            return new I_AddressPair[0];
        } else {
            I_AddressPair[] myParent = new I_AddressPair[1];
            myParent[0] = Pa;
            return myParent;
        }
    }

    /** Return the AddressPairs of neighbor that are children of this node in a spanning tree
 * rooted at <code>rootCoords</code>.
 * This based on if DT_Node.getParent() of the neighbor would return this node.
 */
    public synchronized I_AddressPair[] getChildren(I_LogicalAddress root) {
        if (!joined) throw new IllegalStateException("Must joinGroup before calling getChildren!");
        return neighborhood.getChildren((DT_LogicalAddress) root);
    }

    /** Returns the AddressPairs of all the neighbors 
 */
    public synchronized I_AddressPair[] getAllNeighbors() {
        if (!joined) throw new IllegalStateException("Must joinGroup before calling getAllNeighbors!");
        return neighborhood.getAllNeighbors();
    }

    /** Returns the AddressPair of the neighbor with the greatest coordinates.
 * @return null if this node's LogicalAddress is greater than all its neighbors.
 */
    public synchronized I_AddressPair getNextHopToLeader() {
        if (!joined) throw new IllegalStateException("Must joinGroup before calling getNextHopToLeader!");
        return neighborhood.getNextHopToLeader();
    }

    /** Constructs a DT_LogicalAddress from a byte array. */
    public I_LogicalAddress createLogicalAddress(byte[] array, int offset) {
        if ((array.length - offset) < getAddressPair().getLogicalAddress().getSize()) throw new IllegalArgumentException("The size of byte array is less than the size of DT_LogicalAddress!");
        return new DT_LogicalAddress(array, offset);
    }

    /** Creates a logical address object from a String.
 */
    public I_LogicalAddress createLogicalAddress(String laStr) {
        int x;
        int y;
        StringTokenizer token = new StringTokenizer(laStr, ",\n");
        String[] laString = new String[2];
        for (int i = 0; i < 2; i++) {
            if (token.hasMoreTokens()) laString[i] = token.nextToken(); else throw new IllegalArgumentException("DT_Node: The format of Logical address is incorrect.");
        }
        try {
            x = Integer.parseInt(laString[0]);
            y = Integer.parseInt(laString[1]);
        } catch (NumberFormatException nfe) {
            config.err.println("DT_Node: Could not parse \"" + laString[0] + "," + laString[1] + "\" to create logical address");
            return null;
        }
        return new DT_LogicalAddress(x, y);
    }

    /** Sets the logical address of this node to the specified one.
 */
    public void setLogicalAddress(I_LogicalAddress la) {
        if (!(la instanceof DT_LogicalAddress)) {
            throw new IllegalArgumentException("DT_Node_Buddylist: setLogicalAddress: the logical address type of the parameter is incorrect.");
        }
        this.updateLogicalCoordinate((DT_LogicalAddress) la);
    }

    /** Verify the previous hop of the message. If checkmode is set to "neighborCheck",
 * this method checks if the previous hop is a neighbor; otherwise this method checks
 * if the previous hop is a valid sender of OL messages.
 */
    public boolean previousHopCheck(I_LogicalAddress src, I_LogicalAddress dst, I_LogicalAddress prehop) {
        if (checkmode.equals("neighborCheck")) {
            if (debug) System.out.println("DT_Node: prevhopCheck: doing neighborCheck.");
            return neighborhood.contains(prehop);
        } else {
            if (debug) System.out.println("DT_Node: prevhopCheck: doing prevhopCheck.");
            if (dst == null) {
                I_AddressPair[] Parent = getParent(src);
                if (Parent.length != 1) return false;
                I_LogicalAddress Parent_LA = Parent[0].getLogicalAddress();
                return ((DT_LogicalAddress) Parent_LA).equals(prehop);
            } else {
                return neighborhood.amIParent((DT_LogicalAddress) prehop, (DT_LogicalAddress) dst);
            }
        }
    }

    /**
 * Set notification handler.
 */
    public void setNotificationHandler(NotificationHandler nh) {
        n_handler = nh;
        neighborhood.setNotificationHandler(nh);
    }

    /** This function, a member of I_AdapterCallback, is called for
 * the heartbeat timer. When the timer occurs, the node sends 
 * Hello messages to all of its neighbors.  At the same time, the
 * node checks if any of its neighbors has timed out.
 */
    public synchronized void timerExpired(Object Timer_Index) {
        TimerCallbackDebug = "JustCalled";
        int timerIndex = ((Integer) Timer_Index).intValue();
        if (timerIndex == HEARTBEAT_TIMER_INDEX) {
            long timeOfThisHeartbeat = adapter.getCurrentTime();
            TimerCallbackDebug = "ResettingHeartbeat";
            boolean HeartbeatSetToFastHeartbeat_old = HeartbeatSetToFastHeartbeat;
            boolean StableAtHeartbeat_old = StableAtHeartbeat;
            if (StartConsiderStable) StableAtHeartbeat = neighborhood.isStable();
            if (StableAtHeartbeat && (StableAtHeartbeat != StableAtHeartbeat_old)) {
                if (n_handler != null) {
                    NODE_ISSTABLE ne = new NODE_ISSTABLE(adapter.getCurrentTime(), null);
                    n_handler.eventOccurred(ne);
                }
            }
            HeartbeatSetToFastHeartbeat = !StableAtHeartbeat;
            if (HeartbeatSetToFastHeartbeat) adapter.setTimer(new Integer(HEARTBEAT_TIMER_INDEX), FAST_HEARTBEAT_TIME); else {
                adapter.setTimer(new Integer(HEARTBEAT_TIMER_INDEX), SLOW_HEARTBEAT_TIME);
            }
            TimerCallbackDebug = "Notifying others";
            notifyAll();
            double heartbeatDuration;
            if (timeOfLastHeartbeat > 0) heartbeatDuration = (timeOfThisHeartbeat - timeOfLastHeartbeat) / 1000.0; else heartbeatDuration = FAST_HEARTBEAT_TIME / 1000.0;
            if (!HeartbeatSetToFastHeartbeat_old == StableAtHeartbeat_old) {
                double expectedHeartbeatDuration = SLOW_HEARTBEAT_TIME / 1000.0;
                if (HeartbeatSetToFastHeartbeat_old) expectedHeartbeatDuration = FAST_HEARTBEAT_TIME / 1000.0;
            }
            double receiveRate = messagesReceivedInLastHeartbeat / heartbeatDuration;
            double sendRate = messagesSentInLastHeartbeat / heartbeatDuration;
            maxReceiveRateInAHeartbeat = Math.max(maxReceiveRateInAHeartbeat, receiveRate);
            maxSendRateInAHeartbeat = Math.max(maxSendRateInAHeartbeat, sendRate);
            timeOfLastHeartbeat = timeOfThisHeartbeat;
            messagesSentInLastHeartbeat = 0;
            messagesReceivedInLastHeartbeat = 0;
            TimerCallbackDebug = "TimingoutNeighbors";
            handleTimeoutTimer(timeOfThisHeartbeat);
            TimerCallbackDebug = "SendingSpecHello";
            if (!neighborhood.isStable()) sendHellosToCandidateNeighbors();
            TimerCallbackDebug = "SendingHellosToNeighbors";
            helloAllNeighbors();
        } else {
            throw new IllegalArgumentException("Timer index " + timerIndex + " unexpected in DT_Node");
        }
        TimerCallbackDebug = "Callback returned";
    }

    /** Remove nodes that have timed out. */
    private void handleTimeoutTimer(long currentTime) {
        int count = 0;
        int i = 0;
        while (i < neighborhood.getNumOfNeighbors()) {
            if (neighborhood.getLastContactTimeOfNeighborAtIndex(i) + TIMEOUT_TIME < currentTime) {
                neighborhood.removeNeighbor(i);
                lastChangeToNeighborhood = adapter.getCurrentTime();
                count++;
            } else {
                i++;
            }
        }
        if ((count > 0) && (n_handler != null)) {
            NODE_NEIGHBORHOODCHANGED ne = new NODE_NEIGHBORHOODCHANGED(adapter.getCurrentTime(), null);
            n_handler.eventOccurred(ne);
        }
    }

    /** Sends a Hello message to one of the node's candidate neighbors. */
    private void sendHellosToCandidateNeighbors() {
        Vector dests = new Vector(2 * neighborhood.getNumOfNeighbors());
        for (int i = 0; i < neighborhood.getNumOfNeighbors(); i++) {
            DT_AddressPair cw = neighborhood.getNodeCWOfNeighborAtIndex(i);
            if (cw != null && isCandidateNeighbor(cw)) dests.addElement(cw);
            DT_AddressPair ccw = neighborhood.getNodeCCWOfNeighborAtIndex(i);
            if (ccw != null && isCandidateNeighbor(ccw)) dests.addElement(ccw);
        }
        if (dests.size() == 0) return;
        DT_AddressPair closest = (DT_AddressPair) dests.elementAt(0);
        for (int i = 1; i < dests.size(); i++) {
            if (DT_Point.distance(neighborhood.getMyAddressPair().getDTLogicalAddress(), ((DT_AddressPair) dests.elementAt(i)).getDTLogicalAddress()) < DT_Point.distance(neighborhood.getMyAddressPair().getDTLogicalAddress(), closest.getDTLogicalAddress())) closest = ((DT_AddressPair) dests.elementAt(i));
        }
        dests.removeAllElements();
        dests.addElement(closest);
        for (int i = 0; i < dests.size(); i++) sendHelloNeighborMessage((DT_AddressPair) dests.elementAt(i));
    }

    /** Check if the given address pair matches a candidate neighbor. */
    private boolean isCandidateNeighbor(DT_AddressPair dest) {
        if (neighborhood.contains(dest.getPhysicalAddress())) return false; else return neighborhood.shouldBeNeighbor(dest.getDTLogicalAddress());
    }

    /** Sends a Hello message to all of the node's neighbors. */
    protected final void helloAllNeighbors() {
        for (int i = 0; i < neighborhood.getNumOfNeighbors(); i++) sendHelloNeighborMessage(neighborhood.getNeighborAtIndex(i));
    }

    /** This function, a member of I_AdapterCallback, is called whenever a
 * protocol message arrives.  The function determines the proper
 * reaction to each message type.  
 */
    public synchronized void messageArrivedFromAdapter(I_Message a) {
        if (!(a instanceof DT_Message)) throw new IllegalArgumentException("DT_Node.messageArrivedFromAdapter() passed non-DT_Message I_Message.");
        DT_Message m = (DT_Message) a;
        if (m.getProtoNum() != DT_Message.PROTONUM_DT30) {
            config.log.println("Received message with wrong Protocol Number: " + m.getProtoNum() + " != " + DT_Message.PROTONUM_DT30);
            return;
        }
        if (m.getOverlayHash() != config.getOverlayHash()) {
            return;
        }
        MessageCallbackDebug = "JustCalled";
        messagesReceivedInLastHeartbeat++;
        if (!joined) {
            if (m.getType() != DT_Message.Goodbye) {
                sendGoodbyeMessage(m.getSrc());
            }
        } else {
            switch(m.getType()) {
                case DT_Message.Goodbye:
                    handleGoodbye(m);
                    break;
                case DT_Message.HelloNeighbor:
                    handleHello(m);
                    break;
                case DT_Message.HelloNotNeighbor:
                    handleHello(m);
                    break;
                case DT_Message.NewNode:
                    handleNewNode(m);
                    break;
                case DT_Message.NodePing:
                    handleNodePing(m);
                    break;
                default:
                    config.err.println("DT_Node: message with unknown type arrived!");
            }
        }
        MessageCallbackDebug = "Returned";
    }

    /** Removes the sender of the message from the neighborhood. */
    private void handleGoodbye(DT_Message m) {
        if (neighborhood.contains(m.getSrcPA())) {
            deleteNeighbor(m.getSrc());
            if (n_handler != null) {
                NODE_NEIGHBORHOODCHANGED ne = new NODE_NEIGHBORHOODCHANGED(adapter.getCurrentTime(), null);
                n_handler.eventOccurred(ne);
            }
        }
    }

    /** Preforms actions associated with the arrival of a Hello or HelloNotNeighbor message.   
 */
    private void handleHello(DT_Message m) {
        MessageCallbackDebug = "handleHello";
        DT_AddressPair sendersEntry = neighborhood.getNeighborWithThisPA(m.getSrcPA());
        if (sendersEntry != null && !(m.getSrcLA().equals(sendersEntry.getDTLogicalAddress()))) {
            deleteNeighbor(sendersEntry);
        }
        if (neighborhood.contains(m.getSrcPA())) {
            MessageCallbackDebug = "handleHello:FromNeighbor";
            boolean changed = neighborhood.updateContactTable(m.getSrcPA(), m.getCW(), m.getCCW(), m.getType() == DT_Message.HelloNeighbor);
            if (changed && !neighborhood.isStable()) changeSlowHeartbeatToFastHeartbeat();
        } else if (neighborhood.neighborHasThisLA(m.getSrcLA())) {
            MessageCallbackDebug = "handleHello:HasLASameAsNeighbor";
            System.out.println("send NotHelloNeighbor message.");
            sendHelloNotNeighborMessage(m.getSrc());
        } else {
            MessageCallbackDebug = "handleHello: standard case";
            if (neighborhood.shouldBeNeighbor(m.getSrcLA())) {
                MessageCallbackDebug = "handleHello: standard case: attempting to add neighbor";
                neighborhood.addNeighbor(m.getSrc(), m.getCW(), m.getCCW(), m.getType() == DT_Message.HelloNeighbor);
                lastChangeToNeighborhood = adapter.getCurrentTime();
                MessageCallbackDebug = "handleHello: standard case: possibly sending HelloNotNeighbor message";
                if (!neighborhood.contains(m.getSrcPA()) && m.getType() == DT_Message.HelloNeighbor) {
                    sendHelloNotNeighborMessage(m.getSrc());
                }
                MessageCallbackDebug = "handleHello: standard case: possibly changing heartbeat";
                if (neighborhood.contains(m.getSrcPA())) {
                    changeSlowHeartbeatToFastHeartbeat();
                }
                MessageCallbackDebug = "handleHello: standard case: done first part";
                if (n_handler != null) {
                    NODE_NEIGHBORHOODCHANGED ne = new NODE_NEIGHBORHOODCHANGED(adapter.getCurrentTime(), null);
                    n_handler.eventOccurred(ne);
                }
            } else {
                MessageCallbackDebug = "handleHello: Did not add as neighbor";
                if (m.getType() == DT_Message.HelloNeighbor) {
                    MessageCallbackDebug = "handleHello: standard case:sending HelloNotNeighbor";
                    sendHelloNotNeighborMessage(m.getSrc());
                }
            }
            MessageCallbackDebug = "handleHello: completed stanard case";
        }
        MessageCallbackDebug = "handleHello returned";
    }

    /** Used by NewNode method of joins. */
    protected void handleNewNode(DT_Message m) {
        if ((neighborhood.getPhysicalAddress()).equals(m.getLeaderPA()) || neighborhood.contains(m.getLeaderPA())) {
            return;
        }
        if (isCandidateNeighbor(m.getLeader())) {
            sendHelloNeighborMessage(m.getLeader());
        } else {
            DT_AddressPair parent = neighborhood.getParent(m.getLeaderLA());
            if (parent == null) {
            } else {
                sendNewNodeMessage(parent, m.getLeader());
            }
        }
    }

    /** Handle the Node Ping message from DT server */
    protected void handleNodePing(DT_Message m) {
        messagesSentInLastHeartbeat++;
        I_Message NewPacket;
        NewPacket = createDTMessage(DT_Message.NodePong, m.getSrc(), null, null);
        adapter.sendUnicastMessage(m.getSrcPA(), NewPacket);
    }

    /** Change the frenquency of contacting neighbors. */
    protected final synchronized void changeSlowHeartbeatToFastHeartbeat() {
        long delay = 0;
        if (!HeartbeatSetToFastHeartbeat && !timerCleared) {
            long timeToWait = Math.max(0, FAST_HEARTBEAT_TIME - (SLOW_HEARTBEAT_TIME - delay));
            HeartbeatSetToFastHeartbeat = true;
            adapter.setTimer(new Integer(HEARTBEAT_TIMER_INDEX), timeToWait);
        }
    }

    /** Sends a Goodbye message to all of the node's neighbors. */
    protected final void goodbyeAllNeighbors() {
        for (int i = 0; i < neighborhood.getNumOfNeighbors(); i++) sendGoodbyeMessage(neighborhood.getNeighborAtIndex(i));
    }

    /** Sends a Hello message to a node. */
    protected final void sendHelloNeighborMessage(DT_AddressPair dst) {
        messagesSentInLastHeartbeat++;
        DT_AddressPair cw = neighborhood.getCWNeighbor(dst.getDTLogicalAddress());
        DT_AddressPair ccw = neighborhood.getCCWNeighbor(dst.getDTLogicalAddress());
        I_Message NewPacket;
        NewPacket = createDTMessage(DT_Message.HelloNeighbor, dst, cw, ccw);
        adapter.sendUnicastMessage(dst.getPhysicalAddress(), NewPacket);
    }

    /** Send a HelloNotNeighbor message to a node */
    protected final void sendHelloNotNeighborMessage(DT_AddressPair dst) {
        messagesSentInLastHeartbeat++;
        DT_AddressPair cw = neighborhood.getCWNeighbor(dst.getDTLogicalAddress());
        DT_AddressPair ccw = neighborhood.getCCWNeighbor(dst.getDTLogicalAddress());
        DT_AddressPair sa = neighborhood.getNeighborAtSameAngle(dst.getDTLogicalAddress());
        if (sa != null) {
            cw = sa;
            ccw = sa;
        }
        I_Message NewPacket;
        NewPacket = createDTMessage(DT_Message.HelloNotNeighbor, dst, cw, ccw);
        adapter.sendUnicastMessage(dst.getPhysicalAddress(), NewPacket);
    }

    /** Sends a Goodbye message to a node. */
    protected final void sendGoodbyeMessage(DT_AddressPair dst) {
        messagesSentInLastHeartbeat++;
        I_Message NewPacket;
        NewPacket = createDTMessage(DT_Message.Goodbye, dst, null, null);
        adapter.sendUnicastMessage(dst.getPhysicalAddress(), NewPacket);
    }

    protected void sendNewNodeMessage(DT_AddressPair dst, DT_AddressPair target) {
        messagesSentInLastHeartbeat++;
        I_Message NewPacket;
        NewPacket = createDTMessage(DT_Message.NewNode, dst, target, null);
        adapter.sendUnicastMessage(dst.getPhysicalAddress(), NewPacket);
    }

    protected void sendTestMessage(DT_AddressPair dst, DT_AddressPair target) {
        messagesSentInLastHeartbeat++;
        I_Message NewPacket;
        NewPacket = createDTMessage(DT_Message.TestMessage, dst, target, null);
        adapter.sendUnicastMessage(dst.getPhysicalAddress(), NewPacket);
    }

    /** Deletes a neighbor from the neighborhood table and
 * updates timers. */
    public void deleteNeighbor(DT_AddressPair dtap) {
        neighborhood.removeNeighbor(dtap);
        lastChangeToNeighborhood = adapter.getCurrentTime();
        changeSlowHeartbeatToFastHeartbeat();
    }

    /** This method creates a new DT_Message based on parameters. 
 * It is called by all sending methods and can be overrided in secure node class.
 */
    public I_Message createDTMessage(byte type, DT_AddressPair dst, DT_AddressPair cw, DT_AddressPair ccw) {
        return new DT_Message(Proto_Sub, type, config.getOverlayHash(), (DT_AddressPair) neighborhood.getMyAddressPair(), dst, cw, ccw);
    }

    /**
 * Initialize the data structure for handling the statistics defined in this object.
 * In this method, a StatsProcessor instance is created which contains all statistics
 * , with each represented by a StatsElement instance, supported in this object.
 */
    private void InitStatisticsStructure() {
        statisticsName = config.getTextAttribute(XmlUtil.createXPath(PROPERTY_PROTO_PREFIX + "StatName"));
        statsPro = new StatsProcessor(this, true, true);
        statsPro.addStatsElement("NodeAdapter", adapter, 1, 1);
        statsPro.addStatsElement("SlowHeartbeatTime", new SlowHeartbeatTimePeriod(), 1, 1);
        statsPro.addStatsElement("FastHeartbeatTime", new FastHeartbeatTimePeriod(), 1, 1);
        statsPro.addStatsElement("TimeoutTime", new TimeoutTimeLimit(), 1, 1);
        statsPro.addStatsElement("LogicalAddress", new LogicalAddressOperator(), 1, 1);
        statsPro.addStatsElement("MaxReceiveBurstRate", new MaxReceiveBurstRateLimit(), 1, 1);
        statsPro.addStatsElement("MaxSendBurstRate", new MaxSendBurstRateLimit(), 1, 1);
        statsPro.addStatsElement("StartTime", new NodeStartTime(), 1, 1);
        statsPro.addStatsElement("LastChangeToNeighborhood", new LastChangeToNeighborhoodQuery(), 1, 1);
        statsPro.addStatsElement("StopTime", new NodeStopTime(), 1, 1);
        statsPro.addStatsElement("PhysicalAddress", new PhysicalAddressOperator(), 1, 1);
        statsPro.addStatsElement("NumOfNeighbors", new NumOfCurrentNeighbors(), 1, 1);
        statsPro.addStatsElement("IsConsistent", new IsConsistentTest(), 1, 1);
    }

    /** @see I_Stats#getStatsName() */
    public String getStatsName() {
        return statisticsName;
    }

    /** @see I_Stats#setStatsName(String) */
    public void setStatsName(String name) {
        statisticsName = name;
    }

    /** 
 * Return the result of query for the statistics specified by the given xpath.
 * 
 * @param doc   Document used for create new elements or nodes.
 * @param xpath XPath instance represents the statistics to be queried.
 * @see I_Stats#getStats
 */
    public Element[] getStats(Document doc, XPath xpath) throws HyperCastStatsException {
        return statsPro.getStatsResult(doc, xpath);
    }

    /** 
 * Set the statistics specified by the given xpath. The value actually set is
 * returned.
 * 
 * @param doc   	Document used for create new elements or nodes.
 * @param xpath 	XPath instance represents the statistics to be queried.
 * @param newValue	Element representing the value or sub-tree to be set.
 * @see I_Stats#setStats
 */
    public Element[] setStats(Document doc, XPath xpath, Element newValue) throws HyperCastStatsException {
        return statsPro.setStatsResult(doc, xpath, newValue);
    }

    /** 
 * Return the schema element which represents the root of the sub-tree, specified
 * by the given xpath, in read schema tree. 
 * 
 * @param doc   Document used for create new elements or nodes.
 * @param xpath XPath instance representing the statistics which is the root
 * 	of the sub-tree to be queried.
 * @see I_Stats#getReadSchema(Document, XPath)
 */
    public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
        return statsPro.getReadSchemaResult(doc, xpath);
    }

    /** 
 * Return the schema element which represents the root of the sub-tree, specified
 * by the given xpath, in write schema tree. 
 * 
 * @param doc   Document used for create new elements or nodes.
 * @param xpath XPath instance representing the statistics which is the root
 * 	of the sub-tree to be queried.
 * @see I_Stats#getWriteSchema(Document, XPath)
 */
    public Element[] getWriteSchema(Document doc, XPath xpath) throws HyperCastStatsException {
        return statsPro.getWriteSchemaResult(doc, xpath);
    }

    synchronized void updateLogicalCoordinate(DT_LogicalAddress addr) {
        neighborhood.updateNodeAddress(addr);
    }

    /**
 * This class manages simple statistic "SlowHeartbeatTime".
 */
    class SlowHeartbeatTimePeriod extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return "" + SLOW_HEARTBEAT_TIME;
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            SLOW_HEARTBEAT_TIME = Integer.parseInt(newValue);
            return newValue;
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Long", null, null);
        }

        /** 
	 * @see I_Stats#getWriteSchema(Document, XPath)
	 */
        public Element[] getWriteSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Long", null, null);
        }
    }

    /**
 * This class manages simple statistic "FastHeartbeatTime".
 */
    class FastHeartbeatTimePeriod extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return "" + FAST_HEARTBEAT_TIME;
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            FAST_HEARTBEAT_TIME = Integer.parseInt(newValue);
            return newValue;
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Long", null, null);
        }

        /** 
	 * @see I_Stats#getWriteSchema(Document, XPath)
	 */
        public Element[] getWriteSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Long", null, null);
        }
    }

    /**
 * This class manages simple statistic "TimeoutTime".
 */
    class TimeoutTimeLimit extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return "" + TIMEOUT_TIME;
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            TIMEOUT_TIME = Integer.parseInt(newValue);
            return newValue;
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Long", null, null);
        }

        /** 
	 * @see I_Stats#getWriteSchema(Document, XPath)
	 */
        public Element[] getWriteSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Long", null, null);
        }
    }

    /**
 * This class manages simple statistic "LogicalAddress".
 */
    class LogicalAddressOperator extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return neighborhood.getLogicalAddress().toString();
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            updateLogicalCoordinate(new DT_LogicalAddress(newValue));
            return newValue;
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:simpleType", "xsd:String", "\\d+\\,\\d+");
        }

        /** 
	 * @see I_Stats#getWriteSchema(Document, XPath)
	 */
        public Element[] getWriteSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:simpleType", "xsd:String", "\\d+\\,\\d+");
        }
    }

    /**
 * This class manages simple statistic "PhysicalAddress".
 */
    class PhysicalAddressOperator extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return neighborhood.getPhysicalAddress().toString();
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:String", null, null);
        }
    }

    /**
 * This class manages simple statistic "NumOfNeighbors".
 */
    class NumOfCurrentNeighbors extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return Integer.toString(neighborhood.getTableSize());
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Integer", null, null);
        }
    }

    /**
 * This class manages simple statistic "IsConsistent".
 */
    class IsConsistentTest extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return Integer.toString(neighborhood.isConsistent());
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Integer", null, null);
        }
    }

    /**
 * This class manages simple statistic "MaxReceiveBurstRate".
 */
    class MaxReceiveBurstRateLimit extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return "" + maxReceiveRateInAHeartbeat;
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Double", null, null);
        }
    }

    /**
 * This class manages simple statistic "MaxSendBurstRate".
 */
    class MaxSendBurstRateLimit extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return "" + maxSendRateInAHeartbeat;
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /**
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Double", null, null);
        }
    }

    /**
 * This class manages simple statistic "StartTime".
 */
    class NodeStartTime extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return "" + startTime;
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Long", null, null);
        }
    }

    /**
 * This class manages simple statistic "StopTime".
 */
    class NodeStopTime extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return "" + stopTime;
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Long", null, null);
        }
    }

    /**
 * This class manages simple statistic "LastChangeToNeighborhood".
 */
    class LastChangeToNeighborhoodQuery extends SimpleStats {

        /**
	 * Gets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#getStats()
	 */
        protected String getStats() {
            return "" + lastChangeToNeighborhood;
        }

        /**
	 * Sets the value of a simple statistic managed by the implementation of this class.
	 * 
	 * @see SimpleStats#setStats(String newvalue)
	 */
        protected String setStats(String newValue) throws HyperCastStatsException {
            throw new HyperCastStatsException(statisticsName, HyperCastStatsException.WriteToAReadOnlyValue);
        }

        /** 
	 * @see I_Stats#getReadSchema(Document, XPath)
	 */
        public Element[] getReadSchema(Document doc, XPath xpath) throws HyperCastStatsException {
            return XmlUtil.createSchemaElement(doc, statisticsName, "xsd:Long", null, null);
        }
    }
}
