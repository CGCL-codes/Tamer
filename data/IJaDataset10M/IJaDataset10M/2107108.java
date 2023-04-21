package com.coury.dasle.parser;

import java.util.List;

public class LineParser {

    private Parser parser;

    private final List<String> lineParts;

    private int pos = 0;

    public LineParser(List<String> line) {
        super();
        this.lineParts = line;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public List<String> getLine() {
        return lineParts;
    }

    public boolean hasMore() {
        return pos < lineParts.size();
    }

    public String next() {
        return lineParts.get(pos++);
    }

    public boolean isParsed() {
        return parser != null;
    }

    public int getCurrentPosition() {
        return pos;
    }

    public void setCurrentPosition(int parserPos) {
        this.pos = parserPos;
    }
}
