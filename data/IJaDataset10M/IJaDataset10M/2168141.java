package org.gaea.common.command;

import java.rmi.server.UID;
import java.util.Vector;
import org.gaea.common.exception.GaeaException;

/**
 * Facade for the CommandQueue exclusively enables the user to execute and get
 * the results from the CommandQueue.
 * 
 * @author bdevost
 */
public class QueueExecutionHandler {

    private CommandQueue _queue;

    private boolean _executed;

    /**
	 * Constructor.
	 * @param queue the queue to manage
	 */
    public QueueExecutionHandler(CommandQueue queue) {
        _queue = queue;
        _executed = false;
    }

    /**
	 * Executes the queue.
	 * @param connector the connector to execute with
	 * @return success ot failure
	 * @throws GaeaException
	 */
    public boolean execute(IConnector connector) throws GaeaException {
        if (_executed) throw new RuntimeException("You cannot execute the same CommandQueue twice.");
        boolean rv = _queue.execute(connector);
        _executed = true;
        return rv;
    }

    /**
	 * @return the queue's execution results
	 */
    public Vector<CommandResult<?>> getResults() {
        if (!_executed) throw new RuntimeException("You are trying to get results for a CommandQueue that was never executed.");
        return _queue.getResults();
    }

    /**
	 * Get the results for a specific command
	 * @param command command of which we want the results
	 * @return the results
	 */
    public CommandResult<?> getResultsFor(Command<?> command) {
        if (!_executed) throw new RuntimeException("You are trying to get results for a CommandQueue that was never executed.");
        return _queue.getResultFor(command);
    }

    /**
	 * Get the results for a specific command
	 * @param uid the UID of the command of which we want the results
	 * @return the results
	 */
    public CommandResult<?> getResultsFor(UID uid) {
        if (!_executed) throw new RuntimeException("You are trying to get results for a CommandQueue that was never executed.");
        return _queue.getResultFor(uid);
    }
}
