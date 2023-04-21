package games.strategy.engine.message;

import games.strategy.net.GUID;
import games.strategy.net.IMessageListener;
import games.strategy.net.IMessenger;
import games.strategy.net.IMessengerErrorListener;
import games.strategy.net.INode;
import games.strategy.thread.ThreadPool;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A messenger general enough that both Channel and Remote messenger can be
 * based on it.
 * 
 * @author Sean Bridges
 */
public class UnifiedMessenger {

    private static final Logger s_logger = Logger.getLogger(UnifiedMessenger.class.getName());

    private static ThreadPool m_threadPool = new ThreadPool(15, "UnifiedMessengerPool");

    private final IMessenger m_messenger;

    private final Object m_endPointMutex = new Object();

    private final Map<String, EndPoint> m_localEndPoints = new HashMap<String, EndPoint>();

    private final Object m_pendingLock = new Object();

    private final Map<GUID, CountDownLatch> m_pendingInvocations = new HashMap<GUID, CountDownLatch>();

    private final Map<GUID, RemoteMethodCallResults> m_results = new HashMap<GUID, RemoteMethodCallResults>();

    private UnifiedMessengerHub m_hub;

    private final IMessengerErrorListener m_messengerErrorListener = new IMessengerErrorListener() {

        public void messengerInvalid(final IMessenger messenger, final Exception reason) {
            UnifiedMessenger.this.messengerInvalid();
        }
    };

    /**
	 * @param messenger
	 */
    public UnifiedMessenger(final IMessenger messenger) {
        m_messenger = messenger;
        m_messenger.addMessageListener(m_messageListener);
        m_messenger.addErrorListener(m_messengerErrorListener);
        if (m_messenger.isServer()) {
            m_hub = new UnifiedMessengerHub(m_messenger, this);
        }
    }

    UnifiedMessengerHub getHub() {
        return m_hub;
    }

    private void messengerInvalid() {
        synchronized (m_pendingLock) {
            for (final GUID id : m_pendingInvocations.keySet()) {
                final CountDownLatch latch = m_pendingInvocations.remove(id);
                latch.countDown();
                m_results.put(id, new RemoteMethodCallResults(new ConnectionLostException("Connection Lost")));
            }
        }
    }

    /**
	 * Invoke and wait for all implementors on all vms to finish executing.
	 * 
	 */
    public RemoteMethodCallResults invokeAndWait(final String endPointName, final RemoteMethodCall remoteCall) {
        EndPoint local;
        synchronized (m_endPointMutex) {
            local = m_localEndPoints.get(endPointName);
        }
        if (local == null) return invokeAndWaitRemote(remoteCall); else {
            final long number = local.takeANumber();
            final List<RemoteMethodCallResults> results = local.invokeLocal(remoteCall, number, getLocalNode());
            if (results.size() == 0) throw new RemoteNotFoundException("Not found:" + endPointName);
            if (results.size() > 1) throw new IllegalStateException("Too many implementors, got back:" + results);
            return results.get(0);
        }
    }

    private RemoteMethodCallResults invokeAndWaitRemote(final RemoteMethodCall remoteCall) {
        final GUID methodCallID = new GUID();
        final CountDownLatch latch = new CountDownLatch(1);
        synchronized (m_pendingLock) {
            m_pendingInvocations.put(methodCallID, latch);
        }
        final Invoke invoke = new HubInvoke(methodCallID, true, remoteCall);
        send(invoke, m_messenger.getServerNode());
        if (s_logger.isLoggable(Level.FINER)) {
            s_logger.log(Level.FINER, "Waiting for method:" + remoteCall.getMethodName() + " for remote name:" + remoteCall.getRemoteName() + " with id:" + methodCallID);
        }
        try {
            latch.await();
        } catch (final InterruptedException e) {
            s_logger.log(Level.WARNING, e.getMessage());
        }
        if (s_logger.isLoggable(Level.FINER)) {
            s_logger.log(Level.FINER, "Method returned:" + remoteCall.getMethodName() + " for remote name:" + remoteCall.getRemoteName() + " with id:" + methodCallID);
        }
        RemoteMethodCallResults results;
        synchronized (m_pendingLock) {
            results = m_results.remove(methodCallID);
            if (results == null) throw new IllegalStateException("No results");
        }
        return results;
    }

    /**
	 * invoke without waiting for remote nodes to respond
	 */
    public void invoke(final String endPointName, final RemoteMethodCall call) {
        final Invoke invoke = new HubInvoke(null, false, call);
        send(invoke, m_messenger.getServerNode());
        EndPoint endPoint;
        synchronized (m_endPointMutex) {
            endPoint = m_localEndPoints.get(endPointName);
        }
        if (endPoint != null) {
            final long number = endPoint.takeANumber();
            final List<RemoteMethodCallResults> results = endPoint.invokeLocal(call, number, getLocalNode());
            for (final RemoteMethodCallResults r : results) {
                if (r.getException() != null) {
                    s_logger.log(Level.WARNING, r.getException().getMessage(), r.getException());
                }
            }
        }
    }

