package nl.iblinden.opencms.jsf;

import org.opencms.configuration.CmsConfigurationException;
import org.opencms.file.types.A_CmsResourceType;
import org.opencms.loader.Messages;
import org.opencms.main.OpenCms;

/**
 * Resource type descriptor for the type "jsp".<p>
 * 
 * Ensures that some required file properties are attached to new JSPs.<p>
 * 
 * The value for the encoding properies of a new JSP usually is the
 * system default encoding, but this can be overwritten by 
 * a configuration parameters set in <code>opencms-vfs.xml</code>.<p>
 *
 * @author Alexander Kandzior
 * @author Martin van den Bemt
 * 
 * @version $Revision: 1.1 $ 
 * 
 * @since 6.0.0 
 */
public class CmsResourceTypeFaces extends A_CmsResourceType {

    /** The type id of this resource type. */
    private static final int RESOURCE_TYPE_ID = 90;

    /** The name of this resource type. */
    private static final String RESOURCE_TYPE_NAME = "faces";

    /** Indicates that the static configuration of the resource type has been frozen. */
    private static boolean m_staticFrozen;

    /** The static type id of this resource type. */
    private static int m_staticTypeId;

    /**
     * Default constructor, used to initialize member variables.<p>
     */
    public CmsResourceTypeFaces() {
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
        return CmsFacesLoader.RESOURCE_LOADER_ID;
    }

    /**
     * @see org.opencms.file.types.A_CmsResourceType#initConfiguration(java.lang.String, java.lang.String)
     */
    public void initConfiguration(String name, String id) throws CmsConfigurationException {
        if ((OpenCms.getRunLevel() > OpenCms.RUNLEVEL_2_INITIALIZING) && m_staticFrozen) {
            throw new CmsConfigurationException(Messages.get().container(org.opencms.file.types.Messages.ERR_CONFIG_FROZEN_3, this.getClass().getName(), getStaticTypeName(), new Integer(getStaticTypeId())));
        }
        if (!RESOURCE_TYPE_NAME.equals(name)) {
            throw new CmsConfigurationException(Messages.get().container(org.opencms.file.types.Messages.ERR_INVALID_RESTYPE_CONFIG_NAME_3, this.getClass().getName(), RESOURCE_TYPE_NAME, name));
        }
        m_staticFrozen = true;
        super.initConfiguration(RESOURCE_TYPE_NAME, id);
        m_staticTypeId = m_typeId;
    }
}
