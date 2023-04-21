package org.sodeja.parsec.combinator;

import java.util.Collections;
import java.util.List;
import org.sodeja.collections.ConsList;
import org.sodeja.parsec.AbstractParser;
import org.sodeja.parsec.Parser;
import org.sodeja.parsec.ParsingResult;

public class ZeroOrMoreWithSeparatorParser<Tok, Res, Res1> extends AbstractParser<Tok, List<Res>> {

    private final AlternativeParser<Tok, List<Res>> delegate;

    public ZeroOrMoreWithSeparatorParser(final String name, final Parser<Tok, Res> internal, final Parser<Tok, Res1> separator) {
        super(name);
        OneOrMoreWithSeparator<Tok, Res, Res1> manySepParser = new OneOrMoreWithSeparator<Tok, Res, Res1>(name + "_MANYSEP", internal, separator);
        EmptyParser<Tok, List<Res>> emptyParser = new EmptyParser<Tok, List<Res>>(name + "_EMPTY", Collections.EMPTY_LIST);
        this.delegate = new AlternativeParser<Tok, List<Res>>(name + "ALTERNATIVE", manySepParser, emptyParser);
    }

    @Override
    protected ParsingResult<Tok, List<Res>> executeDelegate(ConsList<Tok> tokens) {
        return delegate.execute(tokens);
    }
}
