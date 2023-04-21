package riverbed.jelan.parser.softparser;

import riverbed.jelan.lexer.Lexer;
import riverbed.jelan.lexer.Token;
import riverbed.jelan.parser.ParseErrorListener;
import riverbed.jelan.parser.ParseException;
import riverbed.jelan.parser.Parser;
import riverbed.jelan.parser.Rule;
import riverbed.jelan.parser.Rule.Match;

/**
 * A parser implementation in which the language grammar is defined by a graph
 * of parse Rules.  A new parser instance is require for each analysis but the 
 * graph of parse rles can be re-used for multiple analyses.  The parser can 
 * be passed a handler object that is made available to the rules as they
 * are executed to enable the results of the parse to be communicate to the
 * client.
 * 
 * TODO investigate traversal of rule graph to optime rule selection
 * TODO consider adding a Grammar object that verifies that all rules in the
 * rule graph are fully specified (have reached an immutable state).  This
 * would strictly require cloning of the rules parameters.  
 */
public class SoftParser implements Parser {

    private Lexer lexer;

    private ParseGrammar grammar;

    private Object handler;

    private ParseErrorListener errorListener;

    private boolean recovering;

    private static final class ExceptionError implements ParseErrorListener {

        public void error(String text, Lexer lexer) {
            throw new ParseException(text, lexer);
        }
    }

    public SoftParser(ParseGrammar grammar, Lexer lexer, Object handler) {
        this(grammar, lexer, handler, new ExceptionError());
    }

    public SoftParser(ParseGrammar grammar, Lexer lexer, Object handler, ParseErrorListener errorListener) {
        this.grammar = grammar;
        this.lexer = lexer;
        this.handler = handler;
        this.errorListener = errorListener;
    }

    /**
     * Verify that the current token matches a specified token.  In normal operation,
     * a failure to match is report as an error to the Parser.  However following an
     * earlier error, this method tries to re-synchronise the grammar with the stream 
     * of tokens being processed.
     */
    public void require(Token token) {
        if (recovering) {
            Token t = lexer.currentToken();
            while (t != Token.END_TOKEN && !token.matches(t)) t = lexer.nextToken();
            lexer.nextToken();
            recovering = false;
        } else if (!lexer.stepToken(token)) error("Required token " + token + " not found");
    }

    protected void error(String text) {
        if (!recovering) {
            errorListener.error(text, lexer);
            recovering = true;
        }
    }

    public void error(Rule rule, String text) {
        error(text + " in " + rule);
    }

    public boolean stepToken(Token token) {
        return lexer.stepToken(token);
    }

    public Token currentToken() {
        return lexer.currentToken();
    }

    public Token nextToken() {
        return lexer.nextToken();
    }

    public void parse() {
        grammar.require(this);
    }

    public boolean matchesClass(Class<? extends Token> clazz) {
        return lexer.matchesClass(clazz);
    }

    public Object getHandler() {
        return handler;
    }

    public Match matchRule(Rule rule) {
        return rule.matches(this);
    }

    public Match requireRule(Rule rule) {
        return rule.require(this);
    }

    public int currentLocation() {
        return lexer.currentLocation();
    }
}
