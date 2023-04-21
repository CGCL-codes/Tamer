package edu.mit.lcs.haystack.adenine.compilers.utils;

import edu.mit.lcs.haystack.adenine.parser2.IAnonymousModelVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IApplyVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IAskModelVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IExpressionVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IListVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IModelVisitor;
import edu.mit.lcs.haystack.adenine.parser2.ISubExpressionVisitor;
import edu.mit.lcs.haystack.adenine.parser2.NullApplyVisitor;
import edu.mit.lcs.haystack.adenine.parser2.NullAskModelVisitor;
import edu.mit.lcs.haystack.adenine.parser2.NullExpressionVisitor;
import edu.mit.lcs.haystack.adenine.parser2.NullSubExpressionVisitor;
import edu.mit.lcs.haystack.adenine.parser2.SyntaxException;
import edu.mit.lcs.haystack.adenine.tokenizer.FloatToken;
import edu.mit.lcs.haystack.adenine.tokenizer.IntegerToken;
import edu.mit.lcs.haystack.adenine.tokenizer.LiteralToken;
import edu.mit.lcs.haystack.adenine.tokenizer.Location;
import edu.mit.lcs.haystack.adenine.tokenizer.StringToken;
import edu.mit.lcs.haystack.adenine.tokenizer.SymbolToken;
import edu.mit.lcs.haystack.rdf.Literal;
import edu.mit.lcs.haystack.rdf.RDFNode;
import edu.mit.lcs.haystack.rdf.Resource;

/**
 * @author David Huynh
 */
public class TopLevelExpressionVisitor extends ParserVisitorBase implements IExpressionVisitor {

    TopLevelVisitorBase m_topLevelVisitor;

    boolean m_expectsResource;

    TopLevelSubExpressionVisitor m_subExpressionVisitor;

    TopLevelListVisitor m_listVisitor;

    TopLevelModelVisitor m_modelVisitor;

    TopLevelAnonymousModelVisitor m_anonymousModelVisitor;

    RDFNode m_result;

    public TopLevelExpressionVisitor(TopLevelVisitorBase topLevelVisitor, boolean expectsResource) {
        super(topLevelVisitor.getChainedVisitor());
        m_topLevelVisitor = topLevelVisitor;
        m_expectsResource = expectsResource;
    }

    public TopLevelExpressionVisitor(TopLevelVisitorBase topLevelVisitor) {
        this(topLevelVisitor, false);
    }

    public ISubExpressionVisitor onDereference(SymbolToken periodT) {
        onException(new SyntaxException("No dereferencement allowed at top level", periodT.getSpan()));
        return new NullSubExpressionVisitor(m_visitor);
    }

    public IExpressionVisitor onLeftBracket(SymbolToken leftBracketT) {
        onException(new SyntaxException("No index allowed at top level", leftBracketT.getSpan()));
        return new NullExpressionVisitor(m_visitor);
    }

    public void onRightBracket(SymbolToken rightBracketT) {
    }

    public ISubExpressionVisitor onSubExpression(Location location) {
        m_subExpressionVisitor = new TopLevelSubExpressionVisitor(m_topLevelVisitor);
        return m_subExpressionVisitor;
    }

    public IAnonymousModelVisitor onAnonymousModel(Location location) {
        m_anonymousModelVisitor = new TopLevelAnonymousModelVisitor(m_topLevelVisitor);
        return m_anonymousModelVisitor;
    }

    public IApplyVisitor onApply(Location location) {
        return new NullApplyVisitor(m_visitor) {

            public void onLeftParenthesis(SymbolToken leftParenthesisT) {
                onException(new SyntaxException("Method application not allowed in top level model", leftParenthesisT.getSpan()));
            }
        };
    }

    public IAskModelVisitor onAskModel(Location location) {
        return new NullAskModelVisitor(m_visitor) {

            public void onModelStart(SymbolToken percentT, SymbolToken leftBraceT) {
                onException(new SyntaxException("Ask model not allowed in top level model", percentT.getSpan()));
            }
        };
    }

    public void onFloat(FloatToken floatToken) {
        m_result = new Literal(floatToken.getContent());
    }

    public void onInteger(IntegerToken integerToken) {
        m_result = new Literal(integerToken.getContent());
    }

    public IListVisitor onList(Location location) {
        m_listVisitor = new TopLevelListVisitor(m_topLevelVisitor);
        return m_listVisitor;
    }

    public void onLiteral(LiteralToken literalToken) {
        m_result = new Literal(literalToken.getContent());
    }

    public IModelVisitor onModel(Location location) {
        m_modelVisitor = new TopLevelModelVisitor(m_topLevelVisitor);
        return m_modelVisitor;
    }

    public void onString(StringToken stringToken) {
        m_result = new Literal(stringToken.getContent());
    }

    public RDFNode getRDFNode() {
        if (m_result != null) {
            return m_result;
        } else if (m_listVisitor != null) {
            return m_listVisitor.getListResource();
        } else if (m_anonymousModelVisitor != null) {
            return m_anonymousModelVisitor.getAnonymousResource();
        } else if (m_modelVisitor != null) {
            return m_modelVisitor.getLastResource();
        } else {
            try {
                return m_subExpressionVisitor.getRDFNode();
            } catch (Exception e) {
                return null;
            }
        }
    }

    public Resource getResource() {
        return (Resource) getRDFNode();
    }
}
