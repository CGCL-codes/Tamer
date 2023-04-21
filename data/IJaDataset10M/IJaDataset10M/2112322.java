package org.gcli;

/**
 * CLI provider which create a {@link ConsoleCommandLineInterface} when there
 * arguments to the main methods. Otherwise it create a
 * {@link InteractiveCommandLineInterface}.
 * 
 * @author Guy Nirpaz
 * 
 */
public class CliProvider {

    /**
	 * 
	 * @param args
	 *            - when null returns instance of
	 *            {@link InteractiveCommandLineInterface} other wise returns
	 *            instance of {@link ConsoleCommandLineInterface}
	 */
    public CommandLineInterface createCli(String[] args) {
        if ((args == null) || (args.length == 0)) {
            return new InteractiveCommandLineInterface(args);
        } else {
            return new ConsoleCommandLineInterface(args);
        }
    }
}
