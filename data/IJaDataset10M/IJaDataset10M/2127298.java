package com.liferay.portal.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.MembershipRequestServiceUtil;
import java.rmi.RemoteException;

/**
 * <a href="MembershipRequestServiceSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides a SOAP utility for the <code>com.liferay.portal.service.MembershipRequestServiceUtil</code>
 * service utility. The static methods of this class calls the same methods of the
 * service utility. However, the signatures are different because it is difficult
 * for SOAP to support certain types.
 * </p>
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a <code>java.util.List</code>, that
 * is translated to an array of <code>com.liferay.portal.model.MembershipRequestSoap</code>.
 * If the method in the service utility returns a <code>com.liferay.portal.model.MembershipRequest</code>,
 * that is translated to a <code>com.liferay.portal.model.MembershipRequestSoap</code>.
 * Methods that SOAP cannot safely wire are skipped.
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
 * @see com.liferay.portal.service.MembershipRequestServiceUtil
 * @see com.liferay.portal.service.http.MembershipRequestServiceHttp
 * @see com.liferay.portal.service.model.MembershipRequestSoap
 *
 */
public class MembershipRequestServiceSoap {

    public static com.liferay.portal.model.MembershipRequestSoap addMembershipRequest(long groupId, java.lang.String comments) throws RemoteException {
        try {
            com.liferay.portal.model.MembershipRequest returnValue = MembershipRequestServiceUtil.addMembershipRequest(groupId, comments);
            return com.liferay.portal.model.MembershipRequestSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void deleteByGroupIdAndStatus(long groupId, int statusId) throws RemoteException {
        try {
            MembershipRequestServiceUtil.deleteByGroupIdAndStatus(groupId, statusId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portal.model.MembershipRequestSoap getMembershipRequest(long membershipRequestId) throws RemoteException {
        try {
            com.liferay.portal.model.MembershipRequest returnValue = MembershipRequestServiceUtil.getMembershipRequest(membershipRequestId);
            return com.liferay.portal.model.MembershipRequestSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void updateStatus(long membershipRequestId, java.lang.String reviewComments, int statusId) throws RemoteException {
        try {
            MembershipRequestServiceUtil.updateStatus(membershipRequestId, reviewComments, statusId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    private static Log _log = LogFactoryUtil.getLog(MembershipRequestServiceSoap.class);
}
