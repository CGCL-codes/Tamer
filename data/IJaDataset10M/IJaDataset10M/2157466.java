package com.liferay.portal.service;

/**
 * <a href="CountryServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides static methods for the <code>com.liferay.portal.service.CountryService</code>
 * bean. The static methods of this class calls the same methods of the bean instance.
 * It's convenient to be able to just write one line to call a method on a bean
 * instead of writing a lookup call and a method call.
 * </p>
 *
 * <p>
 * <code>com.liferay.portal.service.CountryServiceFactory</code> is responsible
 * for the lookup of the bean.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.CountryService
 * @see com.liferay.portal.service.CountryServiceFactory
 *
 */
public class CountryServiceUtil {

    public static java.util.List getCountries() throws com.liferay.portal.SystemException, java.rmi.RemoteException {
        CountryService countryService = CountryServiceFactory.getService();
        return countryService.getCountries();
    }

    public static java.util.List getCountries(boolean active) throws com.liferay.portal.SystemException, java.rmi.RemoteException {
        CountryService countryService = CountryServiceFactory.getService();
        return countryService.getCountries(active);
    }

    public static com.liferay.portal.model.Country getCountry(long countryId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException, java.rmi.RemoteException {
        CountryService countryService = CountryServiceFactory.getService();
        return countryService.getCountry(countryId);
    }
}
