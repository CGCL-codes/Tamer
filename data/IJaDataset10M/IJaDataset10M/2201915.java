package org.mitre.dm.qud;

import org.mitre.midiki.logic.*;
import org.mitre.midiki.agent.*;
import org.mitre.midiki.state.*;
import org.mitre.midiki.impl.mitre.*;
import org.mitre.dm.*;
import org.mitre.dm.qud.conditions.*;
import org.mitre.dm.qud.rules.*;
import java.util.*;
import java.util.logging.*;
import java.io.IOException;

/**
 * Implements the dialogue move engine.
 * 
 */
public class DmeAgent implements Agent {

    private static Logger logger = Logger.getLogger("org.mitre.dm.qud.DmeAgent");

    private Logger discourseLogger = null;

    DialogueSystem dialogueSystem = null;

    Result consequence;

    public void attachTo(DialogueSystem system) {
        dialogueSystem = system;
        discourseLogger = system.getLogger();
    }

    /**
     * Performs one-time <code>Agent</code> initialization.
     * This processing is likely to include initialization
     * of the <code>Contract</code>s and <code>Cell</code>s
     * for the <code>Agent</code>, as well as non-Midiki
     * initialization.
     *
     */
    public void init(Object config) {
        discourseLogger.setLevel(Level.INFO);
        consequence = new Result();
        consequence.setDiscourseLogger(discourseLogger);
        accommodate = new Accommodate(discourseLogger);
        downdateAgenda = new DowndateAgenda(discourseLogger);
        downdateQUD = new DowndateQUD(discourseLogger);
        grounding = new Grounding(discourseLogger);
        integrate = new Integrate(discourseLogger);
        integrate.init(config);
        managePlan = new ManagePlan(discourseLogger);
        refillAgenda = new RefillAgenda(discourseLogger);
        removeAccomplishedTask = new RemoveAccomplishedTask();
        removeAccomplishedTask.setLogger(discourseLogger);
        store = Store.newInstance();
        store.setLogger(discourseLogger);
        storeNIM = StoreNIM.newInstance(new Variable("NIMove"));
        storeNIM.setLogger(discourseLogger);
    }

    /**
     * Release any resources which were created in <code>init</code>.
     * Following this routine, the agent will be terminated.
     *
     */
    public void destroy() {
    }

    Accommodate accommodate;

    DowndateAgenda downdateAgenda;

    DowndateQUD downdateQUD;

    Grounding grounding;

    Integrate integrate;

    ManagePlan managePlan;

    RefillAgenda refillAgenda;

    RemoveAccomplishedTask removeAccomplishedTask;

    Store store;

    StoreNIM storeNIM;

    private boolean agendaIsEmpty(InfoState infoState) {
        Object agenda = infoState.cell("is").cell("private").get("agenda");
        logger.logp(Level.FINER, "org.mitre.dm.qud.DmeAgent", "agendaIsEmpty", "is.private.agenda", agenda);
        if (((agenda instanceof List) && (((List) agenda).isEmpty())) || (agenda == null)) {
            logger.logp(Level.FINER, "org.mitre.dm.qud.DmeAgent", "agendaIsEmpty", "is.private.agenda isEmpty");
            return true;
        }
        logger.logp(Level.FINER, "org.mitre.dm.qud.DmeAgent", "agendaIsEmpty", "is.private.agenda isNotEmpty");
        return false;
    }

    private long updateStarted;

    private static long MAX_UPDATE_TIME = 5000L;

    private void markUpdateStarted() {
        updateStarted = System.currentTimeMillis();
    }

    private boolean canContinue() {
        return (System.currentTimeMillis() - updateStarted) < MAX_UPDATE_TIME;
    }

