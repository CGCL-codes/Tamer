package org.sableccsupport.scclexer.node;

import org.sableccsupport.scclexer.analysis.*;

@SuppressWarnings("nls")
public final class TSyntax extends Token {

    public TSyntax() {
        super.setText("Syntax");
    }

    public TSyntax(int line, int pos) {
        super.setText("Syntax");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TSyntax(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTSyntax(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TSyntax text.");
    }
}
