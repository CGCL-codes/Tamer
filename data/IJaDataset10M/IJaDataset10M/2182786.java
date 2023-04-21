package org.freeform.tokens;

import java.io.IOException;
import java.util.ArrayList;

public class IteratorTokens implements Tokens {

    private ArrayList<TokenDetails> details;

    private ArrayList<String> tokens;

    private TokenIterator i;

    public IteratorTokens(TokenIterator i) {
        details = new ArrayList<TokenDetails>();
        tokens = new ArrayList<String>();
        this.i = i;
    }

    public void prepare(int i) throws IOException {
        while (i >= tokens.size() && this.i.hasNext()) {
            details.add(this.i.peekDetails());
            tokens.add(this.i.next());
        }
    }

    public TokenDetails details(int i) throws IOException {
        prepare(i);
        return (i >= 0 && i < details.size()) ? details.get(i) : null;
    }

    public String get(int i) throws IOException {
        prepare(i);
        return (i >= 0 && i < tokens.size()) ? tokens.get(i) : null;
    }
}
