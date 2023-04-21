package net.sf.japaki.text;

import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import net.sf.japaki.io.RestorableIO;

/**
 * A {@code ParseReader} is used for parsing an input stream.
 * It stores all necessary information about the input stream and the
 * parsing status.
 * @version 1.0
 * @author Ralph Wagner
 */
public abstract class ParseReader implements RestorableIO<ParseReader>, PathTracker {

    /**
     * Forwards to the next line in the input stream.
     * @throws IOException if an I/O error occurs
     * @throws ParseException if end of current line was not reached
     */
    public abstract void newLine() throws IOException, ParseException;

    /**
     * Checks if the input stream is at its end.
     * @return {@code true} if end of file is reached
     */
    public abstract boolean eof();

    /**
     * Returns the current line of the input stream.
     * @return A string containing the contents of the last line read,
     * not including any line-termination characters,
     * or {@code null} if the end of the stream has been reached.
     * @see java.io.BufferedReader#readLine()
     */
    public abstract String getSource();

    /**
     * Returns the current parse position.
     * @return the current parse position
     */
    public abstract ParsePosition getPosition();

    /**
     * Creates a parsing exception with the specified error message.
     * @param errorMessage message set in the exception
     * @throws ParsingException if the error index of the parse position is set.
     */
    public abstract void checkForException(String errorMessage) throws ParsingException;
}
