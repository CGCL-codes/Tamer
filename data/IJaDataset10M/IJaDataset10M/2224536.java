package verinec.netsim.loggers.events;

import verinec.netsim.NetSimException;
import verinec.netsim.loggers.ILogger;
import verinec.gui.core.DrawPanel;
import verinec.util.VerinecNamespaces;
import desmoj.core.simulator.Model;

/**
 * A consume Event detail is at the end of the chain of events.
 * 
 * @author Dominik Jungo
 * @version $Revision: 47 $
 */
public class ConsumeEvent extends EventDetail {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates a new ConsumeEvent
	 * 
	 * @param type
	 *            a type
	 */
    public ConsumeEvent(String type) {
        super("consume", type);
    }

    /**
     * Creates a new ConsumeEvent
     */
    public ConsumeEvent() {
        super("consume", VerinecNamespaces.SCHEMA_EVENTS);
    }

    /**
	 * @see verinec.netsim.loggers.events.EventDetail#createSimEvent(Model,
	 *      ILogger)
	 */
    public desmoj.core.simulator.Event createSimEvent(Model model, ILogger logger) {
        return null;
    }

    /**
     * @see verinec.netsim.loggers.events.EventDetail#initializeLabel(verinec.gui.core.DrawPanel)
     */
    public void initializeLabel(DrawPanel drawpanel) throws NetSimException {
    }

    /**
     * @see verinec.netsim.loggers.events.EventDetail#subanimate(int, int, int, int)
     */
    protected void subanimate(int time, int substep, int myPhase, int totalPhase) {
    }
}
