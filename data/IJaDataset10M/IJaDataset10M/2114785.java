package org.dbe.composer.wfengine.bpel.server.logging;

import java.io.PrintWriter;

/**
 * Provides an unstructured deployment log base class.  The unstructured deployment
 * logger simply writes all log messages to a PrintWriter (or the class can be
 * extended to provide some other implementation of the <code>writeMessage</code>
 * method.
 */
public class SdlUnstructuredDeploymentLog extends SdlDeploymentLog {

    /** records the log as we're going. */
    protected PrintWriter mWriter;

    /**
     * Default constructor.
     */
    public SdlUnstructuredDeploymentLog() {
    }

    /**
     * Creates the logger that sends its output to the provided writer.
     * @param aWriter
     */
    public SdlUnstructuredDeploymentLog(PrintWriter aWriter) {
        setWriter(aWriter);
    }

    public void close() {
        if (getWriter() != null) getWriter().close();
    }

    /**
     * Getter for the writer.
     */
    protected PrintWriter getWriter() {
        return mWriter;
    }

    /**
     * Setter for the writer
     * @param aWriter
     */
    protected void setWriter(PrintWriter aWriter) {
        mWriter = aWriter;
    }

    /**
     * Writes the message to the buffer and the writer if it's present.
     * @param aMessage
     */
    protected void writeMessage(String aMessage) {
        if (getWriter() != null) getWriter().println(aMessage);
    }
}
