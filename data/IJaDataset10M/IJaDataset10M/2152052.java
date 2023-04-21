package galoot.node;

import galoot.analysis.*;

@SuppressWarnings("nls")
public final class TExtendsStart extends Token {

    public TExtendsStart(String text) {
        setText(text);
    }

    public TExtendsStart(String text, int line, int pos) {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TExtendsStart(getText(), getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTExtendsStart(this);
    }
}
