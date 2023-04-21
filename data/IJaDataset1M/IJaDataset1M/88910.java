package net.sf.k_automaton.hypoths;

/**
 *
 * @author Dmitri
 */
public class NoHypothsException extends Exception {

    private int lineNumber;

    private int columnNumber;

    public NoHypothsException(String message, int lineNumber, int columnNumber) {
        super(message);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
