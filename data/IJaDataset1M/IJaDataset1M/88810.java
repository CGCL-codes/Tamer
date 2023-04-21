package org.naturalcli;

import java.util.Set;

/**
 * A set of commands understood by the CLI
 * 
 * @author Ferran Busquets
 */
public class NaturalCLI {

    private Set<Command> commands;

    private ParameterValidator pv;

    /**
     * Creates a new instance.
     * 
     * @param commands the set of commands that can be executed.
     * @param pv the parameter validator.
     */
    public NaturalCLI(Set<Command> commands, ParameterValidator pv) {
        this.commands = commands;
        this.pv = pv;
    }

    /** 
     * Creates a new instance with the default parameter validator.
     * 
     * @param commands the set of commands that can be executed.
     */
    public NaturalCLI(Set<Command> commands) {
        this(commands, new ParameterValidator());
    }

    /**
     * Runs a command based on the arguments.
     * 
     * @param args  the arguments to be parsed
     * @param first the index on <code>args</code> of the first string for the arguments.
     * @throws ExecutionException 
     */
    public void execute(String[] args, int first) throws ExecutionException {
        if (args == null) throw new IllegalArgumentException("The parameter for the arguments cannot be null.");
        if (args.length == 0) return;
        ParseResult parseResult = null;
        Command command = null;
        for (Command c : commands) try {
            parseResult = c.getSyntax().parse(args, first, pv);
            if (parseResult != null) {
                command = c;
                break;
            }
        } catch (UnknownParameterType e) {
            throw new ExecutionException("Unknown parameter type when matching command.", e);
        }
        if (command == null) throw new ExecutionException("No command matches.");
        command.getExecutor().execute(parseResult);
    }

    /**
     * Runs a command based on the arguments.
     * 
     * @param args  the string arguments to run.
     * @param first the index on <code>args</code> of the first string for the arguments.
     * @throws ExecutionException 
     * @throws UnknownParameterType 
     */
    public void execute(String args, int first) throws ExecutionException {
        if (args == null) throw new IllegalArgumentException("The parameter argument string cannot be null.");
        execute(args.split(" "), first);
    }

    /**
     * Runs a command based on the arguments.
     * 
     * @param args  the arguments to be parsed
     * @throws ExecutionException 
     */
    public void execute(String[] args) throws ExecutionException {
        execute(args, 0);
    }

    /**
     * Runs a command based on the arguments in the string.
     * 
     * @param args  the string with the arguments to be parsed
     * @throws ExecutionException 
     */
    public void execute(String args) throws ExecutionException {
        execute(args, 0);
    }
}
