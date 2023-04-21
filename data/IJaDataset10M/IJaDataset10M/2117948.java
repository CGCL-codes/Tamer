package com.monad.homerun.objmgt.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.monad.homerun.action.Binding;
import com.monad.homerun.action.DateBinding;
import com.monad.homerun.action.EventBinding;
import com.monad.homerun.action.Month;
import com.monad.homerun.action.SituationBinding;
import com.monad.homerun.action.Plan;
import com.monad.homerun.action.Schedule;
import com.monad.homerun.action.Scheme;
import com.monad.homerun.action.TimeBinding;
import com.monad.homerun.core.GlobalProps;
import com.monad.homerun.core.NoResourceException;
import com.monad.homerun.event.Event;
import com.monad.homerun.model.Model;
import com.monad.homerun.model.Relation;
import com.monad.homerun.model.ModelStatus;
import com.monad.homerun.model.scalar.ValueType;
import com.monad.homerun.modelmgt.ModelService;
import com.monad.homerun.objmgt.ActionService;
import com.monad.homerun.objmgt.ObjectService;
import com.monad.homerun.rule.Invoker;
import com.monad.homerun.rule.Rule;
import com.monad.homerun.rule.Clause;
import com.monad.homerun.rule.Condition;
import com.monad.homerun.rule.RuleTrace;
import com.monad.homerun.rule.Situation;
import com.monad.homerun.rule.TraceContext;
import com.monad.homerun.rule.TraceDescription;
import com.monad.homerun.store.ObjectStore;
import com.monad.homerun.timing.TimingService;
import com.monad.homerun.util.TimeUtil;

/**
 * ActionManager provides facilities for managing action rules.
 * Users can add, update, or remove actions and related entities.
 * It also manages sets of bound action rules, i.e. schedules, event plans,
 * calendars schemes and the like, especially their runtime behavior.
 */
public class ActionManager implements ActionService, Invoker {

    private Logger sysLogger = null;

    private Logger actLogger = null;

    private ModelService modelSvc = null;

    private ObjectService objSvc = null;

    private TimingService timingSvc = null;

    private ObjectStore storeMan = null;

    private RuntimeSchedule rtSchedule = null;

    private RuntimeCalendar rtCalendar = null;

    private List<RuntimePlan> planList = null;

    private List<RuntimeScheme> schemeList = null;

    private List<RuntimeSituation> pollList = null;

    private Set<String> traceSet = null;

    private String tracePath = null;

    public ActionManager() {
        sysLogger = Activator.logSvc.getLogger();
        actLogger = Activator.logSvc.getLogger("activity");
        storeMan = Activator.getStore("action", Rule.class);
        tracePath = GlobalProps.getHomeDir() + File.separator + "temp" + File.separator + "trace";
    }

    public void init(boolean start) {
        if (GlobalProps.DEBUG) {
            sysLogger.log(Level.FINE, "initializing");
        }
        modelSvc = Activator.modelSvc;
        objSvc = Activator.objSvc;
        timingSvc = Activator.timingSvc;
        if (start) {
            planList = new ArrayList<RuntimePlan>();
            schemeList = new ArrayList<RuntimeScheme>();
            pollList = new ArrayList<RuntimeSituation>();
            traceSet = new HashSet<String>();
            File traceDir = new File(tracePath);
            if (!traceDir.exists()) {
                traceDir.mkdirs();
            }
        }
    }

    public void shutdown() {
        if (rtSchedule != null) {
            rtSchedule.retire();
            rtSchedule = null;
        }
        for (RuntimePlan rtPlan : planList) {
            rtPlan.retire();
        }
        planList.clear();
        for (RuntimeScheme rtScheme : schemeList) {
            rtScheme.retire();
        }
        schemeList.clear();
        sysLogger.log(Level.INFO, "shutdown");
    }

    public void pollEvent() {
        for (RuntimeSituation rts : pollList) {
            rts.evaluate();
        }
    }

    private void requestPoll(RuntimeSituation rts) {
        if (GlobalProps.DEBUG) {
            System.out.println("requestPoll");
        }
        if (pollList.size() == 0) {
            timingSvc.scheduleJob("Poll:minute", PollJob.class, "minutely", null);
        }
        pollList.add(rts);
    }

    private void cancelPoll(RuntimeSituation rts) {
        pollList.remove(rts);
        if (pollList.size() == 0) {
            timingSvc.killJob("Poll:minute");
        }
    }

    public boolean startSchedule(String schedName) {
        Schedule schedule = getSchedule(schedName);
        if (schedule != null && rtSchedule == null) {
            rtSchedule = new RuntimeSchedule(schedule);
        }
        return rtSchedule != null;
    }

