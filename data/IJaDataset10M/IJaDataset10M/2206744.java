package cu.edu.cujae.biowh.logger;

import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * This Class is to handled the system message globally 
 * @author rvera
 * @version 1.0
 * @since Jun 17, 2011
 */
public class VerbLogger {

    /**
     * This is the info message information level 
     */
    public static final int INFO = 1;

    /**
     * This is the debug message information level 
     */
    public static final int DEBUG = 2;

    /**
     * This is the error message information level 
     */
    public static final int ERROR = 3;

    private static int level = 0;

    private static int initialLevel = 0;

    private static PrintStream output = System.out;

    private VerbLogger() {
    }

    /**
     * Print the message to the message PrintStream
     * @param className Class name where the message was created
     * @param msg message to be printed
     */
    public static void println(String className, String msg) {
        switch(level) {
            case (INFO):
                {
                    output.println("INFO: " + msg);
                    break;
                }
            case (DEBUG):
                {
                    output.println("DEBUG: " + className + " " + DateFormat.getDateTimeInstance().format(new Date()) + ": " + msg);
                    break;
                }
            case (ERROR):
                {
                    output.println("ERROR: " + className + " " + DateFormat.getDateTimeInstance().format(new Date()) + ": " + msg);
                    break;
                }
        }
    }

    /**
     * Print the message to the message PrintStream
     * @param className Class name where the message was created
     * @param msg message to be printed
     */
    public static void print(String className, String msg) {
        switch(level) {
            case (INFO):
                {
                    output.print("INFO: " + msg);
                    break;
                }
            case (DEBUG):
                {
                    output.print("DEBUG: " + className + " " + DateFormat.getDateTimeInstance().format(new Date()) + ": " + msg);
                    break;
                }
            case (ERROR):
                {
                    output.print("ERROR: " + className + " " + DateFormat.getDateTimeInstance().format(new Date()) + ": " + msg);
                    break;
                }
        }
    }

    /**
     * Return the PrintStream used to print the messages 
     * @return A PrintStream object
     */
    public static PrintStream getOutput() {
        return output;
    }

    /**
     * Set the PrintStream who will be used to print the messages
     * @param output a PrintStream object
     */
    public static void setOutput(PrintStream output) {
        VerbLogger.output = output;
    }

    /**
     * Return the information level to print the messages 
     * @return An integer 0 not message printing, 1 info level and 2 debug level  
     */
    public static int getLevel() {
        return level;
    }

    /**
     * Set the information level to print the messages
     * @param level a integer with values 0 (do nothing), 1 (info), 2 (debug)
     */
    public static void setLevel(int level) {
        switch(level) {
            case (ERROR):
                {
                    output = System.err;
                    break;
                }
            default:
                {
                    output = System.out;
                    break;
                }
        }
        VerbLogger.level = level;
    }

    /**
     * Return the initial information level to print the messages
     * @return An integer 0 not message printing, 1 info level and 2 debug level  
     */
    public static int getInitialLevel() {
        return initialLevel;
    }
}
