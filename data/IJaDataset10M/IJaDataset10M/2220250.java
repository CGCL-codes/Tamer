package org.eclipse.hyades.test.web.java.runner;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import junit.extensions.RepeatedTest;
import junit.extensions.TestDecorator;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.eclipse.hyades.execution.core.IControlMessage;
import org.eclipse.hyades.internal.execution.local.common.CustomCommand;
import org.eclipse.hyades.internal.execution.remote.RemoteComponentSkeleton;
import org.eclipse.hyades.test.common.event.ExecutionEvent;
import org.eclipse.hyades.test.common.event.InvocationEvent;
import org.eclipse.hyades.test.common.event.JUnitInvocationEvent;
import org.eclipse.hyades.test.common.event.LoopEvent;
import org.eclipse.hyades.test.common.event.MessageEvent;
import org.eclipse.hyades.test.common.event.TypedEvent;
import org.eclipse.hyades.test.common.event.VerdictEvent;
import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;
import org.eclipse.hyades.test.common.junit.HyadesTestUtil;
import org.eclipse.hyades.test.common.junit.IHyadesTest;
import org.eclipse.hyades.test.common.runner.HyadesRunner;
import org.eclipse.hyades.test.common.runner.internal.util.AgentConsoleStream;
import org.eclipse.hyades.test.common.util.BaseString;
import org.eclipse.hyades.test.java.runner.HyadesJUnitRunner;
import org.eclipse.hyades.test.java.runner.JUnitResourceBundle;
import org.eclipse.osgi.util.NLS;

/**
 * @author marcelop
 * @since 1.0.1
 */
public class WebTPTPTestRunner extends HyadesRunner implements TestListener {

    protected static final boolean SYSOUT = true;

    private Test rootTest;

    private RootObject rootObject = new RootObject();

    private StringBuffer failureCauses;

    private StringBuffer errorCauses;

    private Collection succeedTests = new ArrayList();

    private Collection failureTests = new ArrayList();

    private Collection errorTests = new ArrayList();

    private static Hashtable threadIDtoParentStackTable = new Hashtable();

    private static Hashtable threadIDtoParentThreadTable = new Hashtable();

    /**
	 * Describes an element in the parent stack.
	 * @since 4.2
	 */
    protected static interface ParentObject {

        String getId();
    }

    /**
	 * Describes an invocation element in the parent stack.
	 * @since 4.2
	 */
    protected static class Invocation implements ParentObject {

        private String invocationId;

        private Collection succeedTests = Collections.synchronizedCollection(new ArrayList());

        private Collection failureTests = Collections.synchronizedCollection(new ArrayList());

        private Collection errorTests = Collections.synchronizedCollection(new ArrayList());

        public Invocation(String invocationId) {
            this.invocationId = invocationId;
        }

        public void addSucceedTest(Test test) {
            succeedTests.add(test);
        }

        public void addFailureTest(Test test) {
            failureTests.add(test);
        }

        public void addErrorTest(Test test) {
            errorTests.add(test);
        }

        public Collection getErrorTests() {
            return errorTests;
        }

        public Collection getFailureTests() {
            return failureTests;
        }

        public Collection getSucceedTests() {
            return succeedTests;
        }

        public String getId() {
            return invocationId;
        }
    }

    /**
	 * Describes a loop in the parent stack.
	 * @since 4.2
	 */
    protected static class LoopObject implements ParentObject {

        private String loopId;

        public LoopObject(String loopId) {
            this.loopId = loopId;
        }

        public String getId() {
            return loopId;
        }
    }

    /**
	 * Describes the root of the parent stack.
	 * @since 4.2
	 */
    protected static class RootObject extends Invocation {

        public RootObject() {
            super(ExecutionEvent.ROOT_PARENT);
        }
    }

    /**
	 * Constructor for HyadesTestRunner
	 */
    public WebTPTPTestRunner() {
        super();
        initialize();
    }

    public WebTPTPTestRunner(RemoteComponentSkeleton agent) {
        super(agent);
        initialize();
    }

    public WebTPTPTestRunner(String args[]) {
        this(args, null);
    }

    public WebTPTPTestRunner(String args[], Vector commandHandler) {
        super(args, commandHandler);
        initialize();
    }

    private void initialize() {
        System.err.println("Start Web TPTP Test Runner");
        succeedTests = Collections.synchronizedCollection(new ArrayList());
        failureTests = Collections.synchronizedCollection(new ArrayList());
        errorTests = Collections.synchronizedCollection(new ArrayList());
        failureCauses = new StringBuffer();
        errorCauses = new StringBuffer();
    }

