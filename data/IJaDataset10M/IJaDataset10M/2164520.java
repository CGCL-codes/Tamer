package com.agentfactory.astr.interpreter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import com.agentfactory.clf.interpreter.AbstractLanguageAgent;
import com.agentfactory.clf.interpreter.Action;
import com.agentfactory.clf.interpreter.CoreUtilities;
import com.agentfactory.clf.interpreter.Module;
import com.agentfactory.clf.interpreter.Sensor;
import com.agentfactory.clf.lang.IFormula;
import com.agentfactory.clf.lang.IMPLIES;
import com.agentfactory.clf.lang.ITerm;
import com.agentfactory.clf.lang.NOT;
import com.agentfactory.clf.lang.Predicate;
import com.agentfactory.clf.plans.IPlanStep;
import com.agentfactory.clf.reasoner.Bindings;
import com.agentfactory.clf.reasoner.IQueryEngine;
import com.agentfactory.clf.reasoner.IQueryable;
import com.agentfactory.clf.reasoner.QueryResult;
import com.agentfactory.clf.reasoner.ResolutionBasedQueryEngine;
import com.agentfactory.clf.util.LogicException;
import com.agentfactory.platform.core.IMessage;
import com.agentfactory.platform.util.FIPAHelper;
import com.agentfactory.service.migration.MigrationService;

public class AbstractASTRAgent extends AbstractLanguageAgent implements IQueryable {

    /*********************************************************************
	 * Core Interpreter Data Structures
	 *********************************************************************/
    protected List<ASEvent> eventQueue;

    protected List<IMPLIES> inferences;

    protected BeliefManager beliefManager;

    protected IQueryEngine queryEngine;

    protected List<Intention> intentions;

    protected Map<String, List<PlanRule>> ruleEventMap;

    protected Set<String> eventFilter;

    protected Map<String, PartialPlan> partialPlanMap;

    protected Map<String, ASTRProgram> programs;

    protected List<Predicate> activePrograms;

    protected Map<String, List<String>> programStacks;

    protected Map<String, TeleoReactiveExecutor> trExecutors;

    protected List<ActionTraceLogEntry> actionTrace;

    /*********************************************************************
	 * Current State of Algorithm (i.e. what was selected in the loop
	 *********************************************************************/
    private ASEvent event;

    private List<PlanRule> options;

    private PlanRule option;

    private int indentionIndex;

    public AbstractASTRAgent(String name) {
        super(name);
        eventQueue = new LinkedList<ASEvent>();
        partialPlanMap = new HashMap<String, PartialPlan>();
        beliefManager = new BeliefManager();
        inferences = new LinkedList<IMPLIES>();
        queryEngine = new ResolutionBasedQueryEngine();
        queryEngine.addSource(this);
        queryEngine.addSource(beliefManager);
        eventFilter = new HashSet<String>();
        indentionIndex = -1;
        ruleEventMap = new HashMap<String, List<PlanRule>>();
        intentions = new LinkedList<Intention>();
        programs = new HashMap<String, ASTRProgram>();
        programStacks = new HashMap<String, List<String>>();
        actionTrace = new LinkedList<ActionTraceLogEntry>();
        activePrograms = new LinkedList<Predicate>();
        trExecutors = new HashMap<String, TeleoReactiveExecutor>();
        installBasicAPI();
        addAction("tr.start(?name)", new Action() {

            @Override
            public boolean execute(Predicate activity) {
                Predicate fn = Utilities.factory.convertToPredicate(activity.termAt(0));
                if (activePrograms.contains(fn)) {
                    return true;
                }
                ASTRProgram prog = getProgram(fn);
                if (prog == null) {
                    System.out.println("WARNING: Attempt to execute non-existant TR function: " + termAsString(activity, 0));
                    addBelief("tr.unknownFunction(" + termAsString(activity, 0) + ")");
                    return false;
                }
                activePrograms.add(fn);
                programStacks.put(CoreUtilities.createSignature(fn), new LinkedList<String>());
                return true;
            }
        });
        addAction("tr.stop(?name)", new Action() {

            @Override
            public boolean execute(Predicate activity) {
                Predicate fn = Utilities.factory.convertToPredicate(activity.termAt(0));
                if (activePrograms.contains(fn)) {
                    fn = activePrograms.get(activePrograms.indexOf(fn));
                    activePrograms.remove(fn);
                    programStacks.remove(CoreUtilities.createSignature(fn));
                    return true;
                }
                return false;
            }
        });
        addAction(".doAll(?list)", new Action() {

            public boolean execute(Predicate activity) {
                for (ITerm t : activity.termAt(0).terms()) {
                    performActivity(CoreUtilities.factory.convertToPredicate(t));
                }
                return true;
            }
        });
        addSensor("tr", new Sensor() {

            @Override
            public void perceive() {
                for (Predicate program : activePrograms) {
                    addBelief("tr.active(" + CoreUtilities.presenter.toString(program) + ")");
                }
            }
        });
        this.sensorMap.remove(".inbox:0");
    }

