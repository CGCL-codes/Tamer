package org.epoline.phoenix.manager.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import org.epoline.phoenix.authentication.shared.UserAuthenticator;
import org.epoline.phoenix.common.shared.PhoenixException;
import org.epoline.phoenix.common.shared.PhoenixObjectInUseException;

/**
 * Interface for the Management tool algorithm manager. Handles algorithm.
 */
public interface IServiceAlgorithm extends Remote {

    public static final String SERVICE_NAME = IServiceAlgorithm.class.getName();

    /**
	 * Add the supplied algorithm to the DMS.
	 * 
	 * @param user
	 * @param algorithm
	 * @throws PhoenixAuthenticationException the user is not properly
	 *         authenticated.
	 * @throws PhoenixAuthorisationException the user is not authorised to use
	 *         any of the specified functions.
	 * @throws PhoenixObjectInUseException the algorithm already exists
	 * @throws PhoenixException other server exception
	 */
    void addAlgorithm(UserAuthenticator user, ItemAlgorithm algorithm) throws PhoenixException, RemoteException;

    /**
	 * Returns list of ItemAlgorithms from the DMS.
	 * 
	 * @param user
	 * @param refresh algorithms in the cache
	 * @return list of algorithms
	 * @throws PhoenixAuthenticationException the user is not properly
	 *         authenticated.
	 * @throws PhoenixAuthorisationException the user is not authorised to use
	 *         any of the specified functions.
	 * @throws PhoenixException server exception
	 * @see org.epoline.phoenix.manager.shared.ItemAlgorithm
	 */
    List getAlgorithms(UserAuthenticator user, boolean refreshCache) throws PhoenixException, RemoteException;

    /**
	 * Returns list of algorithm teams connected to this algorithm represented
	 * by their names.
	 * 
	 * @param algorithm
	 * @return list of algorithm team names
	 * @throws PhoenixAuthenticationException the user is not properly
	 *         authenticated.
	 * @throws PhoenixAuthorisationException the user is not authorised to use
	 *         any of the specified functions.
	 * @throws PhoenixObjectInUseException the algorithm does not exist
	 * @throws PhoenixException other server exception
	 */
    java.util.List getAlgorithmTeams(UserAuthenticator user, ItemAlgorithm algorithm) throws PhoenixException, RemoteException;

    /**
	 * Checks if algorithm with a given name already exists in the DMS.
	 * 
	 * @param user
	 * @param name
	 * @return true if the algorithm with this name is found in the dms, false
	 *         otherwise
	 * @throws PhoenixAuthenticationException the user is not properly
	 *         authenticated.
	 * @throws PhoenixAuthorisationException the user is not authorised to use
	 *         any of the specified functions.
	 * @throws PhoenixException other server exception
	 */
    boolean isExistingAlgorithm(UserAuthenticator user, String name) throws PhoenixException, RemoteException;

    /**
	 * Remove the supplied algorithm from the DMS. Algorithm must exist in the
	 * DMS, and not have algorithm items connected too it.
	 * 
	 * @param user
	 * @param algorithm
	 * @throws PhoenixAuthenticationException the user is not properly
	 *         authenticated.
	 * @throws PhoenixAuthorisationException the user is not authorised to use
	 *         any of the specified functions.
	 * @throws PhoenixObjectNotFoundException the algorithm does not exists
	 * @throws PhoenixObjectInUseException the algorithm has algorithm items
	 *         connected to it
	 * @throws PhoenixException other server exception
	 */
    void removeAlgorithm(UserAuthenticator user, ItemAlgorithm algorithm) throws PhoenixException, RemoteException;

    /**
	 * Update the supplied algorithm. Algorithm must exist in the dms.
	 * 
	 * @param user
	 * @param algorithm
	 * @throws PhoenixAuthenticationException the user is not properly
	 *         authenticated.
	 * @throws PhoenixAuthorisationException the user is not authorised to use
	 *         any of the specified functions.
	 * @throws PhoenixObjectNotFoundException the algorithm does not exists
	 * @throws PhoenixException other server exception
	 */
    void updateAlgorithm(UserAuthenticator user, ItemAlgorithm Algorithm) throws PhoenixException, RemoteException;
}
