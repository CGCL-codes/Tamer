package net.mattryall.smartquotes;

interface Token {

    String getValue();

    void accept(TokenVisitor visitor);
}
