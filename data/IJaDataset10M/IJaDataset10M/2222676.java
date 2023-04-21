package it.enricod.jcontextfree.engine.sablecc.node;

import it.enricod.jcontextfree.engine.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TTransient extends Token {

    public TTransient() {
        super.setText("transient");
    }

    public TTransient(int line, int pos) {
        super.setText("transient");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TTransient(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTTransient(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TTransient text.");
    }
}
