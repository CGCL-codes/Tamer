package org.dev2live.commands;

import org.dev2live.arguments.Arguments;

/**
 * @author bertram
 * Interface for the Command implementions
 */
public interface Command {

    /**
	 * Ececute the Command with the Arguments
	 * @param args Argument for Execution
	 * @return the Result of execution or null
	 */
    public abstract Object execute(Arguments args);

    /**
	 * Get all Arguments which can be used in the Command
	 * @return the usable Arguments
	 */
    public abstract Arguments getUsableArguments();
}
