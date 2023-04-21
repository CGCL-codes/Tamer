package org.sablecc.objectmacro.syntax3.node;

import org.sablecc.objectmacro.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TShortCommentCommand extends Token {

    public TShortCommentCommand() {
        super.setText("$comment:");
    }

    public TShortCommentCommand(int line, int pos) {
        super.setText("$comment:");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TShortCommentCommand(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTShortCommentCommand(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TShortCommentCommand text.");
    }
}
