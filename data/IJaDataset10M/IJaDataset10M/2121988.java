package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class TStar extends Token {

    public TStar() {
        super.setText("*");
    }

    public TStar(int line, int pos) {
        super.setText("*");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TStar(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTStar(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TStar text.");
    }
}
