package groupcomm.common.abcast;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uka.transport.Transportable;
import framework.Constants;
import framework.GroupCommEventArgs;
import framework.GroupCommException;
import framework.GroupCommMessage;
import framework.PID;
import framework.libraries.FlowControl;
import framework.libraries.Timer;
import framework.libraries.Trigger;
import framework.libraries.serialization.TArrayList;
import framework.libraries.serialization.TBoolean;
import framework.libraries.serialization.TCollection;
import framework.libraries.serialization.THashMap;
import framework.libraries.serialization.THashSet;
import framework.libraries.serialization.TLinkedHashMap;
import framework.libraries.serialization.TList;
import framework.libraries.serialization.TLong;
import framework.libraries.serialization.TMap;
import framework.libraries.serialization.TSet;

/**
 * <b> This class implements the common code for algorithm abcast. </b>
 * <hr>
 * <b> Events: 
 * <dt> <i>Init</i>           </dt> <dd> Initializes the abcast layer </dd>
 * <dt> <i>Abcast</i>         </dt> <dd> Send a Broadcast message, with the abcast algorithm </dd>
 * <dt> <i>Pt2PtDeliver</i>   </dt> <dd> Happend when a message is received by the lower layers </dd>
 * <dt> <i>Decide</i>         </dt> <dd> Happend when consensus has decided </dd>
 * </dl>
 */
public class AbcastImpl {

    private PID myself;

    private boolean initialized = false;

    private Trigger abcast;

    private FlowControl flow_control;

    private int fc_key;

    private int nbMsgsSent = 0;

    private Timer timer;

    private boolean timerOn = false;

    private final int INACTIVITY_TIMEOUT = 5 * 1000;

    private TArrayList known;

    private TSet aDelivered;

    private TLinkedHashMap aUndelivered;

    private TMap maxIdProProc;

    private long k;

    private long gossipK;

    private AbcastMessageID abcastId;

    private boolean consensusStarted;

    public static final int MIN_LOCALLY_ABCAST = 1;

    public static final int MSGS_PER_CONSENSUS = 4;

    private int max_locally_abcast = MSGS_PER_CONSENSUS;

    private static final Logger logger = Logger.getLogger(AbcastImpl.class.getName());

    public static class TriggerItem {

        public int type;

        public GroupCommEventArgs args;

        public TriggerItem(int type, GroupCommEventArgs args) {
            this.type = type;
            this.args = args;
        }
    }

    /**
     * Constructor.
     *
     * @param abcast  object of a framework protocol based class, which ensure event routing for this protocol.
     */
    public AbcastImpl(Trigger abcast, FlowControl fc, Timer t, PID myself) {
        logger.entering("AbcastImpl", "<constr>");
        this.abcast = abcast;
        this.flow_control = fc;
        this.myself = myself;
        this.timer = t;
        aDelivered = new THashSet();
        aUndelivered = new TLinkedHashMap();
        maxIdProProc = new THashMap();
        abcastId = new AbcastMessageID(myself, 0);
        logger.exiting("AbcastImpl", "<constr>");
    }

    /**
     * Handler for the <i>Init</i> event. </br>
     * It sends the list of known processes to the lower layer allowing them to communicate with us
     *
     * @param ev <dl>
     *              <dt> arg1 : Set[PID] </dt> <dd> List of processes for broadcasting </dd>
     *           </dl>
     *
     * @exception InitException An init event has already been received.
     */
    public void handleInit(GroupCommEventArgs ev) throws GroupCommException {
        logger.entering("AbcastImpl", "handleInit");
        TList p = (TList) ev.removeFirst();
        if (initialized) throw new GroupCommException("AbcastImpl already initialized.");
        initialized = true;
        fc_key = flow_control.getFreshKey();
        k = 1;
        gossipK = 1;
        consensusStarted = false;
        known = new TArrayList(p);
        for (int i = 0; i < known.size(); i++) for (int j = i + 1; j < known.size(); j++) if (known.get(i).equals(known.get(j))) throw new GroupCommException("Process" + known.get(i) + " appears more than once in the group.");
        Iterator it = known.iterator();
        PID pid;
        while (it.hasNext()) {
            pid = (PID) it.next();
            maxIdProProc.put(pid, new TLong(-1));
        }
        GroupCommEventArgs jrl = new GroupCommEventArgs();
        jrl.addLast(new THashSet(p));
        jrl.addLast(new THashSet());
        abcast.trigger(Constants.JOINREMOVELIST, jrl);
        logger.exiting("AbcastImpl", "handleInit");
    }

