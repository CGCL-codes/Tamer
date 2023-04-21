package opennlp.ccg.util;

import java.io.*;

/**
 * Abstract command-line reader.
 *
 * @author  Michael White
 * @author  David Reitter
 * @version $Revision: 1.2 $, $Date: 2005/10/13 20:33:49 $
 */
public abstract class LineReader {

    /** Creates a default line reader (currently a JLineReader) with the given completion strings. */
    public static LineReader createLineReader(String[] completions) throws IOException {
        return new JLineReader(completions);
    }

    /** Sets the command history. */
    public abstract void setCommandHistory(String histStr) throws IOException;

    /** Gets the current command history. */
    public abstract String getCommandHistory() throws IOException;

    /** Returns an input string, using the given prompt. */
    public abstract String readLine(String prompt) throws IOException;
}
