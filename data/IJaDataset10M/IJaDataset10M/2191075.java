package c4jason;

import jason.JasonException;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.asSyntax.Trigger.*;
import jason.architecture.*;
import jason.runtime.*;
import java.util.*;
import java.util.concurrent.*;
import jason.mas2j.ClassParameters;
import cartago.*;
import cartago.events.*;
import java.util.logging.Logger;

public class CAgentArch extends AgArch implements cartago.ICartagoListener {

    private ICartagoSession envSession;

    private ConcurrentHashMap<Long, PendingAction> pendingActions;

    private JavaLibrary lib;

    private Logger logger;

    private boolean firstManualFetched;

    public CAgentArch() {
        super();
        pendingActions = new ConcurrentHashMap<Long, PendingAction>();
        logger = Logger.getLogger("CAgentArch");
        lib = new JavaLibrary();
        logger.setLevel(java.util.logging.Level.WARNING);
        firstManualFetched = false;
    }

    /**
	 * Creates the agent class defined by <i>agClass</i>, default is
	 * jason.asSemantics.Agent. The agent class will parse the source code,
	 * create the transition system (TS), ...
	 */
    public void initAg(String agClass, ClassParameters bbPars, String asSrc, Settings stts) throws JasonException {
        super.initAg(agClass, bbPars, asSrc, stts);
        String agentName = getTS().getUserAgArch().getAgName();
        try {
            envSession = CartagoEnvironment.getInstance().startSession(agentName, this);
            setCurrentWsp(envSession.getCurrentWorkspace());
            loadBasicPlans();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.warning("[CARTAGO] WARNING: No default workspace found for " + agentName);
        }
    }

