package org.opencms.loader;

import org.opencms.file.CmsObject;
import org.opencms.file.CmsPropertyDefinition;
import org.opencms.file.CmsResource;
import org.opencms.main.CmsException;
import org.opencms.xml.I_CmsXmlDocument;
import org.opencms.xml.page.CmsXmlPageFactory;
import javax.servlet.ServletRequest;

/**
 * OpenCms loader for resources of type <code>{@link org.opencms.file.types.CmsResourceTypeXmlPage}</code>.<p>
 *
 * @author Alexander Kandzior 
 * @author Carsten Weinholz 
 * 
 * @version $Revision: 1.54 $ 
 * 
 * @since 6.0.0 
 */
public class CmsXmlPageLoader extends A_CmsXmlDocumentLoader {

    /** The id of this loader. */
    public static final int RESOURCE_LOADER_ID = 9;

    /**
     * @see org.opencms.loader.I_CmsResourceLoader#getLoaderId()
     */
    public int getLoaderId() {
        return RESOURCE_LOADER_ID;
    }

    /**
     * Returns a String describing this resource loader, which is (localized to the system default locale)
     * <code>"The OpenCms default resource loader for xml pages"</code>.<p>
     * 
     * @return a describing String for the ResourceLoader 
     */
    public String getResourceLoaderInfo() {
        return Messages.get().getBundle().key(Messages.GUI_LOADER_XMLPAGE_DEFAULT_DESC_0);
    }

    /**
     * @see org.opencms.loader.A_CmsXmlDocumentLoader#unmarshalXmlDocument(org.opencms.file.CmsObject, org.opencms.file.CmsResource, javax.servlet.ServletRequest)
     */
    protected I_CmsXmlDocument unmarshalXmlDocument(CmsObject cms, CmsResource resource, ServletRequest req) throws CmsException {
        return CmsXmlPageFactory.unmarshal(cms, resource, req);
    }

    /**
     * @see org.opencms.loader.A_CmsXmlDocumentLoader#getTemplatePropertyDefinition()
     */
    protected String getTemplatePropertyDefinition() {
        return CmsPropertyDefinition.PROPERTY_TEMPLATE;
    }
}