    public void addImplementor(final RemoteName endPointDescriptor, final Object implementor, final boolean singleThreaded) {
        if (!endPointDescriptor.getClazz().isAssignableFrom(implementor.getClass())) throw new IllegalArgumentException(implementor + " does not implement " + endPointDescriptor.getClazz());
        final EndPoint endPoint = getLocalEndPointOrCreate(endPointDescriptor, singleThreaded);
        endPoint.addImplementor(implementor);
    }

    public INode getLocalNode() {
        return m_messenger.getLocalNode();
    }

    /**
	 * Get the 1 and only implementor for the endpoint. throws an exception if there are not exctly 1 implementors
	 */
    public Object getImplementor(final String name) {
        synchronized (m_endPointMutex) {
            final EndPoint endPoint = m_localEndPoints.get(name);
            return endPoint.getFirstImplementor();
        }
    }

    void removeImplementor(final String name, final Object implementor) {
        EndPoint endPoint;
        synchronized (m_endPointMutex) {
            endPoint = m_localEndPoints.get(name);
            if (endPoint == null) throw new IllegalStateException("No end point for:" + name);
            if (implementor == null) throw new IllegalArgumentException("null implementor");
            final boolean noneLeft = endPoint.removeImplementor(implementor);
            if (noneLeft) {
                m_localEndPoints.remove(name);
                send(new NoLongerHasEndPointImplementor(name), m_messenger.getServerNode());
            }
        }
    }

    private EndPoint getLocalEndPointOrCreate(final RemoteName endPointDescriptor, final boolean singleThreaded) {
        EndPoint endPoint;
        synchronized (m_endPointMutex) {
            if (m_localEndPoints.containsKey(endPointDescriptor.getName())) return m_localEndPoints.get(endPointDescriptor.getName());
            endPoint = new EndPoint(endPointDescriptor.getName(), endPointDescriptor.getClazz(), singleThreaded);
            m_localEndPoints.put(endPointDescriptor.getName(), endPoint);
        }
        final HasEndPointImplementor msg = new HasEndPointImplementor(endPointDescriptor.getName());
        send(msg, m_messenger.getServerNode());
        return endPoint;
    }

    private void send(final Serializable msg, final INode to) {
        if (m_messenger.getLocalNode().equals(to)) {
            m_hub.messageReceived(msg, getLocalNode());
        } else {
            m_messenger.send(msg, to);
        }
    }

    public boolean isServer() {
        return m_messenger.isServer();
    }

    /**
	 * Wait for the messenger to know about the given endpoint.
	 * 
	 * @param endPointName
	 * @param timeout
	 */
    public void waitForLocalImplement(final String endPointName, long timeoutMS) {
        if (timeoutMS <= 0) timeoutMS = Integer.MAX_VALUE;
        final long endTime = timeoutMS + System.currentTimeMillis();
        while (System.currentTimeMillis() < endTime && !hasLocalEndPoint(endPointName)) {
            try {
                Thread.sleep(50);
            } catch (final InterruptedException e) {
            }
        }
    }

    private boolean hasLocalEndPoint(final String endPointName) {
        synchronized (m_endPointMutex) {
            return m_localEndPoints.containsKey(endPointName);
        }
    }

    int getLocalEndPointCount(final RemoteName descriptor) {
        synchronized (m_endPointMutex) {
            if (!m_localEndPoints.containsKey(descriptor.getName())) return 0;
            return m_localEndPoints.get(descriptor.getName()).getLocalImplementorCount();
        }
    }

    private final IMessageListener m_messageListener = new IMessageListener() {

        public void messageReceived(final Serializable msg, final INode from) {
            UnifiedMessenger.this.messageReceived(msg, from);
        }
    };

    private void assertIsServer(final INode from) {
        if (!from.equals(m_messenger.getServerNode())) throw new IllegalStateException("Not from server!  Instead from:" + from);
    }

