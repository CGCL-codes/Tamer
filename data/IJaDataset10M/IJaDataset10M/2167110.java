package org.opencms.file.types;

import org.opencms.configuration.CmsConfigurationException;
import org.opencms.main.OpenCms;

/**
 * Resource type descriptor for unknown folder types.<p>
 * 
 * This will be used for folder corpses when the resource type is not configured.<p>
 * 
 * The most common use case is when deleting a module with the resource type definition,
 * but not the content that uses that resource type definition.<p>
 * 
 * @author Michael Moossen 
 * 
 * @version $Revision: 1.4 $ 
 * 
 * @since 7.0.0 
 */
public class CmsResourceTypeUnknownFolder extends A_CmsResourceTypeFolderBase {

    /** The type id of this resource type. */
    public static final int RESOURCE_TYPE_ID = -2;

    /** The name of this resource type. */
    private static final String RESOURCE_TYPE_NAME = "unknown_folder";

    /** Indicates that the static configuration of the resource type has been frozen. */
    private static boolean m_staticFrozen;

    /** The static type id of this resource type. */
    private static int m_staticTypeId;

    /**
     * Default constructor, used to initialize member variables.<p>
     */
    public CmsResourceTypeUnknownFolder() {
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
}
