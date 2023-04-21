package com.qwantech.diesel.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * Base class for creating applications launched from the command
 * line. To create a standalone application using this base class,
 * start by creating a class that extends AbstractApplication.
 * Implement these methods:
 * <ul>
 * <li> <code>public String getName()</code>
 * <li> <code>protected void addCustomOptions(Options)(Options options)</code>
 * <li> <code>protected void configure(CommandLine commandLine)
 *                           throws ApplicationException</code>
 * <li> <code>protected void execute()()
 *                           throws ApplicationException</code>
 * <li> <code>public static void main(String[] args)</code>
 * <blockquote>
 * This method could be as simple as creating an instance of your
 * AbstractApplication subclass, passing the command line arguments
 * to the <code>configure()</code> method,
 * and calling the <code>run()</code> method.
 * Don't call <code>execute()</code> directly,
 * since <code>run()</code> handles command line
 * parsing and catching exceptions.
 * </blockquote>
 * </ul>
 * <p>
 * Here is an example application that takes one command line option,
 * -m or --message, and that option takes an argument, a text message.
 * The application prints that text message to <code>System.out</code>.
 * <blockquote>
<table bgcolor="#CCCCCC" border="1" cellspacing="0" cellpadding="3"><tr><td><pre>
import com.qwantech.diesel.util.AbstractApplication;
import com.qwantech.diesel.util.ApplicationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ExampleApp extends AbstractApplication {

    private String message;

    public String getName() {
        return "ExampleApp";
    }

    protected void addCustomOptions(Options options) {
        Option opt = new Option("m",
                                "message",
                                true, // <i>option takes an argument</i>
                                "message to display");
        opt.setArgName("text");
        opt.setRequired(true);
        options.addOption(opt);
    }

    protected void configure(CommandLine commandLine)
        throws ApplicationException
    {
        // We're not expecting any command line arguments,
        // except for the argument to the -m option, so let's
        // throw an exception if we see any other arguments.
        if (commandLine.getArgs().length != 0) {
            showHelp(System.err);
            fail("too many arguments");
        }
        this.message = commandLine.getOptionValue("m");
    }

    protected void execute() throws ApplicationException {
        System.out.println(this.message);
    }

    public static void main(String[] args) {
        ExampleApp app = new ExampleApp();
        app.setArgs(args);
        app.run();
    }
}
</pre></td></tr></table>
 * </blockquote>
 */
public abstract class AbstractApplication {

    private String[] args;

    private int exitCode = 0;

    private boolean userWantsHelp = false;

    /**
     * Returns the name of the launch script used to start the program.
     * If no launch script is used, then this could be the class name or
     * application jar file name. The name will be incorporated into
     * the default usage message.
     *
     * @return the application name, to put in the usage message
     */
    public abstract String getName();

    /**
     * This defines the available command line arguments,
     * using the Jakarta CLI Options class. Note that a "help"
     * option is already defined in the AbstractApplication class
     * with a short name of "-h" and a long name of "--help".
     * 
     * @param options Collector object, for app to define options
     */
    protected abstract void addCustomOptions(Options options);

    /**
     * Handles the runtime command line options and arguments.
     * If the command line is not valid, or if there is some other problem
     * in the configuration step, then throw an ApplicationException.
     * If there are different types of things that could go wrong, then
     * consider making subclasses of ApplicationException.
     * 
     * @param commandLine Runtime command line arguments
     * @throws ApplicationException
     */
    protected abstract void configure(CommandLine commandLine) throws ApplicationException;

    /**
     * This is where the application code goes, after the configuration
     * has been done. If the execution fails, there are three choices
     * for exiting:
     * <ol>
     * <li> handle the problem, then return from the method
     * <li> throw <code>ApplicationException</code> or a subclass
     * <li> call one of the <code>fail()</code> methods, which are
     *      shortcuts for throwing an <code>ApplicationException</code>
     * </ol>
     * If you want the process to return a particular exit code to the
     * operating system, use the <code>setExitCode()</code> method before
     * returning or throwing an <code>ApplicationException</code>.
     * 
     * @throws ApplicationException
     */
    protected abstract void execute() throws ApplicationException;

