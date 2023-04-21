package net.sf.lfscontrol.core.exception;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.sf.lfscontrol.core.model.Server;

/**
 * Exception thrown when connection to InSim {@link Server} cannot be
 * initialized.
 *
 * @author Jiří Sotona
 */
public class ServersIOException extends Exception {

    /** List of unknown servers. */
    private final List<Server> fServers;

    private final Map<Server, IOException> fReasons;

    /**
	 * Constructor.
	 *
	 * @param servers
	 *            list of servers to which the connection initialization failed
	 * @param reasons
	 *            reason why server is unknown; for each server
	 */
    public ServersIOException(final List<Server> servers, final Map<Server, IOException> reasons) {
        fServers = servers;
        fReasons = reasons;
    }

    /**
	 * Constructor.
	 *
	 * @param servers
	 *            list of servers to which the connection initialization failed
	 * @param reasons
	 *            for each server reasons why connection initialization failed
	 * @param message
	 */
    public ServersIOException(final List<Server> servers, final Map<Server, IOException> reasons, final String message) {
        super(message);
        fServers = servers;
        fReasons = reasons;
    }

    /**
	 * @return list of servers to which the connection initialization failed
	 */
    public List<Server> getServers() {
        return fServers;
    }

    /**
	 * @return for each server reasons why connection initialization failed
	 */
    public Map<Server, IOException> getReasons() {
        return fReasons;
    }
}
