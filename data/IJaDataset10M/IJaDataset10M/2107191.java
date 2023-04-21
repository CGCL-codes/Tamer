package org.riverock.webmill.portal.context;

import java.util.Locale;
import org.apache.log4j.Logger;
import org.riverock.common.tools.StringTools;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id: PageRequestContextProcessor.java,v 1.4 2006/06/07 16:14:29 serg_main Exp $
 */
public final class PageRequestContextProcessor implements RequestContextProcessor {

    private static final Logger log = Logger.getLogger(PageRequestContextProcessor.class);

    public PageRequestContextProcessor() {
    }

    public RequestContext parseRequest(RequestContextParameter factoryParameter) {
        log.debug("Start process as 'page', format request: " + "/<CONTEXT>/page/<LOCALE>[/<num_of_portlet,portlet_param>]/name/<PAGE_NAME>[?<portlet_param>]...");
        String path = factoryParameter.getRequest().getPathInfo();
        if (path == null || path.equals("/")) {
            return null;
        }
        int idxSlash = path.indexOf('/', 1);
        if (idxSlash == -1) {
            return null;
        }
        Locale locale = StringTools.getLocale(path.substring(1, idxSlash));
        String pageName = path.substring(idxSlash + 1);
        Long ctxId = InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(factoryParameter.getPortalInfo().getSiteId(), locale, pageName);
        if (log.isDebugEnabled()) {
            log.debug("siteId: " + factoryParameter.getPortalInfo().getSiteId());
            log.debug("locale: " + locale.toString());
            log.debug("pageName: " + pageName);
            log.debug("ctxId: " + ctxId);
        }
        if (ctxId == null) {
            return null;
        }
        return RequestContextUtils.getRequestContextBean(factoryParameter, ctxId);
    }
}
