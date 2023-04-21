package org.gamegineer.client.internal.ui.console.commandlets.connection;

import org.gamegineer.client.ui.console.commandlet.AbstractCommandletHelpTestCase;
import org.gamegineer.client.ui.console.commandlet.ICommandletHelp;

/**
 * A fixture for testing the
 * {@link org.gamegineer.client.internal.ui.console.commandlets.connection.GetServerCommandlet}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.client.ui.console.commandlet.ICommandletHelp}
 * interface.
 */
public final class GetServerCommandletAsCommandletHelpTest extends AbstractCommandletHelpTestCase {

    /**
     * Initializes a new instance of the
     * {@code GetServerCommandletAsCommandletHelpTest} class.
     */
    public GetServerCommandletAsCommandletHelpTest() {
        super();
    }

    @Override
    protected ICommandletHelp createCommandletHelp() {
        return new GetServerCommandlet();
    }
}
