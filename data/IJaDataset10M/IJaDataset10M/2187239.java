package com.asl.utils.exec;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.asl.library.executor.services.ExecutionResult;
import com.asl.library.executor.services.Executor;

public class RuntimeExecutor implements Executor {

    /** the key to use when specifying a command for any other OS: <b>*</b> */
    public static final String KEY_OS_DEFAULT = "*";

    private static final String KEY_OS_NAME = "os.name";

    private static final int BUFFER_SIZE = 1024;

    private static final String VAR_OPEN = "${";

    private static final String VAR_CLOSE = "}";

    private static Log logger = LogFactory.getLog(RuntimeExecutor.class);

    private String command;

    private boolean waitForCompletion;

    private Map<String, String> defaultProperties;

    private Set<Integer> errCodes;

    /**
	 * Default constructor. Initialize this instance by setting individual
	 * properties.
	 */
    public RuntimeExecutor() {
        this.waitForCompletion = true;
        defaultProperties = Collections.emptyMap();
        this.errCodes = new HashSet<Integer>(2);
        errCodes.add(1);
        errCodes.add(2);
    }

    /**
	 * Set the command to execute regardless of operating system
	 * 
	 * @param command
	 *            the command string
	 */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
	 * Set whether to wait for completion of the command or not. If there is no
	 * wait for completion, then the return value of <i>out</i> and <i>err</i>
	 * buffers cannot be relied upon as the command may still be in progress.
	 * Failure is therefore not possible unless the calling thread waits for
	 * execution.
	 * 
	 * @param waitForCompletion
	 *            <tt>true</tt> (default) is to wait for the command to exit, or
	 *            <tt>false</tt> to just return an exit code of 0 and whatever
	 *            output is available at that point.
	 * 
	 * @since 2.1
	 */
    public void setWaitForCompletion(boolean waitForCompletion) {
        this.waitForCompletion = waitForCompletion;
    }

    /**
	 * Supply a choice of commands to execute based on a mapping from the
	 * <i>os.name</i> system property to the command to execute. The
	 * {@link #KEY_OS_DEFAULT *} key can be used to get a command where there is
	 * not direct match to the operating system key.
	 * 
	 * @param commandsByOS
	 *            a map of command string keyed by operating system names
	 */
    public void setCommandMap(Map<String, String> commandsByOS) {
        String serverOs = System.getProperty(KEY_OS_NAME);
        String command = commandsByOS.get(serverOs);
        if (command == null) {
            for (String osName : commandsByOS.keySet()) {
                if (osName.equals(KEY_OS_DEFAULT)) {
                    continue;
                }
                if (serverOs.matches(osName)) {
                    command = commandsByOS.get(osName);
                    break;
                }
            }
            if (command == null) {
                command = commandsByOS.get(KEY_OS_DEFAULT);
            }
        }
        if (command == null) {
            throw new RuntimeException("No command found for OS " + serverOs + " or '" + KEY_OS_DEFAULT + "': \n" + "   commands: " + commandsByOS);
        }
        this.command = command;
    }