    public static void setParentThread(Thread child, Thread parent) {
        threadIDtoParentThreadTable.put(new Integer(child.hashCode()), new Integer(parent.hashCode()));
    }

    public static Integer getParentThreadID(Thread t) {
        return (Integer) threadIDtoParentThreadTable.get(new Integer(t.hashCode()));
    }

    private static Invocation peekParentInvocation(Stack stack) {
        for (int i = stack.size(); i > 0; i--) {
            Object o = stack.elementAt(i - 1);
            if (o instanceof Invocation) {
                return (Invocation) o;
            }
        }
        return null;
    }

    public static Integer getParentThreadID(Integer i) {
        return (Integer) threadIDtoParentThreadTable.get(i);
    }

    public static Stack getParentStack() {
        int myThreadID = Thread.currentThread().hashCode();
        return (Stack) threadIDtoParentStackTable.get(new Integer(myThreadID));
    }

    public static void setParentStack(Stack parentStack) {
        int myThreadID = Thread.currentThread().hashCode();
        threadIDtoParentStackTable.put(new Integer(myThreadID), parentStack);
    }

    /**
	 * @deprecated Use the non-static method {@link #_peekParentEventID()} instead.
	 */
    public static String peekParentEventID() {
        return peekParentEventID(new Integer(Thread.currentThread().hashCode()));
    }

    /**
	 * @since 4.2
	 */
    protected String _peekParentEventID() {
        return _peekParentEventID(new Integer(Thread.currentThread().hashCode()));
    }

    /**
	 * @since 4.2
	 */
    protected ParentObject peekParentObject() {
        return peekParentObject(new Integer(Thread.currentThread().hashCode()));
    }

    /**
	 * @since 4.2
	 */
    protected Invocation peekParentInvocation() {
        return peekParentInvocation(new Integer(Thread.currentThread().hashCode()));
    }

    /**
	 * @deprecated Use the non-static method {@link #_peekParentEventID(Integer)} instead.
	 */
    public static String peekParentEventID(Integer threadID) {
        return peekParentObject(threadID, null).getId();
    }

    /**
	 * @since 4.2
	 */
    protected String _peekParentEventID(Integer threadID) {
        return peekParentObject(threadID).getId();
    }

    /**
	 * @since 4.2
	 */
    protected ParentObject peekParentObject(Integer threadID) {
        return peekParentObject(threadID, this);
    }

    private static ParentObject peekParentObject(Integer threadID, WebTPTPTestRunner runner) {
        Stack parentIDStack = (Stack) threadIDtoParentStackTable.get(threadID);
        try {
            if (parentIDStack != null && !parentIDStack.isEmpty()) {
                ParentObject parent = (ParentObject) parentIDStack.peek();
                if (parent != null) {
                    return parent;
                }
            }
        } catch (EmptyStackException e) {
        }
        Integer parentThreadID = getParentThreadID(threadID);
        if (parentThreadID == null) {
            return runner != null ? runner.rootObject : new RootObject();
        }
        return peekParentObject(parentThreadID, runner);
    }

    /**
	 * @since 4.2
	 */
    protected Invocation peekParentInvocation(Integer threadID) {
        Stack parentIDStack = (Stack) threadIDtoParentStackTable.get(threadID);
        try {
            if (parentIDStack != null) {
                Invocation parent = peekParentInvocation(parentIDStack);
                if (parent != null) {
                    return parent;
                }
            }
        } catch (EmptyStackException e) {
        }
        Integer parentThreadID = getParentThreadID(threadID);
        if (parentThreadID == null) {
            return rootObject;
        }
        return peekParentInvocation(parentThreadID);
    }

    /**
	 * @deprecated Use the non-static method {@link #_pushParentObject(org.eclipse.hyades.test.common.junit.HyadesTestRunner.ParentObject)} instead.
	 */
    public static void pushParentObject(ParentObject object) {
        __pushParentObject(object);
    }

    /**
	 * @since 4.2
	 */
    protected void _pushParentObject(ParentObject object) {
        __pushParentObject(object);
    }

    private static synchronized void __pushParentObject(ParentObject object) {
        Stack myParentIDStack = getParentStack();
        if (myParentIDStack != null) {
            myParentIDStack.push(object);
        } else {
            myParentIDStack = new Stack();
            myParentIDStack.push(object);
            setParentStack(myParentIDStack);
        }
    }

