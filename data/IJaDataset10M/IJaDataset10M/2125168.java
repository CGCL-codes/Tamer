package eulergui.parser.n3.impl.parser4j.service.match;

import net.sf.parser4j.parser.entity.IParseSessionForMatchMgr;
import net.sf.parser4j.parser.entity.parsenode.IParseNode;
import net.sf.parser4j.parser.service.ParserException;
import net.sf.parser4j.parser.service.match.AbstractMatchMgrAdapter;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class MatchQuotedString extends AbstractMatchMgrAdapter {

    @Override
    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        fatherParseNode.setData(parseNodes[notWhiteIndexes[1]].getUniqData());
    }
}
