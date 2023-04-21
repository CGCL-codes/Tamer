package hotair.support.actuator;

import com.agentfactory.logic.agent.Actuator;
import com.agentfactory.logic.lang.FOS;

/**
 *
 * @author  remcollier
 */
public class SpecifyDataQueue extends Actuator {

    public boolean act(FOS action) {
        String name = action.argAt(0).toString();
        adoptBelief("BELIEF(dataQueueName(" + name + "Q))");
        return true;
    }
}
