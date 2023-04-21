package org.sablecc.sablecc.syntax3.node;

import org.sablecc.sablecc.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TChar extends Token {

    public TChar(String text) {
        setText(text);
    }

    public TChar(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TChar(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTChar(this);
    }
}
