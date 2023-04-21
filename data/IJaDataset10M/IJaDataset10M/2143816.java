package com.agentfactory.afapl2.compiler;

public class AFAPL2SyntaxError {

    String[] types = new String[] { "ERROR", "WARNING" };

    public static final int ERROR = 0;

    public static final int WARNING = 1;

    String source;

    String issue;

    String message;

    long line;

    int type;

    public AFAPL2SyntaxError(int type, String source, long line, String message) {
        this(type, source, line, message, null);
    }

    public AFAPL2SyntaxError(int type, String source, long line, String message, String issue) {
        this.type = type;
        this.source = source;
        this.line = line;
        this.message = message;
        this.issue = issue;
    }

    public String getMessage() {
        return message;
    }

    public long getLineNumber() {
        return line;
    }

    public String getIssue() {
        return issue;
    }

    @Override
    public String toString() {
        return source + ":" + line + ": " + message + "\n\t" + types[type] + ": " + issue + "\n";
    }
}
