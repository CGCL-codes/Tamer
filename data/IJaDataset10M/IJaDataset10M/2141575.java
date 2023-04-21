package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class TEndOfLineComment extends Token {

    public TEndOfLineComment(String text) {
        setText(text);
    }

    public TEndOfLineComment(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TEndOfLineComment(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTEndOfLineComment(this);
    }
}
