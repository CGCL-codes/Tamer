package eulergui.parser.n3.impl.parser4j.service.match;

import eulergui.parser.n3.impl.parser4j.entity.data.StatementData;
import net.sf.parser4j.parser.entity.IParseSessionForMatchMgr;
import net.sf.parser4j.parser.entity.parsenode.IParseNode;
import net.sf.parser4j.parser.entity.parsenode.data.IParseNodeData;
import net.sf.parser4j.parser.service.IParseNodeDataAccess;
import net.sf.parser4j.parser.service.ParserException;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class MatchStatementOptional extends AbstractMatchStatementList {

    public MatchStatementOptional(final IParseNodeDataAccess parseNodeDataAccess) {
        super(parseNodeDataAccess);
    }

    @Override
    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        StatementData statementList = (StatementData) getUniqData(parseSession, parseNodes[notWhiteIndexes[0]]);
        final IParseNodeData uniqData = getUniqData(parseSession, parseNodes[notWhiteIndexes[1]]);
        reduceAction(fatherParseNode, statementList, uniqData);
    }
}
