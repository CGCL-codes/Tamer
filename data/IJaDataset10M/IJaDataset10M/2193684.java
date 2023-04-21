package org.sablecc.sablecc.intermediate.syntax3.node;

import org.sablecc.sablecc.intermediate.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TLexerStateKeyword extends Token {

    public TLexerStateKeyword() {
        super.setText("lexer_state");
    }

    public TLexerStateKeyword(int line, int pos) {
        super.setText("lexer_state");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TLexerStateKeyword(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLexerStateKeyword(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TLexerStateKeyword text.");
    }
}