    /**
     * Contains the update algorithm.
     *
     * @param infoState an <code>InfoState</code> value
     */
    private void update(InfoState infoState) {
        Bindings bindings = new BindingsImpl();
        markUpdateStarted();
        Object latest_moves = infoState.cell("is").get("latest_moves");
        Integer turn_number = (Integer) infoState.cell("is").get("turn_number");
        String turn_taker = (String) infoState.cell("is").get("turn_taker");
        grounding.evaluate(infoState);
        Object latest_speaker = infoState.cell("is").get("latest_speaker");
        if (infoState.getUnifier().matchTerms(latest_speaker, "sys", bindings)) {
            integrate.evaluate(infoState);
            while (canContinue() && downdateAgenda.evaluate(infoState)) ;
            if (agendaIsEmpty(infoState)) {
                while (canContinue() && managePlan.evaluate(infoState)) ;
            }
            ;
            while (canContinue() && refillAgenda.evaluate(infoState)) ;
            store.evaluate(infoState);
        } else {
            discourseLogger.logp(Level.INFO, "org.mitre.dm.qud.DmeAgent", "update", "user moves", latest_moves);
            boolean success = true;
            while (canContinue() && success) {
                success = false;
                success = success || integrate.evaluate(infoState);
                success = success || accommodate.evaluate(infoState);
                success = success || managePlan.findPlan.evaluate(infoState);
                if (agendaIsEmpty(infoState)) {
                    success = success || managePlan.evaluate(infoState);
                } else {
                    success = success || downdateAgenda.evaluate(infoState);
                }
            }
            while (canContinue() && downdateAgenda.evaluate(infoState)) ;
            if (agendaIsEmpty(infoState)) {
                while (canContinue() && managePlan.evaluate(infoState)) ;
            }
            while (canContinue() && refillAgenda.evaluate(infoState)) ;
            while (canContinue() && storeNIM.evaluate(infoState)) ;
            downdateQUD.evaluate(infoState);
            discourseLogger.logp(Level.INFO, "org.mitre.dm.qud.DmeAgent", "update", "end turn " + turn_number.intValue() + " (usr)");
            turn_number = new Integer(turn_number.intValue() + 1);
            turn_taker = "sys";
            infoState.cell("is").put("turn_number", turn_number);
            infoState.cell("is").put("turn_taker", turn_taker);
            discourseLogger.logp(Level.INFO, "org.mitre.dm.qud.DmeAgent", "update", "start turn " + turn_number.intValue() + " (sys)");
        }
        ;
        if (!canContinue()) {
            System.out.println("*****\n\nUpdate timed out.\n\n*****");
            discourseLogger.logp(Level.WARNING, "org.mitre.dm.qud.DmeAgent", "update", "update failure: timeout");
        }
    }

