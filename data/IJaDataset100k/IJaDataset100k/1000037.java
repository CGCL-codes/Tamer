package net.sf.parser4j.parser.service.parsenode;

import java.util.HashSet;
import java.util.Set;
import net.sf.parser4j.parser.entity.parsenode.IParseNode;
import net.sf.parser4j.parser.service.ParserException;

/**
 * 
 * @author luc peuvrier
 * 
 */
public abstract class AbstractParseNodeVisitor implements IParseNodeVisitor {

    private transient Set<IParseNode> visitedSet = new HashSet<IParseNode>();

    protected void initializeVisit() {
        visitedSet.clear();
    }

    protected void endVisit() {
        visitedSet.clear();
    }

    @Override
    public boolean beginVisit(final IParseNode node) throws ParserException {
        if (!visitedSet.add(node)) {
            throw new ParserException("already visited");
        }
        return beginVisitImpl(node);
    }

    protected abstract boolean beginVisitImpl(IParseNode node) throws ParserException;

    @Override
    public void endVisit(final IParseNode node) throws ParserException {
        endVisitImpl(node);
        visitedSet.remove(node);
    }

    protected abstract void endVisitImpl(final IParseNode node) throws ParserException;

    @Override
    public void beginAlternativeVisit(final IParseNode node, final int alternativeNumber) throws ParserException {
        beginAlternativeVisitImpl(node, alternativeNumber);
    }

    protected abstract void beginAlternativeVisitImpl(IParseNode node, int alternativeNumber);

    @Override
    public void endAlternativeVisit(final IParseNode node, final int alternativeNumber) {
        endAlternativeVisitImpl(node, alternativeNumber);
    }

    protected abstract void endAlternativeVisitImpl(IParseNode node, int alternativeNumber);
}