    /**
     * This is the method that <code>main()</code> should call.
     * It calls <code>parseCommandLine()</code> (which calls
     * <code>createCustomOptions()</code>), and then calls
     * <code>configure()</code> and <code>execute()</code>
     * if there are no problems.
     * <p>
     * If one of the help options ("-h", "--help") is specified
     * on the command line at runtime, this method will call
     * <code>showHelp()</code> and return.
     * <p>
     * If there is a problem, then one of these methods will
     * be called:
     * <ul>
     * <li> <code>handleCommandLineException()</code>
     * <li> <code>handleConfigureException()</code>
     * <li> <code>handleExecuteException()</code>
     * <li> <code>handleThrowable()</code>
     * </ul>
     *
     */
    public void run() {
        Log log = LogFactory.getLog(getClass());
        try {
            CommandLine commandLine = null;
            try {
                commandLine = parseCommandLine();
            } catch (ApplicationException e) {
                handleCommandLineException(log, e);
                return;
            }
            if (userWantsHelp()) {
                showHelp(System.out);
                return;
            }
            try {
                configure(commandLine);
            } catch (ApplicationException e) {
                handleConfigureException(log, e);
                return;
            }
            try {
                execute();
            } catch (ApplicationException e) {
                handleExecuteException(log, e);
            }
            return;
        } catch (Throwable t) {
            handleThrowable(log, t);
            return;
        }
    }

    /**
     * Pass the runtime command line arguments.
     * @param args Runtime command line arguments
     */
    public void setArgs(String[] args) {
        this.args = args;
    }

    /**
     * Returns the runtime command line arguments. For example, let's say
     * this was the command line that executed the application:
     * <blockquote>
     * <code>java ExampleApp -m "hello, world"</code>
     * </blockquote>
     * The array of command arguments would be this:
     * <blockquote>
     * <code>{ "-m", "hello, world" }</code>
     * </blockquote>
     * @return the runtime command line arguments
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Sets the exit code to pass to the operating system.
     * @param exitCode zero for success, non-zero for failure
     */
    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * Returns the exit code to pass to the operating system.
     * @return zero for success, non-zero for failure
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Generates an Options objects to collect the specified options
     * available on the command line. The default includes an option
     * for the user to request the command line usage, with either
     * "-h" or "--help". The <code>addCustomOptions()</code> method
     * can add more options to this definition.
     * @return Options collector object from Jakarta CLI framework
     */
    protected Options createOptions() {
        Options options = new Options();
        options.addOption(new Option("h", "help", false, "shows this message"));
        addCustomOptions(options);
        return options;
    }

    /**
     * This converts an array of strings holding the command line arguments
     * into a CommandLine argument that can be used by the Jakarta CLI
     * framework. This requires that setArgs() has been called somewhere,
     * most likely from <code>main()</code>.
     * @param args Command line arguments, not including the application name
     * @return CommandLine object to pass to <code>configure()<code>
     * @throws ApplicationException
     */
    protected CommandLine parseCommandLine() throws ApplicationException {
        CommandLine commandLine = null;
        String[] args = getArgs();
        if (args == null) {
            fail("Programmer error: command line arguments not passed correctly");
        }
        try {
            CommandLineParser parser = new PosixParser();
            commandLine = parser.parse(createOptions(), args, false);
        } catch (MissingOptionException e) {
            fail(e.getMessage() + "option is required");
        } catch (MissingArgumentException e) {
            fail(e.getLocalizedMessage(), e);
        } catch (ParseException e) {
            fail(e.toString(), e);
        } finally {
            checkForHelpWanted(args);
        }
        Options options = createOptions();
        Iterator i = commandLine.iterator();
        while (i.hasNext()) {
            Option o = (Option) i.next();
            if (!options.hasOption(o.getOpt())) {
                throw new ApplicationException("Unrecognized command-line option" + " \"" + o.getOpt() + "\"");
            }
        }
        return commandLine;
    }

    /**
     * This indicates that "-h" or "--help" was on the runtime
     * command line.
     * @return true if -h/--help was selected, false otherwise
     */
    protected boolean userWantsHelp() {
        return userWantsHelp;
    }

    /**
     * Uses the Jakarta CLI framework to convert the defined options into
     * a formatted usage message, and prints to the selected output stream.
     * @param out Output stream: Typically System.out if the user asked
     *                           for help, System.err if help is being shown
     *                           because of a bad command line option, or
     *                           missing arguments
     */
    protected void showHelp(PrintStream out) {
        HelpFormatter formatter = new HelpFormatter();
        Options options = createOptions();
        if (options.getOptions().size() == 0) {
            out.println("Usage: " + getName() + " <options>");
        } else {
            PrintWriter writer = new PrintWriter(out);
            formatter.printUsage(writer, 80, getName(), options);
            formatter.printOptions(writer, 80, options, 2, 2);
            writer.close();
        }
    }

    /**
     * Convenience method for throwing an ApplicationException.
     * @param message Brief text describing problem, which may be
     *                displayed to user
     * @throws ApplicationException
     */
    protected void fail(String message) throws ApplicationException {
        throw new ApplicationException(message);
    }

    /**
     * Convenience method for throwing an ApplicationException.
     * @param message Brief text describing problem, which may be
     *                displayed to user
     * @param cause   Original exception or error
     * @throws ApplicationException
     */
    protected void fail(String message, Throwable cause) throws ApplicationException {
        throw new ApplicationException(message, cause);
    }

