package eulergui.parser.n3.impl.parser4j.service.match;

import eulergui.parser.n3.impl.parser4j.entity.session.N3ParserSession;
import net.sf.parser4j.parser.entity.IParseSessionForMatchMgr;
import net.sf.parser4j.parser.entity.parsenode.IParseNode;
import net.sf.parser4j.parser.entity.parsenode.data.StringValueData;
import net.sf.parser4j.parser.entity.parsenode.status.impl.DefaultParseNodeInErrorStatus;
import net.sf.parser4j.parser.entity.parsenode.status.impl.StatusSetterTrace;
import net.sf.parser4j.parser.service.IParseNodeDataAccess;
import net.sf.parser4j.parser.service.ParserException;
import net.sf.parser4j.parser.service.match.AbstractMatchMgrAdapter;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class _MatchNullCharacter extends AbstractMatchMgrAdapter {

    private static final N3ParserSession n3ParserSession = N3ParserSession.getInstance();

    public _MatchNullCharacter(final IParseNodeDataAccess parseNodeDataAccess) {
        super(parseNodeDataAccess);
    }

    @Override
    public void reduceAction(final IParseSessionForMatchMgr parseSession, final IParseNode fatherParseNode, final IParseNode[] parseNodes, final int[] notWhiteIndexes) throws ParserException {
        fatherParseNode.setData(new StringValueData((char) 0));
        if (n3ParserSession.isStrictRdf(parseSession)) {
            fatherParseNode.addErrorStatus(new DefaultParseNodeInErrorStatus(fatherParseNode, "\\0 not allowed in rdf", new StatusSetterTrace()));
        }
    }
}
