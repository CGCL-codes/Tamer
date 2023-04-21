package verinec.netsim.processors.applications;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import verinec.netsim.NetSimException;
import verinec.netsim.components.Node;
import verinec.netsim.loggers.ILogger;
import desmoj.core.simulator.Model;

/**
 * @author Dominik Jungo
 * @version $Revision: 791 $
 */
public class ApplicationFactory {

    /**
	 * returns the Class for a requested Implementation of an application. The
	 * Application must be in <tt>verinec.netsim.processors.applications.</tt>
	 * 
	 * @param name
	 *            name of the application without the trailing
	 *            <tt>verinec.netsim.processors.applications.</tt>
	 * @return returns the Class for a requested Implementation
	 * @throws ClassNotFoundException
	 *             if the Application is not found
	 */
    private static Class getImpl(String name) throws ClassNotFoundException {
        return Class.forName("verinec.netsim.processors.applications." + name);
    }

    /**
	 * Creates a Application
	 * 
	 * @param model
	 *            a network model
	 * @param name
	 *            name of the application without the trailing
	 *            <tt>verinec.netsim.processors.applications.</tt>
	 * @param node
	 *            node on which the application should be instanciated
	 * @param arguments
	 *            arguments to be passed to the application on startup
	 * @param logger
	 *            logger used by the application
	 * @param src
	 *            source of the application's packets
	 * @param dst
	 *            destination of the application's packets
	 * @return an instance of the applictaion
	 * @throws NetSimException
	 *             when the Application can not be instanciated
	 */
    public static AbstractApplication createApplication(Model model, String name, Node node, String arguments, ILogger logger, String src, String dst) throws NetSimException {
        AbstractApplication returnvalue = null;
        try {
            Constructor c = getImpl(name).getConstructor(new Class[] { Model.class, Node.class, String.class, ILogger.class, String.class, String.class });
            returnvalue = (AbstractApplication) c.newInstance(new Object[] { model, node, arguments, logger, src, dst });
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            throw new NetSimException("Application cannot be instanciated", e);
        } catch (ClassNotFoundException e) {
            throw new NetSimException("Application Implementation not found: " + name, e);
        } catch (IllegalArgumentException e) {
            throw new NetSimException("Application cannot be instanciated", e);
        } catch (InstantiationException e) {
            throw new NetSimException("Application cannot be instanciated", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }

    public static AbstractApplication createService(Model model, Node node, String name, String parameters) throws NetSimException {
        AbstractApplication returnvalue = null;
        try {
            Constructor c = getImpl(name).getConstructor(new Class[] { Model.class, Node.class, String.class });
            returnvalue = (AbstractApplication) c.newInstance(new Object[] { model, node, parameters });
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            throw new NetSimException("Application cannot be instanciated", e);
        } catch (ClassNotFoundException e) {
            throw new NetSimException("Application Implementation not found: " + name, e);
        } catch (IllegalArgumentException e) {
            throw new NetSimException("Application cannot be instanciated", e);
        } catch (InstantiationException e) {
            throw new NetSimException("Application cannot be instanciated", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnvalue;
    }
}
