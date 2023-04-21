package iwork.patchpanel.manager.script.node;

import iwork.patchpanel.manager.script.analysis.*;

public final class TFormal extends Token {

    public TFormal() {
        super.setText("FORMAL");
    }

    public TFormal(int line, int pos) {
        super.setText("FORMAL");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TFormal(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTFormal(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TFormal text.");
    }
}
