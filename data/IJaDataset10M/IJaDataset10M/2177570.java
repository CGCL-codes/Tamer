package org.supercsv.io;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.List;
import org.supercsv.prefs.CsvPreference;

/**
 * Super CSV v1.02 tokenizer
 * 
 * @author Kasper B. Graversen
 */
public class Tokenizer_v102 implements ITokenizer {

    CsvPreference preferences;

    LineNumberReader lnr;

    StringBuilder sb = null;

    public Tokenizer_v102(final Reader stream, final CsvPreference preference) {
        this.preferences = preference;
        lnr = new LineNumberReader(stream);
        sb = new StringBuilder(500);
    }

    private void addSpaces(final StringBuilder sb, final int spaces) {
        for (int i = 0; i < spaces; i++) {
            sb.append(" ");
        }
    }

    /**
 * {@inheritDoc}
 */
    public void close() throws IOException {
        lnr.close();
    }

    /**
 * {@inheritDoc}
 */
    public int getLineNumber() {
        return lnr.getLineNumber();
    }

    /**
 * {@inheritDoc}
 */
    public boolean readStringList(final List<String> result) throws IOException {
        result.clear();
        PARSERSTATE state = PARSERSTATE.NORMAL;
        final int quote = preferences.getQuoteChar();
        final int delim = preferences.getDelimiterChar();
        String line;
        do {
            line = lnr.readLine();
            if (line == null) {
                return false;
            }
        } while (line.length() == 0);
        line = line + "\n";
        sb = new StringBuilder();
        int p = 0;
        int linenoQuoteState = -1;
        int potentialSpaces = 0;
        while (true) {
            final char c = line.charAt(p);
            switch(state) {
                case NORMAL:
                    if (c == delim) {
                        result.add(sb.toString());
                        sb.delete(0, sb.length());
                        potentialSpaces = 0;
                        break;
                    } else if (c == ' ') {
                        if (sb.length() > 0) {
                            potentialSpaces++;
                        }
                        break;
                    } else if (c == '\n') {
                        result.add(sb.toString());
                        sb = null;
                        potentialSpaces = 0;
                        return true;
                    } else if (c == quote && sb.length() == 0) {
                        state = PARSERSTATE.QUOTESCOPE;
                        linenoQuoteState = getLineNumber();
                        break;
                    } else if (c == quote && line.charAt(p + 1) == quote && sb.length() > 0) {
                        addSpaces(sb, potentialSpaces);
                        potentialSpaces = 0;
                        sb.append(c);
                        p++;
                        break;
                    } else if (c == quote && line.charAt(p + 1) != quote) {
                        state = PARSERSTATE.QUOTESCOPE;
                        linenoQuoteState = getLineNumber();
                        addSpaces(sb, potentialSpaces);
                        potentialSpaces = 0;
                        break;
                    } else {
                        addSpaces(sb, potentialSpaces);
                        potentialSpaces = 0;
                        sb.append(c);
                    }
                    break;
                case QUOTESCOPE:
                    if (c == delim) {
                        sb.append(c);
                        break;
                    } else if (c == '\n') {
                        sb.append('\n');
                        p = -1;
                        line = lnr.readLine();
                        if (line == null) {
                            throw new IOException("File ended unexpectedly while reading a quoted cell starting on line: " + linenoQuoteState);
                        }
                        line = line + '\n';
                        break;
                    } else if (c == quote && line.charAt(p + 1) == quote) {
                        sb.append(c);
                        p++;
                        break;
                    } else if (line.charAt(p) == quote && line.charAt(p + 1) != quote) {
                        state = PARSERSTATE.NORMAL;
                        break;
                    } else {
                        sb.append(c);
                    }
                    break;
                default:
                    throw new RuntimeException("this can never happen!");
            }
            p++;
        }
    }
}
