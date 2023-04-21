package org.sablecc.sablecc.intermediate.syntax3.node;

import org.sablecc.sablecc.intermediate.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TInvestigatorNameKeyword extends Token {

    public TInvestigatorNameKeyword() {
        super.setText("investigator_name");
    }

    public TInvestigatorNameKeyword(int line, int pos) {
        super.setText("investigator_name");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TInvestigatorNameKeyword(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTInvestigatorNameKeyword(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TInvestigatorNameKeyword text.");
    }
}
