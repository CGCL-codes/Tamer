package fulmine.model.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import fulmine.Domain;
import fulmine.IDomain;
import fulmine.IType;
import fulmine.Type;
import fulmine.context.FulmineContext;
import fulmine.context.IFrameworkContext;
import fulmine.event.EventFrameExecution;
import fulmine.event.IEvent;
import fulmine.event.listener.EventListenerUtils;
import fulmine.event.listener.JUnitEventListener;
import fulmine.event.listener.JUnitSystemEventListener;
import fulmine.event.system.EventSourceNotObservedEvent;
import fulmine.event.system.EventSourceObservedEvent;
import fulmine.event.system.TxEvent;
import fulmine.model.container.IContainer.DataState;
import fulmine.model.container.impl.BasicContainer;
import fulmine.model.field.BooleanField;
import fulmine.model.field.IField;
import fulmine.model.field.LongField;
import fulmine.model.field.containerdefinition.ContainerDefinitionField;
import fulmine.model.field.containerdefinition.IContainerDefinitionField;
import fulmine.protocol.specification.FrameReader;
import fulmine.util.collection.CollectionFactory;
import fulmine.util.reference.Value;

/**
 * Test cases for the {@link AbstractContainer}
 * 
 * @author Ramon Servadei
 */
@SuppressWarnings("all")
public class ContainerJUnitTest {

    static final String REMOTE_CONTEXT_IDENTITY = "remoteContextIdentity";

    static final IDomain DOMAIN = Domain.get(2);

    private static final int SLEEP_TIME = 200;

    /** prevents GCing of remote containers during tests */
    static final Set<IContainer> remoteRefs = new HashSet<IContainer>();

    /**
     * JUnit version
     * 
     * @author Ramon Servadei
     * 
     */
    public static final class JUnitListener extends JUnitSystemEventListener {

        public TxEvent data;

        public IContainer container;

        /**
         * Construct the listener
         * 
         * @param name
         *            the name for the listener
         * @param filter
         *            the filter for events
         * @param justContainer
         *            <code>true</code> if the listener will just expect a
         *            container update, <code>false</code> if it expects a
         *            container and {@link TxEvent}.
         */
        public JUnitListener(String name, Class<? extends IEvent>[] filter) {
            super(name, filter);
        }

        @Override
        protected boolean doUpdate(IEvent event) {
            if (event instanceof TxEvent) {
                this.data = (TxEvent) event;
            } else {
                if (event instanceof IContainer) {
                    this.container = (IContainer) event;
                }
            }
            return true;
        }

        @Override
        public void reset() {
            this.container = null;
            this.data = null;
            super.reset();
        }
    }

    static final IType CODE = Type.get(21);

    static final String IWF_FIELD2 = "iwfField2";

    static final String IWF_FIELD1 = "iwfField1";

    IContainerFactory.IContainerBuilder builder;

    Object target = new Object();

    boolean gotLock;

    long time;

    AbstractContainer candidate;

    IContainer result;

    Mockery mocks = new JUnit4Mockery();

    IField field;

    IFrameworkContext context;

    @Before
    public void setUp() throws Exception {
        context = new FulmineContext(getClass().getSimpleName());
        builder = new JUnitContainerBuilder(context);
        context.start();
        context.getContainerFactory().registerBuilder(CODE, builder);
        field = mocks.mock(IField.class);
        candidate = context.getContainerFactory().createContainer(context.getIdentity(), "JUnitContainer-" + System.nanoTime(), CODE, DOMAIN, context, true);
    }

    @After
    public void tearDown() throws Exception {
        context.destroy();
    }

    @Test
    public void testHashCode() {
        assertFalse("Hashcode", candidate.hashCode() != getClone().hashCode());
    }

    @Test
    public void testEqualsObject() {
        assertEquals("Equals", candidate, getClone());
        ((LongField) candidate.get((IWF_FIELD1))).set(21);
        assertTrue("Equals", candidate.equals(getClone()));
    }

    @Test
    public void testInit() {
        BasicContainer clone = new BasicContainer(context.getIdentity(), candidate.getIdentity(), CODE, DOMAIN, context, true);
        assertTrue("Equals", candidate.equals(clone));
        builder.createContainerDefinition().populate(clone);
        assertEquals("Equals", candidate, clone);
    }

    @Test
    public void testGetType() {
        assertEquals("Type", CODE, candidate.getType());
    }

    @Test
    public void testIsEmpty() {
        assertFalse("empty", candidate.isEmpty());
    }

