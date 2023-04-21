package org.opencms.configuration;

import org.opencms.file.types.CmsResourceTypeUnknown;
import org.opencms.file.types.I_CmsResourceType;
import org.opencms.main.CmsLog;
import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.ObjectCreationFactory;
import org.apache.commons.logging.Log;
import org.xml.sax.Attributes;

/**
 * Factory to create resource type instances from the XML configuration.<p>
 * 
 * This is required because the default digester implementation will cause an exception in case 
 * a resource type class is missing. However, a missing class is common if a module with a new resource type
 * class is imported. In this case, the resource type class is changes to <code>{@link org.opencms.file.types.CmsResourceTypeUnknown}</code>,
 * so that the import of the resources can proceed.<p>
 * 
 * @author Alexander Kandzior 
 * 
 * @version $Revision: 1.4 $
 * 
 * @since 6.0.2
 */
public class CmsDigesterResourceTypeCreationFactory extends AbstractObjectCreationFactory implements ObjectCreationFactory {

    /** The log object of this class. */
    private static final Log LOG = CmsLog.getLog(CmsDigesterResourceTypeCreationFactory.class);

    /**
     * Default constructor for the resource type configuration factory.<p> 
     */
    public CmsDigesterResourceTypeCreationFactory() {
        super();
    }

    /**
     * @see org.apache.commons.digester.ObjectCreationFactory#createObject(org.xml.sax.Attributes)
     */
    public Object createObject(Attributes attributes) throws Exception {
        String className = attributes.getValue(I_CmsXmlConfiguration.A_CLASS);
        I_CmsResourceType type;
        try {
            if (className != null) {
                className = className.trim();
            }
            type = (I_CmsResourceType) Class.forName(className).newInstance();
        } catch (Exception e) {
            type = new CmsResourceTypeUnknown();
            LOG.error(Messages.get().getBundle().key(Messages.ERR_UNKNOWN_RESTYPE_CLASS_2, className, type.getClass().getName()), e);
        }
        return type;
    }
}
