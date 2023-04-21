package iwork.patchpanel.manager.script.node;

import iwork.patchpanel.manager.script.analysis.*;

public final class TMultiLineComment extends Token {

    public TMultiLineComment(String text) {
        setText(text);
    }

    public TMultiLineComment(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TMultiLineComment(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTMultiLineComment(this);
    }
}
