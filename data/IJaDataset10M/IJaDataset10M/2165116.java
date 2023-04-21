package de.fmui.cmis.fileshare.bindings.soap;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceContext;
import de.fmui.cmis.fileshare.CMISFileShareException;
import de.fmui.cmis.fileshare.CallContext;
import de.fmui.cmis.fileshare.IFileShareRepository;
import de.fmui.cmis.fileshare.Util;
import de.fmui.cmis.fileshare.jaxb.CmisAccessControlListType;
import de.fmui.cmis.fileshare.jaxb.CmisAllowableActionsType;
import de.fmui.cmis.fileshare.jaxb.CmisContentStreamType;
import de.fmui.cmis.fileshare.jaxb.CmisException;
import de.fmui.cmis.fileshare.jaxb.CmisExtensionType;
import de.fmui.cmis.fileshare.jaxb.CmisObjectType;
import de.fmui.cmis.fileshare.jaxb.CmisPropertiesType;
import de.fmui.cmis.fileshare.jaxb.CmisRenditionType;
import de.fmui.cmis.fileshare.jaxb.CmisTypeDefinitionType;
import de.fmui.cmis.fileshare.jaxb.EnumBaseObjectTypeIds;
import de.fmui.cmis.fileshare.jaxb.EnumIncludeRelationships;
import de.fmui.cmis.fileshare.jaxb.EnumPropertiesBase;
import de.fmui.cmis.fileshare.jaxb.EnumServiceException;
import de.fmui.cmis.fileshare.jaxb.EnumUnfileObject;
import de.fmui.cmis.fileshare.jaxb.EnumVersioningState;
import de.fmui.cmis.fileshare.jaxb.ObjectServicePort;

/**
 * CMIS Object Service.
 * 
 * @author Florian Müller
 */
@WebService(endpointInterface = "de.fmui.cmis.fileshare.jaxb.ObjectServicePort")
public class ObjectService extends AbstractService implements ObjectServicePort {

    @Resource
    WebServiceContext fContext;

