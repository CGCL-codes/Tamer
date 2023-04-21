package com.liferay.portlet.wiki.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portlet.wiki.service.WikiNodeServiceUtil;
import java.rmi.RemoteException;

/**
 * <a href="WikiNodeServiceSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides a SOAP utility for the <code>com.liferay.portlet.wiki.service.WikiNodeServiceUtil</code>
 * service utility. The static methods of this class calls the same methods of the
 * service utility. However, the signatures are different because it is difficult
 * for SOAP to support certain types.
 * </p>
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a <code>java.util.List</code>, that
 * is translated to an array of <code>com.liferay.portlet.wiki.model.WikiNodeSoap</code>.
 * If the method in the service utility returns a <code>com.liferay.portlet.wiki.model.WikiNode</code>,
 * that is translated to a <code>com.liferay.portlet.wiki.model.WikiNodeSoap</code>.
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
 * @see com.liferay.portlet.wiki.service.WikiNodeServiceUtil
 * @see com.liferay.portlet.wiki.service.http.WikiNodeServiceHttp
 * @see com.liferay.portlet.wiki.service.model.WikiNodeSoap
 *
 */
public class WikiNodeServiceSoap {

    public static com.liferay.portlet.wiki.model.WikiNodeSoap addNode(long plid, java.lang.String name, java.lang.String description, boolean addCommunityPermissions, boolean addGuestPermissions) throws RemoteException {
        try {
            com.liferay.portlet.wiki.model.WikiNode returnValue = WikiNodeServiceUtil.addNode(plid, name, description, addCommunityPermissions, addGuestPermissions);
            return com.liferay.portlet.wiki.model.WikiNodeSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portlet.wiki.model.WikiNodeSoap addNode(long plid, java.lang.String name, java.lang.String description, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws RemoteException {
        try {
            com.liferay.portlet.wiki.model.WikiNode returnValue = WikiNodeServiceUtil.addNode(plid, name, description, communityPermissions, guestPermissions);
            return com.liferay.portlet.wiki.model.WikiNodeSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void deleteNode(long nodeId) throws RemoteException {
        try {
            WikiNodeServiceUtil.deleteNode(nodeId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portlet.wiki.model.WikiNodeSoap getNode(long nodeId) throws RemoteException {
        try {
            com.liferay.portlet.wiki.model.WikiNode returnValue = WikiNodeServiceUtil.getNode(nodeId);
            return com.liferay.portlet.wiki.model.WikiNodeSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portlet.wiki.model.WikiNodeSoap updateNode(long nodeId, java.lang.String name, java.lang.String description) throws RemoteException {
        try {
            com.liferay.portlet.wiki.model.WikiNode returnValue = WikiNodeServiceUtil.updateNode(nodeId, name, description);
            return com.liferay.portlet.wiki.model.WikiNodeSoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    private static Log _log = LogFactoryUtil.getLog(WikiNodeServiceSoap.class);
}
