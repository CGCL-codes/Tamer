package de.chdev.artools.sql.utils;

/**
 * @author Christoph Heinig
 * 
 */
public class Statement {

    private String statement = null;

    private String text = null;

    private int startOffset;

    private int endOffset;

    private int startStatementOffset;

    private int endStatementOffset;

    @Override
    public String toString() {
        return getStatement();
    }

    public String getStatement() {
        String stmt = statement.trim();
        return statement.trim();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        if (statement == null) {
            statement = text;
        }
    }

    public void setStatement(String statement) {
        this.statement = statement;
        if (text == null) {
            text = statement;
        }
    }

    /**
     * @param startOffset
     *            the startOffset to set
     */
    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    /**
     * @return the startOffset
     */
    public int getStartOffset() {
        return startOffset;
    }

    /**
     * @param endOffset
     *            the endOffset to set
     */
    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    /**
     * @return the endOffset
     */
    public int getEndOffset() {
        return endOffset;
    }

    public int getStartStatementOffset() {
        return startStatementOffset;
    }

    public int getEndStatementOffset() {
        return endStatementOffset;
    }

    /**
     * @param startStatementOffset
     *            the startStatementOffset to set
     */
    public void setStartStatementOffset(int startStatementOffset) {
        this.startStatementOffset = startStatementOffset;
    }

    /**
     * @param endStatementOffset
     *            the endStatementOffset to set
     */
    public void setEndStatementOffset(int endStatementOffset) {
        this.endStatementOffset = endStatementOffset;
    }
}
