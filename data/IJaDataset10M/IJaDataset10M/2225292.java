package org.hypergraphdb.peer.workflow;

import static org.hypergraphdb.peer.workflow.WorkflowState.*;
import static org.hypergraphdb.peer.Structs.*;
import static org.hypergraphdb.peer.Messages.*;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import org.hypergraphdb.peer.HyperGraphPeer;
import org.hypergraphdb.peer.Message;
import org.hypergraphdb.peer.MessageHandler;
import org.hypergraphdb.peer.Messages;
import org.hypergraphdb.peer.Performative;
import org.hypergraphdb.util.HGUtils;

/**
 * 
 * <p>
 * The <code>ActivityManager</code> manages all activities currently in effect within
 * a given peer.   
 * </p>
 * 
 * @author Borislav Iordanov
 */
public class ActivityManager implements MessageHandler {

    private HyperGraphPeer thisPeer;

    private Map<String, ActivityType> activityTypes = Collections.synchronizedMap(new HashMap<String, ActivityType>());

    private Map<UUID, Activity> activities = Collections.synchronizedMap(new HashMap<UUID, Activity>());

    private Map<Activity, Activity> parents = Collections.synchronizedMap(new HashMap<Activity, Activity>());

    final BlockingQueue<Activity> globalQueue = new PriorityBlockingQueue<Activity>(10, new Comparator<Activity>() {

        public int compare(Activity left, Activity right) {
            if (left.future.isWaitedOn()) {
                if (!right.future.isWaitedOn() && !left.queue.isEmpty()) return -1;
            } else if (right.future.isWaitedOn() && !right.queue.isEmpty()) return 1;
            long st = System.currentTimeMillis();
            long diff = (st - right.lastActionTimestamp) * (1 + right.queue.size()) - (st - left.lastActionTimestamp) * (1 + left.queue.size());
            return diff > 0 ? 1 : diff < 0 ? -1 : 0;
        }
    });

    private class ActivitySchedulingThread extends Thread {

        volatile boolean schedulerRunning = false;

        public ActivitySchedulingThread() {
            super("HGDB Peer Scheduler");
        }

        public void run() {
            int reportEmpty = 0;
            for (schedulerRunning = true; schedulerRunning; ) {
                try {
                    Activity a = globalQueue.poll(1, TimeUnit.SECONDS);
                    if (a == null) {
                        if (reportEmpty >= 50) {
                            reportEmpty = 0;
                        }
                        reportEmpty++;
                        continue;
                    } else if (!a.queue.isEmpty() && !a.getState().isFinished()) {
                        Runnable r = a.queue.take();
                        thisPeer.getExecutorService().execute(r);
                    } else {
                        if (globalQueue.isEmpty()) {
                            Thread.sleep(100);
                        }
                        a.lastActionTimestamp = System.currentTimeMillis();
                        if (!a.getState().isFinished()) globalQueue.put(a);
                    }
                } catch (InterruptedException ex) {
                    break;
                }
            }
            schedulerRunning = false;
        }
    }

    private ActivitySchedulingThread schedulerThread = null;

    private void handleActivityException(Activity activity, Throwable exception, Message msg) {
        activity.future.result.exception = exception;
        activity.getState().assign(WorkflowState.Failed);
        exception.printStackTrace(System.err);
        if (msg != null) thisPeer.getPeerInterface().send(getSender(msg), getReply(msg, Performative.Failure, HGUtils.printStackTrace(exception)));
    }

    private Activity findRootActivity(Activity a) {
        Activity root = a;
        for (Activity tmp = parents.get(root); tmp != null; tmp = parents.get(root)) root = tmp;
        return root;
    }

