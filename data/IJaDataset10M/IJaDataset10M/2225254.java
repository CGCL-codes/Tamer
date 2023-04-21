package com.googlecode.sarasvati.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.ArcTokenSetMember;
import com.googlecode.sarasvati.CustomNode;
import com.googlecode.sarasvati.Engine;
import com.googlecode.sarasvati.ExecutionType;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.GuardResult;
import com.googlecode.sarasvati.JoinAction;
import com.googlecode.sarasvati.JoinResult;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.NodeTokenSetMember;
import com.googlecode.sarasvati.ProcessState;
import com.googlecode.sarasvati.SarasvatiException;
import com.googlecode.sarasvati.TokenSet;
import com.googlecode.sarasvati.env.Env;
import com.googlecode.sarasvati.env.TokenSetMemberEnv;
import com.googlecode.sarasvati.event.ArcTokenEvent;
import com.googlecode.sarasvati.event.DefaultExecutionEventQueue;
import com.googlecode.sarasvati.event.EventActionType;
import com.googlecode.sarasvati.event.EventActions;
import com.googlecode.sarasvati.event.ExecutionEvent;
import com.googlecode.sarasvati.event.ExecutionEventType;
import com.googlecode.sarasvati.event.ExecutionListener;
import com.googlecode.sarasvati.event.GraphDefinedEventListenerInvoker;
import com.googlecode.sarasvati.event.NodeTokenEvent;
import com.googlecode.sarasvati.event.ProcessDefinedEventListenerInvoker;
import com.googlecode.sarasvati.event.ProcessEvent;
import com.googlecode.sarasvati.join.lang.JoinLangEnv;
import com.googlecode.sarasvati.join.lang.JoinLangEnvImpl;
import com.googlecode.sarasvati.rubric.RubricCompiler;
import com.googlecode.sarasvati.rubric.env.DefaultRubricEnv;
import com.googlecode.sarasvati.rubric.env.DefaultRubricFunctionRepository;
import com.googlecode.sarasvati.rubric.env.RubricEnv;
import com.googlecode.sarasvati.script.ScriptEnv;
import com.googlecode.sarasvati.visitor.BacktrackTokenVisitor;
import com.googlecode.sarasvati.visitor.TokenTraversals;

/**
 * Contains all the engine logic which is not backend specific.
 * <p>
 * Instances of BaseEngine have local state which is not thread-safe. Create
 * a new Engine instance for each thread that needs one.
 *
 * @author Paul Lorenz
 */
public abstract class BaseEngine implements Engine {

    protected static final String DEFAULT_APPLICATION_CONTEXT = "";

    protected static final Map<String, DefaultExecutionEventQueue> globalQueueMap = new ConcurrentHashMap<String, DefaultExecutionEventQueue>();

    protected boolean arcExecutionStarted = false;

    protected final List<ArcToken> asyncQueue = new LinkedList<ArcToken>();

    protected final DefaultExecutionEventQueue globalEventQueue;

    protected final String applicationContext;

    protected BaseEngine parentEngine;

    /**
   * Creates a new Engine with the given application context.
   * Each application context has its own set of global listeners.
   *
   * This allows different applications running the same JVM to
   * have different sets of listeners without having to add
   * them at the process level.

   * @param applicationContext The application context
   */
    public BaseEngine(final String applicationContext) {
        this.applicationContext = applicationContext;
        this.globalEventQueue = getGlobalQueueForContext(applicationContext);
    }

    private DefaultExecutionEventQueue getGlobalQueueForContext(final String context) {
        DefaultExecutionEventQueue queue = globalQueueMap.get(context);
        if (queue == null) {
            queue = DefaultExecutionEventQueue.newCopyOnWriteListInstance();
            contributeGlobalListeners(queue);
            globalQueueMap.put(context, queue);
        }
        return queue;
    }

    /**
   * Provides a subclass to override which execution event listeners are added to
   * new global queues. By default this adds the following listeners:
   * <ul>
   *  <li>{@link TokenSetCompletionListener}</li>
   *  <li>{@link GraphDefinedEventListenerInvoker}</li>
   *  <li>{@link ProcessDefinedEventListenerInvoker}</li>
   * </ul>
   *
   * @param queue The new global queue
   */
    protected void contributeGlobalListeners(final DefaultExecutionEventQueue queue) {
        queue.addListener(new TokenSetCompletionListener(), ExecutionEventType.ARC_TOKEN_COMPLETED, ExecutionEventType.NODE_TOKEN_COMPLETED);
        queue.addListener(new GraphDefinedEventListenerInvoker());
        queue.addListener(new ProcessDefinedEventListenerInvoker());
    }

