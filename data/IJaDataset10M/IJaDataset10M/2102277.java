package com.liferay.portal.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.CountryServiceUtil;
import java.rmi.RemoteException;

/**
 * <a href="CountryServiceSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides a SOAP utility for the <code>com.liferay.portal.service.CountryServiceUtil</code>
 * service utility. The static methods of this class calls the same methods of the
 * service utility. However, the signatures are different because it is difficult
 * for SOAP to support certain types.
 * </p>
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a <code>java.util.List</code>, that
 * is translated to an array of <code>com.liferay.portal.model.CountrySoap</code>.
 * If the method in the service utility returns a <code>com.liferay.portal.model.Country</code>,
 * that is translated to a <code>com.liferay.portal.model.CountrySoap</code>. Methods
 * that SOAP cannot safely wire are skipped.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform compatible.
 * SOAP allows different languages like Java, .NET, C++, PHP, and even Perl, to
 * call the generated services. One drawback of SOAP is that it is slow because
 * it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/tunnel-web/secure/axis.
 * Set the property <code>tunnel.servlet.hosts.allowed</code> in portal.properties
 * to configure security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.CountryServiceUtil
 * @see com.liferay.portal.service.http.CountryServiceHttp
 * @see com.liferay.portal.service.model.CountrySoap
 *
 */
public class CountryServiceSoap {

    public static com.liferay.portal.model.CountrySoap[] getCountries() throws RemoteException {
        try {
            java.util.List returnValue = CountryServiceUtil.getCountries();
            return com.liferay.portal.model.CountrySoap.toSoapModels(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.CountrySoap[] getCountries(boolean active) throws RemoteException {
        try {
            java.util.List returnValue = CountryServiceUtil.getCountries(active);
            return com.liferay.portal.model.CountrySoap.toSoapModels(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.CountrySoap getCountry(long countryId) throws RemoteException {
        try {
            com.liferay.portal.model.Country returnValue = CountryServiceUtil.getCountry(countryId);
            return com.liferay.portal.model.CountrySoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    private static Log _log = LogFactoryUtil.getLog(CountryServiceSoap.class);
}
