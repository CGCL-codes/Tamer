package com.gabysoft.edixml.edixml.traductor;

public final class ErrorContext {

    private final CircularQueue queue;

    private final EdiStack ediStack;

    protected ErrorContext() {
        queue = new CircularQueue();
        ediStack = new EdiStack();
    }

    protected void append(final char character) {
        queue.append(character);
    }

    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append(" when processing ");
        text.append(ediStack.toStringBuilder());
        text.append(' ');
        text.append(queue);
        return text.toString();
    }

    public EdiStack getEdiStack() {
        return ediStack;
    }
}
