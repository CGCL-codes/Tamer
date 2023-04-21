package com.safi.workshop.sqlexplorer.parsers.scp;

import java.util.LinkedList;
import java.util.ListIterator;
import com.safi.workshop.sqlexplorer.parsers.ParserException;
import com.safi.workshop.sqlexplorer.parsers.Tokenizer;
import com.safi.workshop.sqlexplorer.parsers.Tokenizer.Token;
import com.safi.workshop.sqlexplorer.parsers.scp.StructuredCommentParser.CommandType;

class Command {

    protected StructuredCommentParser parser;

    protected CommandType commandType;

    protected Token comment;

    protected LinkedList<Token> tokens = new LinkedList<Token>();

    protected CharSequence data;

    /**
   * Constructor
   * 
   * @param comment
   *          the original comment
   * @param tokenizer
   *          a tokenizer built to parse the comment; it is expected that the comment
   *          start and leading ${ have already been be skipped over
   * @throws StructuredCommentException
   */
    public Command(StructuredCommentParser parser, CommandType commandType, Token comment, Tokenizer tokenizer, CharSequence data) throws ParserException {
        this.parser = parser;
        this.commandType = commandType;
        this.comment = comment;
        this.data = data;
        Token token;
        while ((token = tokenizer.nextToken()) != null) tokens.add(token);
    }

    public void process(ListIterator<Command> pIter) {
    }
}
