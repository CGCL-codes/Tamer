package comp.logo.node;

import comp.logo.analysis.*;

@SuppressWarnings("nls")
public final class TLeft extends Token {

    public TLeft() {
        super.setText("LEFT");
    }

    public TLeft(int line, int pos) {
        super.setText("LEFT");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TLeft(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLeft(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TLeft text.");
    }
}