    /**
     * The handler for the <i>Abcast</i> event. <br/>
     * It broadcasts the message to all the processes described by the init event. <br/>
     * It adds an Abcast-Id to the message.
     *
     * @param ev <dl>
     *               <dt> arg1: GroupCommMessage </dt> <dd> The message </dd>
     *           </dl>
     */
    public void handleAbcast(GroupCommEventArgs ev) {
        logger.entering("AbcastImpl", "handleAbcast");
        GroupCommMessage msg = (GroupCommMessage) ev.removeFirst();
        AbcastMessageID id = abcastId.nextId();
        GroupCommMessage cloneM = msg.cloneGroupCommMessage();
        aUndelivered.put(id, cloneM);
        nbMsgsSent++;
        if (nbMsgsSent >= max_locally_abcast) flow_control.block(fc_key);
        TriggerItem propose = testAndConsensus();
        msg.tpack(id);
        Iterator it = ((TList) known.clone()).iterator();
        while (it.hasNext()) {
            PID pid = (PID) it.next();
            if (!pid.equals(myself)) {
                GroupCommMessage newMess = msg.cloneGroupCommMessage();
                GroupCommEventArgs pt2ptSend = new GroupCommEventArgs();
                pt2ptSend.addLast(newMess);
                pt2ptSend.addLast(pid);
                pt2ptSend.addLast(new TBoolean(false));
                logger.log(Level.FINE, "Sending Pt2Pt message id: {0} to {1}\n\tMessage: {2}", new Object[] { id, pid, newMess });
                abcast.trigger(Constants.PT2PTSEND, pt2ptSend);
            }
        }
        if (propose != null) abcast.trigger(propose.type, propose.args);
        logger.exiting("AbcastImpl", "handleAbcast");
    }

    /**
     * The handler for the <i>Pt2PtDeliver</i> event. <br/>
     * When we recieve a message from the Reliable communication layer, we have 
     * to resent the message to all the receipents, if it's the first time it arrives.
     * That's the R-Broadcast part of the protocol. It launch a consensus too.
     * 
     * @param ev <dl>
     *               <dt> arg1: GroupCommMessage (id::m) </dt> <dd> The message, with an id </dd>
     *               <dt> arg2: PID                   </dt> <dd> Source PID </dd>
     *           </dl>
     */
    public void handlePt2PtDeliver(GroupCommEventArgs ev) {
        logger.entering("AbcastImpl", "handlePt2PtDeliver");
        GroupCommMessage msg = (GroupCommMessage) ev.removeFirst();
        PID source = (PID) ev.removeFirst();
        Transportable t = (Transportable) msg.tunpack();
        if (t instanceof TLong) {
            long rem_k = ((TLong) t).longValue();
            gossipK = Math.max(gossipK, rem_k);
            TriggerItem propose = testAndConsensus();
            if (propose != null) abcast.trigger(propose.type, propose.args);
            logger.exiting("AbcastImpl", "handlePt2PtDeliver");
            return;
        }
        AbcastMessageID id = (AbcastMessageID) t;
        logger.log(Level.FINE, "Receiving message id: {0} from {1}\n\tMessage: {2}", new Object[] { id, source, msg });
        if (!aUndelivered.containsKey(id) && !aDelivered.contains(id) && !(id.id <= ((TLong) maxIdProProc.get(id.proc)).longValue())) {
            aUndelivered.put(id, msg);
            TriggerItem propose = testAndConsensus();
            if (nbMsgsSent >= max_locally_abcast) flow_control.block(fc_key);
            if (propose != null) abcast.trigger(propose.type, propose.args);
        }
        logger.exiting("AbcastImpl", "handlePt2PtDeliver");
    }

