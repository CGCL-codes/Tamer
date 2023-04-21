package net.sf.mailsomething.util.localnet;

import java.util.LinkedList;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class OutChannel implements Runnable {

    private boolean run = true;

    private Thread thread;

    private BufferedWriter out;

    private LinkedList fifostack;

    /**
	 * One could consider if each channel could have several listeners. 
	 * 
	 * 
	 */
    public OutChannel(BufferedWriter out) {
        this.out = out;
        fifostack = new LinkedList();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void cleanup() {
        run = false;
        try {
            out.close();
        } catch (IOException f) {
        }
    }

    public void send(byte[] bytes) {
        fifostack.addLast(bytes);
    }

    public void write(byte[] bytes) {
        try {
            for (int i = 0; i < bytes.length; i++) out.write(bytes[i]);
            out.flush();
        } catch (IOException f) {
            f.printStackTrace();
            cleanup();
        }
    }

    public void run() {
        while (0 < 1) {
            String inline;
            if (!run) break;
            try {
                if (fifostack.size() > 0) {
                    byte[] bytes = (byte[]) fifostack.getFirst();
                    for (int i = 0; i < bytes.length; i++) out.write(bytes[i]);
                    out.flush();
                    fifostack.removeFirst();
                }
            } catch (IOException f) {
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException f) {
            }
        }
    }

    private void writeLine(String line) throws IOException {
        out.write(line, 0, line.length());
        out.write(13);
        out.write(10);
        out.flush();
    }
}
