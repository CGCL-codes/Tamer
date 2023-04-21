package seqSamoa.protocols.abcast;

import java.io.OutputStream;
import java.util.LinkedList;
import seqSamoa.Message;
import seqSamoa.ProtocolModule;
import seqSamoa.ProtocolStack;
import seqSamoa.ServiceCallOrResponse;
import seqSamoa.exceptions.AlreadyExistingProtocolModuleException;
import seqSamoa.services.abcast.Abcast;
import seqSamoa.services.abcast.AbcastResponseParameters;
import seqSamoa.services.commit.NbCommits;
import seqSamoa.services.commit.NbCommitsCallParameters;
import seqSamoa.services.consensus.Consensus;
import seqSamoa.services.consensus.ConsensusCallParameters;
import seqSamoa.services.consensus.ConsensusResponseParameters;
import seqSamoa.services.pt2pt.PT2PT;
import seqSamoa.services.pt2pt.PT2PTCallParameters;
import seqSamoa.services.pt2pt.PT2PTResponseParameters;
import static_recovery.common.abcast.AtomicBroadcastCommit;
import uka.transport.Transportable;
import framework.Constants;
import framework.GroupCommEventArgs;
import framework.GroupCommMessage;
import framework.PID;
import framework.libraries.Trigger;
import framework.libraries.serialization.TBoolean;
import framework.libraries.serialization.TLong;

/**
 * This class implement the ABcast with a static set of processes in recovery
 * model. The algorithm implemented is inspired by the Mena and Schiper paper.
 * 
 * This Protocol need a Protocol that implements PT2PT, Consensus and NbCommits
 * 
 * The service implemented is Abcast (described in util/Services.java)
 */
public class ProtocolCrashRecoveryAbcastCommit extends ProtocolModule implements Trigger {

    private static final int MAX_PROCESSES = 7;

    private static final int MAX_MESSAGES = 45;

    private Abcast abcast;

    private Consensus consensus;

    private PT2PT pt2pt;

    private NbCommits nbCommits;

    AtomicBroadcastCommit handlers;

    protected Abcast.Executer abcastExecuter;

    protected Consensus.Listener consensusListener;

    protected PT2PT.Listener pt2ptListener;

    /**
     * Constructor.
     * 
     * @param name
     *            String identifier of the protocol
     * @param stack
     * 			  The stack in which the module will be
     */
    public ProtocolCrashRecoveryAbcastCommit(String name, ProtocolStack stack, boolean uniform, Abcast abcast, Consensus consensus, PT2PT pt2pt, NbCommits nbCommits) throws AlreadyExistingProtocolModuleException {
        super(name, stack);
        this.abcast = abcast;
        this.consensus = consensus;
        this.pt2pt = pt2pt;
        this.nbCommits = nbCommits;
        handlers = new AtomicBroadcastCommit(this, stack.getStorage(), stack.getFlowControl(), stack.getGroup(), stack.getPID(), uniform);
        LinkedList<ServiceCallOrResponse> initiatedAbcast = new LinkedList<ServiceCallOrResponse>();
        for (int i = 0; i < MAX_PROCESSES; i++) initiatedAbcast.add(ServiceCallOrResponse.createServiceCallOrResponse(pt2pt, true));
        initiatedAbcast.add(ServiceCallOrResponse.createServiceCallOrResponse(consensus, true));
        abcastExecuter = abcast.new Executer(this, initiatedAbcast) {

            public void evaluate(Object params, Message dmessage) {
                synchronized (this.parent) {
                    GroupCommEventArgs ga = new GroupCommEventArgs();
                    ga.addLast(dmessage.toGroupCommMessage());
                    handlers.handleAbcast(ga);
                }
            }
        };
        LinkedList<ServiceCallOrResponse> initiatedPt2pt = new LinkedList<ServiceCallOrResponse>();
        for (int i = 0; i < MAX_PROCESSES; i++) initiatedPt2pt.add(ServiceCallOrResponse.createServiceCallOrResponse(pt2pt, true));
        initiatedPt2pt.add(ServiceCallOrResponse.createServiceCallOrResponse(consensus, true));
        pt2ptListener = pt2pt.new Listener(this, initiatedPt2pt) {

            public void evaluate(PT2PTResponseParameters infos, Transportable message) {
                synchronized (this.parent) {
                    GroupCommEventArgs ga = new GroupCommEventArgs();
                    ga.addLast(message);
                    ga.addLast(infos.pid);
                    try {
                        handlers.handlePt2PtDeliver(ga);
                    } catch (Exception ex) {
                        throw new RuntimeException("ProtocolAbcast: " + "pt2ptListener: " + ex.getMessage());
                    }
                }
            }
        };
        LinkedList<ServiceCallOrResponse> initiatedConsensus = new LinkedList<ServiceCallOrResponse>();
        for (int i = 0; i < MAX_PROCESSES; i++) initiatedConsensus.add(ServiceCallOrResponse.createServiceCallOrResponse(pt2pt, true));
        initiatedConsensus.add(ServiceCallOrResponse.createServiceCallOrResponse(consensus, true));
        for (int i = 0; i < MAX_MESSAGES; i++) initiatedConsensus.add(ServiceCallOrResponse.createServiceCallOrResponse(abcast, false));
        consensusListener = consensus.new Listener(this, initiatedConsensus) {

            public void evaluate(ConsensusResponseParameters infos, Transportable message) {
                synchronized (this.parent) {
                    GroupCommEventArgs ga = new GroupCommEventArgs();
                    ga.addLast(infos.id);
                    ga.addLast(message);
                    handlers.handleDecide(ga);
                }
            }
        };
    }

