package galoot.node;

import galoot.analysis.*;

@SuppressWarnings("nls")
public final class TKwNot extends Token {

    public TKwNot() {
        super.setText("not");
    }

    public TKwNot(int line, int pos) {
        super.setText("not");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TKwNot(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKwNot(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKwNot text.");
    }
}
