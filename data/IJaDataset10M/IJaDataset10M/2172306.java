package com.trazere.parser.core;

import com.trazere.parser.BaseParser;
import com.trazere.parser.Parser;
import com.trazere.parser.ParserClosure;
import com.trazere.parser.ParserException;
import com.trazere.parser.ParserHandler;
import com.trazere.parser.ParserState;

/**
 * DOCME
 * 
 * @param <Token>
 * @param <SubResult1>
 * @param <Result>
 */
public abstract class Sequence1Parser<Token, SubResult1, Result> extends BaseParser<Token, Result> {

    protected final Parser<Token, ? extends SubResult1> _subParser1;

    public Sequence1Parser(final Parser<Token, ? extends SubResult1> subParser1, final String description) {
        super(description);
        assert null != subParser1;
        _subParser1 = subParser1;
    }

    public void run(final ParserClosure<Token, Result> closure, final ParserState<Token> state) throws ParserException {
        state.parse(_subParser1, buildHandler1(closure), closure);
    }

    protected ParserHandler<Token, SubResult1> buildHandler1(final ParserClosure<Token, Result> closure) {
        return new ParserHandler<Token, SubResult1>() {

            public void result(final SubResult1 subResult1, final ParserState<Token> state) throws ParserException {
                closure.success(combine(subResult1), state);
            }
        };
    }

    protected abstract Result combine(final SubResult1 subResult1) throws ParserException;
}
