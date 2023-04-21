package com.liferay.portal.service;

/**
 * <a href="RegionService.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface defines the service. The default implementation is <code>com.liferay.portal.service.impl.RegionServiceImpl</code>.
 * Modify methods in that class and rerun ServiceBuilder to populate this class
 * and all other generated classes.
 * </p>
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security
 * checks based on the propagated JAAS credentials because this service can be accessed
 * remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.RegionServiceFactory
 * @see com.liferay.portal.service.RegionServiceUtil
 *
 */
public interface RegionService {

    public java.util.List getRegions() throws com.liferay.portal.SystemException, java.rmi.RemoteException;

    public java.util.List getRegions(long countryId) throws com.liferay.portal.SystemException, java.rmi.RemoteException;

    public java.util.List getRegions(boolean active) throws com.liferay.portal.SystemException, java.rmi.RemoteException;

    public java.util.List getRegions(long countryId, boolean active) throws com.liferay.portal.SystemException, java.rmi.RemoteException;

    public com.liferay.portal.model.Region getRegion(long regionId) throws com.liferay.portal.SystemException, com.liferay.portal.PortalException, java.rmi.RemoteException;
}
