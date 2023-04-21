package net.sf.parser4j.generator.service.match;

import java.util.List;
import net.sf.parser4j.generator.entity.grammardefnode.CharListDef;
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
public class MatchStrListChar extends AbstractMatchMgrAdapter {

    public MatchStrListChar(final IParseNodeDataAccess parseNodeDataAccess) {
        super(parseNodeDataAccess);
    }

    @Override
    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        final List<Character> charList = ((CharListDef) getUniqData(parseSession, parseNodes[notWhiteIndexes[0]])).getCharList();
        final char terminalValue = (char) ((StatementDef) getUniqData(parseSession, parseNodes[notWhiteIndexes[1]])).getTerminalCharValue();
        final CharListDef resultCharListDef = new CharListDef(charList, terminalValue);
        fatherParseNode.setData(resultCharListDef);
    }
}
