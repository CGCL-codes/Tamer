package gridsim.net;

import gridsim.*;
import gridsim.net.*;
import gridsim.util.*;
import eduni.simjava.*;
import java.util.*;

/**
 * GridSim Output defines a port through which a simulation entity sends
 * data to the simulated network.
 * <p>
 * It maintains an event queue to serialize
 * the data-out-flow and delivers to the destination entity.
 * It works along with Input entity to simulate network
 * communication delay. Simultaneous outputs can be modeled by using multiple
 * instances of this class
 *
 * @author       Manzur Murshed and Rajkumar Buyya
 * @since        GridSim Toolkit 1.0
 * @invariant $none
 */
public class Output extends Sim_entity implements NetIO {

    private Sim_port outPort_;

    private Link link_;

    private double baudRate_;

    private int pktID_;

    private Vector packetList_;

    private Random random_;

    private TrafficGenerator gen_;

    private ArrayList list_;

    private boolean hasStarted_;

    /**
     * Allocates a new Output object
     * @param name         the name of this object
     * @param baudRate     the communication speed
     * @throws NullPointerException This happens when creating this entity
     *                  before initializing GridSim package or this entity name
     *                  is <tt>null</tt> or empty
     * @pre name != null
     * @pre baudRate >= 0.0
     * @post $none
     */
    public Output(String name, double baudRate) throws NullPointerException {
        super(name);
        this.baudRate_ = baudRate;
        link_ = null;
        packetList_ = null;
        pktID_ = 0;
        outPort_ = new Sim_port("output_buffer");
        super.add_port(outPort_);
        gen_ = null;
        list_ = null;
        random_ = null;
        hasStarted_ = false;
    }

    /**
     * Sets the background traffic generator for this entity.
     * <p>
     * When simulation starts, this entity will automatically sends junk
     * packets to resource entities.
     * @param gen   a background traffic generator
     * @return <tt>true</tt> if successful, <tt>false</tt> otherwise
     * @pre gen != null
     * @post $none
     */
    public boolean setBackgroundTraffic(TrafficGenerator gen) {
        if (gen == null) {
            return false;
        }
        gen_ = gen;
        if (list_ == null) {
            list_ = new ArrayList();
        }
        return true;
    }

