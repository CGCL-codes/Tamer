package huf.unibus;

import huf.log.Log;
import huf.log.ILogger;
import huf.misc.To;

/**
 * UniBus monitoring tool.
 *
 * <p>
 * This class monitors all UniBus traffic and outputs diagnostic information to the specified output stream.
 * To use this monitor add it to the bus you want to diagnose just as any other member.
 * </p>
 */
public class BusMonitor implements BusMember {

    /**
	 * Create new bus monitor.
	 */
    public BusMonitor() {
        this(Log.getLogger());
    }

    /**
	 * Create new bus monitor.
	 *
	 * @param log logger
	 */
    public BusMonitor(ILogger log) {
        if (log == null) {
            throw new IllegalArgumentException("Logger may not be null");
        }
        this.log = log;
    }

    /** Public due to implementation reasons. */
    @Override
    public void uniBusEvent(UniBus bus, BusEvent evt) {
        logEvent(" event:   ", bus, evt);
    }

    /** Public due to implementation reasons. */
    @Override
    public void uniBusRequest(UniBus bus, BusRequest rq) {
        logEvent(" request: ", bus, rq);
    }

    /** Logger used to log events. */
    private final ILogger log;

    /**
	 * Universal method to log bus event.
	 *
	 * @param typeName textual event type name
	 * @param bus bus
	 * @param evt event
	 */
    protected void logEvent(String typeName, UniBus bus, BusEvent evt) {
        StringBuilder dataStr = new StringBuilder();
        dataStr.append(bus.getName() + typeName);
        if (evt.getType() instanceof Integer) {
            dataStr.append(evt.getType().toString() + " <0x" + To.hexString(((Integer) evt.getType()).intValue()) + ">: ");
        } else {
            dataStr.append(evt.getType().toString() + ": ");
        }
        dataStr.append(evt.toString());
        log.log(new String(dataStr));
    }
}
