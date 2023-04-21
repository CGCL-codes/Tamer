package hotair.support.actuator;

import com.agentfactory.logic.agent.Actuator;
import com.agentfactory.logic.lang.FOS;

/**
 *
 * @author  remcollier
 */
public class CreateUniqueName extends Actuator {

    public boolean act(FOS action) {
        String name1 = action.argAt(0).toString();
        String name2 = action.argAt(1).toString();
        String link = (String) getConfiguration().get("link");
        adoptBelief("BELIEF(uniqueName(" + name1 + link + name2 + "," + name2 + "))");
        return true;
    }
}
