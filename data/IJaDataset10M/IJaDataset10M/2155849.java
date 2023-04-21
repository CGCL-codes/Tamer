package net.sf.parser4j.generator.service.match;

import net.sf.parser4j.generator.entity.grammardefnode.StatementDef;
import net.sf.parser4j.parser.entity.IParseSessionForMatchMgr;
import net.sf.parser4j.parser.entity.parsenode.IParseNode;
import net.sf.parser4j.parser.service.IParseNodeDataAccess;
import net.sf.parser4j.parser.service.ParserException;
import net.sf.parser4j.parser.service.match.AbstractMatchMgrAdapter;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class MatchCharRange extends AbstractMatchMgrAdapter {

    public MatchCharRange(final IParseNodeDataAccess parseNodeDataAccess) {
        super(parseNodeDataAccess);
    }

    @Override
    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        final char first = (char) ((StatementDef) getUniqData(parseSession, parseNodes[notWhiteIndexes[0]])).getTerminalCharValue();
        final char last = (char) ((StatementDef) getUniqData(parseSession, parseNodes[notWhiteIndexes[2]])).getTerminalCharValue();
        final StatementDef statementDef = new StatementDef(first, last);
        fatherParseNode.setData(statementDef);
    }
}
