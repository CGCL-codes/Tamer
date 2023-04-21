package org.gamegineer.client.internal.ui.console.commandlet;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.jcip.annotations.Immutable;
import org.gamegineer.client.core.IGameClient;
import org.gamegineer.client.ui.console.IConsole;
import org.gamegineer.client.ui.console.commandlet.CommandletException;
import org.gamegineer.client.ui.console.commandlet.ICommandlet;
import org.gamegineer.client.ui.console.commandlet.IStatelet;

/**
 * A commandlet executor.
 * 
 * <p>
 * This class is immutable.
 * </p>
 */
@Immutable
public final class CommandletExecutor {

    /** The commandlet to execute. */
    private final ICommandlet m_commandlet;

    /** The commandlet argument list. */
    private final List<String> m_commandletArgs;

    /**
     * Initializes a new instance of the {@code CommandletExecutor} class.
     * 
     * @param commandlet
     *        The commandlet to execute; must not be {@code null}.
     * @param commandletArgs
     *        The commandlet argument list; must not be {@code null}.
     */
    CommandletExecutor(final ICommandlet commandlet, final List<String> commandletArgs) {
        assert commandlet != null;
        assert commandletArgs != null;
        m_commandlet = commandlet;
        m_commandletArgs = Collections.unmodifiableList(new ArrayList<String>(commandletArgs));
    }

    /**
     * Executes the commandlet associated with this executor.
     * 
     * @param console
     *        The console; must not be {@code null}.
     * @param statelet
     *        The console statelet; must not be {@code null}.
     * @param gameClient
     *        The game client; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code console}, {@code statelet}, or {@code gameClient} is
     *         {@code null}.
     * @throws org.gamegineer.client.ui.console.commandlet.CommandletException
     *         If an error occurs during the execution of the commandlet.
     */
    public void execute(final IConsole console, final IStatelet statelet, final IGameClient gameClient) throws CommandletException {
        assertArgumentNotNull(console, "console");
        assertArgumentNotNull(statelet, "statelet");
        assertArgumentNotNull(gameClient, "gameClient");
        m_commandlet.execute(new CommandletContext(this, console, statelet, gameClient));
    }

    ICommandlet getCommandlet() {
        return m_commandlet;
    }

    List<String> getCommandletArguments() {
        return m_commandletArgs;
    }
}
