package org.archive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class providing an Iterator interface over line-oriented
 * text input. By providing regexps indicating lines to ignore
 * (such as pure whitespace or comments), lines to consider input, and
 * what to return from the input lines (such as a whitespace-trimmed
 * non-whitespace token with optional trailing comment), this can
 * be configured to handle a number of formats. 
 * 
 * The public static members provide pattern configurations that will
 * be helpful in a wide variety of contexts. 
 * 
 * @author gojomo
 */
public class LineReadingIterator implements Iterator {

    private static final Logger logger = Logger.getLogger(LineReadingIterator.class.getName());

    public static final String COMMENT_LINE = "\\s*(#.*)?";

    public static final String NONWHITESPACE_ENTRY_TRAILING_COMMENT = "^\\s*(\\S+)\\s*(#.*)?$";

    public static final String ENTRY = "$1";

    protected BufferedReader reader = null;

    protected Matcher ignoreLine = null;

    protected Matcher extractLine = null;

    protected String outputTemplate = null;

    private String next;

    public LineReadingIterator(BufferedReader r, String ignore, String extract, String replace) {
        reader = r;
        ignoreLine = Pattern.compile(ignore).matcher("");
        extractLine = Pattern.compile(extract).matcher("");
        outputTemplate = replace;
    }

    /** 
     * Test whether any items remain; loads next item into
     * holding 'next' field. 
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return (this.next != null) ? true : loadNext();
    }

    /**
     * Loads next item into lookahead spot, if available. Skips
     * lines matching ignoreLine; extracts desired portion of
     * lines matching extractLine; informationally reports any
     * lines matching neither. 
     * 
     * @return whether any item was loaded into next field
     */
    private boolean loadNext() {
        try {
            String read;
            while ((read = this.reader.readLine()) != null) {
                ignoreLine.reset(read);
                if (ignoreLine.matches()) {
                    continue;
                }
                extractLine.reset(read);
                if (extractLine.matches()) {
                    StringBuffer output = new StringBuffer();
                    extractLine.appendReplacement(output, outputTemplate);
                    next = output.toString();
                    return true;
                }
                logger.info("nonsense line: " + read);
            }
            return false;
        } catch (IOException e) {
            logger.warning(e.toString());
            return false;
        }
    }

    /** 
     * Return the next item.
     * 
     * @see java.util.Iterator#next()
     */
    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        String retVal = this.next;
        this.next = null;
        return retVal;
    }

    /**
     * Not supported.
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
