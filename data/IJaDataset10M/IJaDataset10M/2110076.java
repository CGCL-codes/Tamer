package org.jmock;

import java.util.HashSet;
import java.util.Set;
import org.jmock.api.Expectation;
import org.jmock.api.ExpectationError;
import org.jmock.api.ExpectationErrorTranslator;
import org.jmock.api.Imposteriser;
import org.jmock.api.Invocation;
import org.jmock.api.Invokable;
import org.jmock.api.MockObjectNamingScheme;
import org.jmock.internal.CaptureControl;
import org.jmock.internal.ExpectationBuilder;
import org.jmock.internal.ExpectationCapture;
import org.jmock.internal.InvocationDispatcher;
import org.jmock.internal.InvocationDiverter;
import org.jmock.internal.NamedSequence;
import org.jmock.internal.ProxiedObjectIdentity;
import org.jmock.internal.ReturnDefaultValueAction;
import org.jmock.lib.CamelCaseNamingScheme;
import org.jmock.lib.IdentityExpectationErrorTranslator;
import org.jmock.lib.JavaReflectionImposteriser;

/**
 * A Mockery represents the context, or neighbourhood, of the object(s) under test.  
 * 
 * The neighbouring objects in that context are mocked out. The test specifies the 
 * expected interactions between the object(s) under test and its  neighbours and 
 * the Mockery checks those expectations while the test is running.
 * 
 * @author npryce
 * @author smgf
 * @author named by Ivan Moore.
 */
public class Mockery {

    private Set<String> mockNames = new HashSet<String>();

    private Imposteriser imposteriser = JavaReflectionImposteriser.INSTANCE;

    private ExpectationErrorTranslator expectationErrorTranslator = IdentityExpectationErrorTranslator.INSTANCE;

    private MockObjectNamingScheme namingScheme = CamelCaseNamingScheme.INSTANCE;

    private ReturnDefaultValueAction defaultAction = new ReturnDefaultValueAction(imposteriser);

    private InvocationDispatcher dispatcher = new InvocationDispatcher();

    private ExpectationCapture capture = null;

    private Throwable firstError = null;

    /**
     * Sets the result returned for the given type when no return value has been explicitly
     * specified in the expectation.
     * 
     * @param type
     *    The type for which to return <var>result</var>.
     * @param result
     *    The value to return when a method of return type <var>type</var>
     *    is invoked for which an explicit return value has has not been specified.
     */
    public void setDefaultResultForType(Class<?> type, Object result) {
        defaultAction.addResult(type, result);
    }

    /**
     * Changes the imposteriser used to adapt mock objects to the mocked type.
     * 
     * The default imposteriser allows a test to mock interfaces but not
     * classes, so you'll have to plug a different imposteriser into the
     * Mockery if you want to mock classes.
     */
    public void setImposteriser(Imposteriser imposteriser) {
        this.imposteriser = imposteriser;
        this.defaultAction.setImposteriser(imposteriser);
    }

    /**
     * Changes the naming scheme used to generate names for mock objects that 
     * have not been explicitly named in the test.
     * 
     * The default naming scheme names mock objects by lower-casing the first
     * letter of the class name, so a mock object of type BananaSplit will be
     * called "bananaSplit" if it is not explicitly named in the test.
     */
    public void setNamingScheme(MockObjectNamingScheme namingScheme) {
        this.namingScheme = namingScheme;
    }

    /**
     * Changes the expectation error translator used to translate expectation
     * errors into errors that report test failures.
     * 
     * By default, expectation errors are not translated and are thrown as
     * errors of type {@link ExpectationError}.  Plug in a new expectation error
     * translator if you want your favourite test framework to report expectation 
     * failures using its own error type.
     */
    public void setExpectationErrorTranslator(ExpectationErrorTranslator expectationErrorTranslator) {
        this.expectationErrorTranslator = expectationErrorTranslator;
    }

    /**
     * Creates a mock object of type <var>typeToMock</var> and generates a name for it.
     */
    public <T> T mock(Class<T> typeToMock) {
        return mock(typeToMock, namingScheme.defaultNameFor(typeToMock));
    }

    /**
     * Creates a mock object of type <var>typeToMock</var> with the given name.
     */
    public <T> T mock(Class<T> typeToMock, String name) {
        if (mockNames.contains(name)) {
            throw new IllegalArgumentException("a mock with name " + name + " already exists");
        }
        MockObject mock = new MockObject(name);
        mockNames.add(name);
        ProxiedObjectIdentity invokable = new ProxiedObjectIdentity(new InvocationDiverter<CaptureControl>(CaptureControl.class, mock, mock));
        return imposteriser.imposterise(invokable, typeToMock, CaptureControl.class);
    }

    /** 
     * Returns a new sequence that is used to constrain the order in which 
     * expectations can occur.
     * 
     * @param name
     *     The name of the sequence.
     * @return
     *     A new sequence with the given name.
     */
    public Sequence sequence(String name) {
        return new NamedSequence(name);
    }

    /** 
     * Returns a new state machine that is used to constrain the order in which 
     * expectations can occur.
     * 
     * @param name
     *     The name of the state machine.
     * @return
     *     A new state machine with the given name.
     */
    public States states(String name) {
        return dispatcher.newStateMachine(name);
    }

    /**
     * Specifies the expected invocations that the object under test will perform upon
     * objects in its context during the test.
     * 
     * The builder is responsible for interpreting high-level, readable API calls to 
     * construct an expectation.
     */
    public void checking(ExpectationBuilder expectations) {
        expectations.buildExpectations(defaultAction, dispatcher);
        capture = null;
    }

    /**
     * Adds an expected invocation that the object under test will perform upon
     * objects in its context during the test.
     * 
     * This method allows a test to define an expectation explicitly, bypassing the
     * high-level API, if desired.
     */
    public void addExpectation(Expectation expectation) {
        dispatcher.add(expectation);
    }

    /**
     * Fails the test if there are any expectations that have not been met.
     */
    public void assertIsSatisfied() {
        firstError = null;
        if (!dispatcher.isSatisfied()) {
            throw expectationErrorTranslator.translate(new ExpectationError("not all expectations were satisfied", dispatcher));
        }
    }

    private Object dispatch(Invocation invocation) throws Throwable {
        if (isCapturingExpectations()) {
            capture.createExpectationFrom(invocation);
            return defaultAction.invoke(invocation);
        } else if (firstError != null) {
            throw firstError;
        } else {
            try {
                return dispatcher.dispatch(invocation);
            } catch (ExpectationError e) {
                firstError = expectationErrorTranslator.translate(fillInDetails(e));
                firstError.setStackTrace(e.getStackTrace());
                throw firstError;
            }
        }
    }

    private ExpectationError fillInDetails(ExpectationError e) {
        if (e.expectations != null) {
            return e;
        } else {
            ExpectationError filledIn = new ExpectationError(e.getMessage(), dispatcher, e.invocation);
            filledIn.setStackTrace(e.getStackTrace());
            return filledIn;
        }
    }

    private boolean isCapturingExpectations() {
        return capture != null;
    }

    private class MockObject implements Invokable, CaptureControl {

        private String name;

        public MockObject(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public Object invoke(Invocation invocation) throws Throwable {
            return dispatch(invocation);
        }

        public void startCapturingExpectations(ExpectationCapture newCapture) {
            capture = newCapture;
        }

        public void stopCapturingExpectations() {
            capture = null;
        }
    }
}