    /**
	 * Set the default properties to use when executing the command. The
	 * properties supplied during execution will overwrite the default
	 * properties.
	 * <p>
	 * <code>null</code> properties will be treated as an empty string for
	 * substitution purposes.
	 * 
	 * @param defaultProperties
	 *            property values
	 */
    public void setDefaultProperties(Map<String, String> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    /**
	 * A comma or space separated list of values that, if returned by the
	 * executed command, indicate an error value. This defaults to
	 * <b>"1, 2"</b>.
	 * 
	 * @param errCodesStr
	 *            the error codes for the execution
	 */
    public void setErrorCodes(String errCodesStr) {
        errCodes.clear();
        StringTokenizer tokenizer = new StringTokenizer(errCodesStr, " ,");
        while (tokenizer.hasMoreElements()) {
            String errCodeStr = tokenizer.nextToken();
            try {
                int errCode = Integer.parseInt(errCodeStr);
                this.errCodes.add(errCode);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Property 'errorCodes' must be comma-separated list of integers: " + errCodesStr);
            }
        }
    }

    /**
	 * Executes the command using the default properties
	 * 
	 * @see #execute(Map)
	 */
    public ExecutionResult execute() {
        return execute(defaultProperties);
    }

    /**
	 * Executes the statement that this instance was constructed with.
	 * <p>
	 * <code>null</code> properties will be treated as an empty string for
	 * substitution purposes.
	 * 
	 * @return Returns the full execution results
	 */
    public ExecutionResult execute(Map<String, String> params) {
        int defaultFailureExitValue = errCodes.size() > 0 ? ((Integer) errCodes.toArray()[0]) : 1;
        if (command == null) {
            throw new RuntimeException("Runtime command has not been set: \n" + this);
        }
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        String commandToExecute = null;
        try {
            commandToExecute = getCommand(params);
            process = runtime.exec(commandToExecute);
        } catch (IOException e) {
            String execOut = "";
            String execErr = e.getMessage();
            int exitValue = defaultFailureExitValue;
            ExecutionResult result = new RuntimeExecutionResult(commandToExecute, errCodes, exitValue, execOut, execErr);
            if (logger.isDebugEnabled()) {
                logger.debug(result);
            }
            return result;
        }
        InputStreamReaderThread stdOutGobbler = new InputStreamReaderThread(process.getInputStream());
        InputStreamReaderThread stdErrGobbler = new InputStreamReaderThread(process.getErrorStream());
        stdOutGobbler.start();
        stdErrGobbler.start();
        int exitValue = 0;
        try {
            if (waitForCompletion) {
                exitValue = process.waitFor();
            }
        } catch (InterruptedException e) {
            stdErrGobbler.addToBuffer(e.toString());
            exitValue = defaultFailureExitValue;
        }
        if (waitForCompletion) {
            stdOutGobbler.waitForCompletion();
            stdErrGobbler.waitForCompletion();
        }
        String execOut = stdOutGobbler.getBuffer();
        String execErr = stdErrGobbler.getBuffer();
        ExecutionResult result = new RuntimeExecutionResult(commandToExecute, errCodes, exitValue, execOut, execErr);
        if (logger.isDebugEnabled()) {
            logger.debug(result);
        }
        return result;
    }

    /**
	 * @return Returns the command that will be executed if no additional
	 *         properties were to be supplied
	 */
    public String getCommand() {
        return getCommand(defaultProperties);
    }

    /**
	 * Get the command that will be executed post substitution.
	 * <p>
	 * <code>null</code> properties will be treated as an empty string for
	 * substitution purposes.
	 * 
	 * @param properties
	 *            the properties that might be executed with
	 * @return Returns the command that will be executed should the additional
	 *         properties be supplied
	 */
    public String getCommand(Map<String, String> properties) {
        Map<String, String> execProperties = null;
        if (properties == defaultProperties) {
            execProperties = defaultProperties;
        } else {
            execProperties = new HashMap<String, String>(defaultProperties);
            execProperties.putAll(properties);
        }
        StringBuilder sb = new StringBuilder(command);
        for (Map.Entry<String, String> entry : execProperties.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null) {
                value = "";
            }
            key = (VAR_OPEN + key + VAR_CLOSE);
            int index = sb.indexOf(key);
            while (index > -1) {
                sb.replace(index, index + key.length(), value);
                index = sb.indexOf(key, index + 1);
            }
        }
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(256);
        sb.append("RuntimeExec:\n").append("   command:    ").append(command).append("\n").append("   os:         ").append(System.getProperty(KEY_OS_NAME)).append("\n");
        return sb.toString();
    }

    /**
	 * Object to carry the results of an execution to the caller.
	 * 
	 */
    public static class RuntimeExecutionResult implements ExecutionResult {

