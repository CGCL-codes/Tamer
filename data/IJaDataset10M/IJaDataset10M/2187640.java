package com.safi.workshop.sqlexplorer.parsers.scp;

import java.util.LinkedList;
import com.safi.workshop.sqlexplorer.parsers.NamedParameter;
import com.safi.workshop.sqlexplorer.parsers.ParserException;
import com.safi.workshop.sqlexplorer.parsers.Tokenizer;
import com.safi.workshop.sqlexplorer.parsers.Tokenizer.Token;
import com.safi.workshop.sqlexplorer.parsers.Tokenizer.TokenType;
import com.safi.workshop.sqlexplorer.parsers.scp.StructuredCommentParser.CommandType;

class ParameterCommand extends Command {

    public ParameterCommand(StructuredCommentParser parser, Token comment, Tokenizer tokenizer, CharSequence data) throws ParserException {
        super(parser, CommandType.PARAMETER, comment, tokenizer, data);
        String name;
        NamedParameter.DataType dataType = NamedParameter.DataType.STRING;
        NamedParameter.Direction direction = null;
        if (tokens.size() < 1) throw new StructuredCommentException("parameter has no parameter name", comment);
        name = popWord();
        String str = popWord();
        if (str != null) try {
            direction = NamedParameter.Direction.valueOf(str.toUpperCase());
            str = null;
        } catch (IllegalArgumentException e) {
            direction = NamedParameter.Direction.INPUT;
        }
        if (str == null) str = popWord();
        if (str != null) try {
            dataType = NamedParameter.DataType.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new StructuredCommentException("Unrecognised data type " + str, comment);
        }
        LinkedList<Token> arguments = new LinkedList<Token>();
        while (!tokens.isEmpty()) {
            Token token = tokens.removeFirst();
            arguments.add(token);
        }
        parser.parser.addParameter(new NamedParameter(comment, name, dataType, direction, arguments, data));
    }

    protected String popWord() throws StructuredCommentException {
        if (tokens.isEmpty()) return null;
        Token token = tokens.removeFirst();
        if (token.getTokenType() != TokenType.WORD) throw new StructuredCommentException("Invalid token, expected a word but got " + token.toString(), comment);
        return token.toString();
    }

    @Override
    public String toString() {
        return "parameter";
    }
}