    @Override
    public GraphProcess startProcess(final String graphName) {
        return startProcess(graphName, null);
    }

    @Override
    public GraphProcess startProcess(final String graphName, final Env initialEnv) {
        final Graph graph = getRepository().getLatestGraph(graphName);
        if (graph == null) {
            throw new SarasvatiException("No graph found with name '" + graphName + "'");
        }
        return startProcess(graph, initialEnv);
    }

    @Override
    public GraphProcess startProcess(final Graph graph) {
        return startProcess(graph, null);
    }

    @Override
    public GraphProcess startProcess(final Graph graph, final Env env) {
        if (graph == null) {
            throw new NullPointerException("Can not start process for a null graph");
        }
        final GraphProcess process = getFactory().newProcess(graph);
        if (env != null) {
            process.getEnv().importEnv(env);
        }
        ProcessEvent.fireCreatedEvent(this, process);
        startProcess(process);
        return process;
    }

    @Override
    public void startProcess(final GraphProcess process) {
        process.setState(ProcessState.Executing);
        ProcessEvent.fireStartedEvent(this, process);
        arcExecutionStarted = true;
        final List<Node> startNodes = process.getGraph().getStartNodes();
        if (startNodes.isEmpty()) {
            throw new SarasvatiException("Cannot start a workflow which has no start nodes! " + "Please check your process definition: '" + process.getGraph().getName());
        }
        try {
            for (final Node startNode : startNodes) {
                final NodeToken startToken = getFactory().newNodeToken(process, startNode, new ArrayList<ArcToken>(0));
                NodeTokenEvent.fireCreatedEvent(this, startToken);
                process.addNodeToken(startToken);
                executeNode(process, startToken);
            }
            executeQueuedArcTokens(process);
        } finally {
            arcExecutionStarted = false;
            drainAsyncQueue(process);
        }
        if (process.isExecuting()) {
            checkForCompletion(process);
        }
    }

    @Override
    public void cancelProcess(final GraphProcess process) {
        process.setState(ProcessState.PendingCancel);
        final EventActions actions = ProcessEvent.firePendingCancelEvent(this, process);
        if (!actions.isEventTypeIncluded(EventActionType.DELAY_PROCESS_FINALIZE_CANCEL)) {
            finalizeCancel(process);
        }
    }

    @Override
    public void finalizeComplete(final GraphProcess process) {
        process.setState(ProcessState.Completed);
        ProcessEvent.fireCompletedEvent(this, process);
        final NodeToken parentToken = process.getParentToken();
        if (parentToken != null) {
            final Engine engine = getParentEngine() == null ? newEngine(false) : getParentEngine();
            engine.complete(parentToken, Arc.DEFAULT_ARC);
        }
    }

    @Override
    public void finalizeCancel(final GraphProcess process) {
        process.setState(ProcessState.Canceled);
        ProcessEvent.fireCanceledEvent(this, process);
    }

    private void executeArc(final GraphProcess process, final ArcToken token) {
        if (token.isPending()) {
            token.markProcessed();
            ArcTokenEvent.fireProcessedEvent(this, token);
            process.addActiveArcToken(token);
            final Node targetNode = token.getArc().getEndNode();
            final JoinResult result = targetNode.getJoinStrategy(token.getArc()).performJoin(this, token);
            if (JoinAction.Complete == result.getJoinAction()) {
                completeExecuteArc(process, targetNode, result.getArcTokensCompletingJoin());
            } else if (JoinAction.Merge == result.getJoinAction()) {
                for (final ArcToken arcToken : result.getArcTokensCompletingJoin()) {
                    result.getMergeTarget().getParentTokens().add(arcToken);
                    completeArcToken(process, arcToken, result.getMergeTarget());
                    ArcTokenEvent.fireMergedEvent(this, arcToken);
                }
            }
        }
    }

