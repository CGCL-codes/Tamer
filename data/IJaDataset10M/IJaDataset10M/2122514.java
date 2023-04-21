package ar.edu.unicen.exa.server.ia.variable;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import ar.edu.unicen.exa.server.ia.serverLogic.ScrumModelAccess;
import ar.edu.unicen.exa.server.ia.variable.datatypes.IATimeElapsedFloatQueryResult;
import ar.edu.unicen.exa.server.ia.variable.datatypes.IATimeElapsedFloatResult;
import ar.edu.unicen.exa.server.ia.variable.datatypes.IATimeElapsedIntegerQueryResult;
import ar.edu.unicen.exa.server.ia.variable.datatypes.IATimeElapsedIntegerResult;
import ar.edu.unicen.exa.server.ia.variable.datatypes.IATimeElapsedQueryResult;
import ar.edu.unicen.exa.server.ia.variable.datatypes.IATimeElapsedResult;
import ar.edu.unicen.exa.server.ia.variable.datatypes.IATimeElapsedStringQueryResult;
import ar.edu.unicen.exa.server.ia.variable.datatypes.IATimeElapsedStringResult;
import common.ia.datatypes.UserEvent;

public class IATimeElapsed extends IAUserEventTypeVariable {

    private UserEvent.Type from, to;

    public IATimeElapsed(UserEvent.Type from, UserEvent.Type to) {
        super();
        this.from = from;
        this.to = to;
    }

    /**
	 * @author El Bizarro
	 */
    @Override
    public ArrayList<Object> calculate(String user, String value) {
        ArrayList<Object> result = new ArrayList<Object>();
        ArrayList<Object> eventSequence = ScrumModelAccess.getInstance().eventSequence(user, from, to, value);
        Iterator<Object> it = eventSequence.iterator();
        while (it.hasNext()) {
            IATimeElapsedQueryResult iATimeElapsedQueryResult = (IATimeElapsedQueryResult) it.next();
            IATimeElapsedResult iATimeElapsedResult;
            Date elapsedTime = new Date(iATimeElapsedQueryResult.getDatetime().getTime() - iATimeElapsedQueryResult.getTiempoInicial().getTime());
            if (value.equals("stringvalue")) iATimeElapsedResult = new IATimeElapsedStringResult(user, elapsedTime, from.toString(), to.toString(), ((IATimeElapsedStringQueryResult) iATimeElapsedQueryResult).getStringvalue()); else if (value.equals("intvalue")) iATimeElapsedResult = new IATimeElapsedIntegerResult(user, elapsedTime, from.toString(), to.toString(), ((IATimeElapsedIntegerQueryResult) iATimeElapsedQueryResult).getIntvalue()); else iATimeElapsedResult = new IATimeElapsedFloatResult(user, elapsedTime, from.toString(), to.toString(), ((IATimeElapsedFloatQueryResult) iATimeElapsedQueryResult).getFloatvalue());
            result.add(iATimeElapsedResult);
        }
        return result;
    }

    @Override
    public Object calculate(UserEvent event) {
        return null;
    }
}