    public void stopSchedule() {
        if (rtSchedule != null) {
            rtSchedule.retire();
            rtSchedule = null;
        }
    }

    public boolean validateSchedule(String schedName) {
        Schedule schedule = getSchedule(schedName);
        if (schedule != null) {
            for (TimeBinding binding : schedule.getEntries()) {
                if (!ruleExists(binding.getActionName())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void evaluateSchedule() {
        if (rtSchedule != null) {
            rtSchedule.evaluate();
        }
    }

    private class RuntimeSchedule {

        private Schedule schedule = null;

        private int schedLength = 0;

        private int schedCounter = 0;

        public RuntimeSchedule(Schedule schedule) {
            this.schedule = schedule;
            schedLength = schedule.getLength();
            schedCounter = schedule.getRepeat();
            timingSvc.scheduleJob("Schedule:" + schedule.getName(), ScheduleJob.class, "minutely", null);
        }

        public void retire() {
            timingSvc.killJob("Schedule:" + schedule.getName());
        }

        private void evaluate() {
            if (GlobalProps.DEBUG) {
                System.out.println("RtSched eval - enter");
            }
            if (--schedLength == 0) {
                if (--schedCounter < 0) {
                    String nextSchedule = schedule.getNextSchedule();
                    if (nextSchedule == null || nextSchedule.length() == 0 || Schedule.NO_NEXT_SCHEDULE.equals(nextSchedule)) {
                        stopSchedule();
                    } else if (nextSchedule.equals(schedule.getName()) || Schedule.SELF_NEXT_SCHEDULE.equals(nextSchedule)) {
                        schedLength = schedule.getLength();
                        schedCounter = schedule.getRepeat();
                    } else {
                        stopSchedule();
                        startSchedule(nextSchedule);
                    }
                }
            } else {
                short schedTime = TimeUtil.mowFormat(System.currentTimeMillis());
                int entryIndex = schedule.getFirstIndexAt(schedTime);
                if (entryIndex != -1) {
                    TimeBinding entry = schedule.getEntry(entryIndex);
                    while (entry != null && entry.getTime() == schedTime) {
                        dispatchAction(entry, "schedule", schedule.getName());
                        String logMsg = "Schedule '" + schedule.getName() + "' performed action: '" + entry.getActionName() + "'";
                        actLogger.log(Level.INFO, logMsg);
                        entry = schedule.getEntry(++entryIndex);
                    }
                }
            }
        }
    }

    public boolean runPlan(String planName) {
        Plan plan = getPlan(planName);
        if ((plan != null) && (findPlan(planName) == null)) {
            plan.mapBindings();
            planList.add(new RuntimePlan(plan));
            return true;
        }
        return false;
    }

    public void stopPlan(String planName) {
        RuntimePlan rtPlan = findPlan(planName);
        if (rtPlan != null) {
            rtPlan.retire();
            planList.remove(rtPlan);
        }
    }

    public boolean validatePlan(String planName) {
        Plan plan = getPlan(planName);
        if (plan != null) {
            for (EventBinding binding : plan.getBindings()) {
                if (!ruleExists(binding.getActionName()) || !objSvc.objectExists(binding.getDomain(), binding.getObjectName())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private RuntimePlan findPlan(String planName) {
        for (RuntimePlan rtPlan : planList) {
            if (rtPlan.plan.getName().equals(planName)) {
                return rtPlan;
            }
        }
        return null;
    }

    private class RuntimePlan {

        private Plan plan = null;

        private List<Attendant> attendantList = null;

        public RuntimePlan(Plan plan) {
            this.plan = plan;
            init();
        }

        private void init() {
            attendantList = new ArrayList<Attendant>();
            Iterator<String> eventIter = plan.getEventIterator();
            while (eventIter.hasNext()) {
                String key = eventIter.next();
                String eventName = key.substring(key.lastIndexOf(":") + 1);
                if (GlobalProps.DEBUG) {
                    System.out.println("RTPlan init key: " + key + " en: " + eventName);
                }
                attendantList.add(new Attendant(eventName, plan.getEventBindings(key)));
            }
        }

        public void retire() {
            for (Attendant attendant : attendantList) {
                attendant.retire();
            }
        }

        private class Attendant implements Observer {

            private EventBinding[] bindings = null;

            public Attendant(String eventName, EventBinding[] bindings) {
                this.bindings = bindings;
                EventBinding binding = bindings[0];
                String type = binding.getSourceType();
                if ("model".equals(type)) {
                    if (GlobalProps.DEBUG) {
                        System.out.println("Attendant - addModObs for event:" + eventName);
                    }
                    modelSvc.addModelObserver(binding.getDomain(), binding.getObjectName(), binding.getSourceName(), this, false);
                } else if ("emitter".equals(type)) {
                    if (GlobalProps.DEBUG) {
                        System.out.println("Attendant - addObjObs for event:" + eventName);
                    }
                    objSvc.addObjectObserver(binding.getDomain(), binding.getObjectName(), binding.getSourceName(), this, false);
                }
            }

            public void retire() {
                EventBinding binding = bindings[0];
                String type = binding.getSourceType();
                if ("model".equals(type)) {
                    modelSvc.removeModelObserver(binding.getDomain(), binding.getObjectName(), binding.getSourceName(), this);
                } else if ("emitter".equals(type)) {
                    objSvc.removeObjectObserver(binding.getDomain(), binding.getObjectName(), binding.getSourceName(), this);
                }
            }

            public void update(Observable obsModel, Object event) {
                if (GlobalProps.DEBUG) {
                    System.out.println("Attendant - update num bindings: " + bindings.length);
                }
                if (!(event instanceof Event)) {
                    if (GlobalProps.DEBUG) {
                        System.out.println("Attendant - update: not an Event");
                    }
                    return;
                }
                Event evt = (Event) event;
                for (EventBinding binding : bindings) {
                    if (GlobalProps.DEBUG) {
                        System.out.println("Attendant - update looking for" + " event: " + binding.getEvent() + " found: " + (String) (evt).getEvent());
                    }
                    if (binding.matches(evt)) {
                        dispatchAction(binding, "plan", plan.getName());
                        String logMsg = "Plan '" + plan.getName() + "' performed action: '" + binding.getActionName() + "'";
                        actLogger.log(Level.INFO, logMsg);
                    }
                }
            }
        }
    }

    public boolean validateMonth(String monthName) {
        Month month = getMonth(monthName);
        if (month != null) {
            for (DateBinding binding : month.getEntries()) {
                if (!ruleExists(binding.getActionName())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void enableCalendar(boolean enable) {
        if (rtCalendar == null && enable) {
            rtCalendar = new RuntimeCalendar();
            rtCalendar.evaluate();
            timingSvc.scheduleJob("MonthCheck", CalendarEvalJob.class, "monthly", null);
        } else if (rtCalendar != null && !enable) {
            timingSvc.killJob("MonthCheck");
            rtCalendar = null;
        }
    }

    public void applyCalendar() {
        if (rtCalendar != null) {
            rtCalendar.apply();
        }
    }

    public void evaluateCalendar() {
        if (rtCalendar != null) {
            rtCalendar.evaluate();
        }
    }

    private class RuntimeCalendar {

        private DateBinding[] bindings = null;

        private Calendar cal = null;

        public RuntimeCalendar() {
            cal = Calendar.getInstance();
        }

        public void evaluate() {
            if (GlobalProps.DEBUG) {
                System.out.println("Calendar eval");
            }
            cal.setTime(new Date());
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int mins = cal.get(Calendar.MINUTE);
            int bindMins = (hour * 60) + mins;
            Month curMonth = getMonth(year + "-" + month);
            if (curMonth != null) {
                if (GlobalProps.DEBUG) {
                    System.out.println("Calendar eval - got month");
                }
                bindings = curMonth.getEntriesAfter((short) day, (short) bindMins);
                if (bindings.length > 0) {
                    if (GlobalProps.DEBUG) {
                        System.out.println("Calendar eval - got bindings");
                    }
                    cal.clear();
                    cal.set(year, month, bindings[0].getDay(), bindings[0].getMinutes() / 60, bindings[0].getMinutes() % 60);
                    timingSvc.scheduleJob("CalendarWork", CalendarApplyJob.class, "date", cal.getTime());
                }
            }
        }

        public void apply() {
            if (bindings != null) {
                if (GlobalProps.DEBUG) {
                    System.out.println("Calendar apply");
                }
                for (DateBinding binding : bindings) {
                    dispatchAction(binding, "calendar", "none");
                    String logMsg = "Calendar performed action: '" + binding.getActionName() + "'";
                    actLogger.log(Level.INFO, logMsg);
                }
                bindings = null;
            }
            evaluate();
        }
    }

    public boolean runScheme(String schemeName) {
        Scheme scheme = getScheme(schemeName);
        if ((scheme != null) && (findScheme(schemeName) == null)) {
            if (GlobalProps.DEBUG) {
                System.out.println("runScheme: " + schemeName);
            }
            scheme.mapBindings();
            schemeList.add(new RuntimeScheme(scheme));
            return true;
        }
        return false;
    }

    public void stopScheme(String schemeName) {
        RuntimeScheme rtScheme = findScheme(schemeName);
        if (rtScheme != null) {
            rtScheme.retire();
            schemeList.remove(rtScheme);
        }
    }

    public boolean validateScheme(String schemeName) {
        Scheme scheme = getScheme(schemeName);
        if (scheme != null) {
            for (String situName : scheme.getSituationSets()) {
                String[] parts = situName.split(":");
                if (!validateSituation(parts[0], parts[1])) {
                    return false;
                }
                for (SituationBinding binding : scheme.getSituationBindings(parts[0], parts[1])) {
                    if (!ruleExists(binding.getActionName())) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private RuntimeScheme findScheme(String schemeName) {
        for (RuntimeScheme rtScheme : schemeList) {
            if (rtScheme.scheme.getName().equals(schemeName)) {
                return rtScheme;
            }
        }
        return null;
    }

    private class RuntimeScheme {

        private Scheme scheme = null;

        private List<RuntimeSituation> rtSituations = null;

        public RuntimeScheme(Scheme scheme) {
            this.scheme = scheme;
            init();
        }

        public void retire() {
            for (RuntimeSituation rtSituation : rtSituations) {
                rtSituation.retire();
            }
        }

        private void init() {
            rtSituations = new ArrayList<RuntimeSituation>();
            for (String situName : scheme.getSituationSets()) {
                String[] parts = situName.split(":");
                Situation situation = getSituation(parts[0], parts[1]);
                if (situation != null) {
                    rtSituations.add(new RuntimeSituation(scheme.getName(), situation, scheme.getSituationBindings(parts[0], parts[1])));
                }
            }
        }
    }

    public boolean validateSituation(String category, String situationName) {
        Situation situation = getSituation(category, situationName);
        if (situation != null) {
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("invoker", ActionManager.this);
            context.put("trace", null);
            for (int i = 0; i < situation.getNumConditions(); i++) {
                Condition cond = situation.getCondition(i);
                if (!cond.validate(context)) {
                    return false;
                }
            }
        }
        return false;
    }

    private class RuntimeSituation implements Observer {

        private String schemeName = null;

        private Situation situation = null;

        private List<SituationBinding> entryBindings = null;

        private List<SituationBinding> exitBindings = null;

        private boolean obtains = false;

        private List<Condition> condList = null;

        private Map<String, Object> context = null;

        public RuntimeSituation(String schemeName, Situation situation, SituationBinding[] bindings) {
            this.schemeName = schemeName;
            this.situation = situation;
            entryBindings = new ArrayList<SituationBinding>();
            exitBindings = new ArrayList<SituationBinding>();
            condList = new ArrayList<Condition>();
            context = new HashMap<String, Object>();
            context.put("invoker", ActionManager.this);
            context.put("trace", null);
            for (SituationBinding binding : bindings) {
                if (SituationBinding.ENTRY_TYPE == binding.getType()) {
                    entryBindings.add(binding);
                } else {
                    exitBindings.add(binding);
                }
            }
            obtains = evalSituation(situation, "test");
            if (obtains) {
                waitForFail();
            } else {
                waitForObtain();
            }
        }

        public void evaluate() {
            boolean obtained = obtains;
            obtains = evalSituation(situation, "test");
            if (!obtained && obtains) {
                for (SituationBinding binding : entryBindings) {
                    dispatchAction(binding, "scheme", schemeName);
                }
                waitForFail();
            } else if (obtained && !obtains) {
                for (SituationBinding binding : exitBindings) {
                    dispatchAction(binding, "scheme", schemeName);
                }
                waitForObtain();
            }
        }

        private void waitForFail() {
            if (GlobalProps.DEBUG) {
                System.out.println("waitForFail");
            }
            try {
                clearObservers();
                int numCons = 0;
                for (int i = 0; i < situation.getNumConditions(); i++) {
                    Condition cond = situation.getCondition(i);
                    if (cond.isAlternate() && i > 0) {
                        if (numCons == condList.size()) {
                            break;
                        } else {
                            condList.clear();
                            numCons = 0;
                        }
                    }
                    ++numCons;
                    if (cond.isObservable(ActionManager.this) && cond.test(context)) {
                        condList.add(cond);
                    }
                }
                if (numCons == condList.size()) {
                    addObservers();
                    cancelPoll(this);
                } else {
                    condList.clear();
                    requestPoll(this);
                }
            } catch (NoResourceException nre) {
                if (GlobalProps.DEBUG) {
                    nre.printStackTrace();
                }
            }
        }

        private void waitForObtain() {
            if (GlobalProps.DEBUG) {
                System.out.println("waitForObtain");
            }
            try {
                clearObservers();
                int numAlts = 1;
                boolean haveCand = false;
                for (int i = 0; i < situation.getNumConditions(); i++) {
                    Condition cond = situation.getCondition(i);
                    if (cond.isAlternate() && i > 0) {
                        haveCand = false;
                        ++numAlts;
                    }
                    if (!haveCand && cond.isObservable(ActionManager.this) && !cond.test(context)) {
                        condList.add(cond);
                        haveCand = true;
                    }
                }
                if (numAlts == condList.size()) {
                    addObservers();
                    cancelPoll(this);
                } else {
                    condList.clear();
                    requestPoll(this);
                }
            } catch (NoResourceException nre) {
                if (GlobalProps.DEBUG) {
                    nre.printStackTrace();
                }
            }
        }

        private void addObservers() {
            for (Condition cond : condList) {
                Relation rel = cond.getRelation();
                modelSvc.addModelObserver(rel.getDomain(), cond.getObjectName(), rel.getModelName(), this, false);
            }
        }

        private void clearObservers() {
            for (Condition cond : condList) {
                Relation rel = cond.getRelation();
                modelSvc.removeModelObserver(rel.getDomain(), cond.getObjectName(), rel.getModelName(), this);
            }
            condList.clear();
        }

        public void update(Observable obs, Object thing) {
            evaluate();
        }

        public void retire() {
            clearObservers();
            cancelPoll(this);
        }
    }

    private void dispatchAction(Binding binding, String invokerType, String invokerName) {
        if (GlobalProps.DEBUG) {
            System.out.println("dispatchAction: " + binding.getActionName());
        }
        Rule rule = getRule(binding.getActionCategory(), binding.getActionName());
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("mode", "apply");
        context.put("bindingProps", binding.getProperties());
        applyActionInternal(rule, context, -1, invokerType, invokerName);
    }

    public boolean applyRule(String category, String ruleName, Map<String, Object> context) {
        return applyRule(getRule(category, ruleName), context);
    }

    public boolean applyRule(Rule rule, Map<String, Object> context) {
        boolean retVal = false;
        String mode = (String) context.get("mode");
        if (rule != null && mode != null) {
            context.put("invoker", this);
            try {
                if (mode.equals("validate")) {
                    context.put("callStack", new ArrayList<String>());
                    retVal = rule.validate(context);
                } else if (mode.equals("apply")) {
                    applyRuleInternal(rule, null, context, -1);
                    retVal = true;
                } else if (mode.startsWith("test")) {
                    String clauseNum = mode.substring(mode.indexOf(":") + 1);
                    Clause clause = rule.getClause(Integer.parseInt(clauseNum));
                    context.put("invoker", this);
                    retVal = clause.testConditions(context);
                } else if (mode.startsWith("perform")) {
                    int clIdx = 0;
                    int split = mode.indexOf(":");
                    if (split != -1) {
                        String clauseNum = mode.substring(split + 1);
                        clIdx = Integer.parseInt(clauseNum);
                    }
                    applyRuleInternal(rule, null, context, clIdx);
                    retVal = true;
                }
            } catch (NoResourceException urExp) {
                sysLogger.log(Level.SEVERE, "missing resources");
                retVal = false;
            }
        }
        return retVal;
    }

    private void applyRuleInternal(Rule rule, RuleTrace trace, Map<String, Object> context, int clIdx) {
        ExecThread eThread = new ExecThread(rule, trace, clIdx, context, null, null);
        eThread.start();
        if (trace != null && trace.getMode() == RuleTrace.INTERACTIVE_MODE) {
            try {
                eThread.join();
            } catch (InterruptedException iExp) {
                sysLogger.log(Level.INFO, "exec thread interrupted");
            }
        }
    }

    private void applyActionInternal(Rule rule, Map<String, Object> context, int clIdx, String invokerType, String invokerName) {
        if (isTracing(rule.getCategory(), rule.getName())) {
            context.put("trace", new RuleTrace((String) context.get("mode")));
        }
        ExecThread eThread = new ExecThread(rule, clIdx, context, invokerType, invokerName);
        eThread.start();
    }

    private class ExecThread extends Thread {

        private Rule rule = null;

        private RuleTrace trace = null;

        private int clauseIdx = -1;

        private Map<String, Object> context = null;

        private String invokerType = null;

        private String invokerName = null;

        public ExecThread(Rule rule, int clauseIdx, Map<String, Object> context, String invokerType, String invokerName) {
            this.rule = rule;
            this.trace = (RuleTrace) context.get("trace");
            this.clauseIdx = clauseIdx;
            this.context = context;
            this.invokerType = invokerType;
            this.invokerName = invokerName;
        }

        public ExecThread(Rule rule, RuleTrace trace, int clauseIdx, Map<String, Object> context, String invokerType, String invokerName) {
            this.rule = rule;
            this.trace = trace;
            this.clauseIdx = clauseIdx;
            this.context = context;
            this.invokerType = invokerType;
            this.invokerName = invokerName;
        }

        public void run() {
            context.put("invoker", ActionManager.this);
            context.put("trace", trace);
            context.put("callStack", new ArrayList<String>());
            try {
                if (clauseIdx == -1) {
                    rule.apply(context);
                } else {
                    Clause clause = rule.getClause(clauseIdx);
                    clause.performActions(context);
                }
            } catch (NoResourceException nrE) {
                sysLogger.log(Level.SEVERE, "missing resources");
            }
            if (trace != null && trace.getMode() == RuleTrace.RECORD_MODE) {
                TraceContext traceContext = new TraceContext(rule.getCategory(), rule.getName(), invokerType, invokerName);
                traceContext.setTrace(trace);
                String savePath = tracePath + File.separator + String.valueOf(traceContext.getCreateTime()) + ".trc";
                try {
                    FileOutputStream fos = new FileOutputStream(savePath);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(traceContext);
                    oos.close();
                } catch (FileNotFoundException fnfe) {
                    sysLogger.log(Level.SEVERE, savePath + " no found");
                } catch (IOException ioe) {
                    sysLogger.log(Level.SEVERE, "error writing to: " + savePath);
                }
            }
        }
    }

    private boolean isTracing(String category, String ruleName) {
        return traceSet.contains(category + ":" + ruleName) || traceSet.contains(category + ":*") || traceSet.contains("*:*");
    }

    public void startTracing(String category, String ruleName) {
        traceSet.add(category + ":" + ruleName);
    }

    public void stopTracing(String category, String ruleName) {
        traceSet.remove(category + ":" + ruleName);
    }

    public TraceDescription[] getTraceDescriptions() {
        TraceDescription[] retDescs = null;
        File[] traceFiles = null;
        try {
            File traceDir = new File(tracePath);
            if (traceDir.isDirectory()) {
                traceFiles = traceDir.listFiles();
            } else {
                if (GlobalProps.DEBUG) {
                    System.out.println("NOT A DIR!!!");
                }
            }
        } catch (Exception e) {
            if (GlobalProps.DEBUG) {
                System.out.println("Exception!!!");
                e.printStackTrace();
            }
            traceFiles = null;
        }
        if (traceFiles != null) {
            retDescs = new TraceDescription[traceFiles.length];
            int i = 0;
            for (File file : traceFiles) {
                retDescs[i++] = getTraceContext(file.getName()).getDescription();
            }
        }
        return retDescs != null ? retDescs : new TraceDescription[0];
    }

    public TraceContext getTraceContext(String contextID) {
        TraceContext tCtx = null;
        String tcPath = tracePath + File.separator + contextID;
        if (!contextID.endsWith(".trc")) {
            tcPath += ".trc";
        }
        try {
            FileInputStream fis = new FileInputStream(tcPath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            tCtx = (TraceContext) ois.readObject();
            ois.close();
        } catch (FileNotFoundException fnfe) {
            sysLogger.log(Level.SEVERE, tcPath + " not found");
        } catch (IOException ioe) {
            sysLogger.log(Level.SEVERE, "error reading: " + tcPath);
        } catch (ClassNotFoundException cfne) {
            sysLogger.log(Level.SEVERE, tcPath + " not a trace context");
        }
        return tCtx;
    }

    public RuleTrace traceRule(Rule rule, RuleTrace trace) {
        boolean retVal = false;
        if (rule != null && trace != null) {
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("invoker", this);
            context.put("trace", trace);
            try {
                if (trace.isValidation()) {
                    context.put("callStack", new ArrayList<String>());
                    retVal = rule.validate(context);
                } else if (trace.getScope() == RuleTrace.COND_ACT) {
                    applyRuleInternal(rule, trace, context, -1);
                    retVal = true;
                } else if (trace.getScope() == RuleTrace.COND_ONLY) {
                    Clause clause = rule.getClause(trace.getClause());
                    retVal = clause.testConditions(context);
                } else if (trace.getScope() == RuleTrace.ACT_ONLY) {
                    applyRuleInternal(rule, trace, context, trace.getClause());
                    retVal = true;
                }
            } catch (NoResourceException urExp) {
                retVal = false;
            }
        }
        trace.setResult(retVal);
        return trace;
    }

    public String[] getRuleCategories() {
        return getCategories(Rule.class.getName());
    }

    public String[] getRuleNames(String category) {
        return getNames(Rule.class.getName(), category);
    }

    public boolean ruleExists(String ruleName) {
        for (String storeKey : storeMan.getKeys(null, Rule.class.getName())) {
            if (storeKey.indexOf(ruleName) != -1) {
                return true;
            }
        }
        return false;
    }

    public String[] getSituationCategories() {
        return getCategories(Situation.class.getName());
    }

    public String[] getSituationNames(String category) {
        return getNames(Situation.class.getName(), category);
    }

    public boolean situationExists(String category, String situationName) {
        return situationExists(situationName);
    }

    public boolean situationExists(String situationName) {
        for (String storeKey : storeMan.getKeys(null, Situation.class.getName())) {
            if (storeKey.indexOf(situationName) != -1) {
                return true;
            }
        }
        return false;
    }

    public Situation getSituation(String category, String situationName) {
        String key = category + "." + situationName;
        return (Situation) storeMan.getObject(key, Situation.class.getName());
    }

    public boolean addSituation(Situation situation) {
        situation.setModificationTime(System.currentTimeMillis());
        String key = situation.getCategory() + "." + situation.getName();
        boolean ret = storeMan.addObject(key, Situation.class.getName(), situation);
        if (ret) {
            Activator.objSvc.announceEvent("addSituation", situation);
        }
        return ret;
    }

    public boolean updateSituation(Situation situation) {
        situation.setModificationTime(System.currentTimeMillis());
        String key = situation.getCategory() + "." + situation.getName();
        return storeMan.updateObject(key, Situation.class.getName(), situation);
    }

    public boolean removeSituation(String category, String situationName) {
        String key = category + "." + situationName;
        return storeMan.removeObject(key, Situation.class.getName());
    }

    public boolean evalSituation(Situation situation, String mode) {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("invoker", this);
        if ("validate".equals(mode)) {
            context.put("callStack", new ArrayList<String>());
            return situation.validate(context);
        } else if ("test".equals(mode)) {
            try {
                return situation.testConditions(context);
            } catch (Exception e) {
            }
        }
        return false;
    }

    public RuleTrace traceSituation(Situation situation, RuleTrace trace) {
        boolean retVal = false;
        if (situation != null && trace != null) {
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("invoker", this);
            context.put("trace", trace);
            try {
                if (trace.isValidation()) {
                    context.put("callStack", new ArrayList<String>());
                    retVal = situation.validate(context);
                } else {
                    situation.testConditions(context);
                    retVal = true;
                }
            } catch (NoResourceException urExp) {
                retVal = false;
            }
        }
        trace.setResult(retVal);
        return trace;
    }

    public boolean objectExists(String domain, String objectName) {
        return objSvc.objectExists(domain, objectName);
    }

    public boolean domainExists(String domain) {
        return objSvc.domainExists(domain);
    }

    public boolean controlObject(String domain, String objectName, String control, String point, Map modifiers, Map context) {
        return objSvc.controlObject(domain, objectName, control, point, modifiers, context);
    }

    public boolean canObserveModel(String domain, String objectName, String modelName) {
        return modelSvc.canObserveModel(domain, objectName, modelName);
    }

    public void informModel(String domain, String objectName, String modelName, Event event) {
        modelSvc.informModel(domain, objectName, modelName, event);
    }

    public Model getModel(String domain, String modelName) {
        return modelSvc.getModel(domain, modelName);
    }

    public ModelStatus getModelStatus(String domain, String objectName, String modelName) {
        return objSvc.getModelStatus(domain, objectName, modelName);
    }

    public ValueType getValueType(String typeName) {
        return (typeName != null) ? modelSvc.getValueType(typeName) : null;
    }

    public boolean isRuleStartable(String category, String ruleName) {
        Rule rule = getRule(category, ruleName);
        return (rule != null) ? rule.isStartable(this) : false;
    }

    public String[] getRuleBindingVariables(String category, String ruleName) {
        Rule rule = getRule(category, ruleName);
        return (rule != null) ? rule.getBindingVariables().toArray(new String[0]) : new String[0];
    }

    public Rule getRule(String category, String ruleName) {
        String key = category + "." + ruleName;
        return (Rule) storeMan.getObject(key, Rule.class.getName());
    }

    public boolean addRule(Rule rule) {
        rule.setModificationTime(System.currentTimeMillis());
        String key = rule.getCategory() + "." + rule.getName();
        boolean ret = storeMan.addObject(key, Rule.class.getName(), rule);
        if (ret) {
            Activator.objSvc.announceEvent("addRule", rule);
        }
        return ret;
    }

    public boolean updateRule(Rule rule) {
        rule.setModificationTime(System.currentTimeMillis());
        String key = rule.getCategory() + "." + rule.getName();
        return storeMan.updateObject(key, Rule.class.getName(), rule);
    }

    public boolean removeRule(String category, String ruleName) {
        String key = category + "." + ruleName;
        return storeMan.removeObject(key, Rule.class.getName());
    }

    public String getActiveScheduleName() {
        return (rtSchedule != null) ? rtSchedule.schedule.getName() : null;
    }

    public String[] getScheduleNames() {
        return storeMan.getKeys(null, Schedule.class.getName());
    }

    public Schedule getSchedule(String schedName) {
        return (schedName != null) ? (Schedule) storeMan.getObject(schedName, Schedule.class.getName()) : null;
    }

    public boolean addSchedule(Schedule schedule) {
        schedule.setModificationTime(System.currentTimeMillis());
        return storeMan.addObject(schedule.getName(), Schedule.class.getName(), schedule);
    }

    public boolean updateSchedule(Schedule schedule) {
        schedule.setModificationTime(System.currentTimeMillis());
        return storeMan.updateObject(schedule.getName(), Schedule.class.getName(), schedule);
    }

    public boolean removeSchedule(String schedName) {
        return storeMan.removeObject(schedName, Schedule.class.getName());
    }

    public String[] getMonthNames() {
        return storeMan.getKeys(null, Month.class.getName());
    }

    public Month getMonth(String monthName) {
        return (monthName != null) ? (Month) storeMan.getObject(monthName, Month.class.getName()) : null;
    }

    public boolean addMonth(Month month) {
        month.setModificationTime(System.currentTimeMillis());
        boolean ret = storeMan.addObject(month.getName(), Month.class.getName(), month);
        evaluateCalendar();
        return ret;
    }

    public boolean updateMonth(Month month) {
        month.setModificationTime(System.currentTimeMillis());
        boolean ret = storeMan.updateObject(month.getName(), Month.class.getName(), month);
        evaluateCalendar();
        return ret;
    }

    public boolean removeMonth(Month month) {
        boolean ret = storeMan.removeObject(month.getName(), Month.class.getName());
        evaluateCalendar();
        return ret;
    }

    public String[] getActivePlanNames() {
        String[] planNames = new String[planList.size()];
        for (int i = 0; i < planList.size(); i++) {
            planNames[i] = (planList.get(i)).plan.getName();
        }
        return planNames;
    }

    public String[] getPlanNames() {
        return storeMan.getKeys(null, Plan.class.getName());
    }

    public Plan getPlan(String planName) {
        return (planName != null) ? (Plan) storeMan.getObject(planName, Plan.class.getName()) : null;
    }

    public boolean addPlan(Plan plan) {
        plan.setModificationTime(System.currentTimeMillis());
        return storeMan.addObject(plan.getName(), Plan.class.getName(), plan);
    }

    public boolean updatePlan(Plan plan) {
        plan.setModificationTime(System.currentTimeMillis());
        return storeMan.updateObject(plan.getName(), Plan.class.getName(), plan);
    }

    public boolean removePlan(String planName) {
        return storeMan.removeObject(planName, Plan.class.getName());
    }

    public String[] getActiveSchemeNames() {
        String[] planNames = new String[schemeList.size()];
        for (int i = 0; i < schemeList.size(); i++) {
            planNames[i] = (schemeList.get(i)).scheme.getName();
        }
        return planNames;
    }

    public String[] getSchemeNames() {
        return storeMan.getKeys(null, Scheme.class.getName());
    }

    public Scheme getScheme(String schemeName) {
        return (schemeName != null) ? (Scheme) storeMan.getObject(schemeName, Scheme.class.getName()) : null;
    }

    public boolean addScheme(Scheme scheme) {
        scheme.setModificationTime(System.currentTimeMillis());
        return storeMan.addObject(scheme.getName(), Scheme.class.getName(), scheme);
    }

    public boolean updateScheme(Scheme scheme) {
        scheme.setModificationTime(System.currentTimeMillis());
        return storeMan.updateObject(scheme.getName(), Scheme.class.getName(), scheme);
    }

    public boolean removeScheme(String schemeName) {
        return storeMan.removeObject(schemeName, Scheme.class.getName());
    }

    private String[] getCategories(String className) {
        List<String> catList = new ArrayList<String>();
        for (String storeKey : storeMan.getKeys(null, className)) {
            String category = storeKey.substring(0, storeKey.indexOf("."));
            if (!catList.contains(category)) {
                catList.add(category);
            }
        }
        return catList.toArray(new String[0]);
    }

    private String[] getNames(String className, String category) {
        List<String> ruleList = new ArrayList<String>();
        for (String storeKey : storeMan.getKeys(category, className)) {
            String ruleName = storeKey.substring(storeKey.indexOf(".") + 1);
            ruleList.add(ruleName);
        }
        return ruleList.toArray(new String[0]);
    }
}