    private void completeExecuteArc(final GraphProcess process, final Node targetNode, final Collection<ArcToken> tokens) {
        final NodeToken nodeToken = getFactory().newNodeToken(process, targetNode, new ArrayList<ArcToken>(tokens));
        process.addNodeToken(nodeToken);
        final Set<TokenSet> tokenSets = new HashSet<TokenSet>();
        for (final ArcToken token : tokens) {
            for (final ArcTokenSetMember setMember : token.getTokenSetMemberships()) {
                final TokenSet tokenSet = setMember.getTokenSet();
                if (!tokenSet.isComplete() && !tokenSets.contains(tokenSet)) {
                    tokenSets.add(tokenSet);
                    final NodeTokenSetMember newSetMember = getFactory().newNodeTokenSetMember(tokenSet, nodeToken, setMember.getMemberIndex());
                    tokenSet.getActiveNodeTokens(this).add(nodeToken);
                    nodeToken.getTokenSetMemberships().add(newSetMember);
                }
            }
        }
        NodeTokenEvent.fireCreatedEvent(this, nodeToken);
        for (final ArcToken token : tokens) {
            completeArcToken(process, token, nodeToken);
        }
        executeNode(process, nodeToken);
    }

    private void completeArcToken(final GraphProcess process, final ArcToken token, final NodeToken child) {
        process.removeActiveArcToken(token);
        if (token.isPending()) {
            token.markProcessed();
            ArcTokenEvent.fireProcessedEvent(this, token);
        }
        token.markComplete(child);
        ArcTokenEvent.fireCompletedEvent(this, token);
    }

    protected void executeNode(final GraphProcess process, final NodeToken token) {
        final GuardResult response = token.getNode().guard(this, token);
        token.setGuardAction(response.getGuardAction());
        switch(response.getGuardAction()) {
            case AcceptToken:
                process.addActiveNodeToken(token);
                final EventActions actions = NodeTokenEvent.fireAcceptedEvent(this, token);
                if (!actions.isEventTypeIncluded(EventActionType.DELAY_NODE_EXECUTION)) {
                    token.getNode().execute(this, token);
                    NodeTokenEvent.fireExecutedEvent(this, token);
                }
                break;
            case DiscardToken:
                token.markComplete();
                process.removeActiveNodeToken(token);
                NodeTokenEvent.fireDiscardedEvent(this, token);
                NodeTokenEvent.fireCompletedEvent(this, token);
                break;
            case SkipNode:
                process.addActiveNodeToken(token);
                NodeTokenEvent.fireSkippedEvent(this, token, response.getExitArcForSkip());
                complete(token, response.getExitArcForSkip());
                break;
        }
    }

    @Override
    public void complete(final NodeToken token, final String arcName) {
        final GraphProcess process = token.getProcess();
        completeNodeExecution(token, arcName, false);
        if (process.isExecuting() && !arcExecutionStarted) {
            executeQueuedArcTokens(process);
        }
    }

    @Override
    public void complete(final NodeToken token, final String... arcNames) {
        final GraphProcess process = token.getProcess();
        completeNodeExecution(token, false, arcNames);
        if (process.isExecuting() && !arcExecutionStarted) {
            executeQueuedArcTokens(process);
        }
    }

    @Override
    public void completeAsynchronous(final NodeToken token, final String arcName) {
        completeNodeExecution(token, arcName, true);
    }

    @Override
    public void completeAsynchronous(final NodeToken token, final String... arcNames) {
        completeNodeExecution(token, true, arcNames);
    }

