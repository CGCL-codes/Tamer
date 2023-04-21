package net.sourceforge.jdefprog.mcl.generated.node;

import net.sourceforge.jdefprog.mcl.generated.analysis.*;

@SuppressWarnings("nls")
public final class TIdent extends Token {

    public TIdent(String text) {
        setText(text);
    }

    public TIdent(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TIdent(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTIdent(this);
    }
}
