package org.lirc;

import java.io.*;

/**
 * Superclass for senders that send lirc data to an output stream.
 * @version $Revision: 1.1 $
 * @author Bjorn Bringert (bjorn@mumblebee.com)
 * @author Stefan Carlsson, Rasmus Andersson
 */
public class StreamSender implements Sender {

    /** The Sender used to read from the lirc daemon. */
    private BufferedWriter out;

    /**
	 * Creates a new StreamSender
	 */
    public StreamSender() throws LIRCException {
    }

    /**
	 * Creates a new StreamSender
	 */
    public StreamSender(OutputStream outStream) throws LIRCException {
        setOutput(outStream);
    }

    /**
	 * Sets the input stream that this sender writes to.
	 */
    protected void setOutput(OutputStream outStream) {
        out = new BufferedWriter(new OutputStreamWriter(outStream));
    }

    /**
	 * Closes the output stream.
	 */
    public void close() {
        try {
            out.close();
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    }

    public void finalize() {
        close();
    }

    /** 
	 * Writes a string to the lircd socket.
	 * @throws org.lirc.LIRCException if there is a problem writing.
	 */
    public void writeCode(String outLine) throws LIRCException {
        try {
            out.write(outLine, 0, outLine.length());
            out.newLine();
            out.flush();
        } catch (IOException ex) {
            throw new LIRCException(ex.getMessage());
        }
    }
}
