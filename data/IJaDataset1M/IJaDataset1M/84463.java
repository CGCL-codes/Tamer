package eulergui.parser.n3.impl.parser4j.service.match;

import net.sf.parser4j.parser.entity.IParseSessionForMatchMgr;
import net.sf.parser4j.parser.entity.parsenode.IParseNode;
import net.sf.parser4j.parser.entity.parsenode.data.StringValueData;
import net.sf.parser4j.parser.service.IParseNodeDataAccess;
import net.sf.parser4j.parser.service.ParserException;
import net.sf.parser4j.parser.service.match.AbstractMatchMgrAdapter;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class _MatchDoubleCoteCharacter extends AbstractMatchMgrAdapter {

    public _MatchDoubleCoteCharacter(final IParseNodeDataAccess parseNodeDataAccess) {
        super(parseNodeDataAccess);
    }

    @Override
    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        fatherParseNode.setData(new StringValueData('"'));
    }
}
