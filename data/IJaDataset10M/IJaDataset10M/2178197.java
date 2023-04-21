package org.easyb.parser;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class EasybSnippet {

    private String specificationPath;

    public final Coordinate start;

    public final Coordinate end;

    private static String NEW_LINE = System.getProperty("line.separator");

    public EasybSnippet(String specificationPath, Coordinate start, Coordinate end) {
        this.specificationPath = specificationPath;
        this.start = start;
        this.end = end;
    }

    public String toString() {
        return specificationPath + " (" + start + " to " + end + ")";
    }

    public String getText() throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(specificationPath)));
        for (int i = 1; i < end.line; i++) {
            builder.append(reader.readLine()).append(NEW_LINE);
        }
        return builder.toString();
    }

    static class Coordinate {

        public final int line;

        public final int column;

        public static final Coordinate EOF = new Coordinate(-1, -1);

        public Coordinate(int line, int column) {
            this.line = line;
            this.column = column;
        }

        public static Coordinate createCoordinate(int line, int column) {
            return new Coordinate(line, column);
        }

        public String toString() {
            return line + ":" + column;
        }
    }
}
