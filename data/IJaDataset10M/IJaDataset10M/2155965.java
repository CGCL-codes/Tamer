package sls.crystalstore.catalog;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import sls.crystalstore.common.SLSCSConfig;
import net.myvietnam.mvncore.mvnframework.URLMap;
import net.myvietnam.mvncore.util.I18nUtil;
import sls.crystalstore.common.SLSCSResourceBundle;
import sls.crystalstore.util.exception.MissingURLMapEntryException;

class CatalogModuleURLMapHandler {

    CatalogModuleURLMapHandler() {
    }

    /**
     * We must pass the requestURI to this method, instead of from request,
     * because requestURI may be changed from Processor before call this method
     * NOTE: Currently we dont use the param request
     */
    public URLMap getMap(String requestURI, HttpServletRequest request) throws MissingURLMapEntryException {
        URLMap map = new URLMap();
        if (requestURI.equals("/error")) {
            map.setResponse("/slscatalog/error.jsp");
        } else if (requestURI.equals("/index")) {
            map.setResponse("/slscatalog/index.jsp");
        } else if (requestURI.equals("") || requestURI.equals("/")) {
            map.setResponse(SLSCSConfig.getCatalogUrlPattern() + "/index");
        } else if (requestURI.equals("/showcategory")) {
            map.setResponse("/slscatalog/showcategory.jsp");
        } else if (requestURI.equals("/showproduct")) {
            map.setResponse("/slscatalog/showproduct.jsp");
        } else if (requestURI.equals("/showspecials")) {
            map.setResponse("/slscatalog/showspecials.jsp");
        } else if (requestURI.equals("/shownewproducts")) {
            map.setResponse("/slscatalog/listproducts.jsp");
        } else if (requestURI.equals("/quicksearch")) {
            map.setResponse("/slscatalog/listproducts.jsp");
        }
        Locale locale = I18nUtil.getLocaleInRequest(request);
        if (map.getResponse() == null) {
            String localizedMessage = SLSCSResourceBundle.getString(locale, "crystalstore.exception.MissingURLMapEntryException.cannot_find_matching_entry", new Object[] { requestURI });
            throw new MissingURLMapEntryException("Cannot find mathcing entry for request " + requestURI);
        }
        return map;
    }
}
