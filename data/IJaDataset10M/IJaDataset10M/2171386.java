package net.sourceforge.myvd.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamReader extends Thread {

    InputStream in;

    boolean debug;

    boolean done;

    public StreamReader(InputStream in, boolean debug) {
        this.in = in;
        this.debug = debug;
        done = false;
    }

    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(this.in));
        String line;
        try {
            while ((line = in.readLine()) != null) {
                if (debug) {
                }
            }
        } catch (IOException e) {
        }
        done = true;
    }

    public boolean isDone() {
        return this.done;
    }
}
