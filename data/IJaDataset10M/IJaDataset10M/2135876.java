package it.rm.bracco.pipeline.structures;

import java.util.Iterator;
import java.util.Vector;

public class Passage {

    private Vector<Token> tokenVector;

    private int id;

    public Passage(int id) throws IllegalArgumentException {
        if (id >= 0) {
            this.id = id;
            tokenVector = new Vector<Token>();
        } else throw new IllegalArgumentException("The passage id MUST BE a not negative value!");
    }

    public void addToken(Token token) throws NullPointerException {
        if (token != null) tokenVector.add(token); else throw new NullPointerException("The token reference you are adding is null!");
    }

    public TokenReader getTokenReader() {
        return new TokenReader(tokenVector.iterator());
    }

    public int getID() {
        return id;
    }

    public void replaceToken(int id, Token newToken) throws IllegalArgumentException {
        if (id < tokenVector.size()) tokenVector.set(id, newToken); else throw new IllegalArgumentException("Token " + id + " does not exist.");
    }

    public String printPassage() {
        StringBuffer sb = new StringBuffer();
        Token t;
        for (Iterator<Token> iterator = tokenVector.iterator(); iterator.hasNext(); ) {
            t = iterator.next();
            sb.append(t.getToken() + " ");
        }
        return sb.toString();
    }

    public int numberOfTokens() {
        return tokenVector.size();
    }
}
