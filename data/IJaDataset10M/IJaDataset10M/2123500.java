package org.atlantal.api.portal.app.web;

import java.util.Map;
import org.atlantal.api.app.service.Service;
import org.atlantal.api.portal.web.Site;
import org.atlantal.api.portal.web.SiteException;
import org.atlantal.utils.wrapper.ObjectWrapper;

/**
 * @author Fran�ois
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface SiteManager extends Service {

    /**
     * @param id id
     * @return node
     * @throws SiteException TODO
     */
    ObjectWrapper getSiteWrapper(Object id) throws SiteException;

    /**
     * @param id id
     * @return node
     * @throws SiteException TODO
     */
    Site getSite(Object id) throws SiteException;

    /**
     * @return map wrapper
     * @throws SiteException TODO
     */
    ObjectWrapper getVirtualHostsWrapper() throws SiteException;

    /**
     * @return map
     * @throws SiteException TODO
     */
    Map getVirtualHosts() throws SiteException;
}
