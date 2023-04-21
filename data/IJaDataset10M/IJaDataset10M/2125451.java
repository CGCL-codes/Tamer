package com.safi.workshop.sqlexplorer.parsers;

import com.safi.workshop.sqlexplorer.util.BackedCharSequence;

/**
 * Tokenizer is a utility class for reading and tokenising a string; note that this is not
 * restricted to SQL - it is also used for tokenising structured comments.
 * 
 * @author John Spackman
 */
public class Tokenizer {

    public enum TokenType {

        WORD, NUMBER, QUOTED, PUNCTUATION, EOL_COMMENT, ML_COMMENT
    }

    ;

    public static class Token extends BackedCharSequence {

        private TokenType tokenType;

        private int lineNo;

        private int charNo;

        public Token(StringBuffer buffer, TokenType tokenType, int start, int end, int lineNo, int charNo) {
            super(buffer, start, end);
            if (tokenType == null) throw new IllegalArgumentException();
            this.tokenType = tokenType;
            this.lineNo = lineNo;
            this.charNo = charNo;
        }

        /**
     * @return the tokenType
     */
        public TokenType getTokenType() {
            return tokenType;
        }

        /**
     * @return the lineNo
     */
        public int getLineNo() {
            return lineNo;
        }

        /**
     * @return the charNo
     */
        public int getCharNo() {
            return charNo;
        }

        /**
     * Returns the value without quotes, if applicable
     * 
     * @return
     */
        public CharSequence getUnquotedValue() {
            if (tokenType == TokenType.QUOTED) return new BackedCharSequence(buffer, start + 1, end - 1);
            return this;
        }
    }

    private StringBuffer sql;

    private int nextToken;

    private int initialLineNo;

    private int lineNo;

    private int charNo;

    /**
   * Constructor; the tokenizer will work on sql. If sql is a StringBuffer it will use it
   * as is, otherwise it will duplicate it into its own StringBuffer
   * 
   * @param sql
   */
    public Tokenizer(CharSequence sql) {
        super();
        if (sql instanceof StringBuffer) this.sql = (StringBuffer) sql; else this.sql = new StringBuffer(sql);
        lineNo = initialLineNo = 1;
        charNo = 0;
    }

    /**
   * Resets the tokenizer to the start of the buffer
   */
    public void reset() {
        nextToken = 0;
        lineNo = initialLineNo;
        charNo = 0;
    }

    /**
   * Gets the remaining, untokenized part of the string
   * 
   * @return
   */
    public BackedCharSequence getRemainder() {
        return new BackedCharSequence(sql, nextToken, sql.length());
    }

    /**
   * Generates a token which consists of everything from the current position up to and
   * including the end of line character
   * 
   * @return
   */
    public Token skipToEOL() {
        int start = nextToken;
        for (; nextToken < sql.length(); nextToken++) {
            char c = sql.charAt(nextToken);
            if (c == '\n') {
                lineNo++;
                charNo = 1;
                break;
            }
        }
        return new Token(sql, TokenType.WORD, start, nextToken, lineNo, charNo);
    }

