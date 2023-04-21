package org.sodeja.parsec.combinator;

import org.sodeja.collections.ConsList;
import org.sodeja.functional.Function1;
import org.sodeja.parsec.AbstractParser;
import org.sodeja.parsec.ParseSuccess;
import org.sodeja.parsec.Parser;
import org.sodeja.parsec.ParsingResult;

public class ApplyParser<Tok, Res, Res1> extends AbstractParser<Tok, Res> {

    private final Parser<Tok, Res1> delegate;

    private final Function1<Res, Res1> functor;

    public ApplyParser(final String name, final Parser<Tok, Res1> parser, final Function1<Res, Res1> functor) {
        super(name);
        this.delegate = parser;
        this.functor = functor;
    }

    @Override
    protected ParsingResult<Tok, Res> executeDelegate(ConsList<Tok> tokens) {
        ParsingResult<Tok, Res1> delegateResult = delegate.execute(tokens);
        if (isFailure(delegateResult)) {
            return failure(delegateResult);
        }
        ParseSuccess<Tok, Res1> successResult = (ParseSuccess<Tok, Res1>) delegateResult;
        return new ParseSuccess<Tok, Res>(functor.execute(successResult.result), successResult.tokens);
    }
}
