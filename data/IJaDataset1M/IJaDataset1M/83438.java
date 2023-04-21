package de.fmui.cmis.fileshare.bindings.soap;

import java.math.BigInteger;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import de.fmui.cmis.fileshare.CMISFileShareException;
import de.fmui.cmis.fileshare.CallContext;
import de.fmui.cmis.fileshare.IFileShareRepository;
import de.fmui.cmis.fileshare.jaxb.CmisException;
import de.fmui.cmis.fileshare.jaxb.CmisExtensionType;
import de.fmui.cmis.fileshare.jaxb.CmisRepositoryEntryType;
import de.fmui.cmis.fileshare.jaxb.CmisRepositoryInfoType;
import de.fmui.cmis.fileshare.jaxb.CmisTypeContainer;
import de.fmui.cmis.fileshare.jaxb.CmisTypeDefinitionListType;
import de.fmui.cmis.fileshare.jaxb.CmisTypeDefinitionType;
import de.fmui.cmis.fileshare.jaxb.EnumServiceException;
import de.fmui.cmis.fileshare.jaxb.RepositoryServicePort;

/**
 * CMIS Repository Service.
 * 
 * @author Florian Müller
 */
@WebService(endpointInterface = "de.fmui.cmis.fileshare.jaxb.RepositoryServicePort")
public class RepositoryService extends AbstractService implements RepositoryServicePort {

    @Resource
    WebServiceContext fContext;

    /**
	 * getRepositories.
	 */
    public List<CmisRepositoryEntryType> getRepositories(CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        try {
            return RM.getRepositories(context);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getRepositoryInfo.
	 */
    public CmisRepositoryInfoType getRepositoryInfo(String repositoryId, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        IFileShareRepository fsr = getRepository(repositoryId);
        try {
            return fsr.getRepositoryInfo(context);
        } catch (CMISFileShareException e) {
            throw createException(e);
        }
    }

    /**
	 * getTypeChildren.
	 */
    public CmisTypeDefinitionListType getTypeChildren(String repositoryId, String typeId, Boolean includePropertyDefinitions, BigInteger maxItems, BigInteger skipCount, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        getRepository(repositoryId);
        boolean ipd = (includePropertyDefinitions == null ? false : includePropertyDefinitions.booleanValue());
        return RM.getTypes().getTypesChildren(context, typeId, maxItems, skipCount, ipd);
    }

    /**
	 * getTypeDescendants.
	 */
    public List<CmisTypeContainer> getTypeDescendants(String repositoryId, String typeId, BigInteger depth, Boolean includePropertyDefinitions, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        getRepository(repositoryId);
        int d = (depth == null ? -1 : depth.intValue());
        if (d == 0) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Depth must not be 0!", 0, null);
        }
        boolean ipd = (includePropertyDefinitions == null ? false : includePropertyDefinitions.booleanValue());
        return RM.getTypes().getTypesDescendants(context, d, typeId, ipd);
    }

    /**
	 * getTypeDefinition.
	 */
    public CmisTypeDefinitionType getTypeDefinition(String repositoryId, String typeId, CmisExtensionType extension) throws CmisException {
        CallContext context = createContext(fContext);
        getRepository(repositoryId);
        if (typeId == null) {
            throw createException(EnumServiceException.INVALID_ARGUMENT, "Type Id must be set.", 0, null);
        }
        CmisTypeDefinitionType result = RM.getTypes().getTypeDefinition(context, typeId);
        if (result == null) {
            throw createException(EnumServiceException.OBJECT_NOT_FOUND, "Type does not exist!", 0, null);
        }
        return result;
    }
}
