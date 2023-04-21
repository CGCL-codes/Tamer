package iwork.patchpanel.node;

import iwork.patchpanel.analysis.*;

public final class TNeq extends Token {

    public TNeq() {
        super.setText("!=");
    }

    public TNeq(int line, int pos) {
        super.setText("!=");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TNeq(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTNeq(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TNeq text.");
    }
}
