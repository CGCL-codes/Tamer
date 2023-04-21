package gui;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * The main class representing the application. Handles creation of the window
 * and low-level error handling.
 * 
 * @author tom
 * 
 */
public class Application {

    /**
	 * The path to the log file. Note that this file is automatically
	 * overwritten when the application starts.
	 */
    private static final String LOGPATH = "./randomness2.log";

    /**
	 * The main window associated with the application.
	 */
    Window window;

    /**
	 * The writer for the log file. Be sure to flush when done.
	 */
    PrintWriter logWriter;

    /**
	 * 
	 */
    public Application() {
        try {
            logWriter = new PrintWriter(LOGPATH);
        } catch (IOException e) {
            logError("Could not open log file in path '" + LOGPATH + "':\n" + e.getMessage());
        }
        window = new Window(this);
    }

    /**
	 * Start the application. This is blocking and there is no way to return
	 * from this.
	 */
    public void run() {
        window.run();
    }

    /**
	 * Stop the application. Will exit the main process.
	 */
    public void stop() {
        logWriter.close();
        System.exit(0);
    }

    /**
	 * Write an error to stderr and the application log.
	 * 
	 * @param message
	 *            The message to log.
	 */
    public void logError(String message) {
        System.err.println("ERROR: " + message);
        System.err.println();
        if (logWriter != null) {
            logWriter.println(message);
            logWriter.println();
            logWriter.flush();
        }
    }

    /**
	 * Handle a fatal error. The error is written to stderr and the system log,
	 * and the application is shut down with error code -1.
	 * 
	 * @param message
	 *            The message to write to log.
	 */
    public void fatalError(String message) {
        System.err.println("FATAL: " + message);
        System.err.println();
        if (logWriter != null) {
            logWriter.println("FATAL ERROR");
            logWriter.println(message);
            logWriter.println();
            logWriter.flush();
        }
        logWriter.close();
        System.exit(-1);
    }

    /**
	 * Handle a fatal exception. The stack trace is written to stderr and the
	 * system log, and the application is shut down with error code -1.
	 * 
	 * @param e
	 *            The exception describing the failure.
	 */
    public void fatalException(Exception e) {
        System.err.println("FATAL: " + e.getMessage());
        System.err.println();
        if (logWriter != null) {
            logWriter.println("FATAL ERROR");
            logWriter.println(e.getMessage());
            logWriter.println();
            logWriter.flush();
        }
        logWriter.close();
        logStackTrace(e);
        System.exit(-1);
    }

    /**
	 * Write a stack trace to stderr and to the log file.
	 * 
	 * @param ex
	 *            The exception from which to retrieve the stack trace.
	 */
    public void logStackTrace(Exception ex) {
        ex.printStackTrace(System.err);
        System.err.println();
        if (logWriter != null) {
            ex.printStackTrace(logWriter);
            logWriter.println();
            logWriter.flush();
        }
    }

    /**
	 * The main entry point for the application.
	 * 
	 * @param args
	 *            The command-line parameters
	 */
    public static void main(String[] args) {
        new Application().run();
    }
}
