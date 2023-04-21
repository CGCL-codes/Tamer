package tudresden.ocl.codegen.decl.treegen.node;

import tudresden.ocl.codegen.decl.treegen.analysis.*;

public final class TAsterisk extends Token {

    public TAsterisk() {
        super.setText("*");
    }

    public TAsterisk(int line, int pos) {
        super.setText("*");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TAsterisk(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTAsterisk(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TAsterisk text.");
    }
}
