package com.volantis.mcs.protocols.text;

import com.volantis.synergetics.cornerstone.utilities.WhitespaceUtilities;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * A writer which allows explicit control over whitespace in a logical fashion.
 * <p>
 * It allows the client to add "logical" spaces and newlines which are written
 * into the output only as and if required. Specifically:
 * <ul>
 *  <li>Multiple spaces compress to one space.
 *  <li>Multiple lines compress to one line.
 *  <li>Leading and trailing spaces on lines are ignored.
 *  <li>Literal whitespace content is not detected (unlike 
 *      {@link NormaliseWhitespaceWriter}.  
 * </ul>
 */
public class LogicalWhitespaceWriter extends FilterWriter {

    /**
     * The canonical newline character used for whitespace. 
     */
    private static final Character NEWLINE = new Character('\n');

    /** 
     * The canonical space character used for whitespace. 
     */
    private static final Character SPACE = new Character(' ');

    /**
     * Set this to true if you want to see in line debugging comments in the
     * output.
     */
    private static final boolean debug = SMS.debug;

    /**
     * A character which stores the last whitespace which was "queued" to be 
     * written by one of the printXxxx methods.
     */
    private Character whitespace;

    /**
     * A simple flag to remember if we have written anything to our output 
     * writer.<p> Useful for detecting and removing leading whitespace.
     */
    private boolean writtenTo;

    /**
     * Constructs a new <code>LogicalWhitespaceWriter</code>, which writes 
     * though to the quiet writer specified.
     *
     * @param out the writer to send our output to.
     */
    public LogicalWhitespaceWriter(Writer out) {
        super(out);
    }

    /**
     * Write a character to the output, respecting contained whitespace, 
     * and ignoring any IOExceptions.
     * 
     * @param c     char to write.
     * @exception IOException
     */
    public void write(int c) throws IOException {
        flushWhitespace();
        super.write(c);
        writtenTo = true;
    }

    /**
     * Write a section of a character array to the output, respecting
     * contained whitespace, and ignoring any IOExceptions.
     * 
     * @param cbuf  buffer to write from
     * @param off   character offset to start from
     * @param len   number of characters to write
     * @exception IOException
     */
    public void write(char cbuf[], int off, int len) throws IOException {
        if (len > 0) {
            flushWhitespace();
            super.write(cbuf, off, len);
            writtenTo = true;
        }
    }

    /**
     * Write a section of a string to the output, respecting contained 
     * whitespace, and ignoring any IOExceptions.
     * 
     * @param str   string to write from
     * @param off   character offset to start from
     * @param len   number of characters to write
     * @exception IOException
     */
    public void write(String str, int off, int len) throws IOException {
        if (len > 0) {
            flushWhitespace();
            super.write(str, off, len);
            writtenTo = true;
        }
    }

    /**
     * Writes a section of a character array, with leading and trailing 
     * whitespace removed, and ignoring any IOExceptions.
     * 
     * @param cbuf  buffer to write from
     * @param off   character offset to start from
     * @param len   number of characters to write
     * @exception IOException
     */
    public void writeTrimmed(char cbuf[], int off, int len) throws IOException {
        int trimOff = WhitespaceUtilities.getFirstNonWhitespaceIndex(cbuf, off, len);
        int trimEnd = WhitespaceUtilities.getLastNonWhitespaceIndex(cbuf, off, len);
        int trimLen = trimEnd + 1 - trimOff;
        if (trimOff == trimEnd) {
            trimLen = 0;
        }
        write(cbuf, trimOff, trimLen);
    }

    /**
     * Prints a logical space to the output. This will mask any whitespace 
     * considered to be unnecessary for the purposes of SMS output.
     * 
     * @exception IOException 
     */
    public void writeSpace() throws IOException {
        if (whitespace == null) {
            whitespace = SPACE;
        }
    }

    /**
     * Prints a logical line to the output. This will mask any whitespace 
     * considered to be unnecessary for the purposes of SMS output. 
     * 
     * @exception IOException 
     */
    public void writeLine() throws IOException {
        whitespace = NEWLINE;
    }

    /**
     * Called by the write methods to "lazy write" whitespace when we 
     * really have to.

     * @exception IOException
     */
    private void flushWhitespace() throws IOException {
        if (whitespace != null) {
            if (writtenTo) {
                if (debug) {
                    if (whitespace == SPACE) {
                        super.write("_");
                    } else if (whitespace == NEWLINE) {
                        super.write("¬");
                        super.write(NEWLINE.charValue());
                    } else {
                        super.write("{" + whitespace.charValue() + "}");
                    }
                } else {
                    super.write(whitespace.charValue());
                }
            }
            whitespace = null;
        }
    }
}
