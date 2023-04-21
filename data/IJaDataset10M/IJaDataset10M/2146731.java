package afbenchmark;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.agentfactory.logic.agent.Actuator;
import com.agentfactory.logic.lang.FOS;

/**
 *
 * @author  Haja
 */
public class PongedActuator extends Actuator {

    private static String LOGNAME = PongedActuator.class.getName();

    private static Logger logger = Logger.getLogger(LOGNAME);

    static {
        logger.setLevel(Level.WARNING);
    }

    /** Creates a new instance of PongedActuator */
    public PongedActuator() {
        super();
    }

    @Override
    public boolean act(FOS action) {
        logger.info("Pong [" + agent.getIterations() + "] by " + action.getArguments().get(0));
        return true;
    }
}
