package ch.supsi.ist.geoshield;

import java.util.HashMap;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.ows.Dispatcher;
import org.geoserver.ows.Request;
import org.geoserver.security.CatalogMode;
import org.geoserver.security.DataAccessLimits;
import org.geoserver.security.ResourceAccessManager;
import org.geoserver.security.VectorAccessLimits;
import org.geoserver.security.WorkspaceAccessLimits;
import org.springframework.security.Authentication;
import org.opengis.filter.Filter;
import org.geotools.util.logging.Logging;
import org.springframework.security.userdetails.User;
import org.apache.commons.httpclient.NameValuePair;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;

/**
 * @author Milan Antonovic - Istituto Scienze della Terra, SUPSI
 */
public class GeoshieldResourceAccessManager implements ResourceAccessManager {

    protected static Logger LOGGER = Logging.getLogger(GeoshieldResourceAccessManager.class);

    @Override
    public WorkspaceAccessLimits getAccessLimits(Authentication a, WorkspaceInfo wi) {
        return new WorkspaceAccessLimits(CatalogMode.CHALLENGE, true, true);
    }

    @Override
    public DataAccessLimits getAccessLimits(Authentication a, LayerInfo li) {
        CatalogMode mode = CatalogMode.CHALLENGE;
        if (a.getPrincipal() instanceof User) {
            User user = (User) a.getPrincipal();
            if (user.getUsername().equalsIgnoreCase("geoshield")) {
                ResourceInfo info = li.getResource();
                Request req = Dispatcher.REQUEST.get();
                HttpServletRequest request = req.getHttpRequest();
                String geoshieldAddress = (String) request.getAttribute(GeoShieldFilter.GEOSHIELD_URL);
                String geoshieldAuthorization = (String) request.getAttribute(GeoShieldFilter.GEOSHIELD_AUTH);
                HashMap<String, DataAccessLimits> limits = (HashMap<String, DataAccessLimits>) request.getAttribute(GeoShieldFilter.GEOSHIELD_ACCESS_LIMITS);
                if (geoshieldAuthorization != null && geoshieldAddress != null) {
                    String[] geoshielduser = geoshieldAuthorization.split(":");
                    String geoserverUrl = request.getRequestURL().toString();
                    String[] pathInfo = geoserverUrl.split("/");
                    String service = pathInfo[pathInfo.length - 1];
                    if (service.equalsIgnoreCase("OWS")) {
                        service = request.getParameter("service");
                        geoserverUrl = geoserverUrl.substring(0, geoserverUrl.length() - 3) + service;
                    }
                    if (limits.containsKey(info.getPrefixedName())) {
                        System.out.println(" > Loading from cache");
                        return limits.get(info.getPrefixedName());
                    } else {
                        System.out.println(" > Loading from GeoShield");
                        NameValuePair[] nvp = { new NameValuePair("question", "GETFILTER"), new NameValuePair("user", geoshielduser[0]), new NameValuePair("password", geoshielduser[0]), new NameValuePair("layer", info.getPrefixedName()), new NameValuePair("request", req.getRequest()), new NameValuePair("service", req.getService()), new NameValuePair("url", geoserverUrl) };
                        String gsFilter = Connector.post2geoshield(geoshieldAddress, nvp);
                        Filter readFilter;
                        try {
                            if (gsFilter.equalsIgnoreCase("EXCLUDE")) {
                                readFilter = CQL.toFilter("fid=-1");
                            } else {
                                readFilter = CQL.toFilter(gsFilter);
                            }
                            DataAccessLimits dal = new VectorAccessLimits(mode, null, readFilter, null, readFilter);
                            limits.put(info.getPrefixedName(), dal);
                            return dal;
                        } catch (CQLException ex) {
                            System.err.println("CQLException Error: " + ex.toString());
                        }
                    }
                }
            } else if (user.getUsername().equalsIgnoreCase("admin")) {
                return null;
            }
        } else {
            System.out.println(" > User NOT logged in");
            System.out.println(" > " + a.getPrincipal());
        }
        return new VectorAccessLimits(mode, null, Filter.EXCLUDE, null, Filter.EXCLUDE);
    }

    @Override
    public DataAccessLimits getAccessLimits(Authentication a, ResourceInfo ri) {
        return new DataAccessLimits(CatalogMode.CHALLENGE, null);
    }
}