    void messageReceived(final Serializable msg, final INode from) {
        if (msg instanceof SpokeInvoke) {
            assertIsServer(from);
            final SpokeInvoke invoke = (SpokeInvoke) msg;
            EndPoint local;
            synchronized (m_endPointMutex) {
                local = m_localEndPoints.get(invoke.call.getRemoteName());
            }
            if (local == null) {
                if (invoke.needReturnValues) {
                    send(new HubInvocationResults(new RemoteMethodCallResults(new RemoteNotFoundException("No implementors for " + invoke.call)), invoke.methodCallID), from);
                }
                return;
            }
            final long methodRunNumber = local.takeANumber();
            final EndPoint localFinal = local;
            final Runnable task = new Runnable() {

                public void run() {
                    final List<RemoteMethodCallResults> results = localFinal.invokeLocal(invoke.call, methodRunNumber, invoke.getInvoker());
                    if (invoke.needReturnValues) {
                        RemoteMethodCallResults result = null;
                        if (results.size() == 1) {
                            result = results.get(0);
                        } else result = new RemoteMethodCallResults(new IllegalStateException("Invalid result count" + results.size()) + " for end point:" + localFinal);
                        send(new HubInvocationResults(result, invoke.methodCallID), from);
                    }
                }
            };
            m_threadPool.runTask(task);
        } else if (msg instanceof SpokeInvocationResults) {
            assertIsServer(from);
            final SpokeInvocationResults results = (SpokeInvocationResults) msg;
            final GUID methodID = results.methodCallID;
            synchronized (m_pendingLock) {
                m_results.put(methodID, results.results);
                m_pendingInvocations.remove(methodID).countDown();
            }
        }
    }

    public void dumpState(final PrintStream stream) {
        synchronized (m_endPointMutex) {
            stream.println("Local Endpoints:" + m_localEndPoints);
        }
        synchronized (m_endPointMutex) {
            stream.println("Remote nodes with implementors:" + m_results);
            stream.println("Remote nodes with implementors:" + m_pendingInvocations);
        }
    }

    public void waitForAllJobs() {
        m_threadPool.waitForAll();
    }
}

/**
 * This is where the methods finally get called.
 * 
 * An endpoint contains the implementors for a given name that are local to this
 * node.
 * 
 * You can invoke the method and get the results for all the implementors.
 * 
 * @author Sean Bridges
 */
class EndPoint {

    private final AtomicLong m_nextGivenNumber = new AtomicLong();

    private long m_currentRunnableNumber = 0;

    private final Object m_numberMutext = new Object();

    private final Object m_implementorsMutext = new Object();

    private final String m_name;

    private final Class<?> m_remoteClass;

    private final List<Object> m_implementors = new ArrayList<Object>();

    private final boolean m_singleThreaded;

    public EndPoint(final String name, final Class<?> remoteClass, final boolean singleThreaded) {
        m_name = name;
        m_remoteClass = remoteClass;
        m_singleThreaded = singleThreaded;
    }

    public Object getFirstImplementor() {
        synchronized (m_implementorsMutext) {
            if (m_implementors.size() != 1) {
                throw new IllegalStateException("Invalid implementor count, " + m_implementors);
            }
            return m_implementors.get(0);
        }
    }

    public long takeANumber() {
        return m_nextGivenNumber.getAndIncrement();
    }

