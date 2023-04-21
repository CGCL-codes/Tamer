package ca.gc.drdc_rddc.atlantic.hla;

import java.util.Set;

/**
 * Polka version of FederateAmbassador callbacks which don't belong to other
 * classes. Apr 5, 2006 3:09:22 PM
 * 
 * @author dillman
 */
public interface PolkaFederateAmbassador {

    /**
	 * Called when a sync point is announced. Apr 5, 2006 3:10:12 PM
	 * 
	 * @author dillman
	 * @param sp
	 *            the announced sync point
	 * @param tag
	 *            HLA tag on the announcement.
	 * @throws Exception
	 */
    public void announceSynchronizationPoint(HLASyncPoint sp, Object tag) throws Exception;

    /**
	 * Called when all participants acheive a sync point. Apr 5, 2006 3:10:54 PM
	 * 
	 * @author dillman
	 * @param sp
	 *            the sync point which has been acheived successfully by all
	 *            participants.
	 * @throws Exception
	 */
    public void federationSynchronized(HLASyncPoint sp) throws Exception;

    /**
	 * Called when a request for an attribute update is received. By default,
	 * re-send the last known values of all parameters in an object instance.
	 * Note this may not be appropriate for attributes such as dead reckoning
	 * parameters or position data which changes with time. Apr 5, 2006 3:11:39
	 * PM
	 * 
	 * @author dillman
	 * @param obj
	 *            the object for which the update is requested.
	 * @param attrs
	 *            the attribute names for which the update is requested.
	 * @throws Exception
	 */
    public void provideAttributeValueUpdate(HLAObject obj, Set<String> attrs) throws Exception;

    /**
	 * Save the current federate state. Apr 26, 2006 10:00:57 AM
	 * 
	 * @author dillman
	 * @param saveLabel
	 *            the label for the saved state.
	 * @throws Exception
	 */
    public void save(String saveLabel) throws Exception;

    /**
	 * Restore the federate state. Apr 26, 2006 10:00:59 AM
	 * 
	 * @author dillman
	 * @param saveLabel
	 *            the label for the saved state.
	 * @throws Exception
	 */
    public void restore(String saveLabel) throws Exception;

    public void objectInstanceNameReservationFailed(String objectName) throws Exception;

    public void objectInstanceNameReservationSucceeded(String objectName) throws Exception;
}