    @Override
    public void completeWithNewTokenSet(final NodeToken token, final String arcName, final String tokenSetName, final int numberOfTokens, final boolean asynchronous, final Env initialEnv, final Map<String, List<?>> initialMemberEnv) {
        final GraphProcess process = token.getProcess();
        if (!process.isExecuting() || token.isComplete()) {
            return;
        }
        completeNodeToken(process, token, arcName);
        final List<Arc> outArcs = process.getGraph().getOutputArcs(token.getNode(), arcName);
        if (!outArcs.isEmpty()) {
            final TokenSet tokenSet = getFactory().newTokenSet(process, tokenSetName, numberOfTokens);
            if (initialEnv != null) {
                tokenSet.getEnv().importEnv(initialEnv);
            }
            if (initialMemberEnv != null) {
                final TokenSetMemberEnv memberEnv = tokenSet.getMemberEnv();
                for (final Map.Entry<String, List<?>> entry : initialMemberEnv.entrySet()) {
                    memberEnv.setAttribute(entry.getKey(), entry.getValue());
                }
            }
            for (int memberIndex = 0; memberIndex < numberOfTokens; memberIndex++) {
                for (final Arc arc : outArcs) {
                    final ArcToken arcToken = generateArcToken(process, arc, token);
                    final ArcTokenSetMember setMember = getFactory().newArcTokenSetMember(tokenSet, arcToken, memberIndex);
                    tokenSet.getActiveArcTokens(this).add(arcToken);
                    arcToken.getTokenSetMemberships().add(setMember);
                    finishNewArcTokenProcessing(process, arcToken, asynchronous);
                }
            }
        }
    }

    protected void completeNodeExecution(final NodeToken token, final String arcName, final boolean asynchronous) {
        final GraphProcess process = token.getProcess();
        if (!process.isExecuting() || token.isComplete()) {
            return;
        }
        completeNodeToken(process, token, arcName);
        for (final Arc arc : process.getGraph().getOutputArcs(token.getNode(), arcName)) {
            final ArcToken arcToken = generateArcToken(process, arc, token);
            finishNewArcTokenProcessing(process, arcToken, asynchronous);
        }
    }

    protected void completeNodeExecution(final NodeToken token, final boolean asynchronous, final String... arcNames) {
        final GraphProcess process = token.getProcess();
        if (!process.isExecuting() || token.isComplete()) {
            return;
        }
        completeNodeToken(process, token, arcNames);
        for (final Arc arc : process.getGraph().getOutputArcs(token.getNode(), arcNames)) {
            final ArcToken arcToken = generateArcToken(process, arc, token);
            finishNewArcTokenProcessing(process, arcToken, asynchronous);
        }
    }

    private void completeNodeToken(final GraphProcess process, final NodeToken token, final String arcName) {
        process.removeActiveNodeToken(token);
        token.markComplete();
        NodeTokenEvent.fireCompletedEvent(this, token, arcName);
    }

    private void completeNodeToken(final GraphProcess process, final NodeToken token, final String... arcNames) {
        process.removeActiveNodeToken(token);
        token.markComplete();
        NodeTokenEvent.fireCompletedEvent(this, token, arcNames);
    }

    private ArcToken generateArcToken(final GraphProcess process, final Arc arc, final NodeToken token) {
        final ArcToken arcToken = getFactory().newArcToken(process, arc, ExecutionType.Forward, token);
        token.getChildTokens().add(arcToken);
        for (final NodeTokenSetMember setMember : token.getTokenSetMemberships()) {
            final TokenSet tokenSet = setMember.getTokenSet();
            if (!tokenSet.isComplete()) {
                final ArcTokenSetMember newSetMember = getFactory().newArcTokenSetMember(tokenSet, arcToken, setMember.getMemberIndex());
                tokenSet.getActiveArcTokens(this).add(arcToken);
                arcToken.getTokenSetMemberships().add(newSetMember);
            }
        }
        return arcToken;
    }

    private void finishNewArcTokenProcessing(final GraphProcess process, final ArcToken arcToken, final boolean asynchronous) {
        ArcTokenEvent.fireCreatedEvent(this, arcToken);
        if (asynchronous && arcExecutionStarted) {
            asyncQueue.add(arcToken);
        } else {
            process.enqueueArcTokenForExecution(arcToken);
        }
    }

    @Override
    public void executeQueuedArcTokens(final GraphProcess process) {
        arcExecutionStarted = true;
        try {
            while (process.isExecuting() && !process.isArcTokenQueueEmpty()) {
                executeArc(process, process.dequeueArcTokenForExecution());
            }
            checkForCompletion(process);
        } finally {
            arcExecutionStarted = false;
            drainAsyncQueue(process);
        }
    }

    private void drainAsyncQueue(final GraphProcess process) {
        while (!asyncQueue.isEmpty()) {
            process.enqueueArcTokenForExecution(asyncQueue.remove(0));
        }
    }