    /**
     * Connects the <code>Agent</code> to the provided information state.
     * In this routine, the <code>Agent</code> should register any
     * <code>Rule</code>s necessary for normal operation, and perform
     * any other <code>InfoState</code>-specific processing. 
     *
     * @param infoState a compatible information state
     * @return <code>true</code> if connection succeeded
     */
    public boolean connect(InfoState infoState) {
        final Variable topPlan = new Variable("TopPlan");
        final Select select = new Select(discourseLogger);
        Condition resetC = new Condition();
        resetC.extend(new ProgramStateIs("starting"));
        resetC.extend(new HavePlan("top", topPlan));
        ExistsRule reset = new ExistsRule(resetC) {

            public boolean execute(InfoState infoState, Bindings bindings) {
                consequence.instantiatePlan(topPlan, infoState, bindings);
                infoState.cell("output").put("output_index", new Integer(0));
                infoState.cell("is").put("turn_number", new Integer(1));
                infoState.cell("is").put("turn_taker", "sys");
                discourseLogger.logp(Level.INFO, "org.mitre.dm.qud.DmeAgent", "execute", "start turn 1 (sys)");
                consequence.pushAgenda("greet", infoState, bindings);
                infoState.cell("is").put("program_state", "running");
                select.evaluate(infoState);
                Integer turn_number = (Integer) infoState.cell("is").get("turn_number");
                infoState.cell("is").put("turn_for_moves", turn_number);
                Object next_moves = infoState.cell("is").get("next_moves");
                discourseLogger.logp(Level.INFO, "org.mitre.dm.qud.DmeAgent", "execute", "system moves", next_moves);
                return true;
            }
        };
        infoState.cell("is").addInfoListener("program_state", new RuleBasedInfoListener(reset) {
        });
        Condition movesC = new Condition();
        movesC.extend(new LatestMovesAreNotEmpty());
        ExistsRule moves = new ExistsRule(movesC) {

            public boolean execute(InfoState infoState, Bindings bindings) {
                update(infoState);
                select.evaluate(infoState);
                Integer turn_number = (Integer) infoState.cell("is").get("turn_number");
                infoState.cell("is").put("turn_for_moves", turn_number);
                Object next_moves = infoState.cell("is").get("next_moves");
                Object current_moves = infoState.cell("is").get("moves_for_turn");
                if (current_moves == null) {
                    current_moves = new LinkedList();
                }
                if (((List) next_moves).isEmpty()) {
                    infoState.cell("is").put("moves_for_last_turn", current_moves);
                    infoState.cell("is").put("moves_for_turn", new LinkedList());
                    String turn_taker = (String) infoState.cell("is").get("turn_taker");
                    discourseLogger.logp(Level.INFO, "org.mitre.dm.qud.DmeAgent", "execute", "end decisions for turn " + turn_number.intValue() + " (sys)");
                    turn_number = new Integer(turn_number.intValue() + 1);
                    turn_taker = "usr";
                    Object program_state = infoState.cell("is").get("program_state");
                    if (!program_state.equals("quitting")) {
                        infoState.cell("is").put("turn_number", turn_number);
                        infoState.cell("is").put("turn_taker", turn_taker);
                        discourseLogger.logp(Level.INFO, "org.mitre.dm.qud.DmeAgent", "execute", "start turn " + turn_number.intValue() + " (usr)");
                    }
                } else {
                    discourseLogger.logp(Level.INFO, "org.mitre.dm.qud.DmeAgent", "execute", "system moves", next_moves);
                    ((LinkedList) current_moves).addAll((List) next_moves);
                    infoState.cell("is").put("moves_for_turn", current_moves);
                }
                return true;
            }
        };
        infoState.cell("is").addInfoListener("latest_moves", new RuleBasedInfoListener(moves) {
        });
        return true;
    }

    /**
     * Disconnects the <code>Agent</code> from the information state.
     * After this call, the <code>InfoState</code> is assumed to be
     * invalid, and no further processing should be performed
     * until another call to <code>connect</code>.
     * (The API does not require that all implementations
     * of <code>Agent</code> be able to <code>connect</code>
     * again following <code>disconnect</code>.)
     *
     */
    public void disconnect() {
    }

    /**
     * Get the value for the specified Midiki system property.
     *
     * @param key an <code>Object</code> value
     * @return an <code>Object</code> value
     */
    public Object getProperty(Object key) {
        return null;
    }

    /**
     * Set the value for the specified Midiki system property.
     *
     * @param key an <code>Object</code> value
     * @param value an <code>Object</code> value
     * @return previous value for the property
     */
    public Object putProperty(Object key, Object value) {
        return null;
    }

    /**
     * Get the system identifier for this <code>Agent</code>.
     * This is the name by which it is known to the system
     * as a whole, and should be unique.
     *
     * @return a <code>String</code> value
     */
    public String getId() {
        return getName();
    }

    /**
     * Get the name that this <code>Agent</code> calls itself.
     * A Midiki system might have several <code>Agent</code>s
     * with the same name, but each will have a unique id.
     *
     * @return a <code>String</code> value
     */
    public String getName() {
        return "dme_agent";
    }

    /**
     * Get the set of <code>Contract</code>s this <code>Agent</code>
     * must find in its <code>InfoState</code>. There can be more,
     * but these must be there.
     *
     * @return a <code>Set</code> value
     */
    public Set getRequiredContracts() {
        Set contractSet = new HashSet();
        contractSet.add(ContractDatabase.find("is"));
        contractSet.add(ContractDatabase.find("shared"));
        contractSet.add(ContractDatabase.find("private"));
        contractSet.add(ContractDatabase.find("lu"));
        contractSet.add(ContractDatabase.find("domain"));
        return contractSet;
    }

    /**
     * Get the <code>Set</code> of <code>Cell</code>s that this
     * <code>Agent</code> can provide to the <code>InfoState</code>.
     * The actual <code>InfoState</code> must include these.
     *
     * @return a <code>Set</code> value
     */
    public Set getProvidedCells() {
        return null;
    }
}
