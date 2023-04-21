package votebox.auditoriumverifierplugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sexpression.ASExpression;
import sexpression.ListExpression;
import sexpression.ListWildcard;
import sexpression.NoMatch;
import sexpression.StringExpression;
import sexpression.StringWildcard;
import sexpression.Wildcard;
import verifier.FormatException;
import verifier.value.*;
import auditorium.IncorrectFormatException;
import auditorium.Message;
import auditorium.MessagePointer;

public class FastDAGBuilder extends DagBuilder {

    private static final ASExpression PATTERN = new ListExpression(StringExpression.makeString("signed-message"), Wildcard.SINGLETON, new ListExpression(StringExpression.makeString("signature"), StringWildcard.SINGLETON, StringWildcard.SINGLETON, new ListExpression(StringExpression.makeString("succeeds"), new ListWildcard(MessagePointer.PATTERN), Wildcard.SINGLETON)));

    private HashMap<Expression, List<Expression>> _predecessors;

    private HashMap<Expression, Expression> _msgToPtr;

    private Map<String, Map<Integer, Expression>> _timelines;

    public FastDAGBuilder() {
        _predecessors = new HashMap<Expression, List<Expression>>();
        _msgToPtr = new HashMap<Expression, Expression>();
        _timelines = new HashMap<String, Map<Integer, Expression>>();
    }

    public void add(Message message) throws FormatException {
        try {
            MessagePointer msgPtr = new MessagePointer(message);
            Expression ptr = new Expression(msgPtr.toASE());
            Expression expr = new Expression(message.toASE());
            _msgToPtr.put(expr, ptr);
            if (!_timelines.containsKey(msgPtr.getNodeId())) _timelines.put(msgPtr.getNodeId(), new HashMap<Integer, Expression>());
            _timelines.get(msgPtr.getNodeId()).put(new Integer(msgPtr.getNumber()), ptr);
            ASExpression matchresult = PATTERN.match(message.getDatum());
            if (matchresult == NoMatch.SINGLETON) throw new FormatException(message.getDatum(), new Exception("didn't match pattern for an Auditorium message: " + PATTERN));
            ListExpression matchlist = (ListExpression) matchresult;
            ArrayList<Expression> ptrlst = new ArrayList<Expression>();
            for (ASExpression ptrexp : (ListExpression) matchlist.get(3)) {
                ptrlst.add(new Expression(new MessagePointer(ptrexp).toASE()));
            }
            _predecessors.put(ptr, ptrlst);
        } catch (IncorrectFormatException e) {
            throw new FormatException(message.getDatum(), e);
        }
    }

    public DAGValue toDAG() {
        return new FastDAG(_msgToPtr, _predecessors, _timelines);
    }
}
