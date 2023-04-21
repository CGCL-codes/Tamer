package org.gamegineer.engine.internal.core.extensions.commandhistory;

import static org.junit.Assert.assertNotNull;
import org.gamegineer.engine.core.EngineException;
import org.gamegineer.engine.core.EngineFactory;
import org.gamegineer.engine.core.FakeEngineContext;
import org.gamegineer.engine.core.ICommand;
import org.gamegineer.engine.core.IEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Superclass for fixtures that test commands which interact with the command
 * history extension.
 * 
 * @param <C>
 *        The concrete type of the command implementation under test.
 * @param <T>
 *        The result type of the command.
 */
public abstract class AbstractCommandHistoryCommandTestCase<C extends ICommand<T>, T> {

    /** The command under test in the fixture. */
    private C m_command;

    /** The engine for the fixture. */
    private IEngine m_engine;

    /**
     * Initializes a new instance of the
     * {@code AbstractCommandHistoryCommandTestCase} class.
     */
    protected AbstractCommandHistoryCommandTestCase() {
        super();
    }

    protected abstract C createCommand();

    protected final C getCommand() {
        assertNotNull(m_command);
        return m_command;
    }

    protected final IEngine getEngine() {
        assertNotNull(m_engine);
        return m_engine;
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
        m_command = createCommand();
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
        m_engine = null;
    }

    /**
     * Ensures the {@code execute} method throws an exception if the command
     * history extension is not available.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = EngineException.class)
    public void testExecute_CommandHistory_Unavailable() throws Exception {
        m_command.execute(new FakeEngineContext());
    }
}