    @Test
    public void testContains() {
        assertFalse("contains", candidate.contains(("sdf-" + System.nanoTime())));
        assertTrue("contains", candidate.contains((IWF_FIELD1)));
    }

    @Test
    public void testDestroy() {
        assertNotNull("Container not available", context.getLocalContainer(candidate.getIdentity(), CODE, DOMAIN));
        candidate.destroy();
        assertNotNull("Container not available", context.getLocalContainer(candidate.getIdentity(), CODE, DOMAIN));
    }

    @Test
    public void testAddRemove() {
        String identity = "foo";
        IField field = new BooleanField(identity);
        candidate.add(field);
        candidate.remove(field);
        assertNull("component not removed", candidate.getFields().get(identity));
    }

    @Test
    public void testRemove_concurrency() throws Exception {
        doConcurrencyTest(false);
    }

    @Test
    public void testAdd_concurrency() throws Exception {
        doConcurrencyTest(true);
    }

    private void doConcurrencyTest(final boolean doAdd) throws InterruptedException, Exception {
        final CountDownLatch readerLatch = new CountDownLatch(1);
        final CountDownLatch writerLatch = new CountDownLatch(1);
        final Value<Exception> failed = new Value<Exception>();
        if (!doAdd) {
            String identity = "foo";
            IField field = new BooleanField(identity);
            candidate.add(field);
        }
        Thread reader = new Thread(new Runnable() {

            public void run() {
                try {
                    final Iterator<IField> iter = candidate.getFieldsToWrite(true).iterator();
                    writerLatch.countDown();
                    while (iter.hasNext()) {
                        readerLatch.await();
                        candidate.getComponentIdentities();
                        iter.next();
                    }
                } catch (Exception e) {
                    failed.set(e);
                }
            }
        });
        Thread writer = new Thread(new Runnable() {

            public void run() {
                try {
                    writerLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String identity = "foo";
                IField field = new BooleanField(identity);
                if (doAdd) {
                    candidate.add(field);
                } else {
                    candidate.remove(field);
                }
                readerLatch.countDown();
            }
        });
        reader.start();
        writer.start();
        reader.join();
        writer.join();
        if (failed.get() != null) {
            throw failed.get();
        }
    }

    @Test
    public void testAddRemoveNonLocal() throws InterruptedException {
        context.removeContainer(candidate);
        Thread t = new Thread(new Runnable() {

            public void run() {
                candidate = new BasicContainer(REMOTE_CONTEXT_IDENTITY, "JUnitContainer-" + System.nanoTime(), CODE, DOMAIN, context, false);
            }
        });
        t.start();
        t.join();
        try {
            candidate.add(field);
            fail("Should throw exception");
        } catch (IllegalStateException e) {
        }
        try {
            candidate.remove(field);
            fail("Should throw exception");
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testAddRemoveNonLocalWithReaderThread() throws InterruptedException, CloneNotSupportedException {
        context.removeContainer(candidate);
        Thread t = new Thread(new Runnable() {

            public void run() {
                candidate = new BasicContainer(context.getIdentity(), "JUnitContainer-" + System.nanoTime(), CODE, DOMAIN, context, true);
            }
        });
        t.start();
        t.join();
        final IField field = mocks.mock(IField.class);
        final String identity = ("sdf1");
        mocks.checking(new Expectations() {

            {
                one(field).toIdentityString();
                will(returnValue("field"));
                one(field).addedToContainer(candidate);
                one(field).removedFromContainer(candidate);
                allowing(field).getIdentity();
                will(returnValue(identity));
                one(field).clone();
                will(returnValue(field));
                allowing(field).getAddress();
                will(returnValue("address"));
            }
        });
        final Value<Boolean> passed = new Value<Boolean>();
        try {
            t = new Thread(new Runnable() {

                public void run() {
                    candidate.add(field);
                    passed.set(true);
                }
            });
            t.start();
            t.join();
            if (passed.get() == null || !passed.get()) {
                fail("did not pass");
            }
            passed.set(false);
        } catch (IllegalStateException e) {
        }
        try {
            t = new Thread(new Runnable() {

                public void run() {
                    candidate.remove(field);
                    passed.set(true);
                }
            });
            t.start();
            t.join();
            if (!passed.get()) {
                fail("did not pass");
            }
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testMarkForRemoteSubscription() {
        assertEquals("markForRemoteSubscription", 1, candidate.markForRemoteSubscription());
        assertEquals("markForRemoteSubscription", 2, candidate.markForRemoteSubscription());
        assertEquals("getRemoteSubscriptionCount", 2, candidate.getRemoteSubscriptionCount());
        assertEquals("unmarkForRemoteSubscription", 1, candidate.unmarkForRemoteSubscription());
        assertEquals("unmarkForRemoteSubscription", 0, candidate.unmarkForRemoteSubscription());
        assertEquals("unmarkForRemoteSubscription", 0, candidate.unmarkForRemoteSubscription());
        assertEquals("getRemoteSubscriptionCount", 0, candidate.getRemoteSubscriptionCount());
    }

    @Test
    public void testGet() {
        assertNotNull("get", candidate.get((IWF_FIELD1)));
    }

    @Test
    public void testGetComponentIdentities() {
        final String[] componentIdentities = candidate.getComponentIdentities();
        final List<String> list = Arrays.asList(componentIdentities);
        assertEquals("size", 2, list.size());
        assertTrue("IWF_FIELD1", list.contains((IWF_FIELD1)));
        assertTrue("IWF_FIELD2", list.contains((IWF_FIELD2)));
    }

    @Test
    public void testStartFrame() throws InterruptedException {
        candidate.beginFrame(new EventFrameExecution());
        final Value<Boolean> result = new Value<Boolean>();
        Runnable task = new Runnable() {

            public void run() {
                if (candidate.isFrameActive()) {
                    result.set(true);
                }
            }
        };
        Thread t = new Thread(task);
        t.start();
        t.join();
        assertTrue("Was not locked", result.get());
    }

    @Test
    public void testStartEventsOnlyAllowedOnce() throws InterruptedException {
        candidate.beginFrame(new EventFrameExecution());
        try {
            candidate.beginFrame(new EventFrameExecution());
            fail("Should throw exception");
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testCommitEvents() throws InterruptedException {
        candidate.beginFrame(new EventFrameExecution());
        candidate.endFrame();
        final Value<Boolean> result = new Value<Boolean>();
        Runnable task = new Runnable() {

            public void run() {
                if (candidate.eventFrameThread == null) {
                    result.set(true);
                }
            }
        };
        Thread t = new Thread(task);
        t.start();
        t.join();
        assertNotNull("Was locked", result.get());
        assertTrue("Was locked", result.get());
    }

    @Test
    public void testFlushFrame() {
        final Set<Boolean> set = CollectionFactory.newSet();
        AbstractContainer container = new AbstractContainer(context.getIdentity(), "SDF", CODE, DOMAIN, context, true) {

            public void doCommitEvents() {
                set.add(true);
            }

            public void addEvent(IEvent event) {
            }
        };
        container.start();
        container.flushFrame();
        assertTrue("Flush did not call commitEvents", set.size() == 1);
    }

    @Test
    public void testGetSetState() {
        final DataState[] values = DataState.values();
        for (DataState dataState : values) {
            candidate.setState(dataState);
            assertEquals("state", dataState, candidate.getDataState());
        }
    }

    @Test
    public void testReadWrite() throws InterruptedException {
        Random rnd = new Random();
        candidate.beginFrame(new EventFrameExecution());
        ((LongField) candidate.get((IWF_FIELD1))).set(rnd.nextLong());
        ((LongField) candidate.get((IWF_FIELD2))).set(rnd.nextLong());
        doReadWrite(context, candidate);
    }

    /**
     * Utility to get the byte[] for the candidate. This method triggers the
     * processEvents() and endFrame(), waits for the update and generates the
     * byte[] from the update.
     * 
     * @param candidate
     *            the container
     * @param hasLock
     *            does the current thread have the lock manager lock
     * @param waitForEventSourceNotObserved
     *            wait for an EventSourceNotObservedEvent
     * @return
     * @throws InterruptedException
     */
    public static byte[] getFrame(IFrameworkContext context, IContainer candidate, boolean hasLock, boolean waitForEventSourceNotObserved) throws InterruptedException {
        JUnitSystemEventListener notObservedListener = null;
        if (waitForEventSourceNotObserved) {
            notObservedListener = ContainerJUnitTest.subscribeEventSourceNotObservedListener(context, "notObservedListener");
        }
        final JUnitListener listener = new JUnitListener("GetFrame", EventListenerUtils.createFilter(TxEvent.class));
        try {
            candidate.addListener(listener);
            candidate.markForRemoteSubscription();
            if (!hasLock) {
                candidate.beginFrame(new EventFrameExecution());
            }
            candidate.endFrame();
            try {
                listener.waitForUpdate();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return listener.data.getBuffer();
        } finally {
            candidate.removeListener(listener);
            candidate.unmarkForRemoteSubscription();
            if (waitForEventSourceNotObserved) {
                ContainerJUnitTest.waitForNotObservedEventsAndThenRemoveListener(context, notObservedListener);
            }
        }
    }

    /**
     * Get a byte[] frame for the container, then apply this to a remote
     * instance and check that the remote instance is semantically equal
     * 
     * @param context
     *            the context
     * @param candidate
     *            the local container
     * @param waitForEventSourceNotObserved
     *            wait for an EventSourceNotObservedEvent
     * @throws InterruptedException
     */
    public static void doReadWrite(IFrameworkContext context, final IContainer candidate, boolean waitForEventSourceNotObserved) throws InterruptedException {
        final byte[] frame = getFrame(context, candidate, true, waitForEventSourceNotObserved);
        final IContainer result = readFrame(context, candidate, frame, waitForEventSourceNotObserved);
        assertEquals("Container not read correctly", candidate, result);
    }

    /**
     * @see #doReadWrite(IFrameworkContext, IContainer, boolean)
     * @param context
     * @param candidate
     * @throws InterruptedException
     */
    public static void doReadWrite(IFrameworkContext context, final IContainer candidate) throws InterruptedException {
        doReadWrite(context, candidate, true);
    }

    public static IContainer readFrame(final IFrameworkContext context, final IContainer candidate, final byte[] frame) throws InterruptedException {
        return readFrame(context, candidate, frame, true);
    }

    /**
     * Utility to get the created container from the frame
     * 
     * @param candidate
     *            the 'local' container
     * @param frame
     *            the frame for the remote container to read
     * @param waitForEventSourceNotObserved
     *            wait for an EventSourceNotObservedEvent
     * @return the 'remote' container
     * @throws InterruptedException
     */
    public static IContainer readFrame(final IFrameworkContext context, final IContainer candidate, final byte[] frame, boolean waitForEventSourceNotObserved) throws InterruptedException {
        final JUnitListener listener = new JUnitListener("ReadFrame", EventListenerUtils.createFilter(IContainer.class));
        final IContainer remoteContainer = context.getRemoteContainer(REMOTE_CONTEXT_IDENTITY, candidate.getIdentity(), candidate.getType(), candidate.getDomain());
        remoteContainer.addListener(listener);
        remoteRefs.add(remoteContainer);
        remoteContainer.markForRemoteSubscription();
        Runnable task = new Runnable() {

            public void run() {
                new FrameReader().read(frame, REMOTE_CONTEXT_IDENTITY, context);
            }
        };
        Thread t = new Thread(task);
        t.start();
        t.join();
        listener.waitForUpdate();
        JUnitSystemEventListener notObservedListener = null;
        if (waitForEventSourceNotObserved) {
            notObservedListener = ContainerJUnitTest.subscribeEventSourceNotObservedListener(context, "readFrame.allRemoved");
        }
        remoteContainer.removeListener(listener);
        remoteContainer.unmarkForRemoteSubscription();
        if (waitForEventSourceNotObserved) {
            ContainerJUnitTest.waitForNotObservedEventsAndThenRemoveListener(context, notObservedListener);
        }
        return listener.container;
    }

    private BasicContainer getClone() {
        BasicContainer clone = new BasicContainer(context.getIdentity(), candidate.getIdentity(), CODE, DOMAIN, context, true);
        builder.createContainerDefinition().populate(clone);
        return clone;
    }

    @Test
    public void testLockFrame() throws InterruptedException {
        candidate.lockFrame();
        Thread t = new Thread(new Runnable() {

            public void run() {
                long start = System.currentTimeMillis();
                candidate.lockFrame();
                long stop = System.currentTimeMillis();
                time = stop - start;
                gotLock = true;
                candidate.unlockFrame();
            }
        });
        t.start();
        Thread.sleep(SLEEP_TIME);
        candidate.unlockFrame();
        t.join(500);
        assertTrue("Did not get the lock", gotLock);
        assertTrue("Did not wait for the lock, took " + time, time > 100);
    }

    @Test
    public void testReLock() throws InterruptedException {
        candidate.lockFrame();
        candidate.lockFrame();
        Thread t = new Thread(new Runnable() {

            public void run() {
                long start = System.currentTimeMillis();
                candidate.lockFrame();
                long stop = System.currentTimeMillis();
                time = stop - start;
                gotLock = true;
                candidate.unlockFrame();
            }
        });
        t.start();
        Thread.sleep(SLEEP_TIME);
        candidate.unlockFrame();
        Thread.sleep(SLEEP_TIME);
        candidate.unlockFrame();
        t.join(SLEEP_TIME);
        assertTrue("Did not get the lock", gotLock);
        assertTrue("Did not wait for the lock", time > SLEEP_TIME * 1.5);
    }

    @Test
    public void testIsEventFrameThread() throws InterruptedException {
        assertFalse("Should not have the lock", candidate.isEventFrameThread());
        Thread t = new Thread(new Runnable() {

            public void run() {
                gotLock = candidate.isEventFrameThread();
            }
        });
        t.start();
        t.join();
        assertFalse("Should not have the lock", gotLock);
        candidate.lockFrame();
        assertTrue("Should have the lock", candidate.isEventFrameThread());
        t = new Thread(new Runnable() {

            public void run() {
                gotLock = candidate.isEventFrameThread();
            }
        });
        t.start();
        t.join();
        assertFalse("Should not have the lock", gotLock);
    }

    @Test
    public void testAddRemoveListenerRaisesEvents() throws InterruptedException {
        JUnitSystemEventListener listener = new JUnitSystemEventListener("testAddRemoveListenerRaisesEvents", EventListenerUtils.createFilter(EventSourceObservedEvent.class, EventSourceNotObservedEvent.class));
        context.getSystemEventSource(EventSourceObservedEvent.class).addListener(listener);
        final JUnitEventListener listener2 = new JUnitEventListener("another", EventListenerUtils.createFilter(IContainer.class));
        candidate.addListener(listener2);
        candidate.addListener(listener);
        Thread.sleep(100);
        listener.waitForUpdate();
        context.getSystemEventSource(EventSourceObservedEvent.class).removeListener(listener);
        listener.reset();
        context.getSystemEventSource(EventSourceNotObservedEvent.class).addListener(listener);
        candidate.removeListener(listener2);
        candidate.removeListener(listener);
        Thread.sleep(100);
        listener.waitForUpdate();
        context.getSystemEventSource(EventSourceNotObservedEvent.class).removeListener(listener);
    }

    /**
     * Creates a system listener for {@link EventSourceNotObservedEvent}s and
     * register it
     * 
     * @param context
     *            the context
     * @param listenerName
     *            the name for the listener
     * @return the created listener
     */
    private static JUnitSystemEventListener subscribeEventSourceNotObservedListener(IFrameworkContext context, String listenerName) {
        JUnitSystemEventListener listener = new JUnitSystemEventListener(listenerName, EventListenerUtils.createFilter(EventSourceNotObservedEvent.class));
        context.getSystemEventSource(EventSourceNotObservedEvent.class).addListener(listener);
        return listener;
    }

    /**
     * Waits for the expected number of updates then removes the system listener
     * created and registered in a previous call to
     * {@link #subscribeEventSourceNotObservedListener(IFrameworkContext, String)}
     * 
     * @param context
     *            the context
     * @param listener
     *            the created listener
     * @throws InterruptedException
     */
    private static void waitForNotObservedEventsAndThenRemoveListener(IFrameworkContext context, JUnitSystemEventListener listener) throws InterruptedException {
        try {
            listener.waitForUpdate();
        } finally {
            context.getSystemEventSource(EventSourceNotObservedEvent.class).removeListener(listener);
        }
    }
}

/**
 * JUnit version
 * 
 * @author Ramon Servadei
 * 
 */
final class JUnitContainerBuilder implements IContainerFactory.IContainerBuilder {

    final IFrameworkContext context;

    public JUnitContainerBuilder(IFrameworkContext context) {
        super();
        this.context = context;
    }

    public IContainer createContainer(String nativeContextIdentity, String identity, IType type, IDomain domain, IFrameworkContext hostContext, boolean local) {
        return new BasicContainer(nativeContextIdentity, identity, type, domain, hostContext, local);
    }

    public IContainerDefinitionField createContainerDefinition() {
        LongField iwfField1 = new LongField(ContainerJUnitTest.IWF_FIELD1);
        LongField iwfField2 = new LongField(ContainerJUnitTest.IWF_FIELD2);
        return new ContainerDefinitionField("JUnitReadWriteDefintion", iwfField1, iwfField2);
    }
}