    @Override
    public void execute() {
        this.retrieveNewMessages();
        beliefManager.copyAndWipePercepts();
        this.senseEnvironment();
        beliefManager.manage();
        for (ASEvent event : beliefManager.events()) {
            if (eventFilter.contains(event.toSignature())) eventQueue.add(event);
        }
        for (IMessage message : this.getInbox()) {
            this.addEvent(new ASEvent(ASEvent.ADDITION, Utilities.factory.createFormula("message(" + message.getPerformative().toLowerCase() + "," + FIPAHelper.toFOSString(message.getSender()) + "," + message.getContent() + ")")));
        }
        this.getOutbox().clear();
        event = getNextEvent();
        if (migrationState.migrate && migrationState.type == MigrationState.GRACEFULL) {
            while ((event != null) && (!AchievementFormula.class.isInstance(event.content()))) {
                event = getNextEvent();
            }
        }
        if (event != null) {
            options = generateOptions(event);
            option = selectOption(options);
            if (option != null) {
                if (event.getSource() != null) {
                    Intention commitment = (Intention) event.getSource();
                    commitment.addInitialPlan(option.getPlan());
                    commitment.resume();
                } else {
                    intentions.add(new Intention(event, option));
                }
            }
        }
        if (!intentions.isEmpty()) {
            int N = intentions.size();
            for (int i = 0; i < N; i++) {
                Intention intention = selectIntention();
                if (intention != null) {
                    QueryResult res = this.queryAll(intention.getCondition());
                    if (res.found) {
                        IPlanStep planStep = intention.getNextActivity();
                        if (planStep == null) {
                            intentions.remove(intention);
                        } else {
                            planStep.handle(this, intention);
                        }
                    } else {
                        intention.fail();
                    }
                    if (intention.isFailed()) {
                        if (!intention.rollback()) {
                            intentions.remove(intention);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < activePrograms.size(); i++) {
            Predicate activeProgram = activePrograms.get(i);
            String function = CoreUtilities.presenter.toString(activeProgram);
            TeleoReactiveExecutor executor = trExecutors.get(function);
            if (executor == null || !executor.isActive()) {
                List<String> programStack = programStacks.get(CoreUtilities.createSignature(activeProgram));
                programStack.clear();
                List<Predicate> activity = getProgram(activeProgram).executeAll(queryEngine, activeProgram.terms());
                while (programs.containsKey(CoreUtilities.createSignature(activity.get(0)))) {
                    programStack.add(CoreUtilities.presenter.toString(activity.get(0)));
                    LinkedList<ITerm> args = new LinkedList<ITerm>();
                    for (ITerm term : activity.get(0).terms()) {
                        args.add(term);
                    }
                    activity = getProgram(activity.get(0)).executeAll(queryEngine, args);
                }
                String fn = CoreUtilities.presenter.toString(activity.get(0));
                if (CoreUtilities.presenter.toString(activity.get(0)).contains("executeAll(")) {
                    for (Predicate pred : activity) {
                        String iform = CoreUtilities.presenter.toString(pred.termAt(0));
                        if (actionTrace.isEmpty() || (!actionTrace.get(actionTrace.size() - 1).action.equals(iform) && !iform.equals(".nil"))) actionTrace.add(new ActionTraceLogEntry(getIterations(), iform, function));
                    }
                } else {
                    if (actionTrace.isEmpty() || (!actionTrace.get(actionTrace.size() - 1).equals(fn) && !CoreUtilities.presenter.toString(activity.get(0)).equals(".nil"))) actionTrace.add(new ActionTraceLogEntry(getIterations(), fn, function));
                }
                executor = new TeleoReactiveExecutor(activity, function);
                trExecutors.put(function, executor);
                new Thread(executor).start();
                programStacks.get(CoreUtilities.createSignature(activeProgram)).add("---EXECUTING---");
            }
        }
        if (migrationState.migrate) {
            if ((migrationState.type == MigrationState.IMMEDIATE) || (migrationState.type == MigrationState.GRACEFULL && eventQueue.isEmpty() && intentions.isEmpty())) {
                MigrationService ms = (MigrationService) getPlatformService(MigrationService.NAME);
                if (ms == null) {
                    addBelief((Predicate) Utilities.factory.createFormula("migrationFailure(noMigrationService)"));
                } else {
                    ms.sendAgent(this, migrationState.host, migrationState.port);
                }
                migrationState.migrate = false;
            }
        }
        this.endOfIteration();
        event = null;
        option = null;
    }

    private ASTRProgram getProgram(Predicate name) {
        return programs.get(Utilities.createSignature(name));
    }

    class TeleoReactiveExecutor implements Runnable {

        List<Predicate> activity;

        String function;

        boolean active;

        public boolean isActive() {
            return active;
        }

        public TeleoReactiveExecutor(List<Predicate> activity, String function) {
            this.activity = activity;
            this.function = function;
            active = false;
        }

        @Override
        public void run() {
            active = true;
            performActivity(activity, function);
            active = false;
        }
    }

    private void performActivity(List<Predicate> activity, String func) {
        String fn = CoreUtilities.presenter.toString(activity.get(0));
        if (CoreUtilities.presenter.toString(activity.get(0)).contains("executeAll(")) {
            for (Predicate pred : activity) {
                performActivity(CoreUtilities.factory.convertToPredicate(pred.termAt(0)));
            }
        } else {
            performActivity(activity.get(0));
        }
    }

    public void addASTRProgram(ASTRProgram program) {
        programs.put(program.getName(), program);
    }

    public PartialPlan getPartialPlan(Predicate predicate) {
        return partialPlanMap.get(CoreUtilities.createSignature(predicate));
    }

    public Map<String, PartialPlan> getPartialPlans() {
        return partialPlanMap;
    }

    public QueryResult queryAll(IFormula formula) {
        return queryEngine.queryAll(formula);
    }

    @Override
    public void noSuchAction(Predicate activity) {
        String a = CoreUtilities.presenter.toString(activity);
        System.out.println("Unknown Action Call: " + a);
        Predicate predicate = (Predicate) CoreUtilities.factory.createFormula("unknownActivity(" + a + ")[source(percept)]");
        this.addEvent(new ASEvent(ASEvent.ADDITION, predicate));
    }

    public ASEvent getEvent() {
        return event;
    }

    public List<PlanRule> getOptions() {
        return options;
    }

    public PlanRule getSelected() {
        return option;
    }

    public List<Predicate> getBeliefs() {
        return beliefManager.beliefs();
    }

    public List<ASEvent> getEventQueue() {
        return eventQueue;
    }

    public List<Intention> getCommitments() {
        return intentions;
    }

    public List<PlanRule> getRules() {
        List<PlanRule> list = new LinkedList<PlanRule>();
        for (List<PlanRule> rules : ruleEventMap.values()) {
            list.addAll(rules);
        }
        return list;
    }

    public List<String> getActionStrings() {
        List<String> list = new LinkedList<String>();
        for (Action action : actionMap.values()) {
            list.add(Utilities.presenter.toString(action.getId()));
        }
        for (Module module : moduleMap.values()) {
            for (Action action : module.getActions()) {
                list.add(module.getId() + "." + Utilities.presenter.toString(action.getId()));
            }
        }
        return list;
    }

    public Map<String, ASTRProgram> getPrograms() {
        return programs;
    }

    public List<String> getSensorStrings() {
        List<String> list = new LinkedList<String>();
        for (Sensor sensor : sensorMap.values()) {
            list.add(Utilities.presenter.toString(sensor.getId()));
        }
        for (Module module : moduleMap.values()) {
            for (Sensor sensor : module.getSensors()) {
                list.add(module.getId() + "." + Utilities.presenter.toString(sensor.getId()));
            }
        }
        return list;
    }

    public Collection<Action> getActions() {
        return actionMap.values();
    }

    public Collection<Sensor> getSensors() {
        return sensorMap.values();
    }

    @Override
    public String getType() {
        return "ASTR";
    }

    @Override
    public void initialise(String data) {
        if (data.startsWith("+")) {
            Predicate event = (Predicate) Utilities.factory.createFormula(data.substring(1));
            addEvent(new ASEvent(ASEvent.ADDITION, event));
        } else if (data.startsWith("-")) {
            Predicate event = (Predicate) Utilities.factory.createFormula(data.substring(1));
            addEvent(new ASEvent(ASEvent.REMOVAL, event));
        } else {
            try {
                addBelief((Predicate) Utilities.factory.createFormula(data + "[source(self)]"));
            } catch (LogicException le) {
                System.out.println("[" + getName() + "] Initialisation Error: " + le.getMessage());
            }
        }
    }

    @Override
    public void update(Observable arg0, Object arg1) {
    }

    public void addBelief(Predicate belief) {
        beliefManager.adoptBelief(belief, null);
    }

    public void addBelief(Predicate belief, Object source) {
        beliefManager.adoptBelief(belief, source);
    }

    public void removeBelief(Predicate belief) {
        beliefManager.dropBelief(belief, null);
    }

    public void removeBelief(Predicate belief, Object source) {
        beliefManager.dropBelief(belief, source);
    }

    public void addEvent(ASEvent event) {
        String sig = event.toSignature();
        if (eventFilter.contains(sig)) eventQueue.add(event);
    }

    protected void addPlanRule(PlanRule rule) {
        List<PlanRule> list = ruleEventMap.get(rule.getTriggeringEvent().toSignature());
        if (list == null) {
            list = new LinkedList<PlanRule>();
            ruleEventMap.put(rule.getTriggeringEvent().toSignature(), list);
        }
        list.add(rule);
        eventFilter.add(rule.getTriggeringEvent().toSignature());
    }

    protected void addInference(IMPLIES inference) {
        inferences.add(inference);
    }

    public List<IMPLIES> getInferences() {
        return inferences;
    }

    public IQueryEngine getQueryEngine() {
        return queryEngine;
    }

    @Override
    public boolean hasType(String type) {
        return true;
    }

    @Override
    public List<IFormula> query(IFormula query) {
        List<IFormula> list = new LinkedList<IFormula>();
        if (Predicate.class.isInstance(query)) {
            Predicate predicate = (Predicate) query;
            for (IMPLIES inference : inferences) {
                if (inference.result().matches(predicate)) {
                    list.add(inference);
                }
            }
        }
        return list;
    }

    private ASEvent getNextEvent() {
        if (eventQueue.isEmpty()) return null;
        return eventQueue.remove(0);
    }

    private List<PlanRule> generateOptions(ASEvent event) {
        List<PlanRule> list = ruleEventMap.get(event.toSignature());
        List<PlanRule> options = new LinkedList<PlanRule>();
        if (list != null) {
            for (PlanRule rule : list) {
                if (rule.getTriggeringEvent().matches(event)) {
                    Bindings set = rule.getTriggeringEvent().unifyWith(event);
                    if (set != null) {
                        options.add(rule.apply(set));
                    }
                }
            }
        }
        return options;
    }

    private PlanRule selectOption(List<PlanRule> options) {
        for (PlanRule option : options) {
            QueryResult result = queryEngine.query(option.getContext());
            if (result.found) {
                if (result.bindings.isEmpty()) {
                    return option;
                } else {
                    return option.apply(result.bindings.get(0));
                }
            }
        }
        return null;
    }

    private Intention selectIntention() {
        int index = indentionIndex;
        do {
            indentionIndex = (indentionIndex + 1) % intentions.size();
        } while ((indentionIndex != index) && intentions.get(indentionIndex).isSuspended());
        if (intentions.get(indentionIndex).isSuspended()) return null;
        return intentions.get(indentionIndex);
    }

    public Map<String, List<String>> getStack() {
        return programStacks;
    }

    public List<ActionTraceLogEntry> getActionTrace() {
        return actionTrace;
    }

    public List<Predicate> getActivePrograms() {
        return activePrograms;
    }

    public void addBelief(NOT not, Object source) {
        beliefManager.adoptBelief(not, source);
    }
}
