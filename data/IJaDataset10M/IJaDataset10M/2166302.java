package eu.util;

import java.io.IOException;

public class Out {

    /**
	 * Print the string
	 * @param message to be printed
	 */
    public static void print(String message) {
        System.out.print(message);
        try {
            FileOperations.printToFile(message, "serverlog.log");
        } catch (IOException ioe) {
            System.out.println("Error printing - " + ioe.getMessage());
        }
    }

    /**
	 * Print the string on it's own line
	 * @param message to be printed
	 */
    public static void println(String message) {
        System.out.println(message);
        try {
            FileOperations.printlnToFile(message, "serverlog.log");
        } catch (IOException ioe) {
            System.out.println("Error printing - " + ioe.getMessage());
        }
    }
}
