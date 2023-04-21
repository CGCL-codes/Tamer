package oopl;

import ail.others.AILexception;
import ail.others.MAS;
import ail.others.PerceptListener;
import ail.semantics.AILAgent;
import ail.semantics.ApplicablePlan;
import ail.semantics.Message;
import ail.semantics.Unifier;
import ail.semantics.Intention;
import ail.syntax.Action;
import ail.syntax.BeliefBase;
import ail.syntax.Deed;
import ail.syntax.Event;
import ail.syntax.GBelief;
import ail.syntax.Literal;
import ail.syntax.Atom;
import ail.syntax.Plan;
import ail.syntax.SendAction;
import ail.syntax.VarTerm;
import ail.syntax.PlanLibrary;
import gov.nasa.jpf.jvm.Verify;
import gov.nasa.jpf.annotation.FilterField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import ail.others.AILEnv;

/**
 * A Gwendolen Agent - a demonstration of how to subclass an AIL Agent and
 * create a reasoning cycle.
 * 
 * @author louiseadennis
 *
 */
public class OOPLAgent extends AILAgent implements AILEnv {

    public static byte OOPLTell = 1;

    /**
	 * We create an object to signal that percepts are being checked or altered.
	 */
    private Boolean busyperceptflag = true;

    private int outputlevel = 1;

    /**
	 * The list of percept listeners.
	 */
    @FilterField
    private static List<PerceptListener> perceptListeners = null;

    private BeliefBase inst = new BeliefBase();

    private PlanLibrary caRules = new PlanLibrary();

    private PlanLibrary sRules = new PlanLibrary();

    @FilterField
    private Action lastAction;

    @FilterField
    private String lastAgent;

    private final Queue<Literal> nextAction = new LinkedList<Literal>();

    @FilterField
    private final BlockingQueue<Literal> actionResult = new LinkedBlockingQueue<Literal>(1);

    @FilterField
    private LinkedList<ApplicablePlan> generated = new LinkedList<ApplicablePlan>();

    /**
	 * A map of agents to their pending messages.
	 */
    private Map<String, List<Message>> agMessages = new HashMap<String, List<Message>>();

    /**
	 * List of agents who have already "collected" the current set of percepts.
	 */
    @FilterField
    protected List<String> uptodateAgs = new ArrayList<String>();

    private ArrayList<Message> mymessages = new ArrayList<Message>();

    /** Clears the list of  messages of a specific agent */
    private void clearMessages(String agName) {
        synchronized (busyperceptflag) {
            if (agName != null) {
                List<Message> agl = agMessages.get(agName);
                if (agl != null) {
                    agl.clear();
                }
            }
        }
    }

    /**
	 * Construct an OOPLAgent from a name.
	 * 
	 * @param name the name of the agent.
	 */
    public OOPLAgent(String name) throws AILexception {
        super(name);
        MAS orgMas = new MAS(this);
        setMAS(orgMas);
        addBeliefBase(inst, 1);
        try {
            addPerceptListener(this);
        } catch (Exception e) {
            throw new AILexception("AIL: error creating the agent architecture! - " + e);
        }
        setReasoningCycle(new OOPLRC());
    }

    public Literal getAction() {
        return (nextAction.poll());
    }

    /** Adds a perception for a specific agent */
    public void addMessage(String agName, Message msg) {
        if (agName != getAgName()) {
            synchronized (busyperceptflag) {
                if (msg != null && agName != null) {
                    List<Message> msgl = agMessages.get(agName);
                    if (msgl == null) {
                        msgl = new ArrayList<Message>();
                        msgl.add(msg);
                        agMessages.put(agName, msgl);
                    } else {
                        msgl.add(msg);
                    }
                    uptodateAgs.remove(agName);
                    Collections.sort(uptodateAgs);
                }
            }
        } else {
            mymessages.add(msg);
        }
        notifyListeners(agName);
    }

    public List<Message> getMessages(String agName) {
        int size;
        synchronized (busyperceptflag) {
            List<Message> agl = agMessages.get(agName);
            if (agl != null) {
                size = agl.size();
            } else {
                size = 0;
            }
            List<Message> p = new ArrayList<Message>(size);
            if (agl != null) {
                p.addAll(agl);
                clearMessages(agName);
            }
            return p;
        }
    }

    public synchronized Unifier executeAction(String agName, Action act) throws AILexception {
        lastAgent = agName;
        lastAction = act;
        Literal does = new Literal("does");
        does.addTerm(new Atom(agName));
        if (act instanceof SendAction) {
            SendAction sent = (SendAction) act;
            Message m = sent.getMessage(agName);
            String r = m.getReceiver();
            addMessage(r, m);
            does.addTerm(m.toTerm());
        } else {
            does.addTerm(act);
        }
        if (outputlevel == 1) {
            System.out.println(does);
        }
        Verify.endAtomic();
        notifyListeners(getAgName());
        try {
            nextAction.offer(does);
            this.tellawake();
            Verify.endAtomic();
            Literal r1 = getResult();
            Literal r2 = new Literal("return");
            r2.addTerm(new VarTerm("R"));
            Unifier u = new Unifier();
            r2.unifies(r1, u);
            System.out.println(agName + " done " + act.toString());
            return (u);
        } catch (InterruptedException e) {
            Verify.endAtomic();
            throw new AILexception(e.toString());
        }
    }

    public Literal getResult() throws InterruptedException {
        Literal l1 = actionResult.take();
        return l1;
    }

