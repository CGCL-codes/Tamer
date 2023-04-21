package ws.prova.reference2.messaging;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.apache.log4j.Logger;
import ws.prova.agent2.ProvaReagent;
import ws.prova.agent2.ProvaThreadpoolEnum;
import ws.prova.esb2.ProvaAgent;
import ws.prova.kernel2.ProvaConstant;
import ws.prova.kernel2.ProvaGoal;
import ws.prova.kernel2.ProvaKnowledgeBase;
import ws.prova.kernel2.ProvaList;
import ws.prova.kernel2.ProvaLiteral;
import ws.prova.kernel2.ProvaObject;
import ws.prova.kernel2.ProvaPredicate;
import ws.prova.kernel2.ProvaRule;
import ws.prova.kernel2.ProvaVariable;
import ws.prova.kernel2.ProvaVariablePtr;
import ws.prova.kernel2.messaging.ProvaMessenger;
import ws.prova.parser.WhereLexer;
import ws.prova.parser.WhereParser;
import ws.prova.reference2.ProvaConstantImpl;
import ws.prova.reference2.ProvaListImpl;
import ws.prova.reference2.ProvaResolutionInferenceEngineImpl;
import ws.prova.reference2.ProvaVariableImpl;
import ws.prova.reference2.eventing.ProvaAndGroupImpl;
import ws.prova.reference2.eventing.ProvaBasicGroupImpl;
import ws.prova.reference2.eventing.ProvaGroup;
import ws.prova.reference2.eventing.ProvaOrGroupImpl;
import ws.prova.reference2.eventing.ProvaGroup.EventDetectionStatus;
import ws.prova.reference2.messaging.where.WhereNode;
import ws.prova.reference2.messaging.where.WhereTreeVisitor;
import ws.prova.util2.ProvaTimeUtils;

public class ProvaMessengerImpl implements ProvaMessenger {

    private static final Logger log = Logger.getLogger("prova");

    private ProvaReagent prova;

    private ProvaKnowledgeBase kb;

    private String agent;

    private String password;

    private String machine;

    private ProvaAgent esb;

    private AtomicLong unique_iid = new AtomicLong();

    private AtomicLong reaction_iid = new AtomicLong();

    private ConcurrentMap<Long, List<String>> ruleid2outbound = new ConcurrentHashMap<Long, List<String>>();

    private ConcurrentMap<String, List<Long>> inbound2ruleids = new ConcurrentHashMap<String, List<Long>>();

    private ConcurrentMap<String, String> dynamic2Static = new ConcurrentHashMap<String, String>();

    private Map<String, ProvaGroup> dynamic2Group = new ConcurrentHashMap<String, ProvaGroup>();

    private Map<Long, ProvaGroup> ruleid2Group = new ConcurrentHashMap<Long, ProvaGroup>();

    private Map<Long, ProvaGroup> outcomeRuleid2Group = new ConcurrentHashMap<Long, ProvaGroup>();

    private ScheduledThreadPoolExecutor timers;

    private static ThreadLocal<Map<String, String>> tlStatic2Dynamic = new ThreadLocal<Map<String, String>>();

    private static ThreadLocal<Map<String, ProvaGroup>> tlDynamic = new ThreadLocal<Map<String, ProvaGroup>>();

    private class TimerThreadFactory implements ThreadFactory {

