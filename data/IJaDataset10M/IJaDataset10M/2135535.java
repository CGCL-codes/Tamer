package org.gamegineer.engine.internal.core.extensions.stateeventmediator;

import org.gamegineer.engine.core.AbstractInvertibleCommandTestCase;
import org.gamegineer.engine.core.extensions.stateeventmediator.MockStateListener;

/**
 * A fixture for testing the
 * {@link org.gamegineer.engine.internal.core.extensions.stateeventmediator.AddStateListenerCommand}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.engine.core.IInvertibleCommand} interface.
 */
public final class AddStateListenerCommandAsInvertibleCommandTest extends AbstractInvertibleCommandTestCase<AddStateListenerCommand, Void> {

    /**
     * Initializes a new instance of the
     * {@code AddStateListenerCommandAsInvertibleCommandTest} class.
     */
    public AddStateListenerCommandAsInvertibleCommandTest() {
        super();
    }

    @Override
    protected AddStateListenerCommand createCommand() {
        return new AddStateListenerCommand(new MockStateListener());
    }
}
