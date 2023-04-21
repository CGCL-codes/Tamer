package overlaysim.protocol.overlay.pastry;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.NullEnumeration;
import ostore.util.SHA1Hash;
import ostore.util.SecureHash;
import overlaysim.message.Message;
import overlaysim.message.NetworkMessage;
import overlaysim.node.Node;
import overlaysim.protocol.overlay.ExistingList;
import overlaysim.protocol.overlay.GuidTools;
import overlaysim.protocol.overlay.LookUp;
import overlaysim.protocol.overlay.Overlay;
import overlaysim.protocol.overlay.chord.ChordMessage;
import overlaysim.protocol.overlay.chord.LookUpReq;
import overlaysim.protocol.overlay.dhtlayer.RoutePath;
import overlaysim.simulator.Simulator;
import overlaysim.start.Start;
import overlaysim.topology.Topology;
import overlaysim.util.Distribution;

public class Pastry extends Overlay {

    protected static Map<Integer, Pastry> instances = new LinkedHashMap<Integer, Pastry>();

    protected BigInteger my_guid;

    protected int[] my_digits;

    protected NeighborInfo my_neighbor_info;

    protected Logger logger;

    protected int my_node_id;

    protected int network_type;

    public boolean is_live = false;

    public boolean is_joining = false;

    public static ExistingList ex = new ExistingList();

    protected LeafSet leaf_set;

    protected int leaf_set_size;

    protected RoutingTable rt;

    protected static int DIGIT_VALUES = 16;

    protected static int DIGIT_BITS = 4;

    protected static int GUID_DIGITS = GuidTools.GUID_BITS / DIGIT_BITS;

    protected Pastry pastry;

    public Pastry(int id, int net) throws Exception {
        if (Logger.getRoot().getAllAppenders() instanceof NullEnumeration) {
            PatternLayout pl = new PatternLayout("%c: %m\n");
            ConsoleAppender ca = new ConsoleAppender(pl);
            Logger.getRoot().addAppender(ca);
            Logger.getRoot().setLevel(Level.INFO);
        }
        logger = Logger.getLogger(getClass());
        my_node_id = id;
        my_neighbor_info = new NeighborInfo(my_node_id, null);
        network_type = Node.NODE_TYPE_DUAL;
        pastry = this;
        instances.put(my_node_id, this);
        init();
    }

    public void init() {
        my_guid = GuidTools.get_guid(my_node_id);
        leaf_set_size = 8;
        leaf_set = new LeafSet(my_neighbor_info, leaf_set_size, GuidTools.MODULUS);
        double rt_scale = 0.9;
        rt = new RoutingTable(my_neighbor_info, rt_scale, GuidTools.MODULUS, GUID_DIGITS, DIGIT_VALUES);
        my_digits = rt.guid_to_digits(my_guid);
        Start.registerTimer(0, ready, null);
    }

    protected Runnable ready = new Runnable() {

        public void run() {
            if (network_type == Node.NODE_TYPE_DUAL) {
                Start.registerTimer(randomPeriod(10 * 1000 + my_node_id * 100), JoinAlarm, null);
            } else {
                Start.registerTimer(randomPeriod(500 * 1000 + my_node_id * 100), JoinAlarm, null);
            }
        }
    };

    public static int get_gateway(int my_node_id) {
        int gateway = Node.NODE_NULL;
        LinkedList<Integer> gateways = new LinkedList<Integer>();
        for (Iterator<Integer> i = ex.existing_list.iterator(); i.hasNext(); ) {
            gateway = (Integer) i.next();
            if (Node.IsOneSubnet(my_node_id, gateway)) {
                gateways.addLast(gateway);
            }
        }
        int length = gateways.size();
        if (length == 0) {
            return Node.NODE_NULL;
        }
        Random rand = new Random();
        int index = rand.nextInt(length);
        gateway = gateways.get(index);
        return gateway;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("my_node_id:" + my_node_id + "\t" + GuidTools.guid_to_string_40(my_guid) + "\n");
        result.append("leafset\n" + leaf_set.toString() + "\n");
        result.append("rt\n" + rt.toString() + "\n");
        return result.toString();
    }

