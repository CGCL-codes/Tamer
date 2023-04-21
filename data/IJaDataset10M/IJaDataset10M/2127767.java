package qat.plugins.chorus;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStreamReader;

/**
 * This class is responsible for executing a single TestObject, and starting new threads to read it's standard output
 * and error output streams.
 * It allows for killing of the started TestObject at anytime by calling the cancel() method.
 */
public class ExecProcess extends Thread {

    public static final int TIMEDOUT_STATE = -9998;

    private static final int ILLEGAL_THREAD_STATE = -5;

    private static final int NULL_POINTER = -6;

    private static final int OTHER_EXCEPTION = -7;

    private Process process;

    private String command;

    private Console console;

    private String lineSep;

    public ExecProcess(String command, Console console) {
        this.command = command;
        this.console = console;
        lineSep = System.getProperty("line.separator");
    }

    /**
	 * This form will read the output of an executed process as a thread, and
	 * execution returns immediately.
	 */
    public void run() {
        if (console != null) console.setEditable(false);
        try {
            process = Runtime.getRuntime().exec(command, getEnvironment());
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String stdOutLine, stdErrLine;
            if (console != null) {
                console.append(lineSep);
                do {
                    if (stdOut.ready()) {
                        stdOutLine = stdOut.readLine();
                        console.append(stdOutLine + lineSep);
                        console.scrollToEnd();
                    } else {
                        if (stdErr.ready()) {
                            stdErrLine = stdErr.readLine();
                            console.append(stdErrLine + lineSep);
                            console.scrollToEnd();
                        } else {
                            Thread.sleep(75);
                        }
                    }
                    Thread.yield();
                } while ((checkExitValue() < 0) || (stdOut.ready()) || (stdErr.ready()));
                console.append(">>" + lineSep + "<<");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (console != null) console.append("Exception starting process :" + ex.getMessage() + lineSep);
        } finally {
            console.append(lineSep);
            if (console != null) {
                console.setEditable(true);
                console.setPrompt(console.getPrompt());
            }
            interrupt();
        }
    }

    private String readStream(DataInput s) {
        String line, result = "";
        try {
            while ((line = s.readLine()) != null) {
                result += (line + System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            result += e.toString();
        }
        return result;
    }

    public void setCommand(String c) {
        command = c;
    }

    /**
	 * This method will execute the process, and return once it is finished.
	 * It waits for all the output to be completed before returning.
	 */
    public String getProcessOutput() throws Exception {
        process = Runtime.getRuntime().exec(command, getEnvironment());
        String result = "";
        DataInput stderr = new DataInputStream(process.getErrorStream());
        result += readStream(stderr);
        DataInput stdout = new DataInputStream(process.getInputStream());
        result += readStream(stdout);
        return result;
    }

    private String[] getEnvironment() {
        String environment[] = { "PATH=/usr/bin", "LD_LIBRARY_PATH=/usr/lib" };
        return environment;
    }

    /**
	 * This function returns the exit value of the running process if it has finished 
	 * else it returns a negative value.
	 * This method will not block if the process is still running.
	 * @return the absolute value of the exit value of the process, or a negative int to indicate other
	 * thread states (ILLEGAL_THREAD_STATE, NULL_POINTER, OTHER_EXCEPTION)
	 */
    public int checkExitValue() {
        try {
            return Math.abs(process.exitValue());
        } catch (IllegalThreadStateException e) {
            return ILLEGAL_THREAD_STATE;
        } catch (NullPointerException e) {
            return NULL_POINTER;
        } catch (Exception e) {
            return OTHER_EXCEPTION;
        }
    }

    /** 
	 * This will kill the TestObject execution and free all the resource associated with this
	 * TestObject execution.
	 */
    public void interrupt() {
        if (process != null) {
            process.destroy();
        }
    }
}