        private final String command;

        private final Set<Integer> errCodes;

        private final int exitValue;

        private final String stdOut;

        private final String stdErr;

        private RuntimeExecutionResult(final String command, final Set<Integer> errCodes, final int exitValue, final String stdOut, final String stdErr) {
            this.command = command;
            this.errCodes = errCodes;
            this.exitValue = exitValue;
            this.stdOut = stdOut;
            this.stdErr = stdErr;
        }

        @Override
        public String toString() {
            String out = stdOut.length() > 250 ? stdOut.substring(0, 250) : stdOut;
            String err = stdErr.length() > 250 ? stdErr.substring(0, 250) : stdErr;
            StringBuilder sb = new StringBuilder(128);
            sb.append("Execution result: \n").append("   os:         ").append(System.getProperty(KEY_OS_NAME)).append("\n").append("   command:    ").append(command).append("\n").append("   succeeded:  ").append(getSuccess()).append("\n").append("   exit code:  ").append(exitValue).append("\n").append("   out:        ").append(out).append("\n").append("   err:        ").append(err);
            return sb.toString();
        }

        /**
		 * @param exitValue
		 *            the command exit value
		 * @return Returns true if the code is a listed failure code
		 * 
		 * @see #setErrorCodes(String)
		 */
        private boolean isFailureCode(int exitValue) {
            return errCodes.contains((Integer) exitValue);
        }

        /**
		 * @return Returns true if the command was deemed to be successful
		 *         according to the failure codes returned by the execution.
		 */
        public boolean getSuccess() {
            return !isFailureCode(exitValue);
        }

        public int getExitValue() {
            return exitValue;
        }

        public String getStdOut() {
            return stdOut;
        }

        public String getStdErr() {
            return stdErr;
        }
    }

    /**
	 * Gobbles an <code>InputStream</code> and writes it into a
	 * <code>StringBuffer</code>
	 * <p>
	 * The reading of the input stream is buffered.
	 */
    public static class InputStreamReaderThread extends Thread {

        private InputStream is;

        private StringBuffer buffer;

        private boolean isRunning;

        private boolean completed;

        /**
		 * @param is
		 *            an input stream to read - it will be wrapped in a buffer
		 *            for reading
		 */
        public InputStreamReaderThread(InputStream is) {
            super();
            setDaemon(true);
            this.is = is;
            this.buffer = new StringBuffer(BUFFER_SIZE);
            this.isRunning = false;
            this.completed = false;
        }

        public synchronized void run() {
            isRunning = true;
            completed = false;
            byte[] bytes = new byte[BUFFER_SIZE];
            InputStream tempIs = null;
            try {
                tempIs = new BufferedInputStream(is, BUFFER_SIZE);
                int count = -2;
                while (count != -1) {
                    if (count > 0) {
                        String toWrite = new String(bytes, 0, count);
                        buffer.append(toWrite);
                    }
                    count = tempIs.read(bytes);
                }
                isRunning = false;
                completed = true;
            } catch (IOException e) {
                throw new RuntimeException("Unable to read stream", e);
            } finally {
                if (tempIs != null) {
                    try {
                        tempIs.close();
                    } catch (Exception e) {
                    }
                }
            }
        }

        /**
		 * Waits for the run to complete.
		 * <p>
		 * <b>Remember to <code>start</code> the thread first
		 */
        public synchronized void waitForCompletion() {
            while (!completed && !isRunning) {
                try {
                    this.wait(200L);
                } catch (InterruptedException e) {
                }
            }
        }

        /**
		 * @param msg
		 *            the message to add to the buffer
		 */
        public void addToBuffer(String msg) {
            buffer.append(msg);
        }

        public boolean isComplete() {
            return completed;
        }

        /**
		 * @return Returns the current state of the buffer
		 */
        public String getBuffer() {
            return buffer.toString();
        }
    }
}
