package com.noahsloan.nutils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import com.noahsloan.nutils.streams.NullOutputStream;

/**
 * InToOut is a Callable task that is responsible for writing all the bytes from
 * an InputStream to an OutputStream.
 * 
 * InToOut implements Callable so it may be given to a Thread Pool but still
 * throw an Exception.
 * 
 * @author Noah Sloan
 * @see java.util.concurrent.Callable
 * @see java.util.concurrent.Future
 * @see java.util.concurrent.ExecutionException
 */
public class InToOut implements Callable<Void> {

    private static final int BUFFER_SIZE = Integer.getInteger("n-utils.InToOut.BufferSize", 65536);

    private final InputStream in;

    private final OutputStream out;

    private final boolean closeIn;

    private final boolean closeOut;

    /**
     * Creates an instance that will throw away all the bytes left in in.
     * 
     * @param in
     *            the stream to drain.
     * @param closeIn
     *            should we close the input stream when finished?
     */
    public InToOut(InputStream in, boolean closeIn) {
        this(in, new NullOutputStream(), closeIn, true);
    }

    /**
     * Creates a new instance, ready to connect in to out. No streams are closed
     * when it finishes.
     * 
     * @param in
     * @param out
     */
    public InToOut(InputStream in, OutputStream out) {
        this(in, out, false, false);
    }

    /**
     * 
     * @param in
     *            the input
     * @param out
     *            the output stream
     * @param closeIn
     *            should in be closed when the task finishes?
     * @param closeOut
     *            should out be closed when the task finishes?
     */
    public InToOut(InputStream in, OutputStream out, boolean closeIn, boolean closeOut) {
        this.in = in;
        this.out = out;
        this.closeIn = closeIn;
        this.closeOut = closeOut;
    }

    /**
     * Calls connect.
     * 
     * @see Callable#call()
     * @see #connect()
     */
    public Void call() throws IOException {
        connect();
        return null;
    }

    /**
     * Writes all the bytes from in to out. Returns null when finished.
     * 
     * May throw an IOException if the underlying streams do.
     * 
     * Will close the streams if the constructor arguments told it to.
     * 
     * @throws IOException
     */
    public void connect() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        if (closeIn) {
            in.close();
        }
        if (closeOut) {
            out.close();
        }
    }
}
