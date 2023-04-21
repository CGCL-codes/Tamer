package it.enricod.jcontextfree.engine.sablecc.node;

import it.enricod.jcontextfree.engine.sablecc.analysis.*;

@SuppressWarnings("nls")
public final class TRule extends Token {

    public TRule() {
        super.setText("rule");
    }

    public TRule(int line, int pos) {
        super.setText("rule");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TRule(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTRule(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TRule text.");
    }
}
