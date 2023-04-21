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
 * @param <SubResult2>
 * @param <SubResult3>
 * @param <SubResult4>
 * @param <SubResult5>
 * @param <Result>
 */
public abstract class Sequence5Parser<Token, SubResult1, SubResult2, SubResult3, SubResult4, SubResult5, Result> extends BaseParser<Token, Result> {

    protected final Parser<Token, ? extends SubResult1> _subParser1;

    protected final Parser<Token, ? extends SubResult2> _subParser2;

    protected final Parser<Token, ? extends SubResult3> _subParser3;

    protected final Parser<Token, ? extends SubResult4> _subParser4;

    protected final Parser<Token, ? extends SubResult5> _subParser5;

    public Sequence5Parser(final Parser<Token, ? extends SubResult1> subParser1, final Parser<Token, ? extends SubResult2> subParser2, final Parser<Token, ? extends SubResult3> subParser3, final Parser<Token, ? extends SubResult4> subParser4, final Parser<Token, ? extends SubResult5> subParser5, final String description) {
        super(description);
        assert null != subParser1;
        assert null != subParser2;
        assert null != subParser3;
        assert null != subParser4;
        assert null != subParser5;
        _subParser1 = subParser1;
        _subParser2 = subParser2;
        _subParser3 = subParser3;
        _subParser4 = subParser4;
        _subParser5 = subParser5;
    }

    public void run(final ParserClosure<Token, Result> closure, final ParserState<Token> state) throws ParserException {
        state.parse(_subParser1, buildHandler1(closure), closure);
    }

    protected ParserHandler<Token, SubResult1> buildHandler1(final ParserClosure<Token, Result> closure) {
        return new ParserHandler<Token, SubResult1>() {

            public void result(final SubResult1 subResult1, final ParserState<Token> state) throws ParserException {
                state.parse(_subParser2, buildHandler2(closure, subResult1), closure);
            }
        };
    }

    protected ParserHandler<Token, SubResult2> buildHandler2(final ParserClosure<Token, Result> closure, final SubResult1 subResult1) {
        return new ParserHandler<Token, SubResult2>() {

            public void result(final SubResult2 subResult2, final ParserState<Token> state) throws ParserException {
                state.parse(_subParser3, buildHandler3(closure, subResult1, subResult2), closure);
            }
        };
    }

    protected ParserHandler<Token, SubResult3> buildHandler3(final ParserClosure<Token, Result> closure, final SubResult1 subResult1, final SubResult2 subResult2) {
        return new ParserHandler<Token, SubResult3>() {

            public void result(final SubResult3 subResult3, final ParserState<Token> state) throws ParserException {
                state.parse(_subParser4, buildHandler4(closure, subResult1, subResult2, subResult3), closure);
            }
        };
    }

    protected ParserHandler<Token, SubResult4> buildHandler4(final ParserClosure<Token, Result> closure, final SubResult1 subResult1, final SubResult2 subResult2, final SubResult3 subResult3) {
        return new ParserHandler<Token, SubResult4>() {

            public void result(final SubResult4 subResult4, final ParserState<Token> state) throws ParserException {
                state.parse(_subParser5, buildHandler5(closure, subResult1, subResult2, subResult3, subResult4), closure);
            }
        };
    }

    protected ParserHandler<Token, SubResult5> buildHandler5(final ParserClosure<Token, Result> closure, final SubResult1 subResult1, final SubResult2 subResult2, final SubResult3 subResult3, final SubResult4 subResult4) {
        return new ParserHandler<Token, SubResult5>() {

            public void result(final SubResult5 subResult5, final ParserState<Token> state) throws ParserException {
                closure.success(combine(subResult1, subResult2, subResult3, subResult4, subResult5), state);
            }
        };
    }

    protected abstract Result combine(final SubResult1 subResult1, final SubResult2 subResult2, final SubResult3 subResult3, final SubResult4 subResult4, final SubResult5 subResult5) throws ParserException;
}
