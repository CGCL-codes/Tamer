package edu.cmu.sphinx.util;

import java.io.IOException;

/**
 * Provides a standard interface to a batching mechanism
 *
 */
public interface BatchManager {

    /**
     * Starts processing the batch
     *
     * @throws IOException if an error occurs while processing the
     * batch file
     */
    public void start() throws IOException;

    /**
     * Gets the next available batch item or null if no more are
     * available
     *
     * @return the next available batch item
     *
     * @throws IOException if an error occurs while processing the
     * batch file
     */
    public BatchItem getNextItem() throws IOException;

    /**
     * Stops processing the batch
     *
     * @throws IOException if an error occurs while processing the
     * batch file
     */
    public void stop() throws IOException;

    /**
     * Returns the name of the file
     *
     * @return the filename
     */
    public String getFilename();
}