    /**
   * Scans looking for the next token, returning null if there are no more. Whitespace is
   * not a token, but comments are.
   * 
   * @return
   */
    public Token nextToken() throws ParserException {
        TokenType tokenType = null;
        char currentQuote = 0;
        int startCharNo = charNo + 1;
        int startLineNo = lineNo;
        int start = nextToken;
        char c = 0;
        char nextC = 0;
        for (; nextToken < sql.length(); nextToken++) {
            c = sql.charAt(nextToken);
            if (c == '\n') {
                lineNo++;
                charNo = 0;
                if (tokenType == null) {
                    startCharNo = charNo + 1;
                    startLineNo = lineNo;
                }
            }
            charNo++;
            TokenType nextType = null;
            if (c == '\'' || c == '\"') {
                if (tokenType == TokenType.EOL_COMMENT || tokenType == TokenType.ML_COMMENT) continue;
                if (tokenType != TokenType.QUOTED && tokenType != null) break;
                if (tokenType == null) {
                    tokenType = TokenType.QUOTED;
                    currentQuote = c;
                    continue;
                } else if (tokenType == TokenType.QUOTED) {
                    if (c != currentQuote) continue;
                    if (nextToken < sql.length() - 1 && sql.charAt(nextToken + 1) == currentQuote) {
                        nextToken++;
                        charNo++;
                        continue;
                    }
                    nextToken++;
                    charNo++;
                    currentQuote = 0;
                    break;
                }
            }
            if (tokenType == TokenType.QUOTED) continue;
            if (tokenType == TokenType.EOL_COMMENT) {
                if (c == '\n') break;
                continue;
            }
            if (nextToken < sql.length() - 1) {
                nextC = sql.charAt(nextToken + 1);
                if (tokenType == TokenType.ML_COMMENT) {
                    if (c == '*' && nextC == '/') {
                        nextToken += 2;
                        charNo += 2;
                        break;
                    }
                    continue;
                }
                if ((c == '-' && nextC == '-') || (c == '/' && nextC == '/')) nextType = TokenType.EOL_COMMENT; else if (c == '/' && nextC == '*') nextType = TokenType.ML_COMMENT;
                if (nextType != null) {
                    if (tokenType == null) {
                        tokenType = nextType;
                        continue;
                    }
                    break;
                }
            } else nextC = 0;
            if (tokenType == TokenType.WORD) {
                if (!isIdentifier(c)) break;
                continue;
            }
            if (tokenType == null && isFirstIdentifier(c)) {
                tokenType = TokenType.WORD;
                continue;
            }
            if (Character.isDigit(c)) {
                if (tokenType == null || (tokenType == TokenType.PUNCTUATION && sql.substring(start, nextToken).equals("."))) {
                    tokenType = TokenType.NUMBER;
                    continue;
                } else if (tokenType == TokenType.NUMBER) continue;
                break;
            } else if (c == '.' && tokenType == TokenType.NUMBER) {
                continue;
            }
            if (isIdentifier(c) && tokenType != TokenType.WORD) break;
            if (Character.isWhitespace(c)) {
                if (tokenType != null) break;
                start++;
                continue;
            }
            if (tokenType == TokenType.PUNCTUATION) break;
            if (tokenType != null && tokenType != TokenType.PUNCTUATION) break;
            if (tokenType == null) tokenType = TokenType.PUNCTUATION;
        }
        if (currentQuote != 0) throw new ParserException("Unterminated string literal", startLineNo, startCharNo);
        if (tokenType == TokenType.ML_COMMENT && (c != '*' | nextC != '/')) throw new ParserException("Unterminated multi-line comment", startLineNo, startCharNo);
        if (tokenType == null) {
            if (nextToken < sql.length()) throw new RuntimeException("Internal error: could not find a token but buffer is not exhausted");
            return null;
        } else if (c == '\n') {
            lineNo--;
        }
        return new Token(sql, tokenType, start, nextToken, startLineNo, startCharNo);
    }

    /**
   * Returns true if c is suitable as the first character of an identifier; it must be a
   * character or an underscore
   * 
   * @param c
   * @return true if its suitable as an identifier
   */
    private boolean isFirstIdentifier(char c) {
        return Character.isLetter(c) || c == '_';
    }

    /**
   * Returns true if c is suitable a subsequent character of an identifier; it must be a
   * character or an underscore or digits
   * 
   * @param c
   * @return
   */
    private boolean isIdentifier(char c) {
        return Character.isDigit(c) || isFirstIdentifier(c);
    }

    /**
   * Sets the inital line number - the line number that the first line of text is
   * percieved to be on
   * 
   * @param initialLineNo
   */
    public void setInitialLineNo(int initialLineNo) {
        this.initialLineNo = initialLineNo;
        this.lineNo = initialLineNo;
    }
}
