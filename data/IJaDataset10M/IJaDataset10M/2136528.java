package comp.logo.node;

import comp.logo.analysis.*;

@SuppressWarnings("nls")
public final class TSqrt extends Token {

    public TSqrt() {
        super.setText("SQRT");
    }

    public TSqrt(int line, int pos) {
        super.setText("SQRT");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TSqrt(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTSqrt(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TSqrt text.");
    }
}