    /**
	 * @deprecated Use the non-static method {@link #_popParentObject()} instead.
	 */
    public static ParentObject popParentObject() {
        return popParentObject(null);
    }

    /**
	 * @since 4.2
	 * @return
	 */
    protected ParentObject _popParentObject() {
        return popParentObject(this);
    }

    private static synchronized ParentObject popParentObject(WebTPTPTestRunner runner) {
        Stack myParentIDStack = getParentStack();
        try {
            if (myParentIDStack != null && !myParentIDStack.isEmpty()) {
                ParentObject parent = (ParentObject) myParentIDStack.pop();
                if (parent == null) {
                    return runner != null ? runner.rootObject : new RootObject();
                } else {
                    return parent;
                }
            }
        } catch (EmptyStackException e) {
        }
        return runner != null ? runner.rootObject : new RootObject();
    }

    /**
	 * @deprecated Use the non-static method {@link #_seedParentStack(Thread)} instead.
	 */
    public static synchronized void seedParentStack(Thread t) {
        Stack newParentIDStack = new Stack();
        newParentIDStack.push(peekParentObject(new Integer(Thread.currentThread().hashCode()), null));
        int newThreadID = t.hashCode();
        threadIDtoParentStackTable.put(new Integer(newThreadID), newParentIDStack);
    }

    /**
	 * @since 4.2
	 */
    public void _seedParentStack(Thread t) {
        Stack newParentIDStack = new Stack();
        newParentIDStack.push(peekParentObject());
        int newThreadID = t.hashCode();
        threadIDtoParentStackTable.put(new Integer(newThreadID), newParentIDStack);
    }

    /**
	 * Returns the root test, ie. the test that is passed to this
	 * runner.
	 * @return Test
	 */
    public Test getRoot() {
        return rootTest;
    }

    /**
	 * Sets the root test, ie. the test that is passed to this
	 * runner.
	 * @param root
	 */
    protected void setRoot(Test root) {
        rootTest = root;
        rootObject = new RootObject();
    }

    /**
	 * Returns a collection with the hirarchy ids of the test cases that have 
	 * been succeed.
	 * @return Collection
	 */
    public Collection getSucceedTests() {
        return succeedTests;
    }

    /**
	 * Returns a collection with the hirarchy ids of the test cases that have 
	 * ended because of a failure.
	 * @return Collection
	 */
    public Collection getFailureTests() {
        return failureTests;
    }

    /**
	 * Returns a collection with the hirarchy ids of the test cases that have
	 * ended because of an error.
	 * @return Collection
	 */
    public Collection getErrorTests() {
        return errorTests;
    }

    /**
	 * Runs a suite extracted from a TestCase subclass.
	 * @param testClass
	 */
    public void run(Class testClass) {
        run(new HyadesTestSuite(testClass));
    }

    /**
	 * Runs a single test and collects its results. This method can be used to 
	 * start a test run from your program.
	 * @param suite
	 */
    public void run(Test test) {
        setRoot(test);
        TestResult result = createTestResult();
        result.addListener(this);
        runnerStarted();
        long startTime = getCurrentTime();
        try {
            if (!(test instanceof IHyadesTest)) {
                startTest(test);
            }
            test.run(result);
        } finally {
            if (!(test instanceof IHyadesTest)) {
                endTest(test);
            }
            long endTime = getCurrentTime();
            runnerExit((result == null || result.shouldStop()), (endTime - startTime));
        }
    }

    /**
	 * Creates the TestResult to be used for the test run.
	 * @return TestResult
	 */
    protected TestResult createTestResult() {
        return new WebTPTPTestResult();
    }

