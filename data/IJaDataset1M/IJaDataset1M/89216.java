package com.ak.fix.utils;

import quickfix.SessionID;

public class QuoteSessionIDStore {

    public String quoteString;

    public SessionID sessionID;

    public QuoteSessionIDStore(String quoteString, SessionID sessionID) {
        this.quoteString = quoteString;
        this.sessionID = sessionID;
    }
}
