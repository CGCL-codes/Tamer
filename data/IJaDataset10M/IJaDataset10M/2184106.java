package com.trazere.parser;

/**
 * The {@link Parser} interface defines parsing functions from streams of tokens to values.
 * <p>
 * The parsing framework has been designed to encourage the writing of parsers as combinators. Low level parsers implement the primitives of some grammar
 * language (like reading a character or reading some optional value) while higher level parsers implement the grammars themselves.
 * 
 * @param <Token> Type of the tokens.
 * @param <Result> Type of the result values.
 */
public interface Parser<Token, Result> {

    /**
	 * Get the description of the receiver parser.
	 * 
	 * @return The description.
	 */
    public String getDescription();

    /**
	 * Run the receiver parser.
	 * 
	 * @param closure The parsing closure.
	 * @param state The parsing state to use.
	 * @throws ParserException
	 */
    public void run(final ParserClosure<Token, Result> closure, final ParserState<Token> state) throws ParserException;
}