    /**
     * Convenience method for throwing an ApplicationException.
     * @param exitCode Exit code to pass to operating system on exit,
     *                 typically non-zero if there is a problem.
     * @param message  Brief text describing problem, which may be
     *                 displayed to user
     * @throws ApplicationException
     */
    protected void fail(int exitCode, String message) throws ApplicationException {
        setExitCode(exitCode);
        throw new ApplicationException(message);
    }

    /**
     * Convenience method for throwing an ApplicationException.
     * @param exitCode Exit code to pass to operating system on exit,
     *                 typically non-zero if there is a problem.
     * @param message  Brief text describing problem, which may be
     *                 displayed to user
     * @param cause    Original exception or error
     * @throws ApplicationException
     */
    protected void fail(int exitCode, String message, Throwable cause) throws ApplicationException {
        setExitCode(exitCode);
        throw new ApplicationException(message, cause);
    }

    /**
     * Scans the runtime command line arguments looking for "-h" or
     * "--help", sets a flag if found.
     * @param args Runtime command line arguments, not including app name
     */
    protected void checkForHelpWanted(String[] args) {
        String shortOpt = "-h";
        String longOpt = "--help";
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals(shortOpt) || args[i].equals(longOpt)) {
                userWantsHelp = true;
                return;
            }
        }
    }

    /**
     * Called when the runtime command line could not be parsed.
     * If the user was asking for help, with "-h" or "--help",
     * then print the help message to System.out and set the
     * exit code to zero, indicating that there was no problem.
     * If the user was not asking for help, then print the help
     * message to System.err instead, add a message indicating
     * why the command line was bad, and set the exit code to 1
     * unless it was already non-zero.
     * <p>
     * Note that this handles problems in command line syntax.
     * If the problem is because one of the arguments was not
     * in the right format, or because some system resource
     * was not available, then that will be shown in the
     * <code>handleConfigureException()</code> method.
     * 
     * @param log Not currently used, but might be useful in subclass
     * @param e   Exception describing what went wrong
     */
    protected void handleCommandLineException(Log log, ApplicationException e) {
        if (userWantsHelp()) {
            showHelp(System.out);
            setExitCode(0);
        } else {
            showHelp(System.err);
            System.err.println(e.getMessage());
            if (getExitCode() == 0) {
                setExitCode(1);
            }
        }
    }

    /**
     * Called when there was a problem in the configuration step
     * (ie, when <code>configure()</code> was called). This could
     * be because a command line argument was not in the right
     * format, or because some system resource was unavailable.
     * <p>
     * If a required argument was missing, or an invalid option
     * was specified, then that will be shown in the
     * <code>handleCommandLineException()</code> method.
     * <p>
     * This forces the exit code to 1, unless it was already non-zero.
     * 
     * @param log Log object from the Jakarta logging framework
     * @param e   Exception describing what went wrong
     */
    protected void handleConfigureException(Log log, ApplicationException e) {
        if (getExitCode() == 0) {
            setExitCode(1);
        }
        log.error(e.getMessage());
        if (log.isDebugEnabled()) {
            log.debug(e.getMessage(), e);
        }
    }

    /**
     * Called when there was a problem in execution after the
     * command line parsing and configuration is done
     * (ie, the problem happened in <code>execute()</code>).
     * Note that this only applies to anticipated problems,
     * where an ApplicationException was thrown. Uncaught
     * system errors and runtime exceptions are handled by
     * <code>handleThrowable()</code>.
     * <p>
     * This forces the exit code to 1, unless it was already non-zero.
     * 
     * @param log Log object from the Jakarta logging framework
     * @param e   Exception describing what went wrong
     */
    protected void handleExecuteException(Log log, ApplicationException e) {
        if (getExitCode() == 0) {
            setExitCode(1);
        }
        log.error(e.getMessage());
        if (log.isDebugEnabled()) {
            log.debug(e.getMessage(), e);
        }
    }

    /**
     * Called when there was an unanticipated problem in execution
     * anywhere within the <code>run()</code> method. This includes
     * command line parsing, configuration, and execution. It does
     * not include ApplicationException, which is handled by other
     * methods.
     * <p>
     * This forces the exit code to 1, unless it was already non-zero.
     * 
     * @param log Log object from the Jakarta logging framework
     * @param t   Error or runtime exception describing what went wrong
     */
    protected void handleThrowable(Log log, Throwable t) {
        if (getExitCode() == 0) {
            setExitCode(1);
        }
        try {
            String m = t.getClass().getName() + ": " + t.getLocalizedMessage();
            log.error(m);
            if (log.isDebugEnabled()) {
                log.debug(m, t);
            }
        } catch (Throwable t2) {
            t.printStackTrace();
        }
    }
}