    protected Runnable JoinAlarm = new Runnable() {

        public void run() {
            if (ex.node_num() <= 0) {
                ex.Add_Node(my_node_id);
                is_live = true;
                logger.info("Node:" + my_node_id + " Join the pastry as the first node");
                Start.registerTimer(randomPeriod(LookUp.INIT_LOOKUP_INTERVAL, my_guid), LookupAlarm, null);
                return;
            }
            if (ex.contains(my_node_id)) {
                System.out.println("The joining node:" + my_node_id + " has been in the pastry overlay network");
                System.exit(1);
            }
            if (is_joining) {
                System.out.println("The joining node is is_joining:" + my_node_id + " has been in the pastry overlay network");
                System.exit(1);
            }
            int gateway = ex.Get_Random_existing_Node();
            if (gateway == Node.NODE_NULL) {
                System.out.println("The gateway:" + gateway + " has not been in the pastry overlay network");
                System.exit(1);
            }
            JoinReq req = new JoinReq(my_node_id, gateway, my_guid);
            req.physical_path.addLast(my_node_id);
            req.physical_path.addLast(gateway);
            req.path.addLast(my_node_id);
            req.path.addLast(gateway);
            req.overlay_path.addLast(my_node_id);
            NetworkMessageSend(req);
            is_joining = true;
        }
    };

    protected Runnable AddAllAlarm = new Runnable() {

        public void run() {
            System.out.println("Node:" + my_node_id + " before AddAllAlarm");
            PastryAPI.node_state_print(pastry);
            for (Iterator<Integer> it = Node.instances.keySet().iterator(); it.hasNext(); ) {
                Node n = (Node) Node.instances.get((Integer) it.next());
                if (n.overlay instanceof Pastry) {
                    Pastry p = (Pastry) n.overlay;
                    if (p.my_node_id == my_node_id) continue;
                    RoutePath rp = new RoutePath();
                    rp.path.addLast(my_node_id);
                    rp.path.addLast(p.my_node_id);
                    NeighborInfo ni = new NeighborInfo(p.my_node_id, rp);
                    PastryAPI.add_to_node_state(ni, pastry);
                }
            }
            System.out.println("Node:" + my_node_id + " after AddAllAlarm");
            PastryAPI.node_state_print(pastry);
        }
    };

    protected Runnable LookupAlarm = new Runnable() {

        public void run() {
            if (is_live == false) {
                return;
            }
            if (LOOKUP_INTERVAL == 0) {
                LOOKUP_INTERVAL = (int) (Simulator.GetEndTimeMS() - Simulator.GetSimTimeMS());
                LOOKUP_INTERVAL = LOOKUP_INTERVAL / (Overlay.LOOKUP_NUMBER / ex.node_num());
                System.out.println("Time:" + Simulator.GetRSimTimeMS() + "\tNode:" + my_node_id + " init the lookup_interval");
                System.out.println("The EndTimeMs: " + (Simulator.GetEndTimeMS() - Simulator.GetStartTimeMS()));
                System.out.println("The GetSimTimeMS: " + (Simulator.GetSimTimeMS() - Simulator.GetStartTimeMS()));
                System.out.println("The ʣ��ʱ��: " + (Simulator.GetEndTimeMS() - Simulator.GetSimTimeMS()));
                System.out.println("The ex.node_num: " + ex.node_num());
                System.out.println("The LOOKUP_NUMBER: " + Overlay.LOOKUP_NUMBER);
                System.out.println("The LOOKUP_INTERVAL: " + LOOKUP_INTERVAL);
            }
            long l_interval = (long) Distribution.nextExponential(LOOKUP_INTERVAL);
            int lookup_node_id = ex.Get_Random_existing_Node();
            while (lookup_node_id == my_node_id || lookup_node_id == Node.NODE_NULL) {
                lookup_node_id = ex.Get_Random_existing_Node();
            }
            BigInteger lookup_guid = GuidTools.instance(lookup_node_id);
            NeighborInfo next_hop = PastryAPI.calc_next_hop(lookup_guid, pastry, false);
            if (next_hop.node_id == my_node_id) {
                return;
            }
            Overlay.lookup_start_num++;
            int dest = next_hop.path.path.get(1);
            int src = my_node_id;
            LookUpReq req = new LookUpReq(src, src, dest, lookup_guid, lookup_node_id);
            RoutePath.path_copy(req.path, next_hop.path.path);
            req.overlay_path.addLast(my_node_id);
            RoutePath rp_req = new RoutePath(req.path);
            req.routepaths.addLast(rp_req);
            NetworkMessage.Send(req);
            Start.registerTimer(l_interval, LookupAlarm, null);
        }
    };

