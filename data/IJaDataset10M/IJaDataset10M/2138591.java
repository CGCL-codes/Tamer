package net.sourceforge.transmogrify.symtab.parser;

public class Span {

    private int _startLine;

    private int _startColumn;

    private int _endLine;

    private int _endColumn;

    public Span() {
    }

    public Span(int startLine, int startColumn, int endLine, int endColumn) {
        setStart(startLine, startColumn);
        setEnd(endLine, endColumn);
    }

    public Span(Span span) {
        this(span.getStartLine(), span.getStartColumn(), span.getEndLine(), span.getEndColumn());
    }

    public void setStart(int startLine, int startColumn) {
        _startLine = startLine;
        _startColumn = startColumn;
    }

    public void setEnd(int endLine, int endColumn) {
        _endLine = endLine;
        _endColumn = endColumn;
    }

    public int getStartLine() {
        return _startLine;
    }

    public int getStartColumn() {
        return _startColumn;
    }

    public int getEndLine() {
        return _endLine;
    }

    public int getEndColumn() {
        return _endColumn;
    }

    public boolean contains(Span span) {
        return (contains(span.getStartLine(), span.getStartColumn()) && contains(span.getEndLine(), span.getEndColumn()));
    }

    public boolean contains(int line, int column) {
        boolean afterStart = false;
        boolean beforeEnd = false;
        if (getStartLine() < line) {
            afterStart = true;
        } else if (getStartLine() == line && getStartColumn() <= column) {
            afterStart = true;
        }
        if (getEndLine() > line) {
            beforeEnd = true;
        } else if (getEndLine() == line && getEndColumn() >= column) {
            beforeEnd = true;
        }
        return (afterStart && beforeEnd);
    }

    protected boolean startsBefore(Span span) {
        boolean result = false;
        if (getStartLine() < span.getStartLine()) {
            result = true;
        } else if (getStartLine() == span.getStartLine() && getStartColumn() <= span.getStartColumn()) {
            result = true;
        }
        return result;
    }

    protected boolean endsAfter(Span span) {
        boolean result = false;
        if (getEndLine() > span.getEndLine()) {
            result = true;
        } else if (getEndLine() == span.getEndLine() && getEndColumn() >= span.getEndColumn()) {
            result = true;
        }
        return result;
    }

    public void compose(Span span) {
        if (span.startsBefore(this)) {
            setStart(span.getStartLine(), span.getStartColumn());
        }
        if (span.endsAfter(this)) {
            setEnd(span.getEndLine(), span.getEndColumn());
        }
    }

    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof Span) {
            Span span = (Span) o;
            result = (span.getStartLine() == getStartLine() && span.getStartColumn() == getStartColumn() && span.getEndLine() == getEndLine() && span.getEndColumn() == getEndColumn());
        }
        return result;
    }

    public String toString() {
        return "[" + getStartLine() + "," + getStartColumn() + ":" + getEndLine() + "," + getEndColumn() + "]";
    }
}
