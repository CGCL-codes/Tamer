package de.fmui.cmis.fileshare.bindings.soap;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import de.fmui.cmis.fileshare.CMISFileShareException;
import de.fmui.cmis.fileshare.CallContext;
import de.fmui.cmis.fileshare.IFileShareRepository;
import de.fmui.cmis.fileshare.Util;
import de.fmui.cmis.fileshare.jaxb.CmisException;
import de.fmui.cmis.fileshare.jaxb.CmisExtensionType;
import de.fmui.cmis.fileshare.jaxb.CmisObjectInFolderContainerType;
import de.fmui.cmis.fileshare.jaxb.CmisObjectInFolderListType;
import de.fmui.cmis.fileshare.jaxb.CmisObjectInFolderType;
import de.fmui.cmis.fileshare.jaxb.CmisObjectListType;
import de.fmui.cmis.fileshare.jaxb.CmisObjectParentsType;
import de.fmui.cmis.fileshare.jaxb.CmisObjectType;
import de.fmui.cmis.fileshare.jaxb.EnumIncludeRelationships;
import de.fmui.cmis.fileshare.jaxb.EnumServiceException;
import de.fmui.cmis.fileshare.jaxb.NavigationServicePort;

/**
 * CMIS Navigation Service.
 * 
 * @author Florian Müller
 */
@WebService(endpointInterface = "de.fmui.cmis.fileshare.jaxb.NavigationServicePort")
public class NavigationService extends AbstractService implements NavigationServicePort {

    @Resource
    WebServiceContext fContext;

    /**
	 * getDescendants.
	 */
    public List<CmisObjectInFolderContainerType> getDescendants(String repositoryId, String folderId, BigInteger depth, String filter, Boolean includeAllowableActions, EnumIncludeRelationships includeRelationships, String renditionFilter, Boolean includePathSegments, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (folderId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Folder Id must be set.", 0, null);
        }
        int d = (depth == null ? 2 : depth.intValue());
        boolean iaa = (includeAllowableActions == null ? false : includeAllowableActions.booleanValue());
        boolean ips = (includePathSegments == null ? false : includePathSegments.booleanValue());
        if (d == 0) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Depth must not be 0!", 0, null);
        }
        try {
            return fsr.getDescendants(context, folderId, false, d, filter, iaa, ips, null, -1);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getFolderTree.
	 */
    public List<CmisObjectInFolderContainerType> getFolderTree(String repositoryId, String folderId, BigInteger depth, String filter, Boolean includeAllowableActions, EnumIncludeRelationships includeRelationships, String renditionFilter, Boolean includePathSegments, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (folderId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Folder Id must be set.", 0, null);
        }
        int d = (depth == null ? 2 : depth.intValue());
        boolean iaa = (includeAllowableActions == null ? false : includeAllowableActions.booleanValue());
        boolean ips = (includePathSegments == null ? false : includePathSegments.booleanValue());
        if (d == 0) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Depth must not be 0!", 0, null);
        }
        try {
            return fsr.getDescendants(context, folderId, true, d, filter, iaa, ips, null, -1);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getChildren.
	 */
    public CmisObjectInFolderListType getChildren(String repositoryId, String folderId, String filter, String orderBy, Boolean includeAllowableActions, EnumIncludeRelationships includeRelationships, String renditionFilter, Boolean includePathSegments, BigInteger maxItems, BigInteger skipCount, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (folderId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Folder Id must be set.", 0, null);
        }
        boolean iaa = (includeAllowableActions == null ? false : includeAllowableActions.booleanValue());
        boolean ips = (includePathSegments == null ? false : includePathSegments.booleanValue());
        int max = (maxItems != null ? maxItems.intValue() + 1 + (skipCount != null ? skipCount.intValue() : 0) : -1);
        try {
            List<CmisObjectInFolderContainerType> descendants = fsr.getDescendants(context, folderId, false, 1, filter, iaa, ips, orderBy, max);
            List<CmisObjectInFolderType> children = new ArrayList<CmisObjectInFolderType>();
            for (CmisObjectInFolderContainerType desc : descendants) {
                children.add(desc.getObjectInFolder());
            }
            CmisObjectInFolderListType result = new CmisObjectInFolderListType();
            if ((maxItems == null) && (skipCount == null)) {
                result.getObjects().addAll(children);
                result.setNumItems(BigInteger.valueOf(children.size()));
                result.setHasMoreItems(false);
            } else {
                result.setNumItems(null);
                result.setHasMoreItems(Util.subList(children, maxItems, skipCount, result.getObjects()));
            }
            return result;
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getFolderParent.
	 */
    public CmisObjectType getFolderParent(String repositoryId, String folderId, String filter, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (folderId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Folder Id must be set.", 0, null);
        }
        try {
            CmisObjectParentsType parent = fsr.getParent(context, folderId, filter, false, false);
            if (parent == null) {
                throw createException(EnumServiceException.CONSTRAINT, "Folder has no parent.", 0, null);
            }
            return parent.getObject();
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getObjectParents.
	 */
    public List<CmisObjectParentsType> getObjectParents(String repositoryId, String objectId, String filter, Boolean includeAllowableActions, EnumIncludeRelationships includeRelationships, String renditionFilter, Boolean includeRelativePathSegment, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (objectId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Object Id must be set.", 0, null);
        }
        boolean iaa = (includeAllowableActions == null ? false : includeAllowableActions.booleanValue());
        boolean irps = (includeRelativePathSegment == null ? false : includeRelativePathSegment.booleanValue());
        try {
            List<CmisObjectParentsType> result = new ArrayList<CmisObjectParentsType>();
            CmisObjectParentsType parent = fsr.getParent(context, objectId, filter, iaa, irps);
            if (parent != null) {
                result.add(parent);
            }
            return result;
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getCheckedoutDocs.
	 */
    public CmisObjectListType getCheckedOutDocs(String repositoryId, String folderId, String filter, String orderBy, Boolean includeAllowableActions, EnumIncludeRelationships includeRelationships, String renditionFilter, BigInteger maxItems, BigInteger skipCount, CmisExtensionType extension) throws CmisException {
        createContext(fContext);
        getRepository(repositoryId);
        CmisObjectListType result = new CmisObjectListType();
        result.setHasMoreItems(false);
        result.setNumItems(BigInteger.ZERO);
        return result;
    }
}
