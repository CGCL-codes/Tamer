package org.opencms.file.wrapper;

import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.file.CmsResource.CmsResourceCopyMode;
import org.opencms.file.CmsResource.CmsResourceDeleteMode;
import org.opencms.lock.CmsLock;
import org.opencms.main.CmsException;
import org.opencms.main.CmsIllegalArgumentException;
import java.util.List;

/**
 * Default abstract implementation of the interface {@link I_CmsResourceWrapper}.<p>
 *
 * This class returns for all methods that the action is not handled by the 
 * resource wrapper.<p>
 * 
 * Subclasses can only implement those methods where they want to change the default
 * behaviour.<p>
 *
 * @author Peter Bonrad
 * 
 * @version $Revision: 1.6 $
 * 
 * @since 6.5.6
 */
public abstract class A_CmsResourceWrapper implements I_CmsResourceWrapper {

    /** Is handled by this resource wrapper. */
    protected boolean m_isWrappedResource = false;

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#addResourcesToFolder(org.opencms.file.CmsObject, java.lang.String, org.opencms.file.CmsResourceFilter)
     */
    public List addResourcesToFolder(CmsObject cms, String resourcename, CmsResourceFilter filter) throws CmsException {
        if (m_isWrappedResource) {
            return cms.getResourcesInFolder(resourcename, filter);
        }
        return null;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#copyResource(org.opencms.file.CmsObject, java.lang.String, java.lang.String, org.opencms.file.CmsResource.CmsResourceCopyMode)
     */
    public boolean copyResource(CmsObject cms, String source, String destination, CmsResourceCopyMode siblingMode) throws CmsException, CmsIllegalArgumentException {
        if (m_isWrappedResource) {
            cms.copyResource(source, destination, siblingMode);
            return true;
        }
        return false;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#createResource(org.opencms.file.CmsObject, java.lang.String, int, byte[], java.util.List)
     */
    public CmsResource createResource(CmsObject cms, String resourcename, int type, byte[] content, List properties) throws CmsException, CmsIllegalArgumentException {
        if (m_isWrappedResource) {
            return cms.createResource(resourcename, type, content, properties);
        }
        return null;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#deleteResource(org.opencms.file.CmsObject, java.lang.String, org.opencms.file.CmsResource.CmsResourceDeleteMode)
     */
    public boolean deleteResource(CmsObject cms, String resourcename, CmsResourceDeleteMode siblingMode) throws CmsException {
        if (m_isWrappedResource) {
            cms.deleteResource(resourcename, siblingMode);
            return true;
        }
        return false;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#getLock(org.opencms.file.CmsObject, org.opencms.file.CmsResource)
     */
    public CmsLock getLock(CmsObject cms, CmsResource resource) throws CmsException {
        if (m_isWrappedResource) {
            return cms.getLock(resource);
        }
        return null;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#lockResource(CmsObject, String)
     */
    public boolean lockResource(CmsObject cms, String resourcename) throws CmsException {
        if (m_isWrappedResource) {
            cms.lockResource(resourcename);
            return true;
        }
        return false;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#moveResource(org.opencms.file.CmsObject, java.lang.String, java.lang.String)
     */
    public boolean moveResource(CmsObject cms, String source, String destination) throws CmsException, CmsIllegalArgumentException {
        if (m_isWrappedResource) {
            cms.moveResource(source, destination);
            return true;
        }
        return false;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#readFile(org.opencms.file.CmsObject, java.lang.String, org.opencms.file.CmsResourceFilter)
     */
    public CmsFile readFile(CmsObject cms, String resourcename, CmsResourceFilter filter) throws CmsException {
        if (m_isWrappedResource) {
            return cms.readFile(resourcename, filter);
        }
        return null;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#readResource(org.opencms.file.CmsObject, java.lang.String, org.opencms.file.CmsResourceFilter)
     */
    public CmsResource readResource(CmsObject cms, String resourcename, CmsResourceFilter filter) throws CmsException {
        if (m_isWrappedResource) {
            return cms.readResource(resourcename, filter);
        }
        return null;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#restoreLink(org.opencms.file.CmsObject, java.lang.String)
     */
    public String restoreLink(CmsObject cms, String uri) {
        return null;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#rewriteLink(CmsObject, CmsResource)
     */
    public String rewriteLink(CmsObject cms, CmsResource res) {
        return null;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#unlockResource(CmsObject, String)
     */
    public boolean unlockResource(CmsObject cms, String resourcename) throws CmsException {
        if (m_isWrappedResource) {
            cms.unlockResource(resourcename);
            return true;
        }
        return false;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#wrapResource(org.opencms.file.CmsObject, org.opencms.file.CmsResource)
     */
    public CmsResource wrapResource(CmsObject cms, CmsResource resource) {
        return resource;
    }

    /**
     * @see org.opencms.file.wrapper.I_CmsResourceWrapper#writeFile(org.opencms.file.CmsObject, org.opencms.file.CmsFile)
     */
    public CmsFile writeFile(CmsObject cms, CmsFile resource) throws CmsException {
        if (m_isWrappedResource) {
            return cms.writeFile(resource);
        }
        return null;
    }
}
