package net.sf.parser4j.parser.service.match;

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
public class TestMatchEqu extends AbstractMatchMgrAdapter {

    public TestMatchEqu(final IParseNodeDataAccess parseNodeDataAccess) {
        super(parseNodeDataAccess);
    }

    @Override
    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        final IParseNodeData uniqData = getUniqData(parseSession, parseNodes[notWhiteIndexes[0]]);
        fatherParseNode.setData(uniqData);
    }
}
