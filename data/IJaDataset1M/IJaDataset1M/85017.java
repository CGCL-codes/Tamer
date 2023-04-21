package org.ws4d.java.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.ws4d.java.communication.tcp.IConnection;
import org.ws4d.java.communication.udp.AbstractDatagramOutputStream;
import org.ws4d.java.constants.WS4DConstants;
import org.ws4d.java.logging.Log;

/**
 * This class implements a management of open streams. Each stream opened by any
 * class in the framework should be added to the list of open stream to enable
 * the watchdog to end blocked threads and close all related streams.
 */
public class OpenStreamsTable {

    private static Hashtable openStreamsTable = new Hashtable();

    private static int numOfStreams;

    private static byte accessCount = 0;

    private static final byte MAX_ACCESS_COUNT = 10;

    /**
	 * Add an opened stream to the table. The thread is determined by a call to
	 * Thread.currentThread().
	 * 
	 * @param stream The stream to be added.
	 */
    public static synchronized void addStream(Object stream) {
        Thread thread = Thread.currentThread();
        Vector streams = (Vector) openStreamsTable.get(thread);
        if (streams == null) {
            streams = new Vector(2);
            openStreamsTable.put(thread, streams);
        }
        if (!streams.contains(stream)) {
            if (stream instanceof OutputStream || stream instanceof InputStream) {
                streams.insertElementAt(stream, 0);
            } else {
                streams.addElement(stream);
            }
            numOfStreams++;
        }
        if (++accessCount == MAX_ACCESS_COUNT) {
            cleanUp();
            accessCount = 0;
        }
    }

    /**
	 * Remove a closed stream from the table.
	 * 
	 * @param stream The closed stream.
	 */
    public static synchronized boolean removeStream(Object stream) {
        boolean ret = false;
        Thread thread = Thread.currentThread();
        Vector streams = (Vector) openStreamsTable.get(thread);
        if (streams != null) {
            int idx = streams.indexOf(stream);
            if (idx != -1) {
                streams.removeElementAt(idx);
                numOfStreams--;
                ret = true;
                if (streams.size() == 0) openStreamsTable.remove(thread);
            }
        }
        return ret;
    }

    /**
	 * Closes the stream.
	 * 
	 * @param stream The stream to close.
	 * @param sendError <code>true</code> if a error message should be send
	 *            before close, <code>false</code> otherwise.
	 */
    private static void close(Object stream, boolean sendError) {
        try {
            if (stream instanceof InputStream) {
                ((InputStream) stream).close();
            } else if (stream instanceof OutputStream) {
                if (sendError && !(stream instanceof AbstractDatagramOutputStream)) {
                    ResponseOutput out = new ResponseOutput((OutputStream) stream, false);
                    out.sendFault(WS4DConstants.WS4D_EXCEPTION_TIMEOUT);
                }
                ((OutputStream) stream).close();
            } else {
                ((IConnection) stream).close();
            }
        } catch (IOException e) {
            Log.printStackTrace(e);
        }
    }

    /**
	 * Close all open streams of the current thread.
	 */
    public static void closeStreamsOfThread() {
        closeStreamsOfThread(Thread.currentThread(), false);
    }

    /**
	 * Close all open streams of a given thread.
	 * 
	 * @param thread The thread that opened the streams.
	 */
    public static void closeStreamsOfThread(Thread thread) {
        closeStreamsOfThread(thread, false);
    }

    /**
	 * Close all open streams of a given thread.
	 * 
	 * @param thread The thread that opened the streams.
	 * @param sendError Send error response to other side of stream.
	 */
    public static synchronized void closeStreamsOfThread(Thread thread, boolean sendError) {
        Vector streams = (Vector) openStreamsTable.get(thread);
        if (streams != null) {
            int i = 0;
            int size = streams.size();
            for (i = size - 1; i >= 0; i--) {
                Object stream = streams.elementAt(i);
                if (stream != null) {
                    close(stream, sendError);
                }
            }
            numOfStreams -= streams.size();
            openStreamsTable.remove(thread);
        }
    }

    /**
	 * Internal method that cleans up the table.
	 */
    private static void cleanUp() {
        Enumeration threads = openStreamsTable.keys();
        while (threads.hasMoreElements()) {
            Thread t = (Thread) threads.nextElement();
            if (!t.isAlive()) {
                closeStreamsOfThread(t);
                Log.debug("Closing open streams for dead thread " + t);
            }
        }
    }
}
