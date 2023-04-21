package ro.codemart.installer.core.operation;

import org.apache.log4j.Logger;
import ro.codemart.installer.core.InstallerException;
import ro.codemart.installer.core.utils.ProcessThread;
import java.io.IOException;
import java.io.File;

/**
 * Operation used to execute arbitrary commands
 */
public class CommandOperation extends AbstractLongOperation {

    private static final Logger logger = Logger.getLogger(CommandOperation.class);

    /**
     * The command to be executed
     */
    protected String command;

    /**
     * Flag used to decide what to do when an execution error occurs
     */
    protected boolean failOnError;

    /**
     * The command's output message, after it's successful execution
     */
    protected String outputMessage;

    /**
     * The command's error message, after failed execution
     */
    protected String errorMessage;

    /**
     * The folder in which the command to be executed
     */
    protected File workingDir;

    /**
     * The command interceptor
     */
    protected Interceptor interceptor;

    public CommandOperation(String command, boolean failOnError) {
        this.command = command;
        this.failOnError = failOnError;
    }

    public CommandOperation(String command, File workingDir, boolean failOnError) {
        this.command = command;
        this.failOnError = failOnError;
        this.workingDir = workingDir;
    }

    public void execute() throws InstallerException {
        setDescription("Executing " + command);
        runCommand(command, workingDir, failOnError);
        setCompletedPercentage(1);
    }

    /**
     * Set the interceptor for this command
     *
     * @param interceptor the interceptor implementation
     */
    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    /**
     * Executes the given command. If errors are encountered during command execution and failOnError is true, an exception
     * will be thrown, otherwise it is ignored
     *
     * @param command     the command to be executed
     * @param workingDir the working director of the command
     * @param failOnError if true and the execution fails, the operation is aborted @throws InstallerException if any error
     */
    protected void runCommand(String command, File workingDir, boolean failOnError) throws InstallerException {
        try {
            logger.debug("Executing " + command);
            Process p = Runtime.getRuntime().exec(command, null, workingDir);
            ProcessThread input = new ProcessThread(p.getInputStream());
            input.start();
            ProcessThread error = new ProcessThread(p.getErrorStream());
            error.start();
            if (interceptor != null) {
                interceptor.intercept(p, input, error);
            }
            int exitvalue = p.waitFor();
            outputMessage = input.getOutput();
            errorMessage = error.getOutput();
            if (exitvalue == 0) {
                logger.info("Command " + command + " was successfully executed!");
            } else if (failOnError) {
                throw new InstallerException("Could not execute " + command + ". Error code :" + exitvalue);
            } else {
                logger.warn("Could not execute " + command + ". Error code :" + exitvalue);
            }
        } catch (IOException e) {
            if (failOnError) {
                throw new InstallerException("IO error while executing '" + command + "'. Reason: " + e.getMessage(), e);
            } else {
                logger.warn("Command not executed properly", e);
            }
        } catch (InterruptedException e) {
            if (failOnError) {
                throw new InstallerException("Error while executing '" + command + "'. Reason: " + e.getMessage(), e);
            } else {
                logger.warn("Command not executed properly", e);
            }
        }
    }

    /**
     * Return the error message when the command failed
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Return the output message when the command succeded
     *
     * @return the output message
     */
    public String getOutputMessage() {
        return outputMessage;
    }

    /**
     * Executes some code prior to invocation of some process "waitFor()" method, as that method can block
     */
    public interface Interceptor {

        /**
         * Allows for custom code to be executed prior to "waitFor()" method on the given process
         *
         * @param p the process for which  this method should be invoked, before calling "waitFor()'
         * @param processOutput the thread that consumes proces's output stream
         * @param processError  the thread that consumes proces's error stream
         */
        void intercept(Process p, ProcessThread processOutput, ProcessThread processError);
    }
}
