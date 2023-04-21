package org.expasy.jpl.commons.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

/**
 * {@code InteractiveInputScanner} interactively reads input stream.
 * 
 * @author nikitin
 * 
 * @version 1.0
 */
public interface InteractiveInputScanner {

    /** Get the command-line prompt */
    String getPrompt();

    /** Get the pattern matching the input */
    Pattern getInputPattern();

    /** Get the pattern matching the interruption */
    Pattern getExitPattern();

    /** Set input stream from where input is find (stdin by default) */
    void setInputStream(InputStream stream);

    /** Set output stream to where prompt flushed (stdout by default) */
    void setOutputStream(OutputStream stream);

    /** Read input stream */
    String waitInput() throws IOException;

    String getDefaultInput();
}