    /**
	 * createDocument.
	 */
    public void createDocument(String repositoryId, CmisPropertiesType properties, String folderId, CmisContentStreamType contentStream, EnumVersioningState versioningState, List<String> policies, CmisAccessControlListType addACEs, CmisAccessControlListType removeACEs, Holder<CmisExtensionType> extension, Holder<String> objectId) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (folderId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Folder Id must be set.", 0, null);
        }
        if (properties == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Properties must be set.", 0, null);
        }
        String typeId = Util.getFirstIdValue(properties, EnumPropertiesBase.CMIS_OBJECT_TYPE_ID.value());
        CmisTypeDefinitionType type = RM.getTypes().getTypeDefinition(context, typeId);
        if (type == null) {
            throw createException(EnumServiceException.OBJECT_NOT_FOUND, "Type '" + typeId + "' is unknown!", 0, null);
        }
        if (!EnumBaseObjectTypeIds.CMIS_DOCUMENT.value().equals(type.getBaseId().value())) {
            throw createException(EnumServiceException.CONSTRAINT, "Not a document type.", 0, null);
        }
        try {
            objectId.value = fsr.createFile(context, typeId, folderId, properties, contentStream, versioningState);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * createDocumentFromSource.
	 */
    public void createDocumentFromSource(String repositoryId, String sourceId, CmisPropertiesType properties, String folderId, EnumVersioningState versioningState, List<String> policies, CmisAccessControlListType addACEs, CmisAccessControlListType removeACEs, Holder<CmisExtensionType> extension, Holder<String> objectId) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (sourceId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Source Id must be set.", 0, null);
        }
        if (folderId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Folder Id must be set.", 0, null);
        }
        try {
            objectId.value = fsr.createFileFromSource(context, folderId, sourceId, properties, versioningState);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * createFolder.
	 */
    public void createFolder(String repositoryId, CmisPropertiesType properties, String folderId, List<String> policies, CmisAccessControlListType addACEs, CmisAccessControlListType removeACEs, Holder<CmisExtensionType> extension, Holder<String> objectId) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (folderId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Folder Id must be set.", 0, null);
        }
        if (properties == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Properties must be set.", 0, null);
        }
        String typeId = Util.getFirstIdValue(properties, EnumPropertiesBase.CMIS_OBJECT_TYPE_ID.value());
        CmisTypeDefinitionType type = RM.getTypes().getTypeDefinition(context, typeId);
        if (type == null) {
            throw createException(EnumServiceException.OBJECT_NOT_FOUND, "Type '" + typeId + "' is unknown!", 0, null);
        }
        if (!EnumBaseObjectTypeIds.CMIS_FOLDER.value().equals(type.getBaseId().value())) {
            throw createException(EnumServiceException.CONSTRAINT, "Not a folder type.", 0, null);
        }
        try {
            objectId.value = fsr.createFolder(context, typeId, folderId, properties);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * createRelationship. Not supported.
	 */
    public void createRelationship(String repositoryId, CmisPropertiesType properties, List<String> policies, CmisAccessControlListType addACEs, CmisAccessControlListType removeACEs, Holder<CmisExtensionType> extension, Holder<String> objectId) throws CmisException {
        createContext(fContext);
        getRepository(repositoryId);
        throw createNotSupportedException();
    }

    /**
	 * createPolicy. Not supported.
	 */
    public void createPolicy(String repositoryId, CmisPropertiesType properties, String folderId, List<String> policies, CmisAccessControlListType addACEs, CmisAccessControlListType removeACEs, Holder<CmisExtensionType> extension, Holder<String> objectId) throws CmisException {
        createContext(fContext);
        getRepository(repositoryId);
        throw createNotSupportedException();
    }

    /**
	 * deleteContentStream.
	 */
    public void deleteContentStream(String repositoryId, Holder<String> documentId, Holder<String> changeToken, Holder<CmisExtensionType> extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if ((documentId == null) || (documentId.value == null)) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Document Id must be set.", 0, null);
        }
        try {
            fsr.setContentStream(context, documentId.value, true, null);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * deleteObject.
	 */
    public void deleteObject(String repositoryId, String objectId, Boolean allVersions, Holder<CmisExtensionType> extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (objectId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Object Id must be set.", 0, null);
        }
        try {
            if (fsr.delete(context, objectId, false, false).getObjectIds().size() > 0) {
                throw createException(EnumServiceException.UPDATE_CONFLICT, "Could not delete object.", 0, null);
            }
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * deleteTree.
	 */
    public de.fmui.cmis.fileshare.jaxb.DeleteTreeResponse.FailedToDelete deleteTree(String repositoryId, String folderId, Boolean allVersions, EnumUnfileObject unfileObject, Boolean continueOnFailure, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (folderId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Folder Id must be set.", 0, null);
        }
        boolean cof = (continueOnFailure == null ? false : continueOnFailure.booleanValue());
        try {
            return fsr.delete(context, folderId, true, cof);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getAllowableActions.
	 */
    public CmisAllowableActionsType getAllowableActions(String repositoryId, String objectId, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (objectId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Object Id must be set.", 0, null);
        }
        try {
            return fsr.getAllowableActions(context, objectId);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getContentStream.
	 */
    public CmisContentStreamType getContentStream(String repositoryId, String objectId, String streamId, BigInteger offset, BigInteger length, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (objectId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Docuemnt Id must be set.", 0, null);
        }
        try {
            return fsr.getContent(context, objectId);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getObject.
	 */
    public CmisObjectType getObject(String repositoryId, String objectId, String filter, Boolean includeAllowableActions, EnumIncludeRelationships includeRelationships, String renditionFilter, Boolean includePolicyIds, Boolean includeACL, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (objectId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Object Id must be set.", 0, null);
        }
        boolean iaa = (includeAllowableActions == null ? false : includeAllowableActions.booleanValue());
        boolean iacl = (includeACL == null ? false : includeACL.booleanValue());
        try {
            return fsr.getObject(context, objectId, filter, iaa, iacl);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getProperties.
	 */
    public CmisPropertiesType getProperties(String repositoryId, String objectId, String filter, CmisExtensionType extension) throws CmisException {
        CmisObjectType object = getObject(repositoryId, objectId, filter, false, EnumIncludeRelationships.NONE, null, Boolean.FALSE, Boolean.FALSE, null);
        return object.getProperties();
    }

    /**
	 * moveObject.
	 */
    public void moveObject(String repositoryId, Holder<String> objectId, String targetFolderId, String sourceFolderId, Holder<CmisExtensionType> extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if ((objectId == null) || (objectId.value == null)) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Object Id must be set.", 0, null);
        }
        if (targetFolderId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Target folder Id must be set.", 0, null);
        }
        try {
            objectId.value = fsr.move(context, objectId.value, targetFolderId);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * setContentStream.
	 */
    public void setContentStream(String repositoryId, Holder<String> documentId, Boolean overwriteFlag, Holder<String> changeToken, CmisContentStreamType contentStream, Holder<CmisExtensionType> extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if ((documentId == null) || (documentId.value == null)) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Document Id must be set.", 0, null);
        }
        boolean owf = (overwriteFlag == null ? true : overwriteFlag.booleanValue());
        try {
            fsr.setContentStream(context, documentId.value, owf, contentStream);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * updateProperties.
	 */
    public void updateProperties(String repositoryId, Holder<String> objectId, Holder<String> changeToken, CmisPropertiesType properties, Holder<CmisExtensionType> extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if ((objectId == null) || (objectId.value == null)) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Object Id must be set.", 0, null);
        }
        if (properties == null) {
            throw createException(EnumServiceException.CONSTRAINT, "Property " + EnumPropertiesBase.CMIS_NAME.value() + " must be set.", 0, null);
        }
        try {
            objectId.value = fsr.updateProperties(context, objectId.value, properties);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getObjectByPath.
	 */
    public CmisObjectType getObjectByPath(String repositoryId, String path, String filter, Boolean includeAllowableActions, EnumIncludeRelationships includeRelationships, String renditionFilter, Boolean includePolicyIds, Boolean includeACL, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        if (path == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Path must be set.", 0, null);
        }
        boolean iaa = (includeAllowableActions == null ? false : includeAllowableActions.booleanValue());
        boolean iacl = (includeACL == null ? false : includeACL.booleanValue());
        try {
            return fsr.getObjectByPath(context, path, filter, iaa, iacl);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getRenditions.
	 */
    public List<CmisRenditionType> getRenditions(String repositoryId, String objectId, String renditionFilter, BigInteger maxItems, BigInteger skipCount, CmisExtensionType extension) throws CmisException {
        createContext(fContext);
        getRepository(repositoryId);
        return Collections.emptyList();
    }
}