    public List<Literal> getPercepts(String agName, boolean update) {
        return (new ArrayList<Literal>());
    }

    protected List<Plan> getAllReactivePlans(Event ple) {
        if (this.getReasoningCycle().getStage() == ((OOPLRC) this.getReasoningCycle()).StageGCA) {
            return (caRules.getAllReactivePlans());
        } else if (this.getReasoningCycle().getStage() == ((OOPLRC) this.getReasoningCycle()).StageGS) {
            return (sRules.getAllReactivePlans());
        } else {
            return (new LinkedList<Plan>());
        }
    }

    protected List<Plan> getAllRelevantPlans(Event ple) {
        if (this.getReasoningCycle().getStage() == ((OOPLRC) this.getReasoningCycle()).StageGE) {
            return fPL.getAllRelevant(ple.getPredicateIndicator());
        } else {
            return (new LinkedList<Plan>());
        }
    }

    public LinkedList<ApplicablePlan> filterPlans(LinkedList<ApplicablePlan> aps) {
        if (this.getReasoningCycle().getStage() == ((OOPLRC) this.getReasoningCycle()).StageGE && aps.size() > 1) {
            LinkedList<ApplicablePlan> naps = new LinkedList<ApplicablePlan>();
            naps.add(aps.get(0));
            return (naps);
        } else if (this.getReasoningCycle().getStage() == ((OOPLRC) this.getReasoningCycle()).StageGCA || this.getReasoningCycle().getStage() == ((OOPLRC) this.getReasoningCycle()).StageGS) {
            aps.removeAll(generated);
            return (aps);
        } else {
            return (aps);
        }
    }

    public boolean done() {
        return (nextAction.isEmpty());
    }

    public Action lastAction() {
        return (lastAction);
    }

    public String lastActionby() {
        return (lastAgent);
    }

    public boolean nothingPending(String agName) {
        synchronized (busyperceptflag) {
            if (agName != getAgName()) {
                return true;
            } else {
                System.err.println(nextAction.isEmpty());
                return (nextAction.isEmpty());
            }
        }
    }

    public boolean separateThread() {
        return (false);
    }

    public void addEffectRule(EffectRule e) throws AILexception {
        this.addPlan(e);
    }

    public void addCountsAsRule(CountsAsRule ca) throws AILexception {
        this.caRules.add(ca);
    }

    public void addSanctionRule(SanctionRule s) throws AILexception {
        this.sRules.add(s);
    }

    public void addGenerated(ArrayList<ApplicablePlan> ap) {
        this.generated.addAll(ap);
    }

    public void cleanGenerated() {
        this.generated.clear();
    }

    public void cleanInstitutional() {
        this.inst = new BeliefBase();
        bbmap.remove("1.0");
        bbmap.put("1.0", inst);
    }

    public void postResult(Literal r) {
        this.actionResult.add(r);
        System.err.println("Posted: " + r);
    }

    public String toString() {
        Verify.beginAtomic();
        StringBuffer s = new StringBuffer();
        s.append("Brute state: \n");
        s.append(this.getBB().toString());
        s.append("\n");
        s.append("Institutional state: \n");
        s.append(this.inst.toString());
        if (getNameOfLastRule() != null && getNameOfLastRule().equals("ApplyApplicableRules")) {
            Intention i = this.getIntention();
            List<Intention> is = this.getIntentions();
            if (!i.empty()) {
                s.append("\nApplying effect rule:");
                s.append("{ ");
                s.append(i.hdG().toString());
                s.append("} ");
                try {
                    s.append(i.hdE().getLiteral().getTerm(1).toString());
                } catch (Exception e) {
                    s.append("help!");
                }
                s.append(" { ");
                for (Deed d : i.deeds()) {
                    s.append(d.getLiteral().toString());
                    s.append(" ");
                }
                s.append("}");
            } else if (!is.isEmpty()) {
                s.append("\nApplying rules:\n");
                for (Intention r : is) {
                    s.append("{ ");
                    s.append(i.hdG().toString());
                    s.append("} => { ");
                    for (Deed d : r.deeds()) {
                        s.append(d.getLiteral().toString());
                        s.append(" ");
                    }
                    s.append("}\n");
                }
            }
        }
        String s1 = s.toString();
        Verify.endAtomic();
        return s1;
    }

    /**
	 * Add a percept listener to the environment.
	 * @param l
	 */
    public void addPerceptListener(PerceptListener l) {
        if (perceptListeners == null) {
            perceptListeners = new ArrayList<PerceptListener>();
        }
        perceptListeners.add(l);
    }

    /**
     * Notify the listeners that the perceptions have changed.
     *
     */
    public void notifyListeners() {
        if (perceptListeners != null) {
            for (PerceptListener l : perceptListeners) {
                l.perceptionChanged("perception changed");
            }
        }
    }

    /**
     * Notify the listents that a particular agent's perceptions have changed.
     * 
     * @param s the name of the agent whose perceptions have changed.
     */
    public void notifyListeners(String s) {
        if (perceptListeners != null) {
            for (PerceptListener l : perceptListeners) {
                l.perceptionChanged(s);
            }
        }
    }

    public void setOutputLevel(int i) {
        outputlevel = i;
    }

    public int getOutputLevel() {
        return outputlevel;
    }

    public void cleanup() {
    }

    public void init() {
    }

    public void configure(Map<String, String> configuration) {
        if (configuration.containsKey("outputlevel")) {
            setOutputLevel(Integer.valueOf(configuration.get("outputlevel")));
        }
    }
}
