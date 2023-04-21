package edu.caece.langprocessor.lexical.tokens;

public class SimpleLexicalToken implements LexicalToken {

    protected String lexema;

    private Integer line;

    private Integer column;

    private final TokenType token;

    public SimpleLexicalToken(TokenType token, String lexema) {
        this.token = token;
        this.lexema = lexema;
    }

    public String getLexema() {
        return this.lexema;
    }

    public TokenType getToken() {
        return this.token;
    }

    public Integer getLine() {
        return this.line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Integer getColumn() {
        return this.column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEquals = false;
        if (obj instanceof LexicalToken) {
            LexicalToken other = (LexicalToken) obj;
            isEquals = this.lexema.equals(other.getLexema());
        }
        return isEquals;
    }

    @Override
    public int hashCode() {
        return this.token.hashCode();
    }

    @Override
    public String toString() {
        return this.lexema;
    }

    @Override
    public LexicalToken clone() {
        LexicalToken lt = null;
        try {
            lt = this.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return lt;
    }
}
