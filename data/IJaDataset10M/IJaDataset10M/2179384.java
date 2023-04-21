package org.spbu.pldoctoolkit.clones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

final class ReaderThread implements Runnable {

    private final BufferedReader in;

    private volatile boolean stopped;

    private final String processName;

    private final IStopper stopper;

    private boolean needPrint;

    ReaderThread(InputStream inputStream, IStopper stopCond, String processName, boolean needPrint) {
        this(inputStream, stopCond, true, processName, needPrint);
    }

    ReaderThread(InputStream inputStream, IStopper stopCond, boolean isDaemon, String processName, boolean needPrint) {
        assert inputStream != null;
        this.needPrint = needPrint;
        this.processName = processName;
        this.stopper = stopCond;
        BufferedReader inTmp = null;
        try {
            inTmp = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        in = inTmp;
        Thread myThread = new Thread(this, "Process Reader " + processName);
        myThread.setDaemon(isDaemon);
        myThread.start();
    }

    @Override
    public void run() {
        if (in != null) {
            String line = null;
            while (!stopped) {
                try {
                    line = in.readLine();
                    if (line != null) {
                        if (needPrint) System.out.println("PROCESS " + processName + ": " + line);
                        if (stopper.stop(line)) this.stop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void stop() {
        stopped = true;
    }

    static interface IStopper {

        boolean stop(String line);
    }
}
