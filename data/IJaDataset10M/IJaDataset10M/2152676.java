package net.sourceforge.dawnlite.script.output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class provides a simple means of forwarding an InputStream to an OutputStream,
 * with the primary target use case of forwarding a separate process' output to the Java process,
 * either for direct forwarding to the console or any other kind of processing. 
 *
 * @author reiterer
 *
 */
public class InterProcessCommThread extends Thread {

    int sleepTime = 100;

    /**
	 * @return the sleepTime
	 */
    public int getSleepTime() {
        return sleepTime;
    }

    /**
	 * @param sleepTime the sleepTime to set
	 */
    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    final OutputStream outputStream;

    final InputStream inputStream;

    boolean mustQuit = false;

    public InterProcessCommThread(InputStream inputStream, OutputStream outputStream) {
        super();
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        int r;
        while (!mustQuit) {
            try {
                r = 0;
                r += transfer(inputStream, outputStream);
                if (r == 0) {
                    sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

    int transfer(InputStream in, OutputStream out) {
        int r;
        try {
            r = in.available();
        } catch (IOException e1) {
            mustQuit();
            return 0;
        }
        if (r > 0) {
            byte[] buf = new byte[r];
            try {
                r = in.read(buf, 0, r);
            } catch (IOException e) {
                mustQuit();
                return 0;
            }
            try {
                out.write(buf, 0, r);
            } catch (IOException e) {
                mustQuit();
                return 0;
            }
        }
        return r;
    }

    void mustQuit() {
        this.mustQuit = true;
        this.interrupt();
    }
}