    private void notUnderstood(final Message msg, final String explanation) {
        try {
            Message reply = combine(Messages.getReply(msg), struct(PERFORMATIVE, Performative.NotUnderstood, CONTENT, msg, WHY_NOT_UNDERSTOOD, explanation));
            thisPeer.getPeerInterface().send(getSender(msg), reply);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    private Runnable makeTransitionAction(final ActivityType type, final Activity parentActivity, final Activity activity) {
        return new Runnable() {

            public void run() {
                Activity rootActivity = findRootActivity(parentActivity);
                try {
                    Transition transition = type.getTransitionMap().getTransition(parentActivity.getState().getConst(), activity, activity.getState().getConst());
                    if (transition == null) return;
                    WorkflowStateConstant result = transition.apply(parentActivity, activity);
                    if (result != null) parentActivity.getState().assign(result);
                } catch (Throwable t) {
                    handleActivityException(parentActivity, t, null);
                } finally {
                    parentActivity.lastActionTimestamp = System.currentTimeMillis();
                    try {
                        globalQueue.put(rootActivity);
                    } catch (InterruptedException ex) {
                        handleActivityException(rootActivity, ex, null);
                    }
                }
            }
        };
    }

    private Runnable makeTransitionAction(final ActivityType type, final FSMActivity activity, final Message msg) {
        return new Runnable() {

            public void run() {
                Activity rootActivity = findRootActivity(activity);
                try {
                    Transition transition = type.getTransitionMap().getTransition(activity.getState().getConst(), msg);
                    if (transition == null) {
                        Performative perf = Performative.toConstant((String) getPart(msg, PERFORMATIVE));
                        if (perf == Performative.Failure) activity.onPeerFailure(msg); else if (perf == Performative.NotUnderstood) activity.onPeerNotUnderstand(msg); else notUnderstood(msg, " no state transition defined for this performative.");
                    } else {
                        WorkflowStateConstant result = transition.apply(activity, msg);
                        if (result != null) activity.getState().assign(result);
                    }
                } catch (Throwable t) {
                    handleActivityException(activity, t, msg);
                } finally {
                    activity.lastActionTimestamp = System.currentTimeMillis();
                    try {
                        if (!rootActivity.getState().isFinished()) globalQueue.put(rootActivity);
                    } catch (InterruptedException ex) {
                        handleActivityException(rootActivity, ex, null);
                    }
                }
            }
        };
    }

    private Runnable makeMessageHandleAction(final Activity activity, final Message msg) {
        return new Runnable() {

            public void run() {
                Activity rootActivity = findRootActivity(activity);
                try {
                    activity.handleMessage(msg);
                } catch (Throwable t) {
                    handleActivityException(activity, t, msg);
                } finally {
                    activity.lastActionTimestamp = System.currentTimeMillis();
                    try {
                        if (!rootActivity.getState().isFinished()) globalQueue.put(rootActivity);
                    } catch (InterruptedException ex) {
                        handleActivityException(rootActivity, ex, null);
                    }
                }
            }
        };
    }

    private void readTransitionMap(Class<? extends Activity> activityClass, TransitionMap map) {
        for (Method m : activityClass.getMethods()) {
            FromState aFromState = m.getAnnotation(FromState.class);
            OnMessage onMessage = m.getAnnotation(OnMessage.class);
            AtActivity atActivity = m.getAnnotation(AtActivity.class);
            OnActivityState onState = m.getAnnotation(OnActivityState.class);
            if (atActivity != null && onState == null || onState != null && atActivity == null) throw new RuntimeException("Both OnStateActivity and AtActivity annotations need " + "to be specified for method " + m + " in class " + activityClass.getName() + " or neither.");
            if (aFromState == null) {
                if (onMessage != null || atActivity != null || onState != null) throw new RuntimeException("A transition method needs to be annotated with " + " with a FromState annotation."); else continue;
            } else if (onMessage == null && atActivity == null) throw new RuntimeException("A transition method needs to be annotated either " + " with an OnMessage or both AtActivity and OnActivityState annotations.");
            Map<String, String> msgAttrs = null;
            if (onMessage != null) {
                msgAttrs = new HashMap<String, String>();
                msgAttrs.put("performative", onMessage.performative());
            }
            Transition t = new MethodCallTransition(m);
            for (String from : aFromState.value()) {
                WorkflowStateConstant fromState = WorkflowState.toStateConstant(from);
                if (msgAttrs != null) map.setTransition(fromState, msgAttrs, t);
                if (atActivity != null) {
                    for (String to : onState.value()) map.setTransition(fromState, atActivity.value(), WorkflowState.toStateConstant(to), t);
                }
            }
        }
    }

    public ActivityManager(HyperGraphPeer thisPeer) {
        this.thisPeer = thisPeer;
    }

    public void start() {
        schedulerThread = new ActivitySchedulingThread();
        schedulerThread.start();
    }

    public void stop() {
        if (schedulerThread == null) return;
        schedulerThread.schedulerRunning = false;
        try {
            if (schedulerThread.isAlive()) schedulerThread.join();
        } catch (InterruptedException ex) {
        } finally {
            schedulerThread = null;
        }
    }

    /**
     * <p>
     * Clear all internal data structures such as registered activities,
     * queues of pending actions etc. This method should never be called
     * while the scheduler is currently running.
     * </p>
     */
    public void clear() {
        this.activities.clear();
        this.activityTypes.clear();
        this.parents.clear();
        this.globalQueue.clear();
    }

    /**
     * <p>
     * Clear all activity-related data structures. This method should 
     * never be called while the scheduler is currently running. Registered
     * activity types remain so there's no need to re-register and the start
     * method could be called again.
     * </p>
     */
    public void clearActivities() {
        this.activities.clear();
        this.parents.clear();
        this.globalQueue.clear();
    }

    /**
     * <p>
     * Retrieve an {@link Activity} by its UUID.
     * </p>
     */
    public Activity getActivity(UUID id) {
        return activities.get(id);
    }

    /**
     * <p>
     * A simplified version of <code>registerActivityType</code> in which the
     * type name is taken to be the fully qualified classname of the 
     * <code>activityClass</code> parameter and a <code>DefaultActivityFactory</code>
     * instance is going to be used to create new activities of that type.
     * </p>
     * 
     * @param activityClass The class implementing the activity. 
     */
    public void registerActivityType(Class<? extends Activity> activityClass) {
        registerActivityType(activityClass.getName(), activityClass, new DefaultActivityFactory(activityClass));
    }

    /**
     * <p>
     * Register an activity type with an associated factory. The factory will be used
     * to construct new activity instances based on incoming message. 
     * </p>
     * 
     * @param activityClass The class implementing the activity. 
     * @param factory The activity factory associated with this type. 
     */
    public void registerActivityType(Class<? extends Activity> activityClass, ActivityFactory factory) {
        registerActivityType(activityClass.getName(), activityClass, factory);
    }

    /**
     * <p>
     * Register an activity type with the specified non-default type name.
     * </p>
     * @param type The type name.
     * @param activityClass The class that implements the activity.
     */
    public void registerActivityType(String type, Class<? extends Activity> activityClass) {
        registerActivityType(type, activityClass, new DefaultActivityFactory(activityClass));
    }

    /**
     * <p>
     * Register an activity type with the specified non-default type name and 
     * factory.
     * </p>
     * @param type The type name.
     * @param activityClass The class that implements the activity.
     * @param factory The activity factory associated with this type. 
     */
    public void registerActivityType(String type, Class<? extends Activity> activityClass, ActivityFactory factory) {
        if (activityTypes.containsKey(type)) throw new IllegalArgumentException("Activity type '" + type + "' already registered.");
        ActivityType activityType = new ActivityType(type, factory);
        readTransitionMap(activityClass, activityType.getTransitionMap());
        activityTypes.put(type, activityType);
    }

    public Future<ActivityResult> initiateActivity(final Activity activity) {
        return initiateActivity(activity, null, null);
    }

    public Future<ActivityResult> initiateActivity(final Activity activity, final ActivityListener listener) {
        return initiateActivity(activity, null, listener);
    }

    /**
     * <p>
     * Initiate a new activity. 
     * </p>
     * 
     * @param activity
     * @param parentActivity
     * @param listener
     * @return
     */
    public Future<ActivityResult> initiateActivity(final Activity activity, final Activity parentActivity, final ActivityListener listener) {
        ActivityFuture future = insertNewActivity(activity, parentActivity, listener);
        activity.getState().compareAndAssign(Limbo, Started);
        activity.initiate();
        return future;
    }

    private ActivityFuture insertNewActivity(final Activity activity, final Activity parentActivity, final ActivityListener listener) {
        synchronized (activities) {
            if (activities.containsKey(activity.getId())) throw new RuntimeException("Activity " + activity + " with ID " + activity.getId() + " has already been initiated.");
            activities.put(activity.getId(), activity);
        }
        final CountDownLatch completionLatch = new CountDownLatch(1);
        final ActivityFuture future = new ActivityFuture(activity, completionLatch);
        activity.future = future;
        activity.getState().addListener(new StateListener() {

            public void stateChanged(WorkflowState state) {
                if (state.isFinished()) {
                    completionLatch.countDown();
                    if (listener != null) try {
                        listener.activityFinished(future.get());
                    } catch (Throwable t) {
                        t.printStackTrace(System.err);
                    }
                    globalQueue.remove(activity);
                    activities.remove(activity.getId());
                    parents.remove(activity);
                }
            }
        });
        if (parentActivity != null) {
            activity.queue = parentActivity.queue;
            parents.put(activity, parentActivity);
            activity.getState().addListener(new StateListener() {

                public void stateChanged(WorkflowState state) {
                    ActivityType pt = activityTypes.get(parentActivity.getType());
                    parentActivity.queue.add(makeTransitionAction(pt, parentActivity, activity));
                }
            });
        } else try {
            globalQueue.put(activity);
        } catch (InterruptedException ex) {
            handleActivityException(activity, ex, null);
        }
        return future;
    }

    public void handleMessage(final Message msg) {
        UUID activityId = getPart(msg, Messages.CONVERSATION_ID);
        if (activityId == null) {
            notUnderstood(msg, " missing conversation-id in message");
            return;
        }
        Activity activity = activities.get(activityId);
        ActivityType type = null;
        if (activity == null) {
            Activity parentActivity = null;
            UUID parentId = getPart(msg, Messages.PARENT_SCOPE);
            if (parentId != null) {
                parentActivity = activities.get(parentId);
                if (parentActivity == null) {
                    notUnderstood(msg, " unkown parent activity " + parentId);
                    return;
                }
            }
            type = activityTypes.get(getPart(msg, Messages.ACTIVITY_TYPE));
            if (type == null) {
                notUnderstood(msg, " unkown activity type '" + type + "'");
                return;
            }
            activity = type.getFactory().make(thisPeer, activityId, msg);
            insertNewActivity(activity, parentActivity, null);
            activity.getState().compareAndAssign(Limbo, Started);
        } else {
            type = activityTypes.get(activity.getType());
            if (type == null) handleActivityException(activity, new NullPointerException("no local activity type found with name " + activity.getType()), msg);
        }
        try {
            if (activity instanceof FSMActivity) activity.queue.put(makeTransitionAction(type, (FSMActivity) activity, msg)); else activity.queue.put(makeMessageHandleAction(activity, msg));
        } catch (InterruptedException ex) {
            handleActivityException(activity, ex, msg);
        }
    }

    public Activity getParent(Activity a) {
        return parents.get(a);
    }

    class ActivityFuture implements Future<ActivityResult> {

        ActivityResult result;

        CountDownLatch latch;

        AtomicInteger waiting = new AtomicInteger(0);

        boolean isWaitedOn() {
            return waiting.get() > 0;
        }

        public ActivityFuture(Activity activity, CountDownLatch latch) {
            result = new ActivityResult(activity);
            this.latch = latch;
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            throw new UnsupportedOperationException();
        }

        public ActivityResult get() throws InterruptedException, ExecutionException {
            waiting.incrementAndGet();
            try {
                latch.await();
            } catch (InterruptedException ex) {
                waiting.decrementAndGet();
                throw ex;
            }
            return result;
        }

        public ActivityResult get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            waiting.incrementAndGet();
            try {
                if (!latch.await(timeout, unit)) {
                    waiting.decrementAndGet();
                    return null;
                } else return result;
            } catch (InterruptedException ex) {
                waiting.decrementAndGet();
                throw ex;
            }
        }

        public boolean isCancelled() {
            return result.getActivity().getState().isCanceled();
        }

        public boolean isDone() {
            return result.getActivity().getState().isFinished();
        }
    }
}
