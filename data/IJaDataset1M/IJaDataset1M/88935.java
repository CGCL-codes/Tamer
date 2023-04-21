package com.acuityph.commons.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Alistair A. Israel
 * @since 0.1.3
 */
public class CsvReader {

    private final BufferedReader in;

    private boolean trimSpaces;

    /**
     * Constructs a CsvReader around a given {@link Reader} which trims spaces
     * (or not) based on the trimSpaces parameter.
     *
     * @param in
     *        The input Reader
     * @param trimSpaces
     *        indicates whether this CsvReader should trim spaces or not
     */
    public CsvReader(final Reader in, final boolean trimSpaces) {
        if (in instanceof BufferedReader) {
            this.in = (BufferedReader) in;
        } else {
            this.in = new BufferedReader(in);
        }
        this.trimSpaces = trimSpaces;
    }

    /**
     * Constructs a CsvReader around a given {@link Reader}
     *
     * @param in
     *        The input Reader
     */
    public CsvReader(final Reader in) {
        this(in, true);
    }

    /**
     * @return the trimSpaces
     */
    public final boolean isTrimSpaces() {
        return this.trimSpaces;
    }

    /**
     * @param trimSpaces
     *        the trimSpaces to set
     */
    public final void setTrimSpaces(final boolean trimSpaces) {
        this.trimSpaces = trimSpaces;
    }

    /**
     * Reads a single CSV line and returns it as a String array. Returns null if
     * the end of file is reached.
     *
     * @return the CSV line as a String array, or null if EOF is reached
     * @throws IOException
     *         on exception
     */
    public final String[] readLine() throws IOException {
        final String line = in.readLine();
        if (line != null) {
            return CsvUtility.parse(line, trimSpaces);
        }
        return null;
    }
}
