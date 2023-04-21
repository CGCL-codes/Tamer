package es.cells.sardana.client.framework.cli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ExecHelper implements Runnable {

    private byte[] inBuffer = new byte[1024];

    private byte[] errBuffer = new byte[1024];

    private Process process;

    private InputStream pErrorStream;

    private InputStream pInputStream;

    private OutputStream pOutputStream;

    private PrintWriter outputWriter;

    private Thread processThread;

    private Thread inReadThread;

    private Thread errReadThread;

    private ExecProcessor handler;

    private ExecHelper(ExecProcessor ep, Process p) {
        handler = ep;
        process = p;
        pErrorStream = process.getErrorStream();
        pInputStream = process.getInputStream();
        pOutputStream = process.getOutputStream();
        outputWriter = new PrintWriter(pOutputStream, true);
        processThread = new Thread(this);
        inReadThread = new Thread(this);
        errReadThread = new Thread(this);
        processThread.start();
        inReadThread.start();
        errReadThread.start();
    }

    private void processEnded(int exitValue) {
        handler.processEnded(exitValue);
    }

    private void processNewInput(String input) {
        handler.processNewInput(input);
    }

    private void processNewError(String error) {
        handler.processNewError(error);
    }

    public static ExecHelper exec(ExecProcessor handler, String command) throws IOException {
        return new ExecHelper(handler, Runtime.getRuntime().exec(command));
    }

    public void print(String output) {
        outputWriter.print(output);
    }

    public void println(String output) {
        outputWriter.println(output);
    }

    public void run() {
        if (processThread == Thread.currentThread()) {
            try {
                processEnded(process.waitFor());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } else if (inReadThread == Thread.currentThread()) {
            try {
                for (int i = 0; i > -1; i = pInputStream.read(inBuffer)) {
                    processNewInput(new String(inBuffer, 0, i));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (errReadThread == Thread.currentThread()) {
            try {
                for (int i = 0; i > -1; i = pErrorStream.read(errBuffer)) {
                    processNewError(new String(errBuffer, 0, i));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