    public synchronized void commit() {
        try {
            handlers.handleCommit();
        } catch (Exception ex) {
            throw new RuntimeException("ProtocolAbcast: Commit: " + ex.getMessage());
        }
    }

    public synchronized void recovery(boolean recovery) {
        GroupCommEventArgs e = new GroupCommEventArgs();
        e.add(new TBoolean(recovery));
        try {
            handlers.handleRecovery(e);
        } catch (Exception ex) {
            throw new RuntimeException("ProtocolAbcast: Recovery: " + ex.getMessage());
        }
    }

    public synchronized void dump(OutputStream os) {
        handlers.dump(os);
    }

    /**
     * Manage the triggering of the events
     */
    public void trigger(int type, GroupCommEventArgs l) {
        switch(type) {
            case Constants.PT2PTSEND:
                Transportable message = l.remove(0);
                PID pid = (PID) l.remove(0);
                Transportable key = l.remove(0);
                PT2PTCallParameters rparams = new PT2PTCallParameters(pid, key);
                pt2pt.call(rparams, new Message(message, pt2ptListener));
                break;
            case Constants.KILLMESSAGE:
                Transportable kkey = l.remove(0);
                PT2PTCallParameters rkparams = new PT2PTCallParameters(null, kkey);
                pt2pt.call(rkparams, null);
                break;
            case Constants.PROPOSE:
                Transportable id = l.remove(0);
                Transportable cmessage = l.remove(0);
                ConsensusCallParameters cparams = new ConsensusCallParameters(null, id);
                consensus.call(cparams, new Message(cmessage, consensusListener));
                break;
            case Constants.ADELIVER:
                GroupCommMessage gm = (GroupCommMessage) l.remove(0);
                Message dmessage = new Message(gm);
                PID apid = (PID) l.remove(0);
                AbcastResponseParameters infos = new AbcastResponseParameters(apid);
                abcast.response(infos, dmessage);
                break;
            case Constants.NB_COMMITS:
                TLong nbCommitsLong = (TLong) l.remove(0);
                NbCommitsCallParameters uparams = new NbCommitsCallParameters(nbCommitsLong);
                nbCommits.call(uparams, null);
                break;
            default:
                throw new RuntimeException("Error : ProtocolAbcast : trigger" + type + "Trying to send an unknown event type!");
        }
    }
}
