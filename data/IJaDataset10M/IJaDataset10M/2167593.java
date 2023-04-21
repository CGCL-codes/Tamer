package org.omwg.mediation.parser.hrsyntax.node;

import org.omwg.mediation.parser.hrsyntax.analysis.*;

public final class TRelationclassmapping extends Token {

    public TRelationclassmapping() {
        super.setText("relationClassMapping");
    }

    public TRelationclassmapping(int line, int pos) {
        super.setText("relationClassMapping");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TRelationclassmapping(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTRelationclassmapping(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TRelationclassmapping text.");
    }
}