    private void waitTillCanBeRun(final long aNumber) {
        synchronized (m_numberMutext) {
            while (aNumber > m_currentRunnableNumber) {
                try {
                    m_numberMutext.wait();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void releaseNumber() {
        synchronized (m_numberMutext) {
            m_currentRunnableNumber++;
            m_numberMutext.notifyAll();
        }
    }

    /**
	 * 
	 * @return is this the first implementor
	 */
    public boolean addImplementor(final Object implementor) {
        if (!m_remoteClass.isAssignableFrom(implementor.getClass())) throw new IllegalArgumentException(m_remoteClass + " is not assignable from " + implementor.getClass());
        synchronized (m_implementorsMutext) {
            final boolean rVal = m_implementors.isEmpty();
            m_implementors.add(implementor);
            return rVal;
        }
    }

    public boolean isSingleThreaded() {
        return m_singleThreaded;
    }

    public boolean hasImplementors() {
        synchronized (m_implementorsMutext) {
            return !m_implementors.isEmpty();
        }
    }

    public int getLocalImplementorCount() {
        synchronized (m_implementorsMutext) {
            return m_implementors.size();
        }
    }

    /**
	 * 
	 * @return - we have no more implementors
	 */
    boolean removeImplementor(final Object implementor) {
        synchronized (m_implementorsMutext) {
            if (!m_implementors.remove(implementor)) {
                throw new IllegalStateException("Not removed, impl:" + implementor + " have " + m_implementors);
            }
            return m_implementors.isEmpty();
        }
    }

    public String getName() {
        return m_name;
    }

    public Class<?> getRemoteClass() {
        return m_remoteClass;
    }

    public List<RemoteMethodCallResults> invokeLocal(final RemoteMethodCall call, final long number, final INode messageOriginator) {
        try {
            if (m_singleThreaded) {
                waitTillCanBeRun(number);
            }
            return invokeMultiple(call, messageOriginator);
        } finally {
            releaseNumber();
        }
    }

    /**
	 * @param call
	 * @param rVal
	 */
    private List<RemoteMethodCallResults> invokeMultiple(final RemoteMethodCall call, final INode messageOriginator) {
        List<Object> implementorsCopy;
        synchronized (m_implementorsMutext) {
            implementorsCopy = new ArrayList<Object>(m_implementors);
        }
        final List<RemoteMethodCallResults> results = new ArrayList<RemoteMethodCallResults>(implementorsCopy.size());
        for (final Object implementor : implementorsCopy) {
            results.add(invokeSingle(call, implementor, messageOriginator));
        }
        return results;
    }

    /**
	 * @param call
	 * @param implementor
	 * @return
	 */
    private RemoteMethodCallResults invokeSingle(final RemoteMethodCall call, final Object implementor, final INode messageOriginator) {
        call.resolve(m_remoteClass);
        Method method;
        try {
            method = implementor.getClass().getMethod(call.getMethodName(), call.getArgTypes());
            method.setAccessible(true);
        } catch (final SecurityException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
        MessageContext.setSenderNodeForThread(messageOriginator);
        try {
            final Object methodRVal = method.invoke(implementor, call.getArgs());
            return new RemoteMethodCallResults(methodRVal);
        } catch (final InvocationTargetException e) {
            return new RemoteMethodCallResults(e.getTargetException());
        } catch (final IllegalAccessException e) {
            System.err.println("error in call:" + call);
            e.printStackTrace();
            return new RemoteMethodCallResults(e);
        } catch (final IllegalArgumentException e) {
            System.err.println("error in call:" + call);
            e.printStackTrace();
            return new RemoteMethodCallResults(e);
        } finally {
            MessageContext.setSenderNodeForThread(null);
        }
    }

    public boolean equivalent(final EndPoint other) {
        if (other.m_singleThreaded != this.m_singleThreaded) return false;
        if (!other.m_name.equals(this.m_name)) return false;
        if (!(other.m_remoteClass.equals(m_remoteClass))) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Name:" + m_name + " singleThreaded:" + m_singleThreaded + " implementors:" + m_implementors;
    }
}

class EndPointCreated implements Serializable {

    public final String[] classes;

    public final String name;

    public final boolean singleThreaded;

    public EndPointCreated(final String[] classes, final String name, final boolean singleThreaded) {
        this.classes = classes;
        this.name = name;
        this.singleThreaded = singleThreaded;
    }
}

class EndPointDestroyed implements Serializable {

    public final String name;

    public EndPointDestroyed(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EndPointDestroyed:" + name;
    }
}

class HasEndPointImplementor implements Serializable {

    public final String endPointName;

    public HasEndPointImplementor(final String endPointName) {
        this.endPointName = endPointName;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ":" + endPointName;
    }
}

class NoLongerHasEndPointImplementor implements Serializable {

    public final String endPointName;

    public NoLongerHasEndPointImplementor(final String endPointName) {
        this.endPointName = endPointName;
    }
}

abstract class Invoke implements Externalizable {

    public GUID methodCallID;

    public boolean needReturnValues;

    public RemoteMethodCall call;

    public Invoke() {
    }

    @Override
    public String toString() {
        return "invoke on:" + call.getRemoteName() + " method name:" + call.getMethodName() + " method call id:" + methodCallID;
    }

    public Invoke(final GUID methodCallID, final boolean needReturnValues, final RemoteMethodCall call) {
        if (needReturnValues && methodCallID == null) throw new IllegalArgumentException("Cant have no id and need return values");
        if (!needReturnValues && methodCallID != null) throw new IllegalArgumentException("Cant have id and not need return values");
        this.methodCallID = methodCallID;
        this.needReturnValues = needReturnValues;
        this.call = call;
    }

    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        needReturnValues = in.read() == 1;
        if (needReturnValues) methodCallID = (GUID) in.readObject();
        call = new RemoteMethodCall();
        call.readExternal(in);
    }

    public void writeExternal(final ObjectOutput out) throws IOException {
        out.write(needReturnValues ? 1 : 0);
        if (needReturnValues) out.writeObject(methodCallID);
        call.writeExternal(out);
    }
}

abstract class InvocationResults implements Externalizable {

    public RemoteMethodCallResults results;

    public GUID methodCallID;

    public InvocationResults() {
    }

    public InvocationResults(final RemoteMethodCallResults results, final GUID methodCallID) {
        if (results == null) throw new IllegalArgumentException("Null results");
        if (methodCallID == null) throw new IllegalArgumentException("Null id");
        this.results = results;
        this.methodCallID = methodCallID;
    }

    @Override
    public String toString() {
        return "Invocation results for method id:" + methodCallID + " results:" + results;
    }

    public void writeExternal(final ObjectOutput out) throws IOException {
        results.writeExternal(out);
        methodCallID.writeExternal(out);
    }

    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        results = new RemoteMethodCallResults();
        results.readExternal(in);
        methodCallID = new GUID();
        methodCallID.readExternal(in);
    }
}
