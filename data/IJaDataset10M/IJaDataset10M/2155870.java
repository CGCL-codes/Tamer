package org.tigris.subversion.javahl;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * This class connects a java.io.PipedInputStream to a OutputInterface.
 * The other side of the Pipe must read by another thread, or deadlocks
 * will occur.
 */
public class SVNInputStream extends PipedInputStream {

    /**
     * my connection to put data into subversion
     */
    Outputer myOutputer;

    /**
     * Creates a SVNInputStream so that it is connected with an internal
     * PipedOutputStream
     * @throws IOException
     */
    public SVNInputStream() throws IOException {
        myOutputer = new Outputer(this);
    }

    /**
     * Get the Interface to connect to SVNAdmin
     * @return the connection interface
     */
    public OutputInterface getOutputer() {
        return myOutputer;
    }

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream.
     *
     * <p> The <code>close</code> method of <code>InputStream</code> does
     * nothing.
     *
     * @throws  IOException  if an I/O error occurs.
     */
    public void close() throws IOException {
        myOutputer.closed = true;
        super.close();
    }

    /**
     * this class implements the connection to SVNAdmin
     */
    public class Outputer implements OutputInterface {

        /**
         * my side of the pipe
         */
        PipedOutputStream myStream;

        /**
         * flag that the other side of the pipe has been closed
         */
        boolean closed;

        /**
         * build a new connection object
         * @param myMaster  the other side of the pipe
         * @throws IOException
         */
        Outputer(SVNInputStream myMaster) throws IOException {
            myStream = new PipedOutputStream(myMaster);
        }

        /**
         * write the bytes in data to java
         * @param data          the data to be written
         * @throws IOException  throw in case of problems.
         */
        public int write(byte[] data) throws IOException {
            if (closed) throw new IOException("stream has been closed");
            myStream.write(data);
            return data.length;
        }

        /**
         * close the output
         * @throws IOException throw in case of problems.
         */
        public void close() throws IOException {
            myStream.close();
        }
    }
}
