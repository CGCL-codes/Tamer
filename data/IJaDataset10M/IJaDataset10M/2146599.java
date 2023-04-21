package org.sablecc.sablecc.node;

import org.sablecc.sablecc.analysis.*;
import org.sablecc.sablecc.node.Switch;
import org.sablecc.sablecc.node.TExclam;
import org.sablecc.sablecc.node.Token;

public final class TExclam extends Token {

    public TExclam() {
        super.setText("!");
    }

    public TExclam(int line, int pos) {
        super.setText("!");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TExclam(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTExclam(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TExclam text.");
    }
}
