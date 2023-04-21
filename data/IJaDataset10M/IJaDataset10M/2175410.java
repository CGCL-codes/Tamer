package verinec.netsim.processors.applications;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
import verinec.netsim.NetSimException;
import verinec.netsim.components.Node;
import verinec.netsim.constants.Layers;
import verinec.netsim.constants.Protocolls;
import verinec.netsim.loggers.ILogger;
import verinec.netsim.loggers.events.ApplicationEvent;
import verinec.netsim.loggers.events.Event;
import verinec.netsim.util.net.sockets.SimSocketImplFactory;
import verinec.netsim.util.tables.ThreadRegistry;
import desmoj.core.exception.SimFinishedException;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.NetSimProcess;

/**
 * @author Dominik Jungo
 * @version $Revision: 806 $
 */
public abstract class AbstractApplication extends NetSimProcess {

    /**
     * node on which the application runs
     */
    protected Node node;

    /**
	 * arguments used for the application
	 */
    protected String arguments;

    /**
	 * logger used by the application
	 */
    protected ILogger logger;

    /**
	 * Source
	 */
    protected String src;

    /**
	 * Destination
	 */
    protected String dst;

    /**
	 * java logger used by this class
	 */
    protected Logger jlogger;

    /**
	 * An abstract application
	 * 
	 * @param model
	 *            a network model
	 * @param name
	 *            name of the application
	 * @param node
	 *            node on which the application runs
	 * @param arguments
	 *            arguments used for the application
	 * @param logger
	 *            logger used by the application
	 * @param src
	 *            Source
	 * @param dst
	 *            Destination
	 */
    public AbstractApplication(Model model, String name, Node node, String arguments, ILogger logger, String src, String dst) {
        super(model, name, true);
        this.node = node;
        this.arguments = arguments;
        this.logger = logger;
        this.src = src;
        this.dst = dst;
        jlogger = Logger.getLogger(getClass().getName());
        if (this.logger == null) {
            try {
                this.logger = verinec.netsim.loggers.Logger.getInstance();
            } catch (NetSimException e) {
                e.printStackTrace();
            }
        }
    }

    /** Gets this application's logger. At the beginning it should be a top level logger.
	 * @return this application's logger
	 */
    public ILogger getLogger() {
        return logger;
    }

    /**
	 * @see desmoj.core.simulator.SimProcess#lifeCycle()
	 */
    public final void lifeCycle() {
        jlogger.entering(getClass().getName(), "lifeCycle");
        ThreadRegistry.addEntry(Thread.currentThread(), node);
        Event logevent = new Event(currentTime().toString(), node.getNodeID(), Layers.APPLICATION, Protocolls.APPLICATION);
        logevent.setEventDetail(new ApplicationEvent("start", getClass().getName(), arguments));
        logger.addEvent(logevent);
        logger = logevent;
        try {
            Socket.setSocketImplFactory(new SimSocketImplFactory());
        } catch (IOException e1) {
        }
        try {
            ServerSocket.setSocketFactory(new SimSocketImplFactory());
        } catch (IOException e1) {
        }
        try {
            applicationLifeCycle();
        } catch (SimFinishedException e) {
            jlogger.severe("Unfinished Process: " + getClass().getName() + " on node: " + node);
        } catch (Exception e) {
            jlogger.severe("Unfinished Process: " + getClass().getName() + " on node: " + node);
        }
        logevent = new Event(currentTime().toString(), node.getNodeID(), Layers.APPLICATION, Protocolls.APPLICATION);
        logevent.setEventDetail(new ApplicationEvent("end", getClass().getName(), arguments));
        logger.addEvent(logevent);
        jlogger.exiting(getClass().getName(), "lifeCycle");
    }

    /**
	 * runs the application
	 */
    public abstract void applicationLifeCycle();

    public void setLogger(ILogger logger2) {
        this.logger = logger2;
    }
}
