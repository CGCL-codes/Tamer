package com.trazere.parser;

/**
 * DOCME
 * 
 * @param <Token>
 */
public interface ParserState<Token> {

    public ParserPosition<Token> getPosition();

    public <Result> void parse(final Parser<Token, Result> parser, final ParserHandler<Token, ? super Result> handler, final ParserClosure<Token, ?> parent) throws ParserException;

    public void read(final ParserContinuation<Token> continuation) throws ParserException;
}
