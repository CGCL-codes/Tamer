package edu.mit.lcs.haystack.adenine.compilers.rdfCode;

import edu.mit.lcs.haystack.adenine.AdenineConstants;
import edu.mit.lcs.haystack.adenine.compilers.utils.ParserVisitorBase;
import edu.mit.lcs.haystack.adenine.parser2.IExpressionVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IListVisitor;
import edu.mit.lcs.haystack.adenine.tokenizer.Location;
import edu.mit.lcs.haystack.adenine.tokenizer.SymbolToken;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.ListUtilities;
import edu.mit.lcs.haystack.rdf.Literal;
import edu.mit.lcs.haystack.rdf.RDFException;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.rdf.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author David Huynh
 */
public class ListVisitor extends ParserVisitorBase implements IListVisitor {

    protected TopLevelVisitor m_topLevelVisitor;

    protected Resource m_instructionResource;

    protected List m_arguments = new ArrayList();

    public ListVisitor(TopLevelVisitor visitor) {
        super(visitor);
        m_topLevelVisitor = visitor;
    }

    public void end(Location endLocation) {
        IRDFContainer target = m_topLevelVisitor.getTarget();
        m_instructionResource = m_topLevelVisitor.generateInstruction(AdenineConstants.FunctionCall, m_startLocation);
        List arguments = new ArrayList();
        for (int i = 0; i < m_arguments.size(); i++) {
            Resource r = (((ExpressionVisitor) m_arguments.get(i)).getInstructionResource());
            if (r != null) {
                arguments.add(r);
            }
        }
        try {
            Resource identifier = m_topLevelVisitor.generateInstruction(AdenineConstants.Identifier, m_startLocation);
            target.add(new Statement(identifier, AdenineConstants.name, new Literal("List")));
            target.add(new Statement(m_instructionResource, AdenineConstants.function, identifier));
            target.add(new Statement(m_instructionResource, AdenineConstants.PARAMETERS, ListUtilities.createDAMLList(arguments.iterator(), target)));
        } catch (RDFException e) {
            onException(e);
        }
    }

    public IExpressionVisitor onItem(Location location) {
        ExpressionVisitor ev = new ExpressionVisitor(m_topLevelVisitor);
        m_arguments.add(ev);
        return ev;
    }

    public void onLeftParenthesis(SymbolToken atSignT, SymbolToken leftParenthesisT) {
    }

    public void onRightParenthesis(SymbolToken rightParenthesisT) {
    }
}
