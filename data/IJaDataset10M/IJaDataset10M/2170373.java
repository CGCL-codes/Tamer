package org.gamegineer.engine.internal.core.extensions.stateeventmediator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.gamegineer.engine.core.EngineException;
import org.gamegineer.engine.core.EngineFactory;
import org.gamegineer.engine.core.FakeEngineContext;
import org.gamegineer.engine.core.ICommand;
import org.gamegineer.engine.core.IEngine;
import org.gamegineer.engine.core.extensions.stateeventmediator.MockStateListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Superclass for fixtures that test commands which interact with the state
 * event mediator.
 * 
 * @param <C>
 *        The concrete type of the command implementation under test.
 * @param <T>
 *        The result type of the command.
 */
public abstract class AbstractStateEventMediatorCommandTestCase<C extends ICommand<T>, T> {

    /** The command under test in the fixture. */
    private C m_command;

    /** The engine for the fixture. */
    private IEngine m_engine;

    /** The state listener for the fixture. */
    private MockStateListener m_listener;

    /**
     * Initializes a new instance of the
     * {@code AbstractStateEventMediatorCommandTestCase} class.
     */
    protected AbstractStateEventMediatorCommandTestCase() {
        super();
    }

    protected abstract C createCommand(MockStateListener listener);

    protected final C getCommand() {
        assertNotNull(m_command);
        return m_command;
    }

    protected final IEngine getEngine() {
        assertNotNull(m_engine);
        return m_engine;
    }

    protected final MockStateListener getStateListener() {
        assertNotNull(m_listener);
        return m_listener;
    }

    /**
     * Sets up the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Before
    public void setUp() throws Exception {
        m_engine = EngineFactory.createEngine();
        m_listener = new MockStateListener();
        m_command = createCommand(m_listener);
        assertNotNull(m_command);
    }

    /**
     * Tears down the test fixture.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @After
    public void tearDown() throws Exception {
        m_command = null;
        m_listener = null;
        m_engine = null;
    }

    /**
     * Ensures the {@code execute} method throws an exception if the state event
     * mediator extension is not available.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = EngineException.class)
    public void testExecute_StateEventMediator_Unavailable() throws Exception {
        m_command.execute(new FakeEngineContext());
    }

    /**
     * Ensures the {@code execute} method always returns {@code null}.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test
    public void testExecute_ReturnValue_Null() throws Exception {
        assertNull(m_engine.executeCommand(m_command));
    }
}