    /**
	 * Returns the current timestamp.
	 * @return long
	 */
    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
	 * @see junit.framework.TestListener#startTest(junit.framework.Test)
	 */
    public void startTest(Test test) {
        if (SYSOUT) System.out.println("\nStart\n\t" + HyadesTestUtil.getHierarchy(test) + "\n\t" + HyadesTestUtil.getHierarchyIds(test));
        TypedEvent typedEvent = new TypedEvent();
        typedEvent.setType(TypedEvent.START);
        if (test instanceof IHyadesTest) {
            IHyadesTest hyadesTest = (IHyadesTest) test;
            String invocationID = hyadesTest.getTestInvocationId();
            if (invocationID != null) {
                String hierarchyId = HyadesTestUtil.getHierarchyIds(hyadesTest.getParent());
                hierarchyId = HyadesTestUtil.appendToHierarchyId(hierarchyId, invocationID, hyadesTest.getIteration());
                InvocationEvent invocationEvent = new InvocationEvent();
                invocationID = appendLoopIterationToID(invocationID, (IHyadesTest) hyadesTest.getParent());
                invocationEvent.setId(invocationID);
                invocationEvent.setParentId(_peekParentEventID());
                _pushParentObject(new Invocation(invocationID));
                invocationEvent.setOwnerId(hyadesTest.getTestInvocationId());
                invocationEvent.setStatus(InvocationEvent.STATUS_SUCCESSFUL);
                invocationEvent.setReason(InvocationEvent.REASON_NONE);
                invocationEvent.setInvokedId(HyadesTestUtil.getHierarchyIds(hyadesTest));
                writeEvent(invocationEvent);
                typedEvent.setOwnerId(hyadesTest.getTestInvocationId());
            } else if (hyadesTest.getParent() != null) {
                String loopId = hyadesTest.getId();
                loopId = appendLoopIterationToID(loopId, (IHyadesTest) hyadesTest.getParent());
                if (hyadesTest.getIteration() == 1) {
                    String parentID = _peekParentEventID();
                    LoopEvent loopEvent = new LoopEvent();
                    loopEvent.setId(loopId);
                    loopEvent.setAsychronous(!(hyadesTest.isSynchronous()));
                    loopEvent.setOwnerId(hyadesTest.getId());
                    loopEvent.setParentId(parentID);
                    if (hyadesTest instanceof HyadesTestSuite) loopEvent.setIterations(1);
                    writeEvent(loopEvent);
                    _pushParentObject(new LoopObject(loopId));
                    typedEvent.setOwnerId(hyadesTest.getId());
                } else if (hyadesTest.getIteration() > 1) {
                    _pushParentObject(new LoopObject(loopId));
                    typedEvent.setOwnerId(hyadesTest.getId());
                }
            }
        } else {
            if (test != getRoot()) {
                String invocationID = HyadesTestUtil.getTestId(test);
                JUnitInvocationEvent invocationEvent = new JUnitInvocationEvent();
                invocationEvent.setId(invocationID);
                invocationEvent.setParentId(_peekParentEventID());
                _pushParentObject(new Invocation(invocationID));
                invocationEvent.setStatus(InvocationEvent.STATUS_SUCCESSFUL);
                invocationEvent.setReason(InvocationEvent.REASON_NONE);
                invocationEvent.setName(getTestName(test));
                invocationEvent.setClassName(test.getClass().getName());
                writeEvent(invocationEvent);
            }
        }
        typedEvent.setParentId(_peekParentEventID());
        writeEvent(typedEvent);
    }

    protected static String getTestName(Test test) {
        if (test instanceof TestSuite) {
            return ((TestSuite) test).getName();
        } else if (test instanceof TestCase) {
            return ((TestCase) test).getName();
        } else if (test instanceof TestDecorator) {
            return getTestName(((TestDecorator) test).getTest());
        } else {
            return test.toString();
        }
    }