    /**
     * The handler for the <i>Decide</i> event. <br/>
     * It happends when consensus has decided an order to ADeliver messages
     * We are sure that it's the same for everybody, but we test
     * if the message isn't already delivered.
     *
     * @param ev <dl>
     *               <dt> arg1: GroupCommMessage (k::Decision) </dt> <dd> The decision </dd>
     *           </dl>
     *
     * @exception DecideException Thrown when an unknown DecideEvent happends
     */
    public void handleDecide(GroupCommEventArgs ev) throws GroupCommException {
        logger.entering("AbcastImpl", "handleDecide");
        TLinkedHashMap undelivered = (TLinkedHashMap) ev.removeFirst();
        long kdecision = ((TLong) (ev.removeFirst())).longValue();
        if (kdecision + 1 != k) throw new GroupCommException("AbcastImpl: handleDecide: Unordered decide event: incoming = " + kdecision + ", expected = " + (k - 1));
        if (undelivered.size() < MSGS_PER_CONSENSUS) max_locally_abcast = Math.min(MSGS_PER_CONSENSUS * 2, max_locally_abcast + 1);
        if (undelivered.size() > MSGS_PER_CONSENSUS) max_locally_abcast = Math.max(MIN_LOCALLY_ABCAST, max_locally_abcast - 1);
        GroupCommMessage msg, delivered;
        AbcastMessageID id;
        TLinkedHashMap toTrigger = new TLinkedHashMap();
        while (!undelivered.isEmpty()) {
            id = (AbcastMessageID) undelivered.keySet().iterator().next();
            msg = (GroupCommMessage) undelivered.remove(id);
            delivered = msg.cloneGroupCommMessage();
            long maxId = ((TLong) maxIdProProc.get(id.proc)).longValue();
            if (!aDelivered.contains(id) && id.id > maxId) {
                aUndelivered.remove(id);
                aDelivered.add(id);
                toTrigger.put(id, delivered);
                if (id.proc.equals(myself)) {
                    nbMsgsSent--;
                }
                AbcastMessageID newID = new AbcastMessageID(id.proc, maxId + 1);
                while (aDelivered.contains(newID)) {
                    aDelivered.remove(newID);
                    maxId++;
                    newID.id++;
                }
                maxIdProProc.put(id.proc, new TLong(maxId));
            }
        }
        if (nbMsgsSent < max_locally_abcast) flow_control.release(fc_key);
        consensusStarted = false;
        timer.schedule(new TLong(k), false, INACTIVITY_TIMEOUT);
        timerOn = true;
        TriggerItem propose = testAndConsensus();
        while (!toTrigger.isEmpty()) {
            id = (AbcastMessageID) toTrigger.keySet().iterator().next();
            msg = (GroupCommMessage) toTrigger.remove(id);
            GroupCommEventArgs adeliver = new GroupCommEventArgs();
            adeliver.addLast(msg);
            adeliver.addLast(id.proc);
            abcast.trigger(Constants.ADELIVER, adeliver);
        }
        if (propose != null) abcast.trigger(propose.type, propose.args);
        logger.exiting("AbcastImpl", "handleDecide");
    }

