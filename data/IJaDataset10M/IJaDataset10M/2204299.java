package com.trazere.parser.core;

import com.trazere.parser.BaseParser;
import com.trazere.parser.Parser;
import com.trazere.parser.ParserClosure;
import com.trazere.parser.ParserException;
import com.trazere.parser.ParserHandler;
import com.trazere.parser.ParserState;
import com.trazere.util.lang.HashCode;
import com.trazere.util.lang.LangUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DOCME
 * 
 * @param <Token>
 * @param <Result>
 */
public class ManyNParser<Token, Result> extends BaseParser<Token, List<Result>> {

    protected final Parser<Token, ? extends Result> _subParser;

    public ManyNParser(final Parser<Token, ? extends Result> subParser, final String description) {
        super(description);
        assert null != subParser;
        _subParser = subParser;
    }

    public void run(final ParserClosure<Token, List<Result>> closure, final ParserState<Token> state) throws ParserException {
        state.parse(_subParser, buildTwoHandler(closure, new ArrayList<Result>()), closure);
    }

    protected ParserHandler<Token, Result> buildTwoHandler(final ParserClosure<Token, List<Result>> closure, final List<Result> previousResults) {
        return new ParserHandler<Token, Result>() {

            public void result(final Result result, final ParserState<Token> state) throws ParserException {
                final List<Result> results = new ArrayList<Result>(previousResults);
                results.add(result);
                state.parse(_subParser, buildMoreHandler(closure, results), closure);
            }
        };
    }

    protected ParserHandler<Token, Result> buildMoreHandler(final ParserClosure<Token, List<Result>> closure, final List<Result> previousResults) {
        return new ParserHandler<Token, Result>() {

            public void result(final Result result, final ParserState<Token> state) throws ParserException {
                final List<Result> results = new ArrayList<Result>(previousResults);
                results.add(result);
                closure.success(Collections.unmodifiableList(results), state);
                state.parse(_subParser, buildMoreHandler(closure, results), closure);
            }
        };
    }

    @Override
    public int hashCode() {
        final HashCode result = new HashCode(this);
        result.append(_description);
        result.append(_subParser);
        return result.get();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        } else if (null != object && getClass().equals(object.getClass())) {
            final ManyNParser<?, ?> parser = (ManyNParser<?, ?>) object;
            return LangUtils.equals(_description, parser._description) && _subParser.equals(parser._subParser);
        } else {
            return false;
        }
    }
}
