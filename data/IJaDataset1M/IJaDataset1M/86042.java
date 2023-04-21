package jp.seraph.sample.calculator;

public class Token {

    public Token(String aValue, TokenKind aKind) {
        mValue = aValue;
        mKind = aKind;
    }

    private String mValue;

    private TokenKind mKind;

    public String getValue() {
        return mValue;
    }

    public TokenKind getKind() {
        return mKind;
    }

    public int length() {
        return mValue.length();
    }
}
