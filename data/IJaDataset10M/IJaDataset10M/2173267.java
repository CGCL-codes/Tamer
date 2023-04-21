package comp.logo.node;

import comp.logo.analysis.*;

@SuppressWarnings("nls")
public final class TGoto extends Token {

    public TGoto() {
        super.setText("GOTO");
    }

    public TGoto(int line, int pos) {
        super.setText("GOTO");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TGoto(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTGoto(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TGoto text.");
    }
}
