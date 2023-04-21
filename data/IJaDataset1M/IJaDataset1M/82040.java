package iwork.patchpanel.node;

import iwork.patchpanel.analysis.*;

public final class TSin extends Token {

    public TSin() {
        super.setText("sin");
    }

    public TSin(int line, int pos) {
        super.setText("sin");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TSin(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTSin(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TSin text.");
    }
}
