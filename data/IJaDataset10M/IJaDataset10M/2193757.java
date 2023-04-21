package iwork.patchpanel.node;

import iwork.patchpanel.analysis.*;

public final class TCeil extends Token {

    public TCeil() {
        super.setText("ceil");
    }

    public TCeil(int line, int pos) {
        super.setText("ceil");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TCeil(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTCeil(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TCeil text.");
    }
}
