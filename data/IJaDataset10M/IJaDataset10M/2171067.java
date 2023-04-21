package org.gamegineer.client.internal.ui.console.commandlets.server;

import org.gamegineer.client.ui.console.commandlet.AbstractCommandletHelpTestCase;
import org.gamegineer.client.ui.console.commandlet.ICommandletHelp;

/**
 * A fixture for testing the
 * {@link org.gamegineer.client.internal.ui.console.commandlets.server.GetGamesCommandlet}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.client.ui.console.commandlet.ICommandletHelp}
 * interface.
 */
public final class GetGamesCommandletAsCommandletHelpTest extends AbstractCommandletHelpTestCase {

    /**
     * Initializes a new instance of the
     * {@code GetGamesCommandletAsCommandletHelpTest} class.
     */
    public GetGamesCommandletAsCommandletHelpTest() {
        super();
    }

    @Override
    protected ICommandletHelp createCommandletHelp() {
        return new GetGamesCommandlet();
    }
}
