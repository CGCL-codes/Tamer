package iwork.patchpanel.node;

import iwork.patchpanel.analysis.*;

public final class TTan extends Token {

    public TTan() {
        super.setText("tan");
    }

    public TTan(int line, int pos) {
        super.setText("tan");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TTan(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTTan(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TTan text.");
    }
}
