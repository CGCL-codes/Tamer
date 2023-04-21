package tools;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * A somewhat tolerant parser for CSV files.
 * @author Ronny.Standtke@gmx.net
 */
public final class CsvParser {

    private CsvParser() {
    }

    /**
     * parses a CSV grammar inputstream
     *
     * CSV grammar:
     * ------------
     * csvFile ::= (csvLine)* [csvStringList] 'EOF'
     * csvLine ::= csvStringList newLine
     * newLine ::= '\n' | '\r' | '\r\n'
     * csvStringList ::= csvString (',' csvString)*
     * csvString := whitespace* [csvField whitespace*]
     * whitespace ::= ' ' | '\t'
     * csvField ::= simpleField | quotedField
     * simpleField ::= (any char except newLine, EOF, \t, space, comma or double quote)+
     * quotedField ::= '"' subField ('"' '"' subField)* '"'
     * subField ::= (any char except double quote or EOF)+
     *
     * @param reader the character stream where to read the CSV data from
     * @throws IOException if an Exception with IO or CSV syntax is thrown
     * @return a list of CSV lines that in turn contain a list of strings
     */
    public static List<List<String>> parseCsvFile(Reader reader) throws IOException {
        List<List<String>> csvFile = new ArrayList<List<String>>();
        PushbackReader pushbackReader = new PushbackReader(reader, 2);
        int bomTest = pushbackReader.read();
        if (bomTest != '﻿') {
            pushbackReader.unread(bomTest);
        }
        for (int lineCounter = 1, currentChar = pushbackReader.read(); currentChar != -1; ) {
            pushbackReader.unread(currentChar);
            List<String> csvLine = parseCsvLine(pushbackReader, lineCounter);
            csvFile.add(csvLine);
            lineCounter++;
            currentChar = pushbackReader.read();
        }
        return csvFile;
    }

    private static List<String> parseCsvLine(PushbackReader pushbackReader, int lineCounter) throws IOException {
        List<String> csvLine = new ArrayList<String>();
        int column = 1;
        while (true) {
            String csvString = parseCsvString(pushbackReader, lineCounter);
            csvLine.add(csvString);
            if (endOfLineFollows(pushbackReader)) {
                consumeEndOfLine(pushbackReader);
                break;
            }
            int currentChar = pushbackReader.read();
            if (currentChar == -1) {
                break;
            }
            if (currentChar != ',') {
                throw new IOException("missing comma after column " + column + " of line " + lineCounter + " (found character \"" + (char) currentChar + "\" == " + currentChar + ")");
            }
            column++;
        }
        return csvLine;
    }

    private static String parseCsvString(PushbackReader pushbackReader, int lineCounter) throws IOException {
        consumeOptionalWhiteSpaces(pushbackReader);
        if (endOfLineFollows(pushbackReader)) {
            return null;
        }
        int currentChar = pushbackReader.read();
        if (currentChar == ',') {
            pushbackReader.unread(currentChar);
            return null;
        }
        String csvString = null;
        if (currentChar == '"') {
            csvString = parseQuotedCsvString(pushbackReader, lineCounter);
        } else {
            csvString = parseSimpleCsvString(currentChar, pushbackReader);
        }
        consumeOptionalWhiteSpaces(pushbackReader);
        return csvString;
    }

    private static String parseSimpleCsvString(int currentChar, PushbackReader pushbackReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            stringBuilder.append((char) currentChar);
            if (endOfLineFollows(pushbackReader)) {
                break;
            }
            currentChar = pushbackReader.read();
            if (currentChar == ',') {
                pushbackReader.unread(currentChar);
                break;
            }
            if (currentChar == -1) {
                break;
            }
        }
        return stringBuilder.toString().trim();
    }

    private static String parseQuotedCsvString(PushbackReader pushbackReader, int lineCounter) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            if (endOfLineFollows(pushbackReader)) {
                consumeEndOfLine(pushbackReader);
                stringBuilder.append("\n");
            }
            int currentChar = pushbackReader.read();
            if (currentChar == -1) {
                break;
            }
            if (currentChar == '"') {
                currentChar = pushbackReader.read();
                if (currentChar == '"') {
                    stringBuilder.append((char) currentChar);
                } else {
                    if (currentChar != -1) {
                        pushbackReader.unread(currentChar);
                    }
                    pushbackReader.unread('"');
                    break;
                }
            } else {
                stringBuilder.append((char) currentChar);
            }
        }
        int currentChar = pushbackReader.read();
        if (currentChar != '"') {
            throw new IOException("missing terminating double quote in CSV " + "line " + lineCounter);
        }
        return stringBuilder.toString();
    }

    private static void consumeOptionalWhiteSpaces(PushbackReader pushbackReader) throws IOException {
        int currentChar = pushbackReader.read();
        while (isSpace(currentChar)) {
            currentChar = pushbackReader.read();
        }
        if (currentChar != -1) {
            pushbackReader.unread(currentChar);
        }
    }

    private static boolean endOfLineFollows(PushbackReader pushbackReader) throws IOException {
        int currentChar = pushbackReader.read();
        if (currentChar != -1) {
            pushbackReader.unread(currentChar);
        }
        boolean endOfLineFollows = currentChar == '\n' || currentChar == '\r';
        return endOfLineFollows;
    }

    private static void consumeEndOfLine(PushbackReader pushbackReader) throws IOException {
        int currentChar = pushbackReader.read();
        if (currentChar == '\n') {
        } else if (currentChar == '\r') {
            currentChar = pushbackReader.read();
            if (currentChar != '\n') {
                pushbackReader.unread(currentChar);
            } else {
            }
        } else {
            throw new IOException("there was no newline here");
        }
    }

    private static boolean isSpace(int currentChar) {
        return (currentChar == ' ' || currentChar == '\t');
    }
}
