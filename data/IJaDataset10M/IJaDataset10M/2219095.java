package org.likken.web.states;

import org.likken.web.Constants;
import org.likken.web.RequestParameters;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 * <code>StateFactory</code> creates the <code>State</code> classes.
 * @author Stephane Boisson
 * @version $Revision: 1.6 $ $Date: 2004/02/03 13:24:59 $
 */
public class StateFactory {

    protected static Logger logger = Logger.getLogger(StateFactory.class);

    /**
	 * This member is used to implement the Singleton pattern.
	 * @see #getInstance()
	 */
    private static StateFactory instance;

    /**
	 * Maps the name of a state to its matching class.
	 */
    private static HashMap mappings;

    static {
        mappings = new HashMap();
        mappings.put(Constants.States.LOGIN, LoginState.class);
        mappings.put(Constants.States.BROWSING, BrowsingState.class);
        mappings.put(Constants.States.ENTRY_DISPLAY, EntryDisplayState.class);
        mappings.put(Constants.States.ENTRY_EDITION, EntryEditionState.class);
        mappings.put(Constants.States.ATTRIBUTE_DOWNLOAD, AttributeDownloadState.class);
    }

    /**
	 * This class implements the Singleton pattern. Use {@link #getInstance()}
	 * in order to create a new instance of this class.
	 * @see #getInstance()
	 */
    private StateFactory() {
    }

    /**
	 * Returns an instance of the <code>StateFactory</code>.
	 * @return an instance of the <code>StateFactory</code>.
	 */
    public static StateFactory getInstance() {
        if (instance == null) {
            instance = new StateFactory();
        }
        return instance;
    }

    /**
     * Returns the <code>State</code> specified by the parameter <code>params</code>.
     * @param params the <code>RequestParameters</code> used to identify the <code>State</code>.
     * @return the <code>State</code> specified by the parameter <code>params</code>.
     * @throws StateException if no <code>State</code> was specified or it couldn't be identified.
     */
    public static State getState(final RequestParameters params) throws StateException {
        if (logger.isDebugEnabled()) {
            logger.debug("Calling getState(params) with path info '" + params.getPathInfo() + "'");
        }
        if (params.getPathInfo() != null) {
            String stateName = params.getPathInfo().substring(1);
            if (stateName.indexOf('/') > 0) {
                stateName = stateName.substring(0, stateName.indexOf('/'));
            }
            if (stateName.length() > 0) {
                return getState(stateName);
            }
        }
        StateException ex = new StateException("Unable to find state name in '" + params.getPathInfo() + "'");
        logger.error(ex.getMessage());
        throw ex;
    }

    /**
     * Returns the specified <code>State</code>.
     * @param name the name of the <code>State</code>.
     * @return the specified <code>State</code>.
     * @throws StateException if no class is mapped for the state or no instance of it can be created.
     */
    public static State getState(final String name) throws StateException {
        if (logger.isDebugEnabled()) {
            logger.debug("Calling getState(name) with '" + name + "'");
        }
        Object notCastedStateClass = mappings.get(name);
        if (notCastedStateClass == null) {
            logger.error("getState(String) was called with a state name, for which no class is mapped");
            throw new StateException("There's no mapped class for the state '" + name + "'.");
        }
        Class stateType = (Class) notCastedStateClass;
        try {
            return (State) stateType.newInstance();
        } catch (final IllegalAccessException e) {
            throw new StateException(e);
        } catch (final InstantiationException e) {
            throw new StateException(e);
        }
    }
}
