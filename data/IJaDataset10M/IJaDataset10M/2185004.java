package org.opencms.file.types;

import org.opencms.configuration.CmsConfigurationException;
import org.opencms.db.CmsSecurityManager;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.i18n.CmsEncoder;
import org.opencms.i18n.CmsLocaleManager;
import org.opencms.jsp.util.CmsJspLinkMacroResolver;
import org.opencms.loader.CmsJspLoader;
import org.opencms.main.CmsException;
import org.opencms.main.OpenCms;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Resource type descriptor for the type "jsp".<p>
 * 
 * Ensures that some required file properties are attached to new JSPs.<p>
 * 
 * The value for the encoding properties of a new JSP usually is the
 * system default encoding, but this can be overwritten by 
 * a configuration parameters set in <code>opencms-vfs.xml</code>.<p>
 *
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.30 $ 
 * 
 * @since 6.0.0 
 */
public class CmsResourceTypeJsp extends A_CmsResourceTypeLinkParseable {

    /** Indicates that the static configuration of the resource type has been frozen. */
    private static boolean m_staticFrozen;

    /** The static type id of this resource type. */
    private static int m_staticTypeId;

    /** The type id of this resource type. */
    private static final int RESOURCE_TYPE_ID = 4;

    /** The name of this resource type. */
    private static final String RESOURCE_TYPE_NAME = "jsp";

    /**
     * Default constructor, used to initialize member variables.<p>
     */
    public CmsResourceTypeJsp() {
        super();
        m_typeId = RESOURCE_TYPE_ID;
        m_typeName = RESOURCE_TYPE_NAME;
    }

    /**
     * Returns the static type id of this (default) resource type.<p>
     * 
     * @return the static type id of this (default) resource type
     */
    public static int getStaticTypeId() {
        return m_staticTypeId;
    }

    /**
     * Returns the static type name of this (default) resource type.<p>
     * 
     * @return the static type name of this (default) resource type
     */
    public static String getStaticTypeName() {
        return RESOURCE_TYPE_NAME;
    }

    /**
     * @see org.opencms.file.types.I_CmsResourceType#getLoaderId()
     */
    public int getLoaderId() {
        return CmsJspLoader.RESOURCE_LOADER_ID;
    }

    /**
     * @see org.opencms.file.types.A_CmsResourceType#initConfiguration(java.lang.String, java.lang.String, String)
     */
    public void initConfiguration(String name, String id, String className) throws CmsConfigurationException {
        if ((OpenCms.getRunLevel() > OpenCms.RUNLEVEL_2_INITIALIZING) && m_staticFrozen) {
            throw new CmsConfigurationException(Messages.get().container(Messages.ERR_CONFIG_FROZEN_3, this.getClass().getName(), getStaticTypeName(), new Integer(getStaticTypeId())));
        }
        if (!RESOURCE_TYPE_NAME.equals(name)) {
            throw new CmsConfigurationException(Messages.get().container(Messages.ERR_INVALID_RESTYPE_CONFIG_NAME_3, this.getClass().getName(), RESOURCE_TYPE_NAME, name));
        }
        m_staticFrozen = true;
        super.initConfiguration(RESOURCE_TYPE_NAME, id, className);
        m_staticTypeId = m_typeId;
    }

    /**
     * @see org.opencms.relations.I_CmsLinkParseable#parseLinks(org.opencms.file.CmsObject, org.opencms.file.CmsFile)
     */
    public List parseLinks(CmsObject cms, CmsFile file) {
        CmsJspLinkMacroResolver macroResolver = new CmsJspLinkMacroResolver(cms, file.getRootPath(), false);
        String encoding = CmsLocaleManager.getResourceEncoding(cms, file);
        String content = CmsEncoder.createString(file.getContents(), encoding);
        macroResolver.resolveMacros(content);
        return macroResolver.getLinks();
    }

    /**
     * @see org.opencms.file.types.A_CmsResourceType#writeFile(org.opencms.file.CmsObject, org.opencms.db.CmsSecurityManager, org.opencms.file.CmsFile)
     */
    public CmsFile writeFile(CmsObject cms, CmsSecurityManager securityManager, CmsFile resource) throws CmsException {
        CmsJspLinkMacroResolver macroResolver = new CmsJspLinkMacroResolver(cms, resource.getRootPath(), false);
        String encoding = CmsLocaleManager.getResourceEncoding(cms, resource);
        String content = CmsEncoder.createString(resource.getContents(), encoding);
        content = macroResolver.resolveMacros(content);
        try {
            resource.setContents(content.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            resource.setContents(content.getBytes());
        }
        return super.writeFile(cms, securityManager, resource);
    }
}
