package org.ttalbott.mytelly;

import java.io.BufferedReader;

/**
 * @author Tom
 *
 * patterned after LogReaderThread from ddclient
 */
public class WSLogReaderThread extends Thread {

    /**
	 * A <code>Reader</code> that is used to read the messages.
	 */
    private BufferedReader reader;

    /**
	 * The <code>LogCallback</code> to which the messages read are to be
	 * written.
	 */
    private LogCallback logArea;

    /**
	 * Create a new instance of the <code>thread</code>, and initialise
	 * the instance variables.
	 *
	 * @param BufferedReader reader - The {@link #reader} reference to
	 *   set.
	 * @param JTextArea logArea - The {@link #logArea} reference to set.
	 */
    public WSLogReaderThread(BufferedReader reader, LogCallback logArea) {
        this.reader = reader;
        this.logArea = logArea;
    }

    /**
	 * Defines the actions to be performed by this thread when it runs.
	 * Read the lines of text from the {@link #reader}, and appends the 
	 * line to the {@link #logArea}.
	 */
    public void run() {
        try {
            String line = "";
            while ((line = reader.readLine()) != null) {
                logArea.update(null, line);
            }
            reader.close();
        } catch (java.io.IOException ioex) {
        }
    }
}
