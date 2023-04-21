package org.unitils.dbmaintainer.util;

import java.util.ArrayList;
import java.util.List;

/**
 * todo javadoc
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public abstract class BaseScriptParser {

    protected enum SCRIPT_STATE {

        /**
         * The normal, not in quotes, not in comment state.
         */
        NORMAL, /**
         * The in an in-line comment (-- comment) state.
         */
        IN_LINE_COMMENT, /**
         * The in a block comment (/ * comment * /) state.
         */
        IN_BLOCK_COMMENT, /**
         * The in single quotes ('text') state.
         */
        IN_SINGLE_QUOTES, /**
         * The in double quotes ("text") state.
         */
        IN_DOUBLE_QUOTES
    }

    /**
     * The current state
     */
    protected SCRIPT_STATE state = SCRIPT_STATE.NORMAL;

    /**
     * Character that indicates the end of a statement
     */
    protected boolean preserveComments;

    protected boolean preserveNewLines;

    public BaseScriptParser(boolean preserveComments, boolean preserveNewLines) {
        this.preserveComments = preserveComments;
        this.preserveNewLines = preserveNewLines;
    }

    /**
     * Parses all statements out of the given script as described in the class javadoc.
     *
     * @param script the script, not null
     * @return the statements, not null
     */
    public List<String> parseStatements(String script) {
        List<String> statements = new ArrayList<String>();
        StatementBuilder statementBuilder = new StatementBuilder();
        char[] chars = script.toCharArray();
        int currentIndexInScript = 0;
        while (currentIndexInScript < chars.length) {
            int nbOfCharsProcessed = handleNextChar(chars, currentIndexInScript, statementBuilder, statements);
            currentIndexInScript += nbOfCharsProcessed;
        }
        return statements;
    }

    /**
     * Handles a char by delegating it to the method corresponding to the current state.
     *
     * @param script               the comlete script as a char array
     * @param currentIndexInScript
     * @param statementBuilder     the current statement buffer, not null
     * @param statements           the statement list, not null
     * @return true if the next char should be skipped
     */
    protected int handleNextChar(char[] script, int currentIndexInScript, StatementBuilder statementBuilder, List<String> statements) {
        switch(state) {
            case IN_LINE_COMMENT:
                return handleCharInLineComment(script, currentIndexInScript, statementBuilder, statements);
            case IN_BLOCK_COMMENT:
                return handleCharInBlockComment(script, currentIndexInScript, statementBuilder, statements);
            case IN_SINGLE_QUOTES:
                return handleCharInSingleQuotes(script, currentIndexInScript, statementBuilder, statements);
            case IN_DOUBLE_QUOTES:
                return handleCharInDoubleQuotes(script, currentIndexInScript, statementBuilder, statements);
            default:
                return handleCharNormal(script, currentIndexInScript, statementBuilder, statements);
        }
    }

    /**
     * Handles a char in the normal (not quotedOrEmpty, not commented) state. Checks for the beginning of a
     * line comment (-- comment), block comment (/ * comment * /), quotedOrEmpty text ('text') and double
     * quotedOrEmpty text ("text) and changes the state correspondingly. It also checks for the ending of
     * statements by a ;. If a statement is ended the it is trimmed and added to the statement list ( ; not included).
     * New line chars (\n and \r) will be replaced by a single space.
     *
     * @param script     the comlete script as a char array
     * @param currentIndexInScript The current character index in the given script
     * @param statementBuilder The object used as buffer for assembling the statement
     * @param statements the statement list, not null
     * @return true if the next char should be skipped
     */
    public int handleCharNormal(char[] script, int currentIndexInScript, StatementBuilder statementBuilder, List<String> statements) {
        if (getCurrentChar(script, currentIndexInScript) == '-' && getNextChar(script, currentIndexInScript) == '-') {
            statementBuilder.addCodeChar('-');
            state = SCRIPT_STATE.IN_LINE_COMMENT;
            return 1;
        }
        if (getCurrentChar(script, currentIndexInScript) == '/' && getNextChar(script, currentIndexInScript) == '*') {
            statementBuilder.addCodeChar('/');
            state = SCRIPT_STATE.IN_BLOCK_COMMENT;
            return 1;
        }
        if (getCurrentChar(script, currentIndexInScript) == '\n' && getNextChar(script, currentIndexInScript) == '\r' || getCurrentChar(script, currentIndexInScript) == '\r' && getNextChar(script, currentIndexInScript) == '\n') {
            statementBuilder.addNewLine('\n');
            return 2;
        }
        if (getCurrentChar(script, currentIndexInScript) == '\n' || getCurrentChar(script, currentIndexInScript) == '\r') {
            statementBuilder.addNewLine('\n');
            return 1;
        }
        if (getCurrentChar(script, currentIndexInScript) == '\\') {
            statementBuilder.addCodeChar(getCurrentChar(script, currentIndexInScript));
            statementBuilder.addCodeChar(getNextChar(script, currentIndexInScript));
            return 2;
        }
        if (reachedEndOfStatement(script, currentIndexInScript, statementBuilder, statements)) {
            statements.add(statementBuilder.getStatement().trim());
            statementBuilder.resetStatement();
            return 1;
        }
        if (getCurrentChar(script, currentIndexInScript) == '\'') {
            state = SCRIPT_STATE.IN_SINGLE_QUOTES;
            statementBuilder.addCodeChar(getCurrentChar(script, currentIndexInScript));
            return 1;
        } else if (getCurrentChar(script, currentIndexInScript) == '"') {
            state = SCRIPT_STATE.IN_DOUBLE_QUOTES;
            statementBuilder.addCodeChar(getCurrentChar(script, currentIndexInScript));
            return 1;
        }
        statementBuilder.addCodeChar(getCurrentChar(script, currentIndexInScript));
        return 1;
    }

    /**
     * @param script The script that is processed
     * @param currentIndexInScript The current character index in the given script
     * @param statementBuilder
     * @param statements
     * @return true if the end of the current statement has been reached, false otherwise
     */
    protected abstract boolean reachedEndOfStatement(char[] script, int currentIndexInScript, StatementBuilder statementBuilder, List<String> statements);

    /**
     * Handles a char in a line comment (-- comment). Looks for an end of line to end the comment. Skips all other chars.

     * @param script The script that is processed, not null
     * @param currentIndexInScript The current character index in the given script 
     * @param statementBuilder the current statement builder, not null
     * @param statements       the statement list, not null
     * @return true if the next char should be skipped
     */
    protected int handleCharInLineComment(char[] script, int currentIndexInScript, StatementBuilder statementBuilder, List<String> statements) {
        if (getCurrentChar(script, currentIndexInScript) == '\n' || getCurrentChar(script, currentIndexInScript) == '\r') {
            statementBuilder.addNewLine(getCurrentChar(script, currentIndexInScript));
            state = SCRIPT_STATE.NORMAL;
            return 1;
        } else {
            statementBuilder.addCommentChar(getCurrentChar(script, currentIndexInScript));
            return 1;
        }
    }

    /**
     * Handles char in a block comment. Checks for the ending of the comment * followed by /. Skips all other chars.
     
     * @param script  The script that is processed, not null
     * @param currentIndexInScript The current character index in the given script 
     * @param statementBuilder the current statement builder, not null
     * @param statements       the statement list, not null
     * @return true if the next char should be skipped
     */
    protected int handleCharInBlockComment(char[] script, int currentIndexInScript, StatementBuilder statementBuilder, List<String> statements) {
        if (getCurrentChar(script, currentIndexInScript) == '*' && getNextChar(script, currentIndexInScript) == '/') {
            statementBuilder.addCommentChar('*');
            statementBuilder.addCommentChar('/');
            state = SCRIPT_STATE.NORMAL;
            return 2;
        } else {
            statementBuilder.addCommentChar(getCurrentChar(script, currentIndexInScript));
            return 1;
        }
    }

    /**
     * Handles a char in a quotedOrEmpty literal ('text'). Checks for the ending quote, but ignores escaped quotes. All
     * chars, including newlines (\n \r), are appended to the statement.
     * 
     * @param script 
     * @param currentIndexInScript The current character index in the given script 
     * @param statementBuilder 
     * @param statements the statement list, not null
     * @return true if the next char should be skipped
     */
    public int handleCharInSingleQuotes(char[] script, int currentIndexInScript, StatementBuilder statementBuilder, List<String> statements) {
        if (getCurrentChar(script, currentIndexInScript) == '\\' || (getCurrentChar(script, currentIndexInScript) == '\'' && getNextChar(script, currentIndexInScript) == '\'')) {
            statementBuilder.addCodeChar(getCurrentChar(script, currentIndexInScript));
            statementBuilder.addCodeChar(getNextChar(script, currentIndexInScript));
            return 2;
        }
        if (getCurrentChar(script, currentIndexInScript) == '\'') {
            state = SCRIPT_STATE.NORMAL;
        }
        statementBuilder.addCodeChar(getCurrentChar(script, currentIndexInScript));
        return 1;
    }

    /**
     * Handles a char in a double quotedOrEmpty string ("text"). Checks for the ending double quote, but ignores escaped
     * double quotes. All chars, including newlines (\n \r), are appended to the statement.
     * 
     * @param script The script that is processed
     * @param currentIndexInScript The current character index in the given script 
     * @param statementBuilder 
     * @param statements the statement list, not null
     * @return true if the next char should be skipped
     */
    public int handleCharInDoubleQuotes(char[] script, int currentIndexInScript, StatementBuilder statementBuilder, List<String> statements) {
        if (getCurrentChar(script, currentIndexInScript) == '\\' || (getCurrentChar(script, currentIndexInScript) == '"' && getNextChar(script, currentIndexInScript) == '"')) {
            statementBuilder.addCodeChar(getCurrentChar(script, currentIndexInScript));
            statementBuilder.addCodeChar(getNextChar(script, currentIndexInScript));
            return 2;
        }
        if (getCurrentChar(script, currentIndexInScript) == '"') {
            state = SCRIPT_STATE.NORMAL;
        }
        statementBuilder.addCodeChar(getCurrentChar(script, currentIndexInScript));
        return 1;
    }

    protected char getCurrentChar(char[] script, int currentIndexInScript) {
        return script[currentIndexInScript];
    }

    protected char getNextChar(char[] script, int currentIndexInScript) {
        if (currentIndexInScript + 1 >= script.length) {
            return ' ';
        } else {
            return script[currentIndexInScript + 1];
        }
    }

    protected class StatementBuilder {

        private StringBuffer statement;

        public StatementBuilder() {
            resetStatement();
        }

        public void addCommentChar(char commentChar) {
            if (preserveComments) {
                statement.append(commentChar);
            }
        }

        public void addCodeChar(char codeChar) {
            statement.append(codeChar);
        }

        public void addNewLine(char newLineChar) {
            if (preserveNewLines) {
                statement.append(newLineChar);
            } else {
                if (statement.length() > 0 && statement.charAt(statement.length() - 1) != ' ') {
                    statement.append(' ');
                }
            }
        }

        public char getLastChar() {
            return statement.charAt(statement.length() - 1);
        }

        public String getStatement() {
            return statement.toString();
        }

        public void resetStatement() {
            statement = new StringBuffer();
        }
    }
}
