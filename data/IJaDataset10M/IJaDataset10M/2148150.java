package com.google.werkzeugkasten.sqlparser.impl;

import static com.google.werkzeugkasten.sqlparser.TokenKind.*;
import com.google.werkzeugkasten.meta.Chain;
import com.google.werkzeugkasten.sqlparser.SqlTokenizeContext;
import com.google.werkzeugkasten.sqlparser.Status;
import com.google.werkzeugkasten.sqlparser.TokenKind;
import com.google.werkzeugkasten.sqlparser.TokenNode;

public class Tokenizer implements Chain<Status, SqlTokenizeContext> {

    public Status execute(SqlTokenizeContext parameter) {
        RootNode root = new RootNode();
        parameter.setCursor(0);
        tokenize(root, parameter.getTokens(), parameter);
        parameter.setRoot(root);
        return Status.Success;
    }

    protected int tokenize(TokenNode parent, TokenKind[] tokens, SqlTokenizeContext parameter) {
        int index = parameter.getCursor();
        loop: for (; index < tokens.length; index++) {
            switch(tokens[index]) {
                case BeginSemantic:
                    {
                        index += 3;
                        for (; index < tokens.length; index++) {
                            switch(tokens[index]) {
                                case Identifier:
                                    {
                                        index = tokenizeFunctor(parent, tokens, index, parameter);
                                        break;
                                    }
                                case EndSemantic:
                                    {
                                        index += 2;
                                        continue loop;
                                    }
                                case EndBrace:
                                    return index;
                                case Whitespace:
                                default:
                                    {
                                        break;
                                    }
                            }
                        }
                        break;
                    }
                case Whitespace:
                case Text:
                    {
                        index = tokenizeText(parent, tokens, index, parameter);
                        break;
                    }
                default:
                    {
                        break;
                    }
            }
        }
        return index;
    }

    protected int tokenizeText(TokenNode parent, TokenKind[] tokens, int current, SqlTokenizeContext parameter) {
        int index = current;
        Literal text = new Literal(index);
        loop: for (; index < tokens.length; index++) {
            switch(tokens[index]) {
                case Whitespace:
                case Text:
                    {
                        break;
                    }
                default:
                    break loop;
            }
        }
        text.setLength(index - current);
        parent.addChild(text);
        return index - 1;
    }

    protected int tokenizeFunctor(TokenNode parent, TokenKind[] tokens, int current, SqlTokenizeContext parameter) {
        Functor functor = new Functor(current);
        int index = read(tokens, current, Identifier);
        functor.setName(String.copyValueOf(parameter.getFullText(), current, index - current));
        int brace = read(tokens, index, BeginBrace, false);
        int endparenthesis = back(tokens, brace - 1, Whitespace);
        functor.setExpression(String.copyValueOf(parameter.getFullText(), index + 1, endparenthesis - index - 1));
        int endsemantic = read(tokens, index, EndSemantic, false) + 2;
        parameter.setCursor(endsemantic);
        index = tokenize(functor, tokens, parameter);
        parent.addChild(functor);
        return index;
    }

    protected int read(TokenKind[] tokens, int index, TokenKind type) {
        return read(tokens, index, type, true);
    }

    protected int read(TokenKind[] tokens, int index, TokenKind type, boolean is) {
        for (; index < tokens.length && type.equals(tokens[index]) == is; index++) ;
        return index;
    }

    protected int back(TokenKind[] tokens, int index, TokenKind type) {
        return back(tokens, index, type, true);
    }

    protected int back(TokenKind[] tokens, int index, TokenKind type, boolean is) {
        for (; index < tokens.length && type.equals(tokens[index]) == is; index--) ;
        return index;
    }
}
