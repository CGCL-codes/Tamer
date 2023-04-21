package org.gamegineer.client.internal.ui.console.commandlets.farm;

import java.util.Arrays;
import java.util.Collections;
import org.gamegineer.client.ui.console.commandlet.AbstractCommandletTestCase;
import org.gamegineer.client.ui.console.commandlet.CommandletException;
import org.gamegineer.client.ui.console.commandlet.ICommandlet;
import org.gamegineer.client.ui.console.commandlet.ICommandletContext;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.client.internal.ui.console.commandlets.farm.StopLocalServerCommandlet}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.client.ui.console.commandlet.ICommandlet} interface.
 */
public final class StopLocalServerCommandletAsCommandletTest extends AbstractCommandletTestCase {

    /**
     * Initializes a new instance of the
     * {@code StopLocalServerCommandletAsCommandletTest} class.
     */
    public StopLocalServerCommandletAsCommandletTest() {
        super();
    }

    @Override
    protected ICommandlet createCommandlet() {
        return new StopLocalServerCommandlet();
    }

    /**
     * Ensures the {@code execute} method throws an exception when passed an
     * argument list with two elements.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = CommandletException.class)
    public void testExecute_ArgCount_Two() throws Exception {
        getCommandlet().execute(createCommandletContext(Arrays.asList("1", "2")));
    }

    /**
     * Ensures the {@code execute} method throws an exception when passed an
     * empty argument list.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = CommandletException.class)
    public void testExecute_ArgCount_Zero() throws Exception {
        getCommandlet().execute(createCommandletContext(Collections.<String>emptyList()));
    }

    /**
     * Ensures the {@code execute} method throws an exception when the server
     * identifier is not registered.
     * 
     * @throws java.lang.Exception
     *         If an error occurs.
     */
    @Test(expected = CommandletException.class)
    public void testExecute_ServerId_Unregistered() throws Exception {
        final ICommandletContext context = createCommandletContext(Arrays.asList("server-id"));
        getCommandlet().execute(context);
    }
}
