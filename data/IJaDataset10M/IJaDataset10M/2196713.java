package net.sf.parser4j.generator.service.match;

import net.sf.parser4j.generator.entity.grammardefnode.StatementDef;
import net.sf.parser4j.parser.entity.IParseSessionForMatchMgr;
import net.sf.parser4j.parser.entity.parsenode.CharacterParseNode;
import net.sf.parser4j.parser.entity.parsenode.IParseNode;
import net.sf.parser4j.parser.entity.parsenode.StringParseNode;
import net.sf.parser4j.parser.entity.parsenode.data.StringValueData;
import net.sf.parser4j.parser.service.IParseNodeDataAccess;
import net.sf.parser4j.parser.service.ParserException;
import net.sf.parser4j.parser.service.match.AbstractMatchMgrAdapter;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class MatchCharFromEscape extends AbstractMatchMgrAdapter {

    public MatchCharFromEscape(final IParseNodeDataAccess parseNodeDataAccess) {
        super(parseNodeDataAccess);
    }

    @Override
    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        final char terminalValue;
        if (CharacterParseNode.class.equals(fatherParseNode.getClass())) {
            terminalValue = (char) ((CharacterParseNode) parseNodes[notWhiteIndexes[1]]).getTerminalValue();
        } else {
            final IParseNode parseNode = parseNodes[notWhiteIndexes[1]];
            if (CharacterParseNode.class.equals(parseNode.getClass())) {
                final CharacterParseNode charParseNode = (CharacterParseNode) parseNode;
                terminalValue = (char) charParseNode.getTerminalValue();
            } else if (StringParseNode.class.equals(parseNode.getClass())) {
                terminalValue = ((StringParseNode) parseNode).getStringValue().charAt(0);
            } else {
                final StringValueData stringValueData = (StringValueData) parseNode.getUniqData();
                terminalValue = stringValueData.getValue().charAt(0);
            }
        }
        fatherParseNode.setData(new StatementDef(terminalValue));
    }
}
