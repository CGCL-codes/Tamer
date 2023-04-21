package javacode.net.sf.capit.utils;

import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JTextArea;

/**
 * This class represents our output channel in which
 * we print information regarding our program execution
 * and results.
 * 
 * @author Issle
 *
 */
public class Printer {

    public static JTextArea textArea;

    private static ReentrantLock lock = new ReentrantLock();

    /**
	 * Appends the input content to the print area.
	 * 
	 * @param content
	 */
    public static void print(String content) {
        try {
            textArea.append(content + '\n');
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
	 * Cleans the print area.
	 */
    public static void clean() {
        try {
            lock.lock();
            textArea.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
