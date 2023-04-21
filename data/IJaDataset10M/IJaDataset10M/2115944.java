package eulergui.parser.n3.impl.parser4j.service.match;

import eulergui.parser.n3.impl.parser4j.entity.data.UriValueData;
import net.sf.parser4j.parser.entity.IParseSessionForMatchMgr;
import net.sf.parser4j.parser.entity.parsenode.IParseNode;
import net.sf.parser4j.parser.entity.parsenode.StringParseNode;
import net.sf.parser4j.parser.service.IParseNodeDataAccess;
import net.sf.parser4j.parser.service.ParserException;
import net.sf.parser4j.parser.service.match.AbstractMatchMgrAdapter;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class MatchExplicitUri extends AbstractMatchMgrAdapter {

    public MatchExplicitUri(final IParseNodeDataAccess parseNodeDataAccess) {
        super(parseNodeDataAccess);
    }

    @Override
    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        final StringParseNode node = (StringParseNode) parseNodes[notWhiteIndexes[0]];
        final String value = node.getStringValue();
        final StringBuilder stringBuilder = new StringBuilder();
        final int length = value.length();
        for (int index = 1; index < length - 1; index++) {
            final char charValue = value.charAt(index);
            if (charValue > ' ') {
                stringBuilder.append(charValue);
            }
        }
        final String strUri = stringBuilder.toString();
        fatherParseNode.setData(new UriValueData(strUri));
    }
}