    /**
	 * @see junit.framework.TestListener#endTest(junit.framework.Test)
	 */
    public void endTest(Test test) {
        if (test == getRoot()) return;
        ParentObject parent = _popParentObject();
        Invocation parentInvocation = (parent instanceof Invocation) ? (Invocation) parent : null;
        if (SYSOUT) {
            System.out.println("\nEnd\n\t" + HyadesTestUtil.getHierarchy(test) + "\n\t" + HyadesTestUtil.getHierarchyIds(test));
            if (parentInvocation != null && (test instanceof HyadesTestSuite) && (((HyadesTestSuite) test).getArbiter() != null)) {
                System.out.println("\tResults:" + "\n\t\tsucceedCount: " + parentInvocation.getSucceedTests().size() + "\n\t\tfailureCount: " + parentInvocation.getFailureTests().size() + "\n\t\terrorCount: " + parentInvocation.getErrorTests().size());
            }
        }
        if (parentInvocation != null) {
            if ((test instanceof TestCase) && (!parentInvocation.getFailureTests().contains(test)) && (!parentInvocation.getErrorTests().contains(test))) parentInvocation.addSucceedTest(test);
            VerdictEvent verdictEvent = getDefaultVerdictEvent(test, parentInvocation);
            if (!isRedundantVerdict(test, parentInvocation)) {
                writeEvent(verdictEvent);
            }
            getFailureTests().addAll(parentInvocation.getFailureTests());
            getErrorTests().addAll(parentInvocation.getErrorTests());
            getSucceedTests().addAll(parentInvocation.getSucceedTests());
            if (verdictEvent != null) {
                propagateVerdict(test, verdictEvent);
            }
        }
        TypedEvent typedEvent = new TypedEvent();
        if (test instanceof IHyadesTest) {
            IHyadesTest hyadesTest = (IHyadesTest) test;
            String invocationID = hyadesTest.getTestInvocationId();
            if (invocationID != null) {
                String hierarchyId = HyadesTestUtil.getHierarchyIds(hyadesTest.getParent());
                hierarchyId = HyadesTestUtil.appendToHierarchyId(hierarchyId, invocationID, hyadesTest.getIteration());
                typedEvent.setParentId(parent.getId());
                typedEvent.setOwnerId(invocationID);
            } else if (hyadesTest.getParent() != null && hyadesTest.getIteration() > 0) {
                typedEvent.setParentId(parent.getId());
                typedEvent.setOwnerId(hyadesTest.getId());
            }
        } else {
            typedEvent.setParentId(parent.getId());
        }
        typedEvent.setType(TypedEvent.STOP);
        writeEvent(typedEvent);
    }

    protected void propagateVerdict(Test test, VerdictEvent event) {
        Invocation parentInvocation = peekParentInvocation();
        if (event.getVerdict() == VerdictEvent.VERDICT_ERROR) {
            parentInvocation.addErrorTest(test);
        } else if (event.getVerdict() == VerdictEvent.VERDICT_FAIL) {
            parentInvocation.addFailureTest(test);
        } else if (event.getVerdict() == VerdictEvent.VERDICT_PASS) {
            parentInvocation.addSucceedTest(test);
        }
    }

    /**
	 * Returns the default verdict event, which is pass for test
	 * cases and the arbiter evaluation for test suites.
	 * @param test
	 * @return VerdictEvent
	 */
    protected VerdictEvent getDefaultVerdictEvent(Test test, Invocation parent) {
        VerdictEvent verdictEvent = null;
        if (test instanceof TestSuite) {
            if (test instanceof HyadesTestSuite) {
                HyadesTestSuite hyadesTestSuite = (HyadesTestSuite) test;
                if (hyadesTestSuite.getArbiter() != null) {
                    verdictEvent = hyadesTestSuite.getArbiter().analyse(hyadesTestSuite, parent.getSucceedTests(), parent.getErrorTests(), parent.getFailureTests(), errorCauses.toString(), failureCauses.toString());
                    if (verdictEvent != null) {
                        if (verdictEvent.getOwnerId() == null) verdictEvent.setOwnerId(hyadesTestSuite.getId());
                    }
                }
            } else {
                verdictEvent = new VerdictEvent();
                if (!parent.getErrorTests().isEmpty()) {
                    verdictEvent.setVerdict(VerdictEvent.VERDICT_ERROR);
                } else if (!parent.getFailureTests().isEmpty()) {
                    verdictEvent.setVerdict(VerdictEvent.VERDICT_FAIL);
                } else if (!parent.getSucceedTests().isEmpty()) {
                    verdictEvent.setVerdict(VerdictEvent.VERDICT_PASS);
                } else {
                    verdictEvent.setVerdict(VerdictEvent.VERDICT_INCONCLUSIVE);
                }
            }
        } else if (test instanceof TestCase) {
            if (parent.getFailureTests().contains(test)) {
                verdictEvent = new VerdictEvent();
                verdictEvent.setVerdict(VerdictEvent.VERDICT_FAIL);
            } else if (parent.getErrorTests().contains(test)) {
                verdictEvent = new VerdictEvent();
                verdictEvent.setVerdict(VerdictEvent.VERDICT_ERROR);
            } else {
                verdictEvent = new VerdictEvent();
                verdictEvent.setVerdict(VerdictEvent.VERDICT_PASS);
            }
            if (verdictEvent != null) {
                verdictEvent.setId(computeVerdictId(parent.getId()));
                if (test instanceof HyadesTestCase) {
                    HyadesTestCase hyadesTestCase = (HyadesTestCase) test;
                    verdictEvent.setOwnerId(hyadesTestCase.getId());
                }
            }
        }
        if (verdictEvent != null) verdictEvent.setParentId(parent.getId());
        return verdictEvent;
    }

