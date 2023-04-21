package org.silicolife.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PasswordHelper {

    /**
	 * Gets the password and hides the input by having a console eraser thread to print \b characters
	 * @return The password
	 * @throws Exception
	 */
    public static String getPassword() throws Exception {
        ConsoleEraser consoleEraser = new ConsoleEraser();
        System.err.print("password: ");
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        consoleEraser.start();
        String pass = stdin.readLine();
        consoleEraser.halt();
        System.err.print("\n");
        return pass;
    }

    /**
	 * Sends \b to stderr to hide any input by the user on stdin.
	 * @author Tim
	 */
    private static class ConsoleEraser extends Thread {

        private boolean running = true;

        /**
		 * While running print \b
		 * @see java.lang.Runnable#run()
		 */
        public void run() {
            while (running) {
                System.err.print("\b ");
            }
        }

        /**
		 * Halts the thread by setting running to false
		 */
        public synchronized void halt() {
            running = false;
        }
    }
}
