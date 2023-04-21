package edu.mit.lcs.haystack.adenine.compilers.utils;

import edu.mit.lcs.haystack.adenine.parser2.IAttributeVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IExpressionVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IModelVisitor;
import edu.mit.lcs.haystack.adenine.parser2.NullAttributeVisitor;
import edu.mit.lcs.haystack.adenine.tokenizer.Location;
import edu.mit.lcs.haystack.adenine.tokenizer.SymbolToken;
import edu.mit.lcs.haystack.rdf.Resource;

/**
 * @author David Huynh
 */
public class TopLevelModelVisitor extends ParserVisitorBase implements IModelVisitor {

    TopLevelVisitorBase m_topLevelVisitor;

    TopLevelExpressionVisitor m_subjectVisitor;

    public TopLevelModelVisitor(TopLevelVisitorBase topLevelVisitor) {
        super(topLevelVisitor.getChainedVisitor());
        m_topLevelVisitor = topLevelVisitor;
    }

    public IAttributeVisitor onAttribute(SymbolToken semicolonT) {
        if (m_subjectVisitor != null && m_subjectVisitor.getRDFNode() instanceof Resource) {
            return new TopLevelAttributeVisitor(m_topLevelVisitor, m_subjectVisitor.getResource());
        } else {
            return new NullAttributeVisitor(m_visitor);
        }
    }

    public void onModelEnd(SymbolToken rightBraceT) {
    }

    public void onModelStart(SymbolToken leftBraceT) {
    }

    public IExpressionVisitor onSubject(Location location) {
        m_subjectVisitor = new TopLevelExpressionVisitor(m_topLevelVisitor, true);
        return m_subjectVisitor;
    }

    public Resource getLastResource() {
        return m_subjectVisitor != null ? m_subjectVisitor.getResource() : null;
    }
}
