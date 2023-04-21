package eulergui.parser.n3.impl.parser4j.service.match;

import eulergui.parser.n3.impl.parser4j.entity.data.LiteralValueData;
import net.sf.parser4j.parser.entity.IParseSessionForMatchMgr;
import net.sf.parser4j.parser.entity.parsenode.IParseNode;
import net.sf.parser4j.parser.entity.parsenode.data.StringValueData;
import net.sf.parser4j.parser.service.IParseNodeDataAccess;
import net.sf.parser4j.parser.service.ParserException;
import net.sf.parser4j.parser.service.match.AbstractMatchMgrAdapter;

public class _MatchDouble extends AbstractMatchMgrAdapter {

    public _MatchDouble(final IParseNodeDataAccess parseNodeDataAccess) {
        super(parseNodeDataAccess);
    }

    @Override
    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        IParseNode parseNode = parseNodes[notWhiteIndexes[0]];
        final StringValueData optionalSign = (StringValueData) getUniqData(parseSession, parseNode);
        parseNode = parseNodes[notWhiteIndexes[1]];
        final StringValueData digits = (StringValueData) getUniqData(parseSession, parseNode);
        parseNode = parseNodes[notWhiteIndexes[2]];
        final StringValueData optionalDecimalPart = (StringValueData) getUniqData(parseSession, parseNode);
        parseNode = parseNodes[notWhiteIndexes[3]];
        final StringValueData optionalExponant = (StringValueData) getUniqData(parseSession, parseNode);
        final StringValueData doubleValue = new StringValueData(optionalSign, digits, optionalDecimalPart, optionalExponant);
        fatherParseNode.setData(new LiteralValueData(LiteralValueData.EnumValueType.DOUBLE, doubleValue.getValue()));
    }
}