    protected boolean isRedundantVerdict(Test test, Invocation parent) {
        if (test instanceof TestSuite) {
            return false;
        }
        if (parent.getFailureTests().size() + parent.getErrorTests().size() == 1) {
            if (parent.getSucceedTests().size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Returns the default verdict event, which is pass for test
	 * cases and the arbiter evaluation for test suites.
	 * @param test
	 * @return VerdictEvent
	 * @deprecated Use {@link #getDefaultVerdictEvent(Test, org.eclipse.hyades.test.common.junit.HyadesTestRunner.Invocation)} instead.
	 */
    protected VerdictEvent getDefaultVerdictEvent(Test test) {
        VerdictEvent verdictEvent = null;
        if (test instanceof TestSuite) {
            if (test instanceof HyadesTestSuite) {
                HyadesTestSuite hyadesTestSuite = (HyadesTestSuite) test;
                if (hyadesTestSuite.getArbiter() != null) {
                    verdictEvent = hyadesTestSuite.getArbiter().analyse(hyadesTestSuite, filterTests(test, getSucceedTests()), filterTests(test, getErrorTests()), filterTests(test, getFailureTests()), errorCauses.toString(), failureCauses.toString());
                    if (verdictEvent != null) {
                        if (verdictEvent.getOwnerId() == null) verdictEvent.setOwnerId(hyadesTestSuite.getId());
                    }
                }
            } else if (test == getRoot()) {
                verdictEvent = new VerdictEvent();
                if (errorTests.size() > 0) {
                    verdictEvent.setVerdict(VerdictEvent.VERDICT_ERROR);
                } else if (failureTests.size() > 0) {
                    verdictEvent.setVerdict(VerdictEvent.VERDICT_FAIL);
                } else if (succeedTests.size() > 0) {
                    verdictEvent.setVerdict(VerdictEvent.VERDICT_PASS);
                } else {
                    verdictEvent.setVerdict(VerdictEvent.VERDICT_INCONCLUSIVE);
                }
            }
        } else if (test instanceof HyadesTestCase) {
            HyadesTestCase hyadesTestCase = (HyadesTestCase) test;
            if ((!getFailureTests().contains(test)) && (!getErrorTests().contains(test))) {
                verdictEvent = new VerdictEvent();
                verdictEvent.setOwnerId(hyadesTestCase.getId());
                verdictEvent.setVerdict(VerdictEvent.VERDICT_PASS);
                verdictEvent.setId(computeVerdictId(peekParentEventID()));
            }
        } else {
            if (failureTests.contains(test)) {
            } else if (errorTests.contains(test)) {
            } else {
                verdictEvent = new VerdictEvent();
                verdictEvent.setVerdict(VerdictEvent.VERDICT_PASS);
                verdictEvent.setId(computeVerdictId(peekParentEventID()));
            }
        }
        if (verdictEvent != null) verdictEvent.setParentId(peekParentEventID());
        return verdictEvent;
    }

    /**
	 * @see junit.framework.TestListener#addError(junit.framework.Test, java.lang.Throwable)
	 */
    public void addError(Test test, Throwable throwable) {
        if (SYSOUT) System.out.println("\nError\n\t" + HyadesTestUtil.getHierarchy(test) + "\n\t" + HyadesTestUtil.getHierarchyIds(test));
        Invocation parentInvocation = peekParentInvocation();
        ParentObject parent = peekParentObject();
        VerdictEvent verdictEvent = new VerdictEvent();
        if (test instanceof IHyadesTest) {
            String id = computeVerdictId(parent.getId());
            verdictEvent.setId(id);
            synchronized (errorCauses) {
                if (errorCauses.length() > 0) errorCauses.append(',');
                errorCauses.append(id);
            }
        }
        parentInvocation.addErrorTest(test);
        verdictEvent.setParentId(parent.getId());
        verdictEvent.setVerdict(VerdictEvent.VERDICT_ERROR);
        verdictEvent.setReason(VerdictEvent.REASON_SEE_DESCRIPTION);
        verdictEvent.setText(BaseString.getStackTrace(throwable));
        writeEvent(verdictEvent);
    }

    /**
	 * @see junit.framework.TestListener#addFailure(junit.framework.Test, junit.framework.AssertionFailedError)
	 */
    public void addFailure(Test test, AssertionFailedError throwable) {
        if (SYSOUT) System.out.println("\nFailure\n\t" + HyadesTestUtil.getHierarchy(test) + "\n\t" + HyadesTestUtil.getHierarchyIds(test));
        Invocation parentInvocation = peekParentInvocation();
        ParentObject parent = peekParentObject();
        VerdictEvent verdictEvent = new VerdictEvent();
        if (test instanceof IHyadesTest) {
            String id = computeVerdictId(parent.getId());
            verdictEvent.setId(id);
            synchronized (failureCauses) {
                if (failureCauses.length() > 0) failureCauses.append(',');
                failureCauses.append(id);
            }
        }
        parentInvocation.addFailureTest(test);
        verdictEvent.setParentId(parent.getId());
        verdictEvent.setVerdict(VerdictEvent.VERDICT_FAIL);
        verdictEvent.setReason(VerdictEvent.REASON_SEE_DESCRIPTION);
        verdictEvent.setText(BaseString.getStackTrace(throwable));
        writeEvent(verdictEvent);
    }

    /**
	 * Filters and retains from a collection of tests, the tests are direct children
	 * of a parent test.
	 * @param parent
	 * @param tests
	 * @return
	 * @deprecated This method does not work properly when tests contains non-TPTP JUnit tests 
	 * (i.e. tests that do not implement {@link IHyadesTest}).
	 * Use {@link Invocation} instances from the stack {@link #getParentStack()} to determine
	 * which tests are direct children of a given parent.
	 */
    protected Collection filterTests(Test parent, Collection tests) {
        Test[] testArray = (Test[]) tests.toArray(new Test[tests.size()]);
        Collection filteredTests = new ArrayList(testArray.length);
        for (int i = 0, maxi = testArray.length; i < maxi; i++) {
            if ((testArray[i] instanceof IHyadesTest) && HyadesTestUtil.isParentOf(parent, testArray[i])) filteredTests.add(testArray[i]);
        }
        return Collections.unmodifiableCollection(filteredTests);
    }

    protected String computeVerdictId(String parentId) {
        return parentId + "_verdict";
    }

    protected String appendLoopIterationToID(String ID, IHyadesTest parentTest) {
        String iterations = HyadesTestSuite.getIterationsString(_peekParentEventID(), parentTest);
        return ID + iterations;
    }

    public static final String LOADTESTSUITEPREFIX = "_@USERGROUP_";

    protected AgentConsoleStream stdout = null;

    protected AgentConsoleStream stderr = null;

    protected static final String UTF8 = "UTF-8";

    public static Test getTest(Class clazz) {
        try {
            Method m = clazz.getMethod("suite", null);
            return (Test) m.invoke(null, null);
        } catch (InvocationTargetException e) {
            return createErrorTest("Exception thrown by suite() invocation", e.getCause());
        } catch (Exception e) {
            return new TestSuite(clazz);
        }
    }

    protected static Test createFailTest(String testName, final String message) {
        TestSuite ts = new TestSuite();
        ts.addTest(new TestCase(testName) {

            protected void runTest() {
                fail(message);
            }
        });
        return ts;
    }

    protected static Test createErrorTest(String testName, final Throwable e) {
        TestSuite ts = new TestSuite();
        ts.addTest(new TestCase(testName) {

            protected void runTest() throws Throwable {
                throw e;
            }
        });
        return ts;
    }

    public static void main(String[] args) {
        WebTPTPTestRunner runner = new WebTPTPTestRunner(args);
        try {
            runner.stdout = new AgentConsoleStream(runner.agent, AgentConsoleStream.OUT, null, runner._peekParentEventID());
            runner.stderr = new AgentConsoleStream(runner.agent, AgentConsoleStream.ERR, null, runner._peekParentEventID());
            System.setOut(new PrintStream(runner.stdout, false, UTF8));
            System.setErr(new PrintStream(runner.stderr, false, UTF8));
            Class c = Class.forName(runner.testClass, true, WebTPTPTestRunner.class.getClassLoader());
            Test test = getTest(c);
            int instances = 1;
            String sInstances = System.getProperty("hyades.loadtest.nusers");
            if (sInstances != null) {
                try {
                    instances = Integer.valueOf(sInstances).intValue();
                } catch (Exception e) {
                    instances = 1;
                }
            }
            if (instances <= 1) {
                runner.run(test);
            } else if (test instanceof HyadesTestSuite) {
                HyadesTestSuite suite = (HyadesTestSuite) test;
                runner.agent.sendMessageToAttachedClient("Running a " + instances + " instance test", 0);
                HyadesTestSuite loadTest = createLoadTestSuite(suite, instances);
                runner.run(loadTest);
            }
            runner.agent.sendMessageToAttachedClient("Testcase completed successfuly", 0);
            runner.agent.sendMessageToAttachedClient(IControlMessage.STOP, 0);
            System.out.flush();
            System.err.flush();
        } catch (Throwable e) {
            System.out.flush();
            System.err.flush();
            runner.reportException(e);
        }
    }

    protected void reportException(Throwable t) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setText(BaseString.getStackTrace(t));
        messageEvent.setSeverity(MessageEvent.ERROR);
        messageEvent.setOwnerId(testID);
        messageEvent.setParentId("ROOT");
        agent.logMessageUTF8(messageEvent.toString());
    }

    public void handleCustomCommand(CustomCommand command) {
    }

    /**
	 * Flush any data that is cached in the sysout and syserr consoles
	 */
    protected void flushConsoles() {
        if (stdout != null) stdout.flush();
        if (stderr != null) stderr.flush();
    }

    /**
	 * @see org.eclipse.hyades.test.common.junit.HyadesTestRunner#runnerStarted()
	 */
    protected void runnerStarted() {
        writeExecEvent("<EXECUTION>");
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < trace.length; i++) {
            StackTraceElement t = trace[i];
            System.out.println(t.toString());
        }
        int count = HyadesTestUtil.countTests(new Test[] { getRoot() });
        if (SYSOUT) System.out.println("Run Started\n\tNumber of tests: " + count);
    }

    /**
	 * @see org.eclipse.hyades.test.common.junit.HyadesTestRunner#runnerExit(boolean, long)
	 */
    protected void runnerExit(boolean runnerStopped, long elapsedTime) {
        if (SYSOUT) {
            if (runnerStopped) System.out.println("\nRun End"); else System.out.println("\nRun Stop");
            System.out.println("\t" + HyadesTestUtil.getHierarchy(getRoot()) + "\n\t" + HyadesTestUtil.getHierarchyIds(getRoot()));
            System.out.println("\n\nResults:" + "\n\tsucceedCount: " + getSucceedTests().size() + "\n\tfailureCount: " + getFailureTests().size() + "\n\terrorCount: " + getErrorTests().size());
        }
        writeEvent(getDefaultVerdictEvent(getRoot(), rootObject));
        TypedEvent typedEvent = new TypedEvent();
        typedEvent.setOwnerId(HyadesTestUtil.getHierarchyIds(getRoot()));
        typedEvent.setType(TypedEvent.STOP);
        typedEvent.setText(getLastEventText(runnerStopped, elapsedTime));
        typedEvent.setParentId(ExecutionEvent.ROOT_PARENT);
        writeEvent(typedEvent);
        writeExecEvent("</EXECUTION>");
    }

    /**
	 * @see org.eclipse.hyades.test.common.junit.HyadesTestRunner#writeEvent(org.eclipse.hyades.test.common.event.ExecutionEvent)
	 */
    public void writeEvent(ExecutionEvent executionEvent) {
        flushConsoles();
        writeExecEvent(executionEvent);
    }

    /**
	 * @see org.eclipse.hyades.test.common.junit.HyadesTestRunner#getLastEventText(boolean)
	 */
    protected String getLastEventText(boolean runnerStopped, long elapsedTime) {
        String[] args = new String[] { new Integer(getSucceedTests().size() + getFailureTests().size() + getErrorTests().size()).toString(), new Integer(getFailureTests().size()).toString(), new Integer(getErrorTests().size()).toString(), new Long(elapsedTime).toString() };
        if (runnerStopped) return NLS.bind(JUnitResourceBundle.execution_TestStoped, args);
        return NLS.bind(JUnitResourceBundle.execution_TestsFinished, args);
    }

    protected static HyadesTestSuite createLoadTestSuite(HyadesTestSuite suite, int instances) {
        String id = suite.getId();
        HyadesTestSuite root = new HyadesTestSuite("root");
        root.setArbiter(DefaultTestArbiter.INSTANCE).setId(id + "HTTP_ROOTID");
        HyadesTestSuite usergroup = new HyadesTestSuite(LOADTESTSUITEPREFIX + instances);
        root.addTest(new RepeatedTest(usergroup, instances));
        usergroup.setId(id + "_HTTP_USERGROUP").setSynchronous(false);
        usergroup.addTest(suite);
        return root;
    }
}
