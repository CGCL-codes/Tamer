package ws.prova.reference2.messaging.where;

import java.util.Map;

public interface WhereNode {

    boolean evaluate(Map vars, Map<Object, Object> vars2);
}
