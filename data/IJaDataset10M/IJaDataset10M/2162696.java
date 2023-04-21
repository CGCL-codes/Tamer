package com.liferay.portlet.softwarecatalog.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portlet.softwarecatalog.service.SCProductEntryServiceUtil;
import java.rmi.RemoteException;

/**
 * <a href="SCProductEntryServiceSoap.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class provides a SOAP utility for the <code>com.liferay.portlet.softwarecatalog.service.SCProductEntryServiceUtil</code>
 * service utility. The static methods of this class calls the same methods of the
 * service utility. However, the signatures are different because it is difficult
 * for SOAP to support certain types.
 * </p>
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a <code>java.util.List</code>, that
 * is translated to an array of <code>com.liferay.portlet.softwarecatalog.model.SCProductEntrySoap</code>.
 * If the method in the service utility returns a <code>com.liferay.portlet.softwarecatalog.model.SCProductEntry</code>,
 * that is translated to a <code>com.liferay.portlet.softwarecatalog.model.SCProductEntrySoap</code>.
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
 * @see com.liferay.portlet.softwarecatalog.service.SCProductEntryServiceUtil
 * @see com.liferay.portlet.softwarecatalog.service.http.SCProductEntryServiceHttp
 * @see com.liferay.portlet.softwarecatalog.service.model.SCProductEntrySoap
 *
 */
public class SCProductEntryServiceSoap {

    public static com.liferay.portlet.softwarecatalog.model.SCProductEntrySoap addProductEntry(long plid, java.lang.String name, java.lang.String type, java.lang.String tags, java.lang.String shortDescription, java.lang.String longDescription, java.lang.String pageURL, java.lang.String author, java.lang.String repoGroupId, java.lang.String repoArtifactId, long[] licenseIds, java.util.List thumbnails, java.util.List fullImages, boolean addCommunityPermissions, boolean addGuestPermissions) throws RemoteException {
        try {
            com.liferay.portlet.softwarecatalog.model.SCProductEntry returnValue = SCProductEntryServiceUtil.addProductEntry(plid, name, type, tags, shortDescription, longDescription, pageURL, author, repoGroupId, repoArtifactId, licenseIds, thumbnails, fullImages, addCommunityPermissions, addGuestPermissions);
            return com.liferay.portlet.softwarecatalog.model.SCProductEntrySoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portlet.softwarecatalog.model.SCProductEntrySoap addProductEntry(long plid, java.lang.String name, java.lang.String type, java.lang.String tags, java.lang.String shortDescription, java.lang.String longDescription, java.lang.String pageURL, java.lang.String author, java.lang.String repoGroupId, java.lang.String repoArtifactId, long[] licenseIds, java.util.List thumbnails, java.util.List fullImages, java.lang.String[] communityPermissions, java.lang.String[] guestPermissions) throws RemoteException {
        try {
            com.liferay.portlet.softwarecatalog.model.SCProductEntry returnValue = SCProductEntryServiceUtil.addProductEntry(plid, name, type, tags, shortDescription, longDescription, pageURL, author, repoGroupId, repoArtifactId, licenseIds, thumbnails, fullImages, communityPermissions, guestPermissions);
            return com.liferay.portlet.softwarecatalog.model.SCProductEntrySoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static void deleteProductEntry(long productEntryId) throws RemoteException {
        try {
            SCProductEntryServiceUtil.deleteProductEntry(productEntryId);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portlet.softwarecatalog.model.SCProductEntrySoap getProductEntry(long productEntryId) throws RemoteException {
        try {
            com.liferay.portlet.softwarecatalog.model.SCProductEntry returnValue = SCProductEntryServiceUtil.getProductEntry(productEntryId);
            return com.liferay.portlet.softwarecatalog.model.SCProductEntrySoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    public static com.liferay.portlet.softwarecatalog.model.SCProductEntrySoap updateProductEntry(long productEntryId, java.lang.String name, java.lang.String type, java.lang.String tags, java.lang.String shortDescription, java.lang.String longDescription, java.lang.String pageURL, java.lang.String author, java.lang.String repoGroupId, java.lang.String repoArtifactId, long[] licenseIds, java.util.List thumbnails, java.util.List fullImages) throws RemoteException {
        try {
            com.liferay.portlet.softwarecatalog.model.SCProductEntry returnValue = SCProductEntryServiceUtil.updateProductEntry(productEntryId, name, type, tags, shortDescription, longDescription, pageURL, author, repoGroupId, repoArtifactId, licenseIds, thumbnails, fullImages);
            return com.liferay.portlet.softwarecatalog.model.SCProductEntrySoap.toSoapModel(returnValue);
        } catch (Exception e) {
            _log.error(e, e);
            throw new RemoteException(e.getMessage());
        }
    }

    private static Log _log = LogFactoryUtil.getLog(SCProductEntryServiceSoap.class);
}
