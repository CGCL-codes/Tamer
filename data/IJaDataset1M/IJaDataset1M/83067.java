package org.skunk.swing.text.syntax;

public class Token {

    public int offset = 0;

    public int length = 1;

    public String styleName = null;

    public Token nextToken = null;

    public Token(int offset, int length, String styleName) {
        this.offset = offset;
        this.length = length;
        this.styleName = styleName;
    }

    public String toString() {
        return new StringBuffer().append(this.getClass()).append('[').append("offset: ").append(offset).append(", length: ").append(length).append(", style: ").append(styleName).append(", nextToken: ").append(nextToken).append(']').toString();
    }

    public String getStyleName() {
        return this.styleName;
    }

    public Token getNextToken() {
        return this.nextToken;
    }

    public void setNextToken(Token nextToken) {
        this.nextToken = nextToken;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLength() {
        return this.length;
    }
}
