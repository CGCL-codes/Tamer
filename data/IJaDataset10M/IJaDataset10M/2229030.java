package eulergui.parser.n3.impl.parser4j.service.match;

import net.sf.parser4j.parser.entity.IParseSessionForMatchMgr;
import net.sf.parser4j.parser.entity.parsenode.IParseNode;
import net.sf.parser4j.parser.service.ParserException;
import net.sf.parser4j.parser.service.match.AbstractMatchMgrAdapter;
import eulergui.parser.n3.impl.parser4j.entity.data.StringValueData;

public class MatchUnicode32 extends AbstractMatchMgrAdapter {

    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        final String stringValue = ((StringValueData) parseNodes[notWhiteIndexes[1]]).getValue() + ((StringValueData) parseNodes[notWhiteIndexes[2]]).getValue() + ((StringValueData) parseNodes[notWhiteIndexes[3]]).getValue() + ((StringValueData) parseNodes[notWhiteIndexes[4]]).getValue() + ((StringValueData) parseNodes[notWhiteIndexes[5]]).getValue() + ((StringValueData) parseNodes[notWhiteIndexes[6]]).getValue();
        final int codePoint = Integer.parseInt(stringValue, 16);
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.appendCodePoint(codePoint);
        fatherParseNode.setData(new StringValueData(stringBuilder.toString()));
    }
}
