package galoot.node;

import galoot.analysis.*;

@SuppressWarnings("nls")
public final class TIfeqEnd extends Token {

    public TIfeqEnd(String text) {
        setText(text);
    }

    public TIfeqEnd(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TIfeqEnd(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTIfeqEnd(this);
    }
}
