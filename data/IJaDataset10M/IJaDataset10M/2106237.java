package com.liferay.portal.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.OrgLaborServiceUtil;
import java.rmi.RemoteException;

/**
 * <a href="OrgLaborServiceSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides a SOAP utility for the <code>com.liferay.portal.service.OrgLaborServiceUtil</code>
 * service utility. The static methods of this class calls the same methods of the
 * service utility. However, the signatures are different because it is difficult
 * for SOAP to support certain types.
 * </p>
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a <code>java.util.List</code>, that
 * is translated to an array of <code>com.liferay.portal.model.OrgLaborSoap</code>.
 * If the method in the service utility returns a <code>com.liferay.portal.model.OrgLabor</code>,
 * that is translated to a <code>com.liferay.portal.model.OrgLaborSoap</code>. Methods
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
 * @see com.liferay.portal.service.OrgLaborServiceUtil
 * @see com.liferay.portal.service.http.OrgLaborServiceHttp
 * @see com.liferay.portal.service.model.OrgLaborSoap
 *
 */
public class OrgLaborServiceSoap {

    public static com.liferay.portal.model.OrgLaborSoap addOrgLabor(long organizationId, int typeId, int sunOpen, int sunClose, int monOpen, int monClose, int tueOpen, int tueClose, int wedOpen, int wedClose, int thuOpen, int thuClose, int friOpen, int friClose, int satOpen, int satClose) throws RemoteException {
        try {
            com.liferay.portal.model.OrgLabor returnValue = OrgLaborServiceUtil.addOrgLabor(organizationId, typeId, sunOpen, sunClose, monOpen, monClose, tueOpen, tueClose, wedOpen, wedClose, thuOpen, thuClose, friOpen, friClose, satOpen, satClose);
            return com.liferay.portal.model.OrgLaborSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void deleteOrgLabor(long orgLaborId) throws RemoteException {
        try {
            OrgLaborServiceUtil.deleteOrgLabor(orgLaborId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.OrgLaborSoap getOrgLabor(long orgLaborId) throws RemoteException {
        try {
            com.liferay.portal.model.OrgLabor returnValue = OrgLaborServiceUtil.getOrgLabor(orgLaborId);
            return com.liferay.portal.model.OrgLaborSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.OrgLaborSoap[] getOrgLabors(long organizationId) throws RemoteException {
        try {
            java.util.List returnValue = OrgLaborServiceUtil.getOrgLabors(organizationId);
            return com.liferay.portal.model.OrgLaborSoap.toSoapModels(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.OrgLaborSoap updateOrgLabor(long orgLaborId, int sunOpen, int sunClose, int monOpen, int monClose, int tueOpen, int tueClose, int wedOpen, int wedClose, int thuOpen, int thuClose, int friOpen, int friClose, int satOpen, int satClose) throws RemoteException {
        try {
            com.liferay.portal.model.OrgLabor returnValue = OrgLaborServiceUtil.updateOrgLabor(orgLaborId, sunOpen, sunClose, monOpen, monClose, tueOpen, tueClose, wedOpen, wedClose, thuOpen, thuClose, friOpen, friClose, satOpen, satClose);
            return com.liferay.portal.model.OrgLaborSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    private static Log _log = LogFactoryUtil.getLog(OrgLaborServiceSoap.class);
}
