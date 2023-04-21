package org.gamegineer.engine.internal.core.extensions.commandhistory;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import org.gamegineer.engine.core.AbstractInvertibleCommandTestCase;
import org.gamegineer.engine.core.AttributeName;
import org.gamegineer.engine.core.IEngine;
import org.gamegineer.engine.core.IInvertibleCommand;
import org.gamegineer.engine.core.MockCommands;
import org.gamegineer.engine.core.IState.Scope;

/**
 * A fixture for testing the
 * {@link org.gamegineer.engine.internal.core.extensions.commandhistory.UndoCommand}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.engine.core.IInvertibleCommand} interface.
 */
public final class UndoCommandAsInvertibleCommandTest extends AbstractInvertibleCommandTestCase<UndoCommand, Void> {

    /**
     * Initializes a new instance of the
     * {@code UndoCommandAsInvertibleCommandTest} class.
     */
    public UndoCommandAsInvertibleCommandTest() {
        super();
    }

    @Override
    protected UndoCommand createCommand() {
        return new UndoCommand();
    }

    @Override
    protected void prepareEngineForInverseCommandTest(final IEngine engine, final IInvertibleCommand<Void> command) throws Exception {
        assertArgumentNotNull(engine, "engine");
        assertArgumentNotNull(command, "command");
        engine.executeCommand(MockCommands.createAddAttributeCommand(new AttributeName(Scope.APPLICATION, "UndoCommandAsInvertibleCommandTest.attribute"), "value"));
    }
}
