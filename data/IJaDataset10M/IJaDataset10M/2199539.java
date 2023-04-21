package net.engine.file.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CSVLineReader {

    protected static final int END_OF_LINE = -1;

    protected static final int END_OF_FILE = -2;

    protected static final int NOT_ENDED = -3;

    protected char separator;

    protected Set<String> errors;

    protected int endReason;

    private BufferedReader bufferedReader;

    public static List<String> read(String row, char seperator) {
        CSVLineReader csvLineReader = new CSVLineReader(seperator);
        try {
            return csvLineReader.readLine(row);
        } catch (IOException e) {
            return null;
        }
    }

    public CSVLineReader(char separator) {
        this.separator = separator;
        this.bufferedReader = null;
    }

    public List<String> readLine(String row) throws IOException {
        StringReader stringReader = new StringReader(row);
        bufferedReader = new BufferedReader(stringReader);
        errors = null;
        List<String> strings = new ArrayList<String>();
        for (; ; ) {
            String string = readValue();
            if ((endReason == NOT_ENDED)) {
                strings.add(string);
            } else if (endReason == END_OF_FILE) {
                if ((strings.size() == 0) && (string.length() == 0)) {
                    return null;
                } else {
                    strings.add(string);
                    return strings;
                }
            } else if (endReason == END_OF_LINE) {
                strings.add(string);
                return strings;
            }
        }
    }

    private String readValue() throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        boolean inQuotes = false;
        boolean firstChar = true;
        boolean everInQuotes = false;
        for (; ; ) {
            if (!inQuotes) {
                int i = readCharOutsideQuotes();
                if (i == END_OF_FILE) {
                    endReason = END_OF_FILE;
                    break;
                }
                if (i == END_OF_LINE) {
                    endReason = END_OF_LINE;
                    break;
                }
                char c = (char) i;
                if (c == '\"') {
                    if (!firstChar) {
                        addError("Quotes must appear immediately after separator.");
                    }
                    everInQuotes = true;
                    inQuotes = true;
                }
                if (c == separator) {
                    endReason = NOT_ENDED;
                    break;
                }
                if (everInQuotes && !inQuotes) {
                    addError("Quotes must appear immediately before separator.");
                }
                stringBuffer.append(c);
            } else {
                int i = readCharInsideQuotes();
                if (i == END_OF_FILE) {
                    addError("End of line reached in quotes.");
                    endReason = END_OF_FILE;
                    break;
                }
                char c = (char) i;
                if (c == '\"') {
                    if (!readAheadQuote()) {
                        inQuotes = false;
                    }
                }
                stringBuffer.append(c);
            }
            firstChar = false;
        }
        return stringBuffer.toString();
    }

    private void addError(String error) {
        if (errors == null) {
            errors = new LinkedHashSet<String>();
        }
        errors.add(error);
    }

    private int readCharInsideQuotes() throws IOException {
        int i = bufferedReader.read();
        if (i == -1) {
            return END_OF_FILE;
        }
        return i;
    }

    private int readCharOutsideQuotes() throws IOException {
        int i = bufferedReader.read();
        if (i == -1) {
            return END_OF_FILE;
        } else {
            char c = (char) i;
            if (c == '\n') {
                readAheadNewLine('\r');
                return END_OF_LINE;
            } else if (c == '\r') {
                readAheadNewLine('\n');
                return END_OF_LINE;
            }
            return (int) c;
        }
    }

    private boolean readAheadNewLine(char secondNewLineChar) throws IOException {
        bufferedReader.mark(1);
        int i = bufferedReader.read();
        if (i == -1) {
            return true;
        }
        char c = (char) i;
        if (c == secondNewLineChar) {
            return true;
        } else {
            bufferedReader.reset();
            return false;
        }
    }

    private boolean readAheadQuote() throws IOException {
        bufferedReader.mark(1);
        int i = bufferedReader.read();
        if (i == -1) {
            return false;
        }
        char c = (char) i;
        if (c == '\"') {
            return true;
        } else {
            bufferedReader.reset();
            return false;
        }
    }
}
