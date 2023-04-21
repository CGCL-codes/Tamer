package it.enricod.jcontextfree.engine.sablecc.node;

import it.enricod.jcontextfree.engine.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TStrictfp extends Token {

    public TStrictfp() {
        super.setText("strictfp");
    }

    public TStrictfp(int line, int pos) {
        super.setText("strictfp");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TStrictfp(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTStrictfp(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TStrictfp text.");
    }
}
