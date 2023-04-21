package org.jcvi.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import org.jcvi.util.FIFOQueue;

public class TextLineParser implements Closeable {

    private int endOfLine;

    private InputStream in;

    private Object endOfFile = new Object();

    private FIFOQueue<Object> nextQueue = new FIFOQueue<Object>();

    boolean doneFile = false;

    public TextLineParser(InputStream in) throws IOException {
        this(in, '\n');
    }

    public TextLineParser(InputStream in, char endOfLine) throws IOException {
        if (in == null) {
            throw new NullPointerException("inputStream can not be null");
        }
        this.endOfLine = endOfLine;
        this.in = in;
        getNextLine();
    }

    private void getNextLine() throws IOException {
        if (doneFile) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        int value;
        value = in.read();
        while (true) {
            if (value == -1) {
                doneFile = true;
                close();
                break;
            }
            builder.append((char) value);
            if (value == endOfLine) {
                break;
            }
            value = in.read();
        }
        if (builder.length() > 0) {
            nextQueue.add(builder.toString());
        }
        if (doneFile) {
            nextQueue.add(endOfFile);
        }
    }

    public boolean hasNextLine() {
        Object next = nextQueue.peek();
        return next != endOfFile;
    }

    public String nextLine() throws IOException {
        Object next = nextQueue.poll();
        if (next == endOfFile) {
            return null;
        }
        getNextLine();
        return (String) next;
    }

    @Override
    public void close() throws IOException {
        IOUtil.closeAndIgnoreErrors(in);
        nextQueue.clear();
    }
}
