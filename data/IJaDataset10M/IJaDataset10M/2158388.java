package ie.deri.wsmx.scheduler.transport;

import ie.deri.wsmx.exceptions.TransportException;
import ie.deri.wsmx.scheduler.Event;
import org.apache.log4j.Logger;
import org.wsmo.execution.common.nonwsmo.Context;

/**
 * A Transport that prefers locally available components
 * and attempt to publish the task to a space only
 * if no local components can handle it.
 *
 * <pre>
 * Created on Sep 3, 2005
 * Committed by $Author: haselwanter $
 * $Source: /cygdrive/e/progs/cygwin/usr/maczar/cvsbackup/core/src/main/ie/deri/wsmx/scheduler/transport/OptimizedSpaceTransport.java,v $
 * </pre>
 * 
 * @author Thomas Haselwanter
 * @author Michal Zaremba
 *
 * @version $Revision: 1.1 $ $Date: 2006-02-17 11:38:15 $
 */
public class OptimizedSpaceTransport implements Transport {

    private static final long serialVersionUID = 8336579666241420198L;

    static Logger logger = Logger.getLogger(JavaSpaceTransport.class);

    LocalTransport localTransport;

    public OptimizedSpaceTransport(String spaceLocation) throws TransportException {
        super();
        localTransport = new LocalTransport();
    }

    public void send(Event event, Object... modifiers) throws IllegalArgumentException, TransportException {
    }

    public Event receive(Object... modifiers) throws IllegalArgumentException, TransportException {
        return null;
    }

    public Event receiveIfExists(Object... modifiers) throws IllegalArgumentException, TransportException {
        return null;
    }

    public void send(Event event, ie.deri.wsmx.executionsemantic.WSMXExecutionSemantic.Event eventType, Context context) throws IllegalArgumentException, TransportException {
    }

    public Event receive(ie.deri.wsmx.executionsemantic.WSMXExecutionSemantic.Event eventType, Context context) throws IllegalArgumentException, TransportException {
        return null;
    }

    public Event receiveIfExists(ie.deri.wsmx.executionsemantic.WSMXExecutionSemantic.Event eventType, Context context) throws IllegalArgumentException, TransportException {
        return null;
    }
}
