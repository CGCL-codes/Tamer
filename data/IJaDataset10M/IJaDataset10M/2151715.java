package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class TRBrk extends Token {

    public TRBrk() {
        super.setText("]");
    }

    public TRBrk(int line, int pos) {
        super.setText("]");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TRBrk(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTRBrk(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TRBrk text.");
    }
}
