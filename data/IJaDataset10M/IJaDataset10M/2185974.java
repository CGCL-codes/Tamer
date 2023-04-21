package saf.node;

import saf.analysis.*;

@SuppressWarnings("nls")
public final class TLeftBracket extends Token {

    public TLeftBracket() {
        super.setText("[");
    }

    public TLeftBracket(int line, int pos) {
        super.setText("[");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TLeftBracket(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLeftBracket(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TLeftBracket text.");
    }
}