    private void loadBasicPlans() {
        String sensePlan1 = "@sense_plan1[atomic]				\n" + "+!sense(P) : P.					\n";
        String sensePlan2 = "@sense_plan2[atomic]				\n" + "+!sense(P) : not P	<- .wait({+P}).	\n";
        try {
            getTS().getAg().getPL().add(ASSyntax.parsePlan(sensePlan1));
            getTS().getAg().getPL().add(ASSyntax.parsePlan(sensePlan2));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void act(ActionExec actionExec, List<ActionExec> feedback) {
        logger.info("NEW ACTION TODO: " + actionExec + " agent: " + this.getAgName());
        Structure action = actionExec.getActionTerm();
        ArtifactId aid = null;
        WorkspaceId wspId = null;
        String artName = null;
        try {
            boolean failed = false;
            ListTerm lt = action.getAnnots();
            if (lt != null) {
                Iterator<Term> it = lt.iterator();
                while (it.hasNext()) {
                    Term annot = it.next();
                    if (annot.isStructure()) {
                        Structure st = (Structure) annot;
                        if (st.getFunctor().equals("artifact_name")) {
                            artName = ((StringTerm) (st.getTerm(0))).getString();
                        } else if (st.getFunctor().equals("artifact_id")) {
                            Object obj = getObject(st.getTerm(0));
                            if (obj != null && obj instanceof ArtifactId) {
                                aid = (ArtifactId) obj;
                            }
                        } else if (st.getFunctor().equals("wsp_id")) {
                            Object obj = getObject(st.getTerm(0));
                            if (obj != null && obj instanceof WorkspaceId) {
                                wspId = (WorkspaceId) obj;
                            }
                        } else {
                            logger.warning("Use failed: unknown annotatioon " + annot);
                            Literal reason = Literal.parseLiteral("action_failed(" + action + ",unknown_annotation)");
                            String msg = "Use  error - unknown annotation " + annot;
                            notifyActionFailure(actionExec, reason, msg);
                            failed = true;
                            break;
                        }
                    }
                }
            }
            if (!failed) {
                Op op = parseOp(action);
                IAlignmentTest test = null;
                long timeout = Long.MAX_VALUE;
                long actId;
                if (aid != null) {
                    actId = envSession.doAction(aid, op, test, timeout);
                } else if (wspId == null) {
                    if (artName != null) {
                        actId = envSession.doAction(artName, op, test, timeout);
                    } else {
                        actId = envSession.doAction(op, test, timeout);
                    }
                } else {
                    if (artName != null) {
                        actId = envSession.doAction(wspId, artName, op, test, timeout);
                    } else {
                        actId = envSession.doAction(wspId, op, test, timeout);
                    }
                }
                PendingAction pa = new PendingAction(actId, action, (ActionExec) actionExec);
                pendingActions.put(actId, pa);
            }
        } catch (Exception ex) {
            logger.warning("Op " + action + " on artifact " + aid + " by " + this.getAgName() + " failed - op: " + action);
            Literal reason = Literal.parseLiteral("action_failed(" + action + ",generic_error)");
            String msg = "Action failed: " + actionExec.getActionTerm();
            notifyActionFailure(actionExec, reason, msg);
        }
    }

    public List<Literal> perceive() {
        Agent a = getTS().getAg();
        try {
            CartagoEvent evt = envSession.fetchNextPercept();
            logger.info("Processing event: " + evt);
            if (evt != null) {
                Literal l = null;
                if (evt instanceof ActionSucceededEvent) {
                    ActionSucceededEvent ev = (ActionSucceededEvent) evt;
                    PendingAction action = pendingActions.get(ev.getActionId());
                    logger.info("Processing action succeeded: " + action.getAction());
                    if (action != null) {
                        Op op = ev.getOp();
                        notifyActionSuccess(op, action.getAction(), action.getActionExec());
                        if (ev instanceof FocusSucceededEvent) {
                            FocusSucceededEvent ev1 = (FocusSucceededEvent) ev;
                            addObsPropertiesBel(ev1.getTargetArtifact(), ev1.getObsProperties());
                        } else if (ev instanceof StopFocusSucceededEvent) {
                            StopFocusSucceededEvent ev1 = (StopFocusSucceededEvent) ev;
                            removeObsPropertiesBel(ev1.getTargetArtifact(), ev1.getObsProperties());
                        } else if (ev instanceof JoinWSPSucceededEvent) {
                            JoinWSPSucceededEvent ev1 = (JoinWSPSucceededEvent) ev;
                            this.setCurrentWsp(ev1.getWorkspaceId());
                        }
                    }
                } else if (evt instanceof ActionFailedEvent) {
                    ActionFailedEvent ev = (ActionFailedEvent) evt;
                    PendingAction action = pendingActions.get(ev.getActionId());
                    if (action != null) {
                        try {
                            Literal reason = null;
                            Tuple failureInfo = ev.getFailureDescr();
                            try {
                                if (failureInfo != null) {
                                    reason = TermConversionUtils.tupleToLiteral(failureInfo, lib);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            notifyActionFailure(action.getActionExec(), reason, ev.getFailureMsg());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                } else if (evt instanceof ArtifactObsEvent) {
                    ArtifactObsEvent ev = (ArtifactObsEvent) evt;
                    Tuple signal = ev.getSignal();
                    if (signal != null) {
                        l = TermConversionUtils.obsEventToLiteral(ev, lib);
                        Trigger te = new Trigger(TEOperator.add, TEType.belief, l);
                        getTS().updateEvents(new Event(te, Intention.EmptyInt));
                    }
                    ArtifactObsProperty[] props = ev.getChangedProperties();
                    if (props != null) {
                        for (ArtifactObsProperty prop : props) {
                            String propName = prop.getName();
                            String propId = prop.getFullId();
                            logger.info("prop changed " + propName + " " + propId);
                            try {
                                Iterator<Literal> it = a.getBB().getCandidateBeliefs(new PredicateIndicator(propName, prop.getValues().length));
                                boolean found = false;
                                Literal lold = null;
                                ListTerm annots = null;
                                while (!found && it.hasNext()) {
                                    lold = it.next();
                                    annots = lold.getAnnots("obs_prop_id");
                                    if (annots.size() > 0) {
                                        for (Term annot : annots) {
                                            StringTerm st = (StringTerm) ((((Structure) annot).getTerm(0)));
                                            if (st.getString().equals(propId)) {
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (found) {
                                            break;
                                        }
                                    }
                                }
                                if (found) {
                                    if (annots.size() == 1) {
                                        if (!a.getBB().remove(lold)) {
                                            logger.warning("obs prop not found during bel update: " + propName + " " + ev.getArtifactId().getName());
                                        }
                                    } else {
                                        ArtifactId source = ev.getArtifactId();
                                        Iterator<Term> it2 = lold.getAnnots().iterator();
                                        while (it2.hasNext()) {
                                            Term t = it2.next();
                                            if (t.isStructure()) {
                                                Structure st = (Structure) t;
                                                if (st.getArity() > 1) {
                                                    Object artId = TermConversionUtils.termToObject(st.getTerm(0), lib);
                                                    if (artId != null && artId.equals(source)) {
                                                        it2.remove();
                                                    }
                                                } else if (st.getFunctor().equals("obs_prop_id")) {
                                                    StringTerm sst = (StringTerm) ((((Structure) st).getTerm(0)));
                                                    if (sst.getString().equals(propId)) {
                                                        it2.remove();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    logger.warning("!! obs prop not found during bel update: " + propName + " " + ev.getArtifactId().getName() + " " + annots);
                                }
                                l = TermConversionUtils.obsPropToLiteral(propId, ev.getArtifactId(), propName, prop.getValues(), lib);
                                l.forceFullLiteralImpl();
                                if (a.getBB().add(l)) {
                                    logger.info("AGENT: " + getTS().getUserAgArch().getAgName() + " UPDATED BELIEF: " + l);
                                    Literal l1 = l.copy().forceFullLiteralImpl();
                                    Trigger te = new Trigger(TEOperator.add, TEType.belief, l1);
                                    getTS().updateEvents(new Event(te, Intention.EmptyInt));
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                logger.warning("EXCEPTION - processing update obs prop " + ev + " for agent " + getTS().getUserAgArch().getAgName());
                            }
                        }
                    }
                    props = ev.getAddedProperties();
                    if (props != null) {
                        for (ArtifactObsProperty prop : props) {
                            String propName = prop.getName();
                            String propId = prop.getFullId();
                            try {
                                Iterator<Literal> it = a.getBB().getCandidateBeliefs(new PredicateIndicator(propName, prop.getValues().length));
                                boolean found = false;
                                if (it != null) {
                                    Literal lold = null;
                                    while (!found && it.hasNext()) {
                                        lold = it.next();
                                        ListTerm annots = lold.getAnnots("obs_prop_id");
                                        if (annots.size() > 0) {
                                            for (Term annot : annots) {
                                                StringTerm st = (StringTerm) ((((Structure) annot).getTerm(0)));
                                                if (st.getString().equals(propId)) {
                                                    found = true;
                                                    break;
                                                }
                                            }
                                            if (found) {
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (!found) {
                                    l = TermConversionUtils.obsPropToLiteral(propId, ev.getArtifactId(), propName, prop.getValues(), lib);
                                    if (a.getBB().add(l)) {
                                        Literal l1 = l.copy().forceFullLiteralImpl();
                                        Trigger te = new Trigger(TEOperator.add, TEType.belief, l1);
                                        getTS().updateEvents(new Event(te, Intention.EmptyInt));
                                        logger.info("AGENT: " + getTS().getUserAgArch().getAgName() + " NEW BELIEF FOR OBS PROP: " + l1);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                logger.warning("EXCEPTION - processing event " + ev + " for agent " + getTS().getUserAgArch().getAgName());
                            }
                        }
                    }
                    props = ev.getRemovedProperties();
                    if (props != null) {
                        for (ArtifactObsProperty prop : props) {
                            String propName = prop.getName();
                            String propId = prop.getFullId();
                            Iterator<Literal> it = a.getBB().getCandidateBeliefs(new PredicateIndicator(propName, prop.getValues().length));
                            boolean found = false;
                            ListTerm annots = null;
                            Literal toBeRemoved = null;
                            if (it != null) {
                                while (!found && it.hasNext()) {
                                    toBeRemoved = it.next();
                                    annots = toBeRemoved.getAnnots("obs_prop_id");
                                    if (annots.size() > 0) {
                                        for (Term annot : annots) {
                                            StringTerm st = (StringTerm) ((((Structure) annot).getTerm(0)));
                                            if (st.getString().equals(propId)) {
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (found) {
                                            break;
                                        }
                                    }
                                }
                            }
                            if (found) {
                                try {
                                    boolean removed = true;
                                    if (annots.size() == 1) {
                                        removed = a.getBB().remove(toBeRemoved);
                                    } else {
                                        ArtifactId source = ev.getArtifactId();
                                        Iterator<Term> it2 = toBeRemoved.getAnnots().iterator();
                                        while (it2.hasNext()) {
                                            Term t = it2.next();
                                            if (t.isStructure()) {
                                                Structure st = (Structure) t;
                                                if (st.getArity() > 1) {
                                                    Object artId = TermConversionUtils.termToObject(((Structure) st).getTerm(0), lib);
                                                    if (artId != null && artId.equals(source)) {
                                                        it2.remove();
                                                    }
                                                } else if (st.getFunctor().equals("obs_prop_id")) {
                                                    StringTerm sst = (StringTerm) ((((Structure) st).getTerm(0)));
                                                    if (sst.getString().equals(propId)) {
                                                        it2.remove();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (removed) {
                                        Literal l1 = toBeRemoved.copy().forceFullLiteralImpl();
                                        Trigger te = new Trigger(TEOperator.del, TEType.belief, l1);
                                        getTS().updateEvents(new Event(te, Intention.EmptyInt));
                                        logger.info("AGENT: " + getTS().getUserAgArch().getAgName() + " REMOVED BELIEF FOR OBS PROP: " + l1);
                                    } else {
                                        logger.warning("AGENT: " + getTS().getUserAgArch().getAgName() + " OBS PROP NOT FOUND when removing: " + propName);
                                    }
                                } catch (Exception ex) {
                                    logger.warning("EXCEPTION - processing remove obs prop " + ev + " for agent " + getTS().getUserAgArch().getAgName());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.severe("Exception in fetching events from the context.");
        }
        return null;
    }

    private void addObsPropertiesBel(ArtifactId source, List<ArtifactObsProperty> props) {
        Literal l = null;
        Agent a = getTS().getAg();
        for (ArtifactObsProperty p : props) {
            String propName = p.getName();
            String propId = p.getFullId();
            try {
                Iterator<Literal> it = a.getBB().getCandidateBeliefs(new PredicateIndicator(propName, p.getValues().length));
                boolean found = false;
                if (it != null) {
                    Literal lold = null;
                    while (!found && it.hasNext()) {
                        lold = it.next();
                        ListTerm annots = lold.getAnnots("obs_prop_id");
                        if (annots.size() > 0) {
                            for (Term annot : annots) {
                                StringTerm st = (StringTerm) ((((Structure) annot).getTerm(0)));
                                if (st.getString().equals(propId)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                break;
                            }
                        }
                    }
                }
                if (!found) {
                    l = TermConversionUtils.obsPropToLiteral(propId, source, p.getName(), p.getValues(), lib);
                    if (a.getBB().add(l)) {
                        Literal l1 = l.copy().forceFullLiteralImpl();
                        Trigger te = new Trigger(TEOperator.add, TEType.belief, l1);
                        getTS().updateEvents(new Event(te, Intention.EmptyInt));
                        logger.info("AGENT: " + getTS().getUserAgArch().getAgName() + " NEW BELIEF FOR OBS PROP: " + l1);
                    }
                }
            } catch (Exception ex) {
                logger.warning("EXCEPTION - processing new obs prop " + p + " artifact " + source + " for agent " + getTS().getUserAgArch().getAgName());
            }
        }
    }

    private void removeObsPropertiesBel(ArtifactId source, List<ArtifactObsProperty> props) {
        Agent a = getTS().getAg();
        for (ArtifactObsProperty p : props) {
            String propName = p.getName();
            String propId = p.getFullId();
            try {
                Iterator<Literal> it = a.getBB().getCandidateBeliefs(new PredicateIndicator(propName, p.getValues().length));
                Literal toBeRemoved = null;
                ListTerm annots = null;
                if (it != null) {
                    Literal lold = null;
                    while (toBeRemoved == null && it.hasNext()) {
                        lold = it.next();
                        annots = lold.getAnnots("obs_prop_id");
                        if (annots.size() > 0) {
                            for (Term annot : annots) {
                                StringTerm st = (StringTerm) ((((Structure) annot).getTerm(0)));
                                if (st.getString().equals(propId)) {
                                    toBeRemoved = lold;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (toBeRemoved != null) {
                    boolean removed = true;
                    if (annots.size() == 1) {
                        removed = a.getBB().remove(toBeRemoved);
                    } else {
                        Iterator<Term> it2 = toBeRemoved.getAnnots().iterator();
                        while (it2.hasNext()) {
                            Term t = it2.next();
                            if (t.isStructure()) {
                                Structure st = (Structure) t;
                                if (st.getArity() > 1) {
                                    Object artId = TermConversionUtils.termToObject(((Structure) st).getTerm(0), lib);
                                    if (artId != null && artId.equals(source)) {
                                        it2.remove();
                                    }
                                } else if (st.getFunctor().equals("obs_prop_id")) {
                                    StringTerm sst = (StringTerm) ((((Structure) st).getTerm(0)));
                                    if (sst.getString().equals(propId)) {
                                        it2.remove();
                                    }
                                }
                            }
                        }
                    }
                    if (removed) {
                        Literal l1 = toBeRemoved.copy().forceFullLiteralImpl();
                        Trigger te = new Trigger(TEOperator.del, TEType.belief, l1);
                        getTS().updateEvents(new Event(te, Intention.EmptyInt));
                        logger.info("AGENT: " + getTS().getUserAgArch().getAgName() + " REMOVED BELIEF FOR OBS PROP: " + l1);
                    }
                }
            } catch (Exception ex) {
                logger.warning("EXCEPTION - processing remove obs prop " + p + " for agent " + getTS().getUserAgArch().getAgName());
            }
        }
    }

    private Op parseOp(Structure action) {
        Term[] terms = action.getTermsArray();
        Object[] opArgs = new Object[terms.length];
        for (int i = 0; i < terms.length; i++) {
            opArgs[i] = TermConversionUtils.termToObject(terms[i], lib);
        }
        Op op = new Op(action.getFunctor(), opArgs);
        return op;
    }

    private boolean bind(Object obj, Term term, ActionExec act) {
        Unifier un = act.getIntention().peek().getUnif();
        try {
            Term t = TermConversionUtils.objectToTerm(obj, lib);
            return un.unifies(t, term);
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean bind(Term t1, Term t2, ActionExec act) {
        Unifier un = act.getIntention().peek().getUnif();
        try {
            return un.unifies(t1, t2);
        } catch (Exception ex) {
            return false;
        }
    }

    private Object getObject(Term t) {
        return TermConversionUtils.termToObject(t, lib);
    }

    public JavaLibrary getJavaLib() {
        return lib;
    }

    public void setCurrentWsp(WorkspaceId id) throws CartagoException {
        try {
            envSession.setCurrentWorkspace(id);
            Agent a = getTS().getAg();
            Iterator<Literal> it = a.getBB().getCandidateBeliefs(new PredicateIndicator("current_wsp", 3));
            if (it != null) {
                a.getBB().remove(it.next());
            }
            Literal struct = ASSyntax.createLiteral("current_wsp");
            struct.addTerm(TermConversionUtils.objectToTerm(id, lib));
            struct.addTerm(ASSyntax.createString(id.getName()));
            struct.addTerm(ASSyntax.createString(id.getNodeId().getId()));
            if (a.getBB().add(struct)) {
                Trigger te = new Trigger(TEOperator.add, TEType.belief, struct);
                getTS().updateEvents(new Event(te, Intention.EmptyInt));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CartagoException();
        }
    }

    public WorkspaceId getCurrentWsp() throws CartagoException {
        return envSession.getCurrentWorkspace();
    }

    private void notifyActionSuccess(Op op, Structure action, ActionExec actionExec) {
        Object[] values = op.getParamValues();
        for (int i = 0; i < action.getArity(); i++) {
            if (!action.getTerm(i).isGround()) {
                try {
                    boolean bound = bind(values[i], action.getTerm(i), actionExec);
                    if (!bound) {
                        actionExec.setResult(false);
                        Literal reason = Literal.parseLiteral("bind_param_error(" + action.getTerm(i) + "," + values[i]);
                        actionExec.setFailureReason(reason, "Error binding parameters: " + action.getTerm(i) + " with " + values[i]);
                        getTS().getC().addFeedbackAction(actionExec);
                        getArchInfraTier().wake();
                        return;
                    }
                } catch (Exception ex) {
                    actionExec.setResult(false);
                    Literal reason = Literal.parseLiteral("bind_param_error(" + action.getTerm(i) + "," + values[i]);
                    actionExec.setFailureReason(reason, "Error binding parameters: " + action.getTerm(i) + " with " + values[i]);
                    getTS().getC().addFeedbackAction(actionExec);
                    getArchInfraTier().wake();
                    return;
                }
            }
        }
        actionExec.setResult(true);
        getTS().getC().addFeedbackAction(actionExec);
        getArchInfraTier().wake();
    }

    private void notifyActionFailure(ActionExec actionExec, Literal descr, String msg) {
        actionExec.setResult(false);
        actionExec.setFailureReason(descr, msg);
        getTS().getC().addFeedbackAction(actionExec);
        getArchInfraTier().wake();
    }

    public boolean notifyCartagoEvent(CartagoEvent ev) {
        getArchInfraTier().wake();
        return true;
    }
}
