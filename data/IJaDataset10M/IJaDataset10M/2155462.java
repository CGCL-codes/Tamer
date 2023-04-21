package org.openmi.alterra.persistency;

import org.openmi.alterra.configuration.Composition;
import org.openmi.alterra.configuration.SystemDeployer;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Base class for OpenMI writers that handle streams.
 */
public abstract class StreamWriter implements IOpenMIWriter {

    /**
     * The output connection is a stream.
     */
    protected OutputStream outStream = null;

    /**
     * Writes the specified string to the stream.
     *
     * @param str String to write
     * @throws IOException
     */
    protected void writeToStream(String str) throws IOException {
        if (outStream == null) throw new PersistencyException("Output stream not ready!");
        if (str != null) outStream.write(str.getBytes());
    }

    public abstract void write(SystemDeployer aSystem) throws IOException;

    public abstract void write(Composition aComposition) throws IOException;

    /**
     * Writes a SystemDeployer to the specified stream.
     *
     * @param aStream OutputStream to use
     * @param aSystem SystemDeployer to write
     * @throws IOException
     */
    public final void writeToStream(OutputStream aStream, SystemDeployer aSystem) throws IOException {
        outStream = aStream;
        if (outStream != null) write(aSystem);
    }

    /**
     * Writes a Composition to the specified stream.
     *
     * @param aStream      OutputStream to use
     * @param aComposition Composition to write
     * @throws IOException
     */
    public final void writeToStream(OutputStream aStream, Composition aComposition) throws IOException {
        outStream = aStream;
        if (outStream != null) write(aComposition);
    }
}