    public void handleMessage(Message msg) {
        print_msg("recv", msg);
        if (is_live == false) {
            if (msg instanceof JoinReq) {
                System.out.println(" is_live == false:" + msg);
                Start.registerTimer(0, JoinAlarm, null);
                System.exit(1);
                return;
            }
        }
        if (msg instanceof PastryMessage) {
            PastryMessage cm = (PastryMessage) msg;
            int dest = cm.path.getLast();
            if (dest != my_node_id) {
                NetworkMessageSend(cm);
                return;
            }
        }
        if (msg instanceof LookUpReq) {
            LookUpReq cm = (LookUpReq) msg;
            int dest = cm.path.getLast();
            if (dest != my_node_id) {
                NetworkMessageSend(cm);
                return;
            }
        }
        if (msg instanceof JoinResp) {
            JoinResp resp = (JoinResp) msg;
            PastryAPI.handle_join_resp(resp, this);
            ex.Add_Node(my_node_id);
            logger.info("Node:" + my_node_id + " Join the Pasty Node_num:" + ex.node_num());
            Start.registerTimer(randomPeriod(LookUp.INIT_LOOKUP_INTERVAL, my_guid), LookupAlarm, null);
            is_joining = false;
            is_live = true;
        } else if (msg instanceof LookUpReq) {
            handle_lookup_req((LookUpReq) msg);
        } else if (msg instanceof PastryMessage) {
            if (is_live == false) return;
            PastryMessage cm = (PastryMessage) msg;
            PastryAPI.handleMessage(msg, this);
        } else {
            System.out.println("In handleMessage of codhtsimple: recv a error msg:" + msg);
            System.exit(1);
        }
    }

    private void handle_lookup_req(LookUpReq req) {
        if (req.lookup_node_id == my_node_id) {
            Overlay.lookup_succ_num++;
            Overlay.total_lookup_hop_num += (req.path.size() - 1);
            Overlay.total_lookup_hop_delay += Topology.get_delay_by_path(req.path);
        } else {
            NeighborInfo next_hop = PastryAPI.calc_next_hop(req.guid, pastry, false);
            req.overlay_path.addLast(pastry.my_node_id);
            if (req.overlay_path.path.contains(next_hop.node_id)) {
                return;
            }
            req.dest_node_id = next_hop.path.path.get(1);
            req.src_node_id = my_node_id;
            RoutePath rp = RoutePath.path_merge(new RoutePath(req.path), next_hop.path);
            RoutePath.path_copy(req.path, rp.path);
            RoutePath rp_req = new RoutePath(next_hop.path.path);
            req.routepaths.addLast(rp_req);
            pastry.NetworkMessageSend(req);
        }
    }

    public long randomPeriod(int mean) {
        Random rand;
        rand = new Random(my_guid.longValue());
        return mean / 2 + rand.nextInt(mean);
    }

    public void NetworkMessageSend(Message msg) {
        if (msg instanceof PastryMessage) {
            PastryMessage cm = (PastryMessage) msg;
            if (cm.path.isEmpty()) {
                System.out.println("In NetworkMessageSend of pastry \n Error: the path is null");
                System.out.println("msg:" + cm);
                System.exit(1);
            }
            int dest = cm.path.getLast();
            if (dest == my_node_id) {
                System.out.println("The dest is itself:" + msg);
                System.exit(1);
            }
            if (cm.my_node_id == my_node_id) RoutePath.path_check(cm.path);
            int index = cm.path.lastIndexOf(my_node_id);
            if (index == -1) {
                System.out.println("Node:" + my_node_id + " NetworkMessageSend, index == -1:" + msg);
                System.exit(1);
            }
            int next_hop = cm.path.get(index + 1);
            cm.src_node_id = my_node_id;
            cm.dest_node_id = next_hop;
            NetworkMessage.Send(cm);
        } else if (msg instanceof LookUpReq) {
            ChordMessage cm = (ChordMessage) msg;
            if (cm.path.isEmpty()) {
                System.out.println("In NetworkMessageSend of pastry \n Error: the path is null");
                System.out.println("msg:" + cm);
                System.exit(1);
            }
            int dest = cm.path.getLast();
            if (dest == my_node_id) {
                System.out.println("The dest is itself:" + msg);
                System.exit(1);
            }
            if (cm.my_node_id == my_node_id) RoutePath.path_check(cm.path);
            int index = cm.path.lastIndexOf(my_node_id);
            if (index == -1) {
                System.out.println("Node:" + my_node_id + " NetworkMessageSend, index == -1:" + msg);
                System.exit(1);
            }
            int next_hop = cm.path.get(index + 1);
            cm.src_node_id = my_node_id;
            cm.dest_node_id = next_hop;
            NetworkMessage.Send(cm);
        } else {
            System.out.println("NetworkMessageSend of pastry just support pastry message. " + msg);
            System.exit(1);
        }
        print_msg("send", msg);
    }

    public void print_msg(String s, Message msg) {
    }

    public static long randomPeriod(int mean, BigInteger guid) {
        Random rand;
        rand = new Random(guid.longValue());
        return mean / 2 + rand.nextInt(mean);
    }
}
