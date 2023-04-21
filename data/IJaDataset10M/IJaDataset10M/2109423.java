package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class TClazz extends Token {

    public TClazz() {
        super.setText("class");
    }

    public TClazz(int line, int pos) {
        super.setText("class");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TClazz(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTClazz(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TClazz text.");
    }
}
