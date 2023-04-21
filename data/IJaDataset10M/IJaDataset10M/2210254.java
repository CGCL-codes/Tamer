package edu.mit.lcs.haystack.adenine.parser2.constructs;

import edu.mit.lcs.haystack.adenine.constructs.IAssignmentVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IAttributeVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IConstructParser;
import edu.mit.lcs.haystack.adenine.parser2.IConstructVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IExpressionVisitor;
import edu.mit.lcs.haystack.adenine.parser2.NullAttributeVisitor;
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
public class AssignmentParser implements IConstructParser {

    public Location parseConstruct(ITokenIterator tIterator, IConstructVisitor constructVisitor, IndentToken indentToken) {
        IAssignmentVisitor visitor = (constructVisitor instanceof IAssignmentVisitor) ? (IAssignmentVisitor) constructVisitor : new IAssignmentVisitor() {

            public IAttributeVisitor onAttribute(SymbolToken semicolonT) {
                return new NullAttributeVisitor(m_constructVisitor);
            }

            public void onLHS(SymbolToken equalT, GenericToken identifier) {
            }

            public IExpressionVisitor onRHS(Location location) {
                return new NullExpressionVisitor();
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

            public IAssignmentVisitor init(IConstructVisitor constructVisitor) {
                m_constructVisitor = constructVisitor;
                return this;
            }

            IConstructVisitor m_constructVisitor;
        }.init(constructVisitor);
        visitor.start(tIterator.getLocation());
        SymbolToken equalT = (SymbolToken) tIterator.getToken();
        Location endLocation = null;
        tIterator.swallow();
        ParserUtilities.skipWhitespacesAndComments(tIterator);
        Token token = tIterator.getToken();
        if (token instanceof GenericToken || (token instanceof SymbolToken && "@{$%(".indexOf(((SymbolToken) token).getSymbol()) < 0)) {
            visitor.onLHS(equalT, ParserUtilities.parseCompoundGenericToken(tIterator, token, true));
            ParserUtilities.skipWhitespacesAndComments(tIterator);
            ParserUtilities.parseExpression(tIterator, visitor.onRHS(tIterator.getLocation()), false);
            endLocation = tIterator.getLocation();
            token = ParserUtilities.skipToNextLine(tIterator);
            if (token != null) {
                visitor.onException(new SyntaxException("Unexpected code after end of assignment", token.getSpan()));
            }
        } else {
            ParserUtilities.skipToNextLine(tIterator);
            visitor.onException(new SyntaxException("Expected identifier in assignment construct", token.getSpan()));
        }
        if (endLocation == null) {
            endLocation = tIterator.getLocation();
        }
        visitor.end(endLocation);
        return endLocation;
    }
}
