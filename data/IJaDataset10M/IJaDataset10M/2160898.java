package org.sablecc.objectmacro.syntax3.node;

import org.sablecc.objectmacro.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TIdentifier extends Token {

    public TIdentifier(String text) {
        setText(text);
    }

    public TIdentifier(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TIdentifier(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTIdentifier(this);
    }
}
