package org.apache.pluto.driver.services.portal;

import java.util.List;
import org.apache.pluto.driver.services.portal.admin.RenderConfigAdminService;

/**
 * Service interface defining methods necessary for
 * a provider wishing to manage page administration.
 *
 * @since Aug 10, 2005
 */
public interface RenderConfigService extends DriverConfigurationService, RenderConfigAdminService {

    /**
     * Retrieve an ordered list of all PageConfig instances.
     * @return list of all PageConfig instances.
     */
    List getPages();

    /**
     * Retrieve the PageConfig for the default
     * page.
     * @return PageConfig instance of the default page.
     */
    PageConfig getDefaultPage();

    /**
     * Retrieve the PageConfig for the given
     * page id.
     *
     * @param id
     * @return PageConfig instance for the specified id.
     */
    PageConfig getPage(String pageId);
}
