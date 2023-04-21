package iwork.patchpanel.manager.script.node;

import iwork.patchpanel.manager.script.analysis.*;

public final class TEq extends Token {

    public TEq() {
        super.setText("==");
    }

    public TEq(int line, int pos) {
        super.setText("==");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TEq(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTEq(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TEq text.");
    }
}
