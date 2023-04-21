package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class TEllipsis extends Token {

    public TEllipsis() {
        super.setText("...");
    }

    public TEllipsis(int line, int pos) {
        super.setText("...");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TEllipsis(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTEllipsis(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TEllipsis text.");
    }
}
