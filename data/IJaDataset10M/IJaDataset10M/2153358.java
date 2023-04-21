package org.gamegineer.client.internal.ui.console.commandlets.farm;

import org.gamegineer.client.ui.console.commandlet.AbstractCommandletHelpTestCase;
import org.gamegineer.client.ui.console.commandlet.ICommandletHelp;

/**
 * A fixture for testing the
 * {@link org.gamegineer.client.internal.ui.console.commandlets.farm.GetLocalServersCommandlet}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.client.ui.console.commandlet.ICommandletHelp}
 * interface.
 */
public final class GetLocalServersCommandletAsCommandletHelpTest extends AbstractCommandletHelpTestCase {

    /**
     * Initializes a new instance of the
     * {@code GetLocalServersCommandletAsCommandletHelpTest} class.
     */
    public GetLocalServersCommandletAsCommandletHelpTest() {
        super();
    }

    @Override
    protected ICommandletHelp createCommandletHelp() {
        return new GetLocalServersCommandlet();
    }
}