    /**
     * The handler for the <i>Timeout</i> event. <br/>
     * It happends when consensus has started and hasnot decided for a long time
     * This is an optimization to save messages tha are sent
     *
     * @param ev <dl>
     *               <dt> arg: GroupCommMessage (timerKey) </dt> <dd> Timer key. Discarded </dd>
     *           </dl>
     *
     * @exception None
     */
    public void handleTimeout(GroupCommEventArgs arg) {
        logger.entering("AbcastImpl", "handleTimeout");
        timerOn = false;
        Iterator it = ((TList) known.clone()).iterator();
        TLong kObj = new TLong(k);
        while (it.hasNext()) {
            PID pid = (PID) it.next();
            if (!pid.equals(myself)) {
                GroupCommMessage myK = new GroupCommMessage();
                myK.tpack(kObj);
                GroupCommEventArgs pt2ptSend = new GroupCommEventArgs();
                pt2ptSend.addLast(myK);
                pt2ptSend.addLast(pid);
                pt2ptSend.addLast(new TBoolean(false));
                logger.log(Level.FINE, "Sending special Pt2Pt message myK to {0}\n\tMessage: {1}", new Object[] { pid, myK });
                abcast.trigger(Constants.PT2PTSEND, pt2ptSend);
            }
        }
        logger.exiting("AbcastImpl", "handleTimeout");
    }

    private TriggerItem testAndConsensus() {
        logger.entering("AbcastImpl", "testAndConsensus");
        if (!consensusStarted && (!aUndelivered.isEmpty() || gossipK > k)) {
            TMap propose;
            int sizePropose = Math.max(MSGS_PER_CONSENSUS / 2, aUndelivered.size() / 2);
            if (aUndelivered.size() > sizePropose) {
                TCollection ids = aUndelivered.keySet();
                propose = new TLinkedHashMap();
                Iterator it = ids.iterator();
                for (int tune = 0; tune < sizePropose && it.hasNext(); tune++) {
                    AbcastMessageID id = (AbcastMessageID) it.next();
                    propose.put(id, aUndelivered.get(id));
                }
            } else {
                propose = (TLinkedHashMap) aUndelivered.clone();
            }
            TLong kObj = new TLong(k);
            logger.log(Level.FINE, "Launching consensus#{1}:\n\tValue: {0}", new Object[] { propose, kObj });
            k++;
            GroupCommEventArgs run = new GroupCommEventArgs();
            run.addLast(known);
            run.addLast(propose);
            run.addLast(kObj);
            consensusStarted = true;
            if (timerOn) {
                timer.cancel(new TLong(k - 1));
                timerOn = false;
            }
            logger.exiting("AbcastImpl", "testAndConsensus");
            return new TriggerItem(Constants.PROPOSE, run);
        }
        logger.exiting("AbcastImpl", "testAndConsensus");
        return null;
    }

    /**
     * Used for debug
     *
     * @param out The output stream used for showing infos
     */
    public void dump(OutputStream out) {
        PrintStream err = new PrintStream(out);
        err.println("======== AbcastImpl: dump =======");
        err.println(" Initialized: " + String.valueOf(initialized));
        err.println(" ConsensusRunning: " + String.valueOf(consensusStarted));
        err.println(" Next consensus id: " + k);
        err.println(" Last AbcastMessage id used:\n\t" + abcastId);
        err.println(" Flow Control threshold: " + MIN_LOCALLY_ABCAST);
        err.println("\t used: " + nbMsgsSent);
        err.println(" Known processes: size: " + known.size());
        Iterator it = known.iterator();
        PID pid;
        while (it.hasNext()) {
            pid = (PID) it.next();
            err.println("\t" + pid.toString());
        }
        err.println(" A-Undelivered messages:");
        err.println("   " + aUndelivered.toString());
        err.println(" A-Delivered messages IDs:");
        it = aDelivered.iterator();
        AbcastMessageID id;
        while (it.hasNext()) {
            id = (AbcastMessageID) it.next();
            err.println("\t" + id);
        }
        err.println("   and all message with id <= ");
        err.println("\t" + maxIdProProc.toString());
        err.println("==================================");
    }
}