    private void checkForCompletion(final GraphProcess process) {
        if (process.isExecuting() && !process.hasActiveTokens() && process.isArcTokenQueueEmpty() && asyncQueue.isEmpty()) {
            process.setState(ProcessState.PendingCompletion);
            final EventActions actions = ProcessEvent.firePendingCompleteEvent(this, process);
            if (!actions.isEventTypeIncluded(EventActionType.DELAY_PROCESS_FINALIZE_COMPLETE)) {
                finalizeComplete(process);
            }
        }
    }

    @Override
    public void setupScriptEnv(final ScriptEnv env, final NodeToken token) {
        env.addVariable("engine", this);
        env.addVariable("token", token);
    }

    @Override
    public void addNodeType(final String type, final Class<? extends Node> nodeClass) {
        getFactory().addType(type, nodeClass);
    }

    @Override
    public void addGlobalCustomNodeType(final String type, final Class<? extends CustomNode> nodeClass) {
        getFactory().addGlobalCustomType(type, nodeClass);
    }

    @Override
    public void backtrack(final NodeToken token) {
        if (!token.getProcess().isExecuting()) {
            throw new SarasvatiException("Can only backtrack processes with a state of 'Executing'");
        }
        if (!token.isComplete()) {
            throw new SarasvatiException("Cannot backtrack to a node token which isn't completed.");
        }
        if (token.getExecutionType().isBacktracked()) {
            throw new SarasvatiException("Cannot backtrack to a node token which has been backtracked.");
        }
        NodeToken resultToken = null;
        final BacktrackTokenVisitor visitor = new BacktrackTokenVisitor(this, token);
        if (token.getChildTokens().isEmpty()) {
            resultToken = visitor.backtrackDeadEnd(token);
        } else {
            TokenTraversals.traverseChildrenInCreateOrder(token, visitor);
            resultToken = visitor.backtrack();
        }
        executeNode(resultToken.getProcess(), resultToken);
        if (!arcExecutionStarted) {
            executeQueuedArcTokens(token.getProcess());
        }
    }

    @Override
    public RubricEnv newRubricEnv(final NodeToken token) {
        return new DefaultRubricEnv(this, token, DefaultRubricFunctionRepository.getGlobalInstance());
    }

    @Override
    public JoinLangEnv newJoinLangEnv(final ArcToken token) {
        return new JoinLangEnvImpl(this, token, newRubricEnv(token.getParentToken()));
    }

    @Override
    public GuardResult evaluateGuard(final NodeToken token, final String guard) {
        if (guard == null || guard.trim().length() == 0) {
            return AcceptTokenGuardResult.INSTANCE;
        }
        return (GuardResult) RubricCompiler.compile(guard).eval(newRubricEnv(token));
    }

    @Override
    public BaseEngine newEngine(final boolean forNested) {
        final BaseEngine engine = newEngine();
        if (forNested) {
            engine.parentEngine = this;
        }
        return engine;
    }

    @Override
    public BaseEngine getParentEngine() {
        return parentEngine;
    }

    /**
   * Creates a new engine base on the same parameters as this. For
   * example, if the engine is database backed, it should share
   * the same database engine.
   *
   * @return A new engine
   */
    protected abstract BaseEngine newEngine();

    @Override
    public void addExecutionListener(final Class<? extends ExecutionListener> listenerClass, final ExecutionEventType... eventTypes) {
        globalEventQueue.addListener(this, listenerClass, eventTypes);
    }

    @Override
    public void removeExecutionListener(final Class<? extends ExecutionListener> listenerClass, final ExecutionEventType... eventTypes) {
        globalEventQueue.removeListener(this, listenerClass, eventTypes);
    }

    @Override
    public void addExecutionListener(final GraphProcess process, final Class<? extends ExecutionListener> listenerClass, final ExecutionEventType... eventTypes) {
        process.getEventQueue().addListener(this, listenerClass, eventTypes);
    }

    @Override
    public void removeExecutionListener(final GraphProcess process, final Class<? extends ExecutionListener> listenerClass, final ExecutionEventType... eventTypes) {
        process.getEventQueue().removeListener(this, listenerClass, eventTypes);
    }

    @Override
    public EventActions fireEvent(final ExecutionEvent event) {
        return globalEventQueue.fireEvent(event);
    }
}
