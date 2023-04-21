package edu.mit.lcs.haystack.adenine.parser2.constructs;

import edu.mit.lcs.haystack.adenine.constructs.IIfVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IAttributeVisitor;
import edu.mit.lcs.haystack.adenine.parser2.ICodeBlockVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IConstructParser;
import edu.mit.lcs.haystack.adenine.parser2.IConstructVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IExpressionVisitor;
import edu.mit.lcs.haystack.adenine.parser2.NullAttributeVisitor;
import edu.mit.lcs.haystack.adenine.parser2.NullCodeBlockVisitor;
import edu.mit.lcs.haystack.adenine.parser2.NullExpressionVisitor;
import edu.mit.lcs.haystack.adenine.parser2.ParserUtilities;
import edu.mit.lcs.haystack.adenine.parser2.SyntaxException;
import edu.mit.lcs.haystack.adenine.tokenizer.GenericToken;
import edu.mit.lcs.haystack.adenine.tokenizer.ITokenIterator;
import edu.mit.lcs.haystack.adenine.tokenizer.IndentToken;
import edu.mit.lcs.haystack.adenine.tokenizer.Location;
import edu.mit.lcs.haystack.adenine.tokenizer.SymbolToken;
import edu.mit.lcs.haystack.adenine.tokenizer.Token;

/**
 * @author David Huynh
 */
public class IfParser implements IConstructParser {

    public Location parseConstruct(ITokenIterator tIterator, IConstructVisitor constructVisitor, IndentToken indentToken) {
        IIfVisitor visitor = (constructVisitor instanceof IIfVisitor) ? (IIfVisitor) constructVisitor : new IIfVisitor() {

            public IAttributeVisitor onAttribute(SymbolToken semicolonT) {
                return new NullAttributeVisitor(m_constructVisitor);
            }

            public IExpressionVisitor onIf(GenericToken ifKeyword) {
                return new NullExpressionVisitor(m_constructVisitor);
            }

            public ICodeBlockVisitor onIfBody(Location location) {
                return new NullCodeBlockVisitor(m_constructVisitor);
            }

            public IExpressionVisitor onElseIf(GenericToken elseIfKeyword) {
                return new NullExpressionVisitor(m_constructVisitor);
            }

            public ICodeBlockVisitor onElseIfBody(Location location) {
                return new NullCodeBlockVisitor(m_constructVisitor);
            }

            public ICodeBlockVisitor onElse(GenericToken elseKeyword) {
                return new NullCodeBlockVisitor(m_constructVisitor);
            }

            public void start(Location startLocation) {
                m_constructVisitor.start(startLocation);
            }

            public void end(Location endLocation) {
                m_constructVisitor.end(endLocation);
            }

            public void onException(Exception exception) {
                m_constructVisitor.onException(exception);
            }

            public IIfVisitor init(IConstructVisitor constructVisitor) {
                m_constructVisitor = constructVisitor;
                return this;
            }

            IConstructVisitor m_constructVisitor;
        }.init(constructVisitor);
        visitor.start(tIterator.getLocation());
        Token token = tIterator.getToken();
        Location endLocation = null;
        if (token instanceof GenericToken && ((GenericToken) token).getContent().equals("if")) {
            tIterator.swallow();
            ParserUtilities.skipWhitespacesAndComments(tIterator);
            ParserUtilities.parseExpression(tIterator, visitor.onIf((GenericToken) token), false);
            endLocation = tIterator.getLocation();
            token = ParserUtilities.skipToNextLine(tIterator);
            if (token != null) {
                visitor.onException(new SyntaxException("Unexpected code after if expression", token.getSpan()));
            }
            ParserUtilities.skipWhitespacesCommentsAndNewLines(tIterator);
            token = tIterator.getToken();
            if (token instanceof IndentToken) {
                IndentToken indentToken2 = (IndentToken) token;
                if (indentToken.isOuterLevelOf(indentToken2)) {
                    endLocation = ParserUtilities.parseCodeBlock(tIterator, visitor.onIfBody(indentToken2.getSpan().getEnd()), indentToken2);
                    ParserUtilities.skipWhitespacesCommentsAndNewLines(tIterator);
                    token = tIterator.getToken();
                }
                while (token instanceof IndentToken && indentToken.isIdenticalTo((IndentToken) token)) {
                    Token token2 = tIterator.getToken(1);
                    if (!(token2 instanceof GenericToken)) {
                        break;
                    }
                    String content = ((GenericToken) token2).getContent();
                    if (content.equals("elseIf")) {
                        tIterator.swallow(2);
                        ParserUtilities.skipWhitespacesAndComments(tIterator);
                        ParserUtilities.parseExpression(tIterator, visitor.onElseIf((GenericToken) token2), false);
                        endLocation = tIterator.getLocation();
                        token = ParserUtilities.skipToNextLine(tIterator);
                        if (token != null) {
                            visitor.onException(new SyntaxException("Unexpected code after elseIf condition", token.getSpan()));
                        }
                        ParserUtilities.skipWhitespacesCommentsAndNewLines(tIterator);
                        token = tIterator.getToken();
                        if (token instanceof IndentToken) {
                            indentToken2 = (IndentToken) token;
                            if (indentToken.isOuterLevelOf(indentToken2)) {
                                endLocation = ParserUtilities.parseCodeBlock(tIterator, visitor.onElseIfBody(indentToken2.getSpan().getEnd()), indentToken2);
                                ParserUtilities.skipWhitespacesCommentsAndNewLines(tIterator);
                                token = tIterator.getToken();
                            }
                        }
                    } else if (content.equals("else")) {
                        tIterator.swallow(2);
                        token = ParserUtilities.skipToNextLine(tIterator);
                        if (token != null) {
                            visitor.onException(new SyntaxException("Unexpected code after else", token.getSpan()));
                        }
                        ParserUtilities.skipWhitespacesCommentsAndNewLines(tIterator);
                        token = tIterator.getToken();
                        if (token instanceof IndentToken) {
                            indentToken2 = (IndentToken) token;
                            if (indentToken.isOuterLevelOf(indentToken2)) {
                                endLocation = ParserUtilities.parseCodeBlock(tIterator, visitor.onElse((GenericToken) token2), indentToken2);
                            }
                        }
                        break;
                    } else {
                        break;
                    }
                }
            }
        } else {
            visitor.onException(new SyntaxException("Expected if", tIterator.getLocation()));
            ParserUtilities.skipToNextLine(tIterator);
        }
        if (endLocation == null) {
            endLocation = tIterator.getLocation();
        }
        visitor.end(endLocation);
        return endLocation;
    }
}
