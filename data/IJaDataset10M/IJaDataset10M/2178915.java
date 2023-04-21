package it.enricod.jcontextfree.engine.sablecc.node;

import it.enricod.jcontextfree.engine.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TY extends Token {

    public TY() {
        super.setText("y");
    }

    public TY(int line, int pos) {
        super.setText("y");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TY(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTY(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TY text.");
    }
}
