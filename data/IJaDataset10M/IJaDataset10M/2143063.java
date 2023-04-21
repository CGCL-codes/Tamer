package net.sourceforge.jdefprog.mcl.generated.node;

import net.sourceforge.jdefprog.mcl.generated.analysis.*;

@SuppressWarnings("nls")
public final class TOpenParen extends Token {

    public TOpenParen() {
        super.setText("(");
    }

    public TOpenParen(int line, int pos) {
        super.setText("(");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TOpenParen(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTOpenParen(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TOpenParen text.");
    }
}