        private int count = 1;

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("Timer-" + (count++));
            return thread;
        }
    }

    public ProvaMessengerImpl(ProvaReagent prova, ProvaKnowledgeBase kb, String agent, String password, String machine, ProvaAgent esb) {
        this.prova = prova;
        this.kb = kb;
        this.agent = agent;
        this.password = password;
        this.machine = machine;
        this.esb = esb;
        this.timers = new ScheduledThreadPoolExecutor(5, new TimerThreadFactory());
    }

    /**
	 * Prepare a rcvMsg goal for sending on the main agent thread if the verb is
	 * 'self' or a thread chosen according to the conversation-id cid (if the
	 * verb is 'async' or other). The reactions corresponding to the same
	 * conversation-id are thus always run by the same thread.
	 * 
	 * The message is sent when the next rcvMsg literal is encountered or the
	 * whole query is complete.
	 */
    @Override
    public boolean prepareMsg(ProvaLiteral literal, List<ProvaLiteral> newLiterals, ProvaRule query) {
        ProvaDelayedCommand message = null;
        try {
            List<ProvaVariable> variables = query.getVariables();
            ProvaList terms = literal.getTerms();
            ProvaObject[] data = terms.getFixed();
            ProvaObject lt = data[0];
            if (lt instanceof ProvaVariablePtr) {
                ProvaVariablePtr varPtr = (ProvaVariablePtr) lt;
                lt = variables.get(varPtr.getIndex()).getRecursivelyAssigned();
            }
            String cid = "";
            if (lt instanceof ProvaConstant) {
                cid = (String) ((ProvaConstant) lt).getObject();
            } else if (lt instanceof ProvaVariable) {
                cid = generateCid();
                ((ProvaVariable) lt).setAssigned(ProvaConstantImpl.create(cid));
            } else return false;
            if (!(data[1] instanceof ProvaConstant)) return false;
            String protocol = ((ProvaConstant) data[1]).getObject().toString();
            if (!(data[2] instanceof ProvaConstant)) return false;
            String dest = ((ProvaConstant) data[2]).getObject().toString();
            ProvaList termsCopy = (ProvaList) terms.cloneWithVariables(variables);
            ProvaLiteral lit = kb.generateHeadLiteral("rcvMsg", termsCopy);
            ProvaRule goal = kb.generateGoal(new ProvaLiteral[] { lit, kb.generateLiteral("fail") });
            if ("async".equals(protocol)) {
                message = new ProvaMessageImpl(partitionKey(cid), goal, ProvaThreadpoolEnum.CONVERSATION);
            } else if ("task".equals(protocol)) {
                message = new ProvaMessageImpl(0, goal, ProvaThreadpoolEnum.TASK);
            } else if ("swing".equals(protocol)) {
                message = new ProvaMessageImpl(0, goal, ProvaThreadpoolEnum.SWING);
            } else if ("self".equals(protocol) || "0".equals(dest)) {
                message = new ProvaMessageImpl(0, goal, ProvaThreadpoolEnum.MAIN);
            } else if ("esb".equals(protocol)) {
                if (esb == null) return false;
                message = new ProvaESBMessageImpl(dest, termsCopy, esb);
            } else {
            }
        } catch (Exception e) {
            return false;
        }
        if (message != null) {
            List<ProvaDelayedCommand> delayed = ProvaResolutionInferenceEngineImpl.delayedCommands.get();
            delayed.add(message);
            return true;
        }
        return false;
    }

    public static long partitionKey(String cid) {
        long key = Math.abs(cid.hashCode());
        return key;
    }

    /**
	 * Submits asynchronously a rcvMsg goal scheduled on the main agent thread
	 * if the verb is 'self' or a thread chosen according to the conversation-id
	 * cid (if the verb is 'async' or other). The reactions corresponding to the
	 * same conversation-id are thus always run by the same thread.
	 */
    @Override
    public boolean sendMsg(ProvaLiteral literal, List<ProvaLiteral> newLiterals, ProvaRule query) {
        try {
            List<ProvaVariable> variables = query.getVariables();
            ProvaList terms = literal.getTerms();
            ProvaObject[] data = terms.getFixed();
            ProvaObject lt = data[0];
            if (lt instanceof ProvaVariablePtr) {
                ProvaVariablePtr varPtr = (ProvaVariablePtr) lt;
                lt = variables.get(varPtr.getIndex()).getRecursivelyAssigned();
            }
            String cid = "";
            if (lt instanceof ProvaConstant) {
                cid = (String) ((ProvaConstant) lt).getObject();
            } else if (lt instanceof ProvaVariable) {
                cid = generateCid();
                ((ProvaVariable) lt).setAssigned(ProvaConstantImpl.create(cid));
            } else return false;
            if (!(data[1] instanceof ProvaConstant)) return false;
            String protocol = ((ProvaConstant) data[1]).getObject().toString();
            ProvaObject destObject = data[2];
            if (destObject instanceof ProvaVariablePtr) {
                ProvaVariablePtr varPtr = (ProvaVariablePtr) destObject;
                destObject = variables.get(varPtr.getIndex()).getRecursivelyAssigned();
            }
            if (!(destObject instanceof ProvaConstant)) return false;
            String dest = ((ProvaConstant) destObject).getObject().toString();
            ProvaList termsCopy = (ProvaList) terms.cloneWithVariables(variables);
            ProvaLiteral lit = kb.generateHeadLiteral("rcvMsg", termsCopy);
            ProvaRule goal = kb.generateGoal(new ProvaLiteral[] { lit, kb.generateLiteral("fail") });
            if ("async".equals(protocol)) {
                prova.submitAsync(partitionKey(cid), goal, ProvaThreadpoolEnum.CONVERSATION);
                return true;
            } else if ("task".equals(protocol)) {
                prova.submitAsync(0, goal, ProvaThreadpoolEnum.TASK);
                return true;
            } else if ("swing".equals(protocol)) {
                prova.submitAsync(0, goal, ProvaThreadpoolEnum.SWING);
                return true;
            } else if ("self".equals(protocol) || "0".equals(dest)) {
                prova.submitAsync(0, goal, ProvaThreadpoolEnum.MAIN);
                return true;
            } else if ("esb".equals(protocol)) {
                if (esb == null) return false;
                ProvaDelayedCommand message = new ProvaESBMessageImpl(dest, termsCopy, esb);
                message.process(prova);
                return true;
            } else {
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public void sendReturnAsMsg(ProvaConstant cid, Object ret) {
        if (ret == null) ret = 0;
        ProvaList terms = ProvaListImpl.create(new ProvaObject[] { cid, ProvaConstantImpl.create("self"), ProvaConstantImpl.create("0"), ProvaConstantImpl.create("return"), ProvaConstantImpl.create(ret) });
        ProvaLiteral lit = kb.generateHeadLiteral("rcvMsg", terms);
        ProvaRule goal = kb.generateGoal(new ProvaLiteral[] { lit, kb.generateLiteral("fail") });
        prova.submitAsync(partitionKey(cid.getObject().toString()), goal, ProvaThreadpoolEnum.CONVERSATION);
    }

    @Override
    public boolean spawn(ProvaLiteral literal, List<ProvaLiteral> newLiterals, ProvaRule query) {
        List<ProvaVariable> variables = query.getVariables();
        ProvaList terms = (ProvaList) literal.getTerms();
        ProvaObject[] data = terms.getFixed();
        ProvaObject lt = data[0];
        if (lt instanceof ProvaVariablePtr) {
            ProvaVariablePtr varPtr = (ProvaVariablePtr) lt;
            lt = variables.get(varPtr.getIndex()).getRecursivelyAssigned();
        }
        if (lt instanceof ProvaVariable) {
            String cid = generateCid();
            ((ProvaVariable) lt).setAssigned(ProvaConstantImpl.create(cid));
        }
        prova.spawn((ProvaList) terms.cloneWithVariables(variables));
        return true;
    }

    @Override
    public String generateCid() {
        return prova.getAgent() + ':' + unique_iid.incrementAndGet();
    }

    public static WhereNode parse(String expr) throws Exception {
        ByteArrayInputStream rawinput = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(rawinput);
        WhereLexer lexer = new WhereLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        WhereParser parser = new WhereParser(tokens);
        CommonTree tree = (CommonTree) parser.expr().getTree();
        return WhereTreeVisitor.visit(tree);
    }

    @Override
    public boolean rcvMsg(ProvaGoal goal, List<ProvaLiteral> newLiterals, ProvaRule query, boolean mult) {
        ProvaLiteral literal = goal.getGoal();
        ProvaRule copy = query.cloneRule();
        List<ProvaVariable> variables = copy.getVariables();
        ProvaLiteral literalClone = (ProvaLiteral) literal.cloneWithVariables(variables);
        ProvaList terms = literalClone.getTerms();
        try {
            ProvaObject[] data = terms.getFixed();
            ProvaObject xid = data[0];
            if (!(xid instanceof ProvaConstant) && !(xid instanceof ProvaVariable)) return false;
            if (!(data[2] instanceof ProvaConstant) && !(data[2] instanceof ProvaVariable)) return false;
            final long ruleid = reaction_iid.incrementAndGet();
            final ProvaConstant tid = ProvaConstantImpl.create(ruleid);
            ProvaLiteral head = null;
            ProvaLiteral headControl = null;
            List<Object> groups = literal.getMetadata("group");
            List<Object> groupsAnd = literal.getMetadata("and");
            List<Object> groupsOr = literal.getMetadata("or");
            List<Object> groupsNot = literal.getMetadata("not");
            List<Object> groupsStop = literal.getMetadata("stop");
            List<Object> groupsOptional = literal.getMetadata("optional");
            List<Object> groupsCount = literal.getMetadata("count");
            List<Object> groupsSize = literal.getMetadata("size");
            List<Object> groupsPause = literal.getMetadata("pause");
            List<Object> groupsResume = literal.getMetadata("resume");
            List<Object> groupsPaused = literal.getMetadata("paused");
            Object size = null;
            Object sizeReset = null;
            Object sizeObject = null;
            if (groupsSize != null && groupsSize.size() != 0) {
                size = groupsSize.get(0);
                size = goal.lookupMetadata(size.toString(), variables);
                if (groupsSize.size() > 1) {
                    sizeReset = groupsSize.get(1);
                    sizeReset = goal.lookupMetadata(sizeReset.toString(), variables);
                    if (groupsSize.size() > 2) {
                        sizeObject = groupsSize.get(2);
                        sizeObject = goal.lookupMetadata(sizeObject.toString(), variables);
                    }
                }
            }
            Integer countMin = null;
            Integer countMax = null;
            Integer countMode = 0;
            if (groupsCount != null && groupsCount.size() != 0) {
                String s = groupsCount.get(0).toString();
                countMin = Integer.parseInt((String) goal.lookupMetadata(s, variables));
                if (groupsCount.size() > 1) {
                    s = groupsCount.get(1).toString();
                    countMax = Integer.parseInt((String) goal.lookupMetadata(s, variables));
                    if (groupsCount.size() > 2) {
                        s = groupsCount.get(2).toString();
                        if ("record".equals(s)) countMode = 1; else if ("strict".equals(s)) countMode = 2;
                    } else {
                        if (countMax == -1) {
                            countMax = countMin;
                            countMode = 1;
                        }
                    }
                } else {
                    if (countMin == -1) {
                        countMin = 0;
                        countMax = 0;
                        countMode = 1;
                    } else {
                        countMax = countMin;
                        countMode = 0;
                    }
                }
            }
            List<Object> groupsTimeout = literal.getMetadata("timeout");
            Object timeout = null;
            if (groupsTimeout != null && groupsTimeout.size() != 0) {
                timeout = groupsTimeout.get(0);
                timeout = goal.lookupMetadata(timeout.toString(), variables);
            }
            List<Object> groupsTimer = literal.getMetadata("timer");
            Object timer = null;
            Object timerReset = null;
            Object timerObject = null;
            if (groupsTimer != null && groupsTimer.size() != 0) {
                timer = groupsTimer.get(0);
                timer = goal.lookupMetadata(timer.toString(), variables);
                if (groupsTimer.size() > 1) {
                    timerReset = groupsTimer.get(1);
                    timerReset = goal.lookupMetadata(timerReset.toString(), variables);
                } else timerReset = timer;
                if (groupsTimer.size() > 2) {
                    timerObject = groupsTimer.get(2);
                    timerObject = goal.lookupMetadata(timerObject.toString(), variables);
                }
            }
            List<Object> groupsVar = literal.getMetadata("vars");
            List<Object> vars = null;
            if (groupsVar != null && groupsVar.size() != 0) {
                vars = new ArrayList<Object>();
                for (int i = 0; i < groupsVar.size(); i++) {
                    Object var = groupsVar.get(i);
                    var = goal.lookupMetadata(var.toString(), variables);
                    vars.add(var);
                }
            }
            List<Object> groupsWhere = literal.getMetadata("where");
            WhereNode where = null;
            if (groupsWhere != null && groupsWhere.size() != 0) {
                where = parse(groupsWhere.get(0).toString());
            }
            List<ProvaLiteral> body = new ArrayList<ProvaLiteral>();
            List<ProvaLiteral> guard = literalClone.getGuard();
            if (guard != null) {
                for (ProvaLiteral g : guard) body.add(g);
            }
            final ProvaObject poXID = data[0];
            final ProvaObject poProtocol = data[1];
            ProvaObject ctlProtocol = poProtocol instanceof ProvaConstant ? poProtocol : ProvaVariableImpl.create("CtlProtocol");
            ProvaVariable ctlFrom = ProvaVariableImpl.create("CtlFrom");
            ProvaGroup dynamic = null;
            ProvaRule temporalRule = null;
            ProvaList headList = ProvaListImpl.create(new ProvaObject[] { xid, tid, terms });
            ProvaList headControlList = ProvaListImpl.create(new ProvaObject[] { tid, ctlProtocol, ctlFrom, ProvaConstantImpl.create("eof"), terms });
            head = kb.generateLiteral("@temporal_rule", headList);
            headControl = kb.generateLiteral("@temporal_rule_control", (ProvaList) headControlList.cloneWithVariables(variables));
            RemoveList rl = new RemoveList(head.getPredicate(), headControl.getPredicate(), ruleid, (ProvaList) head.getTerms().getFixed()[2].cloneWithVariables(variables));
            dynamic = generateOrReuseDynamicGroup(goal, variables, ruleid, rl);
            ProvaList removeList = ProvaListImpl.create(new ProvaObject[] { ProvaConstantImpl.create(head.getPredicate()), ProvaConstantImpl.create(headControl.getPredicate()), tid, head.getTerms() });
            final ProvaLiteral removeLiteral = kb.generateLiteral("@temporal_rule_remove", removeList);
            if (groupsWhere != null) {
                dynamic.addWhere(where);
            }
            if (dynamic != null && dynamic.getParent() != null) {
                ProvaList addAndResultList = ProvaListImpl.create(new ProvaObject[] { ProvaConstantImpl.create(dynamic.getParent().getDynamicGroup()), head.getTerms() });
                removeLiteral.setMetadata("rule", Arrays.asList(new Object[] { dynamic.getParent().getDynamicGroup() }));
                body.add(kb.generateLiteral("@add_group_result", addAndResultList));
                if (groupsNot != null) {
                    removeLiteral.setMetadata("not", Arrays.asList(new Object[] {}));
                    rl.setNot(true);
                }
                if (groupsTimeout != null) removeLiteral.setMetadata("timeout", groupsTimeout);
                if (groupsStop != null) {
                    removeLiteral.setMetadata("stop", groupsStop);
                    rl.setOptional(groupsStop.isEmpty());
                }
                if (groupsOptional != null) rl.setOptional(true);
                if (groupsPause != null) removeLiteral.setMetadata("pause", groupsPause);
                if (groupsResume != null) removeLiteral.setMetadata("resume", groupsResume);
                if (groupsPaused != null) {
                    removeLiteral.setMetadata("paused", groupsPaused);
                    dynamic.getParent().pause(ruleid);
                }
            } else if (groups != null && groups.size() != 0) {
                ProvaList addAndResultList = ProvaListImpl.create(new ProvaObject[] { ProvaConstantImpl.create(dynamic.getDynamicGroup()), head.getTerms() });
                removeLiteral.setMetadata("rule", Arrays.asList(new Object[] { dynamic.getDynamicGroup() }));
                body.add(kb.generateLiteral("@add_group_result", addAndResultList));
                if (groupsNot != null) {
                    removeLiteral.setMetadata("not", Arrays.asList(new Object[] {}));
                    rl.setNot(true);
                }
                if (groupsTimeout != null) removeLiteral.setMetadata("timeout", groupsTimeout);
                if (groupsStop != null) {
                    removeLiteral.setMetadata("stop", groupsStop);
                    rl.setOptional(groupsStop.isEmpty());
                }
                if (groupsOptional != null) rl.setOptional(true);
                if (groupsPause != null) removeLiteral.setMetadata("pause", groupsPause);
                if (groupsResume != null) removeLiteral.setMetadata("resume", groupsResume);
                if (groupsPaused != null) {
                    removeLiteral.setMetadata("paused", groupsPaused);
                    dynamic.pause(ruleid);
                }
                if (mult) {
                    dynamic.setTemplate(true);
                    mult = false;
                }
            }
            if (!mult) {
                body.add(removeLiteral);
            }
            ProvaLiteral[] queryLiterals = copy.getBody();
            for (int i = 1; i < queryLiterals.length; i++) {
                body.add((ProvaLiteral) queryLiterals[i].cloneWithVariables(variables));
            }
            if (groupsSize != null) {
                if (sizeObject != null) removeLiteral.setMetadata("size", Arrays.asList(new Object[] { size, sizeReset, sizeObject })); else if (sizeReset != null) removeLiteral.setMetadata("size", Arrays.asList(new Object[] { size, sizeReset })); else {
                    removeLiteral.setMetadata("size", Arrays.asList(new Object[] { size }));
                    rl.setOptional(Integer.parseInt((String) size) <= 0);
                }
            }
            if (groupsCount != null) {
                if (groupsAnd != null || groupsOr != null) {
                    dynamic.setCountMax(countMax);
                }
                removeLiteral.setMetadata("count", Arrays.asList(new Object[] { countMin, countMax, countMode }));
                rl.setOptional(countMin <= 0);
            }
            if (groupsTimer != null) {
                if (timerObject != null) removeLiteral.setMetadata("timer", Arrays.asList(new Object[] { timer, timerReset, timerObject })); else if (timerReset != null) removeLiteral.setMetadata("timer", Arrays.asList(new Object[] { timer, timerReset })); else removeLiteral.setMetadata("timer", Arrays.asList(new Object[] { timer }));
            }
            if (groupsVar != null) {
                removeLiteral.setMetadata("vars", vars);
            }
            synchronized (kb) {
                temporalRule = kb.generateRule(head, body.toArray(new ProvaLiteral[] {}));
                kb.generateRule(headControl, new ProvaLiteral[] { removeLiteral });
                if (log.isInfoEnabled()) log.info("Added end-of-reaction: " + temporalRule.getSourceCode());
            }
            if (dynamic != null && dynamic.isOperatorConfigured()) {
                temporalRule.setMetadata("group", Arrays.asList(new Object[] { dynamic.getDynamicGroup() }));
                removeLiteral.setMetadata("group", Arrays.asList(new Object[] { dynamic.getDynamicGroup() }));
                if (timeout != null) {
                    long delay = ProvaTimeUtils.timeIntervalInMilliseconds(timeout);
                    List<ProvaDelayedCommand> delayed = ProvaResolutionInferenceEngineImpl.delayedCommands.get();
                    if (groupsAnd != null || groupsOr != null) {
                        delayed.add(new ProvaScheduleGroupCleanupImpl(dynamic, delay));
                    } else {
                        delayed.add(new ProvaScheduleGroupMemberCleanupImpl(xid, dynamic, head.getPredicate(), headControl.getPredicate(), ruleid, delay, 0, removeLiteral.getMetadata()));
                    }
                }
            } else if (dynamic == null && timeout != null) {
                if (timeout != null) {
                    long delay = ProvaTimeUtils.timeIntervalInMilliseconds(timeout);
                    scheduleCleanup(xid, dynamic, head.getPredicate(), headControl.getPredicate(), ruleid, delay, 0, removeLiteral.getMetadata());
                }
            } else if (dynamic != null && timeout != null) {
                if (timeout != null) {
                    long delay = ProvaTimeUtils.timeIntervalInMilliseconds(timeout);
                    List<ProvaDelayedCommand> delayed = ProvaResolutionInferenceEngineImpl.delayedCommands.get();
                    delayed.add(new ProvaScheduleGroupMemberCleanupImpl(xid, dynamic, head.getPredicate(), headControl.getPredicate(), ruleid, delay, 0, removeLiteral.getMetadata()));
                }
            } else if (dynamic != null && timer != null) {
                if (timer != null) {
                    long delay = ProvaTimeUtils.timeIntervalInMilliseconds(timer);
                    long period = ProvaTimeUtils.timeIntervalInMilliseconds(timerReset);
                    List<ProvaDelayedCommand> delayed = ProvaResolutionInferenceEngineImpl.delayedCommands.get();
                    delayed.add(new ProvaScheduleGroupMemberCleanupImpl(xid, dynamic, head.getPredicate(), headControl.getPredicate(), ruleid, delay, period, removeLiteral.getMetadata()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Possible formating error in rcvMsg: " + e.getMessage());
        }
        return false;
    }

    private ProvaGroup generateOrReuseDynamicGroup(ProvaGoal goal, List<ProvaVariable> variables, long ruleid, RemoveList rl) {
        ProvaLiteral literal = goal.getGoal();
        ProvaGroup g = null;
        List<Object> groups = literal.getMetadata("and");
        String dynamicGroup = null;
        if (groups != null && groups.size() != 0) {
            dynamicGroup = generateOrReuseDynamicGroup(groups.get(0).toString(), goal, variables);
            if (tlDynamic.get() == null) tlDynamic.set(new HashMap<String, ProvaGroup>());
            Map<String, ProvaGroup> d2g = tlDynamic.get();
            g = d2g.get(dynamicGroup);
            if (g == null) {
                g = new ProvaAndGroupImpl(dynamicGroup, groups.get(0).toString());
            } else if (g instanceof ProvaAndGroupImpl) {
                g.addTimeoutEntry(rl);
                return g;
            } else if (g instanceof ProvaBasicGroupImpl) {
                g = new ProvaAndGroupImpl(g);
            } else throw new RuntimeException("Event group can only have one operator");
            d2g.put(dynamicGroup, g);
            dynamic2Group.put(dynamicGroup, g);
            g.start(rl, ruleid2Group);
        }
        groups = literal.getMetadata("or");
        if (groups != null && groups.size() != 0) {
            if (dynamicGroup != null) throw new RuntimeException("Multiple operators on event groups are not allowed");
            dynamicGroup = generateOrReuseDynamicGroup(groups.get(0).toString(), goal, variables);
            if (tlDynamic.get() == null) tlDynamic.set(new HashMap<String, ProvaGroup>());
            Map<String, ProvaGroup> d2g = tlDynamic.get();
            g = d2g.get(dynamicGroup);
            if (g == null) {
                g = new ProvaOrGroupImpl(dynamicGroup, groups.get(0).toString());
            } else if (g instanceof ProvaOrGroupImpl) {
                g.addTimeoutEntry(rl);
                return g;
            } else if (g instanceof ProvaBasicGroupImpl) {
                g = new ProvaOrGroupImpl(g);
            } else throw new RuntimeException("Event group can only have one operator");
            d2g.put(dynamicGroup, g);
            dynamic2Group.put(dynamicGroup, g);
            outcomeRuleid2Group.put(ruleid, g);
            g.start(rl, ruleid2Group);
        }
        ProvaGroup gg = null;
        groups = literal.getMetadata("group");
        if (groups != null && groups.size() != 0) {
            dynamicGroup = generateOrReuseDynamicGroup(groups.get(0).toString(), goal, variables);
            if (tlDynamic.get() == null) tlDynamic.set(new HashMap<String, ProvaGroup>());
            Map<String, ProvaGroup> d2g = tlDynamic.get();
            gg = d2g.get(dynamicGroup);
            if (gg == null) {
                gg = new ProvaBasicGroupImpl(dynamicGroup, groups.get(0).toString());
                d2g.put(dynamicGroup, gg);
            } else gg.setExtended(true);
            gg.addRemoveEntry(ruleid, rl);
            List<Object> groupsId = literal.getMetadata("id");
            if (groupsId != null && groupsId.size() != 0) {
                gg.putId2ruleid(groupsId.get(0).toString(), ruleid);
            }
            if (gg.isOperatorConfigured()) gg.start(rl, ruleid2Group);
            if (g == null) {
                return gg;
            }
            g.setParent(gg);
            gg.addChild(g);
        }
        return g;
    }

    private String generateOrReuseDynamicGroup(String group, ProvaGoal goal, List<ProvaVariable> variables) {
        String dynamicGroup;
        if (Character.isUpperCase(group.charAt(0))) {
            dynamicGroup = goal.lookupMetadata(group, variables).toString();
        } else {
            if (tlStatic2Dynamic.get() == null) tlStatic2Dynamic.set(new HashMap<String, String>());
            Map<String, String> s2d = tlStatic2Dynamic.get();
            dynamicGroup = s2d.get(group);
            if (dynamicGroup == null) {
                dynamicGroup = generateCid();
                s2d.put(group, dynamicGroup);
                dynamic2Static.put(dynamicGroup, group);
            }
        }
        return dynamicGroup;
    }

    @Override
    public boolean rcvMsgP(ProvaGoal goal, List<ProvaLiteral> newLiterals, ProvaRule query, boolean mult) {
        ProvaLiteral literal = goal.getGoal();
        ProvaRule copy = query.cloneRule();
        List<ProvaVariable> variables = copy.getVariables();
        ProvaLiteral literalClone = (ProvaLiteral) literal.cloneWithVariables(variables);
        ProvaList terms = literalClone.getTerms();
        try {
            ProvaObject[] data = terms.getFixed();
            if (!(data[0] instanceof ProvaList) || !(data[1] instanceof ProvaList) || !(data[2] instanceof ProvaList)) return false;
            ProvaObject[] oInbound = ((ProvaList) data[0]).getFixed();
            ProvaObject[] oOutbound = ((ProvaList) data[1]).getFixed();
            ProvaObject[] reaction = ((ProvaList) data[2]).getFixed();
            ProvaObject[] reactionFixed = new ProvaObject[reaction.length - 1];
            System.arraycopy(reaction, 1, reactionFixed, 0, reactionFixed.length);
            ProvaList reactionTerms = ProvaListImpl.create(reactionFixed);
            ProvaObject xid = reactionFixed[0];
            if (!(xid instanceof ProvaConstant) && !(xid instanceof ProvaVariable)) return false;
            if (!(reaction[2] instanceof ProvaConstant) && !(reaction[2] instanceof ProvaVariable)) return false;
            final long ruleid = reaction_iid.incrementAndGet();
            ProvaConstant tid = ProvaConstantImpl.create(ruleid);
            List<String> inbound = new ArrayList<String>();
            for (ProvaObject o : oInbound) {
                String s = ((ProvaConstant) o).getObject().toString();
                inbound.add(s);
                List<Long> ruleids = inbound2ruleids.get(s);
                if (ruleids == null) {
                    ruleids = new ArrayList<Long>();
                    inbound2ruleids.put(s, ruleids);
                }
                ruleids.add(ruleid);
            }
            List<String> outbound = new ArrayList<String>();
            for (ProvaObject o : oOutbound) outbound.add(((ProvaConstant) o).getObject().toString());
            ruleid2outbound.put(ruleid, outbound);
            ProvaLiteral head = null;
            ProvaLiteral headControl = null;
            synchronized (kb) {
                ProvaList headList = ProvaListImpl.create(new ProvaObject[] { xid, tid, reactionTerms });
                head = kb.generateLiteral("@temporal_rule", headList);
                ProvaVariable ctlProtocol = ProvaVariableImpl.create("CtlProtocol");
                ProvaVariable ctlFrom = ProvaVariableImpl.create("CtlFrom");
                headList = ProvaListImpl.create(new ProvaObject[] { tid, ctlProtocol, ctlFrom, ProvaConstantImpl.create("eof"), terms });
                headControl = kb.generateLiteral("@temporal_rule_control", (ProvaList) headList.cloneWithVariables(variables));
                List<ProvaLiteral> body = new ArrayList<ProvaLiteral>();
                ProvaList removeList = ProvaListImpl.create(new ProvaObject[] { ProvaConstantImpl.create(head.getPredicate()), ProvaConstantImpl.create(headControl.getPredicate()), tid, head.getTerms() });
                List<ProvaLiteral> guard = literalClone.getGuard();
                if (guard != null) {
                    for (ProvaLiteral g : guard) body.add(g);
                }
                if (data.length > 3 && data[3] instanceof ProvaList && ((ProvaConstant) ((ProvaList) data[3]).getFixed()[0]).getObject().toString().equals("condition")) {
                    String symbol = ((ProvaConstant) ((ProvaList) ((ProvaList) data[3]).getFixed()[1]).getFixed()[0]).getObject().toString();
                    body.add(kb.generateLiteral(symbol, ((ProvaList) ((ProvaList) data[3]).getFixed()[1]).getFixed(), 1));
                }
                if (!mult) body.add(kb.generateLiteral("@temporal_rule_remove", removeList));
                ProvaLiteral[] queryLiterals = copy.getBody();
                for (int i = 1; i < queryLiterals.length; i++) {
                    body.add((ProvaLiteral) queryLiterals[i].cloneWithVariables(variables));
                }
                @SuppressWarnings("unused") ProvaRule temporalRule = kb.generateRule(head, body.toArray(new ProvaLiteral[] {}));
                temporalRule = kb.generateRule(headControl, new ProvaLiteral[] { kb.generateLiteral("@temporal_rule_remove", removeList) });
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public void scheduleCleanup(final ProvaGroup dynamic, long delay) {
        dynamic.setTimeout(delay);
        final String dynamicGroup = dynamic.getDynamicGroup();
        TimerTask cleanup = new TimerTask() {

            @Override
            public void run() {
                synchronized (kb) {
                    removeGroup(dynamicGroup, true);
                }
            }
        };
        ScheduledFuture<?> future = timers.schedule(cleanup, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void scheduleCleanup(final ProvaObject xid, final ProvaGroup group, final ProvaPredicate p1, final ProvaPredicate p2, final long ruleid, final long delay, final long period, final Map<String, List<Object>> metadata) {
        final String protocol = (xid == null || xid instanceof ProvaVariable) ? "self" : "async";
        final String cid = (xid == null || xid instanceof ProvaVariable) ? "basic" : ((ProvaConstant) xid).getObject().toString();
        TimerTask cleanup = new TimerTask() {

            @Override
            public void run() {
                if ("self".equals(protocol)) prova.executeTask(0, new Runnable() {

                    @Override
                    public void run() {
                        synchronized (kb) {
                            removeTemporalRule(p1, p2, ruleid, true, null, metadata);
                        }
                    }
                }, ProvaThreadpoolEnum.MAIN); else prova.executeTask(partitionKey(cid), new Runnable() {

                    @Override
                    public void run() {
                        synchronized (kb) {
                            removeTemporalRule(p1, p2, ruleid, true, null, metadata);
                        }
                    }
                }, ProvaThreadpoolEnum.CONVERSATION);
            }
        };
        ScheduledFuture<?> future;
        if (period == 0) {
            future = timers.schedule(cleanup, delay, TimeUnit.MILLISECONDS);
        } else future = timers.scheduleAtFixedRate(cleanup, delay, period, TimeUnit.MILLISECONDS);
        if (group != null) group.setTimerFuture(future);
    }

    @Override
    public void addMsg(ProvaList terms) {
        ProvaObject[] data = terms.getFixed();
        ProvaObject lt = data[0];
        String cid = "";
        if (lt instanceof ProvaConstant) cid = (String) ((ProvaConstant) lt).getObject();
        String prot = ((ProvaConstant) data[1]).getObject().toString();
        ProvaLiteral lit = kb.generateHeadLiteral("rcvMsg", terms);
        ProvaRule goal = kb.generateGoal(new ProvaLiteral[] { lit, kb.generateLiteral("fail") });
        ProvaThreadpoolEnum dest = ProvaThreadpoolEnum.TASK;
        if ("self".equals(prot)) dest = ProvaThreadpoolEnum.MAIN; else if ("async".equals(prot)) dest = ProvaThreadpoolEnum.CONVERSATION;
        prova.submitAsync(partitionKey(cid), goal, dest);
    }

    @Override
    public synchronized void removeTemporalRule(ProvaPredicate predicate, ProvaPredicate predicate2, long key, boolean recursive, ProvaList reaction, Map<String, List<Object>> metadata) {
        if (log.isInfoEnabled()) log.info("Removing " + reaction + " at " + key + " with " + metadata);
        if (reaction == null) log.info("Removing on timeout");
        ProvaGroup group = ruleid2Group.get(key);
        List<Object> groups = null;
        if (metadata != null) groups = (List<Object>) metadata.get("group");
        boolean avoidRemovingRule = false;
        if (group == null && metadata == null) {
            group = outcomeRuleid2Group.get(key);
        }
        if (group == null && metadata != null) {
            String dynamic = null;
            if (groups != null) {
                dynamic = groups.get(0).toString();
                group = dynamic2Group.get(dynamic);
            }
        }
        if (group != null) {
            if (group.isPermanent() || group.isTemplate()) avoidRemovingRule = true;
            EventDetectionStatus detectionStatus = group.eventDetected(kb, prova, key, reaction, metadata, ruleid2Group);
            if (detectionStatus == EventDetectionStatus.complete) {
                removeGroup(group.getDynamicGroup(), recursive);
            } else if (detectionStatus == EventDetectionStatus.preserved) return;
        } else if (metadata != null && metadata.containsKey("count")) {
            List<Object> countList = metadata.get("count");
            int count = (Integer) countList.get(0);
            if (count == 0) return;
            countList.set(0, --count);
            if (count != 0) return;
        }
        if (avoidRemovingRule) return;
        predicate.getClauseSet().removeClauses(key, 1);
        predicate2.getClauseSet().removeClauses(key);
        if (ruleid2outbound.get(key) == null) return;
        List<String> outbound = ruleid2outbound.get(key);
        for (String s : outbound) {
            List<Long> inbound = inbound2ruleids.get(s);
            if (inbound == null) continue;
            if (recursive) {
                for (Iterator<Long> iter = inbound.iterator(); iter.hasNext(); ) {
                    long i = iter.next();
                    iter.remove();
                    removeTemporalRule(predicate, predicate2, i, false, reaction, metadata);
                }
            }
            inbound2ruleids.remove(s);
        }
        ruleid2outbound.remove(key);
        outcomeRuleid2Group.remove(key);
    }

    /**
	 * Remove a dynamic group
	 * 
	 * @param dynamicGroup
	 * @param recursive
	 */
    private void removeGroup(String dynamicGroup, boolean recursive) {
        ProvaGroup group = dynamic2Group.get(dynamicGroup);
        if (group != null && !group.isExtended()) {
            group.stop();
        }
        List<ProvaDelayedCommand> delayed = ProvaResolutionInferenceEngineImpl.delayedCommands.get();
        if (delayed == null) {
            cleanupGroup(dynamicGroup);
            return;
        }
        delayed.add(new ProvaGroupCleanupImpl(dynamicGroup));
        if (tlStatic2Dynamic.get() == null) tlStatic2Dynamic.set(new HashMap<String, String>());
        Map<String, String> s2d = tlStatic2Dynamic.get();
        s2d.put(dynamic2Static.get(dynamicGroup), dynamicGroup);
        if (group != null) {
            if (tlDynamic.get() == null) tlDynamic.set(new HashMap<String, ProvaGroup>());
            Map<String, ProvaGroup> d2g = tlDynamic.get();
            d2g.put(dynamicGroup, group);
        }
    }

    @Override
    public void cleanupGroup(String dynamicGroup) {
        ProvaGroup group = dynamic2Group.get(dynamicGroup);
        if (group != null && group.isExtended()) {
            group.setExtended(false);
            return;
        }
        dynamic2Static.remove(dynamicGroup);
        if (group != null) {
            group.cleanup(kb, prova, ruleid2Group, dynamic2Group);
        }
    }

    @Override
    public void addGroupResult(ProvaList terms) {
        ProvaObject[] fixed = terms.getFixed();
        String dynamicGroup = (String) ((ProvaConstant) fixed[0]).getObject();
        ProvaGroup group = dynamic2Group.get(dynamicGroup);
        if (group != null) {
            if (group.isTemplate()) {
                dynamicGroup = this.generateCid();
                group = group.clone();
                group.setDynamicGroup(dynamicGroup);
                group.setTemplate(false);
                group.start(ruleid2Group);
                dynamic2Group.put(dynamicGroup, group);
                if (log.isInfoEnabled()) {
                    log.info("Group " + dynamicGroup + " is a template");
                    log.info("Group " + dynamicGroup + " is concrete");
                }
            }
            group.addResult((ProvaList) ((ProvaList) fixed[1]).getFixed()[2]);
            if (tlStatic2Dynamic.get() == null) tlStatic2Dynamic.set(new HashMap<String, String>());
            Map<String, String> s2d = tlStatic2Dynamic.get();
            String dynamicGroupPrev = s2d.get(group.getStaticGroup());
            if (dynamicGroupPrev == null) {
                s2d.put(group.getStaticGroup(), dynamicGroup);
            }
            if (tlDynamic.get() == null) tlDynamic.set(new HashMap<String, ProvaGroup>());
            Map<String, ProvaGroup> d2g = tlDynamic.get();
            ProvaGroup gg = d2g.get(dynamicGroup);
            if (gg == null) {
                d2g.put(dynamicGroup, group);
            }
        }
    }

    public static void cleanupThreadlocals() {
        tlStatic2Dynamic.remove();
        tlDynamic.remove();
    }

    @Override
    public void stop() {
        timers.shutdownNow();
    }
}
