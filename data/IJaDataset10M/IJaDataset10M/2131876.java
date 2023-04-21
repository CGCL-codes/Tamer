package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class TAnd extends Token {

    public TAnd() {
        super.setText("&");
    }

    public TAnd(int line, int pos) {
        super.setText("&");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TAnd(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTAnd(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TAnd text.");
    }
}
