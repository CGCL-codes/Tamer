package galoot.node;

import galoot.analysis.*;

@SuppressWarnings("nls")
public final class TTemplatetag extends Token {

    public TTemplatetag(String text) {
        setText(text);
    }

    public TTemplatetag(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TTemplatetag(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTTemplatetag(this);
    }
}