    /**
     * Sets the background traffic generator for this entity.
     * <p>
     * When simulation starts, this entity will automatically sends junk
     * packets to resource entities and other entities. <br>
     * NOTE: Sending background traffic to itself is not supported.
     *
     * @param gen       a background traffic generator
     * @param userName  a collection of user entity name (in String object).
     * @return <tt>true</tt> if successful, <tt>false</tt> otherwise
     * @pre gen != null
     * @pre userName != null
     * @post $none
     */
    public boolean setBackgroundTraffic(TrafficGenerator gen, Collection userName) {
        if (gen == null || userName == null) {
            return false;
        }
        boolean flag = true;
        try {
            gen_ = gen;
            if (list_ == null) {
                list_ = new ArrayList();
            }
            Iterator it = userName.iterator();
            int id = -1;
            while (it.hasNext()) {
                String name = (String) it.next();
                id = GridSim.getEntityId("Output_" + name);
                if (id == super.get_id()) {
                    System.out.println(super.get_name() + ".setBackgroundTraffic(): Warning - can not send " + "junk packets to itself.");
                    continue;
                }
                id = GridSim.getEntityId(name);
                if (id > 0) {
                    Integer obj = new Integer(id);
                    list_.add(obj);
                } else {
                    System.out.println(super.get_name() + ".setBackgroundTraffic(): Warning - invalid entity " + "name for \"" + name + "\".");
                }
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * Sets this entity's link. This should be used only if the network
     * extensions are being used.
     *
     * @param link the link to which this Output entity should send data
     * @pre link != null
     * @post $none
     */
    public void addLink(Link link) {
        this.link_ = link;
        baudRate_ = link_.getBaudRate();
        packetList_ = new Vector();
    }

    /**
     * Gets the baud rate
     * @return the baud rate
     * @pre $none
     * @post $result >= 0.0
     */
    public double getBaudRate() {
        return baudRate_;
    }

    /**
     * Gets the I/O real number based on a given value
     * @param value  the specified value
     * @return real number
     * @pre $none
     * @post $result >= 0.0
     */
    public double realIO(double value) {
        return GridSimRandom.realIO(value);
    }

    /**
     * A method that gets one process event at one time until the end
     * of a simulation, then delivers an event to the entity (its parent)
     * @pre $none
     * @post $none
     */
    public void body() {
        int gisID = GridSim.getGridInfoServiceEntityId();
        int statID = GridSim.getGridStatisticsEntityId();
        int shutdownID = GridSim.getGridSimShutdownEntityId();
        startBackgroundTraffic();
        while (Sim_system.running()) {
            Sim_event ev = new Sim_event();
            super.sim_get_next(ev);
            if (ev.get_tag() == GridSimTags.END_OF_SIMULATION) {
                break;
            }
            switch(ev.get_tag()) {
                case GridSimTags.SEND_PACKET:
                    sendPacket();
                    break;
                case GridSimTags.INFOPKT_SUBMIT:
                    sendInfoPacket(ev);
                    break;
                case GridSimTags.INFOPKT_RETURN:
                    returnInfoPacket(ev);
                    break;
                case GridSimTags.JUNK_PKT:
                    generateBackgroundTraffic();
                    break;
                default:
                    defaultSend(ev, gisID, statID, shutdownID);
                    break;
            }
        }
    }

    /**
     * Generates few junk packets at the given interval
     * @pre $none
     * @post $none
     */
    private synchronized void generateBackgroundTraffic() {
        long time = gen_.getNextPacketTime();
        int pattern = gen_.getPattern();
        if (hasStarted_ == false) {
            LinkedList resList = GridSim.getGridResourceList();
            if (resList == null && list_.size() == 0) {
                super.sim_schedule(super.get_id(), time, GridSimTags.JUNK_PKT);
                return;
            }
            hasStarted_ = true;
            list_.addAll(resList);
            if (pattern == TrafficGenerator.SEND_ONE_ONLY && random_ == null) {
                random_ = new Random();
            }
        }
        long size = gen_.getNextPacketSize();
        long freq = gen_.getNextPacketFreq();
        int type = gen_.getServiceType();
        int tag = GridSimTags.JUNK_PKT;
        int MTU = link_.getMTU();
        int numPackets = (int) Math.ceil(size / (MTU * 1.0));
        int i = 0;
        int destId = -1;
        if (pattern == TrafficGenerator.SEND_ONE_ONLY) {
            int index = random_.nextInt(list_.size());
            destId = ((Integer) list_.get(index)).intValue();
            for (i = 0; i < freq; i++) {
                convertIntoPacket(MTU, numPackets + 1, tag, destId, type);
            }
        } else if (pattern == TrafficGenerator.SEND_ALL) {
            for (int k = 0; k < list_.size(); k++) {
                destId = ((Integer) list_.get(k)).intValue();
                for (i = 0; i < freq; i++) {
                    convertIntoPacket(MTU, numPackets + 1, tag, destId, type);
                }
            }
        }
        super.sim_schedule(super.get_id(), time, GridSimTags.JUNK_PKT);
    }

    /**
     * Initial start for the background traffic
     * @pre $none
     * @post $none
     */
    private synchronized void startBackgroundTraffic() {
        if (gen_ == null) {
            return;
        }
        long time = gen_.getNextPacketTime();
        System.out.println(super.get_name() + ": background traffic will start at time " + time);
        if (time == -1) {
            return;
        }
        super.sim_schedule(super.get_id(), time, GridSimTags.JUNK_PKT);
    }

    /**
     * This method processes outgoing data without a network extension.
     * @param ev        a Sim_event object
     * @param gisID     the central/default GIS entity ID
     * @param statID    the GridStatistic entity ID
     * @param shutdownID    the GridSimShutdown entity ID
     * @pre ev != null
     * @post $none
     */
    private synchronized void defaultSend(Sim_event ev, int gisID, int statID, int shutdownID) {
        IO_data io = (IO_data) ev.get_data();
        int destId = io.getDestID();
        if (link_ != null && destId != gisID && destId != statID && destId != shutdownID) {
            submitToLink(ev);
            return;
        }
        int id = GridSim.getEntityId("Input_" + Sim_system.get_entity(destId).get_name());
        super.sim_schedule(id, GridSimTags.SCHEDULE_NOW, ev.get_tag(), io);
        double receiverBaudRate = ((Input) Sim_system.get_entity(id)).getBaudRate();
        double minBaudRate = Math.min(baudRate_, receiverBaudRate);
        double communicationDelay = GridSimRandom.realIO((io.getByteSize() * NetIO.BITS) / minBaudRate);
        super.sim_process(communicationDelay);
    }

    /**
     * This method takes data from an entity. If the size of the data is larger
     * than the MTU of the link, then the packet is split into mutiple size
     * units. After this it calls enque() to queue these packets into its
     * buffer.
     *
     * @param ev    A Sim_event data that contains all the data for this method
     *              to do its task.
     * @pre ev != null
     * @post $none
     */
    private synchronized void submitToLink(Sim_event ev) {
        IO_data data = (IO_data) ev.get_data();
        Object obj = data.getData();
        long size = data.getByteSize();
        int tag = ev.get_tag();
        int destId = data.getDestID();
        int netServiceType = data.getNetServiceLevel();
        int MTU = link_.getMTU();
        int numPackets = (int) Math.ceil(size / (MTU * 1.0));
        convertIntoPacket(MTU, numPackets, tag, destId, netServiceType);
        NetPacket np = null;
        np = new NetPacket(obj, pktID_, size - MTU * (numPackets - 1), tag, super.get_id(), destId, netServiceType, numPackets, numPackets);
        pktID_++;
        enque(np, GridSimTags.SCHEDULE_NOW);
    }

    /**
     * Creates many dummy or null packets
     * @param size          packet size (in bytes)
     * @param numPackets    total number of packets to be created
     * @param tag           packet tag
     * @param destId        destination ID for sending the packet
     * @param netServiceType    level type of service for the packet
     * @pre $none
     * @post $none
     */
    private synchronized void convertIntoPacket(long size, int numPackets, int tag, int destId, int netServiceType) {
        NetPacket np = null;
        for (int i = 0; i < numPackets - 1; i++) {
            if (tag != GridSimTags.JUNK_PKT) {
                tag = GridSimTags.EMPTY_PKT;
            }
            np = new NetPacket(null, pktID_, size, tag, super.get_id(), destId, netServiceType, i + 1, numPackets);
            pktID_++;
            enque(np, GridSimTags.SCHEDULE_NOW);
        }
    }

    /**
     * Sends an InfoPacket for ping request
     * @param ev  a Sim_Event object
     * @pre ev != null
     * @post $none
     */
    private synchronized void sendInfoPacket(Sim_event ev) {
        IO_data data = (IO_data) ev.get_data();
        long size = data.getByteSize();
        int destId = data.getDestID();
        int netServiceType = data.getNetServiceLevel();
        int tag = ev.get_tag();
        String name = GridSim.getEntityName(outPort_.get_dest());
        int MTU = link_.getMTU();
        int numPackets = (int) Math.ceil(size / (MTU * 1.0));
        if (size > MTU && outPort_.get_dest() != destId) {
            convertIntoPacket(MTU, numPackets, tag, destId, netServiceType);
        }
        size = data.getByteSize() - MTU * (numPackets - 1);
        InfoPacket pkt = new InfoPacket(name, pktID_, size, outPort_.get_dest(), destId, netServiceType);
        pkt.setLast(super.get_id());
        pkt.addHop(outPort_.get_dest());
        pkt.addEntryTime(GridSim.clock());
        pkt.setOriginalPingSize(data.getByteSize());
        pktID_++;
        enque(pkt, GridSimTags.SCHEDULE_NOW);
    }

    /**
     * Sends back the ping() request to the next hop or destination
     * @param ev  a Sim_event object
     * @pre ev != null
     * @post $none
     */
    private void returnInfoPacket(Sim_event ev) {
        IO_data data = (IO_data) ev.get_data();
        InfoPacket pkt = (InfoPacket) data.getData();
        long size = pkt.getOriginalPingSize();
        int tag = ev.get_tag();
        int destId = pkt.getSrcID();
        int netServiceType = data.getNetServiceLevel();
        int MTU = link_.getMTU();
        int numPackets = (int) Math.ceil(size / (MTU * 1.0));
        convertIntoPacket(MTU, numPackets, tag, destId, netServiceType);
        pkt.setLast(super.get_id());
        enque(pkt, GridSimTags.SCHEDULE_NOW);
    }

    /**
     * Takes a packet, adds it into a buffer and schedules it to be sent out at
     * an appropriate time.
     *
     * @param pkt   The packet to be buffered
     * @param delay The length of time this packet should be delayed (exclusive
     *              of the transmission time)
     * @pre pkt != null
     * @pre delay > 0
     * @post $none
     */
    private synchronized void enque(Packet pkt, double delay) {
        packetList_.add(pkt);
        if (packetList_.size() == 1) {
            double total = delay + (pkt.getSize() * NetIO.BITS / link_.getBaudRate());
            super.sim_schedule(super.get_id(), total, GridSimTags.SEND_PACKET);
        }
    }

    /**
     * Removes a single packet from the buffer and sends it down the link.
     * Then, schedules the next packet in the list.
     * @pre $none
     * @post $none
     */
    private synchronized void sendPacket() {
        if (packetList_ == null || packetList_.isEmpty() == true) {
            return;
        }
        Packet np = (Packet) packetList_.remove(0);
        boolean ping = false;
        int tag = -1;
        int dest = -1;
        if (np instanceof InfoPacket) {
            ((InfoPacket) np).addExitTime(GridSim.clock());
            ((InfoPacket) np).addBaudRate(link_.getBaudRate());
            ping = true;
        }
        if (np.getDestID() == outPort_.get_dest()) {
            String destName = super.get_name();
            destName = destName.replaceFirst("Output", "Input");
            dest = Sim_system.get_entity_id(destName);
            if (ping == true) {
                tag = GridSimTags.INFOPKT_RETURN;
                ((InfoPacket) np).setTag(tag);
            } else {
                tag = np.getTag();
            }
        } else {
            tag = GridSimTags.PKT_FORWARD;
            if (np.getTag() == GridSimTags.JUNK_PKT) {
                tag = GridSimTags.JUNK_PKT;
            }
            dest = link_.get_id();
        }
        super.sim_schedule(dest, GridSimTags.SCHEDULE_NOW, tag, np);
        if (packetList_.isEmpty() != true) {
            double delay = np.getSize() * NetIO.BITS / link_.getBaudRate();
            super.sim_schedule(super.get_id(), delay, GridSimTags.SEND_PACKET);
        }
    }
}
